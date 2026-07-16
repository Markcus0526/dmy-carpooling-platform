package com.nmdy.operation.feedback.service;

import java.util.List;
import java.util.Map;

import com.nmdy.operation.feedback.dao.FeedBack;

public interface FeedBackService {

	public List<FeedBack> findByCondition(Map<String,Object> params);

	public FeedBack findById(long id);

	public int getCount(Map<String, Object> params);

	public int delete(long id);

	public int countByCondition(Map<String, Object> params);

}
