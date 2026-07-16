package com.nmdy.order.recourse.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.order.appoint.dao.Appoint;
import com.nmdy.order.appoint.dao.Customer;

public interface RecourseMapper extends SupperMapper {
	public List<Recourse> findAll();	
	public int addRecourse(Map<String, Object> map);
	public int editRecourse(Map<String, Object> params);
	public Recourse findOnebyid(int id);
	public int deleteRecourse(int id);
	public int processRecourse(Map<String, Object> params);
	public List<Recourse> search(Map<String, Object> map);
	public List<Recourse> findPagination(Map<String, Object> map);
	public Integer sizeofTable();
	public int addAbb_Record(Map<String, Object> map);
	public Customer findOneByUsername(String customer_name);
	public int findCountByUsername(Map<String, Object> map);
	public Customer findById(long userid);
	public WorkForm findDetails(int id);
	public Appoint findOrder_num(long order_cs_id);
	public OrderSearch findNameSex(Map<String, Object> m);
	public String findCityName(String order_city);
}