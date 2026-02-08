package com.nmdy.financial.ledger.dao;

import java.util.Date;

public class Ledger {

	private int ts_id;
	private int order_cs_id;
	private int passenger_id;
	private String passenger_name;
	private String passReqId;
	private String passReqName;
	private int driver_id;
	private String driver_name;
	private String drivReqId;
	private String drivReqName;
	private float order_balance;
	private String city;
	private Date   date;
	private int bussi_type;
	private int transaction_type;
	private String status_type;
	private float sum;
	private float balance;
	private String remark;
	private String passenger_invitecode_regist;
	private String driver_invitecode_regist;
	//Ts table
	private int ts_way;
	private int userid;
	private int groupid;
	private int unitid;
	private String account;
	private int account_type;
	private int deleted;
	private String application;
	
	
	public int getTs_id() {
		return ts_id;
	}
	public void setTs_id(int ts_id) {
		this.ts_id = ts_id;
	}
	
	public int getOrder_cs_id() {
		return order_cs_id;
	}
	public void setOrder_cs_id(int order_cs_id) {
		this.order_cs_id = order_cs_id;
	}
	public int getPassenger_id() {
		return passenger_id;
	}
	public void setPassenger_id(int passenger_id) {
		this.passenger_id = passenger_id;
	}
	public String getPassenger_name() {
		return passenger_name;
	}
	public void setPassenger_name(String passenger_name) {
		this.passenger_name = passenger_name;
	}
	public String getPassReqId() {
		return passReqId;
	}
	public void setPassReqId(String passReqId) {
		this.passReqId = passReqId;
	}
	public String getPassReqName() {
		return passReqName;
	}
	public void setPassReqName(String passReqName) {
		this.passReqName = passReqName;
	}
	public int getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(int driver_id) {
		this.driver_id = driver_id;
	}
	public String getDriver_name() {
		return driver_name;
	}
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	public String getDrivReqId() {
		return drivReqId;
	}
	public void setDrivReqId(String drivReqId) {
		this.drivReqId = drivReqId;
	}
	public String getDrivReqName() {
		return drivReqName;
	}
	public void setDrivReqName(String drivReqName) {
		this.drivReqName = drivReqName;
	}
	public float getOrder_balance() {
		return order_balance;
	}
	public void setOrder_balance(float order_balance) {
		this.order_balance = order_balance;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getBussi_type() {
		return bussi_type;
	}
	public void setBussi_type(int bussi_type) {
		this.bussi_type = bussi_type;
	}
	public int getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(int transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getStatus_type() {
		return status_type;
	}
	public void setStatus_type(String status_type) {
		this.status_type = status_type;
	}
	public float getSum() {
		return sum;
	}
	public void setSum(float sum) {
		this.sum = sum;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getTs_way() {
		return ts_way;
	}
	public void setTs_way(int ts_way) {
		this.ts_way = ts_way;
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
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getPassenger_invitecode_regist() {
		return passenger_invitecode_regist;
	}
	public void setPassenger_invitecode_regist(String passenger_invitecode_regist) {
		this.passenger_invitecode_regist = passenger_invitecode_regist;
	}
	public String getDriver_invitecode_regist() {
		return driver_invitecode_regist;
	}
	public void setDriver_invitecode_regist(String driver_invitecode_regist) {
		this.driver_invitecode_regist = driver_invitecode_regist;
	}
}
