package com.alibaba.robot.business.yunqi;

public interface IOrderStateListener {
	void onOrderStateChanged(OrderStateMachine stateMachine, OrderState newState);
}
