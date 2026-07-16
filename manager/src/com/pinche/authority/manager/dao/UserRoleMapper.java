package com.pinche.authority.manager.dao;

import java.util.List;

import com.nmdy.base.SupperMapper;

public interface UserRoleMapper extends SupperMapper{

	public UserRole findOneById(int id);
	public List<UserRole> findByUserId(int user_id);
	public List<UserRole> findByRoleId(int role_id);
	public int deleteRole(int id);
	public int addRole(UserRole role);
	
}
