package com.pinche.statistics.coupon.service;

import java.util.List;
import java.util.Map;

import com.pinche.statistics.coupon.dao.Coupon;

public interface CouponService {
	public List<Coupon> search(Map<String, Object> map);
}
