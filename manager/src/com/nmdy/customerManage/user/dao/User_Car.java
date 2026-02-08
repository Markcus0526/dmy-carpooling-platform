package com.nmdy.customerManage.user.dao;

public class User_Car {

	private Long id;
	private Long userid;
	private String vin;
	private String brand;
	private String style;
	private String color;
	private String eno;
	private String car_img;
	private String plate_num;
	private String plate_num_last3;
	private int is_oper_vehicle;
	private int deleted;
	
	
	
	//------------getter/setter--------------------------
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getEno() {
		return eno;
	}
	public void setEno(String eno) {
		this.eno = eno;
	}
	public String getCar_img() {
		return car_img;
	}
	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}
	public String getPlate_num() {
		return plate_num;
	}
	public void setPlate_num(String plate_num) {
		this.plate_num = plate_num;
	}
	public String getPlate_num_last3() {
		return plate_num_last3;
	}
	public void setPlate_num_last3(String plate_num_last3) {
		this.plate_num_last3 = plate_num_last3;
	}
	public int getIs_oper_vehicle() {
		return is_oper_vehicle;
	}
	public void setIs_oper_vehicle(int is_oper_vehicle) {
		this.is_oper_vehicle = is_oper_vehicle;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	
	
}
