package com.alibaba.robot.transport.events;

import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;
import com.alibaba.robot.transport.IServiceCallback;

public class AdvertiseServiceEvent extends Event {

	public String getServiceName() {
		return serviceName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public IServiceCallback getCallback() {
		return callback;
	}

	public AdvertiseServiceEvent(String serviceName, String serviceType,
			IServiceCallback callback) {
		super(EventType.AdvertiseService);
		this.serviceName = serviceName;
		this.serviceType = serviceType;
		this.callback = callback;
	}

	private String serviceName;
	private String serviceType;
	private IServiceCallback callback;

}
