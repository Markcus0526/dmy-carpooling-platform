package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCAbbRecord {
	public long id = 0;
	public long userid = 0;
	public int abb_type = 0;
	public String desc = "";
	public long order_exec_id = 0;
	public int status = 0;
	public String remark = "";
	public long auditor = 0;
	public long reviewer = 0;
	public int limit_days = 0;
	public Date limit_days_begin = null;
	public Date abb_time = null;
	public long balance_ts = 0;
	public int cancel_number = 0;
	public int deleted = 0;

	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("abb_type", abb_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("desc", desc); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_exec_id", order_exec_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("auditor", auditor); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("reviewer", reviewer); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_days", limit_days); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_days_begin", ApiGlobal.Date2String(limit_days_begin, false)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("abb_time", ApiGlobal.Date2String(abb_time, false)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("balance_ts", balance_ts); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("cancel_number", cancel_number); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCAbbRecord decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCAbbRecord record_info = new SVCAbbRecord();

		try { record_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.abb_type = jsonObj.getInt("abb_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.desc = jsonObj.getString("desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.order_exec_id = jsonObj.getLong("order_exec_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.status = jsonObj.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.auditor = jsonObj.getLong("auditor"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.reviewer = jsonObj.getLong("reviewer"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.limit_days = jsonObj.getInt("limit_days"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.limit_days_begin = ApiGlobal.String2Date(jsonObj.getString("limit_days_begin")); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.abb_time = ApiGlobal.String2Date(jsonObj.getString("abb_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.balance_ts = jsonObj.getLong("balance_ts"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.cancel_number = jsonObj.getInt("cancel_number"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return record_info;
	}


	public static SVCAbbRecord decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCAbbRecord record_info = new SVCAbbRecord();

		try { record_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.abb_type = resultSet.getInt("abb_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.desc = resultSet.getString("desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.order_exec_id = resultSet.getLong("order_exec_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.status = resultSet.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.auditor = resultSet.getLong("auditor"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.reviewer = resultSet.getLong("reviewer"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.limit_days = resultSet.getInt("limit_days"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.limit_days_begin = ApiGlobal.String2Date(resultSet.getString("limit_days_begin")); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.abb_time = ApiGlobal.String2Date(resultSet.getString("abb_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.balance_ts = resultSet.getLong("balance_ts"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.cancel_number = resultSet.getInt("cancel_number"); } catch (Exception ex) { ex.printStackTrace(); }
		try { record_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return record_info;
	}

}
