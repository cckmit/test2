package com.alibaba.robot.web.manage.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.robot.web.manage.entity.PoiDataReq;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.mapper.LocationDao;
import com.alibaba.robot.web.manage.pojo.Location;
import com.alibaba.robot.web.manage.service.PoiService;
import com.google.gson.Gson;

@Service
public class PoiServiceImpl implements PoiService {
	
	@Autowired
	private LocationDao locationDao;
	
	private static Logger LOGGER = Logger.getLogger(PoiServiceImpl.class);
	
	//添加地址信息
	public Response addLocation(Request request){
		Map<String, Integer> map = new HashMap<>();
			Gson gson = new Gson();
			String data = request.getData().toString();
			//PoiDataReq poiDataReq = gson.fromJson(data, PoiDataReq.class);
			Location location = gson.fromJson(data, Location.class);
			//掉转换方法
			//Location location = EulerToQuatUtils.getLocation(poiDataReq);
			locationDao.insertLocation(location);
			int id = location.getId();
			map.put("id", id);
			return new Response<Map<String, Integer>>(request.getSn(), "success", 1, map);
	}
	
	
	//查询所有地址信息
	public Response queryLocation(Request request){
		List<Location> locationList = locationDao.selectAll();
		Response response = new Response(request.getSn(),"success",1,locationList);
		return response;
		
	}
	
	
	//查询单个地址信息
	public Response queryById(Request request){
		Gson gson = new Gson();
		String data = request.getData().toString();
		PoiDataReq poiDataReq = gson.fromJson(data, PoiDataReq.class);
		Integer id = poiDataReq.getId();
		if(id != null){
			Location queryLocation = locationDao.selectById(id);
			Response response = new Response(request.getSn(),"success",1,queryLocation);
			return response;
		} else {
			return new Response(request.getSn(), "false without id", 0, null);
		}
		
	}
	
	
	//删除单个地址信息
	public Response deleteById(Request request){
			Gson gson = new Gson();
			String data = request.getData().toString();
			PoiDataReq poiDataReq = gson.fromJson(data, PoiDataReq.class);
			Integer id = poiDataReq.getId();
			if(id != null){
				locationDao.deleteOneById(id);
				return new Response(request.getSn(), "success", 1, null);
			} else {
				return new Response(request.getSn(), "false without id", 0, null);
			}
		
	}
	
	
	//更新修改单个地址信息
	public Response updateLocation(Request request){
			Gson gson = new Gson();
			String data = request.getData().toString();
			data = data.replace("new marker", "new_marker");
			LOGGER.info(data);
			//PoiDataReq poiDataReq = gson.fromJson(data, PoiDataReq.class);
		
			Location location = gson.fromJson(data, Location.class);
			//掉转换方法
			//Location location = EulerToQuatUtils.getLocation(poiDataReq);
			if(location.getId() != null){
				locationDao.updateLocation(location);
				return new Response<>(request.getSn(), "success", 1, null);
			} else {
				return new Response<>(request.getSn(), "false without id", 0, null);
			}
		
	}
	
	
}
