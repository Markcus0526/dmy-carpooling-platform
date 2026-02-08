package com.pinche.authority.menu.dao;

import java.util.List;
import java.util.Map;

public interface MenuMapper {

	public List<Menu> findAll();
	public List<Menu> findAllByRole(int rid);
	public Menu findOneById(int id);
	public Menu findOneByCode(Map<String, Object> map);
	public List<Menu> findAllByMenucode(int menucode);
	public List<Menu> findChildren(int parent);

}
