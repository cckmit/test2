package com.alibaba.robot.web.manage.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.service.FutureHotelService;

@RestController
@RequestMapping("/futurehotel")
public class FutureHotelController {
	
	@Autowired
	private FutureHotelService futureHotelService;
	
	private static Logger LOGGER = Logger.getLogger(FutureHotelController.class);
	
	
	//查询所有机器人状态信息
	@RequestMapping("/query_robot_status")
	public Response queryRobotStatus(@RequestBody Request request){
		LOGGER.info("/futurehotel/query_robot_status--request: " + request);
		Response response = futureHotelService.queryRobotStatus(request);
		LOGGER.info("/futurehotel/query_robot_status--response: " + response);
		return response;
	}
	
	//控制机器人执行任务
	@RequestMapping("/run_task")
	public Response runTask(@RequestBody Request request){
		LOGGER.info("/futurehotel/run_task--request: " + request);
		Response response = futureHotelService.runTask(request);
		LOGGER.info("/futurehotel/run_task--response: " + response);
		return response;
	}
	
}
