package com.alibaba.robot.test;

import java.util.Map;

import org.junit.Test;

import com.alibaba.robot.business.hema.SystemConfiguration;
import com.alibaba.robot.innerhandle.SystemConfInter;

public class ConfTest {
	
	private SystemConfInter sci = new SystemConfInter();
	
	@Test
	public void add(){
		SystemConfiguration sConfiguration = new SystemConfiguration();
		sConfiguration.setConf_name("T8");
		sConfiguration.setConf_type("float");
		sConfiguration.setFloat_value((float) 22.22);
		int systemConfAdd = sci.systemConfAdd(sConfiguration);
		System.out.println(systemConfAdd);
	}
	
	@Test
	public void selectOne(){
		Object one = sci.systemConfSelectOne("T8");
		System.out.println(one);
	}
	
	@Test
	public void update(){
		SystemConfiguration sConfiguration = new SystemConfiguration();
		sConfiguration.setConf_name("T8");
		sConfiguration.setFloat_value((float) 55.55);
		int systemConfUpdate = sci.systemConfUpdate(sConfiguration);
		System.out.println(systemConfUpdate);
	}
	
	@Test
	public void delete(){
		int systemConfDelete = sci.systemConfDelete("T8");
		System.out.println(systemConfDelete);
	}
	
	@Test
	public void selectAll(){
		Map<String, Object> map = sci.systemConfSelectAll();
		System.out.println(map);
	}
	
}
