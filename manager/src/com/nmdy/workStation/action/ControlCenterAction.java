package com.nmdy.workStation.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nmdy.entity.UserOnline;
import com.nmdy.workStation.service.ControlCenterService;
import com.opensymphony.xwork2.ActionSupport;
   /**
    * user_online 实体类
    * Tue Nov 18 11:08:09 CST 2014 bisu
    */ 


public class ControlCenterAction extends ActionSupport{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Long id;
private UserOnline userOnline;
private List<UserOnline> list;
private ControlCenterService controlCenterService;
private Map<String, Object> map = new HashMap<String,Object>();
private static final String beijingCode = "11";
private static final String shanghaiCode = "13";
private static final String guangdongCode = "4601";
private static final String shengzhengCode = "4603";
private int cityType;
private int allNum;
private int onlineNum;
private int unlineNum;
private Map<String,Object> jsonMap = new HashMap<String,Object>();
private boolean online;
private boolean unline;


public String list(){
	Map<String,Object> map = new HashMap<String,Object>();
	switch (cityType) {
	case 0:map.put("code", beijingCode);break;
	case 1:map.put("code", shanghaiCode);break;
	case 2:map.put("code", guangdongCode);break;
	case 3:map.put("code", shengzhengCode);break;
	default:map.put("code", beijingCode);break;
	}
	int second = this.controlCenterService.selectSecond();
	map.put("second", second);
	allNum = this.controlCenterService.countAll(map);
	onlineNum = this.controlCenterService.countOnlineAll(map);
	unlineNum = this.controlCenterService.countUnlineAll(map);
	return "list";
}

public String selectAll(){
	Map<String,Object> map = new HashMap<String,Object>();
	switch (cityType) {
	case 0:map.put("code", beijingCode);break;
	case 1:map.put("code", shanghaiCode);break;
	case 2:map.put("code", guangdongCode);break;
	case 3:map.put("code", shengzhengCode);break;
	default:map.put("code", beijingCode);break;
	}
	int second = this.controlCenterService.selectSecond();
	map.put("second", second);
	if(online&&unline){
		list = this.controlCenterService.selectOnlineAll(map);
		List<UserOnline> listUnline = this.controlCenterService.selectUnLineAll(map);
		list.addAll(listUnline);
	}
	if(online&&!unline){
		list = this.controlCenterService.selectOnlineAll(map);
	}
	if(unline&&!online){
		list = this.controlCenterService.selectUnLineAll(map);
	}
	return "selectAll";
}

public String findById(){
	userOnline=controlCenterService.findById(id);
return "findById";
}

public String insertControlCenter(){
	this.controlCenterService.insertControlCenter(userOnline);
return "insertControlCenter";
}

public String updateControlCenter(){
	this.controlCenterService.updateControlCenter(userOnline);
return "updateControlCenter";
}

public String deleteControlCenter(){
	this.controlCenterService.deleteControlCenter(id);
return "deleteControlCenter";
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public UserOnline getUserOnline() {
	return userOnline;
}

public void setUserOnline(UserOnline userOnline) {
	this.userOnline = userOnline;
}

public List<UserOnline> getList() {
	return list;
}

public void setList(List<UserOnline> list) {
	this.list = list;
}

public ControlCenterService getControlCenterService() {
	return controlCenterService;
}

public void setControlCenterService(ControlCenterService controlCenterService) {
	this.controlCenterService = controlCenterService;
}

public Map<String, Object> getMap() {
	return map;
}

public void setMap(Map<String, Object> map) {
	this.map = map;
}

public int getCityType() {
	return cityType;
}

public void setCityType(int cityType) {
	this.cityType = cityType;
}

public int getAllNum() {
	return allNum;
}

public void setAllNum(int allNum) {
	this.allNum = allNum;
}

public int getOnlineNum() {
	return onlineNum;
}

public void setOnlineNum(int onlineNum) {
	this.onlineNum = onlineNum;
}

public int getUnlineNum() {
	return unlineNum;
}

public void setUnlineNum(int unlineNum) {
	this.unlineNum = unlineNum;
}

public Map<String, Object> getJsonMap() {
	return jsonMap;
}

public void setJsonMap(Map<String, Object> jsonMap) {
	this.jsonMap = jsonMap;
}

public boolean isOnline() {
	return online;
}

public void setOnline(boolean online) {
	this.online = online;
}

public boolean isUnline() {
	return unline;
}

public void setUnline(boolean unline) {
	this.unline = unline;
}
}

