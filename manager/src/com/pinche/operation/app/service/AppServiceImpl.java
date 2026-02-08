package com.pinche.operation.app.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.operation.app.dao.App;
import com.pinche.operation.app.dao.AppMapper;
import com.pinche.operation.app.dao.AppPlatform;

public class AppServiceImpl implements AppService {
	private SqlSession session = null;
    private AppMapper appMapper=null;
   
	public AppServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		appMapper=session.getMapper(AppMapper.class);
	}
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
		int row =appMapper.update(params);
		if(row>0){
			session.commit();
		}
		return row;
	}
	@Override
	public int delete(Long id) {
		int row= appMapper.delete(id);
		if(row>0){
			session.commit();
		}
		return row;
	}
	@Override
	public int insertAppVersion(Map<String, Object> params2) {
		int row =appMapper.insertAppVersion(params2);
		if(row>0){
			session.commit();
		}
		return row;
	}
	@Override
	public int insertApp(Map<String, Object> params1) {
		int row =appMapper.insertApp(params1);
		if(row>0){
			session.commit();
		}
		return row;
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
		int row =appMapper.updateApp(params1);
		if(row>0){
			session.commit();
		}
		return row;
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


}
