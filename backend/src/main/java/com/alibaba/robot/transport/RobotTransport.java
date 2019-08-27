package com.alibaba.robot.transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import com.alibaba.robot.actionlib.Action;
import com.alibaba.robot.common.BuildVariants;
import com.alibaba.robot.common.ITimerListener;
import com.alibaba.robot.task.ITask;
import com.alibaba.robot.task.ITaskStateListener;
import com.alibaba.robot.task.RosContext;
import com.alibaba.robot.task.TaskState;
import com.alibaba.robot.transport.events.AdvertiseServiceEvent;
import com.alibaba.robot.transport.events.CancelTaskEvent;
import com.alibaba.robot.transport.events.DisconnectedEvent;
import com.alibaba.robot.transport.events.ConnectedEvent;
import com.alibaba.robot.transport.events.RunActionEvent;
import com.alibaba.robot.transport.events.RunTaskEvent;
import com.alibaba.robot.transport.events.IncommingMessageEvent;
import com.alibaba.robot.transport.events.PublishEvent;
import com.alibaba.robot.transport.events.SimpleEvent;
import com.alibaba.robot.transport.events.StreamClosedEvent;
import com.alibaba.robot.transport.events.TaskCompletedEvent;
import com.alibaba.robot.transport.events.TimeoutEvent;
import com.alibaba.robot.web.manage.pojo.RobotDetailedStatus;
import com.google.gson.Gson;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Service;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.CallServiceCallback;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.handler.RosHandler;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;
import edu.wpi.rail.jrosbridge.services.ServiceResponse;

/****
 * A transport channel between robot and server<br>
 * TODO: split the class into transportation and logic layer<br>
 * e.g. when transport goes down temporarily, action would still running (as we
 * can subscribe to status to get the status eventually)<br>
 * also consider about robot as client.<br>
 * TODO: need to add task timeout
 **/
public class RobotTransport implements RosHandler, Runnable, ITransport, ITimerListener, ITaskStateListener {

	private static final ConnectedEvent CONNECTED_EVENT = new ConnectedEvent();
	private static final DisconnectedEvent DISCONNECTED_EVENT = new DisconnectedEvent();
	private final Logger LOGGER;
	private String sessionId;
	private static final int HEARTBEAT_TIMEOUT = 15000;
	private static final int CONNECT_INTERVAL = 1500;
	private Gson gson = new Gson();
	private Object pendingHeartbeatLock = new Object();
	private boolean hasPendingHeartbeat = false;
	private Thread thread;
	private CopyOnWriteArraySet<ITransportListener> listenerSet = new CopyOnWriteArraySet<ITransportListener>();
	private HashMap<String, String> topicMessageTypeMap = new HashMap<String, String>();

	private State currentState;
	private Ros ros;

	private int id;
	private Address address;
	private int robotType;
	private String model;

	private Map<StateName, State> statesMap = new HashMap<StateName, State>();

	private Map<String, ITask> runningTasks = new HashMap<String, ITask>();
	private Map<ITask, RosContext> runningTaskContext = new HashMap<ITask, RosContext>();
	private BlockingQueue<Event> blockingQueue = new LinkedBlockingQueue<Event>();
	private Set<String> subscribedTopics = new HashSet<String>();

	public RobotTransport(int id, Address address, int robotType, String model, ITransportListener listener) {
		if (address == null || listener == null) {
			throw new IllegalArgumentException();
		}

		LOGGER = Logger.getLogger("Transport " + address.toString());
		this.id = id;
		this.listenerSet.add(listener);
		this.address = address;
		this.robotType = robotType;
		this.model = model;

		statesMap.put(StateName.Connecting, new ConnectingState());
		statesMap.put(StateName.Disconnected, new DisconnectedState());
		statesMap.put(StateName.Established, new EstablishedState());
		statesMap.put(StateName.ExecutingTask, new ExecutingActionState());
		statesMap.put(StateName.Charging, new ChargingState());
	}

	private void sleep(long timeInMillisecond) {
		try {
			Thread.sleep(timeInMillisecond);
		} catch (InterruptedException e) {
		}
	}

	private class ConnectingState extends State {

		public ConnectingState() {
			super(StateName.Connecting);
		}

		@Override
		public void onEnter() {
			if (connect()) {
				if (runningTasks.size() == 0) {
					postEvent(CONNECTED_EVENT);
				} else {
					postEvent(EventType.RunningAction);
				}
			} else {
				postEvent(DISCONNECTED_EVENT);
			}
		}

		@Override
		public void handleEvent(Event event) {
			if (event.getEventType() == EventType.Connected) {
				setState(StateName.Established);
			} else if (event.getEventType() == EventType.Disconnected) {
				setState(StateName.Disconnected);
			} else if (event.getEventType() == EventType.RunningAction) {
				setupHeartbeat();
				subscribeDefaultTopics();
				for (Map.Entry<ITask, RosContext> entry : runningTaskContext.entrySet()) {
					entry.getValue().setRos(ros);
				}
				setState(StateName.ExecutingTask);
			} else {
				super.handleEvent(event);
			}
		}
	}

	private void releaseExecutingAction() {

	}

	private class BaseState extends State {
		public BaseState(StateName stateName) {
			super(stateName);
		}

		@Override
		public void handleEvent(Event event) {
			if (event.getEventType() == EventType.CancelTask) {
				CancelTaskEvent cancelTaskEvent = (CancelTaskEvent) event;
				if (runningTasks.containsKey(cancelTaskEvent.getTaskId())) {
					LOGGER.info("cancel task: " + cancelTaskEvent.getTaskId());
					runningTasks.get(cancelTaskEvent.getTaskId()).cancel();
				} else {
					LOGGER.warn("task is not found, ignore cancel request " + cancelTaskEvent.getTaskId());
				}
			} else if (event.getEventType() == EventType.TaskCompleted) {
				TaskCompletedEvent taskCompletedEvent = (TaskCompletedEvent) event;
				runningTasks.remove(taskCompletedEvent.getTask().getId());
				runningTaskContext.remove(taskCompletedEvent.getTask());
			} else if (event.getEventType().equals(EventType.Charging)) {
				setState(StateName.Charging);
			} else {
				LOGGER.warn("unhandled event: " + event.getEventType() + ", current state: " + stateName);
			}
		}
	}

	private void close() {
		if (ros != null) {
			ros.removeRosHandler(RobotTransport.this);
			ros.disconnect();
			ros = null;
		}
	}

	private class DisconnectedState extends BaseState {
		public DisconnectedState() {
			super(StateName.Disconnected);
		}

		@Override
		public void onEnter() {

			close();

			resetPingTimer();
			subscribedTopics.clear();
			postEvent(EventType.AttemptToConnect);
			sleep(CONNECT_INTERVAL);
		}

		@Override
		public void handleEvent(Event event) {
			if (event.getEventType() == EventType.AttemptToConnect) {
				setState(StateName.Connecting);
			} else {
				super.handleEvent(event);
			}
		}
	}

	private class ChargingState extends BaseState {
		public ChargingState() {
			super(StateName.Charging);
		}

		@Override
		public void handleEvent(Event event) {
			if (event.getEventType().equals(EventType.ChargeInterrupted)) {
				setState(StateName.Established);
			} else if (event.getEventType() == EventType.RunTask) {
				RunTaskEvent runTaskEvent = (RunTaskEvent) event;
				ITask task = runTaskEvent.getTask();
				if (!doRunTask(runTaskEvent.getTask())) {
					LOGGER.error("run task failed: " + task.getName());
				}
			} else if (event.getEventType() == EventType.Disconnected) {
				setState(StateName.Disconnected);
			} else if (event.getEventType() == EventType.StreamClosed) {
				StreamClosedEvent streamClosedEvent = (StreamClosedEvent) event;
				if (sessionId.equals(streamClosedEvent.getSessionId())) {
					setState(StateName.Disconnected);
				}
			} else {
				super.handleEvent(event);
			}
		}
	}

	private class EstablishedState extends BaseState {
		public EstablishedState() {
			super(StateName.Established);
		}

		@Override
		public void onEnter() {
			setupHeartbeat();
			subscribeDefaultTopics();
		}

		@Override
		public void handleEvent(Event event) {
			if (event.getEventType() == EventType.Publish) {
				PublishEvent publishEvent = (PublishEvent) event;
				doPublish(publishEvent.getTopic(), publishEvent.getMessage(), publishEvent.getMessageType());
			} else if (event.getEventType() == EventType.RunAction) {
				RunActionEvent runActionEvent = (RunActionEvent) event;
				Action action = runActionEvent.geAction();
				if (doRunTask(action)) {
					setState(StateName.ExecutingTask);
				} else {
					LOGGER.error("run action failed: " + action.getName());
				}
			} else if (event.getEventType() == EventType.Disconnected) {
				setState(StateName.Disconnected);
			} else if (event.getEventType() == EventType.StreamClosed) {
				StreamClosedEvent streamClosedEvent = (StreamClosedEvent) event;
				if (sessionId.equals(streamClosedEvent.getSessionId())) {
					setState(StateName.Disconnected);
				}
			} else if (event.getEventType() == EventType.IncomdingMessage) {
				IncommingMessageEvent incommingMessageEvent = (IncommingMessageEvent) event;
				if (!incommingMessageEvent.getTopic().endsWith("result")) {
					return;
				}
			} else if (event.getEventType() == EventType.AdvertiseService) {
				AdvertiseServiceEvent advertiseServiceEvent = (AdvertiseServiceEvent) event;
				doAdvertiseService(advertiseServiceEvent.getServiceName(), advertiseServiceEvent.getServiceType(),
						advertiseServiceEvent.getCallback());
			} else if (event.getEventType() == EventType.RunTask) {
				RunTaskEvent runTaskEvent = (RunTaskEvent) event;
				ITask task = runTaskEvent.getTask();
				if (doRunTask(runTaskEvent.getTask())) {
					setState(StateName.ExecutingTask);
				} else {
					LOGGER.error("run task failed: " + task.getName());
				}
			} else {
				super.handleEvent(event);
			}
		}
	}

	private class ExecutingActionState extends BaseState {
		public ExecutingActionState() {
			super(StateName.ExecutingTask);
		}

		@Override
		public void handleEvent(Event event) {

			if (event.getEventType() == EventType.ActionComplete) {
				sleep(1000);
				releaseExecutingAction();
				setState(StateName.Established);
			} else if (event.getEventType() == EventType.Disconnected) {
				// executingAction.resetListener();
				setState(StateName.Disconnected);
			} else if (event.getEventType() == EventType.StreamClosed) {
				StreamClosedEvent streamClosedEvent = (StreamClosedEvent) event;
				if (sessionId.equals(streamClosedEvent.getSessionId())) {
					setState(StateName.Disconnected);
				}
			} else if (event.getEventType() == EventType.Publish) {
				PublishEvent publishEvent = (PublishEvent) event;
				doPublish(publishEvent.getTopic(), publishEvent.getMessage(), publishEvent.getMessageType());
			} else if (event.getEventType() == EventType.AdvertiseService) {
				AdvertiseServiceEvent advertiseServiceEvent = (AdvertiseServiceEvent) event;
				doAdvertiseService(advertiseServiceEvent.getServiceName(), advertiseServiceEvent.getServiceType(),
						advertiseServiceEvent.getCallback());
			} else if (event.getEventType() == EventType.TaskCompleted) {
				TaskCompletedEvent taskCompletedEvent = (TaskCompletedEvent) event;
				runningTasks.remove(taskCompletedEvent.getTask().getId());
				runningTaskContext.remove(taskCompletedEvent.getTask());
				if (runningTasks.size() == 0) {
					setState(StateName.Established);
				}
			} else {
				super.handleEvent(event);
			}
		}
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void stop() {
		postEvent(new SimpleEvent(EventType.Shutdown));
	}

	public void run() {
		looper();
	}

	private void postEvent(Event event) {
		blockingQueue.offer(event);
	}

	private void postEvent(EventType eventType) {
		blockingQueue.offer(new SimpleEvent(eventType));
	}

	private void looper() {

		LOGGER.debug("running... ");

		Event event = null;
		setState(StateName.Disconnected);
		try {
			while (true) {
				event = blockingQueue.poll(HEARTBEAT_TIMEOUT, TimeUnit.MILLISECONDS);
				if (event == null) {
					synchronized (pendingHeartbeatLock) {
						if (hasPendingHeartbeat) {
							LOGGER.debug("timeout: post disconnected event");
							postEvent(DISCONNECTED_EVENT);
							continue;
						} else {
							ping();
						}
					}
				} else {
					if (event.getEventType() == EventType.Shutdown) {
						LOGGER.debug("shutdown event received.");
						break;
					} else if (event.getEventType() == EventType.IncomdingMessage) {
						resetPingTimer();
					}

					currentState.handleEvent(event);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		LOGGER.debug("closing connection.");
		close();
		LOGGER.debug("shutdown...");
	}

	private void setState(StateName stateName) {
		if (currentState != null && currentState.getStateName().equals(stateName)) {
			return;
		}

		State oldState = currentState;
		if (oldState != null) {
			oldState.onExit();
		}

		currentState = statesMap.get(stateName);
		currentState.onEnter();

		if (oldState == null) {
			LOGGER.debug("state transition: " + currentState.getStateName());
		} else {
			LOGGER.debug("state transition: " + oldState.getStateName() + " ==> " + currentState.getStateName());
		}

		for (ITransportListener listener : listenerSet) {
			listener.onStateChanged(this, oldState == null ? StateName.Disconnected : oldState.getStateName(),
					currentState.getStateName());
		}

		LOGGER.info("STATE CALLBACK FINISHED");
	}

	static class TopicTypeRequestParam {
		private String topic;

		public String getTopic() {
			return topic;
		}

		public TopicTypeRequestParam(String topic) {
			this.topic = topic;
		}
	}

	private boolean connect() {
		ros = new Ros(address.getHost(), address.getPort());
		ros.addRosHandler(this);
		boolean result = ros.connect();
		LOGGER.debug("connect result: " + result);
		return result;
	}

	public boolean publish(String topic, String message, String messageType) {
		if (topic == null || topic.length() == 0 || message == null || message.length() == 0) {
			return false;
		}
		if (messageType == null || messageType.length() == 0) {
			return false;
		}

		PublishEvent publishEvent = new PublishEvent(topic, message, messageType);
		postEvent(publishEvent);
		return true;
	}

	private boolean runAction(Action action) {
		if (action == null) {
			LOGGER.error("action is null!");
			return false;
		}

		RunActionEvent runActionEvent = new RunActionEvent(action);
		postEvent(runActionEvent);
		return true;
	}

	private boolean doPublish(String topic, String message, String messageType) {
		LOGGER.debug("doPublish: " + message);
		JsonObject value = Json.createObjectBuilder().add("data", message).build();
		Message toSend = new Message(value.toString());
		Topic echo = new Topic(ros, topic, messageType);
		echo.publish(toSend);
		return true;
	}

	private void setupHeartbeat() {
		Topic heartbeatTopic = new Topic(ros, HEARTBEAT_TOPIC, "std_msgs/String");
		heartbeatTopic.advertise();
		resetPingTimer();
	}

	private void ping() {
		Topic heartbeatTopic = new Topic(ros, HEARTBEAT_TOPIC, "std_msgs/String");
		heartbeatTopic.advertise();
		Message message = new Message();
		hasPendingHeartbeat = true;
		heartbeatTopic.publish(message);
	}

	void resetPingTimer() {
		synchronized (pendingHeartbeatLock) {
			hasPendingHeartbeat = false;
		}
	}

	private void subscribeDefaultTopics() {

		List<String> topics = new ArrayList<String>();
		topics.add(ROBOT_STATUS_TOPIC_NAME);
		topics.add(ROBOT_TTS_TOPIC_NAME);

		if (BuildVariants.YUNQI_ENABLED) {
			topics.add(ROBOT_SCREEN_TOPIC_NAME);
		}

		updateTopicTypes(topics);

		// do not call listener for heart beat topic
		Topic heartbeatTopic = new Topic(ros, HEARTBEAT_TOPIC, "std_msgs/String");
		if (!subscribedTopics.contains(HEARTBEAT_TOPIC)) {
			LOGGER.info("topic subscribed: " + HEARTBEAT_TOPIC);
			heartbeatTopic.subscribe(new TopicCallback() {
				public void handleMessage(Message message) {
					RobotTransport.this.resetPingTimer();
				}
			});
			subscribedTopics.add(HEARTBEAT_TOPIC);
		}

		for (String topic : topics) {
			if (!topicMessageTypeMap.containsKey(topic)) {
				continue;
			}

			Topic rosTopic = new Topic(ros, topic, this.topicMessageTypeMap.get(topic));
			if (subscribedTopics.contains(topic) || rosTopic.isSubscribed()) {
				continue;
			}

			rosTopic.subscribe(new TopicCallback() {
				public void handleMessage(Message message) {
					RobotTransport.this.resetPingTimer();
					for (ITransportListener listener : listenerSet) {
						listener.onMessage(RobotTransport.this, topic, message.toString());
					}

					if (ROBOT_STATUS_TOPIC_NAME.equals(topic)) {
						RobotDetailedStatus detailedStatus = new RobotDetailedStatus();
						detailedStatus = gson.fromJson(message.toString(), RobotDetailedStatus.class);
						if (RobotTransport.this.currentState.stateName.equals(StateName.Charging)) {
							if (detailedStatus
									.getRobot_battery_running_status() != RobotDetailedStatus.ROBOT_STATUS_CHARGING) {
								RobotTransport.this.postEvent(new SimpleEvent(EventType.ChargeInterrupted));
							}
						} else {
							if (detailedStatus
									.getRobot_battery_running_status() == RobotDetailedStatus.ROBOT_STATUS_CHARGING) {
								RobotTransport.this.postEvent(new SimpleEvent(EventType.Charging));
							}
						}
					}
				}
			});

			subscribedTopics.add(topic);
			LOGGER.info("topic subscribed 2: " + topic);
		}
	}

	private String getTopicType(String topicName) {
		Gson gson = new Gson();
		TopicTypeRequestParam param = new TopicTypeRequestParam(topicName);
		Service messageDetailService = new Service(ros, "/rosapi/topic_type", "rosapi/TopicTypeResponse");
		ServiceRequest messageDetailRequest = new ServiceRequest(gson.toJson(param));
		ServiceResponse messageDetailResponse = messageDetailService.callServiceAndWait(messageDetailRequest);
		if (messageDetailResponse == null) {
			LOGGER.error("rosapi/topic_type failed");
			return null;
		}

		JsonObject jsonObject = messageDetailResponse.toJsonObject();
		return jsonObject.getString("type");
	}

	private boolean updateTopicTypes(List<String> topicList) {
		int topicTypeUpdated = 0;
		for (String topic : topicList) {
			if (!topicMessageTypeMap.containsKey(topic)) {
				String topicType = getTopicType(topic);
				if (topicType != null && topicType.length() > 0) {
					topicMessageTypeMap.put(topic, topicType);
					topicTypeUpdated++;
				} else {
					LOGGER.error("topic type is not found: " + topic);
				}
			}
		}

		if (topicTypeUpdated > 0) {
			LOGGER.info("topic type updated: " + topicTypeUpdated);
		}

		return true;
	}

	private boolean doRunAction(Action<? extends Object, ? extends Object, ? extends Object> action) {
		return doRunTask(action);
	}

	private boolean doRunTask(ITask task) {
		runningTasks.put(task.getId(), task);
		RosContext rosContext = new RosContext(ros, topicMessageTypeMap);
		runningTaskContext.put(task, rosContext);
		task.addListener(this);
		
		LOGGER.info("Running task: " + task);
		int ret = task.run(this, rosContext);
		LOGGER.info("Run task result: " + ret);
		return ret == 0;
	}

	public void handleConnection(Session session) {
		LOGGER.debug("handleConnection: " + session.getId());
		sessionId = session.getId();
	}

	public void handleDisconnection(Session session) {
		if (session.getId() != null && session.getId().equals(sessionId)) {
			LOGGER.debug("handleDisconnection: " + session.getId());
			postEvent(new StreamClosedEvent(session.getId()));
		}
	}

	public void handleError(Session session, Throwable t) {
		t.printStackTrace();
		LOGGER.error("handleError: " + t);
	}

	@Override
	public StateName getState() {
		if (currentState != null) {
			return currentState.getStateName();
		}
		return StateName.Disconnected;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public int getRobotType() {
		return this.robotType;
	}

	@Override
	public String getModel() {
		return this.model;
	}

	@Override
	public Address getAddress() {
		return this.address;
	}

	@Override
	public void onTimeout(long id) {
		postEvent(new TimeoutEvent(id));
	}

	@Override
	public boolean advertiseService(String serviceName, String serviceType, final IServiceCallback serviceCallback) {
		AdvertiseServiceEvent advertiseServiceEvent = new AdvertiseServiceEvent(serviceName, serviceType,
				serviceCallback);
		postEvent(advertiseServiceEvent);
		return false;
	}

	boolean doAdvertiseService(String serviceName, String serviceType, final IServiceCallback serviceCallback) {
		Service service = new Service(ros, serviceName, serviceType);
		service.advertiseService(new CallServiceCallback() {
			@Override
			public void handleServiceCall(ServiceRequest request) {
				serviceCallback.onRequest(service, request);
			}
		});
		return true;
	}

	@Override
	public boolean cancelCurrentTask() {
		postEvent(new SimpleEvent(EventType.ForceStopCurrentTask));
		return true;
	}

	@Override
	public boolean cancelTask(String taskId) {
		postEvent(new CancelTaskEvent(taskId));
		return true;
	}

	@Override
	public void addListener(ITransportListener listener) {
		listenerSet.add(listener);
	}

	@Override
	public void removeListener(ITransportListener listener) {
		listenerSet.remove(listener);
	}

	@Override
	public boolean runTask(ITask task) {
		if (task == null) {
			LOGGER.error("task is null!");
			return false;
		}

		RunTaskEvent runTaskEvent = new RunTaskEvent(task);
		postEvent(runTaskEvent);
		return true;
	}

	@Override
	public void onTaskStarted(ITask task, ITransport transport, TaskState taskState) {

	}

	@Override
	public void onTaskFeedback(ITask task, ITransport transport, TaskState taskState, Object feedback) {

	}

	@Override
	public void onTaskCompleted(ITask task, ITransport transport, TaskState taskState, Object result) {
		task.removeListener(this);
		postEvent(new TaskCompletedEvent(task));
	}
}