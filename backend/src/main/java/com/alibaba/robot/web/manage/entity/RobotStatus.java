package com.alibaba.robot.web.manage.entity;

import com.google.gson.annotations.SerializedName;

public class RobotStatus {
	/***
	 * overall state<br>
	 * OFFLINE,
	 * ONLINE,
	 * EXECUTING_TASK
	 * */
	public RobotStatus(String robot_uniqueId, int status) {
		super();
		this.robot_uniqueId = robot_uniqueId;
		this.status = status;
	}
	
	public RobotStatus(String robot_uniqueId, int status, int task_status, String task_name) {
		super();
		this.robot_uniqueId = robot_uniqueId;
		this.status = status;
		this.task_status = task_status;
		this.task_name = task_name;
	}
	
	public void setTaskStatus(int taskStatus) {
		this.task_status = taskStatus;
	}

	public String getRobot_uniqueId() {
		return robot_uniqueId;
	}

	public void setRobot_uniqueId(String robot_uniqueId) {
		this.robot_uniqueId = robot_uniqueId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTask_status() {
		return task_status;
	}

	public void setTask_status(int task_status) {
		this.task_status = task_status;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	@SerializedName("robot_uniqueId")
	private String robot_uniqueId;
	
	@SerializedName("status")
	private int status;
	
	@SerializedName("task_status")
	private int task_status;
	
	@SerializedName("task_name")
	private String task_name;
	
	@SerializedName("run_id")
	private String run_id;
}
