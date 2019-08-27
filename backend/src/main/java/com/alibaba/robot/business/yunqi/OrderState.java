package com.alibaba.robot.business.yunqi;

/***
 * 20 开始<br>
 * 21 机器人完成取货<br>
 * 22 机器人已到达<br>
 * 23 等待用户取出饮料<br>
 * 100 完成<br>
 */
public enum OrderState {

	None(0),
	
	/***
	 * 饮料不可用，等待饮料
	 * */
	WaitForDrink(1),

	/***
	 * 饮料已经预定
	 * */
	DrinkReserved(2),

	/***
	 * 机器人已经被调度，开始执行任务
	 * */
	Delivering(3),

	/***
	 * 订单正常完成
	 * */
	Finished(4),

	/***
	 * 订单被取消
	 * */
	Cancelled(5),

	/***
	 * 机器人前往货柜取货
	 */
	RobotStarted(20),

	/**
	 * 机器人完成取货
	 */
	DrinkPickedUp(21),

	/***
	 * 带饮料的机器人已经抵达
	 */
	RobotArrived(22),

	/***
	 * 等待用户取出饮料
	 */
	WaitForUserPickUp(23),

	/***
	 * 流程完成
	 */
	Done(100);

	OrderState(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}
	
	public static OrderState fromInt(int value) {
		for(OrderState state: values()) {
			if(state.intValue() == value) {
				return state;
			}
		}
		return None;
	}

	private int value;

}