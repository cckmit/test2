package com.alibaba.robot.transport.events;

import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class DisconnectedEvent extends Event {

	public DisconnectedEvent( ) {
		super(EventType.Disconnected);
	}

}
