package com.alibaba.robot.web.manage.pojo;

import java.util.List;

public class RobotCount {
	private int id;
	//行驶总里程
	private int runDistanceTotal;
	//运送总瓶数
	private int transportTotal;
	//关联的机器人
	private String robot_uniqueId;
	
	
	
	public RobotCount(int id, int runDistanceTotal, int transportTotal, String robot_uniqueId) {
		super();
		this.id = id;
		this.runDistanceTotal = runDistanceTotal;
		this.transportTotal = transportTotal;
		this.robot_uniqueId = robot_uniqueId;
	}
	public RobotCount() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRunDistanceTotal() {
		return runDistanceTotal;
	}
	public void setRunDistanceTotal(int runDistanceTotal) {
		this.runDistanceTotal = runDistanceTotal;
	}
	public int getTransportTotal() {
		return transportTotal;
	}
	public void setTransportTotal(int transportTotal) {
		this.transportTotal = transportTotal;
	}
	public String getRobot_uniqueId() {
		return robot_uniqueId;
	}
	public void setRobot_uniqueId(String robot_uniqueId) {
		this.robot_uniqueId = robot_uniqueId;
	}
	
}
