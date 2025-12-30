package com.nmdy.operation.abbrecord.service;

import java.util.List;
import java.util.Map;

import com.nmdy.operation.abbrecord.dao.AbbLimit;
import com.nmdy.operation.abbrecord.dao.AbbRecord;


public interface AbbRecordService {

	public List<AbbRecord> findpassenger(Map<String, Object> params);

	public int getCount(Map<String, Object> params);

	public List<AbbRecord> finddriver(Map<String, Object> params);

	public AbbRecord findById(long id);

	public int addblack(Map<String, Object> params);

	public List<AbbRecord> findblack(Map<String, Object> params);

	public int getBlackCount(Map<String, Object> params);

	public int deleteBlack(long id);

	public int addAbb2(Map<String, Object> params);

	public int addAbb1(Map<String, Object> params);

	public List<AbbLimit> findAbb();

	public int insertNotify(Map<String, Object> map);

	public int undateWarn(Map<String, Object> map);

	public int undateForfeit(Map<String, Object> map);

	public int updateTSForfeit(Map<String, Object> map);
	
}
