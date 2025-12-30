package com.nmdy.financial.ledger.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.nmdy.financial.ledger.service.LedgerService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.order.appoint.dao.Appoint1;

public class LedgerAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private LedgerService ledgerService;
	private int inp;// 进账
	private int out;// 出账
	private int pin;// 拼车
	private int tstype;// 账目类型

	private JSONObject jsonObject = null;
	private int page;
	private int rows;
	private String buscity = null;// 交易城市
	private String begintime = null;//
	private String endtime = null;//
	private int orderinput = 0;// 订单编号
	private int tsid = 0;// 交易编号
	private int khtype = 0;// 客户类型选择
	private int khinfo = 0;// 客户输入信息选择
	private String xianginput;
	// 查看详细
	private int orderid1;
	private String ordernum;

	/**
	 * 页面跳转
	 * 
	 * @return
	 */
	public String index() {
		return SUCCESS;
	}

	/**
	 * 数据表格加载查询方法
	 * 
	 * @return
	 */
	public String findledger() {
		System.out.println(inp + "     " + out + "     " + khinfo + "      "
				+ buscity + "        " + begintime + "         " + endtime
				+ "           " + orderinput);
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (null != buscity && !"0".equals(buscity)) {
			params.put("city", buscity);
		}
		if (null != begintime && !"0".equals(begintime)) {
			params.put("begintime", begintime+" 00:00:00");

		}
		if (null != endtime && !"0".equals(endtime)) {
			params.put("endtime", endtime+" 23:59:59");
		}
		/*
		 *  个人客户相关查询 id
		 */
		if (khtype == 1 && !"0".equals(xianginput) && khinfo == 1) {
			params.put("khtype", 1);
			params.put("khinfo", 1);
			params.put("khid", Integer.parseInt(xianginput, 10));

		}
		/*
		 * 个人客户相关查询 账号
		 */
		if (khtype == 1 && null != xianginput && khinfo == 2) {
			params.put("khtype", 1);
			params.put("khinfo", 2);
			params.put("khcode", xianginput);

		}
		/*
		 *  个人客户相关查询 姓名
		 */
		if (khtype == 1 && null != xianginput && khinfo == 3) {
			params.put("khtype", 1);
			params.put("khinfo", 3);
			params.put("username", xianginput);

		}
		/*
		 *  个人客户相关查询 手机号码
		 */
		if (khtype == 1 && null != xianginput && khinfo == 4) {
			params.put("khtype", 1);
			params.put("khinfo", 4);
			params.put("phone", xianginput);

		}
		// 集团客户

	
		if(khtype==2&&!"0".equals(xianginput)&&khinfo==1){
		 params.put("khtype",2);
		 params.put("khinfo",1);
		 params.put("khid",Integer.parseInt(xianginput,10));
		
		 }
		// //集团客户相关查询 账号
		// if(khtype==2&&null!=xianginput&&khinfo==2){
		// params.put("khtype",1);
		// params.put("khinfo",2);
		// params.put("khcode",xianginput);
		//
		// }
		/*
		 *  集团客户相关查询 姓名
		 */
		if (khtype == 2 && null != xianginput && khinfo == 3) {
			params.put("khtype", 2);
			params.put("khinfo", 3);
			params.put("username", xianginput);

		}
		// //集团客户相关查询 手机号码
		// if(khtype==2&&null!=xianginput&&khinfo==4){
		// params.put("khtype",1);
		// params.put("khinfo",4);
		// params.put("phone",xianginput);
		//
		// }

		// 合作单位
		// //合作单位客户相关查询 id
	 if(khtype==3&&!"0".equals(xianginput)&&khinfo==1){
		 params.put("khtype",3);
		 params.put("khinfo",1);
		 params.put("khid",Integer.parseInt(xianginput,10));
		
		 }
		// //合作单位客户相关查询 账号
		// if(khtype==3&&null!=xianginput&&khinfo==2){
		// params.put("khtype",1);
		// params.put("khinfo",2);
		// params.put("khcode",xianginput);
		//
		// }
		/*
		 *  合作单位相关查询 姓名
		 */
		if (khtype == 3 && null != xianginput && khinfo == 3) {
			params.put("khtype", 3);
			params.put("khinfo", 3);
			params.put("username", xianginput);

		}
		// 合作单位客户相关查询 手机号码
		// if(khtype==3&&null!=xianginput&&khinfo==4){
		// params.put("khtype",1);
		// params.put("khinfo",4);
		// params.put("phone",xianginput);
		//
		// }
		/*
		 *  进出账
		 */
		if (out == 0 && inp == 1) {
			params.put("oper1", 1);
			params.put("oper2", 0);

		}
		if (inp == 0 && out == 1) {
			params.put("oper1", 0);
			params.put("oper2", 2);

		}
		if (inp == 1 && out == 1) {
			params.put("oper1", 1);
			params.put("oper2", 2);
		}
		if (pin== 0) {
			params.put("pin",0);
		}
		if (pin== 1) {
			params.put("pin",1);
		}

		if (0 != tsid) {
			params.put("tsid", tsid);

		}
		if (0 != orderinput) {
			params.put("order_cs_id", orderinput);
		}
		// tstype
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

		//System.out.println("------" + tstype);
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		List<Map<String, Object>> ledger = ledgerService.findledger(params);
		List<Map<String, Object>> ledger1 = new ArrayList<Map<String, Object>>();
		String pir = "-";
		String dir = "-";
		// 循环取出注册码
		for (Map<String, Object> ledgerinfo : ledger) {
			// user里面的信息
			if (null != ledgerinfo.get("passenger_invitecode_regist")) {
				pir = (String) ledgerinfo.get("passenger_invitecode_regist");
			} else {

			}
			if (null != ledgerinfo.get("driver_invitecode_regist")) {
				dir = (String) ledgerinfo.get("driver_invitecode_regist");
			}

			Map<String, Object> incode = new HashMap<String, Object>();
			Map<String, Object> invite = new HashMap<String, Object>();
			// 6去user里查
			if (pir != null && pir.length() == 6) {
				Map<String, Object> infos = new HashMap<String, Object>();

				infos.put("ins", pir);
				incode = ledgerService.finduserinvit(infos);
				if (null == incode) {
					ledgerinfo.put("invitname", "没有对应数据");
					ledgerinfo.put("invitid", "没有对应数据");
				} else {
					ledgerinfo.put("invitname", incode.get("invitname"));
					ledgerinfo.put("invitid", incode.get("userid"));
				}

				// user

			}
			// 邀请码为集团客户
			if (pir != null && pir.length() == 7) {
				Map<String, Object> infos = new HashMap<String, Object>();

				infos.put("ins", pir);
				incode = ledgerService.findgroupinvit(infos);
				if (null == incode) {
					ledgerinfo.put("invitname", "没有对应数据");
					ledgerinfo.put("invitid", "没有对应数据");
					
				} else {
					ledgerinfo.put("invitname", incode.get("invitname"));
					ledgerinfo.put("invitid", incode.get("userid"));
				}

				// group

			}

			if (null != pir && pir.length() == 8) {
				Map<String, Object> infos = new HashMap<String, Object>();

				infos.put("ins", pir);
				incode = ledgerService.findunitinvit(infos);
				if (null == incode) {
					ledgerinfo.put("invitname", "没有对应数据");
					ledgerinfo.put("invitid", "没有对应数据");
				} else {
					ledgerinfo.put("invitname", incode.get("invitname"));
					ledgerinfo.put("invitid", incode.get("userid"));
				}

				// unit

			}
			if (null != dir && dir.length() == 6) {
				Map<String, Object> infos = new HashMap<String, Object>();

				infos.put("ins", dir);
				incode = ledgerService.finduserinvit(infos);
				if (null == incode) {
					ledgerinfo.put("invitnamed", "没有对应数据");
					ledgerinfo.put("invitid", "没有对应数据");
				} else {
					ledgerinfo.put("invitnamed", incode.get("invitname"));
					ledgerinfo.put("invitid", incode.get("userid"));
				}

				// user

			}
			// 邀请码为集团客户
			if (null != dir && dir.length() == 7) {
				Map<String, Object> infos = new HashMap<String, Object>();

				infos.put("ins", dir);
				incode = ledgerService.findgroupinvit(infos);
				if (null == incode) {
					ledgerinfo.put("invitnamed", "没有对应数据");
					ledgerinfo.put("invitid", "没有对应数据");
				} else {
					ledgerinfo.put("invitnamed", incode.get("invitname"));
					ledgerinfo.put("invitid", incode.get("userid"));
				}

				// group

			}

			if (null != dir && dir.length() == 8) {
				Map<String, Object> infos = new HashMap<String, Object>();

				infos.put("ins", dir);
				incode = ledgerService.findunitinvit(infos);
				if (null == incode) {
					ledgerinfo.put("invitnamed", "没有对应数据");
					ledgerinfo.put("invitid", "没有对应数据");
				} else {
					ledgerinfo.put("invitnamed", incode.get("invitname"));
					ledgerinfo.put("invitid", incode.get("userid"));
				}

				// unit

			}
			ledger1.add(ledgerinfo);
			System.out.println(incode);
		}

		Map<String, Object> count1 = ledgerService.sumbalance();
		BigDecimal countsum = (BigDecimal) count1.get("count");
		jsonMap.put("rows", ledger1);
		jsonMap.put("count", countsum);
		int count = ledgerService.gotCount(params);

		jsonMap.put("total", new Integer(count));

		jsonObject = JSONObject.fromObject(jsonMap);
		return SUCCESS;
	}

	// -----------------------------------------------------------------

	/**
	 *  查看详细跳转
	 * @return
	 */
	public String info() {
		ActionContext.getContext().put("orderid", orderid1);
		return SUCCESS;
	}

	// //详细信息查询
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
	// Map<String,Object> map = ledgerService.findone(orderid1);
	// Map<String, Object> tstype = new HashMap<String, Object>();
	// List<Map<String,Object>> midpointlistmap =
	// ledgerService.findMidPoints(params);
	// //判断订单类型 用来查订单编号
	// ordertype = (int) map.get("order_type");
	// System.out.println("___ordertype___S"+ordertype);
	// if(ordertype==1||ordertype==2){
	// //在临时表里查数据
	//
	// tstype = ledgerService.findts(orderid1);
	// //car = ledgerService.carstyle((String) nummap.get("ordernum"));
	//
	// }
	// if(ordertype==3){
	// //在上下班表里查数据
	//
	// tstype = ledgerService.findts(orderid1);
	// }
	// if(ordertype==4){
	// //在长途订单表里查数据
	//
	// tstype = ledgerService.findts(orderid1);
	// }
	// map.put("ordernum",orderid1);
	// //map.put("carstyle", car.get("reqcarstyle"));
	// map.put("tstype",tstype.get("ts_type"));
	// //将中途点信息存进去
	//
	// int i= midpointlistmap.size();
	// //System.out.println(i+"*****************");
	//
	// if(i>1&&midpointlistmap.get(0)!=null){
	// String mid1=(String) midpointlistmap.get(0).get("addr");
	// map.put("mid1",mid1);
	//
	// }else{
	// map.put("mid1","无");
	// }
	// if(i>2&&midpointlistmap.get(1)!=null){
	// String mid2=(String) midpointlistmap.get(1).get("addr");
	// map.put("mid2",mid2);
	//
	// }else{
	// map.put("mid2","无");
	// }
	// if(i>3&&midpointlistmap.get(2)!=null){
	// String mid3=(String) midpointlistmap.get(2).get("addr");
	// map.put("mid3",mid3);
	//
	// }else {
	// map.put("mid3","无");
	// }
	// if(i>4&&midpointlistmap.get(3)!=null){
	// String mid4=(String) midpointlistmap.get(3).get("addr");
	// map.put("mid4",mid4);
	//
	// }else {
	// map.put("mid4","无");
	// }
	// System.out.println(map.get("passenger").getClass());
	// //获得乘客
	// long passengerid= (long) map.get("passenger");
	// passengermap = ledgerService.finduser(passengerid);
	// passengername = (String) passengermap.get("name");
	// premark = (String) passengermap.get("remark");
	// System.out.println("pn "+passengername);
	// pphone = (String) passengermap.get("phone");
	// //获得车主id
	// long driverid=(long) map.get("driver");
	// drivermap = ledgerService.finduser(driverid);
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
	// //上下班订单特别部分的查询方法
	// public String showonoff(){
	// System.out.println("showonffs ordernum"+ordernum);
	// List<Map<String,Object>> onoff = ledgerService.showonoff(ordernum);
	// Map<String, Object> jsonMap = new HashMap<String, Object>();
	// Map<String,Object> days = ledgerService.findday(ordernum);
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
	// List<Map<String,Object>> longs = ledgerService.showlong(ordernum);;
	// Map<String, Object> jsonMap = new HashMap<String, Object>();
	// jsonMap.put("rows",longs);
	// jsonObject=JSONObject.fromObject(jsonMap);
	// return SUCCESS;
	// }
	//
	// -----------------------------------get set
	// ------------------------------------------------

	public LedgerService getLedgerService() {
		return ledgerService;
	}

	public String getBuscity() {
		return buscity;
	}

	public void setBuscity(String buscity) {
		this.buscity = buscity;
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

	public int getOrderinput() {
		return orderinput;
	}

	public void setOrderinput(int orderinput) {
		this.orderinput = orderinput;
	}

	public int getTsid() {
		return tsid;
	}

	public void setTsid(int tsid) {
		this.tsid = tsid;
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

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public void setLedgerService(LedgerService ledgerService) {
		this.ledgerService = ledgerService;
	}

	public int getInp() {
		return inp;
	}

	public void setInp(int inp) {
		this.inp = inp;
	}

	public int getOut() {
		return out;
	}

	public void setOut(int out) {
		this.out = out;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public String getXianginput() {
		return xianginput;
	}

	public void setXianginput(String xianginput) {
		this.xianginput = xianginput;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getOrderid1() {
		return orderid1;
	}

	public void setOrderid1(int orderid1) {
		this.orderid1 = orderid1;
	}

	public int getTstype() {
		return tstype;
	}

	public void setTstype(int tstype) {
		this.tstype = tstype;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

}
