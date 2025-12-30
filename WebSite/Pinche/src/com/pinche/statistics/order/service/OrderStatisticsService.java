package com.pinche.statistics.order.service;

import java.util.List;
import java.util.Map;

import com.pinche.statistics.order.dao.OrderStatisticsInfo;


public interface OrderStatisticsService {
	public List<OrderStatisticsInfo> getOrderCnt(Map<String, Object> map);	
	public int getOrderCntByStatus2(Map<String, Object> map);
}
