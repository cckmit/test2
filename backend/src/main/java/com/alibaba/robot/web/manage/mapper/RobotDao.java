package com.alibaba.robot.web.manage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.Robot;

import java.util.List;

@Component
@Mapper
public interface RobotDao {
    //注册
    int insertRobot(Robot robot);
    //查询
    List<Robot> selectAll();

    List<Robot> selectByModel(String model);
    
    //根据id 获取单个机器人的详细信息
    Robot selectById(int id);
    //根据 id 删除单个机器人
    void deleteOneById(int id);
    
    Robot selectByUniqueId(String uniqueId);
    
    void deleteOneByUniqueId(String uniqueId);
    
    void updateByUniqueId(Robot robot);
}
