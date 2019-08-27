package com.alibaba.robot.web.manage.pojo;

public class Vector3 {
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public Vector3(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3() {
		
	}
	private double x;
	private double y;
	private double z;
}
