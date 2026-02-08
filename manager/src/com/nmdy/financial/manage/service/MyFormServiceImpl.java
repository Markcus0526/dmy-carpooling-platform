package com.nmdy.financial.manage.service;

import java.util.List;
import java.util.Map;

import com.nmdy.financial.manage.dao.MyFormMapper;
import com.nmdy.financial.manage.dao.OrderEntity;
import com.nmdy.financial.manage.dao.Userinfo;
import com.nmdy.financial.manage.dao.UsersEntity;



public class MyFormServiceImpl implements MyFormService{
	private MyFormMapper myFormMapper;

	@Override
	public List<UsersEntity> find(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.find(params);
	}
	@Override
	public int getCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.getCount(params);
	}
	@Override
	public UsersEntity findbyid(int id) {
		// TODO Auto-generated method stub
		return myFormMapper.findbyid(id);
	}
	@Override
	public List<UsersEntity> findwait(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.findwait(params);
	}
	@Override
	public List<UsersEntity> getCountwait(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.getCountwait(params);
	}
	@Override
	public int  updateunpass(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int row= myFormMapper.updateunpass(params);
	
		return row;
		
	}
	@Override
	public int updatepass(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int row= myFormMapper.updatepass(params);

		return row;
	}

	@Override
	public int getOrderCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.getOrderCount(params);
	}
	@Override
	public List<OrderEntity> findorder(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.findorder(params);
	}
	@Override
	public Userinfo finduserinfo(int usersid) {
		// TODO Auto-generated method stub
		return myFormMapper.finduserinfo(usersid);
	}
	@Override
	public int updatemodifyreq(Map<String, Object> params) {
		int r= myFormMapper.updatemodifyreq(params);
	
		return r;
		
	}

	@Override
	public int findtsid( Map<String, Object> params2) {
		// TODO Auto-generated method stub
		return myFormMapper.findtsid(params2);
	}
	@Override
	public int addmodifytsbal(Map<String, Object> params3) {
		// TODO Auto-generated method stub
		int r=myFormMapper.addmodifytsbal(params3);
	
		return r;
	}
	@Override
	public int updatecolse(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int row= myFormMapper.updatecolse(params);

		return row;
	}
	@Override
	public Map<String, Object> findone(int orderid) {
		// TODO Auto-generated method stub
		return myFormMapper.findone(orderid);
	}
	@Override
	public List<Map<String, Object>> findMidPoints(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.findMidPoints(params);
	}
	@Override
	public Map<String, Object> finduser(long orderid) {
		// TODO Auto-generated method stub
		return myFormMapper.finduser(orderid);
	}

	@Override
	public Map<String, Object> findts(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.findts(params);
	}
	@Override
	public Map<String, Object> findtsinfo(int tsid) {
		// TODO Auto-generated method stub
		return myFormMapper.findtsinfo(tsid);
	}
	@Override
	public int updateupuser(Map<String, Object> params) {
		int r=myFormMapper.updateupuser(params);

		return r;
	}
	
	@Override
	public int updateupgroup(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int r=myFormMapper.updateupgroup(params);
	
		return r;
	}
	@Override
	public int updateupunit(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int r =myFormMapper.updateupunit(params);

		return r;
	}
	@Override
	public Map<String, Object> finduserbalance(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.finduserbalance(params);
	}
	@Override
	public Map<String, Object> findgroupbalance(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.findgroupbalance(params);
	}
	@Override
	public Map<String, Object> findunitbalance(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myFormMapper.findunitbalance(params);
	}
	@Override
	public Map<String, Object> findcar(int orderid) {
		// TODO Auto-generated method stub
		return myFormMapper.findcar(orderid);
	}
	public MyFormMapper getMyFormMapper() {
		return myFormMapper;
	}
	public void setMyFormMapper(MyFormMapper myFormMapper) {
		this.myFormMapper = myFormMapper;
	}

	
}
