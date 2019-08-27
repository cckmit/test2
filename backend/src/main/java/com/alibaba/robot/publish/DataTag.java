package com.alibaba.robot.publish;

public interface DataTag {
	
	/****
	 * 大屏数据
	 * */
	public static final int BIG_SCREEN_DATA = 1 << 0;
	
	/***
	 * 机器人状态信息
	 * */
	public static final int BASIC_STATUS_DATA = 1 << 1;
	
	/****
	 * 机器人详细状态信息
	 * */
	public static final int DETAILED_STATUS_DATA = 1 << 2;
	
	/****
	 * 任务信息
	 * */
	public static final int TASK_DATA = 1 << 3;
	
	/***
	 * 算法团队需要的机器人数据
	 * */
	public static final int CUSTOMIZED_DATA_FOR_ALG = 1 << 4;
	
	/***
	 * 盒马推送业务
	 * */
	public static final int DATA_FOR_HEMA = 1 << 5;
}

