package com.daemon;


import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.common.ApiGlobal;
import com.webapi.structure.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DaemonMain implements ServletContextListener
{
	public static int MAIN_LOOP_INTERVAL = 1000 * 3600;			 // Once per hour

	// Main constants
	private static String gLimitDays = "";
	private static String gDailyEvalLimit = "";

	// Main variables
	private static String gDailyCancelCnt = "";
	private static String gDrvLateLimit = "";
	private static String gConsiderCancelLimit = "";
	private static String gConsiderDriverLate = "";
	private static String gMinuteInterval = "";
	private static String gSmsPrice = "";
	private static String gSmsMaxLen = "";

	// Variables for sms plan
	private static SVCSmsPlan plan = null;
	private static ArrayList<Long> arrUserIDs = new ArrayList<Long>();
	private static ArrayList<String> arrUserPhones = new ArrayList<String>();


	// Initialize all the main variables
	public static void initMainVariables()
	{
		Connection dbConn = null;
		try {
			dbConn = DBManager.getDBConnection();

			gLimitDays = ApiGlobal.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_AUTOPROCLIMITDAYS);
			gDailyEvalLimit = ApiGlobal.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_DAILYEVALCOUNT);

			gDailyCancelCnt = ApiGlobal.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_DAILYCANCELCNT);
			gDrvLateLimit = ApiGlobal.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_DRIVERLATE);
			gConsiderCancelLimit = ApiGlobal.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_CONSIDER_CANCELCNT);
			gConsiderDriverLate = ApiGlobal.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_CONSIDER_DRIVERLATE);
			gMinuteInterval = ApiGlobal.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_MINUTEINTERVAL);
			gSmsPrice = ApiGlobal.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_SMSPRICE);
			gSmsMaxLen = ApiGlobal.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_SMSMAXLEN);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (dbConn != null)
			{
				try { dbConn.close(); } catch (Exception ex) { ex.printStackTrace(); }
			}
		}
	}


	public static void process()
	{
		System.out.print("\n\n\nDaemon started!\n\n");
		Date startTime = null;
		Calendar curCal = Calendar.getInstance();

		if (curCal.get(Calendar.MINUTE) != 0 ||
			curCal.get(Calendar.SECOND) != 0)
		{
			curCal.set(Calendar.MINUTE, 0);
			curCal.set(Calendar.SECOND, 0);
			curCal.add(Calendar.HOUR_OF_DAY, 1);

			startTime = curCal.getTime();
		}

		TimerTask mainTask = new TimerTask() {
			@Override
			public void run() {
				startMainLoop();
			}
		};

		Timer main_timer = new Timer();

		if (startTime == null)
			main_timer.schedule(mainTask, 0);			   // Start immediately
		else
			main_timer.schedule(mainTask, startTime);	   // Start at hour
	}


	// Main loop method
	public static void startMainLoop()
	{
		Calendar calCur = null, calDayStart = null;

		calDayStart = Calendar.getInstance();
		calDayStart.set(Calendar.HOUR_OF_DAY, 0);
		calDayStart.set(Calendar.MINUTE, 0);
		calDayStart.set(Calendar.SECOND, 0);

		while (true)
		{
			try {
				initMainVariables();

				calCur = Calendar.getInstance();
				if (ApiGlobal.getAbsDiffMinute(calCur.getTime(), calDayStart.getTime()) <= 30)		 // This is about 00:00. Must do once a day process.
				{
					dailyProc();

					if (calCur.get(Calendar.DAY_OF_MONTH) == 1)									 // The 1st of this month
						monthlyProc();
				}

				// Hourly process
				int nStartMinute = 60 - Integer.parseInt(gMinuteInterval) % 60;

				Calendar curCal = Calendar.getInstance();
				curCal.set(Calendar.MINUTE, 0);
				curCal.set(Calendar.SECOND, 0);

				if (ApiGlobal.getAbsDiffMinute(curCal.getTime(), new Date()) > 30)
					curCal.add(Calendar.HOUR_OF_DAY, 1);

				curCal.set(Calendar.MINUTE, nStartMinute);

				TimerTask hourlyTask = new TimerTask() {
					@Override
					public void run() {
						hourlyProc();
					}
				};

				Timer hourlyTimer = new Timer();
				hourlyTimer.schedule(hourlyTask, curCal.getTime());
				///////////////////////////////////////////////////////////////////////////////////////////////

			} catch (Exception ex) {
				ex.printStackTrace();
			}


			// Sleep for some time
			try { Thread.sleep(MAIN_LOOP_INTERVAL); } catch (Exception ex) { ex.printStackTrace(); }

		}
	}

	/**
	 * 每天处理一次
	 */
	// Once a day process
	public static void dailyProc()
	{
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
		String szQuery = "";
		String curTime = ApiGlobal.Date2String(new Date(), true);

		Connection dbConn = null;

		Calendar calSend = Calendar.getInstance();

		calSend.set(Calendar.HOUR_OF_DAY, 8);
		calSend.set(Calendar.MINUTE, 0);
		calSend.set(Calendar.SECOND, 0);

		try {
			dbConn = DBManager.getDBConnection();

			try
			{
				stmt = dbConn.createStatement();
				stmt2 = dbConn.createStatement();

				/*********************************************************************
				 Check all the long distance orders and cancel outdated orders，
				检查所有没有被执行并且执行时间已经过了的的长途订单，对于涉及到的乘客，释放其冻结的绿点
				 (1)查询出所有没有被执行并且执行时间已经过了的长途订单
				 (2)针对(1)步骤查询出来的每一个订单，
				 *********************************************************************/
				szQuery = "SELECT * "
						+ "FROM order_longdistance_details "
						+ "WHERE "
						+ "   deleted = 0 AND "
						+ "   status < 3 AND "
						+ "   pre_time <= '" + curTime + "'";

				rs = stmt.executeQuery(szQuery);
				while (rs.next())
				{
					SVCOrderLongDistanceDetails longOrderItem = SVCOrderLongDistanceDetails.decodeFromResultSet(rs);
					long freeze_tsid = 0;

					// Release balance points，对于那些没有被执行的长途订单，释放乘客被冻结的绿点
					szQuery = "SELECT "
							+ "	order_longdistance_users_cs.id AS usercs_id,"
							+ "	order_exec_cs.*, "
							+ "	ts.id AS ts_id, "
							+ "	freeze_points.id AS freeze_id "

							+ " FROM "
							+ "	order_longdistance_users_cs "

							+ "INNER JOIN order_exec_cs "
							+ "ON order_exec_cs.id=order_longdistance_users_cs.order_exec_cs_id "

							+ "INNER JOIN ts "
							+ "ON ts.order_cs_id=order_longdistance_users_cs.order_exec_cs_id "

							+ "INNER JOIN freeze_points "
							+ "ON freeze_points.freeze_ts_id=ts.id "

							+ "WHERE"
							+ "	order_longdistance_users_cs.deleted=0 AND "
							+ "	order_longdistance_users_cs.orderdriverlongdistance_id=" + longOrderItem.id + " AND "
							+ "	order_exec_cs.deleted=0 AND "
							+ "	order_exec_cs.status=2";			 // Now accepted state

					rs2 = stmt2.executeQuery(szQuery);
					while (rs2.next())
					{
						SVCOrderExecCS exec_info = SVCOrderExecCS.decodeFromResultSet(rs2);
						freeze_tsid = rs2.getLong("freeze_id");
						ApiGlobal.releasePointsForUser(dbConn, "", exec_info.passenger, freeze_tsid);
					}
					rs.close();

					// Add notification for order
					String szMsg = ConstMgr.STR_NIFABUDE
									+ ApiGlobal.Date2ChnStr(longOrderItem.pre_time)
									+ ConstMgr.STR_CONG
									+ longOrderItem.start_addr
									+ ConstMgr.STR_QIANWANG
									+ longOrderItem.end_addr
									+ ConstMgr.STR_DE
									+ ConstMgr.STR_CTPCDDYGZXSJ;
					ApiGlobal.addOrderNotification(dbConn, longOrderItem.publisher, 0, szMsg);

					// Send notification to user
					STPushNotificationData data = new STPushNotificationData();
					data.title = ConstMgr.STR_DINGDANXIAOXI;
					data.description = szMsg;
					ApiGlobal.sendPushNotifToUser(dbConn, longOrderItem.publisher, data);
				}
				rs.close();

				szQuery = "UPDATE order_longdistance_details "
						+ "SET status=8 "
						+ "WHERE "
						+ "   deleted = 0 AND "
						+ "   status < 3 AND "
						+ "   pre_time <= '" + curTime + "'";
				stmt.executeUpdate(szQuery);
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			try
			{
				/*********************************************************************
				 Check all the order execute information and pay outdated orders
				*********************************************************************/
				int nLimitHours = Integer.parseInt(gLimitDays) * 24;
				szQuery = "SELECT * FROM order_exec_cs WHERE deleted=0 AND status=6";
				rs = stmt.executeQuery(szQuery);
				while (rs.next())
				{
					SVCOrderExecCS exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
					long freeze_tsid = 0;

					Calendar calExp = Calendar.getInstance();
					calExp.setTime(exec_info.stopservice_time);
					calExp.add(Calendar.HOUR_OF_DAY, nLimitHours);

					if (calExp.getTime().after(new Date()))					 // Time is not expired yet
						continue;

					// Release the frozen balance
					szQuery = "SELECT "
							+ "	ts.id AS ts_id, "
							+ "	freeze_points.id AS freeze_id "

							+ " FROM ts "

							+ "INNER JOIN freeze_points "
							+ "ON freeze_points.freeze_ts_id=ts.id "

							+ "WHERE"
							+ "	ts.order_cs_id=" + exec_info.id;			 // Now accepted state

					rs2 = stmt2.executeQuery(szQuery);
					if (rs2.next())
					{
						freeze_tsid = rs2.getLong("freeze_id");

						// Update freeze_points log as used state
						szQuery = "UPDATE freeze_points SET state=2 WHERE deleted=0 AND state=0 AND id=" + freeze_tsid;
						stmt.executeUpdate(szQuery);

						// Pay to driver
						ApiGlobal.payToDriver(dbConn, "", exec_info.price, 0, exec_info.passenger, exec_info.driver, exec_info.id);

						// Order Notification
						String szMsg = ConstMgr.STR_NIN
								+ ApiGlobal.Date2ChnStr(exec_info.pre_time)
								+ ConstMgr.STR_CONG
								+ exec_info.from_
								+ ConstMgr.STR_QIANWANG
								+ exec_info.to_
								+ ConstMgr.STR_DE
								+ ConstMgr.STR_DDYCGST;

						STPushNotificationData data = new STPushNotificationData();
						data.title = ConstMgr.STR_DINGDANXIAOXI;
						data.description = szMsg;

						// Order notification and push notification for passenger
						ApiGlobal.addOrderNotification(dbConn, exec_info.passenger, exec_info.id, szMsg);
						ApiGlobal.sendPushNotifToUser(dbConn, exec_info.passenger, data);

						// Order notification and push notification for driver
						ApiGlobal.addOrderNotification(dbConn, exec_info.driver, exec_info.id, szMsg);
						ApiGlobal.sendPushNotifToUser(dbConn, exec_info.driver, data);
					}
					rs2.close();

					// Update order_exec_cs table. Set status as paid status
					szQuery = "UPDATE order_exec_cs SET status = 7 WHERE id=" + exec_info.id;
					stmt.executeUpdate(szQuery);
				}
				rs.close();
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}


			try
			{
				/*********************************************************************
				 Check all the order execute information and evaluate outdated orders
				*********************************************************************/
				int nLimitHours = Integer.parseInt(gLimitDays) * 24;
				szQuery = "SELECT * FROM order_exec_cs WHERE deleted=0 AND status=7 AND (has_evaluation_driver=0 OR has_evaluation_passenger=0)";
				rs = stmt.executeQuery(szQuery);
				while (rs.next())
				{
					SVCOrderExecCS exec_info = SVCOrderExecCS.decodeFromResultSet(rs);

					Calendar calExp = Calendar.getInstance();
					calExp.setTime(exec_info.pay_time);
					calExp.add(Calendar.HOUR_OF_DAY, nLimitHours);

					if (calExp.getTime().after(new Date()))					 // Time is not expired yet
						continue;

					szQuery = "INSERT INTO evaluation_cs ("
							+ "from_userid,"
							+ "to_userid,"
							+ "level,"
							+ "msg,"
							+ "ps_date,"
							+ "usertype,"
							+ "order_cs_id"
							+ ") VALUES ";

					String szCurTime = ApiGlobal.Date2String(new Date(), true);
					if (exec_info.has_evaluation_passenger == 0)			// Passenger not evaluated driver
					{
						szQuery += "("
								+ exec_info.passenger + ","
								+ exec_info.driver + ","
								+ 1 + ",'"
								+ ConstMgr.STR_MORENHAOPING + "','"
								+ szCurTime + "',1,"
								+ exec_info.id + ")";
					}

					if (exec_info.has_evaluation_driver == 0)			// Driver not evaluated driver
					{
						if (szQuery.charAt(szQuery.length() - 1) == ')')
							szQuery += ",";

						szQuery += "("
								+ exec_info.driver + ","
								+ exec_info.passenger + ","
								+ 1 + ",'"
								+ ConstMgr.STR_MORENHAOPING + "','"
								+ szCurTime + "',2,"
								+ exec_info.id + ")";
					}

					stmt2.executeUpdate(szQuery);

					// Update order_exec_cs table
					szQuery = "UPDATE order_exec_cs SET has_evaluation_passenger=1, has_evaluation_driver=1 WHERE id=" + exec_info.id;
					stmt2.executeUpdate(szQuery);
				}
				rs.close();
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}


			try
			{
				/*********************************************************************
				 Calculate cancel count and record abnormal behaviour
				*********************************************************************/
				if (gConsiderCancelLimit.equals("1"))
				{
					String szInsQuery = "INSERT INTO abb_record ("
							+ "userid,"
							+ "abb_type,"
							+ "desc,"
							+ "order_exec_id,"
							+ "status,"
							+ "remark,"
							+ "auditor,"
							+ "reviewer,"
							+ "limit_days,"
							+ "abb_time,"
							+ "balance_ts,"
							+ "cancel_number,"
							+ "deleted"
							+ ") VALUES ";

					// Calculate once order cancel count
					Calendar calToday = Calendar.getInstance();
					Date dtLowerBnd = null, dtUpperBnd = null;
					if (calToday.get(Calendar.HOUR_OF_DAY) > 0)		 // Over 00:00
						calToday.add(Calendar.DAY_OF_MONTH, -1);

					calToday.set(Calendar.HOUR_OF_DAY, 0);
					calToday.set(Calendar.MINUTE, 0);
					calToday.set(Calendar.SECOND, 0);
					dtLowerBnd = calToday.getTime();

					calToday.add(Calendar.DAY_OF_MONTH, 1);
					dtUpperBnd = calToday.getTime();

					szQuery = "SELECT "

							+ "	id,"
							+ "	order_cs_id,"
							+ "	publisher,"
							+ "	COUNT(*) AS cnt,"

							+ "FROM order_temp_details "

							+ "WHERE "
							+ "	ps_date>='" + ApiGlobal.Date2String(dtLowerBnd, true) + "' AND "
							+ "	ps_date<'" + ApiGlobal.Date2String(dtUpperBnd, true) + "' AND "
							+ "	status=8 "

							+ "GROUP BY publisher "

							+ "HAVING cnt>=" + gDailyCancelCnt;

					rs = stmt.executeQuery(szQuery);
					while (rs.next())
					{
						long execid = rs.getLong("order_cs_id");
						long userid = rs.getLong("publisher");
						int nCount = rs.getInt("cnt");

						if (szInsQuery.charAt(szQuery.length() - 1) == ')')
							szInsQuery += ",";
						szInsQuery += "(";
						szInsQuery += userid + ",";
						szInsQuery += 1 + ",";
						szInsQuery += "'',";
						szInsQuery += execid + ",";
						szInsQuery += 1 + ",";
						szInsQuery += "'',";
						szInsQuery += "0, 0, 0,'";
						szInsQuery += ApiGlobal.Date2String(new Date(), true) + "',0,";
						szInsQuery += nCount + ",0)";
					}
					rs.close();


					// Calculate on off duty order cancel count
					szQuery = "SELECT "

							+ "	id,"
							+ "	publisher,"
							+ "	COUNT(*) AS cnt,"

							+ "FROM order_onoffduty_details "

							+ "WHERE "
							+ "	publish_date>='" + ApiGlobal.Date2String(dtLowerBnd, true) + "' AND "
							+ "	publish_date<'" + ApiGlobal.Date2String(dtUpperBnd, true) + "' AND "
							+ "	status=3 "

							+ "GROUP BY publisher "

							+ "HAVING cnt>=" + gDailyCancelCnt;
					rs = stmt.executeQuery(szQuery);
					while (rs.next())
					{
						long userid = rs.getLong("publisher");
						int nCount = rs.getInt("cnt");

						if (szInsQuery.charAt(szQuery.length() - 1) == ')')
							szInsQuery += ",";
						szInsQuery += "(";
						szInsQuery += userid + ",";
						szInsQuery += 1 + ",";
						szInsQuery += "'',";
						szInsQuery += 0 + ",";
						szInsQuery += 1 + ",";
						szInsQuery += "'',";
						szInsQuery += "0, 0, 0,'";
						szInsQuery += ApiGlobal.Date2String(new Date(), true) + "',0,";
						szInsQuery += nCount + ",0)";
					}
					rs.close();


					// Calculate long distance order cancel count
					szQuery = "SELECT "

							+ "	id,"
							+ "	publisher,"
							+ "	COUNT(*) AS cnt,"

							+ "FROM order_longdistance_details "

							+ "WHERE "
							+ "	ps_time>='" + ApiGlobal.Date2String(dtLowerBnd, true) + "' AND "
							+ "	ps_time<'" + ApiGlobal.Date2String(dtUpperBnd, true) + "' AND "
							+ "	status=8 "

							+ "GROUP BY publisher "

							+ "HAVING cnt>=" + gDailyCancelCnt;
					rs = stmt.executeQuery(szQuery);
					while (rs.next())
					{
						long userid = rs.getLong("publisher");
						int nCount = rs.getInt("cnt");

						if (szInsQuery.charAt(szQuery.length() - 1) == ')')
							szInsQuery += ",";
						szInsQuery += "(";
						szInsQuery += userid + ",";
						szInsQuery += 1 + ",";
						szInsQuery += "'',";
						szInsQuery += 0 + ",";
						szInsQuery += 1 + ",";
						szInsQuery += "'',";
						szInsQuery += "0, 0, 0,'";
						szInsQuery += ApiGlobal.Date2String(new Date(), true) + "',0,";
						szInsQuery += nCount + ",0)";
					}
					rs.close();


					// Insert into abb_record table
					if (szInsQuery.charAt(szInsQuery.length() - 1) == ')')
						stmt.executeUpdate(szInsQuery);
				}


				if (gConsiderDriverLate.equals("1"))
				{
					// Calculate driver late count
					Calendar calToday = Calendar.getInstance();
					Date dtLowerBnd = null, dtUpperBnd = null;
					if (calToday.get(Calendar.HOUR_OF_DAY) > 0)		 // Over 00:00
						calToday.add(Calendar.DAY_OF_MONTH, -1);

					calToday.set(Calendar.HOUR_OF_DAY, 0);
					calToday.set(Calendar.MINUTE, 0);
					calToday.set(Calendar.SECOND, 0);
					dtLowerBnd = calToday.getTime();

					calToday.add(Calendar.DAY_OF_MONTH, 1);
					dtUpperBnd = calToday.getTime();

					szQuery = "SELECT "

							+ "	id,"
							+ "	driver,"
							+ "	timediff_min(pre_time, driverarrival_time) AS min_diff "

							+ "FROM order_exec_cs "

							+ "WHERE "
							+ "	cr_date>='" + ApiGlobal.Date2String(dtLowerBnd, true) + "' AND "
							+ "	cr_date<'" + ApiGlobal.Date2String(dtUpperBnd, true) + "' "

							+ "GROUP BY driver "

							+ "HAVING min_diff>=" + gDrvLateLimit;

					rs = stmt.executeQuery(szQuery);

					szQuery = "INSERT INTO abb_record ("
							+ "userid,"
							+ "abb_type,"
							+ "desc,"
							+ "order_exec_id,"
							+ "status,"
							+ "remark,"
							+ "auditor,"
							+ "reviewer,"
							+ "limit_days,"
							+ "abb_time,"
							+ "balance_ts,"
							+ "cancel_number,"
							+ "deleted"
							+ ") VALUES ";

					while (rs.next())
					{
						long execid = rs.getLong("id");
						long userid = rs.getLong("driver");
						int nCount = rs.getInt("cnt");

						if (szQuery.charAt(szQuery.length() - 1) == ')')
							szQuery += ",";
						szQuery += "(";
						szQuery += userid + ",";
						szQuery += 2 + ",";
						szQuery += "'',";
						szQuery += execid + ",";
						szQuery += 1 + ",";
						szQuery += "'',";
						szQuery += "0, 0, 0,'";
						szQuery += ApiGlobal.Date2String(new Date(), true) + "',0,";
						szQuery += nCount + ",0)";
					}
					rs.close();

					// Insert into abb_record table
					if (szQuery.charAt(szQuery.length() - 1) == ')')
						stmt.executeUpdate(szQuery);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}


			try
			{
				// Check evaluation records. then shields more than three times a day
				int nDailyEvalLimit = Integer.parseInt(gDailyEvalLimit);

				Calendar calToday = Calendar.getInstance();
				Date dtLowerBnd = null, dtUpperBnd = null;
				if (calToday.get(Calendar.HOUR_OF_DAY) > 0)		 // Over 00:00
					calToday.add(Calendar.DAY_OF_MONTH, -1);

				calToday.set(Calendar.HOUR_OF_DAY, 0);
				calToday.set(Calendar.MINUTE, 0);
				calToday.set(Calendar.SECOND, 0);
				dtLowerBnd = calToday.getTime();

				calToday.add(Calendar.DAY_OF_MONTH, 1);
				dtUpperBnd = calToday.getTime();

				szQuery = "SELECT "
						+ "	from_userid,"
						+ "	to_userid,"
						+ "	COUNT(*) AS cnt "
						+ "FROM evaluation_cs "
						+ "WHERE "
						+ "	ps_date>='" + ApiGlobal.Date2String(dtLowerBnd, true) + "' AND "
						+ "	ps_date<'" + ApiGlobal.Date2String(dtUpperBnd, true) + "' "
						+ "GROUP BY from_userid,to_userid";

				rs = stmt.executeQuery(szQuery);
				while (rs.next())
				{
					long from_id = rs.getLong("from_userid");
					long to_id = rs.getLong("to_userid");
					int nCount = rs.getInt("cnt");

					if (nCount <= nDailyEvalLimit)
						continue;

					String szIDs = "";

					szQuery = "SELECT id FROM evaluation_cs WHERE "
							+ "ps_date>='" + ApiGlobal.Date2String(dtLowerBnd, true) + "' AND "
							+ "ps_date<'" + ApiGlobal.Date2String(dtUpperBnd, true) + "' AND "
							+ "from_userid=" + from_id + " AND to_userid=" + to_id + " "
							+ "ORDER BY ps_date ASC LIMIT 0,3";

					rs2 = stmt2.executeQuery(szQuery);
					while (rs2.next())
					{
						if (!szIDs.equals(""))
							szIDs += ",";
						szIDs += rs2.getLong("id");
					}
					rs2.close();

					szQuery = "UPDATE evaluation_cs SET blocked=1 WHERE "
							+ "ps_date>='" + ApiGlobal.Date2String(dtLowerBnd, true) + "' AND "
							+ "ps_date<'" + ApiGlobal.Date2String(dtUpperBnd, true) + "' AND "
							+ "from_userid=" + from_id + " AND to_userid=" + to_id + " AND id NOT IN (" + szIDs + ")";
					stmt2.executeUpdate(szQuery);
				}
				rs.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				if (rs != null)
				{
					try { rs.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
	
				if (rs2 != null)
				{
					try { rs2.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
	
				if (stmt != null)
				{
					try { stmt.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
	
				if (stmt2 != null)
				{
					try { stmt2.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (dbConn != null)
			{
				try { dbConn.close(); } catch (Exception ex) { ex.printStackTrace(); }
			}
		}
	}


	// Operation for once an hour
	public static void hourlyProc()
	{
		Connection dbConn = null;

		try {
			dbConn = DBManager.getDBConnection();

			int nCurDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

			Calendar calLowBnd = Calendar.getInstance(), calUpBnd = Calendar.getInstance();

			calLowBnd.add(Calendar.HOUR_OF_DAY, 1);
			calLowBnd.set(Calendar.MINUTE, 0);
			calLowBnd.set(Calendar.SECOND, 0);

			calUpBnd.add(Calendar.HOUR_OF_DAY, 2);
			calUpBnd.set(Calendar.MINUTE, 0);
			calUpBnd.set(Calendar.SECOND, 0);


			// Correct system day to our custom day
			switch (nCurDay)
			{
				case Calendar.MONDAY:
					nCurDay = 0;
					break;
				case Calendar.TUESDAY:
					nCurDay = 1;
					break;
				case Calendar.WEDNESDAY:
					nCurDay = 2;
					break;
				case Calendar.THURSDAY:
					nCurDay = 3;
					break;
				case Calendar.FRIDAY:
					nCurDay = 4;
					break;
				case Calendar.SATURDAY:
					nCurDay = 5;
					break;
				case Calendar.SUNDAY:
					nCurDay = 6;
					break;
			}


			// DB variables
			Statement stmt = null, stmt2 = null;
			ResultSet rs = null, rs2 = null;
			String szQuery = "";

			// Get all the on off duty order info which start time is between dtLowBnd and dtUpBnd
			try
			{
				stmt = dbConn.createStatement();
				stmt2 = dbConn.createStatement();

				szQuery = "SELECT "
						+ "	order_onoffduty_details.*,"

						+ "	order_onoffduty_divide.id AS divideid, "
						+ "	order_onoffduty_divide.which_days, "
						+ "	order_onoffduty_divide.driver_id "

						+ "FROM order_onoffduty_details "

						+ "LEFT JOIN order_onoffduty_divide "
						+ "ON order_onoffduty_details.id=order_onoffduty_divide.orderdetails_id "

						+ "WHERE "
						+ "	order_onoffduty_details.deleted=0 AND "
						+ "	order_onoffduty_details.status<>3 AND "		  // Not finished on off duty order
						+ "	order_onoffduty_details.leftdays LIKE '%" + nCurDay + "%'";

				rs = stmt.executeQuery(szQuery);
				while (rs.next())
				{
					Date start_time = rs.getDate("pre_time");
					SVCOrderOnOffDutyDetails onoffOrder = SVCOrderOnOffDutyDetails.decodeFromResultSet(rs);
					long orderid = rs.getLong("id");
					long passid = rs.getLong("publisher");
					long driverid = rs.getLong("driver_id");
					String szDays = rs.getString("which_days");

					Calendar cal_start = Calendar.getInstance();

					cal_start.setTime(start_time);

					// It is not start time yet
					if (cal_start.get(Calendar.HOUR_OF_DAY) < calLowBnd.get(Calendar.HOUR_OF_DAY) ||
						cal_start.get(Calendar.HOUR_OF_DAY) >= calUpBnd.get(Calendar.HOUR_OF_DAY))
					{
						continue;
					}

					if (!szDays.contains("" + nCurDay))					// No driver accepted or today is not accepted
					{
						String szMsg = ConstMgr.STR_NIN
								+ ApiGlobal.Date2ChnTimeStr(start_time)
								+ ConstMgr.STR_CONG
								+ onoffOrder.start_addr
								+ ConstMgr.STR_QIANWANG
								+ onoffOrder.end_addr
								+ ConstMgr.STR_DE
								+ ConstMgr.STR_SXBDDZSWRJD;

						ApiGlobal.addOrderNotification(dbConn, passid, 0, szMsg);


						STPushNotificationData data = new STPushNotificationData();
						data.title = ConstMgr.STR_DINGDANXIAOXI;
						data.description = szMsg;

						STPushNotificationCustomData customData = new STPushNotificationCustomData();
						customData.typecode = STPushNotificationCustomData.PNOTIF_TYPE_ORDER_NEEDTOSTART;
						customData.orderid = orderid;
						customData.ordertype = ConstMgr.ORDER_ONOFF;

						data.custom_data = customData;

						ApiGlobal.sendPushNotifToUser(dbConn, passid, data);
					}
					else if (driverid > 0)			// Send push notification to driver
					{
						String szMsg = ConstMgr.STR_NIN
								+ ApiGlobal.Date2ChnTimeStr(start_time)
								+ ConstMgr.STR_CONG
								+ onoffOrder.start_addr
								+ ConstMgr.STR_QIANWANG
								+ onoffOrder.end_addr
								+ ConstMgr.STR_DE
								+ ConstMgr.STR_SXBDDYDZXSJ;

						ApiGlobal.addOrderNotification(dbConn, driverid, 0, szMsg);


						STPushNotificationData data = new STPushNotificationData();
						data.title = ConstMgr.STR_DINGDANXIAOXI;
						data.description = szMsg;

						STPushNotificationCustomData customData = new STPushNotificationCustomData();
						customData.typecode = STPushNotificationCustomData.PNOTIF_TYPE_OTHER;
						customData.orderid = orderid;
						customData.ordertype = ConstMgr.ORDER_ONOFF;

						data.custom_data = customData;

						ApiGlobal.sendPushNotifToUser(dbConn, driverid, data);
					}
				}
				rs.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}


			// Check all the sms send plan, send sms on time
			try
			{
				szQuery = "SELECT * FROM sms_plan WHERE deleted=0 AND isenabled=1 AND send_mode=2";

				rs = stmt.executeQuery(szQuery);

				final double sms_price = Double.parseDouble(gSmsPrice);
				final int sms_len = Integer.parseInt(gSmsMaxLen);

				boolean isFitCond = false;
				while (rs.next())
				{
					plan = SVCSmsPlan.decodeFromResultSet(rs);

					isFitCond = false;

					arrUserIDs.clear();
					arrUserPhones.clear();

					if (plan.regular_send_mode == 1 && plan.time1 == calLowBnd.get(Calendar.HOUR_OF_DAY))			// Once a day condition is fit
					{
						isFitCond = true;
					}
					else if (plan.regular_send_mode == 2 &&
							plan.time1 == nCurDay &&
							plan.time2 == calLowBnd.get(Calendar.HOUR_OF_DAY))	 // Once a week condition is fit
					{
						isFitCond = true;
					}
					else if (plan.regular_send_mode == 3 &&
							plan.time1 == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) &&
							plan.time2 == calLowBnd.get(Calendar.HOUR_OF_DAY))	  // Once a month condition is fit
					{
						isFitCond = true;
					}


					if (!isFitCond)					 // Condition is not fit. Skip
						continue;


					TimerTask sendTask = null;
					Timer sendTimer = new Timer();

					szQuery = "SELECT "
							+ "   sms_users.*, "
							+ "   user.phone AS user_phone "

							+ "FROM sms_users "

							+ "LEFT JOIN user "
							+ "ON user.id = sms_users.userid "

							+ "WHERE sms_plan_id=" + plan.id + " AND deleted=0";
					rs2 = stmt2.executeQuery(szQuery);
					while (rs2.next())
					{
						long _userid = rs2.getLong("userid");
						if (_userid > 0)
						{
							arrUserIDs.add(_userid);
							arrUserPhones.add(rs2.getString("user_phone"));
						}
						else
						{
							arrUserIDs.add(_userid);
							arrUserPhones.add(rs2.getString("phone"));
						}
					}
					rs2.close();


					sendTask = new TimerTask()
					{
						@Override
						public void run()
						{
							Connection dbConn = null;
							Statement stmt = null;
							String szQuery = "";

							szQuery = "INSERT INTO sms_log ("
									+ "sms_plan_id,"
									+ "user_id,"
									+ "price,"
									+ "status,"
									+ "time,"
									+ "phonenum,"
									+ "deleted) VALUES ";

							int nStatus = 1;
							for (int i = 0; i < arrUserIDs.size(); i++)
							{
								if (ApiGlobal.sendSMS(arrUserPhones.get(i), plan.msg))
									nStatus = 1;
								else
									nStatus = 2;

								if (szQuery.charAt(szQuery.length() - 1) == ')')
									szQuery += ",";

								szQuery += plan.id + ",";
								szQuery += arrUserIDs.get(i) + ",";
								szQuery += sms_price * (plan.msg.length() / sms_len + 1) + ",";
								szQuery += nStatus + ",'";
								szQuery += ApiGlobal.Date2String(new Date(), true) + "','";
								szQuery += arrUserPhones.get(i) + "',";
								szQuery += "0)";
							}

							// Save log in database
							try
							{
								dbConn = DBManager.getDBConnection();
								stmt = dbConn.createStatement();

								stmt.executeUpdate(szQuery);

								stmt.close();
								dbConn.close();
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}
							finally
							{
								if (stmt != null)
								{
									try { stmt.close(); } catch (Exception ex) { ex.printStackTrace(); }
								}

								if (dbConn != null)
								{
									try { dbConn.close(); } catch (Exception ex) { ex.printStackTrace(); }
								}
							}
						}
					};


					if (plan.regular_send_mode == 1 && plan.time1 == calLowBnd.get(Calendar.HOUR_OF_DAY))			// Once a day
					{
						int nHour = plan.time1;
						int nMinute = plan.time2;

						Calendar calSend = Calendar.getInstance();
						calSend.add(Calendar.HOUR_OF_DAY, nHour);
						calSend.set(Calendar.MINUTE, nMinute);
						calSend.set(Calendar.SECOND, 0);

						sendTimer.schedule(sendTask, calSend.getTime());
					}
					else if (plan.regular_send_mode == 2 &&
							plan.time1 == nCurDay &&
							plan.time2 == calLowBnd.get(Calendar.HOUR_OF_DAY))	 // Once a week
					{
						sendTimer.schedule(sendTask, 0);
					}
					else if (plan.regular_send_mode == 3 &&
							plan.time1 == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) &&
							plan.time2 == calLowBnd.get(Calendar.HOUR_OF_DAY))	  // Once a month
					{
						sendTimer.schedule(sendTask, 0);
					}
				}
				rs.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				if (rs != null)
				{
					try { rs.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}

				if (rs2 != null)
				{
					try { rs2.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}

				if (stmt != null)
				{
					try { stmt.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}

				if (stmt2 != null)
				{
					try { stmt2.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (dbConn != null)
			{
				try { dbConn.close(); } catch (Exception ex) { ex.printStackTrace(); }
			}
		}
	}


	// Operation for every month
	public static void monthlyProc()
	{
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		Connection dbConn = null;

		try {
			dbConn = DBManager.getDBConnection();

			try
			{
				stmt = dbConn.createStatement();

				// Find all the users whose register date is before x month ago and has not got coupon
				SVCGlobalParams global_params = null;

				long sysCpnID = 0;
				String sysCpnRemark = "";
				int sysCpnValPrd = 0;
				int sysCpnValPrdUnit = 0;
				int nRegMonth = 0;

				String userIDs = "";
				ArrayList<Long> arrUserIDs = new ArrayList<Long>();
				ArrayList<Integer> arrMonths = new ArrayList<Integer>();

				szQuery = "SELECT * FROM global_params WHERE deleted=0";
				rs = stmt.executeQuery(szQuery);
				rs.next();
				global_params = SVCGlobalParams.decodeFromResultSet(rs);
				rs.close();

				szQuery = "SELECT * FROM sys_coupon WHERE deleted=0 AND isenabled=1 and syscoupon_code='" + global_params.registermonth_syscoupon_id + "'";
				rs = stmt.executeQuery(szQuery);
				if (rs.next())
				{
					sysCpnID = rs.getLong("id");
					sysCpnRemark = rs.getString("remark");
					sysCpnValPrd = rs.getInt("valid_period");
					sysCpnValPrdUnit = rs.getInt("valid_period_unit");
				}
				rs.close();

				szQuery = "SELECT "
						+ "	user.*,"
						+ "	TIMESTAMPDIFF(YEAR, user.reg_date, CURDATE()) AS RegMonths, "
						+ "	city.registermonth_syscoupon_id,"
						+ "	city.num_registermonth,"
						+ "	sys_coupon.id AS syscpnID,"
						+ "	sys_coupon.remark AS syscpnRemark,"
						+ "	sys_coupon.valid_period AS syscpnValPeriod,"
						+ "	sys_coupon.valid_period_unit AS syscpnValPeriodUnit "

						+ "FROM user "

						+ "LEFT JOIN city "
						+ "ON city.code=user.city_cur "

						+ "LEFT JOIN sys_coupon "
						+ "ON sys_coupon.syscoupon_code=city.registermonth_syscoupon_id "

						+ "WHERE "
						+ "	user.got_afterreg_coupon=0 AND "
						+ "	user.deleted=0 AND "
						+ "	sys_coupon.isenabled=1 AND "
						+ "	sys_coupon.deleted=0 "
						+ "HAVING RegMonths>=city.num_registermonth";

				rs = stmt.executeQuery(szQuery);

				szQuery = "INSERT INTO single_coupon ("
						+ "coupon_code,"
						+ "sum,"
						+ "date_expired,"
						+ "isused,"
						+ "password,"
						+ "ps_date,"
						+ "remark,"
						+ "userid,"
						+ "order_cs_id,"
						+ "isenabled,"
						+ "syscoupon_id,"
						+ "active_code,"
						+ "is_generated_by_active,"
						+ "deleted) VALUES ";

				String szCpnCode = "";
				Date dt_exp = null;

				while (rs.next())
				{
					SVCUser user = SVCUser.decodeFromResultSet(rs);

					if (rs.getLong("syscpnID") == 0 && sysCpnID == 0)		   // No coupon is selected for this condition. Skip
						continue;

					if (rs.getLong("syscpnID") != 0)
					{
						nRegMonth = rs.getInt("num_registermonth");
						if (rs.getInt("RegMonths") < rs.getInt("num_registermonth"))		// Not over date yet. Skip
							continue;

						sysCpnID = rs.getLong("syscpnID");
						sysCpnRemark = rs.getString("syscpnRemark");
						sysCpnValPrd = rs.getInt("syscpnValPeriod");
						sysCpnValPrdUnit = rs.getInt("syscpnValPeriodUnit");
					}
					else
					{
						nRegMonth = global_params.num_registermonth;
						if (rs.getInt("RegMonths") < global_params.num_registermonth)		// Not over date yet. Skip
							continue;
					}

					szCpnCode = ApiGlobal.generateSingleCouponCode();

					Calendar cal_cur = Calendar.getInstance();
					if (sysCpnValPrdUnit == 1)					// Unit day
						cal_cur.add(Calendar.DAY_OF_YEAR, sysCpnValPrd);
					else if (sysCpnValPrdUnit == 2)				// Unit week
						cal_cur.add(Calendar.WEEK_OF_YEAR, sysCpnValPrd);
					else if (sysCpnValPrdUnit == 3)				// Unit month
						cal_cur.add(Calendar.MONTH, sysCpnValPrd);
					else if (sysCpnValPrdUnit == 4)				// Unit year
						cal_cur.add(Calendar.YEAR, sysCpnValPrd);
					dt_exp = cal_cur.getTime();

					if (szQuery.charAt(szQuery.length() - 1) == ')')
						szQuery += ",";

					szQuery += "('" + szCpnCode + "',"
							+ "0,'" + ApiGlobal.Date2String(dt_exp, true) + "',"
							+ "0, '', '" + ApiGlobal.Date2String(new Date(), true) + "', '"
							+ sysCpnRemark + "', "
							+ user.id + ", 0, 1, " + sysCpnID + ", '', 0, 0)";


					if (!userIDs.equals(""))
						userIDs += ",";
					userIDs += user.id;
					arrUserIDs.add(user.id);
					arrMonths.add(nRegMonth);
				}
				rs.close();

				if (szQuery.charAt(szQuery.length() - 1) == ')')
				{
					stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS);
					String szMsg = "";
					rs = stmt.getGeneratedKeys();
					int nIndex = 0;
					while (rs.next())
					{
						long couponid = rs.getLong(1);
						szMsg = "您注册的时间已经过" + arrMonths.get(nIndex) + "月了.自动发放给你一个点券";
						ApiGlobal.addPersonNotification(dbConn, arrUserIDs.get(nIndex), couponid, szMsg);
					}
					rs.close();

					// Set flags
					szQuery = "UPDATE user SET got_afterreg_coupon=1 WHERE id IN (" + userIDs + ")";
					stmt.executeUpdate(szQuery);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				if (rs != null)
				{
					try { rs.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
	
				if (stmt != null)
				{
					try { stmt.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (dbConn != null)
			{
				try { dbConn.close(); } catch (Exception ex) { ex.printStackTrace(); }
			}
		}
	}


	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}


	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		process();
	}



}



