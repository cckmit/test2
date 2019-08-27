package com.alibaba.robot.web.manage.pojo;


public class AttributeDefinition {
	
	private Integer attdef_id;
	private String attribute;
	private String attribute_type;
	private String string_value;
	private Integer int_value;
	private float float_value;
	private double double_value;
	private boolean boolean_value;
	private String robot_uniqueId;
	
	
	
	public AttributeDefinition(Integer attdef_id, String attribute, String attribute_type, String string_value,
			Integer int_value, float float_value, double double_value, boolean boolean_value, String robot_uniqueId) {
		super();
		this.attdef_id = attdef_id;
		this.attribute = attribute;
		this.attribute_type = attribute_type;
		this.string_value = string_value;
		this.int_value = int_value;
		this.float_value = float_value;
		this.double_value = double_value;
		this.boolean_value = boolean_value;
		this.robot_uniqueId = robot_uniqueId;
	}
	public AttributeDefinition() {
		super();
	}
	public Integer getAttdef_id() {
		return attdef_id;
	}
	public void setAttdef_id(Integer attdef_id) {
		this.attdef_id = attdef_id;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getAttribute_type() {
		return attribute_type;
	}
	public void setAttribute_type(String attribute_type) {
		this.attribute_type = attribute_type;
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
	public String getRobot_uniqueId() {
		return robot_uniqueId;
	}
	public void setRobot_uniqueId(String robot_uniqueId) {
		this.robot_uniqueId = robot_uniqueId;
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
	
	
	
	public String toString() {
		return "AttributeDefinition [attdef_id=" + attdef_id + ", attribute=" + attribute + ", attribute_type="
				+ attribute_type + ", string_value=" + string_value + ", int_value=" + int_value + ", float_value="
				+ float_value + ", double_value=" + double_value + ", boolean_value=" + boolean_value
				+ ", robot_uniqueId=" + robot_uniqueId + "]";
	}
	
	
}
