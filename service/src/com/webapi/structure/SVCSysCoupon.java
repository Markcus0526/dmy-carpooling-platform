package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCSysCoupon {
	public long id = 0;
	public String syscoupon_code = "";
	public String password = "";
	public Date sc_date = null;
	public String remark = "";
	public int release_channel = 0;
	public long app_spread_unit_id = 0;
	public int goods_or_cash = 0;
	public double sum = 0;
	public String goods = "";
	public int apply_source = 0;
	public int coupon_type = 0;
	public int limit_val = 0;
	public int valid_period_unit = 0;
	public int valid_period = 0;
	public int limit_count = 0;
	public int isenabled = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("syscoupon_code", syscoupon_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("password", password); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sc_date", ApiGlobal.Date2String(sc_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("release_channel", release_channel); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("app_spread_unit_id", app_spread_unit_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("goods_or_cash", goods_or_cash); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sum", sum); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("goods", goods); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("apply_source", apply_source); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("coupon_type", coupon_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_val", limit_val); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("valid_period_unit", valid_period_unit); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("valid_period", valid_period); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_count", limit_count); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("isenabled", isenabled); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}




	public static SVCSysCoupon decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCSysCoupon sys_coupon = new SVCSysCoupon();

		try { sys_coupon.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.syscoupon_code = jsonObj.getString("syscoupon_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.password = jsonObj.getString("password"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.sc_date = ApiGlobal.String2Date(jsonObj.getString("sc_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.release_channel = jsonObj.getInt("release_channel"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.app_spread_unit_id = jsonObj.getLong("app_spread_unit_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.goods_or_cash = jsonObj.getInt("goods_or_cash"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.sum = jsonObj.getDouble("sum"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.goods = jsonObj.getString("goods"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.apply_source = jsonObj.getInt("apply_source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.coupon_type = jsonObj.getInt("coupon_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.limit_val = jsonObj.getInt("limit_val"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.valid_period_unit = jsonObj.getInt("valid_period_unit"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.valid_period = jsonObj.getInt("valid_period"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.limit_count = jsonObj.getInt("limit_count"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.isenabled = jsonObj.getInt("isenabled"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return sys_coupon;
	}


	public static SVCSysCoupon decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCSysCoupon sys_coupon = new SVCSysCoupon();

		try { sys_coupon.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.syscoupon_code = resultSet.getString("syscoupon_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.password = resultSet.getString("password"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.sc_date = ApiGlobal.String2Date(resultSet.getString("sc_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.release_channel = resultSet.getInt("release_channel"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.app_spread_unit_id = resultSet.getLong("app_spread_unit_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.goods_or_cash = resultSet.getInt("goods_or_cash"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.sum = resultSet.getDouble("sum"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.goods = resultSet.getString("goods"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.apply_source = resultSet.getInt("apply_source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.coupon_type = resultSet.getInt("coupon_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.limit_val = resultSet.getInt("limit_val"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.valid_period_unit = resultSet.getInt("valid_period_unit"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.valid_period = resultSet.getInt("valid_period"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.limit_count = resultSet.getInt("limit_count"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.isenabled = resultSet.getInt("isenabled"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sys_coupon.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return sys_coupon;
	}



}
