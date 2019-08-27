package com.alibaba.robot.business.hema;

/***
 * 盒马业务特有代码
 * */
public enum TaskStatus {
	
	TASK_STATUS_NONE  ( 0, "非法状态"),
	TASK_STATUS_WAIT  ( 11, "任务等待调度中"),
	TASK_STATUS_SCHEDULE  ( 12, "任务被调度中"),
	TASK_STATUS_EXECUTE_MOVING  ( 13, "执行任务——移动中"),
	TASK_STATUS_EXECUTE_ARRIVED  ( 14, "执行任务——到达目的地"),
	TASK_STATUS_EXECUTE_PLAYING  ( 15, "执行任务——处理前奏，比如播放欢迎曲目"),
	TASK_STATUS_EXECUTE_HANDLING  ( 16, "执行任务——处理中，比如充电中，收餐中，回收中"),
	TASK_STATUS_FINISH  ( 17, "任务完成——充电完成，收餐完成，回收完成"),
	TASK_STATUS_INTERRUPT_ROBOT_TIMEOUT  ( 18 , "任务中断——机器人超时"),
	TASK_STATUS_INTERRUPT_ROBOT_FULL  ( 19 , "任务中断——机器人装满"),
	TASK_STATUS_INTERRUPT_ROBOT_LOW_POWER  ( 20 , "任务中断——机器人电量不足");
	
	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	private int code;
	private String desc;
	
	TaskStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static TaskStatus statusFromCode(int code) {
		for( TaskStatus status:	values()) {
			if(status.getCode() == code) {
				return status;
			}
		}
		
		return TaskStatus.TASK_STATUS_NONE;
	}	

}
