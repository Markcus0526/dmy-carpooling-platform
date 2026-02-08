package com.nmdy.customerManage.group.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.customerManage.user.dao.User;
public interface GroupNewMapper extends SupperMapper{

	public List<Map<String, Object>> findAllForUser();
	
	public List<Map<String, Object>> findAll();
	/**
	 * 按照条件查询并分页
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findGroupsByCondition(Map<String, Object> params);
	/**
	 * 按照条件查询总记录数
	 * @param params
	 * @return
	 */
	public  int getGroupCountByCondition(Map<String, Object> params);
	
	/**
	 * 根据指定id获得相应信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> getGroupInfo(long id);
	
	/**
	 * 更新集团客户信息
	 * @param params
	 */
	public int updateGroupInfo(Map<String, Object> params);
	/**
	 * 新增集团客户
	 * @param params
	 */
	public int saveGroup(Map<String, Object> params);
	
	/**
	 * 新建集团同时向ts表中插入一条记录，以便财务模块查询使用
	 * @param params
	 * @return
	 */
	public Long insertGroupTS(Map<String, Object> params);
	
	/**
	 * 获得指定集团旗下符合条件的用户（车主）
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getGroupDriversByCondition(Map<String, Object> params);
	
	/**
	 * 获得指定集团旗下符合条件的车主用户数量
	 * @param params
	 * @return
	 */
	public int getGroupDriversCountByCondition(Map<String, Object> params);
	
	/**
	 * 获取指定集团旗下的用户（车主）
	 * @param id
	 * @return
	 */
	public List<User> getGroupDriversById(Long id);
	
	/**
	 * 获得指定集团旗下的车主用户数量
	 * @param id
	 * @return
	 */
	public int getGroupDriversCountById(Long id);
	
	/**
	 * 根据条件获得除指定集团外的车主用户
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getUsersByCondition(Map<String, Object> params);
	
	/**
	 * 根据条件获得除指定集团外的车主用户数量
	 * @param params
	 * @return
	 */
	public int getUsersCountByCondition(Map<String, Object> params);
	/**
	 * 添加非集团车主用户到指定集团
	 * @param params
	 */
	public int addDriverToGroup(Map<String, Object> params);
	
	/**
	 * 将指定车主从集团中移除
	 * @param params
	 */
	public int removeDriverFromGroup(Map<String, Object> params);
	
	/**
	 * 删除集团客户
	 * @param id
	 */
	public int delGroup(long id);
	
	/**
	 * 获得集团返利信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> getGroupRebateInfo(long id);
	
	/**
	 * 获得指定用户的返利信息列表
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getGroupRebateListInfo(long id);
	
	/**
	 * 获得集团内所有用户的返利信息列表
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getGroupAllRebateListInfo(Map<String, Object> params);
	
	/**
	 * 将集团下指定用户的分成比例设置为集团默认的比例
	 * @param params
	 */
	public int setGroupReallocateProfitDefault(Map<String, Object> params);
	
	/**
	 * 设置集团下指定用户的分成比例
	 * @param params
	 */
	public int setGroupReallocateProfit(Map<String, Object> params);
	public int modifytsbal(Map<String, Object> params3);//通过后余额的更新
	public int seGrouptsid(Map<String, Object> params);//注册后返回注册时tsid
	public int findIsGroupid(String groupId);
	public int countGroupAllRebateListInfo(Map<String,Object> map);
	
	public int countMoneyAll(String groupId);
	public String countMoneyNumber(String groupId);
}
