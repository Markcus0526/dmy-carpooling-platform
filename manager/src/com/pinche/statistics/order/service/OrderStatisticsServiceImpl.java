package com.pinche.statistics.order.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.statistics.order.dao.OrderStatisticsInfo;
import com.pinche.statistics.order.dao.OrderStatisticsMapper;

public class OrderStatisticsServiceImpl implements OrderStatisticsService{

	private SqlSession session = null;
	private OrderStatisticsMapper mapper = null;
	public OrderStatisticsServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(OrderStatisticsMapper.class);
	}
	@Override
	public List<OrderStatisticsInfo> getOrderCnt(Map<String, Object> map) {
		return mapper.getOrderCnt(map);
	}
	@Override
	public int getOrderCntByStatus2(Map<String, Object> map) {
		return mapper.getOrderCntByStatus2(map);
	}
}
