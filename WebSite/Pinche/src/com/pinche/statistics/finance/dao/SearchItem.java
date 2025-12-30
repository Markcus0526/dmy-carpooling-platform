package com.pinche.statistics.finance.dao;

import java.sql.Timestamp;

public class SearchItem {
	private String staticsCity;
	private Timestamp beginDate;
	private Timestamp endDate;
	private Timestamp beginTime;
	private Timestamp endTime;
	private int order_1;
	private int order_2;
	private int order_3;
	private int order_4;
	private String date_type;
	
	public String getStaticsCity() {
		return staticsCity;
	}
	
	public void setStaticsCity(String staticsCity) {
		this.staticsCity = staticsCity;
	}
	
	public Timestamp getBeginDate() {
		return beginDate;
	}
	
	public void setBeginDate(Timestamp beginDate) {
		this.beginDate = beginDate;
	}
	
	public Timestamp getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	
	public Timestamp getBeginTime() {
		return beginTime;
	}
	
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	
	public Timestamp getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public int getOrder_1() {
		return order_1;
	}

	public void setOrder_1(int order_1) {
		this.order_1 = order_1;
	}

	public int getOrder_2() {
		return order_2;
	}

	public void setOrder_2(int order_2) {
		this.order_2 = order_2;
	}

	public int getOrder_3() {
		return order_3;
	}

	public void setOrder_3(int order_3) {
		this.order_3 = order_3;
	}

	public int getOrder_4() {
		return order_4;
	}

	public void setOrder_4(int order_4) {
		this.order_4 = order_4;
	}

	public String getDate_type() {
		return date_type;
	}

	public void setDate_type(String date_type) {
		this.date_type = date_type;
	}

}
