package com.alibaba.robot.mqtt;

import org.springframework.stereotype.Component;

import com.alibaba.robot.common.ErrorCode;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.service.HemaService;
import com.alibaba.robot.web.manage.service.Impl.HemaServiceImpl;
import com.google.gson.Gson;

@Component
public class DataTransform {

	private HemaService hemaService = new HemaServiceImpl();

	static String CHARGE = "charge";
	static String RETRIEVEDISH = "retrieve_dish";
	static String REPAIR = "repair";
	static String CRUISECLEANTABLE = "cruise_clean_table";
	static String CALLINGCLEANTABLE = "calling_clean_table";
	static String MOVETO = "move_to";
	static String PLAYAUDIO = "play_audio";
	static String ADDINFO = "robot_add_info";
	static String DELETEINFO = "robot_delete_info";
	static String GETINFO = "robot_get_info";
	static String EMERGENCYHALT = "robot_emergency_halt";
	static String RECOVER = "robot_recover";
	static String SYSCONFADD = "system_conf_add";
	static String SYSCONFUPDATE = "system_conf_update";
	static String SYSCONFDELETE = "system_conf_delete";
	static String SYSCONFSELECTONE = "system_conf_selectone";
	static String SYSCONFSELECTALL = "system_conf_selectall";
	final static String CANCEL_TASK = "cancel_task";

	final static String NOTIFY_CLEAN_COMPLETE = "debug_clean_table_complete";

	private static Gson gson = new Gson();

	public Response call(String commondType, Request request) {
		if (CHARGE.equals(commondType)) {
			return hemaService.charge(request);
		}
		if (RETRIEVEDISH.equals(commondType)) {
			return hemaService.dumpDishes(request);
		}
		if (REPAIR.equals(commondType)) {
			return hemaService.repair(request);
		}
		if (CRUISECLEANTABLE.equals(commondType)) {
			return hemaService.cruiseCleanTable(request);
		}
		if (CALLINGCLEANTABLE.equals(commondType)) {
			return hemaService.schedule(request);
		}
		if (MOVETO.equals(commondType)) {
			return hemaService.moveTo(request);
		}
		if (PLAYAUDIO.equals(commondType)) {
			return hemaService.playAudio(request);
		}
		if (ADDINFO.equals(commondType)) {
			return hemaService.addInfo(request);
		}
		if (DELETEINFO.equals(commondType)) {
			return hemaService.deleteInfo(request);
		}
		if (GETINFO.equals(commondType)) {
			return hemaService.getInfo(request);
		}
		if (EMERGENCYHALT.equals(commondType)) {
			return hemaService.emergencyHalt(request);
		}
		if (RECOVER.equals(commondType)) {
			return hemaService.recover(request);
		}
		if (NOTIFY_CLEAN_COMPLETE.equals(commondType)) {
			return hemaService.notifyCleanTableComplete(request);
		}
		//系统配置项添加
		if(SYSCONFADD.equals(commondType)){
			Response systemConfAdd = hemaService.systemConfAdd(request);
			return systemConfAdd;
		}
		//系统配置项 值修改
		if(SYSCONFUPDATE.equals(commondType)){
			Response systemConfUpdate = hemaService.systemConfUpdate(request);
			return systemConfUpdate;
		}
		//系统配置项删除
		if(SYSCONFDELETE.equals(commondType)){
			Response systemConfDelete = hemaService.systemConfDelete(request);
			return systemConfDelete;
		}
		//单个系统配置项 值查询
		if(SYSCONFSELECTONE.equals(commondType)){
			Response systemConfSelectone = hemaService.systemConfSelectone(request);
			return systemConfSelectone;
		}
		//所有系统配置项查询
		if(SYSCONFSELECTALL.equals(commondType)){
			Response systemConfSelectall = hemaService.systemConfSelectall(request);
			return systemConfSelectall;
		}
		
		if (CANCEL_TASK.equals(commondType)) {
			return hemaService.cancelTask(request);
		}

		return new Response(request.getSn(), "NOT_SUPPORTED_OPERATION", ErrorCode.NOT_SUPPORTED_OPERATION);
	}

	public Request pack(String requestJsonString) {
		return gson.fromJson(requestJsonString, Request.class);
	}
}