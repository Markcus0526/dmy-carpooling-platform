package com.nmdy.spread.sms.service;

import java.util.List;
import java.util.Map;

public interface SMSPlanService {
	//为了避免出现类型匹配错误，所以不建议采用pojo，用Map<String,Object>代替
	public List<Map<String, Object>> findAllPlan();
	public List<Map<String, Object>> findAllPlanLog();
	public List<Map<String, Object>> findAllSMSUserInfo();
	public List<Map<String, Object>> searchPlan(Map<String, Object> map);
	public List<Map<String, Object>> searchSMSUsers(Map<String, Object> map);

	public Map<String, Object> findOneByCode(String planCode);
	public String getEnvVariable(String codeName);
	public int addPlan(Map<String, Object> params);
	public int addPlanLog(Map<String, Object> params);
	public int addSMSUser(Map<String, Object> params);
	public int deleteSMSUser(Map<String, Object> map);
	public int updateSMSPlan(long sms_plan_id);
	public int getCountByCondition(Map<String, Object> params);
	public List<Map<String, Object>> searchUnSelectUsers(Map<String, Object> params);
	public String getPhoneById(long userid);
	public int updateSMSPlans(long sms_plan_id);
}
