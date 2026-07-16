package com.pinche.customer.user.service;

import java.util.List;
import java.util.Map;

import com.pinche.customer.user.dao.Customer;
import com.pinche.customer.user.dao.UserBasicInfo;

public interface CustomerService {

	public List<Customer> findAll();
	public Customer findOneById(int id);
	public Customer findOneByGid(int gid);
	public Customer findOneByUsercode(String usercode);

	public List<UserBasicInfo> findUserBasicInfo(Map<String, Object> map);

	public List<Customer> findAllUsers();
	public List<Customer> searchNumberspread(Map<String, Object> map);
	public int updateNumberspread(Customer customer);
	public int updateTsid(Map<String, Object> map);
	public Customer findOneByUsername(Map<String, Object> map);
}
