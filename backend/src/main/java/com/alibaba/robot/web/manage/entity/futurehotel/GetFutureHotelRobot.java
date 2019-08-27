package com.alibaba.robot.web.manage.entity.futurehotel;

import java.util.List;

public class GetFutureHotelRobot {
	
	private String task_id;
	private String task_name;
	private Integer task_type;
	private List<GoodsParam> goods_param;
	
	
	
	public GetFutureHotelRobot(String task_id, String task_name, Integer task_type, List<GoodsParam> goods_param) {
		super();
		this.task_id = task_id;
		this.task_name = task_name;
		this.task_type = task_type;
		this.goods_param = goods_param;
	}
	public GetFutureHotelRobot() {
		super();
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public Integer getTask_type() {
		return task_type;
	}
	public void setTask_type(Integer task_type) {
		this.task_type = task_type;
	}
	public List<GoodsParam> getGoods_param() {
		return goods_param;
	}
	public void setGoods_param(List<GoodsParam> goods_param) {
		this.goods_param = goods_param;
	}
	@Override
	public String toString() {
		return "GetFutureHotelRobot [task_id=" + task_id + ", task_name=" + task_name + ", task_type=" + task_type
				+ ", goods_param=" + goods_param + "]";
	}
	
}
