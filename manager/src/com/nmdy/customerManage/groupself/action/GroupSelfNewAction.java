package com.nmdy.customerManage.groupself.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.nmdy.customerManage.group.dao.GroupNew;
import com.nmdy.customerManage.groupself.service.GroupSelfNewService;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.customerManage.user.service.UserNewService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*@SuppressWarnings("serial")
@Controller
@Scope("prototype")*/
public class GroupSelfNewAction extends ActionSupport{

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
	private JSONObject resultObject;
	
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
	private String ratio_as_driver_self;  //作为车主返利信息，指的是用户与用户之间
	private String integer_as_driver_self;//作为车主返利信息，指的是用户与用户之间
	private String active_as_driver_self;//作为车主返利信息，指的是用户与用户之间
	private String radioSelectInput;//分成比例输入框
	private String radioSelect;//分成比例下拉框
	
	
	private String activeway_as_driver ; //作为车主返利信息，指的是用户与集团之间
	private String integer_as_driver   ; //作为车主返利信息，指的是用户与集团之间
	private String ratio_as_driver     ; //作为车主返利信息，指的是用户与集团之间 
	
	
	
	private String rpRadioSelectInput;//更改分成比例的输入框
	private String rpRadioSelect;//更改分成比例的下拉框
	private String userid;//更改分成比例时所传的userid
	
	
	//@Resource
	private GroupSelfNewService groupSelfNewService;
	//@Resource
	private UserNewService userNewService;

	
	
	

	public GroupSelfNewService getGroupSelfNewService() {
		return groupSelfNewService;
	}

	public void setGroupSelfNewService(GroupSelfNewService groupSelfNewService) {
		this.groupSelfNewService = groupSelfNewService;
	}

	public UserNewService getUserNewService() {
		return userNewService;
	}

	public void setUserNewService(UserNewService userNewService) {
		this.userNewService = userNewService;
	}

	public String groupSelfInfoUI()  throws Exception {
		ActionContext.getContext().put("groupid", "1");
		return "groupSelfInfoUI";
	}
	
/*	public String groupSelfList() {
		try {
			List<GroupNew> list = null;
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
		
			// 查询结果从第0条开始，查询10条记录
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);

			list = groupSelfNewService.findGroupsByCondition(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println(list.size());

			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());
			jsonMap.put("total", groupSelfNewService.getCountByCondition(params));
			// 需要实现数据的总记录数
			jsonMap.put("rows", list);
			jsonObject = JSONObject.fromObject(jsonMap);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
*/

	
	



	public String findGroupSelfInfo(){

		try {
			Map<String,Object> map = groupSelfNewService.findGroupInfo(Long.parseLong("1"));	
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}
	
	public String  updateGroupSelfInfo(){
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
		//params.put("active_as_driver_self", active_as_driver_self);
		//params.put("remark", remark);

		if ("0".equals(radioSelect)) {
			params.put("active_as_driver_self", Integer.parseInt(radioSelect));
			params.put("integer_as_driver_self", new BigDecimal(radioSelectInput));
		}else {
			params.put("active_as_driver_self", Integer.parseInt(radioSelect));
			params.put("ratio_as_driver_self", new BigDecimal(radioSelectInput));
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			try {
				params.put("sign_time",sdf.parse(sign_time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			groupSelfNewService.updateGroupInfo(params);
		return  "updateGroupSelfInfo";
	}
	
	
	
	public String findGroupUserList(){
		try {
			List<Map<String,Object>> list = null;
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
			
			params.put("id", Long.parseLong(id));
			// 查询结果从第0条开始，查询10条记录
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);

			list = groupSelfNewService.findUsersByCondition(params);
/*			//将用户的分成比例信息由三个字段（一个方式、两个数值组成）转化为一个字段（5%或者5点）
 * 			//不推荐这么使用，可以在数据库中拼好再以一个别名查出来，在USER中加一个别名相同的属性
 * 			//sql语句：CASE WHEN activeway_as_driver=0 THEN CONCAT(ratio_as_driver,'%') ELSE CONCAT(integer_as_driver,'点')   END  rp
			for (User user : list) {
				if (user.getActiveway_as_driver()==0) {//如果分成方式为百分比
					int rp = user.getRatio_as_driver().intValue();//获得百分比参数
					user.setReallocateProfit(rp+"%");//将百分比参数转化成类似5%的字符串传到前台
				}else {
					int rp1 =user.getInteger_as_driver().intValue();
					user.setReallocateProfit(rp1+"点");
				}
			}*/
			
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println(list.size());

			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());
			jsonMap.put("total", groupSelfNewService.findUserCountByCondition(params));
			// 需要实现数据的总记录数
			jsonMap.put("rows", list);
			jsonObject = JSONObject.fromObject(jsonMap);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}

	public String showGroupSelfUserInfoUI(){
		ActionContext.getContext().put("id", id);
		
		return "showGroupSelfUserInfoUI";
		
		
	}
	
	public  String findGroupSelfUserInfo(){
		
		try {
			Map<String,Object> map = userNewService.findUserAllInfo(Long.parseLong(id));	
			jsonObject=JSONObject.fromObject(map);
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	public String removeUserFromGroup(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", Long.parseLong(id));
		params.put("groupid", Long.parseLong(groupid));
		groupSelfNewService.removeUserFromGroup(params);//将用户从集团-用户关系表中移除
		groupSelfNewService.initInvitecodeRegistNull(Long.parseLong(id));//将用户的注册邀请码置为空，不给其他人返利，直至有新的集团邀请他加入
		//ActionContext.getContext().put("gid", gid)	
		

		return "removeUserFromGroup";
	}
	
	public String editReallocateProfit(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", Long.parseLong(userid));
		if ("0".equals(rpRadioSelect)) {
			params.put("activeway_as_driver", Integer.parseInt(rpRadioSelect));
			params.put("ratio_as_driver", new BigDecimal(rpRadioSelectInput));
		}else {
			params.put("activeway_as_driver", Integer.parseInt(rpRadioSelect));
			params.put("integer_as_driver", new BigDecimal(rpRadioSelectInput));
		}
		
		
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			//int j= 2/0;
			
			returnCode = groupSelfNewService.initReallocateProfit(params);
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "更新成功！");
			ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		
		
		
		return "editReallocateProfit";
	}
	public String editReallocateProfitDefault(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", Long.parseLong(userid));
		params.put("groupid", id);
		
		Map<String,Object> resultObj = new HashMap<String,Object>();
		int returnCode= 0;
		try {
			//int j= 2/0;
			
			returnCode = groupSelfNewService.initReallocateProfitDefault(params);
			resultObj.put("returnCode",returnCode);
			resultObj.put("msg", "更新成功！");
			ActionContext.getContext().put("id", id);
		} catch (Exception e) {
			resultObj.put("msg", "更新失败"+"   异常信息 ： "+e.getMessage());
			e.printStackTrace();
		}finally{
			resultObject=JSONObject.fromObject(resultObj);
		}
		
		
		
		
		return "editReallocateProfitDefault";
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

	public String getActiveway_as_driver() {
		return activeway_as_driver;
	}

	public void setActiveway_as_driver(String activeway_as_driver) {
		this.activeway_as_driver = activeway_as_driver;
	}

	public String getInteger_as_driver() {
		return integer_as_driver;
	}

	public void setInteger_as_driver(String integer_as_driver) {
		this.integer_as_driver = integer_as_driver;
	}

	public String getRatio_as_driver() {
		return ratio_as_driver;
	}

	public void setRatio_as_driver(String ratio_as_driver) {
		this.ratio_as_driver = ratio_as_driver;
	}


	
	
}
