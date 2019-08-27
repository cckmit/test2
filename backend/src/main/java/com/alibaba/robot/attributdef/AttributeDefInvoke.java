package com.alibaba.robot.attributdef;

import org.apache.log4j.Logger;

import com.alibaba.robot.common.ErrorCode;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.service.AttributeDefinitionService;
import com.alibaba.robot.web.manage.service.Impl.AttributeDefinitionServiceImpl;
import com.google.gson.Gson;

public class AttributeDefInvoke {
	
	private static Gson gson = new Gson();
	private AttributeDefinitionService aService = new AttributeDefinitionServiceImpl();
	private Logger LOGGER = Logger.getLogger(AttributeDefInvoke.class);
	
	static String ADD = "add_attribute";
	static String DELETE = "delete_attribute";
	static String UPDATE = "update_attribute";
	static String SELECTONE = "select_one_attribute";
	static String SELECTBYTYPE = "select_type_attribute";
	
	public Response attributeDef(String requestStr){
		Request request = pack(requestStr);
		String command = request.getCommand();
		LOGGER.info("request-"+ command + ": " +request);
		if(ADD.equals(command)){
			Response addAttribute = aService.addAttribute(request);
			LOGGER.info("response-" + command + ": " + addAttribute);
			return addAttribute;
		}
		if(DELETE.equals(command)){
			Response deleteAttribute = aService.deleteAttribute(request);
			LOGGER.info("response-" + command + ": " + deleteAttribute);
			return deleteAttribute;
		}
		if(UPDATE.equals(command)){
			Response updateAttribute = aService.updateAttribute(request);
			LOGGER.info("response-" + command + ": " + updateAttribute);
			return updateAttribute;
		}
		if(SELECTONE.equals(command)){
			Response selectOneAttribute = aService.selectOneAttribute(request);
			LOGGER.info("response-" + command + ": " + selectOneAttribute);
			return selectOneAttribute;
		}
		if(SELECTBYTYPE.equals(command)){
			Response selectTypeAttribute = aService.selectTypeAttribute(request);
			LOGGER.info("response-" + command + ": " + selectTypeAttribute);
			return selectTypeAttribute;
		}
		return new Response<>(request.getSn(), "command is not supported", ErrorCode.FAILED, null);
	}
	
	
	
	private Request pack(String requestStr){
		Request request = gson.fromJson(requestStr, Request.class);
		return request;
	}
	
}
