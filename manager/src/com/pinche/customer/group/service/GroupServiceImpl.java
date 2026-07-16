package com.pinche.customer.group.service;

import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nmdy.activity.dao.ActivityDao;
import com.pinche.customer.group.dao.Group;
import com.pinche.customer.group.dao.GroupMapper;






public class GroupServiceImpl implements GroupService {

	private SqlSession session = null;
	private GroupMapper mapper = null;
	
	public GroupServiceImpl() {
		session = com.pinche.common.SqlSessionHelper.getSession();
		mapper = session.getMapper(GroupMapper.class);
	}
	
	private ActivityDao activityDao;
	
	public ActivityDao getActivityDao() {
		return activityDao;
	}

	public void setActivityDao(ActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	@Override
	public int addItem(Group item) {
		// TODO Auto-generated method stub
		return mapper.addItem(item);
	}

	@Override
	public int removeItem(int id) {
		// TODO Auto-generated method stub
		int rows = mapper.removeItem(id);
		if (rows > 0) {
			session.commit();
		}
		return rows;
	}

	@Override
	public List<Group> findAll() {
		// TODO Auto-generated method stub
		return mapper.findAll();
	}

	@Override
	public int editGroup(Group info) {
		// TODO Auto-generated method stub
		int rows=mapper.editGroup(info);
		if (rows > 0) {
			session.commit();
		}
		return rows;
	}

	@Override
	public Group findById(int id) {
		return mapper.findById(id);
	}

	@Override
	public int updateTsid(Map<String, Object> map) {
		int rows = mapper.updateTsid(map);
		if (rows > 0)
			session.commit();
		return rows;
	}

	public List<Group> findRoleGroup(int roleId){
		return mapper.findRoleGroup(roleId);
	}
	
	public List<Group> findRoleCheckedGroup(int roleId){
		List<Group> listAll = mapper.findAll();
		List<Group> listRoleGroup = mapper.findRoleGroup(roleId);
		for(Group group:listAll){
			if(null==listRoleGroup||listRoleGroup.isEmpty()){
				group.setChecked(false);
			}else{
				for(Group g:listRoleGroup){
					group.setChecked(false);
					if(group.getId()==g.getGroupid()){
						group.setChecked(true);
						break;
					}
				}
			}
		}
		return listAll;
	}
	
	public int updateRoleGroup(int roleId,String groupIds){
		this.mapper.deleteGroupByRoleId(roleId);
		String[] ids = groupIds.split(",");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(String id:ids){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("roleId",roleId);
			map.put("groupId", id);
			list.add(map);
		}
		return this.mapper.addGroupBatch(list);
	}
	
	public int findMaxId(){
		return this.activityDao.findMaxId();
	}
}
