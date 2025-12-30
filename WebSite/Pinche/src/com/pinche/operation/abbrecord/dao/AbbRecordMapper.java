package com.pinche.operation.abbrecord.dao;

import java.util.List;
import java.util.Map;

public interface AbbRecordMapper {

	public List<AbbRecord> findpassenger(Map<String, Object> params);

	public int getCount(Map<String, Object> params);

	public List<AbbRecord> finddriver(Map<String, Object> params);

	public AbbRecord findById(long id);

	public int addblack(Map<String, Object> params);

	public List<AbbRecord> findblack(Map<String, Object> params);

	public int getBlackCount(Map<String, Object> params);

	public int removeBlack(long id);

	public int addAbb1(Map<String, Object> params);
	public int addAbb2(Map<String, Object> params);

	public List<AbbLimit> findAbb();
}
