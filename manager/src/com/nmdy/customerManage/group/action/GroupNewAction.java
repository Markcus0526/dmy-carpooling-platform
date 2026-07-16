package com.nmdy.customerManage.group.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.nmdy.customerManage.group.service.GroupNewService;
import com.nmdy.customerManage.user.service.UserNewService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.customer.group.dao.Group;
import com.pinche.customer.group.service.GroupService;

/*@SuppressWarnings("serial")
@Controller
@Scope("prototype")*/
public class GroupNewAction extends ActionSupport{

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
	private String groupid;
	private String group_name;
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
	private String invitecode_self;
	private String remark;
	private String ratio_as_driver_self;
	private String integer_as_driver_self;
	private String active_as_driver_self;
	private String radioSelectInput;//分成比例输入框
	private String radioSelect;//分成比例下拉框
	private String gid;
	
	
	private String rpRadioSelectInput;//更改分成比例的输入框
	private String rpRadioSelect;//更改分成比例的下拉框
	private String userid;//更改分成比例form隐藏字段
	
	//@Resource
	private GroupNewService groupNewService;
	//@Resource
	private UserNewService userNewService;
	private JSONObject resultObject;
	private GroupService groupService;
	private List<Group> listGroup;
	private int roleId;
	private String groupIds;
	private Object rObj;
	private String result;
	private int state;
	private String gProperty;//集团标识
	private Object robj;
	private int i;
	
	public GroupNewService getGroupNewService() {
		return groupNewService;
	}

	public void setGroupNewService(GroupNewService groupNewService) {
		this.groupNewService = groupNewService;
	}

	public UserNewService getUserNewService() {
		return userNewService;
	}

	public void setUserNewService(UserNewService userNewService) {
		this.userNewService = userNewService;
	}

	public String groupList()  throws Exception {
		return "groupList";
	}
	
	public String groupPageList() {
		try {
			List<Map<String, Object>> list = null;
			Map<String, Object> params = new HashMap<String, Object>();
			
			if ("groupid".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "groupid");
				params.put("conditionInput", conditionInput);
			}
			if ("group_name".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "group_name");
				params.put("conditionInput", conditionInput);
			}

			// 查询结果从第0条开始，查询10条记录
			if( (page - 1) * rows==-10){
				params.put("start", 0);
			}else{
				params.put("start", (page - 1) * rows);
			}
			
			params.put("limit", rows);
			System.out.println("start:"+(page - 1) * rows);
			list = groupNewService.findGroupsByCondition(params);
	
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println(list.size());

			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());
			jsonMap.put("total", groupNewService.findCountByCondition(params));
			// 需要实现数据的总记录数
			jsonMap.put("rows", list);
			jsonObject = JSONObject.fromObject(jsonMap);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String findByInvitecode(){
		//i = this.groupService.findByInvitecode(invitecode_self); RCJ
		return "findByInvitecode";
	}

	public String showGroupUI(){

		ActionContext.getContext().put("id", id);
		return "showGroupUI";
	}
	
	
	public String showGroup(){

		try {
			Map<String,Object> map = groupNewService.findGroupInfo(Long.parseLong(id));	
			System.out.println(map);
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "show";
	}

	/**
	 * 查找数据角色所对应的目录
	 * @return
	 */
	public String findRoleGroup(){
		listGroup = groupService.findRoleCheckedGroup(roleId);
		return "findRoleGroup";
	}
	
	/**
	 * 修改数据角色所对应的目录
	 * @return
	 */
	public String updateRoleGroup(){
		this.groupService.updateRoleGroup(roleId, groupIds);
		this.setId(id);
		this.setRoleId(roleId);
		this.setState(1);
		return "updateRoleGroup";
	}
	
	public String findGroupInfo(){

		try {
			Map<String,Object> map = groupNewService.findGroupInfo(Long.parseLong(id));	
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}
	
	
	public String  updateGroupInfoUI(){
		ActionContext.getContext().put("id", id);
		
		return"updateGroupInfoUI";
	}
	
	public String  updateGroupInfo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.parseLong(id));
		params.put("groupid", groupid);
		params.put("group_name", group_name);
		params.put("linkname", linkname);
		params.put("linkphone", linkphone);
		params.put("group_property", group_property);
		params.put("contract_no", contract_no);
		params.put("fix_phone", fix_phone);
		params.put("email", email);
		params.put("fax", fax);
		params.put("group_address", group_address);
		params.put("invitecode_self", invitecode_self);
		params.put("remark", remark);
		if ("0".equals(radioSelect)) {
			params.put("active_as_driver_self", Integer.parseInt(radioSelect));
			params.put("integer_as_driver_self", new BigDecimal(radioSelectInput));
			params.put("ratio_as_driver_self", new BigDecimal(radioSelectInput));
		}else {
			params.put("active_as_driver_self", Integer.parseInt(radioSelect));
			params.put("ratio_as_driver_self", new BigDecimal(radioSelectInput));
			params.put("integer_as_driver_self", new BigDecimal(radioSelectInput));
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			try {
				params.put("sign_time",sdf.parse(sign_time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Map<String,Object> resultObj = new HashMap<String,Object>();
			int returnCode= 0;
			try {
				//int j= 2/0;
				returnCode = groupNewService.updateGroupInfo(params);
				resultObj.put("returnCode",returnCode);
				resultObj.put("msg", "更新成功！");
				//ActionContext.getContext().put("id", id);
			} catch (Exception e) {
				resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
				e.printStackTrace();
			}finally{
				rObj=resultObj;
			}
		
		return  "updateGroupInfo";
	}
	
	public String newGroupUI(){
		return "newGroupUI";
	}
	
	public String getGproperty(){
		int mid = this.getGroupProperty()+1;
		System.out.println("mid:"+mid);
		String result = "";
		if(mid>=0 && mid<10){
			result ="G1000"+mid;
		}else if(mid>=10 && mid<100){
			result = "G100"+mid;
		}else if(mid>=100 && mid<1000){
			result = "G10"+mid;
		}else if(mid>=1000 && mid<10000){
			result = "G0"+mid;
		}else{
			result = "G"+mid;
		}
		robj = result;
		return "getGproperty";
	}
	
	public synchronized int getGroupProperty(){
		return groupService.findMaxId();
	}
	
	public String saveGroup(){
		
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> tsparams = new HashMap<String, Object>();
		Map<String, Object> upgroupparams = new HashMap<String, Object>();
		//params.put("id", Long.parseLong(id));
		
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		int mid = this.getGroupProperty()+1;
		String result = "";
		if(mid>=0 && mid<10){
			result ="G1000"+mid;
		}else if(mid>=10 && mid<100){
			result = "G100"+mid;
		}else if(mid>=100 && mid<1000){
			result = "G10"+mid;
		}else if(mid>=1000 && mid<10000){
			result = "G0"+mid;
		}else{
			result = "G"+mid;
		}
		params.put("create_date", new Date());
		params.put("groupid", result);
		params.put("group_name", group_name);
		params.put("linkname", linkname);
		params.put("linkphone", linkphone);
		params.put("group_property", group_property);
		params.put("contract_no", contract_no);
		params.put("fix_phone", fix_phone);
		params.put("email", email);
		params.put("fax", fax);
		params.put("group_address", group_address);
		params.put("invitecode_self", invitecode_self);
		params.put("remark", remark);
		
		
		if ("0".equals(radioSelect)) {
			params.put("active_as_driver_self", Integer.parseInt(radioSelect));
			params.put("integer_as_driver_self", new BigDecimal(radioSelectInput));
			params.put("ratio_as_driver_self", new BigDecimal(radioSelectInput));
		}else {
			params.put("active_as_driver_self", Integer.parseInt(radioSelect));
			params.put("ratio_as_driver_self", new BigDecimal(radioSelectInput));
			params.put("integer_as_driver_self", new BigDecimal(radioSelectInput));
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		try {
			params.put("sign_time",sdf.parse(sign_time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//groupNewService.saveGroup(params);
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			//int j= 2/0;
			int isGroupId = groupNewService.findIsGroupid(groupid);
			if(isGroupId==0){
				returnCode = groupNewService.saveGroup(params);
				long id = (long) params.get("id");
				//System.out.println("new group_id:"+id);
				resultObj.put("returnCode",returnCode);
				tsparams.put("groupid",id);
				tsparams.put("remark","注册");
				tsparams.put("account_type", 2);
				tsparams.put("ts_type","tx_code_001");
				tsparams.put("balance",0);
				groupNewService.modifytsbal(tsparams);
				long tsid = (long) tsparams.get("id");
				upgroupparams.put("tsid",tsid);
				upgroupparams.put("id", id);
				groupNewService.seGrouptsid(upgroupparams);
				resultObj.put("msg", "更新成功！");
			}else{
				resultObj.put("returnCode",0);
				resultObj.put("msg", "集团标识重复！");
			}
			//ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		return "saveGroup";
	}
	
	
	//-----------人员调整增加的方法20140821-------------
	/**
	 * 进入到人员调整的页面并将需要的参数传递过去（比如集团的id主键等）
	 */
	public String  personnelAdjustmentUI(){
		try {
			Map<String,Object> map = groupNewService.findGroupInfo(Long.parseLong(id));	
			 groupid = (String) map.get("groupid");
			 group_name = (String) map.get("group_name");
			ActionContext.getContext().put(groupid, groupid);
			ActionContext.getContext().put(group_name, group_name);
			ActionContext.getContext().put("gid", id);
/*			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("gid", id);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "personnelAdjustmentUI";
	}
	

	
	/**
	 * 根据条件获取指定集团旗下的车主用户
	 * @return
	 */
	public String findGroupDriversByCondition() {
/*		HttpServletRequest request = ServletActionContext.getRequest();
		String gid=request.getParameter("gid");
		System.out.println("gid is :"+gid);*/
		//System.out.println("conditionSelect:"+conditionSelect+"       dadd  gid:"+gid);
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
			if (gid!=null) {
				params.put("gid",Long.parseLong(gid));
			}
			// 查询结果从第0条开始，查询10条记录
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);

			list = groupNewService.findGroupDriversByCondition(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println(list.size());

			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());
			jsonMap.put("total", groupNewService.findGroupDriversCountByCondition(params));
			// 需要实现数据的总记录数
			jsonMap.put("rows", list);
			jsonObject = JSONObject.fromObject(jsonMap);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String  personnelAdjustment(){
		
		
		
		return "personnelAdjustment";
	}
	
	/**跳转到添加车主的页面*/
	public String addDriverUI(){
		
		
		return "addDriverUI";
	}
	
	/**
	 * 将选中的车主添加到集团中
	 * @return
	 */
	public String addDriverToGroup(){
		Map<String, Object> params = new HashMap<String, Object>();
		
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			params.put("id", id);
			params.put("gid", gid);
			//int j= 2/0;
			returnCode = groupNewService.addDriverToGroup(params);
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "更新成功！");
			//ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		return "addDriverToGroup";	
	}
	
	public String addBatchDriver(){
		
		return "addBatchDriver";
	}
	
	/**
	 * 根据条件获取非指定集团旗下的且经过车主验证的车主用户
	 * @return
	 */
	public String diverList(){
		
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
			if (verifiedStaus!=null) {
				params.put("verifiedStaus", verifiedStaus);				
			}
			// 查询结果从第0条开始，查询10条记录
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);

			list = groupNewService.findUsersByCondition(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println(list.size());

			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());
			jsonMap.put("total", groupNewService.findUsersCountByCondition(params));
			// 需要实现数据的总记录数
			jsonMap.put("rows", list);
			jsonObject = JSONObject.fromObject(jsonMap);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 移除集团下指定的车主
	 * @return
	 */
	public String removeDriverFromGroup(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", Long.parseLong(id));
		params.put("gid", gid);
		//groupNewService.removeDriverFromGroup(params);
		
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			//int j= 2/0;
			returnCode = groupNewService.removeDriverFromGroup(params);
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "删除成功！");
			//ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "删除失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		ActionContext.getContext().put("gid", gid);
		return "removeDriverFromGroup";
	}

	public String  showGroupUserUI(){
		ActionContext.getContext().put("id", id);
		
		return "showGroupUserUI";
	}
	
	public String  showGroupUserInfo(){
		
		try {
			Map<String,Object> map = userNewService.findUserAllInfo(Long.parseLong(id));	
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	//--------删除集团客户--------------
	public String  delGroup(){
		
		//groupNewService.delGroup(Long.parseLong(id));
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			//int j= 2/0;
			returnCode = groupNewService.delGroup(Long.parseLong(id));
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "删除成功！");
			//ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		return "delGroup";
	}
	
	
	//-------------返利信息------------------
	/**
	 * 打开返利信息页面
	 * @return
	 */
	public String rebateInfoUI(){

		ActionContext.getContext().put("id", id);//集团id
		return "rebateInfoUI";
	}
	
	/**
	 * 将集团标识、集团名称等相关信息传递到前端页面
	 * @return
	 */
/*	public String getGroupRebateInfo(){

		Map<String, Object> rebateInfoMap = groupNewService.getGroupInfo(Long.parseLong(id));
		jsonObject=JSONObject.fromObject(rebateInfoMap);
		System.out.println(jsonObject);
		return SUCCESS;
	}*/
	
	
	/**
	 * 获得所有返利列表
	 * @return
	 */
	public  String findGroupAllRebateListInfo(){
		List<Map<String, Object>> list = null;
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("id", Long.parseLong(id));
		params.put("id", id);
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		list = groupNewService.findGroupAllRebateListInfo(params);
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		// 需要显示数据的总页数
		 //jsonMap.put("total", list.size());
		jsonMap.put("total", groupNewService.countGroupAllRebateListInfo(params));
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
	public String findGroupRebateListInfo(){
		List<Map<String, Object>> list = groupNewService.findGroupRebateListInfo(Long.parseLong(id));
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
	
	
	public String count(){
		int count = groupNewService.countMoneyAll(groupid);
		String num = groupNewService.countMoneyNumber(groupid);
		Map<String,Object> map = new HashMap<>();
		map.put("count",count );
		map.put("num", num);
		jsonObject = JSONObject.fromObject(map);
		return SUCCESS;
	}
	
	//-------------更改分成比例---------------------
	public String editGroupReallocateProfit(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", Long.parseLong(userid));
		if ("0".equals(rpRadioSelect)) {
			params.put("activeway_as_driver", Integer.parseInt(rpRadioSelect));
			params.put("ratio_as_driver", new BigDecimal(rpRadioSelectInput));
		}else {
			params.put("activeway_as_driver", Integer.parseInt(rpRadioSelect));
			params.put("integer_as_driver", new BigDecimal(rpRadioSelectInput));
		}
		
		
		//groupNewService.initGroupReallocateProfit(params);
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			//int j= 2/0;
			returnCode = groupNewService.initGroupReallocateProfit(params);
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "更新成功！");
			//ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		return "editGroupReallocateProfit";
	}
	public String editGroupReallocateProfitDefault(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", Long.parseLong(userid));
		params.put("gid", id);
		//groupNewService.initGroupReallocateProfitDefault(params);
		
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			//int j= 2/0;
			returnCode = groupNewService.initGroupReallocateProfitDefault(params);
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "更新成功！");
			//ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		return "editGroupReallocateProfit";
	}	
	
	//------getter/setter----------------------
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
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

	public String getInvitecode_self() {
		return invitecode_self;
	}

	public void setInvitecode_self(String invitecode_self) {
		this.invitecode_self = invitecode_self;
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



	public String getActive_as_driver_self() {
		return active_as_driver_self;
	}

	public void setActive_as_driver_self(String active_as_driver_self) {
		this.active_as_driver_self = active_as_driver_self;
	}

	public String getRadioSelectInput() {
		return radioSelectInput;
	}

	public void setRadioSelectInput(String radioSelectInput) {
		this.radioSelectInput = radioSelectInput;
	}

	public String getRadioSelect() {
		return radioSelect;
	}

	public void setRadioSelect(String radioSelect) {
		this.radioSelect = radioSelect;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getRpRadioSelectInput() {
		return rpRadioSelectInput;
	}

	public void setRpRadioSelectInput(String rpRadioSelectInput) {
		this.rpRadioSelectInput = rpRadioSelectInput;
	}

	public String getRpRadioSelect() {
		return rpRadioSelect;
	}

	public void setRpRadioSelect(String rpRadioSelect) {
		this.rpRadioSelect = rpRadioSelect;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public List<Group> getListGroup() {
		return listGroup;
	}

	public void setListGroup(List<Group> listGroup) {
		this.listGroup = listGroup;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public Object getrObj() {
		return rObj;
	}

	public void setrObj(Object rObj) {
		this.rObj = rObj;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getgProperty() {
		return gProperty;
	}

	public void setgProperty(String gProperty) {
		this.gProperty = gProperty;
	}

	public Object getRobj() {
		return robj;
	}

	public void setRobj(Object robj) {
		this.robj = robj;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}
}
