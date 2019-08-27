package com.alibaba.robot.web.manage.controller;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.robot.status.StatusManager;
import com.alibaba.robot.web.manage.entity.GenericRequest;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.pojo.Robot;
import com.alibaba.robot.web.manage.pojo.RobotDetailedStatus;
import com.alibaba.robot.web.manage.pojo.RobotItem;
import com.alibaba.robot.web.manage.service.RobotService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
@RequestMapping("/robot")
public class RobotController {

	@Autowired
	private RobotService robotService;
	
	private Logger LOGGER = Logger.getLogger(RobotController.class);

	// 查询机器人信息
	@RequestMapping("/get_info")
	@ResponseBody
	public Response GetInfo(@RequestBody Request request) {
		Response response = robotService.GetInfo(request);
		return response;
	}

	// 更新机器人信息
	@RequestMapping("/update_info")
	@ResponseBody
	public Response updateInfo(@RequestBody Request request) {
		Response response = robotService.updateInfo(request);
		return response;
	}

	// 添加机器人信息
	@RequestMapping("/add_info")
	@ResponseBody
	public Response addInfo(@RequestBody Request request) {
		Response response = robotService.addInfo(request);
		return response;
	}
	
	// 添加机器人信息
	@RequestMapping("/add_info_ex")
	@ResponseBody
	public Response addInfo(@RequestBody GenericRequest<Robot> request) {
		return robotService.addInfoEx(request);
	}
	

	// 删除单个机器人信息
	@RequestMapping("/delete_info")
	@ResponseBody
	public Response deleteInfo(@RequestBody Request request) {
		Response response = robotService.deleteInfo(request);
		return response;
	}

	// 执行Action
	@RequestMapping("/run_action")
	@ResponseBody
	public Response runAction(@RequestBody Request request) {
		Response response = robotService.runAction(request);
		return response;
	}

	// 查询Action执行情况
	@RequestMapping("/get_action_result")
	@ResponseBody
	public Response getActionResult(@RequestBody Request request) {
		Response response = robotService.getActionResult(request);
		return response;
	}

	// 查询所有Action 信息
	@RequestMapping("/getActionAll")
	@ResponseBody
	public Response getActionAll(@RequestBody Request request) {
		Response response = robotService.getActionAll(request);
		return response;
	}

	// 接收机器人统计数据做更新
	@RequestMapping("/updateRobotTransportData")
	@ResponseBody
	public Response updateRobotTransportData(@RequestBody Request request) {
		Response response = robotService.updateRobotTransportData(request);
		return response;
	}

	// 获取统计数据 机器人行驶路程和饮料数
	@RequestMapping("/getRobotTransportData")
	@ResponseBody
	public Response getRobotTransportData(@RequestBody Request request) {
		Response response = robotService.getRobotTransportData(request);
		return response;
	}

	// 接收机械臂统计数据做更新
	@RequestMapping("/updateRobotArmData")
	@ResponseBody
	public Response updateRobotArmData(@RequestBody Request request) {
		Response response = robotService.updateRobotArmData(request);
		return response;
	}

	// 获取统计数据 机械臂装载次数和饮料数
	@RequestMapping("/getRobotArmData")
	@ResponseBody
	public Response getRobotArmData(@RequestBody Request request) {
		Response response = robotService.getRobotArmData(request);
		return response;
	}

	// 控制机器人执行Action
	@RequestMapping("/run_action_ex")
	@ResponseBody
	public Response runActionEx(@RequestBody Request request) {
		Response response = robotService.runActionEx(request);
		return response;
	}

	// 查询机器人运行状态信息
	@RequestMapping("/query_status")
	@ResponseBody
	public Response queryStatusRedis(@RequestBody Request request) {
		Response response = robotService.queryStatus(request);
		return response;
	}

	@RequestMapping("/query_detailed_status")
	@ResponseBody
	public Response<List<RobotItem>> queryDetailedStatus(@RequestBody Request request) {
		LOGGER.info("request-/robot/query_detailed_status:" + request);

		List<RobotItem> robotItem = new LinkedList<RobotItem>();

		Gson gson = new Gson();
		String data = request.getData().toString();

		Type listType = new TypeToken<List<RobotItem>>() {
		}.getType();

		robotItem = gson.fromJson(data, listType);
		List<RobotItem> detailedStatus = robotService.getRobotDetailedStatus(robotItem);
		Response<List<RobotItem>> response = new Response<List<RobotItem>>(request.getSn(), "", 1, detailedStatus);
		LOGGER.info("response-/robot/query_detailed_status:" + response);
		return response;
	}

	// 查询机器人运行状态信息
	@RequestMapping("/query_detailed_status_ex")
	@ResponseBody
	public ResponseEntity<String> queryDetailedStatusEx(@RequestBody Request request) {
		LOGGER.info("request-/robot/query_detailed_status_ex:" + request);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json");
		String responseBody = StatusManager.getInstance().getRobotInfo();
		ResponseEntity<String> responseEntity = new ResponseEntity<String>(responseBody, responseHeaders, HttpStatus.CREATED);
		LOGGER.info("response-/robot/query_detailed_status_ex:" + responseEntity);
		return responseEntity;
	}

}
