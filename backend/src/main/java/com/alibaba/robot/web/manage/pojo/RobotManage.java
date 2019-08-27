package com.alibaba.robot.web.manage.pojo;

import com.alibaba.robot.web.manage.entity.Extra;

public class RobotManage {
	private Integer id;
	private String robotUniqueId;
	private String warehouseCode;
	//坐标
	private String robotCoordinate;
	//任务类型
	private Integer taskType;
	//任务ID
	private Integer taskId;
	//任务状态
	private Integer taskState;
	//机器人电量
	private Integer robotEnergy;
	//机器人负载
	private String robotCapacity;
	
	
	
	public RobotManage(Integer id, String robotUniqueId, String warehouseCode, String robotCoordinate, Integer taskType,
			Integer taskId, Integer taskState, Integer robotEnergy, String robotCapacity) {
		super();
		this.id = id;
		this.robotUniqueId = robotUniqueId;
		this.warehouseCode = warehouseCode;
		this.robotCoordinate = robotCoordinate;
		this.taskType = taskType;
		this.taskId = taskId;
		this.taskState = taskState;
		this.robotEnergy = robotEnergy;
		this.robotCapacity = robotCapacity;
	}
	public RobotManage() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getRobotCoordinate() {
		return robotCoordinate;
	}
	public void setRobotCoordinate(String robotCoordinate) {
		this.robotCoordinate = robotCoordinate;
	}
	public Integer getTaskType() {
		return taskType;
	}
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getTaskState() {
		return taskState;
	}
	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}
	public Integer getRobotEnergy() {
		return robotEnergy;
	}
	public void setRobotEnergy(Integer robotEnergy) {
		this.robotEnergy = robotEnergy;
	}
	public String getRobotCapacity() {
		return robotCapacity;
	}
	public void setRobotCapacity(String robotCapacity) {
		this.robotCapacity = robotCapacity;
	}
	
	
}
