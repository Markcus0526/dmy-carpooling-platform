package com.pinche.spread.loan.dao;

import java.util.Map;

public interface CityMapper {

	City findOneByName(String name);
	void update(City city);
	public City findCityParam(String name);
	public City findglobal();
	void update(Map<String, Object> params);

}
