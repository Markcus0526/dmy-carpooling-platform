package com.pinche.authority.role.service;

import java.util.List;
import java.util.Map;

import com.pinche.authority.role.dao.Roleitem;

public interface RoleitemService {
	public List<Roleitem> findAllByRid(int rid);
	public Roleitem findOneById(int id);
	
	public int addItems(Map<String, Object> map);
	public int deleteItems(int rid);
	public int editItem(Roleitem item);
}
