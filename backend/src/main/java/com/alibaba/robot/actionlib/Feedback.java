package com.alibaba.robot.actionlib;

public class Feedback<T> {

	public Status getStatus() {
		return status;
	}
	public Header getHeader() {
		return header;
	}
	public T getFeedback() {
		return feedback;
	}
	
	public Feedback() {
		
	}
	
	public Feedback(Status status, Header header, T feedback) {
		super();
		this.status = status;
		this.header = header;
		this.feedback = feedback;
	}

	private Status status;
	private Header header;
	private T feedback;
	
}
