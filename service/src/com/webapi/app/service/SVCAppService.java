package com.webapi.app.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCApp;
import com.webapi.structure.SVCAppVersion;
import com.webapi.structure.SVCResult;

public class SVCAppService {
	public SVCResult getChildAppList(String source, int platform) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + platform);

		// Check parameters
		if (source.equals("") || platform < 0) {
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
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();

			JSONArray arrApps = new JSONArray();

			stmt = dbConn.createStatement();
			stmt2 = dbConn.createStatement();
			szQuery = "";

			if (platform == ConstMgr.PLATFORM_ANDROID) {// Get android
														// applications
				szQuery = "SELECT * FROM app WHERE deleted = 0 AND (bundle_id='' OR url_scheme='')";
			} else {// Get ios applications
				szQuery = "SELECT * FROM app WHERE deleted = 0 AND pack_name=''";
			}

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCApp appInfo = SVCApp.decodeFromResultSet(rs);

				szQuery = "SELECT * FROM app_version WHERE deleted = 0 AND app_code = \""
						+ appInfo.app_code
						+ "\" ORDER BY upload_time DESC LIMIT 0,1";

				ResultSet rs_version = stmt2.executeQuery(szQuery);
				if (!rs_version.next())
					continue;

				SVCAppVersion app_version = SVCAppVersion
						.decodeFromResultSet(rs_version);

				JSONObject jsonItem = new JSONObject();

				jsonItem.put("uid", appInfo.id);
				jsonItem.put("name", appInfo.app_name);
				jsonItem.put("code", appInfo.app_code);
				jsonItem.put("image",
						ApiGlobal.getAbsoluteURL(app_version.icon_path));
				jsonItem.put("packname", platform == 1 ? appInfo.bundle_id
						: appInfo.pack_name);
				jsonItem.put("urlscheme", appInfo.url_scheme);
				jsonItem.put("latestver", app_version.version);
				jsonItem.put("curver", "");
				jsonItem.put("latestver", app_version.version_code);
				jsonItem.put("curver_code", 0);
				jsonItem.put("downloadurl",
						ApiGlobal.getAbsoluteURL(app_version.url));

				arrApps.add(jsonItem);

				rs_version.close();
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrApps;
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

	public SVCResult recordDownload(String source, long userid,
			String packname, int platform, String version) {
		SVCResult result = new SVCResult();
		ApiGlobal.logMessage(source + "," + userid + "," + packname + "," + platform + "," + version);

		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		// Check parameters
		if (source.equals("") || platform < 0 || packname.equals("")
				|| version.equals("")) {
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

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();

			stmt = dbConn.createStatement();
			szQuery = "SELECT app_version.id "
					+ "FROM app_version "
					+ "INNER JOIN app ON app_version.app_code = app.app_code "
					+ "WHERE (app.bundle_id = '' OR app.url_scheme = '') AND app.deleted = 0 AND app_version.deleted = 0 AND app.pack_name = \""
					+ packname + "\"";

			rs = stmt.executeQuery(szQuery);
			if (rs == null || !rs.next()) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
			} else {
				long app_ver_id = rs.getLong("id");

				rs.close();

				szQuery = "INSERT INTO app_download (app_version_id, userid, ip, ps_date, is_completed, deleted) VALUES ("
						+ app_ver_id
						+ ", "
						+ userid
						+ ", '', \""
						+ ApiGlobal.Date2String(new Date(), true) + "\", 1, 0)";

				if (stmt.executeUpdate(szQuery) == 1) {
					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
				} else {
					result.retcode = ConstMgr.ErrCode_Exception;
					result.retmsg = ConstMgr.ErrMsg_Exception;
				}
			}
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
 * 1,根据包名获得应用代号app_code
 * 2,根据app_code获得最新的版本
 * @param source
 * @param packname
 * @return
 */
	public SVCResult latestAppVersion(String source, String packname) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + packname);

		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		// Check parameters
		if (source.equals("") || packname.equals("")) {
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
		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();
			//1,根据包名获得应用代号app_code
			stmt = dbConn.createStatement();
			szQuery = "SELECT app_code FROM app WHERE deleted = 0 AND (pack_name = \""
					+ packname + "\" OR bundle_id = \"" + packname + "\")";

			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoAppInfo;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			String app_code = rs.getString("app_code");
			rs.close();
			//2,根据app_code获得最新的版本
			szQuery = "SELECT * FROM app_version WHERE deleted = 0 AND app_code = \""
					+ app_code + "\" ORDER BY upload_time DESC LIMIT 0, 1";
			rs = stmt.executeQuery(szQuery);
			if(!rs.next()){
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoAppInfo;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			SVCAppVersion app_ver = SVCAppVersion.decodeFromResultSet(rs);
			rs.close();

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

			JSONObject retdata = new JSONObject();
			retdata.put("latestver", app_ver.version);
			retdata.put("latestver_code", app_ver.version_code);
			retdata.put("downloadurl",
					ApiGlobal.getAbsoluteURL(app_ver.url));

			result.retdata = retdata;

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
