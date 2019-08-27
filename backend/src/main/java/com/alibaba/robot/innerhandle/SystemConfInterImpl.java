package com.alibaba.robot.innerhandle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.robot.business.hema.SystemConfiguration;
import com.alibaba.robot.common.ErrorCode;
import com.alibaba.robot.common.JDBCUtil;

public class SystemConfInterImpl {
	
	static int STRING = 1;
	static int INT = 2;
	static int FLOAT = 3;
	static int DOUBLE = 4;
	static int BOOLEAN = 5;

	public int systemConfAdd(SystemConfiguration configuration) {
		String conf_name = configuration.getConf_name();
		String conf_type = configuration.getConf_type();
		String typeJudge = typeJudge(conf_type);
		int typeValue = getTypeValue(conf_type);
		int add = -1;
		if(typeJudge != null){
			Object value = configuration.getValue(typeJudge);
			String sqlAdd = "insert into systemconfiguration(conf_name,conf_type, "+ typeJudge +") values(?,?,?)";
			Connection connection = JDBCUtil.getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement(sqlAdd);
				statement.setString(1, conf_name);
				statement.setInt(2, typeValue);
				statement.setObject(3, value);
				add = statement.executeUpdate();
				JDBCUtil.close(connection, statement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		if(add == 1){
			return ErrorCode.SUCCESS;
		} else {
			return ErrorCode.FAILED;
		}
		
	}
	


	public int systemConfUpdate(SystemConfiguration configuration) {
		String conf_name = configuration.getConf_name();
		
		Connection connection = JDBCUtil.getConnection();
		String sqlType = "select conf_type from systemconfiguration where conf_name = ?";
		int update = -1;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sqlType);
			prepareStatement.setString(1, conf_name);
			ResultSet resultSet = prepareStatement.executeQuery();
			int typeValue = 0;
			while(resultSet.next()){
				typeValue = resultSet.getInt("conf_type");
			}
			String conf_type = getConfType(typeValue);
			
			if(conf_type != null){
				Object value = configuration.getValue(conf_type);
				String sqlUpdate = "update systemconfiguration set "+ conf_type +" = ? where conf_name = ?";
				prepareStatement = connection.prepareStatement(sqlUpdate);
				prepareStatement.setObject(1, value);
				prepareStatement.setString(2, conf_name);
				update = prepareStatement.executeUpdate();
				JDBCUtil.close(connection, prepareStatement,resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(update == 1){
			return ErrorCode.SUCCESS;
		} else {
			return ErrorCode.FAILED;
		}
		
	}
	
	
	public int systemConfDelete(String conf_name){
		String sqlDelete = "delete from systemconfiguration where conf_name = ?";
		Connection connection = JDBCUtil.getConnection();
		int delete = -1;
		try {
			PreparedStatement statement = connection.prepareStatement(sqlDelete);
			statement.setString(1, conf_name);
			delete = statement.executeUpdate();
			JDBCUtil.close(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(delete == 1){
			return ErrorCode.SUCCESS;
		} else {
			return ErrorCode.FAILED;
		}
		
	}
	
	
	
	public Object systemConfSelectOne(String conf_name){
		Connection connection = JDBCUtil.getConnection();
		String sqlType = "select conf_type from systemconfiguration where conf_name = ?";
		Object value = null;
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sqlType);
			prepareStatement.setString(1, conf_name);
			ResultSet resultSet = prepareStatement.executeQuery();
			int typeValue = 0;
			while(resultSet.next()){
				typeValue = resultSet.getInt("conf_type");
			}
			String conf_type = getConfType(typeValue);
			if(conf_type != null){
				String sqlSelect = "select "+ conf_type +" from systemconfiguration where conf_name = ?";
				prepareStatement = connection.prepareStatement(sqlSelect);
				prepareStatement.setString(1, conf_name);
				resultSet = prepareStatement.executeQuery();
				while(resultSet.next()){
					value = resultSet.getObject(conf_type);
				}
			}
			JDBCUtil.close(connection, prepareStatement, resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	

	public Map<String, Object> systemConfSelectAll() {
		String sqlSelect = "select * from systemconfiguration";
		Map<String, Object> map = new HashMap<>();
		Connection connection = JDBCUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(sqlSelect);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()){
				String name = resultSet.getString("conf_name");
				int typeValue = resultSet.getInt("conf_type");
				String confType = getConfType(typeValue);
				if(confType != null){
					map.put(name, resultSet.getObject(confType));
				}
			}
			JDBCUtil.close(connection, statement, resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return map;
	}

	
	
	public String typeJudge(String conf_type){
		if("String".equals(conf_type)){
			return "string_value";
		} else if("int".equals(conf_type)) {
			return "int_value";
		} else if ("float".equals(conf_type)) {
			return "float_value";
		} else if ("double".equals(conf_type)) {
			return "double_value";
		} else if ("boolean".equals(conf_type)) {
			return "boolean_value";
		}
		return null;
	}
	
	public int getTypeValue(String conf_type){
		if("String".equals(conf_type)){
			return STRING;
		} else if("int".equals(conf_type)) {
			return INT;
		} else if ("float".equals(conf_type)) {
			return FLOAT;
		} else if ("double".equals(conf_type)) {
			return DOUBLE;
		} else if ("boolean".equals(conf_type)) {
			return BOOLEAN;
		}
		return 0;
	}
	
	public String getConfType(int type_value){
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

}
