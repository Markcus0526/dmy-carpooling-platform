package com.pinche.statistics.coupon.dao;

import java.util.List;
import java.util.Map;

public interface CouponMapper {
	
	public List<Coupon> search(Map<String, Object> map);

}
