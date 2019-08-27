package com.alibaba.robot.web.manage.entity;

public class PlaceOrderResult {
	
	public static int SUCCESS = 1;
	public static int HAS_PENDING_ORDER = 2;
 
	
	public PlaceOrderResult(int result,  String message) {
		this.result = result;
		this.message = message;
	}
	
	public PlaceOrderResult(int result, String order_id, String message) {
		this.order_id = order_id;
		this.result = result;
		this.message = message;
	}

	public String getOrder_id() {
		return order_id;
	}

	public int getResult() {
		return result;
	}
	
	public String getMessage() {
		return message;
	}

	private String order_id = "";
	private int result;
	private String message;
}