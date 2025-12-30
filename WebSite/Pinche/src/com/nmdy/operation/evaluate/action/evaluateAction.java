 package com.nmdy.operation.evaluate.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.nmdy.operation.evaluate.dao.EvaluateEntity;
import com.nmdy.operation.evaluate.dao.HelpEntity;
import com.nmdy.operation.evaluate.service.EvaluateService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class evaluateAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	/**
	 * input
	 */
	private int page;
	private int rows;
	private JSONObject jsonObject;// 返回的json
	private int userid;
	private String city_register = "";// 注册城市
	private int  level;// 评价
	private int  level1;// 评价
	private int  level2;// 评价
	private int  level3;// 评价
	private String city_cur = "";// 最后登录的城市
	private String ga_code = "";// 集团联盟标识
	private String ga_name = "";// 集团联盟名
	private String username = "";
	private String phone = "";
	private String usertype;
	private String cityselect = "";// 注册或最后登录城市查询
	private String cityinput = "";// 输入的值 根据cityselect的值来判断map存入哪个字段值、
	private String groupselect = "";// 集团 或者联盟标识
	private String groupinput = "";// 根据groupselect 的值来判断存入map的是哪个字段值
	private String yewuselect = "";// 按业务查询 根据返回的数字字符串来判断 是哪种业务
	private String userselect = "";// 根据用户信息查询
	private String userinput = "";// 根据userselect返回的值来判断存入map的是哪个字段值
	private String khsfselect = "";// 按客户身份查询 根据返回的数字字符串来判断客户的身份
	private int order_cs_id;//订单编号
	private String msg;
	private int driver_verified;
	private int deleted;
	private int totle;//暂时没有用
	private int eid;//评价id 查询评价详情用
	/**
	 * output
	 */

	private EvaluateService evaluateService;// 实现数据操作

	
	/**
	 * 详情
	 */

	public String action2() {
		//System.out.println("action2 开始");
		jsonObject = JSONObject.fromObject(evaluateService.findbyid(userid));
		//System.out.println("test2的 jsonobjec" + jsonObject);
		return SUCCESS;
	}

	public String go() {
		//System.out.println("go has been here...");
		return SUCCESS;
	}

	
	/**
	 * 跳转到详情
	 */
	public String go1() {
		ActionContext.getContext().put("userid", userid);
		//System.out.println("go1 has been here...");
		return SUCCESS;
	}

	public String go2() {
		//System.out.println("go2 has been here...");
		return SUCCESS;
	}

	
	/**
	 * 根据条件查询
	 */
	public String find() {
		//System.out.println("find has been here...");
		HttpServletRequest request = ServletActionContext.getRequest();
		List<EvaluateEntity> evaluate;
		List<EvaluateEntity> evaluate1 = new ArrayList<EvaluateEntity>();
		HelpEntity evaluatehelp;
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			// 返回的实体类存放处
			//System.out.println("find has been here...1");
			// params 需要提交表单之后才能获得
			Map<String, Object> params = new HashMap<String, Object>();
		
		/**
		 * 根据页面下拉选择的来判断输入的是哪个字段
		 */
			if ("1".equals(cityselect)) {
				//System.out.println(cityselect);
				params.put("city_register", cityinput);
				//System.out.println(cityinput);
			}
			if ("2".equals(cityselect)) {
				params.put("city_cur", cityinput);
			}
			if ("3".equals(cityselect)) {
				// 选择其中的一种
				params.put("city_cur1", cityinput);
			}
			// if (groupselect.equals("0")) {
			// groupinput=null;
			// }
			/**
			 * 根据页面下拉选择的来判断输入的是哪个字段
			 */
			if ("1".equals(groupselect)) {
				//System.out.println(groupselect);
				params.put("ga_code", groupinput);
				//System.out.println(groupinput);
			}
			if ("2".equals(groupselect)) {
				params.put("ga_name", groupinput);

			}
			/**
			 * 根据页面下拉选择的来判断输入的是哪个字段
			 */

			if ("1".equals(userselect)) {
				//System.out.println(userselect);
				params.put("userid", userinput);
				//System.out.println(userinput);
			}
			if ("2".equals(userselect)) {
				//System.out.println(userselect);
				params.put("phone", userinput);
				//System.out.println(userinput);
			}
			if ("3".equals(userselect)) {
				//System.out.println(userselect);
				params.put("username", userinput);
				//System.out.println(userinput);

			}
			/**
			 * 根据页面下拉选择的来判断输入的是哪个字段
			 */
			if ("1".equals(khsfselect)) {
				params.put("driver_verified",1);
			}
			if ("0".equals(khsfselect)) {
				params.put("driver_verified","0");
			}
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			//System.out.println(params);// 输出map里的内容
			evaluate = evaluateService.find(params);
			/**
			 * 根据得到的userid查询出没一个客户的好评率相关信息,然后用一个新的实体类实例来存
			 */
	     for(EvaluateEntity evaluateinfo :evaluate){
	    	 int id = evaluateinfo.getUserid();
	    	evaluatehelp = evaluateService.findbyid1(id);
	    	 String czhpString ;
	    	 String ckhpString ;
	    	 int czdd = evaluatehelp.getCzlevelt1();
	    	 int ckdd = evaluatehelp.getCklevelt1();
	    	 
	    	 if(evaluatehelp.getCzpl1()==null){
	    		 czhpString="00.00%";
	    	 }
	    	 else {
	    		 czhpString=evaluatehelp.getCzpl1();
			}
	    	 if(evaluatehelp.getCkpl1()==null){
	    		 ckhpString="00.00%";
	    		 
	    	 }
	    	 else {
	    		 ckhpString = evaluatehelp.getCkpl1();
			}
	    	 evaluateinfo.setCzlevelt1(czdd);
	    	 evaluateinfo.setCklevelt1(ckdd);
	    	evaluateinfo.setCkpl1(ckhpString);
	    	evaluateinfo.setCzpl1(czhpString);
	    	evaluate1.add(evaluateinfo);		 
				
			}
			
			jsonMap.put("total", evaluateService.getCount(params));
			//System.out.println(jsonMap);
			jsonMap.put("rows", evaluate1);
			
			//System.out.println(jsonObject);
			//System.out.println("find has been here...tryend");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jsonObject = JSONObject.fromObject(jsonMap);
		}
		//System.out.println("find has been here...success");
		return SUCCESS;
	}

	/*
	 * 详情页面查询
	 */
	public String action3() {

		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {
			List<EvaluateEntity> evaluate;// 返回的实体类存放处
			// params 需要提交表单之后才能获得
			Map<String, Object> params = new HashMap<String, Object>();
			/**
			 * 根据页面下拉选择的来判断输入的是哪个字段
			 */
			if (userid != 0) {
				params.put("userid", userid);
			}
			if ("1".equals(khsfselect)) {
				params.put("usertype", "2");
			}
			if ("2".equals(khsfselect)) {
				params.put("usertype", "1");
			}
			if(1==level1){
				params.put("level1",1);
				
			}else{
				params.put("level1",5);
			}
			if(1==level2){
				params.put("level2",2);
				
			}else{
				params.put("level2",6);
				
			}
			if(1==level3){
				params.put("level3",3);
			}else{
				params.put("level3",7);
				
			}
			System.out.println(deleted);
			if(0==deleted){
				params.put("deleted",0);
				
			}
			if(1==deleted){
				params.put("deleted",1);
			}
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			//System.out.println(params);// 输出map里的内容
			evaluate = evaluateService.findbyid2(params);
		
			jsonMap.put("total", evaluateService.getCount2(params));
			//System.out.println(jsonMap);
			jsonMap.put("rows", evaluate);
			
			//System.out.println(jsonObject);
			//System.out.println("action3 has been here...tryend");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jsonObject = JSONObject.fromObject(jsonMap);
		}
		//System.out.println("action3 has been here...success");
		return SUCCESS;
	}
	/*
	 * 修改评价
	 */
    public String action4(){
    	
    	HttpServletRequest request = ServletActionContext.getRequest();
    	Map<String, Object> params = new HashMap<String, Object>();
		try {
			List<EvaluateEntity> evaluate;// 返回的实体类存放处
			// params 需要提交表单之后才能获得
			/**
			 * 根据单选选择的来判断输入的是哪个字段
			 */
			
			if ( -1!=order_cs_id) {
				params.put("order_cs_id",order_cs_id);
			}
			if(1==level){
				params.put("level",1);
				
			}
			if(2==level){
				params.put("level",2);
				
			}
			if(3==level){
				params.put("level",3);
			}
			/**
			 * 输入的详细评价
			 */
		
			if(null!=msg){
				params.put("msg",msg);
			}
			//System.out.println(params);// 输出map里的内容
			 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			evaluateService.update(params);
		}
		//System.out.println("action4 has been here...success");
		return SUCCESS;
    }
    /**
	 * 屏蔽
	 */
    public String updatedel(){
    	
    	HttpServletRequest request = ServletActionContext.getRequest();
    	Map<String, Object> params = new HashMap<String, Object>();

		try {
			// params 需要提交表单之后才能获得
			
			if ( -1!=order_cs_id) {
				params.put("order_cs_id",order_cs_id);
				params.put("deleted",deleted);
			}				
			//System.out.println(params);// 输出map里的内容
			 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			evaluateService.updatedel(params);
		}
		//System.out.println("action4 has been here...success");
		return SUCCESS;
    }
  public String findmsg(){
	  Map<String, Object> params = new HashMap<String, Object>();
	  Map<String, Object> msg = new HashMap<String, Object>();
    	if(eid>0){
    		params.put("eid",eid);
    	}
    	msg=evaluateService.findmsg(params);
    	jsonObject = JSONObject.fromObject(msg);

		return SUCCESS;
    }
    
	// ------------------get set---------------------
    

	public String getCity_register() {
		return city_register;
	}


	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public int getDriver_verified() {
		return driver_verified;
	}

	public void setDriver_verified(int driver_verified) {
		this.driver_verified = driver_verified;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getOrder_cs_id() {
		return order_cs_id;
	}

	public void setOrder_cs_id(int order_cs_id) {
		this.order_cs_id = order_cs_id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public void setCity_register(String city_register) {
		this.city_register = city_register;
	}

	public String getCity_cur() {
		return city_cur;
	}

	public void setCity_cur(String city_cur) {
		this.city_cur = city_cur;
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

	public EvaluateService getEvaluateService() {
		return evaluateService;
	}

	public void setEvaluateService(EvaluateService evaluateService) {
		this.evaluateService = evaluateService;
	}

	// public int getId() {
	// return id;
	// }
	// public void setId(int id) {
	// this.id = id;
	// }

	public String getPhone() {
		return phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
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

	public String getCityselect() {
		return cityselect;
	}

	public void setCityselect(String cityselect) {
		this.cityselect = cityselect;
	}

	public String getCityinput() {
		return cityinput;
	}

	public void setCityinput(String cityinput) {
		this.cityinput = cityinput;
	}

	public String getGroupselect() {
		return groupselect;
	}

	public void setGroupselect(String groupselect) {
		this.groupselect = groupselect;
	}

	public String getGroupinput() {
		return groupinput;
	}

	public void setGroupinput(String groupinput) {
		this.groupinput = groupinput;
	}

	public String getYewuselect() {
		return yewuselect;
	}

	public void setYewuselect(String yewuselect) {
		this.yewuselect = yewuselect;
	}

	public String getUserselect() {
		return userselect;
	}

	public void setUserselect(String userselect) {
		this.userselect = userselect;
	}

	public String getUserinput() {
		return userinput;
	}

	public void setUserinput(String userinput) {
		this.userinput = userinput;
	}

	public String getKhsfselect() {
		return khsfselect;
	}

	public void setKhsfselect(String khsfselect) {
		this.khsfselect = khsfselect;
	}

	public int getLevel1() {
		return level1;
	}

	public void setLevel1(int level1) {
		this.level1 = level1;
	}

	public int getLevel2() {
		return level2;
	}

	public void setLevel2(int level2) {
		this.level2 = level2;
	}

	public int getLevel3() {
		return level3;
	}

	public void setLevel3(int level3) {
		this.level3 = level3;
	}

	public int getTotle() {
		return totle;
	}

	public void setTotle(int totle) {
		this.totle = totle;
	}
	

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
