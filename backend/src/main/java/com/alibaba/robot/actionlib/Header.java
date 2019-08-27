package com.alibaba.robot.actionlib;

public class Header {

	public Stamp getStamp() {
		return stamp;
	}

	public String getFrame_id() {
		return frame_id;
	}

	public long getSeq() {
		return seq;
	}

	public Header(Stamp stamp, String frame_id, long seq) {
		super();
		this.stamp = stamp;
		this.frame_id = frame_id;
		this.seq = seq;
	}

	public Header() {
		this(new Stamp(), "", 0);
	}

	private Stamp stamp;
	private String frame_id;
	private long seq;

}
