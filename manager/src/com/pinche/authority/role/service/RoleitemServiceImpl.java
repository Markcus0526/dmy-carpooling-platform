package com.pinche.authority.role.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.authority.role.dao.Roleitem;
import com.pinche.authority.role.dao.RoleitemMapper;

public class RoleitemServiceImpl implements RoleitemService {

	private SqlSession session = null;
	private RoleitemMapper mapper = null;
	
	public RoleitemServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(RoleitemMapper.class);
	}
	
	@Override
	public List<Roleitem> findAllByRid(int rid) {
		return mapper.findAllByRid(rid);
	}

	@Override
	public int addItems(Map<String, Object> map) {
		int rows = mapper.addItems(map);
		if (rows > 0)
			session.commit();
		return rows;
	}

	@Override
	public int editItem(Roleitem item) {
		int rows = mapper.editItem(item);
		if (rows > 0)
			session.commit();
		return rows;
	}

	@Override
	public Roleitem findOneById(int id) {
		return mapper.findOneById(id);
	}

	@Override
	public int deleteItems(int rid) {
		int rows = mapper.deleteItems(rid);
		if (rows > 0)
			session.commit();
		return rows;
	}

}
