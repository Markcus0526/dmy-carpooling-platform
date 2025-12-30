package com.nmdy.financial.ledger.service;

import java.util.List;
import java.util.Map;

import com.nmdy.financial.ledger.dao.LedgerMapper;


public class LedgerServiceImpl implements LedgerService{
	private LedgerMapper myLedgerMapper;

	public LedgerMapper getMyLedgerMapper() {
		return myLedgerMapper;
	}
	public void setMyLedgerMapper(LedgerMapper myLedgerMapper) {
		this.myLedgerMapper = myLedgerMapper;
	}
	@Override
	public List<Map<String, Object>> findledger(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findledger(params);
	}
	@Override
	public int gotCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myLedgerMapper.gotCount(params);
	}
	@Override
	public List<Map<String, Object>> findMidPoints(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findMidPoints(params);
	}
	@Override
	public Map<String, Object> finduser(long id) {
		// TODO Auto-generated method stub
		return myLedgerMapper.finduser(id);
	}
	@Override
	public Map<String, Object> findtemp(int id) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findtemp(id);
	}
	@Override
	public Map<String, Object> findonoff(int id) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findonoff(id);
	}
	@Override
	public Map<String, Object> findlongdis(int id) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findlongdis(id);
	}
	@Override
	public Map<String, Object> findone(int orderexeccsid) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findone(orderexeccsid);
	}
	@Override
	public List<Map<String, Object>> showonoff(String ordernum) {
		// TODO Auto-generated method stub
		return myLedgerMapper.showonoff(ordernum);
	}
	@Override
	public List<Map<String, Object>> showlong(String ordernum) {
		// TODO Auto-generated method stub
		return myLedgerMapper.showlong(ordernum);
	}
	@Override
	public Map<String, Object> carstyle(String ordernum) {
		// TODO Auto-generated method stub
		return myLedgerMapper.carstyle(ordernum);
	}
	@Override
	public Map<String, Object> findday(String ordernum) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findday(ordernum);
	}
	@Override
	public Map<String, Object> findts(int id) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findts(id);
	}
	@Override
	public Map<String, Object> sumbalance() {
		// TODO Auto-generated method stub
		return myLedgerMapper.sumbalance();
	}
	@Override
	public Map<String, Object> finduserinvit(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myLedgerMapper.finduserinvit(params);
	}
	@Override
	public Map<String, Object> findgroupinvit(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findgroupinvit(params);
	}
	@Override
	public Map<String, Object> findunitinvit(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return myLedgerMapper.findunitinvit(params);
	}
	

}
