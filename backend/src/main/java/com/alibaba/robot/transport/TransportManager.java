package com.alibaba.robot.transport;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.alibaba.robot.actionlib.Action;
import com.alibaba.robot.actionlib.Feedback;
import com.alibaba.robot.actionlib.Result;
import com.alibaba.robot.actionlib.StatusArray;

public class TransportManager implements ITransportListener {

	private static final Logger LOGGER = Logger.getLogger(TransportManager.class);
	public static final int ROBOT_DISABLED = 10;
	private Object transportMapLock = new Object();
	private HashMap<Integer, RobotTransport> transportMap = new HashMap<Integer, RobotTransport>();
	private ExecutorService executor = Executors.newCachedThreadPool();
	private static TransportManager INSTANCE = new TransportManager();
	private ConcurrentLinkedQueue<ITransportListener> listeners = new ConcurrentLinkedQueue<ITransportListener>();

	private TransportManager() {

	}

	public static TransportManager getInstance() {
		return INSTANCE;
	}

	public boolean addTransport(int id, String address, int robotType, String model) {
		return addTransport(id, new Address(address), robotType, model);
	}

	public boolean addTransport(int id, Address address, int robotType, String model) {
		if (address == null) {
			return false;
		}
		synchronized (transportMapLock) {
			if (transportMap.containsKey(id)) {
				return false;
			}
			RobotTransport transport = new RobotTransport(id, address, robotType, model, this);
			transportMap.put(Integer.valueOf(id), transport);
			executor.execute(transport);
		}
		
		return true;
	}
	
	
	public boolean cancelTask(int id, String taskId) {
		synchronized (transportMapLock) {
			if (!transportMap.containsKey(id)) {
				return false;
			}
			transportMap.get(id).cancelTask(taskId);
		}
		return true;
	}
	
 
	public boolean removeTransport(int id) {
		synchronized (transportMapLock) {
			if (!transportMap.containsKey(id)) {
				return false;
			}
			transportMap.get(id).stop();
			transportMap.remove(id);
		}

		return true;
	}

	public boolean publishMessage(int id, String topic, String message, String messageType) {
		synchronized (transportMapLock) {
			if (!transportMap.containsKey(id)) {
				return false;
			}
			transportMap.get(id).publish(topic, message, messageType);
		}
		return true;
	}

	
	public ITransport getRobot(int id ) {
		synchronized (transportMapLock) {
			if (transportMap.containsKey(id)) {
				return transportMap.get(id);
			}
		}
		return null;
	}
	
	public RunActionResult runAction(int id, Action action) {
		
		if (action == null) {
			return RunActionResult.BadArguments;
		}

		synchronized (transportMapLock) {
			if (!transportMap.containsKey(id)) {
				return RunActionResult.RobotNotFound;
			}

			RobotTransport transport = transportMap.get(id);
			if (!transport.getState().equals(StateName.Established)) {
				if (transport.getState().equals(StateName.ExecutingTask)) {
					return RunActionResult.ExecutingOtherAction;
				} else {
					return RunActionResult.Offline;
				}
			}

			transport.runTask(action);
		}

		return RunActionResult.Success;
	}

	public void addListner(ITransportListener listener) {
		if (null == listener) {
			return;
		}

		listeners.add(listener);
	}

	public void removeListener(ITransportListener listener) {
		if (null == listener) {
			return;
		}
		listeners.remove(listener);
	}

	@Override
	public void onStateChanged(ITransport transport, StateName oldStateName, StateName newStateName) {
		if (transport == null || newStateName == null) {
			return;
		}

		for (ITransportListener listener : listeners) {
			listener.onStateChanged(transport, oldStateName, newStateName);
		}
	}
	
 
	
	@Override
	public void onMessage(ITransport transport, String topic, String message) {
		if(ITransport.HEARTBEAT_TOPIC.equals(topic)) {
			return;
		}
		
		for (ITransportListener listener : listeners) {
			listener.onMessage(transport, topic, message);
		}
	}
}
