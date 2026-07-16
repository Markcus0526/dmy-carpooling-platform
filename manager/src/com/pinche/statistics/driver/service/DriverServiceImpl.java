package com.pinche.statistics.driver.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.common.SqlSessionHelper;
import com.pinche.statistics.driver.dao.Driver;
import com.pinche.statistics.driver.dao.DriverMapper;



public class DriverServiceImpl implements DriverService{

	SqlSession session = null;
	DriverMapper mapper = null;
	public DriverServiceImpl() {
		session = SqlSessionHelper.getSession();
		mapper = session.getMapper(DriverMapper.class);
	}

	@Override
	public int countofDriver(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.countofDriver(map);
	}

	@Override
	public int countofDriverReg(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.countofDriverReg(map);
	}

	@Override
	public List<Driver> getDriverInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.getDriverInfo(map);
	}


}
