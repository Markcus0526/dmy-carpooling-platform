package com.pinche.statistics.passenger.dao;

import java.sql.Timestamp;

public class SearchItem {
	private String staticsCity;
	private Timestamp beginDate;
	private Timestamp endDate;
	private Timestamp beginTime;
	private Timestamp endTime;
	private int platform_home;
	private int platform_common;
	private String app_download;
	private String date_type;
	
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
	
	public String getApp_download() {
		return app_download;
	}
	
	public void setApp_download(String app_download) {
		this.app_download = app_download;
	}
	
	public String getStaticsCity() {
		return staticsCity;
	}
	
	public void setStaticsCity(String staticsCity) {
		this.staticsCity = staticsCity;
	}
	
	public int getPlatform_home() {
		return platform_home;
	}
	
	public void setPlatform_home(int platform_home) {
		this.platform_home = platform_home;
	}
	
	public int getPlatform_common() {
		return platform_common;
	}
	
	public void setPlatform_common(int platform_common) {
		this.platform_common = platform_common;
	}

	public String getDate_type() {
		return date_type;
	}

	public void setDate_type(String date_type) {
		this.date_type = date_type;
	}
}
