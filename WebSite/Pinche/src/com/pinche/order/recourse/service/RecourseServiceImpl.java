package com.pinche.order.recourse.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pinche.order.appoint.dao.Appoint;
import com.pinche.order.appoint.dao.Customer;
import com.pinche.order.appointdetail.dao.Appointdetail;
import com.pinche.order.recourse.dao.OrderSearch;
import com.pinche.order.recourse.dao.Recourse;
import com.pinche.order.recourse.dao.RecourseMapper;
import com.pinche.order.recourse.dao.WorkForm;


public class RecourseServiceImpl implements RecourseService{
	private SqlSession session=null;
	private RecourseMapper mapper=null;
	
	public RecourseServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(RecourseMapper.class);
	}
	
	public List<Recourse> findAll() {
		return mapper.findAll();
	}

	@Override
	public int addRecourse(Map<String, Object> map) {
		int row = mapper.addRecourse(map);
		if(row > 0)
			session.commit();
		return row;
	}

	@Override
	public int editRecourse(Map<String, Object> params) {
		int row = mapper.editRecourse(params);
		if(row>0)
			session.commit();
		return row;
	}

	@Override
	public Recourse findOnebyid(int id) {
		return mapper.findOnebyid(id);
	}

	@Override
	public int deleteRecourse(int id) {
		int rows = mapper.deleteRecourse(id);
		if (rows > 0) {
			session.commit();
		}
		return rows;
	}


	@Override
	public List<Recourse> search(Map<String, Object> map) {
		return mapper.search(map);
	}

	@Override
	public List<Recourse> findPagination(Map<String, Object> map) {
		return mapper.findPagination(map);
	}

	@Override
	public Integer sizeofTable() {
		return mapper.sizeofTable();
	}


	@Override
	public int addAbb_Record(Map<String, Object> map) {
		int row = mapper.addAbb_Record(map);
		if(row > 0)
			session.commit();
		return row;
	}


	@Override
	public int findCountByUsername(Map<String, Object> map) {
		return mapper.findCountByUsername(map);
	}

	@Override
	public Customer findOneByUsername(String customer_name) {
		return mapper.findOneByUsername(customer_name);
	}

	@Override
	public Customer findById(long userid) {
		return mapper.findById(userid);
	}

	@Override
	public WorkForm findDetails(int id) {
		return mapper.findDetails(id);
	}

	@Override
	public int processRecourse(Map<String, Object> params) {
		int row = mapper.processRecourse(params);
		if(row>0)
			session.commit();
		return row;
	}

	@Override
	public Appoint findOrder_num(long order_cs_id) {
		return mapper.findOrder_num(order_cs_id);
	}

	@Override
	public OrderSearch findNameSex(Map<String, Object> m) {
		return mapper.findNameSex(m);
	}

}
