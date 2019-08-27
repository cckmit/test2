package com.alibaba.robot.task;

import java.util.HashMap;

import edu.wpi.rail.jrosbridge.Ros;

public class RosContext {
	public RosContext(Ros ros, HashMap<String, String> topicMessageTypeMap) {
		super();
		this.ros = ros;
		this.topicMessageTypeMap = topicMessageTypeMap;
	}

	public Ros getRos() {
		return ros;
	}

	public void setRos(Ros ros) {
		this.ros = ros;
	}

	public HashMap<String, String> getTopicMessageTypeMap() {
		return topicMessageTypeMap;
	}

	private Ros ros;
	private HashMap<String, String> topicMessageTypeMap;
}
