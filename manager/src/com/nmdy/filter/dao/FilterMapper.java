package com.nmdy.filter.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.filter.pojo.Function;

public interface FilterMapper extends SupperMapper{

	public int isHasAccessToTheURI(Map<String, Object> map) ;
	public List<Function> getSubFunctions(Map<String, Object> map);
}
