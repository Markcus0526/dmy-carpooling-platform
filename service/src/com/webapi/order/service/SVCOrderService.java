package com.webapi.order.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.STPushNotificationCustomData;
import com.webapi.structure.STPushNotificationData;
import com.webapi.structure.SVCCity;
import com.webapi.structure.SVCEvaluationCS;
import com.webapi.structure.SVCFreezePoints;
import com.webapi.structure.SVCGlobalParams;
import com.webapi.structure.SVCOrderExecCS;
import com.webapi.structure.SVCOrderLongDistanceDetails;
import com.webapi.structure.SVCOrderLongDistanceUsersCS;
import com.webapi.structure.SVCOrderOnOffDutyDetails;
import com.webapi.structure.SVCOrderOnOffDutyDivide;
import com.webapi.structure.SVCOrderOnOffDutyGrab;
import com.webapi.structure.SVCOrderTempDetails;
import com.webapi.structure.SVCOrderTempGrab;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCTs;
import com.webapi.structure.SVCUser;
import com.webapi.structure.SVCUserCar;
import com.webapi.structure.SVCUserOnline;

public class SVCOrderService {
	private class OrderObjectTimeForDriverComparator implements
			Comparator<JSONObject> {
		public int compare(JSONObject object1, JSONObject object2) {
			String szTime1 = object1.getString("create_time");
			String szTime2 = object2.getString("create_time");

			Date dt1 = ApiGlobal.String2Date(szTime1);
			Date dt2 = ApiGlobal.String2Date(szTime2);

			if (dt1.equals(dt2))
				return 0;
			else if (dt1.after(dt2))
				return -1;
			else
				return 1;
		}
	}

	private class OrderObjectTimeForPassComparator implements
			Comparator<JSONObject> {
		public int compare(JSONObject object1, JSONObject object2) {
			String szTime1 = object1.getString("create_time");
			String szTime2 = object2.getString("create_time");

			Date dt1 = ApiGlobal.String2Date(szTime1);
			Date dt2 = ApiGlobal.String2Date(szTime2);

			if (dt1.equals(dt2))
				return 0;
			else if (dt1.after(dt2))
				return -1;
			else
				return 1;
		}
	}

	/*
	 * Function to get once orders for driver's "我的订单" activity
	 * 
	 * @param driverid : userid of driver
	 * 
	 * @param limit_time : based time
	 * 
	 * @param citycode : the city code to search order
	 * 
	 * @param time_first : aligmnent rule. if true, result is rearranged with
	 * the time when it is created
	 */
	private JSONArray getOnceOrdersForDriverAfterTime(long driverid,
			Date limit_time, String citycode, boolean time_first,
			boolean wait_oper) {
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		JSONArray arrOrders = new JSONArray();

		try {
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			String sysfee_desc = "";
			double sysfee_value = 0, price;
			double ratio = 0, integer_ = 0, active = 0;
			int state, has_eval = 0;
			String state_desc = "";

			SVCGlobalParams global_params = null;
			SVCCity city = null;
			// 1,获取该城市平台信息费比例/固定金额
			szQuery = "SELECT * FROM global_params WHERE deleted=0";
			rs = stmt.executeQuery(szQuery);
			// Global parameters must exist. If not exist, it is abnormal
			rs.next();
			global_params = SVCGlobalParams.decodeFromResultSet(rs);
			rs.close();

			szQuery = "SELECT * FROM city WHERE code=\"" + citycode
					+ "\" AND deleted=0";
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				city = SVCCity.decodeFromResultSet(rs);
				rs.close();

				if (city.active < 0) // City parameters are not set. Use global
										// params
				{
					ratio = global_params.ratio;
					integer_ = global_params.integer_;
					active = global_params.active;
				} else // City parameters are set. Use city params
				{
					ratio = city.ratio;
					integer_ = city.integer_;
					active = city.active;
				}
			} else {
				ratio = global_params.ratio;
				integer_ = global_params.integer_;
				active = global_params.active;
			}
			rs.close();

			// 2,获取该车主的订单，主要查order_exec_cs表，以这个表为基准
			szQuery = "SELECT"
					+ "		OrderWithUserInfo.*,"

					+ "		evaluation_cs.level,"
					+ "		evaluation_cs.msg "

					+ "FROM"

					+ "		(SELECT"
					+ "		  OrderInfo.*,"

					+ "		  user.img,"
					+ "		  user.nickname,"
					+ "		  user.sex,"
					+ "		  TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "

					+ "		FROM "
					+ "			(SELECT "
					+ "			  order_temp_details.id," // Returning main ID
					+ "			  order_temp_details.order_num,"
					+ "			  order_temp_details.price,"
					+ "			  order_temp_details.start_addr,"
					+ "			  order_temp_details.end_addr,"
					+ "			  order_temp_details.pre_time,"
					+ "			  order_temp_details.status,"
					+ "			  order_temp_details.order_cs_id,"
					+ "			  order_temp_details.ps_date,"

					+ "			  order_exec_cs.id AS exec_id, "
					+ "			  order_exec_cs.driver,"
					+ "			  order_exec_cs.passenger,"
					+ "			  order_exec_cs.has_evaluation_driver, "
					+ "			  order_exec_cs.cr_date "

					+ "			FROM "
					+ "			  order_temp_details "

					+ "			INNER JOIN "
					+ "			  order_exec_cs "
					+ "			ON "
					+ "			  order_exec_cs.id = order_temp_details.order_cs_id "

					+ "			WHERE "
					+ "			  order_exec_cs.driver="
					+ driverid
					+ " AND "
					+ "			  order_exec_cs.order_type=1 AND "
					+ "			  order_exec_cs.deleted=0 AND "
					+ "			  order_temp_details.order_city LIKE \"%"
					+ citycode
					+ "%\" AND "
					+ "			  order_temp_details.deleted=0) AS OrderInfo "

					+ "		INNER JOIN user "
					+ "		ON user.id = OrderInfo.passenger) AS OrderWithUserInfo "

					+ "LEFT JOIN  evaluation_cs "
					+ "ON "
					+ "		evaluation_cs.from_userid = OrderWithUserInfo.driver AND "
					+ "		evaluation_cs.order_cs_id=OrderWithUserInfo.exec_id ";

			if (wait_oper) {
				szQuery += " WHERE (OrderWithUserInfo.status=2 OR (OrderWithUserInfo.status=7 AND evaluation_cs.level IS NULL))";

				if (limit_time != null)
					szQuery += "	AND OrderWithUserInfo.cr_date > \""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";
			} else {
				if (limit_time != null)
					szQuery += "	WHERE OrderWithUserInfo.cr_date > \""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";

				if (time_first)
					szQuery += "	ORDER BY OrderWithUserInfo.cr_date DESC, FIELD(OrderWithUserInfo.status, 2,7,6,5,4,3,1,8) ";
				else
					szQuery += "	ORDER BY FIELD(OrderWithUserInfo.status, 2,7,6,5,4,3,1,8), OrderWithUserInfo.cr_date DESC ";
			}

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				price = rs.getDouble("price");
				if (active == 0)
					sysfee_value = price * ratio / 100;
				else
					sysfee_value = Math.min(integer_, price);
				sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
						+ ApiGlobal.Double2String(sysfee_value, 2)
						+ ConstMgr.STR_DIAN;

				has_eval = rs.getInt("has_evaluation_driver");
				state = rs.getInt("status");
				switch (state) {
				case 1:
					state_desc = ConstMgr.STR_ORDERSTATE_FABU;
					break;
				case 2:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
					break;
				case 3:
					state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
					break;
				case 4:
					state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
					break;
				case 5:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
					break;
				case 6:
					state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
					break;
				case 7: {
					if (has_eval == 1) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
						state = 8;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
					}
				}
					break;
				case 8:
					state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
					state = 9;
					break;
				default:
					state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
				}

				int evaluated = 0;
				String eval_content = "";
				String eval_desc = "";

				if (rs.getInt("has_evaluation_driver") == 0) // Not evaluated
																// yet
				{
					evaluated = 0;
					eval_content = "";
					eval_desc = ConstMgr.STR_WEIPINGJIA;
				} else {
					evaluated = rs.getInt("level");
					eval_content = rs.getString("msg");
					if (eval_content == null)
						eval_content = "";

					if (evaluated == 1)
						eval_desc = ConstMgr.STR_HAOPING;
					else if (evaluated == 2)
						eval_desc = ConstMgr.STR_ZHONGPING;
					else if (evaluated == 3)
						eval_desc = ConstMgr.STR_CHAPING;
				}

				JSONObject order_item = new JSONObject();

				order_item.put("uid", rs.getLong("id"));
				order_item.put("order_num", rs.getString("order_num"));
				order_item.put("type", 1);
				order_item.put("type_desc", ConstMgr.STR_ONCE);
				order_item.put("pass_id", rs.getLong("passenger"));
				order_item.put("pass_img",
						ApiGlobal.getAbsoluteURL(rs.getString("img")));
				order_item.put("pass_name", rs.getString("nickname"));
				order_item.put("pass_gender", rs.getInt("sex"));
				order_item.put("pass_age", rs.getInt("age"));
				order_item.put("pass_count", 1);
				order_item.put("price", rs.getDouble("price"));

				order_item.put("sysinfo_fee", sysfee_value);
				order_item.put("sysinfo_fee_desc", sysfee_desc);

				order_item.put("start_city", "");
				order_item.put("end_city", "");
				order_item.put("start_addr", rs.getString("start_addr"));
				order_item.put("end_addr", rs.getString("end_addr"));
				order_item.put("start_time", ApiGlobal
						.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
								.getString("pre_time"))));
				order_item.put("evaluated", evaluated);
				order_item.put("eval_content", eval_content);
				order_item.put("evaluated_desc", eval_desc);
				order_item.put("state", state);
				order_item.put("state_desc", state_desc);
				order_item.put("create_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("cr_date")), true));

				arrOrders.add(order_item);
			}

			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
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

		return arrOrders;
	}

	private JSONArray getOnceOrdersForDriverBeforeTime(long driverid,
			Date limit_time, int count, String citycode, boolean time_first,
			boolean wait_oper) {
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		JSONArray arrOrders = new JSONArray();

		try {
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			String sysfee_desc = "";
			double sysfee_value = 0, price;
			double insu_fee = 0;// 保险费 modify by czq
			// long insu_id = 0;//保单id modify by czq
			double ratio = 0, integer_ = 0, active = 0;
			int state, has_eval = 0;
			String state_desc = "";

			SVCGlobalParams global_params = null;
			SVCCity city = null;

			szQuery = "SELECT * FROM global_params WHERE deleted=0";
			rs = stmt.executeQuery(szQuery);
			// Global parameters must exist. If not exist, it is abnormal
			rs.next();
			global_params = SVCGlobalParams.decodeFromResultSet(rs);
			rs.close();
			insu_fee = global_params.insu_fee;//保险费使用全局参数变量 modify by chuzhiqiang
			
			szQuery = "SELECT * FROM city WHERE code=\"" + citycode
					+ "\" AND deleted=0";
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				city = SVCCity.decodeFromResultSet(rs);
				rs.close();

				if (city.active < 0) // City parameters are not set. Use global
										// params
				{
					ratio = global_params.ratio;
					integer_ = global_params.integer_;
					active = global_params.active;
					//insu_fee = global_params.insu_fee;//保险  modify by czq 
				} else // City parameters are set. Use city params
				{
					ratio = city.ratio;
					integer_ = city.integer_;
					active = city.active;
					//insu_fee = city.insu_fee;//保险  modify by czq 

				}
			} else {
				ratio = global_params.ratio;
				integer_ = global_params.integer_;
				active = global_params.active;
			}

			szQuery = "SELECT"
					+ "		OrderWithUserInfo.*,"

					+ "		evaluation_cs.level,"
					+ "		insu.* , "
					+ "		evaluation_cs.msg "

					+ "FROM"

					+ "		(SELECT"
					+ "		  OrderInfo.*,"

					+ "		  user.img,"
					+ "		  user.nickname,"
					+ "		  user.sex,"
					+ "		  user.phone,"
					+ "		  TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "

					+ "		FROM "
					+ "			(SELECT "
					+ "			  order_temp_details.id," // Returning main ID
					+ "			  order_temp_details.order_num,"
					+ "			  order_temp_details.price,"
					+ "			  order_temp_details.start_addr,"
					+ "			  order_temp_details.end_addr,"
					+ "			  order_temp_details.pre_time,"
					+ "			  order_temp_details.status,"
					+ "			  order_temp_details.order_cs_id,"
					+ "			  order_temp_details.ps_date,"

					+ "			  order_exec_cs.id AS exec_id, "
					+ "			  order_exec_cs.driver,"
					+ "			  order_exec_cs.passenger,"
					+ "			  order_exec_cs.has_evaluation_driver, "
					+ "			  order_exec_cs.insu_id_driver, "//保险  modify by czq 
					+ "			  order_exec_cs.cr_date "

					+ "			FROM "
					+ "			  order_temp_details "

					+ "			INNER JOIN "
					+ "			  order_exec_cs "
					+ "			ON "
					+ "			  order_exec_cs.id = order_temp_details.order_cs_id "

					+ "			WHERE "
					+ "			  order_exec_cs.driver="
					+ driverid
					+ " AND "
					+ "			  order_exec_cs.order_type=1 AND "
					+ "			  order_exec_cs.deleted=0 AND "
					+ "			  order_temp_details.order_city LIKE \"%"
					+ citycode
					+ "%\" AND "
					+ "			  order_temp_details.deleted=0) AS OrderInfo "

					+ "		INNER JOIN user "
					+ "		ON user.id = OrderInfo.passenger) AS OrderWithUserInfo "

					+ " LEFT JOIN  evaluation_cs "
					+ "ON "
					+ "		evaluation_cs.from_userid = OrderWithUserInfo.driver AND "
					+ "		evaluation_cs.order_cs_id=OrderWithUserInfo.exec_id "
					+ "		LEFT JOIN insurance insu ON OrderWithUserInfo.insu_id_driver = insu.id ";//modify by chuzhiqiang

			if (wait_oper) {
				szQuery += " WHERE (OrderWithUserInfo.status=2 OR (OrderWithUserInfo.status=7 AND evaluation_cs.level IS NULL))";

				if (limit_time != null)
					szQuery += " AND OrderWithUserInfo.cr_date < \""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";
			} else {
				if (limit_time != null)
					szQuery += " WHERE OrderWithUserInfo.cr_date < \""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";

				if (time_first)
					szQuery += "   ORDER BY OrderWithUserInfo.cr_date DESC, FIELD(OrderWithUserInfo.status, 2,7,6,5,4,3,1,8) LIMIT 0,"
							+ count;
				else
					szQuery += "   ORDER BY FIELD(OrderWithUserInfo.status, 2,7,6,5,4,3,1,8), OrderWithUserInfo.cr_date DESC LIMIT 0,"
							+ count;
			}

			rs = stmt.executeQuery(szQuery);

			System.out.println("sql----->" + szQuery);
			while (rs.next()) {
				price = rs.getDouble("price");
				if (active == 0)
					sysfee_value = price * ratio / 100;
				else
					sysfee_value = Math.min(integer_, price);
				sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
						+ ApiGlobal.Double2String(sysfee_value, 2)
						+ ConstMgr.STR_DIAN;

				has_eval = rs.getInt("has_evaluation_driver");
				state = rs.getInt("status");
				switch (state) {
				case 1:
					state_desc = ConstMgr.STR_ORDERSTATE_FABU;
					break;
				case 2:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
					break;
				case 3:
					state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
					break;
				case 4:
					state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
					break;
				case 5:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
					break;
				case 6:
					state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
					break;
				case 7: {
					if (has_eval == 1) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
						state = 8;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
					}
				}
					break;
				case 8:
					state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
					state = 9;
					break;
				default:
					state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
				}

				int evaluated = 0;
				String eval_content = "";
				String eval_desc = "";

				if (rs.getInt("has_evaluation_driver") == 0) // Not evaluated
																// yet
				{
					evaluated = 0;
					eval_content = "";
					eval_desc = ConstMgr.STR_WEIPINGJIA;
				} else {
					evaluated = rs.getInt("level");
					eval_content = rs.getString("msg");
					if (eval_content == null)
						eval_content = "";

					if (evaluated == 1)
						eval_desc = ConstMgr.STR_HAOPING;
					else if (evaluated == 2)
						eval_desc = ConstMgr.STR_ZHONGPING;
					else if (evaluated == 3)
						eval_desc = ConstMgr.STR_CHAPING;
				}

				JSONObject order_item = new JSONObject();

				order_item.put("uid", rs.getLong("id"));
				order_item.put("order_num", rs.getString("order_num"));
				order_item.put("type", 1);
				order_item.put("type_desc", ConstMgr.STR_ONCE);
				order_item.put("pass_id", rs.getLong("passenger"));
				order_item.put("pass_img",
						ApiGlobal.getAbsoluteURL(rs.getString("img")));
				order_item.put("pass_name", rs.getString("nickname"));
				order_item.put("pass_gender", rs.getInt("sex"));
				order_item.put("pass_age", rs.getInt("age"));
				order_item.put("pass_phone", rs.getString("phone"));//单次 拨打乘客 电话  modify  by  chuzhiqiang
				
				order_item.put("pass_count", 1);
				order_item.put("price", rs.getDouble("price"));

				order_item.put("sysinfo_fee", sysfee_value);
				order_item.put("sysinfo_fee_desc", sysfee_desc);
				
				order_item.put("insu_fee", insu_fee*2);//保险   一份执行订单对应两个保单   modify by czq 
				order_item.put("insu_id", rs.getLong("insu_id_driver"));//保单id  modify by czq 
				order_item.put("appl_no", rs.getString("appl_no"));//保单号  modify by czq 
				order_item.put("total_amount", rs.getDouble("total_amount"));//保单金额  modify by czq 
				order_item.put("isd_id", rs.getLong("isd_id"));//被保险人  modify by czq 
				order_item.put("isd_name", rs.getString("isd_name"));//被保险人姓名  modify by czq 
				order_item.put("insu_status", rs.getInt("insu_status"));//保单状态  modify by czq 
				order_item.put("effect_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("effect_time")),
						true));// 保单生效期 modify by czq
				order_item.put("insexpr_date", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("insexpr_date")),
						true));// 保单生效期 modify by czq

				order_item.put("start_city", "");
				order_item.put("end_city", "");
				order_item.put("start_addr", rs.getString("start_addr"));
				order_item.put("end_addr", rs.getString("end_addr"));
				order_item.put("start_time", ApiGlobal
						.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
								.getString("pre_time"))));
				order_item.put("evaluated", evaluated);
				order_item.put("eval_content", eval_content);
				order_item.put("evaluated_desc", eval_desc);
				order_item.put("state", state);
				order_item.put("state_desc", state_desc);
				order_item.put("create_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("cr_date")), true));

				arrOrders.add(order_item);
			}

			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
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

		return arrOrders;
	}

	// private JSONArray getOnOffOrdersForDriverAfterTime(long driverid,
	// Date limit_time, String citycode, boolean time_first) {
	// JSONArray arrOrders = new JSONArray();
	//
	// Connection dbConn = null;
	// Statement stmt = null;
	// ResultSet rs = null;
	// String szQuery = "";
	//
	// try {
	// dbConn = DBManager.getDBConnection();
	// stmt = dbConn.createStatement();
	//
	// String sysfee_desc = "";
	// double sysfee_value = 0, price;
	// double ratio = 0, integer_ = 0, active = 0;
	// int state, has_eval = 0;
	// String state_desc = "";
	//
	// SVCGlobalParams global_params = null;
	// SVCCity city = null;
	//
	// szQuery = "SELECT * FROM global_params WHERE deleted=0";
	// rs = stmt.executeQuery(szQuery);
	// // Global parameters must exist. If not exist, it is abnormal
	// rs.next();
	// global_params = SVCGlobalParams.decodeFromResultSet(rs);
	// rs.close();
	//
	// szQuery = "SELECT * FROM city WHERE code=\"" + citycode
	// + "\" AND deleted=0";
	// rs = stmt.executeQuery(szQuery);
	// if (rs.next()) {
	// city = SVCCity.decodeFromResultSet(rs);
	// rs.close();
	//
	// if (city.active < 0) // City parameters are not set. Use global
	// // params
	// {
	// ratio = global_params.ratio;
	// integer_ = global_params.integer_;
	// active = global_params.active;
	// } else // City parameters are set. Use city params
	// {
	// ratio = city.ratio;
	// integer_ = city.integer_;
	// active = city.active;
	// }
	// } else {
	// ratio = global_params.ratio;
	// integer_ = global_params.integer_;
	// active = global_params.active;
	// }
	//
	// szQuery = "SELECT "
	// + "  OrderWithUserInfo.*,"
	// + "  evaluation_cs.level, "
	// + "  evaluation_cs.msg "
	//
	// + "FROM "
	// + "  (SELECT "
	// + "	OrderInfo.*,"
	//
	// + "	user.id, "
	// + "	user.img, "
	// + "	user.nickname, "
	// + "	user.sex, "
	// + "	TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "
	//
	// + "  FROM "
	// + "	(SELECT "
	// + "			GrandOrderInfo.*,"
	// + "			OrderExecInfo.* "
	//
	// + "		FROM "
	// + "			(SELECT "
	// + "				order_onoffduty_details.id AS mainID," //
	// order_onoffduty_details_id
	// + "  			order_onoffduty_details.order_num,"
	// + "  			order_onoffduty_details.price,"
	// + "  			order_onoffduty_details.start_addr,"
	// + "  			order_onoffduty_details.end_addr,"
	// + "  			order_onoffduty_details.status," // main order
	// // status
	// + "  			order_onoffduty_details.publish_date,"
	//
	// + "  			order_onoffduty_divide.id AS divide_id," // divide_id
	// + "  			order_onoffduty_divide.orderdetails_id,"
	// + "  			order_onoffduty_divide.which_days "
	//
	// + "			FROM order_onoffduty_details "
	//
	// + "			INNER JOIN order_onoffduty_divide "
	// +
	// "			ON order_onoffduty_divide.orderdetails_id=order_onoffduty_details.id "
	//
	// + "			WHERE "
	// + "				order_onoffduty_details.deleted=0 AND "
	// + "				order_onoffduty_divide.deleted=0"
	// + "			) AS GrandOrderInfo "
	//
	// + "		INNER JOIN "
	// + "			(SELECT "
	// + "  			order_exec_cs.id AS order_exec_cs_id," // order_exec_cs_id
	// + "  			order_exec_cs.passenger,"
	// + "  			order_exec_cs.driver,"
	// + "  			order_exec_cs.from_,"
	// + "  			order_exec_cs.to_,"
	// + "  			order_exec_cs.remark,"
	// + "  			order_exec_cs.pre_time,"
	// + "  			order_exec_cs.cr_date,"
	// + "  			order_exec_cs.has_evaluation_passenger,"
	// + "  			order_exec_cs.has_evaluation_driver,"
	// + "  			order_exec_cs.status AS exec_status,"
	//
	// + "				order_onoffduty_exec_details.onoffduty_divide_id "
	//
	// + "			FROM order_exec_cs "
	//
	// + "			INNER JOIN order_onoffduty_exec_details "
	// + "			ON order_onoffduty_exec_details.order_cs_id=order_exec_cs.id "
	//
	// + "			WHERE "
	// + "				order_exec_cs.driver="
	// + driverid
	// + " AND "
	// + "				order_exec_cs.order_type=3 AND "
	// + "				order_exec_cs.deleted=0"
	//
	// + "			ORDER BY order_exec_cs.cr_date DESC "
	// + "			) AS OrderExecInfo "
	//
	// + "		ON GrandOrderInfo.divide_id=OrderExecInfo.onoffduty_divide_id"
	// + "		GROUP BY GrandOrderInfo.divide_id"
	// + "		) AS OrderInfo "
	//
	// + "  INNER JOIN user "
	// + "  ON user.id = OrderInfo.passenger) AS OrderWithUserInfo "
	//
	// + "LEFT JOIN  evaluation_cs "
	// + "ON "
	// + "		evaluation_cs.order_cs_id=OrderWithUserInfo.order_exec_cs_id AND "
	// + "		evaluation_cs.from_userid=OrderWithUserInfo.driver ";
	//
	// if (limit_time != null)
	// szQuery += "	WHERE OrderWithUserInfo.cr_date > \""
	// + ApiGlobal.Date2String(limit_time, true) + "\"";
	//
	// if (time_first)
	// szQuery +=
	// "	ORDER BY OrderWithUserInfo.cr_date DESC, FIELD(OrderWithUserInfo.exec_status, 2,7,6,5,4,3,1,8)";
	// else
	// szQuery +=
	// "	ORDER BY FIELD(OrderWithUserInfo.exec_status, 2,7,6,5,4,3,1,8), OrderWithUserInfo.cr_date DESC";
	//
	// rs = stmt.executeQuery(szQuery);
	// while (rs.next()) {
	// price = rs.getDouble("price");
	// if (active == 0)
	// sysfee_value = price * ratio / 100;
	// else
	// sysfee_value = Math.min(integer_, price);
	// sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
	// + ApiGlobal.Double2String(sysfee_value, 2)
	// + ConstMgr.STR_DIAN;
	//
	// has_eval = rs.getInt("has_evaluation_driver");
	// state = rs.getInt("exec_status");
	// switch (state) {
	// case 2:
	// state_desc = ConstMgr.STR_ORDERSTATE_YIYUYUE;
	// break;
	// case 3:
	// state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
	// break;
	// case 4:
	// state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
	// break;
	// case 5:
	// state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
	// break;
	// case 6:
	// state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
	// break;
	// case 7: {
	// if (has_eval == 1) {
	// state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
	// state = 8;
	// } else {
	// state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
	// }
	// }
	// break;
	// case 8:
	// state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
	// state = 9;
	// break;
	// default:
	// state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
	// }
	//
	// int evaluated = 0;
	// String eval_content = "";
	// String eval_desc = "";
	//
	// if (rs.getInt("has_evaluation_driver") == 0) // Not evaluated
	// // yet
	// {
	// evaluated = 0;
	// eval_content = "";
	// eval_desc = ConstMgr.STR_WEIPINGJIA;
	// } else {
	// evaluated = rs.getInt("level");
	// eval_content = rs.getString("msg");
	// if (eval_content == null)
	// eval_content = "";
	//
	// if (evaluated == 1)
	// eval_desc = ConstMgr.STR_HAOPING;
	// else if (evaluated == 2)
	// eval_desc = ConstMgr.STR_ZHONGPING;
	// else if (evaluated == 3)
	// eval_desc = ConstMgr.STR_CHAPING;
	// }
	//
	// JSONObject order_item = new JSONObject();
	//
	// order_item.put("uid", rs.getLong("mainID")); // ID of
	// // order_onoffduty_details
	// order_item.put("order_num", rs.getString("order_num"));
	// order_item.put("type", 2);
	// order_item.put("type_desc", ConstMgr.STR_ONOFFDUTY);
	// order_item.put("pass_id", rs.getLong("passenger"));
	// order_item.put("pass_img",
	// ApiGlobal.getAbsoluteURL(rs.getString("img")));
	// order_item.put("pass_name", rs.getString("nickname"));
	// order_item.put("pass_gender", rs.getInt("sex"));
	// order_item.put("pass_age", rs.getInt("age"));
	// order_item.put("pass_count", 1);
	// order_item.put("price", rs.getDouble("price"));
	//
	// order_item.put("sysinfo_fee", sysfee_value);
	// order_item.put("sysinfo_fee_desc", sysfee_desc);
	//
	// order_item.put("start_city", "");
	// order_item.put("end_city", "");
	// order_item.put("start_addr", rs.getString("start_addr"));
	// order_item.put("end_addr", rs.getString("end_addr"));
	// order_item.put("start_time", ApiGlobal.Date2Time(ApiGlobal
	// .String2Date(rs.getString("pre_time"))));
	// order_item.put("evaluated", evaluated);
	// order_item.put("eval_content", eval_content);
	// order_item.put("evaluated_desc", eval_desc);
	// order_item.put("state", state);
	// order_item.put("state_desc", state_desc);
	// order_item.put("create_time", ApiGlobal.Date2String(
	// ApiGlobal.String2Date(rs.getString("cr_date")), true));
	//
	// arrOrders.add(order_item);
	// }
	//
	// rs.close();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// } finally {
	// // Close result set
	// if (rs != null) {
	// try {
	// rs.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// // Close normal statement
	// if (stmt != null) {
	// try {
	// stmt.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// // Close db connection
	// if (dbConn != null) {
	// try {
	// dbConn.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	// }
	//
	// return arrOrders;
	// }
	//
	// private JSONArray getOnOffOrdersForDriverBeforeTime(long driverid,
	// Date limit_time, int count, String citycode, boolean time_first) {
	// JSONArray arrOrders = new JSONArray();
	//
	// Connection dbConn = null;
	// Statement stmt = null;
	// ResultSet rs = null;
	// String szQuery = "";
	//
	// try {
	// dbConn = DBManager.getDBConnection();
	// stmt = dbConn.createStatement();
	//
	// String sysfee_desc = "";
	// double sysfee_value = 0, price;
	// double ratio = 0, integer_ = 0, active = 0;
	// int state, has_eval = 0;
	// String state_desc = "";
	//
	// SVCGlobalParams global_params = null;
	// SVCCity city = null;
	//
	// szQuery = "SELECT * FROM global_params WHERE deleted=0";
	// rs = stmt.executeQuery(szQuery);
	// // Global parameters must exist. If not exist, it is abnormal
	// rs.next();
	// global_params = SVCGlobalParams.decodeFromResultSet(rs);
	// rs.close();
	//
	// szQuery = "SELECT * FROM city WHERE code=\"" + citycode
	// + "\" AND deleted=0";
	// rs = stmt.executeQuery(szQuery);
	// if (rs.next()) {
	// city = SVCCity.decodeFromResultSet(rs);
	// rs.close();
	//
	// if (city.active < 0) // City parameters are not set. Use global
	// // params
	// {
	// ratio = global_params.ratio;
	// integer_ = global_params.integer_;
	// active = global_params.active;
	// } else // City parameters are set. Use city params
	// {
	// ratio = city.ratio;
	// integer_ = city.integer_;
	// active = city.active;
	// }
	// } else {
	// ratio = global_params.ratio;
	// integer_ = global_params.integer_;
	// active = global_params.active;
	// }
	//
	// szQuery = "SELECT "
	// + "  OrderWithUserInfo.*,"
	// + "  evaluation_cs.level, "
	// + "  evaluation_cs.msg "
	//
	// + "FROM "
	// + "  (SELECT "
	// + "	OrderInfo.*,"
	//
	// + "	user.id, "
	// + "	user.img, "
	// + "	user.nickname, "
	// + "	user.sex, "
	// + "	TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "
	//
	// + "  FROM "
	// + "	(SELECT "
	// + "			GrandOrderInfo.*,"
	// + "			OrderExecInfo.* "
	//
	// + "		FROM "
	// + "			(SELECT "
	// + "				order_onoffduty_details.id AS mainID," //
	// order_onoffduty_details_id
	// + "  			order_onoffduty_details.order_num,"
	// + "  			order_onoffduty_details.price,"
	// + "  			order_onoffduty_details.start_addr,"
	// + "  			order_onoffduty_details.end_addr,"
	// + "  			order_onoffduty_details.status," // main order
	// // status
	// + "  			order_onoffduty_details.publish_date,"
	//
	// + "  			order_onoffduty_divide.id AS divide_id," // divide_id
	// + "  			order_onoffduty_divide.orderdetails_id,"
	// + "  			order_onoffduty_divide.which_days "
	//
	// + "			FROM order_onoffduty_details "
	//
	// + "			INNER JOIN order_onoffduty_divide "
	// +
	// "			ON order_onoffduty_divide.orderdetails_id=order_onoffduty_details.id "
	//
	// + "			WHERE "
	// + "				order_onoffduty_details.deleted=0 AND "
	// + "				order_onoffduty_divide.deleted=0"
	// + "			) AS GrandOrderInfo "
	//
	// + "		INNER JOIN "
	// + "			(SELECT "
	// + "  			order_exec_cs.id AS order_exec_cs_id," // order_exec_cs_id
	// + "  			order_exec_cs.passenger,"
	// + "  			order_exec_cs.driver,"
	// + "  			order_exec_cs.from_,"
	// + "  			order_exec_cs.to_,"
	// + "  			order_exec_cs.remark,"
	// + "  			order_exec_cs.pre_time,"
	// + "  			order_exec_cs.cr_date,"
	// + "  			order_exec_cs.has_evaluation_passenger,"
	// + "  			order_exec_cs.has_evaluation_driver,"
	// + "  			order_exec_cs.status AS exec_status,"
	//
	// + "				order_onoffduty_exec_details.onoffduty_divide_id "
	//
	// + "			FROM order_exec_cs "
	//
	// + "			INNER JOIN order_onoffduty_exec_details "
	// + "			ON order_onoffduty_exec_details.order_cs_id=order_exec_cs.id "
	//
	// + "			WHERE "
	// + "				order_exec_cs.driver="
	// + driverid
	// + " AND "
	// + "				order_exec_cs.order_type=3 AND "
	// + "				order_exec_cs.deleted=0"
	//
	// + "			ORDER BY order_exec_cs.cr_date DESC "
	// + "			) AS OrderExecInfo "
	//
	// + "		ON GrandOrderInfo.divide_id=OrderExecInfo.onoffduty_divide_id"
	// + "		GROUP BY GrandOrderInfo.divide_id"
	// + "		) AS OrderInfo "
	//
	// + "  INNER JOIN user "
	// + "  ON user.id = OrderInfo.passenger) AS OrderWithUserInfo "
	//
	// + "LEFT JOIN  evaluation_cs "
	// + "ON "
	// + "		evaluation_cs.order_cs_id=OrderWithUserInfo.order_exec_cs_id AND "
	// + "		evaluation_cs.from_userid=OrderWithUserInfo.driver ";
	//
	// if (limit_time != null)
	// szQuery += "	WHERE OrderWithUserInfo.cr_date < \""
	// + ApiGlobal.Date2String(limit_time, true) + "\"";
	//
	// if (time_first)
	// szQuery +=
	// "	ORDER BY OrderWithUserInfo.cr_date DESC, FIELD(OrderWithUserInfo.exec_status, 2,7,6,5,4,3,1,8) LIMIT 0,"
	// + count;
	// else
	// szQuery +=
	// "	ORDER BY FIELD(OrderWithUserInfo.exec_status, 2,7,6,5,4,3,1,8), OrderWithUserInfo.cr_date DESC LIMIT 0,"
	// + count;
	//
	// rs = stmt.executeQuery(szQuery);
	// while (rs.next()) {
	// price = rs.getDouble("price");
	// if (active == 0)
	// sysfee_value = price * ratio / 100;
	// else
	// sysfee_value = Math.min(integer_, price);
	// sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
	// + ApiGlobal.Double2String(sysfee_value, 2)
	// + ConstMgr.STR_DIAN;
	//
	// has_eval = rs.getInt("has_evaluation_driver");
	// state = rs.getInt("exec_status");
	// switch (state) {
	// case 2:
	// state_desc = ConstMgr.STR_ORDERSTATE_YIYUYUE;
	// break;
	// case 3:
	// state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
	// break;
	// case 4:
	// state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
	// break;
	// case 5:
	// state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
	// break;
	// case 6:
	// state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
	// break;
	// case 7: {
	// if (has_eval == 1) {
	// state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
	// state = 8;
	// } else {
	// state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
	// }
	// }
	// break;
	// case 8:
	// state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
	// state = 9;
	// break;
	// default:
	// state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
	// }
	//
	// int evaluated = 0;
	// String eval_content = "";
	// String eval_desc = "";
	//
	// if (rs.getInt("has_evaluation_driver") == 0) // Not evaluated
	// // yet
	// {
	// evaluated = 0;
	// eval_content = "";
	// eval_desc = ConstMgr.STR_WEIPINGJIA;
	// } else {
	// evaluated = rs.getInt("level");
	// eval_content = rs.getString("msg");
	// if (eval_content == null)
	// eval_content = "";
	//
	// if (evaluated == 1)
	// eval_desc = ConstMgr.STR_HAOPING;
	// else if (evaluated == 2)
	// eval_desc = ConstMgr.STR_ZHONGPING;
	// else if (evaluated == 3)
	// eval_desc = ConstMgr.STR_CHAPING;
	// }
	//
	// JSONObject order_item = new JSONObject();
	//
	// order_item.put("uid", rs.getLong("mainID")); // ID of
	// // order_exec_cs
	// // Table.
	// order_item.put("order_num", rs.getString("order_num"));
	// order_item.put("type", 2);
	// order_item.put("type_desc", ConstMgr.STR_ONOFFDUTY);
	// order_item.put("pass_id", rs.getLong("passenger"));
	// order_item.put("pass_img",
	// ApiGlobal.getAbsoluteURL(rs.getString("img")));
	// order_item.put("pass_name", rs.getString("nickname"));
	// order_item.put("pass_gender", rs.getInt("sex"));
	// order_item.put("pass_age", rs.getInt("age"));
	// order_item.put("pass_count", 1);
	// order_item.put("price", rs.getDouble("price"));
	//
	// order_item.put("sysinfo_fee", sysfee_value);
	// order_item.put("sysinfo_fee_desc", sysfee_desc);
	//
	// order_item.put("start_city", "");
	// order_item.put("end_city", "");
	// order_item.put("start_addr", rs.getString("start_addr"));
	// order_item.put("end_addr", rs.getString("end_addr"));
	// order_item.put("start_time", ApiGlobal.Date2Time(ApiGlobal
	// .String2Date(rs.getString("pre_time"))));
	// order_item.put("evaluated", evaluated);
	// order_item.put("eval_content", eval_content);
	// order_item.put("evaluated_desc", eval_desc);
	// order_item.put("state", state);
	// order_item.put("state_desc", state_desc);
	// order_item.put("create_time", ApiGlobal.Date2String(
	// ApiGlobal.String2Date(rs.getString("cr_date")), true));
	//
	// arrOrders.add(order_item);
	// }
	//
	// rs.close();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// } finally {
	// // Close result set
	// if (rs != null) {
	// try {
	// rs.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// // Close normal statement
	// if (stmt != null) {
	// try {
	// stmt.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// // Close db connection
	// if (dbConn != null) {
	// try {
	// dbConn.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	// }
	//
	// return arrOrders;
	// }

	private JSONArray getLongOrdersForDriverAfterTime(long driverid,
			Date limit_time, String city_code, boolean time_first,
			boolean wait_oper) {
		JSONArray arrOrders = new JSONArray();

		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			String sysfee_desc = "";
			double sysfee_value = 0, price;
			double ratio = 0, integer_ = 0, active = 0;
			int state, eval_count = 0;
			String state_desc = "";

			SVCGlobalParams global_params = null;
			SVCCity city = null;

			szQuery = "SELECT * FROM global_params WHERE deleted=0";
			rs = stmt.executeQuery(szQuery);
			// Global parameters must exist. If not exist, it is abnormal
			rs.next();
			global_params = SVCGlobalParams.decodeFromResultSet(rs);
			rs.close();

			szQuery = "SELECT * FROM city WHERE code=\"" + city_code
					+ "\" AND deleted=0";
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				city = SVCCity.decodeFromResultSet(rs);
				rs.close();

				if (city.active < 0) // City parameters are not set. Use global
										// params
				{
					ratio = global_params.ratio;
					integer_ = global_params.integer_;
					active = global_params.active;
				} else // City parameters are set. Use city params
				{
					ratio = city.ratio;
					integer_ = city.integer_;
					active = city.active;
				}
			} else {
				ratio = global_params.ratio;
				integer_ = global_params.integer_;
				active = global_params.active;
			}

			szQuery = "SELECT"
					+ "		order_longdistance_details.*,"

					+ "		(SELECT COUNT(*) "
					+ "		FROM order_exec_cs "

					+ "		 INNER JOIN order_longdistance_users_cs "
					+ "		 ON order_longdistance_users_cs.order_exec_cs_id=order_exec_cs.id "

					// + "		 INNER JOIN order_longdistance_details"
					// +
					// "		 ON order_longdistance_details.id=order_longdistance_users_cs.orderdriverlongdistance_id "

					+ "		 WHERE "
					+ "			order_longdistance_users_cs.deleted=0 AND "
					+ "			order_longdistance_users_cs.orderdriverlongdistance_id=order_longdistance_details.id AND "
					+ "			order_exec_cs.deleted=0 AND "
					+ "			order_exec_cs.order_type=4 AND "
					+ "			order_exec_cs.driver="
					+ driverid
					+ " AND "
					+ "			order_exec_cs.has_evaluation_driver=0) AS eval_count, "

					+ "		(SELECT COUNT(*) "
					+ "		 FROM order_longdistance_users_cs " + "		 WHERE"
					+ "			order_longdistance_users_cs.deleted=0 "
					+ "		) AS pass_count "

					+ "FROM order_longdistance_details "

					+ "WHERE " + "		order_longdistance_details.deleted=0 AND "
					+ "		order_longdistance_details.publisher=" + driverid;

			if (limit_time != null)
				szQuery += "	AND order_longdistance_details.ps_time>\""
						+ ApiGlobal.Date2String(limit_time, true) + "\"";

			if (wait_oper) {
				szQuery += "	HAVING (status=2 OR status=3 OR status=4 OR status=5 OR (status=7 AND eval_count<>0))";
			} else {
				if (time_first)
					szQuery += "	ORDER BY order_longdistance_details.ps_time DESC, FIELD(order_longdistance_details.status, 2,7,6,5,4,3,1,8)";
				else
					szQuery += "	ORDER BY FIELD(order_longdistance_details.status, 2,7,6,5,4,3,1,8), order_longdistance_details.ps_time DESC";
			}

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				price = rs.getDouble("price");
				if (active == 0)
					sysfee_value = price * ratio / 100;
				else
					sysfee_value = Math.min(integer_, price);
				sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
						+ ApiGlobal.Double2String(sysfee_value, 2)
						+ ConstMgr.STR_DIAN;

				state = rs.getInt("status");
				switch (state) {
				case 1:
					state_desc = ConstMgr.STR_ORDERSTATE_FABU;
					break;
				case 2:
					state_desc = ConstMgr.STR_ORDERSTATE_YIYUYUE;
					break;
				case 3:
					state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
					break;
				case 4:
					state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
					break;
				case 5:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
					break;
				case 6:
					state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
					break;
				case 7: {
					eval_count = rs.getInt("eval_count");

					if (eval_count == 0) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
						state = 8;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
					}
				}
					break;
				case 8:
					state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
					state = 9;
					break;
				default:
					state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
				}

				JSONObject order_item = new JSONObject();

				order_item.put("uid", rs.getLong("id"));
				order_item.put("order_num", rs.getString("order_num"));
				order_item.put("type", 3);
				order_item.put("type_desc", ConstMgr.STR_LONGDISTANCE);
				order_item.put("pass_id", 0);
				order_item.put("pass_img", "");
				order_item.put("pass_name", "");
				order_item.put("pass_gender", 0);
				order_item.put("pass_age", 0);
				order_item.put("pass_count", rs.getInt("pass_count"));
				order_item.put("price", rs.getDouble("price"));

				order_item.put("sysinfo_fee", sysfee_value);
				order_item.put("sysinfo_fee_desc", sysfee_desc);

				order_item.put(
						"start_city",
						ApiGlobal.cityCode2Name(dbConn,
								rs.getString("start_city")));
				order_item.put(
						"end_city",
						ApiGlobal.cityCode2Name(dbConn,
								rs.getString("end_city")));
				order_item.put("start_addr", rs.getString("start_addr"));
				order_item.put("end_addr", rs.getString("end_addr"));
				order_item.put("start_time", ApiGlobal
						.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
								.getString("pre_time"))));
				order_item.put("evaluated", 0);
				order_item.put("eval_content", "");
				order_item.put("evaluated_desc", "");
				order_item.put("state", state);
				order_item.put("state_desc", state_desc);
				order_item.put("create_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_time")), true));

				arrOrders.add(order_item);
			}

			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
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

		return arrOrders;
	}

	private JSONArray getLongOrdersForDriverBeforeTime(long driverid,
			Date limit_time, int count, String city_code, boolean time_first,
			boolean wait_oper) {
		JSONArray arrOrders = new JSONArray();

		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			String sysfee_desc = "";
			double sysfee_value = 0, price;
			double ratio = 0, integer_ = 0, active = 0;
			double insu_fee = 0;// 保险费 modify by czq

			int state, eval_count = 0;
			String state_desc = "";

			SVCGlobalParams global_params = null;
			SVCCity city = null;

			szQuery = "SELECT * FROM global_params WHERE deleted=0";
			rs = stmt.executeQuery(szQuery);
			// Global parameters must exist. If not exist, it is abnormal
			rs.next();
			global_params = SVCGlobalParams.decodeFromResultSet(rs);
			rs.close();
			
			insu_fee= global_params.insu_fee;//保险费直接使用全局的参数

			szQuery = "SELECT * FROM city WHERE code=\"" + city_code
					+ "\" AND deleted=0";
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				city = SVCCity.decodeFromResultSet(rs);
				rs.close();

				if (city.active < 0) // City parameters are not set. Use global
										// params
				{
					ratio = global_params.ratio;
					integer_ = global_params.integer_;
					active = global_params.active;
					//insu_fee = global_params.insu_fee;//保险费
				} else // City parameters are set. Use city params
				{
					ratio = city.ratio;
					integer_ = city.integer_;
					active = city.active;
					//insu_fee = city.insu_fee;//保险费
				}
			} else {
				ratio = global_params.ratio;
				integer_ = global_params.integer_;
				active = global_params.active;
				//insu_fee = global_params.insu_fee;//保险费 
			}

			szQuery = "SELECT"
					+ "		order_longdistance_details.*,"

					+ "		(SELECT COUNT(*) "
					+ "		FROM order_exec_cs "

					+ "		 INNER JOIN order_longdistance_users_cs "
					+ "		 ON order_longdistance_users_cs.order_exec_cs_id=order_exec_cs.id "

					// + "		 INNER JOIN order_longdistance_details"
					// +
					// "		 ON order_longdistance_details.id=order_longdistance_users_cs.orderdriverlongdistance_id "

					+ "		 WHERE "
					+ "			order_longdistance_users_cs.deleted=0 AND "
					+ "			order_longdistance_users_cs.orderdriverlongdistance_id=order_longdistance_details.id AND "
					+ "			order_exec_cs.deleted=0 AND "
					+ "			order_exec_cs.order_type=4 AND "
					+ "			order_exec_cs.driver="
					+ driverid
					+ " AND "
					+ "			order_exec_cs.has_evaluation_driver=0) AS eval_count, "

					+ "		(SELECT COUNT(*)"
					+ "		 FROM order_longdistance_users_cs "
					+ "		 WHERE"
					+ "			order_longdistance_users_cs.deleted=0 AND "
					+ "			order_longdistance_details.id=order_longdistance_users_cs.orderdriverlongdistance_id) AS pass_count ,"
					+ "     order_longdistance_users_cs.order_exec_cs_id , "
					+ "     order_exec_cs.insu_id_driver , "//执行订单表中车主保单id
					+ "     insurance.*  "

					+ "FROM order_longdistance_details "
					+ "		LEFT JOIN order_longdistance_users_cs "
					+ "			ON order_longdistance_details.id = order_longdistance_users_cs.orderdriverlongdistance_id "
					+ "		LEFT JOIN order_exec_cs "
					+ "			ON order_longdistance_users_cs.order_exec_cs_id = order_exec_cs.id "
					+ "		LEFT JOIN insurance "// modify by chuzhiqiang 保险表
					+ "			ON order_exec_cs.insu_id_driver = insurance.id "

					+ "WHERE" + "		order_longdistance_details.deleted=0 AND "
					
					+ "		order_longdistance_details.deleted=0 AND "
					+ "		order_longdistance_details.publisher=" + driverid;

			if (limit_time != null)
				szQuery += "	AND order_longdistance_details.ps_time<\""
						+ ApiGlobal.Date2String(limit_time, true) + "\"";

			if (wait_oper) {
				szQuery += "	HAVING (status=2 OR status=3 OR status=4 OR status=5 OR (status=7 AND eval_count<>0))";
			} else {
				if (time_first)
					szQuery += "	ORDER BY order_longdistance_details.ps_time DESC, FIELD(order_longdistance_details.status, 2,7,6,5,4,3,1,8) LIMIT 0,"
							+ count;
				else
					szQuery += "	ORDER BY FIELD(order_longdistance_details.status, 2,7,6,5,4,3,1,8), order_longdistance_details.ps_time DESC LIMIT 0,"
							+ count;
			}

	System.out
					.println("SVCOrderService.getLongOrdersForDriverBeforeTime()---->"
							+ szQuery);
			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				price = rs.getDouble("price");
				if (active == 0)
					sysfee_value = price * ratio / 100;
				else
					sysfee_value = Math.min(integer_, price);
				sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
						+ ApiGlobal.Double2String(sysfee_value, 2)
						+ ConstMgr.STR_DIAN;

				state = rs.getInt("status");
				switch (state) {
				case 1:
					state_desc = ConstMgr.STR_ORDERSTATE_FABU;
					break;
				case 2:
					state_desc = ConstMgr.STR_ORDERSTATE_YIYUYUE;
					break;
				case 3:
					state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
					break;
				case 4:
					state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
					break;
				case 5:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
					break;
				case 6:
					state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
					break;
				case 7: {
					eval_count = rs.getInt("eval_count");

					if (eval_count == 0) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
						state = 8;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
					}
				}
					break;
				case 8:
					state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
					state = 9;
					break;
				default:
					state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
				}

				JSONObject order_item = new JSONObject();

				order_item.put("uid", rs.getLong("id"));
				order_item.put("order_num", rs.getString("order_num"));
				order_item.put("type", 3);
				order_item.put("type_desc", ConstMgr.STR_LONGDISTANCE);
				order_item.put("pass_id", 0);
				order_item.put("pass_img", "");
				order_item.put("pass_name", "");
				order_item.put("pass_gender", 0);
				order_item.put("pass_age", 0);
				order_item.put("pass_count", rs.getInt("pass_count"));
				order_item.put("price", rs.getDouble("price"));

				order_item.put("sysinfo_fee", sysfee_value);
				order_item.put("sysinfo_fee_desc", sysfee_desc);

				order_item.put(
						"start_city",
						ApiGlobal.cityCode2Name(dbConn,
								rs.getString("start_city")));
				order_item.put(
						"end_city",
						ApiGlobal.cityCode2Name(dbConn,
								rs.getString("end_city")));
				order_item.put("start_addr", rs.getString("start_addr"));
				order_item.put("end_addr", rs.getString("end_addr"));
				order_item.put("start_time", ApiGlobal
						.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
								.getString("pre_time"))));
				order_item.put("evaluated", 0);
				order_item.put("eval_content", "");
				order_item.put("evaluated_desc", "");
				order_item.put("state", state);
				order_item.put("state_desc", state_desc);
				order_item.put("create_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_time")), true));
				
				
				
				order_item.put("insu_fee", insu_fee*2);//保险  一个执行订单对应两个保单   modify by czq 
				order_item.put("insu_id", rs.getLong("insu_id_driver"));//保单id  modify by czq 
				order_item.put("appl_no", rs.getString("appl_no"));//保单号  modify by czq 
				order_item.put("total_amount", rs.getDouble("total_amount"));//保单金额  modify by czq 
				order_item.put("isd_id", rs.getLong("isd_id"));//被保险人  modify by czq 
				order_item.put("isd_name", rs.getString("isd_name"));//被保险人姓名  modify by czq 
				order_item.put("insu_status", rs.getInt("insu_status"));//保单状态  modify by czq 
				order_item.put("effect_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("effect_time")),
						true));// 保单生效期 modify by czq
				order_item.put("insexpr_date", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("insexpr_date")),
						true));// 保单生效期 modify by czq

				arrOrders.add(order_item);
			}

			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
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

		return arrOrders;
	}

	/**
	 * 1,获得"限制订单"对应的日期时间 2,以上一步求出的“限制日期”来按order_type的三种情况查询订单
	 * 
 * @param source
 * @param userid
 * @param order_type
 * @param order_num
 * @param limitid_type
 * @param devtoken
 * @return
 */
	public SVCResult latestDriverOrders(String source, long userid,
			int order_type, String order_num, int limitid_type, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + order_type + "," + order_num + "," + limitid_type + "," + devtoken);

		if (source.equals("") || userid < 0 || order_type < 0
				|| limitid_type < 0 || devtoken.equals("")) {
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
			JSONArray arrOrders = new JSONArray();

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
			if (userinfo.driver_verified != 1) // Not verified driver
												// info. No order.
			{
				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
				result.retdata = arrOrders;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			SVCOrderTempDetails limit_order_temp = null;
			// SVCOrderOnOffDutyDetails limit_order_onoff = null;
			SVCOrderLongDistanceDetails limit_order_longdist = null;

			Date limit_date = null;

			stmt = dbConn.createStatement();
			// 1,获得"限制订单"对应的日期时间
			if (limitid_type == 1) // Temporary order
			{
				szQuery = "SELECT * FROM order_temp_details WHERE deleted=0 AND order_num=\""
						+ order_num + "\"";
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					limit_order_temp = SVCOrderTempDetails
							.decodeFromResultSet(rs);
					limit_date = limit_order_temp.ps_date;
					rs.close();
				}
				// } else if (limitid_type == 2) // On off duty order
				// {
				// szQuery =
				// "SELECT * FROM order_onoffduty_details WHERE deleted=0 AND order_num=\""
				// + order_num + "\"";
				// rs = stmt.executeQuery(szQuery);
				// if (rs.next()) {
				// limit_order_onoff = SVCOrderOnOffDutyDetails
				// .decodeFromResultSet(rs);
				// limit_date = limit_order_onoff.publish_date;
				// rs.close();
				// }
			} else if (limitid_type == 3) // Long distance order
			{
				szQuery = "SELECT * FROM order_longdistance_details WHERE deleted=0 AND order_num=\""
						+ order_num + "\"";
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					limit_order_longdist = SVCOrderLongDistanceDetails
							.decodeFromResultSet(rs);
					limit_date = limit_order_longdist.ps_time;
					rs.close();
				}
			}
			// 2,以上一步求出的“限制日期”来按order_type的三种情况查询订单
			if (order_type == 1) // Requires all
			{
				JSONArray arrOnceOrders = getOnceOrdersForDriverAfterTime(
						userid, limit_date, userinfo.city_cur, true, false);
				// JSONArray arrOnOffOrders = getOnOffOrdersForDriverAfterTime(
				// userid, limit_date, userinfo.city_cur, true);
				JSONArray arrLongOrders = getLongOrdersForDriverAfterTime(
						userid, limit_date, userinfo.city_cur, true, false);

				arrOrders.addAll(arrOnceOrders);
				// arrOrders.addAll(arrOnOffOrders);
				arrOrders.addAll(arrLongOrders);

				ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
				for (int i = 0; i < arrOrders.size(); i++) {
					listOrders.add(arrOrders.getJSONObject(i));
				}

				Collections.sort(listOrders,
						new OrderObjectTimeForDriverComparator());

				arrOrders.clear();
				for (int i = 0; i < listOrders.size(); i++) {
					arrOrders.add(listOrders.get(i));
				}
			} else if (order_type == 2) // Waiting operation
			{
				JSONArray arrOnceOrders = getOnceOrdersForDriverAfterTime(
						userid, limit_date, userinfo.city_cur, false, true);
				// JSONArray arrOnOffOrders = getOnOffOrdersForDriverAfterTime(
				// userid, limit_date, userinfo.city_cur, false);
				JSONArray arrLongOrders = getLongOrdersForDriverAfterTime(
						userid, limit_date, userinfo.city_cur, false, true);

				arrOrders.addAll(arrOnceOrders);
				// arrOrders.addAll(arrOnOffOrders);
				arrOrders.addAll(arrLongOrders);

				ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
				for (int i = 0; i < arrOrders.size(); i++) {
					listOrders.add(arrOrders.getJSONObject(i));
				}

				Collections.sort(listOrders,
						new OrderObjectTimeForDriverComparator());

				arrOrders.clear();
				for (int i = 0; i < listOrders.size(); i++) {
					arrOrders.add(listOrders.get(i));
				}
			} else if (order_type == 3) // Once carpool
			{
				arrOrders = getOnceOrdersForDriverAfterTime(userid, limit_date,
						userinfo.city_cur, true, false);

				ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
				for (int i = 0; i < arrOrders.size(); i++) {
					listOrders.add(arrOrders.getJSONObject(i));
				}

				Collections.sort(listOrders,
						new OrderObjectTimeForDriverComparator());

				arrOrders.clear();
				for (int i = 0; i < listOrders.size(); i++) {
					arrOrders.add(listOrders.get(i));
				}
				// } else if (order_type == 4) // Onoff carpool
				// {
				// arrOrders = getOnOffOrdersForDriverAfterTime(userid,
				// limit_date, userinfo.city_cur, true);
			} else if (order_type == 5) // Long distance carpool
			{
				arrOrders = getLongOrdersForDriverAfterTime(userid, limit_date,
						userinfo.city_cur, true, false);

				ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
				for (int i = 0; i < arrOrders.size(); i++) {
					listOrders.add(arrOrders.getJSONObject(i));
				}

				Collections.sort(listOrders,
						new OrderObjectTimeForDriverComparator());

				arrOrders.clear();
				for (int i = 0; i < listOrders.size(); i++) {
					arrOrders.add(listOrders.get(i));
				}
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = arrOrders;

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

	public SVCResult pagedDriverOrders(String source, long userid,
			int order_type, String limit_time, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + order_type + "," + limit_time + "," + devtoken);

		if (source.equals("") || userid < 0 || order_type < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;

			try {
				JSONArray arrOrders = new JSONArray();
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (userinfo.driver_verified != 1) // Not verified driver
															// information. No
															// orders
				{
					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
				} else {

					Date limit_date = ApiGlobal.String2Date(limit_time);

					stmt = dbConn.createStatement();

					if (order_type == 1) // Requires all
					{
						JSONArray arrOnceOrders = getOnceOrdersForDriverBeforeTime(
								userid, limit_date,
								ApiGlobal.getPageItemCount(),
								userinfo.city_cur, true, false);
						// JSONArray arrOnOffOrders =
						// getOnOffOrdersForDriverBeforeTime(
						// userid, limit_date,
						// ApiGlobal.getPageItemCount(),
						// userinfo.city_cur, true);
						JSONArray arrLongOrders = getLongOrdersForDriverBeforeTime(
								userid, limit_date,
								ApiGlobal.getPageItemCount(),
								userinfo.city_cur, true, false);

						arrOrders.addAll(arrOnceOrders);
						// arrOrders.addAll(arrOnOffOrders);
						arrOrders.addAll(arrLongOrders);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForDriverComparator());

						arrOrders.clear();
						for (int i = 0; i < Math.min(listOrders.size(),
								ApiGlobal.getPageItemCount()); i++) {
							arrOrders.add(listOrders.get(i));
						}
					} else if (order_type == 2) // Waiting operation
					{
						JSONArray arrOnceOrders = getOnceOrdersForDriverBeforeTime(
								userid, limit_date,
								ApiGlobal.getPageItemCount(),
								userinfo.city_cur, false, true);
						// JSONArray arrOnOffOrders =
						// getOnOffOrdersForDriverBeforeTime(
						// userid, limit_date,
						// ApiGlobal.getPageItemCount(),
						// userinfo.city_cur, false);
						JSONArray arrLongOrders = getLongOrdersForDriverBeforeTime(
								userid, limit_date,
								ApiGlobal.getPageItemCount(),
								userinfo.city_cur, false, true);

						arrOrders.addAll(arrOnceOrders);
						// arrOrders.addAll(arrOnOffOrders);
						arrOrders.addAll(arrLongOrders);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForDriverComparator());

						arrOrders.clear();
						for (int i = 0; i < Math.min(listOrders.size(),
								ApiGlobal.getPageItemCount()); i++) {
							arrOrders.add(listOrders.get(i));
						}
					} else if (order_type == 3) // Once carpool
					{
						arrOrders = getOnceOrdersForDriverBeforeTime(userid,
								limit_date, ApiGlobal.getPageItemCount(),
								userinfo.city_cur, true, false);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForDriverComparator());

						arrOrders.clear();
						for (int i = 0; i < listOrders.size(); i++) {
							arrOrders.add(listOrders.get(i));
						}
						// } else if (order_type == 4) // Onoff carpool
						// {
						// arrOrders = getOnOffOrdersForDriverBeforeTime(userid,
						// limit_date, ApiGlobal.getPageItemCount(),
						// userinfo.city_cur, true);
					} else if (order_type == 5) // Long distance carpool
					{
						arrOrders = getLongOrdersForDriverBeforeTime(userid,
								limit_date, ApiGlobal.getPageItemCount(),
								userinfo.city_cur, true, false);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForDriverComparator());

						arrOrders.clear();
						for (int i = 0; i < listOrders.size(); i++) {
							arrOrders.add(listOrders.get(i));
						}
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public JSONArray getOnceOrdersForPassengerAfterTime(long passid,
			Date limit_time, boolean time_first, boolean wait_oper) {
		Connection dbConn = null;
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
		String szQuery = "", szQuery2 = "";

		JSONArray arrOrders = new JSONArray();

		try {
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();
			stmt2 = dbConn.createStatement();

			int state, has_eval = 0;
			String state_desc = "";

			szQuery = "SELECT "
					+ "		OrderWithUserInfo.*,"
					+ "		evaluation_cs.level,"
					+ "		evaluation_cs.msg "

					+ "FROM "
					+ "		(SELECT "
					+ "			OrderInfo.*,"

					+ "			user.id AS driver_id,"
					+ "			user.img,"
					+ "			user.nickname,"
					+ "			user.sex,"
					+ "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "

					+ "		FROM "
					+ "			(SELECT "
					+ "				order_temp_details.id,"
					+ "				order_temp_details.order_num,"
					+ "				order_temp_details.ps_date,"
					+ "				order_temp_details.start_addr,"
					+ "				order_temp_details.end_addr,"
					+ "				order_temp_details.pre_time,"
					+ "				order_temp_details.price,"
					+ "				order_temp_details.order_cs_id,"
					+ "				order_temp_details.publisher,"
					+ "				order_temp_details.status,"

					+ "				order_exec_cs.id AS exec_id,"
					+ "				order_exec_cs.passenger,"
					+ "				order_exec_cs.driver,"
					+ "				order_exec_cs.cr_date,"
					+ "				order_exec_cs.has_evaluation_passenger,"
					+ "				order_exec_cs.has_evaluation_driver,"
					+ "				order_exec_cs.pass_cancel_time,"
					+ "				order_exec_cs.status AS exec_status "

					+ "			FROM order_temp_details "

					+ "			 LEFT JOIN  order_exec_cs "
					+ "			ON order_temp_details.order_cs_id=order_exec_cs.id "

					+ "			WHERE "
					+ "				order_temp_details.deleted=0 AND "
					+ "				order_temp_details.publisher="
					+ passid
					+ ") AS OrderInfo "

					+ "		 LEFT JOIN  user "
					+ "		ON user.id=OrderInfo.driver) AS OrderWithUserInfo "

					+ " LEFT JOIN  evaluation_cs "
					+ "ON "
					+ "		evaluation_cs.order_cs_id=OrderWithUserInfo.exec_id AND "
					+ "		evaluation_cs.from_userid=OrderWithUserInfo.passenger ";

			if (wait_oper) {
				szQuery += "WHERE (OrderWithUserInfo.status=6 OR (OrderWithUserInfo.status=7 AND evaluation_cs.level IS NULL)) ";
				if (limit_time != null)
					szQuery += "	AND OrderWithUserInfo.ps_date>\""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";
			} else {
				if (limit_time != null)
					szQuery += "	WHERE OrderWithUserInfo.ps_date>\""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";

				if (time_first)
					szQuery += "	ORDER BY OrderWithUserInfo.ps_date DESC, FIELD(OrderWithUserInfo.status, 1,6,5,4,3,2,7,8)";
				else
					szQuery += "	ORDER BY FIELD(OrderWithUserInfo.status, 1,6,5,4,3,2,7,8), OrderWithUserInfo.ps_date DESC";
			}

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCUser tmpDrvInfo = null;

				has_eval = rs.getInt("has_evaluation_passenger");
				state = rs.getInt("status");

				switch (state) {
				case 1: {
					long orderid = rs.getLong("id");
					szQuery2 = "SELECT " + "		user.* " + "FROM "
							+ "		order_temp_grab " + "INNER JOIN user "
							+ "ON user.id=order_temp_grab.driverid " + "WHERE "
							+ "		order_temp_grab.deleted=0 AND "
							+ "		order_temp_grab.order_id=" + orderid + " AND "
							+ "		status=0";
					rs2 = stmt2.executeQuery(szQuery2);
					if (rs2.next()) {
						tmpDrvInfo = SVCUser.decodeFromResultSet(rs2);
						state = 0;
						state_desc = ConstMgr.STR_ORDERSTATE_BUTONGYI;
					} else {
						rs2.close();
						continue;
					}
					rs2.close();
					break;
				}
				case 2:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
					break;
				case 3:
					state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
					break;
				case 4:
					state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
					break;
				case 5:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
					break;
				case 6:
					state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
					break;
				case 7: {
					if (has_eval == 1) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
						state = 8;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
					}
				}
					break;
				case 8:
					state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
					if (rs.getDate("pass_cancel_time") == null)
						state = 9;
					else
						state = 10;
					break;
				default:
					state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
				}

				int evaluated = 0;
				String eval_content = "";
				String eval_desc = "";

				if (rs.getInt("has_evaluation_passenger") == 0) // Not evaluated
																// yet
				{
					evaluated = 0;
					eval_content = "";
					eval_desc = ConstMgr.STR_WEIPINGJIA;
				} else {
					evaluated = rs.getInt("level");
					eval_content = rs.getString("msg");
					if (eval_content == null)
						eval_content = "";

					if (evaluated == 1)
						eval_desc = ConstMgr.STR_HAOPING;
					else if (evaluated == 2)
						eval_desc = ConstMgr.STR_ZHONGPING;
					else if (evaluated == 3)
						eval_desc = ConstMgr.STR_CHAPING;
				}

				JSONObject order_item = new JSONObject();

				order_item.put("uid", rs.getLong("id"));
				order_item.put("order_num", rs.getString("order_num"));
				order_item.put("type", 1);
				order_item.put("type_desc", ConstMgr.STR_ONCE);

				if (rs.getLong("driver") == 0 && tmpDrvInfo != null) {
					order_item.put("driver_id", tmpDrvInfo.id);
					order_item.put("driver_img",
							ApiGlobal.getAbsoluteURL(tmpDrvInfo.img));
					order_item.put("driver_name", tmpDrvInfo.nickname);
					order_item.put("driver_gender", tmpDrvInfo.sex);
					order_item.put("driver_age", ApiGlobal.getDiffYear(
							tmpDrvInfo.birthday, new Date()));
				} else {
					order_item.put("driver_id", rs.getLong("driver"));
					order_item.put("driver_img",
							ApiGlobal.getAbsoluteURL(rs.getString("img")));
					order_item.put("driver_name", rs.getString("nickname"));
					order_item.put("driver_gender", rs.getInt("sex"));
					order_item.put("driver_age", rs.getInt("age"));
				}

				order_item.put("price", rs.getDouble("price"));

				order_item.put("start_city", "");
				order_item.put("end_city", "");
				order_item.put("start_addr", rs.getString("start_addr"));
				order_item.put("end_addr", rs.getString("end_addr"));
				order_item.put("start_time", ApiGlobal
						.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
								.getString("pre_time"))));
				order_item.put("evaluated", evaluated);
				order_item.put("eval_content", eval_content);
				order_item.put("evaluated_desc", eval_desc);
				order_item.put("state", state);
				order_item.put("state_desc", state_desc);
				order_item.put("create_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_date")), true));

				arrOrders.add(order_item);
			}

			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			if (rs2 != null) {
				try {
					rs2.close();
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

			if (stmt2 != null) {
				try {
					stmt2.close();
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

		return arrOrders;
	}

	private JSONArray getOnceOrdersForPassengerBeforeTime(long passid,
			Date limit_time, int count, boolean time_first, boolean wait_oper) {
		Connection dbConn = null;
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
		String szQuery = "";

		JSONArray arrOrders = new JSONArray();

		try {
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();
			stmt2 = dbConn.createStatement();

			int state, has_eval = 0;
			String state_desc = "";

			szQuery = "SELECT "
					+ "		OrderWithUserInfo.*,"
					+ "		eval_info.level,"
					+ "		  insu.* , "// 保险的详细信息
					+ "		eval_info.msg "

					+ "FROM "
					+ "		(SELECT "
					+ "			OrderInfo.*,"

					+ "			user.id AS driver_id,"
					+ "			user.img,"
					+ "			user.nickname,"
					+ "			user.sex,"
					+ "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "

					+ "		FROM "
					+ "			(SELECT "
					+ "				order_temp_details.id,"
					+ "				order_temp_details.order_num,"
					+ "				order_temp_details.ps_date,"
					+ "				order_temp_details.start_addr,"
					+ "				order_temp_details.end_addr,"
					+ "				order_temp_details.pre_time,"
					+ "				order_temp_details.price,"
					+ "				order_temp_details.order_cs_id,"
					+ "				order_temp_details.publisher,"
					+ "				order_temp_details.status,"

					+ "				order_exec_cs.id AS exec_id,"
					+ "				order_exec_cs.passenger,"
					+ "				order_exec_cs.driver,"
					+ "				order_exec_cs.cr_date,"
					+ "				order_exec_cs.pass_cancel_time,"
					+ "				order_exec_cs.has_evaluation_passenger,"
					+ "				order_exec_cs.has_evaluation_driver,"
					+ "				order_exec_cs.status AS exec_status ,"
					+ "				order_exec_cs.insu_id_pass "//执行订单表中的乘客保险id

					+ "			FROM order_temp_details "

					+ "			 LEFT JOIN  order_exec_cs "
					+ "			ON order_temp_details.order_cs_id=order_exec_cs.id "

					+ "			WHERE "
					+ "				order_temp_details.deleted=0 AND "
					+ "				order_temp_details.publisher="
					+ passid
					+ ") AS OrderInfo "

					+ "		 LEFT JOIN  user "
					+ "		ON user.id=OrderInfo.driver) AS OrderWithUserInfo "

					+ " LEFT JOIN  (SELECT * FROM evaluation_cs WHERE deleted=0 ORDER BY ps_date DESC) AS eval_info "
					+ "ON "
					+ "		eval_info.from_userid=OrderWithUserInfo.passenger AND "
					+ "		eval_info.to_userid=OrderWithUserInfo.driver AND "
					+ "		eval_info.order_cs_id=OrderWithUserInfo.exec_id "
					+ "		LEFT JOIN  insurance insu ON OrderWithUserInfo.insu_id_pass = insu.id ";//联结insurance表

			if (wait_oper) {
				szQuery += "WHERE (OrderWithUserInfo.status=6 OR (OrderWithUserInfo.status=7 AND eval_info.level IS NULL)) ";
				if (limit_time != null)
					szQuery += "	AND OrderWithUserInfo.ps_date<\""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";
			} else {
				if (limit_time != null)
					szQuery += "	WHERE OrderWithUserInfo.ps_date<\""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";

				if (time_first)
					szQuery += "	ORDER BY OrderWithUserInfo.ps_date DESC, FIELD(OrderWithUserInfo.status, 1,6,5,4,3,2,7,8) LIMIT 0,"
							+ count;
				else
					szQuery += "	ORDER BY FIELD(OrderWithUserInfo.status, 1,6,5,4,3,2,7,8), OrderWithUserInfo.ps_date DESC LIMIT 0,"
							+ count;
			}

			System.out.println("sql--->" + szQuery);
			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCUser tmpDrvInfo = null;

				has_eval = rs.getInt("has_evaluation_passenger");
				state = rs.getInt("status");
				switch (state) {
				case 1: {
					long orderid = rs.getLong("id");
					szQuery = "SELECT " + "		user.* " + "FROM "
							+ "		order_temp_grab " + "INNER JOIN user "
							+ "ON user.id=order_temp_grab.driverid " + "WHERE "
							+ "		order_temp_grab.deleted=0 AND "
							+ "		order_temp_grab.order_id=" + orderid + " AND "
							+ "		status=0";
					rs2 = stmt2.executeQuery(szQuery);
					if (rs2.next()) {
						tmpDrvInfo = SVCUser.decodeFromResultSet(rs2);
						state = 0;
						state_desc = ConstMgr.STR_ORDERSTATE_BUTONGYI;
					} else {
						rs2.close();
						continue;
					}
					rs2.close();
					break;
				}
				case 2:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
					break;
				case 3:
					state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
					break;
				case 4:
					state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
					break;
				case 5:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
					break;
				case 6:
					state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
					break;
				case 7: {
					if (has_eval == 1) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
						state = 8;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
					}
				}
					break;
				case 8:
					state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
					if (rs.getDate("pass_cancel_time") == null)
						state = 9;
					else
						state = 10;
					break;
				default:
					state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
				}

				int evaluated = 0;
				String eval_content = "";
				String eval_desc = "";

				if (rs.getInt("has_evaluation_passenger") == 0) // Not evaluated
																// yet
				{
					evaluated = 0;
					eval_content = "";
					eval_desc = ConstMgr.STR_WEIPINGJIA;
				} else {
					evaluated = rs.getInt("level");
					eval_content = rs.getString("msg");
					if (eval_content == null)
						eval_content = "";

					if (evaluated == 1)
						eval_desc = ConstMgr.STR_HAOPING;
					else if (evaluated == 2)
						eval_desc = ConstMgr.STR_ZHONGPING;
					else if (evaluated == 3)
						eval_desc = ConstMgr.STR_CHAPING;
				}

				JSONObject order_item = new JSONObject();

				order_item.put("uid", rs.getLong("id"));
				order_item.put("order_num", rs.getString("order_num"));
				order_item.put("type", 1);
				order_item.put("type_desc", ConstMgr.STR_ONCE);

				if (rs.getLong("driver") == 0 && tmpDrvInfo != null) {
					order_item.put("driver_id", tmpDrvInfo.id);
					order_item.put("driver_img",
							ApiGlobal.getAbsoluteURL(tmpDrvInfo.img));
					order_item.put("driver_name", tmpDrvInfo.nickname);
					order_item.put("driver_gender", tmpDrvInfo.sex);
					order_item.put("driver_age", ApiGlobal.getDiffYear(
							tmpDrvInfo.birthday, new Date()));
				} else {
					order_item.put("driver_id", rs.getLong("driver"));
					order_item.put("driver_img",
							ApiGlobal.getAbsoluteURL(rs.getString("img")));
					order_item.put("driver_name", rs.getString("nickname"));
					order_item.put("driver_gender", rs.getInt("sex"));
					order_item.put("driver_age", rs.getInt("age"));
				}

				order_item.put("price", rs.getDouble("price"));

				order_item.put("start_city", "");
				order_item.put("end_city", "");
				order_item.put("start_addr", rs.getString("start_addr"));
				order_item.put("end_addr", rs.getString("end_addr"));
				order_item.put("start_time", ApiGlobal
						.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
								.getString("pre_time"))));
				order_item.put("evaluated", evaluated);
				order_item.put("eval_content", eval_content);
				order_item.put("evaluated_desc", eval_desc);
				order_item.put("state", state);
				order_item.put("state_desc", state_desc);
				order_item.put("create_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_date")), true));

				//order_item.put("insu_fee", insu_fee);//保险  modify by czq 
				order_item.put("insu_id", rs.getLong("insu_id_pass"));//保单id  modify by czq 
				order_item.put("appl_no", rs.getString("appl_no"));//保单号  modify by czq 
				order_item.put("total_amount", rs.getDouble("total_amount"));//保单金额  modify by czq 
				order_item.put("isd_id", rs.getLong("isd_id"));//被保险人  modify by czq 
				order_item.put("isd_name", rs.getString("isd_name"));//被保险人姓名  modify by czq 
				order_item.put("insu_status", rs.getInt("insu_status"));//保单状态  modify by czq 
				order_item.put("effect_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("effect_time")),
						true));// 保单生效期 modify by czq
				order_item.put("insexpr_date", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("insexpr_date")),
						true));// 保单生效期 modify by czq

				arrOrders.add(order_item);
			}

			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			if (rs2 != null) {
				try {
					rs2.close();
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

			if (stmt2 != null) {
				try {
					stmt2.close();
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

		return arrOrders;
	}

	// private JSONArray getOnOffOrdersForPassengerAfterTime(long passid,
	// Date limit_time, boolean time_first) {
	// Connection dbConn = null;
	// Statement stmt = null, stmt2 = null;
	// ResultSet rs = null, rs2 = null;
	// String szQuery = "";
	//
	// JSONArray arrOrders = new JSONArray();
	//
	// try {
	// dbConn = DBManager.getDBConnection();
	// stmt = dbConn.createStatement();
	// stmt2 = dbConn.createStatement();
	//
	// int state, has_eval = 0;
	// int exec_status = 0;
	// String state_desc = "";
	//
	// szQuery = "SELECT "
	// + "		OrderWithUserInfo.*,"
	// + "		evaluation_cs.level,"
	// + "		evaluation_cs.msg "
	//
	// + "FROM "
	// + "		(SELECT "
	// + "			OrderInfo.*,"
	//
	// + "			user.id AS driver_id,"
	// + "			user.img,"
	// + "			user.nickname,"
	// + "			user.sex,"
	// + "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "
	//
	// + "		FROM "
	// + "			(SELECT "
	// + "				OrderWithoutExecInfo.*,"
	// + "				OrderExecInfo.* "
	//
	// + "			FROM "
	// + "				(SELECT "
	// + "					order_onoffduty_details.id,"
	// + "					order_onoffduty_details.order_num,"
	// + "					order_onoffduty_details.publish_date,"
	// + "					order_onoffduty_details.price,"
	// + "					order_onoffduty_details.publisher,"
	// + "					order_onoffduty_details.start_addr,"
	// + "					order_onoffduty_details.end_addr,"
	// + "					order_onoffduty_details.pre_time,"
	// + "					order_onoffduty_details.status "
	//
	// + "				FROM order_onoffduty_details "
	//
	// + "				WHERE "
	// + "					order_onoffduty_details.deleted=0 AND "
	// + "					order_onoffduty_details.publisher=" + passid
	// + "				) AS OrderWithoutExecInfo "
	//
	// + "			LEFT JOIN "
	// + "				(SELECT "
	// + "					order_exec_cs.id AS exec_id,"
	// + "					order_exec_cs.passenger,"
	// + "					order_exec_cs.driver,"
	// + "					order_exec_cs.cr_date,"
	// + "					order_exec_cs.has_evaluation_passenger,"
	// + "					order_exec_cs.has_evaluation_driver,"
	// + "					order_exec_cs.status AS exec_status, "
	//
	// + "					order_onoffduty_divide.which_days,"
	// + "					order_onoffduty_divide.orderdetails_id,"
	// + "					order_onoffduty_divide.id AS divide_id "
	//
	// + "				FROM order_exec_cs "
	//
	// + "				INNER JOIN order_onoffduty_exec_details "
	// + "				ON order_onoffduty_exec_details.order_cs_id=order_exec_cs.id "
	//
	// + "				INNER JOIN order_onoffduty_divide "
	// +
	// "				ON order_onoffduty_exec_details.onoffduty_divide_id=order_onoffduty_divide.id "
	//
	// + "				WHERE "
	// + "					order_onoffduty_divide.deleted=0 AND "
	// + "					order_onoffduty_exec_details.deleted=0 AND "
	// + "					order_exec_cs.deleted=0"
	//
	// + "				ORDER BY order_exec_cs.cr_date DESC "
	// + "				) AS OrderExecInfo "
	// + "			ON OrderExecInfo.orderdetails_id=OrderWithoutExecInfo.id "
	// + "			GROUP BY OrderWithoutExecInfo.id"
	// + "			) AS OrderInfo "
	//
	// + "		LEFT JOIN  user "
	// + "		ON user.id=OrderInfo.driver) AS OrderWithUserInfo "
	//
	// + "LEFT JOIN  evaluation_cs "
	// + "ON "
	// + "		evaluation_cs.from_userid=" + passid + " AND "
	// + "		evaluation_cs.to_userid=OrderWithUserInfo.driver AND "
	// + "		evaluation_cs.order_cs_id=OrderWithUserInfo.exec_id ";
	//
	// if (limit_time != null)
	// szQuery += "	WHERE OrderWithUserInfo.publish_date>\""
	// + ApiGlobal.Date2String(limit_time, true) + "\"";
	//
	// if (time_first)
	// szQuery +=
	// "	ORDER BY OrderWithUserInfo.publish_date DESC, FIELD(OrderWithUserInfo.status, 2,1,3), FIELD(OrderWithUserInfo.exec_status, 2,7,6,5,4,3,1,8)";
	// else
	// szQuery +=
	// "	ORDER BY FIELD(OrderWithUserInfo.status, 2,1,3), FIELD(OrderWithUserInfo.exec_status, 2,7,6,5,4,3,1,8), OrderWithUserInfo.publish_date DESC";
	//
	// rs = stmt.executeQuery(szQuery);
	// while (rs.next()) {
	// SVCUser tmpDrvInfo = null;
	// has_eval = rs.getInt("has_evaluation_passenger");
	// state = rs.getInt("status");
	// exec_status = rs.getInt("exec_status");
	// switch (state) {
	// case 1: {
	// long orderid = rs.getLong("id");
	// szQuery = "SELECT " + "		user.* "
	// + "FROM order_onoffduty_grab " + "INNER JOIN user "
	// + "ON user.id=order_onoffduty_grab.driverid "
	// + "WHERE order_onoffduty_grab.order_id=" + orderid
	// + " AND " + "order_onoffduty_grab.status=0";
	//
	// rs2 = stmt2.executeQuery(szQuery);
	// if (rs2.next()) {
	// tmpDrvInfo = SVCUser.decodeFromResultSet(rs2);
	// state = 0;
	// state_desc = ConstMgr.STR_ORDERSTATE_BUTONGYI;
	// } else {
	// state_desc = ConstMgr.STR_ORDERSTATE_FABU;
	// }
	// rs2.close();
	//
	// break;
	// }
	// case 2: {
	// switch (exec_status) {
	// case 2:
	// state = 2;
	// state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
	// break;
	// case 3:
	// state = 3;
	// state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
	// break;
	// case 4:
	// state = 4;
	// state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
	// break;
	// case 5:
	// state = 5;
	// state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
	// break;
	// case 6:
	// state = 6;
	// state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
	// break;
	// case 7: {
	// if (has_eval == 1) {
	// state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
	// state = 8;
	// } else {
	// state = 7;
	// state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
	// }
	// }
	// break;
	// }
	// break;
	// }
	// case 3: {
	// state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
	// state = 9;
	// break;
	// }
	// default: {
	// state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
	// break;
	// }
	// }
	//
	// int evaluated = 0;
	// String eval_content = "";
	// String eval_desc = "";
	//
	// if (rs.getInt("has_evaluation_passenger") == 0) // Not evaluated
	// // yet
	// {
	// evaluated = 0;
	// eval_content = "";
	// eval_desc = ConstMgr.STR_WEIPINGJIA;
	// } else {
	// evaluated = rs.getInt("level");
	// eval_content = rs.getString("msg");
	// if (eval_content == null)
	// eval_content = "";
	//
	// if (evaluated == 1)
	// eval_desc = ConstMgr.STR_HAOPING;
	// else if (evaluated == 2)
	// eval_desc = ConstMgr.STR_ZHONGPING;
	// else if (evaluated == 3)
	// eval_desc = ConstMgr.STR_CHAPING;
	// }
	//
	// JSONObject order_item = new JSONObject();
	//
	// order_item.put("uid", rs.getLong("id"));
	// order_item.put("order_num", rs.getString("order_num"));
	// order_item.put("type", 2);
	// order_item.put("type_desc", ConstMgr.STR_ONOFFDUTY);
	//
	// if (rs.getLong("driver") == 0 && tmpDrvInfo != null) {
	// order_item.put("driver_id", tmpDrvInfo.id);
	// order_item.put("driver_img",
	// ApiGlobal.getAbsoluteURL(tmpDrvInfo.img));
	// order_item.put("driver_name", tmpDrvInfo.nickname);
	// order_item.put("driver_gender", tmpDrvInfo.sex);
	// order_item.put("driver_age", ApiGlobal.getDiffYear(
	// tmpDrvInfo.birthday, new Date()));
	// } else {
	// order_item.put("driver_id", rs.getLong("driver"));
	// order_item.put("driver_img",
	// ApiGlobal.getAbsoluteURL(rs.getString("img")));
	// order_item.put("driver_name", rs.getString("nickname"));
	// order_item.put("driver_gender", rs.getInt("sex"));
	// order_item.put("driver_age", rs.getInt("age"));
	// }
	//
	// order_item.put("price", rs.getDouble("price"));
	//
	// order_item.put("start_city", "");
	// order_item.put("end_city", "");
	// order_item.put("start_addr", rs.getString("start_addr"));
	// order_item.put("end_addr", rs.getString("end_addr"));
	// order_item.put("start_time", ApiGlobal.Date2Time(ApiGlobal
	// .String2Date(rs.getString("pre_time"))));
	// order_item.put("evaluated", evaluated);
	// order_item.put("eval_content", eval_content);
	// order_item.put("evaluated_desc", eval_desc);
	// order_item.put("state", state);
	// order_item.put("state_desc", state_desc);
	// order_item.put("create_time", ApiGlobal.Date2String(
	// ApiGlobal.String2Date(rs.getString("publish_date")),
	// true));
	//
	// arrOrders.add(order_item);
	// }
	//
	// rs.close();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// } finally {
	// // Close result set
	// if (rs != null) {
	// try {
	// rs.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// if (rs2 != null) {
	// try {
	// rs2.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// // Close normal statement
	// if (stmt != null) {
	// try {
	// stmt.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// if (stmt2 != null) {
	// try {
	// stmt2.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// // Close db connection
	// if (dbConn != null) {
	// try {
	// dbConn.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	// }
	//
	// return arrOrders;
	// }
	//
	// private JSONArray getOnOffOrdersForPassengerBeforeTime(long passid,
	// Date limit_time, int count, boolean time_first) {
	// Connection dbConn = null;
	// Statement stmt = null, stmt2 = null;
	// ResultSet rs = null, rs2 = null;
	// String szQuery = "";
	//
	// JSONArray arrOrders = new JSONArray();
	//
	// try {
	// dbConn = DBManager.getDBConnection();
	// stmt = dbConn.createStatement();
	// stmt2 = dbConn.createStatement();
	//
	// int state, has_eval = 0, exec_status = 0;
	// String state_desc = "";
	//
	// szQuery = "SELECT "
	// + "		OrderWithUserInfo.*,"
	// + "		evaluation_cs.level,"
	// + "		evaluation_cs.msg "
	//
	// + "FROM "
	// + "		(SELECT "
	// + "			OrderInfo.*,"
	//
	// + "			user.id AS driver_id,"
	// + "			user.img,"
	// + "			user.nickname,"
	// + "			user.sex,"
	// + "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "
	//
	// + "		FROM " + "			(SELECT " + "				OrderWithoutExecInfo.*,"
	// + "				OrderExecInfo.* "
	//
	// + "			FROM " + "				(SELECT "
	// + "					order_onoffduty_details.id,"
	// + "					order_onoffduty_details.order_num,"
	// + "					order_onoffduty_details.publish_date,"
	// + "					order_onoffduty_details.price,"
	// + "					order_onoffduty_details.publisher,"
	// + "					order_onoffduty_details.start_addr,"
	// + "					order_onoffduty_details.end_addr,"
	// + "					order_onoffduty_details.pre_time,"
	// + "					order_onoffduty_details.status "
	//
	// + "				FROM order_onoffduty_details "
	//
	// + "				WHERE "
	// + "					order_onoffduty_details.deleted=0 AND "
	// + "					order_onoffduty_details.publisher="
	// + passid
	// + "				) AS OrderWithoutExecInfo "
	//
	// + "			LEFT JOIN "
	// + "				(SELECT "
	// + "					order_exec_cs.id AS exec_id,"
	// + "					order_exec_cs.passenger,"
	// + "					order_exec_cs.driver,"
	// + "					order_exec_cs.cr_date,"
	// + "					order_exec_cs.has_evaluation_passenger,"
	// + "					order_exec_cs.has_evaluation_driver,"
	// + "					order_exec_cs.status AS exec_status, "
	//
	// + "					order_onoffduty_divide.which_days,"
	// + "					order_onoffduty_divide.orderdetails_id,"
	// + "					order_onoffduty_divide.id AS divide_id "
	//
	// + "				FROM order_exec_cs "
	//
	// + "				INNER JOIN order_onoffduty_exec_details "
	// + "				ON order_onoffduty_exec_details.order_cs_id=order_exec_cs.id "
	//
	// + "				INNER JOIN order_onoffduty_divide "
	// +
	// "				ON order_onoffduty_exec_details.onoffduty_divide_id=order_onoffduty_divide.id "
	//
	// + "				WHERE "
	// + "					order_onoffduty_divide.deleted=0 AND "
	// + "					order_onoffduty_exec_details.deleted=0 AND "
	// + "					order_exec_cs.deleted=0"
	//
	// + "				ORDER BY order_exec_cs.cr_date DESC "
	// + "				) AS OrderExecInfo "
	// + "			ON OrderExecInfo.orderdetails_id=OrderWithoutExecInfo.id "
	// + "			GROUP BY OrderWithoutExecInfo.id"
	// + "			) AS OrderInfo "
	//
	// + "		LEFT JOIN  user "
	// + "		ON user.id=OrderInfo.driver) AS OrderWithUserInfo "
	//
	// + "LEFT JOIN  evaluation_cs "
	// + "ON "
	// + "		evaluation_cs.from_userid="
	// + passid
	// + " AND "
	// + "		evaluation_cs.to_userid=OrderWithUserInfo.driver AND "
	// + "		evaluation_cs.order_cs_id=OrderWithUserInfo.exec_id ";
	//
	// if (limit_time != null)
	// szQuery += "	WHERE OrderWithUserInfo.publish_date<\""
	// + ApiGlobal.Date2String(limit_time, true) + "\"";
	//
	// if (time_first)
	// szQuery +=
	// "	ORDER BY OrderWithUserInfo.publish_date DESC, FIELD(OrderWithUserInfo.status, 2,1,3), FIELD(OrderWithUserInfo.exec_status, 2,7,6,5,4,3,1,8) LIMIT 0,"
	// + count;
	// else
	// szQuery +=
	// "	ORDER BY FIELD(OrderWithUserInfo.status, 2,1,3), FIELD(OrderWithUserInfo.exec_status, 2,7,6,5,4,3,1,8), OrderWithUserInfo.publish_date DESC LIMIT 0,"
	// + count;
	//
	// rs = stmt.executeQuery(szQuery);
	// while (rs.next()) {
	// SVCUser tmpDrvInfo = null;
	//
	// has_eval = rs.getInt("has_evaluation_passenger");
	// state = rs.getInt("status");
	// exec_status = rs.getInt("exec_status");
	// switch (state) {
	// case 1: {
	// long orderid = rs.getLong("id");
	// szQuery = "SELECT " + "		user.* "
	// + "FROM order_onoffduty_grab " + "INNER JOIN user "
	// + "ON user.id=order_onoffduty_grab.driverid "
	// + "WHERE order_onoffduty_grab.order_id=" + orderid
	// + " AND " + "order_onoffduty_grab.status=0";
	//
	// rs2 = stmt2.executeQuery(szQuery);
	// if (rs2.next()) {
	// tmpDrvInfo = SVCUser.decodeFromResultSet(rs2);
	// state = 0;
	// state_desc = ConstMgr.STR_ORDERSTATE_BUTONGYI;
	// } else {
	// state_desc = ConstMgr.STR_ORDERSTATE_FABU;
	// }
	// rs2.close();
	//
	// break;
	// }
	// case 2: {
	// switch (exec_status) {
	// case 2:
	// state = 2;
	// state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
	// break;
	// case 3:
	// state = 3;
	// state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
	// break;
	// case 4:
	// state = 4;
	// state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
	// break;
	// case 5:
	// state = 5;
	// state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
	// break;
	// case 6:
	// state = 6;
	// state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
	// break;
	// case 7: {
	// if (has_eval == 1) {
	// state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
	// state = 8;
	// } else {
	// state = 7;
	// state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
	// }
	// }
	// break;
	// }
	// break;
	// }
	// case 3: {
	// state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
	// state = 9;
	// break;
	// }
	// default: {
	// state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
	// break;
	// }
	// }
	//
	// int evaluated = 0;
	// String eval_content = "";
	// String eval_desc = "";
	//
	// if (rs.getInt("has_evaluation_passenger") == 0) // Not evaluated
	// // yet
	// {
	// evaluated = 0;
	// eval_content = "";
	// eval_desc = ConstMgr.STR_WEIPINGJIA;
	// } else {
	// evaluated = rs.getInt("level");
	// eval_content = rs.getString("msg");
	// if (eval_content == null)
	// eval_content = "";
	//
	// if (evaluated == 1)
	// eval_desc = ConstMgr.STR_HAOPING;
	// else if (evaluated == 2)
	// eval_desc = ConstMgr.STR_ZHONGPING;
	// else if (evaluated == 3)
	// eval_desc = ConstMgr.STR_CHAPING;
	// }
	//
	// JSONObject order_item = new JSONObject();
	//
	// order_item.put("uid", rs.getLong("id"));
	// order_item.put("order_num", rs.getString("order_num"));
	// order_item.put("type", 2);
	// order_item.put("type_desc", ConstMgr.STR_ONOFFDUTY);
	//
	// if (rs.getLong("driver") == 0 && tmpDrvInfo != null) {
	// order_item.put("driver_id", tmpDrvInfo.id);
	// order_item.put("driver_img",
	// ApiGlobal.getAbsoluteURL(tmpDrvInfo.img));
	// order_item.put("driver_name", tmpDrvInfo.nickname);
	// order_item.put("driver_gender", tmpDrvInfo.sex);
	// order_item.put("driver_age", ApiGlobal.getDiffYear(
	// tmpDrvInfo.birthday, new Date()));
	// } else {
	// order_item.put("driver_id", rs.getLong("driver"));
	// order_item.put("driver_img",
	// ApiGlobal.getAbsoluteURL(rs.getString("img")));
	// order_item.put("driver_name", rs.getString("nickname"));
	// order_item.put("driver_gender", rs.getInt("sex"));
	// order_item.put("driver_age", rs.getInt("age"));
	// }
	//
	// order_item.put("price", rs.getDouble("price"));
	//
	// order_item.put("start_city", "");
	// order_item.put("end_city", "");
	//
	// order_item.put("start_addr", rs.getString("start_addr"));
	// order_item.put("end_addr", rs.getString("end_addr"));
	// order_item.put("start_time", ApiGlobal.Date2Time(ApiGlobal
	// .String2Date(rs.getString("pre_time"))));
	// order_item.put("evaluated", evaluated);
	// order_item.put("eval_content", eval_content);
	// order_item.put("evaluated_desc", eval_desc);
	// order_item.put("state", state);
	// order_item.put("state_desc", state_desc);
	// order_item.put("create_time", ApiGlobal.Date2String(
	// ApiGlobal.String2Date(rs.getString("publish_date")),
	// true));
	//
	// arrOrders.add(order_item);
	// }
	//
	// rs.close();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// } finally {
	// // Close result set
	// if (rs != null) {
	// try {
	// rs.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// if (rs2 != null) {
	// try {
	// rs2.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// // Close normal statement
	// if (stmt != null) {
	// try {
	// stmt.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// if (stmt2 != null) {
	// try {
	// stmt2.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	//
	// // Close db connection
	// if (dbConn != null) {
	// try {
	// dbConn.close();
	// } catch (Exception sql_ex) {
	// sql_ex.printStackTrace();
	// }
	// }
	// }
	//
	// return arrOrders;
	// }

	private JSONArray getLongOrdersForPassengerAfterTime(long passid,
			Date limit_time, boolean time_first, boolean wait_oper) {
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		JSONArray arrOrders = new JSONArray();

		try {
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			int state, has_eval = 0;
			String state_desc = "";

			szQuery = "SELECT "
					+ "		OrderInfo.*,"
					+ "		evaluation_cs.level,"
					+ "		evaluation_cs.msg "

					+ "FROM "
					+ "		(SELECT "
					+ "			order_longdistance_details.id AS order_id,"
					+ "			order_longdistance_details.order_num,"
					+ "			order_longdistance_details.ps_time,"
					+ "			order_longdistance_details.price,"
					+ "			order_longdistance_details.publisher,"
					+ "			order_longdistance_details.start_city,"
					+ "			order_longdistance_details.end_city,"
					+ "			order_longdistance_details.start_addr,"
					+ "			order_longdistance_details.end_addr,"
					+ "			order_longdistance_details.pre_time,"
					+ "			order_longdistance_details.status,"

					+ "			order_longdistance_users_cs.orderdriverlongdistance_id,"
					+ "			order_longdistance_users_cs.userid,"
					+ "			order_longdistance_users_cs.order_exec_cs_id,"
					+ "			order_longdistance_users_cs.seat_num,"

					+ "			order_exec_cs.id AS exec_id,"
					+ "			order_exec_cs.passenger,"
					+ "			order_exec_cs.driver,"
					+ "			order_exec_cs.cr_date,"
					+ "			order_exec_cs.has_evaluation_passenger,"
					+ "			order_exec_cs.has_evaluation_driver,"
					+ "			order_exec_cs.status AS exec_status,"
					+ "			order_exec_cs.pass_cancel_time,"
					+ "			order_exec_cs.driver_cancel_time,"

					+ "			user.id AS driver_id,"
					+ "			user.img,"
					+ "			user.nickname,"
					+ "			user.sex,"
					+ "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "

					+ "		FROM order_exec_cs "

					+ "		INNER JOIN	user "
					+ "		ON			user.id=order_exec_cs.driver "

					+ "		INNER JOIN	order_longdistance_users_cs "
					+ "		ON			order_longdistance_users_cs.order_exec_cs_id=order_exec_cs.id "

					+ "		INNER JOIN	order_longdistance_details "
					+ "		ON			order_longdistance_details.id = order_longdistance_users_cs.orderdriverlongdistance_id "

					+ "		WHERE "
					+ "			order_longdistance_details.deleted=0 AND "
					+ "			order_longdistance_users_cs.deleted=0 AND "
					+ "			order_exec_cs.deleted=0 AND "
					+ "			order_exec_cs.passenger=" + passid
					+ ") AS OrderInfo "

					+ " LEFT JOIN  evaluation_cs " + "ON "
					+ "		evaluation_cs.order_cs_id=OrderInfo.exec_id AND "
					+ "		evaluation_cs.from_userid=" + passid + " ";

			if (wait_oper) {
				szQuery += " WHERE (OrderInfo.exec_status=6 OR (evaluation_cs.level IS NULL AND OrderInfo.exec_status=7))";

				if (limit_time != null)
					szQuery += "	AND OrderInfo.cr_date>\""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";
			} else {
				if (limit_time != null)
					szQuery += "	WHERE OrderInfo.cr_date>\""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";

				if (time_first)
					szQuery += "	ORDER BY OrderInfo.cr_date DESC, FIELD(OrderInfo.status, 1,6,5,4,3,2,7,8)";
				else
					szQuery += "	ORDER BY FIELD(OrderInfo.status, 1,6,5,4,3,2,7,8), OrderInfo.cr_date DESC";
			}

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				has_eval = rs.getInt("has_evaluation_passenger");
				state = rs.getInt("exec_status");
				switch (state) {
				case 1:
					state_desc = ConstMgr.STR_ORDERSTATE_FABU;
					break;
				case 2:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
					break;
				case 3:
					state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
					break;
				case 4:
					state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
					break;
				case 5:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
					break;
				case 6:
					state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
					break;
				case 7: {
					if (has_eval == 1) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
						state = 8;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
					}
				}
					break;
				case 8:
					if (rs.getDate("pass_cancel_time") == null) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
						state = 9;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YITUIPIAO;
						state = 10;
					}
					break;
				default:
					state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
				}

				int evaluated = 0;
				String eval_content = "";
				String eval_desc = "";

				if (rs.getInt("has_evaluation_passenger") == 0) // Not evaluated
																// yet
				{
					evaluated = 0;
					eval_content = "";
					eval_desc = ConstMgr.STR_WEIPINGJIA;
				} else {
					evaluated = rs.getInt("level");
					eval_content = rs.getString("msg");
					if (eval_content == null)
						eval_content = "";

					if (evaluated == 1)
						eval_desc = ConstMgr.STR_HAOPING;
					else if (evaluated == 2)
						eval_desc = ConstMgr.STR_ZHONGPING;
					else if (evaluated == 3)
						eval_desc = ConstMgr.STR_CHAPING;
				}

				JSONObject order_item = new JSONObject();

				order_item.put("uid", rs.getLong("order_id"));
				order_item.put("order_num", rs.getString("order_num"));
				order_item.put("type", 3);
				order_item.put("type_desc", ConstMgr.STR_LONGDISTANCE);
				order_item.put("driver_id", rs.getLong("driver"));
				order_item.put("driver_img",
						ApiGlobal.getAbsoluteURL(rs.getString("img")));
				order_item.put("driver_name", rs.getString("nickname"));
				order_item.put("driver_gender", rs.getInt("sex"));
				order_item.put("driver_age", rs.getInt("age"));
				order_item.put("price",
						rs.getDouble("price") * rs.getInt("seat_num"));
				order_item.put(
						"start_city",
						ApiGlobal.cityCode2Name(dbConn,
								rs.getString("start_city")));
				order_item.put(
						"end_city",
						ApiGlobal.cityCode2Name(dbConn,
								rs.getString("end_city")));
				order_item.put("start_addr", rs.getString("start_addr"));
				order_item.put("end_addr", rs.getString("end_addr"));
				order_item.put("start_time", ApiGlobal
						.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
								.getString("pre_time"))));
				order_item.put("evaluated", evaluated);
				order_item.put("eval_content", eval_content);
				order_item.put("evaluated_desc", eval_desc);
				order_item.put("state", state);
				order_item.put("state_desc", state_desc);
				order_item.put("create_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_time")), true));

				arrOrders.add(order_item);
			}

			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
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

		return arrOrders;
	}

	private JSONArray getLongOrdersForPassengerBeforeTime(long passid,
			Date limit_time, int count, boolean time_first, boolean wait_oper) {
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		JSONArray arrOrders = new JSONArray();

		try {
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			int state, has_eval = 0;
			String state_desc = "";

			szQuery = "SELECT "
					+ "		OrderInfo.*,"
					+ "		evaluation_cs.level,"
					+ "		evaluation_cs.msg ,"
					+ "     insu.*  "

					+ "FROM "
					+ "		(SELECT "
					+ "			order_longdistance_details.id AS order_id,"
					+ "			order_longdistance_details.order_num,"
					+ "			order_longdistance_details.ps_time,"
					+ "			order_longdistance_details.price,"
					+ "			order_longdistance_details.publisher,"
					+ "			order_longdistance_details.start_city,"
					+ "			order_longdistance_details.end_city,"
					+ "			order_longdistance_details.start_addr,"
					+ "			order_longdistance_details.end_addr,"
					+ "			order_longdistance_details.pre_time,"
					+ "			order_longdistance_details.status,"

					+ "			order_longdistance_users_cs.orderdriverlongdistance_id,"
					+ "			order_longdistance_users_cs.userid,"
					+ "			order_longdistance_users_cs.order_exec_cs_id,"
					+ "			order_longdistance_users_cs.seat_num,"

					+ "			order_exec_cs.id AS exec_id,"
					+ "			order_exec_cs.passenger,"
					+ "			order_exec_cs.driver,"
					+ "			order_exec_cs.cr_date,"
					+ "			order_exec_cs.has_evaluation_passenger,"
					+ "			order_exec_cs.has_evaluation_driver,"
					+ "			order_exec_cs.status AS exec_status,"
					+ "			order_exec_cs.pass_cancel_time,"
					+ "			order_exec_cs.driver_cancel_time,"
					+"    		order_exec_cs.insu_id_pass,"

					+ "			user.id AS driver_id,"
					+ "			user.img,"
					+ "			user.nickname,"
					+ "			user.sex,"
					+ "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age "

					+ "		FROM order_exec_cs "

					+ "		INNER JOIN	user "
					+ "		ON			user.id=order_exec_cs.driver "

					+ "		INNER JOIN	order_longdistance_users_cs "
					+ "		ON			order_longdistance_users_cs.order_exec_cs_id=order_exec_cs.id "

					+ "		INNER JOIN	order_longdistance_details "
					+ "		ON			order_longdistance_details.id = order_longdistance_users_cs.orderdriverlongdistance_id "

					+ "		WHERE "
					+ "			order_longdistance_details.deleted=0 AND "
					+ "			order_longdistance_users_cs.deleted=0 AND "
					+ "			order_exec_cs.deleted=0 AND "
					+ "			order_exec_cs.passenger="
					+ passid
					+ ") AS OrderInfo "

					+ " LEFT JOIN  evaluation_cs "
					+ "ON "
					+ "		evaluation_cs.order_cs_id=OrderInfo.exec_id AND "
					+ "		evaluation_cs.from_userid="
					+ passid
					+ " "
					+ " LEFT JOIN insurance insu ON OrderInfo.insu_id_pass = insu.id ";

			if (wait_oper) {
				szQuery += " WHERE (OrderInfo.exec_status=6 OR (evaluation_cs.level IS NULL AND OrderInfo.exec_status=7))";
				if (limit_time != null)
					szQuery += "	AND OrderInfo.cr_date<\""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";
			} else {
				if (limit_time != null)
					szQuery += "	WHERE OrderInfo.cr_date<\""
							+ ApiGlobal.Date2String(limit_time, true) + "\"";

				if (time_first)
					szQuery += "	ORDER BY OrderInfo.cr_date DESC, FIELD(OrderInfo.status, 1,6,5,4,3,2,7,8) LIMIT 0,"
							+ count;
				else
					szQuery += "	ORDER BY FIELD(OrderInfo.status, 1,6,5,4,3,2,7,8), OrderInfo.cr_date DESC LIMIT 0,"
							+ count;
			}

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				has_eval = rs.getInt("has_evaluation_passenger");
				state = rs.getInt("exec_status");
				switch (state) {
				case 1:
					state_desc = ConstMgr.STR_ORDERSTATE_FABU;
					break;
				case 2:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
					break;
				case 3:
					state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
					break;
				case 4:
					state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
					break;
				case 5:
					state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
					break;
				case 6:
					state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
					break;
				case 7: {
					if (has_eval == 1) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
						state = 8;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
					}
				}
					break;
				case 8:
					if (rs.getDate("pass_cancel_time") == null) {
						state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
						state = 9;
					} else {
						state_desc = ConstMgr.STR_ORDERSTATE_YITUIPIAO;
						state = 10;
					}
					break;
				default:
					state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
				}

				int evaluated = 0;
				String eval_content = "";
				String eval_desc = "";

				if (rs.getInt("has_evaluation_passenger") == 0) // Not evaluated
																// yet
				{
					evaluated = 0;
					eval_content = "";
					eval_desc = ConstMgr.STR_WEIPINGJIA;
				} else {
					evaluated = rs.getInt("level");
					eval_content = rs.getString("msg");
					if (eval_content == null)
						eval_content = "";

					if (evaluated == 1)
						eval_desc = ConstMgr.STR_HAOPING;
					else if (evaluated == 2)
						eval_desc = ConstMgr.STR_ZHONGPING;
					else if (evaluated == 3)
						eval_desc = ConstMgr.STR_CHAPING;
				}

				JSONObject order_item = new JSONObject();

				order_item.put("uid", rs.getLong("order_id"));
				order_item.put("order_num", rs.getString("order_num"));
				order_item.put("type", 3);
				order_item.put("type_desc", ConstMgr.STR_LONGDISTANCE);
				order_item.put("driver_id", rs.getLong("driver"));
				order_item.put("driver_img",
						ApiGlobal.getAbsoluteURL(rs.getString("img")));
				order_item.put("driver_name", rs.getString("nickname"));
				order_item.put("driver_gender", rs.getInt("sex"));
				order_item.put("driver_age", rs.getInt("age"));
				order_item.put("price",
						rs.getDouble("price") * rs.getInt("seat_num"));

				order_item.put(
						"start_city",
						ApiGlobal.cityCode2Name(dbConn,
								rs.getString("start_city")));
				order_item.put(
						"end_city",
						ApiGlobal.cityCode2Name(dbConn,
								rs.getString("end_city")));
				order_item.put("start_addr", rs.getString("start_addr"));
				order_item.put("end_addr", rs.getString("end_addr"));
				order_item.put("start_time", ApiGlobal
						.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
								.getString("pre_time"))));
				order_item.put("evaluated", evaluated);
				order_item.put("eval_content", eval_content);
				order_item.put("evaluated_desc", eval_desc);
				order_item.put("state", state);
				order_item.put("state_desc", state_desc);
				order_item.put("create_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("ps_time")), true));

				//order_item.put("insu_fee", insu_fee);//保险  modify by czq 
				order_item.put("insu_id", rs.getLong("insu_id_pass"));//保单id  modify by czq 
				order_item.put("appl_no", rs.getString("appl_no"));//保单号  modify by czq 
				order_item.put("total_amount", rs.getDouble("total_amount"));//保单金额  modify by czq 
				order_item.put("isd_id", rs.getLong("isd_id"));//被保险人  modify by czq 
				order_item.put("isd_name", rs.getString("isd_name"));//被保险人姓名  modify by czq 
				order_item.put("insu_status", rs.getInt("insu_status"));//保单状态  modify by czq 
				order_item.put("effect_time", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("effect_time")),
						true));// 保单生效期 modify by czq
				order_item.put("insexpr_date", ApiGlobal.Date2String(
						ApiGlobal.String2Date(rs.getString("insexpr_date")),
						true));// 保单生效期 modify by czq

				arrOrders.add(order_item);
			}

			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
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

		return arrOrders;
	}

	/**
 * 1,查询指定订单之后的所有该乘客的临时或者长途订单（取出指定订单的时间，然后查询此时间之后的订单）
	 * 2,查询指定时间之后的所有该乘客的订单（包括单次和长途）
	 * 
 * @param source
 * @param userid
 * @param order_type
 * @param order_num
 * @param limitid_type
 * @param devtoken
 * @return
 */
	public SVCResult latestPassengerOrders(String source, long userid,
			int order_type, String order_num, int limitid_type, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + order_type + "," + order_num + "," + limitid_type + "," + devtoken);

		if (source.equals("") || userid < 0 || order_type < 0
				|| limitid_type < 0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				JSONArray arrOrders = new JSONArray();
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {

					SVCOrderTempDetails limit_order_temp = null;
					// SVCOrderOnOffDutyDetails limit_order_onoff = null;
					SVCOrderLongDistanceDetails limit_order_longdist = null;

					Date limit_date = null;

					stmt = dbConn.createStatement();
					// 1,查询指定订单之后的所有该乘客的临时或者长途订单（取出指定订单的时间，然后查询此时间之后的订单）
					if (limitid_type == 1) // Temporary order
					{
						szQuery = "SELECT * FROM order_temp_details WHERE deleted=0 AND order_num=\""
								+ order_num + "\"";
						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							limit_order_temp = SVCOrderTempDetails
									.decodeFromResultSet(rs);
							limit_date = limit_order_temp.ps_date;
							rs.close();
						}
						// } else if (limitid_type == 2) // On off duty order
						// {
						// szQuery =
						// "SELECT * FROM order_onoffduty_details WHERE deleted=0 AND order_num=\""
						// + order_num + "\"";
						// rs = stmt.executeQuery(szQuery);
						// if (rs.next()) {
						// limit_order_onoff = SVCOrderOnOffDutyDetails
						// .decodeFromResultSet(rs);
						// limit_date = limit_order_onoff.publish_date;
						// rs.close();
						// }
					} else if (limitid_type == 3) // Long distance order
					{
						szQuery = "SELECT * FROM order_longdistance_details WHERE deleted=0 AND order_num=\""
								+ order_num + "\"";
						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							limit_order_longdist = SVCOrderLongDistanceDetails
									.decodeFromResultSet(rs);
							limit_date = limit_order_longdist.ps_time;
							rs.close();
						}
					}
					// 2,查询指定时间之后的所有该乘客的订单（包括单次和长途）
					if (order_type == 1) // Requires all
					{
						JSONArray arrOnceOrders = getOnceOrdersForPassengerAfterTime(
								userid, limit_date, true, false);
						// JSONArray arrOnOffOrders =
						// getOnOffOrdersForPassengerAfterTime(
						// userid, limit_date, true);
						JSONArray arrLongOrders = getLongOrdersForPassengerAfterTime(
								userid, limit_date, true, false);

						arrOrders.addAll(arrOnceOrders);
						// arrOrders.addAll(arrOnOffOrders);
						arrOrders.addAll(arrLongOrders);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForPassComparator());

						arrOrders.clear();
						for (int i = 0; i < listOrders.size(); i++) {
							arrOrders.add(listOrders.get(i));
						}
					} else if (order_type == 2) // Waiting operation
					{
						JSONArray arrOnceOrders = getOnceOrdersForPassengerAfterTime(
								userid, limit_date, false, true);
						// JSONArray arrOnOffOrders =
						// getOnOffOrdersForPassengerAfterTime(
						// userid, limit_date, false);
						JSONArray arrLongOrders = getLongOrdersForPassengerAfterTime(
								userid, limit_date, false, true);

						arrOrders.addAll(arrOnceOrders);
						// arrOrders.addAll(arrOnOffOrders);
						arrOrders.addAll(arrLongOrders);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForPassComparator());

						arrOrders.clear();
						for (int i = 0; i < listOrders.size(); i++) {
							arrOrders.add(listOrders.get(i));
						}
					} else if (order_type == 3) // Once carpool
					{
						arrOrders = getOnceOrdersForPassengerAfterTime(userid,
								limit_date, true, false);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForPassComparator());

						arrOrders.clear();
						for (int i = 0; i < listOrders.size(); i++) {
							arrOrders.add(listOrders.get(i));
						}
						// } else if (order_type == 4) // Onoff carpool
						// {
						// arrOrders =
						// getOnOffOrdersForPassengerAfterTime(userid,
						// limit_date, true);
					} else if (order_type == 5) // Long distance carpool
					{
						arrOrders = getLongOrdersForPassengerAfterTime(userid,
								limit_date, true, false);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForPassComparator());

						arrOrders.clear();
						for (int i = 0; i < listOrders.size(); i++) {
							arrOrders.add(listOrders.get(i));
						}
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult pagedPassengerOrders(String source, long userid,
			int order_type, String limit_time, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + order_type + "," + limit_time + "," + devtoken);

		if (source.equals("") || userid < 0 || order_type < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;

			try {
				JSONArray arrOrders = new JSONArray();
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {

					Date limit_date = ApiGlobal.String2Date(limit_time);

					stmt = dbConn.createStatement();
					// 1,按分页查询该乘客的单次订单，长途订单。并把两者结果合并起来，一起返回给手机端。
					if (order_type == 1) // Requires all
					{
						JSONArray arrOnceOrders = getOnceOrdersForPassengerBeforeTime(
								userid, limit_date,
								ApiGlobal.getPageItemCount(), true, false);
						// JSONArray arrOnOffOrders =
						// getOnOffOrdersForPassengerBeforeTime(
						// userid, limit_date,
						// ApiGlobal.getPageItemCount(), true);
						JSONArray arrLongOrders = getLongOrdersForPassengerBeforeTime(
								userid, limit_date,
								ApiGlobal.getPageItemCount(), true, false);

						arrOrders.addAll(arrOnceOrders);
						// arrOrders.addAll(arrOnOffOrders);
						arrOrders.addAll(arrLongOrders);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForPassComparator());

						arrOrders.clear();
						for (int i = 0; i < Math.min(listOrders.size(),
								ApiGlobal.getPageItemCount()); i++) {
							arrOrders.add(listOrders.get(i));
						}
					} else if (order_type == 2) // Waiting operation
					{
						JSONArray arrOnceOrders = getOnceOrdersForPassengerBeforeTime(
								userid, limit_date,
								ApiGlobal.getPageItemCount(), false, true);
						// JSONArray arrOnOffOrders =
						// getOnOffOrdersForPassengerBeforeTime(
						// userid, limit_date,
						// ApiGlobal.getPageItemCount(), false);
						JSONArray arrLongOrders = getLongOrdersForPassengerBeforeTime(
								userid, limit_date,
								ApiGlobal.getPageItemCount(), false, true);

						arrOrders.addAll(arrOnceOrders);
						// arrOrders.addAll(arrOnOffOrders);
						arrOrders.addAll(arrLongOrders);

						ArrayList<JSONObject> listOrders = new ArrayList<JSONObject>();
						for (int i = 0; i < arrOrders.size(); i++) {
							listOrders.add(arrOrders.getJSONObject(i));
						}

						Collections.sort(listOrders,
								new OrderObjectTimeForPassComparator());

						arrOrders.clear();
						for (int i = 0; i < Math.min(listOrders.size(),
								ApiGlobal.getPageItemCount()); i++) {
							arrOrders.add(listOrders.get(i));
						}
					} else if (order_type == 3) // Once carpool
					{
						arrOrders = getOnceOrdersForPassengerBeforeTime(userid,
								limit_date, ApiGlobal.getPageItemCount(), true,
								false);
						// } else if (order_type == 4) // Onoff carpool
						// {
						// arrOrders = getOnOffOrdersForPassengerBeforeTime(
						// userid, limit_date,
						// ApiGlobal.getPageItemCount(), true);
					} else if (order_type == 5) // Long distance carpool
					{
						arrOrders = getLongOrdersForPassengerBeforeTime(userid,
								limit_date, ApiGlobal.getPageItemCount(), true,
								false);
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult latestAcceptableOnceOrders(String source, long userid,
			long limitid, int order_type, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + limitid + "," + order_type + "," + devtoken);

		if (source.equals("") || userid < 0 || order_type < 0 || limitid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				JSONArray arrOrders = new JSONArray();
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				int nUserCarType = ApiGlobal.getUserCarType(dbConn, userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (userinfo.driver_verified != 1) // Not verified driver
				{
					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
				} else {
					SVCOrderTempDetails limit_order_temp = null;
					Date limit_date = null;

					String sysfee_desc = "";
					double sysfee_value = 0, price = 0;
					double ratio = 0, integer_ = 0, active = 0;
					double ratio_city = 0, integer_city = 0, active_city = 0;
					double insu_fee = 0;// 保险费  modify by chuzhiqiang
					// double insu_fee_city = 0;// 保险费 modify by chuzhiqiang

					// SVCOrderTempDetails orderinfo =
					// ApiGlobal.getOnceOrderInfo(dbConn, limitid);

					double userlat = 0, userlng = 0, distance = 0;
					String distance_desc = "";

					SVCGlobalParams global_params = null;

					stmt = dbConn.createStatement();

					szQuery = "SELECT * FROM global_params WHERE deleted=0";
					rs = stmt.executeQuery(szQuery);
					// Global parameters must exist. If not exist, it is
					// abnormal.
					rs.next();
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();

					szQuery = "SELECT * FROM order_temp_details WHERE deleted=0 AND id="
							+ limitid;
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						limit_order_temp = SVCOrderTempDetails
								.decodeFromResultSet(rs);
						limit_date = limit_order_temp.ps_date;
					}
					rs.close();

					szQuery = "SELECT * FROM user_online WHERE deleted=0 AND userid="
							+ userid
							+ " ORDER BY last_heartbeat_time DESC LIMIT 0,1";
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						SVCUserOnline user_online = SVCUserOnline
								.decodeFromResultSet(rs);
						userlat = user_online.lat;
						userlng = user_online.lng;

						rs.close();
					} else {
						userlat = 0;
						userlng = 0;
					}
					// 1,查询order_temp_notify表，看当前车主能接哪些单次订单。
					// 2,车主比如自己的车型级别为豪华型，那么只能接经济型的单子。
					// 3,车主只能查询出没有过期的订单，并且没有被人抢走的单子。
					// 4,车主需要查看limit_date之后的订单。
					// 5,每个单子要能显示平台收取的信息费
					// 6,查询出的订单能按距离排序，按价格排序，按时间排序。
					// 7,需要查询出订单的过期时间，这个是通过发单时间加wait_time实现的，wait_time应该是发单的时候写入的
					// 8,需要查询出中途点的个数

					szQuery = "SELECT "
							+ "		AcceptableOrderInfo.order_id,"
							+ "		order_temp_details.id,"
							+ "		order_temp_details.order_num,"
							+ "		order_temp_details.price,"
							+ "		order_temp_details.start_addr,"
							+ "		order_temp_details.end_addr,"
							+ "		order_temp_details.pre_time,"
							+ "		order_temp_details.ps_date,"
							+ "		order_temp_details.start_lat,"
							+ "		order_temp_details.start_lng,"
							+ "		TIMESTAMPADD(SECOND, order_temp_details.wait_time, order_temp_details.ps_date) AS exp_time,"

							+ "		user.id AS pass_id,"
							+ "		user.nickname AS pass_name,"
							+ "		user.sex AS pass_gender,"
							+ "		user.img AS pass_img,"
							+ "		TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS pass_age, "

							+ "		city_info.ratio,"
							+ "		city_info.integer_,"
							+ "		city_info.active, "
							+ "		city_info.insu_fee, "// 保险费

							+ "		(SELECT COUNT(*) "
							+ "		FROM midpoints "
							+ "		WHERE"
							+ "			deleted=0 AND "
							+ "			orderid=order_temp_details.id) AS mid_count, "

							+ "		(SELECT COUNT(*) "
							+ "		FROM order_temp_grab "
							+ "		WHERE "
							+ "			status=0 AND "
							+ "			deleted=0 AND "
							+ "			order_id = order_temp_details.id) AS accept_count "

							+ "FROM "
							+ "		(SELECT "
							+ "			order_id "
							+ "		FROM "
							+ "			order_temp_notify "
							+ "		WHERE "
							+ "			deleted=0 AND "
							+ "			userid="
							+ userid
							+ ") AS AcceptableOrderInfo "

							+ "INNER JOIN order_temp_details "
							+ "ON order_temp_details.id = AcceptableOrderInfo.order_id "

							+ "INNER JOIN user "
							+ "ON user.id = order_temp_details.publisher "

							+ "INNER JOIN (SELECT * FROM city WHERE deleted=0) AS city_info "
							+ "ON order_temp_details.order_city=city_info.code "

							+ "WHERE" + "		user.deleted=0 AND "
							+ "		order_temp_details.deleted=0 AND "
							+ "		order_temp_details.status=1 AND "
							+ "		order_temp_details.reqcarstyle <= "
							+ nUserCarType + " ";

					if (limit_date != null)
						szQuery += " AND order_temp_details.ps_date>\""
								+ ApiGlobal.Date2String(limit_date, true)
								+ "\"";

					szQuery += " HAVING exp_time >= CURRENT_TIMESTAMP() AND accept_count = 0";

					if (order_type == 1) // Distance sort
					{
						szQuery += " ORDER BY calc_distance(order_temp_details.start_lat, order_temp_details.start_lng,"
								+ userlat
								+ ","
								+ userlng
								+ ") ASC, price ASC, order_temp_details.ps_date DESC";
					} else if (order_type == 2) // Price sort
					{
						szQuery += " ORDER BY price ASC, calc_distance(order_temp_details.start_lat, order_temp_details.start_lng,"
								+ userlat
								+ ","
								+ userlng
								+ ") ASC, order_temp_details.ps_date DESC";
					} else if (order_type == 3) // Time sort
					{
						szQuery += " ORDER BY order_temp_details.ps_date DESC, price ASC, calc_distance(order_temp_details.start_lat, order_temp_details.start_lng,"
								+ userlat + "," + userlng + ") ASC";
					}

					System.out
							.println("SVCOrderService.latestAcceptableOnceOrders()-->"
									+ szQuery);
					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						ratio_city = rs.getDouble("ratio");
						integer_city = rs.getDouble("integer_");
						active_city = rs.getInt("active");
						// insu_fee_city = rs.getDouble("insu_fee");//modify by
						// chuzhiqiang
						insu_fee = global_params.insu_fee;// modify by
															// chuzhiqiang

						if (active_city < 0) // City parameters are not set. Use
												// global params
						{
							ratio = global_params.ratio;
							integer_ = global_params.integer_;
							active = global_params.active;
							// insu_fee = global_params.insu_fee;//modify by
							// chuzhiqiang
						} else // City parameters are set. Use city params
						{
							ratio = ratio_city;
							integer_ = integer_city;
							active = active_city;
							// insu_fee= insu_fee_city;//modify by chuzhiqiang
						}

						price = rs.getDouble("price");
						if (active == 0)
							sysfee_value = price * ratio / 100;
						else
							sysfee_value = Math.min(integer_, price);
						sysfee_desc = ConstMgr.STR_PINGTAI
								+ ApiGlobal.Double2String(sysfee_value, 2)
								+ ConstMgr.STR_DIAN;

						distance = ApiGlobal.calcDist(userlat, userlng,
								rs.getDouble("start_lat"),
								rs.getDouble("start_lng"));
						if (distance < 0.2) {
							distance_desc = "<200m";
						} else if (distance < 1) {
							distance_desc = "" + (int) (distance * 1000) + "km";
						} else {
							distance_desc = ApiGlobal
									.Double2String(distance, 2) + "km";
						}

						int mid_count = rs.getInt("mid_count");
						String midcount_desc = ConstMgr.STR_ZHONGTUDIAN + " : "
								+ mid_count;

						JSONObject jsonItem = new JSONObject();

						jsonItem.put("uid", rs.getLong("id"));
						jsonItem.put("order_num", rs.getString("order_num"));
						jsonItem.put("pass_id", rs.getLong("pass_id"));
						jsonItem.put("pass_img", ApiGlobal.getAbsoluteURL(rs
								.getString("pass_img")));
						jsonItem.put("pass_name", rs.getString("pass_name"));
						jsonItem.put("pass_gender", rs.getInt("pass_gender"));
						jsonItem.put("pass_age", rs.getInt("pass_age"));
						jsonItem.put("price", price);
						jsonItem.put("sysinfo_fee", sysfee_value);
						jsonItem.put("insu_fee", insu_fee);// modify by
															// chuzhiqiang
						jsonItem.put("sysinfo_fee_desc", sysfee_desc);
						jsonItem.put("start_addr", rs.getString("start_addr"));
						jsonItem.put("end_addr", rs.getString("end_addr"));
						jsonItem.put("distance", distance);
						jsonItem.put("distance_desc", distance_desc);
						jsonItem.put("midpoints", mid_count);
						jsonItem.put("midpoints_desc", midcount_desc);
						jsonItem.put("start_time", ApiGlobal
								.Date2StringWithoutSeconds(ApiGlobal
										.String2Date(rs.getString("pre_time"))));
						jsonItem.put("create_time", ApiGlobal.Date2String(
								ApiGlobal.String2Date(rs.getString("ps_date")),
								true));

						arrOrders.add(jsonItem);
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult pagedAcceptableOnceOrders(String source, long userid,
			int pageno, int order_type, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + pageno + "," + order_type + "," + devtoken);

		if (source.equals("") || userid < 0 || order_type < 0 || pageno < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				JSONArray arrOrders = new JSONArray();
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				int nUserCarType = ApiGlobal.getUserCarType(dbConn, userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (userinfo.driver_verified != 1) // Not verified driver
				{
					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
				} else {
					String sysfee_desc = "";
					double sysfee_value = 0, price = 0;
					double ratio = 0, integer_ = 0, active = 0;
					double ratio_city = 0, integer_city = 0, active_city = 0;
					double insu_fee = 0;// modify by chuzhiqiang

					double userlat = 0, userlng = 0;
					double distance = 0;
					String distance_desc = "";

					SVCGlobalParams global_params = null;

					stmt = dbConn.createStatement();

					szQuery = "SELECT * FROM global_params WHERE deleted=0";
					rs = stmt.executeQuery(szQuery);
					// Global parameters must exist. If not exist, it is
					// abnormal.
					rs.next();
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();

					insu_fee = global_params.insu_fee;//使用全局的保险费    modify by chuzhiqiang
					szQuery = "SELECT * FROM user_online WHERE deleted=0 AND userid="
							+ userid
							+ " ORDER BY last_heartbeat_time DESC LIMIT 0,1";
					System.out.println("s1:" + szQuery);
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						SVCUserOnline user_online = SVCUserOnline
								.decodeFromResultSet(rs);
						userlat = user_online.lat;
						userlng = user_online.lng;

						rs.close();
					} else {
						userlat = 0;
						userlng = 0;
					}

					szQuery = "SELECT "
							+ "		order_temp_details.id,"
							+ "		order_temp_details.order_num,"
							+ "		order_temp_details.price,"
							+ "		order_temp_details.start_addr,"
							+ "		order_temp_details.end_addr,"
							+ "		order_temp_details.pre_time,"
							+ "		order_temp_details.ps_date,"
							+ "		order_temp_details.start_lat,"
							+ "		order_temp_details.start_lng,"
							+ "		TIMESTAMPADD(SECOND, order_temp_details.wait_time, order_temp_details.ps_date) AS exp_time,"

							+ "		user.id AS pass_id,"
							+ "		user.nickname AS pass_name,"
							+ "		user.sex AS pass_gender,"
							+ "		user.img AS pass_img,"
							+ "		TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS pass_age, "

							+ "		city_info.ratio,"
							+ "		city_info.integer_,"
							+ "		city_info.active, "

							//中途点 功能 砍掉了  begain modify  by  chuzhiqiang  
							+ "		(SELECT COUNT(*) "
							+ "		FROM midpoints "
							+ "		WHERE"
							+ "			deleted=0 AND "
							+ "			order_type=0 AND "
							+ "			orderid=order_temp_details.id) AS mid_count, "
							//  end  modify  by chuzhiqiang
							+ "		(SELECT COUNT(*) "
							+ "		FROM order_temp_grab "
							+ "		WHERE status=0 AND deleted=0 AND order_id = order_temp_details.id) AS accept_count "

							+ "FROM order_temp_details "

							+ "INNER JOIN user "
							+ "ON user.id = order_temp_details.publisher "

							+ "INNER JOIN (SELECT * FROM city WHERE deleted=0) AS city_info "
							+ "ON order_temp_details.order_city=city_info.code "

							+ "WHERE "
							+ "		user.deleted=0 AND "
							+ "		order_temp_details.deleted=0 AND " // Not
																	// deleted
							+ "		order_temp_details.status=1 AND "
							+ "		order_temp_details.reqcarstyle <= "
							+ nUserCarType + " ";
					//接单列表页面，应该包括发布时间+等待时间》=当前时间（可以抢的情况），还有就是出发时间》=当前时间的单子，就是预约拼的单子。
					szQuery += " HAVING exp_time >= CURRENT_TIMESTAMP() AND accept_count = 0  OR  pre_time >=CURRENT_TIMESTAMP() ";

					if (order_type == 1) // Distance sort
					{
						szQuery += " ORDER BY calc_distance(order_temp_details.start_lat, order_temp_details.start_lng,"
								+ userlat
								+ ","
								+ userlng
								+ ") ASC, price ASC, order_temp_details.ps_date DESC";
					} else if (order_type == 2) // Price sort
					{
						szQuery += " ORDER BY price DESC, calc_distance(order_temp_details.start_lat, order_temp_details.start_lng,"
								+ userlat
								+ ","
								+ userlng
								+ ") ASC, order_temp_details.ps_date DESC";
					} else if (order_type == 3) // Time sort
					{
						szQuery += " ORDER BY order_temp_details.ps_date DESC, price ASC, calc_distance(order_temp_details.start_lat, order_temp_details.start_lng,"
								+ userlat + "," + userlng + ") ASC";
					}

					szQuery += " LIMIT " + ApiGlobal.getPageItemCount()
							* pageno + "," + ApiGlobal.getPageItemCount();

					System.out.println("s2:" + szQuery);

					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						ratio_city = rs.getDouble("ratio");
						integer_city = rs.getDouble("integer_");
						active_city = rs.getInt("active");
						insu_fee = global_params.insu_fee;// maodify by
															// chuzhiqiang
						if (active_city < 0) // City parameters are not set. Use
												// global params
						{
							ratio = global_params.ratio;
							integer_ = global_params.integer_;
							active = global_params.active;
						} else // City parameters are set. Use city params
						{
							ratio = ratio_city;
							integer_ = integer_city;
							active = active_city;
						}

						price = rs.getDouble("price");
						if (active == 0)
							sysfee_value = price * ratio / 100;
						else
							sysfee_value = Math.min(integer_, price);
						sysfee_desc = ConstMgr.STR_PINGTAI
								+ ApiGlobal.Double2String(sysfee_value, 2)
								+ ConstMgr.STR_DIAN;

						distance = ApiGlobal.calcDist(userlat, userlng,
								rs.getDouble("start_lat"),
								rs.getDouble("start_lng"));
						if (distance < 0.2) {
							distance_desc = "<200m";
						} else if (distance < 1) {
							distance_desc = "" + (int) (distance * 1000) + "km";
						} else {
							distance_desc = ApiGlobal
									.Double2String(distance, 2) + "km";
						}

						int mid_count = rs.getInt("mid_count");
						String midcount_desc = ConstMgr.STR_ZHONGTUDIAN + " : "
								+ mid_count;

						JSONObject jsonItem = new JSONObject();

						jsonItem.put("uid", rs.getLong("id"));
						jsonItem.put("order_num", rs.getString("order_num"));
						jsonItem.put("pass_id", rs.getLong("pass_id"));
						jsonItem.put("pass_img", ApiGlobal.getAbsoluteURL(rs
								.getString("pass_img")));
						jsonItem.put("pass_name", rs.getString("pass_name"));
						jsonItem.put("pass_gender", rs.getInt("pass_gender"));
						jsonItem.put("pass_age", rs.getInt("pass_age"));
						jsonItem.put("price", price);
						jsonItem.put("sysinfo_fee", sysfee_value);
						jsonItem.put("insu_fee", insu_fee);
						jsonItem.put("sysinfo_fee_desc", sysfee_desc);
						jsonItem.put("start_addr", rs.getString("start_addr"));
						jsonItem.put("end_addr", rs.getString("end_addr"));
						jsonItem.put("distance", distance);
						jsonItem.put("distance_desc", distance_desc);
						jsonItem.put("midpoints", mid_count);
						jsonItem.put("midpoints_desc", midcount_desc);
						jsonItem.put("start_time", ApiGlobal
								.Date2StringWithoutSeconds(ApiGlobal
										.String2Date(rs.getString("pre_time"))));
						jsonItem.put("create_time", ApiGlobal.Date2String(
								ApiGlobal.String2Date(rs.getString("ps_date")),
								true));

						arrOrders.add(jsonItem);
						System.out.println("jsonItem:" + jsonItem.toString());
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;

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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult latestAcceptableOnOffOrders(String source, long userid,
			long limitid, String start_addr, String end_addr, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + limitid + "," + start_addr + "," + end_addr + "," + devtoken);

		if (source.equals("") || userid < 0 || limitid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				JSONArray arrOrders = new JSONArray();
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				int nUserCarType = ApiGlobal.getUserCarType(dbConn, userid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (userinfo.driver_verified != 1) // Not verified driver
				{
					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
				} else {
					SVCOrderOnOffDutyDetails limit_order_onoff = null;
					Date limit_date = null;

					String sysfee_desc = "";
					double sysfee_value = 0, price = 0;
					double ratio = 0, integer_ = 0, active = 0;
					double ratio_city = 0, integer_city = 0, active_city = 0;

					SVCGlobalParams global_params = null;

					stmt = dbConn.createStatement();

					szQuery = "SELECT * FROM global_params WHERE deleted=0";
					rs = stmt.executeQuery(szQuery);
					// Global parameters must exist. If not exist, it is
					// abnormal.
					rs.next();
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();

					szQuery = "SELECT * FROM order_onoffduty_details WHERE deleted=0 AND id="
							+ limitid;
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						limit_order_onoff = SVCOrderOnOffDutyDetails
								.decodeFromResultSet(rs);
						limit_date = limit_order_onoff.publish_date;
						rs.close();
					}

					szQuery = "SELECT "
							+ "		order_onoffduty_details.id,"
							+ "		order_onoffduty_details.order_num,"
							+ "		order_onoffduty_details.price,"
							+ "		order_onoffduty_details.start_addr,"
							+ "		order_onoffduty_details.end_addr,"
							+ "		order_onoffduty_details.pre_time,"
							+ "		order_onoffduty_details.leftdays,"
							+ "		order_onoffduty_details.publish_date,"

							+ "		user.id AS pass_id,"
							+ "		user.nickname AS pass_name,"
							+ "		user.sex AS pass_gender,"
							+ "		user.img AS pass_img,"
							+ "		TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS pass_age, "

							+ "		city_info.ratio,"
							+ "		city_info.integer_,"
							+ "		city_info.active, "

							+ "		(SELECT COUNT(*) "
							+ "		FROM midpoints "
							+ "		WHERE"
							+ "			deleted=0 AND "
							+ "			order_type=1 AND "
							+ "			orderid=order_onoffduty_details.id) AS mid_count, "

							+ "		(SELECT COUNT(*) "
							+ "		FROM order_onoffduty_grab "
							+ "		WHERE status=0 AND deleted=0 AND order_id = order_onoffduty_details.id) AS accept_count "

							+ "FROM order_onoffduty_details "

							+ "INNER JOIN user "
							+ "ON user.id = order_onoffduty_details.publisher "

							+ "INNER JOIN (SELECT * FROM city WHERE deleted=0) AS city_info "
							+ "ON order_onoffduty_details.order_city=city_info.code "

							+ "WHERE "
							+ "		user.deleted=0 AND "
							+ "		order_onoffduty_details.deleted=0 AND " // Not
																			// deleted
							+ "		order_onoffduty_details.status=1 AND " // Not
																		// accepted
																		// yet
							+ "		order_onoffduty_details.publisher <> "
							+ userid + " AND "
							+ "		order_onoffduty_details.start_addr LIKE \"%"
							+ start_addr + "%\" AND "
							+ "		order_onoffduty_details.end_addr LIKE \"%"
							+ end_addr + "%\" AND "
							+ "		order_onoffduty_details.order_city LIKE \"%"
							+ userinfo.city_cur + "%\" AND "
							+ "		order_onoffduty_details.reqcarstyle <="
							+ nUserCarType;

					if (limit_date != null)
						szQuery += " AND order_onoffduty_details.publish_date>\""
								+ ApiGlobal.Date2String(limit_date, true)
								+ "\" ";

					szQuery += " HAVING accept_count = 0";

					szQuery += " ORDER BY order_onoffduty_details.publish_date DESC, order_onoffduty_details.price ASC, FIELD(order_onoffduty_details.status, 2,1,3)";

					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						ratio_city = rs.getDouble("ratio");
						integer_city = rs.getDouble("integer_");
						active_city = rs.getInt("active");

						if (active_city < 0) // City parameters are not set. Use
												// global params
						{
							ratio = global_params.ratio;
							integer_ = global_params.integer_;
							active = global_params.active;
						} else // City parameters are set. Use city params
						{
							ratio = ratio_city;
							integer_ = integer_city;
							active = active_city;
						}

						price = rs.getDouble("price");
						if (active == 0)
							sysfee_value = price * ratio / 100;
						else
							sysfee_value = Math.min(integer_, price);
						sysfee_desc = ConstMgr.STR_PINGTAI
								+ ApiGlobal.Double2String(sysfee_value, 2)
								+ ConstMgr.STR_DIAN;

						int mid_count = rs.getInt("mid_count");
						String midcount_desc = ConstMgr.STR_ZHONGTUDIAN + " : "
								+ mid_count;

						JSONObject jsonItem = new JSONObject();

						jsonItem.put("uid", rs.getLong("id"));
						jsonItem.put("order_num", rs.getString("order_num"));
						jsonItem.put("pass_id", rs.getLong("pass_id"));
						jsonItem.put("pass_img", ApiGlobal.getAbsoluteURL(rs
								.getString("pass_img")));
						jsonItem.put("pass_name", rs.getString("pass_name"));
						jsonItem.put("pass_gender", rs.getInt("pass_gender"));
						jsonItem.put("pass_age", rs.getInt("pass_age"));
						jsonItem.put("price", price);
						jsonItem.put("sysinfo_fee", sysfee_value);
						jsonItem.put("sysinfo_fee_desc", sysfee_desc);
						jsonItem.put("start_addr", rs.getString("start_addr"));
						jsonItem.put("end_addr", rs.getString("end_addr"));
						jsonItem.put("midpoints", mid_count);
						jsonItem.put("midpoints_desc", midcount_desc);

						String szStartTime = ApiGlobal.Date2Time(ApiGlobal
								.String2Date(rs.getString("pre_time")));
						String szStartTimeDesc = szStartTime
								+ ConstMgr.STR_CHUFA;
						jsonItem.put("start_time", szStartTime);
						jsonItem.put("start_time_desc", szStartTimeDesc);

						jsonItem.put("days", rs.getString("leftdays"));
						jsonItem.put("create_time", ApiGlobal.Date2String(
								ApiGlobal.String2Date(rs
										.getString("publish_date")), true));

						arrOrders.add(jsonItem);
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult pagedAcceptableOnOffOrders(String source, long userid,
			int pageno, String start_addr, String end_addr, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + pageno + "," + start_addr + "," + end_addr + "," + devtoken);

		if (source.equals("") || userid < 0 || pageno < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				JSONArray arrOrders = new JSONArray();
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				int nUserCarType = ApiGlobal.getUserCarType(dbConn, userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (userinfo.driver_verified != 1) // Not verified driver
				{
					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
				} else {
					String sysfee_desc = "";
					double sysfee_value = 0, price = 0;
					double ratio = 0, integer_ = 0, active = 0;
					double ratio_city = 0, integer_city = 0, active_city = 0;

					SVCGlobalParams global_params = null;

					stmt = dbConn.createStatement();

					szQuery = "SELECT * FROM global_params WHERE deleted=0";
					rs = stmt.executeQuery(szQuery);
					// Global parameters must exist. If not exist, it is
					// abnormal.
					rs.next();
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();

					szQuery = "SELECT "
							+ "		order_onoffduty_details.id,"
							+ "		order_onoffduty_details.order_num,"
							+ "		order_onoffduty_details.price,"
							+ "		order_onoffduty_details.start_addr,"
							+ "		order_onoffduty_details.end_addr,"
							+ "		order_onoffduty_details.pre_time,"
							+ "		order_onoffduty_details.leftdays,"
							+ "		order_onoffduty_details.publish_date,"

							+ "		user.id AS pass_id,"
							+ "		user.nickname AS pass_name,"
							+ "		user.sex AS pass_gender,"
							+ "		user.img AS pass_img,"
							+ "		TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS pass_age, "

							+ "		city_info.ratio,"
							+ "		city_info.integer_,"
							+ "		city_info.active, "

							+ "		(SELECT COUNT(*) "
							+ "		FROM midpoints "
							+ "		WHERE"
							+ "			deleted=0 AND "
							+ "			order_type=1 AND "
							+ "			orderid=order_onoffduty_details.id) AS mid_count, "

							+ "		(SELECT COUNT(*) "
							+ "		FROM order_onoffduty_grab "
							+ "		WHERE status=0 AND deleted=0 AND order_id = order_onoffduty_details.id) AS accept_count "

							+ "FROM order_onoffduty_details "

							+ "INNER JOIN user "
							+ "ON user.id = order_onoffduty_details.publisher "

							+ "INNER JOIN (SELECT * FROM city WHERE deleted=0) AS city_info "
							+ "ON order_onoffduty_details.order_city=city_info.code "

							+ "WHERE "
							+ "		user.deleted=0 AND "
							+ "		order_onoffduty_details.deleted=0 AND " // Not
																			// deleted
							+ "		order_onoffduty_details.status=1 AND " // Not
																		// accepted
																		// yet
							+ "		order_onoffduty_details.publisher <> "
							+ userid + " AND "
							+ "		order_onoffduty_details.start_addr LIKE \"%"
							+ start_addr + "%\" AND "
							+ "		order_onoffduty_details.end_addr LIKE \"%"
							+ end_addr + "%\" AND "
							+ "		order_onoffduty_details.reqcarstyle <="
							+ nUserCarType;

					szQuery += " HAVING accept_count = 0";

					szQuery += " ORDER BY order_onoffduty_details.publish_date DESC, order_onoffduty_details.price ASC, FIELD(order_onoffduty_details.status, 2,1,3) LIMIT "
							+ ApiGlobal.getPageItemCount()
							* pageno
							+ ","
							+ ApiGlobal.getPageItemCount();

					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						ratio_city = rs.getDouble("ratio");
						integer_city = rs.getDouble("integer_");
						active_city = rs.getInt("active");

						if (active_city < 0) // City parameters are not set. Use
												// global params
						{
							ratio = global_params.ratio;
							integer_ = global_params.integer_;
							active = global_params.active;
						} else // City parameters are set. Use city params
						{
							ratio = ratio_city;
							integer_ = integer_city;
							active = active_city;
						}

						price = rs.getDouble("price");
						if (active == 0)
							sysfee_value = price * ratio / 100;
						else
							sysfee_value = Math.min(integer_, price);
						sysfee_desc = ConstMgr.STR_PINGTAI
								+ ApiGlobal.Double2String(sysfee_value, 2)
								+ ConstMgr.STR_DIAN;

						int mid_count = rs.getInt("mid_count");
						String midcount_desc = ConstMgr.STR_ZHONGTUDIAN + " : "
								+ mid_count;

						JSONObject jsonItem = new JSONObject();

						jsonItem.put("uid", rs.getLong("id"));
						jsonItem.put("order_num", rs.getString("order_num"));
						jsonItem.put("pass_id", rs.getLong("pass_id"));
						jsonItem.put("pass_img", ApiGlobal.getAbsoluteURL(rs
								.getString("pass_img")));
						jsonItem.put("pass_name", rs.getString("pass_name"));
						jsonItem.put("pass_gender", rs.getInt("pass_gender"));
						jsonItem.put("pass_age", rs.getInt("pass_age"));
						jsonItem.put("price", price);
						jsonItem.put("sysinfo_fee", sysfee_value);
						jsonItem.put("sysinfo_fee_desc", sysfee_desc);
						jsonItem.put("start_addr", rs.getString("start_addr"));
						jsonItem.put("end_addr", rs.getString("end_addr"));
						jsonItem.put("midpoints", mid_count);
						jsonItem.put("midpoints_desc", midcount_desc);

						String szStartTime = ApiGlobal.Date2Time(ApiGlobal
								.String2Date(rs.getString("pre_time")));
						String szStartTimeDesc = szStartTime
								+ ConstMgr.STR_CHUFA;
						jsonItem.put("start_time", szStartTime);
						jsonItem.put("start_time_desc", szStartTimeDesc);

						jsonItem.put("days", rs.getString("leftdays"));
						jsonItem.put("create_time", ApiGlobal.Date2String(
								ApiGlobal.String2Date(rs
										.getString("publish_date")), true));

						arrOrders.add(jsonItem);
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult latestAcceptableLongOrders(String source, long userid,
			long limitid, String start_addr, String end_addr, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + limitid + "," + start_addr + "," + end_addr + "," + devtoken);

		if (source.equals("") || userid < 0 || limitid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				JSONArray arrOrders = new JSONArray();
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {
					SVCOrderLongDistanceDetails limit_order_long = null;
					Date limit_date = null;

					double price = 0;
					String price_desc = "";
					String start_time = "", start_time_desc = "";
					int leftseats = 0;
					String leftseats_desc = "";

					start_addr = ApiGlobal.removeCityCharacter(start_addr);
					end_addr = ApiGlobal.removeCityCharacter(end_addr);

					stmt = dbConn.createStatement();

					szQuery = "SELECT * FROM order_longdistance_details WHERE deleted=0 AND id="
							+ limitid;
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						limit_order_long = SVCOrderLongDistanceDetails
								.decodeFromResultSet(rs);
						limit_date = limit_order_long.ps_time;
						rs.close();
					}
					// 1,查询出长途订单表order_longdistance_details中所有还可以抢座的单子（publisher不等于自己，剩余座位大于0，status是1或者2）
					// 2,对于每一个长途订单，查询出我是否已经接单了，如果我接单了则值accept_cnt>0。
					// 3,在以上的结果集中筛选accept_cnt等于0的，也就是排除掉我已经接单的长途订单。
					// 4,在以上的结果集中筛选出发城市是%A%的，结束城市是%B%的。
					szQuery = "SELECT "
							+ "		start_city_info.name AS start_city_name,"
							+ "		end_city_info.name AS end_city_name,"
							+ "		OrderInfo.* "
							+ "FROM "
							+ "		(SELECT "
							+ "			order_longdistance_details.id,"
							+ "			order_longdistance_details.order_num,"
							+ "			order_longdistance_details.price,"
							+ "			order_longdistance_details.start_city,"
							+ "			order_longdistance_details.end_city,"
							+ "			order_longdistance_details.start_addr,"
							+ "			order_longdistance_details.end_addr,"
							+ "			order_longdistance_details.pre_time,"
							+ "			order_longdistance_details.ps_time,"
							+ "			order_longdistance_details.seat_num,"
							+ "			order_longdistance_details.occupied_num,"
							+ "			order_longdistance_details.status,"

							+ "			user.id AS driver_id,"
							+ "			user.nickname AS driver_name,"
							+ "			user.sex AS driver_gender,"
							+ "			user.img AS driver_img,"
							+ "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS driver_age, "

							+ "			(SELECT "
							+ "				COUNT(*) "
							+ "			FROM order_exec_cs "
							+ "			LEFT JOIN  order_longdistance_users_cs "
							+ "			ON order_longdistance_users_cs.order_exec_cs_id=order_exec_cs.id "
							+ "			WHERE " + "				order_exec_cs.passenger="
							+ userid
							+ " AND "
							+ "				order_exec_cs.order_type=4 AND "
							+ "				order_exec_cs.status<>8 AND "
							+ "				order_longdistance_users_cs.deleted=0 AND "
							+ "				order_longdistance_users_cs.orderdriverlongdistance_id=order_longdistance_details.id) "
							+ "			AS accept_cnt "

							+ "		FROM order_longdistance_details "

							+ "		INNER JOIN user "
							+ "		ON user.id = order_longdistance_details.publisher "

							+ "		WHERE "
							+ "			user.deleted=0 AND "
							+ "			order_longdistance_details.deleted=0 AND "
							+ "			order_longdistance_details.pre_time > \""
							+ ApiGlobal.Date2String(new Date(), true)
							+ "\" AND "
							+ "			(order_longdistance_details.status=1 OR order_longdistance_details.status=2) AND "
							+ "			order_longdistance_details.seat_num - order_longdistance_details.occupied_num > 0 AND "
							+ "			order_longdistance_details.publisher<>"
							+ userid
							+ ") AS OrderInfo "

							+ "INNER JOIN (SELECT * FROM city WHERE deleted=0) AS start_city_info "
							+ "ON start_city_info.code=OrderInfo.start_city "

							+ "INNER JOIN (SELECT * FROM city WHERE deleted=0) AS end_city_info "
							+ "ON end_city_info.code=OrderInfo.end_city ";

					if (limit_date != null)
						szQuery += " WHERE OrderInfo.ps_time>\""
								+ ApiGlobal.Date2String(limit_date, true)
								+ "\"";

					szQuery += "  HAVING " + "		OrderInfo.accept_cnt=0 AND "
							+ "		start_city_name LIKE \"%" + start_addr
							+ "%\" AND " + "		end_city_name LIKE \"%"
							+ end_addr + "%\"";

					szQuery += " ORDER BY OrderInfo.ps_time DESC, OrderInfo.price ASC, FIELD(OrderInfo.status, 2,1,3)";

					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						price = rs.getDouble("price");
						price_desc = "" + price + ConstMgr.STR_DIAN + "/"
								+ ConstMgr.STR_ZUO;

						start_time = ApiGlobal
								.Date2String(ApiGlobal.String2Date(rs
										.getString("pre_time")), true);
						start_time_desc = start_time + ConstMgr.STR_CHUFA;

						leftseats = rs.getInt("seat_num")
								- rs.getInt("occupied_num");
						if (leftseats > 1)
							leftseats_desc = ConstMgr.STR_SHENGYU + leftseats
									+ ConstMgr.STR_ZUO;
						else
							leftseats_desc = ConstMgr.STR_JINYU + leftseats
									+ ConstMgr.STR_ZUO;

						JSONObject jsonItem = new JSONObject();

						jsonItem.put("uid", rs.getLong("id"));
						jsonItem.put("order_num", rs.getString("order_num"));
						jsonItem.put("driver_id", rs.getLong("driver_id"));
						jsonItem.put("driver_img", ApiGlobal.getAbsoluteURL(rs
								.getString("driver_img")));
						jsonItem.put("driver_name", rs.getString("driver_name"));
						jsonItem.put("driver_gender",
								rs.getInt("driver_gender"));
						jsonItem.put("driver_age", rs.getInt("driver_age"));
						jsonItem.put("price", rs.getDouble("price"));
						jsonItem.put("price_desc", price_desc);
						jsonItem.put("start_addr", rs.getString("start_addr"));
						jsonItem.put("end_addr", rs.getString("end_addr"));
						jsonItem.put("start_city",
								rs.getString("start_city_name"));
						jsonItem.put("end_city", rs.getString("end_city_name"));
						jsonItem.put("start_time", start_time);
						jsonItem.put("start_time_desc", start_time_desc);
						jsonItem.put("leftseats", leftseats);
						jsonItem.put("leftseats_desc", leftseats_desc);
						jsonItem.put("create_time", ApiGlobal.Date2String(
								ApiGlobal.String2Date(rs.getString("ps_time")),
								true));

						arrOrders.add(jsonItem);
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult pagedAcceptableLongOrders(String source, long userid,
			int pageno, String start_addr, String end_addr, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + pageno + "," + start_addr + "," + end_addr + "," + devtoken);

		if (source.equals("") || userid < 0 || pageno < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				JSONArray arrOrders = new JSONArray();
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {
					double price = 0;
					String price_desc = "";
					String start_time = "", start_time_desc = "";
					int leftseats = 0;
					String leftseats_desc = "";

					start_addr = ApiGlobal.removeCityCharacter(start_addr);
					end_addr = ApiGlobal.removeCityCharacter(end_addr);

					String szStartCity = ApiGlobal.cityName2Code(dbConn,
							start_addr);
					String szEndCity = ApiGlobal
							.cityName2Code(dbConn, end_addr);

					if (szStartCity == null || szStartCity.equals("")
							|| szEndCity == null || szEndCity.equals("")) {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
						result.retdata = new JSONArray();
					}

					stmt = dbConn.createStatement();

					szQuery = "SELECT "
							+ "		start_city_info.name AS start_city_name,"
							+ "		end_city_info.name AS end_city_name,"
							+ "		OrderInfo.* "
							+ "FROM "
							+ "		(SELECT "
							+ "			order_longdistance_details.id,"
							+ "			order_longdistance_details.order_num,"
							+ "			order_longdistance_details.price,"
							+ "			order_longdistance_details.start_city,"
							+ "			order_longdistance_details.end_city,"
							+ "			order_longdistance_details.start_addr,"
							+ "			order_longdistance_details.end_addr,"
							+ "			order_longdistance_details.pre_time,"
							+ "			order_longdistance_details.ps_time,"
							+ "			order_longdistance_details.seat_num,"
							+ "			order_longdistance_details.occupied_num,"
							+ "			order_longdistance_details.status,"

							+ "			user.id AS driver_id,"
							+ "			user.nickname AS driver_name,"
							+ "			user.sex AS driver_gender,"
							+ "			user.img AS driver_img,"
							+ "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS driver_age, "

							+ "			(SELECT "
							+ "				COUNT(*) "
							+ "			FROM order_exec_cs "
							+ "			LEFT JOIN  order_longdistance_users_cs "
							+ "			ON order_longdistance_users_cs.order_exec_cs_id=order_exec_cs.id "
							+ "			WHERE " + "				order_exec_cs.passenger="
							+ userid
							+ " AND "
							+ "				order_exec_cs.order_type=4 AND "
							+ "				order_exec_cs.status<>8 AND "
							+ "				order_longdistance_users_cs.deleted=0 AND "
							+ "				order_longdistance_users_cs.orderdriverlongdistance_id=order_longdistance_details.id"
							+ "			) AS accept_cnt "

							+ "		FROM order_longdistance_details "

							+ "		INNER JOIN user "
							+ "		ON user.id = order_longdistance_details.publisher "

							+ "		WHERE "
							+ "			user.deleted=0 AND "
							+ "			order_longdistance_details.deleted=0 AND "
							+ "			(order_longdistance_details.status=1 OR order_longdistance_details.status=2) AND "
							+ "			order_longdistance_details.seat_num - order_longdistance_details.occupied_num > 0 AND "
							+ "			order_longdistance_details.publisher<>"
							+ userid
							+ "		) AS OrderInfo "

							+ "INNER JOIN (SELECT * FROM city WHERE deleted=0) AS start_city_info "
							+ "ON start_city_info.code=OrderInfo.start_city "

							+ "INNER JOIN (SELECT * FROM city WHERE deleted=0) AS end_city_info "
							+ "ON end_city_info.code=OrderInfo.end_city ";

					szQuery += "  HAVING " + "		OrderInfo.accept_cnt=0 AND "
							+ "		start_city_name LIKE \"%" + start_addr
							+ "%\" AND " + "		end_city_name LIKE \"%"
							+ end_addr + "%\"";

					szQuery += " ORDER BY OrderInfo.ps_time DESC, OrderInfo.price ASC, FIELD(OrderInfo.status, 2,1,3) LIMIT "
							+ ApiGlobal.getPageItemCount()
							* pageno
							+ ","
							+ ApiGlobal.getPageItemCount();

					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						price = rs.getDouble("price");
						price_desc = "" + price + ConstMgr.STR_DIAN + "/"
								+ ConstMgr.STR_ZUO;

						start_time = ApiGlobal
								.Date2String(ApiGlobal.String2Date(rs
										.getString("pre_time")), true);
						start_time_desc = start_time + ConstMgr.STR_CHUFA;

						leftseats = rs.getInt("seat_num")
								- rs.getInt("occupied_num");
						if (leftseats > 1)
							leftseats_desc = ConstMgr.STR_SHENGYU + leftseats
									+ ConstMgr.STR_ZUO;
						else
							leftseats_desc = ConstMgr.STR_JINYU + leftseats
									+ ConstMgr.STR_ZUO;

						JSONObject jsonItem = new JSONObject();

						jsonItem.put("uid", rs.getLong("id"));
						jsonItem.put("order_num", rs.getString("order_num"));
						jsonItem.put("driver_id", rs.getLong("driver_id"));
						jsonItem.put("driver_img", ApiGlobal.getAbsoluteURL(rs
								.getString("driver_img")));
						jsonItem.put("driver_name", rs.getString("driver_name"));
						jsonItem.put("driver_gender",
								rs.getInt("driver_gender"));
						jsonItem.put("driver_age", rs.getInt("driver_age"));
						jsonItem.put("price", rs.getDouble("price"));
						jsonItem.put("price_desc", price_desc);
						jsonItem.put("start_addr", rs.getString("start_addr"));
						jsonItem.put("end_addr", rs.getString("end_addr"));
						jsonItem.put(
								"start_city",
								ApiGlobal.cityCode2Name(dbConn,
										rs.getString("start_city")));
						jsonItem.put(
								"end_city",
								ApiGlobal.cityCode2Name(dbConn,
										rs.getString("end_city")));
						jsonItem.put("start_time", start_time);
						jsonItem.put("start_time_desc", start_time_desc);
						jsonItem.put("leftseats", leftseats);
						jsonItem.put("leftseats_desc", leftseats_desc);
						jsonItem.put("create_time", ApiGlobal.Date2String(
								ApiGlobal.String2Date(rs.getString("ps_time")),
								true));

						arrOrders.add(jsonItem);
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,获取该乘客好平率:评价次数，好评次数 2,取得拼车次数 3,获取以往该用户的评价信息，有待优化
	 * 
 * @param source
 * @param userid
 * @param passid
 * @param devtoken
 * @return
 */
	public SVCResult passengerInfo(String source, long userid, long passid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + passid + "," + devtoken);

		if (source.equals("") || userid < 0 || passid < 0
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
			SVCUser passinfo = ApiGlobal.getUserInfoFromUserID(dbConn, passid);
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
			int eval_count = 0, goodeval_count = 0;

			double goodeval_rate = 0;
			String goodeval_rate_desc = "";

			int carpool_count = 0;
			String carpool_count_desc = "";

			JSONArray arrEvaluations = new JSONArray();

			stmt = dbConn.createStatement();
			// 1,获取该乘客好平率:评价次数，好评次数
			szQuery = "SELECT "
					+ "		all_info.count AS eval_count,"
					+ "		good_info.count AS goodeval_count "
					+ "FROM"
					+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
					+ passid
					+ " AND usertype=2) AS all_info, "
					+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
					+ passid + " AND usertype=2 AND level=1) AS good_info ";

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				eval_count = rs.getInt("eval_count");
				goodeval_count = rs.getInt("goodeval_count");

				if (eval_count != 0)
					goodeval_rate = goodeval_count * 100 / eval_count;
			}
			rs.close();

			goodeval_rate_desc = ApiGlobal.Double2String(goodeval_rate, 2)
					+ "%";
			// 2,取得拼车次数
			szQuery = "SELECT COUNT(*) AS count " + "FROM order_exec_cs "
					+ "WHERE " + "		deleted=0 AND "
					+ "		(status=6 OR status=7) AND "
					+ "		pass_cancel_time IS NULL AND "
					+ "		driver_cancel_time IS NULL AND " + "		passenger="
					+ passid;
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				carpool_count = rs.getInt("count");
			}
			rs.close();
			carpool_count_desc = carpool_count + ConstMgr.STR_CI;
			// 3,获取以往该用户的评价信息，有待优化
			szQuery = "SELECT " + "		evaluation_cs.*," + "		user.nickname "
					+ "FROM evaluation_cs " + "INNER JOIN user "
					+ "ON user.id = evaluation_cs.from_userid " + "WHERE "
					+ "		evaluation_cs.deleted=0 AND "
					+ "		evaluation_cs.usertype=2 AND "
					+ "		evaluation_cs.to_userid=" + passid + " "
					+ "ORDER BY evaluation_cs.ps_date DESC LIMIT 0,"
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

			long age = ApiGlobal.getDiffYear(passinfo.birthday, new Date());
			if (age < 0)
				age = 1;

			JSONObject retdata = new JSONObject();
			retdata.put("id", passinfo.id);
			retdata.put("name", passinfo.nickname);
			retdata.put("img", ApiGlobal.getAbsoluteURL(passinfo.img));
			retdata.put("gender", passinfo.sex);
			retdata.put("age", age);
			retdata.put("pverified", passinfo.person_verified == 1 ? 1 : 0);
			retdata.put(
					"pverified_desc",
					passinfo.person_verified == 1 ? ConstMgr.STR_SHENFENYIRENZHENG
							: ConstMgr.STR_SHENFENWEIRENZHENG);
			retdata.put("goodeval_rate", goodeval_rate);
			retdata.put("goodeval_rate_desc", goodeval_rate_desc);
			retdata.put("carpool_count", carpool_count);
			retdata.put("carpool_count_desc", carpool_count_desc);
			retdata.put("eval", arrEvaluations);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
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
	 * 1,获得车主好评率:评价总数，好评数量 2，获得车主拼车次数 3，获得车主评价详情信息 4，获得车主车型信息
	 * 
 * @param source
 * @param userid
 * @param driverid
 * @param devtoken
 * @return
 */
	public SVCResult driverInfo(String source, long userid, long driverid,
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
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn, userid);
			SVCUser drvinfo = ApiGlobal.getUserInfoFromUserID(dbConn, driverid);
			SVCUserCar carinfo = ApiGlobal.getUserCarInfoFromUserID(dbConn,
					driverid);
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
				result.retmsg = ConstMgr.ErrMsg_NoDrvInfo;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			if (carinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoCarInfo;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			int eval_count = 0, goodeval_count = 0;

			double goodeval_rate = 0;
			String goodeval_rate_desc = "";

			int carpool_count = 0;
			String carpool_count_desc = "";

			JSONArray arrEvaluations = new JSONArray();

			stmt = dbConn.createStatement();

			// 1,获得车主好评率:评价总数，好评数量
			szQuery = "SELECT "
					+ "		all_info.count AS eval_count,"
					+ "		good_info.count AS goodeval_count "
					+ "FROM "
					+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
					+ driverid
					+ " AND usertype=1) AS all_info, "
					+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
					+ driverid + " AND usertype=1 AND level=1) AS good_info ";

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				eval_count = rs.getInt("eval_count");
				goodeval_count = rs.getInt("goodeval_count");

				if (eval_count != 0)
					goodeval_rate = goodeval_count * 100 / eval_count;
			}
			rs.close();

			goodeval_rate_desc = ApiGlobal.Double2String(goodeval_rate, 2)
					+ "%";

			// 2，获得车主拼车次数
			szQuery = "SELECT COUNT(*) AS count " + "FROM order_exec_cs "
					+ "WHERE " + "		deleted=0 AND "
					+ "		(status=6 OR status=7) AND "
					+ "		driver_cancel_time IS NULL AND "
					+ "		pass_cancel_time IS NULL AND " + "		driver="
					+ driverid;
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				carpool_count = rs.getInt("count");
			}
			rs.close();
			carpool_count_desc = carpool_count + ConstMgr.STR_CI;

			// 3，获得车主评价详情信息
			szQuery = "SELECT " + "		evaluation_cs.*," + "		user.nickname "
					+ "FROM evaluation_cs " + "INNER JOIN user "
					+ "ON user.id = evaluation_cs.from_userid " + "WHERE "
					+ "		evaluation_cs.deleted=0 AND "
					+ "		evaluation_cs.usertype=1 AND "
					+ "		evaluation_cs.to_userid=" + driverid + " "
					+ "ORDER BY evaluation_cs.ps_date DESC LIMIT 0,"
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

			// 4，获得车主车型信息
			int carstyle = 1;
			szQuery = "SELECT type FROM car_type WHERE id="
					+ carinfo.car_type_id;
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				carstyle = rs.getInt("type");
			rs.close();

			JSONObject jsonCar = new JSONObject();
			jsonCar.put("carimg", ApiGlobal.getAbsoluteURL(carinfo.car_img));
			jsonCar.put("brand", carinfo.brand);
			jsonCar.put("type", carinfo.style);
			jsonCar.put("style", carstyle);
			jsonCar.put("color", carinfo.color);

			long age = ApiGlobal.getDiffYear(drvinfo.birthday, new Date());
			if (age < 0)
				age = 1;

			long drv_career = ApiGlobal.getDiffYear(
					ApiGlobal.String2Date(drvinfo.drlicence_ti), new Date());
			String drv_career_desc = ConstMgr.STR_JIALING + drv_career
					+ ConstMgr.STR_NIAN;

			JSONObject retdata = new JSONObject();
			retdata.put("id", drvinfo.id);
			retdata.put("name", drvinfo.nickname);
			retdata.put("img", ApiGlobal.getAbsoluteURL(drvinfo.img));
			retdata.put("gender", drvinfo.sex);
			retdata.put("age", age);
			retdata.put("drv_career", drv_career);
			retdata.put("drv_career_desc", drv_career_desc);
			retdata.put("goodeval_rate", goodeval_rate);
			retdata.put("goodeval_rate_desc", goodeval_rate_desc);
			retdata.put("carpool_count", carpool_count);
			retdata.put("carpool_count_desc", carpool_count_desc);
			retdata.put("carinfo", jsonCar);
			retdata.put("eval", arrEvaluations);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
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
	 * 如果是单次订单： 1,根据订单id和userid查询出订单详细，评价级别(好中差)，评价内容。逻辑没错，但这条sql以后肯定会慢的不行
	 * 2,查询出该乘客参与的已经完成的执行订单数量 3,获取该订单中途点信息
	 * 
 * @param source
 * @param userid
 * @param orderid
 * @param order_type
 * @param devtoken
 * @return
 */
	public SVCResult detailedDriverOrderInfo(String source, long userid,
			long orderid, int order_type, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + order_type + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0 || order_type < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null, stmt2 = null, stmt3 = null;
			ResultSet rs = null, rs2 = null, rs3 = null;
			String szQuery = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
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

					String sysfee_desc = "";
					double sysfee_value = 0, price;
					double insu_fee = 0;// 保险费 maodify by czq
					double ratio = 0, integer_ = 0, active = 0;
					int state, has_eval = 0;
					String state_desc = "";

					SVCGlobalParams global_params = null;
					SVCCity city = null;

					szQuery = "SELECT * FROM global_params WHERE deleted=0";
					rs = stmt.executeQuery(szQuery);
					// Global parameters must exist. If not exist, it is
					// abnormal
					rs.next();
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();
					
					insu_fee = global_params.insu_fee;//保险费使用全局的参数  modify by czq
					
					szQuery = "SELECT * FROM city WHERE code=\""
							+ userinfo.city_cur + "\" AND deleted=0";
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						city = SVCCity.decodeFromResultSet(rs);

						if (city.active < 0) // City parameters are not set. Use
												// global params
						{
							ratio = global_params.ratio;
							integer_ = global_params.integer_;
							active = global_params.active;
							// insu_fee = global_params.insu_fee;//modify by czq

						} else // City parameters are set. Use city params
						{
							ratio = city.ratio;
							integer_ = city.integer_;
							active = city.active;
							// insu_fee = city.insu_fee;//modify by czq

						}
					} else {
						ratio = global_params.ratio;
						integer_ = global_params.integer_;
						active = global_params.active;
						// insu_fee = global_params.insu_fee;//modify by czq

					}
					rs.close();

					// Result variables
					JSONObject retdata = new JSONObject();
					//JSONObject insu_info = new JSONObject();//封装的保险信息
					JSONArray pass_list = new JSONArray();
					
					JSONArray mid_points = new JSONArray();
					JSONObject statistics = new JSONObject();

					if (order_type == 1) // Once order. <orderid> is
											// <order_temp_details> table id
					{// 1,根据订单id和userid查询出订单详细，评价级别(好中差)，评价内容。逻辑没错，但这条sql以后肯定会慢的不行
						szQuery = "SELECT "
								+ "		OrderInfo.*,"
								+ "		evaluation_cs.level,"
								+ "		insu.*,"// 联结保险表 modify by chuzhiqiang
								+ "		evaluation_cs.msg AS eval_content "

								+ "FROM"
								+ "		(SELECT"
								+ "			order_temp_details.id AS mainID,"
								+ "			order_temp_details.order_num,"
								+ "			order_temp_details.price,"
								+ "			order_temp_details.start_addr,"
								+ "			order_temp_details.start_lat,"
								+ "			order_temp_details.start_lng,"
								+ "			order_temp_details.end_addr,"
								+ "			order_temp_details.end_lat,"
								+ "			order_temp_details.end_lng,"
								+ "			order_temp_details.pre_time,"
								+ "			order_temp_details.status,"
								+ "			order_temp_details.order_cs_id,"
								+ "			order_temp_details.ps_date,"

								+ "			order_exec_cs.id AS exec_id,"
								+ "			order_exec_cs.passenger,"
								+ "			order_exec_cs.cr_date," // Create time
								+ "			order_exec_cs.ti_accept_order," // Accept
								// time
								+ "			order_exec_cs.begin_exec_time," // Start
								// exec
								// time
								+ "			order_exec_cs.has_evaluation_driver,"
								+ "			order_exec_cs.insu_id_driver,"//保险单号  modify by czq

								+ "			user.id AS userid,"
								+ "			user.img,"
								+ "			user.nickname,"
								+ "			user.sex,"
								+ "			user.phone,"
								+ "			user.person_verified,"
								+ "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age, "

								+ "			(SELECT "
								+ "				COUNT(*) "
								+ "			FROM "
								+ "				evaluation_cs "
								+ "			WHERE "
								+ "				deleted=0 AND "
								+ "				usertype=2 AND "
								+ "				to_userid=order_exec_cs.passenger) AS eval_count,"

								+ "			(SELECT "
								+ "				COUNT(*) "
								+ "			FROM "
								+ "				evaluation_cs "
								+ "			WHERE "
								+ "				deleted=0 AND "
								+ "				usertype=2 AND "
								+ "				level=1 AND "
								+ "				to_userid=order_exec_cs.passenger) AS goodeval_count "

								+ "		FROM order_exec_cs "

								+ "		INNER JOIN order_temp_details "
								+ "		ON order_temp_details.order_cs_id = order_exec_cs.id "

								+ "		INNER JOIN user "
								+ "		ON user.id = order_exec_cs.passenger "

								+ "		LEFT JOIN evaluation_cs "
								+ "		ON evaluation_cs.to_userid=order_exec_cs.passenger "

								+ "		WHERE "
								+ "			order_temp_details.id="
								+ orderid
								+ " AND "
								+ "			order_exec_cs.deleted=0 AND "
								+ "			order_exec_cs.driver="
								+ userid
								+ ") AS OrderInfo "

								+ "LEFT JOIN  evaluation_cs "
								+ "ON "
								+ "		evaluation_cs.order_cs_id=OrderInfo.exec_id AND "
								+ "		evaluation_cs.from_userid="
								+ userid
								+ " AND "
								+ "		evaluation_cs.to_userid=OrderInfo.passenger"
								+ "		LEFT JOIN insurance insu ON OrderInfo.insu_id_driver = insu.id";//联结保险表  modify by chuzhiqiang

						System.out
								.println("SVCOrderService.detailedDriverOrderInfo()--》"
										+ szQuery);
						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							retdata.put("uid", rs.getLong("mainID"));
							retdata.put("order_num", rs.getString("order_num"));
							
							//-----begain modify by chuzhiqiang-------------------------------
//							insu_info.put("insu_id", rs.getLong("insu_id"));//保单id  modify by czq 
//							insu_info.put("appl_no", rs.getString("appl_no"));//投保单号  modify by czq 
//							insu_info.put("total_amount", rs.getDouble("total_amount"));//保单金额  modify by czq 
//							insu_info.put("isd_name", rs.getString("isd_name"));//被保险人姓名  modify by czq 
//							insu_info.put("isd_id", rs.getLong("isd_id"));//被保险人id  modify by czq 
//							insu_info.put("insu_status", rs.getInt("insu_status"));//保单状态  modify by czq 
//							insu_info.put("effect_time", ApiGlobal.Date2String(
//									ApiGlobal.String2Date(rs.getString("effect_time")), true));//保单生效期  modify by czq 
//							insu_info.put("insexpr_date", ApiGlobal.Date2String(
//									ApiGlobal.String2Date(rs.getString("insexpr_date")), true));//保单失效期  modify by czq 
							retdata.put("insu_id", rs.getLong("insu_id_driver"));//保单id  modify by czq 
							retdata.put("appl_no", rs.getString("appl_no"));//投保单号  modify by czq 
							
							System.out.println("appl_no-=-=-==-=-="+rs.getString("appl_no")+"  ******   "+retdata.get("appl_no"));
							retdata.put("total_amount", rs.getDouble("total_amount"));//保单金额  modify by czq 
							retdata.put("isd_name", rs.getString("isd_name"));//被保险人姓名  modify by czq 
							retdata.put("isd_id", rs.getLong("isd_id"));//被保险人id  modify by czq 
							retdata.put("insu_status", rs.getInt("insu_status"));//保单状态  modify by czq 
							retdata.put("effect_time", ApiGlobal.Date2String(
									ApiGlobal.String2Date(rs.getString("effect_time")), true));//保单生效期  modify by czq 
							retdata.put("insexpr_date", ApiGlobal.Date2String(
									ApiGlobal.String2Date(rs.getString("insexpr_date")), true));//保单失效期  modify by czq 
							//-----end  modify by chuzhiqiang --------------------------------

							

							// ///////////////////////////////////////////////////////////////////////////////////////////////////
							// Passenger information
							// ///////////////////////////////////////////////////////////////////////////////////////////////////
							JSONObject pass_info = new JSONObject();
							long passid = rs.getLong("userid");
							pass_info.put("uid", passid);
							pass_info.put("img", ApiGlobal.getAbsoluteURL(rs
									.getString("img")));
							pass_info.put("name", rs.getString("nickname"));
							pass_info.put("gender", rs.getInt("sex"));
							pass_info.put("age", rs.getInt("age"));
							pass_info.put("state", 0);
							pass_info.put("state_desc", "");
							pass_info.put("seat_count", 0);
							pass_info.put("seat_count_desc", "");
							pass_info.put("phone", rs.getString("phone"));

							int goodeval_count = rs.getInt("goodeval_count");
							int eval_count = rs.getInt("eval_count");
							double evgood_rate = eval_count == 0 ? 0
									: goodeval_count * 100 / eval_count;
							String evgood_rate_desc = ApiGlobal.Double2String(
									evgood_rate, 2) + "%";
							pass_info.put("evgood_rate", evgood_rate);
							pass_info.put("evgood_rate_desc", evgood_rate_desc);
							// 2,查询出该乘客参与的已经完成的执行订单数量
							int nCarpoolCount = 0;// 该乘客参与的已经完成的执行订单数量
							szQuery = "SELECT " + "		COUNT(*) AS cnt "
									+ "FROM order_exec_cs " + "WHERE "
									+ "		deleted=0 AND "
									+ "		(status=6 OR status=7) AND "
									+ "		passenger=" + passid + " AND "
									+ "		driver_cancel_time IS NULL AND "
									+ "		pass_cancel_time IS NULL ";
							rs2 = stmt2.executeQuery(szQuery);
							if (rs2.next())
								nCarpoolCount = rs2.getInt("cnt");
							rs2.close();
							pass_info.put("carpool_count", nCarpoolCount);
							pass_info.put("carpool_count_desc", nCarpoolCount
									+ ConstMgr.STR_CI);

							// Person Verify information
							int pverified = rs.getInt("person_verified");
							String verif_desc = "";
							if (pverified == 1)
								verif_desc = ConstMgr.STR_SHENFENYIRENZHENG;
							else
								verif_desc = ConstMgr.STR_SHENFENWEIRENZHENG;
							pass_info.put("verified", pverified);
							pass_info.put("verified_desc", verif_desc);
							// ///////////////////////////////////////////////////////////////////////////////////////////////////

							if (rs.getInt("has_evaluation_driver") == 0) {
								pass_info.put("evaluated", 0);
								pass_info.put("eval_content", "");
								pass_info.put("evaluated_desc",
										ConstMgr.STR_WEIPINGJIA);
							} else {
								pass_info.put("evaluated", rs.getInt("level"));
								pass_info.put("eval_content", ApiGlobal
										.ignoreNull(rs
												.getString("eval_content")));

								if (rs.getInt("level") == 1)
									pass_info.put("evaluated_desc",
											ConstMgr.STR_HAOPING);
								else if (rs.getInt("level") == 2)
									pass_info.put("evaluated_desc",
											ConstMgr.STR_ZHONGPING);
								else if (rs.getInt("level") == 3)
									pass_info.put("evaluated_desc",
											ConstMgr.STR_CHAPING);
								else
									pass_info.put("evaluated_desc", "");
							}

							pass_list.add(pass_info);
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							retdata.put("pass_list", pass_list);
							retdata.put("total_seat", 1);
							retdata.put("left_seat", 1);

							// Price and system fee
							price = rs.getDouble("price");
							if (active == 0)
								sysfee_value = price * ratio / 100;
							else
								sysfee_value = Math.min(integer_, price);
							sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
									+ ApiGlobal.Double2String(sysfee_value, 2)
									+ ConstMgr.STR_DIAN;
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							retdata.put("price", price);
							retdata.put("sysinfo_fee", sysfee_value);
							retdata.put("insu_fee", insu_fee);// modify by
																// chuzhiqiang
							System.out
									.println("-------------------------------------------------------"
											+ insu_fee);
							retdata.put("sysinfo_fee_desc", sysfee_desc);
							retdata.put("start_city", "");
							retdata.put("start_addr",
									rs.getString("start_addr"));
							retdata.put("start_lat", rs.getDouble("start_lat"));
							retdata.put("start_lng", rs.getDouble("start_lng"));
							retdata.put("end_city", "");
							retdata.put("end_addr", rs.getString("end_addr"));
							retdata.put("end_lat", rs.getDouble("end_lat"));
							retdata.put("end_lng", rs.getDouble("end_lng"));
							retdata.put("left_days", "");
							retdata.put("valid_days", "");
							//3,获取中途点信息
							// Get mid points
							szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=0 AND orderid="
									+ rs.getLong("mainID");
							rs2 = stmt2.executeQuery(szQuery);
							while (rs2.next()) {
								JSONObject midpoint = new JSONObject();
								midpoint.put("index", rs2.getInt("point_index"));
								midpoint.put("lat", rs2.getDouble("lat"));
								midpoint.put("lng", rs2.getDouble("lng"));
								midpoint.put("addr", rs2.getString("addr"));

								mid_points.add(midpoint);
							}
							rs2.close();
							retdata.put("mid_points", mid_points);
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							retdata.put("start_time", ApiGlobal
									.Date2StringWithoutSeconds(ApiGlobal
											.String2Date(rs
													.getString("pre_time"))));
							retdata.put("create_time", ApiGlobal.Date2String(
									ApiGlobal.String2Date(rs
											.getString("ti_accept_order")),
									true));
							retdata.put(
									"accept_time",
									ApiGlobal
											.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
													.getString("ti_accept_order"))));

							// State information
							has_eval = rs.getInt("has_evaluation_driver");
							state = rs.getInt("status");
							switch (state) {
							case 1:
								state_desc = ConstMgr.STR_ORDERSTATE_FABU;
								break;
							case 2:
								state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
								break;
							case 3:
								state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
								break;
							case 4:
								state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
								break;
							case 5:
								state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
								break;
							case 6:
								state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
								break;
							case 7: {
								if (has_eval == 1) {
									state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
									state = 8;
								} else {
									state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
								}
							}
								break;
							case 8:
								state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
								state = 9;
								break;
							default:
								state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
							}

							retdata.put("state", state);
							retdata.put("state_desc", state_desc);
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							// statistics information
							statistics.put("total_count", 0);
							statistics.put("total_count_desc", "");
							statistics.put("total_income", 0);
							statistics.put("total_income_desc", "");
							statistics.put("evgood_count", 0);
							statistics.put("evgood_count_desc", "");
							statistics.put("evnormal_count", 0);
							statistics.put("evnormal_count_desc", "");
							statistics.put("evbad_count", 0);
							statistics.put("evbad_count_desc", "");

							retdata.put("statistics", statistics);
							retdata.put("remark", "");
							//retdata.put("insu_info", insu_info);

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						} else {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
							result.retdata = retdata;
						}
					} else if (order_type == 2) // on off order. <orderid> is
												// <order_onoffduty_details>
												// table id
					{
						szQuery = "SELECT "
								+ "		OrderInfo.*,"
								+ "		evaluation_cs.level,"
								+ "		evaluation_cs.msg AS eval_content "

								+ "FROM "
								+ "		(SELECT"
								+ "			order_onoffduty_details.id AS mainID,"
								+ "			order_onoffduty_details.order_num,"
								+ "			order_onoffduty_details.price,"
								+ "			order_onoffduty_details.start_addr,"
								+ "			order_onoffduty_details.start_lat,"
								+ "			order_onoffduty_details.start_lng,"
								+ "			order_onoffduty_details.end_addr,"
								+ "			order_onoffduty_details.end_lat,"
								+ "			order_onoffduty_details.end_lng,"
								+ "			order_onoffduty_details.pre_time,"
								+ "			order_onoffduty_details.status,"
								+ "			order_onoffduty_details.publish_date,"
								+ "			order_onoffduty_details.leftdays,"

								+ "			order_exec_cs.id AS exec_id,"
								+ "			order_exec_cs.passenger,"
								+ "			order_exec_cs.cr_date,"
								+ "			order_exec_cs.ti_accept_order,"
								+ "			order_exec_cs.begin_exec_time,"
								+ "			order_exec_cs.has_evaluation_driver,"
								+ "			order_exec_cs.status AS exec_status,"

								+ "			order_onoffduty_divide.which_days,"

								+ "			order_onoffduty_exec_details.onoffduty_divide_id, "

								+ "			user.id AS userid,"
								+ "			user.img,"
								+ "			user.nickname,"
								+ "			user.sex,"
								+ "			user.phone,"
								+ "			user.person_verified,"
								+ "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age, "

								+ "			(SELECT COUNT(*) "
								+ "			FROM evaluation_cs "
								+ "			WHERE "
								+ "				deleted=0 AND "
								+ "				usertype=2 AND "
								+ "				to_userid=order_exec_cs.passenger) AS eval_count,"

								+ "			(SELECT COUNT(*) "
								+ "			FROM "
								+ "				evaluation_cs "
								+ "			WHERE "
								+ "				deleted=0 AND "
								+ "				usertype=2 AND "
								+ "				level=1 AND "
								+ "				to_userid = order_exec_cs.passenger) AS goodeval_count "

								+ "		FROM order_exec_cs "

								+ "		INNER JOIN order_onoffduty_exec_details "
								+ "		ON order_onoffduty_exec_details.order_cs_id = order_exec_cs.id "

								+ "		INNER JOIN order_onoffduty_divide "
								+ "		ON order_onoffduty_divide.id = order_onoffduty_exec_details.onoffduty_divide_id "

								+ "		INNER JOIN order_onoffduty_details "
								+ "		ON order_onoffduty_details.id = order_onoffduty_divide.orderdetails_id "

								+ "		INNER JOIN user "
								+ "		ON user.id = order_exec_cs.passenger "

								+ "		LEFT JOIN evaluation_cs "
								+ "		ON evaluation_cs.to_userid=order_exec_cs.passenger "

								+ "		WHERE "
								+ "			order_exec_cs.driver="
								+ userid
								+ " AND "
								+ "			order_onoffduty_details.id="
								+ orderid
								+ " AND "
								+ "			order_onoffduty_details.deleted=0 AND "
								+ "			order_onoffduty_divide.deleted=0 AND "
								+ "			order_onoffduty_exec_details.deleted=0 AND "
								+ "			order_exec_cs.deleted=0) AS OrderInfo "

								+ " LEFT JOIN  evaluation_cs "
								+ "ON "
								+ "		evaluation_cs.order_cs_id=OrderInfo.exec_id AND "
								+ "		evaluation_cs.from_userid="
								+ userid
								+ " AND "
								+ "		evaluation_cs.to_userid=OrderInfo.passenger "
								+ "ORDER BY " + "		OrderInfo.cr_date DESC";

						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							long mainID = rs.getLong("mainID");
							retdata.put("uid", mainID);
							retdata.put("order_num", rs.getString("order_num"));

							// ///////////////////////////////////////////////////////////////////////////////////////////////////
							// Passenger information
							// ///////////////////////////////////////////////////////////////////////////////////////////////////
							JSONObject pass_info = new JSONObject();
							long passid = rs.getLong("userid");
							pass_info.put("uid", passid);
							pass_info.put("img", ApiGlobal.getAbsoluteURL(rs
									.getString("img")));
							pass_info.put("name", rs.getString("nickname"));
							pass_info.put("gender", rs.getInt("sex"));
							pass_info.put("age", rs.getInt("age"));
							pass_info.put("state", 0);
							pass_info.put("state_desc", "");
							pass_info.put("seat_count", 0);
							pass_info.put("seat_count_desc", "");
							pass_info.put("phone", rs.getString("phone"));

							int goodeval_count = rs.getInt("goodeval_count");
							int eval_count = rs.getInt("eval_count");
							double evgood_rate = eval_count == 0 ? 0
									: goodeval_count * 100 / eval_count;
							String evgood_rate_desc = ApiGlobal.Double2String(
									evgood_rate, 2) + "%";
							pass_info.put("evgood_rate", evgood_rate);
							pass_info.put("evgood_rate_desc", evgood_rate_desc);

							int nCarpoolCount = 0;
							szQuery = "SELECT COUNT(*) AS cnt "
									+ "		FROM order_exec_cs " + "WHERE "
									+ "		deleted=0 AND "
									+ "		(status=6 OR status=7) AND "
									+ "		passenger=" + passid + " AND "
									+ "		driver_cancel_time IS NULL AND "
									+ "		pass_cancel_time IS NULL ";
							rs2 = stmt2.executeQuery(szQuery);
							if (rs2.next())
								nCarpoolCount = rs2.getInt("cnt");
							rs2.close();
							pass_info.put("carpool_count", nCarpoolCount);
							pass_info.put("carpool_count_desc", nCarpoolCount
									+ ConstMgr.STR_CI);

							// Person Verify information
							int pverified = rs.getInt("person_verified");
							String verif_desc = "";
							if (pverified == 1)
								verif_desc = ConstMgr.STR_SHENFENYIRENZHENG;
							else
								verif_desc = ConstMgr.STR_SHENFENWEIRENZHENG;
							pass_info.put("verified", pverified);
							pass_info.put("verified_desc", verif_desc);
							// ///////////////////////////////////////////////////////////////////////////////////////////////////

							if (rs.getInt("has_evaluation_driver") == 0) {
								pass_info.put("evaluated", 0);
								pass_info.put("eval_content", "");
								pass_info.put("evaluated_desc",
										ConstMgr.STR_WEIPINGJIA);
							} else {
								pass_info.put("evaluated", rs.getInt("level"));
								pass_info.put("eval_content", ApiGlobal
										.ignoreNull(rs
												.getString("eval_content")));

								if (rs.getInt("level") == 1)
									pass_info.put("evaluated_desc",
											ConstMgr.STR_HAOPING);
								else if (rs.getInt("level") == 2)
									pass_info.put("evaluated_desc",
											ConstMgr.STR_ZHONGPING);
								else if (rs.getInt("level") == 3)
									pass_info.put("evaluated_desc",
											ConstMgr.STR_CHAPING);
								else
									pass_info.put("evaluated_desc", "");
							}

							pass_list.add(pass_info);
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							retdata.put("pass_list", pass_list);
							retdata.put("total_seat", 1);
							retdata.put("left_seat", 1);

							// Price and system fee
							price = rs.getDouble("price");
							if (active == 0)
								sysfee_value = price * ratio / 100;
							else
								sysfee_value = Math.min(integer_, price);
							sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
									+ ApiGlobal.Double2String(sysfee_value, 2)
									+ ConstMgr.STR_DIAN;
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							retdata.put("price", price);
							retdata.put("sysinfo_fee", sysfee_value);
							retdata.put("sysinfo_fee_desc", sysfee_desc);
							retdata.put("start_city", "");
							retdata.put("start_addr",
									rs.getString("start_addr"));
							retdata.put("start_lat", rs.getDouble("start_lat"));
							retdata.put("start_lng", rs.getDouble("start_lat"));
							retdata.put("end_city", "");
							retdata.put("end_addr", rs.getString("end_addr"));
							retdata.put("end_lat", rs.getDouble("end_lat"));
							retdata.put("end_lng", rs.getDouble("end_lat"));
							retdata.put("left_days", rs.getString("leftdays"));
							retdata.put("valid_days",
									rs.getString("which_days"));

							// Get mid points
							szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=1 AND orderid="
									+ rs.getLong("mainID");
							rs2 = stmt2.executeQuery(szQuery);
							while (rs2.next()) {
								JSONObject midpoint = new JSONObject();
								midpoint.put("index", rs2.getInt("point_index"));
								midpoint.put("lat", rs2.getDouble("lat"));
								midpoint.put("lng", rs2.getDouble("lng"));
								midpoint.put("addr", rs2.getString("addr"));

								mid_points.add(midpoint);
							}
							rs2.close();
							retdata.put("mid_points", mid_points);
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							int mainOrderState = rs.getInt("status");
							Date dt_start = ApiGlobal.String2Date(rs
									.getString("pre_time"));

							String szStartTime = "";
							if (mainOrderState < 2) {
								szStartTime = ApiGlobal.Date2Time(dt_start);
							} else {
								String szValidDays = "";
								szQuery = "SELECT " + "		which_days " + "FROM "
										+ "		order_onoffduty_divide "
										+ "WHERE " + "		deleted=0 AND "
										+ "		orderdetails_id=" + mainID;

								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next())
									szValidDays = rs2.getString("which_days");
								rs2.close();

								Date dtNext = ApiGlobal.nextValidDay(
										szValidDays.split(","), null, dt_start);
								szStartTime = ApiGlobal
										.Date2StringWithoutSeconds(dtNext);
							}

							retdata.put("start_time", szStartTime);
							retdata.put("create_time", ApiGlobal.Date2String(
									ApiGlobal.String2Date(rs
											.getString("ti_accept_order")),
									true));
							retdata.put(
									"accept_time",
									ApiGlobal
											.Date2StringWithoutSeconds(ApiGlobal.String2Date(rs
													.getString("ti_accept_order"))));

							// State information
							state = rs.getInt("exec_status");
							has_eval = rs.getInt("has_evaluation_driver");
							switch (state) {
							case 1:
								state_desc = ConstMgr.STR_ORDERSTATE_FABU;
								break;
							case 2:
								state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
								break;
							case 3:
								state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
								break;
							case 4:
								state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
								break;
							case 5:
								state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
								break;
							case 6:
								state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
								break;
							case 7: {
								if (has_eval == 1) {
									state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
									state = 8;
								} else {
									state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
								}
							}
								break;
							case 8:
								state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
								state = 9;
								break;
							default:
								state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
							}

							retdata.put("state", state);
							retdata.put("state_desc", state_desc);
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							// Statistics information
							if (rs.getInt("status") == 3) // This onoffduty
															// order is already
															// finished or
															// cancelled.
							{
								// Total count
								int total_count = 0;
								szQuery = "SELECT "
										+ "		COUNT(order_onoffduty_exec_details.order_cs_id) AS cnt "
										+ "FROM order_onoffduty_exec_details "
										+ "INNER JOIN order_exec_cs "
										+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "
										+ "WHERE "
										+ "		order_onoffduty_exec_details.deleted=0 AND "
										+ "		order_exec_cs.pay_time IS NOT NULL AND "
										+ "		order_exec_cs.driver_cancel_time IS NULL AND "
										+ "		order_exec_cs.pass_cancel_time IS NULL AND "
										+ "		order_onoffduty_exec_details.onoffduty_divide_id="
										+ rs.getInt("onoffduty_divide_id");
								rs2 = stmt2.executeQuery(szQuery);
								rs2.next();
								total_count = rs2.getInt("cnt");
								rs2.close();

								String total_count_desc = ConstMgr.STR_GONGCHUCHE
										+ total_count + ConstMgr.STR_CI;

								statistics.put("total_count", total_count);
								statistics.put("total_count_desc",
										total_count_desc);
								// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								// Get all order_exec_cs ids which are related
								// this order.
								String exec_id_array = "";
								szQuery = "SELECT order_cs_id FROM order_onoffduty_exec_details WHERE deleted=0 AND onoffduty_divide_id="
										+ rs.getLong("onoffduty_divide_id");
								rs2 = stmt2.executeQuery(szQuery);
								while (rs2.next()) {
									if (!exec_id_array.equals(""))
										exec_id_array += ",";
									exec_id_array += rs2.getLong("order_cs_id");
								}
								// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								// Total income
								double total_income = 0;
								String total_income_desc = "";

								szQuery = "SELECT SUM(sum) AS total_income FROM ts WHERE deleted=0 AND ts_type=\""
										+ ConstMgr.txcode_driverFee
										+ "\" AND userid=" + userid;
								if (!exec_id_array.equals(""))
									szQuery += " AND order_cs_id IN ("
											+ exec_id_array + ")";

								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next())
									total_income = rs2
											.getDouble("total_income");
								rs2.close();

								total_income_desc = ConstMgr.STR_ZONGSHOURU
										+ ApiGlobal.Double2String(total_income,
												2) + ConstMgr.STR_DIAN;
								statistics.put("total_income", total_income);
								statistics.put("total_income_desc",
										total_income_desc);

								// Evaluations
								int good_eval_count = 0, normal_eval_count = 0, bad_eval_count = 0;
								String good_eval_desc = "", normal_eval_desc = "", bad_eval_desc = "";

								if (exec_id_array.equals(""))
									exec_id_array = "0";

								szQuery = "SELECT "
										+ "		good_info.count AS goodeval_count, "
										+ "		normal_info.count AS normal_count, "
										+ "		bad_info.count AS bad_count "
										+ "FROM" + "		(SELECT "
										+ "			COUNT(*) AS count" + "		FROM "
										+ "			evaluation_cs " + "		WHERE "
										+ "			deleted=0 AND " + "			to_userid="
										+ userid
										+ " AND "
										+ "			usertype=1 AND "
										+ "			order_cs_id IN ("
										+ exec_id_array
										+ ") AND "
										+ "			level=1) AS good_info, "

										+ "		(SELECT "
										+ "			COUNT(*) AS count "
										+ "		FROM "
										+ "			evaluation_cs "
										+ "		WHERE "
										+ "			deleted=0 AND "
										+ "			to_userid="
										+ userid
										+ " AND "
										+ "			usertype=1 AND "
										+ "			order_cs_id IN ("
										+ exec_id_array
										+ ") AND "
										+ "			level=2) AS normal_info, "

										+ "		(SELECT "
										+ "			COUNT(*) AS count "
										+ "		FROM "
										+ "			evaluation_cs "
										+ "		WHERE "
										+ "			deleted=0 AND "
										+ "			to_userid="
										+ userid
										+ " AND "
										+ "			usertype=1 AND "
										+ "			order_cs_id IN ("
										+ exec_id_array
										+ ") AND " + "			level=3) AS bad_info ";
								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next()) {
									good_eval_count = rs2
											.getInt("goodeval_count");
									normal_eval_count = rs2
											.getInt("normal_count");
									bad_eval_count = rs2.getInt("bad_count");
								}
								rs2.close();

								good_eval_desc = ConstMgr.STR_SHOUQU
										+ good_eval_count + ConstMgr.STR_GE
										+ ConstMgr.STR_HAOPING;
								normal_eval_desc = "" + normal_eval_count
										+ ConstMgr.STR_GE
										+ ConstMgr.STR_ZHONGPING;
								bad_eval_desc = "" + bad_eval_count
										+ ConstMgr.STR_GE
										+ ConstMgr.STR_CHAPING;

								statistics.put("evgood_count", good_eval_count);
								statistics.put("evgood_count_desc",
										good_eval_desc);

								statistics.put("evnormal_count",
										normal_eval_count);
								statistics.put("evnormal_count_desc",
										normal_eval_desc);

								statistics.put("evbad_count", bad_eval_count);
								statistics.put("evbad_count_desc",
										bad_eval_desc);
							} else {
								statistics.put("total_count", 0);
								statistics.put("total_count_desc", "");
								statistics.put("total_income", 0);
								statistics.put("total_income_desc", "");
								statistics.put("evgood_count", 0);
								statistics.put("evgood_count_desc", "");
								statistics.put("evnormal_count", 0);
								statistics.put("evnormal_count_desc", "");
								statistics.put("evbad_count", 0);
								statistics.put("evbad_count_desc", "");
							}

							retdata.put("statistics", statistics);
							retdata.put("remark", "");

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
							result.retdata = retdata;
						}
					} else if (order_type == 3) // long distance order.
												// <orderid> is
												// <order_longdistance_details>
												// table id
					{
						szQuery = "SELECT * FROM order_longdistance_details WHERE deleted=0 AND id="
								+ orderid;
						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							SVCOrderLongDistanceDetails long_order_info = SVCOrderLongDistanceDetails
									.decodeFromResultSet(rs);

							retdata.put("uid", long_order_info.id);
							retdata.put("order_num", long_order_info.order_num);

							// ///////////////////////////////////////////////////////////////////////////////////////////////////
							// Passenger information
							// ///////////////////////////////////////////////////////////////////////////////////////////////////

							szQuery = "SELECT "
									+ "		UserCSInfo.*,"

									+ "		eval_info.level,"
									+ "		eval_info.msg AS eval_content "

									+ "FROM "
									+ "		(SELECT "
									+ "			order_longdistance_users_cs.orderdriverlongdistance_id, "
									+ "			order_longdistance_users_cs.seat_num, "

									+ "			order_exec_cs.id AS exec_id, "
									+ "			order_exec_cs.passenger, "
									+ "			order_exec_cs.status, "
									+ "			order_exec_cs.has_evaluation_driver, "
									+ "			order_exec_cs.ti_accept_order, "
									+ "			order_exec_cs.insu_id_driver , "//保险单号  modify by czq

									+ "			user.id AS userid,"
									+ "			user.img,"
									+ "			user.nickname,"
									+ "			user.sex,"
									+ "			user.phone,"
									+ "			user.person_verified,"
									+ "			TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age, "

									+ "			(SELECT COUNT(*) "
									+ "			FROM evaluation_cs "
									+ "			WHERE "
									+ "				deleted=0 AND "
									+ "				usertype=2 AND "
									+ "				to_userid=order_exec_cs.passenger) AS eval_count,"

									+ "			(SELECT COUNT(*) "
									+ "			FROM evaluation_cs "
									+ "			WHERE "
									+ "				deleted=0 AND "
									+ "				usertype=2 AND "
									+ "				level=1 AND "
									+ "				to_userid = order_exec_cs.passenger) AS goodeval_count ,"
									+ "				insurance.*  "// 联结保险表 modify by
															// chuzhiqiang

									+ "		FROM order_longdistance_users_cs "

									+ "		INNER JOIN user "
									+ "		ON user.id = order_longdistance_users_cs.userid "

									+ "		INNER JOIN order_exec_cs "
									+ "		ON order_exec_cs.id = order_longdistance_users_cs.order_exec_cs_id "

									+ "		LEFT JOIN evaluation_cs "
									+ "		ON "
									+ "			evaluation_cs.to_userid = order_exec_cs.passenger AND "
									+ "			evaluation_cs.order_cs_id=order_exec_cs.id "
									+ "			LEFT JOIN insurance ON order_exec_cs.insu_id_driver = insurance.id "//联结查询保险信息 modify by chuzhiqiang

									+ "		WHERE "
									+ "			order_longdistance_users_cs.orderdriverlongdistance_id="
									+ long_order_info.id
									+ " AND "
									+ "			order_longdistance_users_cs.deleted=0 AND "
									+ "			order_exec_cs.deleted=0) AS UserCSInfo "

									+ " LEFT JOIN "
									+ "		(SELECT * "
									+ "		FROM evaluation_cs "
									+ "		WHERE "
									+ "			deleted=0 "
									+ "		ORDER BY ps_date DESC) AS eval_info "
									+ "ON "
									+ "		eval_info.from_userid="
									+ userid
									+ " AND "
									+ "		eval_info.to_userid=UserCSInfo.passenger AND "
									+ "		eval_info.order_cs_id=UserCSInfo.exec_id ";

							boolean all_decided = true;

							has_eval = 1;

							System.out
									.println("SVCOrderService.detailedDriverOrderInfo()---->"
											+ szQuery);
							rs2 = stmt2.executeQuery(szQuery);
							while (rs2.next()) {
								int exec_status = rs2.getInt("status");
								String exec_status_desc = "";
								

								
								
								if (exec_status == 6) // Not paid yet
								{
									exec_status = 4;
									exec_status_desc = ConstMgr.STR_WEIJIESUAN;
								} else if (rs2.getInt("has_evaluation_driver") == 1) // Evaluated
																						// already
								{
									exec_status = 6;
									exec_status_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
								} else if (exec_status == 7) // Not evaluated,
																// paid already
								{
									exec_status = 5;
									exec_status_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
								} else if (exec_status == 5) // Passenger is on
																// car
								{
									exec_status = 1;
									exec_status_desc = ConstMgr.STR_YIYANPIAOSHANGCHE;
								} else if (exec_status == 8) // Passenger
																// cancelled
								{
									exec_status = 3;
									exec_status_desc = ConstMgr.STR_YIQIPIAO;
								} else if (exec_status < 5) // Passenger is not
															// on car yet
								{
									all_decided = false;
									exec_status = 2;
									exec_status_desc = ConstMgr.STR_WEIYANPIAO;
								}

								//------begain  modify by chuzhiqiang ---------------
								
								System.out.println("appl_no----------"+rs2.getString("appl_no"));
								
								
								retdata.put("insu_id", rs2.getLong("insu_id_driver"));//保单id  modify by czq 
								retdata.put("appl_no", rs2.getString("appl_no"));//投保单号  modify by czq 
								retdata.put("total_amount", rs2.getDouble("total_amount"));//保单金额  modify by czq 
								retdata.put("isd_name", rs2.getString("isd_name"));//被保险人姓名  modify by czq 
								retdata.put("isd_id", rs2.getLong("isd_id"));//被保险人id  modify by czq 
								retdata.put("insu_status", rs2.getInt("insu_status"));//保单状态  modify by czq 
								retdata.put("effect_time", ApiGlobal.Date2String(
										ApiGlobal.String2Date(rs2.getString("effect_time")), true));//保单生效期  modify by czq 
								retdata.put("insexpr_date", ApiGlobal.Date2String(
										ApiGlobal.String2Date(rs2.getString("insexpr_date")), true));//保单失效期  modify by czq 
								
								System.out.println(retdata.get("insu_id")+"-"+retdata.get("appl_no")+"-"+retdata.get("isd_name"));
//								insu_info.put("insu_id", rs2.getLong("insu_id"));//保单id  modify by czq 
//								insu_info.put("appl_no", rs2.getString("appl_no"));//投保单号  modify by czq 
//								insu_info.put("total_amount", rs2.getDouble("total_amount"));//保单金额  modify by czq 
//								insu_info.put("isd_name", rs2.getString("isd_name"));//被保险人姓名  modify by czq 
//								insu_info.put("isd_id", rs2.getLong("isd_id"));//被保险人id  modify by czq 
//								insu_info.put("insu_status", rs2.getInt("insu_status"));//保单状态  modify by czq 
//								insu_info.put("effect_time", ApiGlobal.Date2String(
//										ApiGlobal.String2Date(rs2.getString("effect_time")), true));//保单生效期  modify by czq 
//								insu_info.put("insexpr_date", ApiGlobal.Date2String(
//										ApiGlobal.String2Date(rs2.getString("insexpr_date")), true));//保单失效期  modify by czq 
								//--------end  modify by chuzhiqiang-----------
								
								
								JSONObject pass_info = new JSONObject();
								long passid = rs2.getLong("userid");
								pass_info.put("uid", passid);
								pass_info.put("img", ApiGlobal
										.getAbsoluteURL(rs2.getString("img")));
								pass_info
										.put("name", rs2.getString("nickname"));
								pass_info.put("gender", rs2.getInt("sex"));
								pass_info.put("age", rs2.getInt("age"));
								pass_info.put("state", exec_status);
								pass_info.put("state_desc", exec_status_desc);
								pass_info.put("seat_count",
										rs2.getInt("seat_num"));
								pass_info.put("seat_count_desc",
										rs2.getInt("seat_num")
												+ ConstMgr.STR_ZUO);
								pass_info.put("phone", rs2.getString("phone"));

								int goodeval_count = rs2
										.getInt("goodeval_count");
								int eval_count = rs2.getInt("eval_count");
								double evgood_rate = eval_count == 0 ? 0
										: goodeval_count * 100 / eval_count;
								String evgood_rate_desc = ApiGlobal
										.Double2String(evgood_rate, 2) + "%";
								pass_info.put("evgood_rate", evgood_rate);
								pass_info.put("evgood_rate_desc",
										evgood_rate_desc);
								
								

								

								// Get car pool count
								int nCarpoolCount = 0;
								stmt3 = dbConn.createStatement();
								szQuery = "SELECT COUNT(*) AS carpool_count "
										+ "FROM order_exec_cs " + "WHERE "
										+ "		deleted=0 AND "
										+ "		(status=6 OR status=7) AND "
										+ "		pass_cancel_time IS NULL AND "
										+ "		driver_cancel_time IS NULL AND "
										+ "		passenger=" + passid;
								rs3 = stmt3.executeQuery(szQuery);
								if (rs3.next())
									nCarpoolCount = rs3.getInt("carpool_count");
								rs3.close();

								pass_info.put("carpool_count", nCarpoolCount);
								pass_info.put("carpool_count_desc",
										nCarpoolCount + ConstMgr.STR_CI);

								// Person Verify information
								int pverified = rs2.getInt("person_verified");
								String verif_desc = "";
								if (pverified == 1)
									verif_desc = ConstMgr.STR_SHENFENYIRENZHENG;
								else
									verif_desc = ConstMgr.STR_SHENFENWEIRENZHENG;
								pass_info.put("verified", pverified);
								pass_info.put("verified_desc", verif_desc);
								// ///////////////////////////////////////////////////////////////////////////////////////////////////

								if (rs2.getInt("has_evaluation_driver") == 0) {
									pass_info.put("evaluated", 0);
									pass_info.put("eval_content", "");
									pass_info.put("evaluated_desc",
											ConstMgr.STR_WEIPINGJIA);
								} else {
									pass_info.put("evaluated",
											rs2.getInt("level"));
									pass_info
											.put("eval_content",
													ApiGlobal.ignoreNull(rs2
															.getString("eval_content")));

									if (rs2.getInt("level") == 1)
										pass_info.put("evaluated_desc",
												ConstMgr.STR_HAOPING);
									else if (rs2.getInt("level") == 2)
										pass_info.put("evaluated_desc",
												ConstMgr.STR_ZHONGPING);
									else if (rs2.getInt("level") == 3)
										pass_info.put("evaluated_desc",
												ConstMgr.STR_CHAPING);
									else
										pass_info.put("evaluated_desc", "");
								}

								// If not evaluated at least one passenger, this
								// order is considered as not evaluated state
								if (rs2.getInt("has_evaluation_driver") == 0)
									has_eval = 0;

								pass_list.add(pass_info);
							}
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							//retdata.put("insu_info", insu_info);//保险信息
							retdata.put("pass_list", pass_list);
							retdata.put("total_seat", long_order_info.seat_num);
							retdata.put("left_seat", long_order_info.seat_num
									- long_order_info.occupied_num);

							// Price and system fee
							price = long_order_info.price;
							if (active == 0)
								sysfee_value = price * ratio / 100;
							else
								sysfee_value = Math.min(integer_, price);

							// sysfee_value *= long_order_info.seat_num;
							sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
									+ ApiGlobal.Double2String(sysfee_value, 2)
									+ ConstMgr.STR_DIAN;
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							retdata.put("price", price);
							retdata.put("sysinfo_fee", sysfee_value);
							retdata.put("insu_fee", insu_fee);// modify by
																// chuzhiqiang

							retdata.put("sysinfo_fee_desc", sysfee_desc);
							retdata.put("start_city", ApiGlobal.cityCode2Name(
									dbConn, long_order_info.start_city));
							retdata.put("start_addr",
									long_order_info.start_addr);
							retdata.put("start_lat", long_order_info.start_lat);
							retdata.put("start_lng", long_order_info.start_lng);
							retdata.put("end_city", ApiGlobal.cityCode2Name(
									dbConn, long_order_info.end_city));
							retdata.put("end_addr", long_order_info.end_addr);
							retdata.put("end_lat", long_order_info.end_lat);
							retdata.put("end_lng", long_order_info.end_lng);
							retdata.put("left_days", "");
							retdata.put("valid_days", "");
							retdata.put("mid_points", mid_points);
							retdata.put(
									"start_time",
									ApiGlobal
											.Date2StringWithoutSeconds(long_order_info.pre_time));
							retdata.put("create_time", ApiGlobal.Date2String(
									long_order_info.ti_accept_order, true));
							retdata.put("accept_time", "");

							// State information
							state = long_order_info.status;
							switch (state) {
							case 1:
								state_desc = ConstMgr.STR_ORDERSTATE_FABU;
								break;
							case 2:
								state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
								break;
							case 3:
								state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
								break;
							case 4:
								state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
								break;
							case 5:
								state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
								break;
							case 6:
								state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
								break;
							case 7: {
								if (has_eval == 1) {
									state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
									state = 8;
								} else {
									state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
								}
							}
								break;
							case 8:
								state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
								state = 9;
								break;
							default:
								state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
							}

							retdata.put("state", state);
							retdata.put("state_desc", state_desc);
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							// Statistics information
							statistics.put("total_count", 0);
							statistics.put("total_count_desc", "");
							statistics.put("total_income", 0);
							statistics.put("total_income_desc", "");
							statistics.put("evgood_count", 0);
							statistics.put("evgood_count_desc", "");
							statistics.put("evnormal_count", 0);
							statistics.put("evnormal_count_desc", "");
							statistics.put("evbad_count", 0);
							statistics.put("evbad_count_desc", "");

							retdata.put("statistics", statistics);

							// remark information
							{
								String szRemark = "";

								Date start_time = long_order_info.pre_time;

								String szMinLimit = ApiGlobal
										.getEnvValueFromCode(
												dbConn,
												ConstMgr.ENVCODE_ORDERGIVEUPLIMIT);
								int nMinLimit = Integer.parseInt(szMinLimit);

								if (long_order_info.status <= 3) // Driver not
																	// arrived
																	// yet
								{
									szRemark = ConstMgr.STR_YUYUESHIJIAN
											+ ":"
											+ ApiGlobal
													.Date2StringWithoutSeconds(start_time);
								} else if (long_order_info.status == 4) // Driver
																		// arrived
								{
									if (all_decided) {
										szRemark = ConstMgr.STR_ORDERGIVEUP_MSG2;
									} else if (start_time.after(new Date())) {
										szRemark = ConstMgr.STR_YUYUESHIJIAN
												+ ":"
												+ ApiGlobal
														.Date2StringWithoutSeconds(start_time);
									} else {
										Calendar cal = Calendar.getInstance();
										cal.setTime(start_time);
										cal.add(Calendar.MINUTE, nMinLimit);

										if (cal.getTime().before(new Date())) {
											szRemark = ConstMgr.STR_ORDERGIVEUP_MSG1;
										} else {
											// szRemark =
											// ConstMgr.STR_ORDERGIVEUP_MSG4;
											szRemark = ConstMgr.STR_YUYUESHIJIAN
													+ ":"
													+ ApiGlobal
															.Date2StringWithoutSeconds(start_time);
										}
									}
								} else {
									szRemark = ConstMgr.STR_ORDERGIVEUP_MSG5;
								}

								retdata.put("remark", szRemark);
							}

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
							result.retdata = retdata;
						}
					}
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

				if (rs2 != null) {
					try {
						rs2.close();
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

				if (stmt2 != null) {
					try {
						stmt2.close();
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
		}
		System.out.println("retdata is:"+result.retdata.toString());
		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 如果是单次订单： 1,查询该订单详细信息 2,查询该订单对应的执行订单的详细信息 3,查询执行订单对应的车主详细信息
	 * 4,查询执行订单对应的车主的车辆信息 5,查询所有乘客对该车主的评价次数 6,查询所有乘客对该车主的好评次数
	 * 7,这个sql语句是否有问题，driver_cancel_time为什么没有操作符？ 8,获得该条订单的中途点信息
 * 9,获得该条订单的乘客对车主的评价信息
 * 
	 * 如果是长途订单： 略
	 * 
 * @param source
 * @param userid
 * @param orderid
 * @param order_type
 * @param devtoken
 * @return
 */
	public SVCResult detailedPassengerOrderInfo(String source, long userid,
			long orderid, int order_type, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + order_type + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0 || order_type < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null, stmt2 = null;
			ResultSet rs = null, rs2 = null;
			String szQuery = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
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

					int state, has_eval = 0;
					String state_desc = "";

					// Result variables
					JSONObject retdata = new JSONObject();
					JSONObject driverinfo = new JSONObject();
					JSONArray mid_points = new JSONArray();
					JSONObject statistics = new JSONObject();

					if (order_type == 1) // Once order. <orderid> is
											// <order_temp_details> table id
					{
						SVCOrderTempDetails order_info = null;
						SVCOrderExecCS exec_info = null;
						SVCUser driver_info = null;
						SVCUserCar driver_car = null;
						// 1,查询该订单详细信息
						// -----begain modify by
						// chuzhiqiang----------------------
						szQuery = " SELECT "
							  	+" otd.* ,"
							  	+" oec.insu_id_pass,"
							  	+" insu.*"
						  +" FROM "
						  		+" (SELECT * FROM  order_temp_details otd WHERE deleted=0 AND id="+orderid+" AND publisher="+userid+") AS otd  "
						  +" LEFT JOIN order_exec_cs oec ON otd.order_cs_id = oec.id"
						  +" LEFT JOIN insurance insu ON oec.insu_id_pass = insu.id";
						
						//------end modify by chuzhiqiang-------------------
						
						/*szQuery = "SELECT * FROM order_temp_details WHERE deleted=0 AND id="
								+ orderid + " AND publisher=" + userid;*/
						rs = stmt.executeQuery(szQuery);
						if (!rs.next()) {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
							result.retdata = retdata;
						} else {
							order_info = SVCOrderTempDetails
									.decodeFromResultSet(rs);
							
							//------begain modify by chuzhiqiang----------------
							//乘客在查看订单详细信息时，显示保单详细信息
							retdata.put("insu_id", rs.getLong("insu_id_pass"));//保单id  modify by czq 
							retdata.put("appl_no", rs.getString("appl_no"));//投保单号  modify by czq 
							retdata.put("total_amount", rs.getDouble("total_amount"));//保单金额  modify by czq 
							retdata.put("isd_name", rs.getString("isd_name"));//被保险人姓名  modify by czq 
							retdata.put("isd_id", rs.getLong("isd_id"));//被保险人id  modify by czq 
							retdata.put("insu_status", rs.getInt("insu_status"));//保单状态  modify by czq 
							retdata.put("effect_time", ApiGlobal.Date2String(
									ApiGlobal.String2Date(rs
											.getString("effect_time")), true));// 保单生效期
																				// modify
																				// by
																				// czq
							retdata.put("insexpr_date", ApiGlobal.Date2String(
									ApiGlobal.String2Date(rs
											.getString("insexpr_date")), true));// 保单失效期
																				// modify
																				// by
																				// czq
							// ------end modify by chuzhiqiang-----------------

							szQuery = "SELECT * FROM order_exec_cs WHERE deleted=0 AND id="
									+ order_info.order_cs_id;
							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								exec_info = SVCOrderExecCS
										.decodeFromResultSet(rs);
							rs.close();
							// 3,查询执行订单对应的车主详细信息
							if (exec_info != null) {
								szQuery = "SELECT * FROM user WHERE deleted=0 AND id="
										+ exec_info.driver;
								rs = stmt.executeQuery(szQuery);
								if (rs.next())
									driver_info = SVCUser
											.decodeFromResultSet(rs);
								rs.close();
							}
							// 4,查询执行订单对应的车主的车辆信息
							if (driver_info != null) {
								szQuery = "SELECT * FROM user_car WHERE deleted=0 AND userid="
										+ driver_info.id;
								rs = stmt.executeQuery(szQuery);
								if (rs.next())
									driver_car = SVCUserCar
											.decodeFromResultSet(rs);
								rs.close();
							}

							retdata.put("uid", order_info.id);
							retdata.put("order_num", order_info.order_num);

							// ///////////////////////////////////////////////////////////////////////////////////////////////////
							// Driver information
							// ///////////////////////////////////////////////////////////////////////////////////////////////////
							if (driver_info != null) {
								driverinfo.put("uid", driver_info.id);
								driverinfo.put("img", ApiGlobal
										.getAbsoluteURL(driver_info.img));
								driverinfo.put("name", driver_info.nickname);
								driverinfo.put("gender", driver_info.sex);
								driverinfo.put("age", ApiGlobal.getDiffYear(
										driver_info.birthday, new Date()));
								driverinfo.put("phone", driver_info.phone);

								driverinfo
										.put("carimg",
												driver_car == null ? ""
														: ApiGlobal
																.getAbsoluteURL(driver_car.car_img));

								// Driving career
								long career = ApiGlobal.getDiffYear(ApiGlobal
										.String2Date(driver_info.drlicence_ti),
										new Date());
								String drv_career_desc = ConstMgr.STR_JIALING
										+ career + ConstMgr.STR_NIAN;
								driverinfo.put("drv_career", career);
								driverinfo.put("drv_career_desc",
										drv_career_desc);

								// Evaluation information
								if (rs != null)
									rs.close();

								int goodeval_count = 0, eval_count = 0;
								double evgood_rate = 0;
								String evgood_rate_desc = "";
								// 5,查询所有乘客对该车主的评价次数
								szQuery = "SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND to_userid="
										+ driver_info.id;
								rs = stmt.executeQuery(szQuery);
								if (rs.next())
									eval_count = rs.getInt("count");
								rs.close();
								// 6,查询所有乘客对该车主的好评次数
								szQuery = "SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND level=1 AND to_userid="
										+ driver_info.id;
								rs = stmt.executeQuery(szQuery);
								if (rs.next())
									goodeval_count = rs.getInt("count");
								rs.close();

								evgood_rate = eval_count == 0 ? 0
										: goodeval_count * 100 / eval_count;
								evgood_rate_desc = ApiGlobal.Double2String(
										evgood_rate, 2) + "%";

								driverinfo.put("evgood_rate", evgood_rate);
								driverinfo.put("evgood_rate_desc",
										evgood_rate_desc);
								// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								int carpool_count = 0;
								String carpool_count_desc = "";
								// 7,这个sql语句是否有问题，driver_cancel_time为什么没有操作符？
								szQuery = "SELECT "
										+ "		COUNT(*) AS count "
										+ "FROM order_exec_cs "
										+ "WHERE "
										+ "		deleted=0 AND "
										+ "		(status=6 OR status=7) AND "
										+ "		driver_cancel_time IS NOT NULL AND "
										+ "		pass_cancel_time IS NOT NULL AND "
										+ "		driver=" + driver_info.id;
								rs = stmt.executeQuery(szQuery);
								if (rs.next())
									carpool_count = rs.getInt("count");
								carpool_count_desc = carpool_count
										+ ConstMgr.STR_CI;
								rs.close();

								driverinfo.put("carpool_count", carpool_count);
								driverinfo.put("carpool_count_desc",
										carpool_count_desc);

								if (driver_car != null) {
									driverinfo.put("carno",
											driver_car.plate_num);
									driverinfo.put("brand", driver_car.brand);
									driverinfo.put("style", driver_car.style);

									// Get user car info
									int cartype = 1;
									szQuery = "SELECT type FROM car_type WHERE id="
											+ driver_car.car_type_id;
									rs = stmt.executeQuery(szQuery);
									if (rs.next())
										cartype = rs.getInt("type");
									rs.close();
									driverinfo.put("type", cartype);

									driverinfo.put("color", driver_car.color);
								}
							}

							retdata.put("driver_info", driverinfo);
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							retdata.put("price", order_info.price);
							retdata.put("start_city", "");
							retdata.put("end_city", "");
							retdata.put("start_addr", order_info.start_addr);
							retdata.put("start_lat", order_info.start_lat);
							retdata.put("start_lng", order_info.start_lng);
							retdata.put("end_addr", order_info.end_addr);
							retdata.put("end_lat", order_info.end_lat);
							retdata.put("end_lng", order_info.end_lng);
							retdata.put("leftdays", "");
							retdata.put("days", "");
							// 8,获得该条订单的中途点信息
							// Get mid points
							szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=0 AND orderid="
									+ orderid;
							rs2 = stmt2.executeQuery(szQuery);
							while (rs2.next()) {
								JSONObject midpoint = new JSONObject();
								midpoint.put("index", rs2.getInt("point_index"));
								midpoint.put("lat", rs2.getDouble("lat"));
								midpoint.put("lng", rs2.getDouble("lng"));
								midpoint.put("addr", rs2.getString("addr"));

								mid_points.add(midpoint);
							}
							rs2.close();
							retdata.put("mid_points", mid_points);
							retdata.put("password", exec_info == null ? ""
									: exec_info.password);
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							retdata.put(
									"start_time",
									ApiGlobal
											.Date2StringWithoutSeconds(order_info.pre_time));
							retdata.put("create_time", ApiGlobal.Date2String(
									order_info.accept_time, true));
							retdata.put(
									"accept_time",
									ApiGlobal
											.Date2StringWithoutSeconds(exec_info == null ? null
													: exec_info.ti_accept_order));
							retdata.put(
									"startsrv_time",
									ApiGlobal
											.Date2StringWithoutSeconds(exec_info == null ? null
													: exec_info.begin_exec_time));

							// State information
							has_eval = exec_info == null ? 0
									: exec_info.has_evaluation_passenger;
							state = order_info.status;
							switch (state) {
							case 1:
								state_desc = ConstMgr.STR_ORDERSTATE_FABU;
								break;
							case 2:
								state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
								break;
							case 3:
								state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
								break;
							case 4:
								state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
								break;
							case 5:
								state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
								break;
							case 6:
								state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
								break;
							case 7: {
								if (has_eval == 1) {
									state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
									state = 8;
								} else {
									state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
								}
							}
								break;
							case 8:
								state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
								if (exec_info.pass_cancel_time == null)
									state = 9;
								else
									state = 10;
								break;
							default:
								state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
							}

							retdata.put("state", state);
							retdata.put("state_desc", state_desc);
							retdata.put("cancelled_balance", 0);
							retdata.put("cancelled_balance_desc", "");

							// Evaluation information
							if (exec_info == null
									|| exec_info.has_evaluation_passenger == 0) // Passenger
																				// not
																				// evaluted
																				// yet
							{
								retdata.put("evaluated", 0);
								retdata.put("eval_content", "");
								retdata.put("evaluated_desc",
										ConstMgr.STR_WEIPINGJIA);
							} else {
								// 9,获得该条订单的乘客对车主的评价信息
								szQuery = "SELECT * " + "FROM evaluation_cs "
										+ "WHERE " + "		order_cs_id="
										+ exec_info.id + " AND "
										+ "		from_userid=" + userid + " AND "
										+ "		to_userid=" + exec_info.driver
										+ " " + "ORDER BY ps_date LIMIT 0,1";

								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next()) {
									SVCEvaluationCS evalinfo = SVCEvaluationCS
											.decodeFromResultSet(rs2);

									retdata.put("evaluated", evalinfo.level);
									retdata.put("eval_content", evalinfo.msg);
									retdata.put(
											"evaluated_desc",
											evalinfo.level == 1 ? ConstMgr.STR_HAOPING
													: (evalinfo.level == 2 ? ConstMgr.STR_ZHONGPING
															: ConstMgr.STR_CHAPING));
								} else {
									retdata.put("evaluated", 0);
									retdata.put("eval_content", "");
									retdata.put("evaluated_desc",
											ConstMgr.STR_WEIPINGJIA);
								}
								rs2.close();
							}

							// Statistics information
							statistics.put("total_count", 0);
							statistics.put("total_count_desc", "");
							statistics.put("total_income", 0);
							statistics.put("total_income_desc", "");
							statistics.put("evgood_count", 0);
							statistics.put("evgood_count_desc", "");
							statistics.put("evnormal_count", 0);
							statistics.put("evnormal_count_desc", "");
							statistics.put("evbad_count", 0);
							statistics.put("evbad_count_desc", "");

							retdata.put("statistics", statistics);

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						}
					} else if (order_type == 2) // on off order. <orderid> is
												// <order_onoffduty_details>
												// table id
					{
						SVCOrderOnOffDutyDetails order_info = null;
						SVCOrderOnOffDutyGrab grab_info = null;
						SVCUser driver_info = null;
						SVCUserCar driver_car = null;
						SVCOrderExecCS exec_info = null;
						double driverid = 0;
						long divideid = 0;

						szQuery = "SELECT * FROM order_onoffduty_details WHERE "
								+ "		order_onoffduty_details.deleted=0  AND  "
								+ "		order_onoffduty_details.id="
								+ orderid
								+ " AND "
								+ "		order_onoffduty_details.publisher = "
								+ userid;

						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							order_info = SVCOrderOnOffDutyDetails
									.decodeFromResultSet(rs);

							retdata.put("uid", order_info.id);
							retdata.put("order_num", order_info.order_num);
							retdata.put("price", order_info.price);
							retdata.put("start_city", "");
							retdata.put("end_city", "");
							retdata.put("start_addr", order_info.start_addr);
							retdata.put("start_lat", order_info.start_lat);
							retdata.put("start_lng", order_info.start_lng);
							retdata.put("end_addr", order_info.end_addr);
							retdata.put("end_lat", order_info.end_lat);
							retdata.put("end_lng", order_info.end_lng);

							String szStartTime = "";
							if (order_info.status < 2) {
								szStartTime = ApiGlobal
										.Date2Time(order_info.pre_time);
							} else {
								String szValidDays = "";
								szQuery = "SELECT " + "		which_days " + "FROM "
										+ "		order_onoffduty_divide "
										+ "WHERE " + "		deleted=0 AND "
										+ "		orderdetails_id=" + order_info.id;

								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next())
									szValidDays = rs2.getString("which_days");
								rs2.close();

								Date dtNext = ApiGlobal.nextValidDay(
										szValidDays.split(","), null,
										order_info.pre_time);
								szStartTime = ApiGlobal
										.Date2StringWithoutSeconds(dtNext);
							}

							retdata.put("start_time", szStartTime);
							retdata.put("create_time", ApiGlobal.Date2String(
									order_info.ti_accept_order, true));

							if (order_info.status == 1) // Not accepted yet. No
														// driver information
							{
								szQuery = "SELECT order_onoffduty_grab.* "
										+ "FROM order_onoffduty_grab "
										+ "WHERE " + "		deleted=0 AND "
										+ "		status=0 AND order_id="
										+ order_info.id;
								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next())
									grab_info = SVCOrderOnOffDutyGrab
											.decodeFromResultSet(rs2);
								rs2.close();

								if (grab_info != null) {
									szQuery = "SELECT * FROM user WHERE deleted=0 AND id="
											+ grab_info.driverid;
									rs2 = stmt2.executeQuery(szQuery);
									if (rs2.next())
										driver_info = SVCUser
												.decodeFromResultSet(rs2);
									rs2.close();
								}

								if (driver_info != null) {
									szQuery = "SELECT * FROM user_car WHERE deleted=0 AND userid="
											+ driver_info.id;
									rs2 = stmt2.executeQuery(szQuery);
									if (rs2.next())
										driver_car = SVCUserCar
												.decodeFromResultSet(rs2);
									rs2.close();
								}

								if (driver_info == null) // Not accepted yet.
								{
									retdata.put("driver_info", new JSONObject());
									retdata.put("leftdays", order_info.leftdays);
									retdata.put("days", "");
									retdata.put("accept_time", "");
									retdata.put("startsrv_time", "");
									retdata.put("state", 1);
									retdata.put("state_desc",
											ConstMgr.STR_ORDERSTATE_FABU);
								} else // A driver accepted this order
								{
									long drv_career = ApiGlobal
											.getDiffYear(
													ApiGlobal
															.String2Date(driver_info.drlicence_ti),
													new Date());
									String drv_career_desc = ConstMgr.STR_JIALING
											+ drv_career + ConstMgr.STR_NIAN;

									driverinfo.put("uid", driver_info.id);
									driverinfo.put("img", ApiGlobal
											.getAbsoluteURL(driver_info.img));
									driverinfo
											.put("name", driver_info.nickname);
									driverinfo.put("gender", driver_info.sex);
									driverinfo.put("age", ApiGlobal
											.getDiffYear(driver_info.birthday,
													new Date()));
									driverinfo.put("phone", driver_info.phone);
									driverinfo.put("drv_career", drv_career);
									driverinfo.put("drv_career_desc",
											drv_career_desc);
									driverinfo.put("carno",
											driver_car == null ? ""
													: driver_car.plate_num);
									driverinfo.put("brand",
											driver_car == null ? ""
													: driver_car.brand);
									driverinfo.put("style",
											driver_car == null ? ""
													: driver_car.style);
									driverinfo
											.put("carimg",
													driver_car == null ? ""
															: ApiGlobal
																	.getAbsoluteURL(driver_car.car_img));

									// Type setting
									if (driver_car != null) {
										szQuery = "SELECT * FROM car_type WHERE id="
												+ driver_car.car_type_id;
										rs2 = stmt2.executeQuery(szQuery);
										if (rs2.next())
											driverinfo.put("type",
													rs2.getInt("type"));
										else
											driverinfo.put("type", 1);
										rs2.close();
									} else {
										driverinfo.put("type", 1);
									}
									// //////////////////////////////////////////////////////////////////////////////////////////////////

									driverinfo.put("color",
											driver_car == null ? ""
													: driver_car.color);

									// Evaluation rate information
									int eval_count = 0, good_eval_count = 0, goodeval_rate = 0;
									String goodeval_rate_desc = "";

									szQuery = "SELECT "
											+ "		all_info.count AS eval_count,"
											+ "		good_info.count AS good_eval_count "
											+ "FROM "
											+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND to_userid="
											+ driver_info.id
											+ ") AS all_info,"
											+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND level=1 AND to_userid="
											+ driver_info.id + ") AS good_info";
									rs2 = stmt2.executeQuery(szQuery);
									rs2.next(); // Must exist. Otherwise
												// abnormal
									eval_count = rs2.getInt("eval_count");
									good_eval_count = rs2
											.getInt("good_eval_count");
									rs2.close();

									if (eval_count != 0)
										goodeval_rate = good_eval_count * 100
												/ eval_count;
									goodeval_rate_desc = goodeval_rate + "%";

									driverinfo
											.put("evgood_rate", goodeval_rate);
									driverinfo.put("evgood_rate_desc",
											goodeval_rate_desc);
									// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

									// Carpool count
									int carpool_count = 0;
									String carpool_desc = "";
									szQuery = "SELECT "
											+ "		COUNT(*) AS count "
											+ "FROM order_exec_cs "
											+ "WHERE "
											+ "		deleted=0 AND "
											+ "		(status=6 OR status=7) AND "
											+ "		driver_cancel_time IS NOT NULL AND "
											+ "		pass_cancel_time IS NOT NULL AND "
											+ "		driver=" + driver_info.id;
									rs2 = stmt2.executeQuery(szQuery);
									rs2.next(); // Must exist. Otherwise
												// abnormal
									carpool_count = rs2.getInt("count");
									carpool_desc = carpool_count
											+ ConstMgr.STR_CI;
									rs2.close();
									driverinfo.put("carpool_count",
											carpool_count);
									driverinfo.put("carpool_count_desc",
											carpool_desc);
									// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

									retdata.put("driver_info", driverinfo);
									retdata.put("leftdays", order_info.leftdays);
									retdata.put("days", grab_info.days);
									retdata.put(
											"accept_time",
											ApiGlobal
													.Date2StringWithoutSeconds(grab_info.grab_time));
									retdata.put("startsrv_time", "");
									retdata.put("state", 0);
									retdata.put("state_desc",
											ConstMgr.STR_ORDERSTATE_BUTONGYI);
								}

								retdata.put("evaluated", 0);
								retdata.put("eval_content", "");
								retdata.put("evaluated_desc",
										ConstMgr.STR_WEIPINGJIA);

								retdata.put("password", "");

								// Statistics information
								statistics.put("total_count", 0);
								statistics.put("total_count_desc", "");
								statistics.put("total_income", 0);
								statistics.put("total_income_desc", "");
								statistics.put("evgood_count", 0);
								statistics.put("evgood_count_desc", "");
								statistics.put("evnormal_count", 0);
								statistics.put("evnormal_count_desc", "");
								statistics.put("evbad_count", 0);
								statistics.put("evbad_count_desc", "");

								retdata.put("statistics", statistics);
							} else {
								szQuery = "SELECT "
										+ "		order_onoffduty_divide.which_days,"
										+ "		order_onoffduty_divide.orderdetails_id,"
										+ "		order_onoffduty_divide.driver_id, "

										+ "		order_onoffduty_exec_details.onoffduty_divide_id, "
										+ "		order_onoffduty_exec_details.order_cs_id "

										+ "FROM order_onoffduty_exec_details "

										+ "INNER JOIN order_onoffduty_divide "
										+ "ON order_onoffduty_exec_details.onoffduty_divide_id = order_onoffduty_divide.id "

										+ "WHERE "
										+ "		order_onoffduty_divide.deleted=0 AND "
										+ "		order_onoffduty_exec_details.deleted=0 AND "
										+ "		order_onoffduty_divide.orderdetails_id="
										+ order_info.id;

								String exec_ids = "";
								rs2 = stmt2.executeQuery(szQuery);
								while (rs2.next()) {
									if (divideid == 0)
										divideid = rs2
												.getLong("onoffduty_divide_id");

									if (!exec_ids.equals(""))
										exec_ids += ",";
									exec_ids += rs2.getString("order_cs_id");
								}
								rs2.close();

								if (exec_ids.equals(""))
									exec_ids = "0";

								szQuery = "SELECT "
										+ "		order_exec_cs.*,"

										+ "		order_onoffduty_divide.which_days,"
										+ "		order_onoffduty_divide.orderdetails_id,"
										+ "		order_onoffduty_divide.driver_id, "

										+ "		order_onoffduty_exec_details.onoffduty_divide_id, "
										+ "		order_onoffduty_exec_details.order_cs_id, "

										+ "		user.id AS userid,"
										+ "		user.img,"
										+ "		user.nickname,"
										+ "		user.sex,"
										+ "		TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS age,"
										+ "		TIMESTAMPDIFF(YEAR, user.drlicence_ti, CURDATE()) AS drv_career,"
										+ "		user.phone, "

										+ "		user_car.brand,"
										+ "		user_car.style,"
										+ "		user_car.car_img,"
										+ "		user_car.userid,"
										+ "		user_car.color,"
										+ "		user_car.plate_num, "
										+ "		user_car.car_type_id "

										+ "FROM order_exec_cs "

										+ "INNER JOIN order_onoffduty_exec_details "
										+ "ON order_onoffduty_exec_details.order_cs_id = order_exec_cs.id "

										+ "INNER JOIN order_onoffduty_divide "
										+ "ON order_onoffduty_divide.id = order_onoffduty_exec_details.onoffduty_divide_id "

										+ "INNER JOIN user "
										+ "ON user.id = order_exec_cs.driver "

										+ " LEFT JOIN  user_car "
										+ "ON user_car.userid = order_exec_cs.driver "

										+ "WHERE "
										+ "		order_exec_cs.deleted=0 AND "
										+ "		order_exec_cs.id IN ("
										+ exec_ids
										+ ") AND "
										+ "		user.deleted=0 "

										+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

								int drv_career = 0;
								String drv_career_desc = "";
								String brand = "", style = "";
								long car_type_id = 0;

								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next()) // Must exist. If not exist, it
												// is abnormal.
								{
									exec_info = SVCOrderExecCS
											.decodeFromResultSet(rs2);

									drv_career = rs2.getInt("drv_career");
									drv_career_desc = ConstMgr.STR_JIALING
											+ drv_career + ConstMgr.STR_NIAN;
									brand = rs2.getString("brand");
									style = rs2.getString("style");

									driverinfo
											.put("uid", rs2.getLong("userid"));
									driverinfo.put("img", ApiGlobal
											.getAbsoluteURL(rs2
													.getString("img")));
									driverinfo.put("name",
											rs2.getString("nickname"));
									driverinfo.put("gender", rs2.getInt("sex"));
									driverinfo.put("age", rs2.getInt("age"));
									driverinfo.put("phone",
											rs2.getString("phone"));
									driverinfo.put("drv_career", drv_career);
									driverinfo.put("drv_career_desc",
											drv_career_desc);
									driverinfo.put("carno",
											rs2.getString("plate_num"));
									driverinfo.put("carimg", ApiGlobal
											.getAbsoluteURL(rs2
													.getString("car_img")));
									driverinfo.put("brand", brand);
									driverinfo.put("style", style);
									driverinfo.put("color",
											rs2.getString("color"));

									retdata.put("leftdays", order_info.leftdays);
									retdata.put("days",
											rs2.getString("which_days"));
									retdata.put("password",
											exec_info == null ? ""
													: exec_info.password);

									driverid = rs2.getLong("userid");
									car_type_id = rs2.getLong("car_type_id");
									rs2.close();

									// Type setting
									szQuery = "SELECT * FROM car_type WHERE id="
											+ car_type_id;
									rs2 = stmt2.executeQuery(szQuery);
									if (rs2.next())
										driverinfo.put("type",
												rs2.getInt("type"));
									else
										driverinfo.put("type", 1);
									rs2.close();
									// //////////////////////////////////////////////////////////////////////////////////////////////////

									// Evaluation rate information
									int eval_count = 0, good_eval_count = 0, goodeval_rate = 0;
									String goodeval_rate_desc = "";

									szQuery = "SELECT "
											+ "		all_info.count AS eval_count,"
											+ "		good_info.count AS good_eval_count "
											+ "FROM "
											+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND to_userid="
											+ driverid
											+ ") AS all_info,"
											+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND level=1 AND to_userid="
											+ driverid + ") AS good_info";
									rs2 = stmt2.executeQuery(szQuery);
									rs2.next(); // Must exist. Otherwise
												// abnormal
									eval_count = rs2.getInt("eval_count");
									good_eval_count = rs2
											.getInt("good_eval_count");
									rs2.close();

									if (eval_count != 0)
										goodeval_rate = good_eval_count * 100
												/ eval_count;
									goodeval_rate_desc = goodeval_rate + "%";

									driverinfo
											.put("evgood_rate", goodeval_rate);
									driverinfo.put("evgood_rate_desc",
											goodeval_rate_desc);
									// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

									// Carpool count
									int carpool_count = 0;
									String carpool_desc = "";
									szQuery = "SELECT COUNT(*) AS count "
											+ "FROM order_exec_cs " + "WHERE "
											+ "		deleted=0 AND "
											+ "		(status=6 OR status=7) AND "
											+ "		status<>8 AND driver="
											+ driverid;
									rs2 = stmt2.executeQuery(szQuery);
									rs2.next(); // Must exist. Otherwise
												// abnormal
									carpool_count = rs2.getInt("count");
									carpool_desc = carpool_count
											+ ConstMgr.STR_CI;
									rs2.close();
									driverinfo.put("carpool_count",
											carpool_count);
									driverinfo.put("carpool_count_desc",
											carpool_desc);
								} else {
									driverinfo.put("uid", 0);
									driverinfo.put("img", "");
									driverinfo.put("name", "");
									driverinfo.put("gender", 0);
									driverinfo.put("age", 0);
									driverinfo.put("phone", "");
									driverinfo.put("drv_career", "");
									driverinfo.put("drv_career_desc", "");
									driverinfo.put("carno", "");
									driverinfo.put("carimg", "");
									driverinfo.put("brand", "");
									driverinfo.put("style", "");
									driverinfo.put("color", "");

									retdata.put("leftdays", order_info.leftdays);
									retdata.put("days", "");
									retdata.put("password", "");

									driverid = 0;

									driverinfo.put("type", 1);

									driverinfo.put("evgood_rate", 0);
									driverinfo.put("evgood_rate_desc", "");
									driverinfo.put("carpool_count", 0);
									driverinfo.put("carpool_count_desc", "");
								}

								retdata.put("driver_info", driverinfo);
								// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								retdata.put(
										"accept_time",
										exec_info == null ? ""
												: ApiGlobal
														.Date2StringWithoutSeconds(exec_info.ti_accept_order));
								retdata.put(
										"startsrv_time",
										exec_info == null ? ""
												: ApiGlobal
														.Date2StringWithoutSeconds(exec_info.begin_exec_time));

								// State information
								if (rs.getInt("status") == 2) // A driver
																// accepted this
																// order
								{
									state = exec_info == null ? 1
											: exec_info.status;

									switch (state) {
									case 1:
										state_desc = ConstMgr.STR_ORDERSTATE_FABU;
										break;
									case 2:
										state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
										break;
									case 3:
										state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
										break;
									case 4:
										state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
										break;
									case 5:
										state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
										break;
									case 6:
										state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
										break;
									case 7: {
										if (exec_info != null
												&& exec_info.has_evaluation_passenger == 1) {
											state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
											state = 8;
										} else {
											state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
										}
									}
										break;
									case 8:
										state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
										state = 9;
										break;
									default:
										state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
									}

									// Statistics information
									statistics.put("total_count", 0);
									statistics.put("total_count_desc", "");
									statistics.put("total_income", 0);
									statistics.put("total_income_desc", "");
									statistics.put("evgood_count", 0);
									statistics.put("evgood_count_desc", "");
									statistics.put("evnormal_count", 0);
									statistics.put("evnormal_count_desc", "");
									statistics.put("evbad_count", 0);
									statistics.put("evbad_count_desc", "");

									retdata.put("statistics", statistics);
								} else if (rs.getInt("status") == 1) {
									state = 1;
									state_desc = ConstMgr.STR_ORDERSTATE_FABU;

									// Statistics information
									statistics.put("total_count", 0);
									statistics.put("total_count_desc", "");
									statistics.put("total_income", 0);
									statistics.put("total_income_desc", "");
									statistics.put("evgood_count", 0);
									statistics.put("evgood_count_desc", "");
									statistics.put("evnormal_count", 0);
									statistics.put("evnormal_count_desc", "");
									statistics.put("evbad_count", 0);
									statistics.put("evbad_count_desc", "");

									retdata.put("statistics", statistics);
								} else {
									state = 9;
									state_desc = ConstMgr.STR_ORDERSTATE_JIESHUFUWU;

									// Statistics information
									{
										// Total count
										int total_count = 0;
										String total_count_desc = "";

										szQuery = "SELECT COUNT(order_onoffduty_exec_details.order_cs_id) AS cnt "
												+ "FROM order_onoffduty_exec_details "
												+ "INNER JOIN order_exec_cs "
												+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id"
												+ "WHERE "
												+ "		order_onoffduty_exec_detailsdeleted=0 AND "
												+ "		order_exec_cs.pay_time IS NOT NULL AND "
												+ "		order_exec_cs.driver_cancel_time IS NULL AND "
												+ "		order_exec_cs.pass_cancel_time IS NULL AND "
												+ "		order_onoffduty_exec_details.onoffduty_divide_id="
												+ divideid;
										rs2 = stmt.executeQuery(szQuery);
										if (rs2.next())
											total_count = rs2.getInt("cnt");
										rs2.close();

										total_count_desc = ConstMgr.STR_GONGCHUCHE
												+ total_count + ConstMgr.STR_CI;

										statistics.put("total_count",
												total_count);
										statistics.put("total_count_desc",
												total_count_desc);
										// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

										// Total income
										double total_income = 0;
										String total_income_desc = "";

										szQuery = "SELECT "
												+ "		SUM(price) AS total_income "
												+ "FROM order_exec_cs "
												+ "WHERE " + "		deleted=0";

										if (!exec_ids.equals(""))
											szQuery += " AND id IN ("
													+ exec_ids + ")";

										if (exec_ids.equals(""))
											exec_ids = "0";

										rs2 = stmt2.executeQuery(szQuery);
										if (rs2.next())
											total_income = rs2
													.getDouble("total_income");
										rs2.close();

										total_income_desc = ConstMgr.STR_ZONGZHICHU
												+ ApiGlobal.Double2String(
														total_income, 2)
												+ ConstMgr.STR_DIAN;
										statistics.put("total_income",
												total_income);
										statistics.put("total_income_desc",
												total_income_desc);

										// Evaluations
										int normal_eval_count = 0, bad_eval_count = 0, good_eval_count = 0;

										String good_eval_desc = "", normal_eval_desc = "", bad_eval_desc = "";

										szQuery = "SELECT "
												+ "		good_info.count AS goodeval_count, "
												+ "		normal_info.count AS normal_count, "
												+ "		bad_info.count AS bad_count "
												+ "FROM" + "		(SELECT "
												+ "			COUNT(*) AS count"
												+ "		FROM "
												+ "			evaluation_cs "
												+ "		WHERE "
												+ "			deleted=0 AND "
												+ "			to_userid="
												+ userid
												+ " AND "
												+ "			usertype=2 AND "
												+ "			order_cs_id IN ("
												+ exec_ids
												+ ") AND "
												+ "			level=1) AS good_info, "

												+ "		(SELECT "
												+ "			COUNT(*) AS count "
												+ "		FROM "
												+ "			evaluation_cs "
												+ "		WHERE "
												+ "			deleted=0 AND "
												+ "			to_userid="
												+ userid
												+ " AND "
												+ "			usertype=2 AND "
												+ "			order_cs_id IN ("
												+ exec_ids
												+ ") AND "
												+ "			level=2) AS normal_info, "

												+ "		(SELECT "
												+ "			COUNT(*) AS count "
												+ "		FROM "
												+ "			evaluation_cs "
												+ "		WHERE "
												+ "			deleted=0 AND "
												+ "			to_userid="
												+ userid
												+ " AND "
												+ "			usertype=2 AND "
												+ "			order_cs_id IN ("
												+ exec_ids
												+ ") AND "
												+ "			level=3) AS bad_info ";

										rs2 = stmt2.executeQuery(szQuery);
										if (rs2.next()) {
											good_eval_count = rs2
													.getInt("goodeval_count");
											normal_eval_count = rs2
													.getInt("normal_count");
											bad_eval_count = rs2
													.getInt("bad_count");
										}
										rs2.close();

										good_eval_desc = ConstMgr.STR_SHOUQU
												+ good_eval_count
												+ ConstMgr.STR_GE
												+ ConstMgr.STR_HAOPING;
										normal_eval_desc = ""
												+ normal_eval_count
												+ ConstMgr.STR_GE
												+ ConstMgr.STR_ZHONGPING;
										bad_eval_desc = "" + bad_eval_count
												+ ConstMgr.STR_GE
												+ ConstMgr.STR_CHAPING;

										statistics.put("evgood_count",
												good_eval_count);
										statistics.put("evgood_count_desc",
												good_eval_desc);

										statistics.put("evnormal_count",
												normal_eval_count);
										statistics.put("evnormal_count_desc",
												normal_eval_desc);

										statistics.put("evbad_count",
												bad_eval_count);
										statistics.put("evbad_count_desc",
												bad_eval_desc);

										retdata.put("statistics", statistics);
									}
								}

								retdata.put("state", state);
								retdata.put("state_desc", state_desc);

								// Evaluation information
								if (exec_info == null
										|| exec_info.has_evaluation_passenger == 0)
								// Passenger
								// not
								// evaluted
								// yet
								{
									retdata.put("evaluated", 0);
									retdata.put("eval_content", "");
									retdata.put("evaluated_desc",
											ConstMgr.STR_WEIPINGJIA);
								} else {
									szQuery = "SELECT * " + "FROM "
											+ "		evaluation_cs " + "WHERE "
											+ "		order_cs_id=" + exec_info.id
											+ " AND " + "		from_userid="
											+ userid + " AND " + "		to_userid="
											+ exec_info.driver + " "
											+ " ORDER BY ps_date LIMIT 0,1";
									rs2 = stmt2.executeQuery(szQuery);
									if (rs2.next()) {
										SVCEvaluationCS evalinfo = SVCEvaluationCS
												.decodeFromResultSet(rs2);

										retdata.put("evaluated", evalinfo.level);
										retdata.put("eval_content",
												evalinfo.msg);
										retdata.put(
												"evaluated_desc",
												evalinfo.level == 1 ? ConstMgr.STR_HAOPING
														: (evalinfo.level == 2 ? ConstMgr.STR_ZHONGPING
																: ConstMgr.STR_CHAPING));
									} else {
										retdata.put("evaluated", 0);
										retdata.put("eval_content", "");
										retdata.put("evaluated_desc",
												ConstMgr.STR_WEIPINGJIA);
									}
									rs2.close();
								}

							}

							// Get mid points
							szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=1 AND orderid="
									+ order_info.id;
							rs2 = stmt2.executeQuery(szQuery);
							while (rs2.next()) {
								JSONObject midpoint = new JSONObject();
								midpoint.put("index", rs2.getInt("point_index"));
								midpoint.put("lat", rs2.getDouble("lat"));
								midpoint.put("lng", rs2.getDouble("lng"));
								midpoint.put("addr", rs2.getString("addr"));

								mid_points.add(midpoint);
							}
							rs2.close();
							retdata.put("mid_points", mid_points);
							retdata.put("cancelled_balance", 0);
							retdata.put("cancelled_balance_desc", "");
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
						}
					} else if (order_type == 3) // long distance order.
												// <orderid> is
												// <order_longdistance_details>
												// table id
					{
						SVCOrderLongDistanceDetails long_order = ApiGlobal
								.getLongOrderInfo(dbConn, orderid);
						SVCOrderExecCS exec_info = null;
						SVCUser driver_info = null;
						SVCUserCar driver_car = null;
						int nSeatCount = 1;

						if (long_order != null) {
							szQuery = "SELECT "
									+ "		order_exec_cs.*,"
									+ "		order_longdistance_users_cs.id AS usercs_id, "
									+ "		order_longdistance_users_cs.seat_num AS seats_count ,"
									+ "		insurance.*   "// 保险表 modify by
														// chuzhiqiang

									+ "FROM order_exec_cs "

									+ "INNER JOIN order_longdistance_users_cs "
									+ "		ON order_exec_cs.id = order_longdistance_users_cs.order_exec_cs_id "

									+ "LEFT JOIN insurance "// 联结表 modify by
															// chuzhiqiang
									+ "		ON order_exec_cs.insu_id_pass = insurance.id "

									+ "WHERE "
									+ "		order_exec_cs.deleted=0 AND "
									+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
									+ long_order.id
									+ " AND "
									+ "		order_longdistance_users_cs.deleted=0 AND "
									+ "		order_exec_cs.passenger=" + userid;

							System.out
									.println("SVCOrderService.detailedPassengerOrderInfo()-->"
											+ szQuery);
							rs = stmt.executeQuery(szQuery);
							if (rs.next()) {
								exec_info = SVCOrderExecCS
										.decodeFromResultSet(rs);
								nSeatCount = rs.getInt("seats_count");
								
								//------begain  modify by  chuzhiqiang ------------
								//------begain modify by chuzhiqiang----------------
								//乘客在查看订单详细信息时，显示保单详细信息
								retdata.put("insu_id", rs.getLong("insu_id_pass"));//保单id  modify by czq 
								retdata.put("appl_no", rs.getString("appl_no"));//投保单号  modify by czq 
								retdata.put("total_amount", rs.getDouble("total_amount"));//保单金额  modify by czq 
								retdata.put("isd_name", rs.getString("isd_name"));//被保险人姓名  modify by czq 
								retdata.put("isd_id", rs.getLong("isd_id"));//被保险人id  modify by czq 
								retdata.put("insu_status", rs.getInt("insu_status"));//保单状态  modify by czq 
								retdata.put("effect_time", ApiGlobal.Date2String(
										ApiGlobal.String2Date(rs.getString("effect_time")), true));//保单生效期  modify by czq 
								retdata.put("insexpr_date", ApiGlobal.Date2String(
										ApiGlobal.String2Date(rs.getString("insexpr_date")), true));//保单失效期  modify by czq 
								//------end  modify by chuzhiqiang-----------------
								
								//------end modify by chuzhiqiang------------
								
							}
							rs.close();

							if (exec_info != null) {
								driver_info = ApiGlobal.getUserInfoFromUserID(
										dbConn, exec_info.driver);
							}

							if (driver_info != null) {
								szQuery = "SELECT * FROM user_car WHERE deleted=0 AND userid="
										+ driver_info.id;
								rs = stmt.executeQuery(szQuery);
								if (rs.next())
									driver_car = SVCUserCar
											.decodeFromResultSet(rs);
								rs.close();
							}

							retdata.put("uid", long_order.id);
							retdata.put("order_num", long_order.order_num);

							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// Driver Information
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							if (driver_info != null) {
								driverinfo.put("uid", driver_info.id);
								driverinfo.put("img", ApiGlobal
										.getAbsoluteURL(driver_info.img));
								driverinfo.put("name", driver_info.nickname);
								driverinfo.put("gender", driver_info.sex);
								driverinfo.put("age", ApiGlobal.getDiffYear(
										driver_info.birthday, new Date()));
								driverinfo.put("phone", driver_info.phone);
								driverinfo.put("carimg", ApiGlobal
										.getAbsoluteURL(driver_car == null ? ""
												: driver_car.car_img));

								long drv_career = ApiGlobal
										.getDiffYear(
												ApiGlobal
														.String2Date(driver_info.drlicence_ti),
												new Date());
								String drv_career_desc = ConstMgr.STR_JIALING
										+ drv_career + ConstMgr.STR_NIAN;
								driverinfo.put("drv_career", drv_career);
								driverinfo.put("drv_career_desc",
										drv_career_desc);

								// Evaluation rate information
								int eval_count = 0, good_eval_count = 0, goodeval_rate = 0;
								String goodeval_rate_desc = "";

								szQuery = "SELECT "
										+ "		all_info.count AS eval_count,"
										+ "		good_info.count AS good_eval_count "
										+ "FROM "
										+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND to_userid="
										+ driver_info.id
										+ ") AS all_info,"
										+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND level=1 AND to_userid="
										+ driver_info.id + ") AS good_info";
								rs2 = stmt2.executeQuery(szQuery);
								rs2.next(); // Must exist. Otherwise abnormal
								eval_count = rs2.getInt("eval_count");
								good_eval_count = rs2.getInt("good_eval_count");
								rs2.close();

								if (eval_count != 0)
									goodeval_rate = good_eval_count * 100
											/ eval_count;
								goodeval_rate_desc = goodeval_rate + "%";

								driverinfo.put("evgood_rate", goodeval_rate);
								driverinfo.put("evgood_rate_desc",
										goodeval_rate_desc);
								// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								// Carpool count
								int carpool_count = 0;
								String carpool_desc = "";
								szQuery = "SELECT COUNT(*) AS count "
										+ "FROM order_exec_cs " + "WHERE "
										+ "		deleted=0 AND "
										+ "		(status=6 OR status=7) AND "
										+ "		driver=" + driver_info.id;

								rs2 = stmt2.executeQuery(szQuery);
								rs2.next(); // Must exist. Otherwise abnormal
								carpool_count = rs2.getInt("count");
								carpool_desc = carpool_count + ConstMgr.STR_CI;
								rs2.close();

								driverinfo.put("carpool_count", carpool_count);
								driverinfo.put("carpool_count_desc",
										carpool_desc);
								// ////////////////////////////////////////////////////////////////////////////
								driverinfo.put("carno", driver_car == null ? ""
										: driver_car.plate_num);
								driverinfo.put("brand", driver_car == null ? ""
										: driver_car.brand);
								driverinfo.put("style", driver_car == null ? ""
										: driver_car.style);

								// Type setting
								if (driver_car != null) {
									szQuery = "SELECT * FROM car_type WHERE id="
											+ driver_car.car_type_id;
									rs2 = stmt2.executeQuery(szQuery);
									if (rs2.next())
										driverinfo.put("type",
												rs2.getInt("type"));
									else
										driverinfo.put("type", 1);
									rs2.close();
								} else {
									driverinfo.put("type", 1);
								}
								// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								driverinfo.put("color", driver_car == null ? ""
										: driver_car.color);
							} else {
								driverinfo.put("uid", 0);
								driverinfo.put("img", "");
								driverinfo.put("name", "");
								driverinfo.put("gender", 0);
								driverinfo.put("age", 0);
								driverinfo.put("phone", "");
								driverinfo.put("carimg", "");
								driverinfo.put("drv_career", 0);
								driverinfo.put("drv_career_desc", "");
								driverinfo.put("evgood_rate", 0);
								driverinfo.put("evgood_rate_desc", "");
								driverinfo.put("carpool_count", 0);
								driverinfo.put("carpool_count_desc", "");
								driverinfo.put("carno", "");
								driverinfo.put("brand", "");
								driverinfo.put("style", "");
								driverinfo.put("type", 1);
								driverinfo.put("color", "");
							}

							retdata.put("driver_info", driverinfo);

							retdata.put("price",
									(long_order.price * nSeatCount));
							retdata.put("start_city", ApiGlobal.cityCode2Name(
									dbConn, long_order.start_city));
							retdata.put("end_city", ApiGlobal.cityCode2Name(
									dbConn, long_order.end_city));
							retdata.put("start_addr", long_order.start_addr);
							retdata.put("start_lat", long_order.start_lat);
							retdata.put("start_lng", long_order.start_lng);
							retdata.put("end_addr", long_order.end_addr);
							retdata.put("end_lat", long_order.end_lat);
							retdata.put("end_lng", long_order.end_lng);
							retdata.put("leftdays", "");
							retdata.put("days", "");
							retdata.put("mid_points", new JSONArray());
							retdata.put(
									"start_time",
									ApiGlobal
											.Date2StringWithoutSeconds(long_order.pre_time));
							retdata.put("create_time", ApiGlobal.Date2String(
									long_order.ti_accept_order, true));
							retdata.put(
									"accept_time",
									exec_info == null ? ""
											: ApiGlobal
													.Date2StringWithoutSeconds(exec_info.ti_accept_order));
							retdata.put(
									"startsrv_time",
									exec_info == null ? ""
											: ApiGlobal
													.Date2StringWithoutSeconds(exec_info.begin_exec_time));
							retdata.put("password", exec_info == null ? ""
									: exec_info.password);

							state = long_order.status;

							double fDecPrice = 0;

							if (long_order.status != 8
									&& (exec_info != null && exec_info.status == 8)) {

								if (exec_info.pass_cancel_time != null) {
									state_desc = ConstMgr.STR_ORDERSTATE_YITUIPIAO;
									state = 10;
								} else {
									state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
									state = 9;
								}

								// Cancelled order
								// Must calculate decreased balance
								String szTime1 = ApiGlobal
										.getEnvValueFromCode(dbConn,
												ConstMgr.ENVCODE_RETORDER_TIME1);
								String szTime2 = ApiGlobal
										.getEnvValueFromCode(dbConn,
												ConstMgr.ENVCODE_RETORDER_TIME2);
								String szTime3 = ApiGlobal
										.getEnvValueFromCode(dbConn,
												ConstMgr.ENVCODE_RETORDER_TIME3);

								int nTime1 = Integer.parseInt(szTime1);
								int nTime2 = Integer.parseInt(szTime2);
								int nTime3 = Integer.parseInt(szTime3);

								String szRatio1 = ApiGlobal
										.getEnvValueFromCode(
												dbConn,
												ConstMgr.ENVCODE_RETORDER_RATIO1);
								String szRatio2 = ApiGlobal
										.getEnvValueFromCode(
												dbConn,
												ConstMgr.ENVCODE_RETORDER_RATIO2);
								String szRatio3 = ApiGlobal
										.getEnvValueFromCode(
												dbConn,
												ConstMgr.ENVCODE_RETORDER_RATIO3);
								String szRatio4 = ApiGlobal
										.getEnvValueFromCode(
												dbConn,
												ConstMgr.ENVCODE_RETORDER_RATIO4);

								double fRatio1 = Double.parseDouble(szRatio1);
								double fRatio2 = Double.parseDouble(szRatio2);
								double fRatio3 = Double.parseDouble(szRatio3);
								double fRatio4 = Double.parseDouble(szRatio4);

								long nDiffDay = ApiGlobal.getDiffDay(
										exec_info.pass_cancel_time,
										exec_info.begin_exec_time);
								double fPrice = exec_info.price;
								if (nDiffDay < nTime1)
									fDecPrice = fPrice * fRatio1 / 100;
								else if (nDiffDay < nTime2)
									fDecPrice = fPrice * fRatio2 / 100;
								else if (nDiffDay < nTime3)
									fDecPrice = fPrice * fRatio3 / 100;
								else
									fDecPrice = fPrice * fRatio4 / 100;
							} else {
								switch (state) {
								case 1:
									state_desc = ConstMgr.STR_ORDERSTATE_FABU;
									break;
								case 2:
									state_desc = ConstMgr.STR_ORDERSTATE_CHENGJIAO;
									break;
								case 3:
									state_desc = ConstMgr.STR_ORDERSTATE_KAISHIZHIXING;
									break;
								case 4:
									state_desc = ConstMgr.STR_ORDERSTATE_CHEZHUDAODA;
									break;
								case 5:
									state_desc = ConstMgr.STR_ORDERSTATE_CHENGKESHANGCHE;
									break;
								case 6:
									state_desc = ConstMgr.STR_ORDERSTATE_ZHIXINGWANCHENG;
									break;
								case 7: {
									if (exec_info != null
											&& exec_info.has_evaluation_passenger == 1) {
										state_desc = ConstMgr.STR_ORDERSTATE_YIPINGJIA;
										state = 8;
									} else {
										state_desc = ConstMgr.STR_ORDERSTATE_YIJIESUAN;
									}
								}
									break;
								case 8:
									state_desc = ConstMgr.STR_ORDERSTATE_YIGUANBI;
									state = 9;
									break;
								default:
									state_desc = ConstMgr.STR_ORDERSTATE_WEIDING;
								}
							}

							retdata.put("state", state);
							retdata.put("state_desc", state_desc);

							retdata.put("cancelled_balance", fDecPrice);
							String szDesc = "";
							if (fDecPrice == 0)
								szDesc = ConstMgr.STR_MEIYOUTUIPIAOLVDIAN;
							else
								szDesc = ConstMgr.STR_YIZHIFUTUIPIAOLVDIAN
										+ fDecPrice + ConstMgr.STR_DIAN;
							retdata.put("cancelled_balance_desc", szDesc);

							if (exec_info == null
									|| exec_info.has_evaluation_passenger == 0) {
								retdata.put("evaluated", 0);
								retdata.put("eval_content", "");
								retdata.put("evaluated_desc",
										ConstMgr.STR_WEIPINGJIA);
							} else {
								SVCEvaluationCS eval = null;

								szQuery = "SELECT " + "		* " + "FROM "
										+ "		evaluation_cs " + "WHERE "
										+ "		order_cs_id=" + exec_info.id
										+ " AND " + "		from_userid=" + userid
										+ " AND " + "		to_userid="
										+ exec_info.driver + " "
										+ " ORDER BY ps_date LIMIT 0,1";

								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next())
									eval = SVCEvaluationCS
											.decodeFromResultSet(rs2);
								rs2.close();

								retdata.put("evaluated", eval == null ? 0
										: eval.level);
								retdata.put("eval_content", eval == null ? ""
										: eval.msg);
								retdata.put("evaluated_desc",
										eval == null ? ConstMgr.STR_WEIPINGJIA
												: ConstMgr.STR_YIPINGJIA);
							}

							// Statistics information 这些都是上下班需要的，长途不需要这些字段，设为默认值
							statistics.put("total_count", 0);
							statistics.put("total_count_desc", "");
							statistics.put("total_income", 0);
							statistics.put("total_income_desc", "");
							statistics.put("evgood_count", 0);
							statistics.put("evgood_count_desc", "");
							statistics.put("evnormal_count", 0);
							statistics.put("evnormal_count_desc", "");
							statistics.put("evbad_count", 0);
							statistics.put("evbad_count_desc", "");

							retdata.put("statistics", statistics);

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
						}
					}
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

				if (rs2 != null) {
					try {
						rs2.close();
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

				if (stmt2 != null) {
					try {
						stmt2.close();
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,根据id获取长途订单 2,在该长途订单中，获取指定乘客的执行订单信息 3,获取envirment表中配置的退票时间点(提前多少天退票扣多少钱)
	 * 4,获取envirment表中配置的退票扣点比例(提前多少天退票扣多少钱) 5,组装退票规则字符串:
	 * (1)发车前ret_time1天退票，收取ret_ratio1%
	 * (2)发车前ret_time1至ret_time2天退票，收取ret_ratio2%
	 * (3)发车前ret_time2至ret_time3天退票，收取ret_ratio3%
	 * (4)如果ret_ratio4==0,则发车前ret_time3天以上退票
	 * ，不收取退票绿点;如果ret_ratio4不等于0，则发车前ret_time3天以上退票，收取ret_ratio4% 6，计算距离出发时间还有几天
	 * 7,根据距离出发时间天数，计算出扣点比例 8,生成实际的扣点数 9,生成扣了多少点的提示说明问题
	 * 
 * @param source
 * @param userid
 * @param orderid
 * @param devtoken
 * @return
 */
	public SVCResult longOrderCancelInfo(String source, long userid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
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
			if (!userinfo.device_token.equals(devtoken)) // Device
															// token not
															// match
			{
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			stmt = dbConn.createStatement();

			JSONObject retdata = new JSONObject();
			SVCOrderLongDistanceDetails long_order = null;
			double fPrice = 0;
			// 1,根据id获取长途订单
			szQuery = "SELECT * FROM order_longdistance_details WHERE deleted=0 AND id="
					+ orderid;

			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) // Order doesn't exist. Abnormal situation
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				result.retdata = retdata;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			long_order = SVCOrderLongDistanceDetails.decodeFromResultSet(rs);
			rs.close();
			// 2,在该长途订单中，获取指定乘客的执行订单信息
			szQuery = "SELECT "
					+ "		order_longdistance_users_cs.*,"

					+ "		order_exec_cs.passenger, "
					+ "		order_exec_cs.driver, "
					+ "		order_exec_cs.from_, "
					+ "		order_exec_cs.to_, "
					+ "		order_exec_cs.price, "
					+ "		order_exec_cs.pre_time, "
					+ "		order_exec_cs.cr_date, "
					+ "		order_exec_cs.passenger "

					+ "FROM order_longdistance_users_cs "

					+ "INNER JOIN order_exec_cs "
					+ "ON order_exec_cs.id=order_longdistance_users_cs.order_exec_cs_id "

					+ "WHERE "
					+ "		order_longdistance_users_cs.deleted=0 AND "
					+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
					+ long_order.id + " AND "
					+ "		order_exec_cs.deleted=0 AND "
					+ "		order_exec_cs.passenger=" + userid;

			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) // Exec info not exist. Abnormal
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				result.retdata = retdata;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			fPrice = long_order.price * rs.getInt("seat_num");

			String price_desc = ApiGlobal.Double2String(fPrice, 2)
					+ ConstMgr.STR_DIAN;

			retdata.put("order_id", long_order.id);
			retdata.put("order_num", long_order.order_num);
			retdata.put("start_addr", rs.getString("from_"));
			retdata.put("end_addr", rs.getString("to_"));
			retdata.put("price", fPrice);
			retdata.put("price_desc", price_desc);
			retdata.put("create_time",
					ApiGlobal.Date2String(long_order.ps_time, true));
			retdata.put("start_time",
					ApiGlobal.Date2StringWithoutSeconds(long_order.pre_time));

			// Balance rule
			JSONArray arrBalRules = new JSONArray();
			JSONObject rule_item1 = new JSONObject();
			JSONObject rule_item2 = new JSONObject();
			JSONObject rule_item3 = new JSONObject();
			JSONObject rule_item4 = new JSONObject();
			// 3,获取envirment表中配置的退票时间点(提前多少天退票扣多少钱)
			String ret_time1 = ApiGlobal.getEnvValueFromCode(dbConn,
					ConstMgr.ENVCODE_RETORDER_TIME1);
			String ret_time2 = ApiGlobal.getEnvValueFromCode(dbConn,
					ConstMgr.ENVCODE_RETORDER_TIME2);
			String ret_time3 = ApiGlobal.getEnvValueFromCode(dbConn,
					ConstMgr.ENVCODE_RETORDER_TIME3);

			int nRetTime1 = 0, nRetTime2 = 0, nRetTime3 = 0;
			double ratio1 = 0, ratio2 = 0, ratio3 = 0, ratio4 = 0;

			try {
				nRetTime1 = Integer.parseInt(ret_time1);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				nRetTime2 = Integer.parseInt(ret_time2);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				nRetTime3 = Integer.parseInt(ret_time3);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// 4,获取envirment表中配置的退票扣点比例(提前多少天退票扣多少钱)
			String ret_ratio1 = ApiGlobal.getEnvValueFromCode(dbConn,
					ConstMgr.ENVCODE_RETORDER_RATIO1);
			String ret_ratio2 = ApiGlobal.getEnvValueFromCode(dbConn,
					ConstMgr.ENVCODE_RETORDER_RATIO2);
			String ret_ratio3 = ApiGlobal.getEnvValueFromCode(dbConn,
					ConstMgr.ENVCODE_RETORDER_RATIO3);
			String ret_ratio4 = ApiGlobal.getEnvValueFromCode(dbConn,
					ConstMgr.ENVCODE_RETORDER_RATIO4);

			try {
				ratio1 = Double.parseDouble(ret_ratio1);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				ratio2 = Double.parseDouble(ret_ratio2);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				ratio3 = Double.parseDouble(ret_ratio3);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				ratio4 = Double.parseDouble(ret_ratio4);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// 5,组装退票规则字符串:
			// (1)发车前ret_time1天退票，收取ret_ratio1%
			// (2)发车前ret_time1至ret_time2天退票，收取ret_ratio2%
			// (3)发车前ret_time2至ret_time3天退票，收取ret_ratio3%
			// (4)如果ret_ratio4==0,则发车前ret_time3天以上退票，不收取退票绿点;如果ret_ratio4不等于0，则发车前ret_time3天以上退票，收取ret_ratio4%

			String retmsg1 = ConstMgr.STR_FACHEQIAN + ret_time1
					+ ConstMgr.STR_TIANNEITUIPIAO + ", " + ConstMgr.STR_SHOUQU
					+ ret_ratio1 + "%";
			String retmsg2 = ConstMgr.STR_FACHEQIAN + ret_time1 + "-"
					+ ret_time2 + ConstMgr.STR_TIANNEITUIPIAO + ", "
					+ ConstMgr.STR_SHOUQU + ret_ratio2 + "%";
			String retmsg3 = ConstMgr.STR_FACHEQIAN + ret_time2 + "-"
					+ ret_time3 + ConstMgr.STR_TIANNEITUIPIAO + ", "
					+ ConstMgr.STR_SHOUQU + ret_ratio3 + "%";
			String retmsg4 = "";

			if (ret_ratio4.equals("0"))
				retmsg4 = ConstMgr.STR_FACHEQIAN + ret_time3
						+ ConstMgr.STR_TIANYISHANGTUIPIAO + ", "
						+ ConstMgr.STR_BUSHOUQUTUIPIAOLVDIAN;
			else
				retmsg4 = ConstMgr.STR_FACHEQIAN + ret_time3
						+ ConstMgr.STR_TIANYISHANGTUIPIAO + ", "
						+ ConstMgr.STR_SHOUQU + ret_ratio4 + "%";

			rule_item1.put("rule", retmsg1);
			arrBalRules.add(rule_item1);

			rule_item2.put("rule", retmsg2);
			arrBalRules.add(rule_item2);

			rule_item3.put("rule", retmsg3);
			arrBalRules.add(rule_item3);

			rule_item4.put("rule", retmsg4);
			arrBalRules.add(rule_item4);

			retdata.put("balance_rule", arrBalRules);
			// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// 6，计算距离出发时间还有几天
			int nDiffDay = (int) ApiGlobal.getDiffDay(new Date(),
					long_order.pre_time);
			double fRate = 0, decBalance = 0;
			// 7根据距离出发时间天数，计算出扣点比例
			if (nDiffDay < nRetTime1) {
				fRate = ratio1;
			} else if (nDiffDay < nRetTime2) {
				fRate = ratio2;
			} else if (nDiffDay < nRetTime3) {
				fRate = ratio3;
			} else {
				fRate = ratio4;
			}
			// 8,生成实际的扣点数
			decBalance = fPrice * fRate / 100;

			String diffday_desc = "";
			String balance_desc = "";
			// 9,生成扣了多少点的提示说明问题
			if (nDiffDay < nRetTime1) {
				diffday_desc = ConstMgr.STR_DANGQIANSHIJIANJUFACHE + "<"
						+ nRetTime1 + ConstMgr.STR_TIAN;
				balance_desc = ConstMgr.STR_BCTPJCSTPLD
						+ ((int) (decBalance * 10) / 10.0) + ConstMgr.STR_DIAN;
			} else if (nDiffDay < nRetTime2) {
				diffday_desc = ConstMgr.STR_DANGQIANSHIJIANJUFACHE + "<"
						+ nRetTime2 + ConstMgr.STR_TIAN;
				balance_desc = ConstMgr.STR_BCTPJCSTPLD
						+ ((int) (decBalance * 10) / 10.0) + ConstMgr.STR_DIAN;
			} else if (nDiffDay < nRetTime3) {
				diffday_desc = ConstMgr.STR_DANGQIANSHIJIANJUFACHE + "<"
						+ nRetTime3 + ConstMgr.STR_TIAN;
				balance_desc = ConstMgr.STR_BCTPJCSTPLD
						+ ((int) (decBalance * 10) / 10.0) + ConstMgr.STR_DIAN;
			} else {
				diffday_desc = ConstMgr.STR_DANGQIANSHIJIANJUFACHE + ">"
						+ nRetTime3 + ConstMgr.STR_TIAN;
				balance_desc = ConstMgr.STR_BCTPJCSTPLD
						+ ((int) (decBalance * 10) / 10.0) + ConstMgr.STR_DIAN;
			}

			retdata.put("time_interval_desc", diffday_desc);
			retdata.put("balance_desc", balance_desc);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
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
	 * 1,根据orderid获取订单详情 2,获取平台信息费分成方式，是固定金额还是比例，值是多少
	 * 3，获取乘客信息:id,图片，姓名,年龄，性别，是否通过身份验证，被评价总数，好评率，作为乘客拼车的次数 4,获取中途点信息
	 * 
 * @param source
 * @param userid
 * @param orderid
 * @param order_type
 * @param devtoken
 * @return
 */
	public SVCResult acceptableInCityOrderDetailInfo(String source,
			long userid, long orderid, int order_type, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + order_type + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0 || order_type < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null, stmt2 = null;
			ResultSet rs = null, rs2 = null;
			String szQuery = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {

					stmt = dbConn.createStatement();
					stmt2 = dbConn.createStatement();

					JSONObject retdata = new JSONObject();
					JSONArray mid_points = new JSONArray();

					double sysfee_value = 0, price = 0;
					String sysfee_desc = "", price_desc = "";
					double ratio = 0, integer_ = 0, active = 0;

					SVCGlobalParams global_params = null;
					SVCCity city = null;

					if (order_type == 1) // Once order
					{
						// 1,根据orderid获取订单详情
						szQuery = "SELECT "
								+ "		order_temp_details.*,"

								+ "		user.id AS pass_id,"
								+ "		user.nickname AS pass_name,"
								+ "		TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS pass_age,"
								+ "		user.sex AS pass_gender,"
								+ "		user.img AS pass_img,"
								+ "		user.person_verified AS verified "

								+ "FROM order_temp_details "

								+ "INNER JOIN user "
								+ "ON user.id=order_temp_details.publisher "

								+ "WHERE " + "		user.deleted=0 AND "
								+ "		order_temp_details.deleted=0 AND "
								+ "		order_temp_details.id=" + orderid;

						rs = stmt.executeQuery(szQuery);

						if (rs.next()) {
							// 2,获取平台信息费分成方式，是固定金额还是比例，值是多少
							// Calculate
							szQuery = "SELECT * FROM global_params WHERE deleted=0";
							rs2 = stmt2.executeQuery(szQuery);
							// Global parameters must exist. If not exist, it is
							// abnormal
							rs2.next();
							global_params = SVCGlobalParams
									.decodeFromResultSet(rs2);
							rs2.close();
							double insu_fee = 0;
							insu_fee = global_params.insu_fee;// maodify by
																// chuzhiqiang

							szQuery = "SELECT * FROM city WHERE code=\""
									+ rs.getString("order_city")
									+ "\" AND deleted=0";
							rs2 = stmt2.executeQuery(szQuery);
							if (rs2.next()) {
								city = SVCCity.decodeFromResultSet(rs2);

								if (city.active < 0) // City parameters are not
														// set. Use global
														// params
								{
									ratio = global_params.ratio;
									integer_ = global_params.integer_;
									active = global_params.active;
								} else // City parameters are set. Use city
										// params
								{
									ratio = city.ratio;
									integer_ = city.integer_;
									active = city.active;
								}
							} else {
								ratio = global_params.ratio;
								integer_ = global_params.integer_;
								active = global_params.active;
							}
							rs2.close();
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// 3，获取乘客信息:id,图片，姓名,年龄，性别，是否通过身份验证，被评价总数，好评率，作为乘客拼车的次数
							JSONObject pass_info = new JSONObject();
							{
								pass_info.put("pass_id", rs.getLong("pass_id"));// id
								pass_info.put("pass_img", ApiGlobal
										.getAbsoluteURL(rs
												.getString("pass_img")));// 图片
								pass_info.put("pass_name",
										rs.getString("pass_name"));// 乘客姓名
								pass_info
										.put("pass_age", rs.getInt("pass_age"));// 乘客年龄
								pass_info.put("pass_gender",
										rs.getInt("pass_gender"));// 乘客性别

								pass_info
										.put("verified", rs.getInt("verified"));// 乘客是否通过身份验证
								if (rs.getInt("verified") == 1)
									pass_info.put("verified_desc",
											ConstMgr.STR_SHENFENYIRENZHENG);
								else
									pass_info.put("verified_desc",
											ConstMgr.STR_SHENFENWEIRENZHENG);

								// Evaluation information 乘客被评价的总数，以及好评率
								szQuery = "SELECT "
										+ "		all_info.count AS eval_count,"
										+ "		good_info.count AS goodeval_count "
										+ "FROM"
										+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
										+ rs.getLong("pass_id")
										+ " AND usertype=2) AS all_info, "
										+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
										+ rs.getLong("pass_id")
										+ " AND usertype=2 AND level=1) AS good_info ";

								int goodeval_count = 0, eval_count = 0;
								double goodeval_rate = 0;
								String goodeval_rate_desc = "";

								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next()) {
									goodeval_count = rs2
											.getInt("goodeval_count");
									eval_count = rs2.getInt("eval_count");
								}
								rs2.close();

								if (eval_count != 0)
									goodeval_rate = goodeval_count * 100
											/ eval_count;
								goodeval_rate_desc = ApiGlobal.Double2String(
										goodeval_rate, 2) + "%";

								pass_info.put("evgood_rate", goodeval_rate);
								pass_info.put("evgood_rate_desc",
										goodeval_rate_desc);
								// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								// Carpool count
								int carpool_count = 0;
								String carpool_desc = "";
								// 乘客总共拼车的次数
								szQuery = "SELECT COUNT(*) AS count "
										+ "FROM order_exec_cs " + "WHERE "
										+ "		deleted=0 AND "
										+ "		(status=6 OR status=7) AND "
										+ "		passenger="
										+ rs.getLong("pass_id");

								rs2 = stmt2.executeQuery(szQuery);
								rs2.next(); // Must exist. Otherwise abnormal
								carpool_count = rs2.getInt("count");
								carpool_desc = carpool_count + ConstMgr.STR_CI;
								rs2.close();

								pass_info.put("carpool_count", carpool_count);
								pass_info.put("carpool_count_desc",
										carpool_desc);
								// ////////////////////////////////////////////////////////////////////////////
							}
							retdata.put("pass_info", pass_info);

							retdata.put("start_addr",
									rs.getString("start_addr"));
							retdata.put("start_lat", rs.getDouble("start_lat"));
							retdata.put("start_lng", rs.getDouble("start_lng"));
							retdata.put("end_addr", rs.getString("end_addr"));
							retdata.put("end_lat", rs.getDouble("end_lat"));
							retdata.put("end_lng", rs.getDouble("end_lng"));
							retdata.put("start_time", ApiGlobal
									.Date2StringWithoutSeconds(ApiGlobal
											.String2Date(rs
													.getString("pre_time"))));
							// 4,获取中途点信息
							// Get mid points
							szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=0 AND orderid="
									+ orderid;
							rs2 = stmt2.executeQuery(szQuery);
							while (rs2.next()) {
								JSONObject midpoint = new JSONObject();

								midpoint.put("index", rs2.getInt("point_index"));
								midpoint.put("lat", rs2.getDouble("lat"));
								midpoint.put("lng", rs2.getDouble("lng"));
								midpoint.put("addr", rs2.getString("addr"));

								mid_points.add(midpoint);
							}
							rs2.close();
							retdata.put("mid_points", mid_points);
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							price = rs.getDouble("price");
							price_desc = "" + price + ConstMgr.STR_DIAN;

							if (active == 0)
								sysfee_value = price * ratio / 100;
							else
								sysfee_value = Math.min(integer_, price);
							sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
									+ ApiGlobal.Double2String(sysfee_value, 2)
									+ ConstMgr.STR_DIAN;

							retdata.put("price", price);
							retdata.put("price_desc", price_desc);
							retdata.put("sysinfo_fee", sysfee_value);
							retdata.put("insu_fee", insu_fee*2);// 一个执行订单对应两个保单  modify by  chuzhiqiang
							System.out.println("---==-=-=-=-=-=-=-=-=-=-=-"
									+ insu_fee);
							retdata.put("sysinfo_fee_desc", sysfee_desc);
							retdata.put("valid_days", "");

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
							result.retmsg = ConstMgr.ErrMsg_AlreadyAcceptedOrCancelled;
							result.retdata = retdata;
						}
					} else // On off order
					{
						szQuery = "SELECT "
								+ "		order_onoffduty_details.*,"

								+ "		user.id AS pass_id,"
								+ "		user.nickname AS pass_name,"
								+ "		TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS pass_age,"
								+ "		user.sex AS pass_gender,"
								+ "		user.img AS pass_img,"
								+ "		user.person_verified AS verified "

								+ "FROM order_onoffduty_details "

								+ "INNER JOIN user "
								+ "ON user.id=order_onoffduty_details.publisher "

								+ "WHERE " + "		user.deleted=0 AND "
								+ "		order_onoffduty_details.deleted=0 AND "
								+ "		order_onoffduty_details.id=" + orderid;

						rs = stmt.executeQuery(szQuery);

						if (rs.next()) {
							// Calculate
							szQuery = "SELECT * FROM global_params WHERE deleted=0";
							rs2 = stmt2.executeQuery(szQuery);
							// Global parameters must exist. If not exist, it is
							// abnormal
							rs2.next();
							global_params = SVCGlobalParams
									.decodeFromResultSet(rs2);
							rs2.close();

							szQuery = "SELECT * FROM city WHERE code=\""
									+ rs.getString("order_city")
									+ "\" AND deleted=0";
							rs2 = stmt2.executeQuery(szQuery);
							if (rs2.next()) {
								city = SVCCity.decodeFromResultSet(rs2);

								if (city.active < 0) // City parameters are not
														// set. Use global
														// params
								{
									ratio = global_params.ratio;
									integer_ = global_params.integer_;
									active = global_params.active;
								} else // City parameters are set. Use city
										// params
								{
									ratio = city.ratio;
									integer_ = city.integer_;
									active = city.active;
								}
							} else {
								ratio = global_params.ratio;
								integer_ = global_params.integer_;
								active = global_params.active;
							}
							rs2.close();
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							JSONObject pass_info = new JSONObject();
							{
								pass_info.put("pass_id", rs.getLong("pass_id"));
								pass_info.put("pass_img", ApiGlobal
										.getAbsoluteURL(rs
												.getString("pass_img")));
								pass_info.put("pass_name",
										rs.getString("pass_name"));
								pass_info
										.put("pass_age", rs.getInt("pass_age"));
								pass_info.put("pass_gender",
										rs.getInt("pass_gender"));

								pass_info
										.put("verified", rs.getInt("verified"));
								if (rs.getInt("verified") == 1)
									pass_info.put("verified_desc",
											ConstMgr.STR_SHENFENYIRENZHENG);
								else
									pass_info.put("verified_desc",
											ConstMgr.STR_SHENFENWEIRENZHENG);

								// Evaluation information
								szQuery = "SELECT "
										+ "		all_info.count AS eval_count,"
										+ "		good_info.count AS goodeval_count "
										+ "FROM"
										+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
										+ rs.getLong("pass_id")
										+ " AND usertype=2) AS all_info, "
										+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
										+ rs.getLong("pass_id")
										+ " AND usertype=2 AND level=1) AS good_info ";

								int goodeval_count = 0, eval_count = 0;
								double goodeval_rate = 0;
								String goodeval_rate_desc = "";

								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next()) {
									goodeval_count = rs2
											.getInt("goodeval_count");
									eval_count = rs2.getInt("eval_count");
								}
								rs2.close();

								if (eval_count != 0)
									goodeval_rate = goodeval_count * 100
											/ eval_count;
								goodeval_rate_desc = ApiGlobal.Double2String(
										goodeval_rate, 2) + "%";

								pass_info.put("evgood_rate", ApiGlobal
										.Double2String(goodeval_rate, 2));
								pass_info.put("evgood_rate_desc",
										goodeval_rate_desc);
								// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

								// Carpool count
								int carpool_count = 0;
								String carpool_desc = "";

								szQuery = "SELECT COUNT(*) AS count FROM order_exec_cs WHERE deleted=0 AND status<>8 AND passenger="
										+ rs.getLong("pass_id");

								rs2 = stmt2.executeQuery(szQuery);
								rs2.next(); // Must exist. Otherwise abnormal
								carpool_count = rs2.getInt("count");
								carpool_desc = carpool_count + ConstMgr.STR_CI;
								rs2.close();

								pass_info.put("carpool_count", carpool_count);
								pass_info.put("carpool_count_desc",
										carpool_desc);
								// ////////////////////////////////////////////////////////////////////////////
							}
							retdata.put("pass_info", pass_info);

							retdata.put("start_addr",
									rs.getString("start_addr"));
							retdata.put("start_lat", rs.getDouble("start_lat"));
							retdata.put("start_lng", rs.getDouble("start_lng"));
							retdata.put("end_addr", rs.getString("end_addr"));
							retdata.put("end_lat", rs.getDouble("end_lat"));
							retdata.put("end_lng", rs.getDouble("end_lng"));

							retdata.put(
									"start_time",
									ApiGlobal.Date2Time(ApiGlobal
											.String2Date(rs
													.getString("pre_time")))
											+ ConstMgr.STR_CHUFA);

							// Get mid points
							szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=1 AND orderid="
									+ orderid;
							rs2 = stmt2.executeQuery(szQuery);
							while (rs2.next()) {
								JSONObject midpoint = new JSONObject();

								midpoint.put("index", rs2.getInt("point_index"));
								midpoint.put("lat", rs2.getDouble("lat"));
								midpoint.put("lng", rs2.getDouble("lng"));
								midpoint.put("addr", rs2.getString("addr"));

								mid_points.add(midpoint);
							}
							rs2.close();
							retdata.put("mid_points", mid_points);
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							price = rs.getDouble("price");
							price_desc = "" + price + ConstMgr.STR_DIAN;

							if (active == 0)
								sysfee_value = price * ratio / 100;
							else
								sysfee_value = Math.min(integer_, price);
							sysfee_desc = ConstMgr.STR_PINGTAIXINXIFEI
									+ ApiGlobal.Double2String(sysfee_value, 2)
									+ ConstMgr.STR_DIAN;

							retdata.put("price", price);
							retdata.put("price_desc", price_desc);
							retdata.put("sysinfo_fee", sysfee_value);
							retdata.put("sysinfo_fee_desc", sysfee_desc);
							retdata.put(
									"valid_days",
									rs.getString("order_onoffduty_details.leftdays"));

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
							result.retmsg = ConstMgr.ErrMsg_AlreadyAcceptedOrCancelled;
							result.retdata = retdata;
						}
					}
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

				if (rs2 != null) {
					try {
						rs2.close();
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

				if (stmt2 != null) {
					try {
						stmt2.close();
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,根据订单id获取长途订单信息 2，获取该车主所有的被评价信息，以及好评率 3,获取该车主所有参与的拼车信息
	 * 
 * @param source
 * @param userid
 * @param orderid
 * @param devtoken
 * @return
 */
	public SVCResult acceptableLongOrderDetailInfo(String source, long userid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null, stmt2 = null;
			ResultSet rs = null, rs2 = null;
			String szQuery = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {

					stmt = dbConn.createStatement();
					stmt2 = dbConn.createStatement();

					JSONObject retdata = new JSONObject();
					// 1,根据订单id获取长途订单信息
					szQuery = "SELECT "
							+ "		order_longdistance_details.*,"

							+ "		user.id AS driver_id,"
							+ "		user.nickname AS driver_name,"
							+ "		TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS driver_age,"
							+ "		TIMESTAMPDIFF(YEAR, user.drlicence_ti, CURDATE()) AS drv_career,"
							+ "		user.sex AS driver_gender,"
							+ "		user.img AS driver_img,"

							+ "		user_car.car_img,"
							+ "		user_car.brand,"
							+ "		user_car.style, "
							+ "		user_car.color, "
							+ "		user_car.car_type_id "

							+ "FROM order_longdistance_details "

							+ "INNER JOIN user "
							+ "ON user.id=order_longdistance_details.publisher "

							+ " LEFT JOIN  user_car "
							+ "ON user_car.userid=order_longdistance_details.publisher "

							+ "WHERE "
							+ "		order_longdistance_details.deleted=0 AND "
							+ "		user.deleted=0 AND "
							+ "		order_longdistance_details.id=" + orderid;

					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						JSONObject driver_info = new JSONObject();
						{
							driver_info.put("driver_id",
									rs.getLong("driver_id"));
							driver_info.put("driver_name",
									rs.getString("driver_name"));
							driver_info.put("driver_age",
									rs.getInt("driver_age"));
							driver_info.put("driver_gender",
									rs.getInt("driver_gender"));
							driver_info
									.put("driver_img", ApiGlobal
											.getAbsoluteURL(rs
													.getString("driver_img")));
							driver_info.put("car_img", ApiGlobal
									.getAbsoluteURL(rs.getString("car_img")));
							driver_info.put("brand", rs.getString("brand"));
							driver_info.put("type", rs.getString("style"));
							driver_info.put("car_color", rs.getString("color"));

							// Style setting
							szQuery = "SELECT * FROM car_type WHERE id="
									+ rs.getString("car_type_id");
							rs2 = stmt2.executeQuery(szQuery);
							if (rs2.next())
								driver_info.put("style", rs2.getInt("type"));
							else
								driver_info.put("style", 1);
							rs2.close();
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							// Driver career
							int drv_career = rs.getInt("drv_career");
							String drv_career_desc = ConstMgr.STR_JIALING
									+ drv_career + ConstMgr.STR_NIAN;
							driver_info.put("drv_career", drv_career);
							driver_info.put("drv_career_desc", drv_career_desc);
							// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							// Evaluation rate information
							int eval_count = 0, good_eval_count = 0, goodeval_rate = 0;
							String goodeval_rate_desc = "";
							// 2，获取该车主所有的被评价信息，以及好评率
							szQuery = "SELECT "
									+ "		all_info.count AS eval_count,"
									+ "		good_info.count AS good_eval_count "
									+ "FROM "
									+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND to_userid="
									+ rs.getLong("driver_id")
									+ ") AS all_info,"
									+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND usertype=1 AND level=1 AND to_userid="
									+ rs.getLong("driver_id")
									+ ") AS good_info";
							rs2 = stmt2.executeQuery(szQuery);
							rs2.next(); // Must exist. Otherwise abnormal
							eval_count = rs2.getInt("eval_count");
							good_eval_count = rs2.getInt("good_eval_count");
							rs2.close();

							if (eval_count != 0)
								goodeval_rate = good_eval_count * 100
										/ eval_count;
							goodeval_rate_desc = goodeval_rate + "%";

							driver_info.put("evgood_rate", goodeval_rate);
							driver_info.put("evgood_rate_desc",
									goodeval_rate_desc);
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							// Carpool count
							int carpool_count = 0;
							String carpool_desc = "";
							// 3,获取该车主所有参与的拼车信息
							szQuery = "SELECT COUNT(*) AS count "
									+ "FROM order_exec_cs " + "WHERE "
									+ "		deleted=0 AND "
									+ "		(status=6 OR status=7) AND "
									+ "		driver=" + rs.getLong("driver_id");

							rs2 = stmt2.executeQuery(szQuery);
							rs2.next(); // Must exist. Otherwise abnormal
							carpool_count = rs2.getInt("count");
							carpool_desc = ConstMgr.STR_FUWUCISHU
									+ carpool_count + ConstMgr.STR_CI;
							rs2.close();

							driver_info.put("carpool_count", carpool_count);
							driver_info.put("carpool_count_desc", carpool_desc);
							// ////////////////////////////////////////////////////////////////////////////
						}
						retdata.put("driver_info", driver_info);

						retdata.put(
								"start_city",
								ApiGlobal.cityCode2Name(dbConn,
										rs.getString("start_city")));
						retdata.put(
								"end_city",
								ApiGlobal.cityCode2Name(dbConn,
										rs.getString("end_city")));
						retdata.put("start_addr", rs.getString("start_addr"));
						retdata.put("start_lat", rs.getString("start_lat"));
						retdata.put("start_lng", rs.getString("start_lng"));
						retdata.put("end_addr", rs.getString("end_addr"));
						retdata.put("end_lat", rs.getString("end_lat"));
						retdata.put("end_lng", rs.getString("end_lng"));
						retdata.put("start_time", ApiGlobal
								.Date2StringWithoutSeconds(ApiGlobal
										.String2Date(rs.getString("pre_time"))));

						double price = rs.getDouble("price");
						String price_desc = ApiGlobal.Double2String(price, 2)
								+ ConstMgr.STR_DIAN + "/" + ConstMgr.STR_ZUO;
						retdata.put("price", price);
						retdata.put("price_desc", price_desc);
						retdata.put(
								"left_seats",
								rs.getInt("seat_num")
										- rs.getInt("occupied_num"));

						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
						result.retdata = retdata;

					} else // Not exist order. Abnormal
					{
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
						result.retdata = retdata;
					}
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

				if (rs2 != null) {
					try {
						rs2.close();
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

				if (stmt2 != null) {
					try {
						stmt2.close();
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,按城市来获取播报时间和播报范围 2,将当前订单插入到order_temp_details表中
	 * 3,获取该乘客周边的车主并插入到order_temp_notify表中
	 * ，注意周边的意思是最大范围range3，然后获得到车主后按照距离远近分别标记是属于哪个范围的
	 * ，是range1还是range2还是range3。此函数只把range1范围的车主加入到order_temp_notify表中
	 * 4,将用户传入的中途点信息插入到midpoints表中。
	 * 
	 * 隐含的问题：1，获取周边车主的sql语句，当用户多的时候，可能产生严重的性能问题；
	 * 
	 * @param source
	 * @param userid
	 * @param start_addr
	 * @param start_lat
	 * @param start_lng
	 * @param end_addr
	 * @param end_lat
	 * @param end_lng
	 * @param start_time
	 * @param midpoints
	 * @param remark
	 * @param reqstyle
	 * @param price
	 * @param city
	 * @param devtoken
	 * @return
	 */
	public SVCResult publishOnceOrder(String source, long userid,
			String start_addr, double start_lat, double start_lng,
			String end_addr, double end_lat, double end_lng, String start_time,
			String midpoints, String remark, int reqstyle, double price,
			String city, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + start_addr + "," + start_lat + "," + start_lng + "," + end_addr
				 + "," + end_lat + "," + end_lng + "," + start_time + "," + midpoints + "," + remark + "," + reqstyle
				 + "," + price + "," + city + "," + devtoken);

		if (source.equals("") || userid < 0 || start_addr.equals("")
				|| end_addr.equals("") || start_time.equals("")
				|| city.equals("") || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				} else {
					JSONObject retdata = new JSONObject();

					SVCGlobalParams global_params = null;
					SVCCity city_params = null;
					String szCityCode = "";
					final int DRV_LOCK_TIME_DIFF = 10;

					stmt = dbConn.createStatement();

					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// Global parameters.
					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					szQuery = "SELECT * FROM global_params WHERE deleted=0";
					rs = stmt.executeQuery(szQuery);
					rs.next(); // Global parameters must exist. If not exist, it
								// is abnormal
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();
					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// City parameters
					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					szCityCode = ApiGlobal.cityName2Code(dbConn, city);
					szQuery = "SELECT * FROM city WHERE code=\"" + szCityCode
							+ "\" AND deleted=0";
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						city_params = SVCCity.decodeFromResultSet(rs);
					rs.close();
					// /////////////////////////
					// ///////////////////////////////////////////////////////////////////////////////////

					int wait_time = 0;
					double range1 = 0;
					String hb_vldprd = ApiGlobal.getEnvValueFromCode(dbConn,
							ConstMgr.ENVCODE_HEARTBEAT_VALIDDPERIOD);
					String szTurnTime = "";

					if (city_params == null || city_params.total_time < 0) {
						wait_time = global_params.total_time;
						range1 = global_params.range1;
					} else {
						wait_time = city_params.total_time;
						range1 = city_params.range1;
					}
					// 1,向临时订单表插入该订单数据
					szQuery = "INSERT INTO order_temp_details (" + "order_num,"
							+ "seats_num," + "is_broadcast_stop," + "turn_num,"
							+ "ps_date," + "start_lat," + "start_lng,"
							+ "start_addr," + "end_lat," + "end_lng,"
							+ "end_addr," + "order_city," + "pre_time,"
							+ "price," + "wait_time," + "order_cs_id,"
							+ "is_from_onoffdutyorder," + "orderonoffduty_id,"
							+ "publisher," + "reqcarstyle," + "status,"
							+ "remark," + "deleted" + ") VALUES (\""
							+ ApiGlobal.generateOnceOrderNum()
							+ "\",1,0,0,\""
							+ ApiGlobal.Date2String(new Date(), true)
							+ "\","
							+ start_lat
							+ ","
							+ start_lng
							+ ",\""
							+ start_addr
							+ "\","
							+ end_lat
							+ ","
							+ end_lng
							+ ",\""
							+ end_addr
							+ "\",\""
							+ szCityCode
							+ "\",\""
							+ start_time
							+ "\","
							+ price
							+ ","
							+ wait_time
							+ ",0,0,0,"
							+ userid
							+ ","
							+ reqstyle
							+ ",1,\""
							+ remark + "\", 0)";

					if (stmt.executeUpdate(szQuery,
							Statement.RETURN_GENERATED_KEYS) == 1) {
						ResultSet genKeys = stmt.getGeneratedKeys();
						if (!genKeys.next()) {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(1)";
						} else {
							long orderid = genKeys.getLong(1);
							genKeys.close();

							/**********************************************************************************
							 * Search nearby drivers and save in
							 * <order_temp_notify> table
							 * 获取临近司机的这个sql语句性能不好，以后想办法调优一下此sql语句
							 **********************************************************************************/
							// 2，查询出乘客附近range1范围的车主
							szQuery = "SELECT "
									+ "		user.*, "
									+ "		user_online.lat,"
									+ "		user_online.lng "
									+ "FROM user "

									+ "INNER JOIN user_online "
									+ "ON user_online.userid=user.id "

									+ "WHERE "
									+ "		user.deleted=0 AND "
									+ "		user_online.deleted=0 AND "
									+ "		timediff_min(user_online.last_heartbeat_time, CURRENT_TIMESTAMP())<="
									+ hb_vldprd
									+ " AND "
									+ "		calc_distance(user_online.lat, user_online.lng, "
									+ start_lat + "," + start_lng + ")<="
									+ range1;

							rs = stmt.executeQuery(szQuery);
							// 3，将上一步查询出的range1范围的车主加入到order_temp_notify表中
							szQuery = "INSERT INTO order_temp_notify ("
									+ "userid," + "turn," + "order_id,"
									+ "push_time," + "deleted" + ") VALUES ";

							int drvcount = 0;
							while (rs.next()) {
								drvcount++;

								if (szQuery.charAt(szQuery.length() - 1) == ')')
									szQuery += ",";

								szTurnTime = ApiGlobal.Date2String(new Date(),
										true);

								szQuery += "(";
								szQuery += rs.getLong("id") + ",";
								szQuery += 1 + ",";
								szQuery += orderid + ",";
								szQuery += "\"" + szTurnTime + "\",0)";
							}

							// Insert drivers into order_temp_notify table
							if (drvcount > 0)
								stmt.executeUpdate(szQuery);

							// // Start notification timer tasks
							// if (bHasResult
							// && stmt.executeUpdate(szQuery,
							// Statement.RETURN_GENERATED_KEYS) > 0) {
							// TimerTask pushTask1 = new TimerTask() {
							// @Override
							// public void run() {
							// // Push to 1st range drivers
							// }
							// };
							//
							// TimerTask pushTask2 = new TimerTask() {
							// @Override
							// public void run() {
							// // Push to 2nd range drivers
							// }
							// };
							//
							// TimerTask pushTask3 = new TimerTask() {
							// @Override
							// public void run() {
							// // Push to 3rd range drivers
							// }
							// };
							//
							// Timer push_timer = new Timer();
							// push_timer.schedule(pushTask1, 0);
							// push_timer.schedule(pushTask2, turn1_time);
							// push_timer.schedule(pushTask3, turn2_time);
							// }

							szQuery = "";

							/**********************************************************************************
							 * Prepare return parameters
							 **********************************************************************************/
							if (city_params == null) {
								retdata.put("order_id", orderid);
								retdata.put("wait_time",
										global_params.total_time);
								retdata.put("driver_lock_time",
										global_params.driver_lock_time
												- DRV_LOCK_TIME_DIFF);

								retdata.put("drvcount", drvcount);

								retdata.put("pub12_time",
										global_params.turn1_time);
								retdata.put("pub23_time",
										global_params.turn2_time);

								retdata.put("add_price_time1",
										global_params.add_price_time1);
								retdata.put("add_price_time2",
										global_params.add_price_time2);
								retdata.put("add_price_time3",
										global_params.add_price_time3);
								retdata.put("add_price_time4",
										global_params.add_price_time4);
								retdata.put("add_price_time5",
										global_params.add_price_time5);

								retdata.put("same_price_time1",
										global_params.same_price_time1);
								retdata.put("same_price_time2",
										global_params.same_price_time2);
								retdata.put("same_price_time3",
										global_params.same_price_time3);
								retdata.put("same_price_time4",
										global_params.same_price_time4);
								retdata.put("same_price_time5",
										global_params.same_price_time5);
							} else {
								retdata.put("order_id", orderid);
								retdata.put("wait_time", city_params.total_time);
								retdata.put("driver_lock_time",
										city_params.driver_lock_time
												- DRV_LOCK_TIME_DIFF);

								retdata.put("drvcount", drvcount);

								retdata.put("pub12_time",
										city_params.turn1_time);
								retdata.put("pub23_time",
										city_params.turn2_time);

								retdata.put("add_price_time1",
										city_params.add_price_time1);
								retdata.put("add_price_time2",
										city_params.add_price_time2);
								retdata.put("add_price_time3",
										city_params.add_price_time3);
								retdata.put("add_price_time4",
										city_params.add_price_time4);
								retdata.put("add_price_time5",
										city_params.add_price_time5);

								retdata.put("same_price_time1",
										city_params.same_price_time1);
								retdata.put("same_price_time2",
										city_params.same_price_time2);
								retdata.put("same_price_time3",
										city_params.same_price_time3);
								retdata.put("same_price_time4",
										city_params.same_price_time4);
								retdata.put("same_price_time5",
										city_params.same_price_time5);
							}

							// ////////////////////////////////////////////////////////////////////////
							// Mid points
							// ////////////////////////////////////////////////////////////////////////
							// 4,插入中途点信息
							szQuery = "INSERT INTO midpoints (order_type,orderid,point_index,lat,lng,addr,deleted) VALUES ";

							String[] arrPoints = midpoints.split(",");
							int length = arrPoints.length / 3, point_index = 1;
							for (int i = 0; i < length; i++) {
								String szLat = arrPoints[i * 3];
								String szLng = arrPoints[i * 3 + 1];
								String szAddr = ApiGlobal
										.fixEncoding(arrPoints[i * 3 + 2]);

								double lat = 0, lng = 0;
								try {
									lat = Double.parseDouble(szLat);
									lng = Double.parseDouble(szLng);
								} catch (Exception ex) {
									ex.printStackTrace();
									continue;
								}

								if (szQuery.charAt(szQuery.length() - 1) == ')') {
									szQuery += ",";
								}

								szQuery += "(0," + orderid + "," + point_index
										+ "," + lat + "," + lng + ",\""
										+ szAddr + "\",0)";

								point_index++;
							}

							if (szQuery.charAt(szQuery.length() - 1) == ')')
								stmt.executeUpdate(szQuery);

							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(3)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,向order_onoffduty_details表插入上下班订单 2,向midpoints表插入中途点信息。
	 * 
	 * @param source
	 * @param userid
	 * @param start_addr
	 * @param start_lat
	 * @param start_lng
	 * @param end_addr
	 * @param end_lat
	 * @param end_lng
	 * @param start_time
	 * @param midpoints
	 * @param remark
	 * @param reqstyle
	 * @param price
	 * @param days
	 * @param city
	 * @param devtoken
	 * @return
	 */
	public SVCResult publishOnOffOrder(String source, long userid,
			String start_addr, double start_lat, double start_lng,
			String end_addr, double end_lat, double end_lng, String start_time,
			String midpoints, String remark, int reqstyle, double price,
			String days, String city, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + start_addr + "," + start_lat + "," + start_lng + "," + end_addr
				 + "," + end_lat + "," + end_lng + "," + start_time + "," + midpoints + "," + remark + "," + reqstyle
				 + "," + price + "," + city + "," + devtoken);

		if (source.equals("") || userid < 0 || start_addr.equals("")
				|| end_addr.equals("") || start_time.equals("")
				|| city.equals("") || days.equals("") || devtoken.equals("")
				|| ApiGlobal.String2Date(start_time) == null) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				} else {
					String szCityCode = ApiGlobal.cityName2Code(dbConn, city);

					stmt = dbConn.createStatement();

					szQuery = "INSERT INTO order_onoffduty_details ("
							+ "order_num," + "alldays_beaccepted,"
							+ "from_date," + "price," + "start_lat,"
							+ "start_lng," + "start_addr," + "end_lat,"
							+ "end_lng," + "end_addr," + "order_city,"
							+ "leftdays," + "pre_time," + "publisher,"
							+ "publish_date," + "reqcarstyle," + "status,"
							+ "remark," + "deleted" + ") VALUES (\""
							+ ApiGlobal.generateOnOffOrderNum()
							+ "\",0,\""
							+ start_time
							+ "\","
							+ price
							+ ","
							+ start_lat
							+ ","
							+ start_lng
							+ ",\""
							+ start_addr
							+ "\","
							+ end_lat
							+ ","
							+ end_lng
							+ ",\""
							+ end_addr
							+ "\",\""
							+ szCityCode
							+ "\",\""
							+ days
							+ "\",\""
							+ start_time
							+ "\","
							+ userid
							+ ", \""
							+ ApiGlobal.Date2String(new Date(), true)
							+ "\","
							+ reqstyle + ",1,\"" + remark + "\", 0)";

					if (stmt.executeUpdate(szQuery,
							Statement.RETURN_GENERATED_KEYS) == 1) {
						ResultSet genKeys = stmt.getGeneratedKeys();
						if (!genKeys.next()) {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(4)";
						} else {
							long orderid = genKeys.getLong(1);

							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// Mid points
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							String[] arrPoints = midpoints.split(",");
							int length = arrPoints.length / 3, point_index = 1;
							for (int i = 0; i < length; i++) {
								String szLat = arrPoints[i * 3];
								String szLng = arrPoints[i * 3 + 1];
								String szAddr = ApiGlobal
										.fixEncoding(arrPoints[i * 3 + 2]);

								double lat = 0, lng = 0;
								try {
									lat = Double.parseDouble(szLat);
									lng = Double.parseDouble(szLng);
								} catch (Exception ex) {
									ex.printStackTrace();
									continue;
								}

								szQuery = "INSERT INTO midpoints (order_type,orderid,point_index,lat,lng,addr,deleted) VALUES (1,"
										+ orderid
										+ ","
										+ point_index
										+ ","
										+ lat
										+ ","
										+ lng
										+ ",\""
										+ szAddr
										+ "\",0)";
								if (stmt.executeUpdate(szQuery) == 1)
									point_index++;
							}
							// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;

							// Send push notification and order notification to
							// suitable drivers
							int nSrcDistLimit = 3; // 3km
							int nDstDistLimit = 5; // 3km
							int nTimeLimit = 60; // 60 minutes
							int nOverlapRate = 60;

							Date dt_temp = null, dt_start = ApiGlobal
									.String2Date(start_time);
							Calendar cal_temp = Calendar.getInstance(), cal_start = Calendar
									.getInstance();
							String[] day_array = days.split(",");
							String route_days = "";
							ArrayList<Long> arrDrivers = new ArrayList<Long>();

							cal_start.setTime(dt_start);

							szQuery = "SELECT"
									+ "		routes_settings.*,"
									+ "		user.id AS user_id "
									+ "FROM "
									+ "		routes_settings "

									+ "INNER JOIN user "
									+ "ON user.id=routes_settings.userid "

									+ "WHERE "
									+ "		user.deleted=0 AND"
									+ "		user.driver_verified=1 AND "
									+ "		user.id <> "
									+ userid
									+ " AND "
									+ "		routes_settings.type=2 AND "
									+ "		routes_settings.deleted=0 AND "
									+ "		calc_distance(routes_settings.lat_from,routes_settings.lng_from,"
									+ start_lat
									+ ","
									+ start_lng
									+ ") <="
									+ nSrcDistLimit
									+ " AND "
									+ "		calc_distance(routes_settings.lat_to,routes_settings.lng_to,"
									+ end_lat + "," + end_lng + ") <="
									+ nDstDistLimit;
							rs = stmt.executeQuery(szQuery);
							while (rs.next()) {
								dt_temp = rs.getDate("start_time");
								cal_temp.setTime(dt_temp);

								// For the time comparison, must set the
								// year,month,day the same
								cal_temp.set(Calendar.YEAR,
										cal_start.get(Calendar.YEAR));
								cal_temp.set(Calendar.MONTH,
										cal_start.get(Calendar.MONTH));
								cal_temp.set(Calendar.DAY_OF_MONTH,
										cal_start.get(Calendar.DAY_OF_MONTH));

								long milli_diff = Math.abs(cal_temp
										.getTimeInMillis()
										- cal_start.getTimeInMillis());
								long milli_hour = nTimeLimit * 60 * 1000;

								if (milli_diff > milli_hour)
									continue;

								// Compare days
								route_days = rs.getString("whichdays");

								int nCount = 0;
								for (int i = 0; i < day_array.length; i++) {
									if (route_days.contains(day_array[i]))
										nCount++;
								}

								if (day_array.length * nOverlapRate / 100 > nCount)
									continue;

								arrDrivers.add(rs.getLong("user_id"));
							}
							rs.close();

							String szMsg = "发现您可以接单的上下班订单，消息中心点击本消息可查看详情";

							STPushNotificationData data = new STPushNotificationData();
							data.title = ConstMgr.STR_DINGDANXIAOXI;
							data.description = szMsg;

							STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
							custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
							custom_data.orderid = orderid;
							custom_data.userrole = STPushNotificationCustomData.USER_ROLE_DRIVER;
							custom_data.ordertype = ConstMgr.ORDER_ONOFF;

							data.custom_data = custom_data;

							for (int i = 0; i < arrDrivers.size(); i++)
								ApiGlobal.addOrderNotification(dbConn,
										arrDrivers.get(i), orderid, szMsg);
							ApiGlobal.sendPushNotifToUser(dbConn, arrDrivers,
									data);
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(6)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,向order_longdistance_details插入长途订单信息
	 * 
	 * @param source
	 * @param userid
	 * @param start_addr
	 * @param start_city
	 * @param start_lat
	 * @param start_lng
	 * @param end_addr
	 * @param end_city
	 * @param end_lat
	 * @param end_lng
	 * @param start_time
	 * @param remark
	 * @param price
	 * @param seats_count
	 * @param devtoken
	 * @return
	 */
	public SVCResult publishLongOrder(String source, long userid,
			String start_addr, String start_city, double start_lat,
			double start_lng, String end_addr, String end_city, double end_lat,
			double end_lng, String start_time, String remark, double price,
			int seats_count, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + start_addr + "," + start_city + "," + start_lat + "," + start_lng + "," + end_addr
				 + "," + end_city + "," + end_lat + "," + end_lng + "," + start_time + "," + remark + "," + price + "," + seats_count + "," + devtoken);

		if (source.equals("") || userid < 0 || start_addr.equals("")
				|| start_city.equals("") || end_addr.equals("")
				|| end_city.equals("") || start_time.equals("")
				|| devtoken.equals("")
				|| ApiGlobal.String2Date(start_time) == null) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				} else {
					String szStartCityCode = ApiGlobal.cityName2Code(dbConn,
							start_city);
					String szEndCityCode = ApiGlobal.cityName2Code(dbConn,
							end_city);

					stmt = dbConn.createStatement();

					szQuery = "INSERT INTO order_longdistance_details ("
							+ "order_num," + "pre_time," + "price,"
							+ "start_city," + "end_city," + "start_addr,"
							+ "end_addr," + "start_lat," + "start_lng,"
							+ "end_lat," + "end_lng," + "seat_num,"
							+ "publisher," + "ps_time," + "occupied_num,"
							+ "remark," + "status," + "deleted"
							+ ") VALUES (\""
							+ ApiGlobal.generateLongOrderNum()
							+ "\",\""
							+ start_time
							+ "\","
							+ price
							+ ",\""
							+ szStartCityCode
							+ "\",\""
							+ szEndCityCode
							+ "\",\""
							+ start_addr
							+ "\",\""
							+ end_addr
							+ "\","
							+ start_lat
							+ ","
							+ start_lng
							+ ","
							+ end_lat
							+ ","
							+ end_lng
							+ ","
							+ seats_count
							+ ","
							+ userid
							+ ", \""
							+ ApiGlobal.Date2String(new Date(), true)
							+ "\",0,\"" + remark + "\", 1, 0)";

					if (stmt.executeUpdate(szQuery,
							Statement.RETURN_GENERATED_KEYS) == 1) {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;

						// Send push notification and order notification to
						// suitable passengers

						long orderid = 0;
						rs = stmt.getGeneratedKeys();
						if (rs.next())
							orderid = rs.getLong(1);
						rs.close();

						Date dt_temp = null, dt_start = ApiGlobal
								.String2Date(start_time);
						Calendar cal_temp = Calendar.getInstance(), cal_start = Calendar
								.getInstance();
						ArrayList<Long> arrPass = new ArrayList<Long>();

						cal_start.setTime(dt_start);

						szQuery = "SELECT" + "		routes_settings.*,"
								+ "		user.id AS user_id " + "FROM "
								+ "		routes_settings "

								+ "INNER JOIN user "
								+ "ON user.id=routes_settings.userid "

								+ "WHERE " + "		user.deleted=0 AND"
								+ "		user.id <> " + userid + " AND "
								+ "		routes_settings.type=1 AND "
								+ "		routes_settings.deleted=0 AND "
								+ "		routes_settings.startcity=\""
								+ szStartCityCode + "\" AND "
								+ "		routes_settings.endcity=\""
								+ szEndCityCode + "\"";

						rs = stmt.executeQuery(szQuery);
						while (rs.next()) {
							dt_temp = rs.getDate("start_time");
							cal_temp.setTime(dt_temp);

							// For the time comparison, must set the
							// year,month,day the same
							if (cal_temp.get(Calendar.YEAR) != cal_start
									.get(Calendar.YEAR))
								continue;

							if (cal_temp.get(Calendar.MONTH) != cal_start
									.get(Calendar.MONTH))
								continue;

							if (cal_temp.get(Calendar.DAY_OF_MONTH) != cal_start
									.get(Calendar.DAY_OF_MONTH))
								continue;

							arrPass.add(rs.getLong("user_id"));
						}
						rs.close();

						String szMsg = "发现您可以抢座的长途订单，消息中心点击本消息可查看详情";

						STPushNotificationData data = new STPushNotificationData();
						data.title = ConstMgr.STR_DINGDANXIAOXI;
						data.description = szMsg;

						STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
						custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
						custom_data.orderid = orderid;
						custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
						custom_data.ordertype = ConstMgr.ORDER_ONOFF;

						data.custom_data = custom_data;

						for (int i = 0; i < arrPass.size(); i++)
							ApiGlobal.addOrderNotification(dbConn,
									arrPass.get(i), orderid, szMsg);
						ApiGlobal.sendPushNotifToUser(dbConn, arrPass, data);

					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(7)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,根据订单号获取单次订单
	 * 2,判断是否已经生成了该单次订单的执行订单，查询条件是当前车主加上出发时间，如果已经存在了该单次订单的执行订单，则报错并且返回
	 * 3,如果该车主已经抢单了，则报错返回 4,查询出车主锁定时间
	 * 5,将该车主插入到order_temp_grab表中(谁抢单就把谁插入到表order_temp_grab中)
	 * 6,将车主自己从order_temp_notify表中移除，防止自己接单后还能看到这条记录 问题：
	 * 7,向乘客推送消息，第7步骤是为了加价流程变更而写的
	 * 
	 * (1)对于第5步骤，如果车主将自己插入到表order_temp_grab里了，但是乘客并没有确认，以后如何处理
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param lat
	 * @param lng
	 * @param devtoken
	 * @return
	 */
	public SVCResult acceptOnceOrder(String source, long userid, long orderid,
			double lat, double lng, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + lat + "," + lng + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else if (!ApiGlobal.lockOnceOrderAcceptance(orderid)) {
			result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
			result.retmsg = ConstMgr.ErrMsg_AlreadyAccepted;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				dbConn.setAutoCommit(false);

				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				// 1,根据订单号获取单次订单
				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);
				SVCOrderTempGrab grab_info = ApiGlobal
						.getLatestGrabInfoFromOrderID(dbConn, orderid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (userinfo.driver_verified != 1) // Not verified driver
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotVerifiedDriver;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				} else if (order_info == null || order_info.deleted == 1) // No
																			// order
																			// info
				{
					result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
					result.retmsg = ConstMgr.ErrMsg_AlreadyAccepted;
				} else if (order_info.status != 1) // Order is already accepted
													// or cancelled
				{
					result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
					result.retmsg = ConstMgr.ErrMsg_AlreadyAcceptedOrCancelled;
				} else if (grab_info != null
						&& (grab_info.driverid != userid || grab_info.status != 2)) // Order
																					// is
																					// already
																					// accepted
				{
					result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
					result.retmsg = ConstMgr.ErrMsg_AlreadyAcceptedOrCancelled;
				} 
				//因为加入了预约拼，所以不存在订单已过时，如果定单出发时间已过，则在可接单列表中就看不到该订单 modify by chuzhiqiang
/*				else if (ApiGlobal.onceOrderIsExpiredOrNot(order_info)) // Time
																			// expired
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_OrderExpired;
				}*/ 
				else {
					stmt = dbConn.createStatement();

					// Start time consideration.
					// If other order is reserved to start after or before 30
					// minutes, cannot accept.
					{
						SVCOrderExecCS pre_order = null;
						Calendar cal_lowBnd = Calendar.getInstance();
						Calendar cal_upBnd = Calendar.getInstance();

						String lbnd = ApiGlobal.getEnvValueFromCode(dbConn,
								ConstMgr.ENVCODE_ACCEPT_INVALID_TIME_LBND);
						String ubnd = ApiGlobal.getEnvValueFromCode(dbConn,
								ConstMgr.ENVCODE_ACCEPT_INVALID_TIME_UBND);

						int nLbnd = 30, nUbnd = 60;		// default values

						try {
							nLbnd = Integer.parseInt(lbnd);
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						try {
							nUbnd = Integer.parseInt(ubnd);
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						cal_lowBnd.setTime(order_info.pre_time);
						cal_upBnd.setTime(order_info.pre_time);

						cal_lowBnd.add(Calendar.MINUTE, -nLbnd);
						cal_upBnd.add(Calendar.MINUTE, nUbnd);

						String szLBnd = ApiGlobal.Date2String(
								cal_lowBnd.getTime(), true);
						String szUBnd = ApiGlobal.Date2String(
								cal_upBnd.getTime(), true);
						// 2,判断是否已经生成了该单次订单的执行订单，查询条件是当前车主加上出发时间，如果已经存在了该单次订单的执行订单，则报错并且返回
						szQuery = "SELECT * " + "FROM order_exec_cs "
								+ "WHERE " + "		deleted=0 AND " + "		driver="
								+ userid + " AND " + "		status < 3 AND "
								+ "		pre_time >= \"" + szLBnd + "\" AND "
								+ "		pre_time <= \"" + szUBnd + "\"";
						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							pre_order = SVCOrderExecCS.decodeFromResultSet(rs);
						}
						rs.close();

						if (pre_order != null) {// 存在指定订单的执行订单，报错返回
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_AlreadyHasOtherOrder
									+ ApiGlobal
											.Date2StringWithoutSeconds(pre_order.pre_time);
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}
					}

					JSONObject retdata = new JSONObject();

					SVCGlobalParams global_params = null;
					SVCCity city = null;
					double insu_fee = 0;// modify by chuzhiqiang

					int driver_lock_time = 0;
					// 3,如果该车主已经抢单了，则报错返回
					szQuery = "SELECT COUNT(id) AS exist_cnt "
							+ "FROM order_temp_grab "

							+ "WHERE " + "		order_id=" + orderid + " AND "
							+ "		driverid=" + userid;

					rs = stmt.executeQuery(szQuery);
					if (rs.next() && rs.getInt("exist_cnt") > 0) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_AlreadyAccepted;
						ApiGlobal.logMessage(result.encodeToJSON().toString());

						return result;
					}
					rs.close();
					// 4,查询出车主锁定时间
					szQuery = "SELECT * FROM global_params WHERE deleted=0";
					rs = stmt.executeQuery(szQuery);
					// Global parameters must exist. If not exist, it is
					// abnormal
					rs.next();
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();
					insu_fee = global_params.insu_fee;// modify by chuzhiqiang

					System.out.println("insu_fee---:" + insu_fee);
					szQuery = "SELECT * FROM city WHERE code=\""
							+ userinfo.city_cur + "\" AND deleted=0";
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						city = SVCCity.decodeFromResultSet(rs);
						rs.close();

						if (city.driver_lock_time < 0) // City parameters are
														// not set. Use global
														// params
						{
							driver_lock_time = global_params.driver_lock_time;
						} else // City parameters are set. Use city params
						{
							driver_lock_time = city.driver_lock_time;
						}
					} else {
						driver_lock_time = global_params.driver_lock_time;
					}
					// 5,将该车主插入到order_temp_grab表中(谁抢单就把谁插入到表order_temp_grab中)
					szQuery = "INSERT INTO order_temp_grab ("
							+ "grab_time,"
							+ "distance,"
							+ "status,"
							+ "driverid,"
							+ "order_id,"
							+ "deleted"
							+ ") VALUES (\""
							+ ApiGlobal.Date2String(new Date(), true)
							+ "\","
							+ ApiGlobal.calcDist(order_info.start_lat,
									order_info.end_lat, lat, lng) + ",0,"
							+ userid + "," + orderid + ",0)";

					if (stmt.executeUpdate(szQuery) == 1) {

						// 6,将车主自己从order_temp_notify表中移除，防止自己再刷新出此订单
						// Remove DB records
						szQuery = "UPDATE order_temp_notify SET deleted=1 WHERE order_id="
								+ order_info.id;
						stmt.executeUpdate(szQuery);

						retdata.put("wait_time", driver_lock_time);
						retdata.put("insu_fee", insu_fee);// modify by
															// chuzhiqiang

						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
						result.retdata = retdata;
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(8)";
					}
					//added by zyl begin
					//7,向乘客推送消息，第7步骤是为了加价流程变更而写的
					// Send push notification and order notification
					// to passenger
					String szMsg = "有车主正在抢您的订单！点击查看！";
					STPushNotificationData data = new STPushNotificationData();

					data.title = ConstMgr.STR_DINGDANXIAOXI;
					data.description = szMsg;

					STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
					custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_DRIVER_GRABORDER;
					custom_data.orderid = order_info.id;
					custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
					custom_data.ordertype = ConstMgr.ORDER_ONCE;

					data.custom_data = custom_data;

					ApiGlobal.addOrderNotification(dbConn,
							order_info.publisher, orderid, szMsg);
					ApiGlobal.sendPushNotifToUser(dbConn,
							order_info.publisher, data);
					//added by zyl end

					dbConn.commit();
				}
			} catch (Exception ex) {
				ex.printStackTrace();

				try {
					dbConn.rollback();
				} catch (Exception ex2) {
					ex2.printStackTrace();
				}

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

				ApiGlobal.unlockOnceOrderAcceptance(orderid);
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1，将接单的车主插入到表order_onoffduty_grab中。 2，这个业务逻辑没有搞懂
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param days
	 * @param devtoken
	 * @return
	 */
	public SVCResult acceptOnOffOrder(String source, long userid, long orderid,
			String days, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + days + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0 || days.equals("")
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);
				SVCOrderOnOffDutyGrab grab_info = ApiGlobal
						.onoffOrderIsAcceptedByDriver(dbConn, orderid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (userinfo.driver_verified != 1) // Not verified driver
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotVerifiedDriver;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 1 || grab_info != null) // Order
																		// is
																		// already
																		// accepted
				{
					result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
					result.retmsg = ConstMgr.ErrMsg_AlreadyAcceptedOrCancelled;
				} else {
					JSONObject retdata = new JSONObject();

					stmt = dbConn.createStatement();

					szQuery = "SELECT COUNT(id) AS exist_cnt "
							+ "FROM order_onoffduty_grab "

							+ "WHERE " + "		order_id=" + orderid + " AND "
							+ "		driverid=" + userid;

					rs = stmt.executeQuery(szQuery);
					if (rs.next() && rs.getInt("exist_cnt") > 0) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_AlreadyAccepted;
						ApiGlobal.logMessage(result.encodeToJSON().toString());

						return result;
					}
					rs.close();

					szQuery = "INSERT INTO order_onoffduty_grab ("
							+ "grab_time," + "status," + "driverid," + "days,"
							+ "order_id," + "deleted" + ") VALUES (\""
							+ ApiGlobal.Date2String(new Date(), true) + "\",0,"
							+ userid + ",\"" + days + "\"," + orderid + ",0)";

					if (stmt.executeUpdate(szQuery) == 1) {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
						result.retdata = retdata;

						// Send push notification and order notification
						String szMsg = "您的上下班订单" + order_info.order_num
								+ "有车主接单，快去我的订单看看吧！";

						ApiGlobal.addOrderNotification(dbConn,
								order_info.publisher, orderid, szMsg);

						STPushNotificationData data = new STPushNotificationData();
						data.title = ConstMgr.STR_DINGDANXIAOXI;
						data.description = szMsg;

						STPushNotificationCustomData customData = new STPushNotificationCustomData();
						customData.typecode = STPushNotificationCustomData.PNOTIF_TYPE_ONOFFORDER_ACCEPTED;
						customData.orderid = order_info.id;
						customData.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
						customData.ordertype = ConstMgr.ORDER_ONOFF;

						data.custom_data = customData;

						ApiGlobal.sendPushNotifToUser(dbConn,
								order_info.publisher, data);
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(9)";
						result.retdata = retdata;
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,查询该乘客是否已经抢了座位，下面sql逻辑是看该长途订单对应的order_exec_cs表中是否已经存在了该乘客的记录
	 * 2，将乘客插入到表order_longdistance_users_cs中，包括抢了几个座的信息。
	 * 3,如果order_longdistance_users_cs此订单下，存在该乘客则删除掉，这个应该是为了防止乘客多次抢座
	 * 4,在order_longdistance_users_cs中建立user和order_exec_cs的关系
	 * 5,更新order_longdistance_details表中座位数
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param seats_count
	 * @param devtoken
	 * @return
	 */
	public SVCResult acceptLongOrder(String source, long userid, long orderid,
			int seats_count, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + seats_count + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0 || seats_count < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			Statement stmt2 = null;//用于获取刚生成的投保单号  modify by chuzhiqiang
			ResultSet rs2 = null;//用于获取刚生成的投保单号  modify by chuzhiqiang
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				dbConn.setAutoCommit(false);

				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// ------begain modify by chuzhiqiang
				// -----------------------------
				// 获得长途订单详细信息
				SVCOrderLongDistanceDetails longDistanceDetails = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);
				// 获得车主id
				long driverid = longDistanceDetails.publisher;
				// 获得车主信息,用于给车主买保险
				SVCUser driverInfo = ApiGlobal.getUserInfoByUserID(dbConn,
						driverid);
				// ------end modify by chuzhiqiang -----------------------------
				// user balance
				double balance = ApiGlobal.getUserBalance(dbConn, userid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				}
				// else if (order_info.status != 1) // Order is already accepted
				// {
				// result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
				// result.retmsg = ConstMgr.ErrMsg_AlreadyAccepted;
				// }
				else if (order_info.seat_num - order_info.occupied_num <= 0) // No
																				// seats
																				// remaining
				{
					result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
					result.retmsg = ConstMgr.ErrMsg_NoRemainSeats;
				} else if (order_info.seat_num - order_info.occupied_num < seats_count) // Seats
																						// not
																						// enough
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotEnoughSeats;
				} else if (balance < seats_count * order_info.price) // Price is
																		// not
																		// enough
				{
					result.retcode = ConstMgr.ErrCode_NotEnoughBalance;
					result.retmsg = ConstMgr.ErrMsg_NotEnoughBalance;

					JSONObject retdata = new JSONObject();
					retdata.put("orderID", orderid);
					retdata.put("rembal", balance);
					retdata.put("total_fee", order_info.price * seats_count);

					result.retdata = retdata;
				} else {
					long order_exec_id = 0;
					long insu_id = 0;// 初始化 保险id
					//String appl_no = "0";// 投保单号
					JSONObject objTemp = ApiGlobal.getDistAndSystemAveragePrice(dbConn, userid,
									order_info.start_city, ApiGlobal.Date2String(order_info.pre_time,true),
									order_info.start_lat, order_info.start_lng,
									order_info.end_lat, order_info.end_lng, "",1);
					double order_price = objTemp.getDouble("price");

					stmt = dbConn.createStatement();
					// 1,查询该乘客是否已经抢了座位，下面sql逻辑是看该长途订单对应的order_exec_cs表中是否已经存在了该乘客的记录
					szQuery = "SELECT COUNT(order_exec_cs.id) AS exist_cnt "
							+ "FROM order_exec_cs "

							+ "INNER JOIN order_longdistance_users_cs "
							+ "ON order_longdistance_users_cs.order_exec_cs_id=order_exec_cs.id "

							+ "INNER JOIN order_longdistance_details "
							+ "ON order_longdistance_users_cs.orderdriverlongdistance_id=order_longdistance_details.id "

							+ "WHERE " + "		order_exec_cs.passenger=" + userid
							+ " AND " + "		order_exec_cs.driver="
							+ order_info.publisher + " AND "
							+ "		order_exec_cs.status<>8 AND "
							+ "		order_longdistance_details.id="
							+ order_info.id + " AND "
							+ "		order_longdistance_users_cs.deleted = 0";

					rs = stmt.executeQuery(szQuery);
					if (rs.next() && rs.getInt("exist_cnt") > 0) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_AlreadyAccepted;
						ApiGlobal.logMessage(result.encodeToJSON().toString());

						return result;
					}
					rs.close();
					// 2，将乘客插入到表order_longdistance_users_cs中，包括抢了几个座的信息。
					szQuery = "INSERT INTO order_exec_cs (" + "order_type,"
							+ "passenger," + "driver," + "from_," + "to_,"
							+ "average_price_platform," + "price,"
							+ "pre_time," + "remark," + "cr_date,"
							+ "ti_accept_order," + "city_from," + "city_to,"
							+ "has_evaluation_passenger,"
							+ "has_evaluation_driver," + "password,"
							+ "begin_lat," + "begin_lng," + "end_lat,"
							+ "end_lng," + "freeze_points," + "total_distance,"
							+ "order_city," + "status," + "deleted"
							+ ") VALUES (" + "4,"
							+ userid
							+ ","
							+ order_info.publisher
							+ ",\""
							+ order_info.start_addr
							+ "\",\""
							+ order_info.end_addr
							+ "\","
							+ order_price
							+ ","
							+ order_info.price * seats_count
							+ ",\""
							+ ApiGlobal.Date2String(order_info.pre_time, true)
							+ "\",\""
							+ order_info.remark
							+ "\",\""
							+ ApiGlobal.Date2String(new Date(), true)
							+ "\",\""
							+ ApiGlobal.Date2String(new Date(), true)
							+ "\",\""
							+ order_info.start_city
							+ "\",\""
							+ order_info.end_city
							+ "\",0,0,"
							+ "'0000'," // Default password is "0000"
							+ order_info.start_lat
							+ ","
							+ order_info.start_lng
							+ ","
							+ order_info.end_lat
							+ ","
							+ order_info.end_lng
							+ ","
							+ order_info.price
							* seats_count
							+ ",0,\""
							+ order_info.start_city
							+ "\",2,0)";

					if (stmt.executeUpdate(szQuery,
							Statement.RETURN_GENERATED_KEYS) == 1) // Inserting
																	// exec log
																	// success
					{
						ResultSet genKeys = stmt.getGeneratedKeys();
						if (genKeys.next())
							order_exec_id = genKeys.getLong(1);
					}

					if (order_exec_id > 0) {
						// 3,如果order_longdistance_users_cs此订单下，存在该乘客则删除掉，这个应该是为了防止乘客多次抢座
						// At first, remove all the previous logs
						szQuery = "UPDATE order_longdistance_users_cs "
								+ "SET deleted=1 " + "WHERE "
								+ "		orderdriverlongdistance_id=" + orderid
								+ " AND " + "		userid=" + userid;
						stmt.executeUpdate(szQuery);
						// 4,在order_longdistance_users_cs中建立user和order_exec_cs的关系
						szQuery = "INSERT INTO order_longdistance_users_cs ("
								+ "orderdriverlongdistance_id," + "userid,"
								+ "order_exec_cs_id," + "seat_num,"
								+ "ps_date," + "deleted" + ") VALUES ("
								+ order_info.id + "," + userid + ","
								+ order_exec_id + "," + seats_count + ",\""
								+ ApiGlobal.Date2String(new Date(), true)
								+ "\",0)";
						// 5,更新order_longdistance_details表中座位数
						if (stmt.executeUpdate(szQuery) == 1) {
							szQuery = "UPDATE order_longdistance_details "
									+ "SET " + "		occupied_num="
									+ (order_info.occupied_num + seats_count)
									+ " ";

							if (order_info.status == 1)
								szQuery += ", status=2, ti_accept_order=\""
										+ ApiGlobal.Date2String(new Date(),
												true) + "\" ";

							szQuery += " WHERE deleted=0 AND id="
									+ order_info.id;

							if (stmt.executeUpdate(szQuery) == 1
									&& ApiGlobal.freezePointsWithType(dbConn,
											source, userid, order_exec_id,
											order_info.id, 3, order_info.price
													* seats_count,
											ConstMgr.txcode_userFreezeBalance) > 0) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;

								JSONObject retdata = new JSONObject();

								retdata.put("orderID", orderid);
								retdata.put("rembal", balance - seats_count
										* order_info.price);
								retdata.put("total_fee", seats_count
										* order_info.price);

								result.retdata = retdata;

								dbConn.commit();
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(10)";

								try {
									dbConn.rollback();
								} catch (Exception ex2) {
									ex2.printStackTrace();
								}
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(11)";
							try {
								dbConn.rollback();
							} catch (Exception ex2) {
								ex2.printStackTrace();
							}
						}
							// -----begain modify by chuzhiqiang
							// 接受订单后向数据库中插入保险相关数据----------------------
							// 车主肯定是验证过驾驶照的，所以车主的身份证号就能够知道，就可以给车主买保险

							// 乘客没有验证身份，只给车主买保险
						
							if (driverInfo.username != "" || driverInfo.username != null) {
								stmt2 = dbConn.createStatement();

								System.out.println("driverInfo.birthday----->"
										+ driverInfo.birthday);

								SimpleDateFormat sf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
								SimpleDateFormat sf1 = new SimpleDateFormat( "yyyy-MM-dd");
								Calendar c = Calendar.getInstance();
								// System.out.println("当前日期：       "+sf.format(c.getTime()));
								c.add(Calendar.DAY_OF_MONTH, 1);
								szQuery = "INSERT INTO insurance " + "("
										+ "	insurance.APPL_NO,"// 投保单号
										+ "  insurance.APPL_TIME,"// 投保时间，当前时间
										+ "  insurance.EFFECT_TIME,"// 保单生效时间
										+ "  insurance.INSEXPR_DATE,"// 保单失效时间
										+ "  insurance.TOTAL_AMOUNT,"// 保额
										+ "  insurance.TOTAL_PREMINUM,"// 合计保费，insu_fee
										+ "  insurance.INSU_STATUS,"// 保单状态

										+ "  insurance.APPL_ID,"// 投保人id
										+ "  insurance.APPL_NAME,"// 投保人姓名
										+ "  insurance.APPL_SEX,"// 投保人性别
										+ "  insurance.APPL_BIRTHDAY,"// 投保人生日
										+ "  insurance.CERT_TYPE,"// 投保人证件类型
										+ "  insurance.CERT_CODE,"// 投保人证件号码

										+ "  insurance.ISD_ID,"// 被保人id
										+ "  insurance.ISD_NAME,"// 被保人姓名
										+ "  insurance.ISD_SEX,"// 被保人性别
										+ "  insurance.ISD_BIRTHDAY,"// 被保险人生日
										+ "  insurance.ISD_CERT_TYPE,"// 被保险人证件类型
										+ "  insurance.ISD_CERT_CODE,"// 被保险人证件号码

										+ "  insurance.REQUEST_TYPE,"// 请求类型
										+ "  insurance.REQUSET_TIME,"// 请求时间
										+ "  insurance.REQUEST_TRANSNO,"// 交易流水号，合作方自己定义
										+ "  insurance.USER,"// 保险公司分配给合作方的账号
										+ "  insurance.PASSWORD,"// 保险公司分配给合作方的密码

										+ "  insurance.PROD_TYPE,"// 承包产品类型
										+ "  insurance.PROD_CODE,"// 承包产品代码
										+ "  insurance.BUS_BRANCH,"// 业务受理机构
										+ "  insurance.SALE_CHANNEL,"// 销售渠道
										+ "  insurance.COUNTER_CODE,"// 网店代码
										+ "  insurance.BENEF_ORDER,"// 受益顺序
										+ "  insurance.ISD_COUNT,"// 被保险人数
										+ "  insurance.ISD_SERIAL,"// 被保险人顺序
										+ "  insurance.ISD_TYPE,"// 被保险人类型
										+ "  insurance.APPL_RELATION,"// 投保人和被保人顺序，司乘
										+ "  insurance.ORDER_EXEC_ID "// 投保人和被保人顺序，司乘
										+ ") " + "VALUES ("

										+ "	 \'OOKP" + new Date().getTime() + "\' "// 投保单号 ，格式ht201411111648
										+ " , \'" + sf.format(new Date()) + "\' "// 投保时间，当前时间
										//+ "  ,\'" + sf.format(new Date()) + "\' "// 保单生效时间,暂且定位当前生效时间
										+ "  ,\'" + sf.format(order_info.pre_time) + "\' "// 保单生效时间,暂且定位当前生效时间
										+ "  ,\'" + sf1.format(order_info.pre_time) +" 23:59:59"+ "\' "// 保单失效时间,暂且定位1天
										+ "  ,502000.00 "// 保额，暂且定为2000.00元
										+ "  ,0.5 "// 合计保费，从参数表中读取insu_fee的值,先暂时写死为0.5元
										+ "  ,0 "// 保单状态，0有效，1失效

										+ "  ," + driverInfo.id// 投保人id，指车主
										+ "  ,\'" + driverInfo.username + "\' "// 投保人姓名，即车主姓名
										+ "  ,\'"+(driverInfo.sex==0?"M":"F")+"\'"// 投保人性别，车主性别
										+ "  ,\'"+ApiGlobal.Date2String(driverInfo.birthday, false) + "\' "// 投保人生日，车主生日
										+ "  ,\'I\'"// 投保人证件类型 车主身份证件类型
										+ "  ,\'" + driverInfo.id_card_num + "\'"// 投保人证件号码，车主证件号码

										+ "  ," + driverInfo.id// 被保人id
										+ "  ,\'" + driverInfo.username + "\' "// 被保人姓名
										+ "  ,\'"+(driverInfo.sex==0?"M":"F") + "\' "// 被保人性别
										+ "  ,\'" + ApiGlobal.Date2String( driverInfo.birthday, false) + "\' "// 被保险人生日
										+ "  ,\'I\'"// 被保险人证件类型
										+ "  ,\'" + driverInfo.id_card_num + "\'"// 被保险人证件号码

										+ "  ,\'SA01\'"// 请求类型
										+ "  ,\'" + sf.format(new Date()) + "\'"// 请求时间
										+ "  ,\'tsNo" + new Date().getTime() + "\' "// 交易流水号，合作方自己定义
										+ "  ,\'HAOTANG\'"// 保险公司分配给合作方的账号
										+ "  , \'HAOTANG365\'"// 保险公司分配给合作方的密码

										+ "  ,\'C\' "// 承包产品类型
										+ "  , \'PC_D01\' "// 承包产品代码
										+ "  , \'110108\' "// 业务受理机构
										+ "  ,\'OA\' "// 销售渠道
										+ "  ,\'8008\' "// 网店代码
										+ "  ,1"// 受益顺序
										+ "  ,1"// 被保险人数
										+ "  ,1"// 被保险人顺序
										+ "  ,1"// 被保险人类型
										+ "  ,\'X\' "// 投保人和被保人关系，司乘
										+ " ," + order_exec_id + ")";
								System.out
										.println("SVCOrderService.acceptLongOrder()给车主买保险--->"
												+ szQuery);
								if (stmt.executeUpdate(szQuery,
										Statement.RETURN_GENERATED_KEYS) == 1) {// 向保单表车如数据成功

									ResultSet genKeys = stmt.getGeneratedKeys();
									if (genKeys.next())
										insu_id = genKeys.getLong(1);
								}

								if (insu_id > 0) {

									
								szQuery = "SELECT appl_no from insurance WHERE id = " + insu_id;
								System.out.println("111111111---------"+szQuery);
								rs2=stmt2.executeQuery(szQuery);
								String appl_no =null;
								if (rs2.next()) {
									
									 appl_no = rs2.getString("appl_no");
								}
								rs2.close();	

									szQuery = "INSERT INTO insurance_dtd ("
											+ " insu_id" + " ,APPL_NO"
											+ " ,oper_type" + " ,oper_time"
											+ " ,insu_sum"

											+ ") " + "values" + "("

											+ insu_id 
											+ " , \'" +appl_no+"\'"
											+ " ,0" 
											+ " ,\'"
											+ sf.format(new Date()) + "\' "
											+ " ,502000.00" + ")";
									System.out.println("向流水表中插入数据" + szQuery);
									stmt.executeUpdate(szQuery);

									szQuery = "UPDATE order_exec_cs SET insu_id_driver = "
											+ insu_id
											+ " WHERE id ="
											+ order_exec_id;
									System.out
											.println("将insu_id更新到order_exec_cs表中"
													+ szQuery);
									stmt.executeUpdate(szQuery);
								}
							

								// 判断乘客是否验证了身份，如果验证了，就给买保险
								if (userinfo.person_verified == 1
										&& (userinfo.username != "" || userinfo.username != null)&&(ApiGlobal.getAgeByBirthday(userinfo.birthday)>=18)) {
									System.out
											.println("userinfo.username----------"
													+ userinfo.username
													+ "**"
													+ userinfo.birthday);
									SimpleDateFormat sf2 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
									SimpleDateFormat sf3 = new SimpleDateFormat( "yyyy-MM-dd");
									Calendar c2 = Calendar.getInstance();
									// System.out.println("当前日期：       "+sf.format(c.getTime()));
									c2.add(Calendar.DAY_OF_MONTH, 1);
									szQuery = "INSERT INTO insurance " + "("
											+ "	insurance.APPL_NO,"// 投保单号
											+ "  insurance.APPL_TIME,"// 投保时间，当前时间
											+ "  insurance.EFFECT_TIME,"// 保单生效时间
											+ "  insurance.INSEXPR_DATE,"// 保单失效时间
											+ "  insurance.TOTAL_AMOUNT,"// 保额
											+ "  insurance.TOTAL_PREMINUM,"// 合计保费，insu_fee
											+ "  insurance.INSU_STATUS,"// 保单状态

											+ "  insurance.APPL_ID,"// 投保人id
											+ "  insurance.APPL_NAME,"// 投保人姓名
											+ "  insurance.APPL_SEX,"// 投保人性别
											+ "  insurance.APPL_BIRTHDAY,"// 投保人生日
											+ "  insurance.CERT_TYPE,"// 投保人证件类型
											+ "  insurance.CERT_CODE,"// 投保人证件号码

											+ "  insurance.ISD_ID,"// 被保人id
											+ "  insurance.ISD_NAME,"// 被保人姓名
											+ "  insurance.ISD_SEX,"// 被保人性别
											+ "  insurance.ISD_BIRTHDAY,"// 被保险人生日
											+ "  insurance.ISD_CERT_TYPE,"// 被保险人证件类型
											+ "  insurance.ISD_CERT_CODE,"// 被保险人证件号码

											+ "  insurance.REQUEST_TYPE,"// 请求类型
											+ "  insurance.REQUSET_TIME,"// 请求时间
											+ "  insurance.REQUEST_TRANSNO,"// 交易流水号，合作方自己定义
											+ "  insurance.USER,"// 保险公司分配给合作方的账号
											+ "  insurance.PASSWORD,"// 保险公司分配给合作方的密码

											+ "  insurance.PROD_TYPE,"// 承包产品类型
											+ "  insurance.PROD_CODE,"// 承包产品代码
											+ "  insurance.BUS_BRANCH,"// 业务受理机构
											+ "  insurance.SALE_CHANNEL,"// 销售渠道
											+ "  insurance.COUNTER_CODE,"// 网店代码
											+ "  insurance.BENEF_ORDER,"// 受益顺序
											+ "  insurance.ISD_COUNT,"// 被保险人数
											+ "  insurance.ISD_SERIAL,"// 被保险人顺序
											+ "  insurance.ISD_TYPE,"// 被保险人类型
											+ "  insurance.APPL_RELATION,"// 投保人和被保人顺序，司乘
											+ "  insurance.ORDER_EXEC_ID "// 投保人和被保人顺序，司乘
											+ ") " + "VALUES ("

											+ "	 \'OOKP" + new Date().getTime() + "\' "// 投保单号 ，格式ht201411111648
											+ " , \'" + sf2.format(new Date()) + "\' "// 投保时间，当前时间
											//+ "  ,\'" + sf2.format(new Date()) + "\' "// 保单生效时间,暂且定位当前生效时间
											+ "  ,\'" + sf2.format(order_info.pre_time) + "\' "// 保单生效时间,暂且定位当前生效时间
											+ "  ,\'" + sf3.format(order_info.pre_time)+" 23:59:59" + "\' "// 保单失效时间,暂且定位1天
											+ "  ,502000.00 "// 保额，暂且定为2000.00元
											+ "  ,0.5 "// 合计保费，从参数表中读取insu_fee的值,先暂时写死为0.5元
											+ "  ,0 "// 保单状态，0有效，1失效

											+ "  ," + driverInfo.id// 投保人id，指车主
											+ "  ,\'" + driverInfo.username + "\' "// 投保人姓名，即车主姓名
											+ "  ,\'" + (driverInfo.sex==0?"M":"F")+"\'"// 投保人性别，车主性别
											+ "  ,\'" + ApiGlobal.Date2String(driverInfo.birthday, false) + "\' "// 投保人生日，车主生日
											+ "  ,\'I\'"// 投保人证件类型 车主身份证件类型
											+ "  ,\'" + driverInfo.id_card_num + "\'"// 投保人证件号码，车主证件号码

											+ "  ," + userinfo.id// 被保人id
											+ "  ,\'" + userinfo.username + "\' "// 被保人姓名
											+ "  ,\'" + (userinfo.sex==0?"M":"F") + "\' "// 被保人性别
											+ "  ,\'" + ApiGlobal.Date2String(userinfo.birthday, false) + "\' "// 被保险人生日
											+ "  ,\'I\'"// 被保险人证件类型
											+ "  ,\'" + userinfo.id_card_num + "\'"// 被保险人证件号码

											+ "  ,\'SA01\'"// 请求类型
											+ "  ,\'" + sf2.format(new Date()) + "\'"// 请求时间
											+ "  ,\'tsNo" + new Date().getTime() + "\' "// 交易流水号，合作方自己定义
											+ "  ,\'HAOTANG\'"// 保险公司分配给合作方的账号
											+ "  , \'HAOTANG365\'"// 保险公司分配给合作方的密码

											+ "  ,\'C\' "// 承包产品类型
											+ "  , \'PC_P01\' "// 承包产品代码
											+ "  , \'110108\' "// 业务受理机构
											+ "  ,\'OA\' "// 销售渠道
											+ "  ,\'8008\' "// 网店代码
											+ "  ,1"// 受益顺序
											+ "  ,1"// 被保险人数
											+ "  ,1"// 被保险人顺序
											+ "  ,1"// 被保险人类型
											+ "  ,\'X\' "// 投保人和被保人关系，司乘
											+ " ," + order_exec_id + ")";
									System.out
											.println("SVCOrderService.acceptLongOrder()给乘客买保险--->"
													+ szQuery);
									if (stmt.executeUpdate(szQuery,
											Statement.RETURN_GENERATED_KEYS) == 1) {// 向保单表车如数据成功

										ResultSet genKeys = stmt
												.getGeneratedKeys();
										if (genKeys.next())
											insu_id = genKeys.getLong(1);
									}

									if (insu_id > 0) {

										szQuery = "SELECT appl_no from insurance WHERE id = " + insu_id;

										rs2 = stmt2.executeQuery(szQuery);
										String appl_no = null;
										if (rs2.next()) {
											
											appl_no = rs2.getString("appl_no");
										}
										rs2.close();	

										szQuery = "INSERT INTO insurance_dtd ("
												+ " insu_id" + " ,APPL_NO"
												+ " ,oper_type" + " ,oper_time"
												+ " ,insu_sum"

												+ ") " + "values" + "("

												+ insu_id 
												+ " , \'" +appl_no+"\'"
												+ " ,0"
												+ " ,\'"
												+ sf2.format(new Date())
												+ "\' " + " ,502000.00" + ")";
										System.out.println("向流水表中插入数据"
												+ szQuery);
										stmt.executeUpdate(szQuery);

										szQuery = "UPDATE order_exec_cs SET insu_id_pass = "
												+ insu_id
												+ " WHERE id ="
												+ order_exec_id;
										System.out
												.println("将insu_id更新到order_exec_cs表中"
														+ szQuery);
										stmt.executeUpdate(szQuery);
									}


								}

								// -----end modify by chuzhiqiang
								// 接受订单后向数据库中插入保险相关数据----------------------

							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(12)";
								try {
									dbConn.rollback();
								} catch (Exception ex2) {
									ex2.printStackTrace();
								}
							}
						}
					}

					dbConn.commit();
				
			} catch (Exception ex) {
				ex.printStackTrace();

				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ex.getMessage();
				try {
					dbConn.rollback();
				} catch (Exception ex2) {
					ex2.printStackTrace();
				}
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
				//modify  by  chuzhiqiang
				if (stmt2 != null) {
					try {
						stmt2.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}
				//modify  by  chuzhiqiang
				// Close db connection
				if (dbConn != null) {
					try {
						dbConn.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1，查询出该长途订单下该用户的执行订单 2，在上一步骤查询出的执行订单中设置乘客的密码 3,查询出车主信息 4,查询出车主的经纬度
	 * 5，获得车主拼车次数 6,获得车主的被评价总数以及好评率
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param password
	 * @param devtoken
	 * @return
	 */
	public SVCResult setLongOrderPassword(String source, long userid,
			long orderid, String password, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + password + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| password.length() == 0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null, stmt2 = null;
			ResultSet rs = null, rs2 = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				// Get exec info
				SVCOrderExecCS execInfo = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else {
					stmt = dbConn.createStatement();
					stmt2 = dbConn.createStatement();

					// 1，查询出该长途订单下该用户的执行订单
					szQuery = "SELECT "
							+ "		order_longdistance_users_cs.order_exec_cs_id,"
							+ "		order_exec_cs.* "

							+ "FROM "
							+ "		order_longdistance_users_cs "

							+ "INNER JOIN order_exec_cs "
							+ "ON order_exec_cs.id=order_longdistance_users_cs.order_exec_cs_id "

							+ "WHERE "
							+ "		order_exec_cs.deleted=0 And "
							+ "		order_exec_cs.status<3 AND "
							+ "		order_exec_cs.passenger="
							+ userid
							+ " AND "
							+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
							+ order_info.id + " "
							+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						execInfo = SVCOrderExecCS.decodeFromResultSet(rs);

						if (execInfo.status != 2) {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}
					}
					rs.close();

					if (result.retcode >= 0 && execInfo != null) {
						// 2，在上一步骤查询出的执行订单中设置乘客的密码
						szQuery = "UPDATE order_exec_cs SET password=\""
								+ password + "\" WHERE id=" + execInfo.id;

						if (stmt.executeUpdate(szQuery) == 1) {
							// 3,查询出车主信息
							szQuery = "SELECT "
									+ "		user.*,"
									+ "		TIMESTAMPDIFF(YEAR, user.drlicence_ti, CURDATE()) AS drv_career,"
									+ "		user_car.brand," + "		user_car.style,"
									+ "		user_car.color,"
									+ "		user_car.car_img,"
									+ "		user_car.plate_num, "
									+ "		user_car.car_type_id "

									+ "FROM user "

									+ " LEFT JOIN  user_car "
									+ "ON user_car.userid=user.id "

									+ "WHERE user.id=" + execInfo.driver;

							rs = stmt.executeQuery(szQuery);
							if (rs.next()) {
								double drvlat = 0, drvlng = 0;
								// 4,查询出车主的经纬度
								szQuery = "SELECT * FROM user_online WHERE userid="
										+ rs.getLong("id")
										+ " ORDER BY last_heartbeat_time DESC";
								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next()) {
									drvlat = rs2.getDouble("lat");
									drvlng = rs2.getDouble("lng");
								}
								rs2.close();

								JSONObject retdata = new JSONObject();

								retdata.put("drv_id", rs.getLong("id"));
								retdata.put("drv_name",
										rs.getString("nickname"));
								retdata.put("drv_img", ApiGlobal
										.getAbsoluteURL(rs.getString("img")));
								retdata.put("drv_age", ApiGlobal.getDiffYear(
										rs.getDate("birthday"), new Date()));
								retdata.put("drv_gender", rs.getInt("sex"));
								retdata.put("car_img",
										ApiGlobal.getAbsoluteURL(rs
												.getString("car_img")));

								// Car style
								int carstyle = 1;
								szQuery = "SELECT type FROM car_type "
										+ "WHERE id="
										+ rs.getLong("car_type_id");
								rs2 = stmt2.executeQuery(szQuery);
								if (rs2.next())
									carstyle = rs2.getInt("type");
								rs2.close();
								// ///////////////////////////////////////////////////////////////////////////////////////////

								retdata.put("car_style", carstyle);

								retdata.put("car_brand", rs.getString("brand"));
								retdata.put("car_type", rs.getString("style"));
								retdata.put("car_color", rs.getString("color"));
								retdata.put("carno", rs.getString("plate_num"));

								double distance = ApiGlobal.calcDist(
										order_info.start_lat,
										order_info.start_lng, drvlat, drvlng);
								String dist_desc = ConstMgr.STR_JUNIN;
								if (distance < 0.2) {
									dist_desc += "<200m";
								} else if (distance < 1) {
									dist_desc += "" + (int) (distance * 1000)
											+ "km";
								} else {
									dist_desc += ApiGlobal.Double2String(
											distance, 2) + "km";
								}
								retdata.put("dist", distance);
								retdata.put("dist_desc", dist_desc);
								retdata.put("start_time", ApiGlobal
										.Date2String(order_info.pre_time, true));
								retdata.put("start_addr", order_info.start_addr);
								retdata.put("end_addr", order_info.end_addr);
								retdata.put("password", password);

								long drv_career = rs.getInt("drv_career");
								String drv_career_desc = ConstMgr.STR_JIALING
										+ drv_career + ConstMgr.STR_NIAN;

								retdata.put("drv_career", drv_career);
								retdata.put("drv_career_desc", drv_career_desc);
								rs.close();

								// 5，获得车主拼车次数
								int carpool_count = 0;
								String carpool_count_desc = "";

								szQuery = "SELECT COUNT(*) AS count "
										+ "FROM order_exec_cs " + "WHERE "
										+ "		deleted=0 AND "
										+ "		(status=6 OR status=7) AND "
										+ "		driver=" + order_info.publisher;
								rs = stmt.executeQuery(szQuery);
								if (rs.next()) {
									carpool_count = rs.getInt("count");
								}
								rs.close();
								carpool_count_desc = carpool_count
										+ ConstMgr.STR_CI;

								retdata.put("carpool_count", carpool_count);
								retdata.put("carpool_count_desc",
										carpool_count_desc);

								int eval_count = 0, goodeval_count = 0;
								int goodeval_rate = 0;
								String goodeval_rate_desc = "";
								// 6,获得车主的被评价总数以及好评率
								szQuery = "SELECT "
										+ "		all_info.count AS eval_count,"
										+ "		good_info.count AS goodeval_count "
										+ "FROM "
										+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
										+ order_info.publisher
										+ " AND usertype=1) AS all_info, "
										+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
										+ order_info.publisher
										+ " AND usertype=1 AND level=1) AS good_info ";

								rs = stmt.executeQuery(szQuery);
								if (rs.next()) {
									eval_count = rs.getInt("eval_count");
									goodeval_count = rs
											.getInt("goodeval_count");

									if (eval_count != 0)
										goodeval_rate = goodeval_count * 100
												/ eval_count;
								}
								rs.close();

								goodeval_rate_desc = ApiGlobal.Double2String(
										goodeval_rate, 2) + "%";

								retdata.put("evgood_rate", goodeval_rate);
								retdata.put("evgood_rate_desc",
										goodeval_rate_desc);

								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;
								result.retdata = retdata;
							} else {
								result.retcode = ConstMgr.ErrCode_Normal;
								result.retmsg = ConstMgr.ErrMsg_NoDrvInfo;
							}
							rs.close();
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(13)";
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(14)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,设置order_temp_grab表，将当前车主设置为优胜者 2,获取中途点信息，为了计算出路程，然后再计算出平台平均价
	 * 3,将新生成的订单执行记录插入到order_exec_cs表中 4,更新order_temp_details表的状态 5,查询出接单车主的相关信息
	 * 
	 * 隐含问题：没有找到车主优胜者之间互斥的代码，比如说a抢单成功了，然后手机端出了bug
	 * ，又接到了b车主的抢单请求，又点了确定，那么b车主会把a车主覆盖掉
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param password
	 * @param devtoken
	 * @return
	 */

	public SVCResult confirmOnceOrder(String source, long userid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			Statement stmt2 = null;//用于 获得刚生成保单号   modify  by   chuzhiqiang
			ResultSet rs2 = null;//用于 获得刚生成保单号modify  by   chuzhiqiang
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);

				// Get grab info
				SVCOrderTempGrab grab_info = ApiGlobal
						.getLatestGrabInfoFromOrderID(dbConn, orderid);

				double cur_bal = ApiGlobal.getUserBalance(dbConn, userid);

				// ------begain modify by chuzhiqiang
				// -----------------------------
				// 获得单次订单详细信息
				// SVCOrderTempDetails onceOrderTempDetails =
				// ApiGlobal.getOnceOrderInfo(dbConn, orderid);
				// 获得车主id
				long driverid = grab_info.driverid;
				// 获得车主信息,用于给车主买保险
				SVCUser driverInfo = ApiGlobal.getUserInfoByUserID(dbConn,
						driverid);
				// ------end modify by chuzhiqiang -----------------------------

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else if (order_info == null || order_info.deleted == 1) // No
																			// order
																			// info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else if (order_info.status != 1) // Order is already accepted
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
					// } else if (grab_info == null) // No grab information
					// {
					// result.retcode = ConstMgr.ErrCode_Normal;
					// result.retmsg = ConstMgr.ErrMsg_NoGrabInfo;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else if (cur_bal < order_info.price) {
					result.retcode = ConstMgr.ErrCode_NotEnoughBalance;
					result.retmsg = ConstMgr.ErrMsg_NotEnoughBalance;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				} else {

					stmt = dbConn.createStatement();

					// 1,设置order_temp_grab表，将当前车主设置为优胜者
					// Update grab log
					szQuery = "UPDATE order_temp_grab SET status=1 WHERE id="
							+ grab_info.id;
					if (stmt.executeUpdate(szQuery) != 1) // Update failure
					{
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(15)";
						ApiGlobal.logMessage(result.encodeToJSON().toString());

						return result;
					} else {
						// 2,获取中途点信息，为了计算出路程，然后再计算出平台平均价
						// Get mid points information
						String szMidPoints = "";
						szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=0 AND orderid="
								+ order_info.id;
						rs = stmt.executeQuery(szQuery);
						while (rs.next()) {
							if (!szMidPoints.equals(""))
								szMidPoints += ",";
							szMidPoints += "" + rs.getDouble("lat") + ","
									+ rs.getDouble("lng") + ","
									+ rs.getString("addr");
						}
						rs.close();

						// Insert order_exec_cs log
						long exec_id = 0;
						long insu_id = 0;// 保单id modify by chuzhiqiang
						//String appl_no = "0"; //投保单号

						String szCurTime = ApiGlobal.Date2String(new Date(),
								true);
						// 3,将新生成的订单执行记录插入到order_exec_cs表中
						szQuery = "INSERT INTO order_exec_cs (" + "order_type,"
								+ "passenger," + "driver," + "from_," + "to_,"
								+ "average_price_platform," + "price,"
								+ "pre_time," + "remark," + "cr_date,"
								+ "ti_accept_order," + "city_from,"
								+ "city_to," + "has_evaluation_passenger,"
								+ "has_evaluation_driver," + "password,"
								+ "begin_lat," + "begin_lng," + "end_lat,"
								+ "end_lng," + "freeze_points,"
								+ "total_distance," + "order_city," + "status,"
								+ "deleted" + ") VALUES (" + "1,"
								+ order_info.publisher
								+ ","
								+ grab_info.driverid
								+ ",\""
								+ order_info.start_addr
								+ "\",\""
								+ order_info.end_addr
								+ "\","
								+ ApiGlobal.getDistAndSystemAveragePrice(
										dbConn,
										userid,
										ApiGlobal.cityCode2Name(dbConn,
												order_info.order_city),
										ApiGlobal.Date2String(
												order_info.pre_time, true),
										order_info.start_lat,
										order_info.start_lng,
										order_info.end_lat, order_info.end_lng,
										szMidPoints, order_info.reqcarstyle)
										.getDouble("price")
								+ ","
								+ order_info.price
								+ ",\""
								+ ApiGlobal.Date2String(order_info.pre_time,
										true)
								+ "\",\""
								+ order_info.remark
								+ "\",\""
								+ szCurTime
								+ "\",\""
								+ szCurTime
								+ "\",\""
								+ order_info.order_city
								+ "\",\""
								+ order_info.order_city
								+ "\",0,0,\""
								+ "0000" // Set default password '0000'
								+ "\","
								+ order_info.start_lat
								+ ","
								+ order_info.start_lng
								+ ","
								+ order_info.end_lat
								+ ","
								+ order_info.end_lng
								+ ","
								+ order_info.price
								+ ",0,\""
								+ order_info.order_city + "\",2,0)";

						if (stmt.executeUpdate(szQuery,
								Statement.RETURN_GENERATED_KEYS) == 1) {
							rs = stmt.getGeneratedKeys();
							if (rs.next())
								exec_id = rs.getLong(1);
							rs.close();
						}

						if (exec_id > 0) // Insert order_exec_log success
						{
							// 4,更新order_temp_details表的状态
							// Update order_temp_details info
							szQuery = "UPDATE order_temp_details SET accept_time=\""
									+ szCurTime
									+ "\",order_cs_id="
									+ exec_id
									+ ",status=2 WHERE deleted=0 AND id="
									+ order_info.id;
							if (stmt.executeUpdate(szQuery) == 1) {
								// 5,查询出接单车主的相关信息
								szQuery = "SELECT " + "		user.*,"
										+ "		user_car.brand,"
										+ "		user_car.style,"
										+ "		user_car.color,"
										+ "		user_car.car_img,"
										+ "		user_car.plate_num "

										+ "FROM user "

										+ " LEFT JOIN  user_car "
										+ "ON user_car.userid=user.id "

										+ "WHERE user.id=" + grab_info.driverid;

								rs = stmt.executeQuery(szQuery);
								if (rs.next()) {
									if (ApiGlobal.freezePointsWithType(dbConn,
											source, userid, exec_id, orderid,
											1, order_info.price,
											ConstMgr.txcode_userFreezeBalance) > 0) // Success
																					// freezing
									{
										result.retcode = ConstMgr.ErrCode_None;
										result.retmsg = ConstMgr.ErrMsg_None;
									} else {
										result.retcode = ConstMgr.ErrCode_Exception;
										
										result.retmsg = "insufficient_balance";//乘客绿点不足
										ApiGlobal.logMessage(result.encodeToJSON().toString());

										return result;
									}
								} else {
									result.retcode = ConstMgr.ErrCode_Normal;
									result.retmsg = ConstMgr.ErrMsg_NoDrvInfo;
									ApiGlobal.logMessage(result.encodeToJSON().toString());

									return result;
								}
								rs.close();
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(16)";
								ApiGlobal.logMessage(result.encodeToJSON().toString());

								return result;
							}

							// -----begain modify by chuzhiqiang
							// 接受订单后向数据库中插入保险相关数据----------------------
							// 车主肯定是验证过驾驶照的，所以车主的身份证号就能够知道，就可以给车主买保险

							// 乘客没有验证身份，只给车主买保险

							if (driverInfo.username != ""
									|| driverInfo.username != null) {
								
								String appl_no = null;
								stmt2 =dbConn.createStatement();
								SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
					        Calendar c = Calendar.getInstance();
								// System.out.println("当前日期：       "+sf.format(c.getTime()));
					        c.add(Calendar.DAY_OF_MONTH, 1);
								szQuery = "INSERT INTO insurance " + "("
										+ "	insurance.APPL_NO,"// 投保单号
										+ "  insurance.APPL_TIME,"// 投保时间，当前时间
										+ "  insurance.EFFECT_TIME,"// 保单生效时间
										+ "  insurance.INSEXPR_DATE,"// 保单失效时间
										+ "  insurance.TOTAL_AMOUNT,"// 保额
										+ "  insurance.TOTAL_PREMINUM,"// 合计保费，insu_fee
										+ "  insurance.INSU_STATUS,"// 保单状态

										+ "  insurance.APPL_ID,"// 投保人id
										+ "  insurance.APPL_NAME,"// 投保人姓名
										+ "  insurance.APPL_SEX,"// 投保人性别
										+ "  insurance.APPL_BIRTHDAY,"// 投保人生日
										+ "  insurance.CERT_TYPE,"// 投保人证件类型
										+ "  insurance.CERT_CODE,"// 投保人证件号码

										+ "  insurance.ISD_ID,"// 被保人id
										+ "  insurance.ISD_NAME,"// 被保人姓名
										+ "  insurance.ISD_SEX,"// 被保人性别
										+ "  insurance.ISD_BIRTHDAY,"// 被保险人生日
										+ "  insurance.ISD_CERT_TYPE,"// 被保险人证件类型
										+ "  insurance.ISD_CERT_CODE,"// 被保险人证件号码

										+ "  insurance.REQUEST_TYPE,"// 请求类型
										+ "  insurance.REQUSET_TIME,"// 请求时间
										+ "  insurance.REQUEST_TRANSNO,"// 交易流水号，合作方自己定义
										+ "  insurance.USER,"// 保险公司分配给合作方的账号
										+ "  insurance.PASSWORD,"// 保险公司分配给合作方的密码

										+ "  insurance.PROD_TYPE,"// 承包产品类型
										+ "  insurance.PROD_CODE,"// 承包产品代码
										+ "  insurance.BUS_BRANCH,"// 业务受理机构
										+ "  insurance.SALE_CHANNEL,"// 销售渠道
										+ "  insurance.COUNTER_CODE,"// 网店代码
										+ "  insurance.BENEF_ORDER,"// 受益顺序
										+ "  insurance.ISD_COUNT,"// 被保险人数
										+ "  insurance.ISD_SERIAL,"// 被保险人顺序
										+ "  insurance.ISD_TYPE,"// 被保险人类型
										+ "  insurance.APPL_RELATION,"// 投保人和被保人顺序，司乘
										+ "  insurance.ORDER_EXEC_ID "// 投保人和被保人顺序，司乘
										+ ") " + "VALUES ("

										+ "	 \'OOKP"+ new Date().getTime()+ "\' "// 投保单号 ，格式ht201411111648
										+ " , \'" + sf.format(new Date()) + "\' "// 投保时间，当前时间
										//+ "  ,\'" + sf.format(new Date()) + "\' "// 保单生效时间,暂且定位当前生效时间
										+ "  ,\'" + sf.format(order_info.pre_time) + "\' "// 保单生效时间,出发时间
										+ "  ,\'" + sf1.format(new Date())+" 23:59:59" + "\' "// 保单失效时间,暂且定位1天
										+ "  ,502000.00 "// 保额，暂且定为2000.00元
										+ "  ,0.5 "// 合计保费，从参数表中读取insu_fee的值,先暂时写死为0.5元
										+ "  ,0 "// 保单状态，0有效，1失效
									
										+ "  ,"+driverInfo.id// 投保人id，指车主
										+ "  ,\'"+driverInfo.username+"\' "// 投保人姓名，即车主姓名
										+ "  ,\'"+(driverInfo.sex==0?"M":"F")+"\'"// 投保人性别，车主性别
									
										+ "  ,\'"+ApiGlobal.Date2String(driverInfo.birthday, false)+"\' "// 投保人生日，车主生日
										+ "  ,\'I\'"// 投保人证件类型 车主身份证件类型
										+ "  ,\'"+driverInfo.id_card_num+"\'"// 投保人证件号码，车主证件号码
									
										+ "  ,"+driverInfo.id// 被保人id
										+ "  ,\'"+driverInfo.username+"\' "// 被保人姓名
										+ "  ,\'"+(driverInfo.sex==0?"M":"F")+"\'"// 被保人性别
										+ "  ,\'"+ApiGlobal.Date2String(driverInfo.birthday, false)+"\' "// 被保险人生日
										+ "  ,\'I\'"// 被保险人证件类型
										+ "  ,\'"
										+ driverInfo.id_card_num
										+ "\'"// 被保险人证件号码

										+ "  ,\'SA01\'"// 请求类型
										+ "  ,\'"
										+ sf.format(new Date())
										+ "\'"// 请求时间
										+ "  ,\'tsNo"
										+ new Date().getTime()
										+ "\' "// 交易流水号，合作方自己定义
									+ "  ,\'HAOTANG\'"// 保险公司分配给合作方的账号
									+ "  , \'HAOTANG365\'"// 保险公司分配给合作方的密码

									+ "  ,\'C\' "// 承包产品类型
									+ "  , \'PC_D01\' "// 承包产品代码
									+ "  , \'110108\' "// 业务受理机构
									+ "  ,\'OA\' "// 销售渠道
									+ "  ,\'8008\' "// 网店代码
									+ "  ,1"// 受益顺序
									+ "  ,1"// 被保险人数
									+ "  ,1"// 被保险人顺序
									+ "  ,1"// 被保险人类型
									+ "  ,\'X\' "// 投保人和被保人关系，司乘
										+ " ," + exec_id + ")";
							System.out
										.println("SVCOrderService.confirmOnceOrder()给车主买保险--->"
												+ szQuery);
								if (stmt.executeUpdate(szQuery,
									Statement.RETURN_GENERATED_KEYS) == 1) {// 向保单表车如数据成功

								ResultSet genKeys = stmt.getGeneratedKeys();
								if (genKeys.next())
									insu_id = genKeys.getLong(1);
								System.out.println("insu_id======="+insu_id);
							}
							
							if (insu_id>0) {
								System.out.println("insu_id ----"+insu_id);
								szQuery="SELECT appl_no from insurance WHERE id = "+insu_id;
								
								 rs2 = stmt2.executeQuery(szQuery);
								 System.out.println("szq====="+szQuery);
								 
								 //String appl_no = rs2.getString("APPL_NO");
								 if (rs2.next()) {
									
									  appl_no = rs2.getString(1);
								}
								 rs2.close();
								
								
								szQuery="INSERT INTO insurance_dtd ("
										+" insu_id"
										+" ,APPL_NO"
										+" ,oper_type"
										+" ,oper_time"
										+" ,insu_sum"
										
										+ ") "
										+"values"
										+"("
										
										+insu_id
										+" , \'"+appl_no+"\'"
										+" ,0"
										+" ,\'" +sf.format(new Date())+"\' "
										+" ,502000.00" 
										+")";
								System.out.println("向流水表中插入数据"+szQuery);
								stmt.executeUpdate(szQuery);
								
								szQuery="UPDATE order_exec_cs SET insu_id_driver = "+ insu_id +" WHERE id ="+exec_id;
								System.out.println("将insu_id更新到order_exec_cs表中"+szQuery);
								stmt.executeUpdate(szQuery);
							}

								/*
								 * else {
								 * 
								 * result.retcode = ConstMgr.ErrCode_Exception;
								 * result.retmsg = ConstMgr.ErrMsg_Exception +
								 * "(12)"; }
						*/

					        }

							// 判断乘客是否验证了身份，如果验证了并且满18周岁，就给买保险
							if (userinfo.person_verified == 1
									&& (userinfo.username != "" || userinfo.username != null)&&(ApiGlobal.getAgeByBirthday(userinfo.birthday)>=18)) {
								SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								SimpleDateFormat sf3 = new SimpleDateFormat("yyyy-MM-dd");
						        Calendar c2 = Calendar.getInstance();
								// System.out.println("当前日期：       "+sf.format(c.getTime()));
						        c2.add(Calendar.DAY_OF_MONTH, 1);
								szQuery = "INSERT INTO insurance " + "("
										+ "	insurance.APPL_NO,"// 投保单号
											+ "  insurance.APPL_TIME,"// 投保时间，当前时间
											+ "  insurance.EFFECT_TIME,"// 保单生效时间
											+ "  insurance.INSEXPR_DATE,"// 保单失效时间
											+ "  insurance.TOTAL_AMOUNT,"// 保额
											+ "  insurance.TOTAL_PREMINUM,"// 合计保费，insu_fee
											+ "  insurance.INSU_STATUS,"// 保单状态

											+ "  insurance.APPL_ID,"// 投保人id
											+ "  insurance.APPL_NAME,"// 投保人姓名
											+ "  insurance.APPL_SEX,"// 投保人性别
											+ "  insurance.APPL_BIRTHDAY,"// 投保人生日
											+ "  insurance.CERT_TYPE,"// 投保人证件类型
											+ "  insurance.CERT_CODE,"// 投保人证件号码

											+ "  insurance.ISD_ID,"// 被保人id
											+ "  insurance.ISD_NAME,"// 被保人姓名
											+ "  insurance.ISD_SEX,"// 被保人性别
											+ "  insurance.ISD_BIRTHDAY,"// 被保险人生日
											+ "  insurance.ISD_CERT_TYPE,"// 被保险人证件类型
											+ "  insurance.ISD_CERT_CODE,"// 被保险人证件号码

											+ "  insurance.REQUEST_TYPE,"// 请求类型
											+ "  insurance.REQUSET_TIME,"// 请求时间
											+ "  insurance.REQUEST_TRANSNO,"// 交易流水号，合作方自己定义
											+ "  insurance.USER,"// 保险公司分配给合作方的账号
											+ "  insurance.PASSWORD,"// 保险公司分配给合作方的密码

											+ "  insurance.PROD_TYPE,"// 承包产品类型
											+ "  insurance.PROD_CODE,"// 承包产品代码
											+ "  insurance.BUS_BRANCH,"// 业务受理机构
											+ "  insurance.SALE_CHANNEL,"// 销售渠道
											+ "  insurance.COUNTER_CODE,"// 网店代码
											+ "  insurance.BENEF_ORDER,"// 受益顺序
											+ "  insurance.ISD_COUNT,"// 被保险人数
											+ "  insurance.ISD_SERIAL,"// 被保险人顺序
											+ "  insurance.ISD_TYPE,"// 被保险人类型
											+ "  insurance.APPL_RELATION,"// 投保人和被保人顺序，司乘
											+ "  insurance.ORDER_EXEC_ID "// 投保人和被保人顺序，司乘
										+ ") " + "VALUES ("

										+ "	 \'OOKP" + new Date().getTime() + "\' "// 投保单号 ，格式ht201411111648
										+ " , \'" + sf2.format(new Date()) + "\' "// 投保时间，当前时间
										+ "  ,\'" + sf2.format(order_info.pre_time) + "\' "// 保单生效时间,暂且定位当前生效时间
										//+ "  ,\'" + sf2.format(c2.getTime()) + "\' "// 保单失效时间,暂且定位1天
										+ "  ,\'" + sf3.format(new Date()) + " 23:59:59"+"\' "// 保单失效时间,暂且定位1天
										+ "  ,502000.00 "// 保额，暂且定为2000.00元
										+ "  ,0.5 "// 合计保费，从参数表中读取insu_fee的值,先暂时写死为0.5元
										+ "  ,0 "// 保单状态，0有效，1失效
										
										+ "  ,"+driverInfo.id// 投保人id，指车主
										+ "  ,\'"+driverInfo.username+"\' "// 投保人姓名，即车主姓名
										+ "  ,\'"+(driverInfo.sex==0?"M":"F")+"\'"// 投保人性别，车主性别
										+ "  ,\'"+ApiGlobal.Date2String(driverInfo.birthday, false)+"\' "// 投保人生日，车主生日
										+ "  ,\'I\'"// 投保人证件类型 车主身份证件类型
										+ "  ,\'"+driverInfo.id_card_num+"\'"// 投保人证件号码，车主证件号码
										
										+ "  ,"+userinfo.id// 被保人id
										+ "  ,\'"+userinfo.username+"\' "// 被保人姓名
										+ "  ,\'"+(userinfo.sex==0?"M":"F")+"\'"// 被保人性别
										+ "  ,\'"+ApiGlobal.Date2String(userinfo.birthday, false)+"\' "// 被保险人生日
										+ "  ,\'I\'"// 被保险人证件类型
										+ "  ,\'"
										+ userinfo.id_card_num
										+ "\'"// 被保险人证件号码

										+ "  ,\'SA01\'"// 请求类型
										+ "  ,\'" + sf2.format(new Date()) + "\'"// 请求时间
										+ "  ,\'tsNo" + new Date().getTime() + "\' "// 交易流水号，合作方自己定义
										+ "  ,\'HAOTANG\'"// 保险公司分配给合作方的账号
										+ "  , \'HAOTANG365\'"// 保险公司分配给合作方的密码

										+ "  ,\'C\' "// 承包产品类型
										+ "  , \'PC_P01\' "// 承包产品代码
										+ "  , \'110108\' "// 业务受理机构
										+ "  ,\'OA\' "// 销售渠道
										+ "  ,\'8008\' "// 网店代码
										+ "  ,1"// 受益顺序
										+ "  ,1"// 被保险人数
										+ "  ,1"// 被保险人顺序
										+ "  ,1"// 被保险人类型
										+ "  ,\'X\' "// 投保人和被保人关系，司乘
										+ " ," + exec_id + ")";
								System.out
										.println("SVCOrderService.confirmOnceOrder()给乘客买保险--->"
												+ szQuery);
								if (stmt.executeUpdate(szQuery,
										Statement.RETURN_GENERATED_KEYS) == 1) {// 向保单表车如数据成功

									ResultSet genKeys = stmt.getGeneratedKeys();
									if (genKeys.next())
										insu_id = genKeys.getLong(1);
								}
								
								if (insu_id>0) {
									
									
									szQuery="SELECT appl_no from insurance WHERE id = "+insu_id;
									
									rs2 = stmt2.executeQuery(szQuery);
									String appl_no = null;
									if (rs2.next()) {
										appl_no = rs2.getString("appl_no");
									}
									rs2.close();
									
									szQuery="INSERT INTO insurance_dtd ("
											+" insu_id"
											+" ,appl_no"
											+" ,oper_type"
											+" ,oper_time"
											+" ,insu_sum"
											
											+ ") "
											+"values"
											+"("
											
											+insu_id
											+" ,\'"+appl_no+"\'"
											+" ,0"
											+" ,\'" +sf2.format(new Date())+"\' "
											+" ,502000.00" 
											+")";
									System.out.println("向流水表中插入数据"+szQuery);
									stmt.executeUpdate(szQuery);
									
									szQuery="UPDATE order_exec_cs SET insu_id_pass = "+ insu_id +" WHERE id ="+exec_id;
									System.out.println("将insu_id更新到order_exec_cs表中"+szQuery);
									stmt.executeUpdate(szQuery);
								}
								/*
								 * else {
								 * 
								 * result.retcode = ConstMgr.ErrCode_Exception;
								 * result.retmsg = ConstMgr.ErrMsg_Exception +
								 * "(12)"; }
								 */

							}
							/*
							 * else { result.retcode =
							 * ConstMgr.ErrCode_Exception; result.retmsg =
							 * ConstMgr.ErrMsg_Exception + "(12)"; }
							 */
							// -----end modify by chuzhiqiang
							// 接受订单后向数据库中插入保险相关数据----------------------

						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(17)";
						}
					}
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
				//modify  by  chuzhiqiang
				if (stmt2 != null) {
					try {
						stmt2.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}
				//modify  by  chuzhiqiang
				// Close db connection
				if (dbConn != null) {
					try {
						dbConn.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,如果乘客没有确认车主接单，更新order_temp_grab表里所有该订单的相应记录状态为失败(这里只更新了一条，应该多条)
	 * 1.1如果已经有车主抢单，则更新该抢单记录的状态为失败
	 * 1.2因为乘客还没有确认车主接单，所以该条记录可以直接在order_temp_details表标记为已删除
	 * ，第3步骤还会在此基础上把status字段改为已关闭，所以乘客还没有确认车主抢单的情况，最后这条记录deleted=0同时status=8
	 * 2,乘客已经确认了车主接单 2.1获取相应的执行订单 2.2将此执行订单状态设置为取消状态 3,设置该单次订单记录为已关闭
	 * 4,车主还没有到达的时候，不用罚乘客的绿点，只获取冻结乘客的金额，并将其返还给乘客 5,解冻乘客的金额，将其冻结的金额加入到ts表中
	 * 6,更新订单的所有notify记录为已删除 7,车主已经到达了，乘客取消订单属于违约，要扣乘客的绿点(但这里并没有扣绿点，留给管理员来扣)
	 * 7.1设置该订单所有通知的车主为取消(更新order_temp_notify表状态)
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult cancelOnceOrder(String source, long userid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
/*			Statement stmt2 = null;
			ResultSet rs2 = null;*/
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();

				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);
				SVCOrderExecCS exec_info = null;

				// Get grab info
				SVCOrderTempGrab grab_info = ApiGlobal
						.getLatestGrabInfoFromOrderID(dbConn, orderid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) {
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status == 8) // Already cancelled
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else {
					stmt = dbConn.createStatement();

					String szCancelTime = ApiGlobal.Date2String(new Date(),
							true);
					// 1,如果乘客没有确认车主接单，更新order_temp_grab表里所有该订单的相应记录状态为失败(这里只更新了一条，应该多条)
					if (order_info.status == 1) // Not accepted yet
					{
						if (grab_info != null) {// 1.1如果已经有车主抢单，则更新该抢单记录的状态为失败
							szQuery = "UPDATE order_temp_grab SET status=2 WHERE id="
									+ grab_info.id;
							stmt.executeUpdate(szQuery);
						}
						// 1.2因为乘客还没有确认车主接单，所以该条记录可以直接在order_temp_details表标记为已删除，第3步骤还会在此基础上把status字段改为已关闭，所以乘客还没有确认车主抢单的情况，最后这条记录deleted=0同时status=8
						szQuery = "UPDATE order_temp_details SET deleted=1 WHERE id="
								+ order_info.id;
						stmt.executeUpdate(szQuery);

						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
						ApiGlobal.logMessage(result.encodeToJSON().toString());

						return result;
					} else // Already accepted //2,乘客已经确认了车主接单
					{
						// 2.1获取相应的执行订单
						// Get order exec info
						szQuery = "SELECT * FROM order_exec_cs WHERE id="
								+ order_info.order_cs_id;
						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();

						if (exec_info != null) {
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							// 2.2将此执行订单状态设置为取消状态
							szQuery = "UPDATE order_exec_cs SET status=8,pass_cancel_time=\""
									+ szCancelTime
									+ "\" WHERE id="
									+ exec_info.id;
							
							//stmt.executeUpdate(szQuery);
							
							//------begain  modify by chuzhiqiang -------------------------
							if (stmt.executeUpdate(szQuery)==1) {//如果执行订单状态修改成功
								
							
							long insu_id_pass = exec_info.insu_id_pass;
							szQuery="UPDATE insurance SET insu_status = 1 WHERE ORDER_EXEC_ID = "+exec_info.id;
							System.out.println("单次  撤保 更新保单状态"+szQuery);
							
							stmt.executeUpdate(szQuery);
							
							System.out.println("insu_id ----"+insu_id_pass);
							szQuery="SELECT appl_no from insurance WHERE id = "+insu_id_pass;//根据乘客保单id获得
							
							 rs = stmt.executeQuery(szQuery);
							 
							 System.out.println("szq====="+szQuery);
							 String appl_no = null;
							 //String appl_no = rs2.getString("APPL_NO");
							 if (rs.next()) {
								
								  appl_no = rs.getString(1);
							}
							 rs.close();
							

								szQuery = "INSERT INTO insurance_dtd ("
										+ " insu_id"
										+ " ,APPL_NO"
										+ " ,oper_type"// 1投保,2撤保
										+ " ,oper_time" + " ,insu_sum" + ") "
										+ "values" 
										+ "(" 
										+ insu_id_pass 
										+ " ,\'"+appl_no+"\'"
										+ " ,1" 
										+ " ,\'"
										+ sf.format(new Date()) + "\' "
										+ " ,200000.00" + ")";

								System.out.println("单次 撤保 车主 " + szQuery);
							stmt.executeUpdate(szQuery);

							long insu_id_driver = 0;

								szQuery = "SELECT id,user FROM insurance WHERE ORDER_EXEC_ID = "
										+ exec_info.id
										+ " AND ISD_ID ="
										+ exec_info.driver;
								System.out.println("获得车主保单id----" + szQuery);

								rs = stmt.executeQuery(szQuery);
								if (rs.next()) {
									insu_id_driver = rs.getLong("id");
								}
								rs.close();
								
								System.out.println("insu_id_driver ----"+insu_id_driver);
								szQuery="SELECT appl_no from insurance WHERE id = "+insu_id_driver;//根据乘客保单id获得
								rs = stmt.executeQuery(szQuery);
								
								 
								 System.out.println("szq====="+szQuery);
								 //String appl_no = null;
								 //String appl_no = rs2.getString("APPL_NO");
								 if (rs.next()) {
									
									  appl_no = rs.getString("appl_no");
								}
								 rs.close();
								

								

								szQuery = "INSERT INTO insurance_dtd ("
										+ " insu_id"
										+ " ,APPL_NO"
										+ " ,oper_type"// 1投保,2撤保
										+ " ,oper_time" + " ,insu_sum" + ") "
										+ "values"
										+ "("
										+ insu_id_driver// 车主的保单id
										+ " ,\'"+appl_no+"\'" 
										+ " ,1" 
										+ " ,\'"
										+ sf.format(new Date()) + "\' "
										+ " ,200000.00" + ")";
								System.out.println("单次 撤保 乘客 " + szQuery);
							stmt.executeUpdate(szQuery);
							}
							// ------end modify by chuzhiqiang---------------------------

						}

					}

					// 3,设置该单次订单记录为已关闭
					// Update order_temp_details information
					szQuery = "UPDATE order_temp_details SET status=8,pass_cancel_time=\""
							+ szCancelTime + "\" WHERE id=" + order_info.id;
					if (stmt.executeUpdate(szQuery) == 1) {// 车主还没有到达的时候，不用罚乘客的绿点
						if (order_info.status == 1 || order_info.status == 2
								|| order_info.status == 3) // Driver not
															// arrived. No need
															// to decrease
															// balance. Return
															// to passenger
						{
							// 4,车主还没有到达的时候，不用罚乘客的绿点，只获取冻结乘客的金额，并将其返还给乘客
							SVCFreezePoints freezePoints = ApiGlobal
									.getLatestFreezeLogForOrder(dbConn, userid,
											order_info.id, 1,
											exec_info == null ? 0
													: exec_info.id);
							// 5,解冻乘客的金额，将其冻结的金额加入到ts表中
							if (freezePoints != null
									&& ApiGlobal.releasePointsForUser(dbConn,
											source, userid, freezePoints.id) > 0) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;

								// 6,更新订单的所有notify记录为已删除
								szQuery = "UPDATE order_temp_notify SET deleted=1 WHERE order_id="
										+ order_info.id;
								stmt.executeUpdate(szQuery);

								// Send push notification to driver
								Calendar cal = Calendar.getInstance();
								cal.setTime(order_info.pre_time);

								String szMsg = "您"
										+ cal.get(Calendar.HOUR_OF_DAY) + "点"
										+ cal.get(Calendar.MINUTE) + "分从"
										+ order_info.start_addr + "前往"
										+ order_info.end_addr
										+ "的单词拼车已被乘客取消，前往我的订单可查看详情";
								STPushNotificationData data = new STPushNotificationData();

								data.title = ConstMgr.STR_DINGDANXIAOXI;
								data.description = szMsg;

								STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
								custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
								custom_data.orderid = order_info.id;
								custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
								custom_data.ordertype = ConstMgr.ORDER_ONCE;

								data.custom_data = custom_data;

								ApiGlobal.addOrderNotification(dbConn,
										exec_info.driver, orderid, szMsg);
								ApiGlobal.sendPushNotifToUser(dbConn,
										exec_info.driver, data);
								// //////////////////////////////////////////////////////////////////////////////////////////

							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(18)";
							}
						} else // Order process is already started. Driver will
								// declare this behavior and the passenger will
								// be recorded as abnormal behavior
						{// 7,车主已经到达了，乘客取消订单属于违约，要扣乘客的绿点(但这里并没有扣绿点，留给管理员来扣)
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							// 7.1设置该订单所有通知的车主为取消(更新order_temp_notify表状态)
							szQuery = "UPDATE order_temp_notify SET deleted=1 WHERE order_id="
									+ order_info.id;
							stmt.executeUpdate(szQuery);

							// Send push notification to driver
							Calendar cal = Calendar.getInstance();
							cal.setTime(order_info.pre_time);

							String szMsg = "您" + cal.get(Calendar.HOUR_OF_DAY)
									+ "点" + cal.get(Calendar.MINUTE) + "分从"
									+ order_info.start_addr + "前往"
									+ order_info.end_addr
									+ "的单词拼车已被乘客取消，前往我的订单可查看详情";
							STPushNotificationData data = new STPushNotificationData();

							data.title = ConstMgr.STR_DINGDANXIAOXI;
							data.description = szMsg;

							STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
							custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
							custom_data.orderid = order_info.id;
							custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
							custom_data.ordertype = ConstMgr.ORDER_ONCE;

							data.custom_data = custom_data;

							ApiGlobal.addOrderNotification(dbConn,
									exec_info.driver, orderid, szMsg);
							ApiGlobal.sendPushNotifToUser(dbConn,
									exec_info.driver, data);
							// //////////////////////////////////////////////////////////////////////////////////////////
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(19)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,更新order_onoffduty_grab表，将当前车主标记为优胜者。
	 * 2，将生成的上下班订单插入到order_onoffduty_divide中。 3，将生成的上下班订单进一步插入到表order_exec_cs中。
	 * 4，插入order_onoffduty_exec_details表一条记录，
	 * 此记录把order_exec_cs和order_onoffduty_divide连接起来
	 * ，感觉这个表没有用处，可以直接合并到order_onoffduty_divide里来。
	 * 5，更新order_onoffduty_details表的状态。
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param password
	 * @param devtoken
	 * @return
	 */
	public SVCResult confirmOnOffOrder(String source, long userid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Get grab info
				SVCOrderOnOffDutyGrab grab_info = ApiGlobal
						.onoffOrderIsAcceptedByDriver(dbConn, orderid);

				double cur_bal = ApiGlobal.getUserBalance(dbConn, userid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 1) // Order is already accepted
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else if (grab_info == null) // No grab information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoGrabInfo;
				} else if (cur_bal < order_info.price) {
					result.retcode = ConstMgr.ErrCode_NotEnoughBalance;
					result.retmsg = ConstMgr.ErrMsg_NotEnoughBalance;
				} else {

					stmt = dbConn.createStatement();

					// Update grab log
					szQuery = "UPDATE order_onoffduty_grab SET status=1 WHERE status=0 AND deleted=0 AND id="
							+ grab_info.id;
					if (stmt.executeUpdate(szQuery) != 1) // Update failure
					{
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(20)";
					} else {
						// Get mid points information
						String szMidPoints = "";
						// mobile
						szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=1 AND orderid="
								+ order_info.id;
						rs = stmt.executeQuery(szQuery);
						while (rs.next()) {
							if (!szMidPoints.equals(""))
								szMidPoints += ",";
							szMidPoints += "" + rs.getDouble("lat") + ","
									+ rs.getDouble("lng") + ","
									+ rs.getString("addr");
						}
						rs.close();

						// Insert order_onoffduty_divide log
						szQuery = "INSERT INTO order_onoffduty_divide ("
								+ "which_days," + "orderdetails_id,"
								+ "driver_id" + ") VALUES (\"" + grab_info.days
								+ "\", " + order_info.id + ","
								+ grab_info.driverid + ")";

						if (stmt.executeUpdate(szQuery,
								Statement.RETURN_GENERATED_KEYS) == 1) {
							long divide_id = 0;
							rs = stmt.getGeneratedKeys();
							if (rs.next())
								divide_id = rs.getLong(1);
							rs.close();

							if (divide_id > 0) {
								// Insert order_exec_cs log
								long exec_id = 0;

								Date pre_time = ApiGlobal.nextValidDay(
										grab_info.days.split(","), null,
										order_info.pre_time);
								String szCurTime = ApiGlobal.Date2String(
										new Date(), true);

								szQuery = "INSERT INTO order_exec_cs ("
										+ "order_type," + "passenger,"
										+ "driver," + "from_," + "to_,"
										+ "average_price_platform," + "price,"
										+ "pre_time," + "remark," + "cr_date,"
										+ "ti_accept_order," + "city_from,"
										+ "city_to,"
										+ "has_evaluation_passenger,"
										+ "has_evaluation_driver,"
										+ "password," + "begin_lat,"
										+ "begin_lng," + "end_lat,"
										+ "end_lng," + "freeze_points,"
										+ "total_distance," + "order_city,"
										+ "status," + "deleted" + ") VALUES ("
										+ "3,"
										+ order_info.publisher
										+ ","
										+ grab_info.driverid
										+ ",\""
										+ order_info.start_addr
										+ "\",\""
										+ order_info.end_addr
										+ "\","
										+ ApiGlobal
												.getDistAndSystemAveragePrice(
														dbConn,
														userid,
														ApiGlobal
																.cityCode2Name(
																		dbConn,
																		order_info.order_city),
														ApiGlobal
																.Date2String(
																		order_info.pre_time,
																		true),
														order_info.start_lat,
														order_info.start_lng,
														order_info.end_lat,
														order_info.end_lng,
														szMidPoints,
														order_info.reqcarstyle)
												.getDouble("price")
										+ ","
										+ order_info.price
										+ ",\""
										+ ApiGlobal.Date2String(pre_time, true)
										+ "\",\""
										+ order_info.remark
										+ "\",\""
										+ szCurTime
										+ "\",\""
										+ szCurTime
										+ "\",\""
										+ order_info.order_city
										+ "\",\""
										+ order_info.order_city
										+ "\",0,0,\""
										+ "0000" // Set default password '0000'
										+ "\","
										+ order_info.start_lat
										+ ","
										+ order_info.start_lng
										+ ","
										+ order_info.end_lat
										+ ","
										+ order_info.end_lng
										+ ","
										+ order_info.price
										+ ",0,\""
										+ order_info.order_city + "\",2,0)";

								if (stmt.executeUpdate(szQuery,
										Statement.RETURN_GENERATED_KEYS) == 1) {
									rs = stmt.getGeneratedKeys();
									if (rs.next())
										exec_id = rs.getLong(1);
									rs.close();
								}

								if (exec_id > 0) // Insert order_exec_cs log
													// succeed
								{
									// Insert order_onoffduty_exec_details log
									szQuery = "INSERT order_onoffduty_exec_details ("
											+ "onoffduty_divide_id,"
											+ "order_cs_id,"
											+ "deleted"
											+ ") VALUES ("
											+ divide_id
											+ ","
											+ exec_id + ", 0)";
									if (stmt.executeUpdate(szQuery) == 1) {
										// Update order_onoffduty_details info
										String arr_days[] = grab_info.days
												.split(",");
										String req_days[] = order_info.leftdays
												.split(",");

										String leftDays = "";
										int all_accepted = 0;

										for (int i = 0; i < req_days.length; i++) {
											String dayItem = req_days[i];
											boolean isExist = false;
											for (int j = 0; j < arr_days.length; j++) {
												if (dayItem.equals(arr_days[j])) {
													isExist = true;
													break;
												}
											}

											if (!isExist) {
												if (!leftDays.equals(""))
													leftDays += ",";
												leftDays += dayItem;
											}
										}

										if (leftDays.equals(""))
											all_accepted = 1;

										szQuery = "UPDATE order_onoffduty_details SET alldays_beaccepted="
												+ all_accepted
												+ ", leftdays=\""
												+ leftDays
												+ "\",status=2 ,ti_accept_order=\""
												+ szCurTime
												+ "\" WHERE id="
												+ order_info.id;

										if (stmt.executeUpdate(szQuery) == 1) {
											szQuery = "SELECT "
													+ "		user.*,"
													+ "		user_car.brand,"
													+ "		user_car.style,"
													+ "		user_car.color,"
													+ "		user_car.car_img,"
													+ "		user_car.plate_num "

													+ "FROM user "

													+ "LEFT JOIN  user_car "
													+ "ON user_car.userid=user.id "

													+ "WHERE user.id="
													+ grab_info.driverid;

											rs = stmt.executeQuery(szQuery);
											if (rs.next()) {
												// Insert new log in ts table
												if (ApiGlobal
														.freezePointsWithType(
																dbConn,
																source,
																userid,
																0,
																orderid,
																2,
																order_info.price,
																ConstMgr.txcode_userFreezeBalance) > 0) {
													result.retcode = ConstMgr.ErrCode_None;
													result.retmsg = ConstMgr.ErrMsg_None;

													// Passenger confirm this
													// order, must notify driver
													{
														String szMsg = "您接单的上下班订单"
																+ order_info.order_num
																+ "的乘客已经确认订单啦. 请你查询一下";
														ApiGlobal
																.addOrderNotification(
																		dbConn,
																		grab_info.driverid,
																		order_info.publisher,
																		szMsg);

														STPushNotificationData data = new STPushNotificationData();
														data.title = ConstMgr.STR_DINGDANXIAOXI;
														data.description = szMsg;

														ApiGlobal
																.sendPushNotifToUser(
																		dbConn,
																		grab_info.driverid,
																		data);
													}

													// Search all the on off
													// order for this driver.
													// If start time and days
													// are overlapped, must
													// notify
													rs.close();

													String szOrderIDs = "", szDivideIDs = "";
													ArrayList<Long> arrOrderIDs = new ArrayList<Long>();
													ArrayList<Long> arrDivideIDs = new ArrayList<Long>();
													ArrayList<Long> arrUserIDs = new ArrayList<Long>();
													ArrayList<String> arrOrderNums = new ArrayList<String>();

													String[] arrCurDays = grab_info.days
															.split(",");
													Calendar curPreTime = Calendar
															.getInstance();
													curPreTime
															.setTime(order_info.pre_time);

													String[] arrPresetDays = null;
													Calendar prevPreTime = Calendar
															.getInstance();
													Date dtTemp = null;

													szQuery = "SELECT "
															+ "		order_onoffduty_details.id,"
															+ "		order_onoffduty_details.order_num,"
															+ "		order_onoffduty_details.publisher,"
															+ "		order_onoffduty_details.pre_time,"
															+ "		order_onoffduty_divide.id AS divideid,"
															+ "		order_onoffduty_divide.driver_id,"
															+ "		order_onoffduty_divide.which_days "

															+ "FROM order_onoffduty_details "

															+ "INNER JOIN order_onoffduty_divide "
															+ "ON "
															+ "		order_onoffduty_divide.orderdetails_id="
															+ "		order_onoffduty_details.id "

															+ "WHERE "
															+ "		order_onoffduty_details.status=2 AND "
															+ "		order_onoffduty_divide.driver_id="
															+ grab_info.driverid
															+ " AND "
															+ "		order_onoffduty_details.deleted=0 AND "
															+ "		order_onoffduty_divide.deleted=0 AND "
															+ "		order_onoffduty_details.id<>"
															+ order_info.id;

													rs = stmt
															.executeQuery(szQuery);
													while (rs.next()) {
														if (!szOrderIDs
																.equals(""))
															szOrderIDs += ",";

														if (!szDivideIDs
																.equals(""))
															szDivideIDs += ",";

														String szWhichDays = rs
																.getString("which_days");
														if (szWhichDays == null
																|| szWhichDays
																		.equals(""))
															continue;

														arrPresetDays = szWhichDays
																.split(",");

														// Time condition
														dtTemp = ApiGlobal
																.String2Date(rs
																		.getString("pre_time"));
														prevPreTime
																.setTime(dtTemp);

														prevPreTime
																.set(Calendar.YEAR,
																		curPreTime
																				.get(Calendar.YEAR));
														prevPreTime
																.set(Calendar.MONTH,
																		curPreTime
																				.get(Calendar.MONTH));
														prevPreTime
																.set(Calendar.DAY_OF_MONTH,
																		curPreTime
																				.get(Calendar.DAY_OF_MONTH));

														Calendar tmpCal = prevPreTime;
														if (tmpCal
																.before(curPreTime)) {
															Calendar tmpUpBnd = tmpCal;
															tmpUpBnd.add(
																	Calendar.HOUR,
																	30);
															if (tmpUpBnd
																	.before(curPreTime))
																continue;
														} else {
															Calendar tmpLowBnd = tmpCal;
															tmpLowBnd
																	.add(Calendar.HOUR,
																			-30);
															if (tmpLowBnd
																	.after(curPreTime))
																continue;
														}
														// ////////////////////////////////////////////////////////////////////

														// Day
														// condition(ChuXingRi)
														boolean isOverlap = false;
														for (int i = 0; i < arrPresetDays.length; i++) {
															for (int j = 0; j < arrCurDays.length; j++) {
																if (arrPresetDays
																		.equals(arrCurDays[j])) {
																	isOverlap = true;
																	break;
																}
															}

															if (isOverlap)
																break;
														}

														if (!isOverlap)
															continue;
														// ////////////////////////////////////////////////////////////////////

														szOrderIDs += rs
																.getLong("id");
														arrOrderIDs.add(rs
																.getLong("id"));

														szDivideIDs += rs
																.getLong("divideid");
														arrDivideIDs
																.add(rs.getLong("divideid"));

														arrOrderNums
																.add(rs.getString("order_num"));

														arrUserIDs
																.add(rs.getLong("publisher"));
													}
													rs.close();

													if (arrOrderIDs.size() == 0)
													{
														ApiGlobal.logMessage(result.encodeToJSON().toString());

														return result;
													}

													// Update
													// order_onoffduty_divide
													szQuery = "UPDATE order_onoffduty_divide "
															+ "SET deleted=1 "
															+ "WHERE id IN ("
															+ szDivideIDs + ")";
													stmt.executeUpdate(szQuery);

													// Update
													// order_onoffduty_details
													szQuery = "UPDATE order_onoffduty_details "
															+ "SET status=1 "
															+ "WHERE id IN ("
															+ szOrderIDs + ")";
													stmt.executeUpdate(szQuery);

													SVCUser driver_info = ApiGlobal
															.getUserInfoFromUserID(
																	dbConn,
																	grab_info.driverid);

													// Send push notification
													// and order notification
													for (int i = 0; i < arrOrderIDs
															.size(); i++) {
														String szMsg = "回应您上下班订单"
																+ arrOrderNums
																		.get(i)
																+ "的车主"
																+ driver_info.nickname
																+ "接了同一时间其他订单，无法再接您的订单，您的订单自动回到待接单状态";
														ApiGlobal
																.addOrderNotification(
																		dbConn,
																		arrUserIDs
																				.get(i),
																		arrOrderIDs
																				.get(i),
																		szMsg);

														STPushNotificationData data = new STPushNotificationData();
														data.title = ConstMgr.STR_DINGDANXIAOXI;
														data.description = szMsg;

														ApiGlobal
																.sendPushNotifToUser(
																		dbConn,
																		arrUserIDs
																				.get(i),
																		data);
													}
												} else {
													result.retcode = ConstMgr.ErrCode_Exception;
													result.retmsg = ConstMgr.ErrMsg_Exception
															+ "(5)";
												}
											} else {
												result.retcode = ConstMgr.ErrCode_Normal;
												result.retmsg = ConstMgr.ErrMsg_NoDrvInfo;
											}
											rs.close();
										} else {
											result.retcode = ConstMgr.ErrCode_Exception;
											result.retmsg = ConstMgr.ErrMsg_Exception
													+ "(21)";
										}
									} else {
										result.retcode = ConstMgr.ErrCode_Exception;
										result.retmsg = ConstMgr.ErrMsg_Exception
												+ "(22)";
									}
								} else {
									result.retcode = ConstMgr.ErrCode_Exception;
									result.retmsg = ConstMgr.ErrMsg_Exception
											+ "(23)";
								}
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(24)";
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(25)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,更新order_onoffduty_grab表，设置状态，感觉处理的不对。还是应该对上下班的接单实现逻辑和金学敏讨论一下。
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult refuseOnOffOrder(String source, long userid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Get grab info
				SVCOrderOnOffDutyGrab grab_info = ApiGlobal
						.onoffOrderIsAcceptedByDriver(dbConn, orderid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) // User
				// is
				// recorded
				// in
				// black
				// list
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 1) // Order is already accepted
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else if (grab_info == null) // No grab information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoGrabInfo;
				} else {

					stmt = dbConn.createStatement();

					// Update grab log
					szQuery = "UPDATE order_onoffduty_grab SET status=2 WHERE status=0 AND deleted=0 AND id="
							+ grab_info.id;
					if (stmt.executeUpdate(szQuery) != 1) // Update failure
					{
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(81)";
					} else {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,设置order_onoffduty_details表状态为取消。 2，查询出所有order_exec_cs表中的相关记录
	 * 3，将2步查询出的记录设置取消状态。 4,有个问题，需要userid参数吗？
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult cancelOnOffOrder(String source, long userid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
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
			dbConn = DBManager.getDBConnection();
			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn, userid);

			// Get order info
			SVCOrderOnOffDutyDetails order_info = ApiGlobal.getOnOffOrderInfo(
					dbConn, orderid);

			// driver id
			long driver_id = 0;

			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (!userinfo.device_token.equals(devtoken)) // Device
																// token not
																// match
			{
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (order_info == null) // No order info
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			stmt = dbConn.createStatement();

			if (order_info.status == 1) {
				szQuery = "SELECT COUNT(*) AS cnt "
						+ "FROM order_onoffduty_grab " + "WHERE "
						+ "		status=0 AND " + "		deleted=0 AND "
						+ "		order_id=" + order_info.id;
				rs = stmt.executeQuery(szQuery);
				rs.next();
				if (rs.getInt("cnt") > 0) {
					// At least one driver is now accepted this order.
					// Passenger cannot cancel this order.
					// Only can refuse.
					// After that, this order can be cancelled.
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
					ApiGlobal.logMessage(result.encodeToJSON().toString());

					return result;
				}
				rs.close();

				szQuery = "UPDATE order_onoffduty_details SET deleted=1 WHERE id="
						+ order_info.id;
				stmt.executeUpdate(szQuery);

				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			// Set cancel flag
			szQuery = "UPDATE order_onoffduty_details SET status=3 WHERE id="
					+ order_info.id;
			if (stmt.executeUpdate(szQuery) != 1) // Update failure
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception + "(82)";
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			szQuery = "SELECT "
					+ "		order_onoffduty_divide.which_days,"
					+ "		order_onoffduty_divide.orderdetails_id,"
					+ "		order_onoffduty_divide.driver_id, "

					+ "		order_onoffduty_exec_details.onoffduty_divide_id, "
					+ "		order_onoffduty_exec_details.order_cs_id "

					+ "FROM order_onoffduty_exec_details "

					+ "INNER JOIN order_onoffduty_divide "
					+ "ON order_onoffduty_exec_details.onoffduty_divide_id = order_onoffduty_divide.id "

					+ "WHERE " + "		order_onoffduty_divide.deleted=0 AND "
					+ "		order_onoffduty_exec_details.deleted=0 AND "
					+ "		order_onoffduty_divide.orderdetails_id="
					+ order_info.id;

			String exec_ids = "";
			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				if (!exec_ids.equals(""))
					exec_ids += ",";
				exec_ids += rs.getString("order_cs_id");
			}
			rs.close();

			if (!exec_ids.equals("")) {
				szQuery = "UPDATE order_exec_cs SET status=8 WHERE id IN ("
						+ exec_ids + ")";
				stmt.executeUpdate(szQuery);

				szQuery = "SELECT driver FROM order_exec_cs WHERE id IN ("
						+ exec_ids + ")";
				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					driver_id = rs.getLong("driver");
				rs.close();
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

			// Send push notification to driver
			if (driver_id > 0) {
				Calendar cal_starttime = Calendar.getInstance();
				cal_starttime.setTime(order_info.pre_time);

				String szMsg = cal_starttime.get(Calendar.HOUR) + "点"
						+ cal_starttime.get(Calendar.MINUTE) + "分" + "从"
						+ order_info.start_addr + "出发前往" + order_info.end_addr
						+ "的上下班订单乘客已终止服务，您可在我的订单中查看详情";
				STPushNotificationData data = new STPushNotificationData();

				data.title = ConstMgr.STR_DINGDANXIAOXI;
				data.description = szMsg;

				STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
				custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
				custom_data.orderid = order_info.id;
				custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
				custom_data.ordertype = ConstMgr.ORDER_ONCE;

				data.custom_data = custom_data;

				ApiGlobal.addOrderNotification(dbConn, driver_id, orderid,
						szMsg);
				ApiGlobal.sendPushNotifToUser(dbConn, driver_id, data);
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
	 * 1,查询order_exec_cs表获得要执行的订单。 2，更改1步骤查询出的订单的状态为开始执行。
	 * 3，更改order_temp_details的状态为开始执行。 4，感觉没有必要用userid.
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult executeOnceOrder(String source, long userid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);

				// Exec info
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				}
				// else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1)
				// // User
				// is recorded in black list
				// {
				// result.retcode = ConstMgr.ErrCode_Normal;
				// result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				// }
				else if (order_info == null || order_info.deleted == 1) // No
																		// order
																		// info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();
					// 1,查询order_exec_cs表获得要执行的订单。
					// Select exec_info
					szQuery = "SELECT * FROM order_exec_cs WHERE deleted=0 AND id="
							+ order_info.order_cs_id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
					rs.close();

					if (exec_info == null) // Abnormal situation
					{
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(26)";
					} else {
						String szCurTime = ApiGlobal.Date2String(new Date(),
								true);
						// 2，更改1步骤查询出的订单的状态为开始执行。
						// Update order_exec_cs info
						szQuery = "UPDATE " + "		order_exec_cs " + "SET "
								+ "		begin_exec_time=\"" + szCurTime + "\","
								+ "		status=3 " + "WHERE " + "		deleted=0 AND "
								+ "		status=2 AND " + "		id=" + exec_info.id;

						if (stmt.executeUpdate(szQuery) == 1) {
							// 3,更改order_temp_details的状态为开始执行。
							// Update order_temp_details info
							szQuery = "UPDATE order_temp_details SET begin_exec_time=\""
									+ szCurTime
									+ "\",status=3 WHERE id="
									+ order_info.id;
							if (stmt.executeUpdate(szQuery) == 1) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(27)";
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(28)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,查询出最新的订单执行记录 2，更新1步骤查询出的订单执行记录的状态为已开始执行。
	 * 3，感觉第2个sql语句有问题，多次执行的话总是查询出同一条记录，也可能没有问题，因为新的记录是每天都生成。
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult executeOnOffOrder(String source, long userid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Order Divide Info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Exec info
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				}
				// else if (ApiGlobal.recordedInBlackList(dbConn, userid) == 1)
				// // User
				// is recorded in black list
				// {
				// result.retcode = ConstMgr.ErrCode_Normal;
				// result.retmsg = ConstMgr.ErrMsg_UserInBlackList;
				// }
				else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();
					// /////////////////////////////////////////////////////////////////////////////////////////////////////

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id,"
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null) // Abnormal situation
					{
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(29)";
					} else {
						String szCurTime = ApiGlobal.Date2String(new Date(),
								true);

						// Update order_exec_cs info
						szQuery = "UPDATE " + "		order_exec_cs " + "SET "
								+ "		begin_exec_time=\"" + szCurTime + "\","
								+ "		status=3 " + "WHERE " + "		deleted=0 AND "
								+ "		status=2 AND " + "		id=" + exec_info.id;

						if (stmt.executeUpdate(szQuery) == 1) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(30)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,根据订单号orderid查询出当前的长途订单 2,根据长途id编号，查询出该订单相关的所有执行订单
	 * 3,更新上一步查询出的所有执行订单的状态为开始执行 4,更新长途订单表order_longdistance_details的状态为开始执行
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult executeLongOrder(String source, long userid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				// 1,根据订单号orderid查询出当前的长途订单
				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				// Order Long distance user info
				List<SVCOrderLongDistanceUsersCS> arr_usercs_info = new ArrayList<SVCOrderLongDistanceUsersCS>();

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.publisher != userinfo.id) // Not published
																// user
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_PublisherNotMatch;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();
					// 2,根据长途id编号，查询出该订单相关的所有执行订单
					// Select users cs info
					szQuery = "SELECT * FROM order_longdistance_users_cs WHERE deleted=0 AND orderdriverlongdistance_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						arr_usercs_info.add(SVCOrderLongDistanceUsersCS
								.decodeFromResultSet(rs));
					}
					rs.close();
					// /////////////////////////////////////////////////////////////////////////////////////////////////////

					// Get exec ids
					String ids = "";
					for (int i = 0; i < arr_usercs_info.size(); i++) {
						if (!ids.equals(""))
							ids += ",";
						ids += arr_usercs_info.get(i).order_exec_cs_id;
					}
					// 3,更新上一步查询出的所有执行订单的状态为开始执行
					// Update order_exec_cs table
					String szCurTime = ApiGlobal.Date2String(new Date(), true);
					if (!ids.equals("")) {
						szQuery = "UPDATE " + "		order_exec_cs " + "SET "
								+ "		status=3," + "		begin_exec_time=\""
								+ szCurTime + "\" " + "WHERE "
								+ "		deleted=0 AND" + "		id IN (" + ids
								+ ") AND" + "		status=2";

						stmt.executeUpdate(szQuery);
					}
					// 4,更新长途订单表order_longdistance_details的状态为开始执行
					// Update order_longdistance_details
					szQuery = "UPDATE " + "		order_longdistance_details "
							+ "SET " + "		status=3," + "		begin_exec_time=\""
							+ szCurTime + "\" " + "WHERE " + "		status=2 AND "
							+ "		id=" + order_info.id;

					if (stmt.executeUpdate(szQuery) == 1) {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(31)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/*
 * 每隔3秒调用一次checkOnceOrderAcceptance接口，获取一次订单状态，看有没有车主接单。
	 * 1,查看是否有车主接单，这个通过查询order_temp_grab表来实现 2,如果没有车主接单，直接返回
	 * 3,如果有车主接单，则下面查询该车主的相关信息。 3.1,查询该车主一共做了多少执行订单 3.2，查询该车主的被评价总数和好评率
	 * 3.3,查询该车主的汽车信息 3.4,查询该车主的经纬度坐标(下一步计算出距离乘客多远) 3.5,查询出该订单的中途点信息
 */
	public SVCResult checkOnceOrderAcceptance(String source, long userid,
			long orderid, double lat, double lng, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + lat + "," + lng + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
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
			dbConn = DBManager.getDBConnection();
			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn, userid);

			// Get order info
			SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(dbConn,
					orderid);

			// 1,查看是否有车主接单，这个通过查询order_temp_grab表来实现
			// Get grab info
			SVCOrderTempGrab grab_info = ApiGlobal
					.getLatestGrabInfoFromOrderID(dbConn, orderid);

			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			if (!userinfo.device_token.equals(devtoken)) // Device
															// token not
															// match
			{
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			if (order_info == null) // No order info
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			if (order_info.deleted == 1) {
				result.retcode = ConstMgr.ErrCode_PassDisagreed;
				result.retmsg = ConstMgr.ErrMsg_PassDisagreed;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			if (order_info.publisher != userinfo.id) // Not published
														// user
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_PublisherNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			if (order_info.status != 1) // State not match
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			// 2,如果没有车主接单，直接返回
			if (grab_info == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_OrderNotAccepted;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			stmt = dbConn.createStatement();

			SVCUser driver_info = ApiGlobal.getUserInfoFromUserID(dbConn,
					grab_info.driverid);
			SVCUserCar driver_car = ApiGlobal.getUserCarInfoFromUserID(dbConn,
					grab_info.driverid);

			JSONObject retdata = new JSONObject();

			// Driving career
			long drv_career = ApiGlobal
					.getDiffYear(
							ApiGlobal.String2Date(driver_info.drlicence_ti),
							new Date());
			String drv_career_desc = ConstMgr.STR_JIALING + drv_career
					+ ConstMgr.STR_NIAN;

			// Carpool count
			int carpool_count = 0;
			String carpool_desc = "";
			// 3,如果有车主接单，则下面查询该车主的相关信息。
			// 3.1,查询该车主一共做了多少执行订单
			szQuery = "SELECT COUNT(*) AS count " + "FROM order_exec_cs "
					+ "WHERE " + "		deleted=0 AND "
					+ "		(status=6 OR status=7) AND " + "		driver="
					+ driver_info.id;

			rs = stmt.executeQuery(szQuery);
			rs.next(); // Must exist. Otherwise abnormal
			carpool_count = rs.getInt("count");
			carpool_desc = carpool_count + ConstMgr.STR_CI;
			rs.close();
			// /////////////////////////////////////////////////////////////////////////////////////////////////////

			// good evaluation rate
			int eval_count = 0, goodeval_count = 0;
			double goodeval_rate = 0;
			String goodeval_rate_desc = "";
			// 3.2，查询该车主的被评价总数和好评率
			szQuery = "SELECT "
					+ "		all_info.count AS eval_count,"
					+ "		good_info.count AS goodeval_count "
					+ "FROM "
					+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
					+ driver_info.id
					+ " AND usertype=1) AS all_info, "
					+ "		(SELECT COUNT(*) AS count FROM evaluation_cs WHERE deleted=0 AND to_userid="
					+ driver_info.id
					+ " AND usertype=1 AND level=1) AS good_info ";

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				eval_count = rs.getInt("eval_count");
				goodeval_count = rs.getInt("goodeval_count");

				if (eval_count != 0)
					goodeval_rate = goodeval_count * 100 / eval_count;
			}
			rs.close();

			goodeval_rate_desc = ApiGlobal.Double2String(goodeval_rate, 2)
					+ "%";
			// ///////////////////////////////////////////////////////////////////////////////////////////
			// 3.3,查询该车主的汽车信息
			// Car type
			int cartype = 1;
			szQuery = "SELECT type FROM car_type WHERE id="
					+ driver_car.car_type_id;
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				cartype = rs.getInt("type");
			rs.close();
			// ///////////////////////////////////////////////////////////////////////////////////////////

			// 3.4,查询该车主的经纬度坐标(下一步计算出距离乘客多远)
			// Distance
			double distance = 0;
			String dist_desc = "";

			SVCUserOnline user_online = null;
			szQuery = "SELECT * FROM user_online WHERE userid="
					+ driver_info.id
					+ " AND deleted=0 ORDER BY last_heartbeat_time DESC LIMIT 0,1";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				user_online = SVCUserOnline.decodeFromResultSet(rs);
			rs.close();

			if (user_online != null)
				distance = ApiGlobal.calcDist(user_online.lat, user_online.lng,
						lat, lng);

			if (distance < 0.2) {
				dist_desc = "<200m";
			} else if (distance < 1) {
				dist_desc = "" + (int) (distance * 1000) + "km";
			} else {
				dist_desc = ApiGlobal.Double2String(distance, 2) + "km";
			}
			// ///////////////////////////////////////////////////////////////////////////////////////////
			// 3.5,查询出该订单的中途点信息
			// Mid points
			JSONArray mid_points = new JSONArray();
			szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=0 AND orderid="
					+ order_info.id;
			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				JSONObject midpoint = new JSONObject();

				midpoint.put("index", rs.getInt("point_index"));
				midpoint.put("lat", rs.getDouble("lat"));
				midpoint.put("lng", rs.getDouble("lng"));
				midpoint.put("addr", rs.getString("addr"));

				mid_points.add(midpoint);
			}
			rs.close();
			// ///////////////////////////////////////////////////////////////////////////////////////////

			retdata.put("drvid", driver_info.id);
			retdata.put("name", driver_info.nickname);
			retdata.put("img", ApiGlobal.getAbsoluteURL(driver_info.img));
			retdata.put("gender", driver_info.sex);
			retdata.put("age",
					ApiGlobal.getDiffYear(driver_info.birthday, new Date()));
			retdata.put("carimg", ApiGlobal.getAbsoluteURL(driver_car.car_img));
			retdata.put("carno", ApiGlobal.getAbsoluteURL(driver_car.plate_num));
			retdata.put("drv_career", drv_career);
			retdata.put("drv_career_desc", drv_career_desc);
			retdata.put("evgood_rate", (int) goodeval_rate);
			retdata.put("evgood_rate_desc", goodeval_rate_desc);
			retdata.put("carpool_count", carpool_count);
			retdata.put("carpool_count_desc", carpool_desc);
			retdata.put("brand", driver_car.brand);
			retdata.put("style", driver_car.style);
			retdata.put("type", cartype);
			retdata.put("color", driver_car.color);
			retdata.put("distance", distance);
			retdata.put("distance_desc", dist_desc);
			retdata.put("start_addr", order_info.start_addr);
			retdata.put("end_addr", order_info.end_addr);
			retdata.put("start_lat", order_info.start_lat);
			retdata.put("start_lng", order_info.start_lng);
			retdata.put("end_lat", order_info.end_lat);
			retdata.put("end_lng", order_info.end_lng);
			retdata.put("midpoints", mid_points);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = retdata;

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

	/**
	 * 标记车主到达。标记order_exec_cs和order_temp_details的状态
	 * 
	 * 1,标记order_temp_details表的状态为已到达 2,标记order_exec_cs表的状态为已到达 3,向乘客发送推送消息
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult signOnceOrderDriverArrival(String source, long driverid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);

				// Exec info
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null || order_info.deleted == 1) // No
																			// order
																			// info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 3) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					szQuery = "SELECT * FROM order_exec_cs WHERE deleted=0 AND id="
							+ order_info.order_cs_id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
					rs.close();

					if (exec_info == null) {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(32)";
					} else {
						String szCurTime = ApiGlobal.Date2String(new Date(),
								true);
						// 1,标记order_temp_details表的状态为已到达
						szQuery = "UPDATE " + "		order_temp_details " + "SET "
								+ "		driverarrival_time=\"" + szCurTime
								+ "\","
								+ "		status=4 " // Driver arrive state
								+ "WHERE " + "		deleted=0 AND "
								+ "		status=3 AND " + "		id=" + order_info.id;
						// 2,标记order_exec_cs表的状态为已到达
						if (stmt.executeUpdate(szQuery) == 1) {
							szQuery = "UPDATE " + "		order_exec_cs " + "SET "
									+ "		driverarrival_time=\""
									+ szCurTime
									+ "\","
									+ "		status=4 " // Driver arrive state
									+ "WHERE " + "		deleted=0 AND "
									+ "		status=3 AND " + "		id="
									+ exec_info.id;

							if (stmt.executeUpdate(szQuery) == 1) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;
								// 3,向乘客发送推送消息
								// Send push notification and order notification
								// to passenger
								String szMsg = "您预约从" + order_info.start_addr
										+ "到" + order_info.end_addr
										+ "的拼车车主已到达预定地点，请尽快上车，详细位置可在我的订单中查看";
								STPushNotificationData data = new STPushNotificationData();

								data.title = ConstMgr.STR_DINGDANXIAOXI;
								data.description = szMsg;

								STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
								custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
								custom_data.orderid = order_info.id;
								custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
								custom_data.ordertype = ConstMgr.ORDER_ONCE;

								data.custom_data = custom_data;

								ApiGlobal.addOrderNotification(dbConn,
										order_info.publisher, orderid, szMsg);
								ApiGlobal.sendPushNotifToUser(dbConn,
										order_info.publisher, data);
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(33)";
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(34)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 标记相关order_exec_cs的状态为车主已到达
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult signOnOffOrderDriverArrival(String source, long driverid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Order Divide Info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Exec info
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id,"
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null) // Abnormal situation
					{
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(35)";
					} else {
						String szCurTime = ApiGlobal.Date2String(new Date(),
								true);

						szQuery = "UPDATE " + "		order_exec_cs " + "SET "
								+ "		driverarrival_time=\"" + szCurTime
								+ "\","
								+ "		status=4 " // Driver arrive state
								+ "WHERE " + "		deleted=0 AND " + "		id="
								+ exec_info.id + " AND " + "		status=3";

						if (stmt.executeUpdate(szQuery) == 1) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;

							// Send push notification and order notification to
							// passenger
							String szMsg = "您预约从" + order_info.start_addr + "到"
									+ order_info.end_addr
									+ "的拼车车主已到达预定地点，请尽快上车，详细位置可在我的订单中查看";
							STPushNotificationData data = new STPushNotificationData();

							data.title = ConstMgr.STR_DINGDANXIAOXI;
							data.description = szMsg;

							STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
							custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
							custom_data.orderid = order_info.id;
							custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
							custom_data.ordertype = ConstMgr.ORDER_ONOFF;

							data.custom_data = custom_data;

							ApiGlobal.addOrderNotification(dbConn,
									order_info.publisher, orderid, szMsg);
							ApiGlobal.sendPushNotifToUser(dbConn,
									order_info.publisher, data);
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(36)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 标记车主已到达。设置order_exec_cs和order_longdistance_details表的相关状态位
	 * 1,标记order_exec_cs表的状态为车主已到达 2,标记order_longdistance_details表的状态为车主已到达
	 * 3,向乘客发送推送消息，车主已经到达
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult signLongOrderDriverArrival(String source, long driverid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				// Exec ids
				String exec_ids = "", userids = "";
				ArrayList<Long> arrPass = new ArrayList<Long>();

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 3) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					String szCurTime = ApiGlobal.Date2String(new Date(), true);

					// Select users cs info and exec ids
					szQuery = "SELECT " + "		order_exec_cs_id," + "		userid "
							+ "FROM order_longdistance_users_cs "
							+ "WHERE orderdriverlongdistance_id="
							+ order_info.id;

					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						long exec_cs_id = rs.getLong("order_exec_cs_id");
						long userid = rs.getLong("userid");

						if (!exec_ids.equals(""))
							exec_ids += ",";

						if (!userids.equals(""))
							userids += ",";

						exec_ids += exec_cs_id;
						userids += userid;
						arrPass.add(userid);
					}
					rs.close();
					// 1,标记order_exec_cs表的状态为车主已到达
					// Update exec logs
					if (!exec_ids.equals("")) {
						szQuery = "UPDATE "
								+ "		order_exec_cs "
								+ "SET "
								+ "		status=4," // Driver arrive state
								+ "		driverarrival_time=\"" + szCurTime + "\" "
								+ "WHERE " + "		deleted=0 AND " + "		id IN ("
								+ exec_ids + ") AND " + "		status=3";
						stmt.executeUpdate(szQuery);
					}
					// 2,标记order_longdistance_details表的状态为车主已到达
					// Update order_longdistance_details
					szQuery = "UPDATE " + "		order_longdistance_details "
							+ "SET " + "		status=4,"
							+ "		driverarrival_time=\"" + szCurTime + "\" "
							+ "WHERE " + "		deleted=0 AND " + "		status=3 AND "
							+ "		id=" + order_info.id;

					if (stmt.executeUpdate(szQuery) == 1) {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;

						// Send push notification to passengers
						String szMsg = "您预约从"
								+ ApiGlobal.cityCode2Name(dbConn,
										order_info.start_city)
								+ "到"
								+ ApiGlobal.cityCode2Name(dbConn,
										order_info.end_city)
								+ "的拼车车主已到达预定地点，请尽快上车，详细位置可在我的订单中查看";
						// 3,向乘客发送推送消息，车主已经到达
						STPushNotificationData data = new STPushNotificationData();
						data.title = ConstMgr.STR_DINGDANXIAOXI;
						data.description = szMsg;

						STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
						custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
						custom_data.orderid = order_info.id;
						custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
						custom_data.ordertype = ConstMgr.ORDER_LONG;

						data.custom_data = custom_data;

						for (int i = 0; i < arrPass.size(); i++)
							ApiGlobal.addOrderNotification(dbConn,
									arrPass.get(i), orderid, szMsg);
						ApiGlobal.sendPushNotifToUser(dbConn, arrPass, data);
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(37)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 乘客上车验票：设置order_exec_cs和order_temp_details表的状态位，为已验票 1,乘客密码错误，直接返回
	 * 2,乘客密码正确 2.1更新order_exec_cs表的状态为已验票 2.2更新order_temp_details表的状态为已验票
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param passid
	 * @param password
	 * @param devtoken
	 * @return
	 */
	public SVCResult signOnceOrderPassengerUpload(String source, long driverid,
			long orderid, long passid, String password, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + passid + "," + password + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0 || passid < 0
				|| password.length() != 4 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);

				// Order exec id
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null || order_info.deleted == 1) // No
																			// order
																			// info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 4) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					szQuery = "SELECT * FROM order_exec_cs WHERE deleted=0 AND id="
							+ order_info.order_cs_id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
					rs.close();

					if (exec_info == null) {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(38)";
					} else {
						// 1,乘客密码错误，直接返回
						if (!exec_info.password.equals(password)) {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_OrderPwdNotMatch;

							// Send push notification and order notification to
							// passenger
							String szMsg = "您的电子车票密码验证失败，正确密码为"
									+ exec_info.password + "，如非您本人输入请拨打客服电话"
									+ ApiGlobal.getServiceCall();
							STPushNotificationData data = new STPushNotificationData();

							data.title = ConstMgr.STR_DINGDANXIAOXI;
							data.description = szMsg;

							STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
							custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
							custom_data.orderid = order_info.id;
							custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
							custom_data.ordertype = ConstMgr.ORDER_ONCE;

							data.custom_data = custom_data;

							ApiGlobal.addOrderNotification(dbConn,
									order_info.publisher, orderid, szMsg);
							ApiGlobal.sendPushNotifToUser(dbConn,
									order_info.publisher, data);

						} else {// 2,乘客密码正确

							String szCurTime = ApiGlobal.Date2String(
									new Date(), true);
							// 2.1更新order_exec_cs表的状态为已验票
							szQuery = "UPDATE " + "		order_exec_cs " + "SET "
									+ "		status=5," + "		beginservice_time=\""
									+ szCurTime + "\" " + "WHERE "
									+ "		deleted=0 AND " + "		status=4 AND "
									+ "		id=" + exec_info.id;
							// 2.2更新order_temp_details表的状态为已验票
							if (stmt.executeUpdate(szQuery) == 1) {
								szQuery = "UPDATE " + "		order_temp_details "
										+ "SET " + "		status=5,"
										+ "		beginservice_time=\"" + szCurTime
										+ "\" " + "WHERE " + "		deleted=0 AND "
										+ "		status=4 AND " + "		id="
										+ order_info.id;

								if (stmt.executeUpdate(szQuery) == 1) {
									result.retcode = ConstMgr.ErrCode_None;
									result.retmsg = ConstMgr.ErrMsg_None;
								} else {
									result.retcode = ConstMgr.ErrCode_Exception;
									result.retmsg = ConstMgr.ErrMsg_Exception
											+ "(39)";
								}
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(40)";
							}
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 设置order_exec_cs和order_temp_details表的状态位
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param passid
	 * @param password
	 * @param devtoken
	 * @return
	 */
	public SVCResult signOnOffOrderPassengerUpload(String source,
			long driverid, long orderid, long passid, String password,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + passid + "," + password + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0 || passid < 0
				|| password.length() != 4 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Order Divide Info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Exec info
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id,"
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null) {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(41)";
					} else {
						if (!exec_info.password.equals(password)) {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_OrderPwdNotMatch;

							// Send push notification and order notification to
							// passenger
							String szMsg = "您的电子车票密码验证失败，正确密码为"
									+ exec_info.password + "，如非您本人输入请拨打客服电话"
									+ ApiGlobal.getServiceCall();
							STPushNotificationData data = new STPushNotificationData();

							data.title = ConstMgr.STR_DINGDANXIAOXI;
							data.description = szMsg;

							STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
							custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
							custom_data.orderid = order_info.id;
							custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
							custom_data.ordertype = ConstMgr.ORDER_ONOFF;

							data.custom_data = custom_data;

							ApiGlobal.addOrderNotification(dbConn,
									order_info.publisher, orderid, szMsg);
							ApiGlobal.sendPushNotifToUser(dbConn,
									order_info.publisher, data);
						} else {
							String szCurTime = ApiGlobal.Date2String(
									new Date(), true);

							szQuery = "UPDATE " + "		order_exec_cs " + "SET "
									+ "		status=5," + "		beginservice_time=\""
									+ szCurTime + "\" " + "WHERE "
									+ "		deleted=0 AND " + "		status=4 AND "
									+ "		id=" + exec_info.id;

							if (stmt.executeUpdate(szQuery) == 1) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(42)";
							}
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 乘客验票上车 1,查询出待验票上车的执行订单(指定乘客的执行订单) 2,用户密码输入正确 2.1,设置上一步查询出的执行订单为已验票上车
	 * 3,用户密码输入错误 3.1向乘客发送推送消息
 * 
 * @param source
 * @param driverid
 * @param orderid
 * @param passid
 * @param password
 * @param devtoken
 * @return
 */
	public SVCResult signLongOrderPassengerUpload(String source, long driverid,
			long orderid, long passid, String password, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + passid + "," + password + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0 || passid < 0
				|| password.length() != 4 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 4) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();
					// 1,查询出待验票上车的执行订单(指定乘客的执行订单)
					// Select user cs info
					szQuery = "SELECT "
							+ "		order_longdistance_users_cs.orderdriverlongdistance_id,"
							+ "		order_longdistance_users_cs.userid,"
							+ "		order_longdistance_users_cs.order_exec_cs_id,"
							+ "		order_exec_cs.id AS exec_id,"
							+ "		order_exec_cs.password,"
							+ "		order_exec_cs.status "

							+ "FROM "
							+ "		order_longdistance_users_cs "

							+ "INNER JOIN	order_exec_cs "
							+ "ON			order_longdistance_users_cs.order_exec_cs_id = order_exec_cs.id "

							+ "WHERE "
							+ "		order_longdistance_users_cs.deleted=0 AND "
							+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
							+ order_info.id + " AND "
							+ "		order_longdistance_users_cs.userid=" + passid
							+ " AND " + "		order_exec_cs.deleted=0 AND "
							+ "		order_exec_cs.status=4";

					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						long exec_id = rs.getLong("exec_id");
						String szPwd = rs.getString("password");

						if (szPwd.equals(password)) {// 2用户密码输入正确
							rs.close();

							String szCurTime = ApiGlobal.Date2String(
									new Date(), true);
							// 2.1,设置上一步查询出的执行订单为已验票上车
							szQuery = "UPDATE " + "		order_exec_cs " + "SET "
									+ "		status=5," + "		beginservice_time=\""
									+ szCurTime + "\" " + "WHERE " + "		id="
									+ exec_id;

							if (stmt.executeUpdate(szQuery) == 1) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(43)";
							}
						} else {// 3,用户密码输入错误
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_OrderPwdNotMatch;

							// Send push notification and order notification to
							// passenger
							String szMsg = "您的电子车票密码验证失败，正确密码为" + szPwd
									+ "，如非您本人输入请拨打客服电话"
									+ ApiGlobal.getServiceCall();
							STPushNotificationData data = new STPushNotificationData();

							data.title = ConstMgr.STR_DINGDANXIAOXI;
							data.description = szMsg;
							// 3.1向乘客发送推送消息
							STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
							custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
							custom_data.orderid = order_info.id;
							custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
							custom_data.ordertype = ConstMgr.ORDER_ONOFF;

							data.custom_data = custom_data;

							ApiGlobal.addOrderNotification(dbConn, passid,
									orderid, szMsg);
							ApiGlobal.sendPushNotifToUser(dbConn, passid, data);
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(44)";
					}
					rs.close();
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 退票分为2种：(1)乘客主动退票，这个是发生在订单执行之前；(2)车主主动强制乘客弃票，这个发生在过了出发时间了
	 * 1，如果当前是车主，则出发时间过了15分钟之后，车主可以主动弃票，否则直接返回 2,根据乘客id查询出执行订单
	 * 3,根据乘客的执行订单编号查询出执行订单，取出乘客抢的座位数 4,乘客退票了，所以订单占据的座位数要多一个
	 * 5,是乘客则更新乘客的取消时间，是车主则更新车主的取消时间 6,返回乘客的绿点，
	 * 6.1根据执行订单查询ts表，得出ts.id，(这个地方还需思考，可能处理的不好，是不是有问题) 6.2根据ts.id查询出冻结的记录
	 * 6.3返还乘客的绿点 7，乘客退票了，要根据情况扣点。 7.1查询出扣点的比率 7.2按照上步查询出的扣点比率进行扣点
	 * 8,查询出该订单所有没有被取消的执行订单 8.1如果该订单所有的执行订单都已经被取消了,并且订单已经是执行状态，则标记订单状态为已关闭状态
	 * 8.1.1如果是车主主动弃票，则系统给乘客推送通知消息
	 * 8.2如果该订单的所有执行订单都已经被取消了，但是还没有被执行，则标记该订单状态为待发布状态
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param passid
	 * @param devtoken
	 * @return
	 */
	public SVCResult signLongOrderPassengerGiveup(String source, long driverid,
			long orderid, long passid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + passid + "," + devtoken);

		if (source.equals("") || orderid < 0 || passid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				if (userinfo != null && !userinfo.device_token.equals(devtoken)) // Device
																					// token
																					// not
																					// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status > 4) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_AlreadyStarted;
				} else {
					if (driverid > 0) {// 1，如果当前是车主，则出发时间过了15分钟之后，车主可以主动弃票，否则直接返回
						String szMinLimit = ApiGlobal.getEnvValueFromCode(
								dbConn, ConstMgr.ENVCODE_ORDERGIVEUPLIMIT);// 能弃票的时间限制，出发时间过了15分钟之后才能放弃车票
						int nMinLimit = Integer.parseInt(szMinLimit);

						Calendar cal_pretime = Calendar.getInstance();

						cal_pretime.setTime(order_info.pre_time);
						cal_pretime.add(Calendar.MINUTE, nMinLimit);

						if (cal_pretime.getTime().after(new Date())) // Cannot
																		// give
																		// up
						{// 出发时间过了15分钟之后才能放弃车票
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_OrderGiveUpLimit1
									+ szMinLimit
									+ ConstMgr.ErrMsg_OrderGiveUpLimit2;
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}
					}

					stmt = dbConn.createStatement();

					// 2,根据乘客id查询出执行订单
					// Select user cs info
					szQuery = "SELECT "
							+ "		order_longdistance_users_cs.orderdriverlongdistance_id,"
							+ "		order_longdistance_users_cs.userid,"
							+ "		order_longdistance_users_cs.order_exec_cs_id,"
							+ "		order_longdistance_users_cs.seat_num,"
							+ "		order_exec_cs.id AS exec_id,"
							+ "		order_exec_cs.password,"
							+ "		order_exec_cs.status "

							+ "FROM "
							+ "		order_longdistance_users_cs "

							+ "INNER JOIN	order_exec_cs "
							+ "ON			order_longdistance_users_cs.order_exec_cs_id = order_exec_cs.id "

							+ "WHERE "
							+ "		order_longdistance_users_cs.deleted=0 AND "
							+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
							+ order_info.id + " AND "
							+ "		order_longdistance_users_cs.userid=" + passid
							+ " AND " + "		order_exec_cs.deleted=0";

					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						long exec_id = rs.getLong("exec_id");
						int seat_num = rs.getInt("seat_num");

						rs.close();

						SVCOrderExecCS exec_info = null;
						// 3,根据乘客的执行订单编号查询出执行订单，取出乘客抢的座位数
						szQuery = "SELECT * FROM order_exec_cs WHERE id="
								+ exec_id;
						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();

						if (exec_info == null) {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(100)";
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}

						// 4,乘客退票了，所以订单占据的座位数要多一个
						// Update occupied seat number
						int nNewOccupiedNum = order_info.occupied_num
								- seat_num;
						if (nNewOccupiedNum < 0)
							nNewOccupiedNum = 0;

						szQuery = "UPDATE " + "		order_longdistance_details "
								+ "SET " + "		occupied_num=" + nNewOccupiedNum
								+ " " + "WHERE " + "		id=" + order_info.id;
						stmt.executeUpdate(szQuery);

						// szQuery = "UPDATE order_longdistance_users_cs "
						// + "SET deleted=1 "
						// + "WHERE "
						// + "		orderdriverlongdistance_id=" + orderid + " AND "
						// + "		userid=" + passid;
						// stmt.executeUpdate(szQuery);

						String szCurTime = ApiGlobal.Date2String(new Date(),
								true);
						// 5,是乘客则更新乘客的取消时间，是车主则更新车主的取消时间
						if (driverid == 0) // Passenger cancel in active
						{
							szQuery = "UPDATE " + "		order_exec_cs " + "SET "
									+ "		status=8," + "		pass_cancel_time=\""
									+ szCurTime + "\" " + "WHERE " + "		id="
									+ exec_id;
						} else {
							szQuery = "UPDATE " + "		order_exec_cs " + "SET "
									+ "		status=8," + "		driver_cancel_time=\""
									+ szCurTime + "\" " + "WHERE " + "		id="
									+ exec_id;
						}

						if (stmt.executeUpdate(szQuery) == 1) {
							SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							
							//------begain  modify by  chuzhiqiang 取消订单后进行撤保操作----------------------
							
							long insu_id_pass=exec_info.insu_id_pass;
							szQuery="UPDATE insurance SET insu_status = 1 WHERE ORDER_EXEC_ID = "+exec_info.id;
							System.out.println("单次  撤保 更新保单状态"+szQuery);
							stmt.executeUpdate(szQuery);

							
							System.out.println("insu_id ----"+insu_id_pass);
							szQuery="SELECT appl_no from insurance WHERE id = "+insu_id_pass;//根据乘客保单id获得
							
							 rs = stmt.executeQuery(szQuery);
							 
							 System.out.println("szq====="+szQuery);
							 String appl_no = null;
							 //String appl_no = rs2.getString("APPL_NO");
							 if (rs.next()) {
								
								  appl_no = rs.getString(1);
							}
							 rs.close();
							
							
							szQuery = "INSERT INTO insurance_dtd ("
									+ " insu_id"
									+ " ,APPL_NO"
									+ " ,oper_type"// 1投保,2撤保
									+ " ,oper_time" + " ,insu_sum" + ") "
									+ "values" + "(" 
									+ insu_id_pass 
									+ " ,\'"+appl_no+"\'" 
									+ " ,1"
									+ " ,\'" + sf.format(new Date()) + "\' "
									+ " ,200000.00" + ")";

							System.out.println("单次 撤保 车主 " + szQuery);
							stmt.executeUpdate(szQuery);

							long insu_id_driver = 0;
							szQuery = "SELECT id,user FROM insurance WHERE ORDER_EXEC_ID = "
									+ exec_info.id
									+ " AND ISD_ID ="
									+ exec_info.driver;
							System.out.println("获得车主保单id----" + szQuery);

							rs = stmt.executeQuery(szQuery);
							
							if (rs.next()) {
								insu_id_driver = rs.getLong("id");
								}
								rs.close();

								System.out.println("insu_id ----"+insu_id_driver);
								szQuery="SELECT appl_no from insurance WHERE id = "+insu_id_driver;//根据乘客保单id获得
								
								 rs = stmt.executeQuery(szQuery);
								 
								 if (rs.next()) {
										
									  appl_no = rs.getString(1);
								}
								 rs.close();
								 
								 System.out.println("szq====="+szQuery);
								
								
							szQuery = "INSERT INTO insurance_dtd ("
									+ " insu_id"
									+ " ,APPL_NO"
									+ " ,oper_type"// 1投保,2撤保
									+ " ,oper_time" + " ,insu_sum" + ") "
									+ "values"
									+ "("
									+ insu_id_driver// 车主的保单id
									+ " ,\'"+appl_no+"\'" 
									+ " ,1" 
									+ " ,\'"
									+ sf.format(new Date()) + "\' "
									+ " ,200000.00" + ")";
							System.out.println("单次 撤保 乘客 " + szQuery);
							stmt.executeUpdate(szQuery);

							// ------begain modify by chuzhiqiang
							// 取消订单后进行撤保操作----------------------

							// Return freezed blance to user
							long ts_id = 0, freeze_id = 0;
							// 6,返回乘客的绿点，6.1根据执行订单查询ts表，得出ts.id
							szQuery = "SELECT id " + "FROM ts " + "WHERE "
									+ "		deleted=0 AND " + "		oper=1 AND "
									+ "		order_type=3 AND " + "		order_cs_id="
									+ exec_id + " AND " + "		order_id="
									+ order_info.id;
							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								ts_id = rs.getLong("id");
							rs.close();
							// 6.2根据ts.id查询出冻结的记录
							if (ts_id > 0) {
								szQuery = "SELECT id " + "FROM freeze_points "
										+ "WHERE state=0 AND userid=" + passid
										+ " AND freeze_ts_id=" + ts_id;
								rs = stmt.executeQuery(szQuery);
								if (rs.next())
									freeze_id = rs.getLong("id");
								rs.close();
							}
							// 6.3返还乘客的绿点
							if (freeze_id > 0) {
								// At first all of the balances points return to
								// user
								ApiGlobal.releasePointsForUser(dbConn, source,
										passid, freeze_id);

								// 7，乘客退票了，要根据情况扣点。7.1查询出扣点的比率
								// If user actively give up this ticket,
								// Must decrease some of balance
								if (driverid == 0) {
									double decBalance = 0;

									String ret_time1 = ApiGlobal
											.getEnvValueFromCode(
													dbConn,
													ConstMgr.ENVCODE_RETORDER_TIME1);// 退票时候的时间节点1，为扣点做准备
									String ret_time2 = ApiGlobal
											.getEnvValueFromCode(
													dbConn,
													ConstMgr.ENVCODE_RETORDER_TIME2);// 退票时候的时间节点2，为扣点做准备
									String ret_time3 = ApiGlobal
											.getEnvValueFromCode(
													dbConn,
													ConstMgr.ENVCODE_RETORDER_TIME3);// 退票时候的时间节点3，为扣点做准备

									int nRetTime1 = 0, nRetTime2 = 0, nRetTime3 = 0;

									try {
										nRetTime1 = Integer.parseInt(ret_time1);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
									try {
										nRetTime2 = Integer.parseInt(ret_time2);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
									try {
										nRetTime3 = Integer.parseInt(ret_time3);
									} catch (Exception ex) {
										ex.printStackTrace();
									}

									String ret_ratio1 = ApiGlobal
											.getEnvValueFromCode(
													dbConn,
													ConstMgr.ENVCODE_RETORDER_RATIO1);
									String ret_ratio2 = ApiGlobal
											.getEnvValueFromCode(
													dbConn,
													ConstMgr.ENVCODE_RETORDER_RATIO2);
									String ret_ratio3 = ApiGlobal
											.getEnvValueFromCode(
													dbConn,
													ConstMgr.ENVCODE_RETORDER_RATIO3);
									String ret_ratio4 = ApiGlobal
											.getEnvValueFromCode(
													dbConn,
													ConstMgr.ENVCODE_RETORDER_RATIO4);

									double fRatio1 = Double
											.parseDouble(ret_ratio1);
									double fRatio2 = Double
											.parseDouble(ret_ratio2);
									double fRatio3 = Double
											.parseDouble(ret_ratio3);
									double fRatio4 = Double
											.parseDouble(ret_ratio4);

									int nDiffDay = (int) ApiGlobal.getDiffDay(
											new Date(), order_info.pre_time);
									double fRate = 0;
									if (nDiffDay < nRetTime1) {
										fRate = fRatio1;
									} else if (nDiffDay < nRetTime2) {
										fRate = fRatio2;
									} else if (nDiffDay < nRetTime3) {
										fRate = fRatio3;
									} else {
										fRate = fRatio4;
									}

									decBalance = exec_info.price * fRate / 100;
									// 7.2按照上步查询出的扣点比率进行扣点
									if (decBalance != 0) {
										// And then, decrease some of balance
										ApiGlobal
												.addBalance(
														dbConn,
														source,
														passid,
														ConstMgr.USERTYPE_USER,
														-decBalance, // Decrease
																		// balance
														ConstMgr.txcode_decreaseBalance,
														exec_id, order_info.id,
														3);
									}
								}
								// 8,查询出该订单所有没有被取消的执行订单
								szQuery = "SELECT"
										+ "		order_exec_cs.status "

										+ "FROM "
										+ "		order_exec_cs "

										+ "INNER JOIN	order_longdistance_users_cs "
										+ "ON			order_longdistance_users_cs.order_exec_cs_id=order_exec_cs.id "

										+ "WHERE "
										+ "		order_exec_cs.status<>8 AND "
										+ "		order_exec_cs.deleted=0 AND "
										+ "		order_longdistance_users_cs.deleted=0 AND "
										+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
										+ order_info.id;

								rs = stmt.executeQuery(szQuery);
								if (!rs.next()) {
									if (order_info.status > 2) {// 8.1如果该订单所有的执行订单都已经被取消了,并且订单已经是执行状态，则标记订单状态为已关闭状态
										// The order is already started.
										// And all passengers cancelled this
										// order.
										// This order is automatically
										// cancelled.

										szQuery = "UPDATE "
												+ "		order_longdistance_details "
												+ "SET " + "		status=8,"
												+ "		driver_cancel_time=\""
												+ szCurTime + "\" " + "WHERE "
												+ "		deleted=0 AND " + "		id="
												+ order_info.id;

										if (stmt.executeUpdate(szQuery) == 1) {
											result.retcode = ConstMgr.ErrCode_None;
											result.retmsg = ConstMgr.ErrMsg_OrderAutoClosed;

											// Send push notification and order
											// notification to passenger
											if (driverid > 0) {// 8.1.1如果是车主主动弃票，则系统给乘客推送通知消息
												String szMsg = "由于您超过15分钟仍未抵达，车主已将您弃票，如有疑问请拨打OO客服电话"
														+ ApiGlobal
																.getServiceCall();
												STPushNotificationData data = new STPushNotificationData();

												data.title = ConstMgr.STR_DINGDANXIAOXI;
												data.description = szMsg;

												STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
												custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
												custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
												custom_data.orderid = order_info.id;
												custom_data.ordertype = ConstMgr.ORDER_LONG;

												data.custom_data = custom_data;

												ApiGlobal.addOrderNotification(
														dbConn, passid,
														orderid, szMsg);
												ApiGlobal.sendPushNotifToUser(
														dbConn, passid, data);
											}
										} else {
											result.retcode = ConstMgr.ErrCode_Exception;
											result.retmsg = ConstMgr.ErrMsg_Exception
													+ "(45)";
										}
									} else {// 8.2如果该订单的所有执行订单都已经被取消了，但是还没有被执行，则标记该订单状态为待发布状态
										// The order is not started yet.
										// Return to publish state
										szQuery = "UPDATE "
												+ "		order_longdistance_details "
												+ "SET status=1, occupied_num=0 "
												+ "WHERE" + "		id="
												+ order_info.id;
										stmt.executeUpdate(szQuery);

										result.retcode = ConstMgr.ErrCode_None;
										result.retmsg = ConstMgr.ErrMsg_None;
									}
								} else {
									result.retcode = ConstMgr.ErrCode_None;
									result.retmsg = ConstMgr.ErrMsg_None;
								}

								rs.close();
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(47)";
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(48)";
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(49)";
					}
					rs.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();

				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ex.getMessage();
			} finally {

				if (result.retcode == ConstMgr.ErrCode_None) {
				}

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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,查询该订单哪些乘客没有弃票也没有上车 1.1至少有一个乘客没有弃票也没有上车，直接返回 1.2所有的乘客都上车了，标记该订单状态为乘客上车状态
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult startLongOrderDriving(String source, long driverid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 4) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					// 1,查询该订单哪些乘客没有弃票也没有上车
					// Select user cs info
					szQuery = "SELECT"
							+ "		order_exec_cs.status "

							+ "FROM "
							+ "		order_exec_cs "

							+ "INNER JOIN	order_longdistance_users_cs "
							+ "ON			order_longdistance_users_cs.order_exec_cs_id=order_exec_cs.id "

							+ "WHERE "
							+ "		order_exec_cs.status<>8 AND "
							+ "		order_exec_cs.status<>5 AND "
							+ "		order_exec_cs.deleted=0 AND "
							+ "		order_longdistance_users_cs.deleted=0 AND "
							+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
							+ order_info.id;

					rs = stmt.executeQuery(szQuery);
					if (rs.next()) // At least one passenger is not uploaded and
									// not gave up
					{// 1.1至少有一个乘客没有弃票也没有上车，直接返回
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_NotAllPassProcessed;
					} else {
						// 1.2所有的乘客都上车了，标记该订单状态为乘客上车状态
						String szCurTime = ApiGlobal.Date2String(new Date(),
								true);
						szQuery = "UPDATE " + "		order_longdistance_details "
								+ "SET " + "		status=5,"
								+ "		beginservice_time=\"" + szCurTime + "\" "
								+ "WHERE " + "		id=" + order_info.id;

						if (stmt.executeUpdate(szQuery) == 1) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(50)";
						}
					}
					rs.close();
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,查询出该单次订单对应的执行订单 2,标记上一步查询出的执行订单为结束服务状态 3,标记单次订单的状态为结束服务
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult endOnceOrder(String source, long driverid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);

				// Order exec info
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null || order_info.deleted == 1) // No
																			// order
																			// info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 5) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();
					// 1,查询出该单次订单对应的执行订单
					// Select exec info
					szQuery = "SELECT * FROM order_exec_cs WHERE deleted=0 AND id="
							+ order_info.order_cs_id;

					rs = stmt.executeQuery(szQuery);
					if (rs.next()) // No order exec info
						exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
					rs.close();

					if (exec_info == null) {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(51)";
					} else {
						String szCurTime = ApiGlobal.Date2String(new Date(),
								true);
						// 2,标记上一步查询出的执行订单为结束服务状态
						szQuery = "UPDATE " + "		order_exec_cs " + "SET "
								+ "		status=6," + "		stopservice_time=\""
								+ szCurTime + "\" " + "WHERE " + "		id="
								+ exec_info.id;
						// 3,标记单次订单的状态为结束服务
						if (stmt.executeUpdate(szQuery) == 1) {
							szQuery = "UPDATE " + "		order_temp_details "
									+ "SET " + "		status=6,"
									+ "		endservice_time=\"" + szCurTime
									+ "\" " + "WHERE " + "		status=5 AND "
									+ "		id=" + order_info.id;

							if (stmt.executeUpdate(szQuery) == 1) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(52)";
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(53)";
						}
					}
					rs.close();
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1，根据orderid取得order_onoffduty_details的orderid
	 * 2,根据上一步查询出的orderid，去查order_onoffduty_divide表，查询出divide_id
	 * 3,再根据divide_id查询出order_exec_id 4,将查询出order_exec_cs表的记录标志结束服务
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult endOnOffOrder(String source, long driverid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Order Divide Info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Exec info
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else {
					stmt = dbConn.createStatement();

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id,"
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null) {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(54)";
					} else {
						String szCurTime = ApiGlobal.Date2String(new Date(),
								true);
						szQuery = "UPDATE " + "		order_exec_cs " + "SET "
								+ "		status=6," + "		stopservice_time=\""
								+ szCurTime + "\" " + "WHERE " + "		id="
								+ exec_info.id;

						if (stmt.executeUpdate(szQuery) == 1) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(55)";
						}
					}
					rs.close();
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,查询出该长途订单对应的所有执行订单 2,将该长途订单对应的所有的执行订单，全部标记为执行结束状态 3,将该长途订单的状态标记为执行结束状态
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult endLongOrder(String source, long driverid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				// exec id array
				String exec_ids = "";

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 5) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					String szCurTime = ApiGlobal.Date2String(new Date(), true);
					// 1,查询出该长途订单对应的所有执行订单
					// Select users cs info and exec ids
					szQuery = "SELECT order_exec_cs_id FROM order_longdistance_users_cs WHERE orderdriverlongdistance_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						long exec_cs_id = rs.getLong("order_exec_cs_id");
						if (!exec_ids.equals(""))
							exec_ids += ",";
						exec_ids += exec_cs_id;
					}
					rs.close();
					// 2,将该长途订单对应的所有的执行订单，全部标记为执行结束状态
					if (!exec_ids.equals("")) {
						szQuery = "UPDATE "
								+ "		order_exec_cs "
								+ "SET "
								+ "		status=6," // 结束服务
								+ "		stopservice_time=\"" + szCurTime + "\" "
								+ "WHERE " + "		deleted=0 AND " + "		id IN ("
								+ exec_ids + ") AND " + "		status=5";
						stmt.executeUpdate(szQuery);
					}
					// 3,将该长途订单的状态标记为执行结束状态
					szQuery = "UPDATE " + "		order_longdistance_details "
							+ "SET " + "		status=6," + "		endservice_time=\""
							+ szCurTime + "\" " + "WHERE " + "		deleted=0 AND "
							+ "		id=" + order_info.id;

					if (stmt.executeUpdate(szQuery) == 1) {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(56)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,查询出该乘客被该车主评价的次数 1.1如果该乘客被该车主评价的次数大于1，代表已经评价过了，则直接返回 2,插入该车主对该乘客的评价内容
	 * 3,在order_exec_cs表中标记车主已经评价
	 * 
	 * @param source
	 * @param driverid
	 * @param passid
	 * @param orderid
	 * @param level
	 * @param msg
	 * @param devtoken
	 * @return
	 */
	public SVCResult evaluateOnceOrderPass(String source, long driverid,
			long passid, long orderid, int level, String msg, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + passid + "," + orderid + "," + level + "," + msg + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0 || passid < 0
				|| level < 0 || (level != 1 && msg.equals(""))
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser driver_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						passid);

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);
				long exec_id = 0;

				if (driver_info == null || pass_info == null) // No user
																// information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!driver_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null || order_info.deleted == 1) // No
																			// order
																			// info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
					// } else if (order_info.status != 7) // State not match
					// {
					// result.retcode = ConstMgr.ErrCode_Normal;
					// result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					String szCurTime = ApiGlobal.Date2String(new Date(), true);
					exec_id = order_info.order_cs_id;
					// 1,查询出该乘客被该车主评价的次数
					szQuery = "SELECT COUNT(*) AS cnt " + "FROM evaluation_cs "
							+ "WHERE" + "		order_cs_id=" + exec_id + " AND "
							+ "		from_userid=" + driverid + " AND "
							+ "		to_userid=" + passid;
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						if (rs.getInt("cnt") > 0) {// 1.1如果该乘客被该车主评价的次数大于1，代表已经评价过了，则直接返回
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_AlreadyEvaluated;
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}
					}
					rs.close();

					if (level == 1 && msg.equals("")) {
						msg = ConstMgr.STR_MORENHAOPING;
					}
					// 2,插入该车主对该乘客的评价内容
					szQuery = "INSERT INTO evaluation_cs (" + "from_userid,"
							+ "to_userid," + "level," + "msg," + "ps_date,"
							+ "usertype," + "order_cs_id" + ") VALUES ("
							+ driverid + "," + passid + "," + level + ",\""
							+ msg + "\",\"" + szCurTime + "\",2," + exec_id
							+ ")";

					if (stmt.executeUpdate(szQuery) == 1) {
						// 3,在order_exec_cs表中标记车主已经评价
						szQuery = "UPDATE order_exec_cs SET has_evaluation_driver=1 WHERE id="
								+ exec_id;

						if (stmt.executeUpdate(szQuery) == 1) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;

							JSONObject retdata = new JSONObject();

							retdata.put("level", level);
							retdata.put("msg", msg);

							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(57)";
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(58)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1，向evaluation_cs表增加一条评价乘客的记录
	 * 2，将order_exec_cs的has_evaluation_driver字段标记为车主已经评价
	 * 
	 * @param source
	 * @param driverid
	 * @param passid
	 * @param orderid
	 * @param level
	 * @param msg
	 * @param devtoken
	 * @return
	 */
	public SVCResult evaluateOnOffOrderPass(String source, long driverid,
			long passid, long orderid, int level, String msg, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + passid + "," + orderid + "," + level + "," + msg + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0 || passid < 0
				|| level < 0 || (level != 1 && msg.equals(""))
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser driver_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						passid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Order Divide Info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Order Exec CS Info
				SVCOrderExecCS exec_info = null;

				if (driver_info == null || pass_info == null) // No user
																// information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!driver_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
					// } else if (order_info.status != 2) // State not match
					// {
					// result.retcode = ConstMgr.ErrCode_Normal;
					// result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					String szCurTime = ApiGlobal.Date2String(new Date(), true);

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id,"
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
					} else {
						szQuery = "SELECT COUNT(*) AS cnt "
								+ "FROM evaluation_cs " + "WHERE"
								+ "		order_cs_id=" + exec_info.id + " AND "
								+ "		from_userid=" + driverid + " AND "
								+ "		to_userid=" + passid;
						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							if (rs.getInt("cnt") > 0) {
								result.retcode = ConstMgr.ErrCode_Normal;
								result.retmsg = ConstMgr.ErrMsg_AlreadyEvaluated;
								ApiGlobal.logMessage(result.encodeToJSON().toString());

								return result;
							}
						}
						rs.close();

						if (level == 1 && msg.equals("")) {
							msg = ConstMgr.STR_MORENHAOPING;
						}

						szQuery = "INSERT INTO evaluation_cs ("
								+ "from_userid," + "to_userid," + "level,"
								+ "msg," + "ps_date," + "usertype,"
								+ "order_cs_id" + ") VALUES (" + driverid + ","
								+ passid + "," + level + ",\"" + msg + "\",\""
								+ szCurTime + "\",2," + exec_info.id + ")";

						if (stmt.executeUpdate(szQuery) == 1) {
							szQuery = "UPDATE order_exec_cs SET has_evaluation_driver=1 WHERE id="
									+ exec_info.id;

							if (stmt.executeUpdate(szQuery) == 1) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;

								JSONObject retdata = new JSONObject();

								retdata.put("level", level);
								retdata.put("msg", msg);

								result.retdata = retdata;
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(59)";
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(60)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1，查询出该长途订单下对应该乘客的执行订单 2,查询evaluation_cs表，对于该执行订单，车主是否已经评价过乘客了
	 * 3,向evaluation_cs表插入该车主对该乘客的评价内容 4,在order_exec_cs中标记车主已经评价了该乘客
	 * 
	 * @param source
	 * @param driverid
	 * @param passid
	 * @param orderid
	 * @param level
	 * @param msg
	 * @param devtoken
	 * @return
	 */
	public SVCResult evaluateLongOrderPass(String source, long driverid,
			long passid, long orderid, int level, String msg, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + passid + "," + orderid + "," + level + "," + msg + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0 || passid < 0
				|| level < 0 || (level != 1 && msg.equals(""))
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser driver_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						passid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				long exec_id = 0;

				if (driver_info == null || pass_info == null) // No user
																// information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!driver_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
					// } else if (order_info.status != 7) // State not match
					// {
					// result.retcode = ConstMgr.ErrCode_Normal;
					// result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					String szCurTime = ApiGlobal.Date2String(new Date(), true);
					// 1，查询出该长途订单下对应该乘客的执行订单
					// Select users cs info and exec ids
					szQuery = "SELECT "
							+ "		order_longdistance_users_cs.order_exec_cs_id "

							+ "FROM "
							+ "		order_longdistance_users_cs "

							+ "INNER JOIN order_exec_cs "
							+ "ON order_exec_cs.id=order_longdistance_users_cs.order_exec_cs_id "

							+ "WHERE "
							+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
							+ order_info.id + " AND order_exec_cs.passenger="
							+ passid;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						exec_id = rs.getLong("order_exec_cs_id");
					rs.close();
					// 2,查询evaluation_cs表，对于该执行订单，车主是否已经评价过乘客了
					szQuery = "SELECT COUNT(*) AS cnt " + "FROM evaluation_cs "
							+ "WHERE" + "		order_cs_id=" + exec_id + " AND "
							+ "		from_userid=" + driverid + " AND "
							+ "		to_userid=" + passid;
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						if (rs.getInt("cnt") > 0) {// 2.1车主已经评价过乘客了，直接返回
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_AlreadyEvaluated;
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}
					}
					rs.close();

					if (level == 1 && msg.equals("")) {
						msg = ConstMgr.STR_MORENHAOPING;
					}
					// 3,向evaluation_cs表插入该车主对该乘客的评价内容
					szQuery = "INSERT INTO evaluation_cs (" + "from_userid,"
							+ "to_userid," + "level," + "msg," + "ps_date,"
							+ "usertype," + "order_cs_id" + ") VALUES ("
							+ driverid + "," + passid + "," + level + ",\""
							+ msg + "\",\"" + szCurTime + "\",2," + exec_id
							+ ")";

					if (stmt.executeUpdate(szQuery) == 1) {
						// 4,在order_exec_cs中标记车主已经评价了该乘客
						szQuery = "UPDATE order_exec_cs SET has_evaluation_driver=1 WHERE id="
								+ exec_id;

						if (stmt.executeUpdate(szQuery) == 1) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;

							JSONObject retdata = new JSONObject();

							retdata.put("level", level);
							retdata.put("msg", msg);

							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(61)";
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(62)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,查询评价表，对于该单次订单，是否乘客已经评价过车主了 2,向evaluation_cs表插入乘客对车主的评价内容 3,标记乘客已经评价了车主
	 * 
	 * @param source
	 * @param passid
	 * @param driverid
	 * @param orderid
	 * @param level
	 * @param msg
	 * @param devtoken
	 * @return
	 */
	public SVCResult evaluateOnceOrderDriver(String source, long passid,
			long driverid, long orderid, int level, String msg, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + passid + "," + driverid + "," + orderid + "," + level + "," + msg + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0 || passid < 0
				|| level < 0 || (level != 1 && msg.equals(""))
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						passid);
				SVCUser driver_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);
				long exec_id = 0;

				if (driver_info == null || pass_info == null) // No user
																// information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!pass_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null || order_info.deleted == 1) // No
																			// order
																			// info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
					// } else if (order_info.status != 7) // State not match
					// {
					// result.retcode = ConstMgr.ErrCode_Normal;
					// result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					String szCurTime = ApiGlobal.Date2String(new Date(), true);
					exec_id = order_info.order_cs_id;
					// 1,查询评价表，对于该单次订单，是否乘客已经评价过车主了
					szQuery = "SELECT COUNT(*) AS cnt " + "FROM evaluation_cs "
							+ "WHERE" + "		order_cs_id=" + exec_id + " AND "
							+ "		from_userid=" + passid + " AND "
							+ "		to_userid=" + driverid;
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						if (rs.getInt("cnt") > 0) {// 1.1乘客已经评价过车主了，则直接返回
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_AlreadyEvaluated;
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}
					}
					rs.close();

					if (level == 1 && msg.equals("")) {
						msg = ConstMgr.STR_MORENHAOPING;
					}
					// 2,向evaluation_cs表插入乘客对车主的评价内容
					szQuery = "INSERT INTO evaluation_cs (" + "from_userid,"
							+ "to_userid," + "level," + "msg," + "ps_date,"
							+ "usertype," + "order_cs_id" + ") VALUES ("
							+ passid + "," + driverid + "," + level + ",\""
							+ msg + "\",\"" + szCurTime + "\",1," + exec_id
							+ ")";

					if (stmt.executeUpdate(szQuery) == 1) {
						// 3,标记乘客已经评价了车主
						szQuery = "UPDATE order_exec_cs SET has_evaluation_passenger=1 WHERE id="
								+ exec_id;

						if (stmt.executeUpdate(szQuery) == 1) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;

							JSONObject retdata = new JSONObject();
							retdata.put("level", level);
							retdata.put("msg", msg);
							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(63)";
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(64)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1，向evaluation_cs表增加一条评价乘客的记录
	 * 2，将order_exec_cs的has_evaluation_passenger字段标记为乘客已经评价
	 * 
	 * @param source
	 * @param passid
	 * @param driverid
	 * @param orderid
	 * @param level
	 * @param msg
	 * @param devtoken
	 * @return
	 */
	public SVCResult evaluateOnOffOrderDriver(String source, long passid,
			long driverid, long orderid, int level, String msg, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + passid + "," + driverid + "," + orderid + "," + level + "," + msg + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0 || passid < 0
				|| level < 0 || (level != 1 && msg.equals(""))
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						passid);
				SVCUser driver_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Order Divide Info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Order exec info
				SVCOrderExecCS exec_info = null;

				if (pass_info == null || driver_info == null) // No user
																// information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!pass_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
					// } else if (order_info.status != 2) // State not match
					// {
					// result.retcode = ConstMgr.ErrCode_Normal;
					// result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					String szCurTime = ApiGlobal.Date2String(new Date(), true);

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id,"
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
					} else {
						szQuery = "SELECT COUNT(*) AS cnt "
								+ "FROM evaluation_cs " + "WHERE"
								+ "		order_cs_id=" + exec_info.id + " AND "
								+ "		from_userid=" + passid + " AND "
								+ "		to_userid=" + driverid;
						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							if (rs.getInt("cnt") > 0) {
								result.retcode = ConstMgr.ErrCode_Normal;
								result.retmsg = ConstMgr.ErrMsg_AlreadyEvaluated;
								ApiGlobal.logMessage(result.encodeToJSON().toString());

								return result;
							}
						}
						rs.close();

						if (level == 1 && msg.equals("")) {
							msg = ConstMgr.STR_MORENHAOPING;
						}

						szQuery = "INSERT INTO evaluation_cs ("
								+ "from_userid," + "to_userid," + "level,"
								+ "msg," + "ps_date," + "usertype,"
								+ "order_cs_id" + ") VALUES (" + passid + ","
								+ driverid + "," + level + ",\"" + msg
								+ "\",\"" + szCurTime + "\",1," + exec_info.id
								+ ")";

						if (stmt.executeUpdate(szQuery) == 1) {
							szQuery = "UPDATE order_exec_cs SET has_evaluation_passenger=1 WHERE id="
									+ exec_info.id;

							if (stmt.executeUpdate(szQuery) == 1) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;

								JSONObject retdata = new JSONObject();
								retdata.put("level", level);
								retdata.put("msg", msg);
								result.retdata = retdata;
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(65)";
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(66)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1，向evaluation_cs表增加一条评价乘客的记录
	 * 2，将order_exec_cs的has_evaluation_passenger字段标记为乘客已经评价
	 * 
	 * @param source
	 * @param passid
	 * @param driverid
	 * @param orderid
	 * @param level
	 * @param msg
	 * @param devtoken
	 * @return
	 */
	public SVCResult evaluateLongOrderDriver(String source, long passid,
			long driverid, long orderid, int level, String msg, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + passid + "," + driverid + "," + orderid + "," + level + "," + msg + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0 || passid < 0
				|| level < 0 || (level != 1 && msg.equals(""))
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						passid);
				SVCUser driver_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				long exec_id = 0;

				if (pass_info == null || driver_info == null) // No user
																// information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!pass_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
					// } else if (order_info.status != 7) // State not match
					// {
					// result.retcode = ConstMgr.ErrCode_Normal;
					// result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					String szCurTime = ApiGlobal.Date2String(new Date(), true);

					// 1,查询该乘客对应的执行订单
					// Select users cs info and exec ids
					szQuery = "SELECT "
							+ "		order_longdistance_users_cs.order_exec_cs_id "

							+ "FROM "
							+ "		order_longdistance_users_cs "

							+ "INNER JOIN order_exec_cs "
							+ "ON order_exec_cs.id=order_longdistance_users_cs.order_exec_cs_id "

							+ "WHERE "
							+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
							+ order_info.id + " AND order_exec_cs.passenger="
							+ passid;

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						exec_id = rs.getLong("order_exec_cs_id");
					rs.close();
					// 2,查询出该乘客是否已经评价过该车主了
					szQuery = "SELECT COUNT(*) AS cnt " + "FROM evaluation_cs "
							+ "WHERE" + "		order_cs_id=" + exec_id + " AND "
							+ "		from_userid=" + passid + " AND "
							+ "		to_userid=" + driverid;
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						if (rs.getInt("cnt") > 0) {// 2.1如果该乘客已经评价过车主了，则直接返回
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_AlreadyEvaluated;
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}
					}
					rs.close();

					if (level == 1 && msg.equals("")) {
						msg = ConstMgr.STR_MORENHAOPING;
					}
					// 3,向evaluation_cs表中插入该乘客对该车主的评价
					szQuery = "INSERT INTO evaluation_cs (" + "from_userid,"
							+ "to_userid," + "level," + "msg," + "ps_date,"
							+ "usertype," + "order_cs_id" + ") VALUES ("
							+ passid + "," + driverid + "," + level + ",\""
							+ msg + "\",\"" + szCurTime + "\",1," + exec_id
							+ ")";

					if (stmt.executeUpdate(szQuery) == 1) {
						// 4,标记该乘客已经评价了该车主
						szQuery = "UPDATE order_exec_cs SET has_evaluation_passenger=1 WHERE id="
								+ exec_id;

						if (stmt.executeUpdate(szQuery) == 1) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;

							JSONObject retdata = new JSONObject();
							retdata.put("level", level);
							retdata.put("msg", msg);
							result.retdata = retdata;
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(67)";
						}
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(68)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 如果要连续暂停三天的话，需要点击三次“暂停明天出行”按钮，这个函数是否支持这个功能有待验证
	 * 
	 * @param source
	 * @param passid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult pauseOnOffOrder(String source, long passid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + passid + "," + orderid + "," + devtoken);

		if (source.equals("") || orderid < 0 || passid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						passid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Order Divide Info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Exec info
				SVCOrderExecCS exec_info = null;

				if (pass_info == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!pass_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id,"
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null || exec_info.status != 2) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
					} else {
						// Send push notification and order notification
						String szPreTime = ApiGlobal
								.Date2StringWithoutSeconds(exec_info.pre_time);
						String szMsg = "应乘客要求，" + szPreTime
								+ "的上下班拼车订单暂停执行一天，请查看我的订单";

						STPushNotificationData data = new STPushNotificationData();
						data.title = ConstMgr.STR_DINGDANXIAOXI;
						data.description = szMsg;

						STPushNotificationCustomData customData = new STPushNotificationCustomData();
						customData.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
						customData.orderid = order_info.id;
						customData.userrole = STPushNotificationCustomData.USER_ROLE_DRIVER;
						customData.ordertype = ConstMgr.ORDER_ONOFF;

						data.custom_data = customData;

						Date next_day = ApiGlobal.nextValidDay(
								divide_info.which_days.split(","),
								exec_info.pre_time, order_info.pre_time);
						szQuery = "UPDATE order_exec_cs " + "SET "
								+ "		pre_time=\""
								+ ApiGlobal.Date2String(next_day, true) + "\" "
								+ "WHERE" + "		deleted=0 AND id="
								+ exec_info.id;

						if (stmt.executeUpdate(szQuery) == 1) {
							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;

							ApiGlobal.addOrderNotification(dbConn,
									exec_info.driver, order_info.id, szMsg);
							ApiGlobal.sendPushNotifToUser(dbConn,
									exec_info.driver, data);

						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(69)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,更新order_onoffduty_details表的状态为成交状态 2，将新的预约订单插入到表order_exec_cs中
	 * 3，在连接表order_onoffduty_exec_details里插入一条记录
	 * ，把上一步插入到order_exec_cs的记录和order_onoffduty_divide建立关系
	 * 
	 * 
	 * 
	 * @param source
	 * @param passid
	 * @param orderid
	 * @param password
	 * @param devtoken
	 * @return
	 */
	public SVCResult reserveNextOnOffOrder(String source, long passid,
			long orderid, String password, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + passid + "," + orderid + "," + devtoken);

		if (source.equals("") || orderid < 0 || passid < 0
				|| password.length() != 4 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						passid);
				double cur_balance = ApiGlobal.getUserBalance(dbConn, passid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Order Divide Info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Exec info
				SVCOrderExecCS exec_info = null;
				long new_exec_id = 0;

				if (pass_info == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!pass_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else if (cur_balance < order_info.price) // Not enough balance
				{
					result.retcode = ConstMgr.ErrCode_NotEnoughBalance;
					result.retmsg = ConstMgr.ErrMsg_NotEnoughBalance;
				} else {

					stmt = dbConn.createStatement();

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id,"
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " AND "
								+ "		order_exec_cs.status=7 "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
					} else {
						szQuery = "UPDATE " + "		order_onoffduty_details "
								+ "SET " + "		status=2 " + "WHERE "
								+ "		deleted=0 AND " + "		id=" + order_info.id;

						if (stmt.executeUpdate(szQuery) == 1) {
							String szCurTime = ApiGlobal.Date2String(
									new Date(), true);

							String szNextTime = ApiGlobal.Date2String(ApiGlobal
									.nextValidDay(
											divide_info.which_days.split(","),
											exec_info.pre_time,
											order_info.pre_time), true);

							szQuery = "INSERT INTO order_exec_cs ("
									+ "order_type," + "passenger," + "driver,"
									+ "from_," + "to_,"
									+ "average_price_platform," + "price,"
									+ "pre_time," + "remark," + "cr_date,"
									+ "ti_accept_order," + "city_from,"
									+ "city_to," + "password," + "begin_lat,"
									+ "begin_lng," + "end_lat," + "end_lng,"
									+ "freeze_points," + "total_distance,"
									+ "order_city," + "status," + "deleted"
									+ ") VALUES (" + "3,"
									+ passid
									+ ","
									+ exec_info.driver
									+ ",\""
									+ exec_info.from_
									+ "\",\""
									+ exec_info.to_
									+ "\","
									+ exec_info.average_price_platform
									+ ","
									+ order_info.price
									+ ",\""
									+ szNextTime
									+ "\",\""
									+ order_info.remark
									+ "\",\""
									+ szCurTime
									+ "\",\""
									+ ApiGlobal.Date2String(
											order_info.ti_accept_order, true)
									+ "\",\""
									+ exec_info.city_from
									+ "\",\""
									+ exec_info.city_to
									+ "\",\""
									+ exec_info.password
									+ "\","
									+ exec_info.begin_lat
									+ ","
									+ exec_info.begin_lng
									+ ","
									+ exec_info.end_lat
									+ ","
									+ exec_info.end_lng
									+ ","
									+ order_info.price
									+ ",0,\""
									+ exec_info.order_city + "\",2,0)";

							if (stmt.executeUpdate(szQuery,
									Statement.RETURN_GENERATED_KEYS) == 1) {
								rs = stmt.getGeneratedKeys();
								if (rs.next())
									new_exec_id = rs.getLong(1);
							}

							if (new_exec_id > 0) {
								szQuery = "INSERT INTO order_onoffduty_exec_details ("
										+ "onoffduty_divide_id,"
										+ "order_cs_id,"
										+ "deleted"
										+ ") VALUES ("
										+ divide_info.id
										+ ","
										+ new_exec_id + ", 0)";

								stmt.executeUpdate(szQuery);
							}

							if (ApiGlobal.freezePointsWithType(dbConn, source,
									passid, new_exec_id, order_info.id, 2,
									order_info.price,
									ConstMgr.txcode_userFreezeBalance) > 0) {
								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(70)";
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(71)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,查询single_coupon和sys_coupon表中该用户的点券信息：没有过期的
	 * 
	 * @param source
	 * @param userid
	 * @param devtoken
	 * @return
	 */
	public SVCResult getUsableCoupons(String source, long userid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + devtoken);

		if (source.equals("") || userid < 0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				if (pass_info == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!pass_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {

					stmt = dbConn.createStatement();

					JSONArray arrCoupons = new JSONArray();
					// 1,查询single_coupon和sys_coupon表中该用户的点券信息：没有过期的
					szQuery = "SELECT "
							+ "		single_coupon.id,"
							+ "		single_coupon.coupon_code,"
							+ "		single_coupon.sum,"

							+ "		sys_coupon.id AS syscpnID,"
							+ "		sys_coupon.coupon_type,"
							+ "		sys_coupon.limit_val "

							+ "FROM single_coupon "

							+ "INNER JOIN	sys_coupon "
							+ "ON sys_coupon.id = single_coupon.syscoupon_id "

							+ "WHERE "
							+ "		single_coupon.deleted=0 AND "
							+ "		single_coupon.isenabled=1 AND "
							+ "		single_coupon.date_expired > CURRENT_TIMESTAMP() AND "
							+ "		single_coupon.isused=0 AND "
							+ "		(sys_coupon.apply_source=0 OR sys_coupon.apply_source=1) AND "
							+ "		sys_coupon.goods_or_cash=1 AND "
							+ "		sys_coupon.deleted=0 AND "
							+ "		sys_coupon.isenabled=1 AND " + "		userid="
							+ userid + " "

							+ "ORDER BY single_coupon.sum DESC";

					rs = stmt.executeQuery(szQuery);

					while (rs.next()) {
						JSONObject coupon_item = new JSONObject();

						coupon_item.put("id", rs.getLong("id"));

						double price = rs.getDouble("sum");
						coupon_item.put("price", price);

						// Use Condition
						String szUseCond = "";
						int coupon_type = rs.getInt("coupon_type");
						int limit = rs.getInt("limit_val");

						if (coupon_type == 0) {
							szUseCond = ConstMgr.STR_WUXIANZHITIAOJIAN;
						} else if (coupon_type == 1) {
							szUseCond = ConstMgr.STR_MAN + limit
									+ ConstMgr.STR_SHIYONG + "1"
									+ ConstMgr.STR_ZHANG;
						} else if (coupon_type == 2) {
							szUseCond = ConstMgr.STR_NOTUSEWITHCOUPON;
						} else if (coupon_type == 3) {
							szUseCond = ConstMgr.STR_ZHINENGSHIYONGYIZHANG
									+ "/" + ConstMgr.STR_DINGDAN;
						}

						coupon_item.put("desc", "" + price + ConstMgr.STR_DIAN
								+ "  " + szUseCond);
						coupon_item.put("cond_type", coupon_type + 1);
						coupon_item.put("cond_value", limit);
						coupon_item.put("syscoupon", rs.getLong("syscpnID"));
						// /////////////////////////////////////////////////////////////////////////////////

						arrCoupons.add(coupon_item);
					}

					rs.close();

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrCoupons;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
 * 1，重新设置订单价格为新价格
	 * 
 * @param source
 * @param userid
 * @param orderid
 * @param wait_time
 * @param new_price
 * @param devtoken
 * @return
 */
	public SVCResult changeOnceOrderPrice(String source, long userid,
			long orderid, int wait_time, double new_price, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + wait_time + "," + new_price + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0 || wait_time < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser pass_info = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);

				// Grab info
				SVCOrderTempGrab grab_info = ApiGlobal
						.getLatestGrabInfoFromOrderID(dbConn, orderid);

				// Ts info
				// SVCTs ts_info = null;

				if (pass_info == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!pass_info.device_token.equals(devtoken)) // Device
																		// token
																		// not
																		// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 1 || grab_info != null) // Order
																		// already
																		// accepted
				{
					result.retcode = ConstMgr.ErrCode_AlreadyAccepted;
					result.retmsg = ConstMgr.ErrMsg_AlreadyAcceptedOrCancelled;
				} else {

					stmt = dbConn.createStatement();

					// szQuery = "SELECT * " + "FROM ts " + "WHERE "
					// + "		order_id=" + order_info.id + " AND "
					// + "		order_type=1 AND " + "		deleted=0 AND "
					// + "		userid=" + userid + " "
					// + "ORDER BY ts_date DESC LIMIT 0,1";
					//
					// rs = stmt.executeQuery(szQuery);
					// if (rs.next())
					// ts_info = SVCTs.decodeFromResultSet(rs);
					// rs.close();
					//
					// if (ts_info != null) {
					// szQuery = "UPDATE ts SET sum=" + new_price
					// + ", balance="
					// + (ts_info.balance + ts_info.sum - new_price)
					// + " WHERE id=" + ts_info.id;

					// if (stmt.executeUpdate(szQuery) == 1) {
					// 1，重新设置订单价格为新价格
					szQuery = "UPDATE order_temp_details " + "SET "
							+ "		ps_date=\""
							+ ApiGlobal.Date2String(new Date(), true) + "\","
							+ "		price=" + new_price + "," + "		deleted=0 "
							+ "WHERE id=" + order_info.id;

					if (stmt.executeUpdate(szQuery) == 1) {
						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(72)";
					}
					// } else {
					// result.retcode = ConstMgr.ErrCode_Exception;
					// result.retmsg = ConstMgr.ErrMsg_Exception + "(73)";
					// }
					// } else {
					// result.retcode = ConstMgr.ErrCode_Exception;
					// result.retmsg = ConstMgr.ErrMsg_Exception + "(74)";
					// }
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 发现一个问题，ts表的最新一条记录不一定是该用户的用户余额，因为最新一条记录可能是冻结的绿点
	 * 
	 * 1,根据订单编号获取相应的执行订单 1.1获取单次订单的执行订单 1.2获取上下班订单的执行订单 1.3，获取长途订单的执行订单
	 * 2,获取用户使用点券的总价值 3,获取乘客被冻结的ts记录。下面这个sql语句最好再加一个ts_type作为限制条件
	 * 4,根据上一步查询出的ts.id，在freeze_points表中查询出相应的冻结记录 5,获取用户的最新余额
	 * 6,如果使用的代金券大于0，则为乘客使用代金券来抵一部分款
	 * 6.1实际使用代金券为乘客支付，方式是把代金券的金额折算成钱加到乘客的余额中。这里有个小问题
	 * ，如果订单是10元钱，共选择了30元的代金券，那么代金券支付完之后剩余的20元也不会返还给乘客了。 6.2使用完代金券之后，更新乘客的最新余额
	 * 6.3把刚才使用的所有代金券的状态全标记为已使用过了
	 * 7,把freeze_points表里冻结的该乘客的款项解冻（这里有问题，解冻后的钱去哪了,这里解冻后的金额没有插入到ts表里
	 * ，而是直接做为了总收款进行了各种分配，详细见payToDriver方法），并调用payToDriver方法为乘客付款并生成结算树
	 * 7.1调用payToDriver方法，为乘客付款，并生成结算树 7.2标记order_exec_cs为结算完成 7.3，标记单次订单表为结算完成
	 * 7.4，标记长途订单里相应的乘客的执行订单为结算完成
	 * 7.5,如果长途订单里所有的执行订单都已经结算完成，则标记order_longdistance_details为结算完成
	 * 8,如果用户还没有享受过"完成X各订单送点券"服务，则判断用户已经完成了多少个点券了，看够数了没
	 * 8.1,got_order_coupon==0表示用户还没有享受过"完成X个订单送点券服务"。 8.2,查询系统"完成X各订单"发什么点券
	 * 8.3，将点券发给乘客 8.4,标记乘客已经享受了“完成X各订单发点券”的优惠。
	 * 9,如果车主还没有享受过"完成X各订单送点券"服务，则判断车主已经完成了多少个点券了，看够数了没
	 * 9.1,got_order_coupon==0表示用户还没有享受过"完成X个订单送点券服务"。 9.2,查询系统"完成X各订单"发什么点券
	 * 9.3，给车主发点券 9.4,标记该车主享受了“完成X单发点券”的优惠
	 * 
	 * @param source
	 * @param userid
	 * @param ordId
	 * @param ordType
	 * @param price
	 * @param coupons
	 * @param devtoken
	 * @return
	 */
	public SVCResult payNormalOrder(String source, long userid, long ordId,
			int ordType, double price, String coupons, String devtoken) {
		SVCResult result = new SVCResult();
		JSONObject retdata = new JSONObject();

		ApiGlobal.logMessage(source + "," + userid + "," + ordId + "," + ordType + "," + price + "," + coupons + "," + devtoken);

		if (source.equals("") || userid < 0 || ordId < 0 || ordType < 0
				|| price < 0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
			result.retdata = retdata;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
			result.retdata = retdata;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				dbConn.setAutoCommit(false);//modify  by chuzhiqiang
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Order info
				SVCOrderTempDetails onceOrdInfo = null;
				SVCOrderOnOffDutyDetails dutyOrdInfo = null;
				SVCOrderOnOffDutyDivide divideInfo = null;
				SVCOrderLongDistanceDetails longOrdInfo = null;

				SVCOrderExecCS execInfo = null;

				// Ts info
				SVCTs frzTsInfo = null, userTsInfo = null;
				SVCFreezePoints frzInfo = null;

				// Coupon total price
				double ordPrice = 0, cpnPrice = 0;

				// Driver id
				long drvID = 0;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
					result.retdata = retdata;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
					result.retdata = retdata;
				} else {

					stmt = dbConn.createStatement();
					// 1,根据订单编号获取相应的执行订单
					if (ordType == 1) {// 1.1获取单次订单的执行订单
						onceOrdInfo = ApiGlobal.getOnceOrderInfo(dbConn, ordId);

						if (onceOrdInfo == null || onceOrdInfo.deleted == 1) {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
							result.retdata = retdata;
						} else if (onceOrdInfo.status != 6) // State not match
						{
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
							result.retdata = retdata;
						} else if (onceOrdInfo.publisher != userid) {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_PublisherNotMatch;
							result.retdata = retdata;
						} else {
							// Select exec info
							szQuery = "SELECT * FROM order_exec_cs WHERE deleted=0 AND id="
									+ onceOrdInfo.order_cs_id;

							rs = stmt.executeQuery(szQuery);
							if (rs.next()) // No order exec info
							{
								execInfo = SVCOrderExecCS
										.decodeFromResultSet(rs);

								if (execInfo.status != 6) {
									result.retcode = ConstMgr.ErrCode_Normal;
									result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
									result.retdata = retdata;
								}
							}
							rs.close();
						}
					} else if (ordType == 2) {// 1.2获取上下班订单的执行订单
						dutyOrdInfo = ApiGlobal
								.getOnOffOrderInfo(dbConn, ordId);

						if (dutyOrdInfo.status != 2) // State not match
						{
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
							result.retdata = retdata;
						} else if (dutyOrdInfo.publisher != userid) {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_PublisherNotMatch;
							result.retdata = retdata;
						} else {
							// Select divide_info
							szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
									+ dutyOrdInfo.id;

							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								divideInfo = SVCOrderOnOffDutyDivide
										.decodeFromResultSet(rs);
							rs.close();

							if (divideInfo != null) {
								// Select exec_details_info and exec_info
								szQuery = "SELECT "
										+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
										+ "		order_onoffduty_exec_details.order_cs_id,"
										+ "		order_exec_cs.* "

										+ "FROM order_onoffduty_exec_details "

										+ "INNER JOIN order_exec_cs "
										+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

										+ "WHERE "
										+ "		order_onoffduty_exec_details.deleted=0 AND "
										+ "		order_onoffduty_exec_details.onoffduty_divide_id="
										+ divideInfo.id
										+ " "

										+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

								rs = stmt.executeQuery(szQuery);
								if (rs.next()) {
									execInfo = SVCOrderExecCS
											.decodeFromResultSet(rs);

									if (execInfo.status != 6) {
										result.retcode = ConstMgr.ErrCode_Normal;
										result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
										result.retdata = retdata;
									}
								}
								rs.close();
								// /////////////////////////////////////////////////////////////////////////////////////////////

							}
						}
					} else if (ordType == 3) {// 1.3，获取长途订单的执行订单
						longOrdInfo = ApiGlobal.getLongOrderInfo(dbConn, ordId);

						if (longOrdInfo.status != 6) {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
							result.retdata = retdata;
						} else {
							// Select users cs info and exec ids
							szQuery = "SELECT "
									+ "		order_longdistance_users_cs.order_exec_cs_id,"
									+ "		order_exec_cs.* "

									+ "FROM "
									+ "		order_longdistance_users_cs "

									+ "INNER JOIN order_exec_cs "
									+ "ON order_exec_cs.id=order_longdistance_users_cs.order_exec_cs_id "

									+ "WHERE "
									+ "		order_exec_cs.status=6 AND "
									+ "		order_exec_cs.deleted=0 AND "
									+ "		order_exec_cs.passenger="
									+ userid
									+ " AND "
									+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
									+ longOrdInfo.id;

							rs = stmt.executeQuery(szQuery);
							if (rs.next()) {
								execInfo = SVCOrderExecCS
										.decodeFromResultSet(rs);

								if (execInfo.status != 6) {
									result.retcode = ConstMgr.ErrCode_Normal;
									result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
									result.retdata = retdata;
								}
							}
							rs.close();
						}
					}

					if (result.retcode >= 0) // No error occurred
					{
						ordPrice = execInfo.price;

						drvID = execInfo.driver;

						// 2,获取用户使用点券的总价值
						// Get coupon total price
						if (!coupons.equals("")) {
							szQuery = "SELECT SUM(sum) AS total_price FROM single_coupon WHERE id IN ("
									+ coupons + ")";
							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								cpnPrice = rs.getDouble("total_price");
							rs.close();
						}

						if (price + cpnPrice < ordPrice) // Price not fits.
															// Abnormal
															// situation
						{
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(75)";
							result.retdata = retdata;
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						} else {
							// 3,获取乘客被冻结的ts记录。下面这个sql语句最好再加一个ts_type作为限制条件
							// get freeze ts log
							szQuery = "SELECT * " + "FROM ts " + "WHERE "
									+ "		order_id=" + ordId + " AND "
									+ "		order_type=" + ordType + " AND ts_type='tx_code_009' and "
									+ "		deleted=0 AND " + "		userid=" + userid
									+ " " + "ORDER BY ts_date DESC LIMIT 0,1";

							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								frzTsInfo = SVCTs.decodeFromResultSet(rs);
							rs.close();
							// ////////////////////////////////////////////////////////////////////////////////////

							if (frzTsInfo != null) {
								// 4,根据上一步查询出的ts.id，在freeze_points表中查询出相应的冻结记录
								// get freeze freeze_points log
								szQuery = "SELECT * FROM freeze_points WHERE deleted=0 AND freeze_ts_id="
										+ frzTsInfo.id;

								rs = stmt.executeQuery(szQuery);
								if (rs.next())
									frzInfo = SVCFreezePoints
											.decodeFromResultSet(rs);
								rs.close();
								// ////////////////////////////////////////////////////////////////////////////////////
							}
							// 5,获取用户的最新余额
							// get user balance ts log
							szQuery = "SELECT * " + "FROM ts " + "WHERE "
									+ "		deleted=0 AND " + "		id="
									+ userinfo.balance_ts;

							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								userTsInfo = SVCTs.decodeFromResultSet(rs);
							rs.close();
							//
							String szApp = "";
							if (source.equals(ConstMgr.SRC_MAINAPP))
								szApp = ConstMgr.SRC_CHN_MAINAPP;
							else if (source.equals(ConstMgr.SRC_PINCHEAPP))
								szApp = ConstMgr.SRC_CHN_PINCHEAPP;
							else
								szApp = ConstMgr.STR_OTHERS;
							// ////////////////////////////////////////////////////////////////////////////////////
							// 6,如果使用的代金券大于0，则为乘客使用代金券来抵一部分款
							if (cpnPrice > 0) {
								double retPrice = Math.min(cpnPrice, ordPrice);// 如果取代金券金额和订单金额当中比较小的，如果cpnPrice小于订单金额，
																				// 那么就为乘客支付cpnPrice;如果cpnPrice大于订单金额，那么就为乘客
																				// 支付ordPrice额度的代金券
								

								long newTsID = 0;
								// 6.1实际使用代金券为乘客支付，方式是把代金券的金额折算成钱加到乘客的余额中。这里有个小问题，如果订单是10元钱，共选择了30元的代金券，那么代金券支付完之后剩余的20元也不会返还给乘客了。
								//
								// Return <cpnPrice> to user
								szQuery = "INSERT INTO ts (" + "order_cs_id,"
										+ "order_id," + "order_type," + "oper,"
										+ "ts_way," + "balance," + "ts_date,"
										+ "userid," + "groupid," + "unitid,"
										+ "remark," + "account,"
										+ "account_type," + "application,"
										+ "ts_type," + "sum" + ") VALUES ("
										+ execInfo.id
										+ "," // order_cs_id
										+ ordId
										+ "," // order_id
										+ ordType
										+ "," // order_type
										+ ConstMgr.TSOPER_TYPE_IN
										+ "," // oper
										+ 0
										+ "," // ts_way
										+ (userTsInfo.balance + retPrice)
										+ ",\"" // balance
										+ ApiGlobal.Date2String(new Date(),
												true)
										+ "\"," // ts_date
										+ userid
										+ "," // userid
										+ 0
										+ "," // groupid
										+ 0
										+ ",\"" // unitid
										+ ApiGlobal.getCommentFromTxCode(
												dbConn,
												ConstMgr.txcode_returnBalance)
										+ "\", " // remark
										+ "'', " // account
										+ ConstMgr.USERTYPE_USER + ",\"" // account_type
										+ szApp + "\",\"" // application
										+ ConstMgr.txcode_returnBalance + "\"," // ts_type
										+ retPrice + ")"; // sum

								if (stmt.executeUpdate(szQuery,
										Statement.RETURN_GENERATED_KEYS) == 1) {
									rs = stmt.getGeneratedKeys();
									if (rs.next())
										newTsID = rs.getLong(1);
									rs.close();
								}
								// 6.2使用完代金券之后，更新乘客的最新余额
								if (newTsID > 0) {
									// Update user info
									szQuery = "UPDATE user SET balance_ts="
											+ newTsID + " WHERE id=" + userid;
									stmt.executeUpdate(szQuery);
								}
								// 6.3把刚才使用的所有代金券的状态全标记为已使用过了
								// Update coupons state as used
								if (!coupons.equals("")) {
									szQuery = "UPDATE "
											+ "		single_coupon "
											+ "SET "
											+ "		isused=1,"
											+ "		date_used=\""
											+ ApiGlobal.Date2String(new Date(),
													true) + "\""
											+ " WHERE id IN (" + coupons + ")";
									stmt.executeUpdate(szQuery);
								}
								
								//6.4点券的钱要系统用户来出
								//如果乘客用了点券，那么点券的钱需要系统来出，这里较难理解。
								//首先点券要真正使用，必须把他转换为真正的绿点，这里从sys_id中减去点券的金额就相当于把他转换成了真正的绿点(也就是sys_id出了这笔钱) 
								long sys_id = ApiGlobal.getSystemID(dbConn);//平台用户
								ApiGlobal.shareTreeLog(dbConn, execInfo.id,
										"couponFee_to_user", // Branch no
										sys_id, userid, 0, 0, -retPrice,
										ConstMgr.USERTYPE_OTHER, ConstMgr.USERTYPE_USER);
								//从平台用户中取出用户点券的钱，注意平台用户也在user表里，所以这里的参数是ConstMgr.USERTYPE_USER
								ApiGlobal.addBalance(dbConn, source, sys_id,
										ConstMgr.USERTYPE_USER, -retPrice,
										ConstMgr.txcode_couponPrice, execInfo.id, 0, 0);
								
							}
							// 7,把freeze_points表里冻结的该乘客的款项解冻（这里有问题，解冻后的钱去哪了,这里解冻后的金额没有插入到ts表里，而是直接做为了总收款进行了各种分配，详细见payToDriver方法），并调用payToDriver方法为乘客付款并生成结算树
							// Update freeze_points log as used
							//7.01获取用户冻结的金额
							szQuery="select sum from ts where id="+frzInfo.id;//取出冻结的金额
							rs=stmt.executeQuery(szQuery);
							double frzSum=0;//冻结的金额
							if(rs.next()){
								frzSum=rs.getDouble("sum");
							}
							// 7.02将用户冻结的金额解冻，但是首先要重新查询用户的最新余额，因为上一步的给用户充值点券的步骤变动了用户余额
							szQuery = "SELECT * " + "FROM ts " + "WHERE "
									+ "		deleted=0 AND " + "		id="
									+ userinfo.balance_ts;

							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								userTsInfo = SVCTs.decodeFromResultSet(rs);
							rs.close();
							szQuery="INSERT INTO ts ("
									+ "order_cs_id," + "order_id," + "order_type," + "oper,"
									+ "ts_way," + "balance," + "ts_date," + "userid,"
									+ "groupid," + "unitid," + "remark," + "account,"
									+ "account_type," + "application," + "ts_type," + "sum,"
									+ "deleted"
									+ ") VALUES("+execInfo.id+",0,0,2,"
											+ "4,"+(userTsInfo.balance+frzSum)+",now(),"+userid
											+",0,0,'解冻绿点','',"
											+ "1,'"+szApp+"','tx_code_010',"+frzSum+","
											+ "0) ";
							System.out
									.println("SVCOrderService.payNormalOrder()---------"+szQuery);
							long newTsID=0;
							if (stmt.executeUpdate(szQuery,
									Statement.RETURN_GENERATED_KEYS) == 1) {
								rs = stmt.getGeneratedKeys();
								if (rs.next())
									newTsID = rs.getLong(1);
								rs.close();
							}
							if (newTsID > 0) {
								// Update user info
								szQuery = "UPDATE user SET balance_ts="
										+ newTsID + " WHERE id=" + userid;
								stmt.executeUpdate(szQuery);
							}
							//7.03，给当前用户解冻
							szQuery = "UPDATE freeze_points SET state=2,release_ts_id="+newTsID+" WHERE deleted=0 AND state=0 AND id="
									+ frzInfo.id;
							stmt.executeUpdate(szQuery);
							
							
							// ////////////////////////////////////////////////////////////////////////////////////

							// Give total price to driver and some other related
							// users
							if (ApiGlobal.payToDriver_new(dbConn, source, ordPrice,
									cpnPrice, userid, drvID, execInfo.id) < 0) {// 7.1调用payToDriver方法，为乘客付款，并生成结算树
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(76)";
								result.retdata = retdata;
							} else {// 7.2标记order_exec_cs为结算完成
								szQuery = "UPDATE "
										+ "		order_exec_cs "
										+ "SET "
										+ "		status=7,"
										+ "		pay_time='"
										+ ApiGlobal.Date2String(new Date(),
												true) + "' " + "WHERE "
										+ "		id=" + execInfo.id;

								stmt.executeUpdate(szQuery);
								// 7.3，标记单次订单表为结算完成
								if (ordType == 1) // Once order
								{
									szQuery = "UPDATE "
											+ "		order_temp_details "
											+ "SET "
											+ "		status=7,"
											+ "		pay_time='"
											+ ApiGlobal.Date2String(new Date(),
													true) + "' " + "WHERE "
											+ "		id=" + onceOrdInfo.id;
									stmt.executeUpdate(szQuery);
								} else if (ordType == 3) // Long distance order
								{
									boolean allPaid = true;
									// 7.4，标记长途订单里相应的乘客的执行订单为结算完成
									szQuery = "SELECT * "
											+ "FROM order_longdistance_users_cs "

											+ "INNER JOIN order_exec_cs "
											+ "ON order_exec_cs.id=order_longdistance_users_cs.order_exec_cs_id "

											+ "WHERE "
											+ "		order_exec_cs.status=6 AND "
											+ "		order_exec_cs.deleted=0 AND "
											+ "		order_longdistance_users_cs.deleted=0 AND "
											+ "		order_longdistance_users_cs.orderdriverlongdistance_id="
											+ longOrdInfo.id;

									rs = stmt.executeQuery(szQuery);
									if (rs.next())
										allPaid = false;
									rs.close();
									// 7.5,如果长途订单里所有的执行订单都已经结算完成，则标记order_longdistance_details为结算完成
									if (allPaid) {
										szQuery = "UPDATE "
												+ "		order_longdistance_details "
												+ "SET "
												+ "		status=7,"
												+ "		pay_time='"
												+ ApiGlobal.Date2String(
														new Date(), true)
												+ "' " + "WHERE " + "		id="
												+ longOrdInfo.id;
										stmt.executeUpdate(szQuery);
									}
								}

								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;
								retdata.put("balance", ApiGlobal
										.getUserBalance(dbConn, userid));
								result.retdata = retdata;

								// Calculate passenger carpool count and send
								// coupon if condition is right
								// 8,如果用户还没有享受过"完成X各订单送点券"服务，则判断用户已经完成了多少个点券了，看够数了没
								if (userinfo.got_order_coupon == 0) {// 8.1,got_order_coupon==0表示用户还没有享受过"完成X个订单送点券服务"。
									int nCount = 0;
									// 查询乘客做了多少单了
									szQuery = "SELECT COUNT(*) AS cnt "
											+ "FROM order_exec_cs " + "WHERE "
											+ "		status=7 AND "
											+ "		deleted=0 AND "
											+ "		(passenger=" + userid + " OR "
											+ "		driver=" + userid + ")";
									rs = stmt.executeQuery(szQuery);
									if (rs.next())
										nCount = rs.getInt("cnt");
									rs.close();

									long sysCouponId = 0;
									int nOrderCount = 0;
									String sysCouponCode = "";
									BigDecimal couponSum = null;
									int validPeriodUnit = 0;
									int validPeriod = 0;
									Date dtExp = null;

									szQuery = "SELECT * FROM city WHERE code=\""
											+ userinfo.city_cur + "\"";
									rs = stmt.executeQuery(szQuery);
									if (rs.next()) {
										sysCouponCode = rs
												.getString("finishorders_syscoupon_id");
										nOrderCount = rs
												.getInt("num_order_finished");
									}
									rs.close();

									if (sysCouponCode.equals("")) {
										szQuery = "SELECT * FROM global_params WHERE deleted=0";
										rs = stmt.executeQuery(szQuery);
										if (rs.next()) {
											sysCouponCode = rs
													.getString("finishorders_syscoupon_id");// 这个地方写错了，应该是finishorders_syscoupon_id
											nOrderCount = rs
													.getInt("num_order_finished");
										}
										rs.close();
									}

									if (nOrderCount > 0 && nOrderCount < nCount) {// 如果乘客已经完成了或超过了nOrderCount个订单，那么需要奖励给乘客点券，注意19637行写错了
										// 8.2,查询系统"完成X各订单"发什么点券
										szQuery = "SELECT id,sum,valid_period_unit,valid_period "
												+ "FROM sys_coupon "
												+ "WHERE "
												+ "		syscoupon_code = \""
												+ sysCouponCode + "\"";

										rs = stmt.executeQuery(szQuery);
										if (rs.next()) {
											sysCouponId = rs.getLong("id");
											couponSum = rs.getBigDecimal("sum");
											validPeriodUnit = rs
													.getInt("valid_period_unit");
											validPeriod = rs
													.getInt("valid_period");

											Calendar cal_cur = Calendar
													.getInstance();
											if (validPeriodUnit == 1) {
												cal_cur.add(
														Calendar.DAY_OF_YEAR,
														validPeriod);
											} else if (validPeriodUnit == 2) {
												cal_cur.add(
														Calendar.WEEK_OF_YEAR,
														validPeriod);
											} else if (validPeriodUnit == 3) {
												cal_cur.add(Calendar.MONTH,
														validPeriod);
											} else if (validPeriodUnit == 4) {
												cal_cur.add(Calendar.YEAR,
														validPeriod);
											}
											dtExp = cal_cur.getTime();
										}
										// 8.3，将点券发给乘客
										if (dtExp != null) {
											szQuery = "INSERT INTO single_coupon (sum,date_expired,ps_date,userid,syscoupon_id) VALUES ("
													+ couponSum
													+ ",\""
													+ ApiGlobal.Date2String(
															dtExp, true)
													+ "\",\""
													+ ApiGlobal.Date2String(
															new Date(), true)
													+ "\","
													+ userid
													+ ","
													+ sysCouponId + ")";
											stmt.executeUpdate(
													szQuery,
													Statement.RETURN_GENERATED_KEYS);
											long couponid = 0;
											rs = stmt.getGeneratedKeys();
											if (rs.next()) {
												String szMsg = "恭喜您在OO车系统注册成功.给您一个点券.请查看";
												couponid = rs.getLong(1);

												ApiGlobal
														.addPersonNotification(
																dbConn, userid,
																couponid, szMsg);
											}
											rs.close();
											// 8.4,标记乘客已经享受了“完成X各订单发点券”的优惠。
											szQuery = "UPDATE user SET got_order_coupon=1 WHERE id="
													+ userid;
											stmt.executeUpdate(szQuery);
										}
									}
								}

								// Calculate driver carpool count and send
								// coupon if condition is right
								SVCUser drvinfo = ApiGlobal
										.getUserInfoFromUserID(dbConn, drvID);
								// 9,如果车主还没有享受过"完成X各订单送点券"服务，则判断车主已经完成了多少个点券了，看够数了没
								if (drvinfo.got_order_coupon == 0) {// 9.1,got_order_coupon==0表示用户还没有享受过"完成X个订单送点券服务"。
									int nCount = 0;
									// 查询车主做了多少单了
									szQuery = "SELECT COUNT(*) AS cnt "
											+ "FROM order_exec_cs " + "WHERE "
											+ "		status=7 AND "
											+ "		deleted=0 AND "
											+ "		(passenger=" + drvID + " OR "
											+ "		driver=" + drvID + ")";
									rs = stmt.executeQuery(szQuery);
									if (rs.next())
										nCount = rs.getInt("cnt");
									rs.close();

									long sysCouponId = 0;
									int nOrderCount = 0;
									String sysCouponCode = "";
									BigDecimal couponSum = null;
									int validPeriodUnit = 0;
									int validPeriod = 0;
									Date dtExp = null;

									szQuery = "SELECT * FROM city WHERE code=\""
											+ drvinfo.city_cur + "\"";
									rs = stmt.executeQuery(szQuery);
									if (rs.next()) {
										sysCouponCode = rs
												.getString("finishorders_syscoupon_id");
										nOrderCount = rs
												.getInt("num_order_finished");
									}
									rs.close();

									if (sysCouponCode.equals("")) {
										szQuery = "SELECT * FROM global_params WHERE deleted=0";
										rs = stmt.executeQuery(szQuery);
										if (rs.next()) {
											sysCouponCode = rs
													.getString("finishorders_syscoupon_id");
											nOrderCount = rs
													.getInt("num_order_finished");
										}
										rs.close();
									}

									if (nOrderCount > 0 && nOrderCount < nCount) {// 如果乘客已经完成了或超过了nOrderCount个订单，那么需要奖励给车主点券，注意19761行写错了
										// 9.2,查询系统"完成X各订单"发什么点券
										szQuery = "SELECT id,sum,valid_period_unit,valid_period "
												+ "FROM sys_coupon "
												+ "WHERE "
												+ "		syscoupon_code = \""
												+ sysCouponCode + "\"";

										rs = stmt.executeQuery(szQuery);
										if (rs.next()) {
											sysCouponId = rs.getLong("id");
											couponSum = rs.getBigDecimal("sum");
											validPeriodUnit = rs
													.getInt("valid_period_unit");
											validPeriod = rs
													.getInt("valid_period");

											Calendar cal_cur = Calendar
													.getInstance();
											if (validPeriodUnit == 1) {
												cal_cur.add(
														Calendar.DAY_OF_YEAR,
														validPeriod);
											} else if (validPeriodUnit == 2) {
												cal_cur.add(
														Calendar.WEEK_OF_YEAR,
														validPeriod);
											} else if (validPeriodUnit == 3) {
												cal_cur.add(Calendar.MONTH,
														validPeriod);
											} else if (validPeriodUnit == 4) {
												cal_cur.add(Calendar.YEAR,
														validPeriod);
											}
											dtExp = cal_cur.getTime();
										}
										// 9.3，给车主发点券
										if (dtExp != null) {
											szQuery = "INSERT INTO single_coupon (sum,date_expired,ps_date,userid,syscoupon_id) VALUES ("
													+ couponSum
													+ ",\""
													+ ApiGlobal.Date2String(
															dtExp, true)
													+ "\",\""
													+ ApiGlobal.Date2String(
															new Date(), true)
													+ "\","
													+ drvID
													+ ","
													+ sysCouponId + ")";
											stmt.executeUpdate(
													szQuery,
													Statement.RETURN_GENERATED_KEYS);
											long couponid = 0;
											rs = stmt.getGeneratedKeys();
											if (rs.next()) {
												String szMsg = "恭喜您在OO车系统注册成功.给您一个点券.请查看";
												couponid = rs.getLong(1);

												ApiGlobal
														.addPersonNotification(
																dbConn, drvID,
																couponid, szMsg);
											}
											rs.close();
											// 9.4,标记该车主享受了“完成X单发点券”的优惠
											szQuery = "UPDATE user SET got_order_coupon=1 WHERE id="
													+ drvID;
											stmt.executeUpdate(szQuery);
										}
									}
								}

							}
						}
					}
				}
				dbConn.commit();//modify  by chuzhiqiang
			} catch (Exception ex) {
				//begain   modify by chuzhiqiang
				try {
					dbConn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				//end   modify by chuzhiqiang
				ex.printStackTrace();

				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ex.getMessage();
				result.retdata = retdata;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult payReserveOrder(String source, long userid, long ordId,
			double price, String coupons, String devtoken) {
		SVCResult result = new SVCResult();
		JSONObject retdata = new JSONObject();

		ApiGlobal.logMessage(source + "," + userid + "," + ordId + "," + price + "," + coupons + "," + devtoken);

		if (source.equals("") || userid < 0 || ordId < 0 || price < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
			result.retdata = retdata;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
			result.retdata = retdata;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Order info
				SVCOrderOnOffDutyDetails dutyOrdInfo = null;
				SVCOrderOnOffDutyDivide divideInfo = null;
				SVCOrderExecCS execInfo = null;

				// Ts info
				SVCTs frzTsInfo = null, userTsInfo = null;
				SVCFreezePoints frzInfo = null;

				// Coupon total price
				double ordPrice = 0, cpnPrice = 0;

				// Driver id
				long drvID = 0;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
					result.retdata = retdata;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
					result.retdata = retdata;
				} else if (ApiGlobal.getUserBalance(dbConn, userid) < price) // Device
				// token
				// not
				// match
				{
					result.retcode = ConstMgr.ErrCode_NotEnoughBalance;
					result.retmsg = ConstMgr.ErrMsg_NotEnoughBalance;
					result.retdata = retdata;
				} else {

					stmt = dbConn.createStatement();

					dutyOrdInfo = ApiGlobal.getOnOffOrderInfo(dbConn, ordId);

					if (dutyOrdInfo.status != 2) // State not match
					{
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
						result.retdata = retdata;
					} else if (dutyOrdInfo.publisher != userid) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_PublisherNotMatch;
						result.retdata = retdata;
					} else {
						// Select divide_info
						szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
								+ dutyOrdInfo.id;

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							divideInfo = SVCOrderOnOffDutyDivide
									.decodeFromResultSet(rs);
						rs.close();

						if (divideInfo != null) {
							// Select exec_details_info and exec_info
							szQuery = "SELECT "
									+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
									+ "		order_onoffduty_exec_details.order_cs_id,"
									+ "		order_exec_cs.* "

									+ "FROM order_onoffduty_exec_details "

									+ "INNER JOIN order_exec_cs "
									+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

									+ "WHERE "
									+ "		order_onoffduty_exec_details.deleted=0 AND "
									+ "		order_onoffduty_exec_details.onoffduty_divide_id="
									+ divideInfo.id
									+ " "

									+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

							rs = stmt.executeQuery(szQuery);
							if (rs.next()) {
								execInfo = SVCOrderExecCS
										.decodeFromResultSet(rs);

								if (execInfo.status != 6) {
									result.retcode = ConstMgr.ErrCode_Normal;
									result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
									result.retdata = retdata;
								}
							}
							rs.close();
							// /////////////////////////////////////////////////////////////////////////////////////////////

						}
					}

					if (result.retcode >= 0) // No error occurred
					{
						ordPrice = execInfo.price;
						drvID = execInfo.driver;

						// Get coupon total price
						if (!coupons.equals("")) {
							szQuery = "SELECT SUM(sum) AS total_price FROM single_coupon WHERE id IN ("
									+ coupons + ")";
							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								cpnPrice = rs.getDouble("total_price");
							rs.close();
						}

						if (price + cpnPrice < ordPrice) // Price not fits.
															// Abnormal
															// situation
						{
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(77)";
							result.retdata = retdata;
						} else {
							// get freeze ts log
							szQuery = "SELECT * " + "FROM ts " + "WHERE "
									+ "		order_id=" + ordId + " AND "
									+ "		order_type=2 AND "
									+ "		deleted=0 AND " + "		userid=" + userid
									+ " " + "ORDER BY ts_date DESC LIMIT 0,1";

							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								frzTsInfo = SVCTs.decodeFromResultSet(rs);
							rs.close();
							// ////////////////////////////////////////////////////////////////////////////////////

							if (frzTsInfo != null) {
								// get freeze freeze_points log
								szQuery = "SELECT * FROM freeze_points WHERE deleted=0 AND freeze_ts_id="
										+ frzTsInfo.id;

								rs = stmt.executeQuery(szQuery);
								if (rs.next())
									frzInfo = SVCFreezePoints
											.decodeFromResultSet(rs);
								rs.close();
								// ////////////////////////////////////////////////////////////////////////////////////
							}

							// get user balance ts log
							szQuery = "SELECT * " + "FROM ts " + "WHERE "
									+ "		deleted=0 AND " + "		id="
									+ userinfo.balance_ts;

							rs = stmt.executeQuery(szQuery);
							if (rs.next())
								userTsInfo = SVCTs.decodeFromResultSet(rs);
							rs.close();
							// ////////////////////////////////////////////////////////////////////////////////////

							if (cpnPrice > 0) {
								String szApp = "";
								if (source.equals(ConstMgr.SRC_MAINAPP))
									szApp = ConstMgr.SRC_CHN_MAINAPP;
								else if (source.equals(ConstMgr.SRC_PINCHEAPP))
									szApp = ConstMgr.SRC_CHN_PINCHEAPP;
								else
									szApp = ConstMgr.STR_OTHERS;

								long newTsID = 0;

								// Return <cpnPrice> to user
								szQuery = "INSERT INTO ts (" + "order_cs_id,"
										+ "order_id," + "order_type," + "oper,"
										+ "ts_way," + "balance," + "ts_date,"
										+ "userid," + "groupid," + "unitid,"
										+ "remark," + "account,"
										+ "account_type," + "application,"
										+ "ts_type," + "sum" + ") VALUES ("
										+ execInfo.id
										+ ","
										+ ordId
										+ ","
										+ 2
										+ ","
										+ 2
										+ ","
										+ 0
										+ ","
										+ userTsInfo.balance
										+ cpnPrice
										+ ",\""
										+ ApiGlobal.Date2String(new Date(),
												true)
										+ "\","
										+ drvID
										+ ","
										+ 0
										+ ","
										+ 0
										+ ","
										+ "'', "
										+ "'', "
										+ 1
										+ ",\""
										+ szApp
										+ "\",\""
										+ ConstMgr.txcode_returnBalance
										+ "\","
										+ cpnPrice + "," + 0 + ")";

								if (stmt.executeUpdate(szQuery,
										Statement.RETURN_GENERATED_KEYS) == 1) {
									rs = stmt.getGeneratedKeys();
									if (rs.next())
										newTsID = rs.getLong(1);
									rs.close();
								}

								if (newTsID > 0) {
									// Update user info
									szQuery = "UPDATE user SET balance_ts="
											+ newTsID + " WHERE id=" + userid;
									stmt.executeUpdate(szQuery);
								}

								// Update coupons state as used
								if (!coupons.equals("")) {
									szQuery = "UPDATE single_coupon SET isused=1,date_used=\""
											+ ApiGlobal.Date2String(new Date(),
													true)
											+ "\" WHERE id IN ("
											+ coupons + ")";
									stmt.executeUpdate(szQuery);
								}
							}

							// Update freeze_points log as used
							szQuery = "UPDATE freeze_points SET state=2 WHERE deleted=0 AND state=0 AND id="
									+ frzInfo.id;
							stmt.executeUpdate(szQuery);
							// ////////////////////////////////////////////////////////////////////////////////////

							// Give total price to driver and some other related
							// users
							if (ApiGlobal.payToDriver(dbConn, source, ordPrice,
									cpnPrice, userid, drvID, execInfo.id) < 0) {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(78)";
								result.retdata = retdata;
							}

							if (result.retcode >= 0) {
								String szCurTime = ApiGlobal.Date2String(
										new Date(), true);

								// Default good evaluation
								szQuery = "INSERT INTO evaluation_cs ("
										+ "from_userid," + "to_userid,"
										+ "level," + "msg," + "ps_date,"
										+ "usertype," + "order_cs_id"
										+ ") VALUES (" + userid + "," + drvID
										+ "," + 1 + ",\""
										+ ConstMgr.STR_MORENHAOPING + "\",\""
										+ szCurTime + "\",1," + execInfo.id
										+ ")";
								stmt.executeUpdate(szQuery);

								// Insert new log into order_exec_cs table
								Date pre_time = ApiGlobal
										.nextValidDay(divideInfo.which_days
												.split(","), execInfo.pre_time,
												dutyOrdInfo.pre_time);

								// Get mid points information
								String szMidPoints = "";
								szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=1 AND orderid="
										+ dutyOrdInfo.id;
								rs = stmt.executeQuery(szQuery);
								while (rs.next()) {
									if (!szMidPoints.equals(""))
										szMidPoints += ",";
									szMidPoints += "" + rs.getDouble("lat")
											+ "," + rs.getDouble("lng") + ","
											+ rs.getString("addr");
								}
								rs.close();

								szQuery = "INSERT INTO order_exec_cs ("
										+ "order_type," + "passenger,"
										+ "driver," + "from_," + "to_,"
										+ "average_price_platform," + "price,"
										+ "pre_time," + "remark," + "cr_date,"
										+ "ti_accept_order," + "city_from,"
										+ "city_to,"
										+ "has_evaluation_passenger,"
										+ "has_evaluation_driver,"
										+ "password," + "begin_lat,"
										+ "begin_lng," + "end_lat,"
										+ "end_lng," + "freeze_points,"
										+ "total_distance," + "order_city,"
										+ "status," + "deleted" + ") VALUES ("
										+ "3,"
										+ dutyOrdInfo.publisher
										+ ","
										+ drvID
										+ ",\""
										+ dutyOrdInfo.start_addr
										+ "\",\""
										+ dutyOrdInfo.end_addr
										+ "\","
										+ ApiGlobal
												.getDistAndSystemAveragePrice(
														dbConn,
														userid,
														ApiGlobal
																.cityCode2Name(
																		dbConn,
																		dutyOrdInfo.order_city),
														ApiGlobal
																.Date2String(
																		dutyOrdInfo.pre_time,
																		true),
														dutyOrdInfo.start_lat,
														dutyOrdInfo.start_lng,
														dutyOrdInfo.end_lat,
														dutyOrdInfo.end_lng,
														szMidPoints,
														dutyOrdInfo.reqcarstyle)
												.getDouble("price")
										+ ","
										+ dutyOrdInfo.price
										+ ",\""
										+ ApiGlobal.Date2String(pre_time, true)
										+ "\",\""
										+ dutyOrdInfo.remark
										+ "\",\""
										+ szCurTime
										+ "\",\""
										+ szCurTime
										+ "\",\""
										+ dutyOrdInfo.order_city
										+ "\",\""
										+ dutyOrdInfo.order_city
										+ "\",0,0,\""
										+ execInfo.password
										+ "\","
										+ dutyOrdInfo.start_lat
										+ ","
										+ dutyOrdInfo.start_lng
										+ ","
										+ dutyOrdInfo.end_lat
										+ ","
										+ dutyOrdInfo.end_lng
										+ ","
										+ dutyOrdInfo.price
										+ ",0,\""
										+ dutyOrdInfo.order_city + "\",2,0)";

								if (stmt.executeUpdate(szQuery,
										Statement.RETURN_GENERATED_KEYS) == 1) {
									ResultSet genKeys = stmt.getGeneratedKeys();
									long order_exec_id = genKeys.getLong(1);
									// Freeze balance points
									if (ApiGlobal.freezePointsWithType(dbConn,
											source, userid, order_exec_id,
											ordId, 2, price,
											ConstMgr.txcode_userFreezeBalance) > 0) {
										result.retcode = ConstMgr.ErrCode_None;
										result.retmsg = ConstMgr.ErrMsg_None;
										retdata.put("balance", ApiGlobal
												.getUserBalance(dbConn, userid));
										result.retdata = retdata;
									} else {
										result.retcode = ConstMgr.ErrCode_Exception;
										result.retmsg = ConstMgr.ErrMsg_Exception
												+ "(85)";
										result.retdata = retdata;
									}
								} else {
									result.retcode = ConstMgr.ErrCode_Exception;
									result.retmsg = ConstMgr.ErrMsg_Exception
											+ "(79)";
									result.retdata = retdata;
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();

				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ex.getMessage();
				result.retdata = retdata;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,获取单次订单状态 1.1,将distance设置到order_exec_cs表，在结束服务获取者订单取消之前设置一下汽车走了多少里程
	 * 
	 * 2,获取长途订单的状态 2.1获取该长途订单的执行订单 2.2在结束服务或者取消订单之前，设置一下汽车走了多少里程
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param order_type
	 * @param distance
	 * @param devtoken
	 * @return
	 */
	public SVCResult orderCancelled(String source, long userid, long orderid,
			int order_type, double distance, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + order_type + "," + distance + "," + devtoken);

		if (source.equals("") || userid <= 0 || orderid <= 0 || order_type <= 0
				|| distance < 0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Order info
				SVCOrderTempDetails onceOrdInfo = null;
				SVCOrderLongDistanceDetails longOrdInfo = null;

				// On off order info
				SVCOrderOnOffDutyDetails dutyOrdInfo = null;
				SVCOrderOnOffDutyDivide divideInfo = null;
				SVCOrderExecCS execInfo = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {

					stmt = dbConn.createStatement();

					if (order_type == 1) {
						// 1,获取单次订单状态
						onceOrdInfo = ApiGlobal.getOnceOrderInfo(dbConn,
								orderid);
						// 1.1,将distance设置到order_exec_cs表，在结束服务获取者订单取消之前设置一下汽车走了多少里程
						if (onceOrdInfo.order_cs_id > 0) {
							szQuery = "UPDATE order_exec_cs SET total_distance="
									+ distance
									+ " WHERE id="
									+ onceOrdInfo.order_cs_id;
							stmt.executeUpdate(szQuery);
						}

						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;

						JSONObject retdata = new JSONObject();
						if (onceOrdInfo.deleted == 1 || onceOrdInfo.status == 8) {
							retdata.put("status", 1);
							retdata.put("status_desc",
									ConstMgr.STR_ORDERSTATE_YIGUANBI);
						} else {
							retdata.put("status", 0);
							retdata.put("status_desc",
									ConstMgr.STR_ORDERSTATE_WEIGUANBI);
						}

						result.retdata = retdata;
					} else if (order_type == 2) {
						dutyOrdInfo = ApiGlobal.getOnOffOrderInfo(dbConn,
								orderid);

						// Select divide_info
						szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
								+ dutyOrdInfo.id;

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							divideInfo = SVCOrderOnOffDutyDivide
									.decodeFromResultSet(rs);
						rs.close();

						if (divideInfo != null) {
							// Select exec_details_info and exec_info
							szQuery = "SELECT "
									+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
									+ "		order_onoffduty_exec_details.order_cs_id,"
									+ "		order_exec_cs.* "

									+ "FROM order_onoffduty_exec_details "

									+ "INNER JOIN order_exec_cs "
									+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

									+ "WHERE "
									+ "		order_onoffduty_exec_details.deleted=0 AND "
									+ "		order_onoffduty_exec_details.onoffduty_divide_id="
									+ divideInfo.id
									+ " "

									+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

							rs = stmt.executeQuery(szQuery);
							if (rs.next()) {
								execInfo = SVCOrderExecCS
										.decodeFromResultSet(rs);

								szQuery = "UPDATE order_exec_cs SET total_distance="
										+ distance + " WHERE id=" + execInfo.id;
								stmt.executeUpdate(szQuery);

								result.retcode = ConstMgr.ErrCode_None;
								result.retmsg = ConstMgr.ErrMsg_None;

								JSONObject retdata = new JSONObject();
								if (execInfo.status == 8) {
									retdata.put("status", 1);
									retdata.put("status_desc",
											ConstMgr.STR_ORDERSTATE_YIGUANBI);
								} else {
									retdata.put("status", 0);
									retdata.put("status_desc",
											ConstMgr.STR_ORDERSTATE_WEIGUANBI);
								}

								result.retdata = retdata;
							} else {
								result.retcode = ConstMgr.ErrCode_Normal;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(80)";
							}
							rs.close();
							// /////////////////////////////////////////////////////////////////////////////////////////////
						} else {
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_NotCancelled;
						}
					} else if (order_type == 3) {
						// 2,获取长途订单的状态
						longOrdInfo = ApiGlobal.getLongOrderInfo(dbConn,
								orderid);

						String szExecIDs = "";
						// 2.1获取该长途订单的执行订单
						// Select users cs info and exec ids
						szQuery = "SELECT order_exec_cs_id FROM order_longdistance_users_cs WHERE orderdriverlongdistance_id="
								+ longOrdInfo.id;
						rs = stmt.executeQuery(szQuery);
						while (rs.next()) {
							long exec_cs_id = rs.getLong("order_exec_cs_id");
							if (!szExecIDs.equals(""))
								szExecIDs += ",";
							szExecIDs += exec_cs_id;
						}
						rs.close();
						// 2.2在结束服务或者取消订单之前，设置一下汽车走了多少里程
						if (!szExecIDs.equals("")) {
							szQuery = "UPDATE order_exec_cs SET total_distance="
									+ distance
									+ " WHERE id IN ("
									+ szExecIDs
									+ ") AND status<>8 AND deleted=0";
							stmt.executeUpdate(szQuery);
						}

						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;

						JSONObject retdata = new JSONObject();

						if (longOrdInfo.status == 8) {
							retdata.put("status", 1);
							retdata.put("status_desc",
									ConstMgr.STR_ORDERSTATE_YIGUANBI);
						} else {
							retdata.put("status", 0);
							retdata.put("status_desc",
									ConstMgr.STR_ORDERSTATE_WEIGUANBI);
						}

						result.retdata = retdata;
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 该接口由车主每三秒循环调用一次，用来检查自己是否抢单成功。 1,根据订单编号获取抢单的车主(如果数据库里的抢单车主不是当前车主则返回)
	 * 2,获取抢单的车主，是否抢单成功 2.1,车主抢单失败 2.2车主抢单后，乘客还没有确认
 * 3,车主抢单后，乘客已经确认此车主抢单成功。获取乘客的信息。
 * 
 * @param source
 * @param userid
 * @param orderid
 * @param devtoken
 * @return
 */
	public SVCResult checkOnceOrderAgree(String source, long userid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.deleted == 1) {
					result.retcode = ConstMgr.ErrCode_PassDisagreed;
					result.retmsg = ConstMgr.ErrMsg_PassDisagreed;
				} else {
					// 1,根据订单编号获取抢单的车主
					SVCOrderTempGrab grab_info = ApiGlobal
							.getLatestGrabInfoFromOrderID(dbConn, orderid);

					if (grab_info == null || grab_info.driverid != userid) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_PublisherNotMatch;
					} else {
						// 2,获取抢单的车主，是否抢单成功
						if (grab_info.status == 2) // Disagree//2.1,车主抢单失败
						{
							result.retcode = ConstMgr.ErrCode_PassDisagreed;
							result.retmsg = ConstMgr.ErrMsg_PassDisagreed;
						} else if (grab_info.status == 0) // Not agreed
															// yet//2.2车主抢单后，乘客还没有确认
						{
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_NotAgreedYet;
						} else // Agreed//3,车主抢单后，乘客已经确认此车主抢单成功。获取乘客的信息。
						{
							SVCUser passinfo = ApiGlobal.getUserInfoFromUserID(
									dbConn, order_info.publisher);

							JSONObject retdata = new JSONObject();
							retdata.put("pass_img",
									ApiGlobal.getAbsoluteURL(passinfo.img));
							retdata.put("pass_name", passinfo.nickname);
							retdata.put("pass_gender", passinfo.sex);
							retdata.put("pass_age", ApiGlobal.getDiffYear(
									passinfo.birthday, new Date()));
							retdata.put("start_time", ApiGlobal.Date2String(
									order_info.pre_time, true));
							retdata.put("start_addr", order_info.start_addr);
							retdata.put("end_addr", order_info.end_addr);

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,设置order_longdistance_details和order_exec_cs表的状态为已取消
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult cancelLongOrder(String source, long driverid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			Statement stmt2 = null;// modify  by chuzhiqiang
			ResultSet rs = null;
			ResultSet rs2 = null;// modify  by chuzhiqiang
			String szQuery = "";
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//modify  by  chuzhiqiang
			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser drvinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderLongDistanceDetails order_info = ApiGlobal
						.getLongOrderInfo(dbConn, orderid);

				if (drvinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!drvinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status == 8) // Order is already cancelled
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_AlreadyCancelled;
					// } else if (order_info.status >= 3) // Order is already
					// started
					// {
					// result.retcode = ConstMgr.ErrCode_Normal;
					// result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();
					stmt2= dbConn.createStatement();// modify  by chuzhiqiang
					ArrayList<SVCOrderExecCS> arrExecInfos = new ArrayList<SVCOrderExecCS>();
					String exec_ids = "";
					String user_ids = "";
					ArrayList<Long> arrPass = new ArrayList<Long>();

					szQuery = "SELECT "
							+ "		order_exec_cs.* "

							+ "FROM order_longdistance_users_cs "

							+ "INNER JOIN order_exec_cs "
							+ "ON order_exec_cs.id=order_longdistance_users_cs.order_exec_cs_id "

							+ "INNER JOIN order_longdistance_details "
							+ "ON order_longdistance_details.id=order_longdistance_users_cs.orderdriverlongdistance_id "

							+ "WHERE " + "		order_exec_cs.deleted=0 AND "
							+ "		order_longdistance_users_cs.deleted=0 AND "
							+ "		order_longdistance_details.publisher="
							+ driverid + " AND "
							+ "		order_longdistance_details.id=" + orderid;

					rs = stmt.executeQuery(szQuery);
					System.out.println("SVCOrderService.cancelLongOrder()---------"+szQuery);
					while (rs.next()) {
						SVCOrderExecCS exec_info = SVCOrderExecCS
								.decodeFromResultSet(rs);
						arrExecInfos.add(exec_info);

						if (!exec_ids.equals(""))
							exec_ids += ",";

						if (!user_ids.equals(""))
							user_ids += ",";

						exec_ids += exec_info.id;

						user_ids += exec_info.passenger;
						arrPass.add(exec_info.passenger);
						
						//------begain  modify by chuzhiqiang -------------------------
						
							
						
						long insu_id_pass = exec_info.insu_id_pass;//获得乘客的保单id
						
						szQuery="UPDATE insurance SET insu_status = 1 WHERE ORDER_EXEC_ID = "+exec_info.id;//将保单的状态改为撤保
						System.out.println("单次  撤保 更新保单状态"+szQuery);
						stmt2.executeUpdate(szQuery);
						
						System.out.println("insu_id ----"+insu_id_pass);
						szQuery="SELECT appl_no from insurance WHERE id = "+insu_id_pass;//根据乘客保单id获得
						
						 rs2 = stmt2.executeQuery(szQuery);
						 
						 System.out.println("szq====="+szQuery);
						 String appl_no = null;
						 //String appl_no = rs2.getString("APPL_NO");
						 if (rs2.next()) {
							
							  appl_no = rs2.getString(1);
						}
						 rs2.close();
						

							szQuery = "INSERT INTO insurance_dtd ("
									+ " insu_id"
									+ " ,APPL_NO"
									+ " ,oper_type"// 1投保,2撤保
									+ " ,oper_time" + " ,insu_sum" + ") "
									+ "values" 
									+ "(" 
									+ insu_id_pass 
									+ " ,\'"+appl_no+"\'"
									+ " ,1" 
									+ " ,\'"
									+ sf.format(new Date()) + "\' "
									+ " ,200000.00" + ")";

							System.out.println("单次 撤保 乘客 " + szQuery);
						stmt2.executeUpdate(szQuery);

						long insu_id_driver = 0;

							szQuery = "SELECT id,user FROM insurance WHERE ORDER_EXEC_ID = "
									+ exec_info.id
									+ " AND ISD_ID ="
									+ exec_info.driver;
							System.out.println("获得车主保单id----" + szQuery);

							rs2 = stmt2.executeQuery(szQuery);
							if (rs2.next()) {
								insu_id_driver = rs2.getLong("id");
								System.out.println("isd_insu_id-=-=-"+insu_id_driver);
							}

							rs2.close();
							
							
							
							System.out.println("insu_id ----"+insu_id_driver);
							szQuery="SELECT appl_no from insurance WHERE id = "+insu_id_driver;
							
							 rs2 = stmt2.executeQuery(szQuery);
							 System.out.println("szq====="+szQuery);
							 
							 //String appl_no = rs2.getString("APPL_NO");
							 if (rs2.next()) {
								
								  appl_no = rs2.getString(1);
							}
							 rs2.close();
							 

							szQuery = "INSERT INTO insurance_dtd ("
									+ " insu_id"
									+ " ,APPL_NO"
									+ " ,oper_type"// 1投保,2撤保
									+ " ,oper_time" + " ,insu_sum" + ") "
									+ "values"
									+ "("
									+ insu_id_driver// 车主的保单id
									+ " ,\'"+appl_no+"\'" 
									+ " ,1" 
									+ " ,\'"
									+ sf.format(new Date()) + "\' "
									+ " ,200000.00" + ")";
							System.out.println("单次 撤保 车主 " + szQuery);
						stmt2.executeUpdate(szQuery);
						
						// ------end modify by chuzhiqiang---------------------------
					}
					rs.close();

					for (int i = 0; i < arrExecInfos.size(); i++) {
						SVCOrderExecCS execInfo = arrExecInfos.get(i);

						SVCFreezePoints frzPts = ApiGlobal
								.getLatestFreezeLogForOrder(dbConn,
										execInfo.passenger, order_info.id, 3,
										execInfo.id);

						if (frzPts == null)
							continue;
						ApiGlobal.releasePointsForUser(dbConn, source,
								execInfo.passenger, frzPts.id);
					}

					szQuery = "UPDATE " + "		order_longdistance_details "
							+ "SET " + "		status=8 " + "WHERE " + "		id="
							+ order_info.id;
					stmt.executeUpdate(szQuery);

					if (!exec_ids.equals("")) {
						szQuery = "UPDATE " + "		order_exec_cs " + "SET "
								+ "		status=8 " + "WHERE " + "		id IN ("
								+ exec_ids + ")";
						stmt.executeUpdate(szQuery);
						
						//*-*-
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;

					// Send push notification to passengers
					String szPreTime = ApiGlobal.Date2String(
							order_info.pre_time, false);
					String szMsg = "原定于"
							+ szPreTime
							+ "从"
							+ ApiGlobal.cityCode2Name(dbConn,
									order_info.start_city)
							+ "前往"
							+ ApiGlobal.cityCode2Name(dbConn,
									order_info.end_city)
							+ "的拼车订单已被车主取消，请查看我的订单";

					STPushNotificationData data = new STPushNotificationData();
					data.title = ConstMgr.STR_DINGDANXIAOXI;
					data.description = szMsg;

					STPushNotificationCustomData custom_data = new STPushNotificationCustomData();
					custom_data.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
					custom_data.orderid = order_info.id;
					custom_data.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
					custom_data.ordertype = ConstMgr.ORDER_LONG;

					data.custom_data = custom_data;

					for (int i = 0; i < arrPass.size(); i++)
						ApiGlobal.addOrderNotification(dbConn, arrPass.get(i),
								orderid, szMsg);
					ApiGlobal.sendPushNotifToUser(dbConn, arrPass, data);
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
				//modify   by chuzhiqiang
				if (stmt2 != null) {
					try {
						stmt2.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}
				//modify   by chuzhiqiang
				// Close db connection
				if (dbConn != null) {
					try {
						dbConn.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1,更新order_onoffduty_details表和order_exec_cs表为已停止状态
	 * 
	 * @param source
	 * @param driverid
	 * @param orderid
	 * @param devtoken
	 * @return
	 */
	public SVCResult stopOnOffOrder(String source, long driverid, long orderid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + driverid + "," + orderid + "," + devtoken);

		if (source.equals("") || driverid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser drvinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						driverid);

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Get divide info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Exec info
				SVCOrderExecCS exec_info = null;

				if (drvinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!drvinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();
					// /////////////////////////////////////////////////////////////////////////////////////////////////////

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id "
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null) {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(84)";
					} else if (exec_info.status != 7) {
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
					} else {
						szQuery = "UPDATE order_onoffduty_details SET status=3 WHERE id="
								+ order_info.id;
						if (stmt.executeUpdate(szQuery) == 1) {
							szQuery = "UPDATE order_exec_cs SET status=8 WHERE id="
									+ exec_info.id;
							if (stmt.executeUpdate(szQuery) == 1) {
								if (exec_info.pay_time == null) { // Not paid
									SVCFreezePoints freezePoints = ApiGlobal
											.getLatestFreezeLogForOrder(dbConn,
													exec_info.passenger,
													order_info.id, 2, 0);

									if (freezePoints != null
											&& ApiGlobal.releasePointsForUser(
													dbConn, source,
													exec_info.passenger,
													freezePoints.id) > 0) {
										// Release frozen points
										result.retcode = ConstMgr.ErrCode_None;
										result.retmsg = ConstMgr.ErrMsg_None;

										// Send push notification and order
										// notification to passenger
										String szMsg = "车主终止服务，上下班订单自动重新发布，详情请查看我的订单";

										STPushNotificationData data = new STPushNotificationData();
										data.title = ConstMgr.STR_DINGDANXIAOXI;
										data.description = szMsg;

										STPushNotificationCustomData customData = new STPushNotificationCustomData();
										customData.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
										customData.orderid = order_info.id;
										customData.userrole = STPushNotificationCustomData.USER_ROLE_PASSENGER;
										customData.ordertype = ConstMgr.ORDER_ONOFF;

										data.custom_data = customData;

										ApiGlobal.addOrderNotification(dbConn,
												order_info.publisher, orderid,
												szMsg);
										ApiGlobal.sendPushNotifToUser(dbConn,
												order_info.publisher, data);
									} else {
										result.retcode = ConstMgr.ErrCode_Exception;
										result.retmsg = ConstMgr.ErrMsg_Exception
												+ "(86)";
									}
								}
							} else {
								result.retcode = ConstMgr.ErrCode_Exception;
								result.retmsg = ConstMgr.ErrMsg_Exception
										+ "(83)";
							}
						} else {
							result.retcode = ConstMgr.ErrCode_Exception;
							result.retmsg = ConstMgr.ErrMsg_Exception + "(83)";
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
	 * 1，设置order_exec_cs表的电子密码，并返回车主信息，车辆信息，和距离等信息
	 * 
	 * @param source
	 * @param userid
	 * @param orderid
	 * @param password
	 * @param devtoken
	 * @return
	 */
	public SVCResult setOnceOrderPassword(String source, long userid,
			long orderid, String password, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + password + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null, stmt2 = null;
			ResultSet rs = null, rs2 = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				SVCUser drvinfo = null;

				// Get order info
				SVCOrderTempDetails order_info = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);

				// Excute information
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null || order_info.deleted == 1) // No
																			// order
																			// info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status == 1 || order_info.status == 8) // Order
																				// is
																				// not
																				// accepted
																				// or
																				// cancelled
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();
					stmt2 = dbConn.createStatement();

					szQuery = "SELECT * FROM order_exec_cs WHERE id="
							+ order_info.order_cs_id;
					rs = stmt.executeQuery(szQuery);
					if (!rs.next()) {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(90)";
						ApiGlobal.logMessage(result.encodeToJSON().toString());

						return result;
					}
					exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
					rs.close();

					String brand = "";
					String style = "";
					String color = "";
					String car_img = "";
					String carno = "";
					long car_type_id = 0;

					szQuery = "SELECT " + "		user.*," + "		user_car.brand,"
							+ "		user_car.style," + "		user_car.color,"
							+ "		user_car.plate_num," + "		user_car.car_img, "
							+ "		user_car.car_type_id "

							+ "FROM user "

							+ "LEFT JOIN  user_car " + "ON user_car.userid="
							+ exec_info.driver + " "

							+ "WHERE " + "		user.deleted=0";

					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						drvinfo = SVCUser.decodeFromResultSet(rs);

						brand = rs.getString("brand");
						style = rs.getString("style");
						color = rs.getString("color");
						car_img = rs.getString("car_img");
						carno = rs.getString("plate_num");
						car_type_id = rs.getLong("car_type_id");
					}
					rs.close();

					szQuery = "UPDATE order_exec_cs " + "SET password=\""
							+ password + "\" " + "WHERE id="
							+ order_info.order_cs_id;

					if (stmt.executeUpdate(szQuery) == 1) {
						double drvlat = 0, drvlng = 0;
						szQuery = "SELECT * FROM user_online WHERE userid="
								+ drvinfo.id
								+ " ORDER BY last_heartbeat_time DESC";
						rs2 = stmt2.executeQuery(szQuery);
						if (rs2.next()) {
							drvlat = rs2.getDouble("lat");
							drvlng = rs2.getDouble("lng");
						}
						rs2.close();

						JSONObject retdata = new JSONObject();

						retdata.put("drv_id", drvinfo.id);
						retdata.put("drv_name", drvinfo.nickname);
						retdata.put("drv_img",
								ApiGlobal.getAbsoluteURL(drvinfo.img));
						retdata.put("drv_age", ApiGlobal.getDiffYear(
								drvinfo.birthday, new Date()));
						retdata.put("drv_gender", drvinfo.sex);
						retdata.put("car_img",
								ApiGlobal.getAbsoluteURL(car_img));

						// Car style
						int carstyle = 1;
						szQuery = "SELECT type FROM car_type WHERE id="
								+ car_type_id;
						rs2 = stmt2.executeQuery(szQuery);
						if (rs2.next())
							carstyle = rs2.getInt("type");
						rs2.close();
						// ///////////////////////////////////////////////////////////////////////////////////////////

						retdata.put("car_style", carstyle);
						retdata.put("car_brand", brand);
						retdata.put("car_type", style);
						retdata.put("car_color", color);
						retdata.put("carno", carno);

						double distance = ApiGlobal.calcDist(
								order_info.start_lat, order_info.start_lng,
								drvlat, drvlng);
						String dist_desc = ConstMgr.STR_JUNIN;
						if (distance < 0.2) {
							dist_desc += "<200m";
						} else if (distance < 1) {
							dist_desc += "" + (int) (distance * 1000) + "km";
						} else {
							dist_desc += ApiGlobal.Double2String(distance, 2)
									+ "km";
						}
						retdata.put("dist", distance);
						retdata.put("dist_desc", dist_desc);
						retdata.put("start_time", ApiGlobal.Date2String(
								order_info.pre_time, true));
						retdata.put("start_addr", order_info.start_addr);
						retdata.put("end_addr", order_info.end_addr);
						retdata.put("password", password);

						result.retcode = ConstMgr.ErrCode_None;
						result.retmsg = ConstMgr.ErrMsg_None;
						result.retdata = retdata;
					} else {
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(87)";
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult setOnoffOrderPassword(String source, long userid,
			long orderid, String password, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + password + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null, stmt2 = null;
			ResultSet rs = null, rs2 = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				SVCUser drvinfo = null;

				// Get order info
				SVCOrderOnOffDutyDetails order_info = ApiGlobal
						.getOnOffOrderInfo(dbConn, orderid);

				// Get divide info
				SVCOrderOnOffDutyDivide divide_info = null;

				// Excute information
				SVCOrderExecCS exec_info = null;

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (order_info == null) // No order info
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (order_info.status != 2) // State not match
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_StateNotMatch;
				} else {

					stmt = dbConn.createStatement();
					stmt2 = dbConn.createStatement();

					// Select divide_info
					szQuery = "SELECT * FROM order_onoffduty_divide WHERE deleted=0 AND orderdetails_id="
							+ order_info.id;
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						divide_info = SVCOrderOnOffDutyDivide
								.decodeFromResultSet(rs);
					rs.close();
					// /////////////////////////////////////////////////////////////////////////////////////////////////////

					if (divide_info != null) {
						// Select exec_details_info and exec_info
						szQuery = "SELECT "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id,"
								+ "		order_onoffduty_exec_details.order_cs_id,"
								+ "		order_exec_cs.* "

								+ "FROM order_onoffduty_exec_details "

								+ "INNER JOIN order_exec_cs "
								+ "ON order_exec_cs.id=order_onoffduty_exec_details.order_cs_id "

								+ "WHERE "
								+ "		order_onoffduty_exec_details.deleted=0 AND "
								+ "		order_onoffduty_exec_details.onoffduty_divide_id="
								+ divide_info.id
								+ " "

								+ "ORDER BY order_exec_cs.cr_date DESC LIMIT 0,1";

						rs = stmt.executeQuery(szQuery);
						if (rs.next())
							exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
						rs.close();
						// /////////////////////////////////////////////////////////////////////////////////////////////
					}

					if (exec_info == null) // Abnormal situation
					{
						result.retcode = ConstMgr.ErrCode_Exception;
						result.retmsg = ConstMgr.ErrMsg_Exception + "(91)";
					} else {
						String ret_midpoints = ""; // Function to return to
													// mobile
						szQuery = "SELECT * FROM midpoints WHERE deleted=0 AND order_type=1 AND orderid="
								+ order_info.id;
						rs = stmt.executeQuery(szQuery);
						while (rs.next()) {
							if (!ret_midpoints.equals(""))
								ret_midpoints += ",";
							ret_midpoints += rs.getString("addr");
						}
						rs.close();

						String brand = "";
						String style = "";
						String color = "";
						String car_img = "";
						String carno = "";
						long car_type_id = 0;
						szQuery = "SELECT " + "		user.*," + "		user_car.brand,"
								+ "		user_car.style," + "		user_car.color,"
								+ "		user_car.plate_num,"
								+ "		user_car.car_img, "
								+ "		user_car.car_type_id "

								+ "FROM user "

								+ "LEFT JOIN  user_car "
								+ "ON user_car.userid=user.id "

								+ "WHERE " + "		user.deleted=0	AND "
								+ "		user.id=" + exec_info.driver;

						rs = stmt.executeQuery(szQuery);
						if (rs.next()) {
							drvinfo = SVCUser.decodeFromResultSet(rs);

							brand = rs.getString("brand");
							style = rs.getString("style");
							color = rs.getString("color");
							car_img = rs.getString("car_img");
							carno = rs.getString("plate_num");
							car_type_id = rs.getLong("car_type_id");
						}
						rs.close();

						szQuery = "UPDATE order_exec_cs " + "SET password=\""
								+ password + "\" " + "WHERE id=" + exec_info.id;

						if (stmt.executeUpdate(szQuery) == 1) {
							JSONObject retdata = new JSONObject();

							retdata.put("drv_id", drvinfo.id);
							retdata.put("drv_name", drvinfo.nickname);
							retdata.put("drv_img",
									ApiGlobal.getAbsoluteURL(drvinfo.img));
							retdata.put("drv_age", ApiGlobal.getDiffYear(
									drvinfo.birthday, new Date()));
							retdata.put("drv_gender", drvinfo.sex);
							retdata.put("car_img",
									ApiGlobal.getAbsoluteURL(car_img));

							// Car style
							int carstyle = 1;
							szQuery = "SELECT type " + "FROM car_type "
									+ "WHERE id=" + car_type_id;
							rs2 = stmt2.executeQuery(szQuery);
							if (rs2.next())
								carstyle = rs2.getInt("type");
							rs2.close();
							// ///////////////////////////////////////////////////////////////////////////////////////////

							retdata.put("car_style", carstyle);
							retdata.put("car_brand", brand);
							retdata.put("car_type", style);
							retdata.put("car_color", color);
							retdata.put("carno", carno);
							retdata.put("start_time", ApiGlobal.Date2String(
									order_info.pre_time, true));
							retdata.put("start_addr", order_info.start_addr);
							retdata.put("end_addr", order_info.end_addr);
							retdata.put("password", password);
							retdata.put("days", divide_info.which_days);
							retdata.put("leftdays", order_info.leftdays);
							retdata.put("midpoints", ret_midpoints);

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
						}
					}
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
 * 1,第二轮或者第三轮播报，获取周边范围X公里内的车主，并将其插入到order_temp_notify表中(已经插入的就不再插入)
 * 
 * 注意，没有推送消息的功能，此功能已经取消了。
	 * 
 * @param source
 * @param userid
 * @param orderid
 * @param pub_index
 * @param devtoken
 * @return
 */
	public SVCResult sendOnceOrderNotification(String source, long userid,
			long orderid, int pub_index, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + pub_index + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| (pub_index != 2 && pub_index != 3) || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);

				// Get Order info
				SVCOrderTempDetails orderinfo = ApiGlobal.getOnceOrderInfo(
						dbConn, orderid);

				// Get grab info
				SVCOrderTempGrab grabinfo = ApiGlobal
						.getLatestGrabInfoFromOrderID(dbConn, orderid);

				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) // Device
																	// token not
																	// match
				{
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (orderinfo == null || orderinfo.deleted == 1) {
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NoOrderInfo;
				} else if (orderinfo.status != 1
						|| (grabinfo != null && grabinfo.status != 2)) { // Order
																			// is
																			// already
																			// accepted
					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
				} else {
					SVCGlobalParams global_params = null;
					SVCCity city_params = null;
					String szCityCode = "";

					stmt = dbConn.createStatement();

					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// Global parameters.
					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					szQuery = "SELECT * FROM global_params WHERE deleted=0";
					rs = stmt.executeQuery(szQuery);
					rs.next(); // Global parameters must exist. If not exist, it
								// is abnormal
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();
					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// City parameters
					// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
					szCityCode = userinfo.city_cur;
					szQuery = "SELECT * FROM city WHERE code=\"" + szCityCode
							+ "\" AND deleted=0";
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						city_params = SVCCity.decodeFromResultSet(rs);
					rs.close();
					// /////////////////////////
					// ///////////////////////////////////////////////////////////////////////////////////

					double range2 = 0, range3 = 0;
					String hb_vldprd = ApiGlobal.getEnvValueFromCode(dbConn,
							ConstMgr.ENVCODE_HEARTBEAT_VALIDDPERIOD);
					String szTurnTime = "";

					if (city_params == null || city_params.total_time < 0) {
						range2 = global_params.range2;
						range3 = global_params.range3;
					} else {
						range2 = city_params.range2;
						range3 = city_params.range3;
					}

					ArrayList<Long> idList = new ArrayList<Long>();
					szQuery = "SELECT " + "		* " + "FROM "
							+ "		order_temp_notify " + "WHERE " + "		order_id="
							+ orderid + " AND " + "		deleted=0";
					rs = stmt.executeQuery(szQuery);
					while (rs.next()) {
						idList.add(rs.getLong("userid"));
					}
					rs.close();

					/**********************************************************************************
					 * Search nearby drivers and save in <order_temp_notify>
					 * table 获取临近司机的这个sql语句性能不好，以后想办法调优一下此sql语句
					 **********************************************************************************/
					double curRange = 0;
					if (pub_index == 2) {
						curRange = range2;
					} else if (pub_index == 3) {
						curRange = range3;
					}
					// 1,第二轮或者第三轮播报，获取周边范围X公里内的车主，并将其插入到order_temp_notify表中(已经插入的就不再插入)
					szQuery = "SELECT "
							+ "		user.*, "
							+ "		user_online.lat,"
							+ "		user_online.lng "
							+ "FROM user "

							+ "INNER JOIN user_online "
							+ "ON user_online.userid=user.id "

							+ "WHERE "
							+ "		user.deleted=0 AND "
							+ "		user_online.deleted=0 AND "
							+ "		timediff_min(user_online.last_heartbeat_time, NOW())<="
							+ hb_vldprd
							+ " AND "
							+ "		calc_distance(user_online.lat, user_online.lng, "
							+ orderinfo.start_lat + "," + orderinfo.start_lng
							+ ")<=" + curRange;

					rs = stmt.executeQuery(szQuery);

					szQuery = "INSERT INTO order_temp_notify (" + "userid,"
							+ "turn," + "order_id," + "push_time," + "deleted"
							+ ") VALUES ";

					int drvcount = 0;
					int newcount = 0;
					while (rs.next()) {
						drvcount++;

						long tmpUserId = rs.getLong("id");
						if (idList.contains(tmpUserId))
							continue;

						newcount++;

						if (szQuery.charAt(szQuery.length() - 1) == ')')
							szQuery += ",";

						szTurnTime = ApiGlobal.Date2String(new Date(), true);

						szQuery += "(";
						szQuery += tmpUserId + ",";
						szQuery += pub_index + ",";
						szQuery += orderid + ",";
						szQuery += "\"" + szTurnTime + "\",0)";
					}

					// Insert drivers into order_temp_notify table
					System.out.println("szQuery " + pub_index + " :" + szQuery);
					if (newcount > 0)
						stmt.executeUpdate(szQuery);

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;

					JSONObject retdata = new JSONObject();
					retdata.put("drvcount", drvcount);
					result.retdata = retdata;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	/**
 * 车主登录的时候，检查周边是否有正在发布的单次订单，如果有的话把自己的信息插入到order_temp_notify表中
	 * 
 * @param source
 * @param userid
 * @param devtoken
 * @return
 */
	public SVCResult insertDriverAcceptableOrders(String source, long userid,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + devtoken);

		if (source.equals("") || userid < 0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null, stmt2 = null;
			ResultSet rs = null, rs2 = null;
			String szQuery = "", szQuery2 = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else if (userinfo.driver_verified != 1) // Not verified driver
				{
					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
				} else {
					double userlat = 0, userlng = 0;
					SVCGlobalParams global_params = null;

					stmt = dbConn.createStatement();
					stmt2 = dbConn.createStatement();

					szQuery = "SELECT * FROM global_params WHERE deleted=0";
					rs = stmt.executeQuery(szQuery);
					// Global parameters must exist. If not exist, it is
					// abnormal.
					rs.next();
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();

					szQuery = "SELECT * FROM user_online WHERE deleted=0 AND userid="
							+ userid
							+ " ORDER BY last_heartbeat_time DESC LIMIT 0,1";
					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						SVCUserOnline user_online = SVCUserOnline
								.decodeFromResultSet(rs);
						userlat = user_online.lat;
						userlng = user_online.lng;

						rs.close();
					} else {
						userlat = 0;
						userlng = 0;
					}

					szQuery = "SELECT "
							+ "		order_temp_details.id,"
							+ "		order_temp_details.order_num,"
							+ "		order_temp_details.price,"
							+ "		order_temp_details.start_addr,"
							+ "		order_temp_details.end_addr,"
							+ "		order_temp_details.pre_time,"
							+ "		order_temp_details.ps_date,"
							+ "		order_temp_details.start_lat,"
							+ "		order_temp_details.start_lng,"
							+ "		TIMESTAMPADD(SECOND, order_temp_details.wait_time, order_temp_details.ps_date) AS exp_time,"

							+ "		user.id AS pass_id,"
							+ "		user.nickname AS pass_name,"
							+ "		user.sex AS pass_gender,"
							+ "		user.img AS pass_img,"
							+ "		TIMESTAMPDIFF(YEAR, user.birthday, CURDATE()) AS pass_age, "

							+ "		city_info.range1,"
							+ "		city_info.range2,"
							+ "		city_info.range3,"
							+ "		city_info.turn1_time,"
							+ "		city_info.turn2_time,"

							+ "		(SELECT COUNT(*) "
							+ "		FROM midpoints "
							+ "		WHERE"
							+ "			deleted=0 AND "
							+ "			publisher <> "
							+ userid
							+ " AND "
							+ "			orderid=order_temp_details.id) AS mid_count, "

							+ "		(SELECT COUNT(*) "
							+ "		FROM order_temp_grab "
							+ "		WHERE status=0 AND deleted=0 AND order_id = order_temp_details.id) AS accept_count "

							+ "FROM order_temp_details "

							+ "INNER JOIN user "
							+ "ON user.id = order_temp_details.publisher "

							+ "INNER JOIN (SELECT * FROM city WHERE deleted=0) AS city_info "
							+ "ON order_temp_details.order_city=city_info.code "

							+ "WHERE "
							+ "		user.deleted=0 AND "
							+ "		order_temp_details.deleted=0 AND " // Not
																	// deleted
							+ "		order_temp_details.status=1 AND "
							+ "		order_temp_details.publisher<>" + userid;

					szQuery += " HAVING "
							+ "		exp_time >= CURRENT_TIMESTAMP() AND "
							+ "		accept_count = 0";

					rs = stmt.executeQuery(szQuery);

					szQuery = "INSERT INTO order_temp_notify (" + "userid,"
							+ "turn," + "order_id," + "push_time," + "deleted"
							+ ") VALUES ";

					while (rs.next()) {
						double range1 = 0, range2 = 0, range3 = 0;
						int turn1_time = 0, turn2_time = 0;
						double fTmpDist = 0;
						long orderid = 0;
						int nTmpTurn = 0;
						Calendar calTurnTime = null;
						String szTurnTime = "";

						orderid = rs.getLong("id");

						// Check for existance and
						// does not insert if already exist
						szQuery2 = "SELECT COUNT(*) AS cnt "
								+ "FROM order_temp_notify " + "WHERE "
								+ "		userid=" + userid + " AND "
								+ "		order_id=" + orderid;

						rs2 = stmt2.executeQuery(szQuery2);
						if (rs2.next() && rs2.getInt("cnt") > 0) {
							rs2.close();
							continue;
						}
						rs2.close();
						// ////////////////////////////////////////////////////////////////
						// ////////////////////////////////////////////////////////////////

						calTurnTime = Calendar.getInstance();

						range1 = rs.getDouble("range1");
						range2 = rs.getDouble("range2");
						range3 = rs.getDouble("range3");

						turn1_time = rs.getInt("turn1_time");
						turn2_time = rs.getInt("turn2_time");

						if (range1 <= 0) {
							range1 = global_params.range1;
							range2 = global_params.range2;
							range3 = global_params.range3;
						}

						if (turn1_time <= 0) {
							turn1_time = global_params.turn1_time;
							turn2_time = global_params.turn2_time;
						}

						fTmpDist = ApiGlobal.calcDist(rs.getLong("start_lat"),
								rs.getLong("start_lng"), userlat, userlng);

						if (fTmpDist <= range1) {
							nTmpTurn = 1;
						} else if (fTmpDist <= range2) {
							nTmpTurn = 2;
							calTurnTime.add(Calendar.SECOND, turn1_time);
						} else if (fTmpDist <= range3) {
							nTmpTurn = 3;
							calTurnTime.add(Calendar.SECOND, turn2_time);
						}

						szTurnTime = ApiGlobal.Date2String(
								calTurnTime.getTime(), true);

						if (szQuery.charAt(szQuery.length() - 1) == ')')
							szQuery += ",";

						szQuery += "(";
						szQuery += userid + ",";
						szQuery += nTmpTurn + ",";
						szQuery += orderid + ",";
						szQuery += "\"" + szTurnTime + "\",0)";
					}

					if (szQuery.charAt(szQuery.length() - 1) == ')') {
						stmt.executeUpdate(szQuery);
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult onceOrderDriverPos(String source, long userid,
			long orderid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + orderid + "," + devtoken);

		if (source.equals("") || userid < 0 || orderid < 0
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {
					stmt = dbConn.createStatement();

					String hb_validperiod = ApiGlobal.getEnvValueFromCode(
							dbConn, ConstMgr.ENVCODE_HEARTBEAT_VALIDDPERIOD);
					// 1,查询该单次订单所有收到播报的车主的位置
					szQuery = "SELECT "
							+ "		user.*,"
							+ "		online_info.lat, "
							+ "		online_info.lng "
							+ "FROM order_temp_notify "

							+ "INNER JOIN user "
							+ "ON user.id=order_temp_notify.userid "

							+ "INNER JOIN "
							+ "		(SELECT "
							+ "			user_online.userid,"
							+ "			user_online.lat,"
							+ "			user_online.lng "
							+ "		FROM user_online "
							+ "		WHERE "
							+ "			deleted = 0 AND "
							+ "			timediff_min(last_heartbeat_time, NOW())<="
							+ hb_validperiod
							+ "		ORDER BY last_heartbeat_time DESC) AS online_info "
							+ "ON user.id = online_info.userid "

							+ "WHERE " + "		user.deleted=0 AND "
							+ "		user.driver_verified=1 AND "
							+ "		order_temp_notify.deleted=0 AND "
							+ "		order_temp_notify.order_id=" + orderid;

					rs = stmt.executeQuery(szQuery);

					JSONArray arrOrders = new JSONArray();
					while (rs.next()) {
						JSONObject jsonItem = new JSONObject();

						jsonItem.put("driverid", rs.getLong("id"));
						jsonItem.put("lat", rs.getDouble("lat"));
						jsonItem.put("lng", rs.getDouble("lng"));

						arrOrders.add(jsonItem);
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrOrders;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	
	//------modify  by chuzhiqiang-------------------------
	public SVCResult clickchargingbtn(long userid, long order_id,  String devtoken){
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(userid + "," + order_id + "," + devtoken);

		if ( order_id < 0||userid<0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				dbConn.setAutoCommit(false);
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {
					stmt = dbConn.createStatement();
					
/*					// 1,查询该单次订单对应的执行订单id
					szQuery = "SELECT order_cs_id FROM  order_temp_details  WHERE  id = "+order_id;
					long order_cs_id = 0;
					rs=stmt.executeQuery(szQuery);
					System.out.println("SVCOrderService.clickchargingbtn(2)=="+szQuery);

					if (rs.next()) {
						order_cs_id = rs.getLong("order_cs_id");
					}
					rs.close();*/
					
					//2.根据执行订单id更新has_clickedchargingbtn（用户是否点击去充值）字段
					szQuery = "UPDATE  order_temp_details SET has_clickedchargingbtn = 1 WHERE id=" + order_id;
					int i = stmt.executeUpdate(szQuery);
					
					System.out.println("i========"+i);
						if (i>0) {
							System.out.println("SVCOrderService.clickchargingbtn(1)=="+szQuery);

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
					}
						dbConn.commit();
				}
			} catch (Exception ex) {
				try {
					dbConn.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
		}
			
	
		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult has_clickedchargingbtn(long userid, long order_id,  String devtoken){
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(userid + "," + order_id + "," + devtoken);

		if ( order_id < 0||userid<0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				//dbConn.setAutoCommit(false);
				// Authenticate user
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,
						userid);
				if (userinfo == null) // No user information
				{
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {
					stmt = dbConn.createStatement();
					
/*					// 1,查询该单次订单对应的执行订单id
					szQuery = "SELECT order_cs_id FROM  order_temp_details  WHERE  id = "+order_id;
					long order_cs_id = 0;
					rs=stmt.executeQuery(szQuery);
					System.out.println("SVCOrderService.has_clickedchargingbtn(1)=="+szQuery);
					if (rs.next()) {
						order_cs_id = rs.getLong("order_cs_id");
					}
					rs.close();*/
					
					//2.根据执行订单id更新has_clickedchargingbtn（用户是否点击去充值）字段
					szQuery = "SELECT has_clickedchargingbtn FROM  order_temp_details  WHERE id=" + order_id;
					rs = stmt.executeQuery(szQuery);
					System.out.println("SVCOrderService.has_clickedchargingbtn(2)=="+szQuery);

					rs.next();
					int has_clickedchargingbtn = rs.getInt("has_clickedchargingbtn");
					rs.close();
					
					//3.从全局参数表中查询waittime_when_charging
					//int waittime_when_charging = 0;
					szQuery = "SELECT waittime_when_charging FROM  global_params ";
					rs = stmt.executeQuery(szQuery);
					System.out.println("SVCOrderService.has_clickedchargingbtn(3)=="+szQuery);

					rs.next();
					int waittime_when_charging = rs.getInt("waittime_when_charging");
					rs.close();
					
					
					
					JSONObject retdata = new JSONObject();

					
						if (has_clickedchargingbtn>0) {
							
							retdata.put("waittime_when_charging", waittime_when_charging); // chuzhiqiang

							result.retcode = ConstMgr.ErrCode_None;
							result.retmsg = ConstMgr.ErrMsg_None;
							result.retdata = retdata;
							
							
						}else {
							retdata.put("waittime_when_charging", 0); // chuzhiqiang
							result.retcode = ConstMgr.ErrCode_PassDisagreed;
							result.retmsg = ConstMgr.ErrMsg_UserNotCharge;
							result.retdata = retdata;
						}
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
		}
		
		System.out.println("result===="+result.toString());
		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}
	
}
