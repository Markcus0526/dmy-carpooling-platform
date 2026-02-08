package com.webapi.notifyorder.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCNotifyOrder;
import com.webapi.structure.SVCOrderExecCS;
import com.webapi.structure.SVCOrderLongDistanceDetails;
import com.webapi.structure.SVCOrderOnOffDutyDetails;
import com.webapi.structure.SVCOrderTempDetails;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCUser;

public class SVCNotifyOrderService {
	/**
	 * source和devtoken都是干什么用的,还有limitid是做什么用的
	 * 
	 * @param source
	 * @param userid
	 * @param limitid
	 * @param devtoken
	 * @return
	 */
	public SVCResult getLatestNotifyOrders(String source, long userid,
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
			

			JSONArray arrNotifyOrders = new JSONArray();
			stmt = dbConn.createStatement();
			stmt2 = dbConn.createStatement();

			SVCNotifyOrder limit_item = ApiGlobal.getNotifyOrderFromID(dbConn, limitid);
			szQuery = "SELECT "
					+ "		notify_order.id AS notify_id,"
					+ "		notify_order.title AS notify_title,"
					+ "		notify_order.msg AS notify_contents,"
					+ "		notify_order.ps_date AS notify_time,"
					+ "		notify_order.has_read AS hasread,"
					+ "		order_exec_cs.* "

					+ "FROM notify_order "

					+ "LEFT JOIN order_exec_cs "
					+ "ON order_exec_cs.id=notify_order.order_cs_id "

					+ "WHERE "
					+ "		notify_order.deleted = 0 AND "
					+ "		notify_order.receiver=" + userid;

			if (limit_item != null)
				szQuery += " AND notify_order.ps_date > \""
						+ ApiGlobal.Date2String(limit_item.ps_date, true)
						+ "\"";

			szQuery += " ORDER BY notify_order.ps_date DESC";

			rs = stmt.executeQuery(szQuery);
			while (rs.next())
			{
				SVCOrderExecCS exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
				long orderid = 0;
				int order_type = 0;
				int status = 0;
				int user_role = 0;

				JSONObject jsonItem = new JSONObject();

				jsonItem.put("uid", rs.getLong("notify_id"));
				jsonItem.put("title", rs.getString("notify_title"));
				jsonItem.put("contents", rs.getString("notify_contents"));
				jsonItem.put("time", ApiGlobal.Date2String(ApiGlobal.String2Date(rs.getString("notify_time")), true));
				jsonItem.put("hasread", rs.getInt("hasread"));

				if (exec_info.order_type == 4)			// Long distance order
				{
					szQuery = "SELECT "
							+ "		order_longdistance_details.* "
							+ "FROM "
							+ "		order_longdistance_users_cs "
							+ "INNER JOIN order_longdistance_details "
							+ "ON order_longdistance_details.id=order_longdistance_users_cs.orderdriverlongdistance_id "
							+ "WHERE order_longdistance_users_cs.order_exec_cs_id=" + exec_info.id;
					rs2 = stmt2.executeQuery(szQuery);
					if (rs2.next())
					{
						SVCOrderLongDistanceDetails long_order = SVCOrderLongDistanceDetails.decodeFromResultSet(rs2);
						orderid = long_order.id;
						order_type = 3;
						status = long_order.status;
					}
					rs2.close();
				}
				else if (exec_info.order_type == 3)		// On off order
				{
					szQuery = "SELECT "
							+ "		order_onoffduty_details.* "
							+ "FROM "
							+ "		order_onoffduty_exec_details "

							+ "INNER JOIN order_onoffduty_divide "
							+ "ON order_onoffduty_divide.id=order_onoffduty_exec_details.onoffduty_divide_id "

							+ "INNER JOIN order_onoffduty_details "
							+ "ON order_onoffduty_details.id=order_onoffduty_divide.orderdetails_id "
						
							+ "WHERE "
							+ "		order_onoffduty_exec_details.order_cs_id=" + exec_info.id;
					status = exec_info.status;
					rs2 = stmt2.executeQuery(szQuery);
					if (rs2.next())
					{
						SVCOrderOnOffDutyDetails onoff_order = SVCOrderOnOffDutyDetails.decodeFromResultSet(rs2);
						orderid = onoff_order.id;
						order_type = 2;
					}
					rs2.close();
				}
				else			// Once order
				{
					szQuery = "SELECT * FROM order_temp_details WHERE order_cs_id=" + exec_info.id;

					rs2 = stmt2.executeQuery(szQuery);
					if (rs2.next())
					{
						SVCOrderTempDetails once_order = SVCOrderTempDetails.decodeFromResultSet(rs2);
						orderid = once_order.id;
						order_type = 1;
						status = once_order.status;
					}
					rs2.close();
				}

				if (exec_info.driver == userid)
				{
					user_role = 0;
				}
				else
				{
					user_role = 1;
				}

				jsonItem.put("orderid", orderid);
				jsonItem.put("order_type", order_type);
				jsonItem.put("state", status);
				jsonItem.put("user_role", user_role);

				arrNotifyOrders.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrNotifyOrders;

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
	 * source,devtoken是干什么用的
	 * 
	 * @param source
	 * @param userid
	 * @param pageno
	 * @param devtoken
	 * @return
	 */
	public SVCResult getPagedNotifyOrders(String source, long userid,
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
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
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
			}
			if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			

			JSONArray arrNotifyOrders = new JSONArray();
			stmt = dbConn.createStatement();
			stmt2 = dbConn.createStatement();

			szQuery = "SELECT "
					+ "		notify_order.id AS notify_id,"
					+ "		notify_order.title AS notify_title,"
					+ "		notify_order.msg AS notify_contents,"
					+ "		notify_order.ps_date AS notify_time,"
					+ "		notify_order.has_read AS hasread,"
					+ "		order_exec_cs.* "

					+ "FROM notify_order "

					+ "LEFT JOIN order_exec_cs "
					+ "ON order_exec_cs.id=notify_order.order_cs_id "

					+ "WHERE "
					+ "		notify_order.deleted = 0 AND "
					+ "		notify_order.receiver=" + userid;

			szQuery += " ORDER BY ps_date DESC LIMIT "
					+ ApiGlobal.getPageItemCount() * pageno + ", "
					+ ApiGlobal.getPageItemCount();

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCOrderExecCS exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
				long orderid = 0;
				int order_type = 0;
				int status = 0;
				int user_role = 0;

				JSONObject jsonItem = new JSONObject();

				jsonItem.put("uid", rs.getLong("notify_id"));
				jsonItem.put("title", rs.getString("notify_title"));
				jsonItem.put("contents", rs.getString("notify_contents"));
				jsonItem.put("time", ApiGlobal.Date2String(ApiGlobal.String2Date(rs.getString("notify_time")), true));
				jsonItem.put("hasread", rs.getInt("hasread"));

				if (exec_info.order_type == 4)			// Long distance order
				{
					szQuery = "SELECT "
							+ "		order_longdistance_details.* "
							+ "FROM "
							+ "		order_longdistance_users_cs "
							+ "INNER JOIN order_longdistance_details "
							+ "ON order_longdistance_details.id=order_longdistance_users_cs.orderdriverlongdistance_id "
							+ "WHERE order_longdistance_users_cs.order_exec_cs_id=" + exec_info.id;
					rs2 = stmt2.executeQuery(szQuery);
					if (rs2.next())
					{
						SVCOrderLongDistanceDetails long_order = SVCOrderLongDistanceDetails.decodeFromResultSet(rs2);
						orderid = long_order.id;
						order_type = 3;
						status = long_order.status;
					}
					rs2.close();
				}
				else if (exec_info.order_type == 3)		// On off order
				{
					szQuery = "SELECT "
							+ "		order_onoffduty_details.* "
							+ "FROM "
							+ "		order_onoffduty_exec_details "

							+ "INNER JOIN order_onoffduty_divide "
							+ "ON order_onoffduty_divide.id=order_onoffduty_exec_details.onoffduty_divide_id "

							+ "INNER JOIN order_onoffduty_details "
							+ "ON order_onoffduty_details.id=order_onoffduty_divide.orderdetails_id "
						
							+ "WHERE "
							+ "		order_onoffduty_exec_details.order_cs_id=" + exec_info.id;
					status = exec_info.status;
					rs2 = stmt2.executeQuery(szQuery);
					if (rs2.next())
					{
						SVCOrderOnOffDutyDetails onoff_order = SVCOrderOnOffDutyDetails.decodeFromResultSet(rs2);
						orderid = onoff_order.id;
						order_type = 2;
					}
					rs2.close();
				}
				else			// Once order
				{
					szQuery = "SELECT * FROM order_temp_details WHERE order_cs_id=" + exec_info.id;

					rs2 = stmt2.executeQuery(szQuery);
					if (rs2.next())
					{
						SVCOrderTempDetails once_order = SVCOrderTempDetails.decodeFromResultSet(rs2);
						orderid = once_order.id;
						order_type = 1;
						status = once_order.status;
					}
					rs2.close();
				}

				if (exec_info.driver == userid)
				{
					user_role = 0;
				}
				else
				{
					user_role = 1;
				}

				jsonItem.put("orderid", orderid);
				jsonItem.put("order_type", order_type);
				jsonItem.put("state", status);
				jsonItem.put("user_role", user_role);

				arrNotifyOrders.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrNotifyOrders;

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

				if (rs2 != null)
					rs2.close();
				if (stmt2 != null)
					stmt2.close();

				if (dbConn != null)
					dbConn.close();
			} catch (Exception sql_ex) {
				sql_ex.printStackTrace();
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult readOrderNotifs(String source, long userid,
			long ordernotifid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + ordernotifid + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || ordernotifid < 0
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
			}
			if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			

			SVCNotifyOrder notiorder_item = ApiGlobal
					.getNotifyOrderFromID(dbConn, ordernotifid);

			if (notiorder_item != null) {
				szQuery = "UPDATE notify_order SET has_read = 1 WHERE deleted = 0 AND receiver = "
						+ userinfo.id
						+ " AND ps_date <= \""
						+ ApiGlobal.Date2String(notiorder_item.ps_date, true)
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
 * 标记通知消息是否已读，只是简单更改notify_order表的状态
 * @param source
 * @param userid
 * @param idarray
 * @param devtoken
 * @return
 */

	public SVCResult checkOrderNotifRead(String source, long userid,
			String idarray, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + idarray + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || devtoken.equals("")) {
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
			}
			if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			

			szQuery = "UPDATE "
					+ "		notify_order "
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
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(dbConn!=null) dbConn.close();
			} catch (Exception sql_ex) {
				sql_ex.printStackTrace();
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

}
