package com.nmdy.operation.cityparam.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.common.SqlSessionHelper;
import com.nmdy.operation.cityparam.dao.CityParam;
import com.nmdy.operation.cityparam.dao.CityParamMapper;

public class CityParamServiceImpl implements CityParamService{

	private CityParamMapper cityParamMapper;
	@Override
	public CityParam find(String name) {
		return cityParamMapper.find(name);
	}
	@Override
	public int insert(Map<String, Object> params) {
		return cityParamMapper.save(params);
	}
	@Override
	public CityParam findglobal() {
		return cityParamMapper.findglobal();
	}
	@Override
	public int insertGloble(Map<String, Object> params) {
		return cityParamMapper.saveGloble(params);
	}
	public CityParamMapper getCityParamMapper() {
		return cityParamMapper;
	}
	public void setCityParamMapper(CityParamMapper cityParamMapper) {
		this.cityParamMapper = cityParamMapper;
	}
	
	public int editAllowanceRule(Object param) {
		return 0;
	}
	
	public int addAllowanceRule(Object param) {
		return 0;
	}

	public List loadAllowanceByCity(String param) {
		return null;
	}
	
	public int removeAllowance(int param) {
		return 0;
	}
	
	public int editCarPriceRule(Object param) {
		return 0;
	}
	
	public int addCarPriceRule(Object param) {
		return 0;
	}
	
	public List loadCarPriceRuleByCity(String param) {
		return null;
	}
	
	public int removeCarPriceRule(int param) {
		return 0;
	}
	
	public int editCrowdedFee(Object param) {
		return 0;
	}
	
	public int addCrowdedFee(Object param) {
		return 0;
	}

	public List loadCrowdedFeeByCity(String param) {
		return null;
	}
	
	public int removeCrowdedFee(int param) {
		return 0;
	}
	
	public int editLowSpeedFeeRule(Object param) {
		return 0;
	}
	
	public int addLowSpeedFeeRule(Object param) {
		return 0;
	}

	public List loadLowSpeedFeeRuleByCity(String param) {
		return null;
	}
	
	public int removeLowSpeedFeeRule(int param) {
		return 0;
	}
	
	public int queryLowSpeedOn(Object param) {
		return 0;
	}
	
	public int updateLowSpeedOn(Object param) {
		return 0;
	}

}
