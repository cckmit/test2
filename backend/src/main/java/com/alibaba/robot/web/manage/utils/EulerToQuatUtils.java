package com.alibaba.robot.web.manage.utils;

import java.math.BigDecimal;

import com.alibaba.robot.web.manage.entity.PoiDataReq;
import com.alibaba.robot.web.manage.pojo.Location;

public class EulerToQuatUtils {
	
	//欧拉角转四元素w,x,y,z
	public static Location getLocation(PoiDataReq poiDataReq){
		double euler = poiDataReq.getEuler();
		double w = Math.cos(euler/2);
		double z = Math.sin(euler/2);
		BigDecimal bigDecimal = new BigDecimal(w);
		BigDecimal bigDecimalZ = new BigDecimal(z);
		double w3 = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		double z3 = bigDecimalZ.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		Location location = new Location();
		location.setId(poiDataReq.getId());
		location.setFloor(poiDataReq.getFloor());
		location.setName(poiDataReq.getName());
		location.setW(w3);
		location.setX(poiDataReq.getX());
		location.setY(poiDataReq.getY());
		location.setZ(z3);
		return location;
	}
	
}
