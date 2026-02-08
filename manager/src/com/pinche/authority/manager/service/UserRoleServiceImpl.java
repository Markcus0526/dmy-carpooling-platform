package com.pinche.authority.manager.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.pinche.authority.manager.dao.UserRole;
import com.pinche.authority.manager.dao.UserRoleMapper;

public class UserRoleServiceImpl implements UserRoleService {

	private SqlSession session = null;
	private UserRoleMapper mapper = null;

	public UserRoleServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(UserRoleMapper.class);
	}
	
	@Override
	public UserRole findOneById(int id) {
		return mapper.findOneById(id);
	}

	@Override
	public List<UserRole> findByUserId(int user_id) {
		return mapper.findByUserId(user_id);
	}

	@Override
	public List<UserRole> findByRoleId(int role_id) {
		return mapper.findByRoleId(role_id);
	}

	@Override
	public int deleteRole(int id) {
		int rows = mapper.deleteRole(id);
		if (rows > 0) {
			session.commit();
		}
		
		return rows;
	}

	@Override
	public int addRole(UserRole role) {
		int rows = mapper.addRole(role);
		if (rows > 0) {
			session.commit();
		}
		
		return rows;
	}
}
