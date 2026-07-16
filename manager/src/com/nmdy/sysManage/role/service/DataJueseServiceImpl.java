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

import com.nmdy.entity.DataJuese;
import com.nmdy.entity.Juese;
import com.nmdy.sysManage.role.dao.DataJueseDao;
import com.nmdy.sysManage.role.dao.JueseDao;

public class DataJueseServiceImpl implements DataJueseService {
	private DataJueseDao dataJueseDao;

	public void setDataJueseDao(DataJueseDao dataJueseDao) {
		this.dataJueseDao = dataJueseDao;
	}

	public List<DataJuese> listAllCheckedRole(int id){
		List<DataJuese> listAll = this.dataJueseDao.findAll();
		String username = this.dataJueseDao.findUsernameById(id);
		List<DataJuese> listMyRole = this.dataJueseDao.findUserRoles(username);
		for(DataJuese j:listAll){
			j.setChecked(false);
			for(DataJuese juese:listMyRole){
				if(j.getId()==juese.getId()){
					j.setChecked(true);
				}
			}
		}
		return listAll;
	}
	
	public int updateUserRoles(int id,String roleIds){
		String username = this.dataJueseDao.findUsernameById(id);
		this.dataJueseDao.deleteUserRoles(username);
		String[] juese = roleIds.split(",");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(String j:juese){
			Map<String,Object> map = new HashMap<>();
			map.put("administrator_code",username);
			map.put("role_oper_id", j);
			list.add(map);
		}
		return this.dataJueseDao.addUserRolesBatch(list);
	}
	
	/**
	 * 角色分页查询
	 * @param map
	 * @return
	 */
	public List<DataJuese> findPagination(Map<String,Object> map){
		return this.dataJueseDao.findPagination(map);
	}
	
	public List<DataJuese> searchOp(String name){
		List<DataJuese> list = this.dataJueseDao.searchOp(name);
		return list;
	}
	
	public DataJuese findById(int id){
		return this.dataJueseDao.findById(id);
	}
	
	public int update(DataJuese juese){
		return this.dataJueseDao.update(juese);
	}
	
	
	public int addJuese(DataJuese juese){
		juese.setDeleted(0);
		return this.dataJueseDao.add(juese);
	}
	
	public int countPagination(Map<String,Object> map){
		return this.dataJueseDao.countPagination(map);
	}
	
	public int delete(int id){
		return this.dataJueseDao.delete(id);
	}
}
