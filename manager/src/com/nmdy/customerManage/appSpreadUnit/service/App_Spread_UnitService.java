package com.nmdy.customerManage.appSpreadUnit.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.nmdy.customerManage.appSpreadUnit.dao.AppSpreadUnit;



public interface App_Spread_UnitService {

	public List<Map<String, Object>> findApp_Spread_UnitsByCondition(Map<String, Object> params);

	public  int findApp_Spread_UnitCountByCondition(Map<String, Object> params);

	public Map<String, Object> findApp_Spread_UnitInfo(long id);

	public void updateApp_Spread_UnitInfo(Map<String, Object> params);

	public void saveApp_Spread_Unit(Map<String, Object> params);

	public BigDecimal findBalnace(long parseLong);

	public void delApp_Spread_Unit(long id);

	/**
	 * 获得合作单位发展的所有用户的返利信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findASUAllRebateListInfo(Map<String, Object> params);

	
	/**
	 * 获得指定合作单位发展的用户的返利信息列表
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> findASURebateListInfo(long id);
	public int modifytsbal(Map<String, Object> params3);//通过后余额的更新
	public int seUnittsid(Map<String, Object> params);//注册后返回注册时tsid

	public List<AppSpreadUnit> findAll();
/*	public List<App_Spread_Unit> findAll();
	public App_Spread_Unit findById(int id);
	public int updateTsid(Map<String, Object> map);*/
}
