package com.nmdy.financial.request.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;


public interface FreezeMapper extends SupperMapper {
	public List<Map<String, Object>> findby (Map<String, Object> params);//数据表格加载与查询
	public int findcount(Map<String,Object> params);//总数
	public int updatedealsingle(int reqid);//处理一个
	public Map<String, Object> findbalance(Map<String,Object> params);//执行订单信息查询
	public int updatefinishsingle(int reqid);//完成一个
	public int updateclosesingle(int reqid);//完成一个
	public int addinsertts(Map<String,Object> params);//点击关闭后向ts表里插入一条记录
	public Map<String, Object> finduserbalance(Map<String, Object> params);
	public Map<String, Object> findgroupbalance(Map<String, Object> params);
	public Map<String, Object> findunitbalance(Map<String, Object> params);
	public Map<String, Object> findfpinfo(Map<String, Object> params);
	public int addupfp(Map<String,Object> params);//关闭解冻
	public int updateupfpstate(Map<String,Object> params);//更新状态为用掉了
}
