package com.alibaba.robot.status;

/***
* Category of a robot status info<br>
* status: robot location, rotation, battery, status of parts, etc<br>
* basic: disabled, disconnected, connecting, established, running task<br>
* task: if basic state is running task: task name, task status, task id etc<br>
* */
public enum StatusCategory {
	/**
	 * robot location, rotation, battery, status of parts, etc
	 * **/
	Detailed,
	
	/**
	 * disabled, disconnected, connecting, established, running task
	 * */
	Basic,
	
	/**
	 * if basic state is running task: task name, task status, task id etc
	 * */
	Task,
		
	
	/***
	 * Customized data
	 * */
	Custom
}
