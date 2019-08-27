
package com.alibaba.robot.transport;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.robot.business.hema.RobotMonitor;
import com.alibaba.robot.business.yunqi.DrinkManager;
import com.alibaba.robot.business.yunqi.RobotManager;
import com.alibaba.robot.common.BuildVariants;
import com.alibaba.robot.common.DbUtil;
import com.alibaba.robot.status.StatusManager;
import com.alibaba.robot.task.TaskManager;
import com.alibaba.robot.web.manage.pojo.Robot;

public class TransportMonitor implements Runnable {
	private static Logger LOGGER = Logger.getLogger(TransportMonitor.class);
	private static TransportMonitor INSTANCE = new TransportMonitor();
	private Thread thread;

	public static TransportMonitor getInstance() {
		return INSTANCE;
	}

	private TransportMonitor() {

	}

	public synchronized void start() {
		if (thread != null) {
			return;
		}
		thread = new Thread(this);
		thread.start();
	}

	private static HashMap<Integer, Robot> runningRobots = new HashMap<Integer, Robot>();

	public void updateTransport() {

		List<Robot> allRobots = DbUtil.getAllRobots();
		HashMap<Integer, Robot> robotsFromDb = new HashMap<Integer, Robot>();

		for (Robot item : allRobots) {
			robotsFromDb.put(item.getId(), item);
		}

		for (Integer key : robotsFromDb.keySet()) {

			Robot robot = robotsFromDb.get(key);

			if (runningRobots.containsKey(key)) {

				if (robotsFromDb.get(key).getStatus() != TransportManager.ROBOT_DISABLED
						&& runningRobots.get(key).getAddress().equals(robotsFromDb.get(key).getAddress())) {
					continue;
				}

				TransportManager.getInstance().removeTransport(key.intValue());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}

				if (!runningRobots.get(key).getAddress().equals(robotsFromDb.get(key).getAddress())) {
					TransportManager.getInstance().addTransport(key.intValue(), robot.getAddress(),
							robot.getRobotType(), robot.getModel());
					runningRobots.put(key, robotsFromDb.get(key));

					LOGGER.debug("Update robot transport: " + robotsFromDb.get(key).getAddress());
					
				} else {
					runningRobots.remove(key);
					LOGGER.debug("Remove robot transport: " + robotsFromDb.get(key).getAddress());
				}

			} else if (robot.getStatus() != TransportManager.ROBOT_DISABLED) {

				TransportManager.getInstance().addTransport(key.intValue(), robot.getAddress(), robot.getRobotType(),
						robot.getModel());
				runningRobots.put(key, robotsFromDb.get(key));

				LOGGER.debug("Add robot transport: " + robotsFromDb.get(key).getAddress());
			}
		}

		for (Integer key : runningRobots.keySet()) {
			if (!robotsFromDb.containsKey(key)) {
				TransportManager.getInstance().removeTransport(key.intValue());
				LOGGER.debug("Remove robot transport: " + runningRobots.get(key).getAddress());
				runningRobots.remove(key);
			}
		}
	}

	@Override
	public void run() {

		DrinkManager.getInstance().checkDrinksNow();

		while (true) {
			try {
				updateTransport();
				Thread.sleep(15000);
				// String str = RobotStatusManager.getRobotInfo();
				// LOGGER.info(str);
			} catch (InterruptedException e) {
				e.printStackTrace();
				LOGGER.error("transport refesh thread interrupted.");
				break;
			}
		}

	}
}
