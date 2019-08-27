package com.alibaba.robot.web.manage.entity;

public class ModelActionGoal {
	private int id;
	private String name;
	private String status;
	
	
	
	public ModelActionGoal() {
		super();
	}
	public ModelActionGoal(int id, String name, String status) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ModelActionGoal [id=" + id + ", name=" + name + ", status=" + status + "]";
	}
	
	
}
