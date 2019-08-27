package com.alibaba.robot.business.yunqi;

public enum DrinkType {
	
	DrinkTypeNone(0), 
	DrinkTypeKele(1), 
	DrinkTypeXuebi(2), 
	DrinkTypeFenDa(3), 
	DrinkTypeJiaDuoBao(4);

	int value;

	DrinkType(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}
	
	public static DrinkType  fromInt(int value) {
		for(DrinkType item : values()) {
			if(value == item.intValue()) {
				return item;
			}
		}
		return DrinkType.DrinkTypeNone;
	}
}
