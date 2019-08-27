package com.alibaba.robot.common;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.robot.web.manage.pojo.Location;
import com.alibaba.robot.web.manage.pojo.Robot;

import java.sql.Connection;

public class DbUtil {

	// TODO: do not hard code
	private static final String URL = "jdbc:mysql://localhost:3306/robotmanage?verifyServerCertificate=false&useSSL=false&autoReconnect=true";

	private static final String USER = "root";
	private static final String PASSWORD = "root";

	public static List<Location> getLocations(List<Integer> idList) {

		List<Location> locationList = new LinkedList<Location>();
		Statement st = null;
		Connection conn = null;
		ResultSet rs = null;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < idList.size(); i++) {
			if (i != idList.size() - 1) {
				sb.append(Integer.toString(idList.get(i)) + ",");
			} else {
				sb.append(Integer.toString(idList.get(i)));
			}
		}

		try {

			// 1.加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// 2.获得数据库链接
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			conn.setSchema("robotmanage");

			// 3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
			st = conn.createStatement();

			String sql = "select * from location where id in (" + sb.toString() + ");";
			rs = st.executeQuery(sql);

			// location
			// 4.处理数据库的返回结果(使用ResultSet类)
			while (rs.next()) {
				Location location = new Location(rs.getInt("id"), rs.getString("floor"), rs.getString("name"),
						rs.getDouble("w"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
				locationList.add(location);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return locationList;
	}
	
	
	
	public static List<Location> getLocationsByName(List<String> nameList) {

		List<Location> locationList = new LinkedList<Location>();
		PreparedStatement st = null;
		ResultSet rs = null;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nameList.size(); i++) {
			if (i != nameList.size() - 1) {
				sb.append( "?,");
			} else {
				sb.append("?");
			}
		}

		try {
			 
			try(Connection conn = JDBCUtil.getConnection()){

			// 3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
			st = conn.prepareStatement("select * from location where name in (" + sb.toString() + ");");
			for (int i = 1; i <= nameList.size(); i++) {
				st.setString(i, nameList.get(i-1));
			}
			 
			rs = st.executeQuery( );

			// location
			// 4.处理数据库的返回结果(使用ResultSet类)
			while (rs.next()) {
				Location location = new Location(rs.getInt("id"), rs.getString("floor"), rs.getString("name"),
						rs.getDouble("w"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
				locationList.add(location);
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return locationList;
	}
	
	
	

	public static Location getLocationById(int id) {
		Statement st = null;
		Connection conn = null;
		ResultSet rs = null;
		Location location = null;
		try {

			// 1.加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// 2.获得数据库链接
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			conn.setSchema("robotmanage");

			// 3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
			st = conn.createStatement();

			rs = st.executeQuery("select * from location where id = " + id);

			// location
			// 4.处理数据库的返回结果(使用ResultSet类)
			while (rs.next()) {

				location = new Location(rs.getInt("id"), rs.getString("floor"), rs.getString("name"), rs.getDouble("w"),
						rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return location;
	}

	public static Connection getConnection() {

		Connection conn = null;

		try {
			// 1.加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// 2.获得数据库链接
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static Location getLocationByName(String name) {
		PreparedStatement st = null;
		Connection conn = null;
		ResultSet rs = null;
		Location location = null;
		if (name == null || name.length() == 0) {
			return null;
		}

		try {

			// 1.加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// 2.获得数据库链接
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			conn.setSchema("robotmanage");

			// 3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）

			st = conn.prepareStatement("select * from location where name = ?");
			st.setString(1, name);
			// TODO: SQL injection
			rs = st.executeQuery();

			// location
			// 4.处理数据库的返回结果(使用ResultSet类)
			while (rs.next()) {

				location = new Location(rs.getInt("id"), rs.getString("floor"), rs.getString("name"), rs.getDouble("w"),
						rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return location;
	}

	public static List<Robot> getAllRobots() {
		Statement st = null;
		Connection conn = null;
		ResultSet rs = null;
		List<Robot> robots = new LinkedList<Robot>();

		try {

			// 1.加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// 2.获得数据库链接
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			conn.setSchema("robotmanage");

			// 3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
			st = conn.createStatement();

			rs = st.executeQuery("select * from robot");

			// location
			// 4.处理数据库的返回结果(使用ResultSet类)
			while (rs.next()) {

				Robot robot = new Robot(rs.getInt("id"), rs.getString("name"), rs.getString("address"),
						rs.getString("model"), rs.getString("firmware"), rs.getString("version"), rs.getInt("status"),
						rs.getString("uniqueId"), rs.getInt("robotType"));

				robots.add(robot);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return robots;
	}

	public static Robot getRobotById(String id) {
		PreparedStatement st = null;
		Connection conn = null;
		ResultSet rs = null;
		Robot robot = null;

		try {

			// 1.加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// 2.获得数据库链接
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			conn.setSchema("robotmanage");
			// 3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
			//st = conn.createStatement();
			
			
			st = conn.prepareStatement("select * from robot where id = ?");
			st.setString(1, id);
			 
			rs = st.executeQuery();
			
			// rs = st.executeQuery("select * from robot where id =  " + id);
			// location
			// 4.处理数据库的返回结果(使用ResultSet类)
			while (rs.next()) {

				robot = new Robot(rs.getInt("id"), rs.getString("name"), rs.getString("address"), rs.getString("model"),
						rs.getString("firmware"), rs.getString("version"), rs.getInt("status"),
						rs.getString("uniqueId"), rs.getInt("robotType"));

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return robot;
	}

}
