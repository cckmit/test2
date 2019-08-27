package com.alibaba.robot.web.websocket;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.robot.business.yunqi.RobotManager;

@ServerEndpoint(value = "/robot_status/{token}")
@Component
public class RobotStatusEndPoint {
	
	private static Logger LOGGER = Logger.getLogger(RobotStatusEndPoint.class);
	
	private static Set<RobotStatusEndPoint> robotStatusEndpoints = new CopyOnWriteArraySet<RobotStatusEndPoint>();
	
	private Session session;
	
	private static void removeEndpoint(RobotStatusEndPoint endpoint) {
		robotStatusEndpoints.remove(endpoint);
	}
	
	/***
	 * publish data to WebSocket stream<br>
	 * TODO: make it asynchronous
	 * */
	public static void publish(String message) {
		List<RobotStatusEndPoint> endpointsToRemove = new LinkedList<RobotStatusEndPoint>();
		for(RobotStatusEndPoint endpoint : robotStatusEndpoints) {
			try {
				endpoint.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				endpointsToRemove.add(endpoint);
			}  
		}
		
		for(RobotStatusEndPoint endpoint : endpointsToRemove) {
			removeEndpoint(endpoint);
		}
	}
	
	@OnOpen
	public void onOpen(Session session, @PathParam("token") String token) throws IOException {
		LOGGER.info("onOpen: " + session.getId());
		this.session = session;
		String text = RobotManager.getInstance().getCustomizedRobotInfo();
		if(text != null && text.length() > 0) {
			session.getBasicRemote().sendText(text);
		}
		robotStatusEndpoints.add(this);
	}

	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		LOGGER.info("onMessage: " + message);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		LOGGER.info("onClose");	 
		removeEndpoint(this);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		throwable.printStackTrace();
		LOGGER.error("onError: " + throwable);	 
		removeEndpoint(this);
	}
}
