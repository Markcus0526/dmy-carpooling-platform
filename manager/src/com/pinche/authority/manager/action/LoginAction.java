package com.pinche.authority.manager.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.nmdy.login.service.ILoginService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.authority.manager.dao.Administrator;
import com.pinche.authority.manager.dao.Authority;
import com.pinche.authority.manager.service.AdministratorService;
import com.pinche.authority.manager.service.AuthorityServiceImpl;
import com.pinche.authority.menu.dao.Menu;
import com.pinche.authority.menu.service.MenuServiceImpl;
import com.pinche.common.Constants;
import com.pinche.common.SqlSessionHelper;
import com.pinche.common.UserLoginInfo;
import com.pinche.common.VCodeBuilder;
import com.pinche.common.WebUtil;

public class LoginAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AdministratorService administratorService;
	private AuthorityServiceImpl authorityService;
	private MenuServiceImpl menuService;

	private JSONObject resultObj;
	private UserLoginInfo loginInfo;
	private String vcodestr;
	private ILoginService loginService=null;
	private String newpass;
	private Object rObj;
	
	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public String index() {
		UserLoginInfo info = (UserLoginInfo) WebUtil._session("user");
		if (info != null) {			
			WebUtil._session("menucode", "0");
			WebUtil._session("submenucode", "0");
			return "logged_manager";
		} else {
			String vcode = VCodeBuilder.genSecureCode(4);
			WebUtil._session("vcode", vcode);
			return "login";
		}
	}
	
	public String first_page() {
		return SUCCESS;
	}
	
	public String error() {
		return SUCCESS;
	}
	
	public String login() {

		resultObj = new JSONObject();
		resultObj.put("result", "not_pass");

		UserLoginInfo info = (UserLoginInfo) WebUtil._session("user");

		if(info == null)
		{
			Administrator admin = null;
			String vcode = WebUtil._session("vcode").toString();
			if (vcode == null) {
				return SUCCESS;
			}
			
			if(vcode.equals(vcodestr))
			{
				try {
					admin = administratorService.findOneByUsercode(loginInfo.getUsercode());
					
					if ((admin != null)&&(admin.getPassword().equals(loginInfo.getPassword()))) {
						loginInfo.setId(admin.getId());
						loginInfo.setLevel(admin.getLevel());
						
						List<Menu> menuList = null;
						menuList = makeMenuList(admin.getId());
						
						if (menuList != null) {
							String resourceList = "";
							for (Menu menu : menuList) {
								resourceList += menu.getUrl() + ";";
							}

							WebUtil._session("menuList", menuList);
							WebUtil._session("resourceList", resourceList);
							WebUtil._session("user", loginInfo);
							
							WebUtil._session("menucode", 0);
							WebUtil._session("submenucode", 0);
							WebUtil._session("url", "first_page");
							
							resultObj.put("result", "pass");
						} else {
							resultObj.put("result", "no_authority");
						}
					}						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return SUCCESS;
	}

	private List<Menu> makeMenuList(int admin_id) {
		List<Menu> list = new ArrayList<Menu>();
		List<Authority> resourceList = null;
		Map<Integer, Authority> map = new HashMap<Integer, Authority>();
		List<Menu> menuList = new ArrayList<Menu>();
		
		try {
			
			list = menuService.findAll();
			if (loginInfo.getLevel() == Constants.SUPER_MANAGER)
				return list;

			resourceList = authorityService.getResourceList(admin_id);
			if (resourceList.size() == 0)
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		for (Authority auth : resourceList) {
			Authority a = map.get(auth.getMid());
			if (a == null) {
				map.put(auth.getMid(), auth);
			}
		}
		
		for (Menu menu : list) {
			Authority a = map.get(menu.getId());
			if (a != null) {
				menuList.add(menu);
			}
		}
		
		return menuList;
	}
	
	public String logout() {
		WebUtil._session("user", null);
		WebUtil._session("vcode", null);
		WebUtil._session("resourceList", null);
		return SUCCESS;
	}
	
	public String editpassword(){
		Administrator admin = (Administrator) ActionContext.getContext().getSession().get("admin");
		this.loginService.updatePass(newpass, admin.getUsercode());
		rObj = newpass;
		return "editpassword";
	}

	public AdministratorService getAdministratorService() {
		return administratorService;
	}

	public void setAdministratorService(
			AdministratorService administratorService) {
		this.administratorService = administratorService;
	}

	public UserLoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(UserLoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public AuthorityServiceImpl getAuthorityService() {
		return authorityService;
	}
	
	public void setAuthorityService(AuthorityServiceImpl service) {
		this.authorityService = service;
	}
	
	public String getVcodestr() {
		return vcodestr;
	}

	public void setVcodestr(String vcodestr) {
		this.vcodestr = vcodestr;
	}

	public MenuServiceImpl getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuServiceImpl menuService) {
		this.menuService = menuService;
	}

	public ILoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(ILoginService loginService) {
		this.loginService = loginService;
	}

	public String getNewpass() {
		return newpass;
	}

	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}

	public Object getrObj() {
		return rObj;
	}

	public void setrObj(Object rObj) {
		this.rObj = rObj;
	}
}
