package com.alibaba.robot.web.manage.pojo;


public class RobotDetailedStatus {
	
	public static final int ROBOT_STATUS_CHARGING = 3;
	
	public int getRobot_running_status() {
		return robot_running_status;
	}
	public int getRobot_battery_status() {
		return robot_battery_status;
	}
	public double getRobot_setup_odom() {
		return robot_setup_odom;
	}
	public double getRobot_history_odom() {
		return robot_history_odom;
	}
	public int getRobot_doorA_status() {
		return robot_doorA_status;
	}
	public int getRobot_doorB_status() {
		return robot_doorB_status;
	}
	public int getRobot_doorC_status() {
		return robot_doorC_status;
	}
	public int getRobot_doorD_status() {
		return robot_doorD_status;
	}
 
	
	public RobotDetailedStatus() {
		
	}
	
	public RobotDetailedStatus(int robot_running_status, int robot_battery_status, double robot_setup_odom,
			double robot_history_odom, int robot_doorA_status, int robot_doorB_status, int robot_doorC_status,
			int robot_doorD_status ) {
		super();
		this.robot_running_status = robot_running_status;
		this.robot_battery_status = robot_battery_status;
		this.robot_setup_odom = robot_setup_odom;
		this.robot_history_odom = robot_history_odom;
		this.robot_doorA_status = robot_doorA_status;
		this.robot_doorB_status = robot_doorB_status;
		this.robot_doorC_status = robot_doorC_status;
		this.robot_doorD_status = robot_doorD_status;
 
	}
	


	
	public int getRobot_doorA_capacity() {
		return robot_doorA_capacity;
	}
	public int getRobot_doorB_capacity() {
		return robot_doorB_capacity;
	}
	public int getRobot_doorC_capacity() {
		return robot_doorC_capacity;
	}
	public int getRobot_doorD_capacity() {
		return robot_doorD_capacity;
	}



	public Position getRobot_cur_position() {
		return robot_cur_position;
	}


	private Position robot_cur_position;
	
	private int robot_running_status;
	private int robot_battery_status;
	private double robot_setup_odom;
	private double robot_history_odom;
	private int robot_doorA_status;
	private int robot_doorB_status;
	private int robot_doorC_status;
	private int robot_doorD_status;
	private int robot_doorA_capacity;
	private int robot_doorB_capacity;
	private int robot_doorC_capacity;
	private int robot_doorD_capacity;
	public int getRobot_battery_running_status() {
		return robot_battery_running_status;
	}


	private int robot_battery_running_status;
 
}
