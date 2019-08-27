package com.alibaba.robot.web.manage.entity;


 

public class GenericRequest<T> {
	protected String sn;
	protected String token;
	protected String command;
	protected T data;
	
	public GenericRequest(String sn, String token, String command, T data) {
		this.sn = sn;
		this.token = token;
		this.command = command;
		this.data = data;
	}
	
	public GenericRequest() {
		super();
	}
	public String getSn() {
		return sn;
	}
	public String getToken() {
		return token;
	}
	public String getCommand() {
		return command;
	}
	public T getData() {
		return data;
	}
	@Override
	public String toString() {
		return "Request [sn=" + sn + ", token=" + token + ", command=" + command + ", data=" + data + "]";
	}
}

