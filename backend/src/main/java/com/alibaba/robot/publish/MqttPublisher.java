package com.alibaba.robot.publish;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.wdk.iot.hemamqtt.client.api.WdkIotHemaMqttAPI;
import com.wdk.iot.hemamqtt.result.HMResult;

@Component
public class MqttPublisher implements IPublisher, Runnable {
	Logger LOGGER = Logger.getLogger(MqttPublisher.class);

	private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();

	@Override
	public void publish(int tag, String message) {
		if (tag == DataTag.DATA_FOR_HEMA) {
			messageQueue.offer(message);
		}
	}

	public MqttPublisher() {
		init();
	}

	private void init() {
		new Thread(this).start();
	}

	private void doPublish(String message) {
		if (message == null) {
			return;
		}

		HMResult<Boolean> result = WdkIotHemaMqttAPI.instance().publishRobotStatus(message);
		if (result == null || result.getErrorCode() == null) {
			LOGGER.info("published: " + message);
		} else {
			LOGGER.info("ppublish failed:\r\nerror: " + result.getErrorCode() + "\r\nerror message: "
					+ result.getErrorMsg() + "\r\nmessage:\r\n" + message);
		}
	}

	@Override
	public void run() {
		String message = null;
		while (true) {
			try {
				message = messageQueue.poll(Long.MAX_VALUE, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				break;
			}
			doPublish(message);
		}

		LOGGER.info("shuting down...");
	}

	@Override
	public void publish(int tag, byte[] binary) {
		
	}
}
