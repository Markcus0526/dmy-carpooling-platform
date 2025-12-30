package com.pinche.statistics.driver.dao;

import java.util.List;
import java.util.Map;

public interface DriverMapper {
	public int countofDriver(Map<String,Object> map);
	public int countofDriverReg(Map<String, Object> map);
	public List<Driver> getDriverInfo(Map<String, Object> map);
}
