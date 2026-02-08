package com.pinche.authority.menu.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.pinche.authority.menu.dao.Menu;
import com.pinche.authority.menu.service.MenuServiceImpl;
import com.pinche.common.WebUtil;
import com.pinche.common.action.SecureAction;

public class MenuAction extends SecureAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MenuServiceImpl menuService;
	private String menucode;
	private String submenucode;
	private String url;
	
	private JSONObject resultObj = null;

	//get menu list from logged on user role
	@SuppressWarnings("unchecked")
	public String list() {
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("application/json");

		try {
//			menulist = menuService.findAll();//ex:findAllByRole(64);
			List<Menu> menulist = ((List<Menu>) WebUtil._session("menuList"));
			if (menulist == null) {
				resultObj = null;
			}
			
			ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
			for (Menu info : menulist) {
				Map<String, Object> m = new HashMap<String, Object>();

				m.put("id", info.getId());
				m.put("name", info.getName());
				m.put("url", info.getUrl());
				m.put("menucode", info.getMenucode());
				m.put("submenu", info.getSubmenu());
				m.put("parent", info.getParent());
				al.add(m);
			}
			
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("total", menulist.size());
			json.put("rows", al);
			resultObj = JSONObject.fromObject(json);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		return SUCCESS;
	}

	public String setPageParam() {
		WebUtil._setResponseContentType("application/json");
		WebUtil._session("menucode", Integer.valueOf(menucode));
		WebUtil._session("submenucode", Integer.valueOf(submenucode));
		WebUtil._session("url", url);
		
		return SUCCESS;
	}
	
	public MenuServiceImpl getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuServiceImpl menuService) {
		this.menuService = menuService;
	}

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public String getMenucode() {
		return menucode;
	}

	public void setMenucode(String menucode) {
		this.menucode = menucode;
	}

	public String getSubmenucode() {
		return submenucode;
	}

	public void setSubmenucode(String submenucode) {
		this.submenucode = submenucode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
