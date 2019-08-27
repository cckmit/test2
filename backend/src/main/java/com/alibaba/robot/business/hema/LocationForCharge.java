package com.alibaba.robot.business.hema;


import com.alibaba.robot.web.manage.pojo.Location;

public class LocationForCharge {
	
	public static final int REQUEST_CHARGE = 1;
	public static final int REQUEST_DISCHARGE = 0;
	
	public LocationForCharge(int request ) {
		this.request = request;
	}
	
	public LocationForCharge(Location location) {
		 this.poi1_pos_x = location.getX();
		 this.poi1_pos_y = location.getY();
		 this.poi1_ori_z = location.getZ();
		 this.poi1_ori_w = location.getW();
	}
	
	public void setRequest(int request) {
		this.request = request;
	}

	public int getRequest() {
		return request;
	}

	private int request = 1;
	double poi1_pos_x;
	double poi1_pos_y;
	double poi1_ori_z;
	double poi1_ori_w;
	 
}
