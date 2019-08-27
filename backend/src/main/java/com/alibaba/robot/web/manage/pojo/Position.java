package com.alibaba.robot.web.manage.pojo;

public class Position {
	public Quaternion getRotation() {
		return rotation;
	}
	public Vector3 getTranslation() {
		return translation;
	}
	private Quaternion rotation;
	private Vector3 translation;
}
