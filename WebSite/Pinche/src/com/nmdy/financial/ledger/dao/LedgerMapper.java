package com.nmdy.financial.ledger.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;


public interface LedgerMapper extends SupperMapper {
public List<Map<String, Object>> findledger(Map<String, Object> params);
public int gotCount(Map<String, Object> params);
public List<Map<String, Object>> findMidPoints(Map<String, Object> params);
public Map<String, Object> finduser(long id);//查询乘客及车主相关信息
public Map<String, Object> findtemp(int id);//查询乘客及车主相关信息
public Map<String, Object> findonoff(int id);//查询乘客及车主相关信息
public Map<String, Object> findlongdis(int id);//查询乘客及车主相关信息
public Map<String, Object> findone(int orderexeccsid);//执行订单信息查询
public List<Map<String, Object>> showonoff(String ordernum);//订单详情 上下班订单其他部分的数据表格加载
public List<Map<String, Object>> showlong(String ordernum);//订单详情 长途订单其他部分的数据表格加载
public Map<String, Object> carstyle(String ordernum);//查询需求车型
public Map<String, Object> findday(String ordernum);//查询周几
public Map<String, Object> findts(int id);//查询绿点是否冻结
public Map<String, Object> sumbalance();//查询绿点是否冻结
public Map<String, Object> finduserinvit(Map<String, Object> params);//查询user里面的邀请人
public Map<String, Object> findgroupinvit(Map<String, Object> params);//查询group里面的邀请人
public Map<String, Object> findunitinvit(Map<String, Object> params);//查询unit里面的邀请人

}
