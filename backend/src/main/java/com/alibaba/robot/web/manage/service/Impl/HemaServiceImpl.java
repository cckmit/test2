package com.alibaba.robot.web.manage.service.Impl;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.robot.actionlib.Action;
import com.alibaba.robot.actionlib.Goal;
import com.alibaba.robot.business.hema.HemaModel;
import com.alibaba.robot.business.hema.RobotMonitor;
import com.alibaba.robot.business.hema.RobotStatus;
import com.alibaba.robot.business.hema.SystemConfiguration;
import com.alibaba.robot.common.DbUtil;
import com.alibaba.robot.common.ErrorCode;
import com.alibaba.robot.common.JDBCUtil;
import com.alibaba.robot.actionlib.SyncTask;
import com.alibaba.robot.business.hema.LocationForCharge;
import com.alibaba.robot.business.hema.LocationForMoveTo;
import com.alibaba.robot.business.hema.LocationParam;
import com.alibaba.robot.business.hema.LocationParamForCollection;
import com.alibaba.robot.common.BuildVariants;
import com.alibaba.robot.transport.ITransport;
import com.alibaba.robot.transport.RunActionResult;
import com.alibaba.robot.transport.StateName;
import com.alibaba.robot.transport.TransportManager;
import com.alibaba.robot.web.manage.entity.GetHemaReq;
import com.alibaba.robot.web.manage.entity.GetHemaReqList;
import com.alibaba.robot.web.manage.entity.HemaResp;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.pojo.Location;
import com.alibaba.robot.web.manage.pojo.Robot;
import com.alibaba.robot.web.manage.service.HemaService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Service
public class HemaServiceImpl implements HemaService {

	/***
	 * Topic to notify dish collection completion
	 */
	private static final String NOTIFY_CLEAN_DESKTOP_COMPLETE_TOPIC = "/robot/debug_demo_0913";

	/***
	 * std_msgs/Bool
	 **/
	private static final String EMERGENCY_HALT_RECOVER_TOPIC = "/move_base/DWAPlannerROS/collab_path_ctrl";

	/***
	 * dump dishes
	 */
	private static final String TASK_NAME_DUMP_DISHES = "web_go_pick";

	/***
	 * collect dishes
	 */
	private static final String TASK_NAME_COLLECT_DISHES = "web_go_arrange";

	/***
	 * battery charge
	 */
	private static final String TASK_NAME_CHARGE = "web_battery_charge";

	/***
	 * move to specific location<br>
	 */
	private static String TASK_NAME_MOVE_TO = "webmove";

	/***
	 * collection service POI suffix
	 */
	private static final String COLLECT_SUFFIX_LEFT = "-L";

	/***
	 * collection service POI suffix
	 */
	private static final String COLLECT_SUFFIX_RIGHT = "-R";

	/***
	 * dump service POI suffix
	 */
	private static final String DUMP_SUFFIX_BEHIND_DOOR = "-BEHIND";

	/***
	 * dump service POI suffix
	 */
	private static final String DUMP_SUFFIX_RETURN = "-RETURN";

	private static Gson gson = new Gson();

	private static Logger LOGGER = Logger.getLogger(HemaServiceImpl.class);

	private Executor executor = Executors.newFixedThreadPool(2);
	
	public HemaServiceImpl() {
		if (BuildVariants.HEMA_MOCK_ENABLED) {
			TASK_NAME_MOVE_TO = "web_go_arrange";
		}
	}

	private String buildGoalParam(String taskName, String poiName) {

		if (TASK_NAME_COLLECT_DISHES.equals(taskName)) {
			return buildGoalParamForCollectionTask(poiName);
		} else if (TASK_NAME_DUMP_DISHES.equals(taskName)) {
			return buildGoalParamForDumpTask(poiName);
		} else if (TASK_NAME_CHARGE.equals(taskName)) {
			Location location = DbUtil.getLocationByName(poiName);
			LocationForCharge locationParam = new LocationForCharge(location);
			return gson.toJson(locationParam);
		} else if (TASK_NAME_MOVE_TO.equals(taskName)) {
			Location location = DbUtil.getLocationByName(poiName);
			if(location == null) {
				return null;
			}
			LocationForMoveTo locationParam = new LocationForMoveTo(location, poiName);
			return gson.toJson(locationParam);
		}

		Location location = DbUtil.getLocationByName(poiName);
		LocationParam locationParam = new LocationParam(location);
		return gson.toJson(locationParam);
	}

	/***
	 * Query location for collection service which needs three POI
	 **/
	private String buildGoalParamForCollectionTask(String checkPointPoiName) {

		List<String> poiLists = new LinkedList<String>();
		poiLists.add(checkPointPoiName);
		poiLists.add(checkPointPoiName + COLLECT_SUFFIX_LEFT);
		poiLists.add(checkPointPoiName + COLLECT_SUFFIX_RIGHT);

		List<Location> locationList = DbUtil.getLocationsByName(poiLists);
		if(locationList.size() < 3) {
			LOGGER.error("not all POI found");
			return null;
		}
		
		
		if(locationList.size() >3) {
			LOGGER.warn("there are more POI than expected: " + locationList.size() );
		}

		List<Location> lists = new ArrayList<Location>();
		for (int i = 0; i < locationList.size(); i++) {
			for (Location loc : locationList) {
				if (loc.getName().equals(poiLists.get(i))) {
					lists.add(loc);
					break;
				}
			}
		}

		if (lists.size() != 3) {
			return null;
		}

		LocationParamForCollection locationParam = new LocationParamForCollection(lists);
		locationParam.setTableNumber(checkPointPoiName);
		return gson.toJson(locationParam);
	}

	/***
	 * Query location for clean service which needs three POI
	 **/
	private String buildGoalParamForDumpTask(String checkPointPoiName) {

		List<String> poiLists = new LinkedList<String>();
		poiLists.add(checkPointPoiName);
		poiLists.add(checkPointPoiName + DUMP_SUFFIX_BEHIND_DOOR);
		poiLists.add(checkPointPoiName + DUMP_SUFFIX_RETURN);

		List<Location> locationList = DbUtil.getLocationsByName(poiLists);
		if(locationList.size() < 3) {
			LOGGER.error("not all POI found");
			return null;
		}
		
		
		if(locationList.size() >3) {
			LOGGER.warn("there are more POI than expected: " + locationList.size() );
		}
		
		
		List<Location> lists = new ArrayList<Location>();

		for (int i = 0; i < poiLists.size(); i++) {
			for (Location loc : locationList) {
				if (loc.getName().equals(poiLists.get(i))) {
					lists.add(loc);
					break;
				}
			}
		}

		if (lists.size() != 3) {
			return null;
		}

		LocationParam locationParam = new LocationParam(lists);
		return gson.toJson(locationParam);
	}

	private class OutOfChargePlaceTask implements Runnable {

		public OutOfChargePlaceTask(ITransport transport, Request request, String taskName) {
			this.transport = transport;
			this.request = request;
			this.taskName = taskName;
		}

		private String taskName;
		private Request request;
		private ITransport transport;

		@Override
		public void run() {

			String data = request.getData().toString();
			GetHemaReq fakeReq = gson.fromJson(data, GetHemaReq.class);
			fakeReq.setTaskId("internal-task-id");
			fakeReq.setWarehouseCode("DO-NOT-USE");

			LocationForCharge locationParam = new LocationForCharge(LocationForCharge.REQUEST_DISCHARGE);
			String dischargeGoalParam = gson.toJson(locationParam);
			Goal dischargeGoal = new Goal(fakeReq.getTaskId(), dischargeGoalParam);
			Action dischargeAction = new Action(TASK_NAME_CHARGE, dischargeGoal, 120000);
			dischargeAction.setExtra(fakeReq);

			LOGGER.info("pre-off-charge");
			SyncTask syncTask = new SyncTask(dischargeAction, transport, 125000);
			syncTask.runTaskAndWait();
			LOGGER.info("post-off-charge");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
			
			runTask(taskName, request, false);
		}
	}

	private Response runTask(String taskName, Request request) {
		return runTask(taskName, request, true);
	}
	
	private Response runTask(String taskName, Request request, boolean dischargeIfCharging) {

		String inputArgument = gson.toJson(request);
		LOGGER.info(inputArgument);

		String data = request.getData().toString();
		GetHemaReq getHemaReq = gson.fromJson(data, GetHemaReq.class);
		String robotUniqueId = getHemaReq.getRobotUniqueId();

		String goalParam = buildGoalParam(taskName, getHemaReq.getPoiId());
		if (goalParam == null) {
			return new Response<>(request.getSn(), "POI is not found", ErrorCode.FAILED);
		}
		
		int robotId = 0;
		
		try {
			robotId = Integer.parseInt(robotUniqueId);
		} catch(NumberFormatException ex) {
			return new Response(request.getSn(), "malformed unique robot id", ErrorCode.FAILED);
		}
		
		ITransport robot = TransportManager.getInstance().getRobot(robotId);
		if (robot == null) {
			return new Response(request.getSn(), "Robot is not found", ErrorCode.FAILED);
		}

		if (StateName.Charging.equals(robot.getState())) {
			if(dischargeIfCharging) {
				LOGGER.info("robot is charging, discharge first");
				OutOfChargePlaceTask futureTask = new OutOfChargePlaceTask(robot, request, taskName);
				executor.execute(futureTask);
				return new Response(request.getSn(), "success", ErrorCode.SUCCESS);
			} else {
				return new Response(request.getSn(), "Robot is being charged, can not run task", ErrorCode.FAILED);
			}
		}

		Goal goal = new Goal(getHemaReq.getTaskId(), goalParam);
		Action action = new Action(taskName, goal, getHemaReq.getTaskTimeout());
		action.setExtra(getHemaReq);

		RunActionResult runResult = TransportManager.getInstance().runAction(robotId, action);
		if (runResult == RunActionResult.Success) {
			return new Response(request.getSn(), "success", ErrorCode.SUCCESS);
		}

		return new Response(request.getSn(), runResult.toString(), ErrorCode.FAILED);
	}

	// 充电调度
	public Response charge(Request request) {
		return runTask(TASK_NAME_CHARGE, request);
	}

	// 呼叫收餐调度
	public Response callingCleanTable(Request request) {
		return runTask(TASK_NAME_COLLECT_DISHES, request);
	}

	// 收餐调度
	public Response schedule(Request request) {
		return runTask(TASK_NAME_COLLECT_DISHES, request);
	}

	// 回收调度
	public Response dumpDishes(Request request) {
		return runTask(TASK_NAME_DUMP_DISHES, request);
	}

	// 点对点调度
	public Response moveTo(Request request) {
		return runTask(TASK_NAME_MOVE_TO, request);
	}

	// 检修调度
	public Response repair(Request request) {
		return new Response(request.getSn(), "NOT_IMPLEMENTED_YET", ErrorCode.NOT_IMPLEMENTED_YET);
	}

	// 巡航收餐调度
	public Response cruiseCleanTable(Request request) {
		return runTask(TASK_NAME_COLLECT_DISHES, request);
	}

	// 播放声音
	public Response playAudio(Request request) {
		String data = request.getData().toString();
		GetHemaReq getHemaReq = gson.fromJson(data, GetHemaReq.class);
		int musiceIndex = getHemaReq.getMusiceIndex();

		Response response = new Response<>(request.getSn(), "success", ErrorCode.SUCCESS);
		return response;
	}

	// 添加机器人
	public Response addInfo(Request request) {
		try {
			String data = request.getData().toString();
			GetHemaReq getHemaReq = gson.fromJson(data, GetHemaReq.class);
			String warehouseCode = getHemaReq.getWarehouseCode();
			String robotUniqueId = getHemaReq.getRobotUniqueId();

			Connection connection = JDBCUtil.getConnection();
			String sqlRobot = "insert into robot(robotUniqueId,warehouseCode) values( ? , ? )";
			PreparedStatement ps = connection.prepareStatement(sqlRobot);
			ps.setString(1, robotUniqueId);
			ps.setString(2, warehouseCode);
			ps.executeUpdate();

			JDBCUtil.close(connection, ps);

			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, null);
		} catch (Exception e) {
			LOGGER.info("/hema/addInfo--error: "+ e);
			return new Response<>(request.getSn(), "false", 1, null);
		}

	}

	// 删除机器人
	public Response deleteInfo(Request request) {
		try {
			String data = request.getData().toString();
			GetHemaReq getHemaReq = gson.fromJson(data, GetHemaReq.class);
			String warehouseCode = getHemaReq.getWarehouseCode();
			String robotUniqueId = getHemaReq.getRobotUniqueId();

			Connection connection = JDBCUtil.getConnection();
			String sqlRobot = "delete from robot where robotUniqueId = ? and warehouseCode = ?";
			PreparedStatement ps = connection.prepareStatement(sqlRobot);
			ps.setString(1, robotUniqueId);
			ps.setString(2, warehouseCode);
			ps.executeUpdate();

			JDBCUtil.close(connection, ps);

			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, null);
		} catch (Exception e) {
			LOGGER.info("/hema/addInfo--error: "+ e);
			return new Response<>(request.getSn(), "false", 1, null);
		}
	}

	// 查询机器人
	public Response<List<RobotStatus>> getInfo(Request request) {

		String data = request.getData().toString();
		GetHemaReqList getHemaReqList = gson.fromJson(data, GetHemaReqList.class);
		String warehouseCode = getHemaReqList.getWarehouseCode();

		List<HemaResp> hemaRespList = new ArrayList<>();
		String robotUniqueIds = getHemaReqList.getRobotUniqueIds().toString();
		Type type = new TypeToken<List<String>>() {}.getType();

		List<String> robotUniqueList = gson.fromJson(robotUniqueIds, type);

		Set<String> robotSet = new HashSet<String>();
		for (String id : robotUniqueList) {
			robotSet.add(id);
		}

		List<RobotStatus> robotInfo = RobotMonitor.getInstance().getRobotInfoForHema(robotSet);
		return new Response<List<RobotStatus>>(request.getSn(), "success", ErrorCode.SUCCESS, robotInfo);

	}

	// 紧停
	// topic /move_base/DWAPlannerROS/collab_path_ctrl std_msgs/Bool
	public Response emergencyHalt(Request request) {

		String data = request.getData().toString();
		GetHemaReq getHemaReq = gson.fromJson(data, GetHemaReq.class);
		String robotUniqueId = getHemaReq.getRobotUniqueId();
		Robot robot = DbUtil.getRobotById(robotUniqueId);
		if (robot == null) {
			return new Response<>(request.getSn(), "robot not found", ErrorCode.FAILED);
		}

		boolean result = TransportManager.getInstance().publishMessage(robot.getId(), EMERGENCY_HALT_RECOVER_TOPIC,
				"true", "std_msgs/String");
		if (result) {
			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS);
		}

		return new Response<>(request.getSn(), "failed", ErrorCode.FAILED);
	}

	// 恢复
	// topic /move_base/DWAPlannerROS/collab_path_ctrl std_msgs/Bool
	public Response recover(Request request) {
		String data = request.getData().toString();
		GetHemaReq getHemaReq = gson.fromJson(data, GetHemaReq.class);
		String robotUniqueId = getHemaReq.getRobotUniqueId();
		Robot robot = DbUtil.getRobotById(robotUniqueId);
		if (robot == null) {
			return new Response<>(request.getSn(), "robot not found", ErrorCode.FAILED);
		}

		boolean result = TransportManager.getInstance().publishMessage(robot.getId(), EMERGENCY_HALT_RECOVER_TOPIC,
				"false", "std_msgs/String");
		if (result) {
			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS);
		}
		return new Response<>(request.getSn(), "failed", ErrorCode.FAILED);
	}

	/***
	 * for debug purpose, do not use
	 */
	public Response notifyCleanTableComplete(Request request) {
		String data = request.getData().toString();
		GetHemaReq getHemaReq = gson.fromJson(data, GetHemaReq.class);
		String robotUniqueId = getHemaReq.getRobotUniqueId();
		Robot robot = DbUtil.getRobotById(robotUniqueId);
		if (robot == null) {
			return new Response<>(request.getSn(), "robot not found", ErrorCode.FAILED);
		}

		boolean result = TransportManager.getInstance().publishMessage(robot.getId(),
				NOTIFY_CLEAN_DESKTOP_COMPLETE_TOPIC, "arrange_ok", "std_msgs/String");
		if (result) {
			return new Response(request.getSn(), "success", ErrorCode.SUCCESS);
		}

		return new Response<>(request.getSn(), "failed", ErrorCode.FAILED);
	}

	//系统配置项添加
	public Response systemConfAdd(Request request) {
		Integer addConf = HemaModel.addConf(request);
		if(addConf == 1){
			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, null);
		}
		
		return new Response<>(request.getSn(), "failed", 2, null);
	}

	//系统配置项 值修改
	public Response systemConfUpdate(Request request) {
		Integer updateConf = HemaModel.updateConf(request);
		if(updateConf == 1){
			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, null);
		}
		return new Response<>(request.getSn(), "failed", 2, null);
	}

	//系统配置项删除
	public Response systemConfDelete(Request request) {
		Integer deleteConf = HemaModel.deleteConf(request);
		if(deleteConf == 1){
			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, null);
		}
		return new Response<>(request.getSn(), "failed", 2, null);
	}

	//单个系统配置项 值查询
	public Response systemConfSelectone(Request request) {
		Map<String, Object> confOne = HemaModel.selectConfOne(request);
		return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, confOne);
	}

	//所有系统配置项查询
	public Response systemConfSelectall(Request request) {
		Map<String, Object> confAll = HemaModel.selectConfAll();
		return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, confAll);
	}

	

	public Response cancelTask(Request request) {

		String data = request.getData().toString();
		GetHemaReq getHemaReq = gson.fromJson(data, GetHemaReq.class);
		String robotUniqueId = getHemaReq.getRobotUniqueId();

		try {
			int robotId = Integer.parseInt(robotUniqueId);
			TransportManager.getInstance().cancelTask(robotId, getHemaReq.getTaskId());
			return new Response(request.getSn(), "success", ErrorCode.SUCCESS);
		} catch (NumberFormatException ex) {
			return new Response(request.getSn(), "has bad argument", ErrorCode.BAD_ARGUMENTS);
		}
	}
}
