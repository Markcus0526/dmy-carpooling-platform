package com.nmdy.workStation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.entity.ClientAccount;
   /**
    * req_client_account 实体类
    * Tue Nov 11 16:33:25 CST 2014 bisu
    */ 


public interface WorkStationDao extends SupperMapper{
List<ClientAccount> selectAll(Map<String,Object> map);
int countAll(Map<String,Object> map);
List<User> selectAllUser(Map<String,Object> map);
int countAllUser(Map<String,Object> map);
int countOrder(int status);
}

