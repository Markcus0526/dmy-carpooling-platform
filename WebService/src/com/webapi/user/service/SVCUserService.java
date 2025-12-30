package com.webapi.user.service;

import java.math.BigDecimal;
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
import com.webapi.structure.SVCAppSpreadUnit;
import com.webapi.structure.SVCGroup;
import com.webapi.structure.SVCProfitSharingConfigPerson;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCTs;
import com.webapi.structure.SVCUser;
import com.webapi.structure.SVCUserCar;
import com.webapi.structure.SVCUserLogin;

public class SVCUserService {
	/**
	 * 
1，校验(1)判断参数是否有为空的(2)source是否为main或者pinche
2，判断用户是否已经存在
3，根据邀请码长度判断是个人邀请，群组邀请还是合作单位邀请
4，根据邀请码查询出邀请人
5，获取默认的我要求别人给我的上供信息
6，分别向三种类型的邀请人设置上供信息
7，将当前用户插入到user表中
8，向ts表插入一条默认的记录，并把主键值赋给user表的balance_id
9，如果用户是群组邀请来的，需要加入到群组
10，生成当前用户的邀请码，并赋予给当前用户
11，向user_login表里插入记录，主要是设置devtoken,user_login表在数据库里起到web服务器的session的作用，用于维护当前登录的用户信息和唯一标示devtoken



问题1：设置用户默认余额的时候现在是500元，应该为0
问题2：注册的时候需要奖励点券，目前没有实现
问题3：建议删除single_coupon表里的coupon_code字段


关键点1：为用户自生成的邀请码一定要确保是唯一的，产生邀请码的函数重写了。现在采取用userid转换成34进位的方式实现的。
关键点2：建议删除single_coupon表里的coupon_code字段

	 * 
	 * @param source
	 * @param username
	 * @param mobile
	 * @param nickname
	 * @param password
	 * @param invitecode
	 * @param sex
	 * @param city
	 * @param devtoken
	 * @param platform
	 * @return
	 */
	public SVCResult registerUser(String source, String username,
			String mobile, String nickname, String password, String invitecode,
			int sex, String city, String devtoken,
			String pushnotif_token, int platform,String channel_flag) {
		SVCResult result = new SVCResult();
		ApiGlobal.logMessage(source + "," + username + "," + mobile
				+ "," + nickname + "," + password + ","
				+ invitecode + "," + sex + "," + city
				+ "," + devtoken + "," + pushnotif_token + ","
				+ platform + "," + channel_flag);

		// Check parameters
		if (source.equals("") || username.equals("") || mobile.equals("")
				|| nickname.equals("") || password.equals("") || sex < 0
				|| platform < 0) {
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
			dbConn.setAutoCommit(false);
			// Check the duplicate user
			stmt = dbConn.createStatement();

			String szNickName =  nickname;//页面上有先生/女士的字段，所以不用把先生/女士存到nickname字段中，而且性别可以更改，那么如果存了先生/女士的话，就要额外更改nickname这一字段。 modify by chuzhiqiang 
			// 1,查询用户表里有没有这个用户，如果有的话则返回重复注册的错误
			szQuery = "SELECT usercode AS cnt FROM user WHERE deleted = 0 AND usercode LIKE \"%"
					+ username + "&\" AND CHAR_LENGTH(usercode)=" + username.length();

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_DuplicateUserName;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			rs.close();


			szQuery = "SELECT phone AS cnt FROM user WHERE deleted = 0 AND phone LIKE \"%"
					+ mobile + "%\" AND CHAR_LENGTH(phone)=" + mobile.length();

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_DuplicateMobile;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			rs.close();



			// 2,根据用户输入的邀请码，来判断是个人邀请，还是群组邀请，还是合作单位邀请。可以根据邀请码的长度来判断邀请人是谁，6位是个人邀请，7位是群组邀请，8位是合作单位邀请
			SVCUser invited_user = null;
			SVCAppSpreadUnit invited_unit = null;
			SVCGroup invited_group = null;

			if (invitecode != null && invitecode.trim().length() == 6) {// 邀请人是个人
				// finding invited individual user
				szQuery = "SELECT * FROM user WHERE deleted = 0 AND invitecode_self='"
						+ invitecode + "'";
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					invited_user = SVCUser.decodeFromResultSet(rs);
					if (rs != null)
						rs.close();
				}
			} else if (invitecode != null && invitecode.trim().length() == 7) {// 邀请人是群组
				// finding invited group
				szQuery = "SELECT * FROM group_ WHERE deleted = 0 AND invitecode_self='"
						+ invitecode + "'";
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					invited_group = SVCGroup.decodeFromResultSet(rs);
					if (rs != null)
						rs.close();
				}
			} else if (invitecode != null && invitecode.trim().length() == 8) {// 邀请人是合作单位
				// finding invited app spread unit
				szQuery = "SELECT * FROM app_spread_unit WHERE deleted = 0 AND invite_code='"
						+ invitecode + "'";
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					invited_unit = SVCAppSpreadUnit.decodeFromResultSet(rs);
					if (rs != null)
						rs.close();
				}
			}
			// 2.1,获取别人给我贡献的上供信息的默认设置
			SVCProfitSharingConfigPerson config_info = null;
			szQuery = "SELECT * FROM profitsharing_config_person where deleted = 0";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				config_info = SVCProfitSharingConfigPerson
						.decodeFromResultSet(rs);
			rs.close();
			
			if (config_info == null) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			// Prepare some parameters to save to db
			int app_reg = 1;
			if (source.equals(ConstMgr.SRC_MAINAPP))
				app_reg = 1;
			else if (source.equals(ConstMgr.SRC_PINCHEAPP))
				app_reg = 2;
			else
				app_reg = 3;

			String szCityCode = "";
			// Prepare city parameter
			if (source.equals(ConstMgr.SRC_MAINAPP)) {
				city = ApiGlobal.getCityOfPhone(mobile);
				if (city == null)
					city = "";
			}

			// Get city code from city name
			szCityCode = ApiGlobal.cityName2Code(dbConn,city);

			// <G1 set> parameters
			int provide_profitsh_way = -1;
			int provide_profitsh_time_as_pass = -1;
			int provide_profitsh_count_as_pass = -1;
			int provide_profitsh_time_as_drv = -1;
			int provide_profitsh_count_as_drv = -1;
			double ratio_as_pass = -1;
			double integer_as_pass = -1;
			int activeway_as_pass = -1;
			double ratio_as_drv = -1;
			double integer_as_drv = -1;
			int activeway_as_drv = -1;

			int inviter_type = 0;
			// 3,当前用户需要给其邀请人设置分成信息
			if (invited_user != null) {// 当前用户的邀请人为个人的情况下，当前用户需要给其邀请人贡献的分成
				provide_profitsh_way = invited_user.limit_way;

				provide_profitsh_time_as_pass = invited_user.limit_count_as_passenger_self;
				provide_profitsh_count_as_pass = invited_user.limit_count_as_passenger_self;
				provide_profitsh_time_as_drv = invited_user.limit_month_as_driver_self;
				provide_profitsh_count_as_drv = invited_user.limit_count_as_driver_self;

				ratio_as_pass = invited_user.ratio_as_passenger_self;
				integer_as_pass = invited_user.integer_as_passenger_self;
				activeway_as_pass = invited_user.active_as_passenger_self;

				ratio_as_drv = invited_user.ratio_as_driver_self;
				integer_as_drv = invited_user.integer_as_driver_self;
				activeway_as_drv = invited_user.active_as_driver_self;

				inviter_type = 1;
			} else if (invited_unit != null) {// 当前用户的邀请人为合作单位的情况下，当前用户需要给其邀请人贡献的分成
				provide_profitsh_way = invited_unit.limit_way;

				provide_profitsh_time_as_pass = invited_unit.limit_month_as_passenger_self;
				provide_profitsh_count_as_pass = invited_unit.limit_count_as_passenger_self;
				provide_profitsh_time_as_drv = invited_unit.limit_month_as_driver_self;
				provide_profitsh_count_as_drv = invited_unit.limit_count_as_driver_self;

				ratio_as_pass = invited_unit.ratio_as_passenger_self;
				integer_as_pass = invited_unit.integer_as_passenger_self;
				activeway_as_pass = invited_unit.active_as_passenger_self;

				ratio_as_drv = invited_unit.ratio_as_driver_self;
				integer_as_drv = invited_unit.integer_as_driver_self;
				activeway_as_drv = invited_unit.active_as_driver_self;

				inviter_type = 3;
			} else if (invited_group != null) {// 当前用户的邀请人为群组的情况下，当前用户需要给其邀请人贡献的分成
				provide_profitsh_way = invited_group.limit_way;

				provide_profitsh_time_as_pass = invited_group.limit_month_as_passenger_self;
				provide_profitsh_count_as_pass = invited_group.limit_count_as_passenger_self;
				provide_profitsh_time_as_drv = invited_group.limit_month_as_driver_self;
				provide_profitsh_count_as_drv = invited_group.limit_count_as_driver_self;

				ratio_as_pass = invited_group.ratio_as_passenger_self;
				integer_as_pass = invited_group.integer_as_passenger_self;
				activeway_as_pass = invited_group.active_as_passenger_self;

				ratio_as_drv = invited_group.ratio_as_driver_self;
				integer_as_drv = invited_group.integer_as_driver_self;
				activeway_as_drv = invited_group.active_as_driver_self;

				inviter_type = 2;
			}

			// 4，将当前用户插入到user表中
			szQuery = "INSERT INTO user (" 
					+ "usercode," 
					+ "username,"
					+ "nickname," 
					+ "password," 
					+ "phone," 
					+ "balance_ts,"
					+ "invitecode_self,"
					+ "invitecode_regist," 
					+ "reg_date,"
					+ "last_login_time,"
					+ "app_register," 
					+ "bankcard,"
					+ "bankname," 
					+ "subbranch," 
					+ "sex," 
					+ "id_card1,"
					+ "driver_license1," 
					+ "driving_license1," 
					+ "id_card2,"
					+ "driver_license2," 
					+ "driving_license2," 
					+ "img,"
					+ "person_verified," 
					+ "driver_verified,"
					+ "city_register," 
					+ "city_cur,"
					+ "provide_profitsharing_way,"
					+ "provide_profitsharing_time_as_passenger,"
					+ "provide_profitsharing_count_as_passenger,"
					+ "provide_profitsharing_time_as_driver,"
					+ "provide_profitsharing_count_as_driver,"
					+ "ratio_as_passenger," 
					+ "integer_as_passenger,"
					+ "activeway_as_passenger," 
					+ "ratio_as_driver,"
					+ "integer_as_driver," 
					+ "activeway_as_driver,"
					+ "inviter_type," 
					+ "is_platform,"
					+ "ratio_as_passenger_self," 
					+ "integer_as_passenger_self,"
					+ "active_as_passenger_self," 
					+ "ratio_as_driver_self,"
					+ "integer_as_driver_self," 
					+ "active_as_driver_self,"
					+ "limit_way," 
					+ "limit_month_as_passenger_self,"
					+ "limit_month_as_driver_self,"
					+ "limit_count_as_passenger_self,"
					+ "limit_count_as_driver_self," 
					+ "phone_system,"
					+ "device_token," 
					+ "nation," 
					+ "id_card_num," 
					+ "address,"
					+ "drivinglicence_num," 
					+ "drlicence_ti," 
					+ "deleted,"
					+ "channel_flag)"
					+ " VALUES (\""
					+ username + "\"," // User code
					+ "'',\"" // User name
					+ szNickName
					+ "\",\""
					+ ApiGlobal.MD5Hash(password)
					+ "\",\""
					+ mobile
					+ "\","
					+ "0,'',\""
					+ invitecode
					+ "\",\""
					+ ApiGlobal.Date2String(new Date(), true)
					+ "\",\""
					+ ApiGlobal.Date2String(new Date(), true)
					+ "\","
					+ app_reg
					+ ", '', '', '',"
					+ sex
					+ ", '', '', '', '', '', '', '', 0, 0,\""
					+ szCityCode
					+ "\",\""
					+ szCityCode
					+ "\","
					+ provide_profitsh_way
					+ ","
					+ provide_profitsh_time_as_pass
					+ ","
					+ provide_profitsh_count_as_pass
					+ ","
					+ provide_profitsh_time_as_drv
					+ ","
					+ provide_profitsh_count_as_drv
					+ ","
					+ ratio_as_pass
					+ ","
					+ integer_as_pass
					+ ","
					+ activeway_as_pass
					+ ","
					+ ratio_as_drv
					+ ","
					+ integer_as_drv
					+ ","
					+ activeway_as_drv
					+ ","
					+ inviter_type
					+ ", 0, "
					
					+ config_info.ratio_as_passenger
					+ ","
					+ config_info.integer_as_passenger
					+ ","
					+ config_info.active_as_passenger
					+ ","
					+ config_info.ratio_as_driver
					+ ","
					+ config_info.integer_as_driver
					+ ","
					+ config_info.active_as_driver
					+ ","
					+ config_info.limit_way
					+ ","
					+ config_info.limit_month_as_passenger
					+ ","
					+ config_info.limit_month_as_driver
					+ ","
					+ config_info.limit_count_as_passenger
					+ ","
					+ config_info.limit_count_as_driver
					+ ","
					+ platform
					+ ",\"" + devtoken + "\", '', '', '', '', '', 0,"
							+ "\'"+channel_flag+"\')";
			System.out.println("config_info.active_as_driver===="+config_info.active_as_driver);
			System.out.println("szQuery===="+szQuery);

			int nResult = stmt.executeUpdate(szQuery,
					Statement.RETURN_GENERATED_KEYS);
			if (nResult != 1) {// 插入用户不成功
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			// 5,向ts表插入该用户数据，并且把ts表的该用户主键值取出来备用，以后插入到balance_id这个字段里
			ResultSet genKeys = stmt.getGeneratedKeys();
			if (genKeys == null || !genKeys.next()) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			long userid = genKeys.getLong(1);// 注意这里直接getLong就行，因为上一步已经执行next方法了。获得新增加进user表记录的id
			String szInviteCode_Self = ApiGlobal.long2NLenString(userid, 6);

			long balance_id =-1;
			String application = "";
			if (source.equals(ConstMgr.SRC_MAINAPP))
				application = ConstMgr.SRC_CHN_MAINAPP;
			else if (source.equals(ConstMgr.SRC_PINCHEAPP))
				application = ConstMgr.SRC_CHN_PINCHEAPP;
			else
				application = ConstMgr.STR_OTHERS;

			szQuery = "INSERT INTO ts (oper, ts_way, balance, ts_date, userid, groupid, unitid, remark, account, account_type, application, ts_type, sum, deleted"
					+ ") VALUES ("
					+ "2, -1, "
					+ ApiGlobal.defaultBalance()
					+ ", \""
					+ ApiGlobal.Date2String(new Date(), true)
					+ "\", "
					+ userid
					+ ", 0, 0, \""
					+ ApiGlobal.getCommentFromTxCode(dbConn, ConstMgr.txcode_userRegister)
					+ "\", '', 1, \""
					+ application
					+ "\", \""
					+ ConstMgr.txcode_userRegister + "\", " + ApiGlobal.defaultBalance() + ", 0)";

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) == 1) {
				rs = stmt.getGeneratedKeys();
				if (rs != null && rs.next())
					balance_id = rs.getLong(1);
			}
			
			
			// 6,如果用户属于群组的话，将该用户插入到群组表中。
			if (invited_group != null) {// 如果属于某一个群组，则将该条用户数据插入到group_details表中
		
				szQuery = "INSERT INTO group_details (groupid, userid, group_type, application, deleted) VALUES (\""
						+ invited_group.groupid
						+ "\", "
						+ userid
						+ ", 0, \""
						+ application
						+ "\", 0)";

				stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS);
			}
			// 7,设置user表的邀请码和balance_id字段值
			szQuery = "UPDATE user SET invitecode_self=\"" + szInviteCode_Self
					+ "\", balance_ts=" + balance_id
					+ " WHERE deleted = 0 AND id=" + userid;
			stmt.executeUpdate(szQuery);
			if (rs != null) {
				rs.close();
			}

			// 8，
			// 往user_login表里增加一条记录，user_login表用来精确记录当前用户的登录设备号码，这个表相当于服务器的session机制。
			szQuery = "SELECT * FROM user_login WHERE deleted = 0 AND devtoken=\""
					+ devtoken + "\"";
			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) {// 如果该用户以前没有登录过，则在user_login表新增一条记录
				szQuery = "UPDATE user_login "
						+ "SET loggedout=1 "
						+ "WHERE devtoken=\"" + devtoken + "\"";
				stmt.executeUpdate(szQuery);


				szQuery = "INSERT INTO user_login ("
						+ "userid,"
						+ "source,"
						+ "devtoken,"
						+ "platform,"
						+ "login_time,"
						+ "pushnotif_token,"
						+ "loggedout,"
						+ "auto_login,"
						+ "remem_pwd,"
						+ "deleted"
						+ ") VALUES ("
						+ userid + ","
						+ app_reg + ",\""
						+ devtoken + "\","
						+ platform + ",\""
						+ ApiGlobal.Date2String(new Date(), true) + "\",\""
						+ pushnotif_token + "\","
						+ " 0, 1, 1, 0)";

				stmt.executeUpdate(szQuery);
			} else {// 如果
				SVCUserLogin login_info = SVCUserLogin.decodeFromResultSet(rs);
				szQuery = "UPDATE user_login "
						+ "SET loggedout=1 "
						+ "WHERE "
						+ "		devtoken=\"" + devtoken + "\" AND "
						+ "		id<>" + login_info.id;
				stmt.executeUpdate(szQuery);

				szQuery = "UPDATE "
						+ "		user_login "
						+ "SET "
						+ "		source=" + app_reg + ","
						+ "		userid=" + userid + ","
						+ "		devtoken=\"" + devtoken + "\","
						+ "		pushnotif_token=\"" + pushnotif_token + "\","
						+ "		loggedout=0,"
						+ "		platform=" + platform + ","
						+ "		login_time=\"" + ApiGlobal.Date2String(new Date(), true) + "\" "
						+ "WHERE "
						+ "		id=" + login_info.id;
				stmt.executeUpdate(szQuery);
			}

			//9,系统是否设置了注册的时候发放点券，如果设置了则要给用户发放点券
			long sysCouponId=0;
			String sysCouponCode = "";
			BigDecimal couponSum=null;
			int validPeriodUnit=0;
			int validPeriod=0;
			Date dtExp = null;

			szQuery = "SELECT * FROM city WHERE code=\"" + szCityCode + "\"";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
			{
				sysCouponCode = rs.getString("register_syscoupon_id");
			}
			rs.close();


			if (sysCouponCode.equals(""))
			{
				szQuery = "SELECT * FROM global_params WHERE deleted=0";
				rs = stmt.executeQuery(szQuery);
				if (rs.next())
				{
					sysCouponCode = rs.getString("register_syscoupon_id");
				}
				rs.close();
			}


			szQuery = "SELECT id,sum,valid_period_unit,valid_period "
					+ "FROM sys_coupon "
					+ "WHERE "
					+ "		syscoupon_code = \"" + sysCouponCode + "\"";


			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				sysCouponId = rs.getLong("id");
				couponSum = rs.getBigDecimal("sum");
				validPeriodUnit = rs.getInt("valid_period_unit");
				validPeriod = rs.getInt("valid_period");

				Calendar cal_cur = Calendar.getInstance();
				if (validPeriodUnit == 1) {
					cal_cur.add(Calendar.DAY_OF_YEAR, validPeriod);
				} else if (validPeriodUnit == 2) {
					cal_cur.add(Calendar.WEEK_OF_YEAR, validPeriod);
				} else if (validPeriodUnit == 3) {
					cal_cur.add(Calendar.MONTH, validPeriod);
				} else if (validPeriodUnit == 4) {
					cal_cur.add(Calendar.YEAR, validPeriod);
				}
				dtExp = cal_cur.getTime();
			}

			if (dtExp != null)
			{
				szQuery="INSERT INTO single_coupon (sum,date_expired,ps_date,userid,syscoupon_id) VALUES ("
						+ couponSum + ",\""
						+ ApiGlobal.Date2String(dtExp, true) + "\",\""
						+ ApiGlobal.Date2String(new Date(), true) + "\","
						+ userid + "," + sysCouponId
						+ ")";
				stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS);
				long couponid = 0;
				rs = stmt.getGeneratedKeys();
				if (rs.next())
				{
					String szMsg = "恭喜您在OO车系统注册成功.给您一个点券.请查看";
					couponid = rs.getLong(1);

					ApiGlobal.addPersonNotification(dbConn, userid, couponid, szMsg);
				}
				rs.close();				
			}


			// 9，处理成功，向手机端返回数据
			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

			JSONObject retdata = new JSONObject();
			{
				retdata.put("userid", userid);
				retdata.put("photo", "");
				retdata.put("mobile", mobile);
				retdata.put("nickname",
						ApiGlobal.removeNickNameSuffix(szNickName));
				retdata.put("sex", sex);
				retdata.put("birthday", "");
				retdata.put("person_verified", 0);
				retdata.put("driver_verified", 0);
				retdata.put("carimg", "");
				retdata.put("invitedcode", szInviteCode_Self);
				retdata.put("baiduak", ApiGlobal.getBaiduAKFromUserID(dbConn, userid));
			}

			result.retdata = retdata;
			dbConn.commit();
		} catch (Exception ex) {
			try {
				dbConn.rollback();
			} catch (Exception e) {
				e.printStackTrace();
			}

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
 * 1,校验用户名密码是否正确
 * 2,在user表里设置当前登录用户的手机设备标识号
 * 3，更新user_login表
 * 4,获取user_car信息
 * 5,6,获得用户未完成的订单，这里只获得单次的和上下班的，目的是单次的和上下班的打开之后需要显示上次操作未完成的页面，而长途不需要显示上次的页面
 * 6，返回数据
 * @param source
 * @param username
 * @param password
 * @param city
 * @param devtoken
 * @param platform
 * @return
 */
	public SVCResult loginUser(String source, String username, String password,
			String city, String devtoken, String pushnotif_token, int platform)
	{
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + username + "," + password + "," + city + "," + devtoken + "," + pushnotif_token + "," + platform);

		System.out.println("loginUser....");
		// Check parameters
		if (source.equals("") || username.equals("") || password.equals("")
				|| devtoken.equals("") || platform < 0) {
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
			dbConn.setAutoCommit(false);

			stmt = dbConn.createStatement();

			// 1,校验用户名密码是否正确
			SVCUser userinfo = null;

			szQuery = "SELECT * FROM user WHERE deleted = 0 AND usercode LIKE \"%"
					+ username + "%\" AND CHAR_LENGTH(usercode)=" + username.length();
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				userinfo = SVCUser.decodeFromResultSet(rs);
			}

			if (userinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			if (!userinfo.password.equals(ApiGlobal.MD5Hash(password))) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_PasswordWrong;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}


			// Auto logout all the users who uses same device
			szQuery = "UPDATE "
					+ "		user "
					+ "SET "
					+ "		loggedout=1 "
					+ "WHERE "
					+ "		device_token=\"" + devtoken + "\" AND "
					+ "		id<>" + userinfo.id;
			stmt.executeUpdate(szQuery);


			// 2,在user表里设置当前登录用户的手机设备标识号
			String szCityCode = ApiGlobal.cityName2Code(dbConn,city);
			if (szCityCode == null || szCityCode.equals(""))
			{
				if (userinfo.city_cur == null || userinfo.city_cur.equals(""))
					szCityCode = userinfo.city_register;
				else
					szCityCode = userinfo.city_cur;
			}

			szQuery = "UPDATE user "
					+ "SET "
					+ "		loggedout=0, "
					+ "		device_token=\"" + devtoken + "\","
					+ "		phone_system=" + platform + ", "
					+ "		last_login_time=\"" + ApiGlobal.Date2String(new Date(), true) + "\" ";

			if (!source.equals(ConstMgr.SRC_MAINAPP))
			{
				szQuery += ", city_cur=\"" + szCityCode + "\"";
			}

			szQuery += " WHERE deleted = 0 AND id=" + userinfo.id;

			if (stmt.executeUpdate(szQuery) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			int nSrc = 0;
			if (source.equals(ConstMgr.SRC_MAINAPP)) {
				nSrc = 1;
			} else if (source.equals(ConstMgr.SRC_PINCHEAPP)) {
				nSrc = 2;
			} else {
				nSrc = 3;
			}
			// 3，更新user_login表
			SVCUserLogin user_login = null;
			szQuery = "SELECT * FROM user_login WHERE deleted = 0 AND devtoken = \""
					+ devtoken + "\"";
			rs = stmt.executeQuery(szQuery);


			if (rs.next()) {
				user_login = SVCUserLogin.decodeFromResultSet(rs);
			}
			if (user_login == null) {// The first login device
				szQuery = "UPDATE user_login "
						+ "SET loggedout=1 "
						+ "WHERE devtoken=\"" + devtoken + "\"";
				stmt.executeUpdate(szQuery);


				szQuery = "INSERT INTO user_login ("
						+ "userid,"
						+ "source,"
						+ "devtoken,"
						+ "platform,"
						+ "login_time,"
						+ "pushnotif_token,"
						+ "loggedout,"
						+ "auto_login,"
						+ "remem_pwd,"
						+ "deleted"
						+ ") VALUES ("
						+ userinfo.id + ","
						+ nSrc + ",\""
						+ devtoken + "\","
						+ platform + ",\""
						+ ApiGlobal.Date2String(new Date(), true) + "\",\""
						+ pushnotif_token + "\","
						+ " 0, 1, 1, 0)";

			} else {
				szQuery = "UPDATE user_login "
						+ "SET loggedout=1 "
						+ "WHERE "
						+ "		devtoken=\"" + devtoken + "\" AND "
						+ "		id<>" + user_login.id;
				stmt.executeUpdate(szQuery);


				szQuery = "UPDATE "
						+ "		user_login "
						+ "SET "
						+ "		source=" + nSrc + ","
						+ "		userid=" + userinfo.id + ","
						+ "		devtoken=\"" + devtoken + "\","
						+ "		pushnotif_token=\"" + pushnotif_token + "\","
						+ "		loggedout=0,"
						+ "		platform=" + platform + ","
						+ "		login_time=\"" + ApiGlobal.Date2String(new Date(), true) + "\" "
						+ "WHERE "
						+ "		id=" + user_login.id;
			}

			if (stmt.executeUpdate(szQuery) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			JSONObject retdata = null;
			//4,获取user_car信息
			SVCUserCar usercar = null;
			szQuery = "SELECT * FROM user_car WHERE deleted = 0 AND userid = "
					+ userinfo.id;
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				usercar = SVCUserCar.decodeFromResultSet(rs);
			}

			// 5,返回数据
			retdata = new JSONObject();

			retdata.put("userid", userinfo.id);
			retdata.put("photo", ApiGlobal.getAbsoluteURL(userinfo.img));
			retdata.put("mobile", userinfo.phone);
			retdata.put("nickname",
					ApiGlobal.removeNickNameSuffix(userinfo.nickname));
			retdata.put("sex", userinfo.sex);
			retdata.put(
					"birthday",
					userinfo.birthday == null ? "" : ApiGlobal.Date2String(
							userinfo.birthday, true));
			retdata.put("person_verified", userinfo.person_verified);
			retdata.put("driver_verified", userinfo.driver_verified);
			retdata.put(
					"carimg",
					usercar == null ? "" : ApiGlobal
							.getAbsoluteURL(usercar.car_img));
			retdata.put("invitecode", userinfo.invitecode_self);

			retdata.put("baiduak", ApiGlobal.getBaiduAKFromUserID(dbConn, userinfo.id));

			// 6,获得用户未完成的订单，这里只获得单次的和上下班的，目的是单次的和上下班的打开之后需要显示上次未完成的页面，而长途不需要
			JSONObject exec_info = ApiGlobal
					.getExecuteOrderInfo(dbConn,userinfo.id);
			retdata.put("exec_info", exec_info);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = retdata;

			dbConn.commit();
		} catch (Exception ex) {
			try {
				dbConn.rollback();
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public SVCResult getVerifyKey(String source, String mobile, String username, int registered) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + mobile + "," + username + "," + registered);

		if (source.equals("") || mobile.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			ResultSet rs = null;
			Statement stmt = null;
			String szQuery = "";

			SVCUser userinfo = null;

			try {
				if (registered == 1)
				{
					dbConn = DBManager.getDBConnection();
					stmt = dbConn.createStatement();

					if (!username.equals(""))
					{
						szQuery = "SELECT * "
								+ "FROM user "
								+ "WHERE "
								+ "		usercode LIKE \"%" + username + "%\" AND "
								+ "		CHAR_LENGTH(usercode)=" + username.length() + " AND "
								+ "		deleted=0";

						rs = stmt.executeQuery(szQuery);
						if (!rs.next())
						{
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_UserNameNotExist;
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}
						userinfo = SVCUser.decodeFromResultSet(rs);
						rs.close();

					}
					else
					{
						szQuery = "SELECT * "
								+ "FROM user "
								+ "WHERE "
								+ "		phone LIKE \"%" + mobile + "%\" AND "
								+ "		CHAR_LENGTH(phone)=" + mobile.length() + " AND "
								+ "		deleted=0";

						rs = stmt.executeQuery(szQuery);
						if (!rs.next())
						{
							result.retcode = ConstMgr.ErrCode_Normal;
							result.retmsg = ConstMgr.ErrMsg_UserNamePhoneNotPair;
							ApiGlobal.logMessage(result.encodeToJSON().toString());

							return result;
						}
						userinfo = SVCUser.decodeFromResultSet(rs);
						rs.close();
					}

	
	
					if (!userinfo.phone.equals(mobile))
					{
						result.retcode = ConstMgr.ErrCode_Normal;
						result.retmsg = ConstMgr.ErrMsg_UserNamePhoneNotPair;
						ApiGlobal.logMessage(result.encodeToJSON().toString());

						return result;
					}
				}

				String szCode = ApiGlobal.genRand(4);

				ApiGlobal.sendSMS(mobile, szCode);

				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
				result.retdata = szCode;
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
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult forgetPassword(String source, String username,
			String mobile, String password, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + username + "," + mobile + "," + password + "," + devtoken);

		if (source.equals("") || mobile.equals("") || username.equals("")
				|| password.equals("")) {
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
			SVCUser userinfo = ApiGlobal.getUserInfoFromCode(dbConn, username);
			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			if (!userinfo.phone.equals(mobile)) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_MobileNotCorrect;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			stmt = dbConn.createStatement();
			szQuery = "UPDATE user SET password = \""
					+ ApiGlobal.MD5Hash(password)
					+ "\" WHERE deleted = 0 AND id = " + userinfo.id;

			if (stmt.executeUpdate(szQuery) == 1) // Update success
			{
				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
			} else {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
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

	public SVCResult getUserInfo(String source, long userid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + devtoken);

		if (source.equals("") || devtoken.equals("") || userid < 0) {
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
		try {
			dbConn = DBManager.getDBConnection();

			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoByUserID(dbConn, userid);
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

			SVCUserCar user_car_info = ApiGlobal
					.getUserCarInfoFromUserID(dbConn, userinfo.id);
			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

			JSONObject retdata = new JSONObject();
			retdata.put("userid", userinfo.id);
			retdata.put("photo", ApiGlobal.getAbsoluteURL(userinfo.img));
			retdata.put("mobile", userinfo.phone);
			retdata.put("nickname",
					ApiGlobal.removeNickNameSuffix(userinfo.nickname));
			retdata.put("sex", userinfo.sex);
			retdata.put(
					"birthday",
					userinfo.birthday == null ? "" : ApiGlobal.Date2String(
							userinfo.birthday, false));
			retdata.put("person_verified", userinfo.person_verified);
			retdata.put("driver_verified", userinfo.driver_verified);
			retdata.put(
					"carimg",
					user_car_info == null ? "" : ApiGlobal
							.getAbsoluteURL(user_car_info.car_img));
			retdata.put("invitecode", userinfo.invitecode_self);

			result.retdata = retdata;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ex.getMessage();
		} finally {
			if (dbConn != null)
			{
				try { dbConn.close(); } catch (Exception ex) { ex.printStackTrace(); }
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

	public SVCResult changeUserInfo(String source, long userid, String mobile,
			String nickname, String birthday, int sex, String photo,
			int photo_changed, String carimg, int carimg_changed,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + mobile + "," + nickname + "," + birthday + "," + sex + ",photo" + ","
				+ photo_changed + ",carimg" + "," + carimg_changed + "," + devtoken);

		if (source.equals("") || userid < 0 || mobile.equals("")
				|| nickname.equals("") || sex < 0
				|| photo_changed < 0 || carimg_changed < 0
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
			SVCUser userinfo = ApiGlobal.getUserInfoByUserID(dbConn, userid);
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

			dbConn.setAutoCommit(false);
			stmt = dbConn.createStatement();

			Date dtBirthday = ApiGlobal.String2Date(birthday);
			String szCurBirth = ApiGlobal.Date2String(dtBirthday, false);

			String szNickName =  nickname;//modify by chuzhiqiang  昵称后面有先生/女士标识，所以不用将先生女士存到nickname字段中。

			// Save images
			String photo_path = null, carimg_path = null;
			if (photo_changed == 1 && photo.length() > 0) // Photo
															// changed.
															// Need
															// to
															// save
															// image
			{
				photo_path = ApiGlobal.saveUserPhoto(photo);
			}

			if (carimg_changed == 1 && carimg.length() > 0) // Car
															// image
															// changed.
															// Need
															// to
															// save
															// image
			{
				carimg_path = ApiGlobal.saveCarImage(carimg);
			}

			szQuery = "UPDATE user SET " + "phone=\"" + mobile + "\","
					+ "nickname=\"" + szNickName + "\"," + "sex=" + sex;

			if (!szCurBirth.equals(""))
				szQuery += ", birthday=\"" + szCurBirth + "\"";

			if (photo_path != null)
				szQuery += ",img=\"" + photo_path + "\"";

			szQuery += " WHERE id = " + userinfo.id;

			if (stmt.executeUpdate(szQuery) == 1) {
				if (carimg_path != null) {
					SVCUserCar user_car_info = ApiGlobal
							.getUserCarInfoFromUserID(dbConn, userinfo.id);
					if (user_car_info == null) {
						szQuery = "INSERT INTO user_car (userid, vin, brand, style, color, eno, car_img, plate_num, plate_num_last3, is_oper_vehicle, deleted) VALUES ("
								+ userinfo.id
								+ ", '', '', '', '', '', \""
								+ carimg_path + "\", '', '', 1, 0)";
					} else {
						szQuery = "UPDATE user_car set car_img = \""
								+ carimg_path
								+ "\" WHERE deleted = 0 AND userid = "
								+ userinfo.id;
					}

					stmt.executeUpdate(szQuery);
				}

				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;

				dbConn.commit();
			} else {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				dbConn.rollback();
			}

		} catch (Exception ex) {
			try {
				dbConn.rollback();
			} catch (Exception e) {
				e.printStackTrace();
			}

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

	public SVCResult changePassword(String source, long userid, String oldpwd,
			String newpwd, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + oldpwd + "," + newpwd + "," + devtoken);

		if (source.equals("") || userid < 0 || oldpwd.equals("")
				|| newpwd.equals("") || devtoken.equals("")) {
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
			if (!userinfo.password.equals(ApiGlobal.MD5Hash(oldpwd))) // Original
																		// password
																		// wrong
			{
				result.retcode = ConstMgr.ErrCode_OldPwdWrong;
				result.retmsg = ConstMgr.ErrMsg_OldPwdWrong;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			

			stmt = dbConn.createStatement();
			szQuery = "UPDATE user SET password = \""
					+ ApiGlobal.MD5Hash(newpwd)
					+ "\" WHERE deleted = 0 AND id = " + userinfo.id;

			if (stmt.executeUpdate(szQuery) == 1) {
				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
			} else {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
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

	public SVCResult verifyPersonInfo(String source, long userid,
			String idforeimage, String idbackimage, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + ",idforeimage,idbackimage," + devtoken);

		if (source.equals("") || userid < 0 || idforeimage.equals("")
				|| idbackimage.equals("") || devtoken.equals("")) {
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
			if (userinfo.person_verified != 0) // This user is
												// already submitted
												// verify info or
												// reviewed
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_AlreadyPersonVerify;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			

			String szIDForePath = null, szIDBackPath = null;

			szIDForePath = ApiGlobal.saveIdCardImage(idforeimage, true);
			szIDBackPath = ApiGlobal.saveIdCardImage(idbackimage, false);

			if (szIDForePath == null || szIDBackPath == null) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
			} else {
				stmt = dbConn.createStatement();
				szQuery = "UPDATE user SET id_card1 = \"" + szIDForePath
						+ "\", id_card2 = \"" + szIDBackPath
						+ "\", person_verified=2 WHERE deleted = 0 AND id = "
						+ userinfo.id;
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
 * 1,获得用户余额
 * 2,将充值的前balance插入到ts表中
 * 3,更新用户表的最新余额。
 * @param source
 * @param userid
 * @param balance
 * @param devtoken
 * @param charge_source
 * @return
 */
	public SVCResult charge(String source, long userid, double balance,
			String devtoken, int charge_source) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + balance + "," + devtoken + "," + charge_source);

		if (source.equals("") || userid < 0 || balance < 0 || charge_source < 0
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
			dbConn.setAutoCommit(false);

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

			//1,获得用户余额
			stmt = dbConn.createStatement();
			szQuery = "SELECT * FROM ts WHERE deleted = 0 AND id = "
					+ userinfo.balance_ts;

			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) // No ts record. This is abnormal situation
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			// Add new ts record and update user table
			SVCTs tsinfo = SVCTs.decodeFromResultSet(rs);

			rs.close();

			// String szApp = "";
			String szApp_Chn = "";
			if (source.equals(ConstMgr.SRC_MAINAPP)) {
				szApp_Chn = ConstMgr.SRC_CHN_MAINAPP;
			} else if (source.equals(ConstMgr.SRC_PINCHEAPP)) {
				szApp_Chn = ConstMgr.SRC_CHN_PINCHEAPP;
			} else {
				szApp_Chn = ConstMgr.STR_OTHERS;
			}

			String szCurTime = ApiGlobal.Date2String(new Date(), true);
			String szCurRetTime = ApiGlobal.Date2StringWithoutSeconds(new Date());
			String tsType = ApiGlobal.getCommentFromTxCode(dbConn, ConstMgr.txcode_userCharge);//获取在该交易码的说明，在手机端显示的说明，比正常的短一些
			//2,将充值的前balance插入到ts表中
			double cur_balance = tsinfo.balance + balance;
			szQuery = "INSERT INTO ts (" + "oper," + "ts_way," + "balance,"
					+ "ts_date," + "userid," + "groupid," + "unitid,"
					+ "remark," + "account," + "account_type," + "application,"
					+ "ts_type," + "sum," + "deleted" + ") VALUES (" + "2, "
					+ charge_source + ", " + cur_balance + ", \""
					+ szCurTime + "\", "
					+ userinfo.id + ", 0, 0, \""
					+ tsType
					+ "\", '', 1, \"" + szApp_Chn + "\", \""
					+ ConstMgr.txcode_userCharge + "\", " + balance + ", 0)";

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
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

			//3,更新用户表的最新余额。
			long new_ts_id = genKeys.getLong(1);
			szQuery = "UPDATE user SET balance_ts = " + new_ts_id
					+ " WHERE deleted = 0 AND id = " + userinfo.id;
			if (stmt.executeUpdate(szQuery) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}


			szQuery = "INSERT INTO req_client_account ("
					+ "req_num,"
					+ "account,"
					+ "account_type,"
					+ "user_id,"
					+ "sum,"
					+ "oper,"
					+ "req_date, "
					+ "audit_date, "
					+ "status,"
					+ "oper_type,"
					+ "channel,"
					+ "req_cause,"
					+ "order_cs_id,"
					+ "reject_cause,"
					+ "ts_id,"
					+ "req_user,"
					+ "auditor,"
					+ "req_source,"
					+ "deleted"

					+ ") VALUES ("

					+ "\"" + ApiGlobal.generateReqOperNum() + "\","		// req_num
					+ "'',"												// account
					+ "1,"												// account_type
					+ userinfo.id + ","									// user_id
					+ balance + ","										// sum
					+ "2,"												// oper
					+ "\"" + szCurTime + "\","							// req_date
					+ "\"" + szCurTime + "\","							// audit_date
					+ "2,"												// status
					+ "2,"												// oper_type
					+ "'',"												// channel
					+ "'',"												// req_cause
					+ "0,"												// order_cs_id
					+ "'',"												// reject_cause
					+ new_ts_id + ","									// ts_id
					+ "0,"												// req_user
					+ "0,"												// auditor
					+ "1,"												// req_source
					+ "0)";												// deleted
			if (stmt.executeUpdate(szQuery) != 1)
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception + "(Insert req_client_account)";
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}


			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

			JSONObject retdata = new JSONObject();
			retdata.put("curbalance", cur_balance);

			JSONObject newlog = new JSONObject();
			newlog.put("uid", new_ts_id);
			newlog.put("tsid", String.format("%08d", new_ts_id));
			newlog.put("operbalance", String.format("%.2f", balance));
			newlog.put("source", tsType);
			newlog.put("remainbalance", String.format("%.2f", cur_balance));
			newlog.put("tstime", szCurRetTime);

			retdata.put("newlog", newlog);

			result.retdata = retdata;
			dbConn.commit();
		} catch (Exception ex) {
			try {
				dbConn.rollback();
			} catch (Exception e) {
				e.printStackTrace();
			}

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
 * 该方法是百度服务器已经充值成功了，然后调用此方法来充值绿点
 *  
1,如果百度返回的请求已经处理了，则直接返回。原因是百度服务器会在充值结束后，调用此方法3到5次，来规避网络信号不好的情况，所以程序必须做除重处理
2,获取该用户最新的余额
3,将新充值的钱插入到ts表中
4,更新user表的最新余额
5,将充值的记录插入到req_client_account表中，这个是为了后台管理服务的，只是提供一个充值的查询功能
6,向charge_logs表插入充值记录，charge_logs表是为了除重而设计的，因为百度服务器在充值成功之后，会反复多次调用这个函数造成反复充值好几次

 * @param order_no
 * @param szUserID
 * @param total_amount
 * @return
 */

	public SVCResult chargeWithBaidu(String order_no, String szUserID, double total_amount)
	{
		ApiGlobal.logMessage(order_no + "," + szUserID + "," + total_amount);

		SVCResult result = new SVCResult();

		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		long userid = 0;

		try {
			// Create db connection entity.
			dbConn = DBManager.getDBConnection();
			dbConn.setAutoCommit(false);

			userid = Long.parseLong(szUserID);

			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			stmt = dbConn.createStatement();
			//1,如果百度返回的请求已经处理了，则直接返回。原因是百度服务器会在充值结束后，调用此方法3到5次，来规避网络信号不好的情况，所以程序必须做除重处理
			szQuery = "SELECT COUNT(*) AS cnt FROM charge_logs WHERE order_no=\"" + order_no + "\"";
			rs = stmt.executeQuery(szQuery);
			rs.next();
			if (rs.getInt("cnt") > 0)
			{
				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			rs.close();
			//2,获取该用户最新的余额
			szQuery = "SELECT * FROM ts WHERE deleted = 0 AND id = "
					+ userinfo.balance_ts;

			rs = stmt.executeQuery(szQuery);
			if (!rs.next()) // No ts record. This is abnormal situation
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			// Add new ts record and update user table
			SVCTs tsinfo = SVCTs.decodeFromResultSet(rs);

			rs.close();

			// String szApp = "";
			String szApp_Chn = ConstMgr.SRC_CHN_PINCHEAPP;

			String szCurTime = ApiGlobal.Date2String(new Date(), true);
			String szCurRetTime = ApiGlobal.Date2StringWithoutSeconds(new Date());
			String tsType = ApiGlobal.getCommentFromTxCode(dbConn, ConstMgr.txcode_userCharge);
			//3,将新充值的钱插入到ts表中
			double cur_balance = tsinfo.balance + total_amount;
			szQuery = "INSERT INTO ts (" + "oper," + "ts_way," + "balance,"
					+ "ts_date," + "userid," + "groupid," + "unitid,"
					+ "remark," + "account," + "account_type," + "application,"
					+ "ts_type," + "sum," + "deleted" + ") VALUES (" + "2, "
					+ 3 + ", "														// Charge source. Baidu QianBao is 3
					+ cur_balance + ", \""
					+ szCurTime + "\", "
					+ userinfo.id + ", 0, 0, \""
					+ tsType
					+ "\", '', 1, \"" + szApp_Chn + "\", \""
					+ ConstMgr.txcode_userCharge + "\", " + total_amount + ", 0)";

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
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


			long new_ts_id = genKeys.getLong(1);
			//4,更新user表的最新余额
			szQuery = "UPDATE user SET balance_ts = " + new_ts_id
					+ " WHERE deleted = 0 AND id = " + userinfo.id;
			if (stmt.executeUpdate(szQuery) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}


			//5,将充值的记录插入到req_client_account表中，这个是为了后台管理服务的，只是提供一个充值的查询功能
			szQuery = "INSERT INTO req_client_account ("
					+ "req_num,"
					+ "account,"
					+ "account_type,"
					+ "user_id,"
					+ "sum,"
					+ "oper,"
					+ "req_date,"
					+ "audit_date,"
					+ "status,"
					+ "oper_type,"
					+ "channel,"
					+ "req_cause,"
					+ "order_cs_id,"
					+ "reject_cause,"
					+ "ts_id,"
					+ "req_user,"
					+ "auditor,"
					+ "req_source,"
					+ "deleted"

					+ ") VALUES ("

					+ "\"" + ApiGlobal.generateReqOperNum() + "\","		// req_num
					+ "'',"												// account
					+ "1,"												// account_type
					+ userinfo.id + ","									// user_id
					+ total_amount + ","								// sum
					+ "2,"												// oper
					+ "\"" + szCurTime + "\","							// req_date
					+ "\"" + szCurTime + "\","							// audit_date
					+ "2,"												// status
					+ "2,"												// oper_type
					+ "'',"												// channel
					+ "'',"												// req_cause
					+ "0,"												// order_cs_id
					+ "'',"												// reject_cause
					+ new_ts_id + ","									// ts_id
					+ "0,"												// req_user
					+ "0,"												// auditor
					+ "1,"												// req_source
					+ "0)";												// deleted
			if (stmt.executeUpdate(szQuery) != 1)
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception + "(Insert req_client_account)";
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}



			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			//6,向charge_logs表插入充值记录，charge_logs表是为了除重而设计的，因为百度服务器在充值成功之后，会反复多次调用这个函数造成反复充值好几次
			szQuery = "INSERT INTO charge_logs ("
					+ "		order_no,"
					+ "		log_time,"
					+ "		userid"

					+ ") VALUES ("

					+ "\"" + order_no + "\","
					+ "\"" + ApiGlobal.Date2String(new Date(), true) + "\","
					+ userid + ")";
			stmt.executeUpdate(szQuery);

			JSONObject retdata = new JSONObject();
			retdata.put("curbalance", cur_balance);

			JSONObject newlog = new JSONObject();
			newlog.put("uid", new_ts_id);
			newlog.put("tsid", String.format("%08d", new_ts_id));
			newlog.put("operbalance", String.format("%.2f", total_amount));
			newlog.put("source", tsType);
			newlog.put("remainbalance", String.format("%.2f", cur_balance));
			newlog.put("tstime", szCurRetTime);

			retdata.put("newlog", newlog);

			result.retdata = retdata;
			dbConn.commit();
		} catch (Exception ex) {
			try {
				dbConn.rollback();
			} catch (Exception e) {
				e.printStackTrace();
			}

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
 * 1,查询user表查出该用户的用户名，银行卡号，银行名称，开户行
 * @param source
 * @param userid
 * @param devtoken
 * @return
 */
	public SVCResult accountInfo(String source, long userid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + devtoken);

		if (source.equals("") || userid < 0 || devtoken.equals("")) {
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
		try {
			dbConn = DBManager.getDBConnection();

			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoByUserID(dbConn, userid);
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
			if (userinfo.person_verified != 1) // This user is not
												// reviewed user
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotVerifiedPerson;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			JSONObject retdata = new JSONObject();
			//1,查询user表查出该用户的用户名，银行卡号，银行名称，开户行
			retdata.put("realname", userinfo.username);
			retdata.put("bankcard", userinfo.bankcard);
			retdata.put("bankname", userinfo.bankname);
			retdata.put("subbranch", userinfo.subbranch);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = retdata;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg = ex.getMessage();
		} finally {
			if (dbConn != null)
			{
				try { dbConn.close(); } catch (Exception ex) { ex.printStackTrace(); }
			}
		}

		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}
/**
 * 1,获得用户的当前余额
 * 2,校验用户的提现的用户名，必须是注册的时候留的，当当前余额小于所提款的时候不能提现
 * 3,冻结当前用户的一笔款项，(1)向ts表增加一条扣款记录(2)同时向freeze_points表增加一条冻结记录
 * 4,向req_client_account增加一条请求提现记录
 * @param source
 * @param userid
 * @param realname
 * @param accountname
 * @param balance
 * @param password
 * @param devtoken
 * @return
 */
	public SVCResult withdraw(String source, long userid, String realname,
			String accountname, double balance, String password, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + realname + "," + accountname + "," + balance + "," + password + "," + devtoken);

		if (source.equals("") || realname.equals("") || balance < 0
				|| accountname.equals("") || password.equals("") || userid < 0
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
			//1,获得用户的当前余额
			double latest_balance = ApiGlobal.getUserBalance(dbConn, userid);
			//2,校验用户的提现的用户名，必须是注册的时候留的，当当前余额小于所提款的时候不能提现
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
			} else if (!userinfo.username.equals("")
					&& !userinfo.username.equals(realname)) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_RealNameNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (!userinfo.password.equals(ApiGlobal.MD5Hash(password))) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_LoginPwdNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (userinfo.username.equals("")
					|| userinfo.bankcard.equals("")
					|| userinfo.bankname.equals("")
					|| userinfo.subbranch.equals("")) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_BankCardNotBound;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (latest_balance < balance) {
				result.retcode = ConstMgr.ErrCode_NotEnoughBalance;
				result.retmsg = ConstMgr.ErrMsg_NotEnoughBalance;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			stmt = dbConn.createStatement();

			double cur_balance = latest_balance - balance;

			////////////////////////////////////////////////////////////////////////////////////
			// Insert ts log
			/////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////
			String szCurTime = ApiGlobal.Date2String(new Date(), true);
			String szCurRetTime = ApiGlobal.Date2StringWithoutSeconds(new Date());
			//3,冻结当前用户的一笔款项，(1)向ts表增加一条扣款记录(2)同时向freeze_points表增加一条冻结记录
			long new_ts_id = ApiGlobal.freezePointsWithType(dbConn, source, userid, 0,
					0, 0, balance, ConstMgr.txcode_userWithdraw);
			//4,向req_client_account增加一条请求提现记录
			// Insert log into req_client_account table
			szQuery = "INSERT INTO req_client_account (req_num, account,"
					+ "account_type, user_id, sum, oper, req_date, "
					+ "status, oper_type, channel, req_cause, order_cs_id,"
					+ "reject_cause, ts_id, req_user, auditor, req_source, deleted) VALUES (\""
					+ ApiGlobal.generateReqOperNum() + "\", \"" + accountname
					+ "\", 1, " + userinfo.id + "," + balance + ", 1, \""
					+ szCurTime + "\", 1, 1, \""
					+ userinfo.bankname + "\", '', 0, '', " + new_ts_id
					+ ", 0, 0, 1, 0)";

			if (stmt.executeUpdate(szQuery) != 1) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
			}

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

			JSONObject retdata = new JSONObject();
			retdata.put("curbalance", cur_balance);

			JSONObject newlog = new JSONObject();
			newlog.put("uid", new_ts_id);
			newlog.put("tsid", String.format("%08d", new_ts_id));
			newlog.put("operbalance", String.format("%.2f", balance));
			newlog.put("source", "提现");
			newlog.put("remainbalance", String.format("%.2f", cur_balance));
			newlog.put("tstime", szCurRetTime);

			retdata.put("newlog", newlog);

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

	public SVCResult bindBankCard(String source, long userid, String bankcard,
			String bankname, String subbranch, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + bankcard + "," + bankname + "," + subbranch + "," + devtoken);

		if (source.equals("") || bankcard.equals("") || bankname.equals("")
				|| subbranch.equals("") || userid < 0 || devtoken.equals("")) {
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
			}
			if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			

			stmt = dbConn.createStatement();
			szQuery = "UPDATE user SET bankcard = \"" + bankcard
					+ "\", bankname = \"" + bankname + "\", subbranch = \""
					+ subbranch + "\" WHERE id = " + userinfo.id;
			if (stmt.executeUpdate(szQuery) == 1) {
				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
			} else {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
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

	public SVCResult releaseBankCard(String source, long userid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + devtoken);

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
			szQuery = "UPDATE user SET bankcard='', bankname='', subbranch='' WHERE id="
					+ userinfo.id;
			if (stmt.executeUpdate(szQuery) == 1) {
				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
			} else {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
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
 * 这个是根据devtoken来获取用户的信息，不过需要把这里涉及的user_login表去掉。
 * @param source
 * @param devtoken
 * @param pushnotif_token
 * @return
 */
	public SVCResult loginInfoFromDevtoken(String source, String devtoken, String pushnotif_token) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + devtoken + "," + pushnotif_token);

		if (source.equals("") || devtoken.equals("")) {
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
			JSONObject retdata = new JSONObject();

			// Create database entity
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user WHERE deleted = 0 AND device_token = \""
					+ devtoken + "\" AND loggedout=0";
			rs = stmt.executeQuery(szQuery);

			if (!rs.next()) {
				retdata.put("userid", 0);
				retdata.put("photo", "");
				retdata.put("mobile", "");
				retdata.put("nickname", "");
				retdata.put("sex", 0);
				retdata.put("birthday", "");
				retdata.put("person_verified", 0);
				retdata.put("driver_verified", 0);
				retdata.put("carimg", "");
				retdata.put("invitecode", "");
				retdata.put("baiduak", "");

				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_Normal;
				result.retdata = retdata;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			SVCUser userinfo = SVCUser.decodeFromResultSet(rs);
			rs.close();




			SVCUserLogin user_login = null;
			szQuery = "SELECT * FROM user_login WHERE deleted = 0 AND devtoken = \""
					+ devtoken + "\"";
			rs = stmt.executeQuery(szQuery);

			if (rs.next())
				user_login = SVCUserLogin.decodeFromResultSet(rs);
			rs.close();

			if (user_login != null) {
				szQuery = "UPDATE user_login "
						+ "SET loggedout=1 "
						+ "WHERE "
						+ "		devtoken=\"" + devtoken + "\" AND "
						+ "		id<>" + user_login.id;
				stmt.executeUpdate(szQuery);


				szQuery = "UPDATE "
						+ "		user_login "
						+ "SET "
						+ "		pushnotif_token=\"" + pushnotif_token + "\","
						+ "		loggedout=0,"
						+ "		login_time=\"" + ApiGlobal.Date2String(new Date(), true) + "\" "
						+ "WHERE "
						+ "		id=" + user_login.id;
				stmt.executeUpdate(szQuery);
			}




			szQuery = "SELECT * FROM user_car WHERE deleted = 0 AND userid = " + userinfo.id;
			rs = stmt.executeQuery(szQuery);

			SVCUserCar user_car = null;
			if (rs.next())
				user_car = SVCUserCar.decodeFromResultSet(rs);

			retdata.put("userid", userinfo.id);
			retdata.put("photo", ApiGlobal.getAbsoluteURL(userinfo.img));
			retdata.put("mobile", userinfo.phone);
			retdata.put("nickname", userinfo.nickname);
			retdata.put("sex", userinfo.sex);
			retdata.put(
					"birthday",
					userinfo.birthday == null ? "" : ApiGlobal.Date2String(
							userinfo.birthday, true));
			retdata.put("person_verified", userinfo.person_verified);
			retdata.put("driver_verified", userinfo.driver_verified);
			retdata.put(
					"carimg",
					user_car != null ? ApiGlobal
							.getAbsoluteURL(user_car.car_img) : "");
			retdata.put("invitecode", userinfo.invitecode_self);
			retdata.put("baiduak", ApiGlobal.getBaiduAKFromUserID(dbConn, userinfo.id));

			JSONObject exec_info = ApiGlobal.getExecuteOrderInfo(dbConn,userinfo.id);
			retdata.put("exec_info", exec_info);

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
 * 获得用户最新金额
 * @param source
 * @param userid
 * @param devtoken
 * @return
 */
	public SVCResult latestBalance(String source, long userid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + devtoken);

		if (source.equals("") || devtoken.equals("")) {
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
			}
			

			stmt = dbConn.createStatement();
			szQuery = "SELECT * FROM ts WHERE deleted = 0 AND id="
					+ userinfo.balance_ts;
			rs = stmt.executeQuery(szQuery);

			if (!rs.next()) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			SVCTs tsinfo = SVCTs.decodeFromResultSet(rs);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("money", tsinfo.balance);

			result.retdata = jsonObj;

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

	public SVCResult verifyDriver(String source, long userid,
			String driver_licence_fore, String driver_licence_back,
			String brand, String type, String color, String carimg,
			String driving_licence_fore, String driving_licence_back,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + ",driver_licence_fore,driver_licence_back," + brand + "," + type + "," + color
				+ ",carimg,driving_licence_fore,driving_licence_back," + devtoken);

		if (source.equals("") || userid < 0 || driver_licence_fore.equals("")
				|| driver_licence_back.equals("") || brand.equals("")
				|| type.equals("") || color.equals("")
				|| driving_licence_fore.equals("")
				|| driving_licence_back.equals("") || devtoken.equals("")) {
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
			// else if (userinfo.person_verified != 1)
			// {
			// result.retcode = ConstMgr.ErrCode_Normal;
			// result.retmsg = ConstMgr.ErrMsg_NotVerifiedPerson;
			// }
			if (userinfo.driver_verified != 0) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_AlreadyDriverVerify;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			

			String jsz_forepath = null; // Driver license fore image
										// path
			String jsz_backpath = null; // Driver license back image
										// path
			String cz_path = null; // Car image path
			String xsz_forepath = null; // Driving license fore image
										// path
			String xsz_backpath = null; // Driving license back image
										// path

			jsz_forepath = ApiGlobal.saveDriverLicenceImage(
					driver_licence_fore, true);
			jsz_backpath = ApiGlobal.saveDriverLicenceImage(
					driver_licence_back, false);

			xsz_forepath = ApiGlobal.saveDrivingLicenceImage(
					driving_licence_fore, true);
			xsz_backpath = ApiGlobal.saveDrivingLicenceImage(
					driving_licence_back, false);

			if (!carimg.equals(""))
				cz_path = ApiGlobal.saveCarImage(carimg);

			if (jsz_forepath == null || jsz_backpath == null
					|| xsz_forepath == null || xsz_backpath == null) {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			dbConn.setAutoCommit(false);
			stmt = dbConn.createStatement();
			// 1，设置user表，车辆信息
			szQuery = "UPDATE user SET " + "driver_license1=\"" + jsz_forepath
					+ "\"," + "driver_license2=\"" + jsz_backpath + "\","
					+ "driving_license1=\"" + xsz_forepath + "\","
					+ "driving_license2=\"" + xsz_backpath + "\","
					+ "driver_verified=2" + " WHERE deleted = 0 AND id="
					+ userid;
			if (stmt.executeUpdate(szQuery) == 0)
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}



			long car_type_id = 0;
			szQuery = "SELECT "
					+ "		id AS car_type_id "
					+ "FROM "
					+ "		car_type "
					+ "WHERE "
					+ "		brand=\"" + brand + "\" AND "
					+ "		car_style=\"" + type + "\"";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
			{
				car_type_id = rs.getLong("car_type_id");
			}
			rs.close();



			szQuery = "SELECT * FROM user_car WHERE deleted = 0 AND userid="
					+ userid;
			rs = stmt.executeQuery(szQuery);
			// 2,设置user_car表，车辆信息
			if (rs.next()) {
				szQuery = "UPDATE user_car SET brand=\"" + brand
						+ "\",style=\"" + type + "\",color=\"" + color + "\","
						+ "car_type_id=" + car_type_id;
				if (cz_path != null && !cz_path.equals(""))
					szQuery += ",car_img=\"" + cz_path + "\"";
				szQuery += " WHERE deleted=0 AND userid=" + userid;
			} else {
				szQuery = "INSERT INTO user_car (userid, brand,style,color,car_img,is_oper_vehicle,car_type_id) VALUES ("
						+ userid
						+ ",\""
						+ brand
						+ "\", \""
						+ type
						+ "\", \""
						+ color
						+ "\", \""
						+ (cz_path == null ? "" : cz_path)
						+ "\", 1,"
						+ car_type_id + ")";
			}

			if (stmt.executeUpdate(szQuery) == 1) {
				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
			} else {
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
			}
			dbConn.commit();
		} catch (Exception ex) {
			try{
				dbConn.rollback();
			}catch(Exception e){
				e.printStackTrace();
			}
			
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

	public SVCResult nearbyDrivers(String source, long userid, double lat,
			double lng, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + lat + "," + lng + "," + devtoken);

		if (source.equals("") || devtoken.equals("")) {
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
			JSONArray arrOrders = new JSONArray();

			

			stmt = dbConn.createStatement();

			String hb_validperiod = ApiGlobal
					.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_HEARTBEAT_VALIDDPERIOD);
			String nearby_limit = ApiGlobal
					.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_NEARBYDRVLIMIT);
			//1,查询user_online表，查询出当前在线车主的经纬度，并排序，为online_info
			//2,查询出的在线车主与user表连接，选择与乘客距离小于nearby_limit的车主
			//3,这个函数有优化的空间，第一步骤查询当前在线车主的时候，需要查询指定“坐标格”以内的车主
			szQuery = "SELECT " + "		user.*," + "		online_info.lat, "
					+ "		online_info.lng " + "FROM user "
					+ "INNER JOIN (SELECT " + "					user_online.userid,"
					+ "					user_online.lat," + "					user_online.lng "
					+ "				FROM user_online " + "				WHERE "
					+ "					deleted = 0 AND "
					+ "					timediff_min(last_heartbeat_time, NOW())<="
					+ hb_validperiod
					+ "				ORDER BY last_heartbeat_time DESC) AS online_info "
					+ "ON user.id = online_info.userid " + "WHERE "
					+ "		user.deleted=0 AND " + "		user.driver_verified=1 AND "
					+ "		user.id <> " + userid + " AND "
					+ "		calc_distance(online_info.lat, online_info.lng,"
					+ lat + "," + lng + ")<=" + nearby_limit;

			rs = stmt.executeQuery(szQuery);

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
 * 1,退出登录后，将user表的device_token字段设置为空
 * @param source
 * @param userid
 * @param devtoken
 * @return
 */

	public SVCResult logoutUser(String source, long userid, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + devtoken);

		if (source.equals("") || devtoken.equals("") || userid < 0) {
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
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			if (!userinfo.device_token.equals(devtoken))
			{
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			//1,退出登录后，将user表的device_token字段设置为空
			szQuery = "UPDATE user SET device_token='' WHERE id=" + userinfo.id;
			if (stmt.executeUpdate(szQuery) == 1)
			{
				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
			}
			else
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception;
			}

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg = ex.getMessage();
		} finally {
			try {
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
 * 上传用户头像的接口
 * 1,保存用户头像到本地
 * 2,user表中更改用户头像的地址
 * @param source
 * @param userid
 * @param userphoto
 * @param devtoken
 * @return
 */
	public SVCResult changeUserPhoto(String source, long userid, String userphoto, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + ",userphoto" + "," + devtoken);

		if (source.equals("") || devtoken.equals("") || userid < 0) {
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
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			if (!userinfo.device_token.equals(devtoken))
			{
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			//1,保存用户头像到本地
			String photo_path = ApiGlobal.saveUserPhoto(userphoto);

			if (photo_path == null)
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception + "(Save photo)";
			}
			else
			{//2,更改用户头像的地址
				szQuery = "UPDATE "
						+ "		user "
						+ "SET "
						+ "		img=\"" + photo_path + "\"";
				szQuery += " WHERE id = " + userinfo.id;
	
				if (stmt.executeUpdate(szQuery) == 1) {
					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;

					JSONObject retdata = new JSONObject();
					retdata.put("photo_url", ApiGlobal.getAbsoluteURL(photo_path));
					result.retdata = retdata;

				} else {
					result.retcode = ConstMgr.ErrCode_Exception;
					result.retmsg = ConstMgr.ErrMsg_Exception;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg = ex.getMessage();
		} finally {
			try {
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
 * 上传爱车的照片
 * 
 * 1,将车照片保存到本地
 * 2,更新user_car表相应记录的car_img路径
 * @param source
 * @param userid
 * @param carimg
 * @param devtoken
 * @return
 */

	public SVCResult changeCarImage(String source, long userid, String carimg, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + ",carimg," + devtoken);

		if (source.equals("") || devtoken.equals("") || userid < 0) {
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
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();
			stmt = dbConn.createStatement();

			// Authenticate user
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
			if (userinfo == null) // No user information
			{
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_NotRegisteredUser;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}

			if (!userinfo.device_token.equals(devtoken))
			{
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			//1,将车照片保存到本地
			String photo_path = ApiGlobal.saveCarImage(carimg);

			if (photo_path == null)
			{
				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ConstMgr.ErrMsg_Exception + "(Save photo)";
			}
			else
			{
				SVCUserCar user_car_info = ApiGlobal.getUserCarInfoFromUserID(dbConn, userinfo.id);
				//2,更新user_car表相应记录的car_img路径
				if (user_car_info == null) {
					szQuery = "INSERT INTO user_car (userid, vin, brand, style, color, eno, car_img, plate_num, plate_num_last3, is_oper_vehicle, deleted) VALUES ("
							+ userinfo.id
							+ ", '', '', '', '', '', \""
							+ photo_path + "\", '', '', 1, 0)";
				} else {
					szQuery = "UPDATE user_car set car_img = \""
							+ photo_path
							+ "\" WHERE deleted = 0 AND userid = "
							+ userinfo.id;
				}

				stmt.executeUpdate(szQuery);

				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;

				JSONObject retdata = new JSONObject();
				retdata.put("photo_url", ApiGlobal.getAbsoluteURL(photo_path));
				result.retdata = retdata;

			}
		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg = ex.getMessage();
		} finally {
			try {
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
