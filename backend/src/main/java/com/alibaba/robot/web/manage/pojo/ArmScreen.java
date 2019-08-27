package com.alibaba.robot.web.manage.pojo;

public class ArmScreen {
	
	private Integer id;
	private int armId;
	private int robotId;
	private int times;
	private int total;
	private int timesHistory;
	private int totalHistory;
	
	
	
	public ArmScreen(Integer id, int armId, int robotId, int times, int total, int timesHistory, int totalHistory) {
		super();
		this.id = id;
		this.armId = armId;
		this.robotId = robotId;
		this.times = times;
		this.total = total;
		this.timesHistory = timesHistory;
		this.totalHistory = totalHistory;
	}
	public ArmScreen() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getArmId() {
		return armId;
	}
	public void setArmId(int armId) {
		this.armId = armId;
	}
	public int getRobotId() {
		return robotId;
	}
	public void setRobotId(int robotId) {
		this.robotId = robotId;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTimesHistory() {
		return timesHistory;
	}
	public void setTimesHistory(int timesHistory) {
		this.timesHistory = timesHistory;
	}
	public int getTotalHistory() {
		return totalHistory;
	}
	public void setTotalHistory(int totalHistory) {
		this.totalHistory = totalHistory;
	}
	
}
