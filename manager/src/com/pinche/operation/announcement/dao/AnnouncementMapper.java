package com.pinche.operation.announcement.dao;


import java.util.*;

import com.pinche.operation.announcement.dao.Announcement;

/**
* 
* @ClassName: AnnouncementMapper.java   主键
* @Description: announcement   
* @author haotang365
* @date 2014-8-18 10:16:33
* @version V1.0   
*/

public interface AnnouncementMapper {

	// 基本方法（生成）
	public int insert(Announcement announcement);
	public int delete(Map<String,Object> map);
	public int update(Announcement announcement);
	public List<Announcement> selectList(Map<String,Object> map);
	public int selectCount(Map<String,Object> map);
	public List<Announcement> selectListForUpdate(Map<String,Object> map);
	public int getMaxSequence(Map<String,Object> map);
	public int updateAnnouncementDisable(Announcement announcement);
	public List<Map<String,Object>> selectListMap(Map<String,Object> map);
	public String selectAllPrimaryKey(Map<String,Object> map);

}

