package com.pinche.operation.app.dao;

import java.util.List;
import java.util.Map;

public interface AppMapper {

	public List<App> findall(Map<String, Object> params);

	public int getCount(Map<String, Object> params);

	public int update(Map<String, Object> params);

	public int delete(Long id);

	public int insertApp(Map<String, Object> params1);

	public int insertAppVersion(Map<String, Object> params2);

	public App findById(Map<String, Object> map);

	public int checkAppCode(String app_code);

	public int updateApp(Map<String, Object> params1);

	public List<App> findNew(Map<String, Object> params);

	public AppPlatform findByCode(String app_code);

	public int getCountNew();

	public int getCountAll();
	
	
}
