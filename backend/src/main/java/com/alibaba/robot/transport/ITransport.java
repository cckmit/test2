package com.alibaba.robot.transport;

import com.alibaba.robot.task.ITask;

/**
 * Transportation
 */
public interface ITransport {

	Address getAddress();

	StateName getState();

	String getModel();

	int getId();

	int getRobotType();

	void stop();

	void addListener(ITransportListener listener);

	void removeListener(ITransportListener listener);

	boolean runTask(ITask task);

	boolean publish(String topic, String message, String messageType);

	boolean advertiseService(String serviceName, String serviceType, IServiceCallback serviceCallback);

	boolean cancelCurrentTask();
	
	boolean cancelTask(String taskId);

	/***
	 * TTS请求，内容 <天猫UUID>:<TTS内容>
	 */
	public static final String ROBOT_EMERGENCY_HALT_TOPIC_NAME = "/move_base/DWAPlannerROS/collab_path_ctr";

	/***
	 * TTS请求，内容 <天猫UUID>:<TTS内容>
	 */
	public static final String ROBOT_TTS_TOPIC_NAME = "/robot/robot_tts_server";

	/***
	 * 机器人详细状态
	 */
	public static final String ROBOT_STATUS_TOPIC_NAME = "/robot/robot_status";

	/***
	 * 手臂+机器人大屏显示数据
	 */
	public static final String ROBOT_SCREEN_TOPIC_NAME = "/robot/screen_data";

	/**
	 * 心跳topic
	 */
	public static final String HEARTBEAT_TOPIC = "/robot/rosbridge/heartbeat";
}
