package com.alibaba.robot.web.manage.pojo;

public class Drink {
	
	//饮料类型
	private int type;
	//存储点类型
	private int storage_type;
	//剩余的量
	private int amount;
	
	public Drink(int type, int storage_type, int amount) {
		super();
		this.type = type;
		this.storage_type = storage_type;
		this.amount = amount;
	}
	
	public Drink() {
		super();
	}

	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getStorage_type() {
		return storage_type;
	}
	
	public void setStorage_type(int storage_type) {
		this.storage_type = storage_type;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmout(int amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "Drink [type=" + type + ", storage_type=" + storage_type + ", amount=" + amount + "]";
	}
	
}
