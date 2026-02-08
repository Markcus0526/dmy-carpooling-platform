package com.pinche.spread.loan.dao;

import java.util.List;
import java.util.Map;


public interface SyscouponMapper {

	public List<Syscoupon> search(String releaseChannel, String isNoConditionCash, String isConditionCash, String isGood);
	public List<Map<String, Object>> findPagination(Map<String, Object> map);
	public int getSys_couponCountbyCondition(Map<String, Object> params);
	public List<Syscoupon> findByConditionForGame(Map<String, Object> map);
	public int getMaxId();
	public void insert(Syscoupon newCoupon);
	public Syscoupon findOneById(long syscouponId);
	public void update(Syscoupon syscoupon);
	public List<Syscoupon> search4autoSend(Map<String, Object> map);
	/**
	 * 获得用户信息列表
	 * @param searchParam
	 * @return
	 */
	public List<Map<String, Object>> getUserBasicInfoList(
			Map<String, Object> searchParam);
	/**
	 * 获得符合条件的用户数量
	 * @param params
	 * @return
	 */
	public int getUserCountByCondition(Map<String, Object> params);
	public Syscoupon1 findSyscouponById(long id);
	public int insertNotifyPersion(Map<String, Object> mm);
	public int insertCouponSendLog(Map<String, Object> mm);
	
}
