package com.alibaba.robot.test;

import java.util.List;

import org.junit.Test;

import com.alibaba.robot.business.hema.RobotMonitor;
import com.alibaba.robot.business.hema.RobotStatus;

public class RobotMonitorTest {
	
	@Test
	public void test(){
		List<RobotStatus> robotInfoForHema = RobotMonitor.getInstance().getRobotInfoForHema();
		System.out.println(robotInfoForHema);
	}
	
}
