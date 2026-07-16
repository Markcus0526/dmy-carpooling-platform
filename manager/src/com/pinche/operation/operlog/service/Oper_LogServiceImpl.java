package com.pinche.operation.operlog.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.operation.operlog.dao.Oper_Log;
import com.pinche.operation.operlog.dao.Oper_LogMapper;

public class Oper_LogServiceImpl implements Oper_LogService {
   private SqlSession session =null;
   private Oper_LogMapper operLogMapper=null;
      
     public Oper_LogServiceImpl(){
    	 session = com.pinche.common.SqlSessionHelper.getSession();
    	 operLogMapper=session.getMapper(Oper_LogMapper.class);
     }

	@Override
	public int getCount(Map<String, Object> params) {
		return operLogMapper.getCount(params);
	}

	@Override
	public List<Oper_Log> findLogByCondition(Map<String, Object> params) {
		return operLogMapper.findLogByCondition(params);
	}

}
