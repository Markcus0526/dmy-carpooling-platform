package com.pinche.authority.role.service;

import java.util.List;
import java.util.Map;

import com.pinche.authority.role.dao.Role;

public interface RoleService {
	
	public Role findOneById(int id);
	public Role findLastAdded();
	public List<Role> findAllByName(String name, int type);
	public List<Role> findAllByType(int type);
	public List<Role> findAll();
	public List<Role> findPagination(Map<String, Object> map);
	
	public int addRole(Role role);
	public int editRole(Role role);
	public int deleteRole(int id);
	
	public List<Role> searchOp(String name);
	public List<Role> searchData(String name);

	public List<Role> existName(Map<String, Object> map);
}
