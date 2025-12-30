package com.nmdy.spread.loan.dao;

public class Activities {

	private int id;
	private String activeCode;
	private int limitCount;
	private int syscouponId;
	private String atDate;
	private int isenabled;
	private int deleted;
	
	// appendix
	private String syscouponCode;	// from sys_coupon table
	private int point;	// from sys_coupon table
	private int deployCount;
	private int couponType;		// from sys_coupon table
	private int limit_val;		// meaningful in case couponType=1. refer to sys_coupon table	// from sys_coupon table
	private int valid_period_unit;	// from sys_coupon table
	private int valid_period;	// from sys_coupon table
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getActiveCode() {
		return activeCode;
	}
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	public int getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}
	public int getSyscouponId() {
		return syscouponId;
	}
	public void setSyscouponId(int syscouponId) {
		this.syscouponId = syscouponId;
	}
	public String getAtDate() {
		return atDate;
	}
	public void setAtDate(String atDate) {
		this.atDate = atDate;
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
	public String getSyscouponCode() {
		return syscouponCode;
	}
	public void setSyscouponCode(String syscouponCode) {
		this.syscouponCode = syscouponCode;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getDeployCount() {
		return deployCount;
	}
	public void setDeployCount(int deployCount) {
		this.deployCount = deployCount;
	}
	public int getCouponType() {
		return couponType;
	}
	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}
	
	public int getLimit_val() {
		return limit_val;
	}
	public void setLimit_val(int limit_val) {
		this.limit_val = limit_val;
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
	
	
}
