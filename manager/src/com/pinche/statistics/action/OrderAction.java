package com.pinche.statistics.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.WebUtil;
import com.pinche.statistics.order.dao.OrderStatisticsInfo;
import com.pinche.statistics.order.dao.SearchItem;
import com.pinche.statistics.order.service.OrderStatisticsService;

public class OrderAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SearchItem searchItem;
	private JSONObject resultObj;
	
	private List<OrderStatisticsInfo> orderStatisticsInfos;
	
	// Services
	private OrderStatisticsService orderStatisticsService;

	public String index() {
		return SUCCESS;
	}
	
	public String startOrderStatistics() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dateType", searchItem.getDate_type());
			map.put("orderTemp", searchItem.getOrderTemp());
			map.put("orderLong", searchItem.getOrderLong());
			map.put("orderOnoffDetails", searchItem.getOrderOnoffDetails());
			map.put("orderOnoffDivide", searchItem.getOrderOnoffDivide());
			map.put("orderOnoffExec", searchItem.getOrderOnoffExec());
			map.put("beginDate", searchItem.getBeginDate());
			map.put("endDate", searchItem.getEndDate());
			map.put("beginTime", searchItem.getBeginTime());
			map.put("endTime", searchItem.getEndTime());
			map.put("beginPrice", searchItem.getBeginPrice());
			map.put("endPrice", searchItem.getEndPrice());
			map.put("beginDist", searchItem.getBeginDist());
			map.put("endDist", searchItem.getEndDist());
			map.put("newUser", searchItem.getNewUser());
			map.put("oldUser", searchItem.getOldUser());
			map.put("appMode", searchItem.getAppMode());
			map.put("siteMode", searchItem.getSiteMode());
			
			orderStatisticsInfos = orderStatisticsService.getOrderCnt(map);			
			ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
			for (OrderStatisticsInfo info: orderStatisticsInfos) {
				Map<String, Object> m = new HashMap<String, Object>();
				int nDate = info.getPs_date();
				m.put("date", nDate);
				m.put("pubOrderCnt", info.getPs_order_cnt());
				map.put("date", nDate);
				map.put("status", 2);
				int cnt = orderStatisticsService.getOrderCntByStatus2(map);
				m.put("acptOrderCnt", cnt);
//				m.put("payedOrderCnt", );
//				m.put("completeOrderCnt", );
//				m.put("totalPrice", );
//				m.put("averagePrice", );
//				m.put("totalDist", );
//				m.put("averageDist", );
//				m.put("averagePrice4averageDis", );
				al.add(m);
			}
			
			WebUtil._setResponseContentType("application/json");
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("total", orderStatisticsInfos.size());
			json.put("rows", al);
			resultObj = JSONObject.fromObject(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	private void makeJSONObjFromList(){
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
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

	public OrderStatisticsService getOrderStatisticsService() {
		return orderStatisticsService;
	}

	public void setOrderStatisticsService(
			OrderStatisticsService orderStatisticsService) {
		this.orderStatisticsService = orderStatisticsService;
	}
}
