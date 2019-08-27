package com.alibaba.robot.publish;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.alibaba.robot.business.yunqi.bigscreen.ScreenDataModel;
import com.alibaba.robot.common.JDBCUtil;

//奥1的状态分别包括：
//取货中：自接收到任务开始，至到达机械臂地方截止
//装货中：自到达机小车旁的械臂后停止运行，开启舱门开始，至装货完毕，关闭舱门截止
//送货中：自装货结束，开始重新运行向操作台的机械臂开始，至到达操作台的机械臂膀截止
//卸货中：自已到达操作台旁的机械臂开始，至饮料确认被取走截止，关闭舱门截止
//返程中：自已离开操作台旁机械臂开始，自返回到待机位截止
//奥2的状态分别包括：
//取货中：自接收到任务开始，至到达机械臂地方截止
//装货中：自到达机械臂后停止运行，开启舱门开始，至装货完毕，关闭舱门截止
//送货中：自装货结束，开始重新运行向指定桌号开始，至到达指定桌号截止
//收货中：自已到达指定桌号开始，至饮料确认被取走，关闭舱门截止
//返程中：自已离开桌子开始，自返回到待机位截止

public class ScreenPublisher implements IPublisher, Runnable {

	private static final int MAX_QUEUED_MESSAGE_NUM = 200;

	private static final int CRITICAL_QUEUED_MESSAGE_NUM = 500;

	private Socket socket;
	String host = "";
	int port = 0;
	
	//private static String host = ScreenDataModel.getScreenHost();
	//private static int port = ScreenDataModel.getScreenPort();

	private Logger LOGGER = Logger.getLogger(ScreenPublisher.class);

	private BlockingQueue<byte[]> arrayBlockingQueue = new LinkedBlockingQueue<byte[]>();

	public ScreenPublisher() {
		new Thread(this).start();
		new Thread(new ConfigureUpdater()).start();
	}

	class ConfigureUpdater implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				updatePort();
			}
		}
	}

	public void publish(int tag, String message) {

	}

	private void updatePort() {
		try {
			try (Connection connection = JDBCUtil.getConnection()) {
				String sql = "select * from screenip where id = 1";
				PreparedStatement prepareStatement = connection.prepareStatement(sql);
				ResultSet resultSet = prepareStatement.executeQuery();
				while (resultSet.next()) {
					host = resultSet.getString("screenIP");
					port = resultSet.getInt("port");
				}
				JDBCUtil.close(connection, prepareStatement, resultSet);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private boolean sendData(byte[] data) {
		if (data == null || data.length == 0) {
			return false;
		}

		try {
			if (socket == null) {
				if (!"".equals(host) && host != null) {
					InetAddress inetAddress = InetAddress.getByName(host);
					SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
					socket = new Socket();
					socket.connect(socketAddress, 15000);
				}
			}

			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
			return true;

		} catch (IOException e) {
			LOGGER.error(e);
			try {
				socket.close();
			} catch (IOException ex) {

			} finally {
				socket = null;
			}
		}

		return false;
	}

	@Override
	public void run() {
		updatePort();
		byte[] data;
		try {
			while ((data = arrayBlockingQueue.poll(Long.MAX_VALUE, TimeUnit.SECONDS)) != null) {
				sendData(data);
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void publish(int tag, byte[] message) {
		if (tag != DataTag.BIG_SCREEN_DATA || message == null || message.length == 0) {
			return;
		}

		int count = 0;
		while (arrayBlockingQueue.size() > MAX_QUEUED_MESSAGE_NUM && count++ < MAX_QUEUED_MESSAGE_NUM / 2) {
			arrayBlockingQueue.poll();
		}

		if (arrayBlockingQueue.size() >= CRITICAL_QUEUED_MESSAGE_NUM) {
			arrayBlockingQueue.clear();
			count += arrayBlockingQueue.size();
		}

		if (count > 0) {
			LOGGER.warn("DROPPED " + count + " messages");
		}

		arrayBlockingQueue.offer(message);
	}

}
