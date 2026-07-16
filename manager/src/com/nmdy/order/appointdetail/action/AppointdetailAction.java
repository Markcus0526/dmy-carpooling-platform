package com.nmdy.order.appointdetail.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts2.ServletActionContext;

import com.nmdy.common.Errors;
import com.nmdy.common.UserLoginInfo;
import com.nmdy.common.WebUtil;
import com.nmdy.order.appoint.dao.Customer;
import com.nmdy.order.appoint.dao.ExecDetails;
import com.nmdy.order.appoint.dao.MidPoints;
import com.nmdy.order.appoint.service.AppointService;
import com.nmdy.order.appointdetail.dao.Appointdetail;
import com.nmdy.order.appointdetail.dao.AppointdetailInfo;
import com.nmdy.order.appointdetail.dao.FreezeTsInfo;
import com.nmdy.order.appointdetail.dao.OrderLongdistanceUsersCs;
import com.nmdy.order.appointdetail.dao.OrderModifyLogInfo;
import com.nmdy.order.appointdetail.dao.OrderOnoffdutyDivide;
import com.nmdy.order.appointdetail.dao.OrderOnoffdutyExecDetails;
import com.nmdy.order.appointdetail.service.AppointdetailService;
import com.nmdy.order.recourse.service.RecourseService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 总订单管理表的Action
 * @author xcnana
 *
 */
public class AppointdetailAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private SqlSession session = null;
	int err;
	// Index
	private JSONObject resultObject = null;
	private Integer page;
	private Integer rows;
	private Integer rowLen;
	private SearchItems searchItems;
	private Long id;
	// View
	private String order_num;
	private int order_type;
	private int orderType;
	private String order_city;
	private Date begintime;
	private Date endtime;
	private int order_type1;
	private int order_type2;
	private int order_type3=0;
	private  Integer status=0;
	private String usercode;
	private String username;
	private String userphone;
	private Integer usertype;
	private String nickname;
	private int userId;
	
	private String start_addr;
	private String end_addr;
	private int reqcarstyle;
	private String psgerRemark;
	private String driyerRemark;
	private String addr4midpoint1;
	private String addr4midpoint2;
	private String addr4midpoint3;
	private String addr4midpoint4;
	private Date ps_date;
	private Date accept_time;
	private Date 	begin_exec_time;
	private Date 	beginservice_time;
	private Date 	stopservice_time;
	private Date 	pay_time;
	private Date 	pre_time;
	private String weekdays;
	private Date 	driverarrival_time;
	private int isPayed;
	private int isCatchedCar;
	private String enableMoney;
	private int weekdays_1=0;
	private int weekdays_2=0;
	private int weekdays_3=0;
	private int weekdays_4=0;
	private int weekdays_5=0;
	private int weekdays_6=0;
	private int weekdays_7=-1;
	private OrderDetails orderDetails;
	private AppointService appointService;
	private RecourseService recourseService;
	// Edit
	private String method;
	private List<OrderModifyLogInfo> orderModifyLogInfos;
	private Integer orderExecId;
	private FreezeTsInfo freezeTsInfo;	
	private List<OrderLongdistanceUsersCs> orderLongdistanceUsersCsList;
	private OrderOnoffdutyDivide orderOnoffdutyDivide;
	private List<OrderOnoffdutyExecDetails> orderOnoffdutyExecDetailsList;
	
	// Model
	private Customer passenger = null;
	private Customer driver = null;
	private AppointdetailInfo appointdetailInfo = null;
	private List<Appointdetail> appointdetailList;
	private AppointdetailService appointdetailService;
	/*
	 * 订单管理页面的首页面的跳转方法
	 */
	public String index() {
		
		if(status>0){
			ActionContext.getContext().put("status", status);
		}else{
			ActionContext.getContext().put("status", 1);
		}
		return SUCCESS;
	}
	/*
	 * 订单管理首页面的查询显示方法
	 */
	public String search() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("start_city",  '%'+order_city+'%');
			map.put("beginTime", begintime);
			map.put("endTime", endtime);
			map.put("ordernum", order_num);
			map.put("order_type", "application");
			map.put("order_type1", order_type1);
			map.put("order_type2", order_type2);
			map.put("order_type3", order_type3);
			map.put("status", status);
			map.put("usercode", '%'+usercode+'%');
			map.put("username", '%'+username+'%');
			map.put("userphone", '%'+userphone+'%');
			map.put("usertype", usertype);
			map.put("nickname", '%'+nickname+'%');
			if (("").equals(usercode) && ("").equals(username)&& ("").equals(userphone) && ("").equals(nickname)) {
				map.put("isJoinUser", 0);
			} else {
				map.put("isJoinUser", 1);
			}
			System.out.println(map);
			appointdetailList=appointdetailService.search(map);
			if(appointdetailList==null){
				rowLen=0;
			}else{
				rowLen =appointdetailList.size();
			}
			
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			appointdetailList = appointdetailService.search(map);
		} catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}		
		Date date = null;
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Appointdetail info : appointdetailList) {			
			Map<String, Object> m = new HashMap<String, Object>();
			if(("").equals(info.getPassenger())){
				passenger=null;
			}else{
			passenger = recourseService.findById(info.getPassenger());
			}
			if(("").equals(info.getDriver())){
				driver=null;
			}else{
				driver = recourseService.findById(info.getDriver());
			}
			m.put("order_type", info.getOrder_type());	
			m.put("orderNum", info.getOrder_num());		
			m.put("id", info.getId());	
			m.put("startCity", info.getStart_city()==null?"":info.getStart_city());
			if (passenger == null) {
				m.put("guestName", "");
				m.put("guestPhone", "");
			} else {
				m.put("guestName", passenger.getUsername());
				m.put("guestPhone", passenger.getPhone());
			}			
			
			if (driver == null) {
				m.put("carName", "");
				m.put("carPhone", "");
			} else {
				m.put("carName", driver.getUsername());
				m.put("carPhone", driver.getPhone());
			}
			m.put("status", info.getStatus());
			switch (info.getStatus()) {
			case 1:
				date = info.getPs_date();
				break;
			case 2:
				date = info.getAccept_time();
				break;
			case 3:
				date = info.getBegin_exec_time();
				break;
			case 4:
				date = info.getDriverarrival_time();
				break;
			case 5:
				date = info.getBeginservice_time();
				break;
			case 6:
				date = info.getEndservice_time();
				break;
			case 7:
				date = info.getPay_time();
				break;
			case 8:
				date = info.getPass_cancel_time();
				break;
			case 9:
				date = info.getDriver_cancel_time();
				break;
			default:
				break;
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(date!=null && !"".equals(date)){
				m.put("statusTime", sdf.format(date));	
			}else{
				m.put("statusTime", "");	
			}
					
			al.add(m);
		}
		Map<String, Object> json = new HashMap<String, Object>();
		if(rowLen==0){
			json.put("total", 1);
			json.put("rows", 0);
			resultObject = JSONObject.fromObject(json);
		}else{
			json.put("total", rowLen);
			json.put("rows", al);
			resultObject = JSONObject.fromObject(json);
		}
		return "success";
	}
    /*
     * 查看页面的跳转方法
     */
	public String view1(){
	HttpServletRequest request = ServletActionContext.getRequest();
	 ActionContext.getContext().put("id", id);
		Map<String ,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("order_type", order_type);
		appointdetailInfo = appointdetailService.findOrderdetailInfo(map);
		int status=appointdetailInfo.getStatus();
	 ActionContext.getContext().put("order_type", order_type);
		request.setAttribute("order_type", order_type);
		request.setAttribute("status", status);
		return "success";
	}
	/*
	 * 显示查看页面的方法
	 */
	public String view() {

		Map<String ,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("order_type", order_type);
		appointdetailInfo = appointdetailService.findOrderdetailInfo(map);
		map.clear();
			map.put("order_type",  order_type);
			map.put("order_city",  (appointdetailInfo.getStart_city()==null)?"":appointdetailInfo.getStart_city());
			driver = recourseService.findById(appointdetailInfo.getDriver());
			if (driver != null) {
				map.put("driverid", driver.getId());
				map.put("drivercode", driver.getUsercode());
				map.put("drivername", driver.getUsername());
				map.put("driverphone", driver.getPhone());
			}
			
				map.put("total_sum", (appointdetailInfo.getPrice()==null)?"":appointdetailInfo.getPrice());
			
				map.put("price", (appointdetailInfo.getPrice()==null)?"":appointdetailInfo.getPrice());
			
			passenger = recourseService.findById(appointdetailInfo.getPassenger());
			if (passenger != null) {
				map.put("passagerid", passenger.getId());
				map.put("passagercode", passenger.getUsercode());
				map.put("passagername", passenger.getUsername());
				map.put("passagerphone", passenger.getPhone());
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("temp_price", (appointdetailInfo.getPrice()==null)?"":appointdetailInfo.getPrice());
			if(appointdetailInfo.getBeginservice_time()!=null){
				map.put("iscatchecar", 1);
				map.put("beginservice_time", sdf.format(appointdetailInfo.getBeginservice_time()));
			}else{
				map.put("iscatchecar", 2);
				map.put("beginservice_time", "");
			}
		
			if (appointdetailInfo.getPay_time() != null) {
				map.put("ispayed", 1);
				map.put("pay_time", sdf.format(appointdetailInfo.getPay_time()));
			} else {
				map.put("is_payed", 2);
				map.put("pay_time", "");
			}
			if(appointdetailInfo.getStart_addr()!=null){
				map.put("start_addr", appointdetailInfo.getStart_addr());
			}else{
				map.put("start_addr", "");
			}
			if(appointdetailInfo.getEnd_addr()!=null){
				map.put("end_addr", appointdetailInfo.getEnd_addr());
			}else{
				map.put("end_addr","" );
			}
			
			
			int detailsId = 0;
			String order_num = "";
			switch (appointdetailInfo.getOrder_type()) {
			case 1:
				map.put("order_num", appointdetailInfo.getOrder_num());
				map.put("reqcarstyle", appointdetailInfo.getReqcarstyle());
				map.put("psgerRemark", (appointdetailInfo.getPassengerRemark()==null)?"":appointdetailInfo.getPassengerRemark());
				map.put("driyerremark", (appointdetailInfo.getDriverRemark()==null)?"":appointdetailInfo.getDriverRemark());
				break;
			case 2:
				map.put("order_num", appointdetailInfo.getOrder_num());
				map.put("reqcarstyle", appointdetailInfo.getReqcarstyle());
				map.put("psgerRemark", (appointdetailInfo.getPassengerRemark()==null)?"":appointdetailInfo.getPassengerRemark());
				map.put("driyerremark", (appointdetailInfo.getDriverRemark()==null)?"":appointdetailInfo.getDriverRemark());
				map.put("weekdays", (appointdetailInfo.getWeekdays()==null)?"":appointdetailInfo.getWeekdays());
				break;
			case 3:
				map.put("order_num", appointdetailInfo.getOrder_num());
				map.put("driyerremark", (appointdetailInfo.getDriverRemark()==null)?"":appointdetailInfo.getDriverRemark());
				break;
			default:
				break;
			}
			map.put("id", id);
			// MidPoints
			List<MidPoints> midpointsList;
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("order_type", order_type-1);
			map1.put("detailsId", id);
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
			
			if (appointdetailInfo.getPs_date() != null) 
			map.put("ps_date", sdf.format(appointdetailInfo.getPs_date()));
			if (appointdetailInfo.getAccept_time()!=null) 
			map.put("accept_time",sdf.format(appointdetailInfo.getAccept_time()));
			if (appointdetailInfo.getBegin_exec_time() !=null) 
			map.put("begin_exec_time",  sdf.format(appointdetailInfo.getBegin_exec_time()));
			if (appointdetailInfo.getDriverarrival_time() != null)
			map.put("driverarrival_time", sdf.format(appointdetailInfo.getDriverarrival_time()));
			if (appointdetailInfo.getEndservice_time() != null) 
			map.put("stopservice_time", sdf.format(appointdetailInfo.getEndservice_time()));
			if (appointdetailInfo.getPre_time() != null)
			map.put("pre_time", sdf.format(appointdetailInfo.getPre_time()));
			int status=appointdetailInfo.getStatus();
			map.put("status",status);

			Map<String,Object> map2=new HashMap<String,Object>();
			map2.put("userid", appointdetailInfo.getPassenger());
			map2.put("id", appointdetailInfo.getId());
			int total1=appointdetailService.findAccountFreeze(map2);
			int total2=0;
			if(total1==0){
				map.put("enableMoney", "0");
			}else{
				total2=appointdetailService.findAccountUnfreeze(map2);
				if(total2==0){
					map.put("enableMoney", "0");
				}else{
					Map<String,Object> statusData = appointdetailService.findFreezeStatus(appointdetailInfo.getId());
					BigDecimal freeze=(BigDecimal)statusData.get("freeze") ;
					BigDecimal unfreeze=(BigDecimal)statusData.get("unfreeze") ;
					if (freeze==unfreeze) {
						map.put("enableMoney", "0");
					} else {
						map.put("enableMoney", "1");
					}
				}
			}
			if (appointdetailInfo.getStatus() != 8) {
				map.put("isbreaked", 1);
			} else if (appointdetailService.getBreakAppointCnt(order_num) > 0) {
				map.put("isbreaked", 1);
			} else {
				map.put("isbreaked", 0);
			}
		System.out.println("------:"+map);
		resultObject=JSONObject.fromObject(map);
		System.out.println(resultObject);	
		   return SUCCESS;
	}
	/*
	 * 查看页面里的 执行记录表的显示方法
	 */
	public String findthree(){
		Map<String ,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		int total=0;
		System.out.println(" order_type---------"+order_type);
		List<ExecDetails> execDetails=null;
		switch (order_type) {
		case 1:
			if(appointdetailService.findTempExecByOId(map)!=null){
				total=appointdetailService.findTempExecByOId(map).size();
			}else{
				total=0;
			}
			
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			execDetails=appointdetailService.findTempExecByOId(map);
			break;
		case 2:
			if(appointdetailService.findOnOffDExecByOId(map)!=null){
				total=appointdetailService.findOnOffDExecByOId(map).size();
			}else{
				total=0;
			}
			
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			 execDetails=appointdetailService.findOnOffDExecByOId(map);
			break;
		case 3:
			if(appointdetailService.findLongDExecByOId(map)!=null){
			  total=appointdetailService.findLongDExecByOId(map).size();
			}else{
				total=0;
			}
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			 execDetails=appointdetailService.findLongDExecByOId(map);
			break;
		default:
			break;
		}
		List<Map<String ,Object>> m= new ArrayList<Map<String,Object>>();
		for(ExecDetails e :execDetails){
			Map<String ,Object> params=new  HashMap<String,Object>();
			params.put("id", e.getId());
			params.put("begin_exec_time", (e.getBegin_exec_time()==null)?"":e.getBegin_exec_time());
			m.add(params);
			//params.clear();
		}
		Map<String, Object> json = new HashMap<String, Object>();
		if(total==0){
			json.put("total", 1);
			json.put("rows", 0);
		}else{
			json.put("total", total);
			json.put("rows", m);	
		}
		
		resultObject = JSONObject.fromObject(json);
		return "success";
	}
	/*
	 * 显示修改页面的跳转方法
	 */
	public String edit1(){
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String ,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("order_type", order_type);
		appointdetailInfo = appointdetailService.findOrderdetailInfo(map);
		 ActionContext.getContext().put("id", id);
		// appoint = appointService.findOneById(id);
		// int order_type =appoint.getOrder_type() ;
			request.setAttribute("order_type", order_type);
			if(appointdetailInfo==null){
				request.setAttribute("orderExecId",0);
			}else{
			request.setAttribute("orderExecId", appointdetailInfo.getOrderExecId());
			}
		return "success";
	}

	/*
	 * 总订单的修改方法
	 */
	public String edit() {
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;

		Map<String ,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("order_type", orderType);
		appointdetailInfo = appointdetailService.findOrderdetailInfo(map);
		map.clear();
		/*
		 * setorderDetailsInfo()方法是将页面的数据以及通过页面数据查询的数据 放入到实体类里
		 */
		if(setorderDetailsInfo()==true){
		try {
			/*
			 * editOrderdetailInfo()方法是将实体类的数据进行update 和 insert 操作
			 */
				if (editOrderdetailInfo() == false) 
					err = Errors.ERR_SQL_ERROR;
				/*
				 *将实体类里的数据 和 进行查询的appoint里的数据进行比较 看是否被修改 被修改的数据放到 修改记录表 中 
				 */
				setOrderHistory();
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		}
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		ActionContext.getContext().put("status", status);
		resultObject = JSONObject.fromObject(json);
		return SUCCESS;
	}
	/*
	 * setorderDetailsInfo()方法是将页面的数据以及通过页面数据查询的数据 放入到实体类里
	 */
	private boolean setorderDetailsInfo() {
		try {
			if (orderDetails == null)
				orderDetails = new OrderDetails();
			
			orderDetails.setId(appointdetailInfo.getId()); 								// id in Table "order_details"
			orderDetails.setOrderExecId(appointdetailInfo.getOrderExecId());
			orderDetails.setOrder_num(appointdetailInfo.getOrder_num());
			orderDetails.setOrder_type(appointdetailInfo.getOrder_type());
			orderDetails.setCity_from(appointdetailInfo.getCity_from());
			passenger = appointService.findById(appointdetailInfo.getPassenger());			
			driver = appointService.findById(appointdetailInfo.getDriver());
			if (driver != null) {
				orderDetails.setDriverId(appointdetailInfo.getDriver());
				orderDetails.setDriverCode(driver.getUsercode());
				orderDetails.setDriverName(driver.getUsercode());
				orderDetails.setDriverPhone(driver.getPhone());
			}
			if (passenger != null) {
				orderDetails.setPsgerId(appointdetailInfo.getPassenger());;
				orderDetails.setPsgerCode(passenger.getUsercode());
				orderDetails.setPsgerName(passenger.getUsername());
				orderDetails.setPsgerPhone(passenger.getPhone());
			}			
						
			orderDetails.setTotal_sum(appointdetailInfo.getTotal_sum());		
			orderDetails.setPrice(appointdetailInfo.getPrice());
			orderDetails.setTemp_price(appointdetailInfo.getPrice());
			
			orderDetails.setIsCatchedCar(isCatchedCar);
			orderDetails.setBeginservice_time(beginservice_time);
			orderDetails.setIsPayed(isPayed);
			orderDetails.setPay_time(pay_time);
			orderDetails.setEnableMoney(enableMoney);
			orderDetails.setStart_addr(start_addr);
			orderDetails.setEnd_addr(end_addr);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderType", orderDetails.getOrder_type() - 1);
			map.put("detailsId", appointdetailInfo.getId());
			if(orderDetails.getOrder_type()!=3){
				orderDetails.setAddr4midpoint1(addr4midpoint1);
				orderDetails.setAddr4midpoint2(addr4midpoint2);
				orderDetails.setAddr4midpoint3(addr4midpoint3);
				orderDetails.setAddr4midpoint4(addr4midpoint4);	
			}else{
				
			}
			 orderDetails.setPs_date(ps_date);
			 orderDetails.setAccept_time(accept_time);
			 orderDetails.setBegin_exec_time(begin_exec_time);
			 orderDetails.setDriverarrival_time(driverarrival_time);
			 orderDetails.setStopservice_time(stopservice_time);
			 orderDetails.setPre_time(pre_time);
			 orderDetails.setStatus(status);
			 orderDetails.setReqcarstyle(reqcarstyle);
			 orderDetails.setPsgerRemark(psgerRemark);
			 orderDetails.setDriverRemark(driyerRemark);
			// orderDetails.setWeekdays(weekdays);
			  	if(weekdays_1==1){
			  		orderDetails.setWeekdays_1(false);
			  	 }  if(weekdays_2==2){
					 orderDetails.setWeekdays_2(false);
				 }  if(weekdays_3==3){
					 orderDetails.setWeekdays_3(false);
				 }  if(weekdays_4==4){
					 orderDetails.setWeekdays_4(false);
				 }  if(weekdays_5==5){
					 orderDetails.setWeekdays_5(false);
				 }  if(weekdays_6==6){
					 orderDetails.setWeekdays_6(false);
				 }  if(weekdays_7==0){
					 orderDetails.setWeekdays_7(false);
				 }  
			if (orderDetails.getStatus() != 8) {
				orderDetails.setIsBreaked(1);
			} else if (appointdetailService.getBreakAppointCnt(appointdetailInfo.getOrder_num()) > 0) {
				orderDetails.setIsBreaked(1);
			} else {
				orderDetails.setIsBreaked(0);
			}
			orderDetails.setOrderExecId(appointdetailInfo.getOrderExecId());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
		
		return true;
	}
	
	/*
	 * editOrderdetailInfo()方法是将实体类的数据进行update 和 insert 操作
	 */
	private boolean editOrderdetailInfo() {
		try {
				// order_temp_details(price, beginservice_time, pay_time, start_addr, end_addr, ps_date
				// 				, accept_time, begin_exec_time, driverarrival_time, endservice_time, pre_time, status
				//				, reqcarstyle, remark)
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", orderDetails.getId());
				m.put("price", orderDetails.getPrice());
				if (orderDetails.getBeginservice_time() != null && !orderDetails.getBeginservice_time().equals("")) {
					m.put("beginservice_time", orderDetails.getBeginservice_time());
				}
				if (orderDetails.getPay_time() != null && !orderDetails.getPay_time().equals("")) {
					m.put("pay_time", orderDetails.getPay_time());
				}
				if (orderDetails.getStart_addr() != null)
					m.put("start_addr", orderDetails.getStart_addr());
				else
					m.put("start_addr", "");
				if (orderDetails.getEnd_addr() != null)
					m.put("end_addr", orderDetails.getEnd_addr());
				else
					m.put("end_addr", "");
				if (orderDetails.getPs_date() != null && !orderDetails.getPs_date().equals("")) {
					m.put("ps_date", orderDetails.getPs_date());
				}
				if (orderDetails.getAccept_time() != null && !orderDetails.getAccept_time().equals("")) {
					m.put("accept_time", orderDetails.getAccept_time());
				} else {
					m.put("accept_time", "");
				}
				if (orderDetails.getBegin_exec_time() != null && !orderDetails.getBegin_exec_time().equals("")) {
					m.put("begin_exec_time", orderDetails.getBegin_exec_time());
				} else {
					m.put("begin_exec_time", "");
				}
				if (orderDetails.getDriverarrival_time() != null && !orderDetails.getDriverarrival_time().equals("")) {
					m.put("driverarrival_time", orderDetails.getDriverarrival_time());
				} else {
					m.put("driverarrival_time", "");
				}
				if (orderDetails.getStopservice_time() != null && !orderDetails.getStopservice_time().equals("")) {
					m.put("endservice_time", orderDetails.getStopservice_time());
				} else {
					m.put("endservice_time", "");
				}
				if (orderDetails.getPre_time() != null && !orderDetails.getPre_time().equals("")) {
					m.put("pre_time", orderDetails.getPre_time());
				}				
				m.put("status", orderDetails.getStatus());
				m.put("reqcarstyle", orderDetails.getReqcarstyle());
				m.put("remark", (orderDetails.getPsgerRemark()==null)?"":orderDetails.getPsgerRemark());
				m.put("driverRemark", (orderDetails.getDriverRemark()==null)?"":orderDetails.getDriverRemark());
				switch (orderDetails.getOrder_type()) {
				case 1:
					appointdetailService.updateOrderTempDetail(m);
					break;
				case 2:
					appointdetailService.updateOrderOnoffDetail(m);
					break;
				case 3:
					appointdetailService.updateOrderLongDetail(m);
					break;
				default:
					break;
				}			
				m.clear();
				
				if (orderDetails.getOrder_type() == 2) {
					String str_weekdays = "";
					if (orderDetails.isWeekdays_1() == false) str_weekdays += "1,";
					if (orderDetails.isWeekdays_2() == false) str_weekdays += "2,";
					if (orderDetails.isWeekdays_3() == false) str_weekdays += "3,";
					if (orderDetails.isWeekdays_4() == false) str_weekdays += "4,";
					if (orderDetails.isWeekdays_5() == false) str_weekdays += "5,";
					if (orderDetails.isWeekdays_6() == false) str_weekdays += "6,";
					if (orderDetails.isWeekdays_7() == false) str_weekdays += "0,";
					appointdetailService.updateWeekdays(str_weekdays, orderDetails.getId());
				}
				
				// order_exec_cs(price, beginservice_time, pay_time, from_, to_, ti_accept_order
				//				, begin_exec_time, driverarrival_time, stopservice_time, status, remark)
				if (orderDetails.getOrderExecId() > 0) {
					m.put("order_exec_id", orderDetails.getOrderExecId());
					m.put("price", Double.valueOf(orderDetails.getPrice()));
					if (orderDetails.getBeginservice_time() != null && !orderDetails.getBeginservice_time().equals(""))
						m.put("beginservice_time", orderDetails.getBeginservice_time());
					if (orderDetails.getPay_time() != null && !orderDetails.getPay_time().equals(""))
						m.put("pay_time", orderDetails.getPay_time());					
					m.put("from_", orderDetails.getStart_addr());
					m.put("to_", orderDetails.getEnd_addr());
					if (orderDetails.getOrder_type() == 3) {
						m.put("city_from", orderDetails.getStart_addr());
						m.put("city_to", orderDetails.getEnd_addr());
					}
					if (orderDetails.getPs_date() != null && !orderDetails.getPs_date().equals(""))
						m.put("cr_date", orderDetails.getPs_date());
					if (orderDetails.getAccept_time() != null && !orderDetails.getAccept_time().equals(""))
						m.put("ti_accept_order", orderDetails.getAccept_time());
					if (orderDetails.getBegin_exec_time() != null && !orderDetails.getBegin_exec_time().equals(""))
						m.put("begin_exec_time", orderDetails.getBegin_exec_time());
					if (orderDetails.getDriverarrival_time() != null && !orderDetails.getDriverarrival_time().equals(""))
						m.put("driverarrival_time", orderDetails.getDriverarrival_time());
					if (orderDetails.getStopservice_time() != null && !orderDetails.getStopservice_time().equals(""))
						m.put("stopservice_time", orderDetails.getStopservice_time());
					m.put("status", orderDetails.getStatus());
					if (orderDetails.getOrder_type() == 3){
						m.put("remark", orderDetails.getPsgerRemark());
					} else {
						m.put("remark", orderDetails.getDriverRemark());
					}
					
					if (orderDetails.getStatus() == 1) {
						if (orderDetails.getOrder_type() == 1) {
							appointService.deleteOrderExecCs(orderDetails.getOrderExecId());
						} else if (orderDetails.getOrder_type() == 2) {
							orderOnoffdutyDivide = appointdetailService.findOnoffdutyDivideByOrderdetails_id(orderDetails.getId());
							orderOnoffdutyExecDetailsList = appointdetailService.findOnoffdutyExecByOnoffduty_divide_id(orderOnoffdutyDivide.getId());
							for (OrderOnoffdutyExecDetails info: orderOnoffdutyExecDetailsList) {
								appointService.deleteOrderExecCs(info.getOrder_cs_id());
							}
							appointdetailService.deleteOrderOnoffdutyExecDetailsByOnoffduty_divide_id(orderOnoffdutyDivide.getId());
							appointdetailService.deleteOrderOnoffdutyDivide(orderDetails.getId());
							
						} else if (orderDetails.getOrder_type() == 3) {
							orderLongdistanceUsersCsList = appointdetailService.findLongdistanceUserCs(orderDetails.getId());
							for (OrderLongdistanceUsersCs info : orderLongdistanceUsersCsList) {
								appointService.deleteOrderExecCs(info.getOrder_exec_cs_id());
							}
							appointdetailService.deleteOrderLongdistanceUsers(orderDetails.getId());
						}
					} else {
						appointdetailService.updateOrderExecCs(m);
					}	
					
					m.clear();
				}
				if (orderDetails.getOrder_type() != 3) {
					List<MidPoints> midpointsList;
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("order_type", orderDetails.getOrder_type() - 1);
					map.put("detailsId", orderDetails.getId());
					midpointsList = appointService.findMidPoints(map);
					if (midpointsList.size() == 0) { // insert
						
					} else {	
						System.out.println("      order_type      "+map);
						int row = appointService.deletedMidPoints(map);
						System.out.println(row);
					}	
						MidPoints midpoints = new MidPoints();
						midpoints.setOrder_type(orderDetails.getOrder_type()-1);
						midpoints.setOrderid(orderDetails.getId());
						String addr = "";
						for (int i = 0; i < 4; i ++) {
							midpoints.setIndex(i);
							midpoints.setLat(0.0);
							midpoints.setLng(0.0);
							if (i == 0) {addr = orderDetails.getAddr4midpoint1();}
							else if (i == 1) {addr = orderDetails.getAddr4midpoint2();}
							else if (i == 2) {addr = orderDetails.getAddr4midpoint3();}
							else if (i == 3) {addr = orderDetails.getAddr4midpoint4();}
							midpoints.setAddr(addr);
							appointService.insertMidpoint(midpoints);	
					    	m.clear();
				}
				}
			
				if (orderDetails.getOrder_type() != 3) {
					double price = 0;
						price =orderDetails.getPrice();
					double temp_price = Double.valueOf(orderDetails.getTemp_price());
					double diff_balance = price - temp_price;
					if (orderDetails.getOrderExecId() > 0 && diff_balance != 0) {		// if status == 2 (accept)
						passenger = appointService.findById(orderDetails.getPsgerId());			
						driver = appointService.findById(orderDetails.getDriverId());
						// Table: ts(insert: )	if passenger				
						double curBalance = appointdetailService.getBalanceByUserId(orderDetails.getPsgerId());
						boolean oper = false;
						if (diff_balance > 0) 
							oper = true;			// output user's money				
						m.put("id", 0);
						m.put("order_cs_id", orderDetails.getOrderExecId());
						if (oper == true) {
							m.put("oper", 1);		// output money from passenger
							m.put("ts_type", 3);
						}
						else {
							m.put("oper", 2);		// input money to passenger
							m.put("ts_type", 2);
						}
						curBalance -= diff_balance;
						m.put("ts_way", 4);					
						m.put("balance", curBalance);
						m.put("userid", orderDetails.getDriverId());
						m.put("groupid", 0);
						m.put("unitid", 0);
						m.put("remark", "");
						if (passenger.getAccount() == null) {
							passenger.setAccount("");
						}
						m.put("account", passenger.getAccount());
						m.put("account_type", 1);
						m.put("application", "pinche");
						m.put("sum", diff_balance);
						appointdetailService.insertTsInfo(m);					
						appointdetailService.updateUserBalance_ts(orderDetails.getPsgerId(), Integer.valueOf(m.get("id").toString()));
						m.clear();
						// Table: ts(insert: )	if driver
						curBalance = appointdetailService.getBalanceByUserId(orderDetails.getDriverId());
						m.put("id", 0);
						m.put("order_cs_id", orderDetails.getOrderExecId());
						if (oper == true) {
							m.put("oper", 2);		// input money to driver
							m.put("ts_type", 2);						
						}
						else {
							m.put("oper", 1);		// output money from driver
							m.put("ts_type", 3);
						}
						curBalance += diff_balance;
						m.put("ts_way", 4);
						m.put("balance", curBalance);
						m.put("userid", orderDetails.getPsgerId());
						m.put("groupid", 0);
						m.put("unitid", 0);
						m.put("remark", "");
						if (driver.getAccount() == null) {
							driver.setAccount("");
						}
						m.put("account", driver.getAccount());
						m.put("account_type", 1);
						m.put("application", "pinche");					
						m.put("sum", diff_balance);
						appointdetailService.insertTsInfo(m);
						appointdetailService.updateUserBalance_ts(orderDetails.getDriverId(), Integer.valueOf(m.get("id").toString()));
						m.clear();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
			return true;
		}
	/*
	 *将实体类里的数据 和 进行查询的appoint里的数据进行比较 看是否被修改 被修改的数据放到 修改记录表 中 
	 */
	private void setOrderHistory() {
		
		Map<String, Object> m = new HashMap<String, Object>();
		UserLoginInfo info = (UserLoginInfo) WebUtil._session("user");
		System.out.println("用户的个人信息  :"+info.getId());
		m.put("modifier", info.getId());
		int orderType = orderDetails.getOrder_type();
		m.put("order_exec_id", appointdetailInfo.getOrderExecId());
		if (orderType != 3 && orderDetails.getPrice()!=appointdetailInfo.getPrice()) {
			m.put("md_column", "乘客支付");
			m.put("old_value", String.valueOf(appointdetailInfo.getPrice()));
			m.put("new_value", orderDetails.getPrice());			
			appointdetailService.insertOrderExecHistory(m);
		}
/*		if (orderType == 3 && Double.valueOf(orderDetails.getTotal_sum()) != appointdetailInfo.getTotal_sum()) {
			m.put("md_column", "订单总价");
			m.put("old_value", String.valueOf(appointdetailInfo.getTotal_sum()));
			m.put("new_value", orderDetails.getTotal_sum());
			appointdetailService.insertOrderExecHistory(m);
		}*/
		if (orderType == 1 && orderDetails.getBeginservice_time()!=null &&   !orderDetails.getBeginservice_time().equals(appointdetailInfo.getBeginservice_time())) {
			m.put("md_column", "乘客上车");
			m.put("old_value",appointdetailInfo.getBeginservice_time());
			m.put("new_value", orderDetails.getBeginservice_time());
			appointdetailService.insertOrderExecHistory(m);
		}
		if (orderType == 1 && orderDetails.getPay_time()!=null && appointdetailInfo.getPay_time()!=null &&  !orderDetails.getPay_time().equals(appointdetailInfo.getPay_time())) {
			m.put("md_column", "乘客支付时间");
			m.put("old_value",appointdetailInfo.getPay_time());
			m.put("new_value", orderDetails.getPay_time());
			appointdetailService.insertOrderExecHistory(m);
		}
		if (orderDetails.getStart_addr()!=null && appointdetailInfo.getStart_addr()!=null && !orderDetails.getStart_addr().equals(appointdetailInfo.getStart_addr())) {
			m.put("md_column", "订单起点");
			m.put("old_value", appointdetailInfo.getStart_addr());
			m.put("new_value", orderDetails.getStart_addr());
			appointdetailService.insertOrderExecHistory(m);
		}
		if (orderDetails.getEnd_addr()!=null && appointdetailInfo.getEnd_addr()!=null && !orderDetails.getEnd_addr().equals(appointdetailInfo.getEnd_addr())) {
			m.put("md_column", "订单终点");
			m.put("old_value", appointdetailInfo.getEnd_addr());
			m.put("new_value", orderDetails.getEnd_addr());
			appointdetailService.insertOrderExecHistory(m);
		}
		if (orderDetails.getPs_date()!=null && !orderDetails.getPs_date().equals(appointdetailInfo.getPs_date())) {			
			m.put("md_column", "发布时间");
			m.put("old_value", appointdetailInfo.getPs_date());
			m.put("new_value", orderDetails.getPs_date());
			appointdetailService.insertOrderExecHistory(m);
		}
		if (orderDetails.getAccept_time()!=null && !orderDetails.getAccept_time().equals(appointdetailInfo.getAccept_time())) {			
			m.put("md_column", "成交时间");
			m.put("old_value", appointdetailInfo.getAccept_time());
			m.put("new_value", orderDetails.getAccept_time());
			appointdetailService.insertOrderExecHistory(m);
		}
		if (orderType != 2) {
			if (orderDetails.getBegin_exec_time()!=null && !orderDetails.getBegin_exec_time().equals(appointdetailInfo.getBegin_exec_time())) {			
				m.put("md_column", "开始执行");
				m.put("old_value", appointdetailInfo.getBegin_exec_time());
				m.put("new_value", orderDetails.getBegin_exec_time());
				appointdetailService.insertOrderExecHistory(m);
			}
			if (orderDetails.getDriverarrival_time()!=null && !orderDetails.getDriverarrival_time().equals(appointdetailInfo.getDriverarrival_time())) {			
				m.put("md_column", "车主到达");
				m.put("old_value", appointdetailInfo.getDriverarrival_time());
				m.put("new_value", orderDetails.getDriverarrival_time());
				appointdetailService.insertOrderExecHistory(m);
			}
			if (orderDetails.getStopservice_time()!=null && !orderDetails.getStopservice_time().equals(appointdetailInfo.getEndservice_time())) {			
				m.put("md_column", "结束服务");
				m.put("old_value", appointdetailInfo.getEndservice_time());
				m.put("new_value", orderDetails.getStopservice_time());
				appointdetailService.insertOrderExecHistory(m);
			}
			if (orderDetails.getPre_time()!=null && !orderDetails.getPre_time().equals(appointdetailInfo.getPre_time())) {			
				m.put("md_column", "预约出发");
				m.put("old_value", appointdetailInfo.getPre_time());
				m.put("new_value", orderDetails.getPre_time());
				appointdetailService.insertOrderExecHistory(m);
			}
		}
		if (orderDetails.getStatus() != appointdetailInfo.getStatus()) {			
			m.put("md_column", "当前状态");
			m.put("old_value", String.valueOf(appointdetailInfo.getStatus()));
			m.put("new_value", String.valueOf(orderDetails.getStatus()));
			appointdetailService.insertOrderExecHistory(m);
		}
		if (orderDetails.getReqcarstyle() != appointdetailInfo.getReqcarstyle()) {			
			m.put("md_column", "需求车型");
			m.put("old_value", String.valueOf(appointdetailInfo.getReqcarstyle()));
			m.put("new_value", String.valueOf(orderDetails.getReqcarstyle()));
			appointdetailService.insertOrderExecHistory(m);
		}
		if (orderDetails.getPsgerRemark()!=null && !orderDetails.getPsgerRemark().equals(appointdetailInfo.getPassengerRemark())) {			
			m.put("md_column", "乘客备注");
			m.put("old_value", appointdetailInfo.getPassengerRemark());
			m.put("new_value", orderDetails.getPsgerRemark());
			appointdetailService.insertOrderExecHistory(m);
		}
		if (orderDetails.getDriverRemark()!=null && !orderDetails.getDriverRemark().equals(appointdetailInfo.getDriverRemark())) {			
			m.put("md_column", "车主备注");
			m.put("old_value", appointdetailInfo.getDriverRemark());
			m.put("new_value", orderDetails.getDriverRemark());
			appointdetailService.insertOrderExecHistory(m);
		}
		if (appointdetailInfo.getOrder_type() != 3) {
			List<MidPoints> midpointsList;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("order_type", appointdetailInfo.getOrder_type() - 1);
			map.put("detailsId", appointdetailInfo.getId());
			midpointsList = appointService.findMidPoints(map);
			map.clear();
			String str_addr;
			for (int i=0; i<midpointsList.size(); i++) {
				str_addr = midpointsList.get(i).getAddr();
				switch (midpointsList.get(i).getIndex()) {
				case 0:
					if (orderDetails.getAddr4midpoint1()!=null && !orderDetails.getAddr4midpoint1().equals(str_addr)) {
						m.put("md_column", "中途点1");
						m.put("old_value", str_addr);
						m.put("new_value", orderDetails.getAddr4midpoint1());
						appointdetailService.insertOrderExecHistory(m);
					}
					break;
				case 1:
					if (orderDetails.getAddr4midpoint2()!=null && !orderDetails.getAddr4midpoint2().equals(str_addr)) {
						m.put("md_column", "中途点2");
						m.put("old_value", str_addr);
						m.put("new_value", orderDetails.getAddr4midpoint2());
						appointdetailService.insertOrderExecHistory(m);
					}
					break;
				case 2:
					if (orderDetails.getAddr4midpoint3()!=null && !orderDetails.getAddr4midpoint3().equals(str_addr)) {
						m.put("md_column", "中途点3");
						m.put("old_value", str_addr);
						m.put("new_value", orderDetails.getAddr4midpoint3());
						appointdetailService.insertOrderExecHistory(m);
					}
					break;
				case 3:
					if (orderDetails.getAddr4midpoint4()!=null && !orderDetails.getAddr4midpoint4().equals(str_addr)) {
						m.put("md_column", "中途点4");
						m.put("old_value", str_addr);
						m.put("new_value", orderDetails.getAddr4midpoint4());
						appointdetailService.insertOrderExecHistory(m);
					}
					break;
				default:
					break;
				}
			}
		}
	}
	
	 /*
	  * 退还乘客被冻结的绿点
	  */
	public String cancelFreezePoint() {
		Map<String, Object> json = new HashMap<String, Object>();
		err = Errors.ERR_OK;
		try {
			freezeTsInfo = new FreezeTsInfo();
		//	freezeTsInfo = appointdetailService.findFreezeTsInfo(orderExecId);
			
			if (freezeTsInfo != null) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("order_cs_id", orderExecId);
				m.put("oper", 2);
				m.put("ts_way", 4);
				m.put("balance", freezeTsInfo.getTs_balance());
				m.put("userid", userId);
				m.put("groupid", 0);
				m.put("unitid", 0);
				m.put("remark", "");
				m.put("account", appointdetailService.getBankCard(userId));
				m.put("account_type", 1);
				m.put("application", "pinche");
				m.put("ts_type", 2);
				m.put("sum", freezeTsInfo.getBalance());		
				appointdetailService.insertTsInfo(m);
				m.clear();
				
				UserLoginInfo info = (UserLoginInfo) WebUtil._session("user");
				m.put("adminid", info.getId());
				m.put("balance", freezeTsInfo.getTs_balance());
				m.put("release_ts_id", appointdetailService.getFreezeMaxId());
				appointdetailService.updateFreeze(m);
			} else {
				err = Errors.ERR_SQL_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		json.put("err_code", err);
		json.put("err_msg", "No Freeze_Points");
		resultObject = JSONObject.fromObject(json);
		return SUCCESS;
	}

	/*
	 * 
	 * 显示查询执行总订单被修改的字段的记录数据  
	 */
	public String showEditHistory() {
		try {	
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("orderExecId", orderExecId);
			rowLen = appointdetailService.findOrderExecModifyLog(m).size();
			m.put("page", (page-1)*rows);
			m.put("rows", rows);
			orderModifyLogInfos = appointdetailService.findOrderExecModifyLog(m);
			m.clear();
			ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
			for (OrderModifyLogInfo info : orderModifyLogInfos) {
				Map<String, Object> map = new HashMap<String, Object>();
			//	map.put("modifier", appointService.findById(info.getModifier()).getUsername());
				map.put("md_time", (info.getMd_time()==null)?"":info.getMd_time());
				map.put("md_column", (info.getMd_column()==null)?"":info.getMd_column());
				map.put("old_value", (info.getOld_value()==null)?"":info.getOld_value());
				map.put("new_value", (info.getNew_value()==null)?"":info.getNew_value());
				al.add(map);
			}
			Map<String, Object> json = new HashMap<String, Object>();
			if(rowLen==0){
				json.put("total", 1);
				json.put("rows", 0);
				resultObject = JSONObject.fromObject(json);
			}else{
				json.put("total", rowLen);
				json.put("rows", al);
				resultObject = JSONObject.fromObject(json);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	// Get/Set Functions
	public List<Appointdetail> getAppointdetailList() {
		return appointdetailList;
	}

	public void setAppointdetailList(List<Appointdetail> appointdetailList) {
		this.appointdetailList = appointdetailList;
	}

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}

	public SearchItems getSearchItems() {
		return searchItems;
	}

	public void setSearchItems(SearchItems searchItems) {
		this.searchItems = searchItems;
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

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public int getOrder_type() {
		return order_type;
	}

	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}

	public String getOrder_city() {
		return order_city;
	}

	public void setOrder_city(String order_city) {
		this.order_city = order_city;
	}

	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public int getOrder_type1() {
		return order_type1;
	}

	public void setOrder_type1(int order_type1) {
		this.order_type1 = order_type1;
	}

	public int getOrder_type2() {
		return order_type2;
	}

	public void setOrder_type2(int order_type2) {
		this.order_type2 = order_type2;
	}

	public int getOrder_type3() {
		return order_type3;
	}

	public void setOrder_type3(int order_type3) {
		this.order_type3 = order_type3;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public int getUsertype() {
		return usertype;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}


	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getOrderExecId() {
		return orderExecId;
	}

	public void setOrderExecId(int orderExecId) {
		this.orderExecId = orderExecId;
	}

	public AppointService getAppointService() {
		return appointService;
	}

	public void setAppointService(AppointService appointService) {
		this.appointService = appointService;
	}

	public AppointdetailService getAppointdetailService() {
		return appointdetailService;
	}

	public void setAppointdetailService(AppointdetailService appointdetailService) {
		this.appointdetailService = appointdetailService;
	}

	public RecourseService getRecourseService() {
		return recourseService;
	}

	public void setRecourseService(RecourseService recourseService) {
		this.recourseService = recourseService;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getStart_addr() {
		return start_addr;
	}
	public void setStart_addr(String start_addr) {
		this.start_addr = start_addr;
	}
	public String getEnd_addr() {
		return end_addr;
	}
	public void setEnd_addr(String end_addr) {
		this.end_addr = end_addr;
	}
	public int getReqcarstyle() {
		return reqcarstyle;
	}
	public void setReqcarstyle(int reqcarstyle) {
		this.reqcarstyle = reqcarstyle;
	}

	public String getPsgerRemark() {
		return psgerRemark;
	}
	public void setPsgerRemark(String psgerRemark) {
		this.psgerRemark = psgerRemark;
	}
	public String getDriyerRemark() {
		return driyerRemark;
	}
	public void setDriyerRemark(String driyerRemark) {
		this.driyerRemark = driyerRemark;
	}
	public String getAddr4midpoint1() {
		return addr4midpoint1;
	}
	public void setAddr4midpoint1(String addr4midpoint1) {
		this.addr4midpoint1 = addr4midpoint1;
	}
	public String getAddr4midpoint2() {
		return addr4midpoint2;
	}
	public void setAddr4midpoint2(String addr4midpoint2) {
		this.addr4midpoint2 = addr4midpoint2;
	}
	public String getAddr4midpoint3() {
		return addr4midpoint3;
	}
	public void setAddr4midpoint3(String addr4midpoint3) {
		this.addr4midpoint3 = addr4midpoint3;
	}
	public String getAddr4midpoint4() {
		return addr4midpoint4;
	}
	public void setAddr4midpoint4(String addr4midpoint4) {
		this.addr4midpoint4 = addr4midpoint4;
	}
	public Date getPs_date() {
		return ps_date;
	}
	public void setPs_date(Date ps_date) {
		this.ps_date = ps_date;
	}
	public Date getAccept_time() {
		return accept_time;
	}
	public void setAccept_time(Date accept_time) {
		this.accept_time = accept_time;
	}
	public Date getBegin_exec_time() {
		return begin_exec_time;
	}
	public void setBegin_exec_time(Date begin_exec_time) {
		this.begin_exec_time = begin_exec_time;
	}
	public Date getBeginservice_time() {
		return beginservice_time;
	}
	public void setBeginservice_time(Date beginservice_time) {
		this.beginservice_time = beginservice_time;
	}
	public Date getStopservice_time() {
		return stopservice_time;
	}
	public void setStopservice_time(Date stopservice_time) {
		this.stopservice_time = stopservice_time;
	}
	public Date getPay_time() {
		return pay_time;
	}
	public void setPay_time(Date pay_time) {
		this.pay_time = pay_time;
	}
	public Date getPre_time() {
		return pre_time;
	}
	public void setPre_time(Date pre_time) {
		this.pre_time = pre_time;
	}
	public String getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}
	public Date getDriverarrival_time() {
		return driverarrival_time;
	}
	public void setDriverarrival_time(Date driverarrival_time) {
		this.driverarrival_time = driverarrival_time;
	}
	public int getIsPayed() {
		return isPayed;
	}
	public void setIsPayed(int isPayed) {
		this.isPayed = isPayed;
	}
	public int getIsCatchedCar() {
		return isCatchedCar;
	}
	public void setIsCatchedCar(int isCatchedCar) {
		this.isCatchedCar = isCatchedCar;
	}
	public String getEnableMoney() {
		return enableMoney;
	}
	public void setEnableMoney(String enableMoney) {
		this.enableMoney = enableMoney;
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
	public AppointdetailInfo getAppointdetailInfo() {
		return appointdetailInfo;
	}
	public void setAppointdetailInfo(AppointdetailInfo appointdetailInfo) {
		this.appointdetailInfo = appointdetailInfo;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrder_type(Integer order_type) {
		this.order_type = order_type;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}
	public void setOrderExecId(Integer orderExecId) {
		this.orderExecId = orderExecId;
	}
	public int getWeekdays_1() {
		return weekdays_1;
	}
	public void setWeekdays_1(int weekdays_1) {
		this.weekdays_1 = weekdays_1;
	}
	public int getWeekdays_2() {
		return weekdays_2;
	}
	public void setWeekdays_2(int weekdays_2) {
		this.weekdays_2 = weekdays_2;
	}
	public int getWeekdays_3() {
		return weekdays_3;
	}
	public void setWeekdays_3(int weekdays_3) {
		this.weekdays_3 = weekdays_3;
	}
	public int getWeekdays_4() {
		return weekdays_4;
	}
	public void setWeekdays_4(int weekdays_4) {
		this.weekdays_4 = weekdays_4;
	}
	public int getWeekdays_5() {
		return weekdays_5;
	}
	public void setWeekdays_5(int weekdays_5) {
		this.weekdays_5 = weekdays_5;
	}
	public int getWeekdays_6() {
		return weekdays_6;
	}
	public void setWeekdays_6(int weekdays_6) {
		this.weekdays_6 = weekdays_6;
	}
	public int getWeekdays_7() {
		return weekdays_7;
	}
	public void setWeekdays_7(int weekdays_7) {
		this.weekdays_7 = weekdays_7;
	}
	
}
