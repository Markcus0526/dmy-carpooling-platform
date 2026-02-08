package com.nmdy.workStation.action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.nmdy.customerManage.user.dao.User;
import com.nmdy.entity.ClientAccount;
import com.nmdy.workStation.service.WorkStationService;
import com.opensymphony.xwork2.ActionSupport;
   /**
    * req_client_account 实体类
    * Tue Nov 11 16:33:25 CST 2014 bisu
    */ 


public class WorkStationAction extends ActionSupport{
/**
	 * 
	 */
private static final long serialVersionUID = 1L;
private Long id;
private ClientAccount clientAccount;
private List<ClientAccount> list;
private List<User> listUser;
private WorkStationService workStationService;
private Map<String, Object> map = new HashMap<String,Object>();
private Map<String, Object> jsonMap = new HashMap<String,Object>();
private Integer page;
private Integer rows;
private Integer total_size;


public String list(){
	String path = ServletActionContext.getServletContext().getRealPath("/upload");
	System.out.println("page:"+path);
	workStationService.doPic(path);
	return "list";
}

public String selectAll(){
	Map<String, Object> map = new HashMap<String,Object>();
	map.put("page", (page-1)*rows);
	map.put("rows", rows);
	map.put("status", 1);
	list=workStationService.selectAll(map);
	total_size = workStationService.countAll(map);
	jsonMap.put("total",total_size);
	jsonMap.put("rows",list);
	return SUCCESS;
}

public String selectAllUser(){
	Map<String, Object> map = new HashMap<String,Object>();
	map.put("page", (page-1)*rows);
	map.put("rows", rows);
	map.put("status", 1);
	listUser=workStationService.selectAllUser(map);
	total_size = workStationService.countAllUser(map);
	jsonMap.put("total",total_size);
	jsonMap.put("rows",listUser);
	return SUCCESS;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public ClientAccount getClientAccount() {
	return clientAccount;
}

public void setClientAccount(ClientAccount clientAccount) {
	this.clientAccount = clientAccount;
}

public List<ClientAccount> getList() {
	return list;
}

public void setList(List<ClientAccount> list) {
	this.list = list;
}

public WorkStationService getWorkStationService() {
	return workStationService;
}

public void setWorkStationService(WorkStationService workStationService) {
	this.workStationService = workStationService;
}

public Map<String, Object> getMap() {
	return map;
}

public void setMap(Map<String, Object> map) {
	this.map = map;
}

public Integer getPage() {
	return page;
}

public void setPage(Integer page) {
	this.page = page;
}

public Integer getRows() {
	return rows;
}

public void setRows(Integer rows) {
	this.rows = rows;
}

public Integer getTotal_size() {
	return total_size;
}

public void setTotal_size(Integer total_size) {
	this.total_size = total_size;
}

public Map<String, Object> getJsonMap() {
	return jsonMap;
}

public void setJsonMap(Map<String, Object> jsonMap) {
	this.jsonMap = jsonMap;
}

public List<User> getListUser() {
	return listUser;
}

public void setListUser(List<User> listUser) {
	this.listUser = listUser;
}
}

