package com.alibaba.robot.web.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.RobotManage;

@Component
@Mapper
public interface RobotManageDao {
	
	void insertRobotManage(RobotManage robotManage);
	
	void deleteRobotManageOne(RobotManage robotManage);
	
	RobotManage selectByRobotUniqueIdAndWaercode(RobotManage robotManage);
}
