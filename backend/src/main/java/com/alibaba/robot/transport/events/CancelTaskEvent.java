package com.alibaba.robot.transport.events;

import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class CancelTaskEvent extends Event {

	public String getTaskId() {
		return taskId;
	}

	private String taskId;

	public CancelTaskEvent(String taskId) {
		super(EventType.CancelTask);
		this.taskId = taskId;
	}
}
