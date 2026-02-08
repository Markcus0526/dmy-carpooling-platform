package com.webapi.structure;

import java.sql.ResultSet;
import java.sql.Time;

import com.webapi.common.ApiGlobal;

import net.sf.json.JSONObject;

public class SVCCity {
	public long id = 0;
	public String code = "";
	public String prov = "";
	public String name = "";
	public int level = 0;
	public int platform = 0;
	public double ratio = 0;
	public double integer_ = 0;
	public int active = 0;
	public int branch = 0;
	public int turn1_time = 0;
	public int turn2_time = 0;
	public int total_time = 0;
	public int driver_lock_time = 0;
	public double range1 = 0;
	public double range2 = 0;
	public double range3 = 0;
	public int points_per_add_ratio = 0;
	public int points_per_add_integer = 0;
	public int points_per_add_active = 0;
	public int price_limit_ratio = 0;
	public int price_limit_integer = 0;
	public int price_limit_active = 0;
	public int add_price_time1 = 0;
	public int add_price_time2 = 0;
	public int add_price_time3 = 0;
	public int add_price_time4 = 0;
	public int add_price_time5 = 0;
	public int same_price_time1 = 0;
	public int same_price_time2 = 0;
	public int same_price_time3 = 0;
	public int same_price_time4 = 0;
	public int same_price_time5 = 0;
	public double a1 = 0;
	public double a2 = 0;
	public double b1 = 0;
	public double b2 = 0;
	public double b4 = 0;
	public double c1 = 0;
	public double c2 = 0;
	public Time t1 = null;
	public Time t2 = null;
	public double d1 = 0;
	public double d2 = 0;
	public double e1 = 0;
	public double e2 = 0;
	public double e4 = 0;
	public double f1 = 0;
	public double f2 = 0;
	public double g1 = 0;
	public double g2 = 0;
	public double g3 = 0;
	public double g4 = 0;
	public double g5 = 0;
	public String register_syscoupon_id = "";
	public String verified_syscoupon_id = "";
	public int num_order_finished = 0;
	public String finishorders_syscoupon_id = "";
	public int num_registermonth = 0;
	public String registermonth_syscoupon_id = "";
	public int deleted = 0;
	public double insu_fee = 0;//保险费  modify by chuzhiqiang
	


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("code", code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("prov", prov); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("name", name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("level", level); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("platform", platform); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ratio", ratio); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("integer_", integer_); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("active", active); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("branch", branch); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("turn1_time", turn1_time); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("turn2_time", turn2_time); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("total_time", total_time); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driver_lock_time", driver_lock_time); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("range1", range1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("range2", range2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("range3", range3); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("points_per_add_ratio", points_per_add_ratio); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("points_per_add_integer", points_per_add_integer); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("points_per_add_active", points_per_add_active); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("price_limit_ratio", price_limit_ratio); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("price_limit_integer", price_limit_integer); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("price_limit_active", price_limit_active); } catch (Exception ex) { ex.printStackTrace(); }

		try { jsonObj.put("add_price_time1", add_price_time1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("add_price_time2", add_price_time2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("add_price_time3", add_price_time3); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("add_price_time4", add_price_time4); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("add_price_time5", add_price_time5); } catch (Exception ex) { ex.printStackTrace(); }

		try { jsonObj.put("same_price_time1", same_price_time1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("same_price_time2", same_price_time2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("same_price_time3", same_price_time3); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("same_price_time4", same_price_time4); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("same_price_time5", same_price_time5); } catch (Exception ex) { ex.printStackTrace(); }

		try { jsonObj.put("a1", a1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("a2", a2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("b1", b1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("b2", b2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("b4", b4); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("c1", c1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("c2", c2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("t1", ApiGlobal.Time2String(t1)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("t2", ApiGlobal.Time2String(t2)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("d1", d1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("d2", d2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("e1", e1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("e2", e2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("e4", e4); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("f1", f1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("f2", f2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("g1", g1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("g2", g2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("g3", g3); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("g4", g4); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("g5", g5); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("register_syscoupon_id", register_syscoupon_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("verified_syscoupon_id", verified_syscoupon_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("num_order_finished", num_order_finished); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("finishorders_syscoupon_id", finishorders_syscoupon_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("num_registermonth", num_registermonth); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("registermonth_syscoupon_id", registermonth_syscoupon_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("insu_fee", insu_fee); } catch (Exception ex) { ex.printStackTrace(); }//保险费  modify by chuzhiqiang

		return jsonObj;
	}


	public static SVCCity decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCCity city = new SVCCity();

		try { city.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.code = jsonObj.getString("code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.prov = jsonObj.getString("prov"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.level = jsonObj.getInt("level"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.platform = jsonObj.getInt("platform"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.ratio = jsonObj.getDouble("ratio"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.integer_ = jsonObj.getDouble("integer_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.active = jsonObj.getInt("active"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.branch = jsonObj.getInt("branch"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.turn1_time = jsonObj.getInt("turn1_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.turn2_time = jsonObj.getInt("turn2_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.total_time = jsonObj.getInt("total_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.driver_lock_time = jsonObj.getInt("driver_lock_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.range1 = jsonObj.getDouble("range1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.range2 = jsonObj.getDouble("range2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.range3 = jsonObj.getDouble("range3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.points_per_add_ratio = jsonObj.getInt("points_per_add_ratio"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.points_per_add_integer = jsonObj.getInt("points_per_add_integer"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.points_per_add_active = jsonObj.getInt("points_per_add_active"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.price_limit_ratio = jsonObj.getInt("price_limit_ratio"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.price_limit_integer = jsonObj.getInt("price_limit_integer"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.price_limit_active = jsonObj.getInt("price_limit_active"); } catch (Exception ex) { ex.printStackTrace(); }

		try { city.add_price_time1 = jsonObj.getInt("add_price_time1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.add_price_time2 = jsonObj.getInt("add_price_time2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.add_price_time3 = jsonObj.getInt("add_price_time3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.add_price_time4 = jsonObj.getInt("add_price_time4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.add_price_time5 = jsonObj.getInt("add_price_time5"); } catch (Exception ex) { ex.printStackTrace(); }

		try { city.same_price_time1 = jsonObj.getInt("same_price_time1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.same_price_time2 = jsonObj.getInt("same_price_time2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.same_price_time3 = jsonObj.getInt("same_price_time3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.same_price_time4 = jsonObj.getInt("same_price_time4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.same_price_time5 = jsonObj.getInt("same_price_time5"); } catch (Exception ex) { ex.printStackTrace(); }

		try { city.a1 = jsonObj.getDouble("a1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.a2 = jsonObj.getDouble("a2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.b1 = jsonObj.getDouble("b1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.b2 = jsonObj.getDouble("b2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.b4 = jsonObj.getDouble("b4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.c1 = jsonObj.getDouble("c1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.c2 = jsonObj.getDouble("c2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.t1 = ApiGlobal.String2Time(jsonObj.getString("t1")); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.t2 = ApiGlobal.String2Time(jsonObj.getString("t2")); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.d1 = jsonObj.getDouble("d1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.d2 = jsonObj.getDouble("d2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.e1 = jsonObj.getDouble("e1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.e2 = jsonObj.getDouble("e2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.e4 = jsonObj.getDouble("e4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.f1 = jsonObj.getDouble("f1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.f2 = jsonObj.getDouble("f2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g1 = jsonObj.getDouble("g1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g2 = jsonObj.getDouble("g2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g3 = jsonObj.getDouble("g3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g4 = jsonObj.getDouble("g4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g5 = jsonObj.getDouble("g5"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.register_syscoupon_id = jsonObj.getString("register_syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.verified_syscoupon_id = jsonObj.getString("verified_syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.num_order_finished = jsonObj.getInt("num_order_finished"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.finishorders_syscoupon_id = jsonObj.getString("finishorders_syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.num_registermonth = jsonObj.getInt("num_registermonth"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.registermonth_syscoupon_id = jsonObj.getString("registermonth_syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.insu_fee = jsonObj.getDouble("insu_fee"); } catch (Exception ex) { ex.printStackTrace(); }// 保险费 modify by chuzhiqiang

		return city;
	}



	public static SVCCity decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCCity city = new SVCCity();

		try { city.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.code = resultSet.getString("code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.prov = resultSet.getString("prov"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.name = resultSet.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.level = resultSet.getInt("level"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.platform = resultSet.getInt("platform"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.ratio = resultSet.getDouble("ratio"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.integer_ = resultSet.getDouble("integer_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.active = resultSet.getInt("active"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.branch = resultSet.getInt("branch"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.turn1_time = resultSet.getInt("turn1_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.turn2_time = resultSet.getInt("turn2_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.total_time = resultSet.getInt("total_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.driver_lock_time = resultSet.getInt("driver_lock_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.range1 = resultSet.getDouble("range1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.range2 = resultSet.getDouble("range2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.range3 = resultSet.getDouble("range3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.points_per_add_ratio = resultSet.getInt("points_per_add_ratio"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.points_per_add_integer = resultSet.getInt("points_per_add_integer"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.points_per_add_active = resultSet.getInt("points_per_add_active"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.price_limit_ratio = resultSet.getInt("price_limit_ratio"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.price_limit_integer = resultSet.getInt("price_limit_integer"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.price_limit_active = resultSet.getInt("price_limit_active"); } catch (Exception ex) { ex.printStackTrace(); }

		try { city.add_price_time1 = resultSet.getInt("add_price_time1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.add_price_time2 = resultSet.getInt("add_price_time2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.add_price_time3 = resultSet.getInt("add_price_time3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.add_price_time4 = resultSet.getInt("add_price_time4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.add_price_time5 = resultSet.getInt("add_price_time5"); } catch (Exception ex) { ex.printStackTrace(); }

		try { city.same_price_time1 = resultSet.getInt("same_price_time1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.same_price_time2 = resultSet.getInt("same_price_time2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.same_price_time3 = resultSet.getInt("same_price_time3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.same_price_time4 = resultSet.getInt("same_price_time4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.same_price_time5 = resultSet.getInt("same_price_time5"); } catch (Exception ex) { ex.printStackTrace(); }

		try { city.a1 = resultSet.getDouble("a1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.a2 = resultSet.getDouble("a2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.b1 = resultSet.getDouble("b1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.b2 = resultSet.getDouble("b2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.b4 = resultSet.getDouble("b4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.c1 = resultSet.getDouble("c1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.c2 = resultSet.getDouble("c2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.t1 = resultSet.getTime("t1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.t2 = resultSet.getTime("t2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.d1 = resultSet.getDouble("d1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.d2 = resultSet.getDouble("d2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.e1 = resultSet.getDouble("e1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.e2 = resultSet.getDouble("e2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.e4 = resultSet.getDouble("e4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.f1 = resultSet.getDouble("f1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.f2 = resultSet.getDouble("f2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g1 = resultSet.getDouble("g1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g2 = resultSet.getDouble("g2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g3 = resultSet.getDouble("g3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g4 = resultSet.getDouble("g4"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.g5 = resultSet.getDouble("g5"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.register_syscoupon_id = resultSet.getString("register_syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.verified_syscoupon_id = resultSet.getString("verified_syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.num_order_finished = resultSet.getInt("num_order_finished"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.finishorders_syscoupon_id = resultSet.getString("finishorders_syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.num_registermonth = resultSet.getInt("num_registermonth"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.registermonth_syscoupon_id = resultSet.getString("registermonth_syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }
		try { city.insu_fee = resultSet.getDouble("insu_fee"); } catch (Exception ex) { ex.printStackTrace(); }//保险费 modify by chuzhiqiang

		return city;
	}

}












