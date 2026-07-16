package com.pinche.operation.cityparam.service;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.operation.cityparam.dao.CityParam;
import com.pinche.operation.cityparam.dao.CityParamMapper;

public class CityParamServiceImpl implements CityParamService{

	private SqlSession session;
	private CityParamMapper cityParamMapper;
	public CityParamServiceImpl(){
		session=SqlSessionHelper.getSession();
		cityParamMapper=session.getMapper(CityParamMapper.class);
	}
	@Override
	public CityParam find(String name) {
		return cityParamMapper.find(name);
	}
	@Override
	public int save(Map<String, Object> params) {
		int row =cityParamMapper.save(params);
		if(row>0){
			session.commit();
		}
		return row;
	}
	@Override
	public CityParam findglobal() {
		return cityParamMapper.findglobal();
	}
	@Override
	public int saveGloble(Map<String, Object> params) {
		int row=cityParamMapper.saveGloble(params);
		if(row>0){
			session.commit();
		}
		return row;
	}

}
