package com.alibaba.robot.business.yunqi;

public enum StorageType {
	StorageTypeNone(0), StorageTypeCart(1), StorageTypeCabinet(2);

	int value;

	StorageType(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}
	
	public static StorageType  fromInt(int value) {
		for(StorageType item : values()) {
			if(value == item.intValue()) {
				return item;
			}
		}
		return StorageTypeNone;
	}
}
