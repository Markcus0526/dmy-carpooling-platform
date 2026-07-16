package com.nmdy.operation.abbrecord.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.nmdy.operation.abbrecord.dao.AbbRecord;
import com.nmdy.operation.abbrecord.service.AbbRecordService;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 违约记录表中详细纪录的Action
 * @author xcnana
 *
 */
public class AbbRecordAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String abb_type1=null;
	private String abb_type2=null;
	private int status=0;
	private int uerselect=0;
	private String inputvalue=null;
	private JSONObject resultObject=null;
	private int page;
	private int rows;
	
	private AbbRecordService abbRecordService;
	/*
	 *详细信息查询的跳转页面 
	 */
	public String index(){
		return "success";
	}
	/*
	 * 通过条件查询违约记录 
	 * 
	 * 方法名      abbRecordService.findpassenger(params); 
	 * 
	 * 我写的虽然是查询乘客记录 实际是我将乘客记录查询和车主记录查询合并
	 * 但名字没有改动 造成的
	 */
    public String find(){
    	String abbType2=ServletActionContext.getRequest().getParameter("abb_type2");
    	System.out.println(abb_type1+"````"+abb_type2+"````"+status+"````"+uerselect+"````"+inputvalue+" abbType2:"+abbType2);
    	
    	Map<String ,Object> params=new HashMap<String,Object>();
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
    	params=select(params,uerselect,inputvalue);
    	if(!abb_type1.equals("")){
    		params.put("abb_type1", abb_type1);
    	}
    	if(!abb_type2.equals("")){
    		params.put("abb_type2", abb_type2);
    	}
    	if(status!=0){
    		params.put("status", status);
    	}else{
    		params.put("status", null);
    	}
    	//有条件查询违约记录总数
    	int total=abbRecordService.getCount(params);
    	
    	params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		System.out.println(params);
    	List<AbbRecord> abbRecord=abbRecordService.findpassenger(params);
    	List<Map<String,Object>> map=new ArrayList<Map<String,Object>>();
    	for(AbbRecord ar:abbRecord){
			Map<String,Object> m=new HashMap<String,Object>();
    		if(ar.getAbb_time()==null){
    			m.put("abb_time", "");
    		}else{
    			m.put("abb_time",ar.getAbb_time());
    		}if(ar.getLimit_days_begin()==null){
    			m.put("limit_days_begin", "");
    		}else{
    			m.put("limit_days_begin", ar.getLimit_days_begin());
    		}
    		m.put("abb_type", ar.getAbb_type());
    		m.put("id", ar.getId());
    		m.put("order_exec_id", ar.getOrder_exec_id());
    		m.put("status", ar.getStatus());
    		m.put("limit_days", ar.getLimit_days());
    		m.put("phone", ar.getPhone());
    		m.put("username", ar.getUsername());
    		m.put("userid", ar.getUserid());
    		m.put("cancel_number", ar.getCancel_number());
    		map.add(m);
    	}
    	System.out.println(abbRecord);
    	if(total==0){
    	  	// 需要显示数据的总页数
        	jsonMap.put("total", 1);
    		// 需要实现数据的总记录数
    		jsonMap.put("rows", 0);
    	}else{
    	  	// 需要显示数据的总页数
        	jsonMap.put("total", total);
    		// 需要实现数据的总记录数
    		jsonMap.put("rows", map);
    	}
  
	    //有条件查询违约记录数据
		resultObject = JSONObject.fromObject(jsonMap);
		System.out.println(resultObject);
    	return "success";
      }
    
	/*
	 * find 子方法
	 * 查看从前台传入的数据是哪个条件
	 * 根据放入到params的条件 进行查询 违约记录 的数据 
	 */
	  private Map<String, Object> select(Map<String, Object> params,int uerselect,String inputvalue) {
		  if(inputvalue!=null && ""!=inputvalue){
			if(uerselect==1){
				params.put("userid", inputvalue);
			}if(uerselect==2){
				params.put("phone", inputvalue);
			}if(uerselect==3){
				params.put("username", inputvalue);
			}
		  }
			return params;
		}

	  
	/*
	 * getter 和 setter方法
	 */
	public AbbRecordService getAbbRecordService() {
		return abbRecordService;
	}

	public void setAbbRecordService(AbbRecordService abbRecordService) {
		this.abbRecordService = abbRecordService;
	}

	public String getAbb_type1() {
		return abb_type1;
	}
	public void setAbb_type1(String abb_type1) {
		this.abb_type1 = abb_type1;
	}
	public String getAbb_type2() {
		return abb_type2;
	}
	public void setAbb_type2(String abb_type2) {
		this.abb_type2 = abb_type2;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getUerselect() {
		return uerselect;
	}
	public void setUerselect(int uerselect) {
		this.uerselect = uerselect;
	}
	public String getInputvalue() {
		return inputvalue;
	}
	public void setInputvalue(String inputvalue) {
		this.inputvalue = inputvalue;
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
    
	
}
