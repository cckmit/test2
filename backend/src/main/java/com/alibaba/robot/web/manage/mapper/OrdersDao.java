package com.alibaba.robot.web.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.Order;

@Component
@Mapper
public interface OrdersDao {
	
	//查询每桌的订单状态
	List<Order> selectAll();
}
