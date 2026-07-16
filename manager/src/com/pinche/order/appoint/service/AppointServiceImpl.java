package com.pinche.order.appoint.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.order.appoint.dao.Appoint;
import com.pinche.order.appoint.dao.Appoint1;
import com.pinche.order.appoint.dao.AppointMapper;
import com.pinche.order.appoint.dao.Customer;
import com.pinche.order.appoint.dao.ExecDetails;
import com.pinche.order.appoint.dao.LongDistanceInfo;
import com.pinche.order.appoint.dao.MidPoints;
import com.pinche.order.appoint.dao.OnoffdutyInfo;
import com.pinche.order.appoint.dao.TempInfo;
import com.pinche.order.appointdetail.dao.FreezeTsInfo;
import com.pinche.order.appointdetail.dao.OrderModifyLogInfo;

public class AppointServiceImpl implements AppointService {

	private SqlSession session = null;
	private AppointMapper mapper = null;
	
	public AppointServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(AppointMapper.class);
	}

	@Override
	public Appoint findOneById(long id) {
		return mapper.findOneById(id);
	}

	@Override
	public int editAppoint(Appoint info) {
		return mapper.editAppoint(info);
	}

	@Override
	public List<Appoint1> search(Map<String, Object> map) {
		return mapper.search(map);
	}

	@Override
	public String getWeekdays(int id) {
		return mapper.getWeekdays(id);
	}

	@Override
	public List<Appoint> findAll(Map<String, Object> map) {
		return mapper.findAll(map);
	}

	@Override
	public TempInfo findTempInfo(long id) {
		return mapper.findTempInfo(id);
	}

	@Override
	public LongDistanceInfo findLongDistanceInfo(long id) {
		return mapper.findLongDistanceInfo(id);
	}

	@Override
	public OnoffdutyInfo findOnoffdutyInfo(long id) {
		return mapper.findOnoffdutyInfo(id);
	}

	@Override
	public List<MidPoints> findMidPoints(Map<String, Object> map) {
		return mapper.findMidPoints(map);
	}

	@Override
	public List<Appoint> searchforRecourse(Map<String, Object> map) {
		return mapper.searchforRecourse(map);
	}

	@Override
	public List<Appoint> searchforFinance(Map<String, Object> map) {
		return mapper.searchforFinance(map);
	}

	@Override
	public int insertMidpoint(MidPoints midpoints) {
		int row = mapper.insertMidpoint(midpoints);	
		if(row>0){
			session.commit();
		}
		 return row;
	}

	@Override
	public int updateMidpoints(Map<String, Object> map) {
		return mapper.updateMidpoints(map);

	}

	@Override
	public void deleteOrderExecCs(long id) {
		mapper.deleteOrderExecCs(id);		
	}



	@Override
	public Customer findOneByUsername(Map<String, Object> map) {

		return mapper.findOneByUsername(map);
	}

	@Override
	public List<ExecDetails> findTempExecByOnum(Map<String, Object> map) {
		return mapper.findTempExecByOnum(map);
	}

	@Override
	public List<ExecDetails> findOnOffDExecByOnum(Map<String, Object> map) {
		return mapper.findOnOffDExecByOnum(map);
	}

	@Override
	public List<ExecDetails> findLongDExecByOnum(Map<String, Object> map) {
		return mapper.findLongDExecByOnum(map);
	}

	@Override
	public int deletedMidPoints(Map<String, Object> map) {
		return mapper.deletedMidPoints(map);
	}

	
	
	
	@Override
	public FreezeTsInfo findFreezeTsInfo(Long orderExecId) {
		return mapper.findFreezeTsInfo(orderExecId);
	}

	@Override
	public int insertTsInfo(Map<String, Object> map) {
		return mapper.insertTsInfo(map);
	}

	@Override
	public int getFreezeMaxId() {
		return mapper.getFreezeMaxId();
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
	public int getBreakAppointCnt(String orderNum) {
		return mapper.getBreakAppointCnt(orderNum);
	}

	@Override
	public int updateOrderExecCs(Map<String, Object> map) {
		return mapper.updateOrderExecCs(map);
	}

	@Override
	public int updateOrderTempDetail(Map<String, Object> map) {
		return mapper.updateOrderTempDetail(map);
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


	//@Override
	//public Long getBalanceByUserId(int userId) {
	//	
	//}

	@Override
	public double getBalanceByUserId(long passagerId) {
		return mapper.getBalanceByUserId(passagerId);
	}

	@Override
	public int updateUserBalance_ts(long passagerId, Integer valueOf) {
		return mapper.updateUserBalance_ts(passagerId, valueOf);
	}
	//@Override
	//public int updateUserBalance_ts(int userId, int ts_id) {
	//	return mapper.updateUserBalance_ts(userId, ts_id);
	//}

	@Override
	public void sessionCommit() {
		session.commit();
	}

	@Override
	public void sessionRollback() {
		session.rollback();
	}


	@Override
	public int insertOrderHistory(Map<String, Object> map) {
		int rows = mapper.insertOrderHistory(map);
		if (rows > 0) 
			session.commit();
		else
			return 0;
		return rows;
	}
	
	@Override
	public int findAcountOrderModifyLog(Map<String, Object> m) {
		return mapper.findAcountOrderModifyLog(m);
	}


	@Override
	public List<OrderModifyLogInfo> findOrderModifyLog(Map<String, Object> map) {
		return mapper.findOrderModifyLog(map);
	}

	@Override
	public Customer findById(long userid) {
		return mapper.findById(userid);
	}

	@Override
	public Customer findByPhone(String userphone) {
		return mapper.findByPhone(userphone);
	}

	@Override
	public Map<String, Object> findFreezeStatus(long passenger) {
			return mapper.findFreezeStatus(passenger);
	}

	@Override
	public int findAccountFreeze(Map<String, Object> map1) {
		return mapper.findAccountFreeze(map1);
	}

	@Override
	public int findAccountUnfreeze(Map<String, Object> map1) {
		return mapper.findAccountUnfreeze(map1);
	}


}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
