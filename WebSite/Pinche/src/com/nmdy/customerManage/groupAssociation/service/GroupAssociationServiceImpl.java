package com.nmdy.customerManage.groupAssociation.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nmdy.customerManage.group.dao.GroupNew;
import com.nmdy.customerManage.groupAssociation.dao.GroupAssociation;
import com.nmdy.customerManage.groupAssociation.dao.GroupAssociationMapper;


/*@Service
@Transactional*/
public class GroupAssociationServiceImpl implements GroupAssociationService {

	//@Resource
	private GroupAssociationMapper groupAssociationMapper;
	
	
	
	
	
	public GroupAssociationMapper getGroupAssociationMapper() {
		return groupAssociationMapper;
	}

	public void setGroupAssociationMapper(
			GroupAssociationMapper groupAssociationMapper) {
		this.groupAssociationMapper = groupAssociationMapper;
	}

	public List<Map<String, Object>> findGroupAssociationsByCondition(Map<String, Object> params) {
		return groupAssociationMapper.findGroupAssociationsByCondition(params);
	}

	public int findCountByCondition(Map<String, Object> params) {
		return groupAssociationMapper.getGroupAssociationCountByCondition(params);
	}

	public Map<String, Object> findGroupAssociationInfo(long id) {
		return groupAssociationMapper.getGroupAssociationInfo(id);
	}

	public void updateGroupAssociationInfo(Map<String, Object> params) {
		groupAssociationMapper.updateGroupAssociationInfo(params);
		
	}

	public void saveGroupAssociation(Map<String, Object> params) {
		groupAssociationMapper.saveGroupAssociation(params);
	}

	public List<Map<String, Object>> findGroupsByCondition(Map<String, Object> params) {
		return groupAssociationMapper.getGroupsByCondition(params);
		
	}

	public int findGroupsCountByCondition(Map<String, Object> params) {
		
		return groupAssociationMapper.getGroupsCountByCondition(params);
	}

	public void addMemberToGroupAssociation(Map<String, Object> params) {
		groupAssociationMapper.addMemberToGroupAssociation(params);
	}

	public void removeMember(Map<String, Object> params) {
		groupAssociationMapper.removeMember(params);
		
	}

	public List<Map<String, Object>> findGroupsInGA(Map<String, Object> params) {
		return groupAssociationMapper.getGroupsInGA(params);
	}

	public int findGroupsCountInGA(long id) {
		return groupAssociationMapper.getGroupsCountInGA(id);
	}

	public void delGroupAssociation(long id) {
		
		groupAssociationMapper.delGroupAssociation(id);
	}



}
