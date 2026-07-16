package com.pinche.customer.user.dao;

import java.util.List;
import java.util.Map;

public interface CustomerMapper {

	public List<Customer> findAll();
	public Customer findOneById(int id);
	public Customer findOneByGid(int gid);
	public Customer findOneByUsercode(String usercode);
	public Customer findOneByUsername(Map<String, Object> map);
	
	public List<UserBasicInfo> findUserBasicInfo(Map<String, Object> map);
	
	public List<Customer> findAllUsers();
	public List<Customer> searchNumberspread(Map<String, Object> map);
	public int updateNumberspread(Customer customer);
	public int updateTsid(Map<String, Object> map);
}
