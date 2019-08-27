package com.alibaba.robot.web.manage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.pojo.Order;
import com.alibaba.robot.web.manage.pojo.OrderStatus;

public interface YunQiService {
	
	//查询饮料库存量
	Response getDrinkInfo(Request request);
	
	//查询所有饮料库存量
	Response getDrinkInfoEx(Request request);
	
	//查询每桌的订单状态
	Response<List<Order>> getPendingOrders(Request request);
	
	//查询订单配送状态
	Response<List<OrderStatus>> getOrderDeliveryStatus(Request request);
	
	//查询机器人状态
	Response getRobotStatus(Request request);
	
	//查询系统是否暂停
	Response selectServiceStatusAll(Request request);
	
	//用户登录系统
	Response login(Request request, HttpServletRequest hRequest, HttpServletResponse hResponse);
	
	//饮料下单
	Response placeOrder(Request request);

	//取消订单
	Response cancelOrder(Request request);

	//完成订单
	Response finishOrder(Request request);

	//设置服务不可用
	Response setOutOfService(Request request);

	//调整饮料数量
	Response increaseDrink(Request request);
	
	// 查询订单情况
	Response<OrderStatus> queryOrderStatus(Request request);
	
	Response cancelCarryingTasks(Request request);
	
}
