package com.nmdy.spread.loan.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;

public interface ActivitiesMapper extends SupperMapper{

	List<Activities> search(Map<String, Object> map);
	void insert(Activities activity);
	Activities findOneByCode(String activityCode);
	int update(String stopActivityCode);

}
