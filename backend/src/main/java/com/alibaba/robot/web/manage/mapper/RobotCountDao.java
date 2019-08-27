package com.alibaba.robot.web.manage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.RobotCount;

@Component
@Mapper
public interface RobotCountDao {
	//获取统计数据  机器人行驶路程和饮料数
	RobotCount selectByRobotUniqueId(String robot_uniqueId);
	//接收机器人统计数据做更新
	void updateRobotTransportData(RobotCount robotCount);
}
