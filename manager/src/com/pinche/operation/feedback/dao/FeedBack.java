package com.pinche.operation.feedback.dao;

import java.sql.Timestamp;

/**
 * ç”¨æˆ·å��é¦ˆä¿¡æ�¯çš„å®žä½“ç±»
 * @author è‚–å¨œå¨œ
 * @Date 2014-08-09
 */
public class FeedBack {
	//table feedback and table user
	private Long id;
	private Long userid;
	private String title;
	private Timestamp psDate;
	private Short deleted;
	private String username;
	private String phone;
	private int driver_verified;
	private String city_cur;
	private String content;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getPsDate() {
		return psDate;
	}
	public void setPsDate(Timestamp psDate) {
		this.psDate = psDate;
	}
	public Short getDeleted() {
		return deleted;
	}
	public void setDeleted(Short deleted) {
		this.deleted = deleted;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getDriver_verified() {
		return driver_verified;
	}
	public void setDriver_verified(int driver_verified) {
		this.driver_verified = driver_verified;
	}
	
	public String getCity_cur() {
		return city_cur;
	}
	public void setCity_cur(String city_cur) {
		this.city_cur = city_cur;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
