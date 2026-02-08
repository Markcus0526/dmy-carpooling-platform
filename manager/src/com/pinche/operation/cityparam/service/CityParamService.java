package com.pinche.operation.cityparam.service;

import java.util.Map;

import com.pinche.operation.cityparam.dao.CityParam;

public interface CityParamService {

	public CityParam find(String name);

	public int save(Map<String, Object> params);

	public CityParam findglobal();

	public int saveGloble(Map<String, Object> params);



}
