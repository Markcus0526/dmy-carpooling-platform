package com.nmdy.order.appoint.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;
import com.nmdy.common.Errors;
import com.nmdy.common.UserLoginInfo;
import com.nmdy.common.WebUtil;
import com.nmdy.order.appoint.dao.Appoint;
import com.nmdy.order.appoint.dao.Appoint1;
import com.nmdy.order.appoint.dao.Customer;
import com.nmdy.order.appoint.dao.LongDistanceInfo;
import com.nmdy.order.appoint.dao.MidPoints;
import com.nmdy.order.appoint.dao.OnoffdutyInfo;
import com.nmdy.order.appoint.dao.TempInfo;
import com.nmdy.order.appoint.service.AppointService;
import com.nmdy.order.appointdetail.dao.FreezeTsInfo;
import com.nmdy.order.appointdetail.dao.OrderModifyLogInfo;
import com.nmdy.order.appointdetail.service.AppointdetailService;
import com.nmdy.order.recourse.service.RecourseService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 执行订单管理的Action
 * @author xcnana
 *
 */
public class AppointAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	int err;
	
	// index
	private Integer page;
	private Integer rows;
	private int rowLen;
	private JSONObject resultObject;
	private SearchItems searchItems;
	private Long id;
	private String order_city;
	private Date beginTime;
	private Date endTime;
	private String order_num;
	private int order_type;
	private int order_type1;
	private int order_type2;
	private int order_type3;
	private int order_type4;
	private Short status=0;
	private String usercode;
	private String username;
	private String userphone;
	private Integer dept;
	private String nickname;
	private Date begin_exec_time;
	private Date beginservice_time;
	private Date stopservice_time;
	private Date pay_time;
	private Date pre_time;
	private String weekdays;
	private Date driverarrival_time;
	private int passagerId;
	private int driverId;
    private int choose;
	private int orderType;
	private double temp_price;
	private String start_addr;
	private String end_addr;
	private int reqcarstyle;
	private String psgerRemark;
	private String driyerremark;
	private String addr4midpoint1;
	private String addr4midpoint2;
	private String addr4midpoint3;
	private String addr4midpoint4;
	private Date ps_date;
	private Date accept_time;
	private String password;
	private int orderExecId;
	

	private Appoint appoint = null;
	private TempInfo tempInfo = null;
	private LongDistanceInfo longDistanceInfo = null;
	private OnoffdutyInfo onoffdutyInfo = null;
	private List<Appoint> appointList = null;
	private List<Appoint1> appointList1 = null;
	private List<OrderModifyLogInfo> orderModifyLogInfos=null;
	private Customer passenger = null;
	private Customer driver = null;
	 
	private String param;
	
	private OrderDetails orderDetails = null;
	private  AppointService appointService;
	private RecourseService recourseService;
	
	private AppointdetailService appointDetailService;
	private String method;
		
	public AppointAction() {		
	}	
	/*
	 * 执行订单首页面的跳转方法,状态是指查询或者修改钱的状态和修改后的状态一只
	 * 默认为第二种状态:订单发布
	 */
	public String index() {
		if(status>0){
	  ActionContext.getContext().put("status", status);
		}else{
			ActionContext.getContext().put("status", 2);
		}
		return SUCCESS;
	}

	/*
	 * 执行订单首页面的查询方法
	 * 按条件查询执行订单记录
	 */
	public String search() {		
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("order_city", '%'+order_city+'%');
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("order_num", order_num);
			map.put("order_type", "value");
			map.put("order_type1", order_type1);
			map.put("order_type2", order_type2);
			if(order_type2==2){
				map.put("order_type4", 3);
			}else{
				map.put("order_type3", "");
			}
			if(order_type3==3){
				map.put("order_type3", 4);
			}else{
				map.put("order_type3", "");
			}
			map.put("status", status);
			map.put("usercode",'%'+usercode+'%');
			map.put("username", '%'+username+'%');
			map.put("userphone", '%'+userphone+'%');
			map.put("dept", dept);
			map.put("nickname", nickname);
			System.out.println(map);
			appointList1 = appointService.search(map);
			System.out.println(appointList1);
			  rowLen = appointList1.size();
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			appointList1 = appointService.search(map);		
			
			Date date = null;
			List<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
			for (Appoint1 info : appointList1) {
				
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", info.getId());
				passenger = recourseService.findById(info.getPassenger());
				driver = recourseService.findById(info.getDriver());
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
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				m.put("status", info.getStatus());
				
				date = info.getCr_date();
				if(date!=null){
					m.put("cr_date", sdf.format(date));	
				}
				date =  info.getTi_accept_order();
				if(date!=null){
					m.put("ti_accept_order",sdf.format(date));				
							}
				date =  info.getBegin_exec_time();
				if(date!=null){
					m.put("begin_exec_time",sdf.format(date));				
							}
				date = info.getDriverarrival_time();
				if(date!=null){
					m.put("driverarrival_time",sdf.format(date));
				}
				date =  info.getBeginservice_time();
				if(date!=null){
					m.put("beginservice_time",sdf.format(date));	
				}
				date =  info.getStopservice_time();
				if(date!=null){
					m.put("stopservice_time",sdf.format(date));	
				}
				date =  info.getPay_time();
				if(date!=null){
					m.put("pay_time",sdf.format(date));	
				}
				date = info.getPass_cancel_time();
				if(date!=null){
				m.put("pass_cancel_time",sdf.format(date));
				}
				date = (Date) info.getDriver_cancel_time();
				if(date!=null){
				m.put("driver_cancel_time",sdf.format(date));
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
			System.out.println("resultObject:"+resultObject);
		return "success";
	}
	
	/*
	 * 执行订单的修改页面的跳转页面
	 */
	public String edit1(){
		HttpServletRequest request = ServletActionContext.getRequest();
		 ActionContext.getContext().put("id", id);
		 ActionContext.getContext().put("choose", choose);
		 appoint = appointService.findOneById(id);
		 int order_type =appoint.getOrder_type() ;
			request.setAttribute("order_type", order_type);
			request.setAttribute("orderExecId", id);
			System.out.println(id+"        "+order_type);
			
		return "success";
	}
	
	/*
	 * 执行订单页面的修改方法
	 */
	public String edit() {		
		
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		/*
		 * OrderDetailsInfo()方法是将页面的数据以及通过页面数据查询的数据 放入到实体类里
		 */
		if(OrderDetailsInfo()==true){
		try {			
			appoint = appointService.findOneById(orderDetails.getOrderExecId());
			/*
			 * editOrderExecInfo()方法是将实体类的数据进行update 和 insert 操作
			 */
			if (editOrderExecInfo() == false)
				err = Errors.ERR_SQL_ERROR;		
			/*
			 *将实体类里的数据 和 进行查询的appoint里的数据进行比较 看是否被修改 被修改的数据放到 修改记录表 中 
			 */
			   OrderHistory();
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}	
		}
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));	
		ActionContext.getContext().put("status", status);
		resultObject=JSONObject.fromObject(json);
		return SUCCESS;
	}
	/*
	 * edit 的子方法
	 * OrderDetailsInfo()方法是将页面的数据以及通过页面数据查询的数据 放入到实体类里
	 */
	private boolean OrderDetailsInfo() {
		try {
		
			if (orderDetails == null)
				orderDetails = new OrderDetails();
			appoint = appointService.findOneById(orderExecId);
			orderDetails.setOrderExecId(id);
			int order_type = appoint.getOrder_type();
			orderDetails.setOrder_type(orderType);
			orderDetails.setOrder_city(appoint.getOrder_city());
			
			driver = recourseService.findById(appoint.getDriver());
			if (driver != null) {
				orderDetails.setDriverId(driver.getId());
				orderDetails.setDriverCode(driver.getUsercode());
				orderDetails.setDriverName(driver.getUsername());
				orderDetails.setDriverPhone(driver.getPhone());
			}
			if (order_type == 3) {
				orderDetails.setTotal_sum(appoint.getPrice());
			} else {
				orderDetails.setPrice(appoint.getPrice());
			}
			passenger = recourseService.findById(appoint.getPassenger());
			if (passenger != null) {
				orderDetails.setPassagerId(passenger.getId());
				orderDetails.setPassagerCode(passenger.getUsercode());
				orderDetails.setPassagerName(passenger.getUsername());
				orderDetails.setPassagerPhone(passenger.getPhone());
			}
			
			orderDetails.setTemp_price(temp_price);
			if (appoint.getBeginservice_time() != null) {
				orderDetails.setIsCatchedCar(1);
				orderDetails.setBeginservice_time(beginservice_time);
			} else {
				orderDetails.setIsCatchedCar(2);
			}
			if (appoint.getPay_time() != null) {
				orderDetails.setIsPayed(1);
				orderDetails.setPay_time(pay_time);
			} else {
				orderDetails.setIsPayed(2);
			}
			orderDetails.setStart_addr(start_addr);
			orderDetails.setEnd_addr(end_addr);
			orderDetails.setPassword(password);
			Long detailsId=null; 
			String order_num = "";
			switch (order_type) {
			case 1:
				tempInfo = appointService.findTempInfo(id);
				detailsId = tempInfo.getId();
				order_num = tempInfo.getOrder_num();
				orderDetails.setOrder_num(order_num);
				orderDetails.setReqcarstyle(reqcarstyle);
				orderDetails.setPsgerRemark(psgerRemark);
				orderDetails.setDriverRemark(driyerremark);
				break;
			case 2:
				tempInfo = appointService.findTempInfo(id);
				detailsId = tempInfo.getId();
				order_num = tempInfo.getOrder_num();
				orderDetails.setOrder_num(order_num);
				orderDetails.setReqcarstyle(reqcarstyle);
				orderDetails.setPsgerRemark(psgerRemark);
				orderDetails.setDriverRemark(driyerremark);
				break;
			case 3:
				onoffdutyInfo = appointService.findOnoffdutyInfo(id);
				if(onoffdutyInfo!=null){
					detailsId = onoffdutyInfo.getId();
					order_num = onoffdutyInfo.getOrder_num();
					orderDetails.setWeekdays(onoffdutyInfo.getWhich_days());
				}else{
					detailsId = 0l;
					order_num = "";
					orderDetails.setWeekdays("");
				}
				
				orderDetails.setOrder_num(order_num);
				orderDetails.setReqcarstyle(reqcarstyle);
				orderDetails.setPsgerRemark(psgerRemark);
				orderDetails.setDriverRemark(driyerremark);
				
				break;
			case 4:
				longDistanceInfo = appointService.findLongDistanceInfo(id);
				if(longDistanceInfo!=null){
					detailsId = longDistanceInfo.getId();
					order_num = longDistanceInfo.getOrder_num();
				}
				else{
					detailsId=0l;
					order_num="";
				}
				orderDetails.setOrder_num(order_num);
				orderDetails.setDriverRemark(driyerremark);
				break;
			default:
				break;
			}
			
			orderDetails.setId(detailsId);
			// MidPoints
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderType", orderDetails.getOrder_type() - 1);
			map.put("detailsId", detailsId);
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
			
			// breaking Button "退还乘客冻结的绿点"
			if (orderDetails.getStatus() != 8) {
				orderDetails.setIsBreaked(1);
			} else if (appointService.getBreakAppointCnt(order_num) > 0) {
				orderDetails.setIsBreaked(1);
			} else {
				orderDetails.setIsBreaked(0);
			}
			System.out.println("-----orderDetails:-----"+orderDetails);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	/*
	 * edit 的子方法
	 * editOrderExecInfo()方法是将实体类的数据进行update 和 insert 操作
	 */
	private boolean editOrderExecInfo() {
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			/* order_exec_cs */
			m.put("order_exec_id", orderDetails.getOrderExecId());
			if (orderDetails.getPrice()>=0) m.put("price", orderDetails.getPrice());
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
			m.put("password", orderDetails.getPassword());
			appointService.updateOrderExecCs(m);
			m.clear();
			
			/* order_temp_details, order_long_details, order_onoffduty_details */
			m.put("id", orderDetails.getId());
			if (orderDetails.getPrice() >=0)
				m.put("price", orderDetails.getPrice());
			else
				m.put("price", 0);
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
			}
			if (orderDetails.getBegin_exec_time() != null && !orderDetails.getBegin_exec_time().equals("")) {
				m.put("begin_exec_time", orderDetails.getBegin_exec_time());
			}
			if (orderDetails.getDriverarrival_time() != null && !orderDetails.getDriverarrival_time().equals("")) {
				m.put("driverarrival_time", orderDetails.getDriverarrival_time());
			}
			if (orderDetails.getStopservice_time() != null && !orderDetails.getStopservice_time().equals("")) {
				m.put("endservice_time", orderDetails.getStopservice_time());
			}
			if (orderDetails.getPre_time() != null && !orderDetails.getPre_time().equals("")) {
				m.put("pre_time", orderDetails.getPre_time());
			}				
			m.put("status", orderDetails.getStatus());
			m.put("reqcarstyle", orderDetails.getReqcarstyle());
			m.put("remark", orderDetails.getPsgerRemark());
			m.put("driverRemark", orderDetails.getDriverRemark());
			switch (orderDetails.getOrder_type()) {
			case 1:
				appointService.updateOrderTempDetail(m);
				break;
			case 2:
				appointService.updateOrderTempDetail(m);
				break;
			case 3:
				appointService.updateOrderOnoffDetail(m);
				break;
			case 4:
				appointService.updateOrderLongDetail(m);
				break;
			default:
				break;
			}			
			m.clear();

			
			if (orderDetails.getOrder_type() != 3) {
				List<MidPoints> midpointsList;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("order_type", orderDetails.getOrder_type()-1);
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
				if (orderDetails.getPrice() >=0)
					price = Double.valueOf(orderDetails.getPrice());
				double temp_price = orderDetails.getTemp_price();
				double diff_balance = price - temp_price;
				if (orderDetails.getOrderExecId() > 0 && diff_balance != 0) {		// if status == 2 (accept)
					passenger = recourseService.findById(orderDetails.getPassagerId());			
					driver = recourseService.findById(orderDetails.getDriverId());
					// Table: ts(insert: )	if passenger				
					double curBalance = appointService.getBalanceByUserId(orderDetails.getPassagerId());
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
					appointService.insertTsInfo(m);					
					appointService.updateUserBalance_ts(orderDetails.getPassagerId(), Integer.valueOf(m.get("id").toString()));
					m.clear();
					// Table: ts(insert: )	if driver
					curBalance = appointService.getBalanceByUserId(orderDetails.getDriverId());
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
					m.put("userid", orderDetails.getPassagerId());
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
					appointService.insertTsInfo(m);
					appointService.updateUserBalance_ts(orderDetails.getDriverId(), Integer.valueOf(m.get("id").toString()));
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
	 * edit 的子方法
	 *将实体类里的数据 和 进行查询的appoint里的数据进行比较 看是否被修改 被修改的数据放到 修改记录表 中 
	 */
	private void OrderHistory() {
		try {
			
			Map<String, Object> m = new HashMap<String, Object>();
			int orderType = orderDetails.getOrder_type();
			m.put("order_exec_id", orderDetails.getOrderExecId());
			if (orderType != 3 && orderDetails.getPrice()!=appoint.getPrice()) {
				m.put("md_column", "乘客支付");
				m.put("old_value", String.valueOf(appoint.getPrice()));
				m.put("new_value", orderDetails.getPrice());			
				appointService.insertOrderHistory(m);
			}
			if (orderType == 3 && orderDetails.getTotal_sum() != appoint.getPrice()) {
				m.put("md_column", "订单总价");
				m.put("old_value", String.valueOf(appoint.getTotal_sum()));
				m.put("new_value", orderDetails.getTotal_sum());
				appointService.insertOrderHistory(m);
			}
			if (orderType == 1&&orderDetails.getBeginservice_time()!=null && !orderDetails.getBeginservice_time().equals(appoint.getBeginservice_time())) {
				m.put("md_column", "乘客上车");
				m.put("old_value", appoint.getBeginservice_time());
				m.put("new_value", orderDetails.getBeginservice_time());
				appointService.insertOrderHistory(m);
			}
			if (orderType == 1 && orderDetails.getPay_time()!=null && !orderDetails.getPay_time().equals(appoint.getPay_time())) {
				m.put("md_column", "乘客支付时间");
				m.put("old_value", appoint.getPay_time());
				m.put("new_value", orderDetails.getPay_time());
				appointService.insertOrderHistory(m);
			}
			if (orderDetails.getStart_addr()!=null&&!orderDetails.getStart_addr().equals(appoint.getFrom_())) {
				m.put("md_column", "订单起点");
				m.put("old_value", appoint.getFrom_());
				m.put("new_value", orderDetails.getStart_addr());
				appointService.insertOrderHistory(m);
			}
			if (orderDetails.getEnd_addr()!=null && !orderDetails.getEnd_addr().equals(appoint.getTo_())) {
				m.put("md_column", "订单终点");
				m.put("old_value", appoint.getTo_());
				m.put("new_value", orderDetails.getEnd_addr());
				appointService.insertOrderHistory(m);
			}
			if (orderDetails.getPassword()!=null && !orderDetails.getPassword().equals(appoint.getPassword())) {
				m.put("md_column", "电子密码");
				m.put("old_value", appoint.getPassword());
				m.put("new_value", orderDetails.getPassword());
				appointService.insertOrderHistory(m);
			}
			if (orderDetails.getPs_date()!=null && !orderDetails.getPs_date().equals(appoint.getCr_date())) {			
				m.put("md_column", "发布时间");
				m.put("old_value", appoint.getCr_date());
				m.put("new_value", orderDetails.getPs_date());
				appointService.insertOrderHistory(m);
			}
			if (orderDetails.getAccept_time()!=null && !orderDetails.getAccept_time().equals(appoint.getTi_accept_order())) {			
				m.put("md_column", "成交时间");
				m.put("old_value", appoint.getTi_accept_order());
				m.put("new_value", orderDetails.getAccept_time());
				appointService.insertOrderHistory(m);
			}
			if (orderType != 2) {
				if (orderDetails.getBegin_exec_time()!=null && !orderDetails.getBegin_exec_time().equals(appoint.getBegin_exec_time())) {			
					m.put("md_column", "开始执行");
					m.put("old_value", appoint.getBegin_exec_time());
					m.put("new_value", orderDetails.getBegin_exec_time());
					appointService.insertOrderHistory(m);
				}
				if (orderDetails.getDriverarrival_time()!=null && !orderDetails.getDriverarrival_time().equals(appoint.getDriverarrival_time())) {			
					m.put("md_column", "车主到达");
					m.put("old_value", appoint.getDriverarrival_time());
					m.put("new_value", orderDetails.getDriverarrival_time());
					appointService.insertOrderHistory(m);
				}
				if (orderDetails.getStopservice_time()!=null && !orderDetails.getStopservice_time().equals(appoint.getStopservice_time())) {			
					m.put("md_column", "结束服务");
					m.put("old_value", appoint.getStopservice_time());
					m.put("new_value", orderDetails.getStopservice_time());
					appointService.insertOrderHistory(m);
				}
				if (orderDetails.getPre_time()!=null && !orderDetails.getPre_time().equals(appoint.getPre_time())) {			
					m.put("md_column", "预约出发");
					m.put("old_value", appoint.getPre_time());
					m.put("new_value", orderDetails.getPre_time());
					appointService.insertOrderHistory(m);
				}
			}
			if (orderDetails.getStatus() != appoint.getStatus()) {			
				m.put("md_column", "当前状态");
				  if(appoint.getStatus()==1){
		        		m.put("old_value", "发布");
		          } if(appoint.getStatus()==2){
		        		m.put("old_value", "成交/待执行");
		          } if(appoint.getStatus()==3){
		        		m.put("old_value", "开始执行");
		          } if(appoint.getStatus()==4){
		        		m.put("old_value", "车主到达");
		          } if(appoint.getStatus()==5){
		        		m.put("old_value", "乘客上车");
		          } if(appoint.getStatus()==6){
		        		m.put("old_value", "执行结束/待支付");
		          } if(appoint.getStatus()==7){
		        		m.put("old_value", "已支付/待预约（单拼、长途完结)");
		          } if(appoint.getStatus()==8){
		        		m.put("old_value", "已销单（非正常完结）");
		          } if(appoint.getStatus()==9){
		        		m.put("old_value", "结束服务（上下班完结）");
		          }
		          if(orderDetails.getStatus()==1){
		        		m.put("new_value", "发布");
		          } if(orderDetails.getStatus()==2){
		        		m.put("new_value", "成交/待执行");
		          } if(orderDetails.getStatus()==3){
		        		m.put("new_value", "开始执行");
		          } if(orderDetails.getStatus()==4){
		        		m.put("new_value", "车主到达");
		          } if(orderDetails.getStatus()==5){
		        		m.put("new_value", "乘客上车");
		          } if(orderDetails.getStatus()==6){
		        		m.put("new_value", "执行结束/待支付");
		          } if(orderDetails.getStatus()==7){
		        		m.put("new_value", "已支付/待预约（单拼、长途完结)");
		          } if(orderDetails.getStatus()==8){
		        		m.put("new_value", "已销单（非正常完结）");
		          } if(orderDetails.getStatus()==9){
		        		m.put("new_value", "结束服务（上下班完结）");
		          }
				appointService.insertOrderHistory(m);
			}
			if (orderDetails.getReqcarstyle() != appoint.getReqcarstyle()) {			
				m.put("md_column", "需求车型");
				m.put("old_value", appoint.getReqcarstyle());
				m.put("new_value", orderDetails.getReqcarstyle());
				appointService.insertOrderHistory(m);
			}
			if (orderDetails.getPsgerRemark()!=null&&!"".equals(orderDetails.getPsgerRemark())&&!orderDetails.getPsgerRemark().equals(appoint.getPassengerRemark())) {			
				m.put("md_column", "乘客备注");
				m.put("old_value", appoint.getPassengerRemark());
				m.put("new_value", orderDetails.getPsgerRemark());
				appointService.insertOrderHistory(m);
			}
			if (orderDetails.getDriverRemark()!=null&&!"".equals(orderDetails.getDriverRemark())&&!orderDetails.getDriverRemark().equals(appoint.getDriverRemark())) {			
				m.put("md_column", "车主备注");
				m.put("old_value", appoint.getDriverRemark());
				m.put("new_value", orderDetails.getDriverRemark());
				appointService.insertOrderHistory(m);
			}
			if (appoint.getOrder_type() != 3) {
				List<MidPoints> midpointsList;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("order_type", appoint.getOrder_type() - 1);
				map.put("detailsId", appoint.getId());
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
							appointService.insertOrderHistory(m);
						}
						break;
					case 1:
						if (orderDetails.getAddr4midpoint2()!=null && !orderDetails.getAddr4midpoint2().equals(str_addr)) {
							m.put("md_column", "中途点2");
							m.put("old_value", str_addr);
							m.put("new_value", orderDetails.getAddr4midpoint2());
							appointService.insertOrderHistory(m);
						}
						break;
					case 2:
						if (orderDetails.getAddr4midpoint3()!=null && !orderDetails.getAddr4midpoint3().equals(str_addr)) {
							m.put("md_column", "中途点3");
							m.put("old_value", str_addr);
							m.put("new_value", orderDetails.getAddr4midpoint3());
							appointService.insertOrderHistory(m);
						}
						break;
					case 3:
						if (orderDetails.getAddr4midpoint4()!=null && !orderDetails.getAddr4midpoint4().equals(str_addr)) {
							m.put("md_column", "中途点4");
							m.put("old_value", str_addr);
							m.put("new_value", orderDetails.getAddr4midpoint4());
							appointService.insertOrderHistory(m);
						}
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}	
	
	/*
	 * 执行订单的查看页面的跳转页面
	 */
	public String view1(){
			HttpServletRequest request = ServletActionContext.getRequest();
		 ActionContext.getContext().put("id", id);
		 ActionContext.getContext().put("choose", choose);
		 System.out.println("choose : "+choose);
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
			map.put("reqcarstyle", 0);
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
	  * 退还乘客被冻结的绿点
	  */
	public String cancelFreezePoint() {
		Map<String, Object> json = new HashMap<String, Object>();
		err = Errors.ERR_OK;
		try {
			FreezeTsInfo freezeTsInfo = new FreezeTsInfo();
			freezeTsInfo = appointService.findFreezeTsInfo(id);
			
			if (freezeTsInfo != null) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("order_cs_id", id);
				m.put("oper", 2);
				m.put("ts_way", 4);
				m.put("balance", freezeTsInfo.getTs_balance());
//				m.put("userid", userId);
				m.put("groupid", 0);
				m.put("unitid", 0);
				m.put("remark", "");
//				m.put("account", appointService.getBankCard(userId));
				m.put("account_type", 1);
				m.put("application", "pinche");
				m.put("ts_type", 2);
				m.put("sum", freezeTsInfo.getBalance());		
				appointService.insertTsInfo(m);
				m.clear();
				
				UserLoginInfo info = (UserLoginInfo) WebUtil._session("user");
				m.put("adminid", info.getId());
				m.put("balance", freezeTsInfo.getTs_balance());
				m.put("release_ts_id", appointService.getFreezeMaxId());
				appointService.updateFreeze(m);
			} else {
				err = Errors.ERR_SQL_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		json.put("err_code", err);
		json.put("err_msg", "没有被冻结的绿点");
		resultObject = JSONObject.fromObject(json);
		return SUCCESS;
	}
	
	/*
	 * 查询执行订单被修改的字段的记录数据  
	 */
	public String showEditHistory() {
		try {	
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("orderExecId", orderExecId);
			int  rowLen= appointService.findAcountOrderModifyLog(m);
			m.put("page", (page-1)*rows);
			m.put("rows", rows);
			orderModifyLogInfos = appointService.findOrderModifyLog(m);
			Map<String, Object> json = new HashMap<String, Object>();
			List<Map<String,Object>> map=new ArrayList<Map<String,Object>>();
			for(OrderModifyLogInfo info:orderModifyLogInfos){
				Map<String , Object> params=new HashMap<String , Object>();
				params.put("id", info.getId());
				params.put("md_column", info.getMd_column());
				if(info.getMd_time()!=null){
					params.put("md_time", info.getMd_time());
				}else{
					params.put("md_time", "");
				}
				params.put("modifier", info.getModifier());
				if(info.getOld_value()!=null){
					params.put("old_value", info.getOld_value());
				}else{
					params.put("old_value", "");
				}
				if(info.getNew_value()!=null){
					params.put("new_value", info.getNew_value());	
				}else{
					params.put("new_value", "");
				}
				
				
				params.put("order_exec_id", info.getOrder_exec_id());
				map.add(params);
			}
			if(rowLen==0){
				json.put("total", 1);
				json.put("rows", 0);
				resultObject = JSONObject.fromObject(json);
			}else{
				json.put("total", rowLen);
				json.put("rows", map);
				resultObject = JSONObject.fromObject(json);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	/*
	 * 工单添加新工单里的通过电话号码 查询用户id 和 性别 姓名 城市
	 */
	public String find_id_phone(){
		Customer user=null;
		Map<String ,Object> params=new HashMap<String,Object>();
		if(userphone!=null&&""!=userphone){
		user=appointService.findByPhone(userphone);
		}else{
			 user=null;	
		}
		if(user!=null){
		
			params.put("usercode",  user.getId());
			params.put("username", (user.getUsername()==null)?"":user.getUsername() );
			params.put("phone", (user.getPhone()==null)?"":user.getPhone());
		}
		resultObject = JSONObject.fromObject(params);
		return SUCCESS;
	}
	/*
	 * getter 和 setter 方法
	 */
	public String getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public SearchItems getSearchItems() {
		return searchItems;
	}

	public void setSearchItems(SearchItems searchItems) {
		this.searchItems = searchItems;
	}
	
	public String getParam() {
		return param;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setParam(String param) {
		this.param = param;
	}

	
	public List<Appoint> getAppointList() {
		return appointList;
	}
	
	public Appoint getAppoint() {
		return appoint;
	}

	public void setAppoint(Appoint appoint) {
		this.appoint = appoint;
	}

	public void setAppointList(List<Appoint> appointList) {
		this.appointList = appointList;
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
	
	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}

	public int getOrderExecId() {
		return orderExecId;
	}

	public void setOrderExecId(int orderExecId) {
		this.orderExecId = orderExecId;
	}

	public String getOrder_city() {
		return order_city;
	}

	public void setOrder_city(String order_city) {
		this.order_city = order_city;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public Integer getOrder_type1() {
		return order_type1;
	}

	public void setOrder_type1(Integer order_type1) {
		this.order_type1 = order_type1;
	}

	public Integer getOrder_type2() {
		return order_type2;
	}

	public void setOrder_type2(Integer order_type2) {
		this.order_type2 = order_type2;
	}

	public Integer getOrder_type3() {
		return order_type3;
	}

	public void setOrder_type3(Integer order_type3) {
		this.order_type3 = order_type3;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
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

	public Integer getDept() {
		return dept;
	}

	public void setDept(Integer dept) {
		this.dept = dept;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}
	public void setAppointService(AppointService appointService) {
		this.appointService = appointService;
	}

	public AppointService getAppointService() {
		return appointService;
	}

	public RecourseService getRecourseService() {
		return recourseService;
	}

	public void setRecourseService(RecourseService recourseService) {
		this.recourseService = recourseService;
	}

	public Integer getOrder_type() {
		return order_type;
	}

	public void setOrder_type(Integer order_type) {
		this.order_type = order_type;
	}

	public int getRowLen() {
		return rowLen;
	}

	public void setRowLen(int rowLen) {
		this.rowLen = rowLen;
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

	public int getPassagerId() {
		return passagerId;
	}

	public void setPassagerId(int passagerId) {
		this.passagerId = passagerId;
	}

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	public Date getDriverarrival_time() {
		return driverarrival_time;
	}

	public void setDriverarrival_time(Date driverarrival_time) {
		this.driverarrival_time = driverarrival_time;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public void setTemp_price(double temp_price) {
		this.temp_price = temp_price;
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

	public String getDriyerremark() {
		return driyerremark;
	}

	public void setDriyerremark(String driyerremark) {
		this.driyerremark = driyerremark;
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

	public TempInfo getTempInfo() {
		return tempInfo;
	}

	public void setTempInfo(TempInfo tempInfo) {
		this.tempInfo = tempInfo;
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

	public List<OrderModifyLogInfo> getOrderModifyLogInfos() {
		return orderModifyLogInfos;
	}

	public void setOrderModifyLogInfos(List<OrderModifyLogInfo> orderModifyLogInfos) {
		this.orderModifyLogInfos = orderModifyLogInfos;
	}

	public AppointdetailService getAppointDetailService() {
		return appointDetailService;
	}

	public void setAppointDetailService(AppointdetailService appointDetailService) {
		this.appointDetailService = appointDetailService;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getOrder_type4() {
		return order_type4;
	}

	public void setOrder_type4(int order_type4) {
		this.order_type4 = order_type4;
	}

	public int getChoose() {
		return choose;
	}

	public void setChoose(int choose) {
		this.choose = choose;
	}
	
}
