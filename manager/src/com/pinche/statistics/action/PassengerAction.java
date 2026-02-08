package com.pinche.statistics.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.WebUtil;
import com.pinche.statistics.passenger.dao.Passenger;
import com.pinche.statistics.passenger.dao.SearchItem;
import com.pinche.statistics.passenger.service.PassengerService;

public class PassengerAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PassengerService passengerService;
	private List<Passenger> passengerList;

	//search parameters
	private SearchItem searchItem;
	
	//JSON Object
	private JSONObject resultObj;
	
	//pagination parameter
	private int page;
	private int rows;
	private int total_size;
	
	public String index() {
		return SUCCESS;
	}
	
	public String search() {
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("date_type", searchItem.getDate_type());
			m.put("staticsCity", searchItem.getStaticsCity());
			m.put("beginDate", searchItem.getBeginDate());
			m.put("endDate", searchItem.getEndDate());
			m.put("beginTime", searchItem.getBeginTime());
			m.put("endTime", searchItem.getEndTime());
			m.put("platform_home", searchItem.getPlatform_home());
			m.put("platform_common", searchItem.getPlatform_common());
			m.put("app_download", searchItem.getApp_download());
			passengerList = passengerService.search(m);
			total_size = passengerList.size();
			m.put("page", (page-1)*rows);
			m.put("rows", rows);
			passengerList = passengerService.search(m);
		} catch (Exception e) {
			resultObj = null;
			e.printStackTrace();
			return ERROR;
		}
		WebUtil._setResponseContentType("application/json");
		makeJSONObjFromList();
		return SUCCESS;
	}

	public void makeJSONObjFromList() {
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Passenger info : passengerList) {
			Map<String, Object> m = new HashMap<String, Object>();
			
			m.put("date", info.getDate());
			m.put("cnt_app", info.getCnt_app());
			m.put("cnt_register", info.getCnt_register());
			m.put("cnt_invite", info.getCnt_invitecode());
			m.put("cnt_person", info.getCnt_person());
			m.put("cnt_bankcard", info.getCnt_bankcard());
			m.put("cnt_driver", info.getCnt_driver());

			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total_size);
		json.put("rows", al);
		
		try {
			resultObj = JSONObject.fromObject(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SearchItem getSearchItem() {
		return searchItem;
	}

	public void setSearchItem(SearchItem searchItem) {
		this.searchItem = searchItem;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	public JSONObject getResultObj() {
		return this.resultObj;
	}

	public PassengerService getPassengerService() {
		return passengerService;
	}

	public void setPassengerService(PassengerService passengerService) {
		this.passengerService = passengerService;
	}

	public List<Passenger> getPassengerList() {
		return passengerList;
	}

	public void setPassengerList(List<Passenger> passengerList) {
		this.passengerList = passengerList;
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

	public int getTotal_size() {
		return total_size;
	}

	public void setTotal_size(int total_size) {
		this.total_size = total_size;
	}

}
