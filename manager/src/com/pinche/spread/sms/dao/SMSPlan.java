package com.pinche.spread.sms.dao;

import java.sql.Timestamp;

public class SMSPlan {
	private int id;
	private String planCode;
	private int clientNum;
	private String msg;
	private float price;
	private int sendMode;
	private Timestamp singleTime;
	private int hasSendTimes;
	private int limitTimes;
	private int regularSendMode;
	private int time1;
	private int time2;
	private int isEnabled;
	private String remark;
	private Timestamp createTime;
	private int deleted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public int getClientNum() {
		return clientNum;
	}

	public void setClientNum(int clientNum) {
		this.clientNum = clientNum;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getSendMode() {
		return sendMode;
	}

	public void setSendMode(int sendMode) {
		this.sendMode = sendMode;
	}

	public Timestamp getSingleTime() {
		return singleTime;
	}

	public void setSingleTime(Timestamp singleTime) {
		this.singleTime = singleTime;
	}

	public int getHasSendTimes() {
		return hasSendTimes;
	}

	public void setHasSendTimes(int hasSendTimes) {
		this.hasSendTimes = hasSendTimes;
	}

	public int getLimitTimes() {
		return limitTimes;
	}

	public void setLimitTimes(int limitTimes) {
		this.limitTimes = limitTimes;
	}

	public int getRegularSendMode() {
		return regularSendMode;
	}

	public void setRegularSendMode(int regularSendMode) {
		this.regularSendMode = regularSendMode;
	}

	public int getTime1() {
		return time1;
	}

	public void setTime1(int time1) {
		this.time1 = time1;
	}

	public int getTime2() {
		return time2;
	}

	public void setTime2(int time2) {
		this.time2 = time2;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
}
