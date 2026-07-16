package com.nmdy.customerManage.appSpreadUnit.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.nmdy.customerManage.appSpreadUnit.dao.App_Spread_Unit;
import com.nmdy.customerManage.appSpreadUnit.service.App_Spread_UnitService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;









import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*@Controller
@Scope("prototype")*/
public class App_Spread_UnitAction extends ActionSupport{

	//--------easyUI分页信息参数--------------
	private int page;
	private int rows;
	//-------条件查询参数-----------	
	private String conditionSelect;//下拉框选项参数
	private String conditionInput;//输入查询条件
	private String verifiedStaus;//认证状态

	//-------JSON数据格式参数-----------
	private JSONObject jsonObject;
	private JSONArray jsonArray;
	
	private String id;
	private String unit_id;
	private String name;
	private String create_date; 
	private String linkname;
	private String linkphone;
	private String group_property;
	private String contract_no;
	private String fix_phone;
	private String email;
	private String fax;
	private String group_address;
	private String sign_time;
	private String invite_code;
	private String remark;
	
	//---------分成相关信息---------
	private String ratio_as_passenger_self;
	private String integer_as_passenger_self;
	private String active_as_passenger_self;
	private String ratio_as_driver_self;
	private String integer_as_driver_self;
	private String active_as_driver_self;
	private String limit_month_as_passenger_self;
	private String limit_month_as_driver_self;
	private String limit_count_as_passenger_self;
	private String limit_count_as_driver_self;
	private String passengerRatioSelect;
	private String driverRatioSelect;
	private String passengerRatioSelectInput;
	private String driverRatioSelectInput;
	
	
	//@Resource
	private App_Spread_UnitService app_Spread_UnitService;

	

	public App_Spread_UnitService getApp_Spread_UnitService() {
		return app_Spread_UnitService;
	}

	public void setApp_Spread_UnitService(
			App_Spread_UnitService app_Spread_UnitService) {
		this.app_Spread_UnitService = app_Spread_UnitService;
	}

	public String app_Spread_UnitListUI()  throws Exception {
		return "app_Spread_UnitListUI";
	}
	
	public String app_Spread_UnitPageList() {
		try {
			List<Map<String, Object>> list = null;
			Map<String, Object> params = new HashMap<String, Object>();
			
			if ("unit_id".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "unit_id");
				params.put("conditionInput", conditionInput);
			}
			if ("name".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "name");
				params.put("conditionInput", conditionInput);
			}

			// 查询结果从第0条开始，查询10条记录
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);

			list = app_Spread_UnitService.findApp_Spread_UnitsByCondition(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println(list.size());

			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());
			jsonMap.put("total", app_Spread_UnitService.findApp_Spread_UnitCountByCondition(params));
			// 需要实现数据的总记录数
			jsonMap.put("rows", list);
			jsonObject = JSONObject.fromObject(jsonMap);
			//将余额传到前端页面，供删除时做判断用
/*			BigDecimal balance = app_Spread_UnitService.getBalnace(Long.parseLong(id));
			ActionContext.getContext().put("balance", balance);*/
			
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String showApp_Spread_UnitUI(){

		ActionContext.getContext().put("id", id);
		return "showApp_Spread_UnitUI";
	}
	
	
	public String findApp_Spread_Unit(){

		try {
			Map<String,Object> map = app_Spread_UnitService.findApp_Spread_UnitInfo(Long.parseLong(id));	
			System.out.println(map);
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "show";
	}


	public String findApp_Spread_UnitInfo(){

		try {
			Map<String,Object> map = app_Spread_UnitService.findApp_Spread_UnitInfo(Long.parseLong(id));	
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String  updateApp_Spread_UnitInfoUI(){
		ActionContext.getContext().put("id", id);
		
		return"updateApp_Spread_UnitInfoUI";
	}
	
	public String  updateApp_Spread_UnitInfo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.parseLong(id));
		params.put("name", name);
		params.put("linkname", linkname);
		params.put("linkphone", linkphone);
		params.put("group_property", group_property);
		params.put("contract_no", contract_no);
		params.put("fix_phone", fix_phone);
		params.put("email", email);
		params.put("fax", fax);
		params.put("group_address", group_address);
		params.put("invite_code", invite_code);
		params.put("remark", remark);
		
		
		params.put("limit_month_as_passenger_self", Integer.parseInt(limit_month_as_passenger_self));
		params.put("limit_month_as_driver_self", Integer.parseInt(limit_month_as_driver_self));
		params.put("limit_count_as_passenger_self", Integer.parseInt(limit_count_as_passenger_self));
		params.put("limit_count_as_driver_self", Integer.parseInt(limit_count_as_driver_self));

		if ("0".equals(driverRatioSelect)) {
			params.put("active_as_driver_self", Integer.parseInt(driverRatioSelect));
			params.put("integer_as_driver_self", new BigDecimal(driverRatioSelectInput));
		}else {
			params.put("active_as_driver_self", Integer.parseInt(driverRatioSelect));
			params.put("ratio_as_driver_self", new BigDecimal(driverRatioSelectInput));
		}
		
		if ("0".equals(passengerRatioSelect)) {
			params.put("active_as_passenger_self", Integer.parseInt(passengerRatioSelect));
			params.put("integer_as_passenger_self", new BigDecimal(passengerRatioSelectInput));
		}else {
			params.put("active_as_passenger_self", Integer.parseInt(passengerRatioSelect));
			params.put("ratio_as_passenger_self", new BigDecimal(passengerRatioSelectInput));
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			try {
				params.put("create_date",sdf.parse(create_date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			app_Spread_UnitService.updateApp_Spread_UnitInfo(params);
		
		return  "updateApp_Spread_UnitInfo";
	}
	
	public String newApp_Spread_UnitUI(){
		
		
		return "newApp_Spread_UnitUI";
	}
	
	public String saveApp_Spread_Unit(){
		
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> tsparams = new HashMap<String, Object>();
		Map<String, Object> upunitparams = new HashMap<String, Object>();
		java.util.Date dd=new java.util.Date();
		Long ddxLong=dd.getTime();
		params.put("unit_id", ddxLong.toString());
		params.put("name", name);
		params.put("linkname", linkname);
		params.put("linkphone", linkphone);
		params.put("group_property", group_property);
		params.put("contract_no", contract_no);
		params.put("fix_phone", fix_phone);
		params.put("email", email);
		params.put("fax", fax);
		params.put("group_address", group_address);
		params.put("invite_code", invite_code);
		//params.put("active_as_drivr_self", active_as_drivr_self);
		params.put("remark", remark);
		
		
		params.put("limit_month_as_passenger_self", Integer.parseInt(limit_month_as_passenger_self));
		params.put("limit_month_as_driver_self", Integer.parseInt(limit_month_as_driver_self));
		params.put("limit_count_as_passenger_self", Integer.parseInt(limit_count_as_passenger_self));
		params.put("limit_count_as_driver_self", Integer.parseInt(limit_count_as_driver_self));

		if ("0".equals(driverRatioSelect)) {
			params.put("active_as_driver_self", Integer.parseInt(driverRatioSelect));
			params.put("integer_as_driver_self", new BigDecimal(driverRatioSelectInput));
			params.put("ratio_as_driver_self", new BigDecimal(driverRatioSelectInput));
		}else {
			params.put("active_as_driver_self", Integer.parseInt(driverRatioSelect));
			params.put("ratio_as_driver_self", new BigDecimal(driverRatioSelectInput));
			params.put("integer_as_driver_self", new BigDecimal(driverRatioSelectInput));
			
		}
		
		if ("0".equals(passengerRatioSelect)) {
			params.put("active_as_passenger_self", Integer.parseInt(passengerRatioSelect));
			params.put("integer_as_passenger_self", new BigDecimal(passengerRatioSelectInput));
			params.put("ratio_as_passenger_self", new BigDecimal(passengerRatioSelectInput));
		}else {
			params.put("active_as_passenger_self", Integer.parseInt(passengerRatioSelect));
			params.put("ratio_as_passenger_self", new BigDecimal(passengerRatioSelectInput));
			params.put("integer_as_passenger_self", new BigDecimal(passengerRatioSelectInput));
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		try {
			params.put("create_date",sdf.parse(create_date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		app_Spread_UnitService.saveApp_Spread_Unit(params);
		long id = (long) params.get("id");
		tsparams.put("unitid",id);
		tsparams.put("remark","注册");
		tsparams.put("account_type", 3);
		tsparams.put("ts_type","tx_code_001");
		tsparams.put("balance",0);
		app_Spread_UnitService.modifytsbal(tsparams);
		long tsid = (long) tsparams.get("id");
		upunitparams.put("tsid",tsid);
		upunitparams.put("id", id);
		app_Spread_UnitService.seUnittsid(upunitparams);
		
		return "saveApp_Spread_Unit";
	}
	
	public String  delApp_Spread_Unit(){
		
		app_Spread_UnitService.delApp_Spread_Unit(Long.parseLong(id));
		
		return "delApp_Spread_Unit";
	}
	
	
	//-------------返利信息------------------
	/**
	 * 打开返利信息页面
	 * @return
	 */
	public String ASURebateInfoUI(){

		ActionContext.getContext().put("id", id);//集团id
		return "ASURebateInfoUI";
	}
	

	
	
	/**
	 * 获得所有返利列表
	 * @return
	 */
	public  String findASUAllRebateListInfo(){
		List<Map<String, Object>> list = null;
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("id", Long.parseLong(id));
		params.put("id", id);
		list = app_Spread_UnitService.findASUAllRebateListInfo(params);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		System.out.println(list.size());

		// 需要显示数据的总页数
		 //jsonMap.put("total", list.size());
		jsonMap.put("total", list.size());
		// 需要实现数据的总记录数
		jsonMap.put("rows", list);
		jsonObject = JSONObject.fromObject(jsonMap);
		System.out.println(jsonObject);
		
		return SUCCESS;
	}
	

	
	/**
	 * 获得指定用户的返利列表信息
	 * @return
	 */
	public String findASURebateListInfo(){
		List<Map<String, Object>> list = app_Spread_UnitService.findASURebateListInfo(Long.parseLong(id));
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		System.out.println(list.size());

		// 需要显示数据的总页数
		 //jsonMap.put("total", list.size());
		jsonMap.put("total", list.size());
		// 需要实现数据的总记录数
		jsonMap.put("rows", list);
		jsonObject = JSONObject.fromObject(jsonMap);
		System.out.println(jsonObject);
		return SUCCESS;
	}
	
	
	//------getter/setter----------------------

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

	public String getConditionSelect() {
		return conditionSelect;
	}

	public void setConditionSelect(String conditionSelect) {
		this.conditionSelect = conditionSelect;
	}

	public String getConditionInput() {
		return conditionInput;
	}

	public void setConditionInput(String conditionInput) {
		this.conditionInput = conditionInput;
	}

	public String getVerifiedStaus() {
		return verifiedStaus;
	}

	public void setVerifiedStaus(String verifiedStaus) {
		this.verifiedStaus = verifiedStaus;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getLinkname() {
		return linkname;
	}

	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}

	public String getLinkphone() {
		return linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	public String getGroup_property() {
		return group_property;
	}

	public void setGroup_property(String group_property) {
		this.group_property = group_property;
	}

	public String getContract_no() {
		return contract_no;
	}

	public void setContract_no(String contract_no) {
		this.contract_no = contract_no;
	}

	public String getFix_phone() {
		return fix_phone;
	}

	public void setFix_phone(String fix_phone) {
		this.fix_phone = fix_phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getGroup_address() {
		return group_address;
	}

	public void setGroup_address(String group_address) {
		this.group_address = group_address;
	}

	public String getSign_time() {
		return sign_time;
	}

	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}

	public String getInvite_code() {
		return invite_code;
	}

	public void setInvite_code(String invite_code) {
		this.invite_code = invite_code;
	}



	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRatio_as_driver_self() {
		return ratio_as_driver_self;
	}

	public void setRatio_as_driver_self(String ratio_as_driver_self) {
		this.ratio_as_driver_self = ratio_as_driver_self;
	}

	public String getInteger_as_driver_self() {
		return integer_as_driver_self;
	}

	public void setInteger_as_driver_self(String integer_as_driver_self) {
		this.integer_as_driver_self = integer_as_driver_self;
	}

	public String getRatio_as_passenger_self() {
		return ratio_as_passenger_self;
	}

	public void setRatio_as_passenger_self(String ratio_as_passenger_self) {
		this.ratio_as_passenger_self = ratio_as_passenger_self;
	}

	public String getInteger_as_passenger_self() {
		return integer_as_passenger_self;
	}

	public void setInteger_as_passenger_self(String integer_as_passenger_self) {
		this.integer_as_passenger_self = integer_as_passenger_self;
	}

	public String getActive_as_passenger_self() {
		return active_as_passenger_self;
	}

	public void setActive_as_passenger_self(String active_as_passenger_self) {
		this.active_as_passenger_self = active_as_passenger_self;
	}

	public String getActive_as_driver_self() {
		return active_as_driver_self;
	}

	public void setActive_as_driver_self(String active_as_driver_self) {
		this.active_as_driver_self = active_as_driver_self;
	}

	public String getLimit_month_as_passenger_self() {
		return limit_month_as_passenger_self;
	}

	public void setLimit_month_as_passenger_self(
			String limit_month_as_passenger_self) {
		this.limit_month_as_passenger_self = limit_month_as_passenger_self;
	}

	public String getLimit_month_as_driver_self() {
		return limit_month_as_driver_self;
	}

	public void setLimit_month_as_driver_self(String limit_month_as_driver_self) {
		this.limit_month_as_driver_self = limit_month_as_driver_self;
	}

	public String getLimit_count_as_passenger_self() {
		return limit_count_as_passenger_self;
	}

	public void setLimit_count_as_passenger_self(
			String limit_count_as_passenger_self) {
		this.limit_count_as_passenger_self = limit_count_as_passenger_self;
	}

	public String getLimit_count_as_driver_self() {
		return limit_count_as_driver_self;
	}

	public void setLimit_count_as_driver_self(String limit_count_as_driver_self) {
		this.limit_count_as_driver_self = limit_count_as_driver_self;
	}

	public String getPassengerRatioSelect() {
		return passengerRatioSelect;
	}

	public void setPassengerRatioSelect(String passengerRatioSelect) {
		this.passengerRatioSelect = passengerRatioSelect;
	}

	public String getDriverRatioSelect() {
		return driverRatioSelect;
	}

	public void setDriverRatioSelect(String driverRatioSelect) {
		this.driverRatioSelect = driverRatioSelect;
	}

	public String getPassengerRatioSelectInput() {
		return passengerRatioSelectInput;
	}

	public void setPassengerRatioSelectInput(String passengerRatioSelectInput) {
		this.passengerRatioSelectInput = passengerRatioSelectInput;
	}

	public String getDriverRatioSelectInput() {
		return driverRatioSelectInput;
	}

	public void setDriverRatioSelectInput(String driverRatioSelectInput) {
		this.driverRatioSelectInput = driverRatioSelectInput;
	}


	

	
}
