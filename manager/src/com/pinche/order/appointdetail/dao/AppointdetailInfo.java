package com.pinche.order.appointdetail.dao;

import java.util.Date;

public class AppointdetailInfo {
	private long 	id;
	private String 	order_num;
	private int 	order_type;
	private String 	city_from;
	private long  driver;
	private double 	total_sum;
	private long 	passenger;
	private double	price;	
	private String 	start_addr;
	private String 	end_addr;
	private Date 	beginservice_time;
	private Date 	pay_time;
	private Date 	ps_date;
	private Date 	accept_time;
	private Date 	driverarrival_time;
	private Date  	begin_exec_time;
	private Date 	endservice_time;
	private Date 	pre_time;
	private int 	status;
	private int 	reqcarstyle;
	private String 	passengerRemark;
	private String 	driverRemark;
	private String 	weekdays;
	private long 	orderExecId;
	private String start_city;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public int getOrder_type() {
		return order_type;
	}
	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}
	public String getCity_from() {
		return city_from;
	}
	public void setCity_from(String city_from) {
		this.city_from = city_from;
	}

	public Double getTotal_sum() {
		return total_sum;
	}
	public void setTotal_sum(Double total_sum) {
		this.total_sum = total_sum;
	}

	public Long getDriver() {
		return driver;
	}
	public void setDriver(Long driver) {
		this.driver = driver;
	}
	public Long getPassenger() {
		return passenger;
	}
	public void setPassenger(Long passenger) {
		this.passenger = passenger;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Date getBeginservice_time() {
		return beginservice_time;
	}
	public void setBeginservice_time(Date beginservice_time) {
		this.beginservice_time = beginservice_time;
	}
	public Date getPay_time() {
		return pay_time;
	}
	public void setPay_time(Date pay_time) {
		this.pay_time = pay_time;
	}
	public String getStart_addr() {
		return start_addr;
	}
	public void setStart_addr(String start_addr) {
		this.start_addr = start_addr;
	}
	public String getEnd_addr() {
		return end_addr;
	}
	public void setEnd_addr(String end_addr) {
		this.end_addr = end_addr;
	}
	public Date getPs_date() {
		return ps_date;
	}
	public void setPs_date(Date ps_date) {
		this.ps_date = ps_date;
	}
	public Date getAccept_time() {
		return accept_time;
	}
	public void setAccept_time(Date accept_time) {
		this.accept_time = accept_time;
	}
	public Date getDriverarrival_time() {
		return driverarrival_time;
	}
	public void setDriverarrival_time(Date driverarrival_time) {
		this.driverarrival_time = driverarrival_time;
	}
	public Date getBegin_exec_time() {
		return begin_exec_time;
	}
	public void setBegin_exec_time(Date begin_exec_time) {
		this.begin_exec_time = begin_exec_time;
	}
	public Date getEndservice_time() {
		return endservice_time;
	}
	public void setEndservice_time(Date endservice_time) {
		this.endservice_time = endservice_time;
	}
	public Date getPre_time() {
		return pre_time;
	}
	public void setPre_time(Date pre_time) {
		this.pre_time = pre_time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getReqcarstyle() {
		return reqcarstyle;
	}
	public void setReqcarstyle(int reqcarstyle) {
		this.reqcarstyle = reqcarstyle;
	}
	public String getPassengerRemark() {
		return passengerRemark;
	}
	public void setPassengerRemark(String passengerRemark) {
		this.passengerRemark = passengerRemark;
	}
	public String getDriverRemark() {
		return driverRemark;
	}
	public void setDriverRemark(String driverRemark) {
		this.driverRemark = driverRemark;
	}
	public String getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}
	public long getOrderExecId() {
		return orderExecId;
	}
	public void setOrderExecId(long orderExecId) {
		this.orderExecId = orderExecId;
	}
	public String getStart_city() {
		return start_city;
	}
	public void setStart_city(String start_city) {
		this.start_city = start_city;
	}
	public void setDriver(long driver) {
		this.driver = driver;
	}
	public void setTotal_sum(double total_sum) {
		this.total_sum = total_sum;
	}
	public void setPassenger(long passenger) {
		this.passenger = passenger;
	}
	public void setPrice(double price) {
		this.price = price;
	}

}
