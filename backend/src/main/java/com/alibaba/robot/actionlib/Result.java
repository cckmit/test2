package com.alibaba.robot.actionlib;

public class Result <T> {
	
	public Status getStatus() {
		return status;
	}
	public T getResult() {
		return result;
	}
	public Header getHeader() {
		return header;
	}
	
	private Status status;
	private Header header;
	private T result;

}
