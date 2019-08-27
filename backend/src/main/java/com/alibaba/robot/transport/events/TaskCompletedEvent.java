package com.alibaba.robot.transport.events;

import com.alibaba.robot.task.ITask;
import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class TaskCompletedEvent extends Event{

	public ITask getTask() {
		return task;
	}

	public TaskCompletedEvent(ITask task) {
		super(EventType.TaskCompleted);
		if(task == null) {
			throw new IllegalArgumentException();
		}
		this.task = task;
	}

	private ITask task;
}
