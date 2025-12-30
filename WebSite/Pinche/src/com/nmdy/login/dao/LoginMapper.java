package com.nmdy.login.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.login.pojo.Menu;

public interface LoginMapper extends SupperMapper{

	public int isUserPasswordRight(Map<String, Object> map) ;
	public int isUserPassword(Map<String, Object> map);
	public List<Menu> getMenusOfLevel12(String userId);
	public List<Menu> getMenusByUser(String userId);
	public int updatePass(Map<String,Object> map);
}
