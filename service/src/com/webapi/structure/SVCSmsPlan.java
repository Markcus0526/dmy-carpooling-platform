package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCSmsPlan {
	public long id = 0;
	public String plan_code = "";
	public int client_num = 0;
	public String msg = "";
	public double price = 0;
	public int send_mode = 0;
	public Date single_time = null;
	public int has_send_times = 0;
	public int limit_times = 0;
	public int regular_send_mode = 0;
	public int time1 = 0;
	public int time2 = 0;
	public int isenabled = 0;
	public String remark = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("plan_code", plan_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("client_num", client_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("msg", msg); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("price", price); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("send_mode", send_mode); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("single_time", ApiGlobal.Date2String(single_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("has_send_times", has_send_times); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_times", limit_times); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("regular_send_mode", regular_send_mode); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("time1", time1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("time2", time2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("isenabled", isenabled); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }


		return jsonObj;
	}


	public static SVCSmsPlan decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCSmsPlan sms_plan = new SVCSmsPlan();

		try { sms_plan.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.plan_code = jsonObj.getString("plan_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.client_num = jsonObj.getInt("client_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.msg = jsonObj.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.send_mode = jsonObj.getInt("send_mode"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.single_time = ApiGlobal.String2Date(jsonObj.getString("single_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.has_send_times = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.limit_times = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.regular_send_mode = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.time1 = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.time2 = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.isenabled = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.remark = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.deleted = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }

		return sms_plan;
	}



	public static SVCSmsPlan decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCSmsPlan sms_plan = new SVCSmsPlan();

		try { sms_plan.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.plan_code = resultSet.getString("plan_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.client_num = resultSet.getInt("client_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.msg = resultSet.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.price = resultSet.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.send_mode = resultSet.getInt("send_mode"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.single_time = ApiGlobal.String2Date(resultSet.getString("single_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.has_send_times = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.limit_times = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.regular_send_mode = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.time1 = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.time2 = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.isenabled = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.remark = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_plan.deleted = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }

		return sms_plan;
	}


}






















