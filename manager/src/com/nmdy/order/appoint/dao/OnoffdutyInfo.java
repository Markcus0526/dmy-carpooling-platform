package com.nmdy.order.appoint.dao;

import java.util.Date;

public class OnoffdutyInfo {
	// order_onoffduty_exec_details
	private Long id;
	
	// order_onoffduty_divide
	private String which_days;
	private Integer orderdetails_id;
	private Integer driver_id;
	
	// order_onoffduty_details
	private String order_num;
	private Integer is_cancelservice;
	private Date cancelservice_time;
	private Integer alldays_beaccepted;
	private Date from_date;
	private Double price;
	private Double start_lat;
	private Double start_lng;
	private String start_addr;
	private Double end_lat;
	private Double end_lng;
	private String end_addr;
	private String leftdays;
	private Date pre_time;
	private Integer publisher;
	private Date publish_date;
	private Integer reqcarstyle;
	private String remark;
	private Integer enabled;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWhich_days() {
		return which_days;
	}
	public void setWhich_days(String which_days) {
		this.which_days = which_days;
	}
	public Integer getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(Integer driver_id) {
		this.driver_id = driver_id;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
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
	public Integer getAlldays_beaccepted() {
		return alldays_beaccepted;
	}
	public void setAlldays_beaccepted(Integer alldays_beaccepted) {
		this.alldays_beaccepted = alldays_beaccepted;
	}
	public Date getFrom_date() {
		return from_date;
	}
	public void setFrom_date(Date from_date) {
		this.from_date = from_date;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
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
	public String getLeftdays() {
		return leftdays;
	}
	public void setLeftdays(String leftdays) {
		this.leftdays = leftdays;
	}
	public Date getPre_time() {
		return pre_time;
	}
	public void setPre_time(Date pre_time) {
		this.pre_time = pre_time;
	}
	public Integer getPublisher() {
		return publisher;
	}
	public void setPublisher(Integer publisher) {
		this.publisher = publisher;
	}
	public Date getPublish_date() {
		return publish_date;
	}
	public void setPublish_date(Date publish_date) {
		this.publish_date = publish_date;
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
	public Integer getOrderdetails_id() {
		return orderdetails_id;
	}
	public void setOrderdetails_id(Integer orderdetails_id) {
		this.orderdetails_id = orderdetails_id;
	}
}
