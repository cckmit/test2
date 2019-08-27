package com.alibaba.robot.web.manage.entity;

public class PoiDataReq {
	private int id;
	private String floor;
	private String name;
	private double x;
	private double y;
	//欧拉角
	private double euler;
	
	
	
	public PoiDataReq(int id, String floor, String name, double x, double y, double euler) {
		super();
		this.id = id;
		this.floor = floor;
		this.name = name;
		this.x = x;
		this.y = y;
		this.euler = euler;
	}
	public PoiDataReq() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getEuler() {
		return euler;
	}
	public void setEuler(double euler) {
		this.euler = euler;
	}
	
}
