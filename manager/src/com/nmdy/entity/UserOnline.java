package com.nmdy.entity;

import com.nmdy.customerManage.user.dao.User;
   /**
    * user_online 实体类
    * Tue Nov 18 11:08:09 CST 2014 bisu
    */ 


public class UserOnline{
	private Long id;
	private Long userid;
	private Double lat;
	private Double lng;
	private User user;
	private String groupName;
	private int status;
	private int isonline;
	
	
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getId(){
		return id;
	}
	public void setId(Long id){
	this.id=id;
	}
	public Double getLat(){
		return lat;
	}
	public void setLat(Double lat){
	this.lat=lat;
	}
	public Double getLng(){
		return lng;
	}
	public void setLng(Double lng){
	this.lng=lng;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIsonline() {
		return isonline;
	}
	public void setIsonline(int isonline) {
		this.isonline = isonline;
	}
}

