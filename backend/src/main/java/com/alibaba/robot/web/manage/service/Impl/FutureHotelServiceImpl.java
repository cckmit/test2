package com.alibaba.robot.web.manage.service.Impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.robot.futurehotel.HotelRobotMonitor;
import com.alibaba.robot.futurehotel.TakeCodeManager;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.entity.futurehotel.FutureHotelRobotResp;
import com.alibaba.robot.web.manage.entity.futurehotel.GetFutureHotelRobot;
import com.alibaba.robot.web.manage.entity.futurehotel.GoodsParam;
import com.alibaba.robot.web.manage.entity.futurehotel.HotelRobotStatus;
import com.alibaba.robot.web.manage.mapper.GoodsDao;
import com.alibaba.robot.web.manage.mapper.RobotDao;
import com.alibaba.robot.web.manage.service.FutureHotelService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Service
public class FutureHotelServiceImpl implements FutureHotelService {

	@Autowired
	private RobotDao robotDao;
	@Autowired
	private GoodsDao goodsDao;
	
	static String TAKE_CODE_REDIS = "take_code_";
	
	private Gson gson = new Gson();

	//查询所有机器人状态信息
	public Response queryRobotStatus(Request request) {
		String data = request.getData().toString();
		GetFutureHotelRobot getFutureHotelRobot = gson.fromJson(data, GetFutureHotelRobot.class);
		String goodsList = getFutureHotelRobot.getGoods_param().toString();
		Type type = new TypeToken<List<GoodsParam>>() {
		}.getType();
		List<GoodsParam> goodsParamsList = gson.fromJson(goodsList, type);
		FutureHotelRobotResp futureHotelRobotResp = new FutureHotelRobotResp();
		List<HotelRobotStatus> robotStatusList = new ArrayList<>();
		int goodsSupport = 2;
		int containerCarray = 2;
		for (GoodsParam goodsParam : goodsParamsList) {
			String goods_id = goodsParam.getGoods_id();
			if(goods_id != null && goods_id.length() > 0){
				//判断是否支持该商品
				boolean isUseable = goodsDao.selectIsUseable(Integer.parseInt(goods_id));
				if(isUseable == true){
					futureHotelRobotResp.setGoods_support(1);
					
					List<HotelRobotStatus> robotInfoList = HotelRobotMonitor.getInstance().getRobotInfoForQuery();
					for (HotelRobotStatus hotelRobotStatus : robotInfoList) {
						String uniqueId = hotelRobotStatus.getRobot_uniqueId();
						int takeCodeFromRedis = TakeCodeManager.getInstance().getTakeCodeFromRedis(uniqueId);
						if(takeCodeFromRedis != 0){
							hotelRobotStatus.setTake_code(takeCodeFromRedis);
						}
						robotStatusList.add(hotelRobotStatus);
					}
					
					futureHotelRobotResp.setRobot_status(robotStatusList);
					
				} else {
					futureHotelRobotResp.setGoods_support(2);
				}
				
			} else {
				return new Response<String>(request.getSn(), "goods_id is null", 2);
			}
			
		}
		
		return new Response<>();
	}

	//控制机器人执行任务
	public Response runTask(Request request) {
		
		return null;
	}
	
	
	
	
	
}
