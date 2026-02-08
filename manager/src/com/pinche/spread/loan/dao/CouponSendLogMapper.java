package com.pinche.spread.loan.dao;

import java.util.List;
import java.util.Map;

public interface CouponSendLogMapper {

	public void insert(CouponSendLog log);

	public List<CouponSendLog> search(Map<String, Object> map);
}
