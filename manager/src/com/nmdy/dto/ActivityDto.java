package com.nmdy.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nmdy.entity.Activity;
import com.nmdy.entity.ActivityUser;

public class ActivityDto {
	private String usercode;//帐号
	private String invitecodeRegist;//邀请我的邀请码
	private Activity activity;
    private List<ActivityUser> listActivityUsers = new ArrayList<ActivityUser>();
    private List<Map<String,Object>> listActivityGift = new ArrayList<Map<String,Object>>();
	private long faqiId;
	private long faqiUserId;
	private long activityId;
	private long huojiangId;
	private long fid;
	private Long activityCouponId;
	private String invitecode_self;
	private String password;
	private int sex;
	private int style;
	private long userId;
	private String giftName;
	private Date addtime;
	private int type;
	private int userdNum;
	private String validateCode;
	private long activityUserId;
	private String msg;
	private long myFaqiId;
	private String tip_name;
	private int faqiNum;//有多
	private String nickname;
	
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTip_name() {
		return tip_name;
	}

	public void setTip_name(String tip_name) {
		this.tip_name = tip_name;
	}

	public long getFaqiId() {
		return faqiId;
	}

	public void setFaqiId(long faqiId) {
		this.faqiId = faqiId;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getInvitecodeRegist() {
		return invitecodeRegist;
	}

	public void setInvitecodeRegist(String invitecodeRegist) {
		this.invitecodeRegist = invitecodeRegist;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public List<ActivityUser> getListActivityUsers() {
		return listActivityUsers;
	}

	public void setListActivityUsers(List<ActivityUser> listActivityUsers) {
		this.listActivityUsers = listActivityUsers;
	}

	public List<Map<String, Object>> getListActivityGift() {
		return listActivityGift;
	}

	public void setListActivityGift(List<Map<String, Object>> listActivityGift) {
		this.listActivityGift = listActivityGift;
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public long getFaqiUserId() {
		return faqiUserId;
	}

	public void setFaqiUserId(long faqiUserId) {
		this.faqiUserId = faqiUserId;
	}

	public String getInvitecode_self() {
		return invitecode_self;
	}

	public void setInvitecode_self(String invitecode_self) {
		this.invitecode_self = invitecode_self;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public long getHuojiangId() {
		return huojiangId;
	}

	public void setHuojiangId(long huojiangId) {
		this.huojiangId = huojiangId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getActivityCouponId() {
		return activityCouponId;
	}

	public void setActivityCouponId(Long activityCouponId) {
		this.activityCouponId = activityCouponId;
	}

	public int getUserdNum() {
		return userdNum;
	}

	public void setUserdNum(int userdNum) {
		this.userdNum = userdNum;
	}

	public long getFid() {
		return fid;
	}

	public void setFid(long fid) {
		this.fid = fid;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public long getActivityUserId() {
		return activityUserId;
	}

	public void setActivityUserId(long activityUserId) {
		this.activityUserId = activityUserId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getMyFaqiId() {
		return myFaqiId;
	}

	public void setMyFaqiId(long myFaqiId) {
		this.myFaqiId = myFaqiId;
	}
	
}
