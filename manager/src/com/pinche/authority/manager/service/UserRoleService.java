package com.pinche.authority.manager.service;

import java.util.List;

import com.pinche.authority.manager.dao.UserRole;

public interface UserRoleService {
	
	public UserRole findOneById(int id);
	public List<UserRole> findByUserId(int user_id);
	public List<UserRole> findByRoleId(int role_id);
	public int deleteRole(int id);
	public int addRole(UserRole role);

}
