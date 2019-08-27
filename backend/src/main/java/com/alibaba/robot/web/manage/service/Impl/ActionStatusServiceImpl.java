package com.alibaba.robot.web.manage.service.Impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.robot.web.manage.pojo.ActionStatus;
import com.alibaba.robot.web.manage.service.ActionStatusService;

@Service("actionStatusService")
public class ActionStatusServiceImpl implements ActionStatusService {

	@Override
	@Cacheable(value="default", key="#status.goalId",  unless="#result==null")
	public ActionStatus getStatus(ActionStatus status) {
		return status;
	}
	
	@Override
	@Cacheable("test")
	public String getString(String key) {
		return key;
	}
}
