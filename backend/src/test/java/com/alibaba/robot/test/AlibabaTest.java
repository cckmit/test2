package com.alibaba.robot.test;

import org.junit.Test;

import com.alibaba.robot.publish.DataTag;
import com.alibaba.robot.publish.PublishManager;

public class AlibabaTest {
	
	@Test
	public void aliTest(){
		PublishManager.getInstance().publish(DataTag.BIG_SCREEN_DATA, "hello oo");
	}
	
}
