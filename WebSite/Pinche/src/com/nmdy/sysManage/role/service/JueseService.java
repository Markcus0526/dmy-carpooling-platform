package com.nmdy.sysManage.role.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nmdy.entity.Juese;

public interface JueseService {
	/**
	 * 查找用户所具有的角色
	 * @param username
	 * @return
	 */
	public List<Juese> listAllCheckedRole(int id);
	/**
	 * 修改用户所具有的权限
	 * @param id
	 * @param roleIds
	 * @return
	 */
	public int updateUserRoles(int id,String roleIds);
	/**
	 * 角色分页查询
	 * @param map
	 * @return
	 */
	public List<Juese> findPagination(Map<String, Object> map);
	/**
	 * 根据名称查找
	 * @param name
	 * @return
	 */
	public List<Juese> searchOp(String name);
	/**
	 * 查看
	 * @param id
	 * @return
	 */
	public Juese findById(int id);
	/**
	 * 修改
	 * @param juese
	 * @return
	 */
	public int update(Juese juese);
	/**
	 * 添加角色
	 * @param juese
	 * @return
	 */
	public int addJuese(Juese juese);
	public int delete(int id);
	public int findByName(String name);
	public int countPagination(Map<String,Object> map);
}
