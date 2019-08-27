package com.alibaba.robot.web.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.Location;

@Component
@Mapper
public interface LocationDao {
	void insertLocation(Location location);
	
	List<Location> selectAll();
	
	Location selectById(int id);
	
	void deleteOneById(int id);
	
	void updateLocation(Location location);
}
