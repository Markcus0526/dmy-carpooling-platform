package com.nmdy.operation.cityparam.dao;

public class AllowanceRule {
	int id;
	String cityCode;
	String beginTime;
	String endTime;
	int driverLevel;
	double allowance;
	short type;
	double minMileage;
	double maxAllowance;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getDriverLevel() {
		return driverLevel;
	}
	public void setDriverLevel(int driverLevel) {
		this.driverLevel = driverLevel;
	}
	public double getAllowance() {
		return allowance;
	}
	public void setAllowance(double allowance) {
		this.allowance = allowance;
	}
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}
	public double getMinMileage() {
		return minMileage;
	}
	public void setMinMileage(double minMileage) {
		this.minMileage = minMileage;
	}
	public double getMaxAllowance() {
		return maxAllowance;
	}
	public void setMaxAllowance(double maxAllowance) {
		this.maxAllowance = maxAllowance;
	}
	
}
