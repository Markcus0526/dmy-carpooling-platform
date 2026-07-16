package com.nmdy.order.recourse.dao;

import java.util.Date;



public class Recourse {
	private int id;
	private String workform_num;
	private int adm_id;
	private int form_type;
	private int bussi_type;
	private String phone_incoming;
	private Long order_cs_id;
	private String order_cs_no;
	private int order_dj_id;
	private String order_dj_no;
	private String customer_name;
	private int sex;
	private String city;
	private String reason;
	private String process_result;
	private int form_agree;
	private int status;
	private Date wf_date;
	private int deleted;
	// user table
	private int driver_verified;
	private int person_verified;
	public int getDriver_verified() {
		return driver_verified;
	}
	public void setDriver_verified(int driver_verified) {
		this.driver_verified = driver_verified;
	}
	
	public int getForm_agree() {
		return form_agree;
	}
	public void setForm_agree(int form_agree) {
		this.form_agree = form_agree;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWorkform_num() {
		return workform_num;
	}
	public void setWorkform_num(String workform_num) {
		this.workform_num = workform_num;
	}
	public int getAdm_id() {
		return adm_id;
	}
	public void setAdm_id(int adm_id) {
		this.adm_id = adm_id;
	}
	public int getForm_type() {
		return form_type;
	}
	public void setForm_type(int form_type) {
		this.form_type = form_type;
	}
	public int getBussi_type() {
		return bussi_type;
	}
	public void setBussi_type(int bussi_type) {
		this.bussi_type = bussi_type;
	}
	public String getPhone_incoming() {
		return phone_incoming;
	}
	public void setPhone_incoming(String phone_incoming) {
		this.phone_incoming = phone_incoming;
	}	
	public Long getOrder_cs_id() {
		return order_cs_id;
	}
	public void setOrder_cs_id(Long order_cs_id) {
		this.order_cs_id = order_cs_id;
	}
	public String getOrder_cs_no() {
		return order_cs_no;
	}
	public void setOrder_cs_no(String order_cs_no) {
		this.order_cs_no = order_cs_no;
	}
	public String getOrder_dj_no() {
		return order_dj_no;
	}
	public void setOrder_dj_no(String order_dj_no) {
		this.order_dj_no = order_dj_no;
	}
	public int getOrder_dj_id() {
		return order_dj_id;
	}
	public void setOrder_dj_id(int order_dj_id) {
		this.order_dj_id = order_dj_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getProcess_result() {
		return process_result;
	}
	public void setProcess_result(String process_result) {
		this.process_result = process_result;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getWf_date() {
		return wf_date;
	}
	public void setWf_date(Date temp) {
		this.wf_date = temp;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public int getPerson_verified() {
		return person_verified;
	}
	public void setPerson_verified(int person_verified) {
		this.person_verified = person_verified;
	}
	
}
