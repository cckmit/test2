package com.alibaba.robot.transport;

public enum EventType {
	Disconnected,
	AttemptToConnect,
	Connected,
	Error,
	IncomdingMessage, 
	Command,
	Publish,
	Shutdown,
	RunAction,
	RunTask,
	TaskCompleted,
	CancelTask,
	/**
	 * Still running need to switch to executing task state<br>
	 * typical scenario: disconnected when executing task
	 * */
	RunningAction,
	ActionComplete,
	StreamClosed,
	AdvertiseService,
	ForceStopCurrentTask,
	Timeout,
	Charging,
	ChargeInterrupted,
	
}
