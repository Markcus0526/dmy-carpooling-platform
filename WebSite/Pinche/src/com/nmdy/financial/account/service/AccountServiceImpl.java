package com.nmdy.financial.account.service;

import java.util.List;
import java.util.Map;

import com.nmdy.financial.account.dao.Account;
import com.nmdy.financial.account.dao.AccountMapper;
import com.nmdy.financial.account.dao.AccountUserInfo;
import com.nmdy.financial.account.dao.TransactionDetail;



public class AccountServiceImpl implements AccountService{

	private AccountMapper mapper;
	
	
	public AccountMapper getMapper() {
		return mapper;
	}

	public void setMapper(AccountMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public List<Account> search(Map<String, Object> map) {
		return mapper.search(map);
	}

	@Override
	public int editAccount(Map<String, Object> map) {
		int row = mapper.editAccount(map);
		return row;
	}

	@Override
	public List<TransactionDetail> searchTransInfo(Map<String, Object> map) {
		return mapper.searchTransInfo(map);
	}

	@Override
	public AccountUserInfo searchAccountUserInfo(Map<String, Object> temp) {
		// TODO Auto-generated method stub
		return mapper.searchAccountUserInfo(temp);
	}

	@Override
	public Map<String, Object> findusername(int userid) {
		// TODO Auto-generated method stub
		return mapper.findusername(userid);
	}

	@Override
	public int addcharge(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int row = mapper.addcharge(params);
		return row;
		
	}

	@Override
	public List<Map<String, Object>> findorder(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findorder(params);
	}

	@Override
	public List<Map<String, Object>> findMidPoints(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findMidPoints(params);
	}

	@Override
	public Map<String, Object> finduser(long id) {
		// TODO Auto-generated method stub
		return mapper.finduser(id);
	}

	@Override
	public Map<String, Object> findtemp(int id) {
		// TODO Auto-generated method stub
		return mapper.findtemp(id);
	}

	@Override
	public Map<String, Object> findonoff(int id) {
		// TODO Auto-generated method stub
		return mapper.findonoff(id);
	}

	@Override
	public Map<String, Object> findlongdis(int id) {
		// TODO Auto-generated method stub
		return mapper.findlongdis(id);
	}

	@Override
	public Map<String, Object> findone(int orderexeccsid) {
		// TODO Auto-generated method stub
		return mapper.findone(orderexeccsid);
	}

	@Override
	public int getOrderCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.getOrderCount(params);
	}

	@Override
	public List<Map<String, Object>> search1(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.search1(params);
	}

	@Override
	public int getcountsearch1(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.getcountsearch1(params);
	}

	@Override
	public List<Map<String, Object>> finduserinfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.finduserinfo(params);
	}

	@Override
	public List<Map<String, Object>> findgroupinfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findgroupinfo(params);
	}

	@Override
	public List<Map<String, Object>> findunitinfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.findunitinfo(params);
	}

	@Override
	public int countuserinfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.countuserinfo(params);
	}

	@Override
	public int countgrounpinfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.countgrounpinfo(params);
	}

	@Override
	public int countunitinfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return mapper.countunitinfo(params);
	}

	@Override
	public List<Map<String, Object>> showonoff(String ordernum) {
		// TODO Auto-generated method stub
		return mapper.showonoff(ordernum);
	}

	@Override
	public List<Map<String, Object>> showlong(String ordernum) {
		// TODO Auto-generated method stub
		return mapper.showlong(ordernum);
	}

	@Override
	public Map<String, Object> carstyle(String ordernum) {
		// TODO Auto-generated method stub
		return mapper.carstyle(ordernum);
	}

	@Override
	public Map<String, Object> findday(String ordernum) {
		// TODO Auto-generated method stub
		return mapper.findday(ordernum);
	}

	@Override
	public Map<String, Object> findgroupname(int userid) {
		// TODO Auto-generated method stub
		return mapper.findgroupname(userid);
	}

	@Override
	public Map<String, Object> findunitname(int userid) {
		// TODO Auto-generated method stub
		return mapper.findunitname(userid);
	}






}
