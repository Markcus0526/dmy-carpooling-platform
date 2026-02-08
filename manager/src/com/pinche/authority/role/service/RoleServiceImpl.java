package com.pinche.authority.role.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.authority.role.dao.Role;
import com.pinche.authority.role.dao.RoleMapper;

public class RoleServiceImpl implements RoleService {

	private SqlSession session = null;
	private RoleMapper mapper = null;
	
	public RoleServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(RoleMapper.class);
	}
	
	@Override
	public int addRole(Role role) {
		int rows = mapper.addRole(role);
		if (rows > 0)
			session.commit();
		return rows;
	}

	@Override
	public int editRole(Role role) {
		int rows = mapper.editRole(role);
		if (rows > 0)
			session.commit();
		return rows;
	}

	@Override
	public int deleteRole(int id) {
		int rows = mapper.deleteRole(id);
		if (rows > 0)
			session.commit();
		return rows;
	}

	@Override
	public Role findOneById(int id) {
		return mapper.findOneById(id);
	}

	@Override
	public List<Role> findAllByName(String name, int type) {
		return mapper.findAllByName(name, type);
	}

	@Override
	public List<Role> findAllByType(int type) {
		return mapper.findAllByType(type);
	}

	@Override
	public Role findLastAdded() {
		return mapper.findLastAdded();
	}

	@Override
	public List<Role> searchOp(String name) {
		return mapper.searchOp(name);
	}

	@Override
	public List<Role> searchData(String name) {
		return mapper.searchData(name);
	}

	@Override
	public List<Role> findAll() {
		return mapper.findAll();
	}

	@Override
	public List<Role> findPagination(Map<String, Object> map) {
		return mapper.findPagination(map);
	}

	@Override
	public List<Role> existName(Map<String, Object> map) {
		return mapper.existName(map);
	}
}
