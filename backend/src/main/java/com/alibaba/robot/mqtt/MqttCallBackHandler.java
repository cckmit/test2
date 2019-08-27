package com.alibaba.robot.mqtt;
import com.wdk.iot.hemamqtt.util.LogUtil;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallBackHandler {

	private Logger LOGGER = Logger.getLogger(MqttCallBackHandler.class);
	private static boolean ENABLE_LOGGING = true;
    private static class LazyHolder {
        public static final MqttCallBackHandler INSTANCE = new MqttCallBackHandler();
    }

    public static MqttCallBackHandler instance() {
        return MqttCallBackHandler.LazyHolder.INSTANCE;
    }

    public MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            
        	//LOGGER.warn("connectionLost: " + cause);
        	 LogUtil.print("连接失败,原因:" + cause);
            cause.printStackTrace();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
        	if(ENABLE_LOGGING) {
            LogUtil.print("接收到消息,来至Topic [" + topic + "] , 内容是:["
                + new String(message.getPayload(), "UTF-8") + "],  ");
        	}
            
        	//LOGGER.info("message from Topic [" + topic + "] , content:["
           //         + new String(message.getPayload(), "UTF-8") + "],  ");
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            //如果是QoS0的消息，token.resp是没有回复的
            
        	// LOGGER.info("message sent: "+ ((token == null || token.getResponse() == null) ? "null"
            //        : token.getResponse().getKey()));
        	if(ENABLE_LOGGING) {
        	
        	 LogUtil.print("消息发送成功! " + ((token == null || token.getResponse() == null) ? "null"
                 : token.getResponse().getKey()));
        	} 
        }
    };

}
