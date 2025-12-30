package com.nmdy.operation.cityparam.dao;

public class LowSpeedFeeRule {
	int id;
	String cityCode;
	String beginTime;
	String endTime;
	Double lowSpeedFee;
	Integer rushHour;

	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Double getLowSpeedFee() {
		return lowSpeedFee;
	}
	public void setLowSpeedFee(Double lowSpeedFee) {
		this.lowSpeedFee = lowSpeedFee;
	}
	public Integer getRushHour() {
		return rushHour;
	}
	public void setRushHour(Integer rushHour) {
		this.rushHour = rushHour;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
