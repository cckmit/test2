package com.alibaba.robot.task;

import com.alibaba.robot.actionlib.Status;
import com.alibaba.robot.transport.ITransport;

/****
 * General task
 * */
public interface ITask {
	
	/***
	 * Task is created, wait to be scheduled and run
	 * */
	public static final int STANDBY = 0;
	
	/***
	 * Task is running actively, value = 1
	 * */
	public static final int RUNNING = Status.ACTIVE;
	
	/***
	 * Task has completed, and succeeded, value = 3
	 * */
	public static final int SUCCEEDED = Status.SUCCEEDED;
	
	/***
	 * Task has been cancelled by user, value = 8
	 * */
	public static final int CANCELLED = Status.RECALLED;
	
	/***
	 * Task has aborted, because some prerequisites are not satisfied, value = 4
	 * */
	public static final int ABORTED = Status.ABORTED;
	
	/***
	 * Task has been running for too long, exceeds long time cost, value = 10
	 * */
	public static final int TIMEOUTED = 10;
	
	
	/****
	 * Task has been preempted by higher priority task (if applicable), value = 2
	 * */
	public static final int PREEMPTED = Status.PREEMPTED;
	
	
	/***
	 * run the task
	 * @param transport
	 * @return 0 means success, otherwise failure
	 * */
	int run(ITransport transport, Object context);
	
	
	/***
	 * Cancel
	 * @return 0 means success, otherwise failure
	 * */
	int cancel();
	
	
	/***
	 * get task run id
	 * */
	String getId();
	
	/***
	 * name of the task
	 * */
	String getName();
	
	/**
	 * Add TaskState Listener 
	 * */
	void addListener(ITaskStateListener listener);
	
	/**
	 * Remove TaskState Listener 
	 * */
	void removeListener(ITaskStateListener listener);
	
	/***
	 * Set extra information
	 * */
	void setExtra(Object object);
	
	/***
	 * Get extra information
	 * */
	Object getExtra();
}
