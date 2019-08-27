package com.alibaba.robot.web.manage.service;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;

public interface AttributeDefinitionService {

	Response addAttribute(Request request);

	Response deleteAttribute(Request request);

	Response updateAttribute(Request request);

	Response selectOneAttribute(Request request);

	Response selectTypeAttribute(Request request);
	
	
	
}
