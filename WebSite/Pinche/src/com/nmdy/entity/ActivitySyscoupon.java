package com.nmdy.entity;

import java.io.Serializable;

/**
 * 活动点卷表
 * @author Administrator
 *
 */
public class ActivitySyscoupon implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	  private Long activityId;
	  private Long sysCouponId;
//	  private int num;
	  private String syscouponCode;
	  private ActivityRule activyityRule;
	  private int usedNum;
	  
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public Long getSysCouponId() {
		return sysCouponId;
	}
	public void setSysCouponId(Long sysCouponId) {
		this.sysCouponId = sysCouponId;
	}
	public String getSyscouponCode() {
		return syscouponCode;
	}
	public void setSyscouponCode(String syscouponCode) {
		this.syscouponCode = syscouponCode;
	}
	public ActivityRule getActivyityRule() {
		return activyityRule;
	}
	public void setActivyityRule(ActivityRule activyityRule) {
		this.activyityRule = activyityRule;
	}
	public int getUsedNum() {
		return usedNum;
	}
	public void setUsedNum(int usedNum) {
		this.usedNum = usedNum;
	}
	
}
