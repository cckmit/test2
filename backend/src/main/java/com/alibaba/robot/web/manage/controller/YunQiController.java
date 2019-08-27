package com.alibaba.robot.web.manage.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.pojo.Order;
import com.alibaba.robot.web.manage.pojo.OrderStatus;
import com.alibaba.robot.web.manage.service.YunQiService;

@RestController
@RequestMapping("/yunqi")
public class YunQiController {
	
	@Autowired
	private YunQiService yunQiService;
	
	private Logger LOGGER = Logger.getLogger(YunQiController.class);


	//查询饮料库存量
	@RequestMapping("/get_drink_storage_info")
	@ResponseBody
	public Response getDrinkStorageInfo(@RequestBody Request request){
		Response response = yunQiService.getDrinkInfo(request);
		return response;
	}
	
	
	
	//查询所有饮料库存量
	@RequestMapping("/get_drink_storage_info_ex")
	@ResponseBody
	public Response getDrinkStorageInfoEx(@RequestBody Request request){
		Response response = yunQiService.getDrinkInfoEx(request);
		return response;
	}
	
	//查询每桌的订单状态
	@RequestMapping("/get_pending_orders")
	@ResponseBody
	public Response getPendingOrders(@RequestBody Request request){
		Response<List<Order>> response = yunQiService.getPendingOrders(request);
		return response;
	}
	
	//查询订单配送状态
	@RequestMapping("/get_order_delivery_status")
	@ResponseBody
	public Response getOrderDeliveryStatus(@RequestBody Request request){
		Response<List<OrderStatus>> response = yunQiService.getOrderDeliveryStatus(request);
		return response;
	}
	
	//查询机器人状态
	@RequestMapping("/get_robot_status")
	@ResponseBody
	public Response getRobotStatus(@RequestBody Request request){
		Response response = yunQiService.getRobotStatus(request);
		return response;
	}
	
	//查询系统是否暂停
	@RequestMapping("/get_service_status")
	@ResponseBody
	public Response getServiceStatus(@RequestBody Request request){
		Response response = yunQiService.selectServiceStatusAll(request);
		return response;
	}
	
	
	
	//饮料下单
	@RequestMapping("/place_order")
	@ResponseBody
	public Response placeOrder(@RequestBody Request request){
		Response response = yunQiService.placeOrder(request);
		return response;
	}

	
 
	@RequestMapping("/query_order_status")
	@ResponseBody
	public Response queryOrderStatus(@RequestBody Request request){	
		Response<OrderStatus> response = yunQiService.queryOrderStatus(request);
		return response;
	}

	//取消订单
	@RequestMapping("/cancel_order")
	@ResponseBody
	public Response cancelOrder(@RequestBody Request request){
		Response response = yunQiService.cancelOrder(request);
		return response;
	}
	
	//完成订单
	@RequestMapping("/finish_order")
	@ResponseBody
	public Response finishOrder(@RequestBody Request request){
		Response response = yunQiService.finishOrder(request);
		return response;
	}
	
	
	//设置服务不可用
	@RequestMapping("/set_out_of_service")
	@ResponseBody
	public Response setOutOfService(@RequestBody Request request){
		Response response = yunQiService.setOutOfService(request);
		return response;
	}
	
	
	//调整饮料数量
	@RequestMapping("/increase_drink")
	@ResponseBody
	public Response increaseDrink(@RequestBody Request request){
		Response response = yunQiService.increaseDrink(request);
		return response;
	}
	
	//取消订单
	@RequestMapping("/cancel_carrying_tasks")
	@ResponseBody
	public Response cancelCarryingTasks(@RequestBody Request request){
		Response response = yunQiService.cancelCarryingTasks(request);
		return response;
	}
	
}
