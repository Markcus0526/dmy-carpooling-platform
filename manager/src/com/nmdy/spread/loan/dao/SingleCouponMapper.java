package com.nmdy.spread.loan.dao;

import com.nmdy.base.SupperMapper;

public interface SingleCouponMapper extends SupperMapper{

	public int getMaxId();
	public void insert(SingleCoupon singleCoupon);

}
