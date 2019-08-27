package com.alibaba.robot.web.manage.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.robot.business.yunqi.DrinkManager;
import com.alibaba.robot.business.yunqi.DrinkType;
import com.alibaba.robot.business.yunqi.OrderManager;
import com.alibaba.robot.business.yunqi.RobotManager;

import com.alibaba.robot.common.CookieUtils;

import com.alibaba.robot.business.yunqi.StorageType;
import com.alibaba.robot.common.ErrorCode;
import com.alibaba.robot.transport.StateName;
import com.alibaba.robot.web.manage.entity.GetRobotStatus;
import com.alibaba.robot.web.manage.entity.OrderId;
import com.alibaba.robot.web.manage.entity.PlaceOrder;
import com.alibaba.robot.web.manage.entity.PlaceOrderResult;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.entity.RobotStatusResp;
import com.alibaba.robot.web.manage.mapper.DrinkDao;
import com.alibaba.robot.web.manage.mapper.OrderStatusDao;
import com.alibaba.robot.web.manage.mapper.RobotDao;
import com.alibaba.robot.web.manage.mapper.ServiceStatusDao;
import com.alibaba.robot.web.manage.mapper.UserDao;
import com.alibaba.robot.web.manage.pojo.Drink;
import com.alibaba.robot.web.manage.pojo.Order;
import com.alibaba.robot.web.manage.pojo.OrderStatus;
import com.alibaba.robot.web.manage.pojo.Robot;
import com.alibaba.robot.web.manage.pojo.ServiceStatus;
import com.alibaba.robot.web.manage.pojo.User;
import com.alibaba.robot.web.manage.service.YunQiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class YunQiServiceImpl implements YunQiService {

	@Autowired
	private RobotDao robotDao;
	@Autowired
	private ServiceStatusDao serviceStatusDao;
	@Autowired
	private UserDao userDao;

	Gson gson = new Gson();

	// 查询饮料库存量
	public Response getDrinkInfo(Request request) {
		List<Drink> dinkList = DrinkManager.getInstance().getDrinkInfo();
		Response<List<Drink>> response = new Response<>(request.getSn(), "success", 1, dinkList);
		return response;

	}

	// 查询所有饮料库存量
	public Response getDrinkInfoEx(Request request) {
		List<Drink> dinkList = DrinkManager.getInstance().getAllDrinkInfo();
		Response<List<Drink>> response = new Response<>(request.getSn(), "success", ErrorCode.SUCCESS, dinkList);
		return response;
	}

	// 查询每桌的订单状态
	public Response<List<Order>> getPendingOrders(Request request) {
		List<Order> ordersList = OrderManager.getInstance().getPendingOrders();
		return new Response<>(request.getSn(), "success", 1, ordersList);
	}

	// 查询订单配送状态
	public Response<List<OrderStatus>> getOrderDeliveryStatus(Request request) {
		List<OrderStatus> ordersList = OrderManager.getInstance().getOrderStatus();
		return new Response<List<OrderStatus>>(request.getSn(), "success", 1, ordersList);
	}

	// 查询机器人状态
	public Response getRobotStatus(Request request) {
		String data = request.getData().toString();

		ArrayList<GetRobotStatus> getRobotStatusList = gson.fromJson(data, new TypeToken<List<GetRobotStatus>>() {
		}.getType());
		List<RobotStatusResp> robotList = new ArrayList<RobotStatusResp>();

		if (getRobotStatusList.isEmpty()) {
			List<Robot> robots = robotDao.selectAll();
			for (Robot robotByUniqueId : robots) {
				robotList.add(
						new RobotStatusResp(robotByUniqueId.getUniqueId(), robotByUniqueId.getStatus().intValue()));
			}
		} else {
			// 产品要求， 天猫查询都显示机器人可用
			for (GetRobotStatus getRobotStatus : getRobotStatusList) {
				robotList.add(new RobotStatusResp(getRobotStatus.getId(), StateName.Established.intValue()));
			}
		}

		Response<List<RobotStatusResp>> response = new Response<>(request.getSn(), "success", ErrorCode.SUCCESS,
				robotList);
		return response;

	}

	// 查询系统是否暂停
	public Response selectServiceStatusAll(Request request) {
		List<ServiceStatus> list = serviceStatusDao.selectAll();
		Response<List<ServiceStatus>> response = new Response<>(request.getSn(), "success", 1, list);
		return response;

	}

	// 用户登录系统
	public Response login(Request request, HttpServletRequest hRequest, HttpServletResponse hResponse) {

		String data = request.getData().toString();
		User user = gson.fromJson(data, User.class);
		boolean flage = false;
		User userByName = userDao.selectUserByName(user.getName());
		if (userByName.getName() != null || userByName.getName().length() > 0) {
			String password = userByName.getPassword();
			if (password != null || password.length() > 0) {
				if (password.equals(user.getPassword())) {
					flage = true;
				} else {
					flage = false;
				}
			} else {
				flage = false;
			}

		} else {
			flage = false;
		}

		Map<String, String> map = new HashMap<String, String>();
		if (flage == true) {
			String token = userByName.getToken();
			int id = userByName.getId();
			String tokenResp = String.valueOf(id) + "_" + token;
			CookieUtils.addCookie(hResponse, "token", token);
			map.put("token", tokenResp);
			Response<Map> response = new Response<Map>(request.getSn(), "success", 1, map);
			return response;
		} else {
			map.put("token", null);
			Response<Map> response = new Response<Map>(request.getSn(), "failed", 2, map);
			return response;
		}

	}

	// 饮料下单
	public Response placeOrder(Request request) {
		String data = request.getData().toString();
		PlaceOrder placeOrder = gson.fromJson(data, PlaceOrder.class);
		DrinkType drinkType = DrinkType.fromInt(placeOrder.getDrink_type());
		if (drinkType.equals(DrinkType.DrinkTypeNone)) {
			return new Response(request.getSn(), "invalid drink type", 2);
		}

		com.alibaba.robot.web.manage.entity.PlaceOrderResult placeOrderResult = OrderManager.getInstance()
				.placeOrder(placeOrder.getTable_number(), drinkType);

		Response<PlaceOrderResult> response = new Response<PlaceOrderResult>(request.getSn(), "success",
				placeOrderResult.getResult(), placeOrderResult);

		return response;
	}

	public Response<OrderStatus> queryOrderStatus(Request request) {

		Gson gson = new Gson();
		String data = request.getData().toString();
		OrderId orderId = gson.fromJson(data, OrderId.class);
		OrderStatus orderStatus = null;
		if (orderId.getOrder_id() != null && orderId.getOrder_id().length() > 0) {
			orderStatus = OrderManager.getInstance().queryOrderStatus(orderId.getOrder_id());
		} else {
			orderStatus = OrderManager.getInstance().queryOrderStatus(orderId.getTable_number());
		}
		Response<OrderStatus> response = new Response<OrderStatus>(request.getSn(), "success", 1, orderStatus);

		return response;
	}

	// 取消订单
	public Response cancelOrder(Request request) {
		String data = request.getData().toString();
		PlaceOrder order = gson.fromJson(data, PlaceOrder.class);
		Integer tableNumber = order.getTable_number();
		if (tableNumber == null || tableNumber < 1 || tableNumber > 2) {
			return new Response(request.getSn(), "bad arguments", ErrorCode.BAD_ARGUMENTS);
		}

		if (OrderManager.getInstance().cancelOrder(tableNumber)) {
			return new Response(request.getSn(), "success", ErrorCode.SUCCESS);
		}

		return new Response(request.getSn(), "failed", ErrorCode.FAILED);
	}

	// 完成订单
	public Response finishOrder(Request request) {
		return cancelOrder(request);
	}

	// 设置服务不可用
	public Response setOutOfService(Request request) {

		String data = request.getData().toString();
		PlaceOrder order = gson.fromJson(data, PlaceOrder.class);
		boolean useable = order.isUseable();
		ServiceStatus serviceStatus = new ServiceStatus();
		if (useable == true) {
			serviceStatus.setOut_of_service(false);
		} else {
			serviceStatus.setOut_of_service(true);
		}
		serviceStatusDao.updateStatus(serviceStatus);
		return new Response<>(request.getSn(), "success", 1, null);

	}

	// 调整饮料数量
	public Response increaseDrink(Request request) {

		String data = request.getData().toString();
		PlaceOrder order = gson.fromJson(data, PlaceOrder.class);
		int drink_type = order.getDrink_type();
		String drink_storage_type = order.getDrink_storage_type();
		Integer amount = order.getAmount();

		if (amount == null || drink_storage_type == null) {
			return new Response<>(request.getSn(), "bad argument", ErrorCode.BAD_ARGUMENTS);
		}

		Drink drink = new Drink();
		if ("cabinet".equals(drink_storage_type)) {
			drink.setStorage_type(1);

		}
		if ("cart".equals(drink_storage_type)) {
			drink.setStorage_type(2);
		}

		if ("reserved".equals(drink_storage_type)) {
			drink.setStorage_type(3);
		}
		if ("carrying".equals(drink_storage_type)) {
			drink.setStorage_type(4);
		}
		drink.setAmout(amount);
		drink.setType(drink_type);

		DrinkManager.getInstance().adjustDrinkAmount(DrinkType.fromInt(drink.getType()), drink.getStorage_type(),
				amount);

		return new Response<>(request.getSn(), "success", ErrorCode.SUCCESS);

	}

	public Response cancelCarryingTasks(Request request) {
		RobotManager.getInstance().cancelCarryingTasks();
		return new Response(request.getSn(), "success", ErrorCode.SUCCESS);
	}
}
