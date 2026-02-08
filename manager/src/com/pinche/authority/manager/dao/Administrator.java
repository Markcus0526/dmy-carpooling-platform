package com.pinche.authority.manager.dao;

import java.util.List;

import com.nmdy.customerManage.group.dao.Group;

public class Administrator {

	private Integer id;
	private String usercode;
	private String password;
	private String username;
	private String sex;
	private String unitcode;
	private String phoneNum;
	private String phoneNum2;
	private String qq;
	private String email;
	private int level;
	private String note;
	private int deleted;
	private String groupname;
	private String appUnitName;
	private List<Group> listGroup;
	
	public List<Group> getListGroup() {
		return listGroup;
	}

	public void setListGroup(List<Group> listGroup) {
		this.listGroup = listGroup;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPhoneNum2() {
		return phoneNum2;
	}

	public void setPhoneNum2(String phoneNum2) {
		this.phoneNum2 = phoneNum2;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email= email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	public String getAppUnitName() {
		return appUnitName;
	}
	
	public void setAppUnitName(String appUnitName) {
		this.appUnitName = appUnitName;
	}
}
