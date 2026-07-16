package com.pinche.authority.manager.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.pinche.authority.manager.dao.OperLog;
import com.pinche.authority.manager.dao.OperLogMapper;
import com.pinche.common.SqlSessionHelper;

public class OperLogServiceImpl implements OperLogService {

	private SqlSession session = null;
	private OperLogMapper mapper = null;
	
	public OperLogServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(OperLogMapper.class);
	}
	
	@Override
	public List<OperLog> findAll() {
		return mapper.findAll();
	}

	@Override
	public OperLog findOneById(int id) {
		return mapper.findOneById(id);
	}

	@Override
	public List<OperLog> findByUserId(int uid) {
		return mapper.findByUserId(uid);
	}

	@Override
	public List<OperLog> findByTableName(String tableName) {
		return mapper.findByTableName(tableName);
	}

	@Override
	public List<OperLog> findByActionUrl(String actionUrl) {
		return mapper.findByActionUrl(actionUrl);
	}

	@Override
	public int append(OperLog operLog) {
		int rows = mapper.append(operLog);
		if (rows > 0)
			session.commit();
		return rows;
	}

	@Override
	public int delete(int id) {
		int rows = mapper.delete(id);
		if (rows > 0)
			session.commit();
		return rows;
	}
	
}
