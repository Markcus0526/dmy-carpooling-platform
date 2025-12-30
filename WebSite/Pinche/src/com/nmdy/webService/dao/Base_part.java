package com.nmdy.webService.dao;

import java.math.BigDecimal;
import java.util.Date;

public class Base_part {
	
	private String appl_no;
	private Date appl_time;
	private String prod_type;
	private String prod_code;
	private Date effect_time;
	private Date insexpr_date;
	private String sale_channel;
	private String bus_branch;
	private String counter_code;
	private BigDecimal total_amount;
	private BigDecimal total_preminum;
	public String getAppl_no() {
		return appl_no;
	}
	public void setAppl_no(String appl_no) {
		this.appl_no = appl_no;
	}
	public Date getAppl_time() {
		return appl_time;
	}
	public void setAppl_time(Date appl_time) {
		this.appl_time = appl_time;
	}
	public String getProd_type() {
		return prod_type;
	}
	public void setProd_type(String prod_type) {
		this.prod_type = prod_type;
	}
	public String getProd_code() {
		return prod_code;
	}
	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}
	public Date getEffect_time() {
		return effect_time;
	}
	public void setEffect_time(Date effect_time) {
		this.effect_time = effect_time;
	}
	public Date getInsexpr_date() {
		return insexpr_date;
	}
	public void setInsexpr_date(Date insexpr_date) {
		this.insexpr_date = insexpr_date;
	}
	public String getSale_channel() {
		return sale_channel;
	}
	public void setSale_channel(String sale_channel) {
		this.sale_channel = sale_channel;
	}
	public String getBus_branch() {
		return bus_branch;
	}
	public void setBus_branch(String bus_branch) {
		this.bus_branch = bus_branch;
	}
	public String getCounter_code() {
		return counter_code;
	}
	public void setCounter_code(String counter_code) {
		this.counter_code = counter_code;
	}
	public BigDecimal getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(BigDecimal total_amount) {
		this.total_amount = total_amount;
	}
	public BigDecimal getTotal_preminum() {
		return total_preminum;
	}
	public void setTotal_preminum(BigDecimal total_preminum) {
		this.total_preminum = total_preminum;
	}
	
	
	

}
