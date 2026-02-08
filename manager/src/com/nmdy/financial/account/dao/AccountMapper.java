package com.nmdy.financial.account.dao;
import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;

public interface AccountMapper extends SupperMapper {
	public List<Account> search(Map<String, Object> map);
	public int editAccount(Map<String, Object> map);
	public List<TransactionDetail> searchTransInfo(Map<String, Object> map);
	public AccountUserInfo searchAccountUserInfo(Map<String, Object> temp);
	public Map<String, Object> findusername(int userid);//self
	public Map<String, Object> findgroupname(int userid);//self
	public Map<String, Object> findunitname(int userid);//self
	public int addcharge(Map<String, Object> params);//self
	public List<Map<String, Object>> findorder(Map<String, Object> params);//获得选择的订单编号
	public List<Map<String, Object>> findMidPoints(Map<String, Object> params);
	public Map<String, Object> finduser(long id);//查询乘客及车主相关信息
	public Map<String, Object> findtemp(int id);//查询乘客及车主相关信息
	public Map<String, Object> findonoff(int id);//查询乘客及车主相关信息
	public Map<String, Object> findlongdis(int id);//查询乘客及车主相关信息
	public Map<String, Object> findone(int orderexeccsid);//执行订单信息查询
	public int getOrderCount(Map<String, Object> params);//获得查询订单数据的总数
	public List<Map<String, Object>> search1(Map<String, Object> params);
	public int getcountsearch1(Map<String, Object> params);//获得查询订单数据的总数
	public List<Map<String, Object>> finduserinfo(Map<String, Object> params);//个人炕上
	public List<Map<String, Object>> findgroupinfo(Map<String, Object> params);//集团客户
	public List<Map<String, Object>> findunitinfo(Map<String, Object> params);//合作单位
	public int countuserinfo(Map<String, Object> params);//
	public int countgrounpinfo(Map<String, Object> params);//
	public int countunitinfo(Map<String, Object> params);//
	public List<Map<String, Object>> showonoff(String ordernum);//订单详情 上下班订单其他部分的数据表格加载
	public List<Map<String, Object>> showlong(String ordernum);//订单详情 长途订单其他部分的数据表格加载
	public Map<String, Object> carstyle(String ordernum);//查询需求车型
	public Map<String, Object> findday(String ordernum);//查询周几
}
