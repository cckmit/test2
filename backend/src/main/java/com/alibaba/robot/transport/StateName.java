package com.alibaba.robot.transport;

public enum StateName {
	/***
	 * 离线状态
	 * */
	Disconnected(1),

	/***
	 * 正常尝试连接
	 * */
	Connecting(2),

	/***
	 * 在线&空闲
	 * */
	Established(3),

	/***
	 * 正在执行任务
	 * */
	ExecutingTask(4),
	
	/***
	 * 正在充电
	 * */
	Charging( 1 << 8),
	
	/***
	 * 停用状态
	 * */
	Disabled(10);

	private int value;

	public int intValue() {
		return value;
	}

	StateName(int value) {
		this.value = value;
	}
}
