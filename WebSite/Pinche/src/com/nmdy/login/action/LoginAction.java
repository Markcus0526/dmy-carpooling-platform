package com.nmdy.login.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.nmdy.login.pojo.Menu;
import com.nmdy.login.service.ILoginService;
import com.nmdy.sysManage.role.service.ResourceService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.authority.manager.dao.Administrator;
import com.pinche.authority.manager.service.AdministratorService;
import com.pinche.common.Constants;
import com.pinche.common.UserLoginInfo;
import com.pinche.common.WebUtil;

public class LoginAction extends ActionSupport {

	private String userName=null;
	private String password=null;
	
	private ILoginService loginService=null;
	private AdministratorService administratorService;
	private ResourceService resourceService;
	
	public ILoginService getLoginService() {
		return loginService;
	}
	public void setLoginService(ILoginService loginService) {
		this.loginService = loginService;
	}
	
	public String main(){
		return "main";
	}
	
	public String login(){
		HttpServletRequest request = ServletActionContext.getRequest();
		//
//		userName="user";
//		password="123";
		boolean isPass=loginService.isUserPasswordRight(userName, password);
		if(isPass==false){
			ActionContext.getContext().put("msg",Constants.LOGIN_ERROR);
			return "auth_failed";
		}
		Administrator admin = administratorService.findOneByUsercode(userName);
//		Administrator user = administratorService.findOneByUsercode(userName);
		//把用户名放入到session里
		ActionContext.getContext().getSession().put("userName", userName);
		ActionContext.getContext().getSession().put("admin", admin);
		//把用户所具有的资源放入到session里
		ActionContext.getContext().getSession().put("userResource", resourceService.findResourceByUsername(userName));
		//把全部资源放到session里
		ActionContext.getContext().getSession().put("allResource", resourceService.findAll(userName));
		//1，获得1,2级菜单
		List<Menu> menus=loginService.getMenusOfLevel12(userName);
		//System.out.println("menus.length:"+menus.size());
		JSONObject jroot=new JSONObject();
		JSONArray ja=new JSONArray();
		
		Iterator<Menu> it=menus.iterator();
		while(it.hasNext()){
			Menu menu=(Menu)it.next();
			String pFuncCode=menu.getpFuncCode();
			int index=isArrayContainsItem(ja,pFuncCode);
			if(index<0){//념듸侶섄흖鷄동젭
				JSONObject je1=new JSONObject();
				je1.put("menuid", menu.getpFuncCode());
				je1.put("icon", menu.getpIcon());
				je1.put("menuname", menu.getpFuncName());
				
				JSONArray jarr=new JSONArray();
				JSONObject newObject=new JSONObject();
				newObject.put("menuid", menu.getsFuncCode());
				newObject.put("menuname", menu.getsFuncName());
				newObject.put("icon", menu.getsIcon());
				newObject.put("url", "/bk" + menu.getUrl());
				jarr.add(newObject);
				je1.put("menus", jarr);
				ja.add(je1);
			}else{//념듸侶欺츔동젭팟?쏟덛뭤념듸侶
				JSONObject je1=ja.getJSONObject(index);
				JSONArray jarr=je1.getJSONArray("menus");
				JSONObject newObject=new JSONObject();
				newObject.put("menuid", menu.getsFuncCode());
				newObject.put("menuname", menu.getsFuncName());
				newObject.put("icon", menu.getsIcon());
				newObject.put("url", "/bk" + menu.getUrl());
				jarr.add(newObject);
			}
		}
		jroot.put("menus", ja);
		System.out.println(jroot.toString());
		request.setAttribute("menus", jroot.toString());
		
		//2,获得用户有权限访问的操作菜单
		List<Menu> funcs=loginService.getMenusByUser(userName);
		Iterator it1= funcs.iterator();
		String resourceList = "";
		while(it1.hasNext()){
			Menu menu=(Menu)it1.next();
			resourceList += menu.getUrl()==null?"":menu.getUrl() + ";";
		}
		WebUtil._session("resourceList", resourceList);
		
		System.out.println("resourceList:"+resourceList);
		UserLoginInfo info = new UserLoginInfo();
		info.setUsercode(userName);
		
		WebUtil._session("user", info);

		return "auth_success";

		 
	}
	/*
	 * ja틸얏넌얏欺츔극상팟menuid姜funcCode랐json요쵱
	 */
	public int isArrayContainsItem(JSONArray ja,String funcCode){
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			String fcode=(String)jo.get("menuid");
			if(fcode.equals(funcCode)){
				return i;
			}
		}
		
		return -1;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public AdministratorService getAdministratorService() {
		return administratorService;
	}
	public void setAdministratorService(AdministratorService administratorService) {
		this.administratorService = administratorService;
	}
	public ResourceService getResourceService() {
		return resourceService;
	}
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
}
