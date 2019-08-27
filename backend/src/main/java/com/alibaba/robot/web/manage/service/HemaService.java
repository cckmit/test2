package com.alibaba.robot.web.manage.service;

import java.util.List;

import com.alibaba.robot.business.hema.RobotStatus;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;

public interface HemaService {

	Response charge(Request request);

	Response schedule(Request request);

	Response dumpDishes(Request request);
	
	Response moveTo(Request request);

	Response playAudio(Request request);

	Response addInfo(Request request);

	Response deleteInfo(Request request);

	Response emergencyHalt(Request request);

	Response recover(Request request);

	Response repair(Request request);

	Response cruiseCleanTable(Request request);

	Response callingCleanTable(Request request);
	
	Response notifyCleanTableComplete(Request request);

	Response systemConfAdd(Request request);

	Response systemConfUpdate(Request request);

	Response systemConfDelete(Request request);

	Response systemConfSelectone(Request request);

	Response systemConfSelectall(Request request);

	
	Response cancelTask(Request request);

	Response<List<RobotStatus>> getInfo(Request request);
}
