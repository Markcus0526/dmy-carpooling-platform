package com.pinche.customer.user.dao;

import java.sql.Timestamp;

public class UserBasicInfo {
	protected int id = 0;
	protected String usercode;
	protected String username;
	protected String nickname;
	protected int personVerified = 0;
	protected int driverVerified = 0;
	protected String phone;
	protected String groupId;
	protected String groupName;
	protected Timestamp regDate;
	protected Timestamp lastLoginTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getPersonVerified() {
		return personVerified;
	}

	public void setPersonVerified(int personVerified) {
		this.personVerified = personVerified;
	}

	public int getDriverVerified() {
		return driverVerified;
	}

	public void setDriverVerified(int driverVerified) {
		this.driverVerified = driverVerified;
	}
}
