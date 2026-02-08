package com.pinche.authority.manager.service;

import java.util.List;

import com.pinche.authority.manager.dao.OperLog;

public interface OperLogService {

	public List<OperLog> findAll();
	public OperLog findOneById(int id);
	public List<OperLog> findByUserId(int uid);
	public List<OperLog> findByTableName(String tableName);
	public List<OperLog> findByActionUrl(String actionUrl);
	public int append(OperLog operLog);
	public int delete(int id);

}
