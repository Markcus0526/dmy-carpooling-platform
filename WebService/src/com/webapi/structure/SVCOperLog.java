package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCOperLog {
	public long id = 0;
	public long operuser_id = 0;
	public Date oper_time = null;
	public String t_name = "";
	public String action_url = "";
	public String desc = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonObj.put("operuser_id", operuser_id); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonObj.put("oper_time", ApiGlobal.Date2String(oper_time, true)); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonObj.put("t_name", t_name); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonObj.put("action_url", action_url); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonObj.put("desc", desc); } catch (Exception ex) { ex.printStackTrace(); };
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); };

		return jsonObj;
	}


	public static SVCOperLog decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOperLog operlog = new SVCOperLog();

		try { operlog.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.operuser_id = jsonObj.getLong("operuser_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.oper_time = ApiGlobal.String2Date(jsonObj.getString("oper_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.t_name = jsonObj.getString("t_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.action_url = jsonObj.getString("action_url"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.desc = jsonObj.getString("desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return operlog;
	}


	public static SVCOperLog decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOperLog operlog = new SVCOperLog();

		try { operlog.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.operuser_id = resultSet.getLong("operuser_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.oper_time = ApiGlobal.String2Date(resultSet.getString("oper_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.t_name = resultSet.getString("t_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.action_url = resultSet.getString("action_url"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.desc = resultSet.getString("desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { operlog.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return operlog;
	}



}


























