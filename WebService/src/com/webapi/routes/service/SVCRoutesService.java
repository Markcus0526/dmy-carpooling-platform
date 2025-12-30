package com.webapi.routes.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCRoutesSetting;
import com.webapi.structure.SVCUser;

public class SVCRoutesService {
	/**
	 * 车主往routes_settings表插入日常路线数据，这个光是为了乘客的上下班服务的。暂时砍掉此功能。
	 * @param source
	 * @param userid
	 * @param type
	 * @param daytype
	 * @param startcity
	 * @param endcity
	 * @param startaddr
	 * @param endaddr
	 * @param startlat
	 * @param startlng
	 * @param endlat
	 * @param endlng
	 * @param city
	 * @param start_time
	 * @param devtoken
	 * @return
	 */
	public SVCResult addRoute(String source, long userid, int type,
			int daytype, String startcity, String endcity,
			String startaddr, String endaddr, double startlat,
			double startlng, double endlat, double endlng, String city,
			String start_time, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + type + "," + daytype + "," + startcity + "," + endcity + "," + startaddr
				+ "," + endaddr + "," + startlat + "," + startlng + "," + endlat + "," + endlng + "," + city + "," + start_time + "," + devtoken);

		if (source.equals("") || userid < 0 || type < 0 || daytype < 0
				|| startaddr.equals("") || endaddr.equals("")
				|| city.equals("") || devtoken.equals("")) {
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
			// Create db connection entity.
			dbConn = DBManager.getDBConnection();
			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else {
				

				stmt = dbConn.createStatement();

				// Get city codes
				String city_code = "";
				city = ApiGlobal.removeCityCharacter(city);

				szQuery = "SELECT code FROM city WHERE deleted = 0 AND name LIKE \"%"
						+ city + "%\"";
				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					city_code = rs.getString("code");
				rs.close();


				String startcity_code = "", endcity_code = "";
				startcity = ApiGlobal.removeCityCharacter(startcity);
				endcity = ApiGlobal.removeCityCharacter(endcity);

				szQuery = "SELECT code FROM city WHERE deleted = 0 AND name LIKE \"%"
						+ startcity + "%\"";
				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					startcity_code = rs.getString("code");
				rs.close();

				szQuery = "SELECT code FROM city WHERE deleted = 0 AND name LIKE \"%"
						+ endcity + "%\"";
				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					endcity_code = rs.getString("code");
				rs.close();


				// Prepare some parameters to save in db
				String whichdays = "";
				if (daytype == 1) // Working days
					whichdays = "0,1,2,3,4";
				else if (daytype == 2) // Weekend
					whichdays = "5,6";
				else
					// All days
					whichdays = "0,1,2,3,4,5,6";

				// Added time
				String add_time = ApiGlobal.Date2String(new Date(), true);
				szQuery = "INSERT INTO routes_settings (startcity, endcity, from_,to_,city,lat_from,lng_from,lat_to,lng_to,ps_date,start_time,type,whichdays,userid,deleted) VALUES (\""
						+ startcity_code
						+ "\", \""
						+ endcity_code
						+ "\", \""
						+ startaddr
						+ "\", \""
						+ endaddr
						+ "\", \""
						+ city_code
						+ "\", "
						+ startlat
						+ ", "
						+ startlng
						+ ", "
						+ endlat
						+ ", "
						+ endlng
						+ ", \""
						+ add_time
						+ "\", \""
						+ start_time
						+ "\", "
						+ type
						+ ", \""
						+ whichdays
						+ "\", " + userid + ", 0)";

				if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) != 1) {
					result.retcode = ConstMgr.ErrCode_Exception;
					result.retmsg = "exception in addRoute.";
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				}

				ResultSet genKeys = stmt.getGeneratedKeys();
				if (genKeys == null || !genKeys.next()) {
					result.retcode = ConstMgr.ErrCode_Exception;
					result.retmsg = ConstMgr.ErrMsg_Exception;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				}
				long route_id = genKeys.getLong(1);

				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;


				String szStartTime = "";
				Date dtStart = ApiGlobal.String2Date(start_time);
				if (dtStart != null)
				{
					Calendar cal = Calendar.getInstance();
					cal.setTime(dtStart);

					if (type == 1)		// Long distance route. Return only days
					{
						szStartTime = "" + cal.get(Calendar.YEAR) + "-";
						szStartTime += "" + (cal.get(Calendar.MONTH) + 1) + "-";
						szStartTime += "" + cal.get(Calendar.DAY_OF_MONTH);
					}
					else				// In city route. Return only time without seconds
					{
						szStartTime = "" + cal.get(Calendar.HOUR_OF_DAY) + ":";
						szStartTime += "" + cal.get(Calendar.MINUTE);
					}
				}


				JSONObject retdata = new JSONObject();

				retdata.put("uid", route_id);
				retdata.put("startcity", startcity);
				retdata.put("endcity", endcity);
				retdata.put("startaddr", startaddr);
				retdata.put("startlat", startlat);
				retdata.put("startlng", startlng);
				retdata.put("endaddr", endaddr);
				retdata.put("endlat", endlat);
				retdata.put("endlng", endlng);
				retdata.put("create_time", szStartTime);
				retdata.put("daytype", daytype);

				result.retdata = retdata;

			}
		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
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
 * 车主或者乘客获取设置的路线
 * @param source
 * @param userid
 * @param type
 * @param devtoken
 * @return
 */
	public SVCResult getRoutes(String source, long userid, int type,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + type + "," + devtoken);

		if (source.equals("") || userid < 0 || type < 0 || devtoken.equals("")) {
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
			// Create db connection entity.
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
			

			JSONArray arrItems = new JSONArray();

			Date dt_start = null;
			Calendar cal_start = Calendar.getInstance();
			//车主或者乘客获取设置的路线
			szQuery = "SELECT * FROM routes_settings WHERE deleted=0 AND userid="
					+ userid + " AND type=" + type;

			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCRoutesSetting route_info = SVCRoutesSetting
						.decodeFromResultSet(rs);

				JSONObject route_item = new JSONObject();

				route_item.put("uid", route_info.id);
				route_item.put("startcity", ApiGlobal.cityCode2Name(dbConn, route_info.startcity));
				route_item.put("endcity", ApiGlobal.cityCode2Name(dbConn, route_info.endcity));
				route_item.put("startaddr", route_info.from_);
				route_item.put("startlat", route_info.lat_from);
				route_item.put("startlng", route_info.lng_from);
				route_item.put("endaddr", route_info.to_);
				route_item.put("endlat", route_info.lat_to);
				route_item.put("endlng", route_info.lng_to);

				dt_start = route_info.start_time;
				String szStartTime = "";
				if (dt_start != null)
				{
					cal_start.setTime(dt_start);

					if (type == 1)		// Long distance route. Return only days
					{
						szStartTime = "" + cal_start.get(Calendar.YEAR) + "-";
						szStartTime += "" + (cal_start.get(Calendar.MONTH) + 1) + "-";
						szStartTime += "" + cal_start.get(Calendar.DAY_OF_MONTH);
					}
					else				// In city route. Return only time without seconds
					{
						szStartTime = "" + cal_start.get(Calendar.HOUR_OF_DAY) + ":";
						szStartTime += "" + cal_start.get(Calendar.MINUTE);
					}
				}

				route_item.put("create_time", szStartTime);

				if (route_info.whichdays.contains("0")
						&& route_info.whichdays.contains("1")
						&& route_info.whichdays.contains("2")
						&& route_info.whichdays.contains("3")
						&& route_info.whichdays.contains("4")) {
					route_item.put("daytype", 1);
				} else if (route_info.whichdays.contains("5")
						&& route_info.whichdays.contains("6")) {
					route_item.put("daytype", 2);
				} else {
					route_item.put("daytype", 3);
				}

				arrItems.add(route_item);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrItems;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
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

	public SVCResult removeRoute(String source, long userid, long route_id,
			String devtoken) {
		SVCResult result = new SVCResult();
		ApiGlobal.logMessage(source + "," + userid + "," + route_id + "," + devtoken);

		if (source.equals("") || userid < 0 || route_id < 0
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
			// Create db connection entity.
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
			

			szQuery = "UPDATE routes_settings SET deleted=1 WHERE id="
					+ route_id;

			stmt = dbConn.createStatement();
			stmt.executeUpdate(szQuery);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
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

	public SVCResult changeRoute(String source, long userid, long route_id,
			int type, int daytype, String startcity, String endcity,
			String startaddr, String endaddr,
			double startlat, double startlng, double endlat, double endlng,
			String city, String start_time, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + route_id + "," + type + "," + daytype + "," + startcity + "," + endcity + "," + startaddr
				+ "," + endaddr + "," + startlat + "," + startlng + "," + endlat + "," + endlng + "," + city + "," + start_time + "," + devtoken);

		if (source.equals("") || userid < 0 || route_id < 0 || type < 0
				|| daytype < 0 || startaddr.equals("") || endaddr.equals("")
				|| city.equals("") || devtoken.equals("")) {
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
			// Create db connection entity.
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


			// Get city codes
			String city_code = "";
			city = ApiGlobal.removeCityCharacter(city);

			szQuery = "SELECT code FROM city WHERE deleted = 0 AND name LIKE \"%"
					+ city + "%\"";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				city_code = rs.getString("code");
			rs.close();


			String startcity_code = "", endcity_code = "";
			startcity = ApiGlobal.removeCityCharacter(startcity);
			endcity = ApiGlobal.removeCityCharacter(endcity);


			szQuery = "SELECT code FROM city WHERE deleted = 0 AND name LIKE \"%"
					+ startcity + "%\"";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				startcity_code = rs.getString("code");
			rs.close();


			szQuery = "SELECT code FROM city WHERE deleted = 0 AND name LIKE \"%"
					+ endcity + "%\"";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				endcity_code = rs.getString("code");
			rs.close();


			// Prepare some parameters to save in db
			String whichdays = "";
			if (daytype == 1) // Working days
				whichdays = "0,1,2,3,4";
			else if (daytype == 2) // Weekend
				whichdays = "5,6";
			else
				// All days
				whichdays = "0,1,2,3,4,5,6";

			// Added time
			String add_time = ApiGlobal.Date2String(new Date(), true);

			szQuery = "UPDATE routes_settings SET "
					+ "startcity=\"" + startcity_code
					+ "\", endcity=\"" + endcity_code
					+ "\", from_=\"" + startaddr
					+ "\"," + "to_=\"" + endaddr + "\"," + "city=\""
					+ city_code + "\"," + "lat_from=" + startlat + ","
					+ "lng_from=" + startlng + "," + "lat_to=" + endlat + ","
					+ "lng_to=" + endlng + "," + "ps_date=\"" + add_time
					+ "\"," + "start_time=\"" + start_time + "\"," + "type="
					+ type + "," + "whichdays=\"" + whichdays + "\","
					+ "userid=" + userid + "," + "deleted=0 " + "WHERE id = "
					+ route_id;

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;


			String szStartTime = "";
			Date dtStart = ApiGlobal.String2Date(start_time);
			if (dtStart != null)
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(dtStart);

				if (type == 1)		// Long distance route. Return only days
				{
					szStartTime = "" + cal.get(Calendar.YEAR) + "-";
					szStartTime = "" + (cal.get(Calendar.MONTH) + 1) + "-";
					szStartTime = "" + cal.get(Calendar.DAY_OF_MONTH);
				}
				else				// In city route. Return only time without seconds
				{
					szStartTime = "" + cal.get(Calendar.HOUR_OF_DAY) + ":";
					szStartTime = "" + cal.get(Calendar.MINUTE);
				}
			}


			JSONObject retdata = new JSONObject();

			retdata.put("uid", route_id);
			retdata.put("startcity", startcity);
			retdata.put("endcity", endcity);
			retdata.put("startaddr", startaddr);
			retdata.put("startlat", startlat);
			retdata.put("startlng", startlng);
			retdata.put("endaddr", endaddr);
			retdata.put("endlat", endlat);
			retdata.put("endlng", endlng);
			retdata.put("create_time", szStartTime);
			retdata.put("daytype", daytype);

			result.retdata = retdata;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
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

}
