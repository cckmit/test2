package com.alibaba.robot.transport.events;

import com.alibaba.robot.actionlib.Result;
import com.alibaba.robot.transport.Event;
import com.alibaba.robot.transport.EventType;

/****
 * Action completes signs:
 * status is succeeded/cancelled/preempted/
 * result is returned/succeeded.
 * */
public class ActionCompleteEvent extends Event {
	public ActionCompleteEvent() {
		super(EventType.ActionComplete);
	}
	
	private int status = -1;
	private Result<? extends Object> result;

}
