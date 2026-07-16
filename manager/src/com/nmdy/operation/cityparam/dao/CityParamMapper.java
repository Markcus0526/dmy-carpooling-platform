package com.nmdy.operation.cityparam.dao;

import java.util.Map;

import com.nmdy.base.SupperMapper;


public interface CityParamMapper extends SupperMapper{

	public CityParam find(String name);

	public int save(Map<String, Object> params);

	public CityParam findglobal();

	public int saveGloble(Map<String, Object> params);

}
