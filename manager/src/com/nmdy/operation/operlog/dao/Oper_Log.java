package com.nmdy.operation.operlog.dao;

import java.sql.Timestamp;


public class Oper_Log {
	//table oper_log
  private long id;
  private long operuserid;
  private String username;
  private Timestamp opertime;
  private String desc;
  private short deleted;
  
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public long getOperuserid() {
	return operuserid;
}
public void setOperuserid(long operuserid) {
	this.operuserid = operuserid;
}

public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public Timestamp getOpertime() {
	return opertime;
}
public void setOpertime(Timestamp opertime) {
	this.opertime = opertime;
}
public String getDesc() {
	return desc;
}
public void setDesc(String desc) {
	this.desc = desc;
}
public short getDeleted() {
	return deleted;
}
public void setDeleted(short deleted) {
	this.deleted = deleted;
}
  
}
