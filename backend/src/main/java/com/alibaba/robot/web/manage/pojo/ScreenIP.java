package com.alibaba.robot.web.manage.pojo;

public class ScreenIP {
	
	private Integer id;
	private String screenIP;
	private int port;
	
	
	public ScreenIP(Integer id, String screenIP, int port) {
		super();
		this.id = id;
		this.screenIP = screenIP;
		this.port = port;
	}
	public ScreenIP() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getScreenIP() {
		return screenIP;
	}
	public void setScreenIP(String screenIP) {
		this.screenIP = screenIP;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
