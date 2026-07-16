package com.nmdy.order.appointdetail.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.common.SqlSessionHelper;
import com.nmdy.order.appoint.dao.ExecDetails;
import com.nmdy.order.appointdetail.dao.Appointdetail;
import com.nmdy.order.appointdetail.dao.AppointdetailInfo;
import com.nmdy.order.appointdetail.dao.AppointdetailMapper;
import com.nmdy.order.appointdetail.dao.FreezeTsInfo;
import com.nmdy.order.appointdetail.dao.OrderLongdistanceUsersCs;
import com.nmdy.order.appointdetail.dao.OrderModifyLogInfo;
import com.nmdy.order.appointdetail.dao.OrderOnoffdutyDivide;
import com.nmdy.order.appointdetail.dao.OrderOnoffdutyExecDetails;


public  class AppointdetailServiceImpl implements AppointdetailService {

	private AppointdetailMapper appointdetailMapper;
	@Override
	public List<Appointdetail> search(Map<String, Object> map) {
		return appointdetailMapper.search(map);
	}

	@Override
	public FreezeTsInfo findFreezeTsInfo(Long orderExecId) {
		return appointdetailMapper.findFreezeTsInfo(orderExecId);
	}

	@Override
	public int updateFreeze(Map<String, Object> map) {
		return appointdetailMapper.updateFreeze(map);
	}

	@Override
	public int insertTsInfo(Map<String, Object> map) {
		return appointdetailMapper.insertTsInfo(map);
	}

	@Override
	public String getBankCard(int userId) {
		return appointdetailMapper.getBankCard(userId);
	}

	@Override
	public int getFreezeMaxId() {
		return appointdetailMapper.getFreezeMaxId();
	}

	@Override
	public int getBreakAppointCnt(String orderNum) {
		return appointdetailMapper.getBreakAppointCnt(orderNum);
	}

	@Override
	public int updateOrderTempDetail(Map<String, Object> map) {
		return appointdetailMapper.updateOrderTempDetail(map);
	}

	@Override
	public int updateOrderExecCs(Map<String, Object> map) {
		return appointdetailMapper.updateOrderExecCs(map);
	}

	@Override
	public int updateUserBalance_ts(long userId, int ts_id) {
		return appointdetailMapper.updateUserBalance_ts(userId, ts_id);
	}

	@Override
	public double getBalanceByUserId(long userId) {
		return appointdetailMapper.getBalanceByUserId(userId);
	}

	@Override
	public int insertOrderExecHistory(Map<String, Object> map) {
		return appointdetailMapper.insertOrderExecHistory(map);
	}

	@Override
	public List<OrderModifyLogInfo> findOrderExecModifyLog(Map<String, Object> map) {
		return appointdetailMapper.findOrderExecModifyLog(map);
	}

	@Override
	public int updateOrderOnoffDetail(Map<String, Object> map) {
		return appointdetailMapper.updateOrderOnoffDetail(map);
	}

	@Override
	public int updateOrderLongDetail(Map<String, Object> map) {
		return appointdetailMapper.updateOrderLongDetail(map);
	}

	@Override
	public int updateWeekdays(String weekdays, long orderdetails_id) {
		return appointdetailMapper.updateWeekdays(weekdays, orderdetails_id);
	}

	@Override
	public List<OrderLongdistanceUsersCs> findLongdistanceUserCs(long orderdriverlongdistance_id) {
		return appointdetailMapper.findLongdistanceUserCs(orderdriverlongdistance_id);
	}
	
	@Override
	public int deleteOrderLongdistanceUsers(long orderdriverlongdistance_id) {
		return appointdetailMapper.deleteOrderLongdistanceUsers(orderdriverlongdistance_id);
	}
	@Override
	public OrderOnoffdutyDivide findOnoffdutyDivideByOrderdetails_id(long orderdetails_id) {
		return appointdetailMapper.findOnoffdutyDivideByOrderdetails_id(orderdetails_id);
	}

	@Override
	public int deleteOrderOnoffdutyDivide(long orderdetails_id) {
		return appointdetailMapper.deleteOrderOnoffdutyDivide(orderdetails_id);
	}

	@Override
	public List<OrderOnoffdutyExecDetails> findOnoffdutyExecByOnoffduty_divide_id(
			long onoffduty_divide_id) {
		return appointdetailMapper.findOnoffdutyExecByOnoffduty_divide_id(onoffduty_divide_id);
	}

	@Override
	public int deleteOrderOnoffdutyExecDetailsByOnoffduty_divide_id(
			long onoffduty_divide_id) {
		return appointdetailMapper.deleteOrderOnoffdutyExecDetailsByOnoffduty_divide_id(onoffduty_divide_id);
	}

	@Override
	public int findAcountOrderModifyLog(Map<String, Object> m) {
		return appointdetailMapper.findAcountOrderModifyLog(m);
	}

	@Override
	public AppointdetailInfo findOrderdetailInfo(Map<String, Object> map) {
		return appointdetailMapper.findOrderdetailInfo(map);
	}

	@Override
	public List<ExecDetails> findTempExecByOId(Map<String, Object> map) {
		return appointdetailMapper.findTempExecByOId(map);
	}

	@Override
	public List<ExecDetails> findOnOffDExecByOId(Map<String, Object> map) {
		return appointdetailMapper.findOnOffDExecByOId(map);
	}

	@Override
	public List<ExecDetails> findLongDExecByOId(Map<String, Object> map) {
		return appointdetailMapper.findLongDExecByOId(map);
	}

	@Override
	public Map<String, Object> findFreezeStatus(long id) {
		return appointdetailMapper.findFreezeStatus(id);
	}

	@Override
	public int findAccountFreeze(Map<String, Object> map2) {
		return appointdetailMapper.findAccountFreeze(map2);
	}

	@Override
	public int findAccountUnfreeze(Map<String, Object> map2) {
		return appointdetailMapper.findAccountUnfreeze(map2);
	}

	public AppointdetailMapper getAppointdetailMapper() {
		return appointdetailMapper;
	}

	public void setAppointdetailMapper(AppointdetailMapper appointdetailMapper) {
		this.appointdetailMapper = appointdetailMapper;
	}
	
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
