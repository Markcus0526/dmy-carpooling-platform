package com.nmdy.customerManage.user.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import util.excel.pojo.ExcelUser;

import com.nmdy.customerManage.user.dao.User;

public interface UserNewService {
	public User findUserBaseInfo(Serializable id);
	public Map<String,Object> findUserAllInfo(Serializable id);
	
	public int findCountByCondition(Map<String, Object> params);
	public User findByID(Serializable id);
	public List<Map<String, Object>> findUsersByCondition(Map<String, Object> params);
	
	public int updateUserBaseInfo(Map<String, Object> params);
	public int updatePersonVerifiedInfo(Map<String, Object> params);
	public int updateDriverVerifiedInfo(Map<String, Object> params);
	public int updateDriverVerifiedInfo_UserCar(Map<String, Object> params);
	public int updateRebateInfo(Map<String, Object> params);
	public Map<String, Object> findPersonVerifiedInfo(Serializable id);
	public int personVerifiedRejected(long id);
	public int personVerified(Map<String, Object> params);
	public int driverVerifiedRejected(long id);
	public int driverVerified(Map<String, Object> params);
	public Map<String, Object> findDriverVerifiedInfo(long id);
	public List<Map<String, Object>> findAllRebateListInfo(Map<String, Object> params);
	public List<Map<String, Object>> findRebateListInfo(long parseLong);
	public User findOneById(int driver);
	public int updateTsid(Map<String, Object> map);
	public List<User> searchNumberspread(Map<String, Object> map);
	public List<Map<String, Object>> findCarBrand();
	public List<Map<String, Object>> findCarStyle(Map<String, Object> params);
	
	/**
	 * 根据车辆品牌和型号获得car_type表中的记录ID
	 * @param carMap
	 * @return
	 */
	public long findCarTypeID(Map<String, Object> carMap);
	public List<ExcelUser> findUsersByConditionExcel(Map<String,Object> params);
	


}
