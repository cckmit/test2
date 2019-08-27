package com.alibaba.robot.web.manage.entity;

//盒马请求参数
public class GetHemaReq {
	// 任务ID
	private String taskId;
	// 确定是哪家店
	private String warehouseCode;
	// 机器人唯一标识
	private String robotUniqueId;
	// 指定餐桌PO I ID
	private String deskId;

	private String businessTaskId;
	
	private int taskType;

	/**
	 * Task timeout
	 */
	private int timeout = Integer.MAX_VALUE;
	// 桌号
	private String poiId;
	// 曲目编号
	private Integer musiceIndex;

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public void setBusinessTaskId(String businessTaskId) {
		this.businessTaskId = businessTaskId;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public GetHemaReq(String taskId, String warehouseCode, String robotUniqueId, String deskId, String poiId,
			Integer musiceIndex) {
		super();

		this.taskId = taskId;
		this.warehouseCode = warehouseCode;
		this.robotUniqueId = robotUniqueId;
		this.deskId = deskId;
		this.poiId = poiId;
		this.musiceIndex = musiceIndex;
	}

	public GetHemaReq() {

	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	public String getPoiId() {
		return poiId;
	}

	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}

	public int getTaskType() {
		return this.taskType;
	}

	public int getTaskTimeout() {
		return this.timeout;
	}

	public void setMusiceIndex(int musiceIndex) {
		musiceIndex = Integer.valueOf(musiceIndex);
	}

	public Integer getMusiceIndex() {
		return musiceIndex;
	}

	public void setMusiceIndex(Integer musiceIndex) {

		this.musiceIndex = musiceIndex;
	}

	public String getBusinessTaskId() {
		return businessTaskId;
	}

	public int getTimeout() {
		return timeout;
	}

}
