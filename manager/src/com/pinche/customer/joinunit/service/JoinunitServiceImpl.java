package com.pinche.customer.joinunit.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.customer.joinunit.dao.Joinunit;
import com.pinche.customer.joinunit.dao.JoinunitMapper;

public class JoinunitServiceImpl implements JoinunitService{

	private SqlSession session = null;
	private JoinunitMapper mapper = null;

	public JoinunitServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(JoinunitMapper.class);
	}

	@Override
	public List<Joinunit> findAll() {
		return mapper.findAll();
	}

	@Override
	public Joinunit findById(int id) {
		return mapper.findById(id);
	}

	@Override
	public int updateTsid(Map<String, Object> map) {
		int rows = mapper.updateTsid(map);
		if (rows > 0)
			session.commit();
		return rows;
	}
	
}
