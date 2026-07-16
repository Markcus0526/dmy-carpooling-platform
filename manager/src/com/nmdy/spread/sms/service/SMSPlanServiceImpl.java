package com.nmdy.spread.sms.service;

import java.util.List;
import java.util.Map;
import com.nmdy.spread.sms.dao.SMSPlanMapper;


public class SMSPlanServiceImpl implements SMSPlanService {

		private SMSPlanMapper sMSPlanMapper;
	@Override
	public List<Map<String, Object>> findAllPlan() {
		return sMSPlanMapper.findAllPlan();
	}

	@Override
	public List<Map<String, Object>> findAllPlanLog() {
		return sMSPlanMapper.findAllPlanLog();
	}

	@Override
	public List<Map<String, Object>> findAllSMSUserInfo() {
		return sMSPlanMapper.findAllSMSUserInfo();
	}

	@Override
	public List<Map<String, Object>> searchPlan(Map<String, Object> map) {
		return sMSPlanMapper.searchPlan(map);
	}

	@Override
	public int addPlan(Map<String, Object> params) {
		return  sMSPlanMapper.addPlan(params);
	
	}

	@Override
	public int addPlanLog(Map<String,Object> params) {
		return sMSPlanMapper.addPlanLog(params);	
	}

	@Override
	public int addSMSUser(Map<String, Object> params) {
		return sMSPlanMapper.addSMSUser(params);
	}

	@Override
	public String getEnvVariable(String codeName) {
		return sMSPlanMapper.getEnvVariable(codeName);
	}
	
/*	public SMSPlan findOneByCode(String planCode) {
		return mapper.findOneByCode(planCode);
	}*/
	public Map<String, Object> findOneByCode(String planCode) {
		return sMSPlanMapper.findOneByCode(planCode);
	}

	@Override
	public List<Map<String, Object>> searchSMSUsers(Map<String, Object> map) {
		return sMSPlanMapper.searchSMSUsers(map);
	}

	@Override
	public int deleteSMSUser(Map<String, Object> map) {
		return sMSPlanMapper.deleteSMSUser(map);
	}

	@Override
	public int updateSMSPlan(long id) {
		return sMSPlanMapper.changeSMSPlan(id);
	}

	public int getUserCountByCondition(Map<String, Object> params) {
	
		return sMSPlanMapper.getUserCountByCondition(params);
	}

	public double getSMSPrice() {
		return sMSPlanMapper.getSMSPrice();
	}

	public long findId() {
		return sMSPlanMapper.findId();
	}

	public List<Map<String, Object>> searchSMSId(Map<String, Object> params) {
		return sMSPlanMapper.searchSMSId(params);
	}

	public int findCountUser(Map<String, Object> params) {
		return sMSPlanMapper.findCountUser(params);
	}

	public List<Map<String, Object>> findSMSUser(Map<String, Object> params) {
		return sMSPlanMapper.findSMSUser(params);
	}

	public int findSCNT(Map<String, Object> map) {
		return sMSPlanMapper.findSCNT(map);
	}

	public int findFCNT(Map<String, Object> map) {
		return sMSPlanMapper.findFCNT(map);
	}

	public String getPhoneById(long id) {
		return sMSPlanMapper.getPhoneById(id);
	}

	public List<Map<String, Object>> getPhones(long id) {
		return sMSPlanMapper.getPhones(id);
	}

	public Map<String, Object> show(long id) {
		return sMSPlanMapper.show(id);
	}

	@Override
	public int getCountByCondition(Map<String, Object> params) {
		return sMSPlanMapper.getCountByCondition(params);
	}

	@Override
	public List<Map<String, Object>> searchUnSelectUsers(Map<String, Object> params) {
		return sMSPlanMapper.searchUnSelectUsers(params);
	}

	@Override
	public int updateSMSPlans(long sms_plan_id) {
		return sMSPlanMapper.changeSMSPlanById(sms_plan_id);
		
	}

	public int updatePlan(Map<String, Object> params) {
		return sMSPlanMapper.updatePlan(params);
	}

	public SMSPlanMapper getsMSPlanMapper() {
		return sMSPlanMapper;
	}

	public void setsMSPlanMapper(SMSPlanMapper sMSPlanMapper) {
		this.sMSPlanMapper = sMSPlanMapper;
	}


}
