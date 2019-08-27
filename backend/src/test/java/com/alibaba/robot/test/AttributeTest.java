package com.alibaba.robot.test;

import java.util.Map;

import org.junit.Test;

import com.alibaba.robot.innerhandle.AttributeDefInner;
import com.alibaba.robot.web.manage.pojo.AttributeDefinition;

public class AttributeTest {
	
	private AttributeDefInner ad = new AttributeDefInner();
	
	@Test
	public void add(){
		AttributeDefinition aDefinition = new AttributeDefinition();
		aDefinition.setAttribute("ware");
		aDefinition.setAttribute_type("int");
		aDefinition.setInt_value(22);
		aDefinition.setRobot_uniqueId("321");
		int addAttribute = ad.addAttribute(aDefinition);
		System.out.println(addAttribute);
	}
	
	@Test
	public void update(){
		AttributeDefinition aDefinition = new AttributeDefinition();
		aDefinition.setAttribute("ware");
		aDefinition.setRobot_uniqueId("123");
		aDefinition.setString_value("hehe");
		int updateAttribute = ad.updateAttribute(aDefinition);
		System.out.println(updateAttribute);
	}
	
	@Test
	public void selectOne(){
		AttributeDefinition aDefinition = new AttributeDefinition();
		aDefinition.setAttribute("ware");
		aDefinition.setRobot_uniqueId("321");
		Object object = ad.selectOneAttribute(aDefinition);
		System.out.println(object);
	}
	
	@Test
	public void selectByType(){
		Map<String, Object> map = ad.selectTypeAttribute("ware");
		System.out.println(map);
	}
	
	@Test
	public void delete(){
		AttributeDefinition aDefinition = new AttributeDefinition();
		aDefinition.setAttribute("ware");
		aDefinition.setRobot_uniqueId("321");
		int deleteAttribute = ad.deleteAttribute(aDefinition);
		System.out.println(deleteAttribute);
		
	}
	
	
}
