package com.alibaba.robot.common;

import java.util.Random;

public class RandUtil {
	private static Random RAND = new Random(System.currentTimeMillis());
	public static String getRandomGoalId() {
		StringBuilder sb= new StringBuilder();
		for(int i = 0; i<20; i++) {
			sb.append(Math.abs(RAND.nextInt(10)));
		}
		return sb.toString();
	}
}
