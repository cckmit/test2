package com.alibaba.robot.web.manage.service;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;

public interface PoiService {
	
	public Response addLocation(Request request);
	
	public Response queryLocation(Request request);
	
	public Response queryById(Request request);
	
	public Response deleteById(Request request);
	
	public Response updateLocation(Request request);
	
}
