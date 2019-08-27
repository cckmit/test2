package com.alibaba.robot.innerhandle;

import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.robot.web.manage.pojo.AttributeDefinition;

public class AttributeDefInner {
	
	private AttributeDefInnerImpl aInnerImpl = new AttributeDefInnerImpl();
	private Logger LOGGER = Logger.getLogger(AttributeDefInner.class);
	
	
	public int addAttribute(AttributeDefinition attDefinition){
		LOGGER.info("request--addAttribute: " + attDefinition);
		int result = aInnerImpl.addAttribute(attDefinition);
		LOGGER.info("result--addAttribute: " + result);
		return result;
	}
	
	public int deleteAttribute(AttributeDefinition attDefinition){
		LOGGER.info("request--deleteAttribute: " + attDefinition);
		int result = aInnerImpl.deleteAttribute(attDefinition);
		LOGGER.info("result--deleteAttribute: " + result);
		return result;
	}
	
	public int updateAttribute(AttributeDefinition attDefinition){
		LOGGER.info("request--updateAttribute: " + attDefinition);
		int result = aInnerImpl.updateAttribute(attDefinition);
		LOGGER.info("result--updateAttribute: " + result);
		return result;
	}
	
	public Object selectOneAttribute(AttributeDefinition attDefinition){
		LOGGER.info("request--selectOneAttribute: " + attDefinition);
		Object result = aInnerImpl.selectOneAttribute(attDefinition);
		LOGGER.info("result--selectOneAttribute: " + result);
		return result;
	}
	
	public Map<String, Object> selectTypeAttribute(String attribute){
		LOGGER.info("request--selectTypeAttribute: " + attribute);
		Map<String, Object> map = aInnerImpl.selectTypeAttribute(attribute);
		LOGGER.info("result--selectTypeAttribute: " + map);
		return map;
	}
	
}
