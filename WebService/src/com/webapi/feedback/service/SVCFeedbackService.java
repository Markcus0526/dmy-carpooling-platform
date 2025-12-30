package com.webapi.feedback.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCUser;

public class SVCFeedbackService {
	/**
	 * 简单插入feed_back表
	 * @param source
	 * @param userid
	 * @param contents
	 * @param devtoken
	 * @return
	 */
	public SVCResult advanceOpinion(String source, long userid,
			String contents, String devtoken)
	{
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + contents + "," + devtoken);

		if (source.equals("") || userid < 0 || contents.equals("")
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
			} else if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}



			String app = "";
			if (source.equals(ConstMgr.SRC_MAINAPP))
				app = "主软件";
			else if (source.equals(ConstMgr.SRC_PINCHEAPP))
				app = "拼车";
			else
				app = "其他";

			String szCurTime = ApiGlobal.Date2String(new Date(), true);

			stmt = dbConn.createStatement();

			try {
				szQuery = "INSERT INTO feedback (userid, content, application, ps_date) VALUES ("
						+ userinfo.id
						+ ", \""
						+ contents
						+ "\", \""
						+ app
						+ "\", \""
						+ szCurTime
						+ "\")";
			} catch (Exception ex) {
				ex.printStackTrace();

				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception + "(Encoding)";
			}


			stmt.executeUpdate(szQuery);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

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
