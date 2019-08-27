package com.alibaba.robot.attributdef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.robot.common.JDBCUtil;
import com.alibaba.robot.web.manage.pojo.AttributeDefinition;

public class AttributeDefinitionModel {
	
	static int STRING = 1;
	static int INT = 2;
	static int FLOAT = 3;
	static int DOUBLE = 4;
	static int BOOLEAN = 5;
	
	
	public int addAttribute(AttributeDefinition attDefinition){
		String attribute = attDefinition.getAttribute();
		String attribute_type = attDefinition.getAttribute_type();
		String robot_uniqueId = attDefinition.getRobot_uniqueId();
		String typeJudge = typeJudge(attribute_type);
		int typeValue = getTypeValue(attribute_type);
		int add = 0;
		if(typeJudge != null){
			Object value = attDefinition.getValue(typeJudge);
//			Connection connection = JDBCUtil.getConnection();
//			String sql = "insert into attributedefinition(attribute, attribute_type, "+ typeJudge +", robot_uniqueId) values(?,?,?,?)";
//			try {
//				PreparedStatement statement = connection.prepareStatement(sql);
//				statement.setString(1, attribute);
//				statement.setInt(2, typeValue);
//				statement.setObject(3, value);
//				statement.setString(4, robot_uniqueId);
//				add = statement.executeUpdate();
//				JDBCUtil.close(connection, statement);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			
			add = insertAttributedefinition(typeJudge ,attribute,typeValue,value,robot_uniqueId);
		}
		
		return add;
	}
	
	
	public int deleteAttribute(AttributeDefinition attDefinition){
		String attribute = attDefinition.getAttribute();
		String robot_uniqueId = attDefinition.getRobot_uniqueId();
		int delete = 0;
		Connection connection = JDBCUtil.getConnection();
		String sql = "delete from attributedefinition where attribute = ? and robot_uniqueId = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, attribute);
			statement.setString(2, robot_uniqueId);
			delete = statement.executeUpdate();
			JDBCUtil.close(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return delete;
		
	}
	
	
	public int updateAttribute(AttributeDefinition attDefinition){
		String attribute = attDefinition.getAttribute();
		String robot_uniqueId = attDefinition.getRobot_uniqueId();
		Connection connection = JDBCUtil.getConnection();
		String sqlType = "select attribute_type from attributedefinition where attribute = ? and robot_uniqueId = ?";
		int update = 0;
		int typeValue = 0;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sqlType);
			prepareStatement.setString(1, attribute);
			prepareStatement.setString(2, robot_uniqueId);
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				typeValue = resultSet.getInt("attribute_type");
			}
			String attribute_type = getAttributeType(typeValue);
			if(attribute_type != null){
				Object value = attDefinition.getValue(attribute_type);
				String sql = "update attributedefinition set "+ attribute_type +" = ? where attribute = ? and robot_uniqueId = ?";
				prepareStatement = connection.prepareStatement(sql);
				prepareStatement.setObject(1, value);
				prepareStatement.setString(2, attribute);
				prepareStatement.setString(3, robot_uniqueId);
				update = prepareStatement.executeUpdate();
				JDBCUtil.close(connection, prepareStatement,resultSet);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return update;
		
	}
	
	
	public Map<String, Object> selectOneAttribute(AttributeDefinition attDefinition){
		String attribute = attDefinition.getAttribute();
		String robot_uniqueId = attDefinition.getRobot_uniqueId();
		Map<String, Object> map = new HashMap<>();
		Connection connection = JDBCUtil.getConnection();
		String sqlType = "select attribute_type from attributedefinition where attribute = ? and robot_uniqueId = ?";
		int typeValue = 0;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sqlType);
			prepareStatement.setString(1, attribute);
			prepareStatement.setString(2, robot_uniqueId);
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				typeValue = resultSet.getInt("attribute_type");
			}
			String attribute_type = getAttributeType(typeValue);
			if(attribute_type != null){
				String sql = "select "+ attribute_type +" from attributedefinition where attribute = ? and robot_uniqueId = ?";
				prepareStatement = connection.prepareStatement(sql);
				prepareStatement.setString(1, attribute);
				prepareStatement.setString(2, robot_uniqueId);
				resultSet = prepareStatement.executeQuery();
				while(resultSet.next()){
					map.put("robot_uniqueId:"+robot_uniqueId, resultSet.getObject(attribute_type));
				}
				
				JDBCUtil.close(connection, prepareStatement,resultSet);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return map;
		
	}
	
	public Map<String, Object> selectTypeAttribute(AttributeDefinition attDefinition){
		String attribute = attDefinition.getAttribute();
		Map<String, Object> map = new HashMap<>();
		Connection connection = JDBCUtil.getConnection();
		String sqlType = "select attribute_type from attributedefinition where attribute = ?";
		int typeValue = 0;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sqlType);
			prepareStatement.setString(1, attribute);
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				typeValue = resultSet.getInt("attribute_type");
			}
			String attribute_type = getAttributeType(typeValue);
			if(attribute_type != null){
				String sql = "select "+ attribute_type +" , robot_uniqueId from attributedefinition where attribute = ?";
				prepareStatement = connection.prepareStatement(sql);
				prepareStatement.setString(1, attribute);
				resultSet = prepareStatement.executeQuery();
				while(resultSet.next()){
					Object value = resultSet.getObject(attribute_type);
					String robot_uniqueId = resultSet.getString("robot_uniqueId");
					map.put("robot_uniqueId:"+robot_uniqueId, value);
				}
				
				JDBCUtil.close(connection, prepareStatement,resultSet);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	
	public String typeJudge(String attType){
		if("String".equals(attType)){
			return "string_value";
		} else if("int".equals(attType)) {
			return "int_value";
		} else if ("float".equals(attType)) {
			return "float_value";
		} else if ("double".equals(attType)) {
			return "double_value";
		} else if ("boolean".equals(attType)) {
			return "boolean_value";
		}
		return null;
	}
	
	public int getTypeValue(String attribute_type){
		if("String".equals(attribute_type)){
			return STRING;
		} else if("int".equals(attribute_type)) {
			return INT;
		} else if ("float".equals(attribute_type)) {
			return FLOAT;
		} else if ("double".equals(attribute_type)) {
			return DOUBLE;
		} else if ("boolean".equals(attribute_type)) {
			return BOOLEAN;
		}
		return 0;
	}
	
	public String getAttributeType(int type_value){
		if(type_value == 1){
			return "string_value";
		} else if(type_value == 2) {
			return "int_value";
		} else if (type_value == 3) {
			return "float_value";
		} else if (type_value == 4) {
			return "double_value";
		} else if (type_value == 5) {
			return "boolean_value";
		}
		return null;
	}
	public int insertAttributedefinition(String typeJudge,String attribute,int typeValue,Object value,String robot_uniqueId) {
		int add = 0;
		Connection connection = JDBCUtil.getConnection();
		String sql = "insert into attributedefinition(attribute, attribute_type, "+ typeJudge +", robot_uniqueId) values(?,?,?,?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, attribute);
			statement.setInt(2, typeValue);
			statement.setObject(3, value);
			statement.setString(4, robot_uniqueId);
			add = statement.executeUpdate();
			JDBCUtil.close(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return add;
		
	}

	
}

