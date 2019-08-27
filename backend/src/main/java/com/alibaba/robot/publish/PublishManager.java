package com.alibaba.robot.publish;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.alibaba.robot.common.BuildVariants;

public class PublishManager {

	private static PublishManager sPublishManager = null;

	private PublishManager() {
		init();
	}

	private void init() {
		if (BuildVariants.YUNQI_ENABLED) {
			publisherQueue.offer(new ScreenPublisher());
			publisherQueue.offer(new AlgPublisher());
		}
		if (BuildVariants.HEMA_ENABLED) {
			publisherQueue.offer(new MqttPublisher());
		}
	}

	public static PublishManager getInstance() {
		if (sPublishManager == null) {
			synchronized (PublishManager.class) {
				if (sPublishManager == null) {
					sPublishManager = new PublishManager();
				}
			}
		}
		return sPublishManager;
	}

	private ConcurrentLinkedQueue<IPublisher> publisherQueue = new ConcurrentLinkedQueue<IPublisher>();

	public void publish(int tags, String message) {
		for (IPublisher publisher : publisherQueue) {
			publisher.publish(tags, message);
		}
	}
	
	public void publish(int tags, byte[] binary){
		for (IPublisher publisher : publisherQueue) {
			publisher.publish(tags, binary);
		}
	}
	
	
}
