package com.nmdy.customerManage.groupself.service;

import java.util.List;
import java.util.Map;

import com.nmdy.customerManage.group.dao.GroupNew;
import com.nmdy.customerManage.user.dao.User;


public interface GroupSelfNewService {

	/*public List<GroupNew> findGroupsByCondition(Map<String, Object> params);*/

	/*public  int getCountByCondition(Map<String, Object> params);*/

	public Map<String, Object> findGroupInfo(long id);

	public void updateGroupInfo(Map<String, Object> params);

	public void saveGroup(Map<String, Object> params);

	public List<Map<String,Object>> findUsersByCondition(Map<String, Object> params);

	public int findUserCountByCondition(Map<String, Object> params);

	public void removeUserFromGroup(Map<String, Object> params);

	public int initReallocateProfit(Map<String, Object> params);

	public int initReallocateProfitDefault(Map<String, Object> params);

	/**
	 * 将指定用户的注册邀请码置空，表明没有任何人和集团邀请他和他产生返利关系
	 * @param id
	 */
	public void initInvitecodeRegistNull(long id);

}
