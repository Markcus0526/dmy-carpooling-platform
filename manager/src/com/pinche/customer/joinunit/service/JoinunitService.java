package com.pinche.customer.joinunit.service;

import java.util.List;
import java.util.Map;

import com.pinche.customer.joinunit.dao.Joinunit;

public interface JoinunitService {

	public List<Joinunit> findAll();
	public Joinunit findById(int id);

	public int updateTsid(Map<String, Object> map);
}
