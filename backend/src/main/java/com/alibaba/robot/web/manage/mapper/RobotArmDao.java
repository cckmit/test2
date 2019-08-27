package com.alibaba.robot.web.manage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.RobotArm;

@Component
@Mapper
public interface RobotArmDao {
	
	//获取统计数据  机械臂装载次数和饮料数
	RobotArm selectArmById(int id);
	//接收机械臂统计数据做更新
	void updateRobotArmData(RobotArm robotArm);
}
