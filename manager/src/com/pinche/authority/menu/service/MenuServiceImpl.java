package com.pinche.authority.menu.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.authority.menu.dao.Menu;
import com.pinche.authority.menu.dao.MenuMapper;

public class MenuServiceImpl implements MenuService {
	private SqlSession session = null;
	private MenuMapper mapper = null;
	
	public MenuServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(MenuMapper.class);
	}
	
	@Override
	public Menu findOneById(int id) {
		return mapper.findOneById(id);
	}

	@Override
	public List<Menu> findAll() {
		return mapper.findAll();
	}

	@Override
	public Menu findOneByCode(Map<String, Object> map) {
		return mapper.findOneByCode(map);
	}

	@Override
	public List<Menu> findAllByMenucode(int menucode) {
		return mapper.findAllByMenucode(menucode);
	}

	@Override
	public List<Menu> findChildren(int parent) {
		return mapper.findChildren(parent);
	}

	@Override
	public List<Menu> findAllByRole(int rid) {
		return mapper.findAllByRole(rid);
	}

}
