package com.nmdy.order.appoint.action;

import java.util.Date;

public class OrderDetails {
	private Long id;
	private String order_num;
	private int order_type;
	private String city_from;
	private long driverId;
	private String driverCode;
	private String driverName;
	private String driverPhone;
	private double total_sum;
	private long passagerId;
	private String passagerCode;
	private String passagerName;
	private String passagerPhone;
	private double price;
	private String enableMoney;
	private int isCatchedCar;
	private Date beginservice_time;
	private int isPayed;
	private Date pay_time;
	private String start_addr;
	private String end_addr;
	private String addr4midpoint1;
	private String addr4midpoint2;
	private String addr4midpoint3;
	private String addr4midpoint4;
	private Date ps_date;
	private Date accept_time;
	private Date driverarrival_time;
	private Date begin_exec_time;
	private Date stopservice_time;
	private Date pre_time;
	private int status;
	private int reqcarstyle;
	private String psgerRemark;
	private String driverRemark;
	private String weekdays;	
	private int isBreaked;		// {1:true, 0:false} breaking Button "退还乘客1冻结的绿点" 
	private long orderExecId;
	private double temp_price;
	private String order_city;
	private boolean weekdays_1 = true;
	private boolean weekdays_2 = true;
	private boolean weekdays_3 = true;
	private boolean weekdays_4 = true;
	private boolean weekdays_5 = true;
	private boolean weekdays_6 = true;
	private boolean weekdays_7 = true;
	private String password;

	public double getTemp_price() {
		return temp_price;
	}
	public void setTemp_price(double temp_price) {
		this.temp_price = temp_price;
	}
	public void setOrderExecId(long orderExecId) {
		this.orderExecId = orderExecId;
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
	public String getDriverCode() {
		return driverCode;
	}
	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	
	public double getTotal_sum() {
		return total_sum;
	}
	public void setTotal_sum(double total_sum) {
		this.total_sum = total_sum;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getIsCatchedCar() {
		return isCatchedCar;
	}
	public void setIsCatchedCar(int isCatchedCar) {
		this.isCatchedCar = isCatchedCar;
	}
	public Date getBeginservice_time() {
		return beginservice_time;
	}
	public void setBeginservice_time(Date beginservice_time) {
		this.beginservice_time = beginservice_time;
	}
	public int getIsPayed() {
		return isPayed;
	}
	public void setIsPayed(int isPayed) {
		this.isPayed = isPayed;
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
	public String getAddr4midpoint1() {
		return addr4midpoint1;
	}
	public void setAddr4midpoint1(String addr4midpoint1) {
		this.addr4midpoint1 = addr4midpoint1;
	}
	public String getAddr4midpoint2() {
		return addr4midpoint2;
	}
	public void setAddr4midpoint2(String addr4midpoint2) {
		this.addr4midpoint2 = addr4midpoint2;
	}
	public String getAddr4midpoint3() {
		return addr4midpoint3;
	}
	public void setAddr4midpoint3(String addr4midpoint3) {
		this.addr4midpoint3 = addr4midpoint3;
	}
	public String getAddr4midpoint4() {
		return addr4midpoint4;
	}
	public void setAddr4midpoint4(String addr4midpoint4) {
		this.addr4midpoint4 = addr4midpoint4;
	}

	public Date getPay_time() {
		return pay_time;
	}
	public void setPay_time(Date pay_time) {
		this.pay_time = pay_time;
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
	public Date getStopservice_time() {
		return stopservice_time;
	}
	public void setStopservice_time(Date stopservice_time) {
		this.stopservice_time = stopservice_time;
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
	public String getPsgerRemark() {
		return psgerRemark;
	}
	public void setPsgerRemark(String psgerRemark) {
		this.psgerRemark = psgerRemark;
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
	public int getIsBreaked() {
		return isBreaked;
	}
	public void setIsBreaked(int isBreaked) {
		this.isBreaked = isBreaked;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderExecId() {
		return orderExecId;
	}
	public void setOrderExecId(Long orderExecId) {
		this.orderExecId = orderExecId;
	}
	public String getEnableMoney() {
		return enableMoney;
	}
	public void setEnableMoney(String enableMoney) {
		this.enableMoney = enableMoney;
	}
	
	public boolean isWeekdays_1() {
		return weekdays_1;
	}
	public void setWeekdays_1(boolean weekdays_1) {
		this.weekdays_1 = weekdays_1;
	}
	public boolean isWeekdays_2() {
		return weekdays_2;
	}
	public void setWeekdays_2(boolean weekdays_2) {
		this.weekdays_2 = weekdays_2;
	}
	public boolean isWeekdays_3() {
		return weekdays_3;
	}
	public void setWeekdays_3(boolean weekdays_3) {
		this.weekdays_3 = weekdays_3;
	}
	public boolean isWeekdays_4() {
		return weekdays_4;
	}
	public void setWeekdays_4(boolean weekdays_4) {
		this.weekdays_4 = weekdays_4;
	}
	public boolean isWeekdays_5() {
		return weekdays_5;
	}
	public void setWeekdays_5(boolean weekdays_5) {
		this.weekdays_5 = weekdays_5;
	}
	public boolean isWeekdays_6() {
		return weekdays_6;
	}
	public void setWeekdays_6(boolean weekdays_6) {
		this.weekdays_6 = weekdays_6;
	}
	public boolean isWeekdays_7() {
		return weekdays_7;
	}
	public void setWeekdays_7(boolean weekdays_7) {
		this.weekdays_7 = weekdays_7;
	}
	public String getPassagerCode() {
		return passagerCode;
	}
	public void setPassagerCode(String passagerCode) {
		this.passagerCode = passagerCode;
	}
	public String getPassagerName() {
		return passagerName;
	}
	public void setPassagerName(String passagerName) {
		this.passagerName = passagerName;
	}
	public String getPassagerPhone() {
		return passagerPhone;
	}
	public void setPassagerPhone(String passagerPhone) {
		this.passagerPhone = passagerPhone;
	}
	public String getOrder_city() {
		return order_city;
	}
	public void setOrder_city(String order_city) {
		this.order_city = order_city;
	}
	public long getDriverId() {
		return driverId;
	}
	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}
	public long getPassagerId() {
		return passagerId;
	}
	public void setPassagerId(long passagerId) {
		this.passagerId = passagerId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
