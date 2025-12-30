package com.nmdy.sysManage.role.dao;

import java.util.List;
import java.util.Map;



import com.nmdy.base.SupperMapper;
import com.nmdy.entity.Resource;

public interface ResourceDao extends SupperMapper{
	public List<Resource> findByName();
	public List<Resource> findResourceByUsername(String username);
	public List<Resource> findResourceByRoleId(int roleId);
	public List<Resource> findAll();
	/**
	 * 根据角色ID删除资源
	 * @param roleId
	 * @return
	 */
	public int deleteResourceByRoleId(int roleId);
	public int addResourceBatch(List<Resource> list);
	public List<Integer> findUserRoleId(String username);
}
