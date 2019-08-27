package com.alibaba.robot.web.manage.pojo;

public class Order {
	
	//桌号
	private int table_number;
	//是否有未完成的订单
	private boolean has_pending_order;
	
	public Order(int table_number, boolean has_pending_order) {
		super();
	   this.table_number = table_number;
		this.has_pending_order = has_pending_order;
	}
	
	public Order() {
		super();
	}
	
	public int getTable_number() {
		return table_number;
	}
	public void setTable_number(int table_number) {
		this.table_number = table_number;
	}
	public boolean isHas_pending_order() {
		return has_pending_order;
	}
	public void setHas_pending_order(boolean has_pending_order) {
		this.has_pending_order = has_pending_order;
	}
	@Override
	public String toString() {
		return "Orders [table_number=" + table_number + ", has_pending_order="
				+ has_pending_order + "]";
	}
	
}
