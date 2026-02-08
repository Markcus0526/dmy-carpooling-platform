package com.pinche.customer.group.dao;
import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;

public interface GroupMapper extends SupperMapper{
	public List<Group> findAll();
	public Group findById(int id);
	public int addItem(Group item);
	public int removeItem(int id);
	public int editGroup(Group info);
	public int updateTsid(Map<String, Object> map);
	public List<Group> findRoleGroup(int roleId);
	public int deleteGroupByRoleId(int roleId);
	public int addGroupBatch(List<Map<String,Object>> list);
	public int findMaxId();
}
