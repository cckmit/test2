package com.alibaba.robot.innerhandle;

import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.robot.business.hema.SystemConfiguration;
import com.google.gson.Gson;


public class SystemConfInter {
	
	private SystemConfInterImpl systemConfInterImpl = new SystemConfInterImpl();
	private Logger LOGGER = Logger.getLogger(SystemConfInter.class);
	
	public int systemConfAdd(SystemConfiguration configuration) {
		LOGGER.info("request--systemConfAdd: " + configuration);
		int result = systemConfInterImpl.systemConfAdd(configuration);
		LOGGER.info("result--systemConfAdd: " + result);
		return result;
	}
	
	public int systemConfUpdate(SystemConfiguration configuration){
		LOGGER.info("request--systemConfUpdate: " + configuration);
		int result = systemConfInterImpl.systemConfUpdate(configuration);
		LOGGER.info("result--systemConfUpdate: " + result);
		return result;
	}
	
	public int systemConfDelete(String conf_name){
		LOGGER.info("request--systemConfDelete: " + conf_name);
		int result = systemConfInterImpl.systemConfDelete(conf_name);
		LOGGER.info("result--systemConfDelete: " + result);
		return result;
	}
	
	public Object systemConfSelectOne(String conf_name){
		LOGGER.info("request--systemConfSelectOne: " + conf_name);
		Object result = systemConfInterImpl.systemConfSelectOne(conf_name);
		LOGGER.info("result--systemConfSelectOne: " + result);
		return result;
	}
	
	public Map<String, Object> systemConfSelectAll(){
		Map<String, Object> confAll = systemConfInterImpl.systemConfSelectAll();
		LOGGER.info("result--systemConfSelectAll: " + confAll);
		return confAll;
	}
	
}
