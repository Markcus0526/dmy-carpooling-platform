package com.nmdy.sysManage.role.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.entity.DataJuese;
import com.nmdy.entity.Juese;

public interface DataJueseDao extends SupperMapper{
	public DataJuese findById(int id);
	public List<DataJuese> findAll();
	public List<DataJuese> findUserRoles(String username);
	public int add(DataJuese juese);
	public int update(DataJuese juese);
	public int delete(int id);
	public String findUsernameById(int id);
	public int deleteUserRoles(String username);
	public int addUserRolesBatch(List<Map<String,Object>> list);
	public List<DataJuese> findPagination(Map<String,Object> map);
	public List<DataJuese> searchOp(String name);
	public int countPagination(Map<String,Object> map);
}