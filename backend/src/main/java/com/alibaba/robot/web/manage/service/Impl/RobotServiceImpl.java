package com.alibaba.robot.web.manage.service.Impl;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.robot.actionlib.Action;
import com.alibaba.robot.actionlib.Goal;
import com.alibaba.robot.actionlib.GoalId;
import com.alibaba.robot.attributdef.AttributeDefinitionModel;
import com.alibaba.robot.common.JDBCUtil;
import com.alibaba.robot.status.StatusManager;
import com.alibaba.robot.transport.RunActionResult;
import com.alibaba.robot.transport.TransportManager;
import com.alibaba.robot.web.manage.entity.ActionRequest;
import com.alibaba.robot.web.manage.entity.CacheManager;
import com.alibaba.robot.web.manage.entity.GenericRequest;
import com.alibaba.robot.web.manage.entity.GetInfoReq;
import com.alibaba.robot.web.manage.entity.GetRunActionEX;
import com.alibaba.robot.web.manage.entity.ModelAction;
import com.alibaba.robot.web.manage.entity.ModelActionGoal;
import com.alibaba.robot.web.manage.entity.ModelActionResult;
import com.alibaba.robot.web.manage.entity.Progress;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.entity.RobotStatus;
import com.alibaba.robot.web.manage.mapper.LocationDao;
import com.alibaba.robot.web.manage.mapper.ModelActionGoalDao;
import com.alibaba.robot.web.manage.mapper.RobotArmDao;
import com.alibaba.robot.web.manage.mapper.RobotCountDao;
import com.alibaba.robot.web.manage.mapper.RobotDao;
import com.alibaba.robot.web.manage.pojo.Attributes;
import com.alibaba.robot.web.manage.pojo.DelRobotAttribute;
import com.alibaba.robot.web.manage.pojo.Location;
import com.alibaba.robot.web.manage.pojo.Robot;
import com.alibaba.robot.web.manage.pojo.RobotArm;
import com.alibaba.robot.web.manage.pojo.RobotCount;
import com.alibaba.robot.web.manage.pojo.RobotDetailedStatus;
import com.alibaba.robot.web.manage.pojo.RobotItem;
import com.alibaba.robot.web.manage.service.RobotService;
import com.alibaba.robot.web.manage.utils.UUIDUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Service
public class RobotServiceImpl implements RobotService {

	@Autowired
	private RobotDao robotDao;
	@Autowired
	private LocationDao locationDao;

	// 查询机器人信息
	public Response GetInfo(Request request) {

		Gson gson = new Gson();
		// 将获取的json格式字符串转换为javabean
		String data = request.getData().toString();
		GetInfoReq getInfoReq = gson.fromJson(data, GetInfoReq.class);
		// 获取 参数值
		String robot_uniqueId = getInfoReq.getRobot_uniqueId();
		List<Robot> robotToReturn = new LinkedList<Robot>();
		if (robot_uniqueId == null || robot_uniqueId.length() == 0) {
			robotToReturn = robotDao.selectAll();
		} else {
			Robot robot = robotDao.selectByUniqueId(robot_uniqueId);
			robotToReturn.add(robot);
		}

		/*
		 * int robotState = RobotStateCache.getInstance().getState(robot.getId());
		 * if(robotState != -1) { robot.setState(robotState); }
		 */
		if (robotToReturn.size() > 0) {

		}

		return new Response<List<Robot>>(request.getSn(), "success", 1, robotToReturn);

	}

	// 更新机器人信息
	public Response updateInfo(Request request) {

		Gson gson = new Gson();
		String data = request.getData().toString();
		Robot robot = gson.fromJson(data, Robot.class);
		robotDao.updateByUniqueId(robot);
		return new Response(request.getSn(), "success", 1, null);

	}

	public Response addInfoEx(GenericRequest<Robot> request) {

		Gson gson = new Gson();
		// Type type = new TypeToken<Robot>() {}.getType();
		// String data = request.getData().toString();
		Robot robot = request.getData(); // gson.fromJson(data, type);
		String uniqueId = UUIDUtils.getUUID32();
		robot.setUniqueId(uniqueId);
		if (robotDao.insertRobot(robot) != 1) {
			// TODO
		}

		List<Attributes> attributes = robot.getAttributes();
		for (Attributes addAttribute : attributes) {
			String mAttribute_name = addAttribute.getmAttribute_name();// "name"
			String mAttribute_type = addAttribute.getmAttribute_type();// "String"
			Object mAttribute_value = addAttribute.getmAttribute_value();// "abc"
			AttributeDefinitionModel adm = new AttributeDefinitionModel();
			String typeJudge = adm.typeJudge(mAttribute_type);// string_value
			int typeValue = addAttribute.getValue(mAttribute_type);// 123
			// String attributeType = adm.getAttributeType(typeValue);//string_value
			if (typeJudge != null) {
				// Object value = attDefinition.getValue(typeJudge);
				int res = adm.insertAttributedefinition(typeJudge, mAttribute_name, typeValue, mAttribute_value,
						uniqueId);
				if (res != 1) {
					// TODO
					return new Response(request.getSn(), "false", 2, null);
				}
			}
		}

		return new Response(request.getSn(), "success", 1, null);
	}

	// 添加机器人信息
	public Response addInfo(Request request) {

		Gson gson = new Gson();
		Type type = new TypeToken<Robot>() {
		}.getType();
		String data = request.getData().toString();
		Robot robot = gson.fromJson(data, type);
		String uniqueId = UUIDUtils.getUUID32();
		robot.setUniqueId(uniqueId);
		if (robotDao.insertRobot(robot) != 1) {
			// TODO
		}
		List<Attributes> attributes = robot.getAttributes();
		for (Attributes addAttribute : attributes) {
			String mAttribute_name = addAttribute.getmAttribute_name();// "name"
			String mAttribute_type = addAttribute.getmAttribute_type();// "String"
			Object mAttribute_value = addAttribute.getmAttribute_value();// "abc"
			AttributeDefinitionModel adm = new AttributeDefinitionModel();
			String typeJudge = adm.typeJudge(mAttribute_type);// string_value
			int typeValue = addAttribute.getValue(mAttribute_type);// 123
			// String attributeType = adm.getAttributeType(typeValue);//string_value
			if (typeJudge != null) {
				// Object value = attDefinition.getValue(typeJudge);
				int res = adm.insertAttributedefinition(typeJudge, mAttribute_name, typeValue, mAttribute_value,
						uniqueId);
				if (res != 1) {
					// TODO
					return new Response(request.getSn(), "false", 2, null);
				}
			}
		}

		return new Response(request.getSn(), "success", 1, null);
	}

	// 删除单个机器人信息
	public Response deleteInfo(Request request) {

		Gson gson = new Gson();
		String data = request.getData().toString();
		GetInfoReq getInfoReq = gson.fromJson(data, GetInfoReq.class);
		String uniqueId = getInfoReq.getRobot_uniqueId();
		robotDao.deleteOneByUniqueId(uniqueId);

		return new Response(request.getSn(), "success", 1, null);

	}

	/**
	 * 删除机器人属性
	 * 
	 * @param request
	 * @return Response
	 */
	public Response deleteRobotAttribute(GenericRequest<Robot> request) {

		Gson gson = new Gson();
		Response res = null;

		Robot robot = request.getData(); // gson.fromJson(data, type);
		String uniqueId = robot.getUniqueId();
		robot.setUniqueId(uniqueId);
		String robot_uniqueId = robot.getUniqueId();
		List<Attributes> attributes = robot.getAttributes();

		try {
			Connection connection = JDBCUtil.getConnection();
			Savepoint savept = null;
			PreparedStatement statement = null;
			int delete = 0;
			connection.setAutoCommit(false);//
			savept = connection.setSavepoint();
			for (Attributes addAttribute : attributes) {
				String attribute = addAttribute.getmAttribute_name();
				String sql = "delete from attributedefinition where attribute = ? and robot_uniqueId = ?";
				statement = connection.prepareStatement(sql);
				statement.setString(1, attribute);
				statement.setString(2, robot_uniqueId);
				delete += statement.executeUpdate();
			}
			if (delete == attributes.size()) {
				connection.commit();
				connection.setAutoCommit(true);
				JDBCUtil.close(connection, statement);
				res = new Response(request.getSn(), "success", 1, null);
			} else {
				connection.rollback(savept);
				connection.setAutoCommit(true);
				JDBCUtil.close(connection, statement);
				res = new Response(request.getSn(), "false", 2, null);
			}
		} catch (SQLException e) {
		}
		return res;
	}

	/**
	 * 增加机器人属性
	 * 
	 * @param request
	 * @return Response
	 */
	public Response addRobotAttribute(Request request) {

		Gson gson = new Gson();
		String data = request.getData().toString();
		DelRobotAttribute getInfoReq = gson.fromJson(data, DelRobotAttribute.class);
		String robot_uniqueId = getInfoReq.getRobot_uniqueId();
		ArrayList<Attributes> attributes = getInfoReq.getAttributes();

		int add = 0;
		for (Attributes addAttribute : attributes) {
			String mAttribute_name = addAttribute.getmAttribute_name();// "name"
			String mAttribute_type = addAttribute.getmAttribute_type();// "String"
			Object mAttribute_value = addAttribute.getmAttribute_value();// "abc"
			AttributeDefinitionModel adm = new AttributeDefinitionModel();
			String typeJudge = adm.typeJudge(mAttribute_type);// string_value
			int typeValue = addAttribute.getValue(mAttribute_type);// 123
			// String attributeType = adm.getAttributeType(typeValue);//string_value
			if (typeJudge != null) {
				// Object value = attDefinition.getValue(typeJudge);
				int res = adm.insertAttributedefinition(typeJudge, mAttribute_name, typeValue, mAttribute_value,
						robot_uniqueId);
				if (res != 1) {
					// TODO
				}
			}
		}

		return new Response(request.getSn(), "success", 1, null);
	}

	// 执行Action
	public Response runAction(Request request) {

		HashMap<String, String> requestMap = (HashMap<String, String>) request.getData();

		ActionRequest modelAction = new ActionRequest(requestMap.get("action_name"), requestMap.get("action_goal_id"),
				requestMap.get("robot_id"), requestMap.get("action_goal"));

		String robot_id = modelAction.getRobot_id();
		int id = Integer.parseInt(robot_id);
		// Robot robot = robotService.selecetRobotById(id);

		GoalId goalId = new GoalId(modelAction.getAction_goal_id());

		Goal<String> stringGoal = new Goal<String>(goalId, modelAction.getActionGoal());

		Action<String, String, String> genericStringAction = new Action<String, String, String>(
				modelAction.getAction_name(), stringGoal);

		RunActionResult runActionResult = TransportManager.getInstance().runAction(id, genericStringAction);
		if (runActionResult == RunActionResult.Success) {
			return new Response(request.getSn(), "ok", 1, null);
		}

		return new Response(request.getSn(), runActionResult.toString(), 1, null);

	}

	// 查询Action执行情况
	public Response getActionResult(Request request) {

		Gson gson = new Gson();
		// 获取传递过来的参数 转为javabean
		String data = request.getData().toString();
		ModelAction modelAction = gson.fromJson(data, ModelAction.class);
		String robot_uniqueId = modelAction.getRobot_uniqueId();
		Robot robot = robotDao.selectByUniqueId(robot_uniqueId);
		// ActionResult actionResult = new ActionResult(robot.getState(),"执行正常");

		Progress progress = new Progress();
		progress.setName("");
		progress.setDescribe("");

		ModelActionResult modelActionResult = new ModelActionResult();
		modelActionResult.setStatus(robot.getStatus());
		modelActionResult.setProgress(progress);

		Response response = new Response(request.getSn(), "success", 1, modelActionResult);
		return response;

	}

	@Autowired
	private ModelActionGoalDao modelActionGoalDao;

	// 查询所有Action 信息
	public Response getActionAll(Request request) {

		Gson gson = new Gson();
		// 获取传递过来的参数 转为javabean
		String data = request.getData().toString();
		ModelAction actionRequest = gson.fromJson(data, ModelAction.class);
		int id = Integer.parseInt(actionRequest.getAction_goal_id());
		List<ModelActionGoal> list = modelActionGoalDao.selectAll();
		Response response = new Response(request.getSn(), "success", 1, list);
		return response;

	}

	@Autowired
	private RobotCountDao robotCountDao;
	@Autowired
	private RobotArmDao robotArmDao;

	// 接收机器人统计数据做更新
	public Response updateRobotTransportData(Request request) {

		Gson gson = new Gson();
		// 获取传递过来的参数 转为javabean
		String data = request.getData().toString();
		RobotCount robotCount = gson.fromJson(data, RobotCount.class);
		updateRobotTransportData(robotCount);
		return new Response(request.getSn(), "success", 1, null);

	}

	// 接收机器人统计数据做更新
	public void updateRobotTransportData(RobotCount robotCount) {
		// 先获取数据库的原数据
		RobotCount robotCountBefore = robotCountDao.selectByRobotUniqueId(robotCount.getRobot_uniqueId());
		RobotCount robotCountNew = new RobotCount();
		robotCountNew.setId(robotCount.getId());
		robotCountNew.setRunDistanceTotal(robotCount.getRunDistanceTotal() + robotCountBefore.getRunDistanceTotal());
		robotCountNew.setTransportTotal(robotCount.getTransportTotal() + robotCountBefore.getTransportTotal());
		robotCountNew.setRobot_uniqueId(robotCount.getRobot_uniqueId());
		robotCountDao.updateRobotTransportData(robotCountNew);
	}

	// 获取统计数据 机器人行驶路程和饮料数
	public Response getRobotTransportData(Request request) {
		Gson gson = new Gson();
		String data = request.getData().toString();
		ModelAction modelAction = gson.fromJson(data, ModelAction.class);
		String robot_uniqueId = modelAction.getRobot_uniqueId();
		RobotCount robotCount = selectByRobotUniqueId(robot_uniqueId);
		Response<RobotCount> response = new Response<RobotCount>(request.getSn(), "success", 1, robotCount);
		return response;

	}

	// 获取统计数据 机器人行驶路程和饮料数
	public RobotCount selectByRobotUniqueId(String robot_uniqueId) {
		RobotCount robotCount = robotCountDao.selectByRobotUniqueId(robot_uniqueId);
		return robotCount;
	}

	// 接收机械臂统计数据做更新
	public Response updateRobotArmData(Request request) {

		Gson gson = new Gson();
		// 将获取的json格式字符串转换为javabean
		// Request req = gson.fromJson(request, Request.class);
		// 获取传递过来的参数 转为javabean
		String data = request.getData().toString();
		RobotArm robotArm = gson.fromJson(data, RobotArm.class);
		updateRobotArmData(robotArm);
		return new Response(request.getSn(), "success", 1, null);

	}

	// 接收机械臂统计数据做更新
	public void updateRobotArmData(RobotArm robotArm) {
		// 先获取数据库中原数据
		RobotArm robotArmBefore = robotArmDao.selectArmById(robotArm.getId());
		RobotArm robotArmNew = new RobotArm();
		robotArmNew.setId(robotArm.getId());
		robotArmNew.setLoadCounts(robotArm.getLoadCounts() + robotArmBefore.getLoadCounts());
		robotArmNew.setLoadTotal(robotArm.getLoadTotal() + robotArmBefore.getLoadTotal());
		robotArmDao.updateRobotArmData(robotArmNew);
	}

	// 获取统计数据 机械臂装载次数和饮料数
	public Response getRobotArmData(Request request) {
		String data = request.getData().toString();
		Gson gson = new Gson();
		RobotArm fromJson = gson.fromJson(data, RobotArm.class);
		int armId = fromJson.getId();
		RobotArm robotArm = selectArmById(armId);
		Response<RobotArm> response = new Response<RobotArm>("", "success", 1, robotArm);
		return response;
	}

	// 获取统计数据 机械臂装载次数和饮料数
	public RobotArm selectArmById(int armId) {
		RobotArm robotArm = robotArmDao.selectArmById(armId);
		return robotArm;
	}

	// 控制机器人执行Action
	public Response runActionEx(Request request) {

		Gson gson = new Gson();
		String data = request.getData().toString();
		GetRunActionEX getRunActionEX = gson.fromJson(data, GetRunActionEX.class);
		String poi_id = getRunActionEX.getPoi_id();
		int poiId = Integer.parseInt(poi_id);
		Location poi = locationDao.selectById(poiId);
		double x = poi.getX();
		double y = poi.getY();
		double z = poi.getZ();
		double w = poi.getW();

		// TODO: DO NOT HARD CODE, USE A GENERIC WAY

		String action_goal = "{\"goal_x\":" + String.valueOf(x) + ", \"goal_y\":" + String.valueOf(y) + ", \"ori_z\":"
				+ String.valueOf(z) + ", \"ori_w\":" + String.valueOf(w) + "}";
		ActionRequest actionRequest = new ActionRequest(getRunActionEX.getAction_name(),
				getRunActionEX.getAction_goal_id(), getRunActionEX.getRobot_id(), action_goal);

		String robot_id = actionRequest.getRobot_id();
		int id = Integer.parseInt(robot_id);

		GoalId goalId = new GoalId(actionRequest.getAction_goal_id());

		Goal<String> stringGoal = new Goal<String>(goalId, actionRequest.getActionGoal());

		Action<String, String, String> genericStringAction = new Action<String, String, String>(
				actionRequest.getAction_name(), stringGoal);

		RunActionResult runActionResult = TransportManager.getInstance().runAction(id, genericStringAction);
		if (runActionResult == RunActionResult.Success) {
			return new Response(request.getSn(), "ok", 1, null);
		}

		return new Response(request.getSn(), runActionResult.toString(), 1, null);

	}

	@Autowired
	private RedisTemplate redisTemplate;

	// 从缓存Redis中查询Robot 机器人运行状态信息
	public Response queryStatus(Request request) {

		Gson gson = new Gson();
		String data = request.getData().toString();
		ModelAction modelAction = gson.fromJson(data, ModelAction.class);
		String robot_uniqueId = modelAction.getRobot_uniqueId();
		String key = "rbt_status_" + robot_uniqueId;

		String valueStatus = CacheManager.getInstance().get(key); // opsForValue.get(key);
		RobotStatus status = gson.fromJson(valueStatus, RobotStatus.class);
		List<RobotStatus> results = new ArrayList<RobotStatus>();
		results.add(status);

		if (valueStatus == null || valueStatus.length() <= 0) {
			return new Response<>(request.getSn(), "false", 1, valueStatus);
		} else {
			Response<List<RobotStatus>> response = new Response<List<RobotStatus>>(request.getSn(), "success", 1,
					results);
			return response;
		}
	}

	public List<Robot> selectRobot() {
		return robotDao.selectAll();
	}

	/***
	 * 查询机器人详细信息版本2.0<br>
	 * 返回信息包含<br>
	 * Basic<br>
	 * Detailed<br>
	 * Task<br>
	 */
	// public Response<List<RobotItem>> queryStatusEx(Request request) {

	// }

	public List<RobotItem> getRobotDetailedStatus(List<RobotItem> robotId) {
		List<RobotItem> detailedStatus = new LinkedList<RobotItem>();
		Gson gson = new Gson();
		for (RobotItem id : robotId) {

			String jsonText = CacheManager.getInstance()
					.get(StatusManager.KEY_DETAILED_STATUS_PREFIX + id.getRobot_id(), null);
			RobotDetailedStatus status = gson.fromJson(jsonText, RobotDetailedStatus.class);
			RobotItem robotItem = new RobotItem(id.getRobot_id(), status);
			detailedStatus.add(robotItem);
		}

		return detailedStatus;
	}
}
