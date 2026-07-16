package com.pinche.statistics.driver.service;

import java.util.List;
import java.util.Map;

import com.pinche.statistics.driver.dao.Driver;

public interface DriverService {

	int countofDriver(Map<String, Object> map);

	public int countofDriverReg(Map<String, Object> map);

	public List<Driver> getDriverInfo(Map<String, Object> map);
	
}
