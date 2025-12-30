package com.pinche.customer.group.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.authority.manager.dao.Administrator;
import com.pinche.authority.manager.service.AdministratorService;
import com.pinche.customer.user.service.CustomerService;
import com.pinche.customer.group.dao.Group;
import com.pinche.customer.group.service.GroupService;

@SuppressWarnings("unused")
public class GroupAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Group> groupInfoList = null;
	private Group groupInfo = null;
	private GroupService groupService;
	private String method;
	private String id;
	
	private String module_path = "customer/group/";

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public GroupAction() {

	}

	public String index() {
		return SUCCESS;
	}

	public String getList() {

		try {
			groupInfoList = groupService.findAll();
		} catch (Exception e) {
			return ERROR;
		}

		return SUCCESS;
	}

	public String edit() {
		// for changing administrator information
		int affectedRows;
		HttpServletRequest request = ServletActionContext.getRequest();
		//String method = request.getParameter("method");

		request.setAttribute("menucode", "2");
		request.setAttribute("submenucode", "2");
		
		if (method == null || !method.equals("edit")) {
//			Integer id = Integer.valueOf(request.getParameter("id"));			
			request.setAttribute("content_url", module_path + "getEditGroupPageUrl?id=" + id);
			return "forward";
		}

		try {
			affectedRows = groupService.editGroup(groupInfo);
			if (affectedRows > 0) {
				return SUCCESS;
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String getEditGroupPageUrl() {
		HttpServletRequest request = ServletActionContext.getRequest();
		//Integer id = Integer.valueOf(request.getParameter("id"));
		
		try {
			groupInfo = groupService.findById(Integer.valueOf(id));
		} catch (Exception e) {
			return ERROR;
		}

		return SUCCESS;
	}
	public String delete()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		//String id = request.getParameter("id");
		if (id == null) {
			return ERROR;
		}
		
		try {
			int affectedRows = groupService.removeItem(Integer.valueOf(id));
			if (affectedRows > 0 )
				return SUCCESS;
			else
				return ERROR;
			
		} catch (Exception e) {
			return ERROR;
		}
	}
	public String add()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("content_url", module_path + "add");
		return SUCCESS;
	}
	public List<Group> getGroupInfoList() {
		return groupInfoList;
	}

	public Group getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(Group groupInfo) {
		this.groupInfo = groupInfo;
	}
}
