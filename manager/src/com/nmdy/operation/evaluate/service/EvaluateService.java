package com.nmdy.operation.evaluate.service;

import java.util.List;
import java.util.Map;

import com.nmdy.operation.evaluate.dao.EvaluateEntity;
import com.nmdy.operation.evaluate.dao.HelpEntity;




public interface EvaluateService {
	public List<EvaluateEntity> find(Map<String, Object> params);//页面查询
	public int getCount(Map<String, Object> params);
	public EvaluateEntity findbyid(int userid);
	public  List<EvaluateEntity> findbyid2(Map<String, Object> params);
	public int update(Map<String, Object> params);//修改评价
	public int getCount2(Map<String, Object> params);
	 public int updatedel(Map<String, Object> params);//屏蔽
	 public  HelpEntity findbyid1(int userid);//每个客户的好评率查询
	 public Map<String, Object> findmsg(Map<String, Object> params);
}
