package com.alibaba.robot.business.yunqi.bigscreen;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.alibaba.robot.flatbuffers.FlatBufferBuilder;
import com.alibaba.robot.transport.ITransport;
import com.alibaba.robot.web.manage.entity.ScreenMessage;
import com.google.gson.Gson;

/***
 * {"status":0,"angle":1.2000000476837158,"y":2.0,"odom":24.0,"device":1,"x":1.0,"total":120,"operation_times":22}<br>
 * status: 机械手臂状态<br>
 * 0: 开始动作<br>
 * 1: 停止动作<br>
 * 
 * device:<br>
 * 1 ~ 3 机械手臂<br>
 * 4 机器人<br>
 */

// @Component
public class ScreenDataParser {

	private static Logger LOGGER = Logger.getLogger(ScreenDataParser.class);
	private static Gson gson = new Gson();
	private static int A1_id = 1;
	private static int A2_id = 3;

	public byte[] parse(ITransport transport, String message) throws SQLException {

		if (transport != null) {
			if (message != null && message.length() > 0) {
				Gson gson = new Gson();
				ScreenMessage screenMessage = gson.fromJson(message, ScreenMessage.class);
				byte[] data = paseMessage(screenMessage, transport);
				return data;
			} else {
				return new byte[0];
			}
		} else {

			return new byte[0];
		}

	}

	public byte[] paseMessage(ScreenMessage screenMessage, ITransport transport) throws SQLException {

		ScreenMessage toScreenMessage = new ScreenMessage();

		/**
		 * 大屏 1 2 3 4是机器人, 5 6 7号机械臂
		 * 
		 * 接收的数据device 1 2 3是机械臂 4 是机器人
		 */
		int device = screenMessage.getDevice();
		int operation_times = screenMessage.getOperation_times();
		int total = screenMessage.getTotal();
		int robot_id = transport.getId();
		if (device <= 3) { // robot arm

			toScreenMessage.setArm_id(device + 4);

			int[] selectTimesAndHistoryOld = ScreenDataModel.selectTimesAndHistoryOld(toScreenMessage.getArm_id(),
					robot_id);
			int timesOld = selectTimesAndHistoryOld[0];
			int timesHistoryOld = selectTimesAndHistoryOld[1];

			if (operation_times < timesOld) {
				int update = ScreenDataModel.updateArmTimesLessOld(toScreenMessage.getArm_id(), robot_id,
						operation_times, timesOld + timesHistoryOld);
				//LOGGER.info(update);
			} else {
				int update = ScreenDataModel.updateArmTimesGreaterOld(toScreenMessage.getArm_id(), robot_id,
						operation_times);
				//LOGGER.info(update);
			}

			int selectArmTimesSum = ScreenDataModel.selectArmTimesSum(toScreenMessage.getArm_id());
			toScreenMessage.setOperation_times(selectArmTimesSum);

			int[] selectArmTotalAndHistory = ScreenDataModel.selectArmTotalAndHistory(toScreenMessage.getArm_id(),
					robot_id);
			int totalOld = selectArmTotalAndHistory[0];
			int totalHistoryOld = selectArmTotalAndHistory[1];

			if (total < totalOld) {
				int update = ScreenDataModel.updateArmTotalLessOld(total, totalOld + totalHistoryOld,
						toScreenMessage.getArm_id(), robot_id);
				//LOGGER.info(update);
			} else {
				int update = ScreenDataModel.updateArmTotalGreaterOld(total, toScreenMessage.getArm_id(), robot_id);
				//LOGGER.info(update);
			}

			int totalSum = ScreenDataModel.selectArmTotalSum(toScreenMessage.getArm_id());
			toScreenMessage.setTotal(totalSum);

		} else {
			// 机器人
			int[] totalAndHistoryOld = ScreenDataModel.selectRobotTotalAndHistoryOld(robot_id);
			int totalOld = totalAndHistoryOld[0];
			int totalHistoryOld = totalAndHistoryOld[1];

			if (total < totalOld) {
				int update = ScreenDataModel.updateRobotToalLessOld(total, totalOld + totalHistoryOld, robot_id);
				//LOGGER.info(update);
				toScreenMessage.setTotal(total + totalOld + totalHistoryOld);
			} else {
				int update = ScreenDataModel.updateRobotToalGreaterOld(total, robot_id);
				//LOGGER.info(update);
				toScreenMessage.setTotal(total + totalHistoryOld);
			}
		}

		if (robot_id == 1 || robot_id == 2) {
			toScreenMessage.setRobot_id(A1_id);
		} else if (robot_id == 3 || robot_id == 4) {
			toScreenMessage.setRobot_id(A2_id);
		}

		toScreenMessage.setAngle(screenMessage.getAngle());
		toScreenMessage.setOdom(screenMessage.getOdom());
		toScreenMessage.setStatus(screenMessage.getStatus());
		toScreenMessage.setX(screenMessage.getX());
		toScreenMessage.setY(screenMessage.getY());

		// LOGGER.info("ENCODED MESSAGE: " + gson.toJson(toScreenMessage));

		byte[] data = getData(toScreenMessage, device);
		return data;
	}

	public byte[] getData(ScreenMessage toScreenMessage, int device) {
		FlatBufferBuilder flatBufferBuilder = new FlatBufferBuilder();
		int robot_data_id = Robot_Data.createRobot_Data(flatBufferBuilder, toScreenMessage.getX(),
				toScreenMessage.getY(), toScreenMessage.getAngle(), toScreenMessage.getOdom(),
				toScreenMessage.getTotal(), toScreenMessage.getStatus(), toScreenMessage.getOperation_times());
		int robot_info_id = -1;
		if (device <= 3) {
			robot_info_id = Robot_Info.createRobot_Info(flatBufferBuilder, RobotType.Arm, toScreenMessage.getArm_id(),
					robot_data_id);

		} else {
			robot_info_id = Robot_Info.createRobot_Info(flatBufferBuilder, RobotType.Robot,
					toScreenMessage.getRobot_id(), robot_data_id);
		}

		Robot_Info.finishSizePrefixedRobot_InfoBuffer(flatBufferBuilder, robot_info_id);

		byte[] data = flatBufferBuilder.sizedByteArray();
		return data;
	}

}
