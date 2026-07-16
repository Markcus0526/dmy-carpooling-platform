package com.pinche.spread.loan.dao;


public class Syscoupon {

	private int id;
	private String syscoupon_code = "";
	private String activeCode = "";
	private String password = "";
	private String sc_date = "";
	private String remark = "";
	private int release_channel = 0;
	private long app_spread_unit_id = 0;
	private int goods_or_cash = 0;
	private int sum = 0;
	private String goods = "";
	private int apply_source = 0;
	private int coupon_type = 0;
	private int limit_val = 0;
	private int valid_period_unit = 0;
	private int valid_period = 0;
	private int limit_count = 0;
	private int isenabled = 1;
	private int deleted = 0;
	
	private String appSpreadUnitName = "";
	private int deployCount = 0;
	private int useCount = 0;
	
	private String sumOrGoodsname = "";
	private String useCondition = "";
	private String validPeriodWithUnit = "";
	private String releaseChannelName = "";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getActiveCode() {
		return activeCode;
	}
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public int getLimit_val() {
		return limit_val;
	}
	public void setLimit_val(int limit_val) {
		this.limit_val = limit_val;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public String getAppSpreadUnitName() {
		return appSpreadUnitName;
	}
	public void setAppSpreadUnitName(String appSpreadUnitName) {
		this.appSpreadUnitName = appSpreadUnitName;
	}
	public int getDeployCount() {
		return deployCount;
	}
	public void setDeployCount(int deployCount) {
		this.deployCount = deployCount;
	}
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	public int getIsenabled() {
		return isenabled;
	}
	public void setIsenabled(int isenabled) {
		this.isenabled = isenabled;
	}
	public String getSumOrGoodsname() {
		return sumOrGoodsname;
	}
	public void setSumOrGoodsname(String sumOrGoodsname) {
		this.sumOrGoodsname = sumOrGoodsname;
	}
	public String getUseCondition() {
		return useCondition;
	}
	public void setUseCondition(String useCondition) {
		this.useCondition = useCondition;
	}
	public String getValidPeriodWithUnit() {
		return validPeriodWithUnit;
	}
	public void setValidPeriodWithUnit(String validPeriodWithUnit) {
		this.validPeriodWithUnit = validPeriodWithUnit;
	}
	public String getReleaseChannelName() {
		return releaseChannelName;
	}
	public void setReleaseChannelName(String releaseChannelName) {
		this.releaseChannelName = releaseChannelName;
	}
	public String getSyscoupon_code() {
		return syscoupon_code;
	}
	public void setSyscoupon_code(String syscoupon_code) {
		this.syscoupon_code = syscoupon_code;
	}
	public String getSc_date() {
		return sc_date;
	}
	public void setSc_date(String sc_date) {
		this.sc_date = sc_date;
	}
	public int getRelease_channel() {
		return release_channel;
	}
	public void setRelease_channel(int release_channel) {
		this.release_channel = release_channel;
	}

	public long getApp_spread_unit_id() {
		return app_spread_unit_id;
	}
	public void setApp_spread_unit_id(long app_spread_unit_id) {
		this.app_spread_unit_id = app_spread_unit_id;
	}
	public int getGoods_or_cash() {
		return goods_or_cash;
	}
	public void setGoods_or_cash(int goods_or_cash) {
		this.goods_or_cash = goods_or_cash;
	}
	public int getApply_source() {
		return apply_source;
	}
	public void setApply_source(int apply_source) {
		this.apply_source = apply_source;
	}
	public int getCoupon_type() {
		return coupon_type;
	}
	public void setCoupon_type(int coupon_type) {
		this.coupon_type = coupon_type;
	}
	public int getValid_period_unit() {
		return valid_period_unit;
	}
	public void setValid_period_unit(int valid_period_unit) {
		this.valid_period_unit = valid_period_unit;
	}
	public int getValid_period() {
		return valid_period;
	}
	public void setValid_period(int valid_period) {
		this.valid_period = valid_period;
	}
	public int getLimit_count() {
		return limit_count;
	}
	public void setLimit_count(int limit_count) {
		this.limit_count = limit_count;
	}
	
	
	
}
