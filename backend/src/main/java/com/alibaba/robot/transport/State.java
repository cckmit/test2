package com.alibaba.robot.transport;

public abstract class State {

	protected StateName stateName;

	public State(StateName stateName) {
		this.stateName = stateName;
	}

	public StateName getStateName() {
		return stateName;
	}

	public void onEnter() {

	}

	public void onExit() {

	}

	public void handleEvent(Event event) {

	}
}
