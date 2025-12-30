package com.nmdy.financial.request.service;

import java.util.List;
import java.util.Map;
public interface FreezeService {
	public List<Map<String, Object>> findby(Map<String, Object> params);
	public int findcount(Map<String,Object> params);
	public int updatedealsingle(int reqid);//处理一个
	public int updatefinishsingle(int reqid);//完成一个
	public int updateclosesingle(int reqid);//关闭一个
	public int addinsertts(Map<String,Object> params);//点击关闭后向ts表里插入一条记录
	public Map<String, Object> findbalance(Map<String,Object> params);//执行订单信息查询
	public Map<String, Object> finduserbalance(Map<String, Object> params);
	public Map<String, Object> findgroupbalance(Map<String, Object> params);
	public Map<String, Object> findunitbalance(Map<String, Object> params);
	public Map<String, Object> findfpinfo(Map<String, Object> params);
	public int addupfp(Map<String,Object> params);//关闭解冻
	public int updateupfpstate(Map<String,Object> params);//更新状态为用掉了

}
