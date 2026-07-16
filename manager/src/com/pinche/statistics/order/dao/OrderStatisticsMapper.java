package com.pinche.statistics.order.dao;

import java.util.List;
import java.util.Map;

public interface OrderStatisticsMapper {
	public List<OrderStatisticsInfo> getOrderCnt(Map<String, Object> map);
	public int getOrderCntByStatus2(Map<String, Object> map);
}
