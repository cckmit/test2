package com.alibaba.robot.web.manage.entity;

import com.alibaba.robot.web.manage.pojo.Location;

public class LocationParam {
	public double getPoi1_pos_x() {
		return poi1_pos_x;
	}

	public void setPoi1_pos_x(double poi1_pos_x) {
		this.poi1_pos_x = poi1_pos_x;
	}

	public double getPoi1_pos_y() {
		return poi1_pos_y;
	}

	public void setPoi1_pos_y(double poi1_pos_y) {
		this.poi1_pos_y = poi1_pos_y;
	}

	public double getPoi1_ori_z() {
		return poi1_ori_z;
	}

	public void setPoi1_ori_z(double poi1_ori_z) {
		this.poi1_ori_z = poi1_ori_z;
	}

	public double getPoi1_ori_w() {
		return poi1_ori_w;
	}

	public void setPoi1_ori_w(double poi1_ori_w) {
		this.poi1_ori_w = poi1_ori_w;
	}

	public double getPoi2_pos_x() {
		return poi2_pos_x;
	}

	public void setPoi2_pos_x(double poi2_pos_x) {
		this.poi2_pos_x = poi2_pos_x;
	}

	public double getPoi2_pos_y() {
		return poi2_pos_y;
	}

	public void setPoi2_pos_y(double poi2_pos_y) {
		this.poi2_pos_y = poi2_pos_y;
	}

	public double getPoi2_ori_z() {
		return poi2_ori_z;
	}

	public void setPoi2_ori_z(double poi2_ori_z) {
		this.poi2_ori_z = poi2_ori_z;
	}

	public double getPoi2_ori_w() {
		return poi2_ori_w;
	}

	public void setPoi2_ori_w(double poi2_ori_w) {
		this.poi2_ori_w = poi2_ori_w;
	}

	public double getPoi3_pos_x() {
		return poi3_pos_x;
	}

	public void setPoi3_pos_x(double poi3_pos_x) {
		this.poi3_pos_x = poi3_pos_x;
	}

	public double getPoi3_pos_y() {
		return poi3_pos_y;
	}

	public void setPoi3_pos_y(double poi3_pos_y) {
		this.poi3_pos_y = poi3_pos_y;
	}

	public double getPoi3_ori_z() {
		return poi3_ori_z;
	}

	public void setPoi3_ori_z(double poi3_ori_z) {
		this.poi3_ori_z = poi3_ori_z;
	}

	public double getPoi3_ori_w() {
		return poi3_ori_w;
	}

	public void setPoi3_ori_w(double poi3_ori_w) {
		this.poi3_ori_w = poi3_ori_w;
	}

	public int getDrink_type() {
		return drink_type;
	}

	public void setDrink_type(int drink_type) {
		this.drink_type = drink_type;
	}

	double poi1_pos_x;
	double poi1_pos_y;
	double poi1_ori_z;
	double poi1_ori_w;
	double poi2_pos_x;
	double poi2_pos_y;
	double poi2_ori_z;
	double poi2_ori_w;
	double poi3_pos_x;
	double poi3_pos_y;
	double poi3_ori_z;
	double poi3_ori_w;
	int drink_type;

	public LocationParam() {

	}

	public LocationParam(Location location) {
		poi1_pos_x = location.getX();
		poi1_pos_y = location.getY();
		poi1_ori_z = location.getZ();
		poi1_ori_w = location.getW();
	}
}
