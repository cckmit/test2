package com.alibaba.robot.transport;

import com.alibaba.robot.actionlib.Feedback;
import com.alibaba.robot.actionlib.Result;
import com.alibaba.robot.actionlib.StatusArray;

public interface ITransportListener {
	
	/**
	 * Called when state of the transport has changed
	 * @param transport
	 * @param oldStateName previous state name of the transport
	 * @param newStateName
	 * @param extra extra information of the state
	 * */
	void onStateChanged(ITransport transport, StateName oldStateName, StateName newStateName);
	
	/*
	void onActionFeedback(ITransport transport, Feedback< ? extends Object> feedback);
	
	void onActionResult(ITransport transport, Result<? extends Object> result);
	
	void onActionStatus(ITransport transport, StatusArray statusArray);
	 */
	
	void onMessage(ITransport transport, final String topic, final String message);
}
