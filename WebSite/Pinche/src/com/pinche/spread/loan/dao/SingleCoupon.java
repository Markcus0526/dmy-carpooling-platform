package com.pinche.spread.loan.dao;

public class SingleCoupon {

	private long id;
	private String singleCouponCode = "";
	private int sum = 0;
	private String dateExpired = null;
	private int isused = 0;
	private String dateUsed = null;
	private String password = "";
	private String psDate = null;
	private String remark = "";
	private long userid = 0;
	private long orderCsId = 0;
	private int isenabled = 0;
	private long syscouponId = 0;
	private String activeCode = "";
	private int isGeneratedByActive = 0;
	private int deleted = 0;

	public String getSingleCouponCode() {
		return singleCouponCode;
	}
	public void setSingleCouponCode(String singleCouponCode) {
		this.singleCouponCode = singleCouponCode;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public String getDateExpired() {
		return dateExpired;
	}
	public void setDateExpired(String dateExpired) {
		this.dateExpired = dateExpired;
	}
	public int getIsused() {
		return isused;
	}
	public void setIsused(int isused) {
		this.isused = isused;
	}
	public String getDateUsed() {
		return dateUsed;
	}
	public void setDateUsed(String dateUsed) {
		this.dateUsed = dateUsed;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPsDate() {
		return psDate;
	}
	public void setPsDate(String psDate) {
		this.psDate = psDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getIsenabled() {
		return isenabled;
	}
	public void setIsenabled(int isenabled) {
		this.isenabled = isenabled;
	}
	public String getActiveCode() {
		return activeCode;
	}
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	public int getIsGeneratedByActive() {
		return isGeneratedByActive;
	}
	public void setIsGeneratedByActive(int isGeneratedByActive) {
		this.isGeneratedByActive = isGeneratedByActive;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public long getOrderCsId() {
		return orderCsId;
	}
	public void setOrderCsId(long orderCsId) {
		this.orderCsId = orderCsId;
	}
	public long getSyscouponId() {
		return syscouponId;
	}
	public void setSyscouponId(long syscouponId) {
		this.syscouponId = syscouponId;
	}
	
	
}
