package com.alibaba.robot.web.manage.entity;

public class ModelAction {
	private int id;
	private String action_name;
	private String robot_uniqueId;
	private ModelActionGoal modelActionGoal;
	private String action_goal_id;
	
	
	public ModelAction(int id, String action_name, String robot_uniqueId, ModelActionGoal modelActionGoal,
			String action_goal_id) {
		super();
		this.id = id;
		this.action_name = action_name;
		this.robot_uniqueId = robot_uniqueId;
		this.modelActionGoal = modelActionGoal;
		this.action_goal_id = action_goal_id;
	}
	public ModelAction() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	public String getRobot_uniqueId() {
		return robot_uniqueId;
	}
	public void setRobot_uniqueId(String robot_uniqueId) {
		this.robot_uniqueId = robot_uniqueId;
	}
	public ModelActionGoal getModelActionGoal() {
		return modelActionGoal;
	}
	public void setModelActionGoal(ModelActionGoal modelActionGoal) {
		this.modelActionGoal = modelActionGoal;
	}
	public String getAction_goal_id() {
		return action_goal_id;
	}
	public void setAction_goal_id(String action_goal_id) {
		this.action_goal_id = action_goal_id;
	}
	
	
}
