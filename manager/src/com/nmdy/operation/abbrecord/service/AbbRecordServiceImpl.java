package com.nmdy.operation.abbrecord.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.common.SqlSessionHelper;
import com.nmdy.operation.abbrecord.dao.AbbLimit;
import com.nmdy.operation.abbrecord.dao.AbbRecord;
import com.nmdy.operation.abbrecord.dao.AbbRecordMapper;

public class AbbRecordServiceImpl implements AbbRecordService {

	private AbbRecordMapper abbRecordMapper;
	@Override
	public List<AbbRecord> findpassenger(Map<String, Object> params) {
		return abbRecordMapper.findpassenger(params);
	}
	@Override
	public int getCount(Map<String, Object> params) {
		return abbRecordMapper.getCount(params);
	}
	@Override
	public List<AbbRecord> finddriver(Map<String, Object> params) {
		return abbRecordMapper.finddriver(params);
	}
	@Override
	public AbbRecord findById(long id) {
		return abbRecordMapper.findById(id);
	}
	
	@Override
	public int addblack(Map<String, Object> params) {	
		return abbRecordMapper.addblack(params);
	}
	@Override
	public List<AbbRecord> findblack(Map<String, Object> params) {
		return abbRecordMapper.findblack(params);
	}
	@Override
	public int getBlackCount(Map<String, Object> params) {
		return abbRecordMapper.getBlackCount(params);
	}
	@Override
	public int deleteBlack(long id) {
		return abbRecordMapper.removeBlack(id);
	}
	@Override
	public int addAbb1(Map<String, Object> params) {
		return abbRecordMapper.addAbb1(params);
	}
	@Override
	public int addAbb2(Map<String, Object> params) {
		return abbRecordMapper.addAbb2(params);
	}
	@Override
	public List<AbbLimit> findAbb() {
		return abbRecordMapper.findAbb();
	}
	@Override
	public int insertNotify(Map<String, Object> map) {
		return abbRecordMapper.insertNotify(map);
	}
	public AbbRecordMapper getAbbRecordMapper() {
		return abbRecordMapper;
	}
	public void setAbbRecordMapper(AbbRecordMapper abbRecordMapper) {
		this.abbRecordMapper = abbRecordMapper;
	}
	@Override
	public int undateWarn(Map<String, Object> map) {
		return abbRecordMapper.undateWarn(map);
	}
	@Override
	public int undateForfeit(Map<String, Object> map) {
		return abbRecordMapper.undateForfeit(map);
	}
	@Override
	public int  updateTSForfeit(Map<String, Object> map) {
		return abbRecordMapper.updateTSForfeit(map);
	}

	
	}
