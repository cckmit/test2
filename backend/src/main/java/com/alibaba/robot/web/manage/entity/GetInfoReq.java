package com.alibaba.robot.web.manage.entity;

public class GetInfoReq {
	//唯一编码值
	private String robot_uniqueId;
	//名称
    private String name;
    //地址
    private String address;
    //型号
    private String model;
    private String frimware;
    //版本号
    private String version;
    //状态
    private Integer status;
    private Integer robotType;
    
    
    
	public GetInfoReq(String robot_uniqueId, String name, String address, String model, String frimware, String version,
			Integer status, Integer robotType) {
		super();
		this.robot_uniqueId = robot_uniqueId;
		this.name = name;
		this.address = address;
		this.model = model;
		this.frimware = frimware;
		this.version = version;
		this.status = status;
		this.robotType = robotType;
	}
	public GetInfoReq() {
		super();
	}
	public String getRobot_uniqueId() {
		return robot_uniqueId;
	}
	public void setRobot_uniqueId(String robot_uniqueId) {
		this.robot_uniqueId = robot_uniqueId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getFrimware() {
		return frimware;
	}
	public void setFrimware(String frimware) {
		this.frimware = frimware;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getRobotType() {
		return robotType;
	}
	public void setRobotType(Integer robotType) {
		this.robotType = robotType;
	}
    
	
}
