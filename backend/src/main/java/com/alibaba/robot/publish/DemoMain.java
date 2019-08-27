package com.alibaba.robot.publish;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.robot.mqtt.MqttCallBackHandler;
import com.alibaba.robot.mqtt.ResonseCreatorImpl;
import com.alibaba.robot.web.manage.entity.Extra;
import com.alibaba.robot.web.manage.entity.HemaResp;
import com.google.gson.Gson;
import com.wdk.iot.hemamqtt.client.api.WdkIotHemaMqttAPI;
import com.wdk.iot.hemamqtt.enums.EnvironmentEnum;

public class DemoMain {
	public static void main2(String[] args) {
		ResonseCreatorImpl resonseCreator = new ResonseCreatorImpl();
        //WdkIotHemaMqttAPI.instance().initConnectMqttServer("cleanRobotTest", 
        					//EnvironmentEnum.ENVIRONMENT_DAILY.getIndex(), MqttCallBackHandler.instance().mqttCallback);
        WdkIotHemaMqttAPI.instance().initConnectMqttServer("cleanRobotTest", 
        					EnvironmentEnum.ENVIRONMENT_DAILY.getIndex(), MqttCallBackHandler.instance().mqttCallback);
        
        WdkIotHemaMqttAPI.instance().subscribeSchedulerCommond();
        WdkIotHemaMqttAPI.instance().publishRobotStatus("true");
        
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						HemaResp hemaResp = new HemaResp();
						Extra extra = new Extra();
						extra.setHasBody(true);
						extra.setLastPutTime(null);
						hemaResp.setExtra(extra);
						hemaResp.setRobotUniqueId("robotWXY");
						List<Integer> list2 = new ArrayList<>();
						list2.add(15);
						list2.add(25);
						hemaResp.setRobotCoordinate(list2);
						hemaResp.setTaskType(22);
						hemaResp.setTaskState(2);
						List<Integer> list = new ArrayList<>();
						list.add(123);
						list.add(456);
						hemaResp.setRobotCapacity(list);
						hemaResp.setRobotEnergy(80);
						hemaResp.setTaskId("123");
						hemaResp.setWarehouseCode("123");
						hemaResp.setPoiId("A12");
						Gson gson = new Gson();
						String json = gson.toJson(hemaResp);
						
						WdkIotHemaMqttAPI.instance().publishRobotStatus(json);
						
						Thread.sleep(5000);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		});
        
        thread.start();
        
        WdkIotHemaMqttAPI.instance().init(resonseCreator);
        
        
	}
}
