package com.alibaba.robot.web.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.alibaba.robot.web.manage.pojo.ServiceStatus;

@Component
@Mapper
public interface ServiceStatusDao {
	
	List<ServiceStatus> selectAll();
	
	void updateStatus(ServiceStatus serviceStatus);
}
