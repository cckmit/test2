package com.alibaba.robot.publish;

public interface IPublisher {
	/***
	 * Publish data<br>
	 * @param tag data tag
	 * @param content content to publish
	 * */
	void publish(int tag, String content);
	
	
	void publish(int tag, byte[] binary);
}
