package com.nmdy.order.appoint.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.common.SqlSessionHelper;
import com.nmdy.order.appoint.dao.Appoint;
import com.nmdy.order.appoint.dao.Appoint1;
import com.nmdy.order.appoint.dao.AppointMapper;
import com.nmdy.order.appoint.dao.Customer;
import com.nmdy.order.appoint.dao.ExecDetails;
import com.nmdy.order.appoint.dao.LongDistanceInfo;
import com.nmdy.order.appoint.dao.MidPoints;
import com.nmdy.order.appoint.dao.OnoffdutyInfo;
import com.nmdy.order.appoint.dao.TempInfo;
import com.nmdy.order.appointdetail.dao.FreezeTsInfo;
import com.nmdy.order.appointdetail.dao.OrderModifyLogInfo;

public class AppointServiceImpl implements AppointService {

    private AppointMapper appointMapper; 

	@Override
	public Appoint findOneById(long id) {
		return appointMapper.findOneById(id);
	}

	@Override
	public int updateAppoint(Appoint info) {
		return appointMapper.editAppoint(info);
	}

	@Override
	public List<Appoint1> search(Map<String, Object> map) {
		return appointMapper.search(map);
	}

	@Override
	public String getWeekdays(int id) {
		return appointMapper.getWeekdays(id);
	}

	@Override
	public List<Appoint> findAll(Map<String, Object> map) {
		return appointMapper.findAll(map);
	}

	@Override
	public TempInfo findTempInfo(long id) {
		return appointMapper.findTempInfo(id);
	}

	@Override
	public LongDistanceInfo findLongDistanceInfo(long id) {
		return appointMapper.findLongDistanceInfo(id);
	}

	@Override
	public OnoffdutyInfo findOnoffdutyInfo(long id) {
		return appointMapper.findOnoffdutyInfo(id);
	}

	@Override
	public List<MidPoints> findMidPoints(Map<String, Object> map) {
		return appointMapper.findMidPoints(map);
	}

	@Override
	public List<Appoint> searchforRecourse(Map<String, Object> map) {
		return appointMapper.searchforRecourse(map);
	}

	@Override
	public List<Appoint> searchforFinance(Map<String, Object> map) {
		return appointMapper.searchforFinance(map);
	}

	@Override
	public int insertMidpoint(MidPoints midpoints) {
	return appointMapper.insertMidpoint(midpoints);	
	}

	@Override
	public int updateMidpoints(Map<String, Object> map) {
		return appointMapper.updateMidpoints(map);

	}

	@Override
	public void deleteOrderExecCs(long id) {
		appointMapper.deleteOrderExecCs(id);		
	}



	@Override
	public Customer findOneByUsername(Map<String, Object> map) {

		return appointMapper.findOneByUsername(map);
	}

	@Override
	public List<ExecDetails> findTempExecByOnum(Map<String, Object> map) {
		return appointMapper.findTempExecByOnum(map);
	}

	@Override
	public List<ExecDetails> findOnOffDExecByOnum(Map<String, Object> map) {
		return appointMapper.findOnOffDExecByOnum(map);
	}

	@Override
	public List<ExecDetails> findLongDExecByOnum(Map<String, Object> map) {
		return appointMapper.findLongDExecByOnum(map);
	}

	@Override
	public int deletedMidPoints(Map<String, Object> map) {
		return appointMapper.deletedMidPoints(map);
	}

	
	
	
	@Override
	public FreezeTsInfo findFreezeTsInfo(Long orderExecId) {
		return appointMapper.findFreezeTsInfo(orderExecId);
	}

	@Override
	public int insertTsInfo(Map<String, Object> map) {
		return appointMapper.insertTsInfo(map);
	}

	@Override
	public int getFreezeMaxId() {
		return appointMapper.getFreezeMaxId();
	}

	@Override
	public int updateFreeze(Map<String, Object> map) {
		return  appointMapper.updateFreeze(map);
	}

	@Override
	public int getBreakAppointCnt(String orderNum) {
		return appointMapper.getBreakAppointCnt(orderNum);
	}

	@Override
	public int updateOrderExecCs(Map<String, Object> map) {
		return appointMapper.updateOrderExecCs(map);
	}

	@Override
	public int updateOrderTempDetail(Map<String, Object> map) {
		return appointMapper.updateOrderTempDetail(map);
	}


	@Override
	public int updateOrderOnoffDetail(Map<String, Object> map) {
		return appointMapper.updateOrderOnoffDetail(map);
	}

	@Override
	public int updateOrderLongDetail(Map<String, Object> map) {
		return appointMapper.updateOrderLongDetail(map);
	}


	@Override
	public int updateWeekdays(String weekdays, long orderdetails_id) {
		return appointMapper.updateWeekdays(weekdays, orderdetails_id);
	}


	//@Override
	//public Long getBalanceByUserId(int userId) {
	//	
	//}

	@Override
	public double getBalanceByUserId(long passagerId) {
		return appointMapper.getBalanceByUserId(passagerId);
	}

	@Override
	public int updateUserBalance_ts(long passagerId, Integer valueOf) {
		return appointMapper.updateUserBalance_ts(passagerId, valueOf);
	}
	


	@Override
	public int insertOrderHistory(Map<String, Object> map) {
		return appointMapper.insertOrderHistory(map);
	}
	
	@Override
	public int findAcountOrderModifyLog(Map<String, Object> m) {
		return appointMapper.findAcountOrderModifyLog(m);
	}


	@Override
	public List<OrderModifyLogInfo> findOrderModifyLog(Map<String, Object> map) {
		return appointMapper.findOrderModifyLog(map);
	}

	@Override
	public Customer findById(long userid) {
		return appointMapper.findById(userid);
	}

	@Override
	public Customer findByPhone(String userphone) {
		return appointMapper.findByPhone(userphone);
	}

	@Override
	public Map<String, Object> findFreezeStatus(long passenger) {
			return appointMapper.findFreezeStatus(passenger);
	}

	@Override
	public int findAccountFreeze(Map<String, Object> map1) {
		return appointMapper.findAccountFreeze(map1);
	}

	@Override
	public int findAccountUnfreeze(Map<String, Object> map1) {
		return appointMapper.findAccountUnfreeze(map1);
	}

	public AppointMapper getAppointMapper() {
		return appointMapper;
	}

	public void setAppointMapper(AppointMapper appointMapper) {
		this.appointMapper = appointMapper;
	}



}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
