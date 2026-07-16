package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCSmsLog {
	public long id = 0;
	public long sms_plan_id = 0;
	public long user_id = 0;
	public double price = 0;
	public int status = 0;
	public Date time = null;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sms_plan_id", sms_plan_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("user_id", user_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("price", price); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("time", ApiGlobal.Date2String(time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCSmsLog decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCSmsLog sms_log = new SVCSmsLog();

		try { sms_log.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.sms_plan_id = jsonObj.getLong("sms_plan_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.user_id = jsonObj.getLong("user_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.status = jsonObj.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.time = ApiGlobal.String2Date(jsonObj.getString("time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return sms_log;
	}


	public static SVCSmsLog decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCSmsLog sms_log = new SVCSmsLog();

		try { sms_log.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.sms_plan_id = resultSet.getLong("sms_plan_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.user_id = resultSet.getLong("user_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.price = resultSet.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.status = resultSet.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.time = ApiGlobal.String2Date(resultSet.getString("time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { sms_log.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return sms_log;
	}



}












