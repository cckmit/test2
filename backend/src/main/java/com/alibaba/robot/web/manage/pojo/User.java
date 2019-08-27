package com.alibaba.robot.web.manage.pojo;

public class User {
	private Integer id;
	private String name;
	private String password;
	private int status;
	private String token;
	
	
	
	public User(Integer id, String name, String password, int status, String token) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.status = status;
		this.token = token;
	}
	public User() {
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
