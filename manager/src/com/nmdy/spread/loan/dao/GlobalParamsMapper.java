package com.nmdy.spread.loan.dao;

import java.util.Map;

import com.nmdy.base.SupperMapper;

public interface GlobalParamsMapper extends SupperMapper{

	GlobalParams findOneById(int i);
	void update(Map<String, Object> params);

}
