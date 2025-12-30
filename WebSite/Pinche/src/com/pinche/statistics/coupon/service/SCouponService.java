package com.pinche.statistics.coupon.service;

import java.util.List;
import java.util.Map;

import com.pinche.statistics.coupon.dao.SCoupon;

public interface SCouponService {
	public List<SCoupon> viewDetail(Map<String, Object> map);
}
