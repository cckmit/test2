package com.alibaba.robot.common;

/***
 * 业务开关，控制业务模块
 */
public class BuildVariants {

	/***
	 * 是否启用云栖业务
	 */
	public static final boolean YUNQI_ENABLED = false;

	public static final boolean YUNQI_DEBUG = false;

	public static final boolean YUNQI_DEBUG_WIDH_REAL_BOTS = false;

	/***
	 * 是否启用盒马业务
	 */
	public static final boolean HEMA_ENABLED = true;

	public static final boolean HEMA_MQTT_ENABLED = true;
	
	/***
	 * 盒马模拟测试代码
	 **/
	public static final boolean HEMA_MOCK_ENABLED = false;
	
	public static final String HEMA_DEBUGGING_ROBOT_ID = "2";
	
	public static final boolean HEMA_FILTER_ROBOT_PUSH = true;

}
 