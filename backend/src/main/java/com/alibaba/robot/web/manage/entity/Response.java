package com.alibaba.robot.web.manage.entity;

public class Response<T> {
	public String getSn() {
		return sn;
	}
	public String getMessage() {
		return message;
	}
	public int getResult() {
		return result;
	}
	public T getData() {
		return data;
	}
	public Response(String sn, String message, int result, T data) {
		super();
		this.sn = sn;
		this.message = message;
		this.result = result;
		this.data = data;
	}
	
	public Response(String sn, String message, int result) {
		super();
		this.sn = sn;
		this.message = message;
		this.result = result;
	}
	
	public Response() {
		super();
	}
	

	@Override
	public String toString() {
		return "Response [sn=" + sn + ", message=" + message + ", result=" + result + ", data=" + data + "]";
	}


	protected String sn;
	protected String message;
	protected int result;
	protected T data;
}
