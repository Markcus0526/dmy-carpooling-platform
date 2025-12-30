package com.nmdy.activity.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import util.CommonUtil;

import com.nmdy.activity.service.ActivityService;
import com.nmdy.entity.Activity;
import com.nmdy.entity.ActivityRule;
import com.nmdy.entity.ActivitySyscoupon;
import com.nmdy.entity.Gift;
import com.nmdy.spread.loan.service.LoanService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.Constants;

public class ActivityAction extends ActionSupport{
	private long id;
	private Long ruleId;
	private ActivityService activityService;
	private LoanService loanService;
	private Activity activity;
	private ActivityRule activityRule;
	private List<Activity> list;
	private List<Gift> listGift;
	private Integer page;
	private Integer rows;
	private Integer total_size;
	//type=0:所有  1:活动中
	private int type;
	private String name;
	private Object jsonObj;
	private String giftNames;
	private String giftIds;
	
	private String ruleName;
	private String ruleStartNum;
	private String ruleEndNum;
	private String msg;
	
	private String chooseNames;
	private String chooseIds;
	private int limit_count;
	private int valid_period;
	private List<Map<String, Object>> syscoupons;
	private JSONObject resultObj; 
	private Map<String, Object> map = new HashMap<String,Object>();
	private String result;
	
	public String main(){
		return "main";
	}
	
	/**
	 * 活动添加页面
	 * @return
	 */
	public String add(){
		giftNames = String.valueOf(ActionContext.getContext().getSession().get("giftNames"));
		giftIds = String.valueOf(ActionContext.getContext().getSession().get("giftIds"));
		listGift = new ArrayList<Gift>();
		if(StringUtils.isNotEmpty(giftNames)){
			if(StringUtils.isEmpty(giftIds)){
				ActionContext.getContext().put("msg", Constants.ADD_EMPTY_ERROR);
				return "add";
			}
			String[] names = giftNames.split(",");
			String[] ids = giftIds.split(",");
			if(names.length==ids.length){
				for(int i=0;i<names.length;i++){
					Gift g = new Gift();
					g.setId(ids[i]);
					g.setName(names[i]);
					listGift.add(g);
				}
			}else{
				ActionContext.getContext().put("msg", Constants.ADD_ERROR);
				return "add";
			}
		}
		return "add";
	}
	
	/**
	 * 搜索页面
	 * @return
	 */
	public String search(){
		Map<String, Object> map = new HashMap<String, Object>();	//search conditions
		map.put("page", (page-1)*rows);
		map.put("rows", rows);
		map.put("id", id);
		map.put("type", type);
		map.put("name", name);
		list = activityService.listALl(map);
		total_size = activityService.countListALl(map);
		try {
			makeJSON();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "search";
		
	}
	
	public String listSessionActivityRole(){
		String ruleNames = String.valueOf(ActionContext.getContext().getSession().get("ruleNames"));
		String ruleStartNums = String.valueOf(ActionContext.getContext().getSession().get("ruleStartNums"));
		String ruleEndNums = String.valueOf(ActionContext.getContext().getSession().get("ruleEndNums"));
		if(StringUtils.isEmpty(ruleNames)||StringUtils.isEmpty(ruleStartNums)||StringUtils.isEmpty(ruleEndNums)){
			jsonObj = null;
			return "listActivityRole";
		}
		List<Map<String,Object>> listrule = new ArrayList<>();
		String[] names = ruleNames.split(",");
		String[] startNums = ruleStartNums.split(",");
		String[] endNums = ruleEndNums.split(",");
		if(names.length!=startNums.length||startNums.length!=endNums.length||"null".equals(names[0])){
			CommonUtil.removeSessionRule();
			jsonObj = null;
			return "listActivityRole";
		}
		for(int i=0;i<names.length;i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("operation",String.format("<a href='javascript:delRule(\"%s\",\"%s\",\"%s\");'>移除</a>", names[i],startNums[i],endNums[i]));
			map.put("ruleNum", "第"+startNums[i]+"-"+endNums[i]+"名");
			map.put("gift", names[i]);
			listrule.add(map);
		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("rows",listrule);
		jsonObj = json;
		return "listActivityRole";
	}
	
	
	public String listActivityRole(){
		List<Map<String,Object>> listrule = new ArrayList<>();
		List<ActivityRule> listAr=  new ArrayList<>();
		listAr =activityService.listActivityRole(id);
		for(int i=0;i<listAr.size();i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("operation",String.format("<a href='javascript:delRule(%d);'>移除</a>", listAr.get(i).getId()));
			map.put("ruleNum", "第"+listAr.get(i).getStartNum()+"-"+listAr.get(i).getEndNum()+"名");
			map.put("gift", listAr.get(i).getSyscouponCode());
			listrule.add(map);
		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("rows",listrule);
		jsonObj = json;
		return "listActivityRole";
	}
	
	private void makeJSON() throws ParseException {
		if (list == null) {
			jsonObj = null;
			return;
		}
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Activity info : list) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", info.getId());
			String operate = ""; 
			if(info.getStatus()==0){
				operate += String.format("<a href='javascript:editView(%d);'>修改</a>|", info.getId());
				operate += String.format("<a href='javascript:stopView(%d);'>停止</a>", info.getId());
				m.put("status", "活动中");
			}else{
				operate += String.format("<a href='javascript:showView(%d);'>查看</a>", info.getId());
				m.put("status", "已停止");
			}
			m.put("operation", operate);
			m.put("id", info.getId());
			m.put("name", info.getName());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			m.put("startEndTime", sdf.format(info.getStartTime())+"至"+sdf.format(info.getEndTime()));
			m.put("joinPeople", info.getJoinPeople());
			m.put("giveGiftNum", info.getGiveGiftNum());
			al.add(m);
		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total_size);
		json.put("rows", al);
		jsonObj = JSONObject.fromObject(json);
	}
	
	private void makeJSON(List<ActivityRule> listrule){
		if (listrule == null) {
			jsonObj = null;
			return;
		}
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (ActivityRule info : listrule) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", info.getId());
			String operate = ""; 
			operate = String.format("<a href='javascript:delActivityRule(%d);'>移除</a>", info.getId());
			m.put("operation", operate);
			m.put("id", info.getId());
			m.put("betweenNum,", "第"+info.getStartNum()+"-"+info.getEndNum()+"名");
			m.put("gift",info.getSyscouponCode()+":"+info.getNum()+"张！已使用"+info.getUsedNum()+"张!");
			al.add(m);
		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("rows", al);
		jsonObj = JSONObject.fromObject(json);
	}
	
	public String showGift(){
		return "showGIft";
	}
	
	/**
	 * 选择点卷
	 * @return
	 */
	public String choseGift(){
		ActionContext.getContext().getSession().put("giftNames", giftNames);
		ActionContext.getContext().getSession().put("giftIds", giftIds);
		return "choseGift";
	}
	
	/**
	 * 修改点卷时选择点卷
	 * @return
	 */
	public String choseGiftForUpdate(){
		this.setId(id);
		this.setChooseIds(giftIds);
		this.setChooseNames(giftNames);
		return "choseGiftForUpdate";
	}
	
	/**
	 * 添加SESSION规则
	 * @return
	 */
	public String insertRule(){
		String ruleNames = String.valueOf(ActionContext.getContext().getSession().get("ruleNames"));
		String ruleStartNums = String.valueOf(ActionContext.getContext().getSession().get("ruleStartNums"));
		String ruleEndNums = String.valueOf(ActionContext.getContext().getSession().get("ruleEndNums"));
	
		if(StringUtils.isEmpty(ruleNames)||"null".equals(ruleNames)){
			ActionContext.getContext().getSession().put("ruleNames", ruleName+",");
			ActionContext.getContext().getSession().put("ruleStartNums", ruleStartNum+",");
			ActionContext.getContext().getSession().put("ruleEndNums", ruleEndNum+",");
		}else{
			giftNames = String.valueOf(ActionContext.getContext().getSession().get("giftNames"));
			if(StringUtils.isEmpty(ruleNames)){
				ActionContext.getContext().put("msg", Constants.ADD_RULE_ERROR);
				return "add";
			}
			if(giftNames.split(",").length<=ruleNames.split(",").length){
				ActionContext.getContext().put("msg", Constants.ADD_RULE_GIFT_ERROR);
				return "add";
			}
			ruleNames = ruleNames.replaceAll("null", "")+ruleName+",";
			ruleStartNums = ruleStartNums.replaceAll("null", "")+ruleStartNum+",";
			ruleEndNums = ruleEndNums.replaceAll("null", "")+ruleEndNum+",";
			ActionContext.getContext().getSession().put("ruleNames",ruleNames);
			ActionContext.getContext().getSession().put("ruleStartNums",ruleStartNums);
			ActionContext.getContext().getSession().put("ruleEndNums",ruleEndNums);
		}
		return "insertRule";
	}
	
	/**
	 * 添加规则
	 * @return
	 */
	public String insertActivityRule(){
		this.activityService.insertActivityRule(activityRule);
		this.setId(id);
		return "insertActivityRule";
	}
	
	/**
	 * session删除规则
	 * @return
	 */
	public String removeRule(){
		String ruleNames = String.valueOf(ActionContext.getContext().getSession().get("ruleNames"));
		String ruleStartNums = String.valueOf(ActionContext.getContext().getSession().get("ruleStartNums"));
		String ruleEndNums = String.valueOf(ActionContext.getContext().getSession().get("ruleEndNums"));
		if(StringUtils.isEmpty(ruleNames)||StringUtils.isEmpty(ruleStartNums)||StringUtils.isEmpty(ruleEndNums)){
			return "removeRule";
		}
		if((ruleName+",").equals(ruleNames.trim())){
			CommonUtil.removeSessionRule();
		}else{
			String[] names = ruleNames.split(",");
			String[] startNums = ruleStartNums.split(",");
			String[] endNums = ruleEndNums.split(",");
			if(names.length!=startNums.length||startNums.length!=endNums.length){
				CommonUtil.removeSessionRule();
			}else{
				ActionContext.getContext().getSession().put("ruleNames", ruleNames.replaceFirst(ruleName+",",""));
				ActionContext.getContext().getSession().put("ruleStartNums", ruleStartNums.replaceFirst(ruleStartNum+",",""));
				ActionContext.getContext().getSession().put("ruleEndNums", ruleEndNums.replaceFirst(ruleEndNum+",",""));
			}
		}
		return "removeRule";
	}
	
	/**
	 * 删除规则
	 */
	public String delRule(){
		this.activityService.deleteActivityRule(ruleId);
		return "delRule";
	}
	
	/**
	 * 添加活动
	 * @return
	 */
	public String insertActivity(){
		if(StringUtils.isEmpty(activity.getName())||activity.getStartTime()==null||activity.getShareTime()==0||activity.getSharePeople()==0){
			msg=Constants.ADD_ACTIVITY_EMPTY_ERROR;
			return "insertActivityerror";
		}
		if(activity.getPrizeType()==1){
			this.activityService.insertTipActivity(activity);
		}else{
			int j = this.activityService.insertActivity(activity);
			if(j==0){
				msg=Constants.ADD_ACTIVITY_ERROR;
				return "insertActivityerror";
			}
			CommonUtil.removeSessionGift();
		}
		return "insertActivity";
	}
	
	/**
	 * 停止活动
	 * @return
	 */
	public String stopActivity(){
		this.activityService.udpateActivityStop(id);
		return "stopActivity";
	}
	
	/**
	 * 查看活动
	 */
	public String showActivity(){
		giftNames = "";
		activity = this.activityService.findById(id);
		if(activity.getListActivitySyscoupon()!=null){
			List<ActivitySyscoupon> list = activity.getListActivitySyscoupon();
			for(ActivitySyscoupon as:list){
				giftNames += as.getSyscouponCode()+",";
			}
		}
		return "showActivity";
	}
	
	/**
	 * 修改活动页面
	 * @return
	 */
	public String editActivity(){
		giftNames = "";
		activity = this.activityService.findById(id);
		if(activity.getListActivitySyscoupon()!=null){
			List<ActivitySyscoupon> list = activity.getListActivitySyscoupon();
			for(ActivitySyscoupon as:list){
				giftNames += as.getSyscouponCode()+",";
			}
		}
		this.setChooseIds(chooseIds);
		this.setChooseNames(chooseNames);
		return "editActivity";
	}
	
	/**
	 *  修改活动
	 * @return
	 */
	public String updateActivity(){
		if(StringUtils.isEmpty(activity.getName())||activity.getStartTime()==null||activity.getShareTime()==0||activity.getSharePeople()==0){
			msg=Constants.ADD_ACTIVITY_EMPTY_ERROR;
			return "updateActivityError";
		}
		if(activity.getPrizeType()==0){
			List<ActivityRule> listAr=  new ArrayList<>();
			listAr =activityService.listActivityRole(id);
			if(listAr.size()==0){
				msg = Constants.UPDATE_ACTIVITY_ERROR;
				return "updateActivityError";
			}
			String[] names = giftNames.split(",");
			if(names.length!=listAr.size()){
				msg = Constants.UPDATE_ACTIVITY1_ERROR;
				return "updateActivityError";
			}
		}
		activity.setId(id);
		activityService.updateActivity(activity);
		this.setId(id);
		return "updateActivity";
	}
	
	public String searchGift() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();	//search conditions
			map.put("start", (page-1)*rows);
			map.put("limit", rows);
			map.put("isenabled", 1); 
			if(id!=0){
				map.put("id", id); 
			}
			if(valid_period!=0){
				map.put("valid_period", valid_period); 
			}
			if(limit_count!=0){
				map.put("limit_count", limit_count); 
			}
			int total=loanService.getSys_couponCountbyCondition(map);
			syscoupons = activityService.findPagination(map);
			
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("total", total);
			json.put("rows",syscoupons );
			resultObj = JSONObject.fromObject(json);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**get set**/
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ActivityService getActivityService() {
		return activityService;
	}
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public List<Activity> getList() {
		return list;
	}
	public void setList(List<Activity> list) {
		this.list = list;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getTotal_size() {
		return total_size;
	}
	public void setTotal_size(Integer total_size) {
		this.total_size = total_size;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(Object jsonObj) {
		this.jsonObj = jsonObj;
	}

	public String getGiftNames() {
		return giftNames;
	}

	public void setGiftNames(String giftNames) {
		this.giftNames = giftNames;
	}

	public String getGiftIds() {
		return giftIds;
	}

	public void setGiftIds(String giftIds) {
		this.giftIds = giftIds;
	}

	public List<Gift> getListGift() {
		return listGift;
	}

	public void setListGift(List<Gift> listGift) {
		this.listGift = listGift;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleStartNum() {
		return ruleStartNum;
	}

	public void setRuleStartNum(String ruleStartNum) {
		this.ruleStartNum = ruleStartNum;
	}

	public String getRuleEndNum() {
		return ruleEndNum;
	}

	public void setRuleEndNum(String ruleEndNum) {
		this.ruleEndNum = ruleEndNum;
	}
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public String getChooseNames() {
		return chooseNames;
	}

	public void setChooseNames(String chooseNames) {
		this.chooseNames = chooseNames;
	}

	public String getChooseIds() {
		return chooseIds;
	}

	public void setChooseIds(String chooseIds) {
		this.chooseIds = chooseIds;
	}

	public ActivityRule getActivityRule() {
		return activityRule;
	}

	public void setActivityRule(ActivityRule activityRule) {
		this.activityRule = activityRule;
	}

	public LoanService getLoanService() {
		return loanService;
	}

	public void setLoanService(LoanService loanService) {
		this.loanService = loanService;
	}

	public int getLimit_count() {
		return limit_count;
	}

	public void setLimit_count(int limit_count) {
		this.limit_count = limit_count;
	}

	public int getValid_period() {
		return valid_period;
	}

	public void setValid_period(int valid_period) {
		this.valid_period = valid_period;
	}

	public List<Map<String, Object>> getSyscoupons() {
		return syscoupons;
	}

	public void setSyscoupons(List<Map<String, Object>> syscoupons) {
		this.syscoupons = syscoupons;
	}

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
