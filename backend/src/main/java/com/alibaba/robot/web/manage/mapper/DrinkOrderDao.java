package com.alibaba.robot.web.manage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.Drink;

@Component
@Mapper
public interface DrinkOrderDao {
	Drink selectByDrinkType(int type);
}
