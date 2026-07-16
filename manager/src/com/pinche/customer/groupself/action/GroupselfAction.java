package com.pinche.customer.groupself.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.customer.groupself.dao.Groupself;
import com.pinche.customer.groupself.service.GroupselfService;

public class GroupselfAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GroupselfService groupselfService;
	private List<Groupself> groupList = null;
	private Groupself group = null;
	
	public Groupself getGroup() {
		return group;
	}

	public void setGroup(Groupself group) {
		this.group = group;
	}

	private String module_path = "customer/groupself/";

	public GroupselfAction() {		
	}
	
	public String index() {
		return SUCCESS;
	}	
	
	public List<Groupself> getGroupselfList() {
		return groupList;
	}

	public GroupselfService getGroupselfService() {
		return groupselfService;
	}

	public void setGroupselfService(GroupselfService groupselfService) {
		this.groupselfService = groupselfService;
	}	
}
