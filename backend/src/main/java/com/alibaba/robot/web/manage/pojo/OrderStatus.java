package com.alibaba.robot.web.manage.pojo;

//订单状态
public class OrderStatus {
	
	private int table_number;	 
	private int status;
	
	public OrderStatus(int table_number, int status) {
		super();
		this.table_number = table_number;
		this.status = status;
	}
	public OrderStatus() {
		super();
	}
	public int getTable_number() {
		return table_number;
	}
	public void setTable_number(int table_number) {
		this.table_number = table_number;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
