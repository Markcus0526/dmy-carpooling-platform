package com.pinche.customer.group.dao;

import java.sql.Date;

public class Group {

	private Integer id;
	private Integer groupid;
	private String group_name;
	private Date create_date;
	private String linkname;
	private String linkphone;
	private String group_property;
	private String contract_no;
	private String fix_phone;
	private String email;
	private String fax;
	private String group_address;
	private Date sign_time;
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
	private boolean checked;
	private int fid;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
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
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public int getFid() {
		return 0;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	
}
