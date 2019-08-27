package com.alibaba.robot.actionlib;

import com.alibaba.robot.task.TaskState;

public class ActionTaskState extends TaskState {

	public ActionTaskState(String name, String runId) {
		super(name, runId);
	}

	/***
	 * set business status
	 * */
	public void setStatus(int businessStatus) {
		this.status = businessStatus;
	}

	/***
	 * set task status
	 * */
	public void setTaskStatus(int taskStatus) {
		this.task_status = taskStatus;
	}
	
	public void setExtra(Object extra) {
		this.extra = extra;
	}
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public void setCompletionTime(long completionTime) {
		this.completionTime = completionTime;
	}
}
