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
import java.util.List;
import java.util.Map;



import com.nmdy.entity.Resource;
import com.nmdy.sysManage.role.dao.ResourceDao;

public class ResourceServiceImpl implements ResourceService {
	private ResourceDao resourceDao;
	
	public ResourceDao getResourceDao() {
		return resourceDao;
	}

	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public List<Resource> findResourceByRoleId(int roleId){
		return this.resourceDao.findResourceByRoleId(roleId);
	}
	
	public List<Resource> findAll(){
		return this.resourceDao.findAll();
	}
	
	public List<Resource> findAll(int roleId){
		List<Resource> listResult = new ArrayList<Resource>();
		List<Resource> listAll =  this.resourceDao.findAll();
		List<Resource> listByRoleId = this.resourceDao.findResourceByRoleId(roleId);
		for(Resource r:listAll){
			r.setChecked(false);
			for(Resource roleR:listByRoleId){
				if(r.getCode().equals(roleR.getCode())){
					r.setChecked(true);
					break;
				}
			}
			listResult.add(r);
		}
		return listResult;
	}
	
	public List<Integer> findUserRoleId(String username){
		return this.resourceDao.findUserRoleId(username);
	}
	
	public List<Resource> findAll(String username){
		List<Resource> listByRoleName = this.resourceDao.findResourceByUsername(username);
		List<Resource> listAll =  this.resourceDao.findAll();
		for(Resource r:listAll){
			r.setChecked(false);
			for(Resource roleR:listByRoleName){
				if(r.getCode().equals(roleR.getCode())){
					r.setChecked(true);
					break;
				}
			}
		}
		return listAll;
	}
	
	public int updateResourceForRoleId(int roleId,String codes){
		this.resourceDao.deleteResourceByRoleId(roleId);
		List<Resource> list = new ArrayList<Resource>();
		String[] cos = codes.split(",");
		for(String code:cos){
			Resource r = new Resource();
			r.setCode(code);
			r.setRoleId(roleId);
			list.add(r);
		}
		return this.resourceDao.addResourceBatch(list);
	}


	@Override
	public List<Resource> findResourceByUsername(
			String username) {
		return this.resourceDao.findResourceByUsername(username);
	}
}
