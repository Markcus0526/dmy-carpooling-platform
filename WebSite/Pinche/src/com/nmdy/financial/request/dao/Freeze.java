package com.nmdy.financial.request.dao;

public class Freeze {
	
	//freeze_points table fields
	
	private int id;
	private int userid;
	private int source;
	private int adminid;
	private double balance;
	private int state;
	private int freeze_ts_id;
	private int release_ts_id;
	private int deleted;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
	public int getUserid() {
		return this.userid;
	}
	
	public void setSource(int source) {
		this.source = source;
	}
	
	public int getSource() {
		return this.source;
	}
	
	public void setAdminid(int adminid) {
		this.adminid = adminid;
	}
	
	public int getAdminid() {
		return this.adminid;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}
	
	public void setFreeze_ts_id(int freeze_ts_id) {
		this.freeze_ts_id = freeze_ts_id;
	}
	
	public int getFreeze_ts_id() {
		return this.freeze_ts_id;
	}
	
	public void setRelease_ts_id(int release_ts_id) {
		this.release_ts_id = release_ts_id;
	}
	
	public int getRelease_ts_id() {
		return this.release_ts_id;
	}
	
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	public int getDeleted() {
		return this.deleted;
	}
	
}
