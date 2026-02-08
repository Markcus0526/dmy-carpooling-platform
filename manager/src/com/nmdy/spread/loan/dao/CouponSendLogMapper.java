package com.nmdy.spread.loan.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;

public interface CouponSendLogMapper extends SupperMapper{

	public void insert(CouponSendLog log);

	public List<CouponSendLog> search(Map<String, Object> map);
}
