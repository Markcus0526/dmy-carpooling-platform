package com.nmdy.entity;

import java.util.Date;
   /**
    * req_client_account 实体类
    * Tue Nov 11 16:33:25 CST 2014 bisu
    */ 


public class ClientAccount{
	private Long id;
	private String reqNum;
	private String account;
	private Integer accountType;
	private Long userId;
	private Double sum;
	private Integer oper;
	private Date reqDate;
	private Date auditDate;
	private Integer status;
	private Integer operType;
	private String channel;
	private String reqCause;
	private Long orderCsId;
	private String rejectCause;
	private Long tsId;
	private Long reqUser;
	private Long auditor;
	private Integer reqSource;
	private String remark;
	private Integer deleted;
	private String username;
	
	public Long getId(){
		return id;
	}
	public void setId(Long id){
	this.id=id;
	}
	public String getReqNum(){
		return reqNum;
	}
	public void setReqNum(String reqNum){
	this.reqNum=reqNum;
	}
	public String getAccount(){
		return account;
	}
	public void setAccount(String account){
	this.account=account;
	}
	public Integer getAccountType(){
		return accountType;
	}
	public void setAccountType(Integer accountType){
	this.accountType=accountType;
	}
	public Long getUserId(){
		return userId;
	}
	public void setUserId(Long userId){
	this.userId=userId;
	}
	public Double getSum(){
		return sum;
	}
	public void setSum(Double sum){
	this.sum=sum;
	}
	public Integer getOper(){
		return oper;
	}
	public void setOper(Integer oper){
	this.oper=oper;
	}
	public Date getReqDate(){
		return reqDate;
	}
	public void setReqDate(Date reqDate){
	this.reqDate=reqDate;
	}
	public Date getAuditDate(){
		return auditDate;
	}
	public void setAuditDate(Date auditDate){
	this.auditDate=auditDate;
	}
	public Integer getStatus(){
		return status;
	}
	public void setStatus(Integer status){
	this.status=status;
	}
	public Integer getOperType(){
		return operType;
	}
	public void setOperType(Integer operType){
	this.operType=operType;
	}
	public String getChannel(){
		return channel;
	}
	public void setChannel(String channel){
	this.channel=channel;
	}
	public String getReqCause(){
		return reqCause;
	}
	public void setReqCause(String reqCause){
	this.reqCause=reqCause;
	}
	public Long getOrderCsId(){
		return orderCsId;
	}
	public void setOrderCsId(Long orderCsId){
	this.orderCsId=orderCsId;
	}
	public String getRejectCause(){
		return rejectCause;
	}
	public void setRejectCause(String rejectCause){
	this.rejectCause=rejectCause;
	}
	public Long getTsId(){
		return tsId;
	}
	public void setTsId(Long tsId){
	this.tsId=tsId;
	}
	public Long getReqUser(){
		return reqUser;
	}
	public void setReqUser(Long reqUser){
	this.reqUser=reqUser;
	}
	public Long getAuditor(){
		return auditor;
	}
	public void setAuditor(Long auditor){
	this.auditor=auditor;
	}
	public Integer getReqSource(){
		return reqSource;
	}
	public void setReqSource(Integer reqSource){
	this.reqSource=reqSource;
	}
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
	this.remark=remark;
	}
	public Integer getDeleted(){
		return deleted;
	}
	public void setDeleted(Integer deleted){
	this.deleted=deleted;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}

