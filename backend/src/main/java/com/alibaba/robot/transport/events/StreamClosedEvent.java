package com.alibaba.robot.transport.events;

import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class StreamClosedEvent extends Event{

	private String sessionId;
	public StreamClosedEvent(String sessionId) {
		super(EventType.StreamClosed);
		this.sessionId = sessionId;
	}
	
	public String getSessionId() {
		return sessionId;
	}

}
