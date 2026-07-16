package com.nmdy.entity;

import java.io.Serializable;

public class Tip implements Serializable{
	private long id;
	private long user_id;
	private String tip_name;
	private int is_deleted;
	private int type;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getTip_name() {
		return tip_name;
	}
	public void setTip_name(String tip_name) {
		this.tip_name = tip_name;
	}
	public int getIs_deleted() {
		return is_deleted;
	}
	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
