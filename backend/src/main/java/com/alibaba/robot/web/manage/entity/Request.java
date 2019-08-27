
package com.alibaba.robot.web.manage.entity;

public class Request {
	protected String sn;
	protected String token;
	protected String command;
	protected Object data;
	
	public Request(String sn, String token, String command, Object data) {
		super();
		this.sn = sn;
		this.token = token;
		this.command = command;
		this.data = data;
	}
	public Request() {
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
	public Object getData() {
		return data;
	}
	@Override
	public String toString() {
		return "Request [sn=" + sn + ", token=" + token + ", command=" + command + ", data=" + data + "]";
	}
}

