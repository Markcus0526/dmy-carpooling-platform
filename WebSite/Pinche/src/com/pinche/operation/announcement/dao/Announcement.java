package com.pinche.operation.announcement.dao;

import java.sql.Timestamp;

import java.util.*;

/**
* 
* @ClassName: Announcement.java   主键
* @Description: announcement   
* @author haotang365
* @date 2014-8-18 10:16:33
* @version V1.0   
*/
	
public class Announcement { 	
	
	// 主键 
	private int id;
	// 标题 
	private String title;
	// 公告内容 
	private String content;
	// 更改时间 
	private Timestamp ps_date;
	// 发布者，来自于管理员表的主键 
	private int publisher;
	// 每个城市平台信息费分成不一样 
	private String ps_city;
	// 范围:全部，车主 
	private String range;
	// 有效期 
	private Timestamp validate;
	//撤销
	private int deleted;
	

	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}
	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getContent(){
		return this.content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public Timestamp getPs_date(){
		return this.ps_date;
	}
	public void setPs_date(Timestamp ps_date){
		this.ps_date = ps_date;
	}
	public int getPublisher(){
		return this.publisher;
	}
	public void setPublisher(int publisher){
		this.publisher = publisher;
	}
	public String getPs_city(){
		return this.ps_city;
	}
	public void setPs_city(String ps_city){
		this.ps_city = ps_city;
	}
	public String getRange(){
		return this.range;
	}
	public void setRange(String range){
		this.range = range;
	}
	public Timestamp getValidate(){
		return this.validate;
	}
	public void setValidate(Timestamp validate){
		this.validate = validate;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
}
