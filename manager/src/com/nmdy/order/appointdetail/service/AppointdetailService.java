package com.nmdy.order.appointdetail.service;

import java.util.List;
import java.util.Map;

import com.nmdy.order.appoint.dao.ExecDetails;
import com.nmdy.order.appointdetail.dao.Appointdetail;
import com.nmdy.order.appointdetail.dao.AppointdetailInfo;
import com.nmdy.order.appointdetail.dao.FreezeTsInfo;
import com.nmdy.order.appointdetail.dao.OrderLongdistanceUsersCs;
import com.nmdy.order.appointdetail.dao.OrderModifyLogInfo;
import com.nmdy.order.appointdetail.dao.OrderOnoffdutyDivide;
import com.nmdy.order.appointdetail.dao.OrderOnoffdutyExecDetails;


public interface AppointdetailService{
	public List<Appointdetail> search(Map<String, Object> map);
	public int getBreakAppointCnt(String orderNum);
	public int updateFreeze(Map<String, Object> map);
	public FreezeTsInfo findFreezeTsInfo(Long id);
	public int insertTsInfo(Map<String, Object> map);
	public String getBankCard(int userId);
	public int getFreezeMaxId();
	public int updateOrderTempDetail(Map<String, Object> map);
	public int updateOrderExecCs(Map<String, Object> map);
	public double getBalanceByUserId(long userId);
	public int updateUserBalance_ts(long userId, int ts_id);
	public int updateOrderOnoffDetail(Map<String, Object> map);
	public int updateOrderLongDetail(Map<String, Object> map);
	public int updateWeekdays(String weekdays, long orderdetails_id);
	public List<OrderLongdistanceUsersCs> findLongdistanceUserCs(long orderdriverlongdistance_id);
	public int deleteOrderLongdistanceUsers(long orderdriverlongdistance_id);
	public OrderOnoffdutyDivide findOnoffdutyDivideByOrderdetails_id(long orderdetails_id);
	public int deleteOrderOnoffdutyDivide(long orderdetails_id);
	public List<OrderOnoffdutyExecDetails> findOnoffdutyExecByOnoffduty_divide_id(long onoffduty_divide_id);
	public int deleteOrderOnoffdutyExecDetailsByOnoffduty_divide_id(long onoffduty_divide_id);
	public int findAcountOrderModifyLog(Map<String, Object> m);
	public AppointdetailInfo findOrderdetailInfo(Map<String, Object> map);
	public List<ExecDetails> findTempExecByOId(Map<String, Object> map);
	public List<ExecDetails> findOnOffDExecByOId(Map<String, Object> map);
	public List<ExecDetails> findLongDExecByOId(Map<String, Object> map);
	public List<OrderModifyLogInfo> findOrderExecModifyLog(Map<String, Object> m);
	public int insertOrderExecHistory(Map<String, Object> m);
	public Map<String, Object> findFreezeStatus(long id);
	public int findAccountFreeze(Map<String, Object> map2);
	public int findAccountUnfreeze(Map<String, Object> map2);
}

