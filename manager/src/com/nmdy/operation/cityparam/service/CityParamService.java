package com.nmdy.operation.cityparam.service;

import java.util.Map;
import java.util.List;

import com.nmdy.operation.cityparam.dao.CityParam;


public interface CityParamService {

	public CityParam find(String name);

	public int insert(Map<String, Object> params);

	public CityParam findglobal();

	public int insertGloble(Map<String, Object> params);

	
	// follow added virtual functions because build error by RyuCJ 2015.05.16
	public int editAllowanceRule(Object param);
	
	public int addAllowanceRule(Object param);

	public List loadAllowanceByCity(String param);
	
	public int removeAllowance(int param);
	
	public int editCarPriceRule(Object param);
	
	public int addCarPriceRule(Object param);
	
	public List loadCarPriceRuleByCity(String param);
	
	public int removeCarPriceRule(int param);
	
	public int editCrowdedFee(Object param);
	
	public int addCrowdedFee(Object param);

	public List loadCrowdedFeeByCity(String param);
	
	public int removeCrowdedFee(int param);
	
	public int editLowSpeedFeeRule(Object param);
	
	public int addLowSpeedFeeRule(Object param);

	public List loadLowSpeedFeeRuleByCity(String param);
	
	public int removeLowSpeedFeeRule(int param);
	
	public int queryLowSpeedOn(Object param);
	
	public int updateLowSpeedOn(Object param);
}
