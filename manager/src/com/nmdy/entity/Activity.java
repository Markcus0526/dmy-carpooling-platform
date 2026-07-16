package com.nmdy.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Activity implements Serializable{
    /**
	 * 活动
	 * 王锦成
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Date startTime;
	private Date endTime;
	private int shareTime;
	private int sharePeople;
	private int userdPeople;
	private int prizeType;
	private String lotteryName;
	private List<ActivitySyscoupon> listActivitySyscoupon = new ArrayList<ActivitySyscoupon>();
	private int joinPeople;
	private int giveGiftNum;
	private int status;
	private String startEndTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getShareTime() {
		return shareTime;
	}
	public void setShareTime(int shareTime) {
		this.shareTime = shareTime;
	}
	public int getSharePeople() {
		return sharePeople;
	}
	public void setSharePeople(int sharePeople) {
		this.sharePeople = sharePeople;
	}
	public int getPrizeType() {
		return prizeType;
	}
	public void setPrizeType(int prizeType) {
		this.prizeType = prizeType;
	}
	public String getLotteryName() {
		return lotteryName;
	}
	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}
	public List<ActivitySyscoupon> getListActivitySyscoupon() {
		if(listActivitySyscoupon==null){
			return new ArrayList<ActivitySyscoupon>();
		}
		return this.listActivitySyscoupon;
	}
	
	public void setListActivitySyscoupon(
			List<ActivitySyscoupon> listActivitySyscoupon) {
		this.listActivitySyscoupon = listActivitySyscoupon;
	}
	public int getJoinPeople() {
		return joinPeople;
	}
	public void setJoinPeople(int joinPeople) {
		this.joinPeople = joinPeople;
	}
	public int getGiveGiftNum() {
		return giveGiftNum;
	}
	public void setGiveGiftNum(int giveGiftNum) {
		this.giveGiftNum = giveGiftNum;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStartEndTime() {
		return startEndTime;
	}
	public void setStartEndTime(String startEndTime) {
		this.startEndTime = startEndTime;
	}
	public int getUserdPeople() {
		return userdPeople;
	}
	public void setUserdPeople(int userdPeople) {
		this.userdPeople = userdPeople;
	}
}
