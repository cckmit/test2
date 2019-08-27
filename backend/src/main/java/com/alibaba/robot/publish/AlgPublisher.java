package com.alibaba.robot.publish;

import com.alibaba.robot.web.websocket.RobotStatusEndPoint;

/***
 * Customized data publisher for algorithm team
 * */
public class AlgPublisher implements IPublisher {

	@Override
	public void publish(int tag, String content) {
		if(tag != DataTag.CUSTOMIZED_DATA_FOR_ALG) {
			return;
		}
		
		RobotStatusEndPoint.publish(content);
	}

	@Override
	public void publish(int tag, byte[] binary) {
		// TODO Auto-generated method stub
		
	}
}
