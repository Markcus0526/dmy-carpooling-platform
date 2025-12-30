package com.nmdy.operation.notify.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
public interface NotifyMapper extends SupperMapper {
	public List<NotifyEntity> finduser(Map<String, Object> params);
	public int getTotal(Map<String, Object> params);
	public List<Integer> findids(Map<String, Object> params);
	public int addbatchInsert(Map<String, Object> params);

}
