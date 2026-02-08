package com.pinche.operation.operlog.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.operation.operlog.dao.Oper_Log;
import com.pinche.operation.operlog.service.Oper_LogService;
/**
 * 日志反馈的信息Action
 * @author xcnana
 *
 */
public class Oper_LogAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private int page;
	private int rows;
	
   private String  userName;
   private String begin;
   private String end;
   private String keyword;
   private JSONObject resultObject=null;
   @Resource
   private Oper_LogService oper_LogService;
	 public Oper_LogAction() {
		super();
	}
   /*
    * 日志的首页面的跳转
    */
   public String index(){
	   
	   return "success";
   }
   /*
    * 通过条件查询日志的记录
    */
   public String findLogByCondition(){	
	//HttpServletRequest request = ServletActionContext.getRequest();
	   List<Oper_Log> loginfo=null;
	   Map<String,Object> params=new HashMap<String, Object>();
	   Map<String, Object> jsonMap = new HashMap<String, Object>(); 
		   params.put("username", userName);
		   params.put("keyword", keyword);
		   params.put("begin", begin);
		   params.put("end", end);
		   jsonMap.put("total", oper_LogService.getCount(params));
		   params.put("start", (page - 1) * rows);
		   params.put("limit", rows);
		 
		   loginfo=oper_LogService.findLogByCondition(params);
		   jsonMap.put("rows", loginfo);
		   resultObject = JSONObject.fromObject(jsonMap);
	   return "success";
   }
   
/*
 * getter 和 setter 方法 
 */
public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public String getBegin() {
	return begin;
}

public void setBegin(String begin) {
	this.begin = begin;
}

public String getEnd() {
	return end;
}

public void setEnd(String end) {
	this.end = end;
}

public String getKeyword() {
	return keyword;
}
public void setKeyword(String keyword) {
	this.keyword = keyword;
}


public JSONObject getResultObject() {
	return resultObject;
}


public void setResultObject(JSONObject resultObject) {
	this.resultObject = resultObject;
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
public Oper_LogService getOper_LogService() {
	return oper_LogService;
}
public void setOper_LogService(Oper_LogService oper_LogService) {
	this.oper_LogService = oper_LogService;
}


   
}
