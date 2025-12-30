package com.nmdy.spread.loan.dao;

public class CouponSendLog {

	private int id;
	private String couponCode = "";
	private int operatorId = 0;
	private int sendType = 0;
	private int num = 0;
	private String sendTime = "";
	private String msg = "";
	private String remark = "";
	private int deleted = 0;
	
	//appendix
	private int syscouponId = 0;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public int getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public int getSyscouponId() {
		return syscouponId;
	}
	public void setSyscouponId(int syscouponId) {
		this.syscouponId = syscouponId;
	}
	
	
}
