package com.alibaba.robot.actionlib;

import java.util.Random;

public class GoalId {
	private static Random RAND = new Random();
	//public Stamp getStamp() {
	//	return stamp;
	//}
	public String getId() {
		return id;
	}
	public GoalId(Stamp stamp, String id) {
		super();
		//this.stamp = stamp;
		this.id = id;
	}
	
	public GoalId() {
		//this.stamp = new Stamp();
		this.id =   Long.toString(RAND.nextLong());
	}
	
	public GoalId(String id) {
		//this.stamp = new Stamp();
		this.id =   id;
	}
	
	//private Stamp stamp;
	private String id;

}
