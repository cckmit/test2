package com.alibaba.robot.web.manage.entity.futurehotel;

public class HotelRobotStatus {
	
	private String robot_uniqueId;
	private int status;
	private int task_status;
	private String task_name;
	private String task_id;
	//取货码
	private int take_code;
	
	
	
	public HotelRobotStatus(String robot_uniqueId, int status, int task_status, String task_name, String task_id,
			int take_code) {
		super();
		this.robot_uniqueId = robot_uniqueId;
		this.status = status;
		this.task_status = task_status;
		this.task_name = task_name;
		this.task_id = task_id;
		this.take_code = take_code;
	}
	public HotelRobotStatus() {
		super();
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
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public int getTake_code() {
		return take_code;
	}
	public void setTake_code(int take_code) {
		this.take_code = take_code;
	}
	@Override
	public String toString() {
		return "HotelRobotStatus [robot_uniqueId=" + robot_uniqueId + ", status=" + status + ", task_status="
				+ task_status + ", task_name=" + task_name + ", task_id=" + task_id + ", take_code=" + take_code + "]";
	}
	
}
