package com.webapi.ts.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCTs;
import com.webapi.structure.SVCUser;

public class SVCTsService {
	/**
	 * 1,查询出当前用户余额
	 * 2，查询出该用户的所有交易记录(分页)
	 * @param source
	 * @param userid
	 * @param pageno
	 * @param devtoken
	 * @return
	 */
	public SVCResult pagedTsLogs(String source, long userid, int pageno,
			String devtoken) {
		SVCResult result = new SVCResult();
		ApiGlobal.logMessage(source + "," + userid + "," + pageno + "," + devtoken);

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
			//1,查询出当前用户余额
			SVCTs latest_ts = null;
			szQuery = "SELECT * FROM ts WHERE deleted = 0 AND id = "
					+ userinfo.balance_ts;
			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			latest_ts = SVCTs.decodeFromResultSet(rs);
			rs.close();
			//2，查询出该用户的所有交易记录(分页)
			JSONArray arrTs = new JSONArray();
			szQuery = "SELECT * FROM ts WHERE deleted = 0 AND "
					+ "userid=" + userid + " "
					+ " AND sum<>0 "
					+ " ORDER BY ts_date DESC, id DESC LIMIT "
					+ ApiGlobal.getPageItemCount() * pageno + ","
					+ ApiGlobal.getPageItemCount();
			rs = stmt.executeQuery(szQuery);

			while (rs.next()) {
				SVCTs ts_item = SVCTs.decodeFromResultSet(rs);
				JSONObject json_item = new JSONObject();

				json_item.put("uid", ts_item.id);
				json_item.put("tsid", String.format("%08d", ts_item.id));

				if (ts_item.oper == 1)
					json_item.put("operbalance", String.format("%.2f", -ts_item.sum));
				else
					json_item.put("operbalance", String.format("%.2f", ts_item.sum));

				json_item.put("source", ApiGlobal.getCommentFromTxCode(dbConn, ts_item.ts_type));
				json_item.put("remainbalance", String.format("%.2f", ts_item.balance));
				json_item.put("tstime", ApiGlobal.Date2StringWithoutSeconds(ts_item.ts_date));

				arrTs.add(json_item);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

			JSONObject retdata = new JSONObject();
			retdata.put("curbalance", latest_ts.balance);
			retdata.put("logs", arrTs);

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
/**
 * 1,查询出用户当前余额
 * 2,查询出该用户指定交易记录之后的所有交易记录
 * @param source
 * @param userid
 * @param limitid
 * @param devtoken
 * @return
 */
	public SVCResult latestTsLogs(String source, long userid, long limitid,
			String devtoken) {
		SVCResult result = new SVCResult();
		ApiGlobal.logMessage(source + "," + userid + "," + limitid + "," + devtoken);

		if (source.equals("") || userid < 0 || limitid < 0
				|| devtoken.equals("")) {
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
			//1,查询出用户当前余额
			SVCTs latest_ts = null;
			szQuery = "SELECT * FROM ts WHERE deleted = 0 AND id = "
					+ userinfo.balance_ts;
			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			latest_ts = SVCTs.decodeFromResultSet(rs);
			rs.close();

			JSONArray arrTs = new JSONArray();

			SVCTs ts_limititem = null;
			//2,查询出该用户指定交易记录之后的所有交易记录
			szQuery = "SELECT * FROM ts WHERE deleted = 0 AND id = " + limitid;
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				ts_limititem = SVCTs.decodeFromResultSet(rs);
			}

			szQuery = "SELECT * FROM ts WHERE deleted = 0 AND "
					+ "sum<>0 AND "
					+ "userid=" + userid;
			if (ts_limititem != null)
				szQuery += " AND ts_date > \""
						+ ApiGlobal.Date2String(ts_limititem.ts_date, true)
						+ "\" ";
			szQuery += " ORDER BY ts_date DESC, id DESC";

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCTs ts_item = SVCTs.decodeFromResultSet(rs);
				JSONObject json_item = new JSONObject();
				json_item.put("uid", ts_item.id);
				json_item.put("tsid", String.format("%08d", ts_item.id));

				if (ts_item.oper == 1)
					json_item.put("operbalance", String.format("%.2f", -ts_item.sum));
				else
					json_item.put("operbalance", String.format("%.2f", ts_item.sum));

				json_item.put("source", ApiGlobal.getCommentFromTxCode(dbConn, ts_item.ts_type));
				json_item.put("remainbalance", String.format("%.2f", ts_item.balance));
				json_item.put("tstime", ApiGlobal.Date2StringWithoutSeconds(ts_item.ts_date));

				arrTs.add(json_item);
			}

			JSONObject retdata = new JSONObject();
			retdata.put("curbalance", latest_ts.balance);
			retdata.put("logs", arrTs);

			result.retdata = retdata;

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
