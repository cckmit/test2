package com.alibaba.robot.web.manage.entity.futurehotel;

public class GoodsParam {
	
	private String goods_id;
	private int goods_count;
	
	public GoodsParam() {
		super();
	}
	public GoodsParam(String goods_id, int goods_count) {
		super();
		this.goods_id = goods_id;
		this.goods_count = goods_count;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public int getGoods_count() {
		return goods_count;
	}
	public void setGoods_count(int goods_count) {
		this.goods_count = goods_count;
	}
	@Override
	public String toString() {
		return "GoodsParam [goods_id=" + goods_id + ", goods_count=" + goods_count + "]";
	}
	
}
