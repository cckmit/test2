package com.alibaba.robot.transport.events;

import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class PublishEvent extends Event {

	public String getTopic() {
		return topic;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageType() {
		return messageType;
	}

	private String topic;
	private String message;
	private String messageType;

	public PublishEvent(String topic, String message, String messageType) {
		super(EventType.Publish);
		this.topic = topic;
		this.message = message;
		this.messageType = messageType;
	}
}
