
package com.alibaba.robot.common;

public interface Const {

	public static int ROBOT_STATE_DISCONNECTED = 1;
	public static int ROBOT_STATE_CONNECTING = 2;
	public static int ROBOT_STATE_ESTABLISHED = 3;
	public static int ROBOT_STATE_RUNNING_TASK = 4;
	public static int ROBOT_STATE_DISABLED = 10;

	public static final String CACHE_KEY_PREFIX = "tsk";

	public static final String GOAL_ID_STATUS_KEY_PREFIX = "gid_status_";
	public static final String GOAL_ID_RESULT_KEY_PREFIX = "gid_result_";
	public static final String FEEDBACK_STATUS_KEY = "status";

	public static final String KEY_DETAILED_STATUS_PREFIX = "rbt_sts_dtl_";
	public static final String KEY_SCREEN_PREFIX = "rbt_screen_";

	/***
	 * Top level set contains all robots ids
	 */
	public static final String KEY_INFO_IDS = "rbt_sets";

	/***
	 * 2nd level hash contains categories of information<br>
	 * robotId <===> category names
	 */
	public static final String KEY_CATEGORY_PREFIX = "rbt_cty_";

	/**
	 * 3rd level, actual key value hash<br>
	 * category <===> actual data<br>
	 * key formation: &lt;prefix_&gt;_&lt;category&gt;_&lt;robot_id&gt;
	 */
	public static final String KEY_STATUS_PREFIX = "rbt_sts_";

	/**
	 * Mark task id of a running robot<br>
	 * robot_id &lt;===&gt; task_id
	 */
	public static final String KEY_ROBOT_TASK_PREFIX = "rbt_tsts_";
	
	/**
	 * take goods code 
	 * joint with robot_uniqueId     
	 */
	public static final String TAKE_CODE_REDIS = "take_code_";
	
}
