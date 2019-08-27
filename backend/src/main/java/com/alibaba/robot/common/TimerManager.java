package com.alibaba.robot.common;

import java.lang.management.ManagementFactory;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

/***
 * Generic timer manager<br>
 * OnceTimer <br>
 * Repeat Timer <br>
 * OnDemand Timer<br>
 */
public class TimerManager implements Runnable {

	private static Logger LOGGER = Logger.getLogger(TimerManager.class);

	public static enum TimerType {

		/****
		 * Timer will be fired once, and then removed
		 */
		FireOnce,

		/***
		 * Timer will be fired on demand
		 */
		OnDemand,

		/***
		 * Timer will be fired periodically
		 */
		Repeat
	}

	private static enum Operation {

		AddTimer,

		RemoveTimer,

		/***
		 * Reset on demand timer
		 */
		ResetTimer
	}

	private static class Cmd {
		private Operation ops;
		private long timerId;
		private int timeout;
		private TimerRecord timerRecord;
	}

	private static class TimerRecordComparator implements Comparator<TimerRecord> {
		@Override
		public int compare(TimerRecord t1, TimerRecord t2) {
			return (int) (t1.getFireTime() - t2.getFireTime());
		}
	}

	private static class TimerRecord {

		public TimerRecord(TimerType timerType, long id, long timeout, ITimerListener listener) {
			this.timerType = timerType;
			this.id = id;
			this.timeout = timeout;
			this.listener = listener;
			nextFireTime = now() + timeout;
		}

		TimerType timerType;
		long id;
		long timeout;
		ITimerListener listener;

		long nextFireTime;
		boolean fired;

		long getFireTime() {
			if (TimerType.FireOnce.equals(timerType)) {
				return fired ? Long.MAX_VALUE : nextFireTime;
			} else if (TimerType.OnDemand.equals(timerType)) {
				return fired ? Long.MAX_VALUE : nextFireTime;
			} else if (TimerType.Repeat.equals(timerType)) {
				return nextFireTime;
			}
			return Long.MAX_VALUE;
		}

		void fire() {
			long start = System.currentTimeMillis();
			listener.onTimeout(id);
			long callbackCast = System.currentTimeMillis() - start;
			if (callbackCast > 500) {
				LOGGER.warn("TIMER CALLBACK COSTS TOO LONG: " + callbackCast + ", id = " + id);
			}
			
			fired = !timerType.equals(TimerType.Repeat);
			if (timerType.equals(TimerType.Repeat)) {
				nextFireTime = now() + timeout;
			}
		}

		void reset() {
			fired = false;
			nextFireTime = now() + timeout;
		}
	}

	private static long now() {
		return ManagementFactory.getRuntimeMXBean().getUptime();
	}

	private static TimerManager INSTANCE = new TimerManager();

	private TimerManager() {
		Thread thread = new Thread(this);
		thread.setName("TimerManager");
		thread.start();
	}

	public static TimerManager getInstance() {
		return INSTANCE;
	}

	/***
	 * Add a timer<br>
	 * 
	 * @param timerType
	 * @param timeout   in millisecond
	 * @param listener
	 * @return id of timer
	 */
	public long addTimer(TimerType timerType, int timeout, ITimerListener listener) {
		long id = idSed.incrementAndGet();
		TimerRecord timerRecord = new TimerRecord(timerType, id, timeout, listener);

		Cmd cmd = new Cmd();
		cmd.ops = Operation.AddTimer;
		cmd.timerRecord = timerRecord;
		cmd.timerId = timerRecord.id;

		cmdQueue.offer(cmd);
		return id;
	}

	/*****
	 * Remove a timer by id
	 */
	public void removeTimer(long id) {
		Cmd cmd = new Cmd();
		cmd.ops = Operation.RemoveTimer;
		cmd.timerId = id;
		cmdQueue.offer(cmd);
	}

	/***
	 * reset a on-demand timer
	 * 
	 * @param the     timer id
	 * @param timeout in millisecond
	 */
	public void resetTimer(long id, int timeout) {
		Cmd cmd = new Cmd();
		cmd.ops = Operation.ResetTimer;
		cmd.timerId = id;
		cmd.timeout = timeout;
		cmdQueue.offer(cmd);
	}

	@Override
	public void run() {
		Cmd cmd = null;
		while (true) {
			try {
				long timeout = getLeastTimeout();
				// LOGGER.info("timeout = " + timeout);
				cmd = cmdQueue.poll(timeout, TimeUnit.MILLISECONDS);
				if (cmd != null) {
					handleCommand(cmd);
				}
				checkTimers();
			} catch (InterruptedException ex) {

			}
		}
	}

	private void handleCommand(Cmd cmd) {
		if (Operation.AddTimer.equals(cmd.ops)) {
			records.add(cmd.timerRecord);
			map.put(cmd.timerId, cmd.timerRecord);
		} else if (Operation.RemoveTimer.equals(cmd.ops)) {
			if (map.containsKey(cmd.timerId)) {
				records.remove(map.get(cmd.timerId));
				map.remove(cmd.timerId);
			}
		} else if (Operation.ResetTimer.equals(cmd.ops)) {
			if (map.containsKey(cmd.timerId)) {
				TimerRecord timerRecord = map.get(cmd.timerId);
				if (!timerRecord.timerType.equals(TimerType.OnDemand)) {
					LOGGER.warn("can not reset a non-ondemand timer");
					return;
				}
				timerRecord.timeout = cmd.timeout;
				timerRecord.reset();
				records.remove(timerRecord);
				records.add(timerRecord);
			}
		}
	}

	private long getLeastTimeout() {
		if (map.size() == 0) {
			return Long.MAX_VALUE;
		}
		long timeout = records.peek().getFireTime() - now();
		if (timeout < 0L) {
			return 0L;
		}
		return timeout;
	}

	private void removeTimerRecord(TimerRecord timerRecord) {
		records.remove(timerRecord);
		map.remove(timerRecord.id);
	}

	private void checkTimers() {
		while (records.size() > 0) {
			long currentTime = now();
			TimerRecord timerRecord = records.peek();
			if (timerRecord.getFireTime() <= currentTime) {
				timerRecord.fire();
				if (TimerType.FireOnce.equals(timerRecord.timerType)) {
					removeTimerRecord(timerRecord);
				} else { // order timer record by timeout time
					records.remove(timerRecord);
					records.add(timerRecord);
				}
			} else {
				break;
			}
		}
	}

	private AtomicLong idSed = new AtomicLong();
	private BlockingQueue<Cmd> cmdQueue = new LinkedBlockingQueue<Cmd>();
	private PriorityQueue<TimerRecord> records = new PriorityQueue<TimerRecord>(new TimerRecordComparator());
	private Map<Long, TimerRecord> map = new HashMap<Long, TimerRecord>();

}
