package com.nmdy.operation.evaluate.dao;
/**
 * 
 * @author home liu
 *保存每个用户的好评率及订单数的实体
 */
public class HelpEntity {
    private String ckpl1;//乘客身份好评率
	private String czpl1;//车主身份好评率
	private int czlevelt1;//
	private int cklevelt1;
	public String getCkpl1() {
		return ckpl1;
	}
	public void setCkpl1(String ckpl1) {
		this.ckpl1 = ckpl1;
	}
	public String getCzpl1() {
		return czpl1;
	}
	public void setCzpl1(String czpl1) {
		this.czpl1 = czpl1;
	}
	public int getCzlevelt1() {
		return czlevelt1;
	}
	public void setCzlevelt1(int czlevelt1) {
		this.czlevelt1 = czlevelt1;
	}
	public int getCklevelt1() {
		return cklevelt1;
	}
	public void setCklevelt1(int cklevelt1) {
		this.cklevelt1 = cklevelt1;
	}
	
	
}
