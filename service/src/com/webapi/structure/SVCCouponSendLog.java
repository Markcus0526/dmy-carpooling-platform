package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCCouponSendLog {
	public long id = 0;
	public String coupon_code = "";
	public long operator = 0;
	public int send_type = 0;
	public int num = 0;
	public Date sendtime = null;
	public String msg = "";
	public String remark = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("coupon_code", coupon_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("operator", operator); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("send_type", send_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("num", num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sendtime", ApiGlobal.Date2String(sendtime, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("msg", msg); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCCouponSendLog decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCCouponSendLog send_log = new SVCCouponSendLog();

		try { send_log.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.coupon_code = jsonObj.getString("coupon_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.operator = jsonObj.getLong("operator"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.send_type = jsonObj.getInt("send_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.num = jsonObj.getInt("num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.sendtime = ApiGlobal.String2Date(jsonObj.getString("sendtime")); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.msg = jsonObj.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return send_log;
	}


	public static SVCCouponSendLog decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCCouponSendLog send_log = new SVCCouponSendLog();

		try { send_log.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.coupon_code = resultSet.getString("coupon_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.operator = resultSet.getLong("operator"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.send_type = resultSet.getInt("send_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.num = resultSet.getInt("num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.sendtime = ApiGlobal.String2Date(resultSet.getString("sendtime")); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.msg = resultSet.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { send_log.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return send_log;
	}

}
