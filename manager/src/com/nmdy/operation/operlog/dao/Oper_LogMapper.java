package com.nmdy.operation.operlog.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;

public interface Oper_LogMapper extends SupperMapper{

	public int getCount(Map<String, Object> params);

	public List<Oper_Log> findLogByCondition(Map<String, Object> params);

}
