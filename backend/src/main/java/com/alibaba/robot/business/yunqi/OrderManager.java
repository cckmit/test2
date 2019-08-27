package com.alibaba.robot.business.yunqi;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.alibaba.robot.common.ErrorCode;
import com.alibaba.robot.web.manage.entity.CacheManager;
import com.alibaba.robot.web.manage.entity.PlaceOrderResult;
import com.alibaba.robot.web.manage.pojo.Order;
import com.alibaba.robot.web.manage.pojo.OrderStatus;
import com.google.gson.Gson;

public class OrderManager implements IOrderStateListener, ErrorCode {

	private static Logger LOGGER = Logger.getLogger(OrderManager.class);

	private OrderManager() {

	}

	private static OrderManager INSTANCE = new OrderManager();

	public static OrderManager getInstance() {
		return INSTANCE;
	}

	private static final int ORDER_TIMEOUT = 600000;

	private static final int ORDER_STATUS_READY = 1;
	private static final int ORDER_STATUS_IN_PROGRESS = 2;
	public static final String ORDER_KEY_PREFIX = "ord_";
	private static final int MAX_ORDERS = 2;
	private static Gson gson = new Gson();
	private Executor executor = Executors.newCachedThreadPool();
	private AtomicLong orderIdGen = new AtomicLong(System.currentTimeMillis());
	private Map<String, OrderStateMachine> orders = new ConcurrentHashMap<String, OrderStateMachine>();

	@Override
	public void onOrderStateChanged(OrderStateMachine stateMachine, OrderState newState) {
		if (OrderState.Finished.equals(newState) || OrderState.Cancelled.equals(newState)) {
			if (orders.remove(stateMachine.getOrderId()) != null) {
				LOGGER.info("order finished: " + stateMachine.getOrderId() + ", tableNumber: "
						+ stateMachine.getTableNumber());
			}
		}
	
		OrderStatus orderStatus = new OrderStatus(stateMachine.getTableNumber(), newState.intValue());
		String orderStatusJson = gson.toJson(orderStatus);

		CacheManager.getInstance().set(ORDER_KEY_PREFIX + stateMachine.getOrderId(), orderStatusJson);
		CacheManager.getInstance().set(ORDER_KEY_PREFIX + stateMachine.getTableNumber(), orderStatusJson);
	}

	public synchronized PlaceOrderResult placeOrder(int tableNumber, DrinkType drinkType) {

		LOGGER.info("placeOrder tableNumber = " + tableNumber + ", drinkType = " + drinkType);
		if (tableNumber < 1 || tableNumber > 2) {
			return new PlaceOrderResult(ErrorCode.BAD_ARGUMENTS, "bad arguments");
		}

		if (null == DrinkManager.getInstance().getAvailableCount(drinkType)) {
			return new PlaceOrderResult(6, "the drink is unavailable");
		}

		if (orders.size() >= MAX_ORDERS) {
			LOGGER.error("placeOrder failed, has pending order");
			return new PlaceOrderResult(PlaceOrderResult.HAS_PENDING_ORDER, "has pending order");
		}

		for (String key : orders.keySet()) {
			if (orders.get(key).getTableNumber() == tableNumber) {
				LOGGER.error("placeOrder failed, has pending order");
				return new PlaceOrderResult(PlaceOrderResult.HAS_PENDING_ORDER, "table has pending order");
			}
		}

		String orderId = Long.toString(orderIdGen.incrementAndGet());
		OrderStateMachine stateMachine = new OrderStateMachine(this, tableNumber, drinkType, orderId, ORDER_TIMEOUT);
		orders.put(orderId, stateMachine);

		executor.execute(stateMachine);
		LOGGER.error("placeOrder success, orderId = " + orderId);
		return new PlaceOrderResult(PlaceOrderResult.SUCCESS, orderId, "success");
	}

	public synchronized List<OrderStatus> getOrderStatus() {

		List<OrderStatus> orders = new LinkedList<OrderStatus>();

		for (String key : this.orders.keySet()) {
			int tableNumber = this.orders.get(key).getTableNumber();
			orders.add(new OrderStatus(tableNumber, ORDER_STATUS_IN_PROGRESS));
		}

		for (int i = 1; i <= MAX_ORDERS; i++) {
			boolean found = false;
			for (int j = 0; j < orders.size(); j++) {
				if (orders.get(j).getTable_number() == i) {
					found = true;
					break;
				}
			}
			if (!found) {
				orders.add(new OrderStatus(i, ORDER_STATUS_READY));
			}
		}

		return orders;
	}

	public synchronized List<Order> getPendingOrders() {
		List<Order> orders = new LinkedList<Order>();
		for (int i = 1; i <= MAX_ORDERS; i++) {
			boolean found = false;
			for (String key : this.orders.keySet()) {
				int tableNumber = this.orders.get(key).getTableNumber();
				if (tableNumber == i) {
					orders.add(new Order(i, true));
					found = true;
					break;
				}
			}

			if (!found) {
				orders.add(new Order(i, false));
			}
		}
		return orders;
	}

	public OrderStatus queryOrderStatus(String orderId) {

		String orderStatusJson = CacheManager.getInstance().get(ORDER_KEY_PREFIX + orderId, null);
		if (orderStatusJson == null) {
			return new OrderStatus();
		}
		OrderStatus orderStatus = gson.fromJson(orderStatusJson, OrderStatus.class);
		return orderStatus;
	}

	public OrderStatus queryOrderStatus(int tableNumber) {
		return queryOrderStatus(Integer.toString(tableNumber));
	}

	public synchronized boolean cancelOrder(int tableNumber) {

		boolean orderFound = false;

		for (String key : orders.keySet()) {
			if (orders.get(key).getTableNumber() == tableNumber) {
				orders.get(key).cancel();
				RobotManager.getInstance().cancelDeliveryTask(tableNumber);
				orderFound = true;
				break;
			}
		}

		return orderFound;

	}	
}
