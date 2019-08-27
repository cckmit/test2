package com.alibaba.robot.business.hema;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RobotStatus {
	
	public String robotUniqueId;
	
	public String warehouseCode;
	
	/***
	 * 一些原始参数
	 * */
	public int taskType;
	
	public String taskId = "";
	
	public String poiId = "";
	
	/***
	 * 机器人状态
	 * */
	public int robotState;
	
	/***
	 * 业务任务状态
	 * */
	public int taskState;
	
	/***
	 * 机器人坐标
	 * */
	public List<Double> robotCoordinate = new LinkedList<Double>();
	
	/***
	 * 机器人错误代码， 出问题时用
	 * */
	public int errorCode;
	
	/****
	 * 对应的描述
	 * */
	public String errorMsg;
	
	/****
	 * 额外信息
	 * */
	public Object extra;
	
	/*****
	 * 电量百分比
	 * */
	public int robotEnergy;
	
	
	public String businessTaskId;
	
	public Map<String, Object> task_params;
	
	/***
	 * 餐盘容量， 分别代表, 4层容量， 从上到下， 1表示满， 0 为满
	 * */
	public List<Integer> robotCapacity = new LinkedList<Integer>();
	
	/****
	 * 原始请求信息
	 * */
	public Object originalReq;
}
