package com.pinche.customer.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.pinche.common.action.SecureAction;
import com.pinche.customer.user.dao.Customer;
import com.pinche.customer.user.service.CustomerServiceImpl;

public class UserAction extends SecureAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CustomerServiceImpl customerService;
	
	private List<Customer> userList = null;
	private Customer userInfo = null;
	private String searchColumn;
	private String searchValue;	
	private String id;
	private String method;
	
	private String module_path = "customer/user/";

	public String index() {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		request.setAttribute("menucode", "2");
		request.setAttribute("submenucode", "1");
		request.setAttribute("content_url", module_path + "getListAction");

		return SUCCESS;
	}
	
	public String getListAction() {
		try {
			userList = customerService.findAll();
		} catch (Exception e) {
			return ERROR;
		}

		return SUCCESS;
	}
	
	public List<Customer> getUserList() {
		return userList;
	}

	public Customer getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Customer userInfo) {
		this.userInfo = userInfo;
	}

	public String getSearchColumn() {
		return searchColumn;
	}

	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getModule_path() {
		return module_path;
	}

	public void setModule_path(String module_path) {
		this.module_path = module_path;
	}

	public CustomerServiceImpl getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerServiceImpl customerService) {
		this.customerService = customerService;
	}
}
