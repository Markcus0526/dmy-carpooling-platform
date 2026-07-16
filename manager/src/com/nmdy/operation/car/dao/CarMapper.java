package com.nmdy.operation.car.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
public interface CarMapper extends SupperMapper{
	public List<carEntity> findcarbrand();//查询品牌
	public List<carEntity> findstylebybrand(String brand);//根据品牌差车型
	public List<carEntity> findcolordesc();//查询颜色
	public List<carEntity> outto(Map<String, Object> params);//导出
	public carEntity findtype(Map<String, Object> params);//查询车辆级别
	public int addColor(String color_desc);//添加颜色
	public int addBrand(String brand);//添加品牌
	public int addStyle(Map<String, Object> params);//添加车型
	public int deleteBrand(String brand);//删除品牌
	public int deleteColor(String color_desc);//删除颜色
	public int deleteStyle(Map<String, Object> params);//删除车型
	public int updatesave(Map<String, Object> params);//保存级别设置
	public int getTotal();//导出页面数据表格 里面的总数
	public int samebrand(Map<String,Object> params);//查找有没有用户注册该车品牌
	public int samestyle(Map<String,Object> params);//查找有没有用户注册该车型
	public int samecolor(Map<String,Object> params);//查找有没有用户的车是这个颜色
	public int insamebrand(Map<String,Object> params);//查找有没有品牌
	public int insamestyle(Map<String,Object> params);//查找有没有该车型
	public int insamecolor(Map<String,Object> params);//查找有没有这个颜色
}
