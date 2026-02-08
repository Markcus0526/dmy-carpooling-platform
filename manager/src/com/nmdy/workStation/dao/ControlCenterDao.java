package com.nmdy.workStation.dao;

import java.util.Map;
import java.util.List;

import com.nmdy.base.SupperMapper;
import com.nmdy.entity.UserOnline;
   /**
    * user_online 实体类
    * Tue Nov 18 11:08:09 CST 2014 bisu
    */ 


public interface ControlCenterDao extends SupperMapper{
	List<UserOnline> selectUnLineAll(Map<String,Object> map);
	int countUnlineAll(Map<String,Object> map);
	List<UserOnline> selectOnlineAll(Map<String,Object> map);
	int countOnlineAll(Map<String,Object> map);
	List<UserOnline> selectAll(Map<String,Object> map);
	int countAll(Map<String,Object> map);
	UserOnline findById(Long id);
	int insertControlCenter(UserOnline userOnline);
	int updateControlCenter(UserOnline userOnline);
	int deleteControlCenter(Long id);
	int selectSecond();
}

