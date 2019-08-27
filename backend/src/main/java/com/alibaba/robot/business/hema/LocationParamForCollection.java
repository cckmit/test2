package com.alibaba.robot.business.hema;

import java.util.List;

import com.alibaba.robot.web.manage.pojo.Location;

public class LocationParamForCollection extends LocationParam {

	public void setTimeout_1(int timeout_1) {
		this.timeout_1 = timeout_1;
	}

	public void setTimeout_2(int timeout_2) {
		this.timeout_2 = timeout_2;
	}
	
	public void setTableNumber(String tableNumber) {
		this.table_number = tableNumber;
	}

	String table_number = "";

	/***
	 * T1 timeout<br>
	 * TODO: read from DB
	 */
	int timeout_1 = 30;

	/****
	 * t2 timeout<br>
	 * TODO: read from DB
	 */
	int timeout_2 = 10;

	/***
	 * t3 timeout<br>
	 * TODO: read from DB
	 */
	int timeout_3 = 5;

	public LocationParamForCollection(List<Location> locations) {
		super(locations);
	}
}
