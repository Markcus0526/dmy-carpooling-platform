package com.pinche.authority.role.dao;

import java.util.List;
import java.util.Map;

public interface RoleitemMapper {
	public List<Roleitem> findAllByRid(int rid);
	public Roleitem findOneById(int id);
	
	public int addItems(Map<String, Object> map);
	public int deleteItems(int rid);
	public int editItem(Roleitem item);
}
