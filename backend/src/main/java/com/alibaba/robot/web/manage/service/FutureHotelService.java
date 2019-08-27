package com.alibaba.robot.web.manage.service;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;

public interface FutureHotelService {

	Response queryRobotStatus(Request request);

	Response runTask(Request request);
	
	
}
