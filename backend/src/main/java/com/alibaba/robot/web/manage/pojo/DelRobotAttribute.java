package com.alibaba.robot.web.manage.pojo;

import java.util.ArrayList;

public class DelRobotAttribute {
	private String robot_uniqueId;
	private ArrayList<Attributes> attributes;
	
	public DelRobotAttribute() {
		super();
	}

	public DelRobotAttribute(String robot_uniqueId, ArrayList<Attributes> attributes) {
		super();
		this.robot_uniqueId = robot_uniqueId;
		this.attributes = attributes;
	}

	public String getRobot_uniqueId() {
		return robot_uniqueId;
	}

	public void setRobot_uniqueId(String robot_uniqueId) {
		this.robot_uniqueId = robot_uniqueId;
	}

	public ArrayList<Attributes> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Attributes> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String toString() {
		return "DelRobotAttribute [robot_uniqueId=" + robot_uniqueId + ", attributes=" + attributes + "]";
	}

}
