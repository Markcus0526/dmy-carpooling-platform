package com.nmdy.financial.account.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.nmdy.financial.account.service.AccountService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class AccountAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AccountService accountService;

	private int page;
	private int rows;
	// charge page
	// self
	private JSONObject jsonObject;// 返回的json
	private int userid;
	// find order
	private int chkusertype;
	private int chkusertype1;
	private int ordertype1;
	private int ordertype2;
	private int ordertype3;
	private int ordertype4;
	private int orderstatus1;
	private int orderstatus2;
	private int userorderselect;
	private String ordernum;
	private int tstype;// 账目类型
	private String begintime = "0000-00-00";
	private String endtime = "9999-12-30";
	private int usersid;
	private String username;
	private int orderuid;
	private String ousername;// 用于订单号查询的用户名
	private int orderid1;
	/**
	 * var chergeid =$('#usersid').val();//客户id var
	 * chergeremark=$('#remark').val();//详细说明 var chergesum
	 * =$('#sum1').val();//操作金额 var chergeorder=$('#orderid').val(); var
	 * req_cause=$('#reqselect').combobox('getText'); var
	 * account_type=$('#type').val();
	 * 
	 * @return khtype:khtype, khinfo:khinfo, xianginput:xianginput,
	 */
	private int chergeid;
	private String chergeremark;
	private double chergesum;
	private Integer chergeorder;
	private String req_cause;
	private int account_type;
	private int tsid;
	// search1
	private int khtype = 0;
	private int khinfo = 0;
	private String xianginput;
	// view
	/**
	 * insert :insert, out:out, pinche:pinche, daijia:daijia, type1:type1,
	 * type2:type2, type3:type3, type4:type4, type5:type5, type6:type6,
	 * viewbegintime:viewbegintime, viewendtime:viewendtime, viewtsid:viewtsid,
	 * vieworder_cs_id :vieworder_cs_id ,
	 */
	private int viewaccounttype;
	private int viewuserid;
	private String viewbalance;
	private String viewphone;
	private int viewaccounttype1;
	private int viewuserid1;
	private int insert;
	private int out;
	private int pinche;
	private int daijia;

	private String viewbegintime;
	private String viewendtime;
	private int viewtsid;
	private long vieworder_cs_id;
	private int oper;
	private int acctype;
	private String viewusername;
	
	
	public String index() {

		return SUCCESS;
	}

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 查看详细页面根据userid查询出username
	 */
	public String findusername() {
		Map<String, Object> json = new HashMap<String, Object>();
		Map<String, Object> username = new HashMap<String, Object>();
		if (acctype == 1) {
			username = accountService.findusername(userid);
		} else if (acctype == 2) {
			// 集团客户name
			username = accountService.findgroupname(userid);
		} else if (acctype == 3) {
			// 合作单位name
			username = accountService.findunitname(userid);
		}

		// System.out.println(username.get("username"));

		json.put("rows", username);
		jsonObject = JSONObject.fromObject(username);
		// System.out.println(jsonObject.get("username"));
		return SUCCESS;
	}

	/**
	 * 点击充值确定的action方法
	 */
	public String charge() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_type", account_type);
		params.put("user_id", chergeid);
		params.put("sum", chergesum);
		params.put("order_cs_id", chergeorder);
		params.put("req_cause", req_cause);
		params.put("remark", chergeremark);
		params.put("tsid", tsid);
		params.put("oper", oper);
		accountService.addcharge(params);
		return SUCCESS;
	}

	/**
	 * 订单选择查询
	 */
	public String findorder() {
		HttpServletRequest request = ServletActionContext.getRequest();
		// System.out.println(begintime);
		List<Map<String, Object>> usersorder;// 返回的实体类存放处
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("uid", usersid);

		/*
		 * type:1 表示只选择乘客 type:2 表示只选择车主 type:3表示选择乘客和车主
		 */
		if (chkusertype1 == 0 && chkusertype == 1) {
			params.put("type", 1);
		}
		if (chkusertype == 0 && chkusertype1 == 1) {

			params.put("type", 2);
		}
		if (chkusertype == 1 && chkusertype1 == 1) {
			params.put("type", 3);
		}
		if (chkusertype == 0 && chkusertype1 == 0) {
			params.put("type", 4);

		}
		/*
		 * 1 临时单次 2 上下班 3 长途
		 */
		if (ordertype1 == 1) {
			params.put("ordertype1", 1);
			params.put("ordertype2", 2);
		}
		if (ordertype2 == 1) {
			params.put("ordertype3", 3);
		}
		if (ordertype3 == 1) {
			params.put("ordertype4", 4);

		}
		/*
		 * 1 完成 2 未完成
		 */
		if (orderstatus1 == 1) {
			params.put("orderstatus7", 7);
			params.put("orderstatus9", 9);

		}
		if (orderstatus2 == 1) {
			params.put("orderstatus1", 1);
			params.put("orderstatus2", 2);
			params.put("orderstatus3", 3);
			params.put("orderstatus4", 4);
			params.put("orderstatus5", 5);
			params.put("orderstatus6", 6);
			params.put("orderstatus8", 8);

		}
		/*
		 * bengintime 开始时间 endtime 结束时间 成交时间在二者之间
		 */
		if (null != begintime && !"".equals(begintime)) {
			params.put("begintime", begintime+" 00:00:00");
		}
		if (null != endtime && !"".equals(endtime)) {
			params.put("endtime", endtime+" 23:59:59");
		}
		if (null != ordernum && !"".equals(ordernum)) {
			params.put("ordernum", ordernum);
		}
		// if(null!=ousername&&"".equals(ousername)){
		// params.put("ousername",ousername);
		//
		// }

		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		System.out.println(params);
		usersorder = accountService.findorder(params);
		jsonMap.put("total", accountService.getOrderCount(params));
		jsonMap.put("rows", usersorder);
		jsonObject = JSONObject.fromObject(jsonMap);
		return SUCCESS;
	}

	/**
	 * 查看详细跳转
	 */
	public String info() {
		ActionContext.getContext().put("orderid", orderid1);
		return SUCCESS;
	}

	// //执行订单信息查询
	// public String orederview(){
	// String passengername =null;
	// String drivername =null;
	// String pphone=null;
	// String dphone=null;
	// String premark=null;
	// String dremark=null;
	// int ordertype;
	// Map<String, Object> params = new HashMap<String, Object>();
	// Map<String, Object> midmap = new HashMap<String, Object>();
	// Map<String, Object> passengermap = new HashMap<String, Object>();
	// Map<String, Object> drivermap = new HashMap<String, Object>();
	// Map<String, Object> nummap = new HashMap<String, Object>();
	// Map<String, Object> car = new HashMap<String, Object>();
	// params.put("orderid",orderid1);
	// Map<String,Object> map = accountService.findone(orderid1);
	// List<Map<String,Object>> midpointlistmap =
	// accountService.findMidPoints(params);
	// //判断订单类型 用来查订单编号
	// ordertype = (int) map.get("order_type");
	// if(ordertype==1||ordertype==2){
	// //在临时表里查数据
	// nummap = accountService.findtemp(orderid1);
	//
	// }
	// if(ordertype==3){
	// //在上下班表里查数据
	// nummap = accountService.findonoff(orderid1);
	// //car = accountService.carstyle((String) nummap.get("ordernum"));
	// }
	// if(ordertype==4){
	// //在长途订单表里查数据
	// nummap = accountService.findlongdis(orderid1);
	// }
	// map.put("ordernum",orderid1);
	// //map.put("carstyle", car.get("reqcarstyle"));
	// //将中途点信息存进去
	// int i=0;
	// if(midpointlistmap.get(0)!=null){
	// String mid1=(String) midpointlistmap.get(0).get("addr");
	// map.put("mid1",mid1);
	//
	// }
	// if(midpointlistmap.get(1)!=null){
	// String mid2=(String) midpointlistmap.get(1).get("addr");
	// map.put("mid2",mid2);
	//
	// }
	// if(midpointlistmap.get(2)!=null){
	// String mid3=(String) midpointlistmap.get(2).get("addr");
	// map.put("mid3",mid3);
	//
	// }
	// if(midpointlistmap.get(3)!=null){
	// String mid4=(String) midpointlistmap.get(3).get("addr");
	// map.put("mid4",mid4);
	//
	// }
	//
	// //获得乘客
	// long passengerid= (long) map.get("passenger");
	// passengermap = accountService.finduser(passengerid);
	// passengername = (String) passengermap.get("name");
	// premark = (String) passengermap.get("remark");
	// System.out.println("pn "+passengername);
	// pphone = (String) passengermap.get("phone");
	// //获得车主id
	// long driverid=(long) map.get("driver");
	// drivermap = accountService.finduser(driverid);
	// drivername = (String) drivermap.get("name");
	//
	// System.out.println("dn "+drivername);
	// dphone = (String) drivermap.get("phone");
	// dremark = (String) drivermap.get("remark");
	// map.put("passengername",passengername);
	// map.put("pphone", pphone);
	// map.put("drivername", drivername);
	// map.put("dphone", dphone);
	// map.put("premark",premark);
	// map.put("dremark",dremark);
	//
	// jsonObject=JSONObject.fromObject(map);
	//
	// return SUCCESS;
	// }
	// search
/**
 * 页面数据查询方法
 * @return
 */
	public String search1() {
		HttpServletRequest request = ServletActionContext.getRequest();
		List<Map<String, Object>> account;// 返回的实体类存放处
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		if("0".equals(xianginput)){
			xianginput = null;
		}
		params.put("khtype", khtype);
		params.put("khinfo", khinfo);
		params.put("xianginput",xianginput);
		System.out.println("**********khtype:"+khtype);
		System.out.println("**********xianginput:"+xianginput);
		System.out.println("**********khinfo:"+khinfo);
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		account = accountService.search1(params);
		jsonMap.put("total", accountService.getcountsearch1(params));
		jsonMap.put("rows", account);
		jsonObject = JSONObject.fromObject(jsonMap);
		return SUCCESS;
	}

	// //上下班订单特别部分的查询方法
	// public String showonoff(){
	// System.out.println("showonffs ordernum"+ordernum);
	// List<Map<String,Object>> onoff = accountService.showonoff(ordernum);
	// Map<String, Object> jsonMap = new HashMap<String, Object>();
	// Map<String,Object> days = accountService.findday(ordernum);
	// String daysinfo = (String) days.get("which_days");//获得星期几的哪个字符串
	// System.out.println("daysinfo"+daysinfo);
	// String daysinfos[] = daysinfo.split(",");
	// for(int i=0;i<daysinfos.length;i++){
	// jsonMap.put("day"+i,daysinfos[i]);
	//
	// }
	//
	// jsonMap.put("rows",onoff);
	// jsonObject=JSONObject.fromObject(jsonMap);
	// return SUCCESS;
	// }
	// //上下班订单特别部分的查询方法
	// public String showlong(){
	// System.out.println("longs ordernum"+ordernum);
	// List<Map<String,Object>> longs = accountService.showlong(ordernum);;
	// Map<String, Object> jsonMap = new HashMap<String, Object>();
	// jsonMap.put("rows",longs);
	// jsonObject=JSONObject.fromObject(jsonMap);
	// return SUCCESS;
	// }

	/**
	 * 查看详细跳转
	 */

	public String view() {
		Map<String, Object> username = new HashMap<String, Object>();
		if (viewaccounttype == 1) {
			username = accountService.findusername(viewuserid);
		}
		if (viewaccounttype == 2) {
			username = accountService.findgroupname(viewuserid);
		}
		if (viewaccounttype == 3) {
			username = accountService.findunitname(viewuserid);
		}

		viewusername = (String) username.get("username");
		ActionContext.getContext().put("viewuserid", viewuserid);
//		ActionContext.getContext().put("viewusername", viewusername);
		ActionContext.getContext().put("viewphone", viewphone);
		ActionContext.getContext().put("viewbalance", viewbalance);
		ActionContext.getContext().put("viewuaccounttype", viewaccounttype);
		this.setViewusername(viewusername);
		return SUCCESS;
	}

	/**
	 * 查看详细 表格加载
	 */

	public String findview() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		List<Map<String, Object>> viewinfo;

		params.put("userid", viewuserid1);
		params.put("accounttype", viewaccounttype1);
		// System.out.println(tstype + "as");
		if (1 == tstype) {
			params.put("ts_type", "tx_code_001");
		}
		if (2 == tstype) {
			params.put("ts_type", "tx_code_002");
		}
		if (3 == tstype) {
			params.put("ts_type", "tx_code_003");
		}
		if (4 == tstype) {
			params.put("ts_type", "tx_code_004");
		}
		if (5 == tstype) {
			params.put("ts_type", "tx_code_005");
		}

		if (6 == tstype) {
			params.put("ts_type", "tx_code_006");
		}
		if (7 == tstype) {
			params.put("ts_type", "tx_code_007");
		}
		if (8 == tstype) {
			params.put("ts_type", "tx_code_008");
		}
		if (9 == tstype) {
			params.put("ts_type", "tx_code_009");
		}
		if (10 == tstype) {
			params.put("ts_type", "tx_code_010");
		}

		if (11 == tstype) {
			params.put("ts_type", "tx_code_011");
		}
		if (12 == tstype) {
			params.put("ts_type", "tx_code_012");
		}
		if (13 == tstype) {
			params.put("ts_type", "tx_code_013");
		}
		if (14 == tstype) {
			params.put("ts_type", "tx_code_014");
		}
		if (15 == tstype) {
			params.put("ts_type", "tx_code_015");
		}
		if (16 == tstype) {
			params.put("ts_type", "tx_code_016");
		}
		if (17 == tstype) {
			params.put("ts_type", "tx_code_017");
		}
		if (18 == tstype) {
			params.put("ts_type", "tx_code_018");
		}
		if (0 == out && 0 != insert) {
			params.put("oper1", 1);
			params.put("oper2", 0);
		}
		if (0 == insert && 0 != out) {
			params.put("oper1", 0);
			params.put("oper2", 2);
		}
		if (0 != insert && 0 != out) {
			params.put("oper1", 1);
			params.put("oper2", 2);
		}
		if (vieworder_cs_id > 0) {
			params.put("order_cs_id", vieworder_cs_id);
		}
		if (viewtsid > 0) {
			params.put("id", viewtsid);
		}
		if (null != viewbegintime && !"0".equals(viewbegintime)) {
			params.put("begin", viewbegintime+" 00:00:00");
		}
		if (null != viewendtime && !"0".equals(viewendtime)) {
			params.put("end", viewendtime+" 23:59:59");
		}

		viewinfo = accountService.finduserinfo(params);
		jsonMap.put("rows", viewinfo);
		jsonMap.put("total", accountService.countuserinfo(params));

		jsonObject = JSONObject.fromObject(jsonMap);

		return SUCCESS;
	}

	// ==============================================================================================================

	public String getViewbalance() {
		return viewbalance;
	}

	public void setViewbalance(String viewbalance) {
		this.viewbalance = viewbalance;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
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

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getChergeid() {
		return chergeid;
	}

	public void setChergeid(int chergeid) {
		this.chergeid = chergeid;
	}

	public String getChergeremark() {
		return chergeremark;
	}

	public void setChergeremark(String chergeremark) {
		this.chergeremark = chergeremark;
	}

	public double getChergesum() {
		return chergesum;
	}

	public void setChergesum(double chergesum) {
		this.chergesum = chergesum;
	}

	public Integer getChergeorder() {
		return chergeorder;
	}

	public void setChergeorder(Integer chergeorder) {
		this.chergeorder = chergeorder;
	}

	public String getReq_cause() {
		return req_cause;
	}

	public void setReq_cause(String req_cause) {
		this.req_cause = req_cause;
	}

	public int getAccount_type() {
		return account_type;
	}

	public void setAccount_type(int account_type) {
		this.account_type = account_type;
	}

	public int getChkusertype() {
		return chkusertype;
	}

	public void setChkusertype(int chkusertype) {
		this.chkusertype = chkusertype;
	}

	public int getChkusertype1() {
		return chkusertype1;
	}

	public void setChkusertype1(int chkusertype1) {
		this.chkusertype1 = chkusertype1;
	}

	public int getOrdertype1() {
		return ordertype1;
	}

	public void setOrdertype1(int ordertype1) {
		this.ordertype1 = ordertype1;
	}

	public int getOrdertype2() {
		return ordertype2;
	}

	public void setOrdertype2(int ordertype2) {
		this.ordertype2 = ordertype2;
	}

	public int getOrdertype3() {
		return ordertype3;
	}

	public void setOrdertype3(int ordertype3) {
		this.ordertype3 = ordertype3;
	}

	public int getOrdertype4() {
		return ordertype4;
	}

	public void setOrdertype4(int ordertype4) {
		this.ordertype4 = ordertype4;
	}

	public int getOrderstatus1() {
		return orderstatus1;
	}

	public void setOrderstatus1(int orderstatus1) {
		this.orderstatus1 = orderstatus1;
	}

	public int getOrderstatus2() {
		return orderstatus2;
	}

	public void setOrderstatus2(int orderstatus2) {
		this.orderstatus2 = orderstatus2;
	}

	public int getUserorderselect() {
		return userorderselect;
	}

	public void setUserorderselect(int userorderselect) {
		this.userorderselect = userorderselect;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getUsersid() {
		return usersid;
	}

	public void setUsersid(int usersid) {
		this.usersid = usersid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getOrderuid() {
		return orderuid;
	}

	public void setOrderuid(int orderuid) {
		this.orderuid = orderuid;
	}

	public String getOusername() {
		return ousername;
	}

	public void setOusername(String ousername) {
		this.ousername = ousername;
	}

	public int getOrderid1() {
		return orderid1;
	}

	public void setOrderid1(int orderid1) {
		this.orderid1 = orderid1;
	}

	public int getKhtype() {
		return khtype;
	}

	public void setKhtype(int khtype) {
		this.khtype = khtype;
	}

	public int getKhinfo() {
		return khinfo;
	}

	public void setKhinfo(int khinfo) {
		this.khinfo = khinfo;
	}

	public String getXianginput() {
		return xianginput;
	}

	public void setXianginput(String xianginput) {
		this.xianginput = xianginput;
	}

	public int getViewaccounttype() {
		return viewaccounttype;
	}

	public void setViewaccounttype(int viewaccounttype) {
		this.viewaccounttype = viewaccounttype;
	}

	public int getViewuserid() {
		return viewuserid;
	}

	public void setViewuserid(int viewuserid) {
		this.viewuserid = viewuserid;
	}

	public String getViewphone() {
		return viewphone;
	}

	public void setViewphone(String viewphone) {
		this.viewphone = viewphone;
	}

	public int getViewaccounttype1() {
		return viewaccounttype1;
	}

	public void setViewaccounttype1(int viewaccounttype1) {
		this.viewaccounttype1 = viewaccounttype1;
	}

	public int getViewuserid1() {
		return viewuserid1;
	}

	public void setViewuserid1(int viewuserid1) {
		this.viewuserid1 = viewuserid1;
	}

	public int getInsert() {
		return insert;
	}

	public void setInsert(int insert) {
		this.insert = insert;
	}

	public int getOut() {
		return out;
	}

	public void setOut(int out) {
		this.out = out;
	}

	public int getPinche() {
		return pinche;
	}

	public void setPinche(int pinche) {
		this.pinche = pinche;
	}

	public int getDaijia() {
		return daijia;
	}

	public void setDaijia(int daijia) {
		this.daijia = daijia;
	}

	public String getViewbegintime() {
		return viewbegintime;
	}

	public void setViewbegintime(String viewbegintime) {
		this.viewbegintime = viewbegintime;
	}

	public String getViewendtime() {
		return viewendtime;
	}

	public void setViewendtime(String viewendtime) {
		this.viewendtime = viewendtime;
	}

	public int getViewtsid() {
		return viewtsid;
	}

	public void setViewtsid(int viewtsid) {
		this.viewtsid = viewtsid;
	}

	public long getVieworder_cs_id() {
		return vieworder_cs_id;
	}

	public void setVieworder_cs_id(long vieworder_cs_id) {
		this.vieworder_cs_id = vieworder_cs_id;
	}

	public int getOper() {
		return oper;
	}

	public void setOper(int oper) {
		this.oper = oper;
	}

	public int getTstype() {
		return tstype;
	}

	public void setTstype(int tstype) {
		this.tstype = tstype;
	}

	public int getTsid() {
		return tsid;
	}

	public void setTsid(int tsid) {
		this.tsid = tsid;
	}

	public int getAcctype() {
		return acctype;
	}

	public void setAcctype(int acctype) {
		this.acctype = acctype;
	}

	public String getViewusername() {
		return viewusername;
	}

	public void setViewusername(String viewusername) {
		this.viewusername = viewusername;
	}
}
