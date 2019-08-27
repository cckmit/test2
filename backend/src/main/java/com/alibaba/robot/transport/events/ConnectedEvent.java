package com.alibaba.robot.transport.events;

import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

public class ConnectedEvent extends Event {
	public ConnectedEvent( ) {
		super(EventType.Connected);
	}
}
