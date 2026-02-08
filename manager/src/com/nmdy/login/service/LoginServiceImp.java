package com.nmdy.login.service;

import java.util.HashMap;
import java.util.List;

import com.nmdy.login.dao.LoginMapper;
import com.nmdy.login.pojo.Menu;

public class LoginServiceImp implements ILoginService{
	
	private LoginMapper loginMapper=null;
	
	public LoginMapper getLoginMapper() {
		return loginMapper;
	}
	public void setLoginMapper(LoginMapper loginMapper) {
		this.loginMapper = loginMapper;
	}

	public boolean isUserPasswordRight(String userName,String password){		
		HashMap<String ,Object> hm=new HashMap<String,Object>();
		hm.put("userName", userName);
		hm.put("password", password);
		int cnt=loginMapper.isUserPasswordRight(hm);
		return cnt==0?false:true;
	}
	
	public boolean isUserPassword(String userName,String password){		
		HashMap<String ,Object> hm=new HashMap<String,Object>();
		hm.put("userName", userName);
		hm.put("password", password);
		int cnt=loginMapper.isUserPassword(hm);
		return cnt==0?false:true;
	}
	
	public List<Menu> getMenusOfLevel12(String userCode){
		List<Menu> menus=loginMapper.getMenusOfLevel12(userCode);
		return menus;
	}
	public List<Menu> getMenusByUser(String userCode){

		List<Menu> menus=loginMapper.getMenusByUser(userCode);
		return menus;
	}
	
	public int updatePass(String newPass,String usercode){
		HashMap<String ,Object> hm=new HashMap<String,Object>();
		hm.put("newPass", newPass);
		hm.put("usercode", usercode);
		return this.loginMapper.updatePass(hm);
	}
}
