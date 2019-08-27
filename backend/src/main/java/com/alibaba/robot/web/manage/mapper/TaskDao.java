package com.alibaba.robot.web.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.Task;

@Component
@Mapper
public interface TaskDao {
	
	void insertTask(Task task);
	
	List<Task> selectAllTask();
	
}
