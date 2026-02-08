package com.nmdy.webService.dao;

import java.util.Date;

public class InsuranceDTD {

    
	private long id;  //主键
	private long insu_id;//保单表的主键
	private String appl_no;//投保单号
	private int oper_type;//操作类型，0投保，1撤保
	private Date oper_time;//操作时间
	private double insu_sum;//保单的保额
	private int hasFetched;//是否已被保险公司获取

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getInsu_id() {
		return insu_id;
	}

	public void setInsu_id(long insu_id) {
		this.insu_id = insu_id;
	}

	public String getAppl_no() {
		return appl_no;
	}

	public void setAppl_no(String appl_no) {
		this.appl_no = appl_no;
	}

	public int getOper_type() {
		return oper_type;
	}

	public void setOper_type(int oper_type) {
		this.oper_type = oper_type;
	}

	public Date getOper_time() {
		return oper_time;
	}

	public void setOper_time(Date oper_time) {
		this.oper_time = oper_time;
	}

	public double getInsu_sum() {
		return insu_sum;
	}

	public void setInsu_sum(double insu_sum) {
		this.insu_sum = insu_sum;
	}

	public int getHasFetched() {
		return hasFetched;
	}

	public void setHasFetched(int hasFetched) {
		this.hasFetched = hasFetched;
	}
	
}
