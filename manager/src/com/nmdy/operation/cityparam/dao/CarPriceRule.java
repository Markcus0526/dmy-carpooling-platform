package com.nmdy.operation.cityparam.dao;

public class CarPriceRule {
	int id;
	String cityCode;
	String beginTime;
	String endTime;
	Double cheapTaxiRatio;
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
	public Double getCheapTaxiRatio() {
		return cheapTaxiRatio;
	}
	public void setCheapTaxiRatio(Double cheapTaxiRatio) {
		this.cheapTaxiRatio = cheapTaxiRatio;
	}

}
