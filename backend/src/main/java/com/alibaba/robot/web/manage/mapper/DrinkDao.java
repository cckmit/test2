package com.alibaba.robot.web.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.Drink;

@Component
@Mapper
public interface DrinkDao {
	//查询所有饮料信息
	List<Drink> selectAll();
	
	void updateDrinkAmount(Drink drink);
}
