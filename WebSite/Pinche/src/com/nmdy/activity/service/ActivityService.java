package com.nmdy.activity.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nmdy.customerManage.user.dao.User;
import com.nmdy.dto.ActivityDto;
import com.nmdy.entity.Activity;
import com.nmdy.entity.ActivityRule;
import com.nmdy.entity.ActivitySyscoupon;
import com.nmdy.entity.ActivityUser;

public interface ActivityService {
	public Activity findById(long id);
	public List<Activity> listALl(Map<String,Object> map);
	public int countListALl(Map<String,Object> map);
	public List<ActivityRule> listActivityRole(long id);
	public int insertActivity(Activity activity);
	public int udpateActivityStop(long id);
	/**
	 * 删除活动规则
	 * @param id
	 * @return
	 */
	public int deleteActivityRule(long id);
	/**
	 * 修改活动
	 * @param activity
	 * @return
	 */
	public int updateActivity(Activity activity);
	/**
	 * 插入活动规则
	 * @param activityRule
	 * @return
	 */
	public int insertActivityRule(ActivityRule activityRule);
	/**
	 * 批量插入活动奖券
	 * @param list
	 * @return
	 */
	public int insertBatchActivityCoupon(List<ActivitySyscoupon> list);
	/**
	 * 删除活动所有的奖券
	 * @param id
	 * @return
	 */
	public int deleteCouponByActivityId(long id);
	public User findByCode(String usercode);
	public Activity findActivity();
	/**
	 * 查找活动前三位获得奖品的用户
	 * @param id
	 * @return
	 */
	public List<ActivityUser> findActivityUser(long faqiId);
	/**
	 * 查找用户的蒋品
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> findMyGift(long userid);
	public long insertUserActivityReturnId(ActivityDto activityDto);
	public int countUserActivity(long faqiId);
	public int countAllUserActivity(long userId);
	/**
	 * 注岫
	 * @param activityDto
	 * @return
	 */
	public long insertUserForReturnId(ActivityDto activityDto);
	/**
	 * 前台插入获奖用户
	 * @param activityDto
	 * @return
	 */
	public long insertActivityHuojiangUser(ActivityDto activityDto);
	public int insertTip(ActivityDto activityDto);
	/**
	 * 修改用户对应的活动状态为停止
	 * @param faqiId
	 * @return
	 */
	public int updateUserActivity(long faqiId);
	public long booleanFaqiActivity(ActivityDto activityDto);
	/**
	 * 查看发起活动的用户ID
	 * @param faqiId
	 * @return
	 */
	public long findFaqiId(long faqiId);
	public List<Map<String, Object>> findPagination(Map<String, Object> map);
	public User findUserById(long userId);
	/**
	 * 用户登陆
	 * @param usercode
	 * @param password
	 * @return
	 */
	public boolean login(String usercode,String password);
	/**
	 * 用户是否发起活动
	 * @param userId
	 * @return
	 */
	public int countFaqi(long userId);
	/**
	 * 查找发起活动的时间
	 * @param userId
	 * @return
	 */
	public Date findFaqiTime(long userId);
	public User findUser(String usercode);
	public int insertUserForDriver(Map<String,Object> map);
	public int findIsUserByUsercode(String usercode);
	public void insertTipActivity(Activity activity);
	public String findRegistCode(long id);
	public String findAndUpdateTip(long userId,int type);
	public int countUserFx(String invitecode_self);
}
