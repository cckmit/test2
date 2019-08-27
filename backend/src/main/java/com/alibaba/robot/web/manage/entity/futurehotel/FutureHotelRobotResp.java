package com.alibaba.robot.web.manage.entity.futurehotel;

import java.util.List;

public class FutureHotelRobotResp {
	
	private int goods_support;
	private int container_carray;
	private List<HotelRobotStatus> robot_status;
	
	
	
	public FutureHotelRobotResp(int goods_support, int container_carray, List<HotelRobotStatus> robot_status) {
		super();
		this.goods_support = goods_support;
		this.container_carray = container_carray;
		this.robot_status = robot_status;
	}
	public FutureHotelRobotResp() {
		super();
	}
	public int getGoods_support() {
		return goods_support;
	}
	public void setGoods_support(int goods_support) {
		this.goods_support = goods_support;
	}
	public int getContainer_carray() {
		return container_carray;
	}
	public void setContainer_carray(int container_carray) {
		this.container_carray = container_carray;
	}
	public List<HotelRobotStatus> getRobot_status() {
		return robot_status;
	}
	public void setRobot_status(List<HotelRobotStatus> robot_status) {
		this.robot_status = robot_status;
	}
	@Override
	public String toString() {
		return "FutureHotelRobotResp [goods_support=" + goods_support + ", container_carray=" + container_carray
				+ ", robot_status=" + robot_status + "]";
	}
	
	
}
