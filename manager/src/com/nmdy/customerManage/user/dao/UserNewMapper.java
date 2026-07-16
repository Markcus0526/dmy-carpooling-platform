package com.nmdy.customerManage.user.dao;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import util.excel.pojo.ExcelUser;

import com.nmdy.base.SupperMapper;
import com.nmdy.customerManage.user.dao.PushInfo;




public interface UserNewMapper extends SupperMapper{
	
	
	/**
	 * 根据条件查询
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> findUsersByCondition(Map<String, Object> params);
	/**
	 * 根据条件查询
	 * @param condition
	 * @return
	 */
	public List<User> findUsersByConditionForGame(Map<String, Object> params);
	/**
	 * 根据批量的玩家编号查询
	 * @param listIds
	 * @return
	 */
	public List<User> listByBatchUserIds(List<Long> listIds);
	/**
	 * 获得符合查询条件的总记录数
	 * @param params
	 * @return
	 */
	public int getCountByCondition(Map<String, Object> params);
	/**
	 * 根据ID获得User对象
	 * @param id
	 * @return
	 */
	public  User getUserByID(Serializable id);
	
	
	/**
	 * 显示用户所有信息，四个tab页的内容都查询出来，再同时加载到页面
	 * @param params
	 * @return
	 */
	public Map<String,Object> getUserAllInfo(Serializable id);
	
	/**
	 * 显示用户基本信息
	 * @param id
	 */
	public User getUserBaseInfo(Serializable id);
	
	
	/**
	 * 显示身份认证信息
	 * @param id
	 */
	public Map<String, Object> getPersonVerifiedInfo(Serializable id);
	
	
	/**
	 * 显示车主认证信息
	 * @param id
	 */
	public Map<String, Object> getDriverVerifiedInfo(Long id);
	
	
	/**
	 *显示返利信息
	 * @param id
	 */
	public User getRebateInfo(Long id);
	
	
	/**
	 * 修改用户基本信息
	 * @param user
	 */
	public int updateUserBaseInfo(Map<String, Object> params);
	
	
	/**
	 * 修改身份认证信息
	 * @param user
	 */
	public int updatePersonVerifiedInfo(Map<String, Object> params);
	
	
	/**
	 * 修改车主认证信息
	 * @param user
	 */
	public int updateDriverVerifiedInfo(Map<String, Object> params);
	
	/**
	 * 修改车主认证信息(user_car表中的数据)
	 * @param user
	 */
	public int updateDriverVerifiedInfo_UserCar(Map<String, Object> params);
	
	
	/**
	 * 修改返利信息
	 * @param user
	 */
	public int updateRebateInfo(Map<String, Object> params);
	
	
	/**
	 * 个人身份认证
	 * @param user
	 */
	public int personVerified(Map<String, Object> params);
	
	/**
	 * 驳回个人身份认证申请
	 * @param user
	 */
	public int personVerifiedRejected(Long id);
	
	
	/**
	 * 车主身份认证
	 * @param user
	 */
	public int driverVerified(Map<String, Object> params);
	
	/**
	 * 驳回车主认证申请
	 * @param user
	 */
	public int driverVerifiedRejected(Long id);
	
	
	/**
	 * 根据用户id查看身份认证图片
	 * @param id
	 * @return
	 */
	public InputStream showPersonVerifiedImg(Long id);
	
	/**
	 * 根据用户id查看车主认证图片
	 * @param id
	 * @return
	 */
	public InputStream showDriverVerifiedImg(Long id);
	
	/**
	 * 根据记录的id导出到Excel
	 * @param id
	 * @return
	 */
	public InputStream importToExcel(Long ... id);
	/**
	 * 根据邀请码获得使用该邀请码注册的所有用户的返利信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getAllRebateListInfo(Map<String, Object> params);
	/**
	 * 根据指定id查出该用户的返利信息列表
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getRebateListInfo(long id);
	
	/**
	 * 列出所有的车辆品牌
	 * @return
	 */
	public List<Map<String, Object>> findCarBrand();
	
	/**
	 * 根据品牌列出所有的型号
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findCarStyle(Map<String, Object> params);
	
	/**
	 * 根据车辆品牌和型号获得car_type表中的记录ID
	 * @param carMap
	 * @return
	 */
	public long getCarTypeID(Map<String, Object> carMap);
	
	public long countCityUserByTime(Map<String, Object> map);
	
	/**
	 * 根据帐号查找用户
	 * @param usercode
	 * @return
	 */
	public User findByCode(String usercode);
	public User findById(long id);
	public List<ExcelUser> findUsersByConditionExcel(Map<String,Object> params);
	
	//查询推送id
	public List<PushInfo> queryPushInfoByUserId(long userId);
	
	
}
