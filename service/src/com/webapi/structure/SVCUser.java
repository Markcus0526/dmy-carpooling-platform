package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCUser
{
	public long id							= 0;
	public String usercode					= "";
	public String username					= "";
	public String nickname					= "";
	public String password					= "";
	public String phone						= "";
	public long balance_ts					= 0;
	public String invitecode_self			= "";
	public String invitecode_regist			= "";
	public Date reg_date					= null;
	public Date last_login_time				= null;
	public int app_register					= 0;
	public String bankcard					= "";
	public String bankname					= "";
	public String subbranch					= "";
	public int sex							= 0;
	public Date birthday					= null;
	public String id_card1					= "";
	public String driver_license1			= "";
	public String driving_license1			= "";
	public String id_card2					= "";
	public String driver_license2			= "";
	public String driving_license2			= "";
	public String img						= "";
	public int person_verified				= 0;
	public int driver_verified				= 0;
	public int provide_profitsharing_way	= 3;
	public int provide_profitsharing_time_as_passenger		= 0;
	public int provide_profitsharing_count_as_passenger		= 0;
	public int provide_profitsharing_time_as_driver			= 0;
	public int provide_profitsharing_count_as_driver		= 0;
	public String city_register				= "";
	public String city_cur					= "";
	public double ratio_as_passenger		= 0;
	public double integer_as_passenger		= 0;
	public int activeway_as_passenger		= 0;
	public double ratio_as_driver			= 0;
	public double integer_as_driver			= 0;
	public int activeway_as_driver			= 0;
	public int inviter_type					= 0;
	public int is_platform					= 0;
	public double ratio_as_passenger_self		= 0;
	public double integer_as_passenger_self		= 0;
	public int active_as_passenger_self			= 0;
	public double ratio_as_driver_self			= 0;
	public double integer_as_driver_self		= 0;
	public int active_as_driver_self			= 0;
	public int limit_way						= 3;
	public int limit_month_as_passenger_self	= 0;
	public int limit_month_as_driver_self		= 0;
	public int limit_count_as_passenger_self	= 0;
	public int limit_count_as_driver_self		= 0;
	public int phone_system						= 0;
	public String device_token					= "";
	public String nation						= "";
	public String id_card_num					= "";
	public String address						= "";
	public String drivinglicence_num			= "";
	public String drlicence_ti					= "";
	public String remark						= "";
	public int got_reg_coupon					= 0;
	public int got_verify_coupon				= 0;
	public int got_order_coupon					= 0;
	public int got_afterreg_coupon				= 0;
	public int loggedout						= 0;
	public int deleted							= 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonResult = new JSONObject();

		try { jsonResult.put("id", id); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("usercode", usercode); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("username", username); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("nickname", nickname); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("password", password); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("phone", phone); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("balance_ts", balance_ts); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("invitecode_self", invitecode_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("invitecode_regist", invitecode_regist); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("reg_date", reg_date); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("last_login_time", last_login_time); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("app_register", app_register); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("bankcard", bankcard); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("bankname", bankname); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("subbranch", subbranch); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("sex", sex); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("birthday", ApiGlobal.Date2String(birthday, true)); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("id_card1", id_card1); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("driver_license1", driver_license1); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("driving_license1", driving_license1); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("id_card2", id_card2); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("driver_license2", driver_license2); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("driving_license2", driving_license2); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("img", img); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("person_verified", person_verified); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("driver_verified", driver_verified); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("provide_profitsharing_way", provide_profitsharing_way); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("provide_profitsharing_time_as_passenger", provide_profitsharing_time_as_passenger); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("provide_profitsharing_count_as_passenger", provide_profitsharing_count_as_passenger); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("provide_profitsharing_time_as_driver", provide_profitsharing_time_as_driver); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("provide_profitsharing_count_as_driver", provide_profitsharing_count_as_driver); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("city_register", city_register); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("city_cur", city_cur); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("ratio_as_passenger", ratio_as_passenger); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("integer_as_passenger", integer_as_passenger); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("activeway_as_passenger", activeway_as_passenger); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("ratio_as_driver", ratio_as_driver); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("integer_as_driver", integer_as_driver); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("activeway_as_driver", activeway_as_driver); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("inviter_type", inviter_type); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("is_platform", is_platform); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("ratio_as_passenger_self", ratio_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("integer_as_passenger_self", integer_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("active_as_passenger_self", active_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("ratio_as_driver_self", ratio_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("integer_as_driver_self", integer_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("active_as_driver_self", active_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("limit_way", limit_way); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("limit_month_as_passenger_self", limit_month_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("limit_month_as_driver_self", limit_month_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("limit_count_as_passenger_self", limit_count_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("limit_count_as_driver_self", limit_count_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("phone_system", phone_system); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("device_token", device_token); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("nation", nation); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("id_card_num", id_card_num); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("address", address); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("drivinglicence_num", drivinglicence_num); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("drlicence_ti", drlicence_ti); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("got_reg_coupon", remark); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("got_verify_coupon", remark); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("got_order_coupon", remark); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("got_afterreg_coupon", remark); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("loggedout", loggedout); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonResult.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); };

		return jsonResult;
	}


	public static SVCUser decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCUser userinfo = new SVCUser();

		try { userinfo.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.usercode = jsonObj.getString("usercode"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.username = jsonObj.getString("username"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.nickname = jsonObj.getString("nickname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.password = jsonObj.getString("password"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.phone = jsonObj.getString("phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.balance_ts = jsonObj.getLong("balance_ts"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.invitecode_self = jsonObj.getString("invitecode_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.invitecode_regist = jsonObj.getString("invitecode_regist"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.reg_date = ApiGlobal.String2Date(jsonObj.getString("reg_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.last_login_time = ApiGlobal.String2Date(jsonObj.getString("last_login_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.app_register = jsonObj.getInt("app_register"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.bankcard = jsonObj.getString("bankcard"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.bankname = jsonObj.getString("bankname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.subbranch = jsonObj.getString("subbranch"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.sex = jsonObj.getInt("sex"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.birthday = ApiGlobal.String2Date(jsonObj.getString("birthday")); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.id_card1 = jsonObj.getString("id_card1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driver_license1 = jsonObj.getString("driver_license1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driving_license1 = jsonObj.getString("driving_license1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.id_card2 = jsonObj.getString("id_card2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driver_license2 = jsonObj.getString("driver_license2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driving_license2 = jsonObj.getString("driving_license2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.img = jsonObj.getString("img"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.person_verified = jsonObj.getInt("person_verified"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driver_verified = jsonObj.getInt("driver_verified"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_way = jsonObj.getInt("provide_profitsharing_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_time_as_passenger = jsonObj.getInt("provide_profitsharing_time_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_count_as_passenger = jsonObj.getInt("provide_profitsharing_count_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_time_as_driver = jsonObj.getInt("provide_profitsharing_time_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_count_as_driver = jsonObj.getInt("provide_profitsharing_count_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.city_register = jsonObj.getString("city_register"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.city_cur = jsonObj.getString("city_cur"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.ratio_as_passenger = jsonObj.getDouble("ratio_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.integer_as_passenger = jsonObj.getDouble("integer_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.activeway_as_passenger = jsonObj.getInt("activeway_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.ratio_as_driver = jsonObj.getDouble("ratio_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.integer_as_driver = jsonObj.getDouble("integer_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.activeway_as_driver = jsonObj.getInt("activeway_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.inviter_type = jsonObj.getInt("inviter_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.is_platform = jsonObj.getInt("is_platform"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.ratio_as_passenger_self = jsonObj.getDouble("ratio_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.integer_as_passenger_self = jsonObj.getDouble("integer_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.active_as_passenger_self = jsonObj.getInt("active_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.ratio_as_driver_self = jsonObj.getDouble("ratio_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.integer_as_driver_self = jsonObj.getDouble("integer_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.active_as_driver_self = jsonObj.getInt("active_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_way = jsonObj.getInt("limit_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_month_as_passenger_self = jsonObj.getInt("limit_month_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_month_as_driver_self = jsonObj.getInt("limit_month_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_count_as_passenger_self = jsonObj.getInt("limit_count_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_count_as_driver_self = jsonObj.getInt("limit_count_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.phone_system = jsonObj.getInt("phone_system"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.device_token = jsonObj.getString("device_token"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.nation = jsonObj.getString("nation"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.id_card_num = jsonObj.getString("id_card_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.address = jsonObj.getString("address"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.drivinglicence_num = jsonObj.getString("drivinglicence_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.drlicence_ti = jsonObj.getString("drlicence_ti"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.got_reg_coupon = jsonObj.getInt("got_reg_coupon"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.got_verify_coupon = jsonObj.getInt("got_verify_coupon"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.got_order_coupon = jsonObj.getInt("got_order_coupon"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.got_afterreg_coupon = jsonObj.getInt("got_afterreg_coupon"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.loggedout = jsonObj.getInt("loggedout"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return userinfo;
	}


	public static SVCUser decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCUser userinfo = new SVCUser();

		try { userinfo.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.usercode = resultSet.getString("usercode"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.username = resultSet.getString("username"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.nickname = resultSet.getString("nickname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.password = resultSet.getString("password"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.phone = resultSet.getString("phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.balance_ts = resultSet.getLong("balance_ts"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.invitecode_self = resultSet.getString("invitecode_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.invitecode_regist = resultSet.getString("invitecode_regist"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.reg_date = ApiGlobal.String2Date(resultSet.getString("reg_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.last_login_time = ApiGlobal.String2Date(resultSet.getString("last_login_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.app_register = resultSet.getInt("app_register"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.bankcard = resultSet.getString("bankcard"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.bankname = resultSet.getString("bankname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.subbranch = resultSet.getString("subbranch"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.sex = resultSet.getInt("sex"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.birthday = ApiGlobal.String2Date(resultSet.getString("birthday")); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.id_card1 = resultSet.getString("id_card1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driver_license1 = resultSet.getString("driver_license1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driving_license1 = resultSet.getString("driving_license1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.id_card2 = resultSet.getString("id_card2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driver_license2 = resultSet.getString("driver_license2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driving_license2 = resultSet.getString("driving_license2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.img = resultSet.getString("img"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.person_verified = resultSet.getInt("person_verified"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.driver_verified = resultSet.getInt("driver_verified"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_way = resultSet.getInt("provide_profitsharing_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_time_as_passenger = resultSet.getInt("provide_profitsharing_time_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_count_as_passenger = resultSet.getInt("provide_profitsharing_count_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_time_as_driver = resultSet.getInt("provide_profitsharing_time_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.provide_profitsharing_count_as_driver = resultSet.getInt("provide_profitsharing_count_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.city_register = resultSet.getString("city_register"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.city_cur = resultSet.getString("city_cur"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.ratio_as_passenger = resultSet.getDouble("ratio_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.integer_as_passenger = resultSet.getDouble("integer_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.activeway_as_passenger = resultSet.getInt("activeway_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.ratio_as_driver = resultSet.getDouble("ratio_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.integer_as_driver = resultSet.getDouble("integer_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.activeway_as_driver = resultSet.getInt("activeway_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.inviter_type = resultSet.getInt("inviter_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.is_platform = resultSet.getInt("is_platform"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.ratio_as_passenger_self = resultSet.getDouble("ratio_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.integer_as_passenger_self = resultSet.getDouble("integer_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.active_as_passenger_self = resultSet.getInt("active_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.ratio_as_driver_self = resultSet.getDouble("ratio_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.integer_as_driver_self = resultSet.getDouble("integer_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.active_as_driver_self = resultSet.getInt("active_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_way = resultSet.getInt("limit_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_month_as_passenger_self = resultSet.getInt("limit_month_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_month_as_driver_self = resultSet.getInt("limit_month_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_count_as_passenger_self = resultSet.getInt("limit_count_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.limit_count_as_driver_self = resultSet.getInt("limit_count_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.phone_system = resultSet.getInt("phone_system"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.device_token = resultSet.getString("device_token"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.nation = resultSet.getString("nation"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.id_card_num = resultSet.getString("id_card_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.address = resultSet.getString("address"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.drivinglicence_num = resultSet.getString("drivinglicence_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.drlicence_ti = resultSet.getString("drlicence_ti"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.got_reg_coupon = resultSet.getInt("got_reg_coupon"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.got_verify_coupon = resultSet.getInt("got_verify_coupon"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.got_order_coupon = resultSet.getInt("got_order_coupon"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.got_afterreg_coupon = resultSet.getInt("got_afterreg_coupon"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.loggedout = resultSet.getInt("loggedout"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userinfo.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return userinfo;
	}
}
