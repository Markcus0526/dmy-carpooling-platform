package com.nmdy.customerManage.groupself.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nmdy.customerManage.group.dao.GroupNew;
import com.nmdy.customerManage.groupself.dao.GroupSelfNewMapper;
import com.nmdy.customerManage.user.dao.User;


/*@Service
@Transactional*/
public class GroupSelfNewServiceImpl implements GroupSelfNewService {

	//@Resource
	private GroupSelfNewMapper groupSelfNewMapper;
	
	
	
	
	public GroupSelfNewMapper getGroupSelfNewMapper() {
		return groupSelfNewMapper;
	}

	public void setGroupSelfNewMapper(GroupSelfNewMapper groupSelfNewMapper) {
		this.groupSelfNewMapper = groupSelfNewMapper;
	}

/*	public List<GroupNew> findGroupsByCondition(Map<String, Object> params) {
		return groupSelfNewMapper.findGroupsByCondition(params);
	}

	public int getCountByCondition(Map<String, Object> params) {
		return groupSelfNewMapper.getGroupCountByCondition(params);
	}*/

	public Map<String, Object> findGroupInfo(long id) {
		return groupSelfNewMapper.getGroupInfo(id);
	}

	public void updateGroupInfo(Map<String, Object> params) {
		groupSelfNewMapper.updateGroupInfo(params);
		
	}

	public void saveGroup(Map<String, Object> params) {
		groupSelfNewMapper.saveGroup(params);
	}

	public List<Map<String,Object>> findUsersByCondition(Map<String, Object> params) {
		return groupSelfNewMapper.getUsersByCondition(params);
	}

	public int findUserCountByCondition(Map<String, Object> params) {
		return groupSelfNewMapper.getUserCountByCondition(params);
	}

	public void removeUserFromGroup(Map<String, Object> params) {
			groupSelfNewMapper.removeUserFromGroup(params);
	}

	public int initReallocateProfit(Map<String, Object> params) {
			return groupSelfNewMapper.setReallocateProfit(params);
	}

	public int initReallocateProfitDefault(Map<String, Object> params) {
			return groupSelfNewMapper.setReallocateProfitDefault(params);
	}

	public void initInvitecodeRegistNull(long id) {
		groupSelfNewMapper.setInvitecodeRegistNull(id);
	}

}
