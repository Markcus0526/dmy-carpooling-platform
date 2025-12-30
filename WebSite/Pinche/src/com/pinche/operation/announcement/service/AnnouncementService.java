package com.pinche.operation.announcement.service;

import java.util.List;
import java.util.Map;

import com.pinche.operation.announcement.dao.Announcement;

public interface AnnouncementService {
	
	/**
	 *
	 * 根据视图ID取得数据(更新用) [视图]
	 *
	 * @param 视图ID
	 * @return 返回检索结果
	*/
	public Announcement getAnnouncementByIdForUpdate(Integer primaryId) ;
	

	/**
	 *
	 * 更新数据
	 *
	 * @param Announcement 需要更新的数据行
	 * @return 返回受影响的数据行数
	*/
	public int update(Announcement announcement);

	/**
	 *
	 * 插入数据
	 *
	 * @param Announcement 需要插入的数据行
	 * @return 返回受影响的数据行数
	*/
	public int insert(Announcement announcement) ;



	
	
	/**
	 * 获得数据总条数
	 *
	 * @param map
	 * @return 数据条数
	 */
	public int selectCount(Map<String, Object> map);

	/**
	 * 取出所有主键,返回以逗号分隔的字符串
	 *
	 * @param map
	 * @return 以逗号分隔的字符串
	 */
	public String selectAllPrimaryKey(Map<String, Object> map);


	public List<Map<String, Object>> selectListMap( Map<String, Object> searchConditionMap);


	public int updateAnnouncementDisable(String id);
}
