package com.nmdy.operation.evaluate.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
public interface EvaluateMapper extends SupperMapper{
	/*
	 * 查询方法 根据条件来查询
	 */
	public List<EvaluateEntity> finduser(Map<String, Object> params);
	public int getTotal(Map<String, Object> params);//获得评价管理页面表格的总数
	public  EvaluateEntity findbyid(int userid);
	//以下是详细评价里面的方法
     public  List<EvaluateEntity> findbyid2(Map<String, Object> params);//点击查看详情 跳转到详情页面加载时的方法
     public int update(Map<String, Object> params);//修改
     public int getTotal2(Map<String, Object> params);//总数
     public int updatedel(Map<String, Object> params);//屏蔽
     public  HelpEntity findbyid1(int userid);//查询出每个用户好评率等信息的方法
     public Map<String, Object> findmsg(Map<String, Object> params);
}
 