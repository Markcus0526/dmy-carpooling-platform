package com.nmdy.customerManage.groupself.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.customerManage.group.dao.GroupNew;
import com.nmdy.customerManage.user.dao.User;

public interface GroupSelfNewMapper extends SupperMapper{

	/**
	 * 按照条件查询并分页
	 * @param params
	 * @return
	 */
	//public List<GroupNew> findGroupsByCondition(Map<String, Object> params);
	/**
	 * 按照条件查询总记录数
	 * @param params
	 * @return
	 */
	//public  int getGroupCountByCondition(Map<String, Object> params);
	
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
	public void updateGroupInfo(Map<String, Object> params);
	/**
	 * 新增集团客户
	 * @param params
	 */
	public void saveGroup(Map<String, Object> params);
	/**
	 * 获得指定集团旗下的用户列表
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getUsersByCondition(Map<String, Object> params);
	/**
	 * 获得指定集团旗下的用户数
	 * @param params
	 * @return
	 */
	public int getUserCountByCondition(Map<String, Object> params);
	
	/**
	 * 删除集团下指定用户
	 * @param params
	 */
	public void removeUserFromGroup(Map<String, Object> params);
	
	/**
	 * 更改用户的分成比例
	 * @param params
	 */
	public int setReallocateProfit(Map<String, Object> params);
	
	/**
	 * 更改用户的分成比例为默认值
	 * @param params
	 */
	public int setReallocateProfitDefault(Map<String, Object> params);
	
	/**
	 * 将指定用户的注册邀请码置空，表明没有任何人或集团与他产生返利关系
	 * @param id
	 */
	public void setInvitecodeRegistNull(long id);
	
	
}
