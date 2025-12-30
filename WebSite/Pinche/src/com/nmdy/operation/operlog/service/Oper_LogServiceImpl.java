package com.nmdy.operation.operlog.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.operation.operlog.dao.Oper_Log;
import com.nmdy.operation.operlog.dao.Oper_LogMapper;

public class Oper_LogServiceImpl implements Oper_LogService {
  private Oper_LogMapper oper_LogMapper;
	@Override
	public int getCount(Map<String, Object> params) {
		return oper_LogMapper.getCount(params);
	}

	@Override
	public List<Oper_Log> findLogByCondition(Map<String, Object> params) {
		return oper_LogMapper.findLogByCondition(params);
	}

	public Oper_LogMapper getOper_LogMapper() {
		return oper_LogMapper;
	}

	public void setOper_LogMapper(Oper_LogMapper oper_LogMapper) {
		this.oper_LogMapper = oper_LogMapper;
	}

}
