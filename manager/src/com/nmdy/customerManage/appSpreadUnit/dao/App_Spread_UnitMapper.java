package com.nmdy.customerManage.appSpreadUnit.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.transaction.Transaction;

import com.nmdy.base.SupperMapper;
import com.nmdy.financial.account.dao.TransactionDetail;


public interface App_Spread_UnitMapper extends SupperMapper {

	public List<Map<String, Object>> findApp_Spread_UnitsByCondition(Map<String, Object> params);

	public int getApp_Spread_UnitCountByCondition(Map<String, Object> params);

	public Map<String, Object> getApp_Spread_UnitInfo(long id);

	public void updateApp_Spread_UnitInfo(Map<String, Object> params);

	public void saveApp_Spread_Unit(Map<String, Object> params);

	public BigDecimal getBalnace(long id);

	public void delApp_Spread_Unit(long id);

	/**
	 * 获得合作单位发展的所有用户的返利信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getASUAllRebateListInfo(Map<String, Object> params);

	/**
	 * 获得指定合作单位发展的用户的返利信息列表
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getASURebateListInfo(long id);
	
	
/*	public List<App_Spread_Unit> findAll();
	public App_Spread_Unit findById(int id);
	public int updateTsid(Map<String, Object> map);*/
	public List<AppSpreadUnit> findAll();

	public TransactionDetail findByMaxUserId(int userId);
	
	public int insertTransactionDetail(TransactionDetail t);
	public int modifytsbal(Map<String, Object> params3);//通过后余额的更新
	public int seUnittsid(Map<String, Object> params);//注册后返回注册时tsid
}
