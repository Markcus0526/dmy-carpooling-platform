package com.webapi.evaluation.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCEvaluationCS;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCUser;

public class SVCEvalService {
	/**
	 * 这个函数是返回所有车主对该乘客的评价列表
	 * @param source
	 * @param userid
	 * @param passid
	 * @param limitid
	 * @param devtoken
	 * @return
	 */
	public SVCResult passengerLatestEvalInfo(String source, long userid,
			long passid, long limitid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + passid + "," + limitid + "," + devtoken);

		if (source.equals("") || userid < 0 || passid < 0 || limitid < 0
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
			SVCUser passinfo = ApiGlobal.getUserInfoFromUserID(dbConn,passid);
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
			if (passinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoPassInfo;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			JSONArray arrEvaluations = new JSONArray();
			// 1,根据limitid获得该limitid对应的时间
			SVCEvaluationCS limit_info = ApiGlobal
					.getEvaluationInfoFromID(dbConn, limitid);

			

			stmt = dbConn.createStatement();

			// 2，获得所有车主对此乘客的评价信息
			szQuery = "SELECT " + "		evaluation_cs.*," + "		user.nickname "
					+ "FROM evaluation_cs " + "INNER JOIN user "
					+ "ON user.id = evaluation_cs.from_userid " + "WHERE "
					+ "		evaluation_cs.deleted=0 AND "
					+ "		evaluation_cs.usertype=2 AND "
					+ "		evaluation_cs.to_userid=" + passid;

			if (limit_info != null) {
				szQuery += " AND ps_date > \""
						+ ApiGlobal.Date2String(limit_info.ps_date, true)
						+ "\"";
			}

			szQuery += " ORDER BY evaluation_cs.ps_date DESC";

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				int eval = rs.getInt("level");
				String eval_desc = "";
				if (eval == 1)
					eval_desc = ConstMgr.STR_HAOPING;
				else if (eval == 2)
					eval_desc = ConstMgr.STR_ZHONGPING;
				else
					eval_desc = ConstMgr.STR_CHAPING;

				JSONObject eval_item = new JSONObject();

				eval_item.put("uid", rs.getLong("id"));
				eval_item.put("driver_name", rs.getString("nickname"));
				eval_item.put("eval", rs.getInt("level"));
				eval_item.put("eval_desc", eval_desc);
				eval_item.put("time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_date")), true));
				eval_item.put("contents", rs.getString("msg"));

				arrEvaluations.add(eval_item);
			}
			rs.close();

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrEvaluations;

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

	public SVCResult passengerPagedEvalInfo(String source, long userid,
			long passid, int pageno, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + passid + "," + pageno + "," + devtoken);

		if (source.equals("") || userid < 0 || passid < 0 || pageno < 0
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
			SVCUser passinfo = ApiGlobal.getUserInfoFromUserID(dbConn,passid);
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
			if (passinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoPassInfo;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			JSONArray arrEvaluations = new JSONArray();

			

			stmt = dbConn.createStatement();
			//这个函数是返回所有车主对该乘客的评价列表,分页显示
			// Get evaluation info
			szQuery = "SELECT " + "		evaluation_cs.*," + "		user.nickname "
					+ "FROM evaluation_cs " + "INNER JOIN user "
					+ "ON user.id = evaluation_cs.from_userid " + "WHERE "
					+ "		evaluation_cs.deleted=0 AND "
					+ "		evaluation_cs.usertype=2 AND "
					+ "		evaluation_cs.to_userid=" + passid + " "
					+ "ORDER BY evaluation_cs.ps_date DESC " + "LIMIT "
					+ ApiGlobal.getPageItemCount() * pageno + ","
					+ ApiGlobal.getPageItemCount();

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				int eval = rs.getInt("level");
				String eval_desc = "";
				if (eval == 1)
					eval_desc = ConstMgr.STR_HAOPING;
				else if (eval == 2)
					eval_desc = ConstMgr.STR_ZHONGPING;
				else
					eval_desc = ConstMgr.STR_CHAPING;

				JSONObject eval_item = new JSONObject();

				eval_item.put("uid", rs.getLong("id"));
				eval_item.put("driver_name", rs.getString("nickname"));
				eval_item.put("eval", rs.getInt("level"));
				eval_item.put("eval_desc", eval_desc);
				eval_item.put("time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_date")), true));
				eval_item.put("contents", rs.getString("msg"));

				arrEvaluations.add(eval_item);
			}
			rs.close();

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrEvaluations;

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

	public SVCResult driverLatestEvalInfo(String source, long userid,
			long driverid, long limitid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + driverid + "," + limitid + "," + devtoken);

		if (source.equals("") || userid < 0 || driverid < 0 || limitid < 0
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
			SVCUser drvinfo = ApiGlobal.getUserInfoFromUserID(dbConn,driverid);
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
			if (drvinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoPassInfo;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			JSONArray arrEvaluations = new JSONArray();
			//1,获得limitid对应的评论记录
			SVCEvaluationCS limit_info = ApiGlobal
					.getEvaluationInfoFromID(dbConn, limitid);

			

			stmt = dbConn.createStatement();
			//2，获得所有车主对此乘客的评价信息
			// Get evaluation info
			szQuery = "SELECT " + "		evaluation_cs.*," + "		user.nickname "
					+ "FROM evaluation_cs " + "INNER JOIN user "
					+ "ON user.id = evaluation_cs.from_userid " + "WHERE "
					+ "		evaluation_cs.deleted=0 AND "
					+ "		evaluation_cs.usertype=1 AND "
					+ "		evaluation_cs.to_userid=" + driverid;

			if (limit_info != null) {
				szQuery += " AND ps_date > \""
						+ ApiGlobal.Date2String(limit_info.ps_date, true)
						+ "\"";
			}

			szQuery += " ORDER BY evaluation_cs.ps_date DESC";

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				int eval = rs.getInt("level");
				String eval_desc = "";
				if (eval == 1)
					eval_desc = ConstMgr.STR_HAOPING;
				else if (eval == 2)
					eval_desc = ConstMgr.STR_ZHONGPING;
				else
					eval_desc = ConstMgr.STR_CHAPING;

				JSONObject eval_item = new JSONObject();

				eval_item.put("uid", rs.getLong("id"));
				eval_item.put("pass_name", rs.getString("nickname"));
				eval_item.put("eval", rs.getInt("level"));
				eval_item.put("eval_desc", eval_desc);
				eval_item.put("time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_date")), true));
				eval_item.put("contents", rs.getString("msg"));

				arrEvaluations.add(eval_item);
			}
			rs.close();

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrEvaluations;

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

	public SVCResult driverPagedEvalInfo(String source, long userid,
			long driverid, int pageno, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + driverid + "," + pageno + "," + devtoken);

		if (source.equals("") || userid < 0 || driverid < 0 || pageno < 0
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
			SVCUser drvinfo = ApiGlobal.getUserInfoFromUserID(dbConn,driverid);
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
			if (drvinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoPassInfo;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			JSONArray arrEvaluations = new JSONArray();

			

			stmt = dbConn.createStatement();
			//返回所有乘客对该车主的所有评价信息
			// Get evaluation info
			szQuery = "SELECT " + "		evaluation_cs.*," + "		user.nickname "
					+ "FROM evaluation_cs " + "INNER JOIN user "
					+ "ON user.id = evaluation_cs.from_userid " + "WHERE "
					+ "		evaluation_cs.deleted=0 AND "
					+ "		evaluation_cs.usertype=1 AND "
					+ "		evaluation_cs.to_userid=" + driverid + " "
					+ "ORDER BY evaluation_cs.ps_date DESC LIMIT "
					+ ApiGlobal.getPageItemCount() * pageno + ", "
					+ ApiGlobal.getPageItemCount();

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				int eval = rs.getInt("level");
				String eval_desc = "";
				if (eval == 1)
					eval_desc = ConstMgr.STR_HAOPING;
				else if (eval == 2)
					eval_desc = ConstMgr.STR_ZHONGPING;
				else
					eval_desc = ConstMgr.STR_CHAPING;

				JSONObject eval_item = new JSONObject();

				eval_item.put("uid", rs.getLong("id"));
				eval_item.put("pass_name", rs.getString("nickname"));
				eval_item.put("eval", rs.getInt("level"));
				eval_item.put("eval_desc", eval_desc);
				eval_item.put("time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_date")), true));
				eval_item.put("contents", rs.getString("msg"));

				arrEvaluations.add(eval_item);
			}
			rs.close();

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrEvaluations;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
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
