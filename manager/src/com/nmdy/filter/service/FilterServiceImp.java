package com.nmdy.filter.service;

import java.util.List;
import java.util.Map;

import com.nmdy.filter.dao.FilterMapper;
import com.nmdy.filter.pojo.Function;

public class FilterServiceImp implements IFilterService{

	private FilterMapper filterMapper = null;
	
	
	public FilterMapper getFilterMapper() {
		return filterMapper;
	}
	public void setFilterMapper(FilterMapper filterMapper) {
		this.filterMapper = filterMapper;
	}
	
	
	public int isHasAccessToTheURI(Map<String, Object> map) {
		return filterMapper.isHasAccessToTheURI(map);
	}
	
	public List<Function> getSubFunctions(Map<String, Object> map){
		return filterMapper.getSubFunctions(map);
		
	}
	
}
