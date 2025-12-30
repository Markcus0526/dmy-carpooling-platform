package com.nmdy.financial.account.dao;

import java.util.Date;

public class Account {
	//user table
	private int    userid;
	private String usercode;
	private String username;
	private String userphone;
	//ts table
	private int ts_order_cs_id;
	private float ts_balance;
	private int   ts_account_type;
	private String ts_account;
	private float ts_sum;
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
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
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	public float getTs_balance() {
		return ts_balance;
	}
	public void setTs_balance(float ts_balance) {
		this.ts_balance = ts_balance;
	}
	public int getTs_account_type() {
		return ts_account_type;
	}
	public void setTs_account_type(int ts_account_type) {
		this.ts_account_type = ts_account_type;
	}
	public String getTs_account() {
		return ts_account;
	}
	public void setTs_account(String ts_account) {
		this.ts_account = ts_account;
	}
	public int getTs_order_cs_id() {
		return ts_order_cs_id;
	}
	public void setTs_order_cs_id(int ts_order_cs_id) {
		this.ts_order_cs_id = ts_order_cs_id;
	}
	public float getTs_sum() {
		return ts_sum;
	}
	public void setTs_sum(float ts_sum) {
		this.ts_sum = ts_sum;
	}
	
	
}
