package com.alibaba.robot.business.hema;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.alibaba.robot.common.BuildVariants;
import com.alibaba.robot.common.DbUtil;
import com.alibaba.robot.common.TtsExecutor;
import com.alibaba.robot.publish.DataTag;
import com.alibaba.robot.publish.PublishManager;
import com.alibaba.robot.status.StatusManager;
import com.alibaba.robot.status.StatusCategory;
import com.alibaba.robot.status.StatusInfo;
import com.alibaba.robot.task.ITask;
import com.alibaba.robot.task.ITaskStateListener;
import com.alibaba.robot.task.TaskState;
import com.alibaba.robot.transport.ITransport;
import com.alibaba.robot.transport.ITransportListener;
import com.alibaba.robot.transport.StateName;
import com.alibaba.robot.web.manage.entity.CacheManager;
import com.alibaba.robot.web.manage.entity.GetHemaReq;
import com.alibaba.robot.web.manage.pojo.Robot;
import com.alibaba.robot.web.manage.pojo.RobotDetailedStatus;
import com.alibaba.robot.web.manage.pojo.Vector3;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RobotMonitor implements ITransportListener, ITaskStateListener {

	private static class Data {
		private String data;
	}

	private static final Logger LOGGER = Logger.getLogger(RobotMonitor.class);
	private static final Gson gson = new Gson();
	private static final int STATUS_PUSH_INTERVAL = 2000;
	private static RobotMonitor INSTANCE = new RobotMonitor();

	private Map<String, Long> idPushTimeMap = new ConcurrentHashMap<String, Long>();

	public static RobotMonitor getInstance() {
		return INSTANCE;
	}

	private RobotMonitor() {
		init();
	}

	private void publishUpdate(String robotId) {
		if (BuildVariants.HEMA_FILTER_ROBOT_PUSH && !BuildVariants.HEMA_DEBUGGING_ROBOT_ID.equals(robotId)) {
			return;
		}

		Set<String> idSet = new HashSet<String>();
		idSet.add(robotId);
		List<RobotStatus> allRobotStatus = getRobotInfoForHema(idSet);
		for (RobotStatus robotStatus : allRobotStatus) {
			PublishManager.getInstance().publish(DataTag.DATA_FOR_HEMA, gson.toJson(robotStatus));
		}
	}

	public List<RobotStatus> getRobotInfoForHema() {
		return getRobotInfoForHema(null);
	}

	public List<RobotStatus> getRobotInfoForHema(Set<String> interestedIds) {

		List<RobotStatus> statusList = new LinkedList<RobotStatus>();

		Set<String> robotIdSet = new HashSet<String>();
		CacheManager.getInstance().smembers(StatusManager.KEY_INFO_IDS, robotIdSet);

		// get all categories
		for (String id : robotIdSet) {
			if (interestedIds != null && !interestedIds.contains(id)) {
				continue;
			}

			RobotStatus robotStatus = getRobotStatusById(id);
			statusList.add(robotStatus);
		}

		return statusList;
	}

	private RobotStatus getRobotStatusById(String robotId) {

		RobotStatus robotStatus = new RobotStatus();
		RobotStatus robotTaskStatus = new RobotStatus();
		Set<String> categorySet = new HashSet<String>();
		CacheManager.getInstance().smembers(StatusManager.KEY_CATEGORY_PREFIX + robotId, categorySet);

		int businessStatus = -1;

		for (String category : categorySet) {

			Map<String, String> statusMap = new HashMap<String, String>();
			// get all data of each category
			CacheManager.getInstance().hgetall(StatusManager.KEY_STATUS_PREFIX + category + robotId, statusMap);

			for (Map.Entry<String, String> entry : statusMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				if (StatusCategory.Basic.toString().equals(category)) {
					if (StatusInfo.ID.equals(key)) {
						robotStatus.robotUniqueId = value;
					} else if (StatusInfo.STATE.equals(key)) {
						LOGGER.info("STATE: " + value);
						robotStatus.robotState = Integer.parseInt(value);
					}
				} else if (StatusCategory.Task.toString().equals(category)) {
					if (StatusInfo.TASK_RUN_ID.equals(key)) {
						robotTaskStatus.taskId = value;
					} else if (StatusInfo.TASK_STATUS.equals(key)) {
						robotTaskStatus.taskState = (int) Double.parseDouble(value);
					} else if (StatusInfo.BUSINESS_STATUS.equals(key)) {
						businessStatus = (int) Double.parseDouble(value);
					} else if (StatusInfo.TASK_PARAMS.equals(key)) {
						try {
							GetHemaReq getHemaReq = gson.fromJson(value, GetHemaReq.class);
							robotTaskStatus.originalReq = getHemaReq;
							robotTaskStatus.taskType = getHemaReq.getTaskType();
							robotTaskStatus.poiId = getHemaReq.getPoiId();
							robotStatus.warehouseCode = getHemaReq.getWarehouseCode();
							robotTaskStatus.businessTaskId = getHemaReq.getBusinessTaskId();
						} catch (JsonSyntaxException ex) {
							LOGGER.error(ex.getMessage());
						}
					}
				} else if (StatusCategory.Detailed.toString().equals(category)) {

					if (StatusInfo.DATA.equals(key)) {
						RobotDetailedStatus detailedStatus = new RobotDetailedStatus();
						Gson gson = new Gson();
						detailedStatus = gson.fromJson(value, RobotDetailedStatus.class);

						Vector3 location = detailedStatus.getRobot_cur_position().getTranslation();

						robotStatus.robotCoordinate.add(location.getX());
						robotStatus.robotCoordinate.add(location.getY());

						robotStatus.robotCapacity.add(detailedStatus.getRobot_doorA_capacity());
						robotStatus.robotCapacity.add(detailedStatus.getRobot_doorB_capacity());
						robotStatus.robotCapacity.add(detailedStatus.getRobot_doorC_capacity());
						robotStatus.robotCapacity.add(detailedStatus.getRobot_doorD_capacity());

						robotStatus.robotEnergy = detailedStatus.getRobot_battery_status();
					}
				}
			}
		}



		if (robotStatus.robotState == StateName.ExecutingTask.intValue()) {
			robotStatus.taskId = robotTaskStatus.taskId;
			robotStatus.taskState = robotTaskStatus.taskState;
			robotStatus.originalReq = robotTaskStatus.originalReq;
			robotStatus.taskType = robotTaskStatus.taskType;
			robotStatus.poiId = robotTaskStatus.poiId;
			robotStatus.businessTaskId = robotTaskStatus.businessTaskId;
			
			if (businessStatus != -1 && robotStatus.taskState == ITask.RUNNING) {
				robotStatus.taskState = businessStatus;
			} else {
				switch (robotStatus.taskState) {

				case ITask.STANDBY:
					robotStatus.taskState = TaskStatus.TASK_STATUS_WAIT.getCode();
					break;

				case ITask.SUCCEEDED:
					robotStatus.taskState = TaskStatus.TASK_STATUS_FINISH.getCode();
					break;

				case ITask.RUNNING:
					robotStatus.taskState = TaskStatus.TASK_STATUS_SCHEDULE.getCode();
					break;

				case ITask.TIMEOUTED:
					robotStatus.taskState = TaskStatus.TASK_STATUS_INTERRUPT_ROBOT_TIMEOUT.getCode();
					break;

				case ITask.ABORTED:

				}
			}
		} else {
			robotStatus.taskState = 0;
		}

		return robotStatus;
	}

	@Override
	public void onStateChanged(ITransport transport, StateName oldStateName, StateName newStateName) {
		publishUpdate(Integer.toString(transport.getId()));
	}

	private Map<String, String> ttsMap = new HashMap<String, String>();

	private void init() {
		ttsMap.put("greeting", "您好， 我来收餐啦。");
		ttsMap.put("leaving", "放好了吗？  我要走了， 祝您用餐愉快！");
		ttsMap.put("leaving_as_dishes_full", "啊哦，装满了， 下次再叫我吧。");
		ttsMap.put("leaving_no_task", "打扰了， 祝您用餐愉快！");
	}

	private String hackTtsForDemo(String originalText) {
		if (ttsMap.containsKey(originalText)) {
			return ttsMap.get(originalText);
		}

		LOGGER.warn("tts not found, use origianl text");
		return "您好。";
	}

	@Override
	public void onMessage(ITransport transport, String topic, String message) {

		if (topic == null || message == null) {
			return;
		}

		if (ITransport.ROBOT_TTS_TOPIC_NAME.equals(topic)) {

			Data data = gson.fromJson(message, Data.class);
			String content = data.data;

			String[] parts = content.split(":");
			if (parts.length != 2) {
				LOGGER.error("TM TTS encoding is incorrect");
				return;
			}

			String text = hackTtsForDemo(parts[1]);
			Robot robot = DbUtil.getRobotById(Integer.toString(transport.getId()));
			if (robot == null) {
				LOGGER.error("robot is not found");
				return;
			}

			if (robot.getFirmware() == null) {
				LOGGER.error("firmware is not set for TM UUID");
				return;
			}

			TtsExecutor.getInstance().ttsAsync(robot.getFirmware(), text);
		} else if (ITransport.ROBOT_STATUS_TOPIC_NAME.equals(topic)) {

			String key = Integer.toString(transport.getId());
			boolean needToPush = true;

			if (idPushTimeMap.containsKey(key)) {
				long tm = idPushTimeMap.get(key).longValue();
				if (System.currentTimeMillis() - tm < STATUS_PUSH_INTERVAL) {
					needToPush = false;
				}
			}

			if (needToPush) {
				publishUpdate(key);
				idPushTimeMap.put(key, System.currentTimeMillis());
			}
		}
	}

	@Override
	public void onTaskStarted(ITask task, ITransport transport, TaskState taskState) {
		publishUpdate(Integer.toString(transport.getId()));
	}

	@Override
	public void onTaskFeedback(ITask task, ITransport transport, TaskState taskState, Object feedback) {
		publishUpdate(Integer.toString(transport.getId()));
	}

	@Override
	public void onTaskCompleted(ITask task, ITransport transport, TaskState taskState, Object result) {
		publishUpdate(Integer.toString(transport.getId()));
	}
}
