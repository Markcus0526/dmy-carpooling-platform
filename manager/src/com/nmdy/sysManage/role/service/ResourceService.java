package com.nmdy.sysManage.role.service;

import java.util.List;
import java.util.Map;

import com.nmdy.entity.Resource;

public interface ResourceService {
	/**
	 * 查找用户他所具有的资源
	 * @param username
	 * @return
	 */
	public List<Resource> findResourceByUsername(String username);
	/**
	 * 根据角色ID查找资源
	 * @param roleId
	 * @return
	 */
	public List<Resource> findResourceByRoleId(int roleId);
	public List<Resource> findAll();
	public List<Resource> findAll(int roleId);
	public List<Resource> findAll(String username);
	/**
	 * 更新角色资源
	 * @param roleId
	 * @param codes
	 * @return
	 */
	public int updateResourceForRoleId(int roleId,String codes);
	public List<Integer> findUserRoleId(String username);
}
