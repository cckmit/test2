package com.alibaba.robot.web.manage.entity;

public class Progress {
	private String name;
	private String describe;
	
	
	public Progress(String name, String describe) {
		super();
		this.name = name;
		this.describe = describe;
	}
	public Progress() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
}
