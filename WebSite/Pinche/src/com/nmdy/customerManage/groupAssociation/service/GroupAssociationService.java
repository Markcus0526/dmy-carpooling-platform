package com.nmdy.customerManage.groupAssociation.service;

import java.util.List;
import java.util.Map;

import com.nmdy.customerManage.group.dao.GroupNew;
import com.nmdy.customerManage.groupAssociation.dao.GroupAssociation;



public interface GroupAssociationService {

	public List<Map<String, Object>> findGroupAssociationsByCondition(Map<String, Object> params);

	public  int findCountByCondition(Map<String, Object> params);

	public Map<String, Object> findGroupAssociationInfo(long id);

	public void updateGroupAssociationInfo(Map<String, Object> params);

	public void saveGroupAssociation(Map<String, Object> params);

	public List<Map<String, Object>> findGroupsByCondition(Map<String, Object> params);

	public int findGroupsCountByCondition(Map<String, Object> params);

	public void addMemberToGroupAssociation(Map<String, Object> params);

	public void removeMember(Map<String, Object> params);

	public List<Map<String, Object>> findGroupsInGA(Map<String, Object> params);

	public int findGroupsCountInGA(long id);

	public void delGroupAssociation(long id);
	

}
