package com.nmdy.entity;

   /**
    * administrator 实体类
    * Thu Oct 09 18:08:43 CST 2014 bisu
    */ 


public class Admin{
	private Integer id;
	private String usercode;
	private String password;
	private String username;
	private Integer unit;
	private String phoneNum;
	private String sex;
	private String phoneNum2;
	private String qq;
	private String email;
	private Integer level;
	private String note;
	private Integer deleted;
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
	this.id=id;
	}
	public String getUsercode(){
		return usercode;
	}
	public void setUsercode(String usercode){
	this.usercode=usercode;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
	this.password=password;
	}
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
	this.username=username;
	}
	public Integer getUnit(){
		return unit;
	}
	public void setUnit(Integer unit){
	this.unit=unit;
	}
	public String getPhoneNum(){
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum){
	this.phoneNum=phoneNum;
	}
	public String getSex(){
		return sex;
	}
	public void setSex(String sex){
	this.sex=sex;
	}
	public String getPhoneNum2(){
		return phoneNum2;
	}
	public void setPhoneNum2(String phoneNum2){
	this.phoneNum2=phoneNum2;
	}
	public String getQq(){
		return qq;
	}
	public void setQq(String qq){
	this.qq=qq;
	}
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
	this.email=email;
	}
	public Integer getLevel(){
		return level;
	}
	public void setLevel(Integer level){
	this.level=level;
	}
	public String getNote(){
		return note;
	}
	public void setNote(String note){
	this.note=note;
	}
	public Integer getDeleted(){
		return deleted;
	}
	public void setDeleted(Integer deleted){
	this.deleted=deleted;
	}
}

