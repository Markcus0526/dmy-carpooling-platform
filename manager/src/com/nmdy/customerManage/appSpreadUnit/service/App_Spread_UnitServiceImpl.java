package com.nmdy.customerManage.appSpreadUnit.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.nmdy.customerManage.appSpreadUnit.dao.AppSpreadUnit;
import com.nmdy.customerManage.appSpreadUnit.dao.App_Spread_UnitMapper;



/*@Service
@Transactional*/
public class App_Spread_UnitServiceImpl implements App_Spread_UnitService {

	//@Resource
	private App_Spread_UnitMapper app_Spread_UnitMapper;
	
	
	
	public App_Spread_UnitMapper getApp_Spread_UnitMapper() {
		return app_Spread_UnitMapper;
	}

	public void setApp_Spread_UnitMapper(App_Spread_UnitMapper app_Spread_UnitMapper) {
		this.app_Spread_UnitMapper = app_Spread_UnitMapper;
	}

	public List<Map<String, Object>> findApp_Spread_UnitsByCondition(Map<String, Object> params) {
		return app_Spread_UnitMapper.findApp_Spread_UnitsByCondition(params);
	}

	public int findApp_Spread_UnitCountByCondition(Map<String, Object> params) {
		return app_Spread_UnitMapper.getApp_Spread_UnitCountByCondition(params);
	}

	public Map<String, Object> findApp_Spread_UnitInfo(long id) {
		return app_Spread_UnitMapper.getApp_Spread_UnitInfo(id);
	}

	public void updateApp_Spread_UnitInfo(Map<String, Object> params) {
		app_Spread_UnitMapper.updateApp_Spread_UnitInfo(params);
		
	}

	public void saveApp_Spread_Unit(Map<String, Object> params) {
		app_Spread_UnitMapper.saveApp_Spread_Unit(params);
	}

	public BigDecimal findBalnace(long id) {
		
		return app_Spread_UnitMapper.getBalnace(id);
	}

	public void delApp_Spread_Unit(long id) {
		app_Spread_UnitMapper.delApp_Spread_Unit(id);
	}

	/**
	 * 获得合作单位发展的所有用户的返利信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findASUAllRebateListInfo(Map<String, Object> params) {
		return app_Spread_UnitMapper.getASUAllRebateListInfo(params);
	}

	/**
	 * 获得指定合作单位发展的用户的返利信息列表
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> findASURebateListInfo(long id) {
		return app_Spread_UnitMapper.getASURebateListInfo(id);
	}

	public List<AppSpreadUnit> findAll() {
		return app_Spread_UnitMapper.findAll();
	}
/*	public List<App_Spread_Unit> findAll() {
		return app_Spread_UnitMapper.findAll();
	}

	public App_Spread_Unit findById(int id) {
		return app_Spread_UnitMapper.findById(id);
	}

	public int updateTsid(Map<String, Object> map) {
		return app_Spread_UnitMapper.updateTsid(map);
	}*/

	@Override
	public int modifytsbal(Map<String, Object> params3) {
		// TODO Auto-generated method stub
		return app_Spread_UnitMapper.modifytsbal(params3);
	}

	@Override
	public int seUnittsid(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return app_Spread_UnitMapper.seUnittsid(params);
	}

}
