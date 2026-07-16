package com.nmdy.sysManage.role.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.entity.Juese;

public interface JueseDao extends SupperMapper{
	public Juese findById(int id);
	public List<Juese> findAll();
	public List<Juese> findUserRoles(String username);
	public int add(Juese juese);
	public int update(Juese juese);
	public int delete(int id);
	public String findUsernameById(int id);
	public int deleteUserRoles(String username);
	public int addUserRolesBatch(List<Map<String,Object>> list);
	public List<Juese> findPagination(Map<String,Object> map);
	public List<Juese> searchOp(String name);
	public int findByName(String name);
	public int countPagination(Map<String,Object> map);
}