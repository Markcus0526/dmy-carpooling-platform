package com.nmdy.webService.dao;

import java.util.Date;
import java.util.List;

public class Insured_data {
	
	private int isd_serial;
	private String isd_type;
	private String appl_relation;
	private String isd_name;
	private String sex;
	private Date birthday;
	private String cert_type;
	private String cert_code;
	
	private  List<Benefit_data> benefit_List ;

	public int getIsd_serial() {
		return isd_serial;
	}

	public void setIsd_serial(int isd_serial) {
		this.isd_serial = isd_serial;
	}

	public String getIsd_type() {
		return isd_type;
	}

	public void setIsd_type(String isd_type) {
		this.isd_type = isd_type;
	}

	public String getAppl_relation() {
		return appl_relation;
	}

	public void setAppl_relation(String appl_relation) {
		this.appl_relation = appl_relation;
	}
	
	
	

	public String getIsd_name() {
		return isd_name;
	}

	public void setIsd_name(String isd_name) {
		this.isd_name = isd_name;
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

	public List<Benefit_data> getBenefit_List() {
		return benefit_List;
	}

	public void setBenefit_List(List<Benefit_data> benefit_List) {
		this.benefit_List = benefit_List;
	}



	


	
	
	
}
