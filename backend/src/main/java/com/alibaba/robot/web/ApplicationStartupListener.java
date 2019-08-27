package com.alibaba.robot.web;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.robot.business.hema.RobotMonitor;
import com.alibaba.robot.business.yunqi.RobotManager;
import com.alibaba.robot.common.BuildVariants;
import com.alibaba.robot.mqtt.MqttCallBackHandler;
import com.alibaba.robot.mqtt.ResonseCreatorImpl;
import com.alibaba.robot.status.StatusManager;
import com.alibaba.robot.task.TaskManager;
import com.alibaba.robot.transport.TransportManager;
import com.alibaba.robot.transport.TransportMonitor;
import com.wdk.iot.hemamqtt.client.api.WdkIotHemaMqttAPI;
import com.wdk.iot.hemamqtt.enums.EnvironmentEnum;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger LOGGER = Logger.getLogger(ApplicationStartupListener.class);
	private static String HEMA_MQTT_ID = "cleanRobotHema";

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		TransportMonitor.getInstance().start();
		init();
	}

	void init() {

		TransportManager.getInstance().addListner(TaskManager.getInstance());
		TransportManager.getInstance().addListner(StatusManager.getInstance());

		if (BuildVariants.YUNQI_ENABLED) {
			LOGGER.info("init YunQi business");
			TransportManager.getInstance().addListner(RobotManager.getInstance());
			TaskManager.getInstance().addListener(RobotManager.getInstance());
		} else if (BuildVariants.HEMA_ENABLED) {
			LOGGER.info("init HeMa business");
			TaskManager.getInstance().addListener(RobotMonitor.getInstance());
			if (BuildVariants.HEMA_MQTT_ENABLED) {
				initMqtt();
				TransportManager.getInstance().addListner(RobotMonitor.getInstance());
			}
		}
	}

	void initMqtt() {

		WdkIotHemaMqttAPI.instance().init(new ResonseCreatorImpl());

		WdkIotHemaMqttAPI.instance().initConnectMqttServer(HEMA_MQTT_ID, EnvironmentEnum.ENVIRONMENT_DAILY.getIndex(),
				MqttCallBackHandler.instance().mqttCallback);

		WdkIotHemaMqttAPI.instance().subscribeSchedulerCommond();
	}
}
