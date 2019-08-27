package com.alibaba.robot.task;

import com.alibaba.robot.transport.ITransport;

/***
 * Task state listener
 */
public interface ITaskStateListener {

	/***
	 * Task started callback<br>
	 * 
	 * @param transport
	 * @param taskState
	 */
	void onTaskStarted(ITask task, ITransport transport, TaskState taskState);

	
	/***
	 * Task completed callback
	 * 
	 * @param transport
	 * @param taskState
	 */
	void onTaskFeedback(ITask task, ITransport transport, TaskState taskState, Object feedback);
	
	
	/***
	 * Task completed callback<br>
	 * Task terminal state:<br>
	 * finished normally, preempted, cancelled, timeout
	 * 
	 * @param transport
	 * @param taskState
	 * @param result of the task run
	 */
	void onTaskCompleted(ITask task, ITransport transport, TaskState taskState, Object result);
}
