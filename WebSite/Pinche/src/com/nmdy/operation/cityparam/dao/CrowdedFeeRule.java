package com.nmdy.operation.cityparam.dao;

public class CrowdedFeeRule {
	int id;
	String cityCode;
	String beginTime;
	String endTime;
	double crowdedFee;
	double addPricedefault;
	double addPriceRange;
	int rushHour;
	String prompt1st;
	String prompt;
	double addPriceMin;
	double cutPriceRange;
	
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
	public double getCrowdedFee() {
		return crowdedFee;
	}
	public void setCrowdedFee(double crowdedFee) {
		this.crowdedFee = crowdedFee;
	}
	public double getAddPricedefault() {
		return addPricedefault;
	}
	public void setAddPricedefault(double addPricedefault) {
		this.addPricedefault = addPricedefault;
	}
	public double getAddPriceRange() {
		return addPriceRange;
	}
	public void setAddPriceRange(double addPriceRange) {
		this.addPriceRange = addPriceRange;
	}
	public int getRushHour() {
		return rushHour;
	}
	public void setRushHour(int rushHour) {
		this.rushHour = rushHour;
	}
	public String getPrompt1st() {
		return prompt1st;
	}
	public void setPrompt1st(String prompt1st) {
		this.prompt1st = prompt1st;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public double getAddPriceMin() {
		return addPriceMin;
	}
	public void setAddPriceMin(double addPriceMin) {
		this.addPriceMin = addPriceMin;
	}
	public double getCutPriceRange() {
		return cutPriceRange;
	}
	public void setCutPriceRange(double cutPriceRange) {
		this.cutPriceRange = cutPriceRange;
	}
}
