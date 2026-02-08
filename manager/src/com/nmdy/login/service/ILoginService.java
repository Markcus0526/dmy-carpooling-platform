package com.nmdy.login.service;

import java.util.List;

import com.nmdy.login.pojo.Menu;

public interface ILoginService {
	public boolean isUserPasswordRight(String userName,String password);
	public List<Menu> getMenusOfLevel12(String userCode);
	public List<Menu> getMenusByUser(String userCode);
	public boolean isUserPassword(String userName,String password);
	public int updatePass(String newPass,String usercode);
}
