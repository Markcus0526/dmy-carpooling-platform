package com.nmdy.order.appointdetail.dao;

public class OrderLongdistanceUsersCs {
	private int		id;
	private int 	orderdriverlongdistance_id;
	private int 	userid;
	private int 	order_exec_cs_id;
	private int 	seat_num;
	private int		ps_date;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderdriverlongdistance_id() {
		return orderdriverlongdistance_id;
	}
	public void setOrderdriverlongdistance_id(int orderdriverlongdistance_id) {
		this.orderdriverlongdistance_id = orderdriverlongdistance_id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getOrder_exec_cs_id() {
		return order_exec_cs_id;
	}
	public void setOrder_exec_cs_id(int order_exec_cs_id) {
		this.order_exec_cs_id = order_exec_cs_id;
	}
	public int getSeat_num() {
		return seat_num;
	}
	public void setSeat_num(int seat_num) {
		this.seat_num = seat_num;
	}
	public int getPs_date() {
		return ps_date;
	}
	public void setPs_date(int ps_date) {
		this.ps_date = ps_date;
	}
}
