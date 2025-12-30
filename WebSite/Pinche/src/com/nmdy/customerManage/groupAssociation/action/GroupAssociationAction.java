package com.nmdy.customerManage.groupAssociation.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.nmdy.customerManage.group.dao.GroupNew;
import com.nmdy.customerManage.groupAssociation.dao.GroupAssociation;
import com.nmdy.customerManage.groupAssociation.service.GroupAssociationService;
import com.nmdy.customerManage.user.service.UserNewService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/*@Controller
@Scope("prototype")*/
public class GroupAssociationAction extends ActionSupport{

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
	private String ga_code;
	private String ga_name;
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
	private String desc_;
	private String ratio_as_driver_self;
	private String integer_as_driver_self;
	private String active_as_drivr_self;
	private String radioSelectInput;//分成比例输入框
	private String radioSelect;//分成比例下拉框

	private String groupid;//集团客户id
	
	
	//@Resource
	private GroupAssociationService groupAssociationService;
	
	//@Resource
	private UserNewService userService;


	
	
	
	
	public GroupAssociationService getGroupAssociationService() {
		return groupAssociationService;
	}

	public void setGroupAssociationService(
			GroupAssociationService groupAssociationService) {
		this.groupAssociationService = groupAssociationService;
	}

	public UserNewService getUserService() {
		return userService;
	}

	public void setUserService(UserNewService userService) {
		this.userService = userService;
	}

	public String groupAssociationList()  throws Exception {
		return "groupAssociationList";
	}
	
	public String groupAssociationPageList() {
		try {
			List<Map<String, Object>> list = null;
			Map<String, Object> params = new HashMap<String, Object>();
			
			if ("ga_code".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "ga_code");
				params.put("conditionInput", conditionInput);
			}
			if ("ga_name".equals(conditionSelect)&& conditionInput.length() > 0) {
				params.put("conditionSelect", "ga_name");
				params.put("conditionInput", conditionInput);
			}

			
			// 查询结果从第0条开始，查询10条记录
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);

			list = groupAssociationService.findGroupAssociationsByCondition(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println(list.size());

			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());
			jsonMap.put("total", groupAssociationService.findCountByCondition(params));
			// 需要实现数据的总记录数
			jsonMap.put("rows", list);
			jsonObject = JSONObject.fromObject(jsonMap);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String showGroupAssociationUI(){

		ActionContext.getContext().put("id", id);
		return "showGroupAssociationUI";
	}
	
	
	public String showGroup(){

		try {
			Map<String,Object> map = groupAssociationService.findGroupAssociationInfo(Long.parseLong(id));	
			System.out.println(map);
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "show";
	}


	public String findGroupAssociationInfo(){

		try {
			Map<String,Object> map = groupAssociationService.findGroupAssociationInfo(Long.parseLong(id));	
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}
	
	
	public String  updateGroupAssociationInfoUI(){
		ActionContext.getContext().put("id", id);
		
		return"updateGroupAssociationInfoUI";
	}
	
	public String  updateGroupAssociationInfo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.parseLong(id));
		params.put("ga_code", ga_code);
		params.put("ga_name", ga_name);
		params.put("linkname", linkname);
		params.put("linkphone", linkphone);
		params.put("group_property", group_property);
		params.put("contract_no", contract_no);
		params.put("fix_phone", fix_phone);
		params.put("email", email);
		params.put("fax", fax);
		params.put("group_address", group_address);
		params.put("desc_", desc_);
		
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			try {
				params.put("sign_time",sdf.parse(sign_time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			groupAssociationService.updateGroupAssociationInfo(params);
		
		return  "updateGroupAssociationInfo";
	}
	
	public String newGroupAssociationUI(){
		
		
		return "newGroupAssociationUI";
	}
	
	public String saveGroupAssociation(){
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		//params.put("id", Long.parseLong(id));
		params.put("ga_code", ga_code);
		params.put("ga_name", ga_name);
		params.put("linkname", linkname);
		params.put("linkphone", linkphone);
		params.put("group_property", group_property);
		params.put("contract_no", contract_no);
		params.put("fix_phone", fix_phone);
		params.put("email", email);
		params.put("fax", fax);
		params.put("group_address", group_address);
		params.put("invitecode_self", invitecode_self);
		params.put("desc_", desc_);
		

		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		try {
			params.put("sign_time",sdf.parse(sign_time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		groupAssociationService.saveGroupAssociation(params);
		
		
		return "saveGroupAssociation";
	}
	
	
	//-----成员调整增加的方法20140822----------
	
	/**
	 * 进入添加集团联盟客户的页面并将需要的参数传递过去（比如集团联盟的id主键等）
	 * @return
	 */
	public String addMemberUI(){
		Map<String, Object> map = groupAssociationService.findGroupAssociationInfo(Long.parseLong(id));
		
		 ga_code = (String) map.get("ga_code");
		 ga_name = (String) map.get("ga_name");
		ActionContext.getContext().put(ga_code, ga_code);
		ActionContext.getContext().put(ga_name, ga_name);
		ActionContext.getContext().put("id", id);
		
		
		return "addMemberUI";
	}
	
	/**
	 * 获得非指定集团联盟旗下的集团客户
	 * @return
	 */
	public String  findGroupsByCondition(){
		
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
		if (id!=null) {
			params.put("id", id);
		}
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		list = groupAssociationService.findGroupsByCondition(params);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		System.out.println(list.size());
		// 需要显示数据的总页数
		 //jsonMap.put("total", list.size());
		jsonMap.put("total", groupAssociationService.findGroupsCountByCondition(params));
		// 需要实现数据的总记录数
		jsonMap.put("rows", list);
		jsonObject = JSONObject.fromObject(jsonMap);
		System.out.println(jsonObject);
		
		return SUCCESS;
	}
	
	/**
	 * 将指定集团客户添加到集团联盟中
	 * @return
	 */
	public String addMemberToGroupAssociation(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);//集团联盟客户id
		params.put("groupid", groupid);//集团客户id
		
		groupAssociationService.addMemberToGroupAssociation(params);
		
		return "addMemberUI";
	}
	
	/**
	 * 从集团联盟中移除指定集团客户
	 * @return
	 */
	public String removeMember(){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.parseLong(id));
		params.put("groupid", groupid);
		groupAssociationService.removeMember(params);
		ActionContext.getContext().put("groupid", groupid);
		
		return"addMemberUI";
	}
	
	public String getGroupsInGA(){
		
		List<Map<String, Object>> list = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", Long.parseLong(id));
		
		
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		list = groupAssociationService.findGroupsInGA(params);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		System.out.println(list.size());
		// 需要显示数据的总页数
		 //jsonMap.put("total", list.size());
		jsonMap.put("total", groupAssociationService.findGroupsCountInGA(Long.parseLong(id)));
		// 需要实现数据的总记录数
		jsonMap.put("rows", list);
		jsonObject = JSONObject.fromObject(jsonMap);
		System.out.println(jsonObject);
		return SUCCESS;
	}
	
	public String  showUserUI(){
		ActionContext.getContext().put("id", id);
		
		return "showUserUI";
	}
	
	public String  showUserInfo(){
		
		try {
			Map<String,Object> map = userService.findUserAllInfo(Long.parseLong(id));	
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	//---------集团联盟调整----------------
	
	public String delGroupAssociation(){
		
		groupAssociationService.delGroupAssociation(Long.parseLong(id));
		
		return "delGroupAssociation";
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



	public String getGa_code() {
		return ga_code;
	}

	public void setGa_code(String ga_code) {
		this.ga_code = ga_code;
	}

	public String getGa_name() {
		return ga_name;
	}

	public void setGa_name(String ga_name) {
		this.ga_name = ga_name;
	}

	public String getDesc_() {
		return desc_;
	}

	public void setDesc_(String desc_) {
		this.desc_ = desc_;
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

	public String getActive_as_drivr_self() {
		return active_as_drivr_self;
	}

	public void setActive_as_drivr_self(String active_as_drivr_self) {
		this.active_as_drivr_self = active_as_drivr_self;
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

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}


	
	
}
