package com.nmdy.financial.manage.service;

import java.util.List;
import java.util.Map;

import com.nmdy.financial.manage.dao.OrderEntity;
import com.nmdy.financial.manage.dao.Userinfo;
import com.nmdy.financial.manage.dao.UsersEntity;




public interface MyFormService {

	public List<UsersEntity> find(Map<String, Object> params);//查询	全部
	public int getCount(Map<String, Object> params);
	public UsersEntity findbyid(int id);
	public List<UsersEntity> findwait(Map<String, Object> params);//查询 待我审核
	public List<UsersEntity> getCountwait(Map<String, Object> params);
	public int updateunpass(Map<String, Object> params);//驳回
	public int updatepass(Map<String, Object> params);//通过
	public List<OrderEntity> findorder(Map<String, Object> params);//获得选择的订单编号
	public int getOrderCount(Map<String, Object> params);//获得查询订单数据的总数
	public Userinfo finduserinfo(int usersid);//客户姓名查询
	public int updatemodifyreq(Map<String, Object> params);//修改req

	public int findtsid( Map<String, Object> params2);//查找ts_id 用于确定修改哪个ts
	public int addmodifytsbal(Map<String, Object> params3);//通过后余额的更新
	public int updatecolse(Map<String, Object> params);//关闭
	public Map<String, Object> findone(int orderid);//执行订单信息查询
	public List<Map<String, Object>> findMidPoints(Map<String, Object> params);//查询中途点信息
	public Map<String, Object> finduser(long orderid);//查询乘客及车主相关信息
	public Map<String, Object> findts(Map<String, Object> params);//查询绿点是否冻结
	public Map<String, Object> findtsinfo(int tsid);//根据充值提现管理传过来的tsid查询出来ts表里的用户的信息方便插入到新的信息
	public int updateupuser(Map<String, Object> params);//更新user
	public int updateupgroup(Map<String, Object> params);
	public int updateupunit(Map<String, Object> params);//通过
	public Map<String, Object> finduserbalance(Map<String, Object> params);
	public Map<String, Object> findgroupbalance(Map<String, Object> params);
	public Map<String, Object> findunitbalance(Map<String, Object> params);
	public Map<String, Object> findcar(int orderid);//执行订单车型
	
}
