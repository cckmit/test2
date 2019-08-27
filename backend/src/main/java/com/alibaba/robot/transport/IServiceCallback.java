package com.alibaba.robot.transport;

import edu.wpi.rail.jrosbridge.Service;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;

/***
 * Service callback<br>
 * e.g. ROS service callback
 * */
public interface IServiceCallback {
	/***
	 * Handle service call
	 * */
	void onRequest(Service service, ServiceRequest serviceRequest);
}
