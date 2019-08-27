package com.alibaba.robot.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.alibaba.robot.innerhandle.AttributeDefInner;
import com.alibaba.robot.web.manage.entity.GenericRequest;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.pojo.Attributes;
import com.alibaba.robot.web.manage.pojo.Robot;
import com.alibaba.robot.web.manage.service.Impl.RobotServiceImpl;

public class RobotAttributesTest {

	private AttributeDefInner ad = new AttributeDefInner();

	@Test
	public void addRobotAttriute() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		Attributes e = new Attributes();
		e.setmAttribute_name("addTname");
		e.setmAttribute_type("String");
		e.setmAttribute_value("we will be success");

		List<Attributes> list = new ArrayList<Attributes>();
		list.add(e);
		map.put("name", "T1");
		map.put("address", "192.168.100.100");
		map.put("model", "T1");
		map.put("firmware", "");
		map.put("version", "");
		map.put("status", 1);
		map.put("uniqueId", "111");
		map.put("robotType", 200);
		map.put("attributes", list);

		//
		//
		// public Robot(String name, String address,
		// String model, String firmware, String version, Integer status,
		// String uniqueId, Integer robotType) {

		//

		Robot robot = new Robot("T1", "192.168.100.100:9090", "", "", "", 1, "111", 0);
		robot.setAttributes(list);

		GenericRequest<Robot> res = new GenericRequest<Robot>("sn", "token", "command", robot);
		RobotServiceImpl rsi = new RobotServiceImpl();
		Response addInfo = rsi.addInfoEx(res);
		System.out.println(addInfo);

	}

	@Test
	public void deleteRobotAttriute() {
		
		Attributes e = new Attributes();
		e.setmAttribute_name("qqqq");
		e.setmAttribute_type("String");
		e.setmAttribute_value("we will be success");

		List<Attributes> list =  new ArrayList<Attributes>();
		list.add(e);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", "T1");
		map.put("address", "192.168.100.100");
		map.put("model", "T1");
		map.put("firmware", "");
		map.put("version", "");
		map.put("status", 1);
		map.put("uniqueId", "111");
		map.put("robotType", 200);
		map.put("attributes", list);
		Robot robot = new Robot("T1", "192.168.100.100:9090", "", "", "", 1, "111", 0);
		robot.setAttributes(list);

		GenericRequest<Robot> res = new GenericRequest<Robot>("sn", "token", "command", robot);
		RobotServiceImpl rsi = new RobotServiceImpl();
		Response deleteRobotAttribute = rsi.deleteRobotAttribute(res);
		System.out.println(deleteRobotAttribute);
	}

}
