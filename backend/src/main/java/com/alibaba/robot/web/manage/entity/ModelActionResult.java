package com.alibaba.robot.web.manage.entity;



public class ModelActionResult {
	private int status;
	private Progress progress;;

	
	public ModelActionResult(int status, Progress progress) {
		super();
		this.status = status;
		this.progress = progress;
	}
	public ModelActionResult() {
		super();
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Progress getProgress() {
		return progress;
	}
	public void setProgress(Progress progress) {
		this.progress = progress;
	}

	
}
