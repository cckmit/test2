package com.alibaba.robot.web.manage.service;

import com.alibaba.robot.web.manage.pojo.ActionStatus;

public interface ActionStatusService {
	ActionStatus getStatus(ActionStatus status);
	String getString(String key);
}
