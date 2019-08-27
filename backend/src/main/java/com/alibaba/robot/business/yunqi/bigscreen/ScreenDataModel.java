package com.alibaba.robot.business.yunqi.bigscreen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alibaba.robot.common.JDBCUtil;
import com.alibaba.robot.web.manage.entity.ScreenMessage;

public class ScreenDataModel {
	
	
	public static int[] selectTimesAndHistoryOld(int arm_id, int robot_id) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlSelect = "select times, timesHistory from armscreen where armId = ? and robotId = ?";
		PreparedStatement psSelect = connection.prepareStatement(sqlSelect);
		psSelect.setInt(1, arm_id);
		psSelect.setInt(2, robot_id);
		ResultSet resultSelect = psSelect.executeQuery();
		int[] timesAndHistory = new int[5];
		while(resultSelect.next()){
			timesAndHistory[0] = resultSelect.getInt("times");
			timesAndHistory[1] = resultSelect.getInt("timesHistory");
		}
		JDBCUtil.close(connection, psSelect, resultSelect);
		return timesAndHistory;
	}
	
	public static int updateArmTimesLessOld(int arm_id, int robot_id, int times, int timesHistory) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlUpdate = "update armscreen set times = ? , timesHistory = ? where armId = ? and robotId = ?";
		PreparedStatement ps = connection.prepareStatement(sqlUpdate);
		ps.setInt(1, times);
		ps.setInt(2, timesHistory);
		ps.setInt(3, arm_id);
		ps.setInt(4, robot_id);
		int update = ps.executeUpdate();
		JDBCUtil.close(connection, ps);
		return update;
	}
	
	public static int updateArmTimesGreaterOld(int arm_id, int robot_id, int times) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlUpdateLittle = "update armscreen set times = ? where armId = ? and robotId = ?";
		PreparedStatement psUpdateLittle = connection.prepareStatement(sqlUpdateLittle);
		psUpdateLittle.setInt(1, times);
		psUpdateLittle.setInt(2, arm_id);
		psUpdateLittle.setInt(3, robot_id);
		int update = psUpdateLittle.executeUpdate();
		JDBCUtil.close(connection, psUpdateLittle);
		return update;
	}
	
	public static int selectArmTimesSum(int arm_id) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlSumlittle = "select sum(times) st, sum(timesHistory) sh from armscreen where armId = ?";
		PreparedStatement psSumlittle = connection.prepareStatement(sqlSumlittle);
		psSumlittle.setInt(1, arm_id);
		ResultSet resultSetSum = psSumlittle.executeQuery();
		int sumTimes = 0;
		while (resultSetSum.next()) {
			sumTimes = resultSetSum.getInt("st") + resultSetSum.getInt("sh");
		}
		JDBCUtil.close(connection, psSumlittle, resultSetSum);
		return sumTimes;
	}
	
	public static int[] selectArmTotalAndHistory(int arm_id, int robot_id) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlSelectNext = "select total, totalHistory from armscreen where armId = ? and robotId = ?";
		PreparedStatement psSelectNext = connection.prepareStatement(sqlSelectNext);
		psSelectNext.setInt(1, arm_id);
		psSelectNext.setInt(2, robot_id);
		ResultSet resultSelectNext = psSelectNext.executeQuery();
		int[] totalAndHistory = new int[5];
		while (resultSelectNext.next()) {
			totalAndHistory[0] = resultSelectNext.getInt("total");
			totalAndHistory[1] = resultSelectNext.getInt("totalHistory");
		}
		JDBCUtil.close(connection, psSelectNext, resultSelectNext);
		return totalAndHistory;
	}
	
	public static int updateArmTotalLessOld(int total, int totalHistory, int arm_id, int robot_id) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlTotal = "update armscreen set total = ? , totalHistory = ? where armId = ? and robotId = ?";
		PreparedStatement psTotal = connection.prepareStatement(sqlTotal);
		psTotal.setInt(1, total);
		psTotal.setInt(2, totalHistory);
		psTotal.setInt(3, arm_id);
		psTotal.setInt(4, robot_id);
		int update = psTotal.executeUpdate();
		JDBCUtil.close(connection, psTotal);
		return update;
	}
	
	public static int updateArmTotalGreaterOld(int total, int arm_id, int robot_id) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlUP = "update armscreen set total = ? where armId = ? and robotId = ?";
		PreparedStatement psUP = connection.prepareStatement(sqlUP);
		psUP.setInt(1, total);
		psUP.setInt(2, arm_id);
		psUP.setInt(3, robot_id);
		int update = psUP.executeUpdate();
		JDBCUtil.close(connection, psUP);
		return update;
	}
	
	public static int selectArmTotalSum(int arm_id) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlUPSum = "select sum(total) st, sum(totalHistory) sh from armscreen where armId = ?";
		PreparedStatement psUPSum = connection.prepareStatement(sqlUPSum);
		psUPSum.setInt(1, arm_id);
		ResultSet resultSetUPSum = psUPSum.executeQuery();
		int totalSum = 0;
		while (resultSetUPSum.next()) {
			totalSum = resultSetUPSum.getInt("st") + resultSetUPSum.getInt("sh");
		}
		JDBCUtil.close(connection, psUPSum, resultSetUPSum);
		return totalSum;
	}
	
	public static int[] selectRobotTotalAndHistoryOld(int robot_id) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlSelectTT = "select total, totalHistory from robotscreen where robotId = ?";
		PreparedStatement psSelectTT = connection.prepareStatement(sqlSelectTT);
		psSelectTT.setInt(1, robot_id);
		ResultSet resultSetSelectTT = psSelectTT.executeQuery();
		int[] totalAndHistoryOld = new int[5];
		while (resultSetSelectTT.next()) {
			totalAndHistoryOld[0] = resultSetSelectTT.getInt("total");
			totalAndHistoryOld[1] = resultSetSelectTT.getInt("totalHistory");
		}
		JDBCUtil.close(connection, psSelectTT, resultSetSelectTT);
		return totalAndHistoryOld;
	}
	
	public static int updateRobotToalLessOld(int total, int totalHistory, int robot_id) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlUpdateTotal = "update robotscreen set total = ? , totalHistory = ? where robotId = ?";
		PreparedStatement psUpdateTotal = connection.prepareStatement(sqlUpdateTotal);
		psUpdateTotal.setInt(1, total);
		psUpdateTotal.setInt(2, totalHistory);
		psUpdateTotal.setInt(3, robot_id);
		int update = psUpdateTotal.executeUpdate();
		JDBCUtil.close(connection, psUpdateTotal);
		return update;
	}
	
	public static int updateRobotToalGreaterOld(int total, int robot_id) throws SQLException{
		Connection connection = JDBCUtil.getConnection();
		String sqlUpdate = "update robotscreen set total = ? where robotId = ?";
		PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate);
		psUpdate.setInt(1, total);
		psUpdate.setInt(2, robot_id);
		int update = psUpdate.executeUpdate();
		JDBCUtil.close(connection, psUpdate);
		return update;
	}
	
	public static String getScreenHost(){
		Connection connection = JDBCUtil.getConnection();
		String host = "";
		String sql = "select screenIP from screenip where id = 1";
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		try {
			prepareStatement = connection.prepareStatement(sql);
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				host = resultSet.getString("screenIP");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(connection, prepareStatement, resultSet);
		}
		return host;
	}
	
	public static int getScreenPort(){
		Connection connection = JDBCUtil.getConnection();
		int port = 0;
		String sql = "select port from screenip where id = 1";
		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;
		try {
			prepareStatement = connection.prepareStatement(sql);
			resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				port = resultSet.getInt("port");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(connection, prepareStatement, resultSet);
		}
		return port;
	}
	
}
