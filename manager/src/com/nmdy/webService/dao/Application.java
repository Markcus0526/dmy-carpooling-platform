package com.nmdy.webService.dao;

import java.util.Date;

public class Application {

	private String appl_name;
	private String sex;
	private Date birthday;
	private String cert_type;
	private String cert_code;
	
	
	
	public String getAppl_name() {
		return appl_name;
	}
	public void setAppl_name(String appl_name) {
		this.appl_name = appl_name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getCert_type() {
		return cert_type;
	}
	public void setCert_type(String cert_type) {
		this.cert_type = cert_type;
	}
	public String getCert_code() {
		return cert_code;
	}
	public void setCert_code(String cert_code) {
		this.cert_code = cert_code;
	}
	
	

	
}
