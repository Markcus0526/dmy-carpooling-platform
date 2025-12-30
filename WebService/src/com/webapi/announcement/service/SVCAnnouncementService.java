package com.webapi.announcement.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCAnnouncement;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCUser;

public class SVCAnnouncementService {

	/**
	 * 问题：这里的source参数是干什么用的？token是干什么用的？
	 * 
	 * @param source
	 * @param userid
	 * @param city
	 * @param driververif
	 * @param limitid
	 * @param devtoken
	 * @return
	 */
	public SVCResult getLatestAnnouncements(String source, long userid,
			String city, int driververif, long limitid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + city + "," + driververif + "," + limitid + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || driververif < 0 || limitid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		}

		if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		}

		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
			if (userinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_AuthenticateError;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			

			JSONArray arrAncs = new JSONArray();
			stmt = dbConn.createStatement();

			String szCity = userinfo.city_cur;

			SVCAnnouncement limit_item = ApiGlobal
					.getAnnouncementFromID(dbConn, limitid);
			szQuery = "SELECT * FROM announcement WHERE deleted = 0 AND (ps_city LIKE \"%"
					+ szCity
					+ "%\" OR ps_city='') AND "
					+ "validate >= \""
					+ ApiGlobal.Date2String(new Date(), true) + "\"";

			if (limit_item != null)
				szQuery += " AND ps_date > \""
						+ ApiGlobal.Date2String(limit_item.ps_date, true)
						+ "\"";

			if (driververif == 0) // This is not driver verified user. Must
									// not contain even those news for
									// driver.
				szQuery += " AND 'range' = 0";

			szQuery += " ORDER BY ps_date DESC";

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCAnnouncement anc_item = SVCAnnouncement
						.decodeFromResultSet(rs);

				JSONObject jsonItem = new JSONObject();

				jsonItem.put("uid", anc_item.id);
				jsonItem.put("title", anc_item.title);
				jsonItem.put("contents", anc_item.content);
				jsonItem.put("time",
						ApiGlobal.Date2String(anc_item.ps_date, true));

				arrAncs.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrAncs;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ex.getMessage();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (dbConn != null)
					dbConn.close();
				if (rs != null)
					rs.close();
			} catch (Exception sql_ex) {
				sql_ex.printStackTrace();
			}
		}


		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * source参数是做什么用的，devtoken是干什么用的
	 * 
	 * @param source
	 * @param userid
	 * @param city
	 * @param driververif
	 * @param pageno
	 * @param devtoken
	 * @return
	 */
	public SVCResult getPagedAnnouncements(String source, long userid,
			String city, int driververif, int pageno, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + city + "," + driververif + "," + pageno + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || driververif < 0 || pageno < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		}
		if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		}
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
			if (userinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_AuthenticateError;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			

			JSONArray arrAncs = new JSONArray();
			stmt = dbConn.createStatement();

			String szCity = userinfo.city_cur;

			szQuery = "SELECT * FROM announcement WHERE deleted = 0 AND (ps_city LIKE \"%"
					+ szCity
					+ "%\" OR ps_city='') AND "
					+ "validate >= \""
					+ ApiGlobal.Date2String(new Date(), true) + "\"";

			if (driververif == 0) // This is not driver verified user.
									// Must not contain even those news
									// for driver.
				szQuery += " AND 'range' = 0";

			szQuery += " ORDER BY ps_date DESC LIMIT "
					+ ApiGlobal.getPageItemCount() * pageno + ", "
					+ ApiGlobal.getPageItemCount();

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCAnnouncement anc_item = SVCAnnouncement
						.decodeFromResultSet(rs);

				JSONObject jsonItem = new JSONObject();

				jsonItem.put("uid", anc_item.id);
				jsonItem.put("title", anc_item.title);
				jsonItem.put("contents", anc_item.content);
				jsonItem.put("time",
						ApiGlobal.Date2String(anc_item.ps_date, true));

				arrAncs.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrAncs;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ex.getMessage();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (dbConn != null)
					dbConn.close();
			} catch (Exception sql_ex) {
				sql_ex.printStackTrace();
			}
		}


		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}



	public SVCResult hasNews(String source, long userid, String city,
			int driververif, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + city + "," + driververif + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || driververif < 0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		}

		Connection dbConn = null;
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();


			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);

			if (userinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_AuthenticateError;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}


			int nAncCount = 0, nNotifyOrderCount = 0, nNotifyPersonCount = 0;

			stmt = dbConn.createStatement();
			stmt2 = dbConn.createStatement();

			// Get unread announcement count
			String szCity = userinfo.city_cur;

			szQuery = "SELECT * "
					+ "FROM "
					+ "		announcement "
					+ "WHERE "
					+ "		announcement.deleted = 0 AND "
					+ "		(announcement.ps_city=\"" + szCity + "\" OR announcement.ps_city='') AND "
					+ "		announcement.validate >= \"" + ApiGlobal.Date2String(new Date(), true) + "\"";

			if (driververif == 0) // This is not driver verified user. Must not contain even those news for driver.
				szQuery += " AND 'range' = 0";



			nAncCount = 0;

			rs = stmt.executeQuery(szQuery);
			while (rs.next())
			{
				SVCAnnouncement read_ann = SVCAnnouncement.decodeFromResultSet(rs);

				szQuery = "SELECT COUNT(*) AS cnt "
						+ "FROM read_announcements "
						+ "WHERE "
						+ "		userid=" + userid + " AND "
						+ "		announceid=" + read_ann.id;
				rs2 = stmt2.executeQuery(szQuery);
				if (rs2.next() && rs2.getInt("cnt") > 0)
					continue;
				rs2.close();

				nAncCount++;
			}
			rs.close();


			// Get unread notify order count
			szQuery = "SELECT COUNT(*) as count FROM notify_order WHERE deleted = 0 AND has_read = 0 AND receiver = "
					+ userinfo.id;

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				nNotifyOrderCount = rs.getInt("count");

			if (rs != null)
				rs.close();
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// Get unread person notify count
			szQuery = "SELECT COUNT(*) as count FROM notify_person WHERE deleted = 0 AND couponid > 0 AND has_read = 0 AND receiver = "
					+ userinfo.id;

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				nNotifyPersonCount = rs.getInt("count");

			if (rs != null)
				rs.close();
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

			JSONObject retdata = new JSONObject();

			retdata.put("announcement", nAncCount);
			retdata.put("ordernotif", nNotifyOrderCount);
			retdata.put("personnotif", nNotifyPersonCount);

			result.retdata = retdata;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ex.getMessage();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			if (rs2 != null) {
				try {
					rs2.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close db connection
			if (dbConn != null) {
				try {
					dbConn.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}


		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}




	public SVCResult readAnnouncement(String source, long userid, String annids, String devtoken)
	{
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + annids + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		}

		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();

			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);

			if (userinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_AuthenticateError;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}


			stmt = dbConn.createStatement();

			String szQuery2 = "INSERT INTO read_announcements ("
					+ "userid, announceid, readtime) VALUES ";
			boolean isExist = false;

			String[] arrIDs = annids.split(",");
			for (int i = 0; i < arrIDs.length; i++)
			{
				long annid = 0;
				try {
					annid = Long.parseLong(arrIDs[i]);
				} catch (Exception ex) {
					ex.printStackTrace();
					continue;
				}

				szQuery = "SELECT COUNT(*) AS cnt "
						+ "FROM read_announcements "
						+ "WHERE "
						+ "		userid=" + userid + " AND "
						+ "		announceid=" + annid;
				rs = stmt.executeQuery(szQuery);
				if (rs.next() && rs.getInt("cnt") > 0)
					continue;
				rs.close();

				if (!isExist)
					isExist = true;

				if (szQuery2.charAt(szQuery2.length() - 1) == ')')
					szQuery2 += ",";

				szQuery2 += "("
						+ userid + ","
						+ annid + ","
						+ "\"" + ApiGlobal.Date2String(new Date(), true) + "\")";
			}

			if (isExist)
			{
				stmt.executeUpdate(szQuery2);
			}


			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ex.getMessage();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close db connection
			if (dbConn != null) {
				try {
					dbConn.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}


		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}






}
