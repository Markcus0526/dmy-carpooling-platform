package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCSingleCoupon {
	public long id = 0;
	public String coupon_code = "";
	public double sum = 0;
	public Date date_expired = null;
	public int isused = 0;
	public Date date_used = null;
	public String password = "";
	public Date ps_date = null;
	public String remark = "";
	public long userid = 0;
	public long order_cs_id = 0;
	public int isenabled = 0;
	public long syscoupon_id = 0;
	public String active_code = "";
	public int is_generated_by_active = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("coupon_code", coupon_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sum", sum); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("date_expired", ApiGlobal.Date2String(date_expired, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("isused", isused); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("date_used", ApiGlobal.Date2String(date_used, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("password", password); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ApiGlobal.Date2String(ps_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("isenabled", isenabled); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("syscoupon_id", syscoupon_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("active_code", active_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("is_generated_by_active", is_generated_by_active); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCSingleCoupon decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCSingleCoupon single_coupon = new SVCSingleCoupon();

		try { single_coupon.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.coupon_code = jsonObj.getString("coupon_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.sum = jsonObj.getDouble("sum"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.date_expired = ApiGlobal.String2Date(jsonObj.getString("date_expired")); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.isused = jsonObj.getInt("isused"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.date_used = ApiGlobal.String2Date(jsonObj.getString("date_used")); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.password = jsonObj.getString("password"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.order_cs_id = jsonObj.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.isenabled = jsonObj.getInt("isenabled"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.syscoupon_id = jsonObj.getLong("syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.active_code = jsonObj.getString("active_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.is_generated_by_active = jsonObj.getInt("is_generated_by_active"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return single_coupon;
	}


	public static SVCSingleCoupon decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCSingleCoupon single_coupon = new SVCSingleCoupon();

		try { single_coupon.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.coupon_code = resultSet.getString("coupon_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.sum = resultSet.getDouble("sum"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.date_expired = ApiGlobal.String2Date(resultSet.getString("date_expired")); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.isused = resultSet.getInt("isused"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.date_used = ApiGlobal.String2Date(resultSet.getString("date_used")); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.password = resultSet.getString("password"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.order_cs_id = resultSet.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.isenabled = resultSet.getInt("isenabled"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.syscoupon_id = resultSet.getLong("syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.active_code = resultSet.getString("active_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.is_generated_by_active = resultSet.getInt("is_generated_by_active"); } catch (Exception ex) { ex.printStackTrace(); }
		try { single_coupon.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return single_coupon;
	}


}
