package com.nmdy.financial.request.service;

import java.util.List;
import java.util.Map;

import com.nmdy.financial.request.dao.FreezeMapper;

public class FreezeServiceImpl implements FreezeService {

	
	private FreezeMapper mapper ;
	
	
	

	public FreezeMapper getMapper() {
		return mapper;
	}

	public void setMapper(FreezeMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public List<Map<String, Object>> findby(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findby(params);
	}

	@Override
	public int findcount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findcount(params);
	}

	@Override
	public int updatedealsingle(int reqid) {
		int r = mapper.updatedealsingle(reqid);
	
		return r;
	}

	@Override
	public int updatefinishsingle(int reqid) {
		int r = mapper.updatefinishsingle(reqid);
	
		return r;
	}

	@Override
	public int updateclosesingle(int reqid) {
		// TODO Auto-generated method stub
		int r = mapper.updateclosesingle(reqid);
	
		return r;
	}

	@Override
	public int addinsertts(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int r = mapper.addinsertts(params);

		return r;
	}

	@Override
	public Map<String, Object> findbalance(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findbalance(params);
	}

	@Override
	public Map<String, Object> finduserbalance(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.finduserbalance(params);
	}

	@Override
	public Map<String, Object> findgroupbalance(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findgroupbalance(params);
	}

	@Override
	public Map<String, Object> findunitbalance(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findunitbalance(params);
	}

	@Override
	public Map<String, Object> findfpinfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findfpinfo(params);
	}

	@Override
	public int addupfp(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int r = mapper.addupfp(params);
	
		return r;
	}

	@Override
	public int updateupfpstate(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int r = mapper.updateupfpstate(params);

		return r;
	}




}
