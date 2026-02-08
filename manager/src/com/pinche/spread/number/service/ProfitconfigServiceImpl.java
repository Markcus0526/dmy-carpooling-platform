package com.pinche.spread.number.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.spread.number.dao.Profitconfig;
import com.pinche.spread.number.dao.ProfitconfigMapper;

public class ProfitconfigServiceImpl implements ProfitconfigService {

	SqlSession session = null;
	ProfitconfigMapper mapper = null;
	
	public ProfitconfigServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(ProfitconfigMapper.class);
	}

	@Override
	public List<Profitconfig> findAll() {
		return mapper.findAll();
	}

	@Override
	public Profitconfig findLast() {
		return mapper.findLast();
	}

	@Override
	public Profitconfig findById(int id) {
		return mapper.findById(id);
	}

	@Override
	public int deleteAll() {
		int rows = mapper.deleteAll();
		if (rows > 0) {
			session.commit();
		}
		return rows;
	}

	@Override
	public int update(Profitconfig profitconfig) {
		int rows = mapper.update(profitconfig);
		if (rows > 0) {
			session.commit();
		}
		return rows;
	}
	
	@Override
	public List<Profitconfig> findAllPersons() {
		return mapper.findAllPersons();
	}
	
	@Override
	public Profitconfig findOneById(int id) {
		return mapper.findOneById(id);
	}

	@Override
	public Profitconfig findOneByGid(int gid) {
		return mapper.findOneByGid(gid);
	}

	@Override
	public Profitconfig findOneByUsercode(String usercode) {
		return mapper.findOneByUsercode(usercode);
	}

	@Override
	public List<Profitconfig> findUserBasicInfo(Map<String, Object> map) {
		return mapper.findUserBasicInfo(map);
	}

	@Override
	public List<Profitconfig> findAllUsers() {
		return mapper.findAllUsers();
	}

	@Override
	public List<Profitconfig> searchNumberspread(Map<String, Object> map) {
		return mapper.searchNumberspread(map);
	}

	@Override
	public int updateTsid(Map<String, Object> map) {
		int rows = mapper.updateTsid(map);
		if (rows > 0)
			session.commit();
		return rows;
	}

	@Override
	public Profitconfig findOneByUsername(Map<String, Object> map) {
		return mapper.findOneByUsername(map);
	}

	@Override
	public int updateNumberspread(Profitconfig customer) {
		int rows = mapper.updateNumberspread(customer);
		if (rows > 0)
			session.commit();
		return rows;
	}


}
