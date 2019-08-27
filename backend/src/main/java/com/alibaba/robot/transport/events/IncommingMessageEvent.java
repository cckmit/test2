package com.alibaba.robot.transport.events;

import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class IncommingMessageEvent extends Event {
	
	public IncommingMessageEvent(String message, String topic) {
		super(EventType.IncomdingMessage);
		this.message = message;
		this.topic = topic;
	}

	public String getMessage() {
		return message;
	}
	
	public String getTopic() {
		return topic;
	}

	private String message;
	private String topic;

}
