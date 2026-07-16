package com.nmdy.customerManage.group.dao;

import java.sql.Date;
import java.sql.Timestamp;

import org.apache.log4j.helpers.DateTimeDateFormat;
import org.springframework.format.annotation.DateTimeFormat;

public class Group {

	private Integer id;
	private String groupid;
	private String group_name;
	private String create_date;
	private String linkname;
	private String linkphone;
	private String group_property;
	private String contract_no;
	private String fix_phone;
	private String email;
	private String fax;
	private String group_address;
	private String sign_time;
	private String invitecode_self;
	private int balance_ts;
	private String account;
	private int ratio_as_passenger_self;
	private int integer_as_passenger_self;
	private int active_as_passenger_self;
	private int ratio_as_driver_self;
	private int integer_as_driver_self;
	private int active_as_drivr_self;
	private int limit_month_as_passenger_self;
	private int limit_month_as_driver_self;
	private int limit_count_as_passenger_self;
	private int limit_count_as_driver_self;
	private int deleted;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
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

	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getSign_time() {
		return sign_time;
	}
	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}
	public String getInvitecode_self() {
		return invitecode_self;
	}
	public void setInvitecode_self(String invitecode_self) {
		this.invitecode_self = invitecode_self;
	}
	public int getBalance_ts() {
		return balance_ts;
	}
	public void setBalance_ts(int balance_ts) {
		this.balance_ts = balance_ts;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getRatio_as_passenger_self() {
		return ratio_as_passenger_self;
	}
	public void setRatio_as_passenger_self(int ratio_as_passenger_self) {
		this.ratio_as_passenger_self = ratio_as_passenger_self;
	}
	public int getInteger_as_passenger_self() {
		return integer_as_passenger_self;
	}
	public void setInteger_as_passenger_self(int integer_as_passenger_self) {
		this.integer_as_passenger_self = integer_as_passenger_self;
	}
	public int getActive_as_passenger_self() {
		return active_as_passenger_self;
	}
	public void setActive_as_passenger_self(int active_as_passenger_self) {
		this.active_as_passenger_self = active_as_passenger_self;
	}
	public int getRatio_as_driver_self() {
		return ratio_as_driver_self;
	}
	public void setRatio_as_driver_self(int ratio_as_driver_self) {
		this.ratio_as_driver_self = ratio_as_driver_self;
	}
	public int getInteger_as_driver_self() {
		return integer_as_driver_self;
	}
	public void setInteger_as_driver_self(int integer_as_driver_self) {
		this.integer_as_driver_self = integer_as_driver_self;
	}
	public int getActive_as_drivr_self() {
		return active_as_drivr_self;
	}
	public void setActive_as_drivr_self(int active_as_drivr_self) {
		this.active_as_drivr_self = active_as_drivr_self;
	}
	public int getLimit_month_as_passenger_self() {
		return limit_month_as_passenger_self;
	}
	public void setLimit_month_as_passenger_self(int limit_month_as_passenger_self) {
		this.limit_month_as_passenger_self = limit_month_as_passenger_self;
	}
	public int getLimit_month_as_driver_self() {
		return limit_month_as_driver_self;
	}
	public void setLimit_month_as_driver_self(int limit_month_as_driver_self) {
		this.limit_month_as_driver_self = limit_month_as_driver_self;
	}
	public int getLimit_count_as_passenger_self() {
		return limit_count_as_passenger_self;
	}
	public void setLimit_count_as_passenger_self(int limit_count_as_passenger_self) {
		this.limit_count_as_passenger_self = limit_count_as_passenger_self;
	}
	public int getLimit_count_as_driver_self() {
		return limit_count_as_driver_self;
	}
	public void setLimit_count_as_driver_self(int limit_count_as_driver_self) {
		this.limit_count_as_driver_self = limit_count_as_driver_self;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	
}
