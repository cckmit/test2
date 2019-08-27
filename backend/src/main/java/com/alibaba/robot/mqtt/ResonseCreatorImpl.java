package com.alibaba.robot.mqtt;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.google.gson.Gson;
import com.wdk.iot.hemamqtt.client.api.ResponseCreator;

@Component
public class ResonseCreatorImpl implements ResponseCreator {

	private Logger LOGGER = Logger.getLogger(ResonseCreatorImpl.class);
	private DataTransform dataTransform = new DataTransform();

	@Override
	public String genResponse(String commondType, String requestJsonString) {

		LOGGER.info("COMMAND TYPE: " + commondType);
		LOGGER.info("REQUEST\r\n: " + requestJsonString);

		Request request = dataTransform.pack(requestJsonString.toString());
		Response response = dataTransform.call(commondType, request);

		Gson gson = new Gson();
		String responseStr = gson.toJson(response);
		LOGGER.info("RESPONSE: \r\n" + responseStr);
		return responseStr;
	}
}