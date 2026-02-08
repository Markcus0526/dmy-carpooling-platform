package com.nmdy.filter.service;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.filter.pojo.Function;

public interface IFilterService extends SupperMapper{
	public int isHasAccessToTheURI(Map<String, Object> map) ;
	public List<Function> getSubFunctions(Map<String, Object> map);

}
