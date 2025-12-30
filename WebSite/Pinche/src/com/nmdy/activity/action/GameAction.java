package com.nmdy.activity.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nmdy.activity.service.GameService;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.spread.loan.dao.Syscoupon;
import com.opensymphony.xwork2.ActionSupport;

public class GameAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long userId;
	private String username;
	private String password;
	private String usercode;
	private String phone;
	private String citycode;
	private String startTime;
	private String endTime;
	private List<User> listUser;
	private int num;
	private Object jsonObj;
	private GameService gameService;
	private Long[] listIds;
	private Integer[] listUserId;
	private float balance;
	private int couponId;
	private List<Syscoupon> listSyscoupon;
	private int month;
	private String cityName;
	private String code;
	
	
	public String findLoginAdminId(){
		int i = gameService.findLoginAdminId();
		 Map<String,Object> jsonMap = new HashMap<>();
		jsonMap.put("result",i);
		jsonObj = jsonMap;
		return "findLoginAdminId";
	}
	
	public String listByCondition(){
		List<User> list = null;
		try {
			list = gameService.listByCondition(usercode, username, userId, phone);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 Map<String,Object> jsonMap = new HashMap<>();
		jsonMap.put("result",list);
		jsonObj = jsonMap;
		return "listByCondition";
	}
	
	public String listByBatchUserIds(){
		List<User> list = gameService.listByBatchUserIds(listIds);
		 Map<String,Object> jsonMap = new HashMap<>();
		jsonMap.put("result",list);
		jsonObj = jsonMap;
		return "listByBatchUserIds";
	}
	
	public String countCityUserByCode(){
		long i = 0;
		try {
			i = gameService.countCityUserByCode(cityName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Map<String,Object> jsonMap = new HashMap<>();
		jsonMap.put("result",i);
		jsonObj = jsonMap;
		return "countCityUserByTime";
	}
	
	public String countCityUserByTime(){
		long i = 0;
		try {
			i = gameService.countCityUserByTime(cityName,startTime,endTime);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Map<String,Object> jsonMap = new HashMap<>();
		jsonMap.put("result",i);
		jsonObj = jsonMap;
		return "countCityUserByTime";
	}
	
	public String giveUserMoney(){
		boolean i = gameService.giveUserMoney(listUserId, balance, couponId);
		 Map<String,Object> jsonMap = new HashMap<>();
		jsonMap.put("result",i);
		jsonObj = jsonMap;
		return "giveUserMoney";
	}
	
	public String findSyscoupon1Condition(){
		List<Syscoupon> list = gameService.findSyscoupon1Condition(couponId, num, month,code);
		 Map<String,Object> jsonMap = new HashMap<>();
		jsonMap.put("result",list);
		jsonObj = jsonMap;
		return "findSyscoupon1Condition";
	}
	
	public String booleanLogin(){
		boolean i = gameService.login(username, password);
		 Map<String,Object> jsonMap = new HashMap<>();
		jsonMap.put("result",i);
		jsonObj = jsonMap;
		return "booleanLogin";
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<User> getListUser() {
		return listUser;
	}

	public void setListUser(List<User> listUser) {
		this.listUser = listUser;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Object getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(Object jsonObj) {
		this.jsonObj = jsonObj;
	}

	public GameService getGameService() {
		return gameService;
	}

	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	public Long[] getListIds() {
		return listIds;
	}

	public void setListIds(Long[] listIds) {
		this.listIds = listIds;
	}

	public Integer[] getListUserId() {
		return listUserId;
	}

	public void setListUserId(Integer[] listUserId) {
		this.listUserId = listUserId;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public List<Syscoupon> getListSyscoupon() {
		return listSyscoupon;
	}

	public void setListSyscoupon(List<Syscoupon> listSyscoupon) {
		this.listSyscoupon = listSyscoupon;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
