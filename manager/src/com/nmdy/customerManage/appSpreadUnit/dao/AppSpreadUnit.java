package com.nmdy.customerManage.appSpreadUnit.dao;

import java.sql.Date;

public class AppSpreadUnit {
	private int	id;
	private String	unit_id;
	private String	name;
	private Date	create_date;
	private String	linkname;
	private String	linkphone;
	private String	group_property;
	private String	contract_no;
	private String	fix_phone;
	private String	email;
	private String	fax;
	private String	group_address;
	private String	invite_code;
	private long	balance_ts;
	private double	ratio_as_passenger_self;
	private double	integer_as_passenger_self;
	private int	active_as_passenger_self;
	private double	ratio_as_driver_self;
	private double	integer_as_driver_self;
	private int	active_as_driver_self;
	private int	limit_month_as_passenger_self;
	private int	limit_month_as_driver_self;
	private int	limit_count_as_passenger_self;
	private int	limit_count_as_driver_self;
	private String	goods;
	private int	deleted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
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

	public long getBalance_ts() {
		return balance_ts;
	}

	public void setBalance_ts(long balance_ts) {
		this.balance_ts = balance_ts;
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

	public void setFix_phoner(String fix_phone) {
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

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public double getRatio_as_passenger_self() {
		return ratio_as_passenger_self;
	}

	public void setRatio_as_passenger_self(double ratio_as_passenger_self) {
		this.ratio_as_passenger_self = ratio_as_passenger_self;
	}

	public double getInteger_as_passenger_self() {
		return integer_as_passenger_self;
	}

	public void setInteger_as_passenger_self(double integer_as_passenger_self) {
		this.integer_as_passenger_self = integer_as_passenger_self;
	}

	public int getActive_as_passenger_self() {
		return active_as_passenger_self;
	}

	public void setActive_as_passenger_self(int active_as_passenger_self) {
		this.active_as_passenger_self = active_as_passenger_self;
	}

	public double getRatio_as_driver_self() {
		return ratio_as_driver_self;
	}

	public void setRatio_as_driver_self(double ratio_as_driver_self) {
		this.ratio_as_driver_self = ratio_as_driver_self;
	}

	public double getInteger_as_driver_self() {
		return integer_as_driver_self;
	}

	public void setInteger_as_driver_self(double integer_as_driver_self) {
		this.integer_as_driver_self = integer_as_driver_self;
	}

	public int getActive_as_driver_self() {
		return active_as_driver_self;
	}

	public void setActive_as_driver_self(int active_as_driver_self) {
		this.active_as_driver_self = active_as_driver_self;
	}

	public int getLimit_month_as_passenger_self() {
		return limit_month_as_passenger_self;
	}

	public void setLimit_month_as_passenger_self(
			int limit_month_as_passenger_self) {
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

	public void setLimit_count_as_passenger_self(
			int limit_count_as_passenger_self) {
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
