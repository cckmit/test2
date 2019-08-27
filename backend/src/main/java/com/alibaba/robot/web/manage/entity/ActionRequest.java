package com.alibaba.robot.web.manage.entity;

public class ActionRequest {

	public String getAction_name() {
		return action_name;
	}

	public String getAction_goal_id() {
		return action_goal_id;
	}

	public String getRobot_id() {
		return robot_id;
	}

	public String getActionGoal() {
		return action_goal;
	}


	public ActionRequest(String action_name, String action_goal_id, String robot_id, String action_goal) {
		super();
		this.action_name = action_name;
		this.action_goal_id = action_goal_id;
		this.robot_id = robot_id;
		this.action_goal = action_goal;
	}

	private String action_name;
	private String action_goal_id;
	private String robot_id;
	private String action_goal;

}
