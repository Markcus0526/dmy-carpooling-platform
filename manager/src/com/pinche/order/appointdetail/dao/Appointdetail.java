package com.pinche.order.appointdetail.dao;

import java.util.Date;

public class Appointdetail {
	private  long  id;
	private String		order_num;
	private String 		start_city;
	private int 		order_type;
	private int 		passenger;
	private int 		driver;
	private int 		status;
	private Date 		ps_date;			// status = 1
	private Date 		accept_time;		// status = 2
	private Date 		begin_exec_time;	// status = 3
	private Date 		driverarrival_time; // status = 4
	private Date 		beginservice_time;	// status = 5
	private Date 		endservice_time;	// status = 6
	private Date 		pay_time;			// status = 7
	private Date 		pass_cancel_time;
	private Date 		driver_cancel_time;
	
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public String getStart_city() {
		return start_city;
	}
	public void setStart_city(String start_city) {
		this.start_city = start_city;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public Date getBegin_exec_time() {
		return begin_exec_time;
	}
	public void setBegin_exec_time(Date begin_exec_time) {
		this.begin_exec_time = begin_exec_time;
	}
	public Date getDriverarrival_time() {
		return driverarrival_time;
	}
	public void setDriverarrival_time(Date driverarrival_time) {
		this.driverarrival_time = driverarrival_time;
	}
	public Date getBeginservice_time() {
		return beginservice_time;
	}
	public void setBeginservice_time(Date beginservice_time) {
		this.beginservice_time = beginservice_time;
	}
	public Date getEndservice_time() {
		return endservice_time;
	}
	public void setEndservice_time(Date endservice_time) {
		this.endservice_time = endservice_time;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
