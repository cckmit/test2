package com.alibaba.robot.web.manage.pojo;

public class Location {
	private Integer id;
	private String floor;
	private String name;
	private double w;
	private double x;
	private double y;
	private double z;
	
	
	public Location(Integer id, String floor, String name, double w, double x, double y, double z) {
		super();
		this.id = id;
		this.floor = floor;
		this.name = name;
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Location() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public double getW() {
		return w;
	}
	public void setW(double w) {
		this.w = w;
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
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	@Override
	public String toString() {
		return "Location [id=" + id + ", floor=" + floor + ", name=" + name + ", w=" + w + ", x=" + x + ", y=" + y
				+ ", z=" + z + "]";
	}
	

}
