package com.alibaba.robot.web.manage.entity;

public class RobotStatusResp {
	public String getId() {
		return id;
	}
	public int getStatus() {
		return status;
	}
	public RobotStatusResp(String id, int status) {
		super();
		this.id = id;
		this.status = status;
	}
	private String id;
	private int status;
}
