package com.alibaba.robot.transport.events;

import com.alibaba.robot.task.ITask;
import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class RunTaskEvent extends Event {

	public RunTaskEvent(EventType eventType) {
		super(eventType);
	}
	
	public ITask getTask() {
		return task;
	}

	public RunTaskEvent(ITask task) {
		super(EventType.RunTask);
		if(task == null) {
			throw new IllegalArgumentException();
		}
		this.task = task;
	}

	private ITask task;

}
