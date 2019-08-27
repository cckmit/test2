package com.alibaba.robot.web.manage.pojo;

public class RobotItem {
	public String getRobot_id() {
		return robot_id;
	}

	public RobotDetailedStatus getRobot_data() {
		return robot_data;
	}
	
	public String getRobot_model() {
		return robot_model;
	}
	
	public RobotItem() {
		
	}

	public RobotItem(String robot_id, RobotDetailedStatus robot_data) {
		super();
		this.robot_id = robot_id;
		this.robot_data = robot_data;
	}
	
	public RobotItem(String robot_id, String robot_model, RobotDetailedStatus robot_data) {
		super();
		this.robot_id = robot_id;
		this.robot_model = robot_model;
		this.robot_data = robot_data;
	}

	private String robot_id;
	private String robot_model;
	private RobotDetailedStatus robot_data;
}
