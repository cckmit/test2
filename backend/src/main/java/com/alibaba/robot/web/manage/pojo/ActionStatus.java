package com.alibaba.robot.web.manage.pojo;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

public class ActionStatus implements Serializable {

	private static final long serialVersionUID = -6995298780651280918L;
	public String getGoalId() {
		return goalId;
	}
	public String getResult() {
		return result;
	}
	public String getStatus() {
		return status;
	}
	private String goalId;
	private String result;
	private String status;
}
