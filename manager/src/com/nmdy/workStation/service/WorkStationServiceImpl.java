package com.nmdy.workStation.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import util.PieChartDemo;

import com.nmdy.workStation.dao.WorkStationDao;
import com.nmdy.workStation.service.WorkStationService;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.entity.ClientAccount;
   /**
    * req_client_account 实体类
    * Tue Nov 11 16:33:25 CST 2014 bisu
    */ 


public class WorkStationServiceImpl implements WorkStationService{
private WorkStationDao workStationDao;
public WorkStationDao getWorkStationDao() {
	return workStationDao;
}
public void setWorkStationDao(WorkStationDao workStationDao){
this.workStationDao=workStationDao;
}

public List<ClientAccount> selectAll(Map<String,Object> map){
return this.workStationDao.selectAll(map);
}

public int countAll(Map<String,Object> map){
return this.workStationDao.countAll(map);
}

public List<User> selectAllUser(Map<String,Object> map){
	return this.workStationDao.selectAllUser(map);
}

public int countAllUser(Map<String,Object> map){
	return this.workStationDao.countAllUser(map);
}
@Override
public int countOrder(int status) {
	return this.workStationDao.countOrder(status);
}

public boolean doPic(String path){
	//正常完成
		int donei = this.workStationDao.countOrder(7);
		//执行中
		int doi = this.workStationDao.countOrder(6);
		//乘客销单
		int xiaodani = this.workStationDao.countOrder(8);
		//待执行
		int daizhixingi = this.workStationDao.countOrder(3);
		//未接单
		int weijiedan = this.workStationDao.countOrder(0);
		int[] shu = new int[]{donei,doi,xiaodani,daizhixingi,weijiedan};
		try {
			PieChartDemo.chansheng(shu,path);
		} catch (IOException e) {
			return false;
		}
		return true;
}
}

