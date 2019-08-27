package com.alibaba.robot.web.manage.pojo;

public class Quaternion {
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public double getW() {
		return w;
	}
	public Quaternion(double x, double y, double z, double w) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion() {
		
	}
	 
	private double x;
	private double y;
	private double z;
	private double w;
}
