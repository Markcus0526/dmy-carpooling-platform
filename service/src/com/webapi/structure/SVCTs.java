package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCTs {
	public long id = 0;
	public long order_cs_id = 0;
	public long order_id = 0;
	public int order_type = 0;
	public int oper = 0;
	public int ts_way = 0;
	public double balance = 0;
	public Date ts_date = null;
	public long userid = 0;
	public long groupid = 0;
	public long unitid = 0;
	public String remark = "";
	public String account = "";
	public int account_type = 0;
	public String application = "";
	public String ts_type = "";
	public double sum = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_id", order_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_type", order_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("oper", oper); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ts_way", ts_way); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("balance", balance); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ts_date", ApiGlobal.Date2String(ts_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("groupid", groupid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("unitid", unitid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("account", account); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("account_type", account_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("application", application); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ts_type", ts_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sum", sum); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCTs decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCTs ts_info = new SVCTs();

		try { ts_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.order_cs_id = jsonObj.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.order_id = jsonObj.getLong("order_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.order_type = jsonObj.getInt("order_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.oper = jsonObj.getInt("oper"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.ts_way = jsonObj.getInt("ts_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.balance = jsonObj.getDouble("balance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.ts_date = ApiGlobal.String2Date(jsonObj.getString("ts_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.groupid = jsonObj.getLong("groupid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.unitid = jsonObj.getLong("unitid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.account = jsonObj.getString("account"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.account_type = jsonObj.getInt("account_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.application = jsonObj.getString("application"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.ts_type = jsonObj.getString("ts_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.sum = jsonObj.getDouble("sum"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return ts_info;
	}


	public static SVCTs decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCTs ts_info = new SVCTs();

		try { ts_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.order_cs_id = resultSet.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.order_id = resultSet.getLong("order_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.order_type = resultSet.getInt("order_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.oper = resultSet.getInt("oper"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.ts_way = resultSet.getInt("ts_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.balance = resultSet.getDouble("balance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.ts_date = ApiGlobal.String2Date(resultSet.getString("ts_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.groupid = resultSet.getLong("groupid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.unitid = resultSet.getLong("unitid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.account = resultSet.getString("account"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.account_type = resultSet.getInt("account_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.application = resultSet.getString("application"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.ts_type = resultSet.getString("ts_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.sum = resultSet.getDouble("sum"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return ts_info;
	}



}
