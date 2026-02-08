package com.nmdy.operation.share.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
public interface ShareMapper extends SupperMapper{
	public List<ShareEntity> findvalue();
	public int update1(String  value);//更新id=3
	public int update2(String  value);//更新id=4
	public int update3(String  value);//更新id=5

}
