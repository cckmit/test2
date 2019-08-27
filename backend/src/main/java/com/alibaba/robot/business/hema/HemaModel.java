package com.alibaba.robot.business.hema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.robot.common.JDBCUtil;
import com.alibaba.robot.web.manage.entity.Request;
import com.google.gson.Gson;

public class HemaModel {
	
	
	private static Gson gson = new Gson();
	
	static int STRING = 1;
	static int INT = 2;
	static int FLOAT = 3;
	static int DOUBLE = 4;
	static int BOOLEAN = 5;
	
	
	public static Integer addConf(Request request){
		String data = request.getData().toString();
		SystemConfiguration configuration = gson.fromJson(data, SystemConfiguration.class);
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
		
		return add;
	}
	
	public static Integer updateConf(Request request){
		String data = request.getData().toString();
		SystemConfiguration configuration = gson.fromJson(data, SystemConfiguration.class);
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
		
		return update;
	}
	
	
	public static Integer deleteConf(Request request){
		String data = request.getData().toString();
		SystemConfiguration configuration = gson.fromJson(data, SystemConfiguration.class);
		String conf_name = configuration.getConf_name();
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
		
		return delete;
	}
	
	
	public static Map<String, Object> selectConfOne(Request request){
		String data = request.getData().toString();
		SystemConfiguration configuration = gson.fromJson(data, SystemConfiguration.class);
		String conf_name = configuration.getConf_name();
		
		Map<String, Object> map = new HashMap<>();
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
					map.put(conf_name, resultSet.getObject(conf_type));
				}
			}
			JDBCUtil.close(connection, prepareStatement, resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	
	public static Map<String, Object> selectConfAll(){
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
	
	
	
	public static String typeJudge(String attType){
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
	
	public static int getTypeValue(String conf_type){
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
	
	public static String getConfType(int type_value){
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
