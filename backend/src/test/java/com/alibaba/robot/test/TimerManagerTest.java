package com.alibaba.robot.test;

import com.alibaba.robot.common.ITimerListener;
import com.alibaba.robot.common.TimerManager;
import com.alibaba.robot.common.TimerManager.TimerType;

public class TimerManagerTest {

	public static void main2(String[] args) {

		long onceId = TimerManager.getInstance().addTimer(TimerType.FireOnce, 1000, new TimerListener("Once-1000"));
	
		for(int i = 0; i<1000; i++) {
			TimerManager.getInstance().addTimer(TimerType.Repeat, 1000, new TimerListener("Repeat-1000"));
		}
		long onDemandId = TimerManager.getInstance().addTimer(TimerType.OnDemand, 1000,
				new TimerListener("OnDemand-1000"));

		while (true) {
			TimerManager.getInstance().resetTimer(onDemandId, 1000);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
			}

		}
	}

	static class TimerListener implements ITimerListener {
		public TimerListener(String name) {
			super();
			this.name = name;
		}

		private String name;

		@Override
		public void onTimeout(long id) {
			System.out.println("Timer fired: " + name);
		}
	}

}
