package com.nmdy.financial.manage.dao;

import java.math.BigInteger;

/**
 * @author liu jun hong
 *
 */
public class UsersEntity {
	private int userid;//个人客户id
	private String username;//个人客户姓名
	private String  phone;//个人客户电话
	private String  account_type;//用户类型 个人 集团  合作单位
	private Double  sum;// 审核金额
	private String  status;//状态 审核 驳回。。。
	private String  remark;//详细说明
	private BigInteger order_id;//订单编号
	private BigInteger req_user;//管理员
	private String req_cause;//操作原因
	private String reject_cause;//驳回理由
	private int formid;//表单号码
	private Double balance;//余额
	private int order_cs_id;
	private String aname;
	private String bname;
	
	
	//------------------------------get set-----------------------------
	
	
	
	public int getUserid() {
		return userid;
	}

	public String getAname() {
		return aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigInteger getOrder_id() {
		return order_id;
	}
	public void setOrder_id(BigInteger order_id) {
		this.order_id = order_id;
	}
	public BigInteger getReq_user() {
		return req_user;
	}
	public void setReq_user(BigInteger req_user) {
		this.req_user = req_user;
	}
	public String getReq_cause() {
		return req_cause;
	}
	public void setReq_cause(String req_cause) {
		this.req_cause = req_cause;
	}
	public String getReject_cause() {
		return reject_cause;
	}
	public void setReject_cause(String reject_cause) {
		this.reject_cause = reject_cause;
	}

	public int getFormid() {
		return formid;
	}
	public void setFormid(int formid) {
		this.formid = formid;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public int getOrder_cs_id() {
		return order_cs_id;
	}

	public void setOrder_cs_id(int order_cs_id) {
		this.order_cs_id = order_cs_id;
	}
	
	

}
