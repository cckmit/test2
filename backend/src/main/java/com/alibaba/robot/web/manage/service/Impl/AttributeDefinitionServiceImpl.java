package com.alibaba.robot.web.manage.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.robot.attributdef.AttributeDefinitionModel;
import com.alibaba.robot.common.ErrorCode;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.pojo.AttributeDefinition;
import com.alibaba.robot.web.manage.service.AttributeDefinitionService;
import com.google.gson.Gson;

@Service
public class AttributeDefinitionServiceImpl implements AttributeDefinitionService {
	
	private static Gson gson = new Gson();
	
	private AttributeDefinitionModel aDefinitionModel = new AttributeDefinitionModel();
	
	//添加属性
	public Response addAttribute(Request request) {
		String data = request.getData().toString();
		AttributeDefinition attributeDefinition = gson.fromJson(data, AttributeDefinition.class);
		int addAttribute = aDefinitionModel.addAttribute(attributeDefinition);
		if(addAttribute == 1){
			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, null);
		}
		return new Response<>(request.getSn(), "failed", ErrorCode.FAILED, null);
	}

	//删除属性
	public Response deleteAttribute(Request request) {
		String data = request.getData().toString();
		AttributeDefinition attributeDefinition = gson.fromJson(data, AttributeDefinition.class);
		int deleteAttribute = aDefinitionModel.deleteAttribute(attributeDefinition);
		if(deleteAttribute == 1){
			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, null);
		}
		return new Response<>(request.getSn(), "failed", ErrorCode.FAILED, null);
	}

	//修改属性值
	public Response updateAttribute(Request request) {
		String data = request.getData().toString();
		AttributeDefinition attributeDefinition = gson.fromJson(data, AttributeDefinition.class);
		int updateAttribute = aDefinitionModel.updateAttribute(attributeDefinition);
		if(updateAttribute == 1){
			return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, null);
		}
		return new Response<>(request.getSn(), "failed", ErrorCode.FAILED, null);
	}

	//查询单个属性值
	public Response selectOneAttribute(Request request) {
		String data = request.getData().toString();
		AttributeDefinition attributeDefinition = gson.fromJson(data, AttributeDefinition.class);
		Map<String, Object> map = aDefinitionModel.selectOneAttribute(attributeDefinition);
		return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, map);
	}

	//只查询属性名称相关的属性值
	public Response selectTypeAttribute(Request request) {
		String data = request.getData().toString();
		AttributeDefinition attributeDefinition = gson.fromJson(data, AttributeDefinition.class);
		Map<String, Object> map = aDefinitionModel.selectTypeAttribute(attributeDefinition);
		return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, map);
	}
	
	
	
}
