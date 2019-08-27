package com.alibaba.robot.business.hema;

//系统配置项
public class SystemConfiguration {
	
	private Integer conf_id;
	private String conf_name;
	private String conf_type;
	private String string_value;
	private Integer int_value;
	private float float_value;
	private double double_value;
	private boolean boolean_value;
	public SystemConfiguration(Integer conf_id, String conf_name, String conf_type, String string_value,
			Integer int_value, float float_value, double double_value, boolean boolean_value) {
		super();
		this.conf_id = conf_id;
		this.conf_name = conf_name;
		this.conf_type = conf_type;
		this.string_value = string_value;
		this.int_value = int_value;
		this.float_value = float_value;
		this.double_value = double_value;
		this.boolean_value = boolean_value;
	}
	public SystemConfiguration() {
		super();
	}
	public Integer getConf_id() {
		return conf_id;
	}
	public void setConf_id(Integer conf_id) {
		this.conf_id = conf_id;
	}
	public String getConf_name() {
		return conf_name;
	}
	public void setConf_name(String conf_name) {
		this.conf_name = conf_name;
	}
	public String getConf_type() {
		return conf_type;
	}
	public void setConf_type(String conf_type) {
		this.conf_type = conf_type;
	}
	public String getString_value() {
		return string_value;
	}
	public void setString_value(String string_value) {
		this.string_value = string_value;
	}
	public Integer getInt_value() {
		return int_value;
	}
	public void setInt_value(Integer int_value) {
		this.int_value = int_value;
	}
	public float getFloat_value() {
		return float_value;
	}
	public void setFloat_value(float float_value) {
		this.float_value = float_value;
	}
	public double getDouble_value() {
		return double_value;
	}
	public void setDouble_value(double double_value) {
		this.double_value = double_value;
	}
	public boolean isBoolean_value() {
		return boolean_value;
	}
	public void setBoolean_value(boolean boolean_value) {
		this.boolean_value = boolean_value;
	}
	
	public Object getValue(String type){
		if("string_value".equals(type)){
			return string_value;
		} else if("int_value".equals(type)){
			return int_value;
		} else if("float_value".equals(type)){
			return float_value;
		} else if("double_value".equals(type)){
			return double_value;
		} else if("boolean_value".equals(type)){
			return boolean_value;
		}
		return null;
	}
	
	
	@Override
	public String toString() {
		return "SystemConfiguration [conf_id=" + conf_id + ", conf_name=" + conf_name + ", conf_type=" + conf_type
				+ ", string_value=" + string_value + ", int_value=" + int_value + ", float_value=" + float_value
				+ ", double_value=" + double_value + ", boolean_value=" + boolean_value + "]";
	}
	
	
	
	
}
