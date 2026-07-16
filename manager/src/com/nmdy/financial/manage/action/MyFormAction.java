package com.nmdy.financial.manage.action;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.nmdy.financial.manage.dao.OrderEntity;
import com.nmdy.financial.manage.dao.UsersEntity;
import com.nmdy.financial.manage.service.MyFormService;
import com.sun.org.apache.xpath.internal.operations.And;

public class MyFormAction extends ActionSupport {
	private MyFormService myService;
	private int page;
	private int ctype;
	private int rows;
	private JSONObject jsonObject;// 返回的json
	private int chkwait;
	private int chkfinish;
	private int chkrefuse;
	private int chkcancel;
	private int id;
	private int chkuser;
	private int chkgroup;
	private int chkunit;
	private int userselect;
	private String input;
	private int unformid;// 被驳回的表单的id
	private String reject_cause;// 驳回理由
	// findorder
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

	private String begintime = "0000-00-00";
	private String endtime = "9999-12-30";
	private int usersid;
	private String username;
	private int orderuid;
	private String ousername;// 用于订单号查询的用户名

	// 修改重提用
	private int chergeformid;
	private int chergeid;
	private double chergebalance;
	private String chergeremark;
	private double chergesum;
	private String chergeorder;
	private String req_cause;
	// 通过
	private double passbalance;
	private BigDecimal passsum;
	private long tsorderid;
	private long uid;// 用于插入数据到用户集团，或者合作单位
	private int uaccounttype;
	BigDecimal ubalance ;
	BigDecimal ubalance1 ;
	// 关闭功能
	private int colseformid;
	// 查看详细
	private int orderid;

	public String index() {
		//System.out.println("bengin account");

		return SUCCESS;
	}

	/**
	 *  我的表单查询方法
	 * @return
	 */
	public String find() {
		//System.out.println("find has been here...");

	
			List<UsersEntity> users;// 返回的实体类存放处
			//System.out.println("find has been here");
			// params 需要提交表单之后才能获得
			Map<String, Object> params = new HashMap<String, Object>();
			// if ("1".equals(cityselect)) {
			// System.out.println(cityselect);
			// params.put("city_register", cityinput);
			// System.out.println(cityinput);
			// }
			// if ("2".equals(cityselect)) {
			// params.put("city_cur", cityinput);
			// }
			// if ("3".equals(cityselect)) {
			// // 选择其中的一种
			// params.put("city_cur1", cityinput);
			// }
			// if (groupselect.equals("0")) {
			// groupinput=null;
			// }
			// 只查询出个人用户
			if (chkuser == 1) {
				params.put("gr", "个人客户");

			}
			if (chkgroup == 1) {
				params.put("jt", "集团客户");

			}
			if (chkunit == 1) {
				params.put("hz", "合作单位");

			}
			if (chkwait == 1) {
				params.put("dsh", "待审核");
			}
			if (chkfinish == 1) {
				params.put("ysh", "已通过");
			}
			if (chkrefuse == 1) {
				params.put("ybh", "已驳回");
			}
			if (chkcancel == 1) {
				params.put("ycx", "已关闭");
			}
			if (userselect == 0) {
				params.put("userid", input);

			}
			if (userselect == 1) {
				params.put("username", input);

			}
			if (userselect == 2) {
				params.put("phone", input);
			}
			if (userselect == 3) {
				params.put("sum", input);
			}
			if (userselect == 4) {
				params.put("order_cs_id", input);

			}

			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			users = myService.find(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", myService.getCount(params));

			//System.out.println(params);// 输出map里的内容
			//System.out.println(jsonMap);
			jsonMap.put("rows", users);
			jsonObject = JSONObject.fromObject(jsonMap);
			//System.out.println(jsonObject);
			//System.out.println("find has been here...tryend");
	
		//System.out.println("find has been here...success");
		return SUCCESS;
	}

	/**
	 * 我的表单关闭
	 * @return
	 */
	public String colse() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("colseformid", colseformid);
		
			myService.updatecolse(params);
			
	
		
		return SUCCESS;
	}

	/**
	 * 待我审核查询方法
	 * @return
	 */
	public String findwait() {
		//System.out.println("find has been here...");
		HttpServletRequest request = ServletActionContext.getRequest();
	
			List<UsersEntity> users;// 返回的实体类存放处
			//System.out.println("find has been here");
			// params 需要提交表单之后才能获得
			Map<String, Object> params = new HashMap<String, Object>();
			// if ("1".equals(cityselect)) {
			// System.out.println(cityselect);
			// params.put("city_register", cityinput);
			// System.out.println(cityinput);
			// }
			// if ("2".equals(cityselect)) {
			// params.put("city_cur", cityinput);
			// }
			// if ("3".equals(cityselect)) {
			// // 选择其中的一种
			// params.put("city_cur1", cityinput);
			// }
			// if (groupselect.equals("0")) {
			// groupinput=null;
			// }
			// 只查询出个人用户
			if (chkuser == 1) {
				params.put("gr", "1");

			}
			if (chkgroup == 1) {
				params.put("jt", "2");

			}
			if (chkunit == 1) {
				params.put("hz", "3");

			}
			if (userselect == 0) {
				params.put("userid", input);

			}
			if (userselect == 1) {
				params.put("username", "%"+input+"%");

			}
			if (userselect == 2) {
				params.put("phone", input);
			}
			if (userselect == 3) {
				params.put("sum", input);
			}
			if (userselect == 4) {
				params.put("order_cs_id", input);

			}
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			users = myService.findwait(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", myService.getCountwait(params));
			jsonMap.put("rows", users);
			jsonObject = JSONObject.fromObject(jsonMap);

		
		
	

		return SUCCESS;
	}

	/**
	 *  修改重提 信息获得
	 */
	public String findbyid() {
		//System.out.println(usersid + "," + username);

		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		UsersEntity usersEntity;
	
			if (id > 0) {
				usersEntity = myService.findbyid(id);
				jsonMap.put("rows", usersEntity);
			}
			jsonObject = JSONObject.fromObject(jsonMap);
			
		return SUCCESS;
	}

	/**
	 *  驳回
	 */
	public String unpass() {
		//System.out.println("unpass begin");
		//System.out.println("id" + unformid);
		//System.out.println("cause" + reject_cause);
		Map<String, Object> params = new HashMap<String, Object>();
		
			if (unformid > -1 && null != reject_cause) {
				System.out.println("unpass if");
				params.put("unformid", unformid);
				params.put("reject_cause", reject_cause);
				myService.updateunpass(params);

			}
			



		return SUCCESS;
	}

	/**
	 *  通过
	 * @return
	 */
	public String pass() {
		//System.out.println("pass begin");
		//System.out.println("id" + unformid);
		//System.out.println("cause" + reject_cause);
		Map<String, Object> params2 = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> params3 = new HashMap<String, Object>();
		Map<String, Object> params4 = new HashMap<String, Object>();
		Map<String, Object> params5 = new HashMap<String, Object>();
		Map<String, Object> tsinfo = new HashMap<String, Object>();
		Map<String, Object> balance = new HashMap<String, Object>();

			if (unformid > -1) {
				params2.put("chergeformid", unformid);
				int passtsid = myService.findtsid(params2);
				//System.out.println("pass if");
				params.put("unformid", unformid);
				// params3.put("tsid", passtsid);
			BigDecimal b = new BigDecimal(0.00);

				tsinfo = myService.findtsinfo(passtsid);	// 取出里面的信息,用于新数据的插入
			
				long order_cs_id = (long) tsinfo.get("order_cs_id");
				long order_id = (long) tsinfo.get("order_id");
				int order_type = (int) tsinfo.get("order_type");
				String ts_type ;
			
				int oper;
				//根据sum与0的比较来判断进出帐
		if(passsum.compareTo(b)==-1){
					oper=1;
					ts_type="tx_code_012";
				}
		if(passsum.compareTo(b)==1){
			oper=2;	
			ts_type="tx_code_011";
			}else {
				oper=0;
				ts_type="";
			}
				int ts_way = 4;
				long userid = (long) tsinfo.get("userid");
				long groupid = (long) tsinfo.get("groupid");
				long unitid = (long) tsinfo.get("unitid");
				String remark = (String) tsinfo.get("remark");
				String account = (String) tsinfo.get("account");
				int account_type = (int) tsinfo.get("account_type");
				String application = "管理后台";				
				params5.put("uid", uid);				
				//根据客户类型的不同拿出的信息也就不同
				if (uaccounttype == 1) {

					balance = myService.finduserbalance(params5);
					ubalance = (BigDecimal) balance.get("ubalance");
					ubalance1=ubalance.add(passsum);
				}
				if (uaccounttype == 2) {
					balance=myService.findgroupbalance(params5);
					ubalance =  (BigDecimal) balance.get("ubalance");
					ubalance1=ubalance.add(passsum);
				}
				if (uaccounttype == 3) {
					balance=myService.findunitbalance(params5);
					ubalance =  (BigDecimal) balance.get("ubalance");
					ubalance1=ubalance.add(passsum);
				}
				
				params3.put("sum", passsum);
				params3.put("balance", ubalance1);
				//System.out.println(ubalance1+"最后余额");
				if (tsorderid > 0) {
					params3.put("order_cs_id", tsorderid);
				} else {
					params3.put("order_cs_id", 0);
				}
				if (order_id > 0) {
					params3.put("order_id", order_id);
				} else {
					params3.put("order_id", 0);
				}
				if (order_type > 0) {
					params3.put("order_type", order_type);
				} else {
					params3.put("order_type", 0);
				}
				
					params3.put("oper", oper);
				
				if (ts_way > 0) {
					params3.put("ts_way", ts_way);
				} else {
					params3.put("ts_way", 1);// 默认
				}
				if (userid > 0) {
					params3.put("userid", userid);
				} else {
					params3.put("userid", 0);
				}
				if (groupid > 0) {
					params3.put("groupid", groupid);
				} else {
					params3.put("groupid", 0);
				}
				if (unitid > 0) {
					params3.put("unitid", unitid);
				} else {
					params3.put("unitid", 0);

				}
				if (null != remark) {
					params3.put("remark", remark);
				} else {
					params3.put("remark", "");
				}
				if (null != account) {
					params3.put("account", account);
				} else {
					params3.put("account", "");
				}
				if (account_type > 0) {
					params3.put("account_type", account_type);
				} else {
					params3.put("account_type", 1);
				}
				if (null != application) {
					params3.put("application", application);
				} else {
					params3.put("application", "");
				}
			
					params3.put("ts_type", ts_type);


				params4.put("uid", uid);

				// params.put("tsid",params3.get("id"));
				//插入一条新数据
				myService.addmodifytsbal(params3);
				//获得返回的id
				long mytsid = (long) params3.get("id");
				params.put("tsid", mytsid);
				params4.put("tsid", mytsid);
				//根据客户类型返回给不同客户
				if (uaccounttype == 1) {

					myService.updateupuser(params4);
				}
				if (uaccounttype == 2) {
					myService.updateupgroup(params4);

				}
				if (uaccounttype == 3) {
					myService.updateupunit(params4);
				}
				myService.updatepass(params);
				System.out.println(mytsid);

			}
			
		
		

		return SUCCESS;
	}

	// 跳转到选择订单
	// public String ordersearch(){
	//
	// ActionContext.getContext().put("usersid",usersid);
	//
	//
	// return SUCCESS;
	// }
	/**
	 * 选择单个订单
	 * @return
	 */
	public String findorder() {
		HttpServletRequest request = ServletActionContext.getRequest();
		//System.out.println(begintime);
		List<OrderEntity> usersorder;// 返回的实体类存放处
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
		/**
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
		/**
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
		/**
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

			usersorder = myService.findorder(params);
			jsonMap.put("total", myService.getOrderCount(params));
			jsonMap.put("rows", usersorder);
			jsonObject = JSONObject.fromObject(jsonMap);
			
	
		return SUCCESS;
	}

	/**
	 *  充值action
	 * @return
	 */
	public String cherge() {
		//System.out.println("cid" + chergeformid);
		// 用于修改req
		Map<String, Object> params = new HashMap<String, Object>();
		// 用于修改ts
		Map<String, Object> params1 = new HashMap<String, Object>();
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("chergeformid", chergeformid);
		int tsid = myService.findtsid(params2);
		params.put("chergeformid", chergeformid);
		if(ctype==1){chergesum=0-chergesum;}
		params.put("sum", chergesum);
		params.put("req_cause", req_cause);
		params.put("remark", chergeremark);
		params.put("order_id", chergeorder);

			myService.updatemodifyreq(params);
			
	
		

		return SUCCESS;
	}

	/**
	 *  查看详细跳转
	 * @return
	 */
	public String info() {

		ActionContext.getContext().put("orderid", orderid);
		return SUCCESS;
	}

	/*
	 * 详细信息查询
	 */
	public String orederview() {
		String passengername = null;
		String drivername = null;
		String pphone = null;
		String dphone = null;
		String premark = null;
		String dremark = null;
		int ordertype;
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> midmap = new HashMap<String, Object>();
		Map<String, Object> passengermap = new HashMap<String, Object>();
		Map<String, Object> drivermap = new HashMap<String, Object>();
		Map<String, Object> nummap = new HashMap<String, Object>();
		Map<String, Object> tstype = new HashMap<String, Object>();
		Map<String, Object> car = new HashMap<String, Object>();
		Map<String, Object> idMap = new HashMap<String, Object>();
		params.put("orderid", orderid);
		Map<String, Object> map = myService.findone(orderid);
		int order_type;
		
		if(null!=map.get("order_type")){
			order_type =  (int) map.get("order_type");
			if(order_type==1&&null!=myService.findcar(orderid)){
				car=myService.findcar(orderid);
				int cartype = (int) car.get("reqcarstyle");
				map.put("car",cartype);
			
			}else {
				map.put("car", 0);
			}
			
		}else {
			map.put("car", 0);
		}
	
		List<Map<String, Object>> midpointlistmap = myService
				.findMidPoints(params);
		
		// 将中途点信息存进去
		if(null==midpointlistmap){
			map.put("mid1", "无");
			map.put("mid2", "无");
			map.put("mid3", "无");
			map.put("mid4", "无");
		}
		int i = midpointlistmap.size();
		if (i > 1 && midpointlistmap.get(0) != null) {
			String mid1 = (String) midpointlistmap.get(0).get("addr");
			map.put("mid1", mid1);

		} else {
			map.put("mid1", "无");
		}
		if (i > 2 && midpointlistmap.get(1) != null) {
			String mid2 = (String) midpointlistmap.get(1).get("addr");
			map.put("mid2", mid2);

		} else {
			map.put("mid2", "无");

		}
		if (i > 3 && midpointlistmap.get(2) != null) {
			String mid3 = (String) midpointlistmap.get(2).get("addr");
			map.put("mid3", mid3);

		} else {
			map.put("mid3", "无");
		}
		if (i > 4 && midpointlistmap.get(3) != null) {
			String mid4 = (String) midpointlistmap.get(3).get("addr");
			map.put("mid4", mid4);

		} else {
			map.put("mid4", "无");
		}
		// System.out.println(map.get("passenger").getClass());
		// 获得乘客
		long passengerid = (long) map.get("passenger");
		idMap.put("passengerid",passengerid);
		 int result;
		
			tstype = myService.findts(idMap);//统计客户冻结及没有冻结的绿点
			BigDecimal unfBigDecimal  = (BigDecimal) tstype.get("unf");//没冻结的总和
			BigDecimal  fBigDecimal   =(BigDecimal) tstype.get("f");//冻结的综合
			result = unfBigDecimal.compareTo(fBigDecimal);//没冻结-冻结 -1：结果小于0，0：结果等于0，1：结果大于0
			if(result<0){
				map.put("freeze",1);
			}else {
				map.put("freeze",2);}
			
		
	    
	
		passengermap = myService.finduser(passengerid);
		if(null!=passengermap.get("name")){
			passengername = (String) passengermap.get("name");
		}else {
			passengername="-";
		}
		if(null!=(String) passengermap.get("remark")){
			premark = (String) passengermap.get("remark");
		}else {
			premark="-";
		}
		if(null!=(String) passengermap.get("phone")){
			pphone = (String) passengermap.get("phone");
		}else {
			pphone="-";
		}
		
		
		// System.out.println("pn "+passengername);
		
		// 获得车主id
		long driverid = (long) map.get("driver");
		drivermap = myService.finduser(driverid);
		if(null!=drivermap.get("name")){
			drivername = (String) drivermap.get("name");
		}else {
			drivername = "-";
		}
		if(null!=drivermap.get("phone")){
			dphone = (String) drivermap.get("phone");
		}else {
			dphone = "-";
		}
		
		if(null!=drivermap.get("remark")){
			dremark = (String) drivermap.get("remark");
		}else {
			dremark ="-";
		}			
		map.put("passengername", passengername);
		map.put("pphone", pphone);
		map.put("drivername", drivername);
		map.put("dphone", dphone);
		map.put("premark", premark);
		map.put("dremark", dremark);
		//System.out.println("map:" + map);
		jsonObject = JSONObject.fromObject(map);

		return SUCCESS;
		}
	

	// ----------------------------get set--------------------

	public int getPage() {
		return page;
	}

	public double getPassbalance() {
		return passbalance;
	}

	public void setPassbalance(double passbalance) {
		this.passbalance = passbalance;
	}

	
	public BigDecimal getPasssum() {
		return passsum;
	}

	public void setPasssum(BigDecimal passsum) {
		this.passsum = passsum;
	}

	public int getChergeformid() {
		return chergeformid;
	}

	public void setChergeformid(int chergeformid) {
		this.chergeformid = chergeformid;
	}

	public int getChergeid() {
		return chergeid;
	}

	public void setChergeid(int chergeid) {
		this.chergeid = chergeid;
	}

	public double getChergebalance() {
		return chergebalance;
	}

	public void setChergebalance(double chergebalance) {
		this.chergebalance = chergebalance;
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

	public String getChergeorder() {
		return chergeorder;
	}

	public void setChergeorder(String chergeorder) {
		this.chergeorder = chergeorder;
	}

	public String getReq_cause() {
		return req_cause;
	}

	public void setReq_cause(String req_cause) {
		this.req_cause = req_cause;
	}

	public String getReject_cause() {
		return reject_cause;
	}

	public void setReject_cause(String reject_cause) {
		this.reject_cause = reject_cause;
	}

	public int getUnformid() {
		return unformid;
	}

	public void setUnformid(int unformid) {
		this.unformid = unformid;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public int getChkwait() {
		return chkwait;
	}

	public void setChkwait(int chkwait) {
		this.chkwait = chkwait;
	}

	public int getChkfinish() {
		return chkfinish;
	}

	public void setChkfinish(int chkfinish) {
		this.chkfinish = chkfinish;
	}

	public int getChkrefuse() {
		return chkrefuse;
	}

	public void setChkrefuse(int chkrefuse) {
		this.chkrefuse = chkrefuse;
	}

	public int getChkcancel() {
		return chkcancel;
	}

	public void setChkcancel(int chkcancel) {
		this.chkcancel = chkcancel;
	}

	public int getChkuser() {
		return chkuser;
	}

	public void setChkuser(int chkuser) {
		this.chkuser = chkuser;
	}

	public int getChkgroup() {
		return chkgroup;
	}

	public void setChkgroup(int chkgroup) {
		this.chkgroup = chkgroup;
	}

	public int getChkunit() {
		return chkunit;
	}

	public void setChkunit(int chkunit) {
		this.chkunit = chkunit;
	}

	public int getUserselect() {
		return userselect;
	}

	public void setUserselect(int userselect) {
		this.userselect = userselect;
	}

	// public MyFormService getMyService() {
	// return myService;
	// }
	public void setMyService(MyFormService myService) {
		this.myService = myService;
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

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getCtype() {
		return ctype;
	}

	public void setCtype(int ctype) {
		this.ctype = ctype;
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

	public MyFormService getMyService() {
		return myService;
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

	public int getColseformid() {
		return colseformid;
	}

	public void setColseformid(int colseformid) {
		this.colseformid = colseformid;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public long getTsorderid() {
		return tsorderid;
	}

	public void setTsorderid(long tsorderid) {
		this.tsorderid = tsorderid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getUaccounttype() {
		return uaccounttype;
	}

	public void setUaccounttype(int uaccounttype) {
		this.uaccounttype = uaccounttype;
	}

}
