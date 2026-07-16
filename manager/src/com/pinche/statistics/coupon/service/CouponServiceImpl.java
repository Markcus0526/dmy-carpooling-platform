package com.pinche.statistics.coupon.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.statistics.coupon.dao.Coupon;
import com.pinche.statistics.coupon.dao.CouponMapper;

public class CouponServiceImpl implements CouponService {

	SqlSession session = null;
	CouponMapper mapper = null;
	
	public CouponServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(CouponMapper.class);
	}

	@Override
	public List<Coupon> search(Map<String, Object> map) {
		return mapper.search(map);
	}

}
