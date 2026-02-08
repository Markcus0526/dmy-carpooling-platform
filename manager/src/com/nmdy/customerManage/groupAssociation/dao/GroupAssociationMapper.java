package com.nmdy.customerManage.groupAssociation.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.customerManage.group.dao.GroupNew;

public interface GroupAssociationMapper extends SupperMapper{

	public List<Map<String, Object>> findGroupAssociationsByCondition(Map<String, Object> params);
	public int getGroupAssociationCountByCondition(Map<String, Object> params);
	public Map<String, Object> getGroupAssociationInfo(long id);
	public void updateGroupAssociationInfo(Map<String, Object> params);
	public void saveGroupAssociation(Map<String, Object> params);
	
	public List<Map<String, Object>> getGroupsByCondition(Map<String, Object> params);
	public int getGroupsCountByCondition(Map<String, Object> params);
	public void addMemberToGroupAssociation(Map<String, Object> params);
	public void removeMember(Map<String, Object> params);
	public List<Map<String, Object>> getGroupsInGA(Map<String, Object> params);
	public int getGroupsCountInGA(long id);
	public void delGroupAssociation(long id);
	


}
