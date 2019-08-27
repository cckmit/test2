package com.alibaba.robot.web.manage.entity;

public class OrderId {
	
	public String getOrder_id() {
		return order_id;
	}
 
	public int getTable_number() {
		return table_number;
	}

	public OrderId(String order_id, int table_number) {
		this.order_id = order_id;
		this.table_number = table_number;
	}

	private String order_id;
	private int table_number;
}
