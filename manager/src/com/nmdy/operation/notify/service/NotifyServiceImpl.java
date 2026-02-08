package com.nmdy.operation.notify.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.operation.notify.dao.NotifyEntity;
import com.nmdy.operation.notify.dao.NotifyMapper;





public class NotifyServiceImpl implements NotifyService{
	private NotifyMapper notifymapper ;

	public NotifyMapper getNotifymapper() {
		return notifymapper;
	}

	public void setNotifymapper(NotifyMapper notifymapper) {
		this.notifymapper = notifymapper;
	}

	@Override
	public List<NotifyEntity> finduser(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return notifymapper.finduser(params);
	}

	@Override
	public int getTotal(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return notifymapper.getTotal(params);
	}

	@Override
	public List<Integer> findids(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return notifymapper.findids(params);
	}

	@Override
	public int addbatchInsert(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return notifymapper.addbatchInsert(params);
	}

//	@Override
//	public boolean addinsert(String title,String msg,String receiver) {
//		con = sqlsession.getConnection();
//	
//		try {
//			String sql = "insert into notify_person (title,msg,msg_type,receiver,ps_date) "
//					+ "values(?,?,5,?,now())";
//			PreparedStatement ps = con.prepareStatement(sql);
//			if (null != receiver && null != title && null != msg) {
////				System.out.println("receiver1" + receiver);
////				System.out.println("title1"+title);
//				
//				String receivers[] = receiver.split(",");
//				for (int i = 0; i < receivers.length; i++) {
////					System.out.println("receivers[i]" + receivers[i]);
//					ps.setObject(1,title);
//					ps.setObject(2, msg);
//					ps.setObject(3, receivers[i]);
//	//				System.out.println("ps"+ps.toString());
//					ps.addBatch();
//					
//				}
//				ps.executeBatch();
//				con.commit();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		return true;
//	}
//
//	@Override
//	public boolean addinsertall(String title, String msg,List receivers) {
//		// TODO Auto-generated method stub
//		//Connection con = sqlsession.getConnection();
//		con = sqlsession.getConnection();
//		String sql = "insert into notify_person (title,msg,msg_type,receiver,ps_date) "
//				+ "values(?,?,5,?,now())";
//		try {
//			PreparedStatement ps = con.prepareStatement(sql);
//			if (receivers.size()>0 && !"".equals(title) &&!"".equals(msg)) {				
//				for(int i=0;i<receivers.size();i++){
//					int recriver = (int) receivers.get(i);
//					System.out.println("receiver2"+recriver);
//					ps.setObject(1,title);
//					ps.setObject(2, msg);
//					ps.setObject(3, recriver);
//					ps.addBatch();
//					System.out.println("title2"+title);
//					System.out.println("msg2"+msg);
//	//				System.out.println("ps"+ps.toString());									
//				}
//				ps.executeBatch();	
//				con.commit();	
//				}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			try {
//				con.rollback();
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				
//			}
//		}finally {
//			try {
//				con.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		return true;
//	}
//



}
