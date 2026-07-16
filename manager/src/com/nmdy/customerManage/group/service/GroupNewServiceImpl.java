package com.nmdy.customerManage.group.service;

import java.util.List;
import java.util.Map;

import com.nmdy.customerManage.group.dao.GroupNewMapper;
import com.nmdy.customerManage.user.dao.User;



/*@Service
@Transactional*/
public class GroupNewServiceImpl implements GroupNewService {

	//@Resource
	private GroupNewMapper groupNewMapper;

	
	
	public GroupNewMapper getGroupNewMapper() {
		return groupNewMapper;
	}

	public void setGroupNewMapper(GroupNewMapper groupNewMapper) {
		this.groupNewMapper = groupNewMapper;
	}

	public List<Map<String, Object>> findGroupsByCondition(Map<String, Object> params) {
		return groupNewMapper.findGroupsByCondition(params);
	}

	public int findCountByCondition(Map<String, Object> params) {
		return groupNewMapper.getGroupCountByCondition(params);
	}

	public Map<String, Object> findGroupInfo(long id) {
		return groupNewMapper.getGroupInfo(id);
	}

	public int updateGroupInfo(Map<String, Object> params) {
		return groupNewMapper.updateGroupInfo(params);
		
	}

	public int saveGroup(Map<String, Object> params) {
		return groupNewMapper.saveGroup(params);
	}

	public List<Map<String, Object>> findGroupDriversByCondition(Map<String, Object> params) {
		return groupNewMapper.getGroupDriversByCondition(params);
	}

	public List<User> findgroupDriversByID(long id) {
		return groupNewMapper.getGroupDriversById(id);
	}

	public int findGroupDriversCountByID(Long id) {
		return groupNewMapper.getGroupDriversCountById(id);
	}

	public List<User> groupDriversByCondition(Map<String, Object> params) {
		return groupDriversByCondition(params);
	}

	public int findGroupDriversCountByCondition(Map<String, Object> params) {
		return groupNewMapper.getGroupDriversCountByCondition(params);
	}

	public List<Map<String, Object>> findUsersByCondition(Map<String, Object> params) {
		return groupNewMapper.getUsersByCondition(params);
	}

	public Object findUsersCountByCondition(Map<String, Object> params) {
		return groupNewMapper.getUsersCountByCondition(params);
	}

	public int addDriverToGroup(Map<String, Object> params) {
		return groupNewMapper.addDriverToGroup(params);
		
	}

	public int removeDriverFromGroup(Map<String, Object> params) {
		return groupNewMapper.removeDriverFromGroup( params);
	}

	public int delGroup(long id) {
		return groupNewMapper.delGroup(id);
	}

	public Map<String, Object> findGroupRebateInfo(long id) {
		return groupNewMapper.getGroupRebateInfo(id);
	}

	public List<Map<String, Object>> findGroupRebateListInfo(long id) {
		return groupNewMapper.getGroupRebateListInfo(id);
	}

	public List<Map<String, Object>> findGroupAllRebateListInfo(Map<String, Object> params) {
		return groupNewMapper.getGroupAllRebateListInfo(params);
	}

	public int initGroupReallocateProfit(Map<String, Object> params) {
		return groupNewMapper.setGroupReallocateProfit(params);	
	}

	public int initGroupReallocateProfitDefault(Map<String, Object> params) {
		return groupNewMapper.setGroupReallocateProfitDefault(params);
	}

	public List<Map<String, Object>> findAll() {
		return groupNewMapper.findAll();
	}

	@Override
	public Long insertGroupTS(Map<String, Object> params) {
		
		return groupNewMapper.insertGroupTS(params);
	}

	/*	@Override
	public GroupNew findById(int user_id) {
		return null;
	}

	@Override
	public int updateTsid(Map<String, Object> map) {
		return 0;
	}

	public List<Group> findAll() {
		return groupNewMapper.findAll();
	}
*/
	public List<Map<String, Object>> findAllForUser(){
		return groupNewMapper.findAllForUser();
	}

	@Override
	public int modifytsbal(Map<String, Object> params3) {
		// TODO Auto-generated method stub
		
		return groupNewMapper.modifytsbal(params3);
	}

	@Override
	public int seGrouptsid(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return groupNewMapper.seGrouptsid(params);
	}

	@Override
	public List<User> findGroupDriversByID(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int findIsGroupid(String groupId){
		return this.groupNewMapper.findIsGroupid(groupId);
	}
	
	public int countGroupAllRebateListInfo(Map<String,Object> map){
		return this.groupNewMapper.countGroupAllRebateListInfo(map);
	}
	
	public int countMoneyAll(String groupId){
		return this.groupNewMapper.countMoneyAll(groupId);
	}
	
	public String countMoneyNumber(String groupId){
		return this.groupNewMapper.countMoneyNumber(groupId);
	}
}
