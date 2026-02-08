package com.nmdy.order.recourse.service;

import java.util.List;
import java.util.Map;

import com.nmdy.order.appoint.dao.Appoint;
import com.nmdy.order.appoint.dao.Customer;
import com.nmdy.order.recourse.dao.OrderSearch;
import com.nmdy.order.recourse.dao.Recourse;
import com.nmdy.order.recourse.dao.RecourseMapper;
import com.nmdy.order.recourse.dao.WorkForm;


public class RecourseServiceImpl implements RecourseService{
	private RecourseMapper recourseMapper;
	
	public List<Recourse> findAll() {
		return recourseMapper.findAll();
	}

	@Override
	public int addRecourse(Map<String, Object> map) {
		return  recourseMapper.addRecourse(map);
	}

	@Override
	public int updateRecourse1(Map<String, Object> params) {
		return  recourseMapper.editRecourse(params);

	}

	@Override
	public Recourse findOnebyid(int id) {
		return recourseMapper.findOnebyid(id);
	}

	@Override
	public int deleteRecourse(int id) {
		return  recourseMapper.deleteRecourse(id);
	}


	@Override
	public List<Recourse> search(Map<String, Object> map) {
		return recourseMapper.search(map);
	}

	@Override
	public List<Recourse> findPagination(Map<String, Object> map) {
		return recourseMapper.findPagination(map);
	}

	@Override
	public Integer sizeofTable() {
		return recourseMapper.sizeofTable();
	}


	@Override
	public int addAbb_Record(Map<String, Object> map) {
		return  recourseMapper.addAbb_Record(map);
	}


	@Override
	public int findCountByUsername(Map<String, Object> map) {
		return recourseMapper.findCountByUsername(map);
	}

	@Override
	public Customer findOneByUsername(String customer_name) {
		return recourseMapper.findOneByUsername(customer_name);
	}

	@Override
	public Customer findById(long userid) {
		return recourseMapper.findById(userid);
	}

	@Override
	public WorkForm findDetails(int id) {
		return recourseMapper.findDetails(id);
	}

	@Override
	public int updateRecourse2(Map<String, Object> params) {
		return  recourseMapper.processRecourse(params);
	}

	@Override
	public Appoint findOrder_num(long order_cs_id) {
		return recourseMapper.findOrder_num(order_cs_id);
	}

	@Override
	public OrderSearch findNameSex(Map<String, Object> m) {
		return recourseMapper.findNameSex(m);
	}

	public RecourseMapper getRecourseMapper() {
		return recourseMapper;
	}

	public void setRecourseMapper(RecourseMapper recourseMapper) {
		this.recourseMapper = recourseMapper;
	}

	@Override
	public String findCityName(String order_city) {
		return recourseMapper.findCityName(order_city);
	}

}
