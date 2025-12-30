package com.pinche.order.appoint.dao;

import java.util.Date;

public class LongDistanceInfo {
	// order_longdistance_users_cs
	private Long id;
	private Integer orderdriverlongdistance_id;
	private Integer userid;
	private Integer order_exec_cs_id;
	private Integer seat_num;
	private Date ps_date;
	
	// order_longdistance_details
	private String order_num;
	private Date pre_time;
	private Double price;
	private String start_city;
	private String end_city;
	private Integer publisher;
	private Date ps_time;
	private Integer occupied_num;
	private String remark;
	private Integer enabled;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getOrderdriverlongdistance_id() {
		return orderdriverlongdistance_id;
	}
	public void setOrderdriverlongdistance_id(Integer orderdriverlongdistance_id) {
		this.orderdriverlongdistance_id = orderdriverlongdistance_id;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getOrder_exec_cs_id() {
		return order_exec_cs_id;
	}
	public void setOrder_exec_cs_id(Integer order_exec_cs_id) {
		this.order_exec_cs_id = order_exec_cs_id;
	}
	public Integer getSeat_num() {
		return seat_num;
	}
	public void setSeat_num(Integer seat_num) {
		this.seat_num = seat_num;
	}
	public Date getPs_date() {
		return ps_date;
	}
	public void setPs_date(Date ps_date) {
		this.ps_date = ps_date;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public Date getPre_time() {
		return pre_time;
	}
	public void setPre_time(Date pre_time) {
		this.pre_time = pre_time;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getStart_city() {
		return start_city;
	}
	public void setStart_city(String start_city) {
		this.start_city = start_city;
	}
	public String getEnd_city() {
		return end_city;
	}
	public void setEnd_city(String end_city) {
		this.end_city = end_city;
	}
	public Integer getPublisher() {
		return publisher;
	}
	public void setPublisher(Integer publisher) {
		this.publisher = publisher;
	}
	public Date getPs_time() {
		return ps_time;
	}
	public void setPs_time(Date ps_time) {
		this.ps_time = ps_time;
	}
	public Integer getOccupied_num() {
		return occupied_num;
	}
	public void setOccupied_num(Integer occupied_num) {
		this.occupied_num = occupied_num;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}	
}
