package com.alibaba.robot.actionlib;

public class Status {
	
	/**
	 * The goal is currently being processed by the action server.
	 */
	public static final byte ACTIVE = 1;

	/**
	 * The goal received a cancel request after it started executing and has
	 * since completed its execution (Terminal State).
	 */
	public static final byte PREEMPTED = 2;

	/**
	 * The goal was achieved successfully by the action server (Terminal State).
	 */
	public static final byte SUCCEEDED = 3;

	/**
	 * The goal was aborted during execution by the action server due to some
	 * failure (Terminal State).
	 */
	public static final byte ABORTED = 4;

	/**
	 * The goal was rejected by the action server without being processed,
	 * because the goal was unattainable or invalid (Terminal State).
	 */
	public static final byte REJECTED = 5;

	/**
	 * The goal received a cancel request after it started executing and has not
	 * yet completed execution.
	 */
	public static final byte PREEMPTING = 6;

	/**
	 * The goal received a cancel request before it started executing, but the
	 * action server has not yet confirmed that the goal is canceled.
	 */
	public static final byte RECALLING = 7;

	/**
	 * The goal received a cancel request before it started executing and was
	 * successfully cancelled (Terminal State).
	 */
	public static final byte RECALLED = 8;

	/**
	 * An action client can determine that a goal is LOST. This should not be
	 * sent over the wire by an action server.
	 */
	public static final byte LOST = 9;
	
	
	public boolean isTerminalStatus() {
		return (status == PREEMPTED || status == SUCCEEDED || status == ABORTED
				|| status == REJECTED || status == RECALLED );
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getText() {
		return text;
	}
	
	public GoalId getGoal_id() {
		return goal_id;
	}
	
	
	public Status(int status, String text, GoalId goal_id) {
		super();
		this.status = status;
		this.text = text;
		this.goal_id = goal_id;
	}
	private int status;
	private String text;
	private GoalId goal_id;

}
