package com.nmdy.operation.car.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.nmdy.operation.car.dao.carEntity;



public interface CarService {
	public List<carEntity> findcarbrand();
	public List<carEntity> findstylebybrand(String brand);
	public List<carEntity>  findcolordesc();
	public int addColor(String color_desc);
	public int addBrand(String brand);
	public int addStyle(Map<String, Object> params);
	public int deleteBrand(String brand);
	public int deleteColor(String color_desc);
	public int deleteStyle(Map<String, Object> params);
	public int updateSave(Map<String, Object> params);
	public List<carEntity> outto(Map<String, Object> params);
	public int getTotal();
	public carEntity findtype(Map<String, Object> params);//查询车辆级别
	public int samebrand(Map<String,Object> params);//查找有没有用户注册该车品牌
	public int samestyle(Map<String,Object> params);//查找有没有用户注册该车型
	public int samecolor(Map<String,Object> params);//查找有没有用户的车是这个颜色
	public int insamebrand(Map<String,Object> params);//查找有没有品牌
	public int insamestyle(Map<String,Object> params);//查找有没有该车型
	public int insamecolor(Map<String,Object> params);//查找有没有这个颜色
	//public int test();

}
