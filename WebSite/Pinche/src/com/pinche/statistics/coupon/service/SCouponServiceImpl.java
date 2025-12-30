package com.pinche.statistics.coupon.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.statistics.coupon.dao.SCoupon;
import com.pinche.statistics.coupon.dao.SCouponMapper;

public class SCouponServiceImpl implements SCouponService {

	SqlSession session = null;
	SCouponMapper mapper = null;
	
	public SCouponServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(SCouponMapper.class);
	}

	@Override
	public List<SCoupon> viewDetail(Map<String, Object> map) {
		return mapper.viewDetail(map);
	}

}
