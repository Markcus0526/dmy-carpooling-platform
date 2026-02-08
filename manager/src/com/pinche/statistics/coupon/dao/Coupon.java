package com.pinche.statistics.coupon.dao;

import java.sql.Timestamp;

public class Coupon {
	private int id;
	private String syscoupon_code;
	private String password;
	private Timestamp sc_date;
	private String remark;
	private int release_channel;
	private int app_spread_unit_id;
	private int goods_or_cash;
	private double sum;
	private String goods;
	private int apply_source;
	private int coupon_type;
	private int limit;
	private int valid_period_unit;
	private int valid_period;
	private String active_code;
	private int limit_count;
	private int isenabled;
	private int deleted;
	private int deployCount;
	private int useCount;
	
	public int getDeployCount() {
		return deployCount;
	}
	public void setDeployCount(int deployCount) {
		this.deployCount = deployCount;
	}
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSyscoupon_code() {
		return syscoupon_code;
	}
	public void setSyscoupon_code(String syscoupon_code) {
		this.syscoupon_code = syscoupon_code;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getSc_date() {
		return sc_date;
	}
	public void setSc_date(Timestamp sc_date) {
		this.sc_date = sc_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getRelease_channel() {
		return release_channel;
	}
	public void setRelease_channel(int release_channel) {
		this.release_channel = release_channel;
	}
	public int getApp_spread_unit_id() {
		return app_spread_unit_id;
	}
	public void setApp_spread_unit_id(int app_spread_unit_id) {
		this.app_spread_unit_id = app_spread_unit_id;
	}
	public int getGoods_or_cash() {
		return goods_or_cash;
	}
	public void setGoods_or_cash(int goods_or_cash) {
		this.goods_or_cash = goods_or_cash;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public int getApply_source() {
		return apply_source;
	}
	public void setApply_source(int apply_source) {
		this.apply_source = apply_source;
	}
	public int getCoupon_type() {
		return coupon_type;
	}
	public void setCoupon_type(int coupon_type) {
		this.coupon_type = coupon_type;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getValid_period_unit() {
		return valid_period_unit;
	}
	public void setValid_period_unit(int valid_period_unit) {
		this.valid_period_unit = valid_period_unit;
	}
	public int getValid_period() {
		return valid_period;
	}
	public void setValid_period(int valid_period) {
		this.valid_period = valid_period;
	}
	public String getActive_code() {
		return active_code;
	}
	public void setActive_code(String active_code) {
		this.active_code = active_code;
	}
	public int getLimit_count() {
		return limit_count;
	}
	public void setLimit_count(int limit_count) {
		this.limit_count = limit_count;
	}
	public int getIsenabled() {
		return isenabled;
	}
	public void setIsenabled(int isenabled) {
		this.isenabled = isenabled;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

}
