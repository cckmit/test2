package com.alibaba.robot.transport;

public class Event {
	
	public Event(EventType eventType) {
		super();
		this.eventType = eventType;
	}

	public EventType getEventType() {
		return eventType;
	}

	private EventType eventType;
	 
}
