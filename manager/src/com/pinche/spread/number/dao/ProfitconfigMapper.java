package com.pinche.spread.number.dao;

import java.util.List;
import java.util.Map;

public interface ProfitconfigMapper {
	public List<Profitconfig> findAll();
	public Profitconfig findLast();
	public Profitconfig findById(int id);
	public int deleteAll();

	public int update(Profitconfig profitconfig);
	
	public List<Profitconfig> findAllPersons();
	
	public Profitconfig findOneById(int id);
	public Profitconfig findOneByGid(int gid);
	public Profitconfig findOneByUsercode(String usercode);
	public Profitconfig findOneByUsername(Map<String, Object> map);
	
	public List<Profitconfig> findUserBasicInfo(Map<String, Object> map);
	
	public List<Profitconfig> findAllUsers();
	public List<Profitconfig> searchNumberspread(Map<String, Object> map);
	public int updateNumberspread(Profitconfig customer);
	public int updateTsid(Map<String, Object> map);
}
