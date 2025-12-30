package com.pinche.customer.group.service;

import java.util.List;
import java.util.Map;

import com.pinche.customer.group.dao.Group;

public interface GroupService {
	public List<Group> findAll();
	public Group findById(int id);
	public int addItem(Group item);
	public int removeItem(int id);
	public int editGroup(Group info);
	public int updateTsid(Map<String, Object> map);
	public List<Group> findRoleGroup(int roleId);
	public List<Group> findRoleCheckedGroup(int roleId);
	public int updateRoleGroup(int roleId,String groupIds);
	public int findMaxId();
}
