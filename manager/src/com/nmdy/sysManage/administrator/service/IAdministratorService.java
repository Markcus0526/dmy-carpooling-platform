package com.nmdy.sysManage.administrator.service;

import java.util.List;
import java.util.Map;

import com.nmdy.sysManage.administrator.pojo.Administrator;

public interface IAdministratorService {
	
	public Administrator findOneById(int id);
	public Administrator findOneByUsercode(String usercode);
	public List<Administrator> findAll(Map<String, Object> map);
	public int sizeofTable();
	public List<Administrator> searchByUsercode(String usercode);
	public List<Administrator> searchByUsername(String username);
	public List<Administrator> searchByPhone(String phoneNum);
	public List<Administrator> searchByUnit(int unit);
	public List<Administrator> search(Map<String, Object> map);
	
	public int addAdministrator(Administrator info);
	public int changePassword(Administrator info);
	public int editAdministrator(Administrator info);	
	public int deleteAdministrator(int id);
	public boolean login(String usercode, String password);
}
