package com.nmdy.customerManage.groupAssociation.dao;

import java.util.Date;

public class GroupAssociation {
	
	  private long id;// bigint(20) NOT NULL AUTO_INCREMENT,
	  private String ga_code;// varchar(50) NOT NULL DEFAULT '',
	  private String ga_name;// varchar(256) NOT NULL DEFAULT '',
	  private String linkname;// varchar(50) DEFAULT '',
	  private String linkphone;// varchar(50) DEFAULT '',
	  private String group_property;// varchar(50) DEFAULT '',
	  private String contract_no;// varchar(100) DEFAULT '',
	  private String fix_phone;// varchar(50) DEFAULT '',
	  private String email;// varchar(50) DEFAULT '',
	  private String fax;// varchar(50) DEFAULT '',
	  private String group_address;// varchar(100) DEFAULT '',
	  private Date sign_time;// datetime DEFAULT NULL,
	  private String desc;// varchar(50) DEFAULT '',
	  private int deleted;// tinyint(4) NOT NULL DEFAULT '0',
	
	  
	  
	//-----getter/setter---------
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGa_code() {
		return ga_code;
	}
	public void setGa_code(String ga_code) {
		this.ga_code = ga_code;
	}
	public String getGa_name() {
		return ga_name;
	}
	public void setGa_name(String ga_name) {
		this.ga_name = ga_name;
	}
	public String getLinkname() {
		return linkname;
	}
	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}
	public String getLinkphone() {
		return linkphone;
	}
	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}
	public String getGroup_property() {
		return group_property;
	}
	public void setGroup_property(String group_property) {
		this.group_property = group_property;
	}
	public String getContract_no() {
		return contract_no;
	}
	public void setContract_no(String contract_no) {
		this.contract_no = contract_no;
	}
	public String getFix_phone() {
		return fix_phone;
	}
	public void setFix_phone(String fix_phone) {
		this.fix_phone = fix_phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getGroup_address() {
		return group_address;
	}
	public void setGroup_address(String group_address) {
		this.group_address = group_address;
	}
	public Date getSign_time() {
		return sign_time;
	}
	public void setSign_time(Date sign_time) {
		this.sign_time = sign_time;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

}
