package com.pinche.authority.manager.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import util.excel.pojo.ExcelUser;

import com.pinche.authority.manager.dao.Administrator;
import com.pinche.authority.manager.dao.AdministratorMapper;

public class AdministratorServiceImpl implements AdministratorService {

	private SqlSession session = null;
	private AdministratorMapper mapper = null;
	
	public AdministratorServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(AdministratorMapper.class);
	}
	
	public Administrator findOneById(int id) {
		return mapper.findOneById(id);
	}

	@Override
	public Administrator findOneByUsercode(String usercode) {
		return mapper.findOneByUsercode(usercode);
	}

	@Override
	public int addAdministrator(Administrator info) {
		int rows = mapper.addAdministrator(info);
		if (rows > 0) {
			session.commit();
		}
		return rows;
	}

	@Override
	public int changePassword(Administrator info) {
		int rows = mapper.changePassword(info);
		if (rows > 0) {
			session.commit();
		}
		return rows;
	}

	public int changeLevel(Administrator info) {
		int rows = mapper.changePassword(info);
		if (rows > 0) {
			session.commit();
		}
		return rows;
	}

	@Override
	public int deleteAdministrator(int id) {
		int rows = mapper.deleteAdministrator(id);
		if (rows > 0) {
			session.commit();
		}
		return rows;
	}

	@Override
	public int editAdministrator(Administrator info) {
		int rows = mapper.editAdministrator(info);
		if (rows > 0) {
			session.commit();
		}
		
		return rows;
	}

	@Override
	public List<Administrator> searchByUsercode(String usercode) {
		return mapper.searchByUsercode(usercode);
	}

	@Override
	public List<Administrator> searchByUsername(String username) {
		return mapper.searchByUsername(username);
	}

	@Override
	public List<Administrator> searchByPhone(String phoneNum) {
		return mapper.searchByPhone(phoneNum);
	}

	@Override
	public List<Administrator> searchByUnit(int unit) {
		return mapper.searchByUnit(unit);
	}

	@Override
	public List<Administrator> search(Map<String, Object> map) {
		return mapper.search(map);
	}

	@Override
	public List<Administrator> findAll(Map<String, Object> map) {
		return mapper.findAll(map);
	}

	@Override
	public int sizeofTable() {
		return mapper.sizeofTable();
	}

	@Override
	public boolean login(String usercode, String password) {
		// TODO Auto-generated method stub
		int row = mapper.login(usercode, password);
		if(row>0)
			return true;
		else
		return false;
	}

	@Override
	public int findByRid(int rid) {
		return mapper.findByRid(rid);
	}

	@Override
	public List<Administrator> searchAll(Map<String, Object> map) {
		return mapper.searchAll(map);
	}
	
	public List<ExcelUser> searchForExcel(Map<String,Object> map){
		return mapper.searchForExcel(map);
	}
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
