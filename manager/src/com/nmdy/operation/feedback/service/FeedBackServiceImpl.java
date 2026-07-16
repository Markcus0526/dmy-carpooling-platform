package com.nmdy.operation.feedback.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.operation.feedback.dao.FeedBack;
import com.nmdy.operation.feedback.dao.FeedBackMapper;

public class FeedBackServiceImpl implements FeedBackService {
	private FeedBackMapper feedBackMapper;
	
	@Override
	public List<FeedBack> findByCondition(Map<String, Object> params){
		
		return feedBackMapper.findByCondition(params);
		
	}
	@Override
	public int getCount(Map<String, Object> params) {
		return feedBackMapper.getCount(params);
	}

	@Override
	public FeedBack findById(long id) {
		return feedBackMapper.findById(id);
	}

	@Override
	public int delete(long id) {
		return feedBackMapper.delete(id);
	}

	@Override
	public int countByCondition(Map<String, Object> params) {
		return feedBackMapper.countByCondition(params);
	}
	public FeedBackMapper getFeedBackMapper() {
		return feedBackMapper;
	}
	public void setFeedBackMapper(FeedBackMapper feedBackMapper) {
		this.feedBackMapper = feedBackMapper;
	}

}
