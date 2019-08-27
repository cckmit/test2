package com.alibaba.robot.web.manage.pojo;

public class Attributes {
	
	private String mAttribute_name;
	private String mAttribute_type;
	private Object mAttribute_value;
	
	private static final String string_value  = "String";
	private static final String int_value     = "int";
	private static final String float_value   = "float";
	private static final String double_value  = "double";
	private static final String boolean_value = "boolean";
	
	
	public Attributes(String mAttribute_name, String mAttribute_type, Object mAttribute_value) {
		super();
		this.mAttribute_name = mAttribute_name;
		this.mAttribute_type = mAttribute_type;
		this.mAttribute_value = mAttribute_value;
	}

	public Attributes() {
		super();
	}
	
	public String getmAttribute_name() {
		return mAttribute_name;
	}
	public void setmAttribute_name(String mAttribute_name) {
		this.mAttribute_name = mAttribute_name;
	}
	public String getmAttribute_type() {
		return mAttribute_type;
	}
	public void setmAttribute_type(String mAttribute_type) {
		this.mAttribute_type = mAttribute_type;
	}
	public Object getmAttribute_value() {
		return mAttribute_value;
	}
	public void setmAttribute_value(Object mAttribute_value) {
		this.mAttribute_value = mAttribute_value;
	}
	
	@Override
	public String toString() {
		return "Attributes [mAttribute_name=" + mAttribute_name + ", mAttribute_type=" + mAttribute_type
				+ ", mAttribute_value=" + mAttribute_value + "]";
	}
	
	public int getValue(String type){
		switch (type) {
		case string_value:    return   1;
		case int_value:       return   2;
		case float_value:     return   3;
		case double_value:    return   4;
		case boolean_value:   return   5;
		default:              break;
		}
		return 0;
	}
	
}
