package com.pinche.customer.joinunit.dao;

import java.util.List;
import java.util.Map;

public interface JoinunitMapper {

	public List<Joinunit> findAll();
	public Joinunit findById(int id);
	public int updateTsid(Map<String, Object> map);
}
