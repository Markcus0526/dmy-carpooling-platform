package com.nmdy.customerManage.group.service;

import java.util.List;
import java.util.Map;

import com.nmdy.customerManage.group.dao.GroupNew;
import com.nmdy.customerManage.user.dao.User;


public interface GroupNewService {

	public List<Map<String, Object>> findGroupsByCondition(Map<String, Object> params);

	public  int findCountByCondition(Map<String, Object> params);

	public Map<String, Object> findGroupInfo(long id);

	public int updateGroupInfo(Map<String, Object> params);

	public int saveGroup(Map<String, Object> params);
	
	public Long insertGroupTS(Map<String, Object> params);
	

	public List<Map<String, Object>> findGroupDriversByCondition(Map<String, Object> params);

	public List<User> findGroupDriversByID(long id);

	public int findGroupDriversCountByID(Long id);
	
	public int findGroupDriversCountByCondition(Map<String, Object> params);

	public List<Map<String, Object>> findUsersByCondition(Map<String, Object> params);

	public Object findUsersCountByCondition(Map<String, Object> params);

	public int addDriverToGroup(Map<String, Object> params);

	public int removeDriverFromGroup(Map<String, Object> params);

	public int delGroup(long id);

	/**
	 * 根据id获得相关返利信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> findGroupRebateInfo(long id);

	/**
	 * 获得指定用户的返利信息列表
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> findGroupRebateListInfo(long id);

	/**
	 * 获得集团内所有用户的返利信息列表
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findGroupAllRebateListInfo(Map<String, Object> params);

	/**
	 * 设置集团下指定用户的分成比例
	 * @param params
	 */
	public int initGroupReallocateProfit(Map<String, Object> params);

	/**
	 * 设置集团下所有指定用户的分成比例为集团默认比例
	 * @param params
	 */
	public int initGroupReallocateProfitDefault(Map<String, Object> params);

	public List<Map<String, Object>> findAll();

	/*	public GroupNew findById(int user_id);

	public int updateTsid(Map<String, Object> map);
	public List<Group> findAll();*/
	
	public List<Map<String, Object>> findAllForUser();
	public int modifytsbal(Map<String, Object> params3);//通过后余额的更新
	public int seGrouptsid(Map<String, Object> params);//注册后返回注册时tsid
	public int findIsGroupid(String groupId);
	public int countGroupAllRebateListInfo(Map<String,Object> map);
	public int countMoneyAll(String groupId);
	public String countMoneyNumber(String groupId);
}
