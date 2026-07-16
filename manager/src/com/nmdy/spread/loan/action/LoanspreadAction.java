package com.nmdy.spread.loan.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.nmdy.common.Common;
import com.nmdy.common.Constants;
import com.nmdy.common.UserLoginInfo;
import com.nmdy.common.WebUtil;
import com.nmdy.spread.loan.dao.City;
import com.nmdy.spread.loan.dao.CouponSendLog;
import com.nmdy.spread.loan.dao.SingleCoupon;
import com.nmdy.spread.loan.dao.Syscoupon;
import com.nmdy.spread.loan.dao.Syscoupon1;
import com.nmdy.spread.loan.service.LoanService;
import com.sun.org.apache.xpath.internal.operations.And;

public class LoanspreadAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	private LoanService loanService;


	private JSONObject resultObj = null;
	
	//pagination
	private int page;
	private int rows;
	private int total_size;


	private String conditionSelect="0";
	
	private String irelease_channel;//发放平台
	private String bnocondcash="";//无条件绿点
	private String bcondcash="";//有条件绿点
	private String bgood="";//实物
	private String syscoupon_code;//点券编号
	private String pointNumber;//点券数额
	private long id;//点券id
	private int valid_period=0;//点券有效期
	private String valid_period_unit;//点券有效期单位;
	private String valid_period_page;
	private String userIdList;
	private String selectAll;
	private int num;
	private String remark;
	private String idList;
	private String msg;

	//-------查找用户--------
	// for search
	private String cityItemSelect;//城市 条件 选择
	private String cityItemInput; //城市输入
	private String groupItemSelect;//集团条件选择 
	private String groupItemInput; //集团输入     
	private String userItemSelect; //用户条件 选择 
	private String userItemInput;  //用户输入     
	private String fromRegTime;   //注册日期条件 起始
	private String toRegTime;     //注册日期条件结束    
	private String fromLoginTime; //最后登录时间起始
	private String toLoginTime;   //最后登录时间结束 
	private String userType;//用户身份
	private String sysCouponCode;
	private int applySource;
	private int releaseChannel;
	private int goodsOrCash;
	private String appSpreadUnitId;
	private String sumOrGoodsname;
	private int couponType;
	private int limit_val=0;
	private int validPeriodUnit;
	private int validPeriod=0;
	private Syscoupon newCoupon;
	private Syscoupon disableSyscoupon;
	private Syscoupon syscoupon;
	private List<Map<String, Object>> syscoupons;
	private CouponSendLog newSendLog;
	private CouponSendLog viewSendLog;
	private List<CouponSendLog> sendLogs;
	private static List<Map<String, Object>> userInfo ;
	//private String id;
	private int limit_count=0;
	private int vp;
	private int lc;
	
	public LoanService getLoanService() {
		return loanService;
	}

	public void setLoanService(LoanService loanService) {
		this.loanService = loanService;
	}
	/*
	 * 点券推广 首页面的跳转方法
	 */
	public String index() {
		return SUCCESS;
	}
	/*
	 * 点券推广管理的发送点券页面
	 */
	public String deploy(){
		 String[] ids;
			Map<String,Object> mapp=new HashMap<String,Object>();
	try{
		if(selectAll!=null){
			//向sigle_coupon表里面插入数据
			Syscoupon1 syscoupon=loanService.findSyscouponById(id);
			String[] userids=idList.split(",");
			for(String userid:userids){
				SingleCoupon singleCoupon = new SingleCoupon();
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("syscoupon_id", id);
				singleCoupon.setSyscouponId(id);
				params.put("userid",userid);
				singleCoupon.setUserid(Long.parseLong(userid));
				Date date =new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				params.put("ps_date", sdf.format(date));
				singleCoupon.setPsDate(sdf.format(date));
				if(syscoupon.getValidPeriodUnit()==1){
					 String time= getTime(1,syscoupon.getValidPeriod(),date);
					 params.put("date_expired", time);
					 singleCoupon.setDateExpired(time);
				}if(syscoupon.getValidPeriodUnit()==2){
					 String time= getTime(2,syscoupon.getValidPeriod(),date);
					 params.put("date_expired", time);
					 singleCoupon.setDateExpired(time);
				}if(syscoupon.getValidPeriodUnit()==3){
					 String time= getTime(3,syscoupon.getValidPeriod(),date);
					 params.put("date_expired", time);
					 singleCoupon.setDateExpired(time);
				}if(syscoupon.getValidPeriodUnit()==4){
					 String time= getTime(4,syscoupon.getValidPeriod(),date);
					 params.put("date_expired", time);
					 singleCoupon.setDateExpired(time);
				}
				singleCoupon.setSum(syscoupon.getSum());
				SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMddhhmmssSSS");
				params.put("coupon_code", "SICP"+sdf1.format(date));
				singleCoupon.setSingleCouponCode("SICP"+sdf1.format(date));
				System.out.println(params);
				loanService.insertSingleCoupon(singleCoupon);
				Map<String,Object> mm=new HashMap<String,Object>();
				
				//向notify_persion表里插入数据
				Map<String,Object> mm1=new HashMap<String,Object>();
				mm1.put("msg", msg);
				mm1.put("id", id);
				mm1.put("reciver", userid);
				Date date1=new Date();
				mm1.put("ps_date", sdf.format(date1));
				loanService.insertNotifyPersion(mm1);
			 }
			//向 coupon_send_log 表里插入数据
			Map<String,Object> mm=new HashMap<String,Object>();
			mm.put("msg", msg);
			mm.put("num", num);
			mm.put("remark", remark);
			mm.put("coupon_code", syscoupon.getSysCouponCode());
			mm.put("operator", 1);
			mm.put("send_type", 5);
			Date date2=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mm.put("ps_date", sdf.format(date2));
			loanService.insertCouponSendLog(mm);
			}else{
			Syscoupon1 syscoupon=loanService.findSyscouponById(id);
			  ids=idList.split(",");
				for(String userid:ids){
				SingleCoupon singleCoupon = new SingleCoupon();
				singleCoupon.setSyscouponId(id);
				singleCoupon.setUserid(Integer.parseInt(userid));
				Date date =new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				singleCoupon.setPsDate(sdf.format(date));
				if(syscoupon.getValidPeriodUnit()==1){
					 String time= getTime(1,syscoupon.getValidPeriod(),date);
					 singleCoupon.setDateExpired(time);
				}if(syscoupon.getValidPeriodUnit()==2){
					 String time= getTime(2,syscoupon.getValidPeriod(),date);
					 singleCoupon.setDateExpired(time);
				}if(syscoupon.getValidPeriodUnit()==3){
					 String time= getTime(3,syscoupon.getValidPeriod(),date);
					 singleCoupon.setDateExpired(time);
				}if(syscoupon.getValidPeriodUnit()==4){
					 String time= getTime(4,syscoupon.getValidPeriod(),date);
					 singleCoupon.setDateExpired(time);
				}
				singleCoupon.setSum(syscoupon.getSum());
				SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMddhhmmssSSS");
				singleCoupon.setSingleCouponCode("SICP"+sdf1.format(date));
				loanService.insertSingleCoupon(singleCoupon);
				//向notify_persion表里插入数据
				Map<String,Object> mm=new HashMap<String,Object>();
				mm.put("msg", msg);
				mm.put("id", id);
				mm.put("reciver", Integer.parseInt(userid));
				Date date1=new Date();
				mm.put("ps_date", sdf.format(date1));
				loanService.insertNotifyPersion(mm);
				mm.clear();
	
			}
				//"id":id,"msg":msg,"idList":idList,"num":num,"remark":remark,"selectAll":selectAll
				//向 coupon_send_log 表里插入数据
				Map<String,Object> mm=new HashMap<String,Object>();
				mm.put("msg", msg);
				mm.put("num", num);
				mm.put("remark", remark);
				mm.put("coupon_code", syscoupon.getSysCouponCode());
				mm.put("operator", 1);
				mm.put("send_type", 5);
				Date date2=new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				mm.put("ps_date", sdf.format(date2));
				loanService.insertCouponSendLog(mm);

				mapp.put("massage", "ok");
			}
	}catch(Exception e){
		e.printStackTrace();
       mapp.put("massage", "error");
	}

		resultObj = JSONObject.fromObject(mapp);
		return "success";
	}
	/*
	 * 获取有效期限的方法
	 */
	private String getTime(int data, int validPeriod,Date date) {
		String time="";
		 Calendar c = Calendar.getInstance();         
		 c.setTime(date);           
		 int dDay   =  c.get(Calendar.DATE);     
		 int dYear  =  c.get(Calendar.YEAR);      
		 int dMonth =  c.get(Calendar.MONTH) + 1;  //注意month是从0开始计的我们加1    
		 int days=0;             int[] months={0,31,28,31,30,31,30,31,31,30,31,30,31};     
		 if(dYear%400==0||dYear%4==0&&dYear%100!=0){       
			 months[2]=29;           
			 }     
		 if(data==1){
		 int day=dDay+validPeriod;
		 int month=dMonth;
		 int year=dYear;
		 if(day>months[dMonth]){
			 day=day-months[dMonth];
			 month=dMonth+1;
		 }if(month>12){
			 month=month-1;
			 year=dYear+1;
		 }	
		  time=year+"-"+month+"-"+day;
		 } if(data==2){
			 int day=dDay+validPeriod*7;
			 int month=dMonth;
			 int year=dYear;
			 if(day>months[dMonth]){
				 day=day-months[dMonth];
				 month=dMonth+1;
			 }if(month>12){
				 month=month-12;
				 year=dYear+1;
			 } 
			  time=year+"-"+month+"-"+day;
		 }if(data==2){
			 int day=dDay;
			 int year=dYear;
			 int month=dMonth+validPeriod;
			 if(month>12){
				 month=month-12;
				 year=dYear+1;
			 } 
			  time=year+"-"+month+"-"+day;
		 }if(data==2){
			 int day=dDay;
			 int month=dMonth;
			 int year=dYear+validPeriod;
			 if(month>12){
				 month=month-12;
				 year=dYear+1;
			 } 
			  time=year+"-"+month+"-"+day;
		 }
		
		 return time;
	}

	/*
	 * 发行渠道  irelease_channel ---	 1 : App
	 *								2: 合作单位
	 * 点券类别  :
	 *			无条件绿点   bnocondcash ---	true/false
	 *			有条件绿点   bcondcash ---	true/false
	 *			实物    		bgood ---		true/false
	 * 
	 */
	public String search() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();	//search conditions
			map.put("conditionSelect", conditionSelect);
			map.put("bnocondcash", bnocondcash);
			map.put("bcondcash", bcondcash);
			map.put("bgood", bgood);
			int total=loanService.getSys_couponCountbyCondition(map);
			map.put("start", (page-1)*rows);
			map.put("limit", rows);
			syscoupons = loanService.findPagination(map);
			
			Map<String, Object> json = new HashMap<String, Object>();
			
			if(total==0){
				json.put("total", 1);
				json.put("rows",syscoupons );
			}else{
				json.put("total", total);
				json.put("rows",syscoupons );
			}
			resultObj = JSONObject.fromObject(json);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		return SUCCESS;
	}
	
	/*
	 * 发行渠道  irelease_channel ---	 1 : App
	 *								2: 合作单位
	 * 点券类别  :
	 *			无条件绿点   bnocondcash ---	true/false
	 *			有条件绿点   bcondcash ---	true/false
	 *			实物    		bgood ---		true/false
	 * 
	 */
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
			syscoupons = loanService.findPagination(map);
			
			Map<String, Object> json = new HashMap<String, Object>();
			//**************************great***************************//
			if(total==0){
				json.put("total", 1);
				json.put("rows",syscoupons );
			}if(total==0){
				json.put("total", total);
				json.put("rows",syscoupons );
			}
	
			resultObj = JSONObject.fromObject(json);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		return SUCCESS;
	}
	
	
	/*
	 * 通过syscoupon_code查询发放记录
	 */
	public String searchSendLog() {
		/*if(page == null || rows == null) {
			return ERROR;
		}*/
		try {
			sendLogs = loanService.searchCouponSendLogs(syscoupon_code);
				
			Map<String, Object> map = new HashMap<String, Object>();	//search conditions
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			map.put("syscouponCode", syscoupon_code);
			
			total_size = sendLogs.size();
			sendLogs = loanService.searchCouponSendLogs(map);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("application/json");
		makeJSONObjFromCouponSendLogs();

		return SUCCESS;
	}
	/*
	 * searchSendLog() 子方法
	 */
	private void makeJSONObjFromCouponSendLogs() {

		if (sendLogs == null) {
			resultObj = null;
			return;
		}

		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (CouponSendLog info : sendLogs) {

			Map<String, Object> m = new HashMap<String, Object>();

			m.put("operator", info.getOperatorId());
			m.put("sendtype", info.getSendType());
			m.put("num", info.getNum());
			m.put("sendtime", info.getSendTime());
			m.put("msg", info.getMsg());
	
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		if(total_size==0){
			json.put("total", 1);
			json.put("rows", 0);
		}else{
			json.put("total", total_size);
			json.put("rows", al);
		}
	
		resultObj = JSONObject.fromObject(json);
	}
	
	
	/*
	 * 
	 */
	public String retrieveNewCouponCode() {
		String newCouponCode;
		try {
			newCouponCode = loanService.genNewCouponCode();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("application/json");
		
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("newCouponCode", newCouponCode);
		resultObj = JSONObject.fromObject(json);

		return SUCCESS;
	}

	
	
	/*
	 * 
	 */
/*	public String createSyscoupon() {
		UserLoginInfo info = (UserLoginInfo) WebUtil._session("user");
		if (info != null) {			
			WebUtil._session("menucode", "0");
			WebUtil._session("submenucode", "0");
			WebUtil._session("url", "/spread/loan/create_syscoupon_page");
			return "success";
		} else {
			return "login";
		}
	}*/
	
	/*
	 * 新建点券的跳转方法
	 */
	public String create_syscoupon_page() {
		return SUCCESS;
	}
	/*
	 * 新建点券页面的 保存功能方法
	 */
	public String doCreateSyscoupon() {
		Map<String,Object> map=new  HashMap<String,Object>();
		try {
			Syscoupon newCoupon=new Syscoupon();
			newCoupon.setSyscoupon_code(sysCouponCode);
			newCoupon.setApply_source(applySource);
			newCoupon.setRelease_channel(releaseChannel);
			newCoupon.setGoods_or_cash(goodsOrCash);
			if(appSpreadUnitId!=null&& !"".equals(appSpreadUnitId)){
			newCoupon.setApp_spread_unit_id(Long.parseLong(appSpreadUnitId));
			}else{
				newCoupon.setApp_spread_unit_id(0);
			}
			if(goodsOrCash==1){
				newCoupon.setSum(Integer.parseInt(sumOrGoodsname));
			}else{
				newCoupon.setGoods(sumOrGoodsname);
			}
			
			newCoupon.setCoupon_type(couponType);
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			newCoupon.setSc_date(sdf.format(date));
			newCoupon.setLimit_val(limit_val);
			newCoupon.setValid_period_unit(validPeriodUnit);
			newCoupon.setValid_period(validPeriod);
			loanService.insert(newCoupon);
			map.put("msg", "ok");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "error");
		}
		resultObj=JSONObject.fromObject(map);
		return SUCCESS;
	}
	
	
/*	public String manualDeploySyscoupon() {
	 
		UserLoginInfo info = (UserLoginInfo) WebUtil._session("user");
		if (info != null) {			
			WebUtil._session("menucode", "0");
			WebUtil._session("submenucode", "0");
			WebUtil._session("url", "/spread/loan/manualdeploy_syscoupon_page");
			WebUtil._session(Constants.MANUAL_DEPLOY_COUPONCODE, newSendLog.getCouponCode());
			WebUtil._session(Constants.MANUAL_DEPLOY_COUPONID, newSendLog.getSyscouponId());
			return "success";
		} else {
			return "login";
		}
	}
*/

/* public String doManualDeploySyscoupon() {
		try {
			newSendLog.setSendTime(Common.getCurrentDateTimeString());
			newSendLog.setSendType(Constants.CouponSendLogTable.SendType.MANUAL_SEND);
			newSendLog.setOperatorId(((UserLoginInfo) WebUtil._session("user")).getId());
			
			loanService.insertCouponSendLog(newSendLog);
			
			Syscoupon tSyscoupon = loanService.findOneSyscouponById(newSendLog.getSyscouponId());
			
			String[] uIdList = userIdList.split(";");
			for(int i=0; i<uIdList.length; i++) {
				SingleCoupon singleCoupon = new SingleCoupon();
				
				singleCoupon.setSingleCouponCode(loanService.genNewSingleCouponCode());
				if(tSyscoupon.getGoods_or_cash() == Constants.SyscouponTable.GoodsOrCash.CASH) {
					singleCoupon.setSum(tSyscoupon.getSum());
				}
				singleCoupon.setPsDate(Common.getCurrentDateTimeString());
				singleCoupon.setUserid(Integer.parseInt(uIdList[i]));
				singleCoupon.setIsenabled(Constants.SingleCouponTable.Enabled.ENABLED);
				singleCoupon.setSyscouponId(tSyscoupon.getId());
				singleCoupon.setIsGeneratedByActive(Constants.SingleCouponTable.IsGeneratedByActive.NO);
				
				loanService.insertSingleCoupon(singleCoupon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}*/
	

	/*
	 * 点券管理的 发放页面 的跳转页面
	 */
	public String manualdeploy_syscoupon_page() {
		ActionContext.getContext().put("id", id);
		ActionContext.getContext().put("pointNumber", pointNumber+"点");
		if (valid_period_unit!=""&"1".equals(valid_period_unit)) {
			ActionContext.getContext().put("valid_period_page", valid_period+"日");
		}
		if (valid_period_unit!=""&"2".equals(valid_period_unit)) {
			ActionContext.getContext().put("valid_period_page", valid_period+"周");
		}
		if (valid_period_unit!=""&"3".equals(valid_period_unit)) {
			ActionContext.getContext().put("valid_period_page", valid_period+"月");
		}
		if (valid_period_unit!=""&"4".equals(valid_period_unit)) {
			ActionContext.getContext().put("valid_period_page", valid_period+"年");
		}
		//ActionContext.getContext().put("valid_period_unit", valid_period_unit);
		return SUCCESS;
	}
	
/*	public String findSyscouponById(){
		return SUCCESS;
	}*/

	/*
	 * 点券推广页面的  停发点券
	 */
	public String disableSyscoupon() {
		int error;
		try{
		disableSyscoupon = loanService.findOneSyscouponById(disableSyscoupon.getId());
		disableSyscoupon.setIsenabled(Constants.SyscouponTable.Enabled.DISABLED);
		loanService.updateSyscoupon(disableSyscoupon);
		error=0;
		}catch(Exception e){
			e.printStackTrace();
			error=1;
		}
		Map<String ,Object> map = new HashMap<String,Object>();
		map.put("error", error);
		resultObj = JSONObject.fromObject(map);
		return SUCCESS;
	}

	/*public String viewSyscoupon() {
		
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		ArrayList<Map<String, Object>> infos = new ArrayList<Map<String, Object>>(); 
		
		try {
			viewSyscoupon = loanService.findOneSyscouponById(viewSyscoupon.getId());
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("sysCouponCode", viewSyscoupon.getSysCouponCode()==null?"":viewSyscoupon.getSysCouponCode());
			m.put("goodsOrCash", viewSyscoupon.getGoodsOrCash());
			m.put("validPeriod", viewSyscoupon.getValidPeriod());
			m.put("releaseChannel", viewSyscoupon.getReleaseChannel());
			m.put("couponType", viewSyscoupon.getCouponType());
			m.put("validPeriodUnit", viewSyscoupon.getValidPeriodUnit());
			infos.add(m);
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		json.put("infos", infos);
		resultObj = JSONObject.fromObject(json);
		return SUCCESS;
	}*/
	
	
	/*	public String viewCouponSendLog() {
		UserLoginInfo info = (UserLoginInfo) WebUtil._session("user");
		if (info != null) {
			WebUtil._session("menucode", "0");
			WebUtil._session("submenucode", "0");
			WebUtil._session("url", "/spread/loan/couponsendlog_page");
			WebUtil._session(Constants.MANUAL_DEPLOY_COUPONCODE, viewSendLog.getCouponCode());
			WebUtil._session(Constants.MANUAL_DEPLOY_COUPONID, viewSendLog.getSyscouponId());
			return "success";
		} else {
			return "login";
		}
	}*/
	/*
	 * 点券推广首页面 的 发放记录 页面的跳转页面
	 */
	public String couponsendlog_page() {
		
		try {
			//int couponid = viewSendLog.getSyscouponId();
			
		      syscoupon = loanService.findOneSyscouponById(id);
			String sysCouponCode=syscoupon.getSyscoupon_code();
			
			if(syscoupon.getGoods_or_cash() == Constants.SyscouponTable.GoodsOrCash.GOODS) {
				syscoupon.setSumOrGoodsname(syscoupon.getGoods());
			}
			else if(syscoupon.getGoods_or_cash() == Constants.SyscouponTable.GoodsOrCash.CASH) {
				syscoupon.setSumOrGoodsname( String.valueOf(syscoupon.getSum()));
			}
			else {
				syscoupon.setSumOrGoodsname("unknown");
			}
			String sumOrGoodsname=syscoupon.getSumOrGoodsname();
			if(syscoupon.getCoupon_type() == Constants.SyscouponTable.CouponType.NONE) {
				syscoupon.setUseCondition("无");
			}
			else if(syscoupon.getCoupon_type() == Constants.SyscouponTable.CouponType.OVER) {
				syscoupon.setUseCondition("满" + syscoupon.getLimit_val() + "点使用1张");
			}
			else if(syscoupon.getCoupon_type() == Constants.SyscouponTable.CouponType.NO_WITH_OTHER) {
				syscoupon.setUseCondition("不与其他点券共享");
			}
			else if(syscoupon.getCoupon_type() == Constants.SyscouponTable.CouponType.ONLY_ONE) {
				syscoupon.setUseCondition("每张订单使用1张");
			}
			else {
				syscoupon.setUseCondition("unknown");
			}
			String useCondition=syscoupon.getUseCondition();
			if(syscoupon.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.DAY) {
				syscoupon.setValidPeriodWithUnit(syscoupon.getValid_period() + "天");
			}
			else if(syscoupon.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.WEEK) {
				syscoupon.setValidPeriodWithUnit(syscoupon.getValid_period() + "周");
			}
			if(syscoupon.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.MONTH) {
				syscoupon.setValidPeriodWithUnit(syscoupon.getValid_period() + "月");
			}
			else if(syscoupon.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.YEAR) {
				syscoupon.setValidPeriodWithUnit(syscoupon.getValid_period() + "年");
			}
			String validPeriodWithUnit=syscoupon.getValidPeriodWithUnit();
			if(syscoupon.getRelease_channel() == Constants.SyscouponTable.ReleaseChannel.APP) {
				syscoupon.setReleaseChannelName("APP");
			}
			else if(syscoupon.getRelease_channel() == Constants.SyscouponTable.ReleaseChannel.APP_SPREAD_UNIT){
				syscoupon.setReleaseChannelName("合作单位");
			}
			String releaseChannelName=syscoupon.getReleaseChannelName();
			ActionContext.getContext().put("sysCouponCode", sysCouponCode);
			ActionContext.getContext().put("sumOrGoodsname", sumOrGoodsname);
			ActionContext.getContext().put("useCondition", useCondition);
			ActionContext.getContext().put("validPeriodWithUnit", validPeriodWithUnit);
			ActionContext.getContext().put("releaseChannelName", releaseChannelName);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}

	/*
	 * 自助添加点券 的  新建自助添加活动功能 是弹出是传输数据的的方法
	 */
	public String couponsendlog_page1() {
		
		try {
			//int couponid = viewSendLog.getSyscouponId();
			
		      syscoupon = loanService.findOneSyscouponById(id);
			String sysCouponCode=syscoupon.getSyscoupon_code();
			
			if(syscoupon.getGoods_or_cash() == Constants.SyscouponTable.GoodsOrCash.GOODS) {
				syscoupon.setSumOrGoodsname(syscoupon.getGoods());
			}
			else if(syscoupon.getGoods_or_cash() == Constants.SyscouponTable.GoodsOrCash.CASH) {
				syscoupon.setSumOrGoodsname( String.valueOf(syscoupon.getSum()));
			}
			else {
				syscoupon.setSumOrGoodsname("unknown");
			}
			String sumOrGoodsname=syscoupon.getSumOrGoodsname();
			if(syscoupon.getCoupon_type() == Constants.SyscouponTable.CouponType.NONE) {
				syscoupon.setUseCondition("无");
			}
			else if(syscoupon.getCoupon_type() == Constants.SyscouponTable.CouponType.OVER) {
				syscoupon.setUseCondition("满" + syscoupon.getLimit_val() + "点使用1张");
			}
			else if(syscoupon.getCoupon_type() == Constants.SyscouponTable.CouponType.NO_WITH_OTHER) {
				syscoupon.setUseCondition("不与其他点券共享");
			}
			else if(syscoupon.getCoupon_type() == Constants.SyscouponTable.CouponType.ONLY_ONE) {
				syscoupon.setUseCondition("每张订单使用1张");
			}
			else {
				syscoupon.setUseCondition("unknown");
			}
			String useCondition=syscoupon.getUseCondition();
			if(syscoupon.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.DAY) {
				syscoupon.setValidPeriodWithUnit(syscoupon.getValid_period() + "天");
			}
			else if(syscoupon.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.WEEK) {
				syscoupon.setValidPeriodWithUnit(syscoupon.getValid_period() + "周");
			}
			if(syscoupon.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.MONTH) {
				syscoupon.setValidPeriodWithUnit(syscoupon.getValid_period() + "月");
			}
			else if(syscoupon.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.YEAR) {
				syscoupon.setValidPeriodWithUnit(syscoupon.getValid_period() + "年");
			}
			String validPeriodWithUnit=syscoupon.getValidPeriodWithUnit();
			if(syscoupon.getRelease_channel() == Constants.SyscouponTable.ReleaseChannel.APP) {
				syscoupon.setReleaseChannelName("APP");
			}
			else if(syscoupon.getRelease_channel() == Constants.SyscouponTable.ReleaseChannel.APP_SPREAD_UNIT){
				syscoupon.setReleaseChannelName("合作单位");
			}
			String releaseChannelName=syscoupon.getReleaseChannelName();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("sysCouponCode", sysCouponCode);
			map.put("sumOrGoodsname", sumOrGoodsname);
			map.put("useCondition", useCondition);
			map.put("validPeriodWithUnit", validPeriodWithUnit);
			map.put("releaseChannelName", releaseChannelName);
			resultObj=JSONObject.fromObject(map);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	//convert Syscoupon list to JSON object 
/*	private void makeJSONObjFromSyscoupons() {

		if (syscoupons == null) {
			resultObj = null;
			return;
		}

		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Syscoupon info : syscoupons) {

			Map<String, Object> m = new HashMap<String, Object>();

			String url = "";
			if(info.getIsenabled() == Constants.SyscouponTable.Enabled.ENABLED) {
				url = String.format("<a href='javascript:deploy(%d, \"%s\");'>发放</a>|", info.getId(), info.getSysCouponCode());
				url += String.format("<a href='javascript:stop(%d);'>停发</a>|", info.getId());
			}
			url += String.format("<a href='javascript:deploylist(%d, \"%s\");'>发放记录</a>", info.getId(), info.getSysCouponCode());

			m.put("operation", url);
			
			m.put("couponid", info.getSysCouponCode());
			
			if(info.getReleaseChannel() == 1)
				m.put("releasechannel", "APP");
			else if(info.getReleaseChannel() == 2)
				m.put("releasechannel", info.getAppSpreadUnitName());
			else
				m.put("releasechannel", "unknown");
			
			if(info.getGoodsOrCash() == 1)
				m.put("content", info.getSum());
			else if(info.getGoodsOrCash() == 2)
				m.put("content", info.getGoods());
			else
				m.put("content", "unknown");
			
			if(info.getGoodsOrCash() == 1)
				m.put("goodsorcash", "绿点");
			else if(info.getGoodsOrCash() == 2)
				m.put("goodsorcash", "实物");
			else
				m.put("goodsorcash", "unknown");
			
			if(info.getCouponType() == 0)
				m.put("coupontype", "无");
			else if(info.getCouponType() == 1)
				m.put("coupontype", "满" + info.getLimit_val() + "点使用1张");
			else if(info.getCouponType() == 2)
				m.put("coupontype", "不与其他点券共享");
			else if(info.getCouponType() == 3)
				m.put("coupontype", "每张订单使用1张");
			else
				m.put("coupontype", "unknown");
			
			String unit = "";
			if(info.getValidPeriodUnit() == 1)
				unit = "天";
			else if(info.getValidPeriodUnit() == 2)
				unit = "周";
			else if(info.getValidPeriodUnit() == 3)
				unit = "月";
			else if(info.getValidPeriodUnit() == 4)
				unit = "年";
			m.put("validperiod", info.getValidPeriod() + unit);
			m.put("deploycount", info.getDeployCount());
			m.put("usecount", info.getUseCount());
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total_size);
		json.put("rows", al);
		resultObj = JSONObject.fromObject(json);
	}*/
	
	//convert CouponSendLog list to JSON object 
	
	
	
	/*
	 * -----------查找用户---------------
	 * 发放点券页面   的(条件)查询方法
	 */
	public String searchUser(){
		List<Map<String, Object>> userList = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			//城市查询条件
			if (!"".equals(cityItemSelect)&& cityItemInput!=null&& cityItemInput!="") {
				if ("city_register".equals(cityItemSelect)) {
					params.put("cityItemSelect", "city_register");
					params.put("cityItemInput", cityItemInput);
				}
				if ("city_cur".equals(cityItemSelect)) {
					params.put("cityItemSelect", "city_cur");
					params.put("cityItemInput", cityItemInput);
				}
				if ("register_or_cur".equals(cityItemSelect)) {
					params.put("cityItemSelect", "register_or_cur");
					params.put("cityItemInput", cityItemInput);
				}
			}
			//集团/联盟查询条件
			if (!"".equals(groupItemSelect)&& groupItemInput!=null&& groupItemInput!="") {
				if ("group_or_asso_id".equals(groupItemSelect)) {
					params.put("groupItemSelect", "group_or_asso_id");
					params.put("groupItemInput", groupItemInput);
				}
				if ("group_or_asso_name".equals(groupItemSelect)) {
					params.put("groupItemSelect", "group_or_asso_name");
					params.put("groupItemInput", groupItemInput);
				}
			}
			//用户查询条件
			if (!"".equals(userItemSelect)&& userItemInput!=null && userItemInput!="") {
				if ("id".equals(userItemSelect)) {
					params.put("userItemSelect", "id");
					params.put("userItemInput", userItemInput);
				}
				if ("name".equals(userItemSelect)) {
					params.put("userItemSelect", "name");
					params.put("userItemInput", userItemInput);
				}
				if ("phone".equals(userItemSelect)) {
					params.put("userItemSelect", "phone");
					params.put("userItemInput", userItemInput);
				}
			}
			//用户类型查询条件
			if (!"".equals(userType)||userType!=null) {
				if ("driver".equals(userType)) {
					params.put("userType", "driver");
				}
				if ("passenger".equals(userType)) {
					params.put("userType", "passenger");
				}
			}
			//注册日期查询条件
			if (fromRegTime != null && !fromRegTime.isEmpty())
				params.put("fromRegTime", fromRegTime);

			if (toRegTime != null && !toRegTime.isEmpty())
				params.put("toRegTime", toRegTime);
			//最后登录时间查询条件
			if (fromLoginTime != null && !fromLoginTime.isEmpty())
				params.put("fromLoginTime", fromLoginTime);

			if (toLoginTime != null && !toLoginTime.isEmpty())
				params.put("toLoginTime", toLoginTime);
			int total =loanService.getUserCountByCondition(params);
			// 查询结果从第0条开始，查询10条记录
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			userList = loanService.getUserBasicInfoList(params);
			for(Map<String,Object> map:userList){
				Map<String,Object> param=new HashMap<String,Object>();
				param.put("id", map.get("id"));
				param.put("username", map.get("username"));
				param.put("person_verified", map.get("person_verified"));
				param.put("driver_verified", map.get("driver_verified"));
				param.put("city_register", map.get("city_register"));
				param.put("city_cur", map.get("city_cur"));
				param.put("reg_date", map.get("reg_date"));
				param.put("last_login_time", map.get("last_login_time"));
				param.put("gid", (map.get("gid")==null)?"":map.get("gid"));
				param.put("group_name", (map.get("group_name")==null)?"":map.get("group_name"));
				param.put("userid", (map.get("userid")==null)?"":map.get("userid"));
			}
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			
			
			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());//不能用这种形式，因为有可能查出来的list为null，那么就会出现空指针异常。可以做判断，如果list为null，就返回0；也可以单独定义一个count（*）的方法来获得符合条件的总记录数。
			if(total==0){
				jsonMap.put("total", 1);
				
				// 需要实现数据的总记录数
				jsonMap.put("rows", 0);
			}else{
				jsonMap.put("total", total);
				// 需要实现数据的总记录数
				jsonMap.put("rows", userList);
			}
			
			resultObj = JSONObject.fromObject(jsonMap);						
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return SUCCESS;
	}


	/*
	 * getter 和 setter 方法
	 */
	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public String getIrelease_channel() {
		return irelease_channel;
	}

	public void setIrelease_channel(String irelease_channel) {
		this.irelease_channel = irelease_channel;
	}

	public String getBnocondcash() {
		return bnocondcash;
	}

	public void setBnocondcash(String bnocondcash) {
		this.bnocondcash = bnocondcash;
	}

	public String getBcondcash() {
		return bcondcash;
	}

	public void setBcondcash(String bcondcash) {
		this.bcondcash = bcondcash;
	}

	public String getBgood() {
		return bgood;
	}

	public void setBgood(String bgood) {
		this.bgood = bgood;
	}

	public Syscoupon getNewCoupon() {
		return newCoupon;
	}

	public void setNewCoupon(Syscoupon newCoupon) {
		this.newCoupon = newCoupon;
	}

	public CouponSendLog getNewSendLog() {
		return newSendLog;
	}

	public void setNewSendLog(CouponSendLog newSendLog) {
		this.newSendLog = newSendLog;
	}

	public Syscoupon getDisableSyscoupon() {
		return disableSyscoupon;
	}

	public void setDisableSyscoupon(Syscoupon disableSyscoupon) {
		this.disableSyscoupon = disableSyscoupon;
	}

	public CouponSendLog getViewSendLog() {
		return viewSendLog;
	}

	public void setViewSendLog(CouponSendLog viewSendLog) {
		this.viewSendLog = viewSendLog;
	}

	public Syscoupon getSyscoupon() {
		return syscoupon;
	}

	public void setSyscoupon(Syscoupon syscoupon) {
		this.syscoupon = syscoupon;
	}





	public String getSyscoupon_code() {
		return syscoupon_code;
	}

	public void setSyscoupon_code(String syscoupon_code) {
		this.syscoupon_code = syscoupon_code;
	}

	public String getConditionSelect() {
		return conditionSelect;
	}

	public void setConditionSelect(String conditionSelect) {
		this.conditionSelect = conditionSelect;
	}

	public String getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(String userIdList) {
		this.userIdList = userIdList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getValid_period_unit() {
		return valid_period_unit;
	}

	public void setValid_period_unit(String valid_period_unit) {
		this.valid_period_unit = valid_period_unit;
	}

	public String getValid_period_page() {
		return valid_period_page;
	}

	public void setValid_period_page(String valid_period_page) {
		this.valid_period_page = valid_period_page;
	}

	

	public String getCityItemSelect() {
		return cityItemSelect;
	}

	public void setCityItemSelect(String cityItemSelect) {
		this.cityItemSelect = cityItemSelect;
	}

	public String getCityItemInput() {
		return cityItemInput;
	}

	public void setCityItemInput(String cityItemInput) {
		this.cityItemInput = cityItemInput;
	}

	public String getGroupItemSelect() {
		return groupItemSelect;
	}

	public void setGroupItemSelect(String groupItemSelect) {
		this.groupItemSelect = groupItemSelect;
	}

	public String getGroupItemInput() {
		return groupItemInput;
	}

	public void setGroupItemInput(String groupItemInput) {
		this.groupItemInput = groupItemInput;
	}

	public String getUserItemSelect() {
		return userItemSelect;
	}

	public void setUserItemSelect(String userItemSelect) {
		this.userItemSelect = userItemSelect;
	}

	public String getUserItemInput() {
		return userItemInput;
	}

	public void setUserItemInput(String userItemInput) {
		this.userItemInput = userItemInput;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
	public String getUserType() {
		return userType;
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



	public String getPointNumber() {
		return pointNumber;
	}

	public void setPointNumber(String pointNumber) {
		this.pointNumber = pointNumber;
	}
	public static List<Map<String, Object>> getUserInfo() {
		return userInfo;
	}

	public static void setUserInfo(List<Map<String, Object>> userInfo) {
		LoanspreadAction.userInfo = userInfo;
	}

	public String getSelectAll() {
		return selectAll;
	}

	public void setSelectAll(String selectAll) {
		this.selectAll = selectAll;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIdList() {
		return idList;
	}

	public void setIdList(String idList) {
		this.idList = idList;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSysCouponCode() {
		return sysCouponCode;
	}

	public void setSysCouponCode(String sysCouponCode) {
		this.sysCouponCode = sysCouponCode;
	}

	public int getApplySource() {
		return applySource;
	}

	public void setApplySource(int applySource) {
		this.applySource = applySource;
	}

	public int getReleaseChannel() {
		return releaseChannel;
	}

	public void setReleaseChannel(int releaseChannel) {
		this.releaseChannel = releaseChannel;
	}

	public int getGoodsOrCash() {
		return goodsOrCash;
	}

	public void setGoodsOrCash(int goodsOrCash) {
		this.goodsOrCash = goodsOrCash;
	}

	public String getAppSpreadUnitId() {
		return appSpreadUnitId;
	}

	public void setAppSpreadUnitId(String appSpreadUnitId) {
		this.appSpreadUnitId = appSpreadUnitId;
	}

	public String getSumOrGoodsname() {
		return sumOrGoodsname;
	}

	public void setSumOrGoodsname(String sumOrGoodsname) {
		this.sumOrGoodsname = sumOrGoodsname;
	}

	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public int getLimit_val() {
		return limit_val;
	}

	public void setLimit_val(int limit_val) {
		this.limit_val = limit_val;
	}

	public int getValidPeriodUnit() {
		return validPeriodUnit;
	}

	public void setValidPeriodUnit(int validPeriodUnit) {
		this.validPeriodUnit = validPeriodUnit;
	}

	public int getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(int validPeriod) {
		this.validPeriod = validPeriod;
	}

	public int getTotal_size() {
		return total_size;
	}

	public void setTotal_size(int total_size) {
		this.total_size = total_size;
	}

	public List<Map<String, Object>> getSyscoupons() {
		return syscoupons;
	}

	public void setSyscoupons(List<Map<String, Object>> syscoupons) {
		this.syscoupons = syscoupons;
	}

	public List<CouponSendLog> getSendLogs() {
		return sendLogs;
	}

	public void setSendLogs(List<CouponSendLog> sendLogs) {
		this.sendLogs = sendLogs;
	}
	public int getValid_period() {
		return valid_period;
	}

	public void setValid_period(int valid_period) {
		this.valid_period = valid_period;
	}

	public int getLimit_count() {
		return limit_count;
	}

	public void setLimit_count(int limit_count) {
		this.limit_count = limit_count;
	}

	public int getVp() {
		return vp;
	}

	public void setVp(int vp) {
		this.vp = vp;
	}

	public int getLc() {
		return lc;
	}

	public void setLc(int lc) {
		this.lc = lc;
	}
	
	
}
