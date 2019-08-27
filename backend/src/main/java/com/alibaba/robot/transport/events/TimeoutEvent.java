package com.alibaba.robot.transport.events;

import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class TimeoutEvent extends Event {

	public TimeoutEvent(long timerId) {
		super(EventType.Timeout);
		this.timerId = timerId;
	}

	private long timerId;

	public long getTimerId() {
		return timerId;
	}

}
