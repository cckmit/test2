package com.alibaba.robot.web.manage.pojo;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    //属性id
    private Integer id;
    //名称
    private String name;
    //地址
    private String address;
    //型号
    private String model;
    
    private String firmware;
    //版本号
    private String version;
    //状态
    private Integer status;
    //唯一编码值
    private String uniqueId;
    
    private Integer robotType;
    
    private List<Attributes> attributes;
    
	public Robot(Integer id, String name, String address, String model, String firmware, String version, Integer status,
			String uniqueId, Integer robotType) {
 
		this.id = id;
		this.name = name;
		this.address = address;
		this.model = model;
		this.firmware = firmware;
		this.version = version;
		this.status = status;
		this.uniqueId = uniqueId;
		this.robotType = robotType;
	}
	
	
	public Robot(String name, String address, String model, String firmware, String version, Integer status,
			String uniqueId, Integer robotType) {
 
		this.name = name;
		this.address = address;
		this.model = model;
		this.firmware = firmware;
		this.version = version;
		this.status = status;
		this.uniqueId = uniqueId;
		this.robotType = robotType;
	}

	public Robot(Integer id, String name, String address, String model, String firmware, String version, Integer status,
			String uniqueId, Integer robotType, List<Attributes> attributes) {

		this.id = id;
		this.name = name;
		this.address = address;
		this.model = model;
		this.firmware = firmware;
		this.version = version;
		this.status = status;
		this.uniqueId = uniqueId;
		this.robotType = robotType;
		this.attributes = attributes;
	}

	public Robot() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String frimware) {
		this.firmware = frimware;
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

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Integer getRobotType() {
		return robotType;
	}

	public void setRobotType(Integer robotType) {
		this.robotType = robotType;
	}
	
	public List<Attributes> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attributes> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "Robot [id=" + id + ", name=" + name + ", address=" + address + ", model=" + model + ", firmware="
				+ firmware + ", version=" + version + ", status=" + status + ", uniqueId=" + uniqueId + ", robotType="
				+ robotType + ", attributes=" + attributes + "]";
	}
	
	
    
}
