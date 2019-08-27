package com.alibaba.robot.web.manage.pojo;

public class RobotScreen {
	
	private Integer id;
	private int robotId;
	private int total;
	private int totalHistory;
	
	
	
	public RobotScreen(Integer id, int robotId, int total, int totalHistory) {
		super();
		this.id = id;
		this.robotId = robotId;
		this.total = total;
		this.totalHistory = totalHistory;
	}
	public RobotScreen() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getRobotId() {
		return robotId;
	}
	public void setRobotId(int robotId) {
		this.robotId = robotId;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotalHistory() {
		return totalHistory;
	}
	public void setTotalHistory(int totalHistory) {
		this.totalHistory = totalHistory;
	}
	
}
