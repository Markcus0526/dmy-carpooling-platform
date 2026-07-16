package com.nmdy.financial.account.dao;

import java.util.Date;

public class TransactionDetail {
	//ts table
	private int id;
	private int order_cs_id;
	private int oper;
	private int ts_way;
	private float balance;
	private Date ts_date;
	private int userid;
	private int groupid;
	private int unitid;
	private String remark;
	private String account;
	private int account_type;
	private String application;
	private String ts_type;
	private float sum;
	private int deleted;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrder_cs_id() {
		return order_cs_id;
	}
	public void setOrder_cs_id(int order_cs_id) {
		this.order_cs_id = order_cs_id;
	}
	public int getOper() {
		return oper;
	}
	public void setOper(int oper) {
		this.oper = oper;
	}
	public int getTs_way() {
		return ts_way;
	}
	public void setTs_way(int ts_way) {
		this.ts_way = ts_way;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public Date getTs_date() {
		return ts_date;
	}
	public void setTs_date(Date ts_date) {
		this.ts_date = ts_date;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public int getUnitid() {
		return unitid;
	}
	public void setUnitid(int unitid) {
		this.unitid = unitid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getAccount_type() {
		return account_type;
	}
	public void setAccount_type(int account_type) {
		this.account_type = account_type;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getTs_type() {
		return ts_type;
	}
	public void setTs_type(String ts_type) {
		this.ts_type = ts_type;
	}
	public float getSum() {
		return sum;
	}
	public void setSum(float sum) {
		this.sum = sum;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
}
