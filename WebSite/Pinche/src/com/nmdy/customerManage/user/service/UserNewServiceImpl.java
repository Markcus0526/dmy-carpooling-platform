package com.nmdy.customerManage.user.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import util.excel.pojo.ExcelUser;

import com.nmdy.common.PushUtil;
import com.nmdy.common.STPushNotificationData;
import com.nmdy.customerManage.user.dao.PushInfo;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.customerManage.user.dao.UserNewMapper;

/*@Service
@Transactional*/
public class UserNewServiceImpl implements UserNewService {

	//@Resource
	private  UserNewMapper userNewMapper;
	
	
	
	
	
	public UserNewMapper getUserNewMapper() {
		return userNewMapper;
	}

	public void setUserNewMapper(UserNewMapper userNewMapper) {
		this.userNewMapper = userNewMapper;
	}

	public User findUserBaseInfo(Serializable id) {
		return userNewMapper.getUserBaseInfo(id);
	}

	public int findCountByCondition(Map<String, Object> params) {
		return userNewMapper.getCountByCondition(params);
	}

	public User findByID(Serializable id) {
		return userNewMapper.getUserByID(id);
	}

	public List<Map<String, Object>> findUsersByCondition(Map<String, Object> params) {
		return userNewMapper.findUsersByCondition(params);
	}

	public int updateUserBaseInfo(Map<String, Object> params) {
		return userNewMapper.updateUserBaseInfo(params);
		
	}

	public int updatePersonVerifiedInfo(Map<String, Object> params) {
		return userNewMapper.updatePersonVerifiedInfo(params);
	}

	public int updateDriverVerifiedInfo(Map<String, Object> params) {
		return userNewMapper.updateDriverVerifiedInfo(params);
	}
	public int updateDriverVerifiedInfo_UserCar(Map<String, Object> params) {
		return userNewMapper.updateDriverVerifiedInfo_UserCar(params);
	}

	public int updateRebateInfo(Map<String, Object> params) {
		return userNewMapper.updateRebateInfo(params);
		
	}

	/**
	 * 获取四个标签页的信息
	 */
	public Map<String, Object> findUserAllInfo(Serializable id) {
		return userNewMapper.getUserAllInfo(id);
	}

	/**
	 * 获取身份认证信息
	 */
	public Map<String, Object> findPersonVerifiedInfo(Serializable id) {
		
		return userNewMapper.getPersonVerifiedInfo(id);
	}
	/**
	 * 驳回身份验证
	 */
	public int personVerifiedRejected(long id) {
		int ret = userNewMapper.personVerifiedRejected(id);
		pushToUser(id, "【oo拼车】", "【oo拼车】乘客身份验证失败了。T_T", STPushNotificationData.PNOTIF_TYPE_USER_VERIFY_FAIL);
		return ret;
	}

	public int personVerified(Map<String, Object> params) {
		return userNewMapper.personVerified(params);
		
	}

	public int driverVerifiedRejected(long id) {
		int ret = userNewMapper.driverVerifiedRejected(id);
		pushToUser(id, "【oo拼车】", "【oo拼车】车主身份验证失败了。T_T", STPushNotificationData.PNOTIF_TYPE_DRIVER_VERIFY_FAIL);
		return ret;
	}

	public int driverVerified(Map<String, Object> params) {
		return userNewMapper.driverVerified(params);
	}

	public Map<String, Object> findDriverVerifiedInfo(long id) {
		return userNewMapper.getDriverVerifiedInfo(id);
	}

	public List<Map<String, Object>> findAllRebateListInfo(Map<String, Object> params) {
		return userNewMapper.getAllRebateListInfo(params);
	}

	@Override
	public List<Map<String, Object>> findRebateListInfo(long id) {
		return userNewMapper.getRebateListInfo(id);
	}

	@Override
	public User findOneById(int driver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateTsid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<User> searchNumberspread(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * 列出所有车辆品牌
	 */
	public List<Map<String, Object>> findCarBrand() {
		return userNewMapper.findCarBrand();
	}


	/**
	 * 根据品牌列出所有的车辆型号
	 */
	public List<Map<String, Object>> findCarStyle(Map<String, Object> params) {
		return userNewMapper.findCarStyle(params);
	}

	/**
	 * 根据车辆品牌和型号获得car_type表中的记录ID
	 * @param carMap
	 * @return
	 */
	public long findCarTypeID(Map<String, Object> carMap) {
		
		try {
			return userNewMapper.getCarTypeID(carMap);
		} catch (Exception e) {
			return 0;
		}
	}
	
   //推送
	public void pushToUser(long userId, String title, String description, int type){
		STPushNotificationData data = new STPushNotificationData();

		data.setTitle(title);
		data.setDescription(description);
		data.getCustom_content().setTypecode(type);
		
		List<PushInfo> listUserToken = userNewMapper.queryPushInfoByUserId(userId);
		for(PushInfo pushInfo: listUserToken){
			PushUtil.pushToMobile(pushInfo.getUserIdPush(), pushInfo.getChannelId(), 
					data, pushInfo.getPhoneSystem() == 2);
		}
	}

	public List<ExcelUser> findUsersByConditionExcel(Map<String,Object> params){
		return this.userNewMapper.findUsersByConditionExcel(params);
	}
}
