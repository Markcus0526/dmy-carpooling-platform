package com.pinche.customer.user.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.customer.user.dao.Customer;
import com.pinche.customer.user.dao.CustomerMapper;
import com.pinche.customer.user.dao.UserBasicInfo;

public class CustomerServiceImpl implements CustomerService{

	private SqlSession session = null;
	private CustomerMapper mapper = null;

	public CustomerServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(CustomerMapper.class);
	}
	
	@Override
	public List<Customer> findAll() {
		return mapper.findAll();
	}

	@Override
	public Customer findOneById(int id) {
		return mapper.findOneById(id);
	}

	@Override
	public Customer findOneByUsercode(String usercode) {
		return mapper.findOneByUsercode(usercode);
	}


	@Override
	public Customer findOneByGid(int gid) {
		return mapper.findOneByGid(gid);
	}

	@Override
	public List<UserBasicInfo> findUserBasicInfo(Map<String, Object> map) {
		return mapper.findUserBasicInfo(map);
	}

	@Override
	public List<Customer> findAllUsers() {
		return mapper.findAllUsers();
	}

	@Override
	public List<Customer> searchNumberspread(Map<String, Object> map) {
		return mapper.searchNumberspread(map);
	}

	@Override
	public int updateNumberspread(Customer customer) {
		int rows = mapper.updateNumberspread(customer);
		if (rows > 0)
			session.commit();
		return rows;
	}

	@Override
	public int updateTsid(Map<String, Object> map) {
		int rows = mapper.updateTsid(map);
		if (rows > 0)
			session.commit();
		return rows;
	}

	@Override
	public Customer findOneByUsername(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.findOneByUsername(map);
	}

}
