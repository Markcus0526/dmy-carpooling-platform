package com.pinche.order.appointdetail.dao;

import java.util.Date;

public class OrderModifyLogInfo {
	private int id;
	private int modifier;
	private Date md_time;
	private String md_column;
	private String old_value;
	private String new_value;
	private int order_exec_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getModifier() {
		return modifier;
	}
	public void setModifier(int modifier) {
		this.modifier = modifier;
	}
	public Date getMd_time() {
		return md_time;
	}
	public void setMd_time(Date md_time) {
		this.md_time = md_time;
	}
	public String getMd_column() {
		return md_column;
	}
	public void setMd_column(String md_column) {
		this.md_column = md_column;
	}
	public String getOld_value() {
		return old_value;
	}
	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}
	public String getNew_value() {
		return new_value;
	}
	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}
	public int getOrder_exec_id() {
		return order_exec_id;
	}
	public void setOrder_exec_id(int order_exec_id) {
		this.order_exec_id = order_exec_id;
	}
}
