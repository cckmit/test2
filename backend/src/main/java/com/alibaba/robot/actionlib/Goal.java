package com.alibaba.robot.actionlib;

public class Goal<T> {
	public GoalId getGoal_id() {
		return goal_id;
	}

	/**
	public Header getHeader() {
		return header;
	}*/

	public T getGoal() {
		return goal;
	}

	 

	public Goal(GoalId goal_id, T goal) {
		super();
		this.goal_id = goal_id;
		//this.header = new Header();
		this.goal = goal;
	}
	
	public Goal(String goalId, T goal) {
		super();
		this.goal_id = new GoalId(goalId);
		//this.header = new Header();
		this.goal = goal;
	}

	private GoalId goal_id;
	//private Header header;
	private T goal;

}
