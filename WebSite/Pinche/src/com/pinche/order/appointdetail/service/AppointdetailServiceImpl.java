package com.pinche.order.appointdetail.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.order.appoint.dao.ExecDetails;
import com.pinche.order.appointdetail.dao.Appointdetail;
import com.pinche.order.appointdetail.dao.AppointdetailInfo;
import com.pinche.order.appointdetail.dao.AppointdetailMapper;
import com.pinche.order.appointdetail.dao.FreezeTsInfo;
import com.pinche.order.appointdetail.dao.OrderLongdistanceUsersCs;
import com.pinche.order.appointdetail.dao.OrderModifyLogInfo;
import com.pinche.order.appointdetail.dao.OrderOnoffdutyDivide;
import com.pinche.order.appointdetail.dao.OrderOnoffdutyExecDetails;

public  class AppointdetailServiceImpl implements AppointdetailService {

	private SqlSession session = null;
	private AppointdetailMapper mapper = null;
	
	public AppointdetailServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(AppointdetailMapper.class);
	}

	@Override
	public List<Appointdetail> search(Map<String, Object> map) {
		return mapper.search(map);
	}

	@Override
	public FreezeTsInfo findFreezeTsInfo(Long orderExecId) {
		return mapper.findFreezeTsInfo(orderExecId);
	}

	@Override
	public int updateFreeze(Map<String, Object> map) {
		int rows = mapper.updateFreeze(map);
		if (rows > 0)
			session.commit();
		else
			return 0;
		return rows;
	}

	@Override
	public int insertTsInfo(Map<String, Object> map) {
		return mapper.insertTsInfo(map);
	}

	@Override
	public String getBankCard(int userId) {
		return mapper.getBankCard(userId);
	}

	@Override
	public int getFreezeMaxId() {
		return mapper.getFreezeMaxId();
	}

	@Override
	public int getBreakAppointCnt(String orderNum) {
		return mapper.getBreakAppointCnt(orderNum);
	}

	@Override
	public int updateOrderTempDetail(Map<String, Object> map) {
		return mapper.updateOrderTempDetail(map);
	}

	@Override
	public int updateOrderExecCs(Map<String, Object> map) {
		return mapper.updateOrderExecCs(map);
	}

	@Override
	public int updateUserBalance_ts(long userId, int ts_id) {
		return mapper.updateUserBalance_ts(userId, ts_id);
	}

	@Override
	public double getBalanceByUserId(long userId) {
		return mapper.getBalanceByUserId(userId);
	}

	@Override
	public int insertOrderExecHistory(Map<String, Object> map) {
		int rows = mapper.insertOrderExecHistory(map);
		if (rows > 0) 
			session.commit();
		else
			return 0;
		return rows;
	}

	@Override
	public List<OrderModifyLogInfo> findOrderExecModifyLog(Map<String, Object> map) {
		return mapper.findOrderExecModifyLog(map);
	}

	@Override
	public int updateOrderOnoffDetail(Map<String, Object> map) {
		return mapper.updateOrderOnoffDetail(map);
	}

	@Override
	public int updateOrderLongDetail(Map<String, Object> map) {
		return mapper.updateOrderLongDetail(map);
	}

	@Override
	public int updateWeekdays(String weekdays, long orderdetails_id) {
		return mapper.updateWeekdays(weekdays, orderdetails_id);
	}

	@Override
	public List<OrderLongdistanceUsersCs> findLongdistanceUserCs(long orderdriverlongdistance_id) {
		return mapper.findLongdistanceUserCs(orderdriverlongdistance_id);
	}
	
	@Override
	public int deleteOrderLongdistanceUsers(long orderdriverlongdistance_id) {
		return mapper.deleteOrderLongdistanceUsers(orderdriverlongdistance_id);
	}

	@Override
	public void sessionCommit() {
		session.commit();
	}

	@Override
	public void sessionRollback() {
		session.rollback();
	}

	@Override
	public OrderOnoffdutyDivide findOnoffdutyDivideByOrderdetails_id(long orderdetails_id) {
		return mapper.findOnoffdutyDivideByOrderdetails_id(orderdetails_id);
	}

	@Override
	public int deleteOrderOnoffdutyDivide(long orderdetails_id) {
		return mapper.deleteOrderOnoffdutyDivide(orderdetails_id);
	}

	@Override
	public List<OrderOnoffdutyExecDetails> findOnoffdutyExecByOnoffduty_divide_id(
			long onoffduty_divide_id) {
		return mapper.findOnoffdutyExecByOnoffduty_divide_id(onoffduty_divide_id);
	}

	@Override
	public int deleteOrderOnoffdutyExecDetailsByOnoffduty_divide_id(
			long onoffduty_divide_id) {
		return mapper.deleteOrderOnoffdutyExecDetailsByOnoffduty_divide_id(onoffduty_divide_id);
	}

	@Override
	public int findAcountOrderModifyLog(Map<String, Object> m) {
		return mapper.findAcountOrderModifyLog(m);
	}

	@Override
	public AppointdetailInfo findOrderdetailInfo(Map<String, Object> map) {
		return mapper.findOrderdetailInfo(map);
	}

	@Override
	public List<ExecDetails> findTempExecByOId(Map<String, Object> map) {
		return mapper.findTempExecByOId(map);
	}

	@Override
	public List<ExecDetails> findOnOffDExecByOId(Map<String, Object> map) {
		return mapper.findOnOffDExecByOId(map);
	}

	@Override
	public List<ExecDetails> findLongDExecByOId(Map<String, Object> map) {
		return mapper.findLongDExecByOId(map);
	}

	@Override
	public Map<String, Object> findFreezeStatus(long id) {
		return mapper.findFreezeStatus(id);
	}

	@Override
	public int findAccountFreeze(Map<String, Object> map2) {
		return mapper.findAccountFreeze(map2);
	}

	@Override
	public int findAccountUnfreeze(Map<String, Object> map2) {
		return mapper.findAccountUnfreeze(map2);
	}
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
