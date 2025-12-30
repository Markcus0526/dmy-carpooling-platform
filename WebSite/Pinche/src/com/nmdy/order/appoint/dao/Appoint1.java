package com.nmdy.order.appoint.dao;

import java.util.Date;

public class Appoint1 {
	private Integer 	id;
	private int 	order_type;
	private int 	passenger;
	private int 	driver;
	private String 	from_;
	private String 	to_;
	private double 	average_price_platform;
	private double 	price;
	private String	city_from;
	private String 	city_to;
	private int 	has_evaluation_passenger;
	private int 	has_evaluation_driver;
	private String 	password;
	private double 	begin_lat;
	private double 	begin_lng;
	private double 	end_lat;
	private double 	end_lng;
	private double 	freeze_points;
	private double 	total_distance;
	private String 	order_city;
	private int 	status;	
	private int 	deleted;
	private Date 	begin_exec_time;
	private Date 	cr_date;
	private Date 	ti_accept_order;
	private Date 	beginservice_time;
	private Date 	stopservice_time;
	private Date 	pay_time;
	private Date 	pass_cancel_time;
	private Date 	driver_cancel_time;
	private Date 	pre_time;
	private String 	remark;
    private String  order_num;
	private Date 	driverarrival_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getOrder_type() {
		return order_type;
	}

	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}

	public int getPassenger() {
		return passenger;
	}

	public void setPassenger(int passenger) {
		this.passenger = passenger;
	}

	public int getDriver() {
		return driver;
	}

	public void setDriver(int driver) {
		this.driver = driver;
	}

	public String getFrom_() {
		return from_;
	}

	public void setFrom_(String from_) {
		this.from_ = from_;
	}

	public String getTo_() {
		return to_;
	}

	public void setTo_(String to_) {
		this.to_ = to_;
	}

	public double getAverage_price_platform() {
		return average_price_platform;
	}

	public void setAverage_price_platform(double average_price_platform) {
		this.average_price_platform = average_price_platform;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCity_from() {
		return city_from;
	}

	public void setCity_from(String city_from) {
		this.city_from = city_from;
	}

	public String getCity_to() {
		return city_to;
	}

	public void setCity_to(String city_to) {
		this.city_to = city_to;
	}

	public int getHas_evaluation_passenger() {
		return has_evaluation_passenger;
	}

	public void setHas_evaluation_passenger(int has_evaluation_passenger) {
		this.has_evaluation_passenger = has_evaluation_passenger;
	}

	public int getHas_evaluation_driver() {
		return has_evaluation_driver;
	}

	public void setHas_evaluation_driver(int has_evaluation_driver) {
		this.has_evaluation_driver = has_evaluation_driver;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getBegin_lat() {
		return begin_lat;
	}

	public void setBegin_lat(double begin_lat) {
		this.begin_lat = begin_lat;
	}

	public double getBegin_lng() {
		return begin_lng;
	}

	public void setBegin_lng(double begin_lng) {
		this.begin_lng = begin_lng;
	}

	public double getEnd_lat() {
		return end_lat;
	}

	public void setEnd_lat(double end_lat) {
		this.end_lat = end_lat;
	}

	public double getEnd_lng() {
		return end_lng;
	}

	public void setEnd_lng(double end_lng) {
		this.end_lng = end_lng;
	}

	public double getFreeze_points() {
		return freeze_points;
	}

	public void setFreeze_points(double freeze_points) {
		this.freeze_points = freeze_points;
	}

	public double getTotal_distance() {
		return total_distance;
	}

	public void setTotal_distance(double total_distance) {
		this.total_distance = total_distance;
	}

	public String getOrder_city() {
		return order_city;
	}

	public void setOrder_city(String order_city) {
		this.order_city = order_city;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Date getBegin_exec_time() {
		return begin_exec_time;
	}

	public void setBegin_exec_time(Date begin_exec_time) {
		this.begin_exec_time = begin_exec_time;
	}

	public Date getCr_date() {
		return cr_date;
	}

	public void setCr_date(Date cr_date) {
		this.cr_date = cr_date;
	}

	public Date getTi_accept_order() {
		return ti_accept_order;
	}

	public void setTi_accept_order(Date ti_accept_order) {
		this.ti_accept_order = ti_accept_order;
	}

	public Date getBeginservice_time() {
		return beginservice_time;
	}

	public void setBeginservice_time(Date beginservice_time) {
		this.beginservice_time = beginservice_time;
	}

	public Date getStopservice_time() {
		return stopservice_time;
	}

	public void setStopservice_time(Date stopservice_time) {
		this.stopservice_time = stopservice_time;
	}

	public Date getPay_time() {
		return pay_time;
	}

	public void setPay_time(Date pay_time) {
		this.pay_time = pay_time;
	}

	public Date getPass_cancel_time() {
		return pass_cancel_time;
	}

	public void setPass_cancel_time(Date pass_cancel_time) {
		this.pass_cancel_time = pass_cancel_time;
	}

	public Date getDriver_cancel_time() {
		return driver_cancel_time;
	}

	public void setDriver_cancel_time(Date driver_cancel_time) {
		this.driver_cancel_time = driver_cancel_time;
	}

	public Date getPre_time() {
		return pre_time;
	}

	public void setPre_time(Date pre_time) {
		this.pre_time = pre_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDriverarrival_time() {
		return driverarrival_time;
	}

	public void setDriverarrival_time(Date driverarrival_time) {
		this.driverarrival_time = driverarrival_time;
	}

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	
}
