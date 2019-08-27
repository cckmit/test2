package com.alibaba.robot.business.yunqi;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.alibaba.robot.actionlib.Status;
import com.alibaba.robot.common.RandUtil;
import com.alibaba.robot.task.ITask;
import com.alibaba.robot.task.ITaskStateListener;
import com.alibaba.robot.task.TaskManager;
import com.alibaba.robot.task.TaskState;
import com.alibaba.robot.transport.ITransport;
import com.alibaba.robot.web.manage.entity.CacheManager;
import com.alibaba.robot.web.manage.pojo.OrderStatus;
import com.google.gson.Gson;

/****
 * Instantiated only if there is no drink in cabinet <br>
 * states:<br>
 * WaitingForDrink: other robots are fetching drinks <br>
 * DeliveryingDrink:<br>
 * drink is locked<br>
 * delivering drink<br>
 * 
 * events:<br>
 * Fetch drink<br>
 * timeout<br>
 * Required drink is available and reserved<br>
 */
public class OrderStateMachine implements Runnable, ITaskStateListener {

	private static final Logger LOGGER = Logger.getLogger(OrderStateMachine.class);
	private IOrderStateListener stateListener;
	private int tableNumber;
	private long orderTimeout;
	private DrinkType drinkType;
	private String orderId;
	private long orderStartTime = System.currentTimeMillis();
	private OrderState state;
	private volatile boolean cancelled;
	private boolean completed = false;
	private static Gson gson = new Gson();
	private String goalId = RandUtil.getRandomGoalId();
	private BlockingQueue<Boolean> queue = new LinkedBlockingQueue<Boolean>();

	public OrderStateMachine(IOrderStateListener stateListener, int tableNumber, DrinkType drinkType, String orderId,
			long orderTimeout) {
		this.tableNumber = tableNumber;
		this.drinkType = drinkType;
		this.orderTimeout = orderTimeout;
		this.orderId = orderId;
		this.stateListener = stateListener;
		this.setState(OrderState.WaitForDrink);
	}

	public String getOrderId() {
		return orderId;
	}

	public String getGoalId() {
		return goalId;
	}

	public int getTableNumber() {
		return this.tableNumber;
	}

	private void setState(OrderState orderState) {
		if (orderState == null || orderState.equals(state)) {
			return;
		}

		state = orderState;
		stateListener.onOrderStateChanged(this, state);
		LOGGER.info("ORDER STATE CHANGED: tableNubmer= " + this.tableNumber + ", state = " + orderState.toString()
				+ ", order_id = " + this.orderId);
	}

	private void sleep(int timeInMillisecond) {
		try {
			Thread.sleep(timeInMillisecond);
		} catch (InterruptedException e) {
		}
	}

	public void cancel() {
		cancelled = true;
	}

	@Override
	public void run() {

		// 1. query if we have the drink
		// 2. try to reserve the drink
		// 3. choose a robot to delivery (wait for robot, if robot is unavailable)
		// 4. wait for the result
		// 5. done

		while (!cancelled && !completed) {

			if (System.currentTimeMillis() - orderStartTime > orderTimeout) {
				LOGGER.info("order timeout: " + this.orderId);
				setState(OrderState.Cancelled);
				break;
			}

			if (OrderState.WaitForDrink.equals(state)) {
				StorageInfo storageInfo = DrinkManager.getInstance().getAvailableCount(drinkType);
				if (storageInfo == null) {
					LOGGER.info("not drinks at all, cancel order: " + this.orderId);
					setState(OrderState.Cancelled);
					break;
				}

				if (storageInfo.getStorage_type() == StorageType.StorageTypeCart) {
					// trigger to carry robot and wait for a while
					sleep(3000);
					LOGGER.info("drink temporarily unavailable, wait " + this.orderId);
					continue;
				} else if (DrinkManager.getInstance().reserveCabinetDrink(drinkType)) {
					// reset timeout
					orderStartTime = System.currentTimeMillis();
					setState(OrderState.DrinkReserved);
				}
			}

			// now delivery
			if (OrderState.DrinkReserved.equals(state)) {
				int placeOrderResult = RobotManager.getInstance().deliveryDrink(tableNumber, drinkType.intValue(),
						goalId, this);
				if (placeOrderResult != RobotManager.DELIVERY_SCHEULE_SUCCESS) {

					if (RobotManager.DELIVERY_ROBOT_UNAVAILABLE == placeOrderResult) {
						LOGGER.warn("deliveryDrink failed, robot unavailable, wait " + this.orderId);
						sleep(3000);
					} else {
						LOGGER.warn("deliveryDrink failed, cancel order, result =  " + placeOrderResult
								+ ", orderId is " + this.orderId);
						setState(OrderState.Cancelled);
						break;
					}
				} else {
					// reset timeout
					orderStartTime = System.currentTimeMillis();
					setState(OrderState.Delivering);
				}
			}

			if (OrderState.Delivering.equals(state) || state.intValue() > OrderState.Delivering.intValue()) {
				// query drink state and update order sub state
				while (!completed && !cancelled) {
					try {
						queue.poll(3000, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {

					}

					int businessStatus = TaskManager.getInstance().getBusinessStatus(goalId);
					OrderState orderState = OrderState.fromInt(businessStatus);
					if (!orderState.equals(OrderState.None)) {
						setState(orderState);
					}

					int status = TaskManager.getInstance().getTaskStatus(goalId);
					if (status == Status.ABORTED || status == Status.PREEMPTED || status == Status.REJECTED
							|| status == Status.RECALLED) {
						setState(OrderState.Cancelled);
						completed = true;
						break;
					} else if (state == OrderState.Done || status == Status.SUCCEEDED) {
						setState(OrderState.Finished);
						completed = true;
						break;
					}
				}
			}
		}

		if (state != OrderState.Finished && cancelled) {
			setState(OrderState.Cancelled);
		}
	}

	@Override
	public void onTaskStarted(ITask task, ITransport transport, TaskState taskState) {
		queue.offer(true);
	}

	@Override
	public void onTaskFeedback(ITask task, ITransport transport, TaskState taskState, Object feedback) {
		OrderStatus orderStatus = new OrderStatus(tableNumber, taskState.getStatus() + 10);
		String orderStatusJson = gson.toJson(orderStatus);
		CacheManager.getInstance().set(OrderManager.ORDER_KEY_PREFIX + orderId, orderStatusJson);
		CacheManager.getInstance().set(OrderManager.ORDER_KEY_PREFIX + tableNumber, orderStatusJson);
		queue.offer(true);
	}

	@Override
	public void onTaskCompleted(ITask task, ITransport transport, TaskState taskState, Object result) {
		queue.offer(true);
	}
}
