package com.alibaba.robot.web.manage.entity;

import java.util.List;

public class GetHemaReqList {
	//任务ID
	private String taskId;
	//确定是哪家店
	private String warehouseCode;
	//机器人唯一标识
	private List<String> robotUniqueIds;
	//指定餐桌PO I ID
	private String deskId;
	//桌号
	private String poiId;
	//曲目编号
	private Integer musiceIndex;
	
	
	
	public GetHemaReqList(String taskId, String warehouseCode, List<String> robotUniqueIds, String deskId,
			String poiId, Integer musiceIndex) {
		super();
		this.taskId = taskId;
		this.warehouseCode = warehouseCode;
		this.robotUniqueIds = robotUniqueIds;
		this.deskId = deskId;
		this.poiId = poiId;
		this.musiceIndex = musiceIndex;
	}
	public GetHemaReqList() {
		super();
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
	public List<String> getRobotUniqueIds() {
		return robotUniqueIds;
	}
	public void setRobotUniqueIds(List<String> robotUniqueIds) {
		this.robotUniqueIds = robotUniqueIds;
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
	public Integer getMusiceIndex() {
		return musiceIndex;
	}
	public void setMusiceIndex(Integer musiceIndex) {
		this.musiceIndex = musiceIndex;
	}
	
	
}
