package com.alibaba.robot.web.manage.pojo;

public class Task {
	//任务ID
	private int taskId;
	private String name;
	//确定是哪家店
	private String warehouseCode;
	//机器人唯一标识
	private String robotUniqueId;
	//指定餐桌PO I ID
	private String deskId;
	private int poiId;
	//曲目编号
	private int musiceIndex;
	
	
	
	public Task(int taskId, String name, String warehouseCode, String robotUniqueId, String deskId, int poiId,
			int musiceIndex) {
		super();
		this.taskId = taskId;
		this.name = name;
		this.warehouseCode = warehouseCode;
		this.robotUniqueId = robotUniqueId;
		this.deskId = deskId;
		this.poiId = poiId;
		this.musiceIndex = musiceIndex;
	}
	public Task() {
		super();
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getRobotUniqueId() {
		return robotUniqueId;
	}
	public void setRobotUniqueId(String robotUniqueId) {
		this.robotUniqueId = robotUniqueId;
	}
	public String getDeskId() {
		return deskId;
	}
	public void setDeskId(String deskId) {
		this.deskId = deskId;
	}
	public int getPoiId() {
		return poiId;
	}
	public void setPoiId(int poiId) {
		this.poiId = poiId;
	}
	public int getMusiceIndex() {
		return musiceIndex;
	}
	public void setMusiceIndex(int musiceIndex) {
		this.musiceIndex = musiceIndex;
	}
	
}
