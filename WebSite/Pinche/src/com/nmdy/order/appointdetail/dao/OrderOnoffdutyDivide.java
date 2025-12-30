package com.nmdy.order.appointdetail.dao;

public class OrderOnoffdutyDivide {
	private int id;
	private String which_days;
	private int orderdetails_id;
	private int driver_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWhich_days() {
		return which_days;
	}
	public void setWhich_days(String which_days) {
		this.which_days = which_days;
	}
	public int getOrderdetails_id() {
		return orderdetails_id;
	}
	public void setOrderdetails_id(int orderdetails_id) {
		this.orderdetails_id = orderdetails_id;
	}
	public int getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(int driver_id) {
		this.driver_id = driver_id;
	}
}
