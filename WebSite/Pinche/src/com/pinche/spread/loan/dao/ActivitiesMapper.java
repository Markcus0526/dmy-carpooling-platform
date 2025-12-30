package com.pinche.spread.loan.dao;

import java.util.List;
import java.util.Map;

public interface ActivitiesMapper {

	List<Activities> search(Map<String, Object> map);
	void insert(Activities activity);
	Activities findOneByCode(String activityCode);
	void update(Activities activity);

}
