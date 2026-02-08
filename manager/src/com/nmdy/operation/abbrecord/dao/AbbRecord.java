package com.nmdy.operation.abbrecord.dao;
import java.util.Date;

public class AbbRecord {
	//table abb_record and table user
	private long userid;
	private String username;
	private String phone;
	private int abb_type;//"1,乘客一天内X次取消订单2,车主迟到X分钟3,乘客不去4,车主不去"
	private Date limit_days_begin;//加入黑名单起始日期
	private long order_exec_id;//订单交易编号
	private int status;//1,待处理，2已做警告处罚，3已做扣款处罚，4已做加入黑名单处罚
	private long id;
	private int limit_days;  //如果此值大于0，单表黑名单天数，如果为-1代表永久加入黑名单
	private int cancel_number;
	private Date abb_time;//违约日期
	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getAbb_type() {
		return abb_type;
	}
	public void setAbb_type(int abb_type) {
		this.abb_type = abb_type;
	}

	public Date getLimit_days_begin() {
		return limit_days_begin;
	}
	public void setLimit_days_begin(Date limit_days_begin) {
		this.limit_days_begin = limit_days_begin;
	}
	public long getOrder_exec_id() {
		return order_exec_id;
	}
	public void setOrder_exec_id(long order_exec_id) {
		this.order_exec_id = order_exec_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getLimit_days() {
		return limit_days;
	}
	public void setLimit_days(int limit_days) {
		this.limit_days = limit_days;
	}
	public int getCancel_number() {
		return cancel_number;
	}
	public void setCancel_number(int cancel_number) {
		this.cancel_number = cancel_number;
	}
	public Date getAbb_time() {
		return abb_time;
	}
	public void setAbb_time(Date abb_time) {
		this.abb_time = abb_time;
	}
	
	
}
