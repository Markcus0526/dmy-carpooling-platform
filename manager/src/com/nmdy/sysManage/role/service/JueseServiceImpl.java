/*===================================================
 * File:SystemService.java
 * Created:[2012-11-14 下午1:11:47] by Liam
 *===================================================
 * Project:bekiz_new_back
 * Package:com.tongyiku.system.service.impl
 * <p>Description:</p>
 *====================================================
 * Copyright:bekiz.tongyiku
 * Version:1.0
 *===================================================*/
package com.nmdy.sysManage.role.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nmdy.entity.Juese;
import com.nmdy.sysManage.role.dao.JueseDao;

public class JueseServiceImpl implements JueseService {
	private JueseDao jueseDao;

	public void setJueseDao(JueseDao jueseDao) {
		this.jueseDao = jueseDao;
	}
	
	public List<Juese> listAllCheckedRole(int id){
		List<Juese> listAll = this.jueseDao.findAll();
		String username = this.jueseDao.findUsernameById(id);
		List<Juese> listMyRole = this.jueseDao.findUserRoles(username);
		for(Juese j:listAll){
			j.setChecked(false);
			for(Juese juese:listMyRole){
				if(j.getId()==juese.getId()){
					j.setChecked(true);
				}
			}
		}
		return listAll;
	}
	
	public int updateUserRoles(int id,String roleIds){
		String username = this.jueseDao.findUsernameById(id);
		this.jueseDao.deleteUserRoles(username);
		String[] juese = roleIds.split(",");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(String j:juese){
			Map<String,Object> map = new HashMap<>();
			map.put("administrator_code",username);
			map.put("role_oper_id", j);
			list.add(map);
		}
		return this.jueseDao.addUserRolesBatch(list);
	}
	
	/**
	 * 角色分页查询
	 * @param map
	 * @return
	 */
	public List<Juese> findPagination(Map<String,Object> map){
		return this.jueseDao.findPagination(map);
	}
	
	public List<Juese> searchOp(String name){
		List<Juese> list = this.jueseDao.searchOp(name);
		return list;
	}
	
	public Juese findById(int id){
		return this.jueseDao.findById(id);
	}
	
	public int update(Juese juese){
		return this.jueseDao.update(juese);
	}
	
	public int addJuese(Juese juese){
		juese.setDeleted(0);
		return this.jueseDao.add(juese);
	}
	
	public int delete(int id){
		return this.jueseDao.delete(id);
	}
	
	public int findByName(String name){
		return this.jueseDao.findByName(name);
	}

	public int countPagination(Map<String, Object> map) {
		return this.jueseDao.countPagination(map);
	}
}
