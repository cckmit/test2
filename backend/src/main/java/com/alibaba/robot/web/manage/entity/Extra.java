package com.alibaba.robot.web.manage.entity;

import java.sql.Date;

public class Extra {
	//最近一次放垃圾时间
	private Date lastPutTime;
	//有人、没人
	private boolean hasBody;
	
	
	
	public Extra(Date lastPutTime, boolean hasBody) {
		super();
		this.lastPutTime = lastPutTime;
		this.hasBody = hasBody;
	}
	public Extra() {
		super();
	}
	public Date getLastPutTime() {
		return lastPutTime;
	}
	public void setLastPutTime(Date lastPutTime) {
		this.lastPutTime = lastPutTime;
	}
	public boolean isHasBody() {
		return hasBody;
	}
	public void setHasBody(boolean hasBody) {
		this.hasBody = hasBody;
	}
	
}
