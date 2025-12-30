package com.nmdy.customerManage.user.dao;

public class PushInfo {
	long userId;
	String userIdPush;
	String channelId;
	int phoneSystem;
	String pushToken;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserIdPush() {
		return userIdPush;
	}
	public void setUserIdPush(String userIdPush) {
		this.userIdPush = userIdPush;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public int getPhoneSystem() {
		return phoneSystem;
	}
	public void setPhoneSystem(int phoneSystem) {
		this.phoneSystem = phoneSystem;
	}
	public String getPushToken() {
		return pushToken;
	}
	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
		String[] fields = pushToken.split(",");
		
		if(fields.length >= 1){
			this.setUserIdPush(fields[0]);
			this.setChannelId(fields[1]);
		}
	}
}
