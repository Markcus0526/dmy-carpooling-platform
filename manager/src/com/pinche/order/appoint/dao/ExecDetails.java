package com.pinche.order.appoint.dao;

import java.util.Date;

/**
 * 用来接收查询到的执行信息详情
 * 放在修改订单页面的执行订单详情上
 * @author xcnana
 *
 */
public class ExecDetails {
   public Long id;
   public Date begin_exec_time;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Date getBegin_exec_time() {
	return begin_exec_time;
}
public void setBegin_exec_time(Date begin_exec_time) {
	this.begin_exec_time = begin_exec_time;
}
   
}
