package com.alibaba.robot.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;

import com.alibaba.robot.common.Const;
import com.alibaba.robot.status.StatusManager;
import com.alibaba.robot.status.StatusCategory;
import com.alibaba.robot.status.StatusInfo;
import com.alibaba.robot.transport.ITransport;
import com.alibaba.robot.transport.ITransportListener;
import com.alibaba.robot.transport.StateName;
import com.alibaba.robot.web.manage.entity.CacheManager;
import com.alibaba.robot.web.manage.entity.RobotStatus;
import com.google.gson.Gson;

/**
 * General task manager
 */
public class TaskManager implements Const, ITransportListener, ITaskStateListener {

	private static Logger LOGGER = Logger.getLogger(TaskManager.class);
	private static TaskManager INSTANCE = new TaskManager();
	private static Gson gson = new Gson();

	private CopyOnWriteArraySet<ITaskStateListener> listenerSet = new CopyOnWriteArraySet<ITaskStateListener>();

	public static TaskManager getInstance() {
		return INSTANCE;
	}

	private TaskManager() {

	}

	@Override
	public void onStateChanged(ITransport transport, StateName oldStateName, StateName newStateName) {

	}

	public RobotStatus getStatus(String robotId) {

		Gson gson = new Gson();
		String statusText = CacheManager.getInstance().get(KEY_STATUS_PREFIX + robotId, null);
		if (null == statusText) {
			return null;
		}

		RobotStatus status = gson.fromJson(statusText, RobotStatus.class);
		return status;
	}

	@Override
	public void onMessage(ITransport transport, String topic, String message) {

	}

	/**
	 * get task status by goal id
	 */
	public int getTaskStatus(String goalId) {
		return CacheManager.getInstance().getInt(GOAL_ID_STATUS_KEY_PREFIX + goalId, -1);
	}

	/**
	 * get task status by goal id
	 */
	public int getBusinessStatus(String taskId) {
		String bussnessKey = StatusManager.KEY_STATUS_PREFIX + StatusCategory.Task + taskId;
		String value = CacheManager.getInstance().hget(bussnessKey, StatusInfo.BUSINESS_STATUS);
		if (value == null) {
			return 0;
		}

		int ret = Integer.parseInt(value);
		return ret;
	}

	@Override
	public void onTaskStarted(ITask task, ITransport transport, TaskState taskState) {

		LOGGER.info("onTaskStarted");

		String robotKey = StatusManager.KEY_STATUS_PREFIX + StatusCategory.Task + transport.getId();
		String taskKey = StatusManager.KEY_STATUS_PREFIX + StatusCategory.Task + task.getId();

		Map<String, String> hash = new HashMap<String, String>();
		
		hash.put(StatusInfo.TASK_NAME, taskState.getName());
		hash.put(StatusInfo.TASK_STATUS, Integer.toString(taskState.getTaskStatus()));
		hash.put(StatusInfo.BUSINESS_STATUS, Integer.toString(taskState.getStatus()));
		hash.put(StatusInfo.TASK_START_NAME, Long.toString(taskState.getStartTime()));
		hash.put(StatusInfo.TASK_RUN_ID, taskState.getRunId());

		if (taskState.getExtra() != null) {
			String params = gson.toJson(taskState.getExtra());
			hash.put( StatusInfo.TASK_PARAMS, params);
		}
		
		CacheManager.getInstance().hmset(robotKey, hash);
		CacheManager.getInstance().hmset(taskKey, hash);
		
		CacheManager.getInstance().set(StatusManager.KEY_ROBOT_TASK_PREFIX + transport.getId() , task.getId());

		
		for (ITaskStateListener listener : listenerSet) {
			listener.onTaskStarted(task, transport, taskState);
		}
	}

	@Override
	public void onTaskFeedback(ITask task, ITransport transport, TaskState taskState, Object feedback) {
		if (transport == null || taskState == null) {
			LOGGER.error("taskState is null");
			return;
		}
		
		String robotKey = StatusManager.KEY_STATUS_PREFIX + StatusCategory.Task + transport.getId();
		CacheManager.getInstance().hset(robotKey, StatusInfo.TASK_STATUS, Integer.toString(taskState.getTaskStatus()));
		CacheManager.getInstance().hset(robotKey, StatusInfo.BUSINESS_STATUS, Integer.toString(taskState.getStatus()));

		String taskKey = StatusManager.KEY_STATUS_PREFIX + StatusCategory.Task + task.getId();
		CacheManager.getInstance().hset(taskKey, StatusInfo.TASK_STATUS, Integer.toString(taskState.getTaskStatus()));
		CacheManager.getInstance().hset(taskKey, StatusInfo.BUSINESS_STATUS, Integer.toString(taskState.getStatus()));
		
		for (ITaskStateListener listener : listenerSet) {
			listener.onTaskFeedback(task, transport, taskState, feedback);
		}
	}

	@Override
	public void onTaskCompleted(ITask task, ITransport transport, TaskState taskState, Object result) {
		if (taskState == null) {
			LOGGER.error("taskState is null");
			return;
		}
		
		String robotKey = StatusManager.KEY_STATUS_PREFIX + StatusCategory.Task + transport.getId();
		String taskKey = StatusManager.KEY_STATUS_PREFIX + StatusCategory.Task + task.getId();

		CacheManager.getInstance().hset(robotKey, StatusInfo.TASK_STATUS, Integer.toString(taskState.getTaskStatus()));
		CacheManager.getInstance().hset(robotKey, StatusInfo.BUSINESS_STATUS, Integer.toString(taskState.getStatus()));
		
		CacheManager.getInstance().hset(taskKey, StatusInfo.TASK_STATUS, Integer.toString(taskState.getTaskStatus()));
		CacheManager.getInstance().hset(taskKey, StatusInfo.BUSINESS_STATUS, Integer.toString(taskState.getStatus()));
		
		for (ITaskStateListener listener : listenerSet) {
			listener.onTaskCompleted(task, transport, taskState, result);
		}
	}

	public void addListener(ITaskStateListener listener) {
		listenerSet.add(listener);
	}

	public void removeListener(ITaskStateListener listener) {
		listenerSet.remove(listener);
	}
}
