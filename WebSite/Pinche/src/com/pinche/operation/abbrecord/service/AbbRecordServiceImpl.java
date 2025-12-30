package com.pinche.operation.abbrecord.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.operation.abbrecord.dao.AbbLimit;
import com.pinche.operation.abbrecord.dao.AbbRecord;
import com.pinche.operation.abbrecord.dao.AbbRecordMapper;

public class AbbRecordServiceImpl implements AbbRecordService {

	private SqlSession session=null;
	private AbbRecordMapper abbmapper=null;
	public AbbRecordServiceImpl(){
		session=SqlSessionHelper.getSession();
		abbmapper=session.getMapper(AbbRecordMapper.class);
	}
	@Override
	public List<AbbRecord> findpassenger(Map<String, Object> params) {
		return abbmapper.findpassenger(params);
	}
	@Override
	public int getCount(Map<String, Object> params) {
		return abbmapper.getCount(params);
	}
	@Override
	public List<AbbRecord> finddriver(Map<String, Object> params) {
		return abbmapper.finddriver(params);
	}
	@Override
	public AbbRecord findById(long id) {
		return abbmapper.findById(id);
	}
	
	@Override
	public int addblack(Map<String, Object> params) {	
		int row = abbmapper.addblack(params);
		if(row>0){
			session.commit();
		}
		return row;
	}
	@Override
	public List<AbbRecord> findblack(Map<String, Object> params) {
		return abbmapper.findblack(params);
	}
	@Override
	public int getBlackCount(Map<String, Object> params) {
		return abbmapper.getBlackCount(params);
	}
	@Override
	public int removeBlack(long id) {
		int row=abbmapper.removeBlack(id);
		if (row>0){
			session.commit();
		}
		return row;
	}
	@Override
	public int addAbb1(Map<String, Object> params) {
		int row =abbmapper.addAbb1(params);
		if(row>0){
			session.commit();
		}
		return row;
	}
	@Override
	public int addAbb2(Map<String, Object> params) {
		int row =abbmapper.addAbb2(params);
		if(row>0){
			session.commit();
		}
		return row;
	}
	@Override
	public List<AbbLimit> findAbb() {
		return abbmapper.findAbb();
	}
	
	}
