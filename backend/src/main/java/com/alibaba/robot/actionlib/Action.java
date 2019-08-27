package com.alibaba.robot.actionlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

import com.alibaba.robot.common.ITimerListener;
import com.alibaba.robot.common.TimerManager;
import com.alibaba.robot.common.TimerManager.TimerType;
import com.alibaba.robot.task.ITask;
import com.alibaba.robot.task.ITaskStateListener;
import com.alibaba.robot.task.RosContext;
import com.alibaba.robot.task.TaskManager;
import com.alibaba.robot.transport.ITransport;
import com.alibaba.robot.transport.ITransportListener;
import com.alibaba.robot.transport.StateName;
import com.google.gson.Gson;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;

/**
 * Represent for a ROS action_lib task<br>
 * G: type of goal<br>
 * F: type of feedback<br>
 * R: type of result
 */
public class Action<G, F, R> implements ITask, ITransportListener, ITimerListener {

	public enum ExecuteResult {
		MessageTypeNotFound, BadArgs, Success,
	}

	private static final Logger LOGGER = Logger.getLogger(Action.class);

	public Action(String name, Goal<G> goal) {
		this(name, goal, Integer.MAX_VALUE);
	}

	public Action(String name, Goal<G> goal, int timeout) {
		if (name == null || name.length() == 0 || goal == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.goal = goal;
		this.timeout = timeout;

		goalTopicName = String.format("/%s/goal", name);
		feedbackTopicName = String.format("/%s/feedback", name);
		resultTopicName = String.format("/%s/result", name);
		statusTopicName = String.format("/%s/status", name);
		cancellTopicName = String.format("/%s/cancel", name);

		keyTopicNames.add(goalTopicName);
		keyTopicNames.add(feedbackTopicName);
		keyTopicNames.add(resultTopicName);
		keyTopicNames.add(statusTopicName);
		keyTopicNames.add(cancellTopicName);

		taskState = new ActionTaskState(name, getId());

		taskState.setExtra(extra);

		// TODO this is not good
		listenerSet.add(TaskManager.getInstance());

	}

	public List<String> getKeyTopicNames() {
		return keyTopicNames;
	}

	public ExecuteResult subscribeKeyTopics(Ros ros) {
		if (ros == null) {
			return ExecuteResult.BadArgs;
		}

		for (String topicName : keyTopicNames) {
			String topicType = ros.getTopicType(topicName);
			topicMessageTypeMap.put(topicName, topicType);
		}

		if (!topicMessageTypeMap.containsKey(feedbackTopicName) 
			|| !topicMessageTypeMap.containsKey(goalTopicName)
			|| !topicMessageTypeMap.containsKey(resultTopicName)
			|| !topicMessageTypeMap.containsKey(cancellTopicName)
			|| !topicMessageTypeMap.containsKey(resultTopicName)) {
			return ExecuteResult.MessageTypeNotFound;
		}

		subscribe(feedbackTopicName, topicMessageTypeMap.get(feedbackTopicName));
		subscribe(resultTopicName, topicMessageTypeMap.get(resultTopicName));
		subscribe(statusTopicName, "actionlib_msgs/GoalStatusArray");

		return ExecuteResult.Success;
	}

	public ExecuteResult execute(Ros ros, HashMap<String, String> topicMessageTypeMap) {
		ExecuteResult result = subscribeKeyTopics(ros);
		if (result != ExecuteResult.Success) {
			return result;
		}

		Gson gson = new Gson();
		String body = gson.toJson(goal);

		// TODO: this is not good
		if (goal.getGoal().getClass().getName().equals(String.class.getName())) {
			int index = body.indexOf("\"goal\"");
			String tmp = body.substring(0, index);
			body = tmp + "\"goal\":" + (String) goal.getGoal() + "}";
		}

		LOGGER.info("ACTION GOAL: " + body);

		Message message = new Message(body);
		Topic goalTopic = new Topic(ros, goalTopicName, topicMessageTypeMap.get(goalTopicName));
		goalTopic.advertise();
		goalTopic.publish(message);

		return ExecuteResult.Success;
	}

	private void handleMessage(final String topicName, final String message) {
		synchronized (taskStateLock) {
			if (completed) {
				return;
			}

			if (feedbackTopicName.equals(topicName)) {

				if (DEBUG) {
					LOGGER.info("FEEDBACK: " + message);
				}
				Feedback<F> feedback = new Feedback<F>();
				feedback = gson.fromJson(message, feedback.getClass());

				if (feedback.getStatus().getGoal_id().getId().equals(getId())) {

					Map<String, Object> map = (Map<String, Object>) feedback.getFeedback();
					if (map != null && map.containsKey(FEEDBACK_STATUS_KEY)) {
						int status = (int) ((Double) map.get(FEEDBACK_STATUS_KEY)).doubleValue();
						taskState.setStatus(status);
					}

					taskState.setTaskStatus(RUNNING);

					for (ITaskStateListener listener : listenerSet) {
						listener.onTaskFeedback(this, transport, taskState, message);
					}
				}

			} else if (resultTopicName.equals(topicName)) {

				Result<R> result = new Result<R>();
				result = (Result<R>) gson.fromJson(message, result.getClass());

				// TODO: parse the final result
				if (DEBUG) {
					LOGGER.info("RESULT: " + message);
				}
				if (result.getStatus().getGoal_id().getId().equals(getId())) {
					taskState.setTaskStatus(SUCCEEDED);
					completeTask();
				}

			} else if (statusTopicName.equals(topicName)) {

				StatusArray statusArray = new StatusArray();
				statusArray = gson.fromJson(message, statusArray.getClass());

				for (Status statusItem : statusArray.getStatus_list()) {
					if (!statusItem.getGoal_id().getId().equals(getId())) {
						continue;
					}

					int localStatus = statusItem.getStatus();
					if (localStatus == Status.ABORTED || localStatus == Status.PREEMPTED
							|| localStatus == Status.REJECTED || localStatus == Status.RECALLED) {
						taskState.setTaskStatus(CANCELLED);
					} else if (localStatus == Status.SUCCEEDED) {
						taskState.setTaskStatus(SUCCEEDED);
					}

					if (taskState.isCompleted()) {
						completeTask();
						break;
					}
				}
			}
		}
	}

	private void subscribe(final String topicName, String dataType) {
		Topic topic = new Topic(context.getRos(), topicName, dataType);
		if (topic.isSubscribed()) {
			return;
		}

		topic.subscribe(new TopicCallback() {
			public void handleMessage(Message message) {
				Action.this.handleMessage(topicName, message.toString());
			}
		});

		synchronized (subscribedTopicLock) {
			subscribedTopics.add(topic);
		}

		LOGGER.info("topic subscribed: " + topicName);
	}

	public Goal<G> getGoal() {
		return goal;
	}

	public String getGoalId() {
		if (goal == null || goal.getGoal_id() == null || goal.getGoal_id().getId() == null) {
			return "";
		}

		return goal.getGoal_id().getId();
	}

	public F getProgressParam() {
		return progressParam;
	}

	public R getResultParam() {
		return resultParam;
	}

	@Override
	public String toString() {
		if (name == null) {
			return "";
		}
		return name;
	}

	public synchronized void clean() {
		synchronized (subscribedTopicLock) {
			for (Topic topic : subscribedTopics) {
				if (topic.isSubscribed()) {
					topic.unsubscribe();
				}
			}
			subscribedTopics.clear();
		}
		listenerSet.remove(TaskManager.getInstance());
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
		if (taskState != null) {
			taskState.setExtra(extra);
		}
	}

	public int getTimeout() {
		return this.timeout;
	}

	private void notifyStarted() {
		for (ITaskStateListener listener : listenerSet) {
			listener.onTaskStarted(this, transport, taskState);
		}
	}

	private void completeTask() {
		if (completed) {
			return;
		}

		for (ITaskStateListener listener : listenerSet) {
			listener.onTaskCompleted(this, transport, taskState, runResult);
		}

		TimerManager.getInstance().removeTimer(timeoutTimerId);

		listenerSet.clear();
		clean();
		completed = true;

		LOGGER.info("notifyCompleted " + getId());
	}

	@Override
	public int run(ITransport transport, Object contextObj) {
		synchronized (taskStateLock) {
			if (started) {
				LOGGER.error("already started " + getId());
				return -3;
			}
			
			this.transport = transport;
			started = true;
			taskState.setStartTime(System.currentTimeMillis());
			notifyStarted();

			context = (RosContext) contextObj;
			if (context == null) {
				LOGGER.error("context is not RosContext, abort " + getId());
				taskState.setTaskStatus(ABORTED);
				completeTask();
				return -1;
			}

			ExecuteResult result = subscribeKeyTopics(context.getRos());
			// TODO: exception handler
			if (result != ExecuteResult.Success) {
				LOGGER.error("key topic(s) not found, abort " + getId());
				taskState.setTaskStatus(ABORTED);
				completeTask();
				return -2;
			}

			Gson gson = new Gson();
			String body = gson.toJson(goal);

			// TODO: this is not good !!!
			if (goal.getGoal().getClass().getName().equals(String.class.getName())) {
				int index = body.indexOf("\"goal\"");
				String tmp = body.substring(0, index);
				body = tmp + "\"goal\":" + (String) goal.getGoal() + "}";
			}

			timeoutTimerId = TimerManager.getInstance().addTimer(TimerType.FireOnce, timeout, this);

			LOGGER.info("GOAL: \r\n" + body);
			Message message = new Message(body);
			Topic goalTopic = new Topic(context.getRos(), goalTopicName, topicMessageTypeMap.get(goalTopicName));
			goalTopic.advertise();
			goalTopic.publish(message);
			LOGGER.info("GOAL published " + getId());

			return 0;
		}
	}

	@Override
	public int cancel() {
		synchronized (taskStateLock) {

			if (completed) {
				LOGGER.error("task has already completed: " + getId());
				return -2;
			}

			if (!started) {
				LOGGER.error("task has not started yet: " + getId());
				return -1;
			}

			// TODO: cancelled actual action_lib

			LOGGER.info("cancell task: " + getId());

			String body = gson.toJson(goal.getGoal_id());

			Message message = new Message(body);
			Topic cancelTopic = new Topic(context.getRos(), cancellTopicName,
					topicMessageTypeMap.get(cancellTopicName));
			cancelTopic.publish(message);

			return 0;
		}
	}

	@Override
	public String getId() {
		return this.goal.getGoal_id().getId();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onStateChanged(ITransport transport, StateName oldStateName, StateName newStateName) {
		if(StateName.ExecutingTask.equals(newStateName) && StateName.Connecting.equals(oldStateName)) {
			subscribeKeyTopics(context.getRos());
		}
	}

	@Override
	public void onMessage(ITransport transport, String topic, String message) {

	}

	@Override
	public void onTimeout(long id) {
		if (timeoutTimerId != id) {
			return;
		}
		LOGGER.warn("Task has timedout: " + getId());

		synchronized (this.taskStateLock) {
			taskState.setTaskStatus(TIMEOUTED);
			completeTask();
		}
	}

	@Override
	public void addListener(ITaskStateListener listener) {
		listenerSet.add(listener);
	}

	@Override
	public void removeListener(ITaskStateListener listener) {
		listenerSet.remove(listener);
	}

	private Goal<G> goal;
	private F progressParam;
	private R resultParam;
	private String name;
	private boolean completed;
	private boolean started;
	private String goalTopicName;
	private String feedbackTopicName;
	private String resultTopicName;
	private String statusTopicName;
	private String cancellTopicName;
	private List<Topic> subscribedTopics = new ArrayList<Topic>();
	private List<String> keyTopicNames = new ArrayList<String>();
	private CopyOnWriteArraySet<ITaskStateListener> listenerSet = new CopyOnWriteArraySet<ITaskStateListener>();
	private Object subscribedTopicLock = new Object();
	private Object taskStateLock = new Object();
	private Object extra;
	private int timeout;
	private ActionTaskState taskState;
	private ITransport transport;
	private long timeoutTimerId;
	private Result<R> runResult;
	private RosContext context;
	private static final String FEEDBACK_STATUS_KEY = "status";
	private Gson gson = new Gson();
	private static boolean DEBUG = false;
	private Map<String, String> topicMessageTypeMap = new HashMap<String, String>();
}
