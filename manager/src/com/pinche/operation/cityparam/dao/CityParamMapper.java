package com.pinche.operation.cityparam.dao;

import java.util.Map;


public interface CityParamMapper {

	public CityParam find(String name);

	public int save(Map<String, Object> params);

	public CityParam findglobal();

	public int saveGloble(Map<String, Object> params);

}
