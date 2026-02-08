package com.pinche.order.appoint.dao;

import java.util.Date;

public class TempInfo {
	// order_temp_details
	private Long id;
	private String order_num;
	private Integer seats_num;
	private Integer is_cancelservice;
	private Date cancelservice_time;
	private Integer is_broadcast_stop;
	private Integer turn_num;
	private Date ps_date;
	private Double start_lat;
	private Double start_lng;
	private String start_addr;
	private Double end_lat;
	private Double end_lng;
	private String end_addr;
	private Date pre_time;
	private Double price;
	private Integer order_cs_id;
	private Integer is_from_onoffdutyorder;
	private Integer orderonoffduty_id;
	private Integer publisher;
	private Integer reqcarstyle;
	private String remark;
	private Integer enabled;
	private Integer deleted;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public Integer getSeats_num() {
		return seats_num;
	}
	public void setSeats_num(Integer seats_num) {
		this.seats_num = seats_num;
	}
	public Integer getIs_cancelservice() {
		return is_cancelservice;
	}
	public void setIs_cancelservice(Integer is_cancelservice) {
		this.is_cancelservice = is_cancelservice;
	}
	public Date getCancelservice_time() {
		return cancelservice_time;
	}
	public void setCancelservice_time(Date cancelservice_time) {
		this.cancelservice_time = cancelservice_time;
	}
	public Integer getIs_broadcast_stop() {
		return is_broadcast_stop;
	}
	public void setIs_broadcast_stop(Integer is_broadcast_stop) {
		this.is_broadcast_stop = is_broadcast_stop;
	}
	public Integer getTurn_num() {
		return turn_num;
	}
	public void setTurn_num(Integer turn_num) {
		this.turn_num = turn_num;
	}
	public Date getPs_date() {
		return ps_date;
	}
	public void setPs_date(Date ps_date) {
		this.ps_date = ps_date;
	}
	public Double getStart_lat() {
		return start_lat;
	}
	public void setStart_lat(Double start_lat) {
		this.start_lat = start_lat;
	}
	public Double getStart_lng() {
		return start_lng;
	}
	public void setStart_lng(Double start_lng) {
		this.start_lng = start_lng;
	}
	public String getStart_addr() {
		return start_addr;
	}
	public void setStart_addr(String start_addr) {
		this.start_addr = start_addr;
	}
	public Double getEnd_lat() {
		return end_lat;
	}
	public void setEnd_lat(Double end_lat) {
		this.end_lat = end_lat;
	}
	public Double getEnd_lng() {
		return end_lng;
	}
	public void setEnd_lng(Double end_lng) {
		this.end_lng = end_lng;
	}
	public String getEnd_addr() {
		return end_addr;
	}
	public void setEnd_addr(String end_addr) {
		this.end_addr = end_addr;
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
	public Integer getOrder_cs_id() {
		return order_cs_id;
	}
	public void setOrder_cs_id(Integer order_cs_id) {
		this.order_cs_id = order_cs_id;
	}
	public Integer getIs_from_onoffdutyorder() {
		return is_from_onoffdutyorder;
	}
	public void setIs_from_onoffdutyorder(Integer is_from_onoffdutyorder) {
		this.is_from_onoffdutyorder = is_from_onoffdutyorder;
	}
	public Integer getOrderonoffduty_id() {
		return orderonoffduty_id;
	}
	public void setOrderonoffduty_id(Integer orderonoffduty_id) {
		this.orderonoffduty_id = orderonoffduty_id;
	}
	public Integer getPublisher() {
		return publisher;
	}
	public void setPublisher(Integer publisher) {
		this.publisher = publisher;
	}
	public Integer getReqcarstyle() {
		return reqcarstyle;
	}
	public void setReqcarstyle(Integer reqcarstyle) {
		this.reqcarstyle = reqcarstyle;
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
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}	
}
