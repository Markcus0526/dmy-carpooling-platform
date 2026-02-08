package com.pinche.customer.groupself.dao;

import java.util.List;

public interface GroupselfMapper {
	public Groupself findOneByGroupId(String groupid);
	public int editGroupself(Groupself info);
	public List<Groupself> findAll();
}
