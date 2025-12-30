package com.nmdy.activity.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.CommonUtil;

import com.nmdy.activity.dao.ActivityDao;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.customerManage.user.dao.UserNewMapper;
import com.nmdy.dto.ActivityDto;
import com.nmdy.entity.Activity;
import com.nmdy.entity.ActivityRule;
import com.nmdy.entity.ActivitySyscoupon;
import com.nmdy.entity.ActivityUser;
import com.nmdy.entity.Tip;
import com.opensymphony.xwork2.ActionContext;

public class ActivityServiceImpl implements ActivityService{
	private ActivityDao activityDao;
	
	public ActivityDao getActivityDao() {
		return activityDao;
	}

	public void setActivityDao(ActivityDao activityDao) {
		this.activityDao = activityDao;
	}
	
	public Activity findById(long id){
		return this.activityDao.findById(id);
	}
	
	public List<Activity> listALl(Map<String,Object> map){
		return this.activityDao.listALl(map);
	}
	
	public int countListALl(Map<String,Object> map){
		return this.activityDao.countListALl(map);
	}
	
	public List<ActivityRule> listActivityRole(long id){
		return this.activityDao.listActivityRole(id);
	}
	
	public int insertActivity(Activity activity){
		String giftNames = String.valueOf(ActionContext.getContext().getSession().get("giftNames"));
		String giftIds = String.valueOf(ActionContext.getContext().getSession().get("giftIds"));
		if("null".equals(giftNames)||"null".equals(giftIds)){
			CommonUtil.removeSessionGift();
			return 0;
		}
		String[] gnames = giftNames.split(",");
		String[] gids = giftIds.split(",");
		if(gnames.length!=gids.length){
			CommonUtil.removeSessionGift();
			return 0;
		}
		String ruleNames = String.valueOf(ActionContext.getContext().getSession().get("ruleNames"));
		String ruleStartNums = String.valueOf(ActionContext.getContext().getSession().get("ruleStartNums"));
		String ruleEndNums = String.valueOf(ActionContext.getContext().getSession().get("ruleEndNums"));
		if("null".equals(ruleNames)||"null".equals(ruleStartNums)||"null".equals(ruleEndNums)){
			CommonUtil.removeSessionRule();
			return 0;
		}
		String[] names = ruleNames.split(",");
		String[] startNums = ruleStartNums.split(",");
		String[] endNums = ruleEndNums.split(",");
		if(names.length!=startNums.length||startNums.length!=endNums.length){
			CommonUtil.removeSessionRule();
			return 0;
		}
		if(gnames.length!=names.length){
			return 0;
		}
		
		 this.activityDao.insertActivityReturnId(activity);
	    long id = activity.getId();
		if(activity.getPrizeType()==1){
			return 1;
		}
		for(int i=0;i<gnames.length;i++){
			ActivitySyscoupon as = new ActivitySyscoupon();
			as.setActivityId(id);
			as.setSysCouponId(Long.parseLong(gids[i]));
			this.activityDao.insertActivityCouponReturnId(as);
			long activitySyscouponId = as.getId();
			for(int j=0;j<names[j].length();j++){
				if(gnames[i].equals(names[j])){
					ActivityRule ar = new ActivityRule();
					ar.setActivityCouponId(activitySyscouponId);
					ar.setStartNum(Integer.parseInt(startNums[i]));
					ar.setEndNum(Integer.parseInt(endNums[i]));
					this.activityDao.insertActivityRule(ar);
					break;
				}
				
			}
		}
		return 1;
	}
	
	public void insertTipActivity(Activity activity){
		 this.activityDao.insertActivityReturnId(activity);
	}
	
	public int updateActivity(Activity activity){
		return activityDao.updateActivity(activity);
	}
	
	public int insertActivityRule(ActivityRule activityRule){
		return this.activityDao.insertActivityRule(activityRule);
	}
	
	public int udpateActivityStop(long id){
		return this.activityDao.udpateActivityStop(id);
	}
	
	public int deleteActivityRule(long id){
		return this.activityDao.deleteActivityRule(id);
	}
	
	public int insertBatchActivityCoupon(List<ActivitySyscoupon> list){
		return this.activityDao.insertBatchActivityCoupon(list);
	}
	
	public int deleteCouponByActivityId(long id){
		return this.activityDao.deleteCouponByActivityId(id);
	}
	
	private  UserNewMapper userNewMapper;
	public void setUserNewMapper(UserNewMapper userNewMapper) {
		this.userNewMapper = userNewMapper;
	}
	
	public User findByCode(String usercode){
		return this.userNewMapper.findByCode(usercode);
	}
	
	public Activity findActivity(){
		return this.activityDao.findActivity();
	}
	
	public List<ActivityUser> findActivityUser(long faqiId){
		return this.activityDao.findActivityUser(faqiId);
	}
	
	public List<Map<String,Object>> findMyGift(long userid){
		return this.activityDao.findMyGift(userid);
	}
	
	public long insertUserActivityReturnId(ActivityDto activityDto){
		return this.activityDao.insertUserActivityReturnId(activityDto);
	}
	
	public long insertUserForReturnId(ActivityDto activityDto){
		return this.activityDao.insertUserForReturnId(activityDto);
	}
	
	public int countUserActivity(long faqiId){
		return this.activityDao.countUserActivity(faqiId);
	}
	
	public int countAllUserActivity(long faqiId){
		return this.activityDao.countAllUserActivity(faqiId);
	}
	
	public long insertActivityHuojiangUser(ActivityDto activityDto){
		return this.activityDao.insertActivityHuojiangUser(activityDto);
	}
	
	public int insertTip(ActivityDto activityDto){
		return this.activityDao.insertTip(activityDto);
	}
	
	public int updateUserActivity(long faqiId){
		return this.activityDao.updateUserActivity(faqiId);
	}

	public long booleanFaqiActivity(ActivityDto activityDto) {
		try {
			return this.activityDao.booleanFaqiActivity(activityDto);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public long findFaqiId(long faqiId){
		return this.activityDao.findFaqiId(faqiId);
	}
	
	public List<Map<String, Object>> findPagination(Map<String, Object> map){
		return this.activityDao.findPagination(map);
	}
	
	public User findUserById(long userId){
		return this.userNewMapper.findById(userId);
	}
	
	public boolean login(String usercode,String password){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usercode", usercode);
		map.put("password", password);
		 return this.activityDao.login(map)==0?false:true;
	}
	
	public int countFaqi(long userId){
		return this.activityDao.countFaqi(userId);
	}
	
	public Date findFaqiTime(long userId){
		System.out.println("userIdxx:"+userId);
		return this.activityDao.findFaqiTime(userId);
	}
	
	public User findUser(String usercode){
		return this.activityDao.findUser(usercode);
	}
	
	public int insertUserForDriver(Map<String,Object> map){
		return this.activityDao.insertUserForDriver(map);
	}

	@Override
	public int findIsUserByUsercode(String usercode) {
		return this.activityDao.findIsUserByUsercode(usercode);
	}
	
	public String findRegistCode(long id){
		return this.activityDao.findRegistCode(id);
	}
	
	public String findAndUpdateTip(long userId,int type){
		Tip tip = this.activityDao.findTip(type);
		if(tip==null){
			return "error";
		}
		Map<String,Object> map = new HashMap<>();
		map.put("userId",userId);
		map.put("id",tip.getId());
		this.activityDao.updateTip(map);
		return tip.getTip_name();
	}
	
	public int countUserFx(String invitecode_self){
		return this.activityDao.countUserFx(invitecode_self);
	}
}
