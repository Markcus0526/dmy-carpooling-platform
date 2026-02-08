package util;

import java.util.Date;
   /**
    * ts 实体类
    * Tue Nov 11 16:19:43 CST 2014 bisu
    */ 


public class Ts{
	private Long id;
	private Long orderCsId;
	private Long orderId;
	private Integer orderType;
	private Integer oper;
	private Integer tsWay;
	private Double balance;
	private Date tsDate;
	private Long userid;
	private Long groupid;
	private Long unitid;
	private String remark;
	private String account;
	private Integer accountType;
	private String application;
	private String tsType;
	private Double sum;
	private Integer deleted;
	public Long getId(){
		return id;
	}
	public void setId(Long id){
	this.id=id;
	}
	public Long getOrderCsId(){
		return orderCsId;
	}
	public void setOrderCsId(Long orderCsId){
	this.orderCsId=orderCsId;
	}
	public Long getOrderId(){
		return orderId;
	}
	public void setOrderId(Long orderId){
	this.orderId=orderId;
	}
	public Integer getOrderType(){
		return orderType;
	}
	public void setOrderType(Integer orderType){
	this.orderType=orderType;
	}
	public Integer getOper(){
		return oper;
	}
	public void setOper(Integer oper){
	this.oper=oper;
	}
	public Integer getTsWay(){
		return tsWay;
	}
	public void setTsWay(Integer tsWay){
	this.tsWay=tsWay;
	}
	public Double getBalance(){
		return balance;
	}
	public void setBalance(Double balance){
	this.balance=balance;
	}
	public Date getTsDate(){
		return tsDate;
	}
	public void setTsDate(Date tsDate){
	this.tsDate=tsDate;
	}
	public Long getUserid(){
		return userid;
	}
	public void setUserid(Long userid){
	this.userid=userid;
	}
	public Long getGroupid(){
		return groupid;
	}
	public void setGroupid(Long groupid){
	this.groupid=groupid;
	}
	public Long getUnitid(){
		return unitid;
	}
	public void setUnitid(Long unitid){
	this.unitid=unitid;
	}
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
	this.remark=remark;
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
	public String getApplication(){
		return application;
	}
	public void setApplication(String application){
	this.application=application;
	}
	public String getTsType(){
		return tsType;
	}
	public void setTsType(String tsType){
	this.tsType=tsType;
	}
	public Double getSum(){
		return sum;
	}
	public void setSum(Double sum){
	this.sum=sum;
	}
	public Integer getDeleted(){
		return deleted;
	}
	public void setDeleted(Integer deleted){
	this.deleted=deleted;
	}
}

