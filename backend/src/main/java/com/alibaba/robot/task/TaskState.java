package com.alibaba.robot.task;

/***
 * Task state info
 * */
public class TaskState {
	
	/***
	 * Construct a TaskState object<br>
	 * @param name 
	 * @param runId
	 * */
	public TaskState(String name, String runId) {
		this(name, runId,  ITask.STANDBY, 0);
	}

	/***
	 * Construct a TaskState object<br>
	 * @param name 
	 * @param runId
	 * @param taskStatus status of the task: running,succeeded, cancelled, etc
	 * */
	public TaskState(String name, String runId, int taskStatus) {
		this(name, runId, taskStatus, 0);
	}
	
	/***
	 * Construct a TaskState object<br>
	 * @param name 
	 * @param runId
	 * @param taskStatus status of the task: running,succeeded, cancelled, etc
	 * @param status customized business status code
	 * */
	public TaskState(String name, String runId, int taskStatus, int status) {
		this.name = name;
		this.status = status;
		this.task_status = taskStatus;
		this.runId = runId;
	}

	public Object getExtra() {
		return extra;
	}

	public String getName() {
		return name;
	}

	/***
	 * Get business status of the task
	 * */
	public int getStatus() {
		return status;
	}
	
	/****
	 * Get status of the task<br>
	 * */
	public int getTaskStatus() {
		return this.task_status;
	}

	public String getRunId() {
		return runId;
	}

	public long getStartTime() {
		return startTime;
	}

	public int getSequence() {
		return sequence;
	}
	
	public long getCompletionTime() {
		return completionTime;
	}
	
	public boolean isCompleted() {
		return (task_status == ITask.CANCELLED  ||
				task_status == ITask.PREEMPTED ||
				task_status == ITask.SUCCEEDED ||
				task_status == ITask.TIMEOUTED);
	}
	
	protected String name;
	/**
	 * Business status
	 * */
	protected int status;
	
	/**
	 * Task status
	 * */
	protected int task_status;
	
	protected String runId;
	
	protected long startTime;
	
	protected long completionTime;
	
	protected int sequence;
	
	protected Object extra;
}
