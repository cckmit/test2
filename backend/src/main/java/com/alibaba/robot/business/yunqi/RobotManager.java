package com.alibaba.robot.business.yunqi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import com.alibaba.robot.actionlib.Action;
import com.alibaba.robot.actionlib.Goal;
import com.alibaba.robot.business.yunqi.bigscreen.ScreenDataParser;
import com.alibaba.robot.common.DbUtil;
import com.alibaba.robot.common.BuildVariants;
import com.alibaba.robot.common.Const;
import com.alibaba.robot.common.RandUtil;
import com.alibaba.robot.common.TtsExecutor;
import com.alibaba.robot.publish.DataTag;
import com.alibaba.robot.publish.PublishManager;
import com.alibaba.robot.status.StatusManager;
import com.alibaba.robot.status.StatusCategory;
import com.alibaba.robot.status.StatusInfo;
import com.alibaba.robot.task.ITask;
import com.alibaba.robot.task.ITaskStateListener;
import com.alibaba.robot.task.TaskManager;
import com.alibaba.robot.task.TaskState;
import com.alibaba.robot.transport.ITransport;
import com.alibaba.robot.transport.ITransportListener;
import com.alibaba.robot.transport.RunActionResult;
import com.alibaba.robot.transport.StateName;
import com.alibaba.robot.transport.TransportManager;
import com.alibaba.robot.web.manage.entity.CacheManager;
import com.alibaba.robot.web.manage.pojo.Location;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/****
 * 
 * Specific Yunqi Conference Robot Manager<br>
 * 
 * 
 * Hints:<br>
 * When drink in cabinet is empty, trigger robot to fetch from cart<br>
 * Map runActionEx to order and form a virtual order management<br>
 * Map order and executing robot id<br>
 * Robot responsibility : <br>
 * A2 to delivery drink to customer, <br>
 * A1 to carry drinks from cart to cabinet
 * 
 * <br>
 * We have 2 orders in parallel at most <br>
 * Information to feed: <br>
 * RunActionEx goal parameters, feedback, result, status<br>
 * 
 * Order object: 1. any order has state, and unique with table number and
 * drink<br>
 * type 2. status:<br>
 * S1 WaitingForDrink to be available<br>
 * S2 Delivery drink<br>
 * S3 finished<br>
 * S4 cancelled(due to timeout, no drinks at all)<br>
 * 
 * any placed order can be queried at any time (HTTP polling)<br>
 * 
 * CarryThread <br>
 * 1. periodically check if we need to carry drinks in<br>
 * 2. trigger check if drink number changes <br>
 * 3. run action with resource lock<br>
 * 4 different drinks at a time), failure is allowed<br>
 * 
 **/

public class RobotManager implements ITransportListener, ITaskStateListener {

	private static final Logger LOGGER = Logger.getLogger(RobotManager.class);

	private static RobotManager INSTANCE = new RobotManager();

	private static Random RAND = new Random(System.currentTimeMillis());

	private static final boolean USE_SIX_POINTS = true;

	private static Gson gson = new Gson();

	private static String TASK_NAME_DELIVERY = "web_go_pick";
	private static String TASK_NAME_CARRY = "web_go_arrange";

	public static final String MODEL_DELIVERY = "A2";

	public static final String MODEL_CARRY = "A1";

	// an extra idle point
	private static int[] A1_POI_IDS;

	private static int[] A2_POI_IDS;

	public static final int DELIVERY_SCHEULE_SUCCESS = 1;

	public static final int DELIVERY_BUSY = 2;

	public static final int DELIVERY_ROBOT_UNAVAILABLE = 3;

	public static final int DELIVERY_DRINK_UNAVAILABLE = 4;

	public static final int DELIVERY_SCHEDULE_FAILED = 5;

	/**
	 * 送饮料任务超时时间, 单位毫秒
	 */
	private static final int TIMEOUT_DELIVERY = 600000;

	/***
	 * 补饮料任务超时时间, 单位毫秒
	 */
	private static final int TIMEOUT_CARRY = 1200000;

	public static final String GOAL_DRINK_TYPE_KEY_PREFIX = "drk_goal_";

	private final Object deliveryRobotLock = new Object();

	private final Map<Integer, Integer> deliveryRobotStatus = new ConcurrentHashMap<Integer, Integer>();
	private final Set<Integer> deliveryingRobotSet = new HashSet<Integer>();
	private final Map<Integer, ITask> tableTaskMap = new HashMap<Integer, ITask>();

	private final Object carryRobotLock = new Object();

	private final Map<Integer, Integer> carryRobotStatus = new ConcurrentHashMap<Integer, Integer>();
	private final Set<Integer> carryingRobotSet = new HashSet<Integer>();
	private final Map<Integer, ITask> carryingTaskMap = new HashMap<Integer, ITask>();

	static class Data {
		String data;
	}

	private ScreenDataParser screenDataParser = new ScreenDataParser();

	public static RobotManager getInstance() {
		return INSTANCE;
	}

	private void init() {
		A1_POI_IDS = new int[] { 1, 2, 3, 4, 5, 6, 7 };
		A2_POI_IDS = new int[] { 11, 12, 13, 14, 15, 16, 17 };
	}

	public static enum RobotScheduleResult {
		Success, RobotIsBusy, RobotUnavailable, Failed
	}

	private RobotManager() {
		init();
	}

	public synchronized int deliveryDrink(int tableNumber, int drinkType, ITaskStateListener taskStateListener) {
		return deliveryDrink(tableNumber, drinkType, RandUtil.getRandomGoalId(), taskStateListener);
	}

	public int carryDrink(int drinkType) {
		return carryDrink(drinkType, RandUtil.getRandomGoalId());
	}

	private List<Location> getLocationForCarryRobot(int robotId) {
		List<Integer> idList = new LinkedList<Integer>();

		for (int i = 0; i < A1_POI_IDS.length - 2; i++) {
			idList.add(A1_POI_IDS[i]);
		}

		if ((robotId % 2) == 1) {
			idList.add(A1_POI_IDS[A1_POI_IDS.length - 1]);
		} else {
			idList.add(A1_POI_IDS[A1_POI_IDS.length - 2]);
		}

		// sort location, as DB may return location in arbitrary order
		List<Location> locationLists = DbUtil.getLocations(idList);

		List<Location> lists = new ArrayList<Location>();
		for (int i = 0; i < idList.size(); i++) {
			for (Location loc : locationLists) {
				if (loc.getId().intValue() == idList.get(i).intValue()) {
					lists.add(loc);
					break;
				}
			}
		}

		return lists;
	}

	private List<Location> getLocationForDeliveryRobot(int robotId, int tableNumber) {

		List<Integer> idList = new LinkedList<Integer>();

		// 3rd location locates table
		
		
		// 1 2 3 5 6
		// 1 2 4 5 6
		
		// 1,2,3 4,6,7
		// 1,2,3 5,6,7
		for (int i = 0; i < 3; i++) {
			idList.add(A2_POI_IDS[i]);
		}

		if (tableNumber == 1) {
			idList.add(A2_POI_IDS[3]);
		} else {
			idList.add(A2_POI_IDS[4]);
		}

		idList.add(A2_POI_IDS[5]);
		idList.add(A2_POI_IDS[6]);

		// sort location, as DB may return location in arbitrary order
		List<Location> locationLists = DbUtil.getLocations(idList);

		List<Location> lists = new ArrayList<Location>();
		for (int i = 0; i < idList.size(); i++) {
			for (Location loc : locationLists) {
				if (loc.getId().intValue() == idList.get(i).intValue()) {
					lists.add(loc);
					break;
				}
			}
		}

		return lists;
	}

	private String buildGoalParam(List<Location> locationLists, int drinkType, int tableNumber) {

		StringBuilder sb = new StringBuilder();
		if (BuildVariants.YUNQI_DEBUG) {
			Location location = locationLists.get(0);
			sb.append(String.format("{\"pos_x\":%f, \"pos_y\":%f, \"ori_z\":%f, \"ori_w\":%f, \"drink_type\":%d}",
					location.getX(), location.getY(), location.getZ(), location.getW(), drinkType));
		} else {
			for (int i = 0; i < locationLists.size(); i++) {
				Location location = locationLists.get(i);
				if (i == 0) {
					sb.append("{");
				}
				if (i == locationLists.size() - 1) {
					sb.append(String.format(
							"\"poi%d_pos_x\":%f, \"poi%d_pos_y\":%f, \"poi%d_ori_z\":%f, \"poi%d_ori_w\":%f", i + 1,
							location.getX(), i + 1, location.getY(), i + 1, location.getZ(), i + 1, location.getW()));
					if (tableNumber > 0) {
						sb.append(", \"drink_type\":" + drinkType + ", \"table_number\":" + tableNumber + "}");
					} else {
						sb.append(", \"drink_type\":" + drinkType + "}");
					}

				} else {
					sb.append(String.format(
							"\"poi%d_pos_x\":%f, \"poi%d_pos_y\":%f, \"poi%d_ori_z\":%f, \"poi%d_ori_w\":%f,", i + 1,
							location.getX(), i + 1, location.getY(), i + 1, location.getZ(), i + 1, location.getW()));
				}
			}
		}

		return sb.toString();
	}

	public int carryDrink(int drinkType, String goalId) {

		LOGGER.info("carryDrink drinkType = " + drinkType + ", taskId = " + goalId);

		synchronized (carryRobotLock) {

			List<Integer> availableRobots = new LinkedList<Integer>();
			for (Integer key : carryRobotStatus.keySet()) {
				if (carryRobotStatus.get(key) == StateName.Established.intValue() && !carryingRobotSet.contains(key)) {
					availableRobots.add(key);
				}
			}

			if (availableRobots.size() == 0) {
				LOGGER.info("carryDrink abort, robot unavailable");
				return DELIVERY_ROBOT_UNAVAILABLE;
			}

			int robotId = availableRobots.get(RAND.nextInt(availableRobots.size()));

			LOGGER.info("carryDrink carry robot chosen: " + robotId);

			CacheManager.getInstance().set(GOAL_DRINK_TYPE_KEY_PREFIX + goalId, drinkType);
			String goalParam = buildGoalParam(getLocationForCarryRobot(robotId), drinkType, 0);
			if (goalParam == null) {
				LOGGER.error("goal param is null");
				return DELIVERY_SCHEDULE_FAILED;
			}

			Goal goal = new Goal(goalId, goalParam);
			Action action = new Action(TASK_NAME_CARRY, goal, TIMEOUT_CARRY);
			action.addListener(this);

			RunActionResult runResult = TransportManager.getInstance().runAction(robotId, action);
			if (runResult == RunActionResult.Success) {
				carryingRobotSet.add(Integer.valueOf(robotId));
				carryingTaskMap.put(robotId, action);
				return DELIVERY_SCHEULE_SUCCESS;
			}

			return DELIVERY_SCHEDULE_FAILED;
		}
	}

	public boolean cancelDeliveryTask(int tableNumber) {

		synchronized (deliveryRobotLock) {
			if (tableTaskMap.containsKey(Integer.valueOf(tableNumber))) {
				return tableTaskMap.get(tableNumber).cancel() == 0;
			}
		}

		return false;
	}

	public void cancelCarryingTasks() {
		synchronized (this.carryRobotLock) {
			for (Map.Entry<Integer, ITask> entry : carryingTaskMap.entrySet()) {
				LOGGER.info("cancal task: " + entry.getValue().getId());
				entry.getValue().cancel();
			}
		}
	}

	/***
	 * Place an order<br>
	 * Find a spare delivery robot to run delivery task
	 * 
	 * @param drinkType
	 * @param tableNumber to find relevant POI and form required action goal
	 */
	public int deliveryDrink(int tableNumber, int drinkType, String goalId, ITaskStateListener taskStateListener) {

		LOGGER.info(
				"deliveryDrink drinkType = " + drinkType + ", tableNumber = " + tableNumber + ", goal_id = " + goalId);

		synchronized (deliveryRobotLock) {

			List<Integer> availableRobots = new LinkedList<Integer>();

			for (Integer key : deliveryRobotStatus.keySet()) {
				if (deliveryRobotStatus.get(key) == StateName.Established.intValue()
						&& !deliveryingRobotSet.contains(key)) {
					availableRobots.add(key);
				}
			}

			if (availableRobots.size() == 0) {
				LOGGER.info("deliveryDrink robot unavailable");
				return DELIVERY_ROBOT_UNAVAILABLE;
			}

			int robotId = availableRobots.get(RAND.nextInt(availableRobots.size()));

			LOGGER.info("deliveryDrink carry robot chosen: " + robotId);

			CacheManager.getInstance().set(GOAL_DRINK_TYPE_KEY_PREFIX + goalId, drinkType);
			String goalParam = buildGoalParam(getLocationForDeliveryRobot(robotId, tableNumber), drinkType,
					tableNumber);

			if (goalParam == null) {
				LOGGER.error("goal param is null");
				return DELIVERY_SCHEDULE_FAILED;
			}

			Goal goal = new Goal(goalId, goalParam);
			Action action = new Action(TASK_NAME_DELIVERY, goal, TIMEOUT_DELIVERY);
			action.setExtra(Integer.valueOf(tableNumber));
			action.addListener(this);
			action.addListener(taskStateListener);

			RunActionResult runResult = TransportManager.getInstance().runAction(robotId, action);
			if (runResult == RunActionResult.Success) {
				tableTaskMap.put(tableNumber, action);
				deliveryingRobotSet.add(Integer.valueOf(robotId));
				return DELIVERY_SCHEULE_SUCCESS;
			}

			return DELIVERY_SCHEDULE_FAILED;
		}
	}

	@Override
	public void onStateChanged(ITransport transport, StateName oldStateName, StateName newStateName) {

		DrinkManager.getInstance().onStateChanged(transport, oldStateName, newStateName);

		if (transport.getModel().equals(MODEL_DELIVERY)) {
			synchronized (deliveryRobotLock) {
				deliveryRobotStatus.put(transport.getId(), newStateName.intValue());
			}
		} else if (transport.getModel().equals(MODEL_CARRY)) {
			synchronized (carryRobotLock) {
				carryRobotStatus.put(transport.getId(), newStateName.intValue());
			}
		}

//		for delta information
//		online event: 1. connecting --> established, 2 connecting --> executing_task
//		offline event: 1. established --> disconnected, 2 executing_task --> disconnected
//		executing task robots: 
//
//
//		data:
//		online details
//		offline robot_id
//		executing_task: details, task information
		int id = transport.getId();
		JsonObject resultList = new JsonObject();
		JsonArray offlineRobots = new JsonArray();
		JsonArray onlineRobots = new JsonArray();
		JsonArray runningRobots = new JsonArray();

		/***
		 * online event:<br>
		 * 1. connecting --> established<br>
		 * 2. connecting --> executing_task
		 */
		if (StateName.Connecting.equals(oldStateName)
				&& (StateName.Established.equals(newStateName) || StateName.ExecutingTask.equals(newStateName))) {

			JsonObject robotObjct = new JsonObject();
			robotObjct.add(StatusCategory.Basic.toString(),
					StatusManager.getInstance().getBasicInfo(Integer.toString(id)));
			robotObjct.add(StatusCategory.Detailed.toString(),
					StatusManager.getInstance().getDetailedInfo(Integer.toString(id)));

			onlineRobots.add(robotObjct);

		} else if (StateName.Disconnected.equals(newStateName)
				&& (StateName.Established.equals(oldStateName) || StateName.ExecutingTask.equals(oldStateName))) {

			JsonObject robotObjct = new JsonObject();
			robotObjct.add(StatusCategory.Basic.toString(),
					StatusManager.getInstance().getBasicInfo(Integer.toString(id)));
			offlineRobots.add(robotObjct);

		} else if (StateName.Established.equals(oldStateName) && StateName.ExecutingTask.equals(newStateName)) {
			JsonObject robotObjct = new JsonObject();
			robotObjct.add(StatusCategory.Basic.toString(),
					StatusManager.getInstance().getBasicInfo(Integer.toString(id)));
			robotObjct.add(StatusCategory.Detailed.toString(),
					StatusManager.getInstance().getDetailedInfo(Integer.toString(id)));
			robotObjct.add(StatusCategory.Task.toString(),
					StatusManager.getInstance().getTaskInfo(Integer.toString(id)));
			runningRobots.add(robotObjct);
		}

		if (onlineRobots.size() > 0) {
			resultList.add("online", onlineRobots);
		}
		if (offlineRobots.size() > 0) {
			resultList.add("offline", offlineRobots);
		}
		if (runningRobots.size() > 0) {
			resultList.add("running", runningRobots);
		}

		if (resultList.size() > 0) {
			PublishManager.getInstance().publish(DataTag.CUSTOMIZED_DATA_FOR_ALG, resultList.toString());
		}
	}

	@Override
	public void onMessage(ITransport transport, String topic, String message) {
		if (!BuildVariants.YUNQI_ENABLED || topic == null || message == null) {
			return;
		}

		if (ITransport.ROBOT_TTS_TOPIC_NAME.equals(topic)) {
			Data data = gson.fromJson(message, Data.class);
			String content = data.data;
			LOGGER.info("TTS MESSAGE: " + content);

			String robotKey = StatusManager.KEY_STATUS_PREFIX + StatusCategory.Task + transport.getId();
			String value = CacheManager.getInstance().hget(robotKey, StatusInfo.TASK_PARAMS);
			int tableNumber = 0;
			try {
				tableNumber = Integer.parseInt(value);
			} catch (NumberFormatException ex) {
				LOGGER.info("param is not number!");
				return;
			}

			// TTS 语料
			// 您好， 饮料已送到， 请取用
			//
			// end 祝您观展愉快

			// TaskManager.getInstance().

			// 1 --> 白色那台的 uuid 已变更为：3E5C050FCBBEDEEF31CA124ADD23A8C7；
			// 2 --> 红色的 uuid ：9D10CDE4DAFB4F1E43903647E7FCCE63

			String uid = tableNumber == 1 ? "3E5C050FCBBEDEEF31CA124ADD23A8C7" : "9D10CDE4DAFB4F1E43903647E7FCCE63";
			String realContent = content.equals("start") ? "您好， 饮料已送到， 请取用" : "祝您观展愉快";

			TtsExecutor.getInstance().ttsAsync(uid, realContent);
 
			LOGGER.info("uuid = " + uid + ", " + realContent);

		} else if (ITransport.ROBOT_SCREEN_TOPIC_NAME.equals(topic)) {

			// LOGGER.info(message);
			byte[] data = null;
			try {
				data = screenDataParser.parse(transport, message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (data != null && data.length > 0) {
				PublishManager.getInstance().publish(DataTag.BIG_SCREEN_DATA, data);
			}
		}
	}

	/***
	 * 给算法调用
	 */
	public String getCustomizedRobotInfo() {

		Set<String> robotIdSet = new HashSet<String>();
		CacheManager.getInstance().smembers(StatusManager.KEY_INFO_IDS, robotIdSet);
		JsonObject resultList = new JsonObject();
		JsonArray offlineRobots = new JsonArray();
		JsonArray onlineRobots = new JsonArray();
		JsonArray runningRobots = new JsonArray();

		// get all categories
		for (String id : robotIdSet) {

			JsonObject robotObject = new JsonObject();
			int robotState = Const.ROBOT_STATE_DISABLED;

			Set<String> categorySet = new HashSet<String>();
			CacheManager.getInstance().smembers(StatusManager.KEY_CATEGORY_PREFIX + id, categorySet);

			for (String category : categorySet) {
				JsonObject categoryObject = new JsonObject();

				Map<String, String> statusMap = new HashMap<String, String>();
				// get all data of each category
				CacheManager.getInstance().hgetall(StatusManager.KEY_STATUS_PREFIX + category + id, statusMap);

				if (statusMap.size() > 0) {
					for (Map.Entry<String, String> entry : statusMap.entrySet()) {
						categoryObject.addProperty(entry.getKey(), entry.getValue());
						if (StatusCategory.Basic.toString().equals(category) && StatusInfo.ID.equals(entry.getKey())) {
							robotState = Integer.parseInt(entry.getValue());
						}
					}
				}

				robotObject.add(category, categoryObject);
			}

			if (robotState == Const.ROBOT_STATE_RUNNING_TASK) {
				runningRobots.add(robotObject);
			} else if (robotState == Const.ROBOT_STATE_DISCONNECTED || robotState == Const.ROBOT_STATE_CONNECTING) {
				offlineRobots.add(robotObject);
			} else if (robotState == Const.ROBOT_STATE_ESTABLISHED) {
				onlineRobots.add(robotObject);
			}
		}

		resultList.add("online", onlineRobots);

		resultList.add("offline", offlineRobots);

		resultList.add("running", runningRobots);

		return resultList.toString();
	}

	@Override
	public void onTaskStarted(ITask task, ITransport transport, TaskState taskState) {

	}

	@Override
	public void onTaskFeedback(ITask task, ITransport transport, TaskState taskState, Object feedback) {
		DrinkManager.getInstance().onTaskFeedback(task, transport, taskState, feedback);
	}

	@Override
	public void onTaskCompleted(ITask task, ITransport transport, TaskState taskState, Object result) {

		if (transport.getModel().equals(MODEL_DELIVERY)) {
			synchronized (deliveryRobotLock) {
				deliveryingRobotSet.remove(Integer.valueOf(transport.getId()));
				Integer tableNumber = (Integer) task.getExtra();
				tableTaskMap.remove(tableNumber.intValue());
			}
		} else if (transport.getModel().equals(MODEL_CARRY)) {
			synchronized (this.carryRobotLock) {
				carryingRobotSet.remove(Integer.valueOf(transport.getId()));
				carryingTaskMap.remove(Integer.valueOf(transport.getId()));
			}
		}
	}
}
