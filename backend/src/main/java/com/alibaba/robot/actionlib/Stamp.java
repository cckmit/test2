package com.alibaba.robot.actionlib;

public class Stamp {
	public long getSecs() {
		return secs;
	}
	public long getNsecs() {
		return nsecs;
	}
	 
	public Stamp(long secs, long nsecs) {
		super();
		this.secs = secs;
		this.nsecs = nsecs;
	}
	
	public Stamp() {
		this(0, 0);
	}
	
	private long secs;
	private long nsecs;

}
