package com.pinche.spread.sms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.pinche.common.Errors;
import com.pinche.common.WebUtil;
import com.pinche.common.action.SecureAction;
import com.pinche.customer.user.dao.UserBasicInfo;
import com.pinche.customer.user.service.CustomerServiceImpl;
import com.pinche.spread.sms.dao.SMSUserInfo;
import com.pinche.spread.sms.service.SMSPlanService;


public class SMSUserAction extends SecureAction {

	private static final long serialVersionUID = 1L;

	// for search
	private int cityItem = 0;
	private String cityName = null;
	private int groupItem = 0;
	private String groupName = null;
	private int userItem = 0;
	private String userInfo = null;
	private String fromRegTime = null;
	private String toRegTime = null;
	private String fromLoginTime = null;
	private String toLoginTime = null;
	private int userType = 0;
	private Long id;
	private long userid;
	private long sms_plan_id;
	// for pagination
	private Integer rows;
	private Integer page;

	private SMSPlanService smsPlanService;
	private JSONObject resultObj = null;
	
	public String selectPage() {	
		ActionContext.getContext().put("id", id);
		return SUCCESS;
	}
  /*
   * 修改页面的跳转方法
   */
	public String editPage(){
		return "success";
	}
	/*
	 * 修改页面的没有此计划被选中的 用户列表的查询 方法 
	 */
	public String getUnSelectUser() {
		List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
		Map<String, Object> json = new HashMap<String, Object>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			if (!"".equals(cityItem) && cityName != null && cityName != "") {
				if ("city_register".equals(cityItem)) {
					params.put("cityItemSelect", "city_register");
					params.put("cityItemInput", cityName);
				}
				if ("city_cur".equals(cityItem)) {
					params.put("cityItemSelect", "city_cur");
					params.put("cityItemInput", cityName);
				}
				if ("register_or_cur".equals(cityItem)) {
					params.put("cityItemSelect", "register_or_cur");
					params.put("cityItemInput", cityName);
				}
			}
			// 集团/联盟查询条件
			if (!"".equals(groupItem) && groupName != null && groupName != "") {
				if ("group_or_asso_id".equals(groupItem)) {
					params.put("groupItemSelect", "group_or_asso_id");
					params.put("groupItemInput", groupName);
				}
				if ("group_or_asso_name".equals(groupItem)) {
					params.put("groupItemSelect", "group_or_asso_name");
					params.put("groupItemInput", groupName);
				}
			}
			// 用户查询条件
			if (!"".equals(userItem) && userInfo != null && userInfo != "") {
				if ("id".equals(userItem)) {
					params.put("userItemSelect", "id");
					params.put("userItemInput", userInfo);
				}
				if ("name".equals(userItem)) {
					params.put("userItemSelect", "name");
					params.put("userItemInput", userInfo);
				}
				if ("phone".equals(userItem)) {
					params.put("userItemSelect", "phone");
					params.put("userItemInput", userInfo);
				}
			}
			// 用户类型查询条件
			if (userType == 2) {
				params.put("userType", "driver");
			}
			else if (userType == 1) {
				params.put("userType", "passenger");
			}else{
				params.put("userType", null);
			}
			// 注册日期查询条件
			if (fromRegTime != null && !fromRegTime.isEmpty())
				params.put("fromRegTime", fromRegTime);

			if (toRegTime != null && !toRegTime.isEmpty())
				params.put("toRegTime", toRegTime);
			// 最后登录时间查询条件
			if (fromLoginTime != null && !fromLoginTime.isEmpty())
				params.put("fromLoginTime", fromLoginTime);

			if (toLoginTime != null && !toLoginTime.isEmpty())
				params.put("toLoginTime", toLoginTime);
			int total = smsPlanService.getCountByCondition(params);
			// 查询结果从第0条开始，查询10条记录
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			List<Map<String,Object>> userList = smsPlanService.searchUnSelectUsers(params);	
			for (Map<String,Object> user: userList) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", user.get("id"));
				m.put("usercode", user.get("usercode"));
				m.put("username", user.get("username"));
				m.put("phone", user.get("phone"));
				m.put("group_name", user.get("group_name")==null?"":user.get("group_name"));
				m.put("groupid", user.get("groupid")==null?"":user.get("groupid"));
				m.put("reg_date", user.get("reg_date")==null?"": user.get("reg_date"));
				m.put("last_login_time", user.get("last_login_time")==null?"": user.get("last_login_time"));
				rowList.add(m);
			}
			
			json.put("total", total);
			json.put("rows", rowList);
		
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}

	/*
	 *  修改 页面的 选择 用户功能 的方法
	 */
	public String addSMSuser(){
		int error=0;
		Map<String ,Object> map=new HashMap<String,Object>();
		try{
			map.put("sms_plan_id", sms_plan_id);
			smsPlanService.changeSMSPlan(sms_plan_id);
		map.put("userid", userid);
		String phone=smsPlanService.getPhoneById(userid);
		map.put("phone", phone);
		smsPlanService.addSMSUser(map);
		error=0;
		}catch(Exception e){
			e.printStackTrace();
			error=1;
		}
		Map<String,Object> m=new HashMap<String,Object>();
		m.put("error", error);
		resultObj=JSONObject.fromObject(m);
		return "success";
	}
	
	/*
	 * 修改 页面的 移除 用户功能 的方法
	 */
	public String deleteSMSuser(){
		int error=0;
		Map<String ,Object> map=new HashMap<String,Object>();
		try{
		map.put("sms_plan_id", sms_plan_id);
		smsPlanService.changeSMSPlans(sms_plan_id);
		map.put("userid", userid);
		smsPlanService.deleteSMSUser(map);
		error=0;
		}catch(Exception e){
			e.printStackTrace();
			error=1;
		}
		Map<String,Object> m=new HashMap<String,Object>();
		m.put("error", error);
		resultObj=JSONObject.fromObject(m);
		return "success";
	}
	public int getCityItem() {
		return cityItem;
	}

	public void setCityItem(int cityItem) {
		this.cityItem = cityItem;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getGroupItem() {
		return groupItem;
	}

	public void setGroupItem(int groupItem) {
		this.groupItem = groupItem;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getUserItem() {
		return userItem;
	}

	public void setUserItem(int userItem) {
		this.userItem = userItem;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getFromRegTime() {
		return fromRegTime;
	}

	public void setFromRegTime(String fromRegTime) {
		this.fromRegTime = fromRegTime;
	}

	public String getToRegTime() {
		return toRegTime;
	}

	public void setToRegTime(String toRegTime) {
		this.toRegTime = toRegTime;
	}

	public String getFromLoginTime() {
		return fromLoginTime;
	}

	public void setFromLoginTime(String fromLoginTime) {
		this.fromLoginTime = fromLoginTime;
	}

	public String getToLoginTime() {
		return toLoginTime;
	}

	public void setToLoginTime(String toLoginTime) {
		this.toLoginTime = toLoginTime;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SMSPlanService getSmsPlanService() {
		return smsPlanService;
	}

	public void setSmsPlanService(SMSPlanService smsPlanService) {
		this.smsPlanService = smsPlanService;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getSms_plan_id() {
		return sms_plan_id;
	}

	public void setSms_plan_id(long sms_plan_id) {
		this.sms_plan_id = sms_plan_id;
	}
	
}
