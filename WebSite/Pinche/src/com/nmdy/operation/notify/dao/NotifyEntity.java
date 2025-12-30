package com.nmdy.operation.notify.dao;
public class NotifyEntity {
	private String group_name;// 集团名
	private int userid;// userid
	private String username;// 名
	private String phone;// 用户电话号码
	private String msg;// 评价内容
	private String reg_date;//注册时间
	private String last_login_time;//最后登录时间
	private String driver_verified;
	private String usercode;
	private int receiver;
	
	public int getReceiver() {
		return receiver;
	}
	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getDriver_verified() {
		return driver_verified;
	}
	public void setDriver_verified(String driver_verified) {
		this.driver_verified = driver_verified;
	}
	private int totle;
	
	public int getTotle() {
		return totle;
	}
	public void setTotle(int totle) {
		this.totle = totle;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}
	
	

}
