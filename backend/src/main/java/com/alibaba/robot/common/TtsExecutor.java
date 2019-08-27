package com.alibaba.robot.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class TtsExecutor implements Runnable {
	private static class TtsCommand {
		public TtsCommand(String uuid, String text) {
			super();
			this.uuid = uuid;
			this.text = text;
		}

		public String uuid;
		public String text;
	}

	private TtsExecutor() {
		new Thread(this).start();
	}

	private static TtsExecutor INSTANCE = new TtsExecutor();
	private static Logger LOGGER = Logger.getLogger(TtsExecutor.class);

	public static TtsExecutor getInstance() {
		return INSTANCE;
	}

	private static final String EXIT_COMMAND = "exit";

	private BlockingQueue<TtsCommand> queue = new LinkedBlockingQueue<TtsCommand>();
 
	public boolean ttsAsync(String uuid, String text) {
		if (uuid == null || uuid.length() == 0 || text == null || text.length() == 0) {
			return false;
		}
		return queue.offer(new TtsCommand(uuid, text));
	}

	@Override
	public void run() {
		while (true) {
			TtsCommand command = null;
			try {
				command = queue.poll(Long.MAX_VALUE, TimeUnit.MINUTES);
				if (command.text == null || command.text.length() == 0) {
					LOGGER.warn("ignored empty text command");
					continue;
				}
				
				LOGGER.info("uuid " + command.uuid + ", text " + command.text );

				TtsUtil.pushCommand(command.uuid, EXIT_COMMAND);
				Thread.sleep(100);
				if (!TtsUtil.ttsBroadcast(command.uuid, command.text)) {
					LOGGER.error("tts failed, retry " + command.text);
					if (!TtsUtil.ttsBroadcast(command.uuid, command.text)) {
						LOGGER.error("tts failed: " + command.text);
					}
				} else {
					LOGGER.info("tts succeeded: " + command.text);
				}

			} catch (InterruptedException e) {

			}
		}
	}
}
