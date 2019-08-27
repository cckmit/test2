package com.alibaba.robot.business.hema;

import com.alibaba.robot.web.manage.pojo.Location;

public class LocationForMoveTo extends LocationParam {
	public LocationForMoveTo(Location location, String destination) {
		super(location);
		this.destination = destination;
	}
	
	public String getDestination() {
		return destination;
	}

	private String destination;
	
}
