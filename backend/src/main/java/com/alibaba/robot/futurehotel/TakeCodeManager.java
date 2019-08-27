package com.alibaba.robot.futurehotel;

import org.apache.log4j.Logger;

import com.alibaba.robot.common.Const;
import com.alibaba.robot.web.manage.entity.CacheManager;

public class TakeCodeManager implements Const {
	
	private static Logger LOGGER = Logger.getLogger(TakeCodeManager.class);
	private static TakeCodeManager INSTANCE = new TakeCodeManager();
	
	public static TakeCodeManager getInstance(){
		return INSTANCE;
	}
	
	
	public boolean setTakeCode(String robotUniqueId, int value){
		LOGGER.info("set_take_code: uniqueId-" + robotUniqueId + ", takeCode-" + value);
		boolean set = false;
		if(robotUniqueId != null && robotUniqueId.length() > 0){
			set = CacheManager.getInstance().set(TAKE_CODE_REDIS + robotUniqueId, value);
		}
		return set;
	}
	
	public int getTakeCodeFromRedis(String robotUniqueId){
		LOGGER.info("get_take_code: uniqueId-" + robotUniqueId);
		int value = 0;
		/**
		 * 若缓存中value 不存在，  则返回默认值 0
		 */
		if(robotUniqueId != null && robotUniqueId.length() > 0){
			value = CacheManager.getInstance().getInt(TAKE_CODE_REDIS + robotUniqueId, value);
		}
		LOGGER.info("get_take_code: uniqueId-" + robotUniqueId + ", takeCode-" +value);
		return value;
	}
	
	public boolean deleteTakeCode(String robotUniqueId){
		LOGGER.info("delete_take_code: uniqueId-" + robotUniqueId);
		boolean delete = false;
		if(robotUniqueId != null && robotUniqueId.length() > 0){
			Long del = CacheManager.getInstance().del(TAKE_CODE_REDIS + robotUniqueId);
			if(del > 0 ){
				delete = true;
			}
			LOGGER.info("delete_take_code: uniqueId-" + robotUniqueId + ", del-" +del);
		}
		return delete;
	}
	
}
