package com.nmdy.activity.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.dto.ActivityDto;
import com.nmdy.entity.Activity;
import com.nmdy.entity.ActivityRule;
import com.nmdy.entity.ActivitySyscoupon;
import com.nmdy.entity.ActivityUser;
import com.nmdy.entity.Tip;


public interface ActivityDao extends SupperMapper{
	public Activity findById(long id);
	public List<Activity> listALl(Map<String,Object> map);
	public int countListALl(Map<String,Object> map);
	public List<ActivityRule> listActivityRole(long id);
	public long insertActivityReturnId(Activity activity);
	public long insertActivityCouponReturnId(ActivitySyscoupon activitySyscoupon);
	public int insertActivityRule(ActivityRule activityRule);
	public int udpateActivityStop(long id);
	public int deleteActivityRule(long id);
	public int insertBatchActivityCoupon(List<ActivitySyscoupon> list);
	public int deleteCouponByActivityId(long id);
	public int updateActivity(Activity activity);
	public Activity findActivity();
	public List<ActivityUser> findActivityUser(long fid);
	public List<Map<String,Object>> findMyGift(long userid);
	public long insertUserActivityReturnId(ActivityDto activityDto);
	public long insertUserForReturnId(ActivityDto activityDto);
	public int countUserActivity(long faqiId);
	public int countAllUserActivity(long faqiId);//含过期的
	public long insertActivityHuojiangUser(ActivityDto activityDto);
	public int insertTip(ActivityDto activityDto);
	public int updateUserActivity(long faqiId);
	public long booleanFaqiActivity(ActivityDto activityDto);
	public long findFaqiId(long faqiId);
	public List<Map<String, Object>> findPagination(Map<String, Object> map);
	public int login(Map<String, Object> map);
	public int countFaqi(long userId);
	public Date findFaqiTime(long userId);
	public User findUser(String usercode);
	public int findMaxId();
	public int insertUserForDriver(Map<String,Object> map);
	public int findIsUserByUsercode(String usercode);
	public String findRegistCode(long id);
	public Tip findTip(int type);
	public int updateTip(Map<String,Object> map);
	public int countUserFx(String invitecode_self);
}