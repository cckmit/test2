package com.alibaba.robot.web.manage.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.service.AttributeDefinitionService;

@RestController
@RequestMapping("/attribute")
public class AttributeDefinitionController {
	
	@Autowired
	private AttributeDefinitionService attributeDefinitionService;
	
	private Logger LOGGER = Logger.getLogger(AttributeDefinitionController.class);
	
	
	//添加属性
	@RequestMapping("/add_attribute")
	@ResponseBody
	public Response addAttribute(@RequestBody Request request){
		Response response = attributeDefinitionService.addAttribute(request);
		return response;
	}
	
	
	//删除属性
	@RequestMapping("/delete_attribute")
	@ResponseBody
	public Response deleteAttribute(@RequestBody Request request){
		Response response = attributeDefinitionService.deleteAttribute(request);
		return response;
	}
	
	
	//修改属性值
	@RequestMapping("/update_attribute")
	@ResponseBody
	public Response updateAttribute(@RequestBody Request request){
		Response response = attributeDefinitionService.updateAttribute(request);
		return response;
	}
	
	
	//查询单个属性值
	@RequestMapping("/select_one_attribute")
	@ResponseBody
	public Response selectOneAttribute(@RequestBody Request request){
		Response response = attributeDefinitionService.selectOneAttribute(request);
		return response;
	}
	
	
	
	//只查询属性名称相关的属性值
	@RequestMapping("/select_type_attribute")
	@ResponseBody
	public Response selectTypeAttribute(@RequestBody Request request){
		Response response = attributeDefinitionService.selectTypeAttribute(request);
		return response;
	}
}
