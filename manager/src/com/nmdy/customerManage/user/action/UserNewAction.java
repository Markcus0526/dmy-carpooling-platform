package com.nmdy.customerManage.user.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.nmdy.activity.service.ActivityService;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.customerManage.user.service.UserNewService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/*@Controller
@Scope("prototype")*/
public class UserNewAction extends ActionSupport{
	//--------easyUI分页信息参数--------------
	private int page;
	private int rows;
	//-------条件查询参数-----------	
	private String conditionSelect;//下拉框选项参数
	private String conditionInput;//输入查询条件
	private String verifiedStaus;//认证状态

	//-------JSON数据格式参数-----------
	
	private JSONObject jsonObject;
	private JSONObject resultObject;
	private JSONArray jsonArray;
	//---------用户基本信息参数-----------
	
	private String id ;
	private String group_name;//所属集团名称----三表联查，user表-group_details表-group_表
	private String usercode  ;// text comment '用户名',
	private String nickname  ;// text comment '昵称',
	private String phone     ;// text comment '电话，忘记密码时用',
	private String remark           ;//text comment  '备注',
	private String img              ;// text comment '用户图像',
	private String person_verified  ;//  int comment '个人身份通过验证,0未认证，1已认证，2待认证',
	private String driver_verified  ;//  int comment '车主身份通过验证,0未认证，1已认证，2待认证',

	//----------身份认证相关信息-----------
	private String username  ;// text comment '姓名',
	private String nation           ;//text comment '民族',
	private String id_card_num     ;//text comment  '身份证号',
	private String sex              ;//text comment '性别',
	private String address          ;//text comment '住址',
	private String birthday        ;//date comment '出生日期',年龄不用单独存，用当前日期-出生日期计算得出
	private String id_card1         ;// text comment '身份证 正面',
	private String id_card2         ;// text,comment '身份证 反面',
	
	//-----------车主认证相关属性---------------------
	private String drivinglicence_num;
	private String brand;
	private String drlicence_ti;
	private String style;
	private String color;
	private String plate_num;
	private String vin;
	private String eno;
	private String vehicle_owner;
	private String is_oper_vehicle;
	private String driver_license1;
	private String driver_license2;
	private String driving_license1;
	private String driving_license2;
	private String car_img;
	private String person_sex;
	private String person_username;
	private String car_type_id;//

	
	//返利相关
	private String invitecode_self;
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
	private String oper_vehicle_Select;
	private String sexSelect;
	private String person_sexSelect;
	private ActivityService activityService;
	//--------service属性--------------
	//@Resource
	private UserNewService userNewService;
	private long ids;
	private int status;
	private int driver_level;
	


	public UserNewService getUserNewService() {
		return userNewService;
	}

	public void setUserNewService(UserNewService userNewService) {
		this.userNewService = userNewService;
	}

	public String list() throws Exception {
		return "list";
	}

	public String pageList() {
		try {
			List<Map<String, Object>> list = null;
			Map<String, Object> params = new HashMap<String, Object>();
			
			if ("usercode".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "usercode");
				params.put("conditionInput", conditionInput);
			}
			if ("username".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "username");
				params.put("conditionInput", conditionInput);
			}

			if ("id".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "id");
				params.put("conditionInput", conditionInput);
			}
			if ("phone".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "phone");
				params.put("conditionInput", conditionInput);
			}
			if ("0".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "all");
				params.put("conditionInput", conditionInput);
			}
			if (verifiedStaus!=null) {
				params.put("verifiedStaus", verifiedStaus);				
			}
			// 查询结果从第0条开始，查询10条记录
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			params.put("status", status);
			System.out.println("status:"+status);
			list = userNewService.findUsersByCondition(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println(list.size());

			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());//不能用这种形式，因为有可能查出来的list为null，那么就会出现空指针异常。可以做判断，如果list为null，就返回0；也可以单独定义一个count（*）的方法来获得符合条件的总记录数。
			jsonMap.put("total", userNewService.findCountByCondition(params));
			
			// 需要实现数据的总记录数
			jsonMap.put("rows", list);
			jsonObject = JSONObject.fromObject(jsonMap);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String index() {
		return "index";
	}
	
	public String showUI(){
		//System.out.println("UserAction.showUI()********************"+ActionContext.getContext().getSession().put("id",id));

		
		ActionContext.getContext().put("id", id);
		ActionContext.getContext().put("car_type_id", car_type_id);
		return "showUI";
	}
	
	public String show(){

		try {
			Map<String,Object> map = userNewService.findUserAllInfo(Long.parseLong(id));	
/*			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("map", map);*/
			System.out.println(map);
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "show";
	}
	
	public String findUserAllInfo(){

		try {
			Map<String,Object> map = userNewService.findUserAllInfo(Long.parseLong(id));	
/*			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("map", map);*/
			//System.out.println(map);
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}
	
	public String updateUserBaseInfoUI(){
		//把id放入到session作用域中，其他tab页可以直接从session中拿到id
		ActionContext.getContext().getSession().put("id", id);
		//把car_type_id 放入到值栈中
		//由于车辆品牌、型号要修改，要从car_type表中读取数据，所以默认显示（未修改前回显得值）需要通过这个id查出来
		ActionContext.getContext().put("car_type_id", car_type_id);
		
		return "updateUserBaseInfoUI";
	}
	
	public String updateUserBaseInfo(){
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("id", id);
		System.out.println("ids:"+ids);
		params.put("id", ids);
		params.put("usercode", usercode);
		params.put("username", username);
		params.put("nickname", nickname);
		System.out.println("driver_level:"+driver_level);
		params.put("driver_level", driver_level);
		params.put("phone", phone);
		if ("0".equals(sexSelect)) {
			params.put("sex", 0);
		}else {
			params.put("sex", 1);
		}
		params.put("remark", remark);
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			//int j= 2/0;
			returnCode = userNewService.updateUserBaseInfo(params);
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "更新成功！");
			ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		return "ddd";
	}
	
	public String updatePersonVerifiedInfo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.parseLong(id));
		params.put("username", person_username);
		params.put("nation", nation);
		params.put("id_card_num", id_card_num);
		params.put("address", address);
		if ("0".equals(person_sexSelect)) {
			params.put("sex", 0);
		}else {
			params.put("sex", 1);
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			try {
				params.put("birthday",sdf.parse(birthday));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			returnCode = userNewService.updatePersonVerifiedInfo(params);
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "更新成功！");
			ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		return "updatePersonVerifiedInfo";
	}
	
	

	public String updateDriverVerifiedInfo() throws ParseException{
		//------更新user表中数据-----------
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.parseLong(id));
		params.put("drivinglicence_num", drivinglicence_num);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		params.put("drlicence_ti",sdf.parse(drlicence_ti));
		userNewService.updateDriverVerifiedInfo(params);
		//----------更user_car表中的数据--------------
		
		//-----------------------
		//根据品牌和型号获得对应的car_type表中的记录，查出对应的car_type_id并将其保存到user_car表中
		Map<String,Object> carMap = new HashMap<String,Object>();
		carMap.put("brand", brand);
		carMap.put("style", style);
		long car_type_id = userNewService.findCarTypeID(carMap);
		//-----------------------
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("id", Long.parseLong(id));
		params1.put("car_type_id", car_type_id);
/*		params1.put("brand", brand);
		params1.put("style", style);*/
		params1.put("color", color);
		params1.put("plate_num", plate_num);
		params1.put("vin", vin);
		params1.put("eno", eno);
		params1.put("vehicle_owner", vehicle_owner);
		if ("0".equals(oper_vehicle_Select)) {
			params1.put("is_oper_vehicle", 0);
		}else if("1".equals(oper_vehicle_Select)){
			params1.put("is_oper_vehicle", 1);
		}
		
		
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			returnCode = userNewService.updateDriverVerifiedInfo_UserCar(params1);
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "更新成功！");
			ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		return "updateDriverVerifiedInfo";
	}
	
	public String personVerifiedUI(){
		
		ActionContext.getContext().put("id", id);

		return "personVerifiedUI";
	}
	
	public String findPersonVerifiedInfo(){
		
		try {
			Map<String,Object> map = userNewService.findPersonVerifiedInfo(Long.parseLong(id));	
/*			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("map", map);*/
			//System.out.println(map);
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return SUCCESS;
	}
	
	
	public String personVerified(){
		Map<String, Object> params = new HashMap<String, Object>();		
		params.put("id", Long.parseLong(id));
		params.put("username", username);
		params.put("nation", nation);
		params.put("id_card_num", id_card_num);
		params.put("address", address);
		if ("0".equals(person_sexSelect)) {
			params.put("sex", 0);
		}else {
			params.put("sex", 1);
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			try {
				params.put("birthday",sdf.parse(birthday));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Map<String,Object> resultObj = new HashMap<String,Object>();
			int returnCode= 0;
			try {
				//int i=2/0;
				returnCode = userNewService.personVerified(params);	
				resultObj.put("returnCode",returnCode);
				resultObj.put("msg", "更新成功！");
				ActionContext.getContext().put("id", id);
			} catch (Exception e) {
				resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
				e.printStackTrace();
			}finally{
				resultObject=JSONObject.fromObject(resultObj);
			}
			
		
		return "personVerified";
	}

	public String personVerifiedRejected(){
		userNewService.personVerifiedRejected(Long.parseLong(id));
		return "personVerifiedRejected";
	}
	
	//--------------车主验证相关--------------------
	public String driverVerifiedUI(){
		
		ActionContext.getContext().put("id", id);
		//把car_type_id 放入到值栈中
		//由于车辆品牌、型号要修改，要从car_type表中读取数据，所以默认显示（未修改前回显得值）需要通过这个id查出来
		ActionContext.getContext().put("car_type_id", car_type_id);
		return "driverVerifiedUI";
	}
	
	
	public String findDriverVerifiedInfo(){
		
		try {
			Map<String,Object> map = userNewService.findDriverVerifiedInfo(Long.parseLong(id));	
/*			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("map", map);*/
			//System.out.println(map);
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return SUCCESS;
	}
	
	
	public String driverVerified() throws ParseException{
//		"driverVerified"
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		String str = drivinglicence_num.substring(6, 14);
		String y = str.substring(0, 4);
		y += "-";
		String m = str.substring(4, 6);
		m += "-";
		String d = str.substring(str.length() - 2, str.length());
		String result = y+m+d;	
		params.put("id", Long.parseLong(id));
		params.put("drivinglicence_num", drivinglicence_num);
		params.put("username", username);
		params.put("birthday", sdf.parse(result));
		params.put("id_card_num", drivinglicence_num);
		
		try {
			params.put("drlicence_ti",sdf.parse(drlicence_ti));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
		/*根据品牌和型号获得对应的car_type表中的记录*/
		Map<String,Object> carMap = new HashMap<String,Object>();
		carMap.put("brand", brand);
		carMap.put("style", style);
		long car_type_id = userNewService.findCarTypeID(carMap);
		userNewService.driverVerified(params);
		//----------更user_car表中的数据--------------
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("id", Long.parseLong(id));
		params1.put("car_type_id", car_type_id);
		params1.put("color", color);
		params1.put("plate_num", plate_num);
		params1.put("vin", vin);
		params1.put("eno", eno);
		params1.put("vehicle_owner", vehicle_owner);
		if ("1".equals(oper_vehicle_Select)) {
			params1.put("is_oper_vehicle", 1);
		}else if("0".equals(oper_vehicle_Select)){
			params1.put("is_oper_vehicle", 0);
		}
			returnCode = userNewService.updateDriverVerifiedInfo_UserCar(params1);	
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "车主认证成功！");
			ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败");
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		return "driverVerified";
	}

	public String driverVerifiedRejected(){
		
		
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			returnCode = userNewService.driverVerifiedRejected(Long.parseLong(id));
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "车主认证驳回成功！");
			ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "操作失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		return "driverVerifiedRejected";
	}
	
	/*查询所有的车辆品牌*/
	public String findCarBrand(){
		List<Map<String, Object>> list = userNewService.findCarBrand();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		 jsonArray = new JSONArray() ;
		 jsonArray.addAll(list);
		return SUCCESS;
	}
	
	/*根据品牌查询旗下的车辆型号*/
	public String findCarStyle(){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("brand",brand);
		List<Map<String, Object>> list = userNewService.findCarStyle(params);
		jsonArray = new JSONArray() ;
		jsonArray.addAll(list);
		System.out.println(jsonArray);
		//jsonObject=jsonObject.fromObject(modelService.findAll(params));
		return SUCCESS;
	}
	
	
	

	
	//-------------返利信息---------------
	public String updateRebateInfo(){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.parseLong(id));
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
		
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			returnCode = userNewService.updateRebateInfo(params);	
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "更新成功！");
			ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		return "updateRebateInfo";
	} 
	/**
	 * 获得所有返利列表
	 * @return
	 */
	public  String findAllRebateListInfo(){
		List<Map<String, Object>> list = null;
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("id", Long.parseLong(id));
		params.put("invitecode_self", invitecode_self);
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);

		list = userNewService.findAllRebateListInfo(params);
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
	
	public String showRebateListInfoUI(){
		
		
		return "showRebateListInfoUI";
	}
	
	/**
	 * 获得指定用户的返利列表信息
	 * @return
	 */
	public String findRebateListInfo(){
		List<Map<String, Object>> list = userNewService.findRebateListInfo(Long.parseLong(id));
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
	
	// -----------------Getter Setter--------------------------------

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	
	
	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
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
	



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}



	public String getPerson_verified() {
		return person_verified;
	}

	public void setPerson_verified(String person_verified) {
		this.person_verified = person_verified;
	}

	public String getDriver_verified() {
		return driver_verified;
	}

	public void setDriver_verified(String driver_verified) {
		this.driver_verified = driver_verified;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}





	public String getId_card_num() {
		return id_card_num;
	}

	public void setId_card_num(String id_card_num) {
		this.id_card_num = id_card_num;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getId_card1() {
		return id_card1;
	}

	public void setId_card1(String id_card1) {
		this.id_card1 = id_card1;
	}

	public String getId_card2() {
		return id_card2;
	}

	public void setId_card2(String id_card2) {
		this.id_card2 = id_card2;
	}

	public String getDrivinglicence_num() {
		return drivinglicence_num;
	}

	public void setDrivinglicence_num(String drivinglicence_num) {
		this.drivinglicence_num = drivinglicence_num;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDrlicence_ti() {
		return drlicence_ti;
	}

	public void setDrlicence_ti(String drlicence_ti) {
		this.drlicence_ti = drlicence_ti;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getPlate_num() {
		return plate_num;
	}

	public void setPlate_num(String plate_num) {
		this.plate_num = plate_num;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getEno() {
		return eno;
	}

	public void setEno(String eno) {
		this.eno = eno;
	}

	public String getVehicle_owner() {
		return vehicle_owner;
	}

	public void setVehicle_owner(String vehicle_owner) {
		this.vehicle_owner = vehicle_owner;
	}

	public String getIs_oper_vehicle() {
		return is_oper_vehicle;
	}

	public void setIs_oper_vehicle(String is_oper_vehicle) {
		this.is_oper_vehicle = is_oper_vehicle;
	}

	public String getDriver_license1() {
		return driver_license1;
	}

	public void setDriver_license1(String driver_license1) {
		this.driver_license1 = driver_license1;
	}

	public String getDriver_license2() {
		return driver_license2;
	}

	public void setDriver_license2(String driver_license2) {
		this.driver_license2 = driver_license2;
	}

	public String getDriving_license1() {
		return driving_license1;
	}

	public void setDriving_license1(String driving_license1) {
		this.driving_license1 = driving_license1;
	}

	public String getDriving_license2() {
		return driving_license2;
	}

	public void setDriving_license2(String driving_license2) {
		this.driving_license2 = driving_license2;
	}

	public String getCar_img() {
		return car_img;
	}

	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}

	public String getPerson_sex() {
		return person_sex;
	}

	public void setPerson_sex(String person_sex) {
		this.person_sex = person_sex;
	}

	public String getPerson_username() {
		return person_username;
	}

	public void setPerson_username(String person_username) {
		this.person_username = person_username;
	}

	public String getInvitecode_self() {
		return invitecode_self;
	}

	public void setInvitecode_self(String invitecode_self) {
		this.invitecode_self = invitecode_self;
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

	public String getOper_vehicle_Select() {
		return oper_vehicle_Select;
	}

	public void setOper_vehicle_Select(String oper_vehicle_Select) {
		this.oper_vehicle_Select = oper_vehicle_Select;
	}

	public String getSexSelect() {
		return sexSelect;
	}

	public void setSexSelect(String sexSelect) {
		this.sexSelect = sexSelect;
	}

	public String getPerson_sexSelect() {
		return person_sexSelect;
	}

	public void setPerson_sexSelect(String person_sexSelect) {
		this.person_sexSelect = person_sexSelect;
	}

	public String getCar_type_id() {
		return car_type_id;
	}

	public void setCar_type_id(String car_type_id) {
		this.car_type_id = car_type_id;
	}

	public ActivityService getActivityService() {
		return activityService;
	}

	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

	public long getIds() {
		return ids;
	}

	public void setIds(long ids) {
		this.ids = ids;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDriver_level() {
		return driver_level;
	}

	public void setDriver_level(int driver_level) {
		this.driver_level = driver_level;
	}
}
