package com.nmdy.spread.loan.dao;

import java.util.Map;

import com.nmdy.base.SupperMapper;

public interface CityMapper extends SupperMapper{

	City findOneByName(String name);
	void update(City city);
	public City findCityParam(String name);
	public City findglobal();
	void update(Map<String, Object> params);

}
