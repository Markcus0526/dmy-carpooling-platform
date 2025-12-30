package com.webapi.notifyperson.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCNotifyPerson;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCUser;

public class SVCNotifyPersonService {
	/**
	 * source和limitid,devtoken都是干什么用的
	 * 
	 * @param source
	 * @param userid
	 * @param limitid
	 * @param devtoken
	 * @return
	 */
	public SVCResult getLatestNotifications(String source, long userid,
			long limitid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + limitid + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || limitid < 0
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
			

			JSONArray arrNotifyPersons = new JSONArray();
			stmt = dbConn.createStatement();

			SVCNotifyPerson limit_item = ApiGlobal
					.getNotifyPersonFromID(dbConn, limitid);
			szQuery = "SELECT * FROM notify_person WHERE deleted = 0 AND receiver="
					+ userid;

			if (limit_item != null)
				szQuery += " AND ps_date > \""
						+ ApiGlobal.Date2String(limit_item.ps_date, true)
						+ "\"";

			szQuery += " ORDER BY ps_date DESC";

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCNotifyPerson notifyperson_item = SVCNotifyPerson
						.decodeFromResultSet(rs);

				JSONObject jsonItem = new JSONObject();

				jsonItem.put("uid", notifyperson_item.id);
				jsonItem.put("title", notifyperson_item.title);
				jsonItem.put("contents", notifyperson_item.msg);
				jsonItem.put("time",
						ApiGlobal.Date2String(notifyperson_item.ps_date, true));
				jsonItem.put("couponid", notifyperson_item.couponid);
				jsonItem.put("hasread", notifyperson_item.has_read);

				arrNotifyPersons.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrNotifyPersons;

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

	/**
	 * source和devtoken都是干什么用的
	 * 
	 * @param source
	 * @param userid
	 * @param pageno
	 * @param devtoken
	 * @return
	 */
	public SVCResult getPagedNotifications(String source, long userid,
			int pageno, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + pageno + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || pageno < 0
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
			

			JSONArray arrNotifyPersons = new JSONArray();
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM notify_person WHERE deleted = 0 AND receiver="
					+ userid;
			szQuery += " ORDER BY ps_date DESC LIMIT "
					+ ApiGlobal.getPageItemCount() * pageno + ","
					+ ApiGlobal.getPageItemCount();

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCNotifyPerson notifyperson_item = SVCNotifyPerson
						.decodeFromResultSet(rs);

				JSONObject jsonItem = new JSONObject();

				jsonItem.put("uid", notifyperson_item.id);
				jsonItem.put("title", notifyperson_item.title);
				jsonItem.put("contents", notifyperson_item.msg);
				jsonItem.put("time",
						ApiGlobal.Date2String(notifyperson_item.ps_date, true));
				jsonItem.put("couponid", notifyperson_item.couponid);
				jsonItem.put("hasread", notifyperson_item.has_read);

				arrNotifyPersons.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrNotifyPersons;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ex.getMessage();
		} finally {
			// Close result set
			if (rs != null) {
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult readPersonNotifs(String source, long userid,
			long personnotifid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + personnotifid + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || personnotifid < 0
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
			// Database entity
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

			

			SVCNotifyPerson notiperson_item = ApiGlobal
					.getNotifyPersonFromID(dbConn, personnotifid);

			if (notiperson_item != null) {
				szQuery = "UPDATE notify_person SET has_read = 1 WHERE deleted = 0 AND receiver = "
						+ userinfo.id
						+ " AND ps_date <= \""
						+ ApiGlobal.Date2String(notiperson_item.ps_date, true)
						+ "\"";
				stmt = dbConn.createStatement();
				stmt.executeUpdate(szQuery);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ex.getMessage();
		} finally {
			try {
				if(rs!=null)rs.close();
				if(stmt!=null)stmt.close();
				if(dbConn!=null)dbConn.close();
			} catch (Exception sql_ex) {
				sql_ex.printStackTrace();
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

/**
 * 简单标记notify_person中的某条消息已读
 * @param source
 * @param userid
 * @param idarray
 * @param devtoken
 * @return
 */


	public SVCResult checkPersonNotifRead(String source, long userid,
			String idarray, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + idarray + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0
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
			// Database entity
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

			

			szQuery = "UPDATE "
					+ "		notify_person "
					+ "SET "
					+ "		has_read = 1 "
					+ "WHERE "
					+ "		deleted = 0 AND "
					+ "		id IN (" + idarray + ") AND "
					+ "		receiver = " + userinfo.id;
			stmt = dbConn.createStatement();
			stmt.executeUpdate(szQuery);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ex.getMessage();
		} finally {
			try {
				if(rs!=null)rs.close();
				if(stmt!=null)stmt.close();
				if(dbConn!=null)dbConn.close();
			} catch (Exception sql_ex) {
				sql_ex.printStackTrace();
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

}
