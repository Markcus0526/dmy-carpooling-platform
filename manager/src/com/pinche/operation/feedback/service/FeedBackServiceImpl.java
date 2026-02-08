package com.pinche.operation.feedback.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.operation.feedback.dao.FeedBack;
import com.pinche.operation.feedback.dao.FeedBackMapper;

public class FeedBackServiceImpl implements FeedBackService {
	private SqlSession session = null;
    private FeedBackMapper feedBackMapper=null;
   
	public FeedBackServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		feedBackMapper=session.getMapper(FeedBackMapper.class);
	}
	
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
		int row=feedBackMapper.delete(id);
		if(row>0){
			session.commit();
		}
		return row;
	}

	@Override
	public int countByCondition(Map<String, Object> params) {
		return feedBackMapper.countByCondition(params);
	}

}
