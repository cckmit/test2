package com.alibaba.robot.web.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.entity.ModelActionGoal;

@Component
@Mapper
public interface ModelActionGoalDao {
	List<ModelActionGoal> selectAll();
}
