package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCReqClientAccount {
	public long id = 0;
	public String req_num = "";
	public String account = "";
	public int account_type = 0;
	public long user_id = 0;
	public double sum = 0;
	public int oper = 0;
	public Date req_date = null;
	public Date audit_date = null;
	public int status = 0;
	public int oper_type = 0;
	public String channel = "";
	public String req_cause = "";
	public long order_cs_id = 0;
	public String reject_cause = "";
	public long ts_id = 0;
	public long req_user = 0;
	public long auditor = 0;
	public long req_source = 0;
	public String remark = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("req_num", req_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("account", account); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("account_type", account_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("user_id", user_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sum", sum); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("oper", oper); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("req_date", ApiGlobal.Date2String(req_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("audit_date", ApiGlobal.Date2String(audit_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("oper_type", oper_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("channel", channel); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("req_cause", req_cause); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("reject_cause", reject_cause); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ts_id", ts_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("req_user", req_user); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("auditor", auditor); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("req_source", req_source); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCReqClientAccount decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCReqClientAccount req_info = new SVCReqClientAccount();

		try { req_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_num = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.account = jsonObj.getString("account"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.account_type = jsonObj.getInt("account_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.user_id = jsonObj.getLong("user_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.sum = jsonObj.getDouble("sum"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.oper = jsonObj.getInt("oper"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_date = ApiGlobal.String2Date(jsonObj.getString("req_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.audit_date = ApiGlobal.String2Date(jsonObj.getString("audit_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.status = jsonObj.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.oper_type = jsonObj.getInt("oper_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.channel = jsonObj.getString("channel"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_cause = jsonObj.getString("req_cause"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.order_cs_id = jsonObj.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.reject_cause = jsonObj.getString("reject_cause"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.ts_id = jsonObj.getLong("ts_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_user = jsonObj.getLong("req_user"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.auditor = jsonObj.getLong("auditor"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_source = jsonObj.getLong("req_source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return req_info;
	}


	public static SVCReqClientAccount decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCReqClientAccount req_info = new SVCReqClientAccount();

		try { req_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_num = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.account = resultSet.getString("account"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.account_type = resultSet.getInt("account_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.user_id = resultSet.getLong("user_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.sum = resultSet.getDouble("sum"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.oper = resultSet.getInt("oper"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_date = ApiGlobal.String2Date(resultSet.getString("req_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.audit_date = ApiGlobal.String2Date(resultSet.getString("audit_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.status = resultSet.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.oper_type = resultSet.getInt("oper_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.channel = resultSet.getString("channel"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_cause = resultSet.getString("req_cause"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.order_cs_id = resultSet.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.reject_cause = resultSet.getString("reject_cause"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.ts_id = resultSet.getLong("ts_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_user = resultSet.getLong("req_user"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.auditor = resultSet.getLong("auditor"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.req_source = resultSet.getLong("req_source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { req_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return req_info;
	}


}
