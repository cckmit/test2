package com.alibaba.robot.actionlib;

import java.util.LinkedList;
import java.util.List;

public class StatusArray {
	
	public Header getHeader() {
		return header;
	}
	public List<Status> getStatus_list() {
		return status_list;
	}
	
	public StatusArray(Header header) {
		super();
		this.header = header;
	}
	
	public StatusArray() {
		
	}

	private Header header;
	private List<Status> status_list = new LinkedList<Status>();
}
	