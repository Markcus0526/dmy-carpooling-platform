package com.pinche.spread.loan.service;

import java.util.List;
import java.util.Map;

import com.pinche.customer.user.dao.UserBasicInfo;
import com.pinche.spread.loan.dao.Activities;
import com.pinche.spread.loan.dao.City;
import com.pinche.spread.loan.dao.CouponSendLog;
import com.pinche.spread.loan.dao.GlobalParams;
import com.pinche.spread.loan.dao.SingleCoupon;
import com.pinche.spread.loan.dao.Syscoupon;
import com.pinche.spread.loan.dao.Syscoupon1;

public interface LoanService {
	
	public Syscoupon findOneSyscouponById(long syscouponId);
	public List<Syscoupon> search(String releaseChannel, String isNoConditionCash, String isConditionCash, String isGood);
	public List<Map<String, Object>> findPagination(Map<String, Object> map);
	
	public int getSys_couponCountbyCondition(Map<String, Object> params);
	
	public List<Syscoupon> search4autoSend(String couponCode, Integer price,
			String validPeriod);
	public List<Syscoupon> findPagination4autoSend(Map<String, Object> map);
	public String genNewCouponCode();
	public void insert(Syscoupon newCoupon);
	public void updateSyscoupon(Syscoupon disableSyscoupon);
	
	public void insertCouponSendLog(CouponSendLog log);
	public List<CouponSendLog> searchCouponSendLogs(String syscouponCode);
	public List<CouponSendLog> searchCouponSendLogs(Map<String, Object> map);
		
	public void insertSingleCoupon(SingleCoupon singleCoupon);
	public String genNewSingleCouponCode();
	
	public GlobalParams getGlobalParam();
	public void updateGlobalParam(Map<String, Object> params);
	
	public City findOneCityByName(String name);
	public void updateCity(Map<String, Object> params);
	
	public List<Activities> search4activity(String activityCode, int price);
	public List<Activities> findPagination4activity(Map<String, Object> map);
	public void insertActivity(Activities activity);
	public Activities findOneActivityByCode(String activityCode);
	public void updateActivity(Activities activity);
	/**
	 * 获得符合条件的用户列表
	 * @param searchParam
	 * @return
	 */
	public List<Map<String, Object>> getUserBasicInfoList(Map<String, Object> searchParam);
	/**
	 * 获得符合查询条件的用户数量
	 * @param params
	 * @return
	 */
	public int getUserCountByCondition(Map<String, Object> params);
	public Syscoupon1 findSyscouponById(long id);
	public int insertNotifyPersion(Map<String, Object> mm);
	public int insertCouponSendLog(Map<String, Object> mm);
	public City findCityParam(String name);
	public City findglobal();
	
	public List<Syscoupon> findByConditionForGame(Map<String, Object> map);
}
