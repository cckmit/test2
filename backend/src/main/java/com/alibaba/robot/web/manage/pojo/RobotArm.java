package com.alibaba.robot.web.manage.pojo;

public class RobotArm {
	private int id;
	//装载总次数
	private int loadCounts;
	//装载总瓶数
	private int loadTotal;
	
	
	public RobotArm(int id, int loadCounts, int loadTotal) {
		super();
		this.id = id;
		this.loadCounts = loadCounts;
		this.loadTotal = loadTotal;
	}
	public RobotArm() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLoadCounts() {
		return loadCounts;
	}
	public void setLoadCounts(int loadCounts) {
		this.loadCounts = loadCounts;
	}
	public int getLoadTotal() {
		return loadTotal;
	}
	public void setLoadTotal(int loadTotal) {
		this.loadTotal = loadTotal;
	}
	@Override
	public String toString() {
		return "RobotArm [id=" + id + ", loadCounts=" + loadCounts + ", loadTotal=" + loadTotal + "]";
	}
	
}
