package com.alibaba.robot.common;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 连接池工具类
 *
 */
public class JDBCUtil {
	
	private static DataSource dataSource = null;
	
	static{
		try {
			Properties properties = new Properties();
			properties.load(JDBCUtil.class.getResourceAsStream("/druid.properties"));
			dataSource = DruidDataSourceFactory.createDataSource(properties);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到数据源
	 */
	public static DataSource getDataSource(){
		return dataSource;
	}
	
	/**
	 * 获取连接对象
	 */
	public static Connection getConnection(){
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 释放资源
	 */
	public static void close(Connection connection, Statement statement, ResultSet resultSet){
		
		if(resultSet != null){
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(statement != null){
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void close(Connection connection, Statement statement){
		close(connection, statement, null);
	}
	
	
}
