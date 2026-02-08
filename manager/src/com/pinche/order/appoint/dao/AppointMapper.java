package com.pinche.order.appoint.dao;

import java.util.List;
import java.util.Map;

import com.pinche.order.appointdetail.dao.FreezeTsInfo;
import com.pinche.order.appointdetail.dao.OrderModifyLogInfo;

public interface AppointMapper {
	public Appoint findOneById(long id);
	public List<Appoint> findAll(Map<String, Object> map);
	public int editAppoint(Appoint info);	
	public List<Appoint1> search(Map<String, Object> map);
	public String getWeekdays(int id);
	public TempInfo findTempInfo(long id);
	public LongDistanceInfo findLongDistanceInfo(long id);
	public OnoffdutyInfo findOnoffdutyInfo(long id);
	public List<MidPoints> findMidPoints(Map<String, Object> map);
	public int insertMidpoint(MidPoints midpoints);
	public int updateMidpoints(Map<String, Object> map);
	public List<Appoint> searchforRecourse(Map<String, Object> map);	
	public List<Appoint> searchforFinance(Map<String, Object> map);	
	public void deleteOrderExecCs(long id);
	public Customer findOneByUsername(Map<String, Object> map);
	public List<ExecDetails> findTempExecByOnum(Map<String, Object> map);
	public List<ExecDetails> findOnOffDExecByOnum(Map<String, Object> map);
	public List<ExecDetails> findLongDExecByOnum(Map<String, Object> map);
	public int deletedMidPoints(Map<String, Object> map);
	
	public FreezeTsInfo findFreezeTsInfo(Long orderExecId);
	public int insertTsInfo(Map<String, Object> map);
	public int getFreezeMaxId();
	public int updateFreeze(Map<String, Object> map);
	public int getBreakAppointCnt(String orderNum);
	public int updateOrderExecCs(Map<String, Object> map);
	public int updateOrderTempDetail(Map<String, Object> map);
	public int updateOrderOnoffDetail(Map<String, Object> map);
	public int updateOrderLongDetail(Map<String, Object> map);
	public int updateWeekdays(String weekdays, long orderdetails_id);
	public double getBalanceByUserId(long passagerId);
	public int insertOrderHistory(Map<String, Object> map);
	public int findAcountOrderModifyLog(Map<String, Object> m);
	public List<OrderModifyLogInfo> findOrderModifyLog(Map<String, Object> map);
	public int updateUserBalance_ts(long passagerId, Integer valueOf);
	public Customer findById(long userid);
	public Customer findByPhone(String userphone);
	public Map<String, Object> findFreezeStatus(long passenger);
	public int findAccountFreeze(Map<String, Object> map1);
	public int findAccountUnfreeze(Map<String, Object> map1);

}
