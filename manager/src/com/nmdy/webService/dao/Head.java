package com.nmdy.webService.dao;

import java.util.Date;

public class Head {

	private String request_type;
	private String user;
	private String password;
	private Date request_time;
	private String request_transno;
	public String getRequest_type() {
		return request_type;
	}
	public void setRequest_type(String request_type) {
		this.request_type = request_type;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getRequest_time() {
		return request_time;
	}
	public void setRequest_time(Date request_time) {
		this.request_time = request_time;
	}
	public String getRequest_transno() {
		return request_transno;
	}
	public void setRequest_transno(String request_transno) {
		this.request_transno = request_transno;
	}
	
	

	
	
	
}
