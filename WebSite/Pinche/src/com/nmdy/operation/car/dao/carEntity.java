package com.nmdy.operation.car.dao;

public class carEntity {
private String brand;//车牌
private String car_style;//车型
private int type;//级别
private String color_desc;//颜色描述
private int id;

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getColor_desc() {
	return color_desc;
}
public void setColor_desc(String color_desc) {
	this.color_desc = color_desc;
}
public String getBrand() {
	return brand;
}
public void setBrand(String brand) {
	this.brand = brand;
}
public String getCar_style() {
	return car_style;
}
public void setCar_style(String car_style) {
	this.car_style = car_style;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}

}
