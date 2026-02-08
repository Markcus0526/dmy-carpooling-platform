package com.pinche.authority.manager.service;

import java.util.List;
import com.pinche.authority.manager.dao.Authority;

public interface AuthorityService {
	public List<Authority> getResourceList(int id);
}
