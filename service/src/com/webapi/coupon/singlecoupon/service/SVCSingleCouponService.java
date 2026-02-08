package com.webapi.coupon.singlecoupon.service;

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
import com.webapi.structure.SVCSingleCoupon;
import com.webapi.structure.SVCSysCoupon;
import com.webapi.structure.SVCUser;

public class SVCSingleCouponService {
	/**
	 * 
	 * @param source
	 * @param couponid
	 * @param userid
	 * @param devtoken
	 * @return
	 */
	public SVCResult getCouponDetails(String source, long couponid,
			long userid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + couponid + "," + userid + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || couponid < 0
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
			

			stmt = dbConn.createStatement();

			szQuery = "SELECT "
					+ "		single_coupon.id,"
					+ "		sys_coupon.goods,"
					+ "		sys_coupon.goods_or_cash,"
					+ "		sys_coupon.coupon_type,"
					+ "		sys_coupon.limit_val,"
					+ "		single_coupon.date_expired,"
					+ "		single_coupon.coupon_code,"
					+ "		single_coupon.ps_date "

					+ "FROM single_coupon "

					+ "INNER JOIN sys_coupon "
					+ "ON sys_coupon.id=single_coupon.syscoupon_id "

					+ "WHERE "
					+ "		sys_coupon.deleted = 0 AND "
					+ "		single_coupon.deleted = 0 AND "
					+ "		single_coupon.isenabled=1 AND "
					+ "		single_coupon.userid=" + userid + " AND "
					+ "		single_coupon.id=" + couponid;

			szQuery += " AND single_coupon.date_expired >= \""
					+ ApiGlobal.Date2String(new Date(), true) + "\"";

			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoCouponInfo;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}



			// Use Condition
			String szUseCond = "";
			int coupon_type = rs.getInt("coupon_type");
			int limit = rs.getInt("limit_val");
			if (rs.getInt("goods_or_cash") == 1) // This is cash coupon
			{
				if (coupon_type == 0) {
					szUseCond = ConstMgr.STR_WU;
				} else if (coupon_type == 1) {
					szUseCond = "1" + ConstMgr.STR_ZHANG + limit
							+ ConstMgr.STR_DIAN;
				} else if (coupon_type == 2) {
					szUseCond = ConstMgr.STR_NOTUSEWITHCOUPON;
				} else if (coupon_type == 3) {
					szUseCond = "1" + ConstMgr.STR_ZHANG + "/"
							+ ConstMgr.STR_DINGDAN;
				}
			} else {
				szUseCond = rs.getString("goods") + ConstMgr.STR_DIANJISHIYONG;
			}
			// /////////////////////////////////////////////////////////////////////////////////

			Date dt_exp = rs.getDate("date_expired");



			JSONObject retdata = new JSONObject();

			retdata.put("uid", rs.getLong("id"));
			retdata.put("product_name", rs.getString("goods"));
			retdata.put("coupon_code", rs.getString("coupon_code"));
			retdata.put("time",
					ApiGlobal.Date2String(rs.getDate("ps_date"), true));
			retdata.put("unitname", "");
			retdata.put("usecond", szUseCond);
			retdata.put("dateexp", ApiGlobal.Date2String(dt_exp, false));

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = retdata;

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
 * //1,获取该用户指定id以后的所有点券(实际上就是查询出指定时间之后的所有点券)，
			//  这应该有问题，因为用户使用点券是随机的，所以这里只查询出指定时间之后的点券，肯定会遗漏该时间点之前没有被使用过的点券
			//后来证实这其实没有问题，因为这个只是查询点券的列表，在CouponActivity.java的160行用
	2,查询该用户指定时间点之后的点券
 * @param source
 * @param userid
 * @param limitid
 * @param devtoken
 * @return
 */
	public SVCResult latestCoupon(String source, long userid, long limitid,
			String devtoken) {
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
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null;
		String szQuery = "", szQuery2 = "";

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
			

			JSONArray arrCoupons = new JSONArray();

			stmt = dbConn.createStatement();
			stmt2 = dbConn.createStatement();
			//1,获取该用户指定id以后的所有点券(实际上就是查询出指定时间之后的所有点券)，
			//  这应该有问题，因为用户使用点券是随机的，所以这里只查询出指定时间之后的点券，肯定会遗漏该时间点之前没有被使用过的点券
			//后来证实这其实没有问题，因为这个只是查询点券的列表，在CouponActivity.java的160行用
			szQuery = "SELECT * FROM single_coupon WHERE deleted = 0 AND isenabled = 1 AND id = "
					+ limitid;

			SVCSingleCoupon limit_coupon = null;

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				limit_coupon = SVCSingleCoupon.decodeFromResultSet(rs);
				rs.close();
			}
			//2,查询该用户指定时间点之后的点券
			szQuery = "SELECT "
					+ "		single_coupon.id,"
					+ "		single_coupon.date_expired,"
					+ "		single_coupon.coupon_code,"
					+ "		sys_coupon.goods,"
					+ "		sys_coupon.sum,"
					+ "		sys_coupon.goods_or_cash,"
					+ "		sys_coupon.apply_source,"
					+ "		sys_coupon.coupon_type,"
					+ "		sys_coupon.limit_val,"
					+ "		sys_coupon.valid_period_unit,"
					+ "		sys_coupon.app_spread_unit_id,"
					+ "		sys_coupon.valid_period "

					+ "FROM single_coupon "

					+ "INNER JOIN sys_coupon "
					+ "ON sys_coupon.id=single_coupon.syscoupon_id "

					+ "WHERE "
					+ "		single_coupon.deleted = 0 AND "
					+ "		sys_coupon.deleted = 0 AND "
					+ "		single_coupon.isenabled = 1 AND "
					+ "		single_coupon.date_expired >= CURRENT_TIMESTAMP() AND "
					+ "		single_coupon.userid=" + userid + " AND "
					+ "		single_coupon.isused=0 AND "
					+ "		(sys_coupon.apply_source=0 OR sys_coupon.apply_source=1) AND "
					+ "		sys_coupon.goods_or_cash=1 AND "
					+ "		sys_coupon.isenabled=1 ";


			if (limit_coupon != null)
				szQuery += " AND single_coupon.ps_date > \""
						+ ApiGlobal.Date2String(limit_coupon.ps_date, true)
						+ "\"";
			szQuery += " ORDER BY single_coupon.ps_date DESC";

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				JSONObject jsonItem = new JSONObject();

				// Apply range
				String szApplySource = "";
				int apply_source = rs.getInt("apply_source");
				if (apply_source == 0)
					szApplySource = ConstMgr.STR_ALL;
				else if (apply_source == 1)
					szApplySource = ConstMgr.STR_PINCHE;
				else if (apply_source == 2)
					szApplySource = ConstMgr.STR_DAIJIA;
				else
					szApplySource = ConstMgr.STR_OTHERS;
				// ///////////////////////////////////////////////////////////////////////////////////////

				// Contents
				int goods_or_cash = rs.getInt("goods_or_cash");
				double balance = rs.getDouble("sum");
				String goods = rs.getString("goods");

				String szContents = "";
				if (goods_or_cash == 1) // Cash coupon
				{
					szContents = "" + String.format("%.2f", balance)
							+ ConstMgr.STR_DIAN;
				} else // Goods coupon
				{
					szContents = goods;
				}
				// ////////////////////////////////////////////////////////////////////////////////////////

				// Use Condition
				String szUseCond = "";
				int coupon_type = rs.getInt("coupon_type");
				int limit = rs.getInt("limit_val");
				if (goods_or_cash == 1) // This is cash coupon
				{
					if (coupon_type == 0) {
						szUseCond = ConstMgr.STR_WU;
					} else if (coupon_type == 1) {
						szUseCond = "1" + ConstMgr.STR_ZHANG + limit
								+ ConstMgr.STR_DIAN;
					} else if (coupon_type == 2) {
						szUseCond = ConstMgr.STR_NOTUSEWITHCOUPON;
					} else if (coupon_type == 3) {
						szUseCond = "1" + ConstMgr.STR_ZHANG + "/"
								+ ConstMgr.STR_DINGDAN;
					}
				} else {
					szUseCond = goods + ConstMgr.STR_DIANJISHIYONG;
				}
				// /////////////////////////////////////////////////////////////////////////////////

				// App spread unit info
				String szUnitName = "";
				long unit_id = rs.getLong("app_spread_unit_id");
				if (goods_or_cash == 1) // Cash coupon
				{
					szQuery2 = "SELECT name FROM app_spread_unit WHERE deleted = 0 AND id = "
							+ unit_id;

					ResultSet rs2 = stmt2.executeQuery(szQuery2);
					if (rs2.next())
						szUnitName = rs2.getString("name");

					if (rs2 != null)
						rs2.close();
				}
				// /////////////////////////////////////////////////////////////////////////////////

				Date dt_exp = rs.getDate("date_expired");

				jsonItem.put("uid", rs.getLong("id"));
				jsonItem.put("range", szApplySource);
				jsonItem.put("contents", szContents);
				jsonItem.put("usecond", szUseCond);
				jsonItem.put("dateexp", ApiGlobal.Date2String(dt_exp, false));
				jsonItem.put("couponcode", rs.getString("coupon_code"));
				jsonItem.put("unitname", szUnitName);
				jsonItem.put("is_goods", goods_or_cash == 1 ? 0 : 1);

				arrCoupons.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrCoupons;

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


/**
 * 此方法同latestCoupon，只是加了分页功能
 * @param source
 * @param userid
 * @param pageno
 * @param devtoken
 * @return
 */
	public SVCResult pagedCoupon(String source, long userid, int pageno,
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
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null;
		String szQuery = "", szQuery2 = "";

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
			

			JSONArray arrCoupons = new JSONArray();

			stmt = dbConn.createStatement();
			stmt2 = dbConn.createStatement();

			szQuery = "SELECT "
					+ "		single_coupon.id,"
					+ "		single_coupon.date_expired,"
					+ "		single_coupon.coupon_code,"
					+ "		sys_coupon.goods,"
					+ "		sys_coupon.sum,"
					+ "		sys_coupon.goods_or_cash,"
					+ "		sys_coupon.apply_source,"
					+ "		sys_coupon.coupon_type,"
					+ "		sys_coupon.limit_val,"
					+ "		sys_coupon.valid_period_unit,"
					+ "		sys_coupon.app_spread_unit_id,"
					+ "		sys_coupon.valid_period "

					+ "FROM single_coupon "

					+ "INNER JOIN sys_coupon "
					+ "ON sys_coupon.id=single_coupon.syscoupon_id "

					+ "WHERE "
					+ "		single_coupon.deleted = 0 AND "
					+ "		sys_coupon.deleted = 0 AND "
					+ "		single_coupon.isenabled = 1 AND "
					+ "		single_coupon.date_expired >= CURRENT_TIMESTAMP() AND "
					+ "		single_coupon.userid=" + userid + " AND "
					+ "		single_coupon.isused=0 AND "
					+ "		(sys_coupon.apply_source=0 OR sys_coupon.apply_source=1) AND "
					+ "		sys_coupon.goods_or_cash=1 AND "
					+ "		sys_coupon.isenabled=1 ";

			szQuery += " ORDER BY single_coupon.ps_date DESC LIMIT "
					+ ApiGlobal.getPageItemCount() * pageno + ","
					+ ApiGlobal.getPageItemCount();

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				JSONObject jsonItem = new JSONObject();

				// Apply range
				String szApplySource = "";
				int apply_source = rs.getInt("apply_source");
				if (apply_source == 0)
					szApplySource = ConstMgr.STR_ALL;
				else if (apply_source == 1)
					szApplySource = ConstMgr.STR_PINCHE;
				else if (apply_source == 2)
					szApplySource = ConstMgr.STR_DAIJIA;
				else
					szApplySource = ConstMgr.STR_OTHERS;
				// ///////////////////////////////////////////////////////////////////////////////////////

				// Contents
				int goods_or_cash = rs.getInt("goods_or_cash");
				double balance = rs.getDouble("sum");
				String goods = rs.getString("goods");

				String szContents = "";
				if (goods_or_cash == 1) // Cash coupon
				{
					szContents = "" + String.format("%.2f", balance)
							+ ConstMgr.STR_DIAN;
				} else // Goods coupon
				{
					szContents = goods;
				}
				// ////////////////////////////////////////////////////////////////////////////////////////

				// Use Condition
				String szUseCond = "";
				int coupon_type = rs.getInt("coupon_type");
				int limit = rs.getInt("limit_val");
				if (goods_or_cash == 1) // This is cash coupon
				{
					if (coupon_type == 0) {
						szUseCond = ConstMgr.STR_WU;
					} else if (coupon_type == 1) {
						szUseCond = "1" + ConstMgr.STR_ZHANG + limit
								+ ConstMgr.STR_DIAN;
					} else if (coupon_type == 2) {
						szUseCond = ConstMgr.STR_NOTUSEWITHCOUPON;
					} else if (coupon_type == 3) {
						szUseCond = "1" + ConstMgr.STR_ZHANG + "/"
								+ ConstMgr.STR_DINGDAN;
					}
				} else {
					szUseCond = goods + ConstMgr.STR_DIANJISHIYONG;
				}
				// /////////////////////////////////////////////////////////////////////////////////

				// App spread unit info
				String szUnitName = "";
				long unit_id = rs.getLong("app_spread_unit_id");
				if (goods_or_cash == 1) // Cash coupon
				{
					szQuery2 = "SELECT name FROM app_spread_unit WHERE deleted = 0 AND id = "
							+ unit_id;

					ResultSet rs2 = stmt2.executeQuery(szQuery2);
					if (rs2.next())
						szUnitName = rs2.getString("name");

					if (rs2 != null)
						rs2.close();
				}
				// /////////////////////////////////////////////////////////////////////////////////

				Date dt_exp = rs.getDate("date_expired");

				jsonItem.put("uid", rs.getLong("id"));
				jsonItem.put("range", szApplySource);
				jsonItem.put("contents", szContents);
				jsonItem.put("usecond", szUseCond);
				jsonItem.put("dateexp", ApiGlobal.Date2String(dt_exp, false));
				jsonItem.put("couponcode", rs.getString("coupon_code"));
				jsonItem.put("unitname", szUnitName);
				jsonItem.put("is_goods", goods_or_cash == 1 ? 0 : 1);

				arrCoupons.add(jsonItem);
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrCoupons;

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
/**
 * 1,根据活动编号查询相关活动
 * 2,判断此活动号一共允许发行多少张代金券，如果已经超出了该活动号规定的限制的话则不允许用户再添加此代金券
 * 3,判断该用户是否已经添加了此代金券，如果已经添加了则不允许再次添加
 * 4,为该用户产生一个代金券号码，并计算此代金券的有效期，
 * 5，将此用户的代金券插入到single_coupon表中,注意同时把active_code也插入到single_coupon表中
 * 6,完毕
 * @param source
 * @param userid
 * @param active_code
 * @param devtoken
 * @return
 */
	public SVCResult addCoupon(String source, long userid, String active_code,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + active_code + "," + devtoken);

		if (source.equals("") || userid < 0 || active_code.equals("")
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
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn, userid);
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

			//1,根据活动编号查询相关活动
			szQuery = "SELECT "
					+ "		activities.syscoupon_id,"
					+ "		sys_coupon.* "
					+ "FROM "
					+ "		activities "
					+ "INNER JOIN sys_coupon "
					+ "ON sys_coupon.id=activities.syscoupon_id "

					+ "WHERE "
					+ "		sys_coupon.deleted=0 AND "
					+ "		sys_coupon.isenabled=1 AND "
					+ "		activities.active_code=\"" + active_code + "\"";

			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) // No active coupon for the activity
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoCouponForTheActivity;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			SVCSysCoupon sys_coupon = SVCSysCoupon.decodeFromResultSet(rs);
			rs.close();

			// 2,判断此活动号一共允许发行多少张代金券，如果已经超出了该活动号规定的限制的话则不允许用户再添加此代金券
			int nUsedCount = 0;
			szQuery = "SELECT COUNT(*) AS count FROM single_coupon "
					+ "WHERE syscoupon_id = " + sys_coupon.id + " "
					+ "AND is_generated_by_active=1 " + "AND active_code = \""
					+ active_code + "\" " + "AND deleted=0 AND isenabled=1";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				nUsedCount = rs.getInt("count");

			if (rs != null)
				rs.close();

			if (nUsedCount >= sys_coupon.limit_count) {// 现在已经达到了该系统代金券发行的限制张数
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoRemainCoupon;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			// 3,判断该用户是否已经添加了此代金券，如果已经添加了则不允许再次添加
			szQuery = "SELECT * FROM single_coupon " + "WHERE syscoupon_id = "
					+ sys_coupon.id + " " + "AND is_generated_by_active=1 "
					+ "AND active_code = \"" + active_code + "\" "
					+ "AND deleted=0 AND isenabled=1 AND userid=" + userinfo.id;

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) // This user already get this coupon
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_CouponAlreadyGot;
				rs.close();

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			rs.close();

			// 4,为该用户产生一个代金券号码，并计算此代金券的有效期，
			// Prepare some parameters to save in db
			String szCouponCode = ApiGlobal.generateSingleCouponCode();// 为该用户产生一个代金券号码
			double sum = (sys_coupon.goods_or_cash == 1 ? sys_coupon.sum : 0);

			Date dt_exp = null;
			Calendar cal_cur = Calendar.getInstance();
			int valid_per = sys_coupon.valid_period;
			if (sys_coupon.valid_period_unit == 1) // Unit
													// day
			{
				cal_cur.add(Calendar.DAY_OF_YEAR, valid_per);
			} else if (sys_coupon.valid_period_unit == 2) // Unit
															// week
			{
				cal_cur.add(Calendar.WEEK_OF_YEAR, valid_per);
			} else if (sys_coupon.valid_period_unit == 3) // Unit
															// month
			{
				cal_cur.add(Calendar.MONTH, valid_per);
			} else if (sys_coupon.valid_period_unit == 4) // Unit
															// year
			{
				cal_cur.add(Calendar.YEAR, valid_per);
			}
			dt_exp = cal_cur.getTime();

			// 5，将此用户的代金券插入到single_coupon表中,注意同时把active_code也插入到single_coupon表中
			szQuery = "INSERT INTO single_coupon (" + "coupon_code," + "sum,"
					+ "date_expired," + "isused," + "password," + "ps_date,"
					+ "remark," + "userid," + "order_cs_id," + "isenabled,"
					+ "syscoupon_id," + "active_code,"
					+ "is_generated_by_active," + "deleted) VALUES (\""
					+ szCouponCode + "\"," + sum + ",\""
					+ ApiGlobal.Date2String(dt_exp, true) + "\"," + "0, '', \""
					+ ApiGlobal.Date2String(new Date(), true) + "\", \""
					+ sys_coupon.remark + "\", " + userinfo.id + ", 0, 1, "
					+ sys_coupon.id + ", \"" + active_code + "\", 1, 0)";

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			//6,处理此代金券的使用条件
			ResultSet genKeys = stmt.getGeneratedKeys();
			if (genKeys == null || !genKeys.next()) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			// Prepare some return parameters
			String szRange = "";
			if (sys_coupon.apply_source == 0) {
				szRange = ConstMgr.STR_ALL;
			} else if (sys_coupon.apply_source == 1) {
				szRange = ConstMgr.STR_PINCHE;
			} else {
				szRange = ConstMgr.STR_DAIJIA;
			}

			String contents = "";
			if (sys_coupon.goods_or_cash == 1) // Cash
												// coupon
			{
				contents = "" + sys_coupon.sum + ConstMgr.STR_DIAN;
			} else {
				contents = sys_coupon.goods;
			}

			String useCond = "";//使用此代金券的条件
			if (sys_coupon.goods_or_cash == 1) {
				if (sys_coupon.coupon_type == 0) // No
													// condition
				{
					useCond = ConstMgr.STR_WU;
				} else if (sys_coupon.coupon_type == 1) // Every
														// 'sys_coupon.limit'
														// cash
														// can
														// consume
														// one
														// coupon
				{
					useCond = "1" + ConstMgr.STR_ZHANG + "/" + sys_coupon.limit_val
							+ ConstMgr.STR_DIAN;
				} else if (sys_coupon.coupon_type == 2) // Cannot
														// use
														// with
														// other
														// coupon
				{
					useCond = ConstMgr.STR_NOTUSEWITHCOUPON;
				} else if (sys_coupon.coupon_type == 3) // Can
														// use
														// only
														// one
														// coupon
														// every
														// order
				{
					useCond = "1" + ConstMgr.STR_ZHANG + "/"
							+ ConstMgr.STR_DINGDAN;
				}
			} else {
				useCond = sys_coupon.goods + ConstMgr.STR_DIANJISHIYONG;
			}

			JSONObject jsonNewLog = new JSONObject();
			jsonNewLog.put("uid", genKeys.getLong(1));
			jsonNewLog.put("range", szRange);
			jsonNewLog.put("contents", contents);
			jsonNewLog.put("usecond", useCond);
			jsonNewLog.put("dateexp", ApiGlobal.Date2String(dt_exp, false));
			jsonNewLog.put("is_goods", sys_coupon.goods_or_cash == 1 ? 0 : 1);

			jsonNewLog.put("couponcode", szCouponCode);

			genKeys.close();
			
			//7,获得与此代金券相关联的合作单位名称
			// Get app spread unit info
			szQuery = "SELECT name FROM app_spread_unit WHERE deleted = 0 AND id = "
					+ sys_coupon.app_spread_unit_id;
			rs = stmt.executeQuery(szQuery);

			if (!rs.next())
				jsonNewLog.put("unitname", "");
			else
				jsonNewLog.put("unitname", rs.getString("name"));

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = jsonNewLog;

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
