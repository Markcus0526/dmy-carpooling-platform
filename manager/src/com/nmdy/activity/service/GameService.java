package com.nmdy.activity.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import com.nmdy.customerManage.user.dao.User;
import com.nmdy.spread.loan.dao.Syscoupon;

public interface GameService {
	/**
	 * 获取当前登录者信息
	 * 返回当前登陆的管理员ID
	 * @return
	 */
	public int findLoginAdminId();
	/**
	 * 根据玩家的编号、名称、昵称、手机号码获取玩家的详情
	 * @param usercode  用户编号
	 * @param username  用户名
	 * @param id         用户ID
	 * @param phone      电话
	 * @return          玩家list
	 * @throws UnsupportedEncodingException 
	 */
	public List<User> listByCondition(String usercode,String username,Long id,String phone) throws UnsupportedEncodingException;
	/**
	 * 2.根据批量的玩家编号获取玩家的详情
	 * @param listIds   玩家ID的LIST
	 * @return   玩家LIST
	 */
	public List<User> listByBatchUserIds(Long[] listIds);
	
	/**
	 * 1.根据城市、开始时间、结束时间获取注册人数
	 * @param cityName   城市名称
	 * @param startTime  开始时间
	 * @param endTime    结束时间
	 * @return        注册人数
	 * @throws UnsupportedEncodingException 
	 */
	public long countCityUserByTime(String cityName,String startTime,String endTime) throws UnsupportedEncodingException;
	
	/**
	 * 2.根据城市获取总注册人数
	 * @param cityName   城市名称
	 * @return   注册人数
	 * @throws UnsupportedEncodingException 
	 */
	public long countCityUserByCode(String cityName) throws UnsupportedEncodingException;
	
	/**
	 * 1.发放奖励接口
	 * @param listUserId   用户ID的list
	 * @param balance        绿点
	 * @param id            点卷ID
	 * @return              是否正确的发送
	 */
	public boolean giveUserMoney(Integer[] listUserId,float balance,int id);
	
	/**
	 * 按照点劵Id，点数，有效期来查询点劵列表
	 * @param id    点劵Id
	 * @param num     点数
	 * @param month  有效月份
	 * @return      点劵信息列表
	 */
	public List<Syscoupon> findSyscoupon1Condition(int id,int num,int month,String code);
	
	/**
	 * 用户名和密码是否匹配
	 * @param username   用户名
	 * @param password   密码
	 * @return   true 和 false
	 */
	public boolean login(String username,String password);
	/**
	 * 发送消息
	 * @param userId
	 * @param tip_name
	 * @return
	 */
	public boolean sendMsg(long userId,String tip_name);
}
