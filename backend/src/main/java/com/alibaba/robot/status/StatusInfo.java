package com.alibaba.robot.status;

/****
 * Generic status info <br>
 * 
 * There are many different kinds of information that people are interested in<br>
 * 1. data from ros topic:<br>
 * 	1.1 data status for big screen<br>
 * 	1.2. robot status (posture, door status, other ...)<br>
 * 2. data from action feedback/result/status, status in feedback as sub task status (requirement of hema)<br>
 * 3. robot self status (disconnected, connecting, established, executing task)
 * 
 * */
public class StatusInfo<T> {

	public static final String ID = "id";

	/***
	 * robot model
	 * */
	public static final String MODEL = "model";
	
	public static final String NAME = "name";
	
	public static final String TASK_NAME = "task_name";
	
	public static final String TASK_START_NAME = "task_start_time";
	
	/***
	 * status of task, integer
	 * */
	public static final String TASK_STATUS = "task_status";
	
	
	/***
	 * status of business, integer
	 * */
	public static final String BUSINESS_STATUS = "biz_status";

	/***
	 * Task id of a running robot
	 * */
	public static final String RUNNING_TASK_ID = "running_task_id";
	
	public static final String TASK_RUN_ID = "task_id";
	
	public static final String TASK_RESULT = "task_result";
	
	public static final String TASK_PARAMS = "task_params";
	
	/**
	 * state: integer
	 * */
	public static final String STATE = "state";
	
	/**
	 * general data field
	 * */
	public static final String DATA = "data";
	
	/****
	 * robot rosbridge server address
	 * */
	public static final String ADDRESS = "address";
	
	
	
	/***
	 * Category of the status<br>
	 * e.g.<br>
	 * status: robot location, rotation, battery, status of parts, etc<br>
	 * basic: disabled, disconnected, connecting, established, running task<br>
	 * task: if basic state is running task: task name, task status, task id etc<br>
	 * */
	public StatusCategory getCategory() {
		return category;
	}

	public String getRobot_id() {
		return robot_id;
	}
	public T getRobot_data() {
		return robot_data;
	}

	public String getRobot_model() {
		return robot_model;
	}
	
	public String getName() {
		return name;
	}

	public StatusInfo(String robot_id, StatusCategory category, String robot_model, T robot_data) {
		this.robot_id = robot_id;
		this.category = category;
		this.robot_model = robot_model;
		this.robot_data = robot_data;
	}

	private StatusCategory category;
	private String name;
	private String robot_id;
	private String robot_model;
	private T robot_data;
 
}
