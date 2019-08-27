package com.alibaba.robot.web.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.OrderStatus;

@Component
@Mapper
public interface OrderStatusDao {
	
	//查询所有的订单状态
	List<OrderStatus> selectAll();
	
	void changeByOrderId(OrderStatus orderStatus);
}
