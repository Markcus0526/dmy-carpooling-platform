package com.nmdy.financial.manage.dao;

public class OrderEntity {
	private String pname;//乘客名
	private String pphone;//乘客手机号
	private String dname;//车主名
	private String dphone;//车主手机号
	private String ordertype;//订单类型
	private int orderstatus;//订单状态
	private String ordernum;//用于订单选择的页面的订单编号
	private String cr_date;
	private String ti_accept_order;
	private String begin_exec_time;
	private String driverarrival_time;
	private String beginservice_time;
	private String stopservice_time;
	private String pay_time;
	private String pass_cancel_time;
	private String driver_cancel_time;
	private int oid;
	private int passenger;
	private int driver;
	private int ordercsid;
	//----------------get set-------------
	
	
	
	public String getPname() {
		return pname;
	}
	public int getOrdercsid() {
		return ordercsid;
	}
	public void setOrdercsid(int ordercsid) {
		this.ordercsid = ordercsid;
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
	public String getCr_date() {
		return cr_date;
	}
	public void setCr_date(String cr_date) {
		this.cr_date = cr_date;
	}
	public String getTi_accept_order() {
		return ti_accept_order;
	}
	public void setTi_accept_order(String ti_accept_order) {
		this.ti_accept_order = ti_accept_order;
	}
	public String getBegin_exec_time() {
		return begin_exec_time;
	}
	public void setBegin_exec_time(String begin_exec_time) {
		this.begin_exec_time = begin_exec_time;
	}
	public String getDriverarrival_time() {
		return driverarrival_time;
	}
	public void setDriverarrival_time(String driverarrival_time) {
		this.driverarrival_time = driverarrival_time;
	}
	public String getBeginservice_time() {
		return beginservice_time;
	}
	public void setBeginservice_time(String beginservice_time) {
		this.beginservice_time = beginservice_time;
	}
	public String getStopservice_time() {
		return stopservice_time;
	}
	public void setStopservice_time(String stopservice_time) {
		this.stopservice_time = stopservice_time;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}
	public String getPass_cancel_time() {
		return pass_cancel_time;
	}
	public void setPass_cancel_time(String pass_cancel_time) {
		this.pass_cancel_time = pass_cancel_time;
	}
	public String getDriver_cancel_time() {
		return driver_cancel_time;
	}
	public void setDriver_cancel_time(String driver_cancel_time) {
		this.driver_cancel_time = driver_cancel_time;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPphone() {
		return pphone;
	}
	public void setPphone(String pphone) {
		this.pphone = pphone;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getDphone() {
		return dphone;
	}
	public void setDphone(String dphone) {
		this.dphone = dphone;
	}
	public String getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}
	
	public int getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(int orderstatus) {
		this.orderstatus = orderstatus;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}

	
	
	
}
