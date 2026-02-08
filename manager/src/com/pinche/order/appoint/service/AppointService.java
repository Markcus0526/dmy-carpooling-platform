package com.pinche.order.appoint.service;

import java.util.List;
import java.util.Map;

import com.pinche.order.appoint.dao.Appoint;
import com.pinche.order.appoint.dao.Appoint1;
import com.pinche.order.appoint.dao.Customer;
import com.pinche.order.appoint.dao.ExecDetails;
import com.pinche.order.appoint.dao.LongDistanceInfo;
import com.pinche.order.appoint.dao.MidPoints;
import com.pinche.order.appoint.dao.OnoffdutyInfo;
import com.pinche.order.appoint.dao.TempInfo;
import com.pinche.order.appointdetail.dao.FreezeTsInfo;
import com.pinche.order.appointdetail.dao.OrderModifyLogInfo;

public interface AppointService{
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
	public Customer findById(long userid);
	public Customer findOneByUsername(Map<String, Object> map);
	public List<ExecDetails> findTempExecByOnum(Map<String, Object> map);
	public List<ExecDetails> findOnOffDExecByOnum(Map<String, Object> map);
	public List<ExecDetails> findLongDExecByOnum(Map<String, Object> map);
	public int deletedMidPoints(Map<String, Object> map);
	
	
	public FreezeTsInfo findFreezeTsInfo(Long id);
	public int insertTsInfo(Map<String, Object> m);
	public int getFreezeMaxId();
	public int updateFreeze(Map<String, Object> m);
	public int getBreakAppointCnt(String order_num);
	public int updateOrderExecCs(Map<String, Object> m);
	public int updateOrderTempDetail(Map<String, Object> m);
	public int updateOrderOnoffDetail(Map<String, Object> m);
	public int updateOrderLongDetail(Map<String, Object> m);
	public int updateWeekdays(String str_weekdays, long id);
	public double getBalanceByUserId(long passagerId);
	public int updateUserBalance_ts(long passagerId, Integer valueOf);
	public void sessionCommit();
	public void sessionRollback();
	public int insertOrderHistory(Map<String, Object> m);
	public int findAcountOrderModifyLog(Map<String, Object> m);
	public List<OrderModifyLogInfo> findOrderModifyLog(Map<String, Object> m);
	public Customer findByPhone(String userphone);
	public Map<String, Object> findFreezeStatus(long passenger);
	public int findAccountFreeze(Map<String, Object> map1);
	public int findAccountUnfreeze(Map<String, Object> map1);
}
