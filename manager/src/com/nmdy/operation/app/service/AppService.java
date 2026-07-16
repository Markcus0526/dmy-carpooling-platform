package com.nmdy.operation.app.service;

import java.util.List;
import java.util.Map;

import com.nmdy.operation.app.dao.App;
import com.nmdy.operation.app.dao.AppPlatform;


public interface AppService {

	public List<App> findAll(Map<String, Object> params);

	public int getCount(Map<String, Object> params);

	public int delete(Long id);

	public int update(Map<String, Object> params);

	public int insertAppVersion(Map<String, Object> params2);

	public int insertApp(Map<String, Object> params1);

	public App findById(Map<String, Object> map);

	public int checkAppCode(String app_code);

	public int updateApp(Map<String, Object> params1);

	public List<App> findNew(Map<String, Object> params);

	public AppPlatform findByCode(String app_code);

	public int getCountNew();

	public int getCountAll();


}
