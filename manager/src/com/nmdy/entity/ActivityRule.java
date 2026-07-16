package com.nmdy.entity;

import java.io.Serializable;

/**
 * 活动规则
 * @author Administrator
 *
 */
public class ActivityRule implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long activityCouponId;
	private int num;
	private String syscouponCode;
	private int startNum;
	private int endNum;
	private int usedNum; 
	private String activityCouponName;
	private Long couponId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getActivityCouponId() {
		return activityCouponId;
	}
	public void setActivityCouponId(Long activityCouponId) {
		this.activityCouponId = activityCouponId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSyscouponCode() {
		return syscouponCode;
	}
	public void setSyscouponCode(String syscouponCode) {
		this.syscouponCode = syscouponCode;
	}
	public int getStartNum() {
		return startNum;
	}
	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}
	public int getEndNum() {
		return endNum;
	}
	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}
	public int getUsedNum() {
		return usedNum;
	}
	public void setUsedNum(int usedNum) {
		this.usedNum = usedNum;
	}
	public String getActivityCouponName() {
		return activityCouponName;
	}
	public void setActivityCouponName(String activityCouponName) {
		this.activityCouponName = activityCouponName;
	}
	public Long getCouponId() {
		return couponId;
	}
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
 }
