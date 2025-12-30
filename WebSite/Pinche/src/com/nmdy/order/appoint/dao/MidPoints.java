package com.nmdy.order.appoint.dao;

public class MidPoints {
	private Long id;
	private int order_type;
	private Long orderid;
	private int index;
	private Double lat;
	private Double lng;
	private String addr;
	private int deleted;
	
	public int getOrder_type() {
		return order_type;
	}
	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderid() {
		return orderid;
	}
	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}
	
}
