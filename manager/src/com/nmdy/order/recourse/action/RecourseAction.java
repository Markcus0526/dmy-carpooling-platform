package com.nmdy.order.recourse.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.nmdy.common.Errors;
import com.nmdy.common.UserLoginInfo;
import com.nmdy.common.WebUtil;
import com.nmdy.order.appoint.action.OrderDetails;
import com.nmdy.order.appoint.dao.Appoint;
import com.nmdy.order.appoint.dao.Customer;
import com.nmdy.order.appoint.dao.LongDistanceInfo;
import com.nmdy.order.appoint.dao.MidPoints;
import com.nmdy.order.appoint.dao.OnoffdutyInfo;
import com.nmdy.order.appoint.dao.TempInfo;
import com.nmdy.order.appoint.service.AppointService;
import com.nmdy.order.appointdetail.dao.AppointdetailInfo;
import com.nmdy.order.appointdetail.service.AppointdetailService;
import com.nmdy.order.recourse.dao.OrderSearch;
import com.nmdy.order.recourse.dao.Recourse;
import com.nmdy.order.recourse.dao.WorkForm;
import com.nmdy.order.recourse.service.RecourseService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 工单管理Action
 * @author xcnana
 *
 */
public class RecourseAction  extends ActionSupport{

	private static final long serialVersionUID = 1L;
	 
	private Recourse recourseInfo=null;
	private Recourse recourseInfotemp=null;
	
	
	private RecourseService recourseService;
	private String checkbox_value;
	
	private OrderDetails orderDetails = null;
	private TempInfo tempInfo = null;
	private LongDistanceInfo longDistanceInfo = null;
	private OnoffdutyInfo onoffdutyInfo = null;

	private Customer passenger = null;
	private Customer driver = null;
	private JSONObject resultObject=null;
	private int id;
	private int total_size;
	private int total_size_atorder;
	//order part
	public Integer form_type;
	public Integer bussi_type;
	public String start_time;
	public String end_time;
	public String city;
	public String customer_name;
	public String phone_incoming;
	public String identity;
	public String process_state;
	public int userid=0;
	private  String  usercode;
	public String userphone;
	public String order_type;
	public String order_state;
	public String order_num;
	public int sex;
	public String reason;
	public String process_result;
	public int status;
	public int admin_id;
	private String username;
	private String phone;
	private int form_agree;
	private long order_cs_id;
	private int identity1;
	private int identity2;
	private int order_type1;
	private int order_type2;
	private int order_type3;
	private int order_state1;
	private int order_state2;
	private int identify;
	private Appoint appoint = null;
	private List<Appoint> appointList = null; 
	private AppointService appointService;
	private AppointdetailService appointdetailService;
	private String method;
	private String param;
	private SearchItems searchItems;
	private int page;
	private int rows;
	private String orderNum;
	
	private static Recourse temp_recourseInfo= new Recourse();
	private static OrderSearchItem temp_orderSearchItem = new OrderSearchItem();
	
	/*
	 * 工单页面的首页面跳转方法
	 */
	public String index()
	{
		return SUCCESS;
	}

	/*
	 * 工单管理的添加新工单页面的跳转方法
	 */
	public String add1()
	{
		return "success";
	}
	/*
	 * 工单管理的添加新工单页面的保存工单信息 方法
	 */
	public String add2(){
		Map<String, Object> json = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_cs_id", ("".equals(order_cs_id))?'0':order_cs_id);
		map.put("bussi_type", ("".equals(bussi_type))?'0':bussi_type);
		map.put("form_type", form_type);
		map.put("phone_incoming", ("".equals(phone_incoming))?'0':phone_incoming);
		map.put("city", ("".equals(city))?'0':city);
		map.put("customer_name", ("".equals(customer_name))?'0':customer_name);
		map.put("sex", sex);
		map.put("reason", ("".equals(reason))?"":reason);
		map.put("process_result",("".equals(process_result))?"":process_result );
		map.put("status", status);
		
		int err = Errors.ERR_OK;
		map.put("username",customer_name);
		
		Date temp = new Date(System.currentTimeMillis());
		map.put("wf_date", temp);
		map.put("workform_num", "8");
		try {
			int affectedRows = recourseService.addRecourse(map);
			if (affectedRows == 0) {
				err = Errors.ERR_SQL_ERROR;
			} 
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		resultObject = JSONObject.fromObject(json);
		return "success";
	}
	
	/*
	 * 添加新工单,修改,处理各页面的查询执行订单的所附带的条件
	 */
	public String ordersearchPage()
	{
		System.out.println(username+"   "+usercode+"    "+phone);
		ActionContext.getContext().get(username);
		ActionContext.getContext().get(usercode);
		ActionContext.getContext().get(phone);
		return "success";
	}
	/*
	 * 添加新工单,修改,处理各页面 查询时的弹出框 里的条件查询方法
	 * 选择订单编号(进行条件查询)的页面
	 */
	public String searchOrder()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", usercode);
		map.put("userPhone", '%'+userphone+'%');
		map.put("userName", '%'+customer_name+'%');
		map.put("beginTime", start_time);
		map.put("endTime", end_time);
		map.put("orderNum", order_num);
		map.put("identity", "condition");
		map.put("identity1",identity1);
		map.put("identity2",identity2);
		map.put("order_type1",order_type1);
		map.put("order_type2",order_type2);
		if(order_type2==2){
			map.put("order_type4", 3);
		}else{
			map.put("order_type4", "");
		}
		if(order_type3==3){
			map.put("order_type3", 4);
		}else{
			map.put("order_type3", "");
		}
		map.put("order_state1",order_state1);
		map.put("order_state2",order_state2);
		map.put("order_type", "condition");
		map.put("order_state", "condition");

		System.out.println(map);
			appointList = appointService.searchforRecourse(map);
			if(appointList==null){
				total_size_atorder=0;
			}else{
				total_size_atorder = appointList.size();
			}
			total_size_atorder = appointList.size();
		map.put("page", (page-1)*rows);
		map.put("rows", rows);	
				appointList = appointService.searchforRecourse(map);
		Date date = null;
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Appoint info : appointList) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", info.getId());
			long userid=info.getPassenger();
			passenger = recourseService.findById(userid);
			driver = recourseService.findById(info.getDriver());
			m.put("orderNum", (info.getOrder_num()==null)?"":info.getOrder_num());			//订单编号
			m.put("order_city", info.getOrder_city());
			m.put("order_type",info.getOrder_type());
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
				date = info.getCr_date();
				if(date!=null){
					m.put("cr_date", sdf.format(date));
					}else{
						m.put("cr_date", "");
					}
			
				date =  info.getTi_accept_order();
				if(date!=null){
					m.put("ti_accept_order",sdf.format(date));
					}else{
						m.put("ti_accept_order","");
					}
				
				date =  info.getBegin_exec_time();
				if(date!=null){
					m.put("begin_exec_time",sdf.format(date));
					}else{
						m.put("begin_exec_time","");
					}
				
				date = info.getDriverarrival_time();
				if(date!=null){
					m.put("driverarrival_time",sdf.format(date));
					}else{
						m.put("driverarrival_time","");
					}
				
				date =  info.getBeginservice_time();
				if(date!=null){
					m.put("beginservice_time",sdf.format(date));
					}else{
						m.put("beginservice_time","");
					}
				date =  info.getStopservice_time();
				if(date!=null){
					m.put("stopservice_time",sdf.format(date));
					}else{
						m.put("stopservice_time","");
					}
	
				date =  info.getPay_time();
				if(date!=null){
					m.put("pay_time",sdf.format(date));
					}else{
						m.put("pay_time","");
					}
				
				date = info.getPass_cancel_time();
				if(date!=null){
					m.put("pass_cancel_time",sdf.format(date));
					}else{
					m.put("pass_cancel_time","");
					}
				
				date = (Date) info.getDriver_cancel_time();
				if(date!=null){
				m.put("driver_cancel_time",sdf.format(date));
				}else{
					m.put("driver_cancel_time","");
				}
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		if(total_size_atorder==0){
			json.put("total", 1);
			json.put("rows", 0);
		}else{
			json.put("total", total_size_atorder);
			json.put("rows", al);	
		}
		
		resultObject = JSONObject.fromObject(json);
		System.out.println(resultObject);
		return SUCCESS;
	}
	
	/*
	 * 工单管理的查看页面的跳转方法
	 */
	public String view1()
	{
		ActionContext.getContext().put("id", id);
		return SUCCESS;
	}

	/*
	 * 工单管理的修改页面的跳转方法
	 */
	public 	String edit1()
	{ 
		ActionContext.getContext().put("id", id);
	    return "success";
	}
	
	/*
	 * 工单管理的修改页面的 修改方法
	 */
	public 	String edit2()
	{
		Map<String,Object> params=new HashMap<String,Object>();
		recourseInfo = recourseService.findOnebyid(Integer.valueOf(id));
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		try {
			params.put("id", id);
			params.put("order_cs_id", ("".equals(order_cs_id))?'0':order_cs_id);
			params.put("form_agree", ("".equals(form_agree))?'0':form_agree);
			params.put("form_type", form_type);
			params.put("bussi_type", bussi_type);
			params.put("phone_incoming", ("".equals(bussi_type))?'0':phone_incoming);
			params.put("admin_id", ("".equals(admin_id))?'0':admin_id);
			params.put("customer_name", ("".equals(bussi_type))?'0':customer_name);
			params.put("sex", sex);
			params.put("reason", ("".equals(bussi_type))?"":reason);
			params.put("process_result",("".equals(bussi_type))?"":process_result );
			params.put("status", status);
			if(form_agree == 1){
				recourseInfotemp = recourseService.findOnebyid(id);
				Map<String, Object> map = new HashMap<String, Object>();
			UserLoginInfo info = (UserLoginInfo)WebUtil._session("user");
				if(order_cs_id==0){
					map.put("userid", recourseInfotemp.getId());
					map.put("desc", recourseInfotemp.getReason());
					map.put("order_exec_id", recourseInfotemp.getOrder_cs_id());
					map.put("auditor", info.getId());
					map.put("abb_type", 3);
					try{
						recourseService.addAbb_Record(map);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				else{
					appoint = appointService.findOneById(recourseInfotemp.getOrder_cs_id());
						if(identify==1){
							map.put("abb_type", 4);
							map.put("userid", appoint.getDriver());
						}else if(identify==2){
							map.put("abb_type", 3);
							map.put("userid", appoint.getPassenger());
						}
					map.put("desc", reason);
					map.put("order_exec_id", order_cs_id);
					map.put("auditor", /*info.getId()*/ 0);
					try{
						recourseService.addAbb_Record(map);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			int affectedRows = recourseService.updateRecourse1(params);
			if (affectedRows == 0) {
				err = Errors.ERR_SQL_ERROR;
			} 
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		resultObject = JSONObject.fromObject(json);
		return SUCCESS;
	}
	
	/*
	 * 工单管理的处理页面的跳转方法
	 */
	public String process1(){
		ActionContext.getContext().put("id", id); 
		return "success";
	}
	
	/*
	 * 工单管理的处理页面的处理方法
	 */
	public String process2()
	{	
       Map<String,Object> params=new HashMap<String,Object>();
            params.put("id", id);
		 
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		try {
	    params.put("process_result", process_result);
	    params.put("order_cs_id", order_cs_id);
		if(form_agree == 1){
			recourseInfotemp = recourseService.findOnebyid(id);
			Map<String, Object> map = new HashMap<String, Object>();
			//UserLoginInfo info = (UserLoginInfo)WebUtil._session("user");
			if(order_cs_id==0){
				map.put("userid", recourseInfotemp.getId());
				map.put("desc", recourseInfotemp.getReason());
				map.put("order_exec_id", recourseInfotemp.getOrder_cs_id());
				map.put("auditor", /*info.getId()*/ 0);
				map.put("abb_type", 3);
				try{
					recourseService.addAbb_Record(map);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			else{
				appoint = appointService.findOneById(order_cs_id);
					if(identify==1){
						map.put("abb_type", 4);
						map.put("userid", appoint.getDriver());
					}else if(identify==2){
						map.put("abb_type", 3);
						map.put("userid", appoint.getPassenger());
					}
				map.put("desc", reason);
				map.put("order_exec_id", order_cs_id);
				map.put("auditor", /*info.getId()*/ 0);
				try{
					recourseService.addAbb_Record(map);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
			params.put("form_agree", form_agree);
			System.out.println(params);
			int affectedRows = recourseService.updateRecourse2(params);
			if (affectedRows == 0) {
				err = Errors.ERR_SQL_ERROR;
			} 
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		resultObject = JSONObject.fromObject(json);
		return SUCCESS;
	}
	
	/*
	 * 添加新工单,修改,处理各页面 的查询方法查询的数据进行选择执行定单id
	 * 通过订单号查询执行订单的详情数据
	 */
	public String findOrder_num(){
		
		System.out.println(order_cs_id);
		 Appoint appoint=recourseService.findOrder_num(order_cs_id);
		Map<String, Object> map = new HashMap<String, Object>();
		if(appoint==null){
			map.put("0", 0);
		}else{
			int ordertype= appoint.getOrder_type();
			if(ordertype==3){
				ordertype=2;
			}if(ordertype==4){
				ordertype=3;
			}
		map.put("id", appoint.getId());
		map.put("order_type",ordertype);
		AppointdetailInfo appointdetailInfo = appointdetailService.findOrderdetailInfo(map);
		    map.clear();
			int order_type =appoint.getOrder_type() ;
			long id=appoint.getId();
			map.put("order_type",  order_type);
			String city=recourseService.findCityName(appoint.getOrder_city());
			map.put("order_city",  city);
			if(appoint==null || "".equals(appoint)){
				map.put("driverid", "");
				map.put("drivercode", "");
				map.put("drivername", "");
				map.put("driverphone", "");
				map.put("start_addr", "");
				map.put("end_addr","" );
				map.put("price","");
			}else{
			driver = recourseService.findById(appoint.getDriver());
			if (driver != null) {
				map.put("driverid", driver.getId());
				map.put("drivercode", driver.getUsercode());
				map.put("drivername", driver.getUsername());
				map.put("driverphone", driver.getPhone());
			}	
			if(appoint.getFrom_()!=null){
				map.put("start_addr", appoint.getFrom_());
			}else{
				map.put("start_addr", "");
			}
			if(appoint.getTo_()!=null){
				map.put("end_addr", appoint.getTo_());
			}else{
				map.put("end_addr","" );
			}
			map.put("price", (appoint.getPrice()==0)?"0":appoint.getPrice());
			}
			
			passenger = recourseService.findById(appoint.getPassenger());
			if (passenger != null) {
				map.put("passagerid", passenger.getId());
				map.put("passagercode", passenger.getUsercode());
				map.put("passagername", passenger.getUsername());
				map.put("passagerphone", passenger.getPhone());
			}
		
			map.put("password", appoint.getPassword());
			map.put("order_num", appoint.getId());
			map.put("id", order_cs_id);
			// MidPoints
			List<MidPoints> midpointsList;
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("order_type", order_type-1);
			map1.put("detailsId", id);
			midpointsList = appointService.findMidPoints(map1);
			int midpoint=0;
			if(midpointsList==null && "".equals(midpointsList)){
				midpoint=0;
			}else{
				midpoint=midpointsList.size();
			}
			map.put("midpoint", midpoint);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (appoint.getTi_accept_order()!=null) 
			map.put("accept_time",sdf.format(appoint.getTi_accept_order()));
			if (appoint.getBegin_exec_time() !=null) 
			map.put("begin_exec_time",  sdf.format(appoint.getBegin_exec_time()));
			int status=appoint.getStatus();
			map.put("status",status);
		}
		System.out.println("------:"+map);
		resultObject=JSONObject.fromObject(map);
		System.out.println(resultObject);	
		return "success";
	}
	
	/*
	 * 添加新工单页面
	 * 通过电话号码查询用户姓名和性别
	 */
	public String findNameSex(){
		OrderSearch orderSearch ;
		Map<String,Object> m=new HashMap<String,Object>();
		if(!"".equals(phone)){
			m.put("phone", phone);
		}else{
			m.put("phone", "");	
		}
		
		orderSearch = recourseService.findNameSex(m);
		Map<String,Object> map =new HashMap<String,Object>();
		if(orderSearch!=null){
		map.put("username", orderSearch.getUsername());
		map.put("sex", orderSearch.getSex());
		map.put("city_cur", orderSearch.getCity_cur());
		}else{
			map.put("username", "");
			map.put("sex", 0);
			map.put("city_cur", "");
		}
		System.out.println(map);
		resultObject=JSONObject.fromObject(map);
		return "success";
	}
	/*
	 * 查看页面  的通过按id查询工单信息数据
	 */
	public String findDetails(){
		WorkForm workForm=recourseService.findDetails(id);
		resultObject = JSONObject.fromObject(workForm);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("adm_id",workForm.getAdm_id());
		map.put("bussi_type",workForm.getBussi_type());
		map.put("city",workForm.getCity());
		map.put("customer_name",workForm.getCustomer_name());
		map.put("form_type",workForm.getForm_type());
		map.put("id",workForm.getId());
		map.put("order_cs_id",workForm.getOrder_cs_id());
		map.put("phone_incoming",workForm.getPhone_incoming());
		map.put("sex",workForm.getSex());
		map.put("workform_num",workForm.getWorkform_num());
		map.put("status",workForm.getStatus());
		map.put("wf_date",workForm.getWf_date());
		map.put("from_agree",workForm.getForm_agree());
		map.put("process_result",workForm.getProcess_result());
		map.put("from_agree",workForm.getForm_agree());
		System.out.println(resultObject);
		return "success";
	}
	/*
	 * 工单首页 通过条件查询 显示工单数据信息
	 */
	public String show1()
	{
		List<Recourse> recourseInfoList=null;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("form_type", form_type);
				map.put("bussi_type",bussi_type);
				map.put("start_time", start_time);
				map.put("end_time", end_time);
				map.put("city", city);
				map.put("customer_name", customer_name);
				map.put("phone_incoming", phone_incoming);
				map.put("identity", identity);
				map.put("process_state", process_state);
				map.put("page", (page-1)*rows);
				map.put("rows", rows);
				System.out.println(map);
			 recourseInfoList = recourseService.search(map);
			 if(recourseInfoList==null){
				 total_size=0;
			 }else{
				 total_size = recourseInfoList.size();	 
			 }
			 total_size = recourseInfoList.size();
				System.out.println(recourseInfoList);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(total_size==0){
			 jsonMap.put("total",1);
				// 需要实现数据的总记录数
				jsonMap.put("rows",0);
		}else{
			 jsonMap.put("total",total_size);
				// 需要实现数据的总记录数
				jsonMap.put("rows",recourseInfoList);
		}
		
		    
			resultObject = JSONObject.fromObject(jsonMap);
			System.out.println(resultObject);
		return "success";
	}
	/*
	 * 批量删除工单信息
	 */
	public String batchDel() {
		HttpServletRequest request = ServletActionContext.getRequest();
	
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		String ids[] = request.getParameterMap().get("ids")[0].toString().split(",");
		for (String id : ids) {
			try {
				int affectedRows = recourseService.deleteRecourse(Integer.valueOf(id));

				if (affectedRows == 0)
					err = Errors.ERR_SQL_ERROR;
				
			} catch (Exception e) {
				e.printStackTrace();
				err = Errors.ERR_SQL_ERROR;
			}
		}
		
		json.put("err_code", err);
		resultObject = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
	/*
	 * 逻辑删除单个的工单数据信息
	 */
	public String delete() {
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		
		try {
			int affectedRows = recourseService.deleteRecourse(id);
			

			if (affectedRows == 0)
				err = Errors.ERR_SQL_ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		
		json.put("err_code", err);
		resultObject = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
	/*
	 * 查看    执行订单详情页面   跳转页面
	 */
	public String viewInDetail1(){
		HttpServletRequest request =  ServletActionContext.getRequest();	
	appoint = appointService.findOneById(id);
	ActionContext.getContext().put("id", id);
	request.setAttribute("order_type", appoint.getOrder_type());
	System.out.println(id+"     ..................     "+ appoint.getOrder_type());
		return "success";
	}
	/*
	 *显示 查看通过 id  执行订单详情页面 数据信息
	 */
	public String viewInDetail()
	{
		Map<String,Object> m=new HashMap<String,Object>();
		try {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			appoint = appointService.findOneById(id);
			driver = recourseService.findById(appoint.getDriver());			
			passenger = recourseService.findById(appoint.getPassenger());
			m.put("order_type", appoint.getOrder_type());
			switch (appoint.getOrder_type()) {
			case 1:
				tempInfo = appointService.findTempInfo(id);
				m.put("order_num", tempInfo.getOrder_num());
				m.put("total_sum", tempInfo.getPrice());
				m.put("start_addr", tempInfo.getStart_addr());
				m.put("end_addr", tempInfo.getEnd_addr());
				if (tempInfo.getPs_date() != null) tempInfo.getPs_date();
				if (tempInfo.getPre_time() != null) tempInfo.getPre_time();
				m.put("reqcarstyle", tempInfo.getReqcarstyle());
				m.put("psgerRemark", tempInfo.getRemark());
				break;
			case 2:
				tempInfo = appointService.findTempInfo(id);
				m.put("order_num", tempInfo.getOrder_num());
				m.put("total_sum", tempInfo.getPrice());
				m.put("start_addr", tempInfo.getStart_addr());
				m.put("end_addr", tempInfo.getEnd_addr());
				if (tempInfo.getPs_date() != null) tempInfo.getPs_date();
				if (tempInfo.getPre_time() != null) tempInfo.getPre_time();
				m.put("reqcarstyle", tempInfo.getReqcarstyle());
				m.put("psgerRemark", tempInfo.getRemark());
				break;
			case 3:
				onoffdutyInfo = appointService.findOnoffdutyInfo(id);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("orderType", appoint.getOrder_type()-1);
				map.put("detailsId", onoffdutyInfo.getOrderdetails_id());
				List<MidPoints> midpointsList;
				midpointsList = appointService.findMidPoints(map);
				for (int i=0; i<midpointsList.size(); i++) {
					switch (midpointsList.get(i).getIndex()) {
					case 0:
						m.put("addr4midpoint1", midpointsList.get(i).getAddr());
						break;
					case 1:
						m.put("addr4midpoint2", midpointsList.get(i).getAddr());
						break;
					case 2:
						m.put("addr4midpoint3", midpointsList.get(i).getAddr());
						break;
					default:
						m.put("addr4midpoint4", midpointsList.get(i).getAddr());
						break;
					}
				}
				m.put("order_num", onoffdutyInfo.getOrder_num());
				m.put("total_sum", onoffdutyInfo.getPrice());
				m.put("start_addr", onoffdutyInfo.getStart_addr());
				m.put("end_addr", onoffdutyInfo.getEnd_addr());
				if (onoffdutyInfo.getPublish_date() != null) 
					m.put("ps_date", onoffdutyInfo.getPublish_date());
				if (onoffdutyInfo.getPre_time() != null) 
					m.put("pre_time", sdf.format(onoffdutyInfo.getPre_time()));
				m.put("reqcarstyle", onoffdutyInfo.getReqcarstyle());
				m.put("psgerRemark", onoffdutyInfo.getRemark());
				m.put("weekdays", onoffdutyInfo.getWhich_days());
				break;
			case 4:
				longDistanceInfo = appointService.findLongDistanceInfo(id);
				m.put("order_num", longDistanceInfo.getOrder_num());
				m.put("total_sum", longDistanceInfo.getSeat_num() * longDistanceInfo.getPrice());
				m.put("start_addr", longDistanceInfo.getStart_city());
				m.put("end_addr", longDistanceInfo.getEnd_city());
				if (longDistanceInfo.getPs_time() != null)
					longDistanceInfo.getPs_time();
				if (longDistanceInfo.getPre_time() != null) 
					longDistanceInfo.getPre_time();
				m.put("driverRemark", longDistanceInfo.getRemark());
				break;
			default:
				break;
			}			
			m.put("order_city", appoint.getOrder_city());
			if (driver != null) {
				m.put("driverCode",driver.getUsercode());
				m.put("driverName", driver.getUsername());
				m.put("driverPhone", driver.getPhone());
			}
			if (passenger != null) {
				m.put("passagerCode", passenger.getUsercode());
				m.put("passagerName", passenger.getUsername());
				m.put("passagerPhone", passenger.getPhone());
			}
			m.put("price", appoint.getPrice());
			m.put("status", appoint.getStatus());
			Map<String,Object> map1=new HashMap<String,Object>();
			map1.put("userid", appoint.getPassenger());
			map1.put("id", appoint.getId());
			int total1=appointService.findAccountFreeze(map1);
			int total2=0;
			if(total1==0){
				m.put("enableMoney", "0");
			}else{
				total2=appointService.findAccountUnfreeze(map1);
				if(total2==0){
					m.put("enableMoney", "0");
				}else{
					Map<String,Object> statusData = appointService.findFreezeStatus(appoint.getId());
					BigDecimal freeze=(BigDecimal)statusData.get("freeze") ;
					BigDecimal unfreeze=(BigDecimal)statusData.get("unfreeze") ;
					if (freeze==unfreeze) {
						m.put("enableMoney", "0");
					} else {
						m.put("enableMoney", "1");
					}
				}
			}
	
			
			if (appoint.getBeginservice_time() != null) {
				m.put("isCatchedCar", 1);
				m.put("beginservice_time", sdf.format(appoint.getBeginservice_time()));
			} else {
				m.put("isCatchedCar", 2);
			}
			
			if (appoint.getPay_time() != null) {
				m.put("isPayed",1);
				m.put("pay_time", sdf.format(appoint.getPay_time()));
			} else {
				m.put("isPayed", 2);
			}
			
			if (appoint.getTi_accept_order() != null) 
				m.put("accept_time", sdf.format(appoint.getTi_accept_order()));
			if (appoint.getBegin_exec_time() != null) 
				m.put("beginservice_time", sdf.format(appoint.getBegin_exec_time()));
			if (appoint.getDriverarrival_time() != null) 
				m.put("driverarrival_time", sdf.format(appoint.getDriverarrival_time()));
			if (appoint.getStopservice_time() != null) 
				m.put("stopservice_time", sdf.format(appoint.getStopservice_time()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultObject = JSONObject.fromObject(m);
		return SUCCESS;
	}


	/*
	 * getter 和 setter 方法
	 */
	public Recourse getRecourseInfotemp() {
		return recourseInfotemp;
	}
	public void setRecourseInfotemp(Recourse recourseInfotemp) {
		this.recourseInfotemp = recourseInfotemp;
	}
	public int getTotal_size() {
		return total_size;
	}
	public void setTotal_size(int total_size) {
		this.total_size = total_size;
	}
	public int getTotal_size_atorder() {
		return total_size_atorder;
	}
	public void setTotal_size_atorder(int total_size_atorder) {
		this.total_size_atorder = total_size_atorder;
	}
	public static Recourse getTemp_recourseInfo() {
		return temp_recourseInfo;
	}
	public static void setTemp_recourseInfo(Recourse temp_recourseInfo) {
		RecourseAction.temp_recourseInfo = temp_recourseInfo;
	}
	public static OrderSearchItem getTemp_orderSearchItem() {
		return temp_orderSearchItem;
	}
	public static void setTemp_orderSearchItem(OrderSearchItem temp_orderSearchItem) {
		RecourseAction.temp_orderSearchItem = temp_orderSearchItem;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public OrderDetails getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
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
	public String getCheckbox_value() {
		return checkbox_value;
	}
	public void setCheckbox_value(String checkbox_value) {
		this.checkbox_value = checkbox_value;
	}
	public Appoint getAppoint() {
		return appoint;
	}
	public void setAppoint(Appoint appoint) {
		this.appoint = appoint;
	}
	public List<Appoint> getAppointList() {
		return appointList;
	}
	public void setAppointList(List<Appoint> appointList) {
		this.appointList = appointList;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
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
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public SearchItems getSearchItems() {
		return searchItems;
	}
	public void setSearchItems(SearchItems searchItems) {
		this.searchItems = searchItems;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Recourse getRecourseInfo() {
		return recourseInfo;
	}
	public void setRecourseInfo(Recourse recourseInfo) {
		this.recourseInfo = recourseInfo;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderInfo(){
		return SUCCESS;
	}

	public Integer getForm_type() {
		return form_type;
	}

	public void setForm_type(Integer form_type) {
		this.form_type = form_type;
	}

	public Integer getBussi_type() {
		return bussi_type;
	}

	public void setBussi_type(Integer bussi_type) {
		this.bussi_type = bussi_type;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	public String getPhone_incoming() {
		return phone_incoming;
	}
	public void setPhone_incoming(String phone_incoming) {
		this.phone_incoming = phone_incoming;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getProcess_state() {
		return process_state;
	}
	public void setProcess_state(String process_state) {
		this.process_state = process_state;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public String getOrder_state() {
		return order_state;
	}
	public void setOrder_state(String order_state) {
		this.order_state = order_state;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public JSONObject getResultObject() {
		return resultObject;
	}
	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getProcess_result() {
		return process_result;
	}
	public void setProcess_result(String process_result) {
		this.process_result = process_result;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getForm_agree() {
		return form_agree;
	}
	public void setForm_agree(int form_agree) {
		this.form_agree = form_agree;
	}
	public RecourseService getRecourseService() {
		return recourseService;
	}
	public void setRecourseService(RecourseService recourseService) {
		this.recourseService = recourseService;
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
	public long getOrder_cs_id() {
		return order_cs_id;
	}
	public void setOrder_cs_id(long order_cs_id) {
		this.order_cs_id = order_cs_id;
	}
	public int getIdentity1() {
		return identity1;
	}
	public void setIdentity1(int identity1) {
		this.identity1 = identity1;
	}
	public int getIdentity2() {
		return identity2;
	}
	public void setIdentity2(int identity2) {
		this.identity2 = identity2;
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
	public int getOrder_state1() {
		return order_state1;
	}
	public void setOrder_state1(int order_state1) {
		this.order_state1 = order_state1;
	}
	public int getOrder_state2() {
		return order_state2;
	}
	public void setOrder_state2(int order_state2) {
		this.order_state2 = order_state2;
	}
	public int getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(int admin_id) {
		this.admin_id = admin_id;
	}
	public int getIdentify() {
		return identify;
	}
	public void setIdentify(int identify) {
		this.identify = identify;
	}


	public int getUserid() {
		return userid;
	}


	public void setUserid(int userid) {
		this.userid = userid;
	}
	
}

