package com.pinche.authority.manager.dao;

import java.util.List;

import com.nmdy.base.SupperMapper;

public interface AuthorityMapper extends SupperMapper {
	public List<Authority> getResourceList(int id);
}
