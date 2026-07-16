package com.nmdy.sysManage.role.service;

import java.util.List;
import java.util.Map;

import com.nmdy.entity.DataJuese;

public interface DataJueseService {
	public List<DataJuese> listAllCheckedRole(int id);
	
	public int updateUserRoles(int id,String roleIds);
		
	public List<DataJuese> findPagination(Map<String,Object> map);
	
	public List<DataJuese> searchOp(String name);
	
	public DataJuese findById(int id);
	
	public int update(DataJuese juese);
	
	public int addJuese(DataJuese juese);
	public int countPagination(Map<String,Object> map);
	
	public int delete(int id);
}
