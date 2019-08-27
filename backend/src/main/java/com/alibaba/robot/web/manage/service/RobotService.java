package com.alibaba.robot.web.manage.service;

import java.util.List;

import com.alibaba.robot.web.manage.entity.GenericRequest;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.pojo.Robot;
import com.alibaba.robot.web.manage.pojo.RobotDetailedStatus;
import com.alibaba.robot.web.manage.pojo.RobotItem;

public interface RobotService {
	
	public Response GetInfo(Request request);
	
	public Response updateInfo(Request request);
	
	public Response addInfo(Request request);
	
	public Response addInfoEx(GenericRequest<Robot> request);
	
	
	public Response deleteInfo(Request request);
	
	public Response runAction(Request request);
	
	public Response getActionResult(Request request);
	
	public Response getActionAll(Request request);
	
	public Response updateRobotTransportData(Request request);
	
	public Response getRobotTransportData(Request request);
	
	public Response updateRobotArmData(Request request);
	
	public Response getRobotArmData(Request request);
	
	public Response runActionEx(Request request);
	
	public Response queryStatus(Request request);

	public List<Robot> selectRobot();
	
	List<RobotItem> getRobotDetailedStatus(List<RobotItem> robotId);

	/***
	 * 查询机器人详细信息版本2.0<br>
	 * 返回信息包含<br>
	 * Basic<br>
	 * Detailed<br>
	 * Task<br>
	 * */
	// Response<List<RobotItem>> queryStatusEx(Request request);
}
