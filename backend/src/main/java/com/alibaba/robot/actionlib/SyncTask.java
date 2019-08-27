package com.alibaba.robot.actionlib;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.alibaba.robot.task.ITask;
import com.alibaba.robot.task.ITaskStateListener;
import com.alibaba.robot.task.TaskState;
import com.alibaba.robot.transport.ITransport;

public class SyncTask implements ITaskStateListener {

	public SyncTask(Action action, ITransport transport, int timeout) {
		this.action = action;
		this.transport = transport;
		this.timeout = timeout;
	}

	public synchronized TaskState runTaskAndWait() {
		if (action == null || transport == null || executed) {
			return taskState;
		}

		executed = true;
		action.addListener(this);
		transport.runTask(action);

		try {
			completionCountdownLatch.await((long) timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {

		}
		return taskState;
	}

	@Override
	public void onTaskStarted(ITask task, ITransport transport, TaskState taskState) {

	}

	@Override
	public void onTaskFeedback(ITask task, ITransport transport, TaskState taskState, Object feedback) {

	}

	@Override
	public void onTaskCompleted(ITask task, ITransport transport, TaskState taskState, Object result) {
		this.taskState = taskState;
		completionCountdownLatch.countDown();
	}

	private boolean executed;
	private Action action;
	private ITransport transport;
	private int timeout;
	private TaskState taskState;
	private CountDownLatch completionCountdownLatch = new CountDownLatch(1);
}
