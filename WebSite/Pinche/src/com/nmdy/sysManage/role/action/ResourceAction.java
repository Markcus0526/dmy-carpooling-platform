package com.nmdy.sysManage.role.action;

import java.util.List;

import com.nmdy.entity.Resource;
import com.nmdy.sysManage.role.service.ResourceService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.Constants;

/**
 * 资源管理ACTION
 * @author bisu
 *
 */
public class ResourceAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private ResourceService resourceService;
	private List<Resource> list;
	private int roleId;//角色ID
	private String roleIds;//角色IDS
	private String codes;//多个资源编号
	
	public String listPath(){
		this.setRoleId(roleId);
		return "listPath";
	}

	public String findResourceByRoleId(){
		System.out.println("roleId:"+roleId);
		list = resourceService.findAll(roleId);
//		list = resourceService.findResourceByRoleId(roleId);
		return "findResourceByRoleId";
	}
	
	public String updateResourceByRoleId(){
		this.resourceService.updateResourceForRoleId(roleId, codes);
		ActionContext.getContext().put("msg", Constants.UPDATE_SUCCESS);
		return "updateResourceByRoleId";
	}
	
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	public List<Resource> getList() {
		return list;
	}
	public void setList(List<Resource> list) {
		this.list = list;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}
}
