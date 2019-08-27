package com.alibaba.robot.web.manage.pojo;

public class ServiceStatus {
	private Integer id;
	//是否暂停服务
	private boolean out_of_service;
	
	
	public ServiceStatus(Integer id, boolean out_of_service) {
		super();
		this.id = id;
		this.out_of_service = out_of_service;
	}
	public ServiceStatus() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isOut_of_service() {
		return out_of_service;
	}
	public void setOut_of_service(boolean out_of_service) {
		this.out_of_service = out_of_service;
	}
	@Override
	public String toString() {
		return "ServiceStatus [id=" + id + ", out_of_service=" + out_of_service + "]";
	}
	
}
