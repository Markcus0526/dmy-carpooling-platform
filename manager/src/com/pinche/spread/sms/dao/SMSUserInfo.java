package com.pinche.spread.sms.dao;

import com.pinche.customer.user.dao.UserBasicInfo;

public class SMSUserInfo extends UserBasicInfo {
	private int successCount = 0;
	private int failCount = 0;
	private int planId = 0;
	
	public int getSuccessCount() {
		return successCount;
	}
	
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	
	public int getFailCount() {
		return failCount;
	}
	
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}
}
