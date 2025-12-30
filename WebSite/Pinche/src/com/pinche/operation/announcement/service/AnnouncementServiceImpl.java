package com.pinche.operation.announcement.service;


import java.util.*;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.pinche.authority.manager.dao.UserRoleMapper;
import com.pinche.operation.announcement.dao.AnnouncementMapper;
import com.pinche.operation.announcement.dao.Announcement;
/**
* 
* @ClassName: AnnouncementServiceImpl.java
* @Description: announcement
* @author haotang365
* @date 2014-8-18 10:16:33
* @version V1.0   
*/
	
public class AnnouncementServiceImpl implements AnnouncementService{
		
		private SqlSession session = null;
		private AnnouncementMapper announcementMapper = null;

		public AnnouncementServiceImpl() {
			session = com.pinche.common.SqlSessionHelper.getSession();
			announcementMapper = session.getMapper(AnnouncementMapper.class);
		}
		/**
		 *
		 * 获取全部数据
		 *
		 * @param map查询条件
		 * @return 返回检索结果List<Announcement>
		*/
		public List<Announcement> selectList(Map<String, Object> map) {
			return announcementMapper.selectList(map);
		}

		/**
		 *
		 * 获取全部数据
		 *
		 * @param map查询条件
		 * @return 返回检索结果List<Map<String, Object>>
		*/
		public List<Map<String, Object>> selectListMap(Map<String, Object> map) {
			return announcementMapper.selectListMap(map);
		}

		/**
		 *
		 * 根据视图ID取得数据 [视图]
		 *
		 * @param ID值
		 * @return 返回检索结果
		*/
		public Announcement getAnnouncementById(Integer primaryId) {
			if (primaryId == null) {
				return null;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("primaryIdEqual", primaryId);
			List<Announcement> announcementList = announcementMapper.selectList(map);
			if (announcementList == null || announcementList.size() == 0) {
				return null;
			}
			return announcementList.get(0);
		}
		
		/**
		 *
		 * 根据视图ID取得数据(更新用) [视图]
		 *
		 * @param 视图ID
		 * @return 返回检索结果
		*/
		public Announcement getAnnouncementByIdForUpdate(Integer primaryId) {
			if (primaryId == null) {
				return null;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("primaryIdEqual", primaryId);
			List<Announcement> announcementList = announcementMapper.selectListForUpdate(map);
			if (announcementList == null || announcementList.size() == 0) {
				return null;
			}
			return announcementList.get(0);
		}
		

		/**
		 *
		 * 更新数据
		 *
		 * @param Announcement 需要更新的数据行
		 * @return 返回受影响的数据行数
		*/
		public int update(Announcement announcement) {
			if (announcement == null) {
				return 0;
			}
			return announcementMapper.update(announcement);
		}

		/**
		 *
		 * 插入数据
		 *
		 * @param Announcement 需要插入的数据行
		 * @return 返回受影响的数据行数
		*/
		public int insert(Announcement announcement) {
			if (announcement == null) {
				return 0;
			}
			int rows = announcementMapper.insert(announcement);
			if(rows > 0){
				session.commit();
			}
			return rows;
		}

		
		public int updateAnnouncementDisable(String id) {
			Announcement announcement = new Announcement();
			announcement.setId(Integer.parseInt(id));
			announcement.setDeleted(1);
			int num = announcementMapper.updateAnnouncementDisable(announcement);
			if(num > 0){
				session.commit();
			}
			return num;
		}

		

		
		
		/**
		 * 获得数据总条数
		 *
		 * @param map
		 * @return 数据条数
		 */
		public int selectCount(Map<String, Object> map){
			return announcementMapper.selectCount(map);
		}

		/**
		 * 取出所有主键,返回以逗号分隔的字符串
		 *
		 * @param map
		 * @return 以逗号分隔的字符串
		 */
		public String selectAllPrimaryKey(Map<String, Object> map){
			return announcementMapper.selectAllPrimaryKey(map);
		}

		public AnnouncementMapper getAnnouncementMapper() {
			return announcementMapper;
		}

		public void setAnnouncementMapper(AnnouncementMapper announcementMapper) {
			this.announcementMapper = announcementMapper;
		}

		
		
}
