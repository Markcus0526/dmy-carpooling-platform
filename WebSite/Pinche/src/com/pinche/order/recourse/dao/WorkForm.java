package com.pinche.order.recourse.dao;

import java.util.Date;

public class WorkForm {
	private int id;
	private String workform_num;
	private int adm_id;
	private int form_type;
	private int bussi_type;
	private String phone_incoming;
	private int order_cs_id;
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
	public int getOrder_cs_id() {
		return order_cs_id;
	}
	public void setOrder_cs_id(int order_cs_id) {
		this.order_cs_id = order_cs_id;
	}
	public String getOrder_cs_no() {
		return order_cs_no;
	}
	public void setOrder_cs_no(String order_cs_no) {
		this.order_cs_no = order_cs_no;
	}
	public int getOrder_dj_id() {
		return order_dj_id;
	}
	public void setOrder_dj_id(int order_dj_id) {
		this.order_dj_id = order_dj_id;
	}
	public String getOrder_dj_no() {
		return order_dj_no;
	}
	public void setOrder_dj_no(String order_dj_no) {
		this.order_dj_no = order_dj_no;
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
	public int getForm_agree() {
		return form_agree;
	}
	public void setForm_agree(int form_agree) {
		this.form_agree = form_agree;
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
	public void setWf_date(Date wf_date) {
		this.wf_date = wf_date;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
}
