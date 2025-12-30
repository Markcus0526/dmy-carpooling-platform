package com.pinche.operation.operlog.service;

import java.util.List;
import java.util.Map;

import com.pinche.operation.operlog.dao.Oper_Log;

public interface Oper_LogService {


	 public int getCount(Map<String, Object> params);

	public List<Oper_Log> findLogByCondition(Map<String, Object> params);


}

