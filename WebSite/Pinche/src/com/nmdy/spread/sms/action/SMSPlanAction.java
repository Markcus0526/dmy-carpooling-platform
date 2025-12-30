package com.nmdy.spread.sms.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.nmdy.common.Common;
import com.nmdy.common.Constants;
import com.nmdy.common.Errors;
import com.nmdy.common.WebUtil;
import com.nmdy.spread.sms.dao.SMSPlan;
import com.nmdy.spread.sms.dao.SMSUserInfo;
import com.nmdy.spread.sms.service.SMSPlanServiceImpl;

public class SMSPlanAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	// for pagination
	private int rows;
	private int page;

	// for search plan
	private Timestamp beginTime;
	private Timestamp endTime;
	private int statusType;
	private String sendMode1;
	private String sendMode2;
	// for add plan
	// private String phoneList;
	private String idList;
	private String msg;
	private String single_time;
	private String desc;
	private String remark;
	private String limit_times;
	private String regular_send_mode;
	private int time1=0;
	private int time2=0;
	private String client_num;
	private String planPrice;
	private String smsPrice;
	private String smsMaxLen;
	private String isenabled;
	private int cityItem;
	private String cityName;
	private int groupItem;
	private String groupName;
	private String userItem;
	private String userInfo;
	private int userType=0;
	private String fromRegTime;
	private String toRegTime;
	private String fromLoginTime;
	private String toLoginTime;
	// for editing plan info
	private String isAdd;
	private int regularSendMode=0;
	private int limitTimes=0;
	private String sendTime;
	private float  price4sms=0.0f;
	private String  smsContent;
	private String phoneList;
	private int selUserCnt=0;
	private int clientNum=0;
	private long id ;
	private long sms_plan_id;
	// for getting SMS users
	private String plan_code = null;

	private SMSPlanServiceImpl smsPlanService;
	private JSONObject resultObj = null;

	/*
	 * 首页面的跳转方法
	 */
	public String index() {
		return SUCCESS;
	}

	/*
	 * 添加新的短信推广页面的跳转方法
	 */
	public String addPage() {
		double price = smsPlanService.getSMSPrice();
		ActionContext.getContext().put("price", price);
		return "addPage";
	}
	/*
	 * 新建短信推广计划 的 新建 功能的方法
	 */
    public String add(){
    	int err_code=0;
    	try{
    	Date date =new Date();
    	String createTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
      	Map<String ,Object> params=new HashMap<String,Object>();
    	params.put("plan_code", 0);
    	params.put("client_num", selUserCnt);
    	params.put("msg", smsContent);
    	params.put("price", smsPrice);
    	if(sendTime!=null &&!"".equals(sendTime)){
    	params.put("send_mode", 1);
    	params.put("has_send_times", 1);
    	params.put("single_time" , sendTime);
    	}else{
    		params.put("send_mode", 2);
    		params.put("has_send_times", 1);
    		params.put("single_time" , null);
    	}
    	params.put("regular_send_mode", regularSendMode);
    	params.put("limit_times" , limitTimes);
    	params.put("time1", time1);
    	params.put("time2", time2);
    	params.put("isenabled", 1);
    	params.put("remark", desc);

    	params.put("create_time", createTime);
    	smsPlanService.addPlan(params);
    	long id =smsPlanService.findId();
  
    	
    	String[] userids=idList.split(",");
    	for(String userid:userids){
    		if(userid!=null&&!userid.equals("")){
    			params.put("sms_plan_id", id);
        		params.put("user_id", userid);
        		String phone=smsPlanService.getPhoneById(Long.parseLong(userid));
        		params.put("phone", phone);
        		smsPlanService.addSMSUser(params);	
    		}else{
    		
    		}
    		params.clear();
    	}	
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       	if(phoneList!=null&&!phoneList.equals("")){
    		String[] phones=phoneList.split("[，|\\,|\\\n]");
         	for(String phone:phones){
        		params.put("sms_plan_id", id);
        		params.put("phone", phone);
        		params.put("user_id", 0);
        		smsPlanService.addSMSUser(params);	
        		params.put("price", smsPrice);
        		String time=sdf.format(new Date()); 
        		params.put("time", time);
        		params.put("status", 1);
        		smsPlanService.addPlanLog(params);
        		params.clear();
         	}
    	}else{
    	
     	}
		err_code=0;
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		err_code=1;
    	}
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
    	jsonMap.put("err_code", err_code);
    	resultObj=JSONObject.fromObject(jsonMap);
    	return "success";
    }
	
    /*
     * 短信推广的 修改页面的保存功能的方法
     */
    public String edit(){
    	Map<String,Object> params=new HashMap<String,Object>();

    	String[] phones = phoneList.split(",");
    	for(int i=1;i<phones.length;i++){
    		if( phones[i]!=null&&!phones[i].equals("")){
    			params.put("sms_plan_id", sms_plan_id);
        		params.put("phone", phones[i]);
        		params.put("user_id", 0);
        		smsPlanService.addSMSUser(params);
        		smsPlanService.updateSMSPlan(sms_plan_id);
    		}
    	
    		params.clear();
    	}
    	params.put("id", sms_plan_id);
    	params.put("msg", (smsContent==null)?"":smsContent);
    	params.put("remark", (desc==null)?"":desc);
    	smsPlanService.updatePlan(params);
    	
    	return "success";
    } 
    /*
	 * 首页面的 查询方法
	 */
    public String findSmsPlanList() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			Map<String, Object> searchParam = new HashMap<String, Object>();
			if (beginTime != null) {
				searchParam.put("beginTime", beginTime);
			}

			if (endTime != null) {
				searchParam.put("endTime", endTime);
			}
			if (sendMode1 != null) {
				searchParam.put("sendMode1", sendMode1);
			}
			if (sendMode2 != null) {
				searchParam.put("sendMode2", sendMode2);
			}
			searchParam.put("status", statusType);
			searchParam.put("start", (page - 1) * rows);
			searchParam.put("limit", rows);
			List<Map<String, Object>> planList = smsPlanService
					.searchPlan(searchParam);
			int totalSize = planList.size();
			if (totalSize == 0) {
				jsonMap.put("total", 1);
			} else {
				jsonMap.put("total", totalSize);
			}
			jsonMap.put("rows", planList);
			resultObj = JSONObject.fromObject(jsonMap);

		} catch (Exception e) {
			e.printStackTrace();
			resultObj = null;
			return ERROR;
		}

		return SUCCESS;
	}

	/*
	 * 添加新短信推广 的 页面 中的查询用户的方法
	 */
	public String findUserList() {
		Map<String, Object> json = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		// 城市查询条件

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
		int total = smsPlanService.getUserCountByCondition(params);
		// 查询结果从第0条开始，查询10条记录
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		List<Map<String, Object>> smsUserList = smsPlanService.searchSMSUsers(params);
		if (total == 0) {
			json.put("total", 1);

		} else {
			json.put("total", total);

		}
		json.put("rows", smsUserList);
		List<Map<String, Object>> idlists=smsPlanService.searchSMSId(params);
		String idlist="";
		for(Map<String,Object> id:idlists){
			idlist=idlist+id.get("id");
			idlist+=",";
		}
		json.put("idlist", idlist);

		resultObj = JSONObject.fromObject(json);
		return SUCCESS;
	}
	/*
	 *获取短信推广用户的 详细信息
	 */
	public String findSMSUserList(){
		Map<String, Object> json = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		int total= smsPlanService.findCountUser(params);
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		List<Map<String, Object>> smsUserList=smsPlanService.findSMSUser(params);
		int fcnt=0;
		int scnt=0;
		List<Map<String, Object>> resultList=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String ,Object>();
		for(Map<String,Object> smsUser:smsUserList){
			Long userid = (Long) smsUser.get("id");
			map.put("userid", userid);
			map.put("id", id);
			scnt=smsPlanService.findSCNT(map);
			fcnt=smsPlanService.findFCNT(map);
			smsUser.put("scnt", scnt);
			smsUser.put("fcnt", fcnt);
			resultList.add(smsUser);
		}
		if(total==0){
			json.put("total", 1);
			json.put("rows", resultList);
		}else{
			json.put("total", total);
			json.put("rows", resultList);
		}
		resultObj=JSONObject.fromObject(json);
		return "success";
	}
 
	/* 
     * 短信推广的首页面 的 查询 计划的详细信息
     
	public String getPlanInfo() {
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;

		try {
			// SMSPlan plan = smsPlanService.findOneByCode(planCode);
			Map<String, Object> smsPlanMap = smsPlanService
					.findOneByCode(plan_code);

			if (smsPlanMap == null) {
				err = Errors.ERR_SQL_ERROR;
			} else {
				json.put("plan_code", smsPlanMap.get("plan_code"));
				json.put("client_num", smsPlanMap.get("client_num"));
				json.put("send_mode", smsPlanMap.get("send_mode"));
				json.put(
						"single_time",
						smsPlanMap.get("single_time").equals(
								Common._strToDate(Constants.DEFALUT_TIME)) ? ""
								: smsPlanMap.get("single_time"));
				json.put("has_send_times", smsPlanMap.get("has_send_times"));
				json.put("reqular_send_mode",
						smsPlanMap.get("reqular_send_mode"));
				json.put("time1", smsPlanMap.get("planCode"));
				json.put("time2", smsPlanMap.get("planCode"));
				json.put("status", smsPlanMap.get("planCode"));
				json.put("create_time", smsPlanMap.get("create_time"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		// WebUtil._setResponseContentType("application/json");
		resultObj = JSONObject.fromObject(json);
		return SUCCESS;
	}
*/

	
	/*
    * 查看页面 和修改页面的 显示数据的查询方法
    */
	public String show(){
		List<Map<String,Object>> phoneLists=smsPlanService.getPhones(id);
		Map<String ,Object> params=smsPlanService.show(id);
		int list=phoneLists.size();
		if(list==1){
			params.put("phoneList", phoneLists.get(0).get("phone"));
		}else if(list>1){
			params.put("phoneList", phoneLists.get(0).get("phone")+"...."+phoneLists.get(list-1).get("phone"));
		}
	
		Map<String ,Object> map=new HashMap<String,Object>();
		map.put("id", "");
		map.put("id", id);
		int successnumber=smsPlanService.findSCNT(map);
		int failnumber=smsPlanService.findFCNT(map);
		params.put("successnumber", successnumber);
		params.put("failnumber", failnumber);
		resultObj=JSONObject.fromObject(params);
		return "success";
	}

	/*
	 * getter 和 setter 方法
	 */
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public SMSPlanServiceImpl getSmsPlanService() {
		return smsPlanService;
	}

	public void setSmsPlanService(SMSPlanServiceImpl smsPlanService) {
		this.smsPlanService = smsPlanService;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public int getStatusType() {
		return statusType;
	}

	public void setStatusType(int statusType) {
		this.statusType = statusType;
	}

	// public String getPhoneList() {
	// return phoneList;
	// }
	//
	// public void setPhoneList(String phoneList) {
	// this.phoneList = phoneList;
	// }

	public String getSendMode1() {
		return sendMode1;
	}

	public void setSendMode1(String sendMode1) {
		this.sendMode1 = sendMode1;
	}

	public String getSendMode2() {
		return sendMode2;
	}

	public void setSendMode2(String sendMode2) {
		this.sendMode2 = sendMode2;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSingle_time() {
		return single_time;
	}

	public void setSingle_time(String single_time) {
		this.single_time = single_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLimit_times() {
		return limit_times;
	}

	public void setLimit_times(String limit_times) {
		this.limit_times = limit_times;
	}

	public String getRegular_send_mode() {
		return regular_send_mode;
	}

	public void setRegular_send_mode(String regular_send_mode) {
		this.regular_send_mode = regular_send_mode;
	}

	public String getClient_num() {
		return client_num;
	}

	public void setClient_num(String client_num) {
		this.client_num = client_num;
	}

	public String getIsenabled() {
		return isenabled;
	}

	public void setIsenabled(String isenabled) {
		this.isenabled = isenabled;
	}

	public String getPlan_code() {
		return plan_code;
	}

	public void setPlan_code(String plan_code) {
		this.plan_code = plan_code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSmsPrice() {
		return smsPrice;
	}

	public void setSmsPrice(String smsPrice) {
		this.smsPrice = smsPrice;
	}

	public String getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(String planPrice) {
		this.planPrice = planPrice;
	}

	public String getIdList() {
		return idList;
	}

	public void setIdList(String idList) {
		this.idList = idList;
	}

	public String getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(String isAdd) {
		this.isAdd = isAdd;
	}

	public String getSmsMaxLen() {
		return smsMaxLen;
	}

	public void setSmsMaxLen(String smsMaxLen) {
		this.smsMaxLen = smsMaxLen;
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

	public String getUserItem() {
		return userItem;
	}

	public void setUserItem(String userItem) {
		this.userItem = userItem;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
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

	public int getRegularSendMode() {
		return regularSendMode;
	}

	public void setRegularSendMode(int regularSendMode) {
		this.regularSendMode = regularSendMode;
	}


	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}


	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(String phoneList) {
		this.phoneList = phoneList;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getTime1() {
		return time1;
	}

	public void setTime1(int time1) {
		this.time1 = time1;
	}

	public int getTime2() {
		return time2;
	}

	public void setTime2(int time2) {
		this.time2 = time2;
	}

	public int getLimitTimes() {
		return limitTimes;
	}

	public void setLimitTimes(int limitTimes) {
		this.limitTimes = limitTimes;
	}

	public float getPrice4sms() {
		return price4sms;
	}

	public void setPrice4sms(float price4sms) {
		this.price4sms = price4sms;
	}

	public int getSelUserCnt() {
		return selUserCnt;
	}

	public void setSelUserCnt(int selUserCnt) {
		this.selUserCnt = selUserCnt;
	}

	public int getClientNum() {
		return clientNum;
	}

	public void setClientNum(int clientNum) {
		this.clientNum = clientNum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSms_plan_id() {
		return sms_plan_id;
	}

	public void setSms_plan_id(long sms_plan_id) {
		this.sms_plan_id = sms_plan_id;
	}

}
