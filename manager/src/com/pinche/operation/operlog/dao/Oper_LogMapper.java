package com.pinche.operation.operlog.dao;

import java.util.List;
import java.util.Map;

public interface Oper_LogMapper {

	public int getCount(Map<String, Object> params);

	public List<Oper_Log> findLogByCondition(Map<String, Object> params);

}
