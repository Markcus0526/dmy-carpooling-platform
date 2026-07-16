package com.pinche.spread.sms.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.customer.user.dao.UserBasicInfo;
import com.pinche.spread.sms.dao.SMSPlan;
import com.pinche.spread.sms.dao.SMSPlanLog;
import com.pinche.spread.sms.dao.SMSPlanMapper;
import com.pinche.spread.sms.dao.SMSUserInfo;

public class SMSPlanServiceImpl implements SMSPlanService {

	SqlSession session = null;
	SMSPlanMapper mapper = null;
	
	public SMSPlanServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(SMSPlanMapper.class);
	}
	
	@Override
	public List<Map<String, Object>> findAllPlan() {
		return mapper.findAllPlan();
	}

	@Override
	public List<Map<String, Object>> findAllPlanLog() {
		return mapper.findAllPlanLog();
	}

	@Override
	public List<Map<String, Object>> findAllSMSUserInfo() {
		return mapper.findAllSMSUserInfo();
	}

	@Override
	public List<Map<String, Object>> searchPlan(Map<String, Object> map) {
		return mapper.searchPlan(map);
	}

	@Override
	public int addPlan(Map<String, Object> params) {
		int rows = mapper.addPlan(params);
		if (rows > 0)
			session.commit();
		
		return rows;
	}

	@Override
	public int addPlanLog(Map<String,Object> params) {
		int rows = mapper.addPlanLog(params);	
		if (rows > 0)
			session.commit();
		
		return rows;
	}

	@Override
	public int addSMSUser(Map<String, Object> params) {
		int rows = mapper.addSMSUser(params);
		if (rows > 0)
			session.commit();
		
		return rows;
	}

	@Override
	public String getEnvVariable(String codeName) {
		return mapper.getEnvVariable(codeName);
	}
	
/*	public SMSPlan findOneByCode(String planCode) {
		return mapper.findOneByCode(planCode);
	}*/
	public Map<String, Object> findOneByCode(String planCode) {
		return mapper.findOneByCode(planCode);
	}

	@Override
	public List<Map<String, Object>> searchSMSUsers(Map<String, Object> map) {
		return mapper.searchSMSUsers(map);
	}

	@Override
	public int deleteSMSUser(Map<String, Object> map) {
		int rows = mapper.deleteSMSUser(map);
		if (rows > 0)
			session.commit();
		
		return rows;
	}

	@Override
	public int changeSMSPlan(long id) {
		int rows = mapper.changeSMSPlan(id);
		if (rows > 0)
			session.commit();
		
		return rows;
	}

	public int getUserCountByCondition(Map<String, Object> params) {
	
		return mapper.getUserCountByCondition(params);
	}

	public double getSMSPrice() {
		return mapper.getSMSPrice();
	}

	public long findId() {
		return mapper.findId();
	}

	public List<Map<String, Object>> searchSMSId(Map<String, Object> params) {
		return mapper.searchSMSId(params);
	}

	public int findCountUser(Map<String, Object> params) {
		return mapper.findCountUser(params);
	}

	public List<Map<String, Object>> findSMSUser(Map<String, Object> params) {
		return mapper.findSMSUser(params);
	}

	public int findSCNT(Map<String, Object> map) {
		return mapper.findSCNT(map);
	}

	public int findFCNT(Map<String, Object> map) {
		return mapper.findFCNT(map);
	}

	public String getPhoneById(long id) {
		return mapper.getPhoneById(id);
	}

	public List<Map<String, Object>> getPhones(long id) {
		return mapper.getPhones(id);
	}

	public Map<String, Object> show(long id) {
		return mapper.show(id);
	}

	@Override
	public int getCountByCondition(Map<String, Object> params) {
		return mapper.getCountByCondition(params);
	}

	@Override
	public List<Map<String, Object>> searchUnSelectUsers(Map<String, Object> params) {
		return mapper.searchUnSelectUsers(params);
	}

	@Override
	public int changeSMSPlans(long sms_plan_id) {
		int rows = mapper.changeSMSPlanById(sms_plan_id);
		if (rows > 0)
			session.commit();
		
		return rows;
		
	}

	public int updatePlan(Map<String, Object> params) {
		int rows = mapper.updatePlan(params);
		if (rows > 0)
			session.commit();
		
		return rows;
	}


}
