package com.nmdy.operation.abbrecord.action;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.nmdy.operation.abbrecord.dao.AbbLimit;
import com.nmdy.operation.abbrecord.dao.AbbRecord;
import com.nmdy.operation.abbrecord.service.AbbRecordService;
import com.nmdy.order.appoint.dao.Appoint;
import com.nmdy.order.appoint.dao.Customer;
import com.nmdy.order.appoint.dao.LongDistanceInfo;
import com.nmdy.order.appoint.dao.MidPoints;
import com.nmdy.order.appoint.dao.OnoffdutyInfo;
import com.nmdy.order.appoint.dao.TempInfo;
import com.nmdy.order.appoint.service.AppointService;
import com.nmdy.order.recourse.service.RecourseService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 *  对违约仅此能处理的action
 *  @author xiaonana
 *
 */
public class AbbHandleAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private long id;
	private long  userid;
	private JSONObject resultObject=null;
	private int page;
	private int rows;
	private int black;
	private int limit_days;
	private int uerselect=0;
	private String inputvalue=null;
	public int abb_handle1=0;
	public int abb_handle2=0;
	public String cancel_number1;
	public String cancel_number2;
	private AbbRecordService abbRecordService;
	private Appoint appoint = null;
	private TempInfo tempInfo = null;
	private LongDistanceInfo longDistanceInfo = null;
	private OnoffdutyInfo onoffdutyInfo = null;
	private  AppointService appointService;
	private RecourseService recourseService;
	private Customer passenger = null;
	private Customer driver = null;
	
	private String msg;
	private long order_exec_id ;
	private int point;
	/*
	 * 首页面的跳转方法
	 */
	public String list(){
		return "success";
	}
     /*
      * 进入  黑名单首页面  的跳转方法
      */
	public String  blacklist(){
		return "success";
	}
	/*
	 * 保存  自定义处理违约   的方法
	 */
	public String addAbb(){
		Map<String ,Object> params=new HashMap<String,Object>();
		Map<String ,Object> params1=new HashMap<String,Object>();
		int error=0;
	try{
		if(abb_handle1!=0){
			if(cancel_number1!=null&&""!=cancel_number1){
				params.put("dailyCancelCount", cancel_number1);
				abbRecordService.addAbb1(params);	
			}
		}
		if(abb_handle2!=0){
			if(cancel_number2!=null&&""!=cancel_number2){
				params1.put("driverLate", cancel_number2);
				abbRecordService.addAbb2(params1);	
			}
		}
		error=0;
	}catch(Exception e){
		e.printStackTrace();
		error=1;
	}
	Map<String,Object> map=new HashMap<String,Object>();
	map.put("error", error);
	resultObject=JSONObject.fromObject(map);
		return "success";
	}
	/*
	 * 违约处理页面 的 警告 
	 * 通过id查询一条违约记录
	 */
	public String findById(){
		AbbRecord abb=abbRecordService.findById(id);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", abb.getId());
		map.put("abb_time", (abb.getAbb_time()==null)?"":abb.getAbb_time());
		resultObject = JSONObject.fromObject(map);
		return "success";
	}
	/*
	 * 违约处理页面 的 添加黑名单
	 * 通过添加黑名单的条件 将违约的user 添加到黑名单 的方法
	 */
	public String addblack(){
		Map<String,Object> params=new HashMap<String ,Object>();
		if(black==1){
			params.put("limit_days", -1);
		}else{
			params.put("limit_days", limit_days);
		}
		Date date=new Date();
		Timestamp limit_days_begin = new Timestamp(date.getTime());
		params.put("limit_days_begin", limit_days_begin);
		params.put("userid", userid);
		System.out.println(params);
		abbRecordService.addblack(params);
		return "success";
	}
	/*
	 * 黑名单页面中的对黑名单用户 进行条件查询 的方法
	 */
	public String findblack() {
		
		Map<String,Object> params=new HashMap<String,Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
	      params=addParams(params,uerselect,inputvalue);
	      List<AbbRecord> abbBlack=abbRecordService.findblack(params);
	      int total=abbRecordService.getBlackCount(params);
	   
	    	List<Map<String,Object>> map=new ArrayList<Map<String,Object>>();
       for(AbbRecord ar:abbBlack){
    	   Date time1= ar.getLimit_days_begin();
    	   int day= ar.getLimit_days();
    	   if(day==-1){
    		 	ar.setLimit_days(-1);
    	   }else{
    		   Date time2=new Date();
    		   /*
    		    * getDays(time) 获取天数 进行加减 求得获取的  Limit_day 时间期限
    		    */
    		   int day2=getDays(time2) - getDays(time1);   
	    	 	ar.setLimit_days(day-day2);
	    	 	System.out.println(day+"   "+day2);
    	   }
    	   System.out.println(time1);
    	   //调取时间
    	   System.out.println("getDays(time1):"+getDays(time1));
			Map<String,Object> m=new HashMap<String,Object>();
   		if(ar.getAbb_time()==null){
   			m.put("abb_time", "");
   		}else{
   			m.put("abb_time",ar.getAbb_time());
   		}if(ar.getLimit_days_begin()==null){
   			m.put("limit_days_begin", "");
   		}else{
   			m.put("limit_days_begin", ar.getLimit_days_begin());
   		}
   		m.put("abb_type", ar.getAbb_type());
   		m.put("id", ar.getId());
   		m.put("order_exec_id", ar.getOrder_exec_id());
   		m.put("status", ar.getStatus());
   		m.put("limit_days", ar.getLimit_days());
   		m.put("phone", ar.getPhone());
   		m.put("username", ar.getUsername());
   		m.put("userid", ar.getUserid());
   		m.put("cancel_number", ar.getCancel_number());
   		map.add(m);
       }
       if(total==0){
    	   jsonMap.put("total", 1);

   		// 需要实现数据的总记录数
   		jsonMap.put("rows", 0); 
       }else{
    	   jsonMap.put("total", total);

   		// 需要实现数据的总记录数
   		jsonMap.put("rows", map);
       }
       
		resultObject = JSONObject.fromObject(jsonMap);
		System.out.println(resultObject);
		
		return "success";
	}
	/*
	 *   findblack 的子方法
	 * 	 getDays(time) 获取天数 进行加减 求得获取的  Limit_day 时间期限
	 */
	 public static int getDays(Date d)       {            
		 Calendar c = Calendar.getInstance();         
		 c.setTime(d);           
		 int dDay   =  c.get(Calendar.DATE);     
		 int dYear  =  c.get(Calendar.YEAR);      
		 int dMonth =  c.get(Calendar.MONTH) + 1;  //注意month是从0开始计的我们加1    
		 int days=0;             int[] months={0,31,28,31,30,31,30,31,31,30,31,30,31};     
		 if(dYear%400==0||dYear%4==0&&dYear%100!=0){       
			 months[2]=29;           
			 }               
		 days=(dYear-1)*365+(dYear-1)/4-(dYear-1)/100+(dYear-1)/400;   
		 for(int i=1;i<dMonth;i++){   
			 days+=months[i];         
			 }          
		 days+=dDay;        
		 return days;      
		 }  
	  
	 /*
	  * findblack 子方法
	  * 查看从前台传入的数据是哪个条件
	  * 根据放入到params的条件 进行查询 黑名单的数据 
	  */
	 private Map<String, Object> addParams(Map<String, Object> params,int uerselect, String inputvalue) {
	     if(inputvalue!=null&&""!=inputvalue){
		  if(uerselect==1){
			  params.put("userid", inputvalue);
		  }if(uerselect==2){
			  params.put("phone", inputvalue);
		  }if(uerselect==3){
			  params.put("username", inputvalue);
		  }
	  }
		return params;
	}
	/*
	 * 将数据从黑名单移除至违约表里(status改变)
	 */
	public String removeBlack(){
		abbRecordService.deleteBlack(userid);
		return "success";
	}
	/*
	 * 查询自处理违约的数据信息
	 */
	public String findAbb(){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<AbbLimit> message = abbRecordService.findAbb();
		int code1=message.get(0).getValue();
		int code2=message.get(1).getValue();
		jsonMap.put("code1", code1);
		jsonMap.put("code2", code2);
		resultObject = JSONObject.fromObject(jsonMap);

		return "success";
	}
	
	 
	/*
	 * 执行订单的查看页面的跳转页面
	 */
	public String view1(){
			HttpServletRequest request = ServletActionContext.getRequest();
		 ActionContext.getContext().put("id", id);
	/*	 ActionContext.getContext().put("choose", choose);
		 System.out.println("choose : "+choose);*/
		 appoint = appointService.findOneById(id);
		 int order_type =appoint.getOrder_type() ;
			int status=appoint.getStatus();
		ActionContext.getContext().put("order_type", order_type);
			request.setAttribute("order_type", order_type);
			request.setAttribute("status", status);
		 return "success";
	 }
	 
	/*
	 * 执行订单的查看页面
	 */
	public String view() {
		/*
		 * 查看页面的查询方法
		 */
	 Map<String,Object> map=appointget();
		resultObject=JSONObject.fromObject(map);
		System.out.println(resultObject);	
		return SUCCESS;
	}
	/*
	 * view 的子方法
	 * 查看页面的查询方法
	 */
	 public Map<String,Object> appointget() {
		Map<String,Object> map=new HashMap<String,Object>();
		appoint = appointService.findOneById(id);
		int order_type =appoint.getOrder_type() ;
		map.put("order_type",  order_type);
		map.put("order_city",  appoint.getOrder_city());
		driver = recourseService.findById(appoint.getDriver());
		if (driver != null) {
			map.put("driverid", driver.getId());
			map.put("drivercode", driver.getUsercode());
			map.put("drivername", driver.getUsername());
			map.put("driverphone", driver.getPhone());
		}
		
			map.put("total_sum", appoint.getPrice());
		
			map.put("price", appoint.getPrice());
		
		passenger = recourseService.findById(appoint.getPassenger());
		if (passenger != null) {
			map.put("passagerid", passenger.getId());
			map.put("passagercode", passenger.getUsercode());
			map.put("passagername", passenger.getUsername());
			map.put("passagerphone", passenger.getPhone());
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("temp_price", appoint.getPrice());
		if (appoint.getBeginservice_time() != null) {
			map.put("iscatchecar", 1);
			map.put("beginservice_time", sdf.format(appoint.getBeginservice_time()));
		} else {
			map.put("iscatchecar", 2);
			map.put("beginservice_time", "");
		}
		
		if (appoint.getPay_time() != null) {
			map.put("ispayed", 1);
			map.put("pay_time", sdf.format(appoint.getPay_time()));
		} else {
			map.put("is_payed", 2);
			map.put("pay_time", "");
		}
		map.put("start_addr", appoint.getFrom_());
		map.put("end_addr", appoint.getTo_());
		map.put("password", (appoint.getPassword()==null)?"":appoint.getPassword());
		long detailsId = 0;
		String order_num = "";
		switch (order_type) {
		case 1:
			tempInfo = appointService.findTempInfo(id);
			if(tempInfo==null){
				map.put("order_num", "");
				map.put("reqcarstyle", 1);
				map.put("psgerRemark", "");
				map.put("driyerremark", appoint.getRemark());
			}else{
				detailsId = tempInfo.getId();
				order_num = tempInfo.getOrder_num();
				map.put("order_num", id);
				map.put("reqcarstyle", tempInfo.getReqcarstyle());
				map.put("psgerRemark", tempInfo.getRemark());
				map.put("driyerremark", appoint.getRemark());
			}
			
			break;
		case 2:
			tempInfo = appointService.findTempInfo(id);
			if(tempInfo==null){
				map.put("order_num", "");
				map.put("reqcarstyle", 1);
				map.put("psgerRemark", "");
				map.put("driyerremark", appoint.getRemark());
			}else{
				detailsId = tempInfo.getId();
				order_num = tempInfo.getOrder_num();
				map.put("order_num", id);
				map.put("reqcarstyle", tempInfo.getReqcarstyle());
				map.put("psgerRemark", tempInfo.getRemark());
				map.put("driyerremark", appoint.getRemark());
			}
			break;
		case 3:
			onoffdutyInfo = appointService.findOnoffdutyInfo(id);
			if(onoffdutyInfo!=null){
				detailsId=onoffdutyInfo.getId();
				order_num=onoffdutyInfo.getOrder_num();
				map.put("order_num",  id);
				map.put("reqcarstyle", onoffdutyInfo.getReqcarstyle());
				map.put("psgerRemark", onoffdutyInfo.getRemark());
				map.put("driyerremark", appoint.getRemark());
				map.put("weekdays", onoffdutyInfo.getWhich_days());
			}
		
			break;
		case 4:
			longDistanceInfo = appointService.findLongDistanceInfo(id);
			if(longDistanceInfo!=null){
				detailsId=longDistanceInfo.getId();
				order_num=longDistanceInfo.getOrder_num();
			map.put("order_num", id);
			map.put("driyerremark", longDistanceInfo.getRemark());
			}
			break;
		default:
			break;
		}
		
		map.put("id", detailsId);
		// MidPoints
		List<MidPoints> midpointsList;
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("order_type", order_type - 1);
		map1.put("detailsId", detailsId);
		midpointsList = appointService.findMidPoints(map1);
		for (int i=0; i<midpointsList.size(); i++) {
			System.out.println(midpointsList.get(i).getIndex());
			switch (midpointsList.get(i).getIndex()) {
			case 0:
				map.put("addr4midpoint1", midpointsList.get(i).getAddr());
				break;
			case 1:
				map.put("addr4midpoint2", midpointsList.get(i).getAddr());
				break;
			case 2:
				map.put("addr4midpoint3", midpointsList.get(i).getAddr());
				break;
			case 3:
				map.put("addr4midpoint4", midpointsList.get(i).getAddr());
				break;
			default:
				break;
			}
		}
		
		if (appoint.getCr_date() != null) 
		map.put("ps_date", sdf.format(appoint.getCr_date()));
		if (appoint.getTi_accept_order() !=null) 
		map.put("accept_time",sdf.format(appoint.getTi_accept_order()));
		if (appoint.getBegin_exec_time() !=null) 
		map.put("begin_exec_time",  sdf.format(appoint.getBegin_exec_time()));
		if (appoint.getDriverarrival_time() != null)
		map.put("driverarrival_time", sdf.format(appoint.getDriverarrival_time()));
		if (appoint.getStopservice_time() != null) 
		map.put("stopservice_time", sdf.format(appoint.getStopservice_time()));
		if (appoint.getPre_time() != null)
		map.put("pre_time", sdf.format(appoint.getPre_time()));
		int status=appoint.getStatus();
		map.put("status",status);
		Map<String,Object> map2=new HashMap<String,Object>();
		map2.put("userid", appoint.getPassenger());
		map2.put("id", appoint.getId());
		int total1=appointService.findAccountFreeze(map2);
		int total2=0;
		if(total1==0){
			map.put("enableMoney", "0");
		}else{
			total2=appointService.findAccountUnfreeze(map2);
			if(total2==0){
				map.put("enableMoney", "0");
			}else{
				Map<String,Object> statusData = appointService.findFreezeStatus(appoint.getId());
				BigDecimal freeze=(BigDecimal)statusData.get("freeze") ;
				BigDecimal unfreeze=(BigDecimal)statusData.get("unfreeze") ;
				if (freeze==unfreeze) {
					map.put("enableMoney", "0");
				} else {
					map.put("enableMoney", "1");
				}
			}
		}
		// breaking Button "退还乘客冻结的绿点"
		if (appoint.getStatus() != 8) {
			map.put("isbreaked", 1);
		} else if (appointService.getBreakAppointCnt(order_num) > 0) {
			map.put("isbreaked", 1);
		} else {
			map.put("isbreaked", 0);
		}
	System.out.println("------:"+map);
	return map;
	
	}

	 /*
	  * 警告
	  */
	public String warn(){
		
		Map<String,Object> map=new HashMap<String,Object>();
		int error=0;
		try{
			map.put("msg", msg);
			map.put("userid", userid);
			abbRecordService.insertNotify(map);
			map.put("id", id);
			abbRecordService.undateWarn(map);
			error=0;
		}catch(Exception e){
			error=1;
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("error", error);
		resultObject=JSONObject.fromObject(params);
		return "success";
	}
	
	/*
	 * 扣款
	 */
	public String forfeit(){
		Map<String,Object> map=new HashMap<String,Object>();
		int error=0;
		int right=0;
		try{
		map.put("point", point);
		map.put("order_exec_id", order_exec_id);
	int row =	abbRecordService.updateTSForfeit(map);
		if(row==0){
			right=1;
		}
		map.put("id", id);
		abbRecordService.undateForfeit(map);
		error=0;
		}catch(Exception e){
		error=1;
		}
		if(right==1){
			error=2;
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("error", error);
		resultObject=JSONObject.fromObject(params);
		return "success";
	}
	
	/*
	 * getter和setter方法
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
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

	public AbbRecordService getAbbRecordService() {
		return abbRecordService;
	}

	public void setAbbRecordService(AbbRecordService abbRecordService) {
		this.abbRecordService = abbRecordService;
	}

	public int getBlack() {
		return black;
	}

	public void setBlack(int black) {
		this.black = black;
	}

	public int getLimit_days() {
		return limit_days;
	}

	public void setLimit_days(int limit_days) {
		this.limit_days = limit_days;
	}

	public int getUerselect() {
		return uerselect;
	}

	public void setUerselect(int uerselect) {
		this.uerselect = uerselect;
	}

	public String getInputvalue() {
		return inputvalue;
	}

	public void setInputvalue(String inputvalue) {
		this.inputvalue = inputvalue;
	}

	public int getAbb_handle1() {
		return abb_handle1;
	}

	public void setAbb_handle1(int abb_handle1) {
		this.abb_handle1 = abb_handle1;
	}

	public int getAbb_handle2() {
		return abb_handle2;
	}

	public void setAbb_handle2(int abb_handle2) {
		this.abb_handle2 = abb_handle2;
	}

	public String getCancel_number1() {
		return cancel_number1;
	}

	public void setCancel_number1(String cancel_number1) {
		this.cancel_number1 = cancel_number1;
	}

	public String getCancel_number2() {
		return cancel_number2;
	}

	public void setCancel_number2(String cancel_number2) {
		this.cancel_number2 = cancel_number2;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public Appoint getAppoint() {
		return appoint;
	}
	public void setAppoint(Appoint appoint) {
		this.appoint = appoint;
	}
	public TempInfo getTempInfo() {
		return tempInfo;
	}
	public void setTempInfo(TempInfo tempInfo) {
		this.tempInfo = tempInfo;
	}
	public LongDistanceInfo getLongDistanceInfo() {
		return longDistanceInfo;
	}
	public void setLongDistanceInfo(LongDistanceInfo longDistanceInfo) {
		this.longDistanceInfo = longDistanceInfo;
	}
	public OnoffdutyInfo getOnoffdutyInfo() {
		return onoffdutyInfo;
	}
	public void setOnoffdutyInfo(OnoffdutyInfo onoffdutyInfo) {
		this.onoffdutyInfo = onoffdutyInfo;
	}
	public Customer getPassenger() {
		return passenger;
	}
	public void setPassenger(Customer passenger) {
		this.passenger = passenger;
	}
	public Customer getDriver() {
		return driver;
	}
	public void setDriver(Customer driver) {
		this.driver = driver;
	}
	public AppointService getAppointService() {
		return appointService;
	}
	public void setAppointService(AppointService appointService) {
		this.appointService = appointService;
	}
	public RecourseService getRecourseService() {
		return recourseService;
	}
	public void setRecourseService(RecourseService recourseService) {
		this.recourseService = recourseService;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public long getOrder_exec_id() {
		return order_exec_id;
	}
	public void setOrder_exec_id(long order_exec_id) {
		this.order_exec_id = order_exec_id;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}

	
}
