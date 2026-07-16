package com.pinche.statistics.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.WebUtil;
import com.pinche.statistics.driver.dao.Driver;
import com.pinche.statistics.driver.service.DriverService;

public class DriverAction extends ActionSupport {

	/**
	 * 
	 */
	private String city;
	private String beginDate;
	private String endDate;
	private String beginTime;
	private String endTime;
	private int page;
	private int rows;
	private DriverService driverService;
	private int countofDriver = 0;
	private int countofDriverReg = 0;
	private int tableSize;
	private JSONObject resultObj = null;
	private String method;
	private static final long serialVersionUID = 1L;
	private List<Driver>	driverList = null;

	public String index() {
		return SUCCESS;
	}
	
	public String search(){
		Map<String, Object> map = new HashMap<String, Object>();
		WebUtil._setResponseContentType("application/json");
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		Map<String, Object> re = new HashMap<String, Object>();
		Map<String, Object> json = new HashMap<String, Object>();
		map.put("city", "%"+city+"%");
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		if((method != null)&&(method.equals("showall")))
		{	
			try{
			countofDriver = driverService.countofDriver(map);
			countofDriverReg = driverService.countofDriverReg(map);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			re.put("ts_id", 1);
			re.put("countofDriver",countofDriver);
			re.put("countofRegDriver", countofDriverReg);
			al.add(re);
			json.put("total", 1);
			json.put("rows", al);
			resultObj = JSONObject.fromObject(json);
		}
		if(!method.equals("showall")){
			map.put("type", method);
			try{
				tableSize = driverService.getDriverInfo(map).size();
				map.put("start",(page-1)*rows);
				map.put("end", rows);
				driverList = driverService.getDriverInfo(map);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			makeJSONObjFromList();
		}
		return SUCCESS;
	}

	public void makeJSONObjFromList(){
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Driver info: driverList){
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ts_id",info.getNum());
			m.put("countofDriver", info.getCountofDriver());
			m.put("countofRegDriver", info.getCountofDriverReg());
			al.add(m);
		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", tableSize);
		json.put("rows", al);
		resultObj = JSONObject.fromObject(json);
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public DriverService getDriverService() {
		return driverService;
	}

	public void setDriverService(DriverService driverService) {
		this.driverService = driverService;
	}

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<Driver> getDriverList() {
		return driverList;
	}

	public void setDriverList(List<Driver> driverList) {
		this.driverList = driverList;
	}
}
