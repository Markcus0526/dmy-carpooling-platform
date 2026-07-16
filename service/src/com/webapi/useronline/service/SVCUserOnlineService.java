package com.webapi.useronline.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCUser;

public class SVCUserOnlineService {
	/**
	 * 1,根据userid查询出此车主在user_online里的id，如果存在则执行2步骤；如果不存在则执行3步骤
	   2,在user_online表里更新此车主的经纬度
	   3,在user_online表里插入此车主的经纬度。
	   4,查询出driver_online_stat表该小时的数据，如果driver_online_stat该小时的数据存在则将心跳次数加1，否则向driver_online_stat插入该小时的数据(心跳次数是1)。

	 * @param source
	 * @param userid
	 * @param lat
	 * @param lng
	 * @param devtoken
	 * @return
	 */
	public SVCResult reportDriverPos(String source, long userid, double lat,
			double lng, String devtoken) {
		SVCResult result = new SVCResult();
		ApiGlobal.logMessage(source + "," + userid + "," + lat + "," + lng + "," + devtoken);

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
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();

			// Authenticate user
			
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
			} else if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
			} else {
				

				stmt = dbConn.createStatement();
				stmt2 = dbConn.createStatement();
				//1,根据userid查询出此车主在user_online里的id，如果存在则执行2步骤；如果不存在则执行3步骤
				//2,在user_online表里更新此车主的经纬度
				//3,在user_online表里插入此车主的经纬度。
				szQuery = "SELECT * FROM user_online WHERE deleted=0 AND userid="
						+ userid;
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					szQuery = "UPDATE "
							+ "		user_online "
							+ "SET "
							+ "		lat="
							+ lat
							+ ","
							+ "		lng="
							+ lng
							+ ","
							+ "		login_time=\""
							+ ApiGlobal.Date2String(userinfo.last_login_time,
									true) + "\"," + "		last_heartbeat_time=\""
							+ ApiGlobal.Date2String(new Date(), true) + "\","
							+ "		userid=" + userid + "  " + "WHERE id="
							+ rs.getLong("id");

					if (stmt2.executeUpdate(szQuery) > 0) {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception;
					}
				} else {
					szQuery = "INSERT INTO user_online (lat, lng, login_time, last_heartbeat_time, userid, deleted) VALUES ("
							+ lat
							+ ","
							+ lng
							+ ", \""
							+ ApiGlobal.Date2String(userinfo.last_login_time,
									true)
							+ "\",\""
							+ ApiGlobal.Date2String(new Date(), true)
							+ "\","
							+ userid + "," + "0)";

					if (stmt2.executeUpdate(szQuery) > 0) {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception;
					}
				}
				rs.close();
				//4,查询出driver_online_stat表该小时的数据，如果driver_online_stat该小时的数据存在则将心跳次数加1，否则向driver_online_stat插入该小时的数据(心跳次数是1)。
				// Update driver_online_state table
				if (result.retcode == ConstMgr.ErrCode_None) {
					Calendar cal = Calendar.getInstance();
					int nHour = cal.get(Calendar.HOUR_OF_DAY);
					String cur_date = ApiGlobal.Date2String(new Date(), false);

					szQuery = "SELECT * FROM driver_online_stat WHERE deleted=0 AND userid="
							+ userid
							+ " AND hour="
							+ nHour
							+ " AND ps_date=\""
							+ cur_date + "\"";
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						szQuery = "UPDATE " + "		driver_online_stat " + "SET"
								+ "		count=" + (rs.getInt("count") + 1) + " "
								+ "WHERE" + "		id=" + rs.getLong("id");

						if (stmt2.executeUpdate(szQuery) > 0) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception;
						}
					} else {
						szQuery = "INSERT INTO driver_online_stat (userid, count, hour, ps_date, deleted) VALUES "
								+ "("
								+ userid
								+ ", 1, "
								+ nHour
								+ ", \""
								+ cur_date + "\", 0)";

						if (stmt2.executeUpdate(szQuery) > 0) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception;
						}
					}
					rs.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg = ex.getMessage();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (rs2 != null)
					rs2.close();
				if (stmt != null)
					stmt.close();
				if (stmt2 != null)
					stmt2.close();
				if (dbConn != null)
					dbConn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}
	//获取车主当前的位置
	public SVCResult driverPos(String source, long userid, long driverid,
			String devtoken) {
		SVCResult result = new SVCResult();
		ApiGlobal.logMessage(source + "," + userid + "," + driverid + "," + devtoken);

		if (source.equals("") || userid < 0 || driverid < 0
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
			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
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

			JSONObject retdata = new JSONObject();
			String szValidPeriod = ApiGlobal
					.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_HEARTBEAT_VALIDDPERIOD);
			//获取车主当前的位置
			szQuery = "SELECT "
					+ "		user_online.*,"
					+ "		user.nickname "
					+ "FROM user_online "
					+ "INNER JOIN user "
					+ "ON user.id=user_online.userid "
					+ "WHERE "
					+ "		user.deleted=0 AND "
					+ "		user_online.deleted=0 AND "
					+ "		user_online.userid="
					+ driverid
					+ " AND "
					+ "		TIMESTAMPDIFF(MINUTE, user_online.last_heartbeat_time, CURDATE()) < "
					+ szValidPeriod + " " + "ORDER BY "
					+ "		user_online.last_heartbeat_time";

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				retdata.put("driver_name", rs.getString("nickname"));
				retdata.put("lat", rs.getDouble("lat"));
				retdata.put("lng", rs.getDouble("lng"));
				retdata.put("time",
						ApiGlobal.Date2String(ApiGlobal.String2Date(rs
								.getString("last_heartbeat_time")), true));

				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
				result.retdata = retdata;
			} else {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoCoordinateInfo;
			}
			rs.close();

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg = ex.getMessage();
		} finally {
			try {
				if(rs!=null)rs.close();
				if(stmt!=null)stmt.close();
				if(dbConn!=null)dbConn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

}
