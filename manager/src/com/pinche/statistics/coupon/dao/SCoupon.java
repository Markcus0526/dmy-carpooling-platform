package com.pinche.statistics.coupon.dao;

import java.sql.Timestamp;

public class SCoupon {
	private int id;
	private String syscoupon_code;
	private Timestamp sc_date;
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
	private Timestamp used_date;
	private int isused;
	
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
	public Timestamp getSc_date() {
		return sc_date;
	}
	public void setSc_date(Timestamp sc_date) {
		this.sc_date = sc_date;
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
	public Timestamp getUsed_date() {
		return used_date;
	}
	public void setUsed_date(Timestamp used_date) {
		this.used_date = used_date;
	}
	public int getIsused() {
		return isused;
	}
	public void setIsused(int isused) {
		this.isused = isused;
	}
	
		
}
