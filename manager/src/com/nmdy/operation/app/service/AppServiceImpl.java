package com.nmdy.operation.app.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.operation.app.dao.App;
import com.nmdy.operation.app.dao.AppMapper;
import com.nmdy.operation.app.dao.AppPlatform;

public class AppServiceImpl implements AppService {
	private AppMapper appMapper;
	@Override

	public List<App> findAll(Map<String, Object> params) {
		
		return appMapper.findall(params);
	}

	@Override
	public int getCount(Map<String, Object> params) {
		
		return appMapper.getCount(params);
	}
		@Override
	public int update(Map<String, Object> params) {
		return appMapper.update(params);
	}
	@Override
	public int delete(Long id) {
		return  appMapper.delete(id);
	}
	@Override
	public int insertAppVersion(Map<String, Object> params2) {
		return appMapper.insertAppVersion(params2);
	}
	@Override
	public int insertApp(Map<String, Object> params1) {
	return appMapper.insertApp(params1);
	}
	@Override
	public App findById(Map<String, Object> map) {
		return appMapper.findById(map);
	}
	@Override
	public int checkAppCode(String app_code) {
		return appMapper.checkAppCode(app_code);
	}
	@Override
	public int updateApp(Map<String, Object> params1) {
		return appMapper.updateApp(params1);
	}
	@Override
	public List<App> findNew(Map<String, Object> params) {
		return appMapper.findNew(params);
	}
	@Override
	public AppPlatform findByCode(String app_code) {
		return appMapper.findByCode(app_code);
	}
	@Override
	public int getCountNew() {
		return appMapper.getCountNew();
	}
	@Override
	public int getCountAll() {
		return appMapper.getCountAll();
	}

	public AppMapper getAppMapper() {
		return appMapper;
	}

	public void setAppMapper(AppMapper appMapper) {
		this.appMapper = appMapper;
	}


}
