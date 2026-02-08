package com.pinche.order.appointdetail.dao;

public class FreezeTsInfo {
	private int id;
	private int userId;
	private double balance;
	private int freeze_ts_id;
	private double ts_balance;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getFreeze_ts_id() {
		return freeze_ts_id;
	}
	public void setFreeze_ts_id(int freeze_ts_id) {
		this.freeze_ts_id = freeze_ts_id;
	}
	public double getTs_balance() {
		return ts_balance;
	}
	public void setTs_balance(double ts_balance) {
		this.ts_balance = ts_balance;
	}
}
