package com.pinche.statistics.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.WebUtil;
import com.pinche.statistics.finance.dao.Finance;
import com.pinche.statistics.finance.dao.SearchItem;
import com.pinche.statistics.finance.service.FinanceService;

public class FinanceAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FinanceService financeService;
	private List<Finance> financeList;

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
			m.put("order_1", searchItem.getOrder_1());
			m.put("order_2", searchItem.getOrder_2());
			m.put("order_3", searchItem.getOrder_3());
			m.put("order_4", searchItem.getOrder_4());
			financeList = financeService.search(m);
			total_size = financeList.size();
			m.put("page", (page-1)*rows);
			m.put("rows", rows);
			financeList = financeService.search(m);
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
		for (Finance info : financeList) {
			Map<String, Object> m = new HashMap<String, Object>();
			
			m.put("date", info.getDate());
			m.put("money_all", info.getMoney_all());
			m.put("money_loan", info.getMoney_loan());
			m.put("money_info", info.getMoney_info());
			m.put("money_person", info.getMoney_person());
			m.put("money_group", info.getMoney_group());
			m.put("money_unit", info.getMoney_unit());

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
	
	public FinanceService getFinanceService() {
		return financeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	public List<Finance> getFinanceList() {
		return financeList;
	}

	public void setFinanceList(List<Finance> financeList) {
		this.financeList = financeList;
	}

	public SearchItem getSearchItem() {
		return searchItem;
	}

	public void setSearchItem(SearchItem searchItem) {
		this.searchItem = searchItem;
	}

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
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
