package com.pinche.spread.number.service;

import java.util.List;
import java.util.Map;

import com.pinche.customer.user.dao.Customer;
import com.pinche.customer.user.dao.UserBasicInfo;
import com.pinche.spread.number.dao.Profitconfig;

public interface ProfitconfigService {
	public List<Profitconfig> findAllPersons();
	public Profitconfig findLast();
	public Profitconfig findById(int id);
	public int deleteAll();
	public int update(Profitconfig profitconfig);
	
	public Profitconfig findOneById(int id);
	public Profitconfig findOneByGid(int gid);
	public Profitconfig findOneByUsercode(String usercode);

	public List<Profitconfig> findAll();
	public List<Profitconfig> findUserBasicInfo(Map<String, Object> map);

	public List<Profitconfig> findAllUsers();
	public List<Profitconfig> searchNumberspread(Map<String, Object> map);
	public int updateNumberspread(Profitconfig customer);
	public int updateTsid(Map<String, Object> map);
	public Profitconfig findOneByUsername(Map<String, Object> map);
}
