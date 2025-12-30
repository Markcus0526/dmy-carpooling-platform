package com.pinche.customer.groupself.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.pinche.customer.groupself.dao.Groupself;
import com.pinche.customer.groupself.dao.GroupselfMapper;

public class GroupselfServiceImpl implements GroupselfService {

	private SqlSession session = null;
	private GroupselfMapper mapper = null;
	
	public GroupselfServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(GroupselfMapper.class);
	}
	
	@Override
	public Groupself findOneByGroupId(String GroupId) {
		// TODO Auto-generated method stub
		return mapper.findOneByGroupId(GroupId);
	}

	@Override
	public int editGroupself(Groupself info) {
		// TODO Auto-generated method stub
		return mapper.editGroupself(info);
	}

	@Override
	public List<Groupself> findAll() {
		// TODO Auto-generated method stub
		return mapper.findAll();
	}

}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
