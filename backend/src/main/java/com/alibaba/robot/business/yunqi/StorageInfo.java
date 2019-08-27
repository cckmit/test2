package com.alibaba.robot.business.yunqi;

public class StorageInfo {
	public StorageInfo(DrinkType drink_type, StorageType storage_type, int amount) {
		super();
		this.drink_type = drink_type;
		this.storage_type = storage_type;
		this.amount = amount;
	}

	public DrinkType getDrink_type() {
		return drink_type;
	}

	public StorageType getStorage_type() {
		return storage_type;
	}

	public int getAmount() {
		return amount;
	}

	private DrinkType drink_type;
	private StorageType storage_type;
	private int amount;

}
