package com.alibaba.robot.web.manage.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.robot.business.hema.RobotStatus;
import com.alibaba.robot.status.StatusManager;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.service.HemaService;

@RestController
@RequestMapping("/hema")
public class HemaController {

	@Autowired
	private HemaService hemaService;
	
	private Logger LOGGER = Logger.getLogger(HemaController.class);

	// 充电调度
	@RequestMapping("/charge")
	@ResponseBody
	public Response charge(@RequestBody Request request) {
		Response response = hemaService.charge(request);
		return response;
	}

	// 收餐调度
	@RequestMapping("/schedule")
	@ResponseBody
	public Response schedule(@RequestBody Request request) {
		Response response = hemaService.schedule(request);
		return response;
	}

	// 回收调度
	@RequestMapping("/retrieve_dish")
	@ResponseBody
	public Response retrieveDish(@RequestBody Request request) {
		Response response = hemaService.dumpDishes(request);
		return response;
	}


	// 检修调度
	@RequestMapping("/repair")
	@ResponseBody
	public Response repair(@RequestBody Request request) {
		Response response = hemaService.repair(request);
		return response;
	}

	// 巡航收餐调度
	@RequestMapping("/cruise_clean_table")
	@ResponseBody
	public Response cruiseCleanTable(@RequestBody Request request) {
		Response response = hemaService.cruiseCleanTable(request);
		return response;
	}

	// 呼叫收餐调度
	@RequestMapping("/calling_clean_table")
	@ResponseBody
	public Response callingCleanTable(@RequestBody Request request) {
		Response response = hemaService.callingCleanTable(request);
		return response;
	}

	// 点对点调度
	@RequestMapping("/move_to")
	@ResponseBody
	public Response moveTo(@RequestBody Request request) {
		Response response = hemaService.moveTo(request);
		return response;
	}

	// 播放声音
	@RequestMapping("/play_audio")
	@ResponseBody
	public Response playAudio(@RequestBody Request request) {
		Response response = hemaService.playAudio(request);
		return response;
	}

	// 添加机器人
	@RequestMapping("/robot_add_info")
	@ResponseBody
	public Response addInfo(@RequestBody Request request) {
		Response response = hemaService.addInfo(request);
		return response;
	}

	// 删除机器人
	@RequestMapping("/robot_delete_info")
	@ResponseBody
	public Response deleteInfo(@RequestBody Request request) {
		Response response = hemaService.deleteInfo(request);
		return response;
	}

	// 查询机器人
	@RequestMapping("/robot_get_info")
	@ResponseBody
	public Response<List<RobotStatus>> getInfo(@RequestBody Request request) {
		Response<List<RobotStatus>> response = hemaService.getInfo(request);
		return response;
	}

	// 紧停
	
	@RequestMapping("/robot_emergency_halt")
	@ResponseBody
	public Response emergencyHalt(@RequestBody Request request) {
		Response response = hemaService.emergencyHalt(request);
		return response;
	}

	// 恢复
	@RequestMapping("/robot_recover")
	@ResponseBody
	public Response recover(@RequestBody Request request) {
		Response response = hemaService.recover(request);
		return response;
	}
	
	
	@RequestMapping("/system_conf_add")
	@ResponseBody
	public Response systemConfAdd(@RequestBody Request request) {
		Response response = hemaService.systemConfAdd(request);
		return response;
	}
	
	
	@RequestMapping("/system_conf_update")
	@ResponseBody
	public Response systemConfUpdate(@RequestBody Request request) {
		Response response = hemaService.systemConfUpdate(request);
		return response;
	}
	
	
	
	@RequestMapping("/system_conf_delete")
	@ResponseBody
	public Response systemConfDelete(@RequestBody Request request) {
		Response response = hemaService.systemConfDelete(request);
		return response;
	}
	
	
	
	@RequestMapping("/system_conf_selectone")
	@ResponseBody
	public Response systemConfSelectone(@RequestBody Request request) {
		Response response = hemaService.systemConfSelectone(request);
		return response;
	}
	
	
	
	@RequestMapping("/system_conf_selectall")
	@ResponseBody
	public Response systemConfSelectall(@RequestBody Request request) {
		Response response = hemaService.systemConfSelectall(request);
		return response;
	}
	
	// 恢复
	@RequestMapping("/cancelTask")
	@ResponseBody
	public Response cancelTask(@RequestBody Request request) {
		LOGGER.info("request-/hema/cancelTask:"+request);
		Response response = hemaService.cancelTask(request);
		LOGGER.info("response-/hema/cancelTask:"+response);
		return response;
	}
}
