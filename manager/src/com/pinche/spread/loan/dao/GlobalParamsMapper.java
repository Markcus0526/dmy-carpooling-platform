package com.pinche.spread.loan.dao;

import java.util.Map;

public interface GlobalParamsMapper {

	GlobalParams findOneById(int i);
	void update(Map<String, Object> params);

}
