package com.nmdy.operation.notify.service;

import java.util.List;
import java.util.Map;

import com.nmdy.operation.notify.dao.NotifyEntity;


public interface NotifyService {
	public List<NotifyEntity> finduser(Map<String, Object> params);
	public int getTotal(Map<String, Object> params);
	public List<Integer> findids(Map<String, Object> params);
	public int addbatchInsert(Map<String, Object> params);
//	public boolean addinsert(String title,String msg,String receiver);
//	public boolean addinsertall(String title,String msg,List receivers);



}
