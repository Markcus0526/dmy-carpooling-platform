package com.nmdy.spread.sms.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.pinche.customer.user.dao.UserBasicInfo;

public interface SMSPlanMapper extends SupperMapper{

/** 不用pojo而采用map来装载返回的结果集
 * 	public List<SMSPlan> findAllPlan();
	public List<SMSPlanLog> findAllPlanLog();
	public List<SMSUserInfo> findAllSMSUserInfo();
	public List<SMSPlan> searchPlan(Map<String, Object> map);
	public List<SMSUserInfo> searchSMSUsers(Map<String, Object> map);*/
	
	public List<Map<String, Object>> findAllPlan();
	public List<Map<String, Object>> findAllPlanLog();
	public List<Map<String, Object>> findAllSMSUserInfo();
	public List<Map<String, Object>> searchPlan(Map<String, Object> map);
	public List<Map<String, Object>> searchSMSUsers(Map<String, Object> map);
	
	//public SMSPlan findOneByCode(String planCode);
	public Map<String, Object> findOneByCode(String planCode);
	public String getEnvVariable(String codeName);
	public int addPlan(Map<String, Object> params);
	public int addPlanLog(Map<String, Object> params);
	public int addSMSUser(Map<String, Object> map);
	public int deleteSMSUser(Map<String, Object> map);
	public int changeSMSPlan(long id);
	public int getUserCountByCondition(Map<String, Object> params);
	public double getSMSPrice();
	public long findId();
	public List<Map<String, Object>> searchSMSId(Map<String, Object> params);
	public int findCountUser(Map<String, Object> params);
	public List<Map<String, Object>> findSMSUser(Map<String, Object> params);
	public int findSCNT(Map<String, Object> map);
	public int findFCNT(Map<String, Object> map);
	public String getPhoneById(long id);
	public List<Map<String, Object>> getPhones(long id);
	public Map<String, Object> show(long id);
	public int getCountByCondition(Map<String, Object> params);
	public List<Map<String, Object>> searchUnSelectUsers(Map<String, Object> params);
	public int changeSMSPlanById(long sms_plan_id);
	public int updatePlan(Map<String, Object> params);
}
