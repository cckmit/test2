
package com.alibaba.robot.status;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.robot.common.Const;
import com.alibaba.robot.common.DbUtil;
import com.alibaba.robot.transport.ITransport;
import com.alibaba.robot.transport.ITransportListener;
import com.alibaba.robot.transport.StateName;
import com.alibaba.robot.web.manage.entity.CacheManager;
import com.alibaba.robot.web.manage.pojo.Robot;
import com.alibaba.robot.web.manage.pojo.RobotDetailedStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/****
 * Common robot status manager
 */
public class StatusManager implements Const, ITransportListener {

	private static Logger LOGGER = Logger.getLogger(StatusManager.class);
	private static StatusManager INSTANCE = new StatusManager();

	public static StatusManager getInstance() {
		return INSTANCE;
	}

	private static Gson gson = new Gson();

	private StatusManager() {
		init();
	}

	// TODO: read data from database to get initial status and rbt_sets
	private void init() {

		// refresh robot lists from database
		CacheManager.getInstance().del(KEY_INFO_IDS);

		List<Robot> robots = DbUtil.getAllRobots();

		for (Robot robot : robots) {

			CacheManager.getInstance().sadd(KEY_INFO_IDS, robot.getId().toString());
			// add basic category
			CacheManager.getInstance().sadd(KEY_CATEGORY_PREFIX + robot.getId(), StatusCategory.Basic.toString());

			// and task category
			CacheManager.getInstance().sadd(KEY_CATEGORY_PREFIX + robot.getId(), StatusCategory.Task.toString());

			// add detailed category
			CacheManager.getInstance().sadd(KEY_CATEGORY_PREFIX + robot.getId(), StatusCategory.Detailed.toString());

			// add initial status of basic category
			CacheManager.getInstance().hset(KEY_STATUS_PREFIX + StatusCategory.Basic + robot.getId(), StatusInfo.ID,
					robot.getId().toString());
			CacheManager.getInstance().hset(KEY_STATUS_PREFIX + StatusCategory.Basic + robot.getId(), StatusInfo.MODEL,
					robot.getModel());
			CacheManager.getInstance().hset(KEY_STATUS_PREFIX + StatusCategory.Basic + robot.getId(), StatusInfo.NAME,
					robot.getName());
			CacheManager.getInstance().hset(KEY_STATUS_PREFIX + StatusCategory.Basic + robot.getId(), StatusInfo.STATE,
					robot.getStatus().toString());
			CacheManager.getInstance().hset(KEY_STATUS_PREFIX + StatusCategory.Basic + robot.getId(),
					StatusInfo.ADDRESS, robot.getAddress());
		}
	}

	public JsonObject getBasicInfo(String robotId) {
		JsonObject robotObjct = new JsonObject();
		Map<String, String> statusDataMap = new HashMap<String, String>();
		CacheManager.getInstance().hgetall(KEY_STATUS_PREFIX + StatusCategory.Basic + robotId, statusDataMap);
		for (Map.Entry<String, String> entry : statusDataMap.entrySet()) {
			if (StatusInfo.STATE.equals(entry.getKey()) || StatusInfo.TASK_STATUS.equals(entry.getKey())) {
				robotObjct.addProperty(entry.getKey(), Integer.parseInt(entry.getValue()));
			} else {
				robotObjct.addProperty(entry.getKey(), entry.getValue());
			}
		}
		return robotObjct;
	}

	public JsonObject getDetailedInfo(String robotId) {
		JsonObject robotObjct = new JsonObject();
		Map<String, String> statusDataMap = new HashMap<String, String>();
		CacheManager.getInstance().hgetall(KEY_STATUS_PREFIX + StatusCategory.Detailed + robotId, statusDataMap);
		for (Map.Entry<String, String> entry : statusDataMap.entrySet()) {
			robotObjct.addProperty(entry.getKey(), entry.getValue());
		}
		return robotObjct;
	}

	public JsonObject getTaskInfo(String robotId) {
		JsonObject robotObjct = new JsonObject();
		Map<String, String> statusDataMap = new HashMap<String, String>();
		CacheManager.getInstance().hgetall(KEY_STATUS_PREFIX + StatusCategory.Task + robotId, statusDataMap);
		for (Map.Entry<String, String> entry : statusDataMap.entrySet()) {
			if (StatusInfo.STATE.equals(entry.getKey()) || StatusInfo.TASK_STATUS.equals(entry.getKey())) {
				robotObjct.addProperty(entry.getKey(), Integer.parseInt(entry.getValue()));
			} else {
				robotObjct.addProperty(entry.getKey(), entry.getValue());
			}

		}
		return robotObjct;
	}

	@Override
	public void onStateChanged(ITransport transport, StateName oldStateName, StateName newStateName) {
		int id = transport.getId();
		String value = Integer.toString(newStateName.intValue());
		CacheManager.getInstance().hset(KEY_STATUS_PREFIX + StatusCategory.Basic + id, StatusInfo.STATE, value);
	}

	@Override
	public void onMessage(ITransport transport, String topic, String message) {

		if (ITransport.ROBOT_STATUS_TOPIC_NAME.equals(topic)) {

			String key = KEY_DETAILED_STATUS_PREFIX + transport.getId();
			CacheManager.getInstance().set(key, message);

			CacheManager.getInstance().hset(
					StatusManager.KEY_STATUS_PREFIX + StatusCategory.Detailed + transport.getId(), StatusInfo.DATA,
					message);

		}
	}

	public String getRobotInfo() {
		return getRobotInfo(null);
	}

	public String getRobotInfo(Set<String> interestedId) {

		Set<String> robotIdSet = new HashSet<String>();
		CacheManager.getInstance().smembers(KEY_INFO_IDS, robotIdSet);
		List<Map<String, Object>> resultList = new LinkedList<Map<String, Object>>();

		// get all categories
		for (String id : robotIdSet) {

			if (null != null && !interestedId.contains(id)) {
				continue;
			}

			Map<String, Object> robotObject = new HashMap<String, Object>();
			Set<String> categorySet = new HashSet<String>();
			CacheManager.getInstance().smembers(KEY_CATEGORY_PREFIX + id, categorySet);

			for (String category : categorySet) {
				Map<String, Object> categoryObject = new HashMap<String, Object>();

				Map<String, String> statusMap = new HashMap<String, String>();
				// get all data of each category
				CacheManager.getInstance().hgetall(KEY_STATUS_PREFIX + category + id, statusMap);

				for (Map.Entry<String, String> entry : statusMap.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					if (StatusInfo.STATE.equals(key) || StatusInfo.TASK_STATUS.equals(key)) {
						categoryObject.put(key, Integer.parseInt(value));
					} else if (StatusInfo.DATA.equals(key)) {
						RobotDetailedStatus detailedStatus = new RobotDetailedStatus();
						Gson gson = new Gson();
						detailedStatus = gson.fromJson(value, RobotDetailedStatus.class);
						categoryObject.put(key, detailedStatus);
					} else {
						categoryObject.put(key, value);
					}
				}

				robotObject.put(category, categoryObject);
			}

			resultList.add(robotObject);
		}

		return gson.toJson(resultList);
	}
}
