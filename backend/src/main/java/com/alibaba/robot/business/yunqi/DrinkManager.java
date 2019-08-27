package com.alibaba.robot.business.yunqi;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import javax.json.JsonObject;

import org.apache.log4j.Logger;
import com.alibaba.robot.actionlib.Feedback;
import com.alibaba.robot.common.BuildVariants;
import com.alibaba.robot.task.ITask;
import com.alibaba.robot.task.ITaskStateListener;
import com.alibaba.robot.task.TaskState;
import com.alibaba.robot.transport.IServiceCallback;
import com.alibaba.robot.transport.ITransport;
import com.alibaba.robot.transport.StateName;
import com.alibaba.robot.web.manage.entity.CacheManager;
import com.alibaba.robot.web.manage.pojo.Drink;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.wpi.rail.jrosbridge.Service;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;
import edu.wpi.rail.jrosbridge.services.ServiceResponse;

public class DrinkManager implements Runnable, IServiceCallback, ITaskStateListener {

	private static Logger LOGGER = Logger.getLogger(DrinkManager.class);

	private static Gson gson = new Gson();

	/***
	 * pick_armend: 饮料被拿走， 一次1罐
	 */
	private static final String CRAWLER_PICKED_UP = "pick_armend";

	/***
	 * arrange_armend 饮料放到柜子， 一次4罐
	 */
	private static final String CRAWLER_ARRANGED = "arrange_armend";

	private Object drinkCheckLock = new Object();

	private static final int CHECK_DRINK_INTERVAL = 5000;

	/**
	 * pick_armend: 饮料被拿走， 一次4罐 arrange_armend: 饮料放到柜子， 一次4罐
	 */
	static class DeliveryInfo {
		public double getPick_process() {
			return pick_process;
		}

		public String getPick_description() {
			return pick_description;
		}

		private double pick_process;
		private String pick_description;
	}

	static class ArrangeInfo {
		public double getPick_process() {
			return arrange_process;
		}

		public String getPick_description() {
			return arrange_description;
		}

		private double arrange_process;
		private String arrange_description;
	}

	private static DrinkManager INSTANCE = new DrinkManager();

	public static DrinkManager getInstance() {
		return INSTANCE;
	}

	private DrinkManager() {
		if (BuildVariants.YUNQI_ENABLED) {
			init();
		}
	}

	public static interface DrinkNumberUpdatedListener {
		void onDrinkUpdated();
	}

	private void init() {

		for (int i = 0; i < DRINK_TYPES; i++) {
			if (BuildVariants.YUNQI_DEBUG) {
				availableDrinks[0][i] = 4;
				availableDrinks[1][i] = TOTAL_DRINK_OF_SINGLE_TYPE;
			} else {
				availableDrinks[0][i] = 5;
				availableDrinks[1][i] = 24; // TOTAL_DRINK_OF_SINGLE_TYPE;
			}
		}

		// availableDrinks[0][2] = 2;
		// availableDrinks[0][0] = 1;

		// 0: 货柜
		// 1: 车里

		// 1: 可乐
		// 2: 雪碧
		// 3: 芬达
		// 4: 加多宝

		if (BuildVariants.YUNQI_DEBUG_WIDH_REAL_BOTS) {
			availableDrinks[0][0] = 3;
			availableDrinks[1][0] = 5;

			availableDrinks[0][1] = 3;
			availableDrinks[1][1] = 5;

			availableDrinks[0][2] = 4;
			availableDrinks[1][2] = 5;

			availableDrinks[0][3] = 1;
			availableDrinks[1][3] = 5;
		}

		Thread drinkManagerThread = new Thread(this);
		drinkManagerThread.setName("DrinkManager");
		drinkManagerThread.start();
	}

	private final static int DRINK_TYPES = 4;
	private final static int STORAGE_TYPES = 2;
	private final static int MAX_DRINKS_IN_CABINET = 5;
	private final static int TOTAL_DRINK_OF_SINGLE_TYPE = 24;

	private Object drinkLock = new Object();
	private int[][] availableDrinks = new int[STORAGE_TYPES][DRINK_TYPES];

	/**
	 * 被预订的饮料类型和数量
	 */
	private int[] reservedDrinks = new int[DRINK_TYPES];

	/**
	 * 正在从车里搬运到柜子里的饮料类型和数量
	 */
	private int[] carryingDrinks = new int[DRINK_TYPES];

//	int32 drink_type
//	---
//	int32 data_buffer
//	int32 data_warehouse

	private static final String DRINK_SERVICE_NAME = "/drink_data_search";
	private static final String DRINK_SERVICE_TYPE = "/robotic_arm/DrinkDataSearch";

	private String[] drinkInfo = new String[4];

	public void onStateChanged(ITransport transport, StateName oldStateName, StateName newStateName) {
		if (StateName.Connecting.equals(oldStateName)
				&& (StateName.Established.equals(newStateName) || StateName.ExecutingTask.equals(newStateName))) {
			transport.advertiseService(DRINK_SERVICE_NAME, DRINK_SERVICE_TYPE, this);
			LOGGER.info("Drink service advertised");
		}
	}

	@Override
	public void onTaskStarted(ITask task, ITransport transport, TaskState taskState) {
	}

	@Override
	public void onTaskCompleted(ITask task, ITransport transport, TaskState taskState, Object result) {
	}

	@Override
	public void onTaskFeedback(ITask task, ITransport transport, TaskState taskState, Object feedback) {
		Gson gson = new Gson();

		LOGGER.info(feedback);

		if (RobotManager.MODEL_DELIVERY.equals(transport.getModel())) {

			Feedback<DeliveryInfo> exactFeedback = new Feedback<DeliveryInfo>();
			Type type = new TypeToken<Feedback<DeliveryInfo>>() {
			}.getType();

			exactFeedback = gson.fromJson(feedback.toString(), type);
			String goalId = task.getId();
			int drinkType = CacheManager.getInstance().getInt(RobotManager.GOAL_DRINK_TYPE_KEY_PREFIX + goalId, -1);
			if (drinkType <= 0 || drinkType > 4) {
				LOGGER.error("drink type is invalid: " + drinkType);
				return;
			}

			if (CRAWLER_PICKED_UP.equals(exactFeedback.getFeedback().pick_description)) {
				onDrinkPicked(drinkType, true);
			}
		} else if (RobotManager.MODEL_CARRY.equals(transport.getModel())) {

			Feedback<ArrangeInfo> exactFeedback = new Feedback<ArrangeInfo>();
			Type type = new TypeToken<Feedback<ArrangeInfo>>() {
			}.getType();

			exactFeedback = gson.fromJson(feedback.toString(), type);
			String goalId = task.getId();
			int drinkType = CacheManager.getInstance().getInt(RobotManager.GOAL_DRINK_TYPE_KEY_PREFIX + goalId, -1);
			if (drinkType <= 0 || drinkType > 4) {
				LOGGER.error("drink type is invalid: " + drinkType);
				return;
			}

			if (CRAWLER_PICKED_UP.equals(exactFeedback.getFeedback().arrange_description)) {
				onDrinkPicked(drinkType, false);
			} else if (CRAWLER_ARRANGED.equals(exactFeedback.getFeedback().arrange_description)) {
				onDrinkArranged(drinkType);
			}
		}

	}

//	public void onActionFeedback(ITransport transport, Feedback<? extends Object> feedback) {
//
//		Gson gson = new Gson();
//		String jsonFeedback = gson.toJson(feedback);
//
//		LOGGER.info(jsonFeedback);
//
//		if (RobotManager.DELIVERY_ROBOT_MODEL.equals(transport.getModel())) {
//
//			Feedback<DeliveryInfo> exactFeedback = new Feedback<DeliveryInfo>();
//			Type type = new TypeToken<Feedback<DeliveryInfo>>() {
//			}.getType();
//
//			exactFeedback = gson.fromJson(jsonFeedback, type);
//			String goalId = feedback.getStatus().getGoal_id().getId();
//			int drinkType = CacheManager.getInstance().getInt(RobotManager.GOAL_DRINK_TYPE_KEY_PREFIX + goalId, -1);
//			if (drinkType <= 0 || drinkType > 4) {
//				LOGGER.error("drink type is invalid: " + drinkType);
//				return;
//			}
//
//			if (CRAWLER_PICKED_UP.equals(exactFeedback.getFeedback().pick_description)) {
//				onDrinkPicked(drinkType, true);
//			}
//		} else if (RobotManager.CARRY_ROBOT_MODEL.equals(transport.getModel())) {
//
//			Feedback<ArrangeInfo> exactFeedback = new Feedback<ArrangeInfo>();
//			Type type = new TypeToken<Feedback<ArrangeInfo>>() {
//			}.getType();
//
//			exactFeedback = gson.fromJson(jsonFeedback, type);
//			String goalId = feedback.getStatus().getGoal_id().getId();
//			int drinkType = CacheManager.getInstance().getInt(RobotManager.GOAL_DRINK_TYPE_KEY_PREFIX + goalId, -1);
//			if (drinkType <= 0 || drinkType > 4) {
//				LOGGER.error("drink type is invalid: " + drinkType);
//				return;
//			}
//
//			if (CRAWLER_PICKED_UP.equals(exactFeedback.getFeedback().arrange_description)) {
//				onDrinkPicked(drinkType, false);
//			} else if (CRAWLER_ARRANGED.equals(exactFeedback.getFeedback().arrange_description)) {
//				onDrinkArranged(drinkType);
//			}
//		}
//
//	}

	private void onDrinkPicked(int drinkType, boolean isDelivery) {
		LOGGER.info("onDrinkPicked drinkType = " + drinkType + ", isDelivery = " + isDelivery);
		int i = drinkType - 1;
		synchronized (drinkLock) {
			if (isDelivery) {
				reservedDrinks[i]--;
				availableDrinks[0][i]--;
			} else {
				availableDrinks[1][i]--;
			}
			dumpDrinkInfo();
		}
	}

	private void onDrinkArranged(int drinkType) {
		LOGGER.info("onDrinkArranged drinkType = " + drinkType);
		int i = drinkType - 1;
		synchronized (drinkLock) {
			availableDrinks[0][i] += 4;
			availableDrinks[1][i] -= 4;
			carryingDrinks[i] -= 4;
			dumpDrinkInfo();
		}
	}

	public boolean reserveCabinetDrink(DrinkType drinkType) {
		int i = drinkType.intValue() - 1;
		synchronized (drinkLock) {
			if (availableDrinks[0][i] - reservedDrinks[i] > 0) {
				reservedDrinks[i]++;
				dumpDrinkInfo();
				return true;
			}
			return false;
		}
	}

	/***
	 * 计划开始搬运饮料<br>
	 * 前提:<br>
	 * 1. 车里有对应的饮料， 柜子里加上搬运和正在搬运的不超过5罐子
	 * 
	 * @return 0 表现目前不需要搬运，正数表示已经保留为正在搬运的
	 */
	private int prepareToCarry(int i) {

		// 可以搬运的饮料，目前车里有的 - 即将被搬走的
		int drinkInCart = availableDrinks[1][i] - carryingDrinks[i];
		// 最多搬运4罐
		if (drinkInCart > 4) {
			drinkInCart = 4;
		}

		if (drinkInCart > 0 && drinkInCart + availableDrinks[0][i] + carryingDrinks[i] <= MAX_DRINKS_IN_CABINET) {
			carryingDrinks[i] += drinkInCart;
			return drinkInCart;
		}

		return 0;
	}

	private void dumpDrinkInfo() {

		String cabinetDrink = String.format("%02d %02d %02d %02d", availableDrinks[0][0], availableDrinks[0][1],
				availableDrinks[0][2], availableDrinks[0][3]);
		String cartDrink = String.format("%02d %02d %02d %02d", availableDrinks[1][0], availableDrinks[1][1],
				availableDrinks[1][2], availableDrinks[1][3]);
		String reservedDrink = String.format("%02d %02d %02d %02d", reservedDrinks[0], reservedDrinks[1],
				reservedDrinks[2], reservedDrinks[3]);
		String carryingDrink = String.format("%02d %02d %02d %02d", carryingDrinks[0], carryingDrinks[1],
				carryingDrinks[2], carryingDrinks[3]);

		if (!cabinetDrink.equals(drinkInfo[0]) || !cartDrink.equals(drinkInfo[1]) || !reservedDrink.equals(drinkInfo[2])
				|| !carryingDrink.equals(drinkInfo[3])) {

			drinkInfo[0] = cabinetDrink;
			drinkInfo[1] = cartDrink;
			drinkInfo[2] = reservedDrink;
			drinkInfo[3] = carryingDrink;

			LOGGER.info("CABINET : " + cabinetDrink);
			LOGGER.info("CART    : " + cartDrink);
			LOGGER.info("RESERVED: " + reservedDrink);
			LOGGER.info("CARRYING: " + carryingDrink);
		}
	}

	public StorageInfo getAvailableCount(DrinkType drinkType) {
		int i = drinkType.intValue() - 1;
		synchronized (drinkLock) {
			if (availableDrinks[0][i] > 0) {
				return new StorageInfo(drinkType, StorageType.StorageTypeCabinet,
						availableDrinks[0][i] - reservedDrinks[i]);

			} else if (availableDrinks[1][i] > 0) {
				return new StorageInfo(drinkType, StorageType.StorageTypeCart, availableDrinks[0][i]);
			}
			return null;
		}
	}

	/***
	 * API to show current drinks available<br>
	 */
	public List<Drink> getDrinkInfo() {

		List<Drink> drinkInfo = new LinkedList<Drink>();
		synchronized (drinkLock) {
			for (int i = 0; i < STORAGE_TYPES; i++) {
				for (int j = 0; j < DRINK_TYPES; j++) {
					if (i == StorageType.StorageTypeCabinet.intValue() - 1) {
						drinkInfo.add(new Drink(j + 1, i + 1, availableDrinks[i][j] - reservedDrinks[j]));
					} else {
						drinkInfo.add(new Drink(j + 1, i + 1, availableDrinks[i][j]));
					}
				}
			}
		}

		return drinkInfo;
	}

	/***
	 * API to show all drinks data including reserved and carrying<br>
	 */
	public List<Drink> getAllDrinkInfo() {

		List<Drink> drinkInfo = new LinkedList<Drink>();
		synchronized (drinkLock) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < DRINK_TYPES; j++) {
					if (i == StorageType.StorageTypeCabinet.intValue() - 1) {
						drinkInfo.add(new Drink(j + 1, i + 1, availableDrinks[i][j] - reservedDrinks[j]));
					} else if (i == StorageType.StorageTypeCart.intValue() - 1) {
						drinkInfo.add(new Drink(j + 1, i + 1, availableDrinks[i][j]));
					} else if (i == 2) { // reserved
						drinkInfo.add(new Drink(j + 1, i + 1, reservedDrinks[j]));
					} else {
						drinkInfo.add(new Drink(j + 1, i + 1, carryingDrinks[j]));
					}
				}
			}
		}

		return drinkInfo;
	}

	/**
	 * 调整饮料数量
	 */
	public void adjustDrinkAmount(DrinkType drinkType, int storageType, int amount) {
		if (drinkType == null || storageType <= 0 || storageType > 4) {
			return;
		}

		int storageIndex = storageType - 1;
		int drinkIndex = drinkType.intValue() - 1;
		int finalAmount = 0;

		synchronized (drinkLock) {
			if (storageIndex < 2) {
				finalAmount = availableDrinks[storageIndex][drinkIndex] + amount;
				if (finalAmount >= 0) {
					availableDrinks[storageIndex][drinkIndex] = finalAmount;
				}
			} else if (storageIndex == 2) { // reserved
				finalAmount = reservedDrinks[drinkIndex] + amount;
				if (finalAmount >= 0) {
					reservedDrinks[drinkIndex] = finalAmount;
				}
			} else { // carrying
				finalAmount = carryingDrinks[drinkIndex] + amount;
				if (finalAmount >= 0) {
					carryingDrinks[drinkIndex] = finalAmount;
				}
			}

			dumpDrinkInfo();
		}
	}

	private boolean carryingDrink = false;

	/***
	 * 检查饮料情况， 如果有需要下发送货任务
	 */
	private void checkDrinks() {

		synchronized (drinkLock) {
			if (carryingDrink) {
				LOGGER.info("carrying drink, return");
				return;
			}

			dumpDrinkInfo();

			for (int i = 0; i < DRINK_TYPES; i++) {
				// 货柜 + 要运来的还是 缺货， 并且车里还有，那么就尝试补货
				if (availableDrinks[0][i] + carryingDrinks[i] <= 1 && availableDrinks[1][i] > 0) {
					int drinkNum = prepareToCarry(i);
					if (drinkNum > 0) {
						int result = RobotManager.getInstance().carryDrink(i + 1);
						if (result != RobotManager.DELIVERY_SCHEULE_SUCCESS) {
							carryingDrinks[i] -= drinkNum;
						} else {
							// carryingDrink = true;
						}
					}
				}
			}
		}
	}

	public void checkDrinksNow() {
		synchronized (drinkCheckLock) {
			drinkCheckLock.notify();
		}
	}

	@Override
	public void run() {
		while (true) {
			synchronized (drinkCheckLock) {
				try {
					drinkCheckLock.wait(CHECK_DRINK_INTERVAL);
				} catch (InterruptedException e) {
					break;
				}
			}

			checkDrinks();
		}
	}

	public boolean cancelReservedDrink(DrinkType drinkType) {
		synchronized (drinkLock) {
			if (reservedDrinks[drinkType.intValue() - 1] > 0) {
				reservedDrinks[drinkType.intValue() - 1]--;
				availableDrinks[0][drinkType.intValue() - 1]++;
				dumpDrinkInfo();
				return true;
			}
			return false;
		}
	}

	static class DrinkInfoForRobot {
		public int data_buffer;
		public int data_warehouse;
	}

	/****
	 * ROS Service call handler
	 */
	@Override
	public void onRequest(Service service, ServiceRequest serviceRequest) {
		LOGGER.info("Service REQ: " + serviceRequest.toString() + ", id = " + serviceRequest.getId());

//		int32 drink_type
//		---
//		int32 data_buffer
//		int32 data_warehouse

		// {"drink_type":1}

		JsonObject requestObject = serviceRequest.toJsonObject();
		int drinkType = requestObject.getInt("drink_type", 0);

		String responseStr;
		synchronized (drinkLock) {
			DrinkInfoForRobot drinkInfoForRobot = new DrinkInfoForRobot();
			if (drinkType > 0 && drinkType < 5) {
				drinkInfoForRobot.data_buffer = availableDrinks[0][drinkType - 1];
				drinkInfoForRobot.data_warehouse = availableDrinks[1][drinkType - 1];
			} else {
				LOGGER.error("drinkType is out of boundary");
			}
			responseStr = gson.toJson(drinkInfoForRobot);
		}

		LOGGER.info("Service RESP: " + responseStr + ", id = " + serviceRequest.getId());
		ServiceResponse response = new ServiceResponse(responseStr, DRINK_SERVICE_TYPE, true);

		service.sendResponse(response, serviceRequest.getId());
	}
}
