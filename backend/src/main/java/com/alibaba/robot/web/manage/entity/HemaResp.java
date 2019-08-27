package com.alibaba.robot.web.manage.entity;

import java.util.List;

public class HemaResp {
	
	private String robotUniqueId;
	private String warehouseCode;
	//坐标
	private List<Integer> robotCoordinate;
	//任务类型
	private int taskType;
	//任务ID
	private String taskId;
	//任务状态
	private int taskState;
	//桌号
	private String poiId;
	
	private Extra extra;
	//机器人电量
	private int robotEnergy;
	//机器人负载
	private List<Integer> robotCapacity;
	
	
	
	
	public HemaResp(String robotUniqueId, String warehouseCode, List<Integer> robotCoordinate, int taskType,
			String taskId, int taskState, String poiId, Extra extra, int robotEnergy, List<Integer> robotCapacity) {
		super();
		this.robotUniqueId = robotUniqueId;
		this.warehouseCode = warehouseCode;
		this.robotCoordinate = robotCoordinate;
		this.taskType = taskType;
		this.taskId = taskId;
		this.taskState = taskState;
		this.poiId = poiId;
		this.extra = extra;
		this.robotEnergy = robotEnergy;
		this.robotCapacity = robotCapacity;
	}
	public HemaResp() {
		super();
	}
	public String getRobotUniqueId() {
		return robotUniqueId;
	}
	public void setRobotUniqueId(String robotUniqueId) {
		this.robotUniqueId = robotUniqueId;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public List<Integer> getRobotCoordinate() {
		return robotCoordinate;
	}
	public void setRobotCoordinate(List<Integer> robotCoordinate) {
		this.robotCoordinate = robotCoordinate;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public int getTaskState() {
		return taskState;
	}
	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}
	public String getPoiId() {
		return poiId;
	}
	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}
	public Extra getExtra() {
		return extra;
	}
	public void setExtra(Extra extra) {
		this.extra = extra;
	}
	public int getRobotEnergy() {
		return robotEnergy;
	}
	public void setRobotEnergy(int robotEnergy) {
		this.robotEnergy = robotEnergy;
	}
	public List<Integer> getRobotCapacity() {
		return robotCapacity;
	}
	public void setRobotCapacity(List<Integer> robotCapacity) {
		this.robotCapacity = robotCapacity;
	}
	
}
