package com.alibaba.robot.web.manage.entity;

public class ScreenMessage {
	private int robot_id;
	private int arm_id;
	private int status;
	private float angle;
	private float odom;
	private float y;
	private float x;
	private int total;
	private int device;
	private int operation_times;
	
	
	
	
	public ScreenMessage(int robot_id, int arm_id, int status, float angle, float odom, float y, float x, int total,
			int device, int operation_times) {
		super();
		this.robot_id = robot_id;
		this.arm_id = arm_id;
		this.status = status;
		this.angle = angle;
		this.odom = odom;
		this.y = y;
		this.x = x;
		this.total = total;
		this.device = device;
		this.operation_times = operation_times;
	}
	public ScreenMessage() {
		super();
	}
	public int getRobot_id() {
		return robot_id;
	}
	public void setRobot_id(int robot_id) {
		this.robot_id = robot_id;
	}
	public int getArm_id() {
		return arm_id;
	}
	public void setArm_id(int arm_id) {
		this.arm_id = arm_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	public float getOdom() {
		return odom;
	}
	public void setOdom(float odom) {
		this.odom = odom;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getDevice() {
		return device;
	}
	public void setDevice(int device) {
		this.device = device;
	}
	public int getOperation_times() {
		return operation_times;
	}
	public void setOperation_times(int operation_times) {
		this.operation_times = operation_times;
	}
	@Override
	public String toString() {
		return "ScreenMessage [robot_id=" + robot_id + ", arm_id=" + arm_id + ", status=" + status + ", angle=" + angle
				+ ", odom=" + odom + ", y=" + y + ", x=" + x + ", total=" + total + ", device=" + device
				+ ", operation_times=" + operation_times + "]";
	}
	
}
