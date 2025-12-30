package com.pinche.authority.manager.service;

import com.pinche.authority.manager.dao.Authority;
import com.pinche.authority.manager.dao.AuthorityMapper;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

public class AuthorityServiceImpl implements AuthorityService {
	private SqlSession session = null;
	private AuthorityMapper mapper = null;

	public AuthorityServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(AuthorityMapper.class);
	}
	
	public List<Authority> getResourceList(int id) {
		return mapper.getResourceList(id);
	}

	public SqlSession getSession() {
		return session;
	}

	public void setSession(SqlSession session) {
		this.session = session;
	}

	public AuthorityMapper getMapper() {
		return mapper;
	}

	public void setMapper(AuthorityMapper mapper) {
		this.mapper = mapper;
	}
}
