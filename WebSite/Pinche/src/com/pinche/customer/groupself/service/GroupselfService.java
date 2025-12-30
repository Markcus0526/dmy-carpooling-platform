package com.pinche.customer.groupself.service;

import java.util.List;

import com.pinche.authority.manager.dao.Administrator;
import com.pinche.customer.groupself.dao.Groupself;

public interface GroupselfService {	
	public Groupself findOneByGroupId(String GroupId);
	public int editGroupself(Groupself info);
	public List<Groupself> findAll();
}
