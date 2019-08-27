package com.alibaba.robot.web.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.User;

@Component
@Mapper
public interface UserDao {
	
	List<User> selectAll();
	
	String selectPasswordByUserName(String name);
	
	User selectUserById(int id);
	
	String selectTokenById(Integer id);
	
	User selectUserByName(String name);
}
