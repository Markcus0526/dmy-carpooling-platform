package com.nmdy.workStation.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map;

import com.nmdy.entity.UserOnline;
import com.nmdy.workStation.dao.ControlCenterDao;
   /**
    * user_online 实体类
    * Tue Nov 18 11:08:09 CST 2014 bisu
    */ 


public class ControlCenterServiceImpl implements ControlCenterService{
private ControlCenterDao controlCenterDao;
public void setControlCenterDao(ControlCenterDao controlCenterDao){
this.controlCenterDao=controlCenterDao;
}


public UserOnline findById(Long id){
return this.controlCenterDao.findById(id);
}

public int insertControlCenter(UserOnline userOnline){
return this.controlCenterDao.insertControlCenter(userOnline);
}

public int updateControlCenter(UserOnline userOnline){
return this.controlCenterDao.updateControlCenter(userOnline);
}

public int deleteControlCenter(Long id){
return this.controlCenterDao.deleteControlCenter(id);
}

public int selectSecond() {
	return this.controlCenterDao.selectSecond();
}


@Override
public List<UserOnline> selectUnLineAll(Map<String, Object> map) {
	return this.controlCenterDao.selectUnLineAll(map);
}


public int countUnlineAll(Map<String, Object> map) {
	return this.controlCenterDao.countUnlineAll(map);
}


@Override
public List<UserOnline> selectOnlineAll(Map<String, Object> map) {
	return this.controlCenterDao.selectOnlineAll(map);
}


@Override
public int countOnlineAll(Map<String, Object> map) {
	return this.controlCenterDao.countOnlineAll(map);
}


@Override
public List<UserOnline> selectAll(Map<String, Object> map) {
	return this.controlCenterDao.selectAll(map);
}


@Override
public int countAll(Map<String, Object> map) {
	return this.controlCenterDao.countAll(map);
}


}

