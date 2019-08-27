package com.alibaba.robot.transport.events;

import com.alibaba.robot.actionlib.Action;
import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class RunActionEvent extends Event {

	public RunActionEvent(Action action) {
		super(EventType.RunAction);
		this.action = action;
	}

	public Action geAction() {
		return action;
	}

	private Action action;
}
