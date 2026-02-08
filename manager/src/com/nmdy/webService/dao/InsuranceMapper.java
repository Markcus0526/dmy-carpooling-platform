package com.nmdy.webService.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;

public interface InsuranceMapper extends SupperMapper{
	/**
	 * 通过id获取保险表数据
	 * @param long id 保单id
	 * @return 保险数据列表
	 */
	public Map<String, Object> fetcheInsuData(long id);
	
	/**
	 * 通过id区间获取流水表数据列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> fetcheInsuDTD(Map<String, Object> map);


}
