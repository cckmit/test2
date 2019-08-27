package com.alibaba.robot.web.manage.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.service.PoiService;

@Component
@RequestMapping("/poi")
public class PoiController {
	

	@Autowired
	private PoiService poiService;
	
	private Logger LOGGER = Logger.getLogger(PoiController.class);
	
	//添加地址信息
	@RequestMapping("/addLocation")
	@ResponseBody
	public Response addLocation(@RequestBody Request request){
		Response response = poiService.addLocation(request);
		return response;
	}
	
	
	//查询地址信息
	@RequestMapping("/queryLocation")
	@ResponseBody
	public Response queryLocation(@RequestBody Request request){
		Response response = poiService.queryLocation(request);
		return response;
	}
	
	
	//查询单个地址信息
	@RequestMapping("/queryById")
	@ResponseBody
	public Response queryById(@RequestBody Request request){
		Response response = poiService.queryById(request);
		return response;
	}
	
	
	//删除单个地址信息
	@RequestMapping("/deleteById")
	@ResponseBody
	public Response deleteById(@RequestBody Request request){
		Response response = poiService.deleteById(request);
		return response;
	}
	
	
	//更新修改单个地址信息
	@RequestMapping("/updateLocation")
	@ResponseBody
	public Response updateLocation(@RequestBody Request request){
		Response response = poiService.updateLocation(request);
		return response;
	}
	

}
