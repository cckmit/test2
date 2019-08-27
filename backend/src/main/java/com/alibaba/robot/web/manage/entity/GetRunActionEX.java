package com.alibaba.robot.web.manage.entity;

public class GetRunActionEX {
	private String poi_id;
	private String robot_id;
	private String action_name;
	private String action_goal_id;
	
	
	public GetRunActionEX(String poi_id, String robot_id, String action_name, String action_goal_id) {
		super();
		this.poi_id = poi_id;
		this.robot_id = robot_id;
		this.action_name = action_name;
		this.action_goal_id = action_goal_id;
	}
	public GetRunActionEX() {
		super();
	}
	public String getPoi_id() {
		return poi_id;
	}
	public void setPoi_id(String poi_id) {
		this.poi_id = poi_id;
	}
	public String getRobot_id() {
		return robot_id;
	}
	public void setRobot_id(String robot_id) {
		this.robot_id = robot_id;
	}
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	public String getAction_goal_id() {
		return action_goal_id;
	}
	public void setAction_goal_id(String action_goal_id) {
		this.action_goal_id = action_goal_id;
	}
	
}
