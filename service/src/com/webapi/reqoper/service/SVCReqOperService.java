package com.webapi.reqoper.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCFreezePoints;
import com.webapi.structure.SVCReqClientAccount;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCTs;
import com.webapi.structure.SVCUser;

public class SVCReqOperService {
	/**
	 * 1,查询指定记录之后的所有提现请求
	 * @param source
	 * @param userid
	 * @param limitid
	 * @param devtoken
	 * @return
	 */
	public SVCResult latestWithdrawLogs(String source, long userid,
			long limitid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + limitid + "," + devtoken);

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
			if (userinfo.person_verified != 1) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotVerifiedPerson;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			

			JSONArray arrLogs = new JSONArray();
			//1,查询指定记录之后的所有提现请求
			stmt = dbConn.createStatement();
			szQuery = "SELECT * FROM req_client_account WHERE deleted = 0 AND id = "
					+ limitid;
			rs = stmt.executeQuery(szQuery);

			SVCReqClientAccount reqoper_log = null;
			if (rs.next())
				reqoper_log = SVCReqClientAccount.decodeFromResultSet(rs);

			if (rs != null)
				rs.close();
			//2,查询指定记录之后的所有的提现请求
			szQuery = "SELECT * FROM req_client_account WHERE deleted=0 AND oper_type=1 AND user_id="
					+ userinfo.id;
			if (reqoper_log != null)
				szQuery += " AND req_date>\""
						+ ApiGlobal.Date2String(reqoper_log.req_date, true)
						+ "\"";
			szQuery += " ORDER BY req_date DESC";

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				JSONObject jsonItem = new JSONObject();

				jsonItem.put("uid", rs.getLong("id"));
				jsonItem.put("req_date",
						ApiGlobal.Date2String(rs.getDate("req_date"), false));
				jsonItem.put("balance",
						String.format("%.2f", rs.getDouble("sum")));
				jsonItem.put("state", rs.getInt("status"));

				arrLogs.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrLogs;

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
 * 1,分页查询req_client_account表
 * @param source
 * @param userid
 * @param pageno
 * @param devtoken
 * @return
 */
	public SVCResult pagedWithdrawLogs(String source, long userid, int pageno,
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
			if (userinfo.person_verified != 1) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotVerifiedPerson;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			

			JSONArray arrLogs = new JSONArray();

			stmt = dbConn.createStatement();
			// 1,分页查询req_client_account表
			szQuery = "SELECT * FROM req_client_account WHERE deleted=0 AND oper_type=1 AND user_id="
					+ userinfo.id;
			szQuery += " ORDER BY req_date DESC LIMIT "
					+ ApiGlobal.getPageItemCount() * pageno + ", "
					+ ApiGlobal.getPageItemCount();

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				JSONObject jsonItem = new JSONObject();

				jsonItem.put("uid", rs.getLong("id"));
				jsonItem.put("req_date",
						ApiGlobal.Date2String(rs.getDate("req_date"), false));
				jsonItem.put("balance",
						String.format("%.2f", rs.getDouble("sum")));
				jsonItem.put("state", rs.getInt("status"));

				arrLogs.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrLogs;

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
 * 1,查询是否存在要撤销的记录
 * 2,设置req_client_account该条记录的状态为撤销
 * 3,在ts表中查询要撤销提现的记录
 * 3.1获取用户当前最新余额
 * 4,将上步骤查询出的ts表中的最新余额，加上提现金额，就等于该用户当前的余额。
   5,根据ts表中冻结记录的id查询freeze_points表，查询出相关的冻结记录
   6,将解冻的ts记录更新到5步骤查询出的freeze_points原来的冻结的记录中
 * @param source
 * @param userid
 * @param req_id
 * @param devtoken
 * @return
 */
	public SVCResult cancelWithdraw(String source, long userid, long req_id,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + req_id + "," + devtoken);

		if (source.equals("") || userid < 0 || req_id < 0
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
			} else if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (userinfo.person_verified != 1) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotVerifiedPerson;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}



			// Release req_client_account table
			stmt = dbConn.createStatement();
			// 1,查询是否存在要撤销的记录
			szQuery = "SELECT * FROM req_client_account WHERE deleted = 0 AND id = "
					+ req_id;
			rs = stmt.executeQuery(szQuery);

			if (!rs.next()) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			SVCReqClientAccount reqoper_log = SVCReqClientAccount
					.decodeFromResultSet(rs);
			rs.close();

			// 2,设置req_client_account该条记录的状态为撤销
			// Set status of req_client_account table
			szQuery = "UPDATE req_client_account SET status = 4 WHERE deleted = 0 AND id = "
					+ req_id;
			if (stmt.executeUpdate(szQuery) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			// 3,在ts表中查询要撤销提现的记录
			// Add record to 'ts' table
			szQuery = "SELECT * FROM ts WHERE deleted = 0 AND id = "
					+ reqoper_log.ts_id;
			rs = stmt.executeQuery(szQuery);

			if (!rs.next()) // No ts record. Abnormal situation
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			SVCTs tsinfo = SVCTs.decodeFromResultSet(rs);
			rs.close();

			String szApp = "";
			if (source.equals(ConstMgr.SRC_MAINAPP))
				szApp = ConstMgr.SRC_CHN_MAINAPP;
			else if (source.equals(ConstMgr.SRC_PINCHEAPP))
				szApp = ConstMgr.SRC_CHN_PINCHEAPP;
			else
				szApp = ConstMgr.STR_OTHERS;
			//3.1获取用户当前最新余额 added by zyl begin
			szQuery="select balance from ts where id="+userinfo.balance_ts;
			rs=stmt.executeQuery(szQuery);
			double balance_now=0;
			if(!rs.next()){
				balance_now=rs.getDouble("balance");
			}
			//added by zyl end
			//4,将上步骤查询出的ts表中最新的余额，加上提现金额，就等于该用户当前的余额。
			double cur_balance = balance_now + reqoper_log.sum;
			szQuery = "INSERT INTO ts ("
					+ "oper,"
					+ "ts_way,"
					+ "balance,"
					+ "ts_date,"
					+ "userid,"
					+ "remark,"
					+ "account_type,"
					+ "application,"
					+ "ts_type,"
					+ "sum"
					+ ") VALUES ("
					+ "2, 0, "
					+ cur_balance
					+ ", \""
					+ ApiGlobal.Date2String(new Date(), true)
					+ "\", "
					+ userid
					+ ", \""
					+ ApiGlobal.getCommentFromTxCode(dbConn, ConstMgr.txcode_userReleaseBalance)
					+ "\", 1, \"" + szApp + "\", \""
					+ ConstMgr.txcode_userReleaseBalance + "\", "
					+ reqoper_log.sum + ")";

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) == 1) {
				ResultSet genKeys = stmt.getGeneratedKeys();
				if (genKeys == null || !genKeys.next()) // No
														// generated
														// keys.
														// Abnormal
														// situation
				{
					result.retcode = ConstMgr.ErrCode_Exception;
					result.retmsg = ConstMgr.ErrMsg_Exception;
				} else {
					long new_ts_id = genKeys.getLong(1);
					//5,根据ts表中冻结记录的id查询freeze_points表，查询出相关的冻结记录
					szQuery = "SELECT * FROM freeze_points WHERE deleted = 0 AND userid = "
							+ userid + " AND freeze_ts_id = " + tsinfo.id;
					rs = stmt.executeQuery(szQuery);

					if (!rs.next()) // No freeze points log.
									// Abnormal situation
					{
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception;
					} else {
						SVCFreezePoints frz_points = SVCFreezePoints
								.decodeFromResultSet(rs);
						rs.close();
						//6,将解冻的ts记录更新到5步骤查询出的freeze_points原来的冻结的记录中
						szQuery = "UPDATE freeze_points SET state = 1, release_ts_id = "
								+ new_ts_id
								+ " WHERE deleted = 0 AND id = "
								+ frz_points.id;
						if (stmt.executeUpdate(szQuery) == 1) {
							// Everything is successful.
							// Update user table
							szQuery = "UPDATE user SET balance_ts = "
									+ new_ts_id + " WHERE id = " + userid;
							if (stmt.executeUpdate(szQuery) == 1) {
								// Success all.
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;

								JSONObject retdata = new JSONObject();
								retdata.put("curbalance", cur_balance);

								result.retdata = retdata;
							} else // Update failure.
									// Abnormal situation
							{
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception;
							}
						} else // Update failure. Abnormal
								// situation
						{
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception;
						}
					}
				}
			} else // Insert failure. Abnormal situation
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
			}

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
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
