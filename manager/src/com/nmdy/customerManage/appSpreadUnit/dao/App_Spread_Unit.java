package com.nmdy.customerManage.appSpreadUnit.dao;

import java.math.BigDecimal;
import java.util.Date;

public class App_Spread_Unit {
	
	  private long id;// bigint(20) NOT NULL AUTO_INCREMENT,
	  private String name;// varchar(256) NOT NULL DEFAULT '',
	  private Date create_date; //datetime DEFAULT NULL,
	  private String linkname;// varchar(50) DEFAULT '',
	  private String linkphone;// varchar(50) DEFAULT '',
	  private String group_property;// varchar(50) DEFAULT '',
	  private String contract_no;// varchar(100) DEFAULT '',
	  private String fix_phone;// varchar(50) DEFAULT '',
	  private String email;// varchar(50) DEFAULT '',
	  private String fax;// varchar(50) DEFAULT '',
	  private String group_address;// varchar(100) DEFAULT '',
	  private String invite_code;// varchar(50) DEFAULT '',
	  private long balance_ts;// bigint(20) DEFAULT '0',
	  private BigDecimal ratio_as_passenger_self;// decimal(8,2) DEFAULT '0.00',
	  private BigDecimal integer_as_passenger_self;// decimal(8,2) DEFAULT '0.00',
	  private int active_as_passenger_self;// int(11) DEFAULT '0',
	  private BigDecimal ratio_as_driver_self;// decimal(8,2) DEFAULT '0.00',
	  private BigDecimal integer_as_driver_self;// decimal(8,2) DEFAULT '0.00',
	  private int active_as_drivr_self;// int(11) DEFAULT '0',
	  private int limit_month_as_passenger_self;// int(11) DEFAULT '0',
	  private int limit_month_as_driver_self;// int(11) DEFAULT '0',
	  private int limit_count_as_passenger_self;// int(11) DEFAULT '0',
	  private int limit_count_as_driver_self;// int(11) DEFAULT '0',
	  private String goods;// varchar(500) DEFAULT '',
	  private int deleted;// tinyint(4) NOT NULL DEFAULT '0',
	  private int balance;//合作单位余额（由其他表查出）
	
	  
	  
	//-----getter/setter---------
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getInvite_code() {
		return invite_code;
	}
	public void setInvite_code(String invite_code) {
		this.invite_code = invite_code;
	}
	public long getBalance_ts() {
		return balance_ts;
	}
	public void setBalance_ts(long balance_ts) {
		this.balance_ts = balance_ts;
	}
	public BigDecimal getRatio_as_passenger_self() {
		return ratio_as_passenger_self;
	}
	public void setRatio_as_passenger_self(BigDecimal ratio_as_passenger_self) {
		this.ratio_as_passenger_self = ratio_as_passenger_self;
	}
	public BigDecimal getInteger_as_passenger_self() {
		return integer_as_passenger_self;
	}
	public void setInteger_as_passenger_self(BigDecimal integer_as_passenger_self) {
		this.integer_as_passenger_self = integer_as_passenger_self;
	}
	public int getActive_as_passenger_self() {
		return active_as_passenger_self;
	}
	public void setActive_as_passenger_self(int active_as_passenger_self) {
		this.active_as_passenger_self = active_as_passenger_self;
	}
	public BigDecimal getRatio_as_driver_self() {
		return ratio_as_driver_self;
	}
	public void setRatio_as_driver_self(BigDecimal ratio_as_driver_self) {
		this.ratio_as_driver_self = ratio_as_driver_self;
	}
	public BigDecimal getInteger_as_driver_self() {
		return integer_as_driver_self;
	}
	public void setInteger_as_driver_self(BigDecimal integer_as_driver_self) {
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
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}


	  
}
