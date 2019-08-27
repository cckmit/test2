package com.alibaba.robot.futurehotel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import java.util.Set;

import com.alibaba.robot.status.StatusCategory;
import com.alibaba.robot.status.StatusInfo;
import com.alibaba.robot.status.StatusManager;
import com.alibaba.robot.web.manage.entity.CacheManager;
import com.alibaba.robot.web.manage.entity.futurehotel.HotelRobotStatus;

public class HotelRobotMonitor {
	
	private static final Logger LOGGER = Logger.getLogger(HotelRobotMonitor.class);
	private static HotelRobotMonitor INSTANCE = new HotelRobotMonitor();
	
	public static HotelRobotMonitor getInstance(){
		return INSTANCE;
	}
	
	
	
	public List<HotelRobotStatus> getRobotInfoForQuery(){
		List<HotelRobotStatus> statusList = new LinkedList<HotelRobotStatus>();
		Set<String> robotUniqueIdSet = new HashSet<String>();
		CacheManager.getInstance().smembers(StatusManager.KEY_INFO_IDS, robotUniqueIdSet);
		for (String robotUniqueId : robotUniqueIdSet) {
			
			HotelRobotStatus hotelRobotStatus = getRobotStatusByUniqueId(robotUniqueId);
			statusList.add(hotelRobotStatus);
		}
		
		
		return statusList;
	}
	
	public HotelRobotStatus getRobotStatusByUniqueId(String robotUniqueId){
		HotelRobotStatus hotelRobotStatus = new HotelRobotStatus();
		Set<String> categorySet = new HashSet<String>();
		CacheManager.getInstance().smembers(StatusManager.KEY_CATEGORY_PREFIX + robotUniqueId, categorySet);
		
		for (String category : categorySet) {
			
			Map<String, String> statusMap = new HashMap<String, String>();
			CacheManager.getInstance().hgetall(StatusManager.KEY_STATUS_PREFIX + category + robotUniqueId, statusMap);
			Set<Entry<String,String>> entrySet = statusMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String key = entry.getKey();
				String value = entry.getValue();
				if(StatusCategory.Basic.toString().equals(category)){
					if(StatusInfo.ID.equals(key)){
						hotelRobotStatus.setRobot_uniqueId(value);
					} else if(StatusInfo.STATE.equals(key)){
						LOGGER.info("STATE: " + value);
						hotelRobotStatus.setStatus(Integer.parseInt(value));
					}
					
				} else if(StatusCategory.Task.toString().equals(category)){
					if(StatusInfo.TASK_RUN_ID.equals(key)){
						hotelRobotStatus.setTask_id(value);
					} else if(StatusInfo.TASK_STATUS.equals(key)){
						hotelRobotStatus.setTask_status((int) Double.parseDouble(value));
					} else if(StatusInfo.TASK_NAME.equals(key)){
						hotelRobotStatus.setTask_name(value);
					}
				}
				
			}
			
		}
		
		return hotelRobotStatus;
	}
	
}
