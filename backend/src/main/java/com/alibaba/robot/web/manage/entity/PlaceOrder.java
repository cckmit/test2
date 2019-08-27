package com.alibaba.robot.web.manage.entity;

//下单
public class PlaceOrder {
	
	//订单id
	private String order_id;
	//饮料类型
	private int drink_type;
	//桌号
	private int table_number;
	//饮料类别
	private String drink_storage_type;
	//服务是否可用
	private boolean useable;
	//饮料数量
	private Integer amount;
	
	
	
	public PlaceOrder(String order_id, int drink_type, int table_number, String drink_storage_type, boolean useable,
			Integer amount) {
		super();
		this.order_id = order_id;
		this.drink_type = drink_type;
		this.table_number = table_number;
		this.drink_storage_type = drink_storage_type;
		this.useable = useable;
		this.amount = amount;
	}
	public PlaceOrder() {
		super();
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public int getDrink_type() {
		return drink_type;
	}
	public void setDrink_type(int drink_type) {
		this.drink_type = drink_type;
	}
	public int getTable_number() {
		return table_number;
	}
	public void setTable_number(int table_number) {
		this.table_number = table_number;
	}
	public String getDrink_storage_type() {
		return drink_storage_type;
	}
	public void setDrink_storage_type(String drink_storage_type) {
		this.drink_storage_type = drink_storage_type;
	}
	public boolean isUseable() {
		return useable;
	}
	public void setUseable(boolean useable) {
		this.useable = useable;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
