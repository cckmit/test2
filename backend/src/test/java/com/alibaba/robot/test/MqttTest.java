package com.alibaba.robot.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.alibaba.robot.mqtt.MqttCallBackHandler;
import com.alibaba.robot.mqtt.ResonseCreatorImpl;
import com.alibaba.robot.publish.PublishManager;
import com.alibaba.robot.web.manage.entity.Extra;
import com.alibaba.robot.web.manage.entity.HemaResp;
import com.google.gson.Gson;
import com.wdk.iot.hemamqtt.client.api.WdkIotHemaMqttAPI;
import com.wdk.iot.hemamqtt.enums.EnvironmentEnum;

public class MqttTest {
	
	@Test
	public void testMqtt(){
		ResonseCreatorImpl resonseCreator = new ResonseCreatorImpl();
        WdkIotHemaMqttAPI.instance().initConnectMqttServer("cleanRobotTest", EnvironmentEnum.ENVIRONMENT_DAILY.getIndex(), MqttCallBackHandler.instance().mqttCallback);
        
        HemaResp hemaResp = new HemaResp();
        Extra extra = new Extra();
        extra.setHasBody(true);
        extra.setLastPutTime(null);
        hemaResp.setExtra(extra);
        List<Integer> list = new ArrayList<>();
        list.add(123);
        list.add(456);
        hemaResp.setRobotCapacity(list);
        hemaResp.setRobotEnergy(23);
        hemaResp.setTaskId("123");
        hemaResp.setWarehouseCode("123");
        Gson gson = new Gson();
        String json = gson.toJson(hemaResp);
        
        WdkIotHemaMqttAPI.instance().publishRobotStatus(json);
        
        resonseCreator.genResponse("robot_add_info", "{'sn': '1230120312313','token': '1_admintoken','command': 'getinfo','data': {'robotUniqueId':'robotXYZ_new','warehouseCode':'test_warehouse'}}");
        
        WdkIotHemaMqttAPI.instance().init(resonseCreator);
	}
	
}
