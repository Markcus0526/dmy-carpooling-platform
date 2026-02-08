package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCOrderModifyLog {
	public long id = 0;
	public long modifier = 0;
	public Date md_time = null;
	public String md_column = "";
	public String old_value = "";
	public String new_value = "";
	public long order_exec_id = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("modifier", modifier); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("md_time", ApiGlobal.Date2String(md_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("md_column", md_column); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("old_value", old_value); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("new_value", new_value); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_exec_id", order_exec_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCOrderModifyLog decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOrderModifyLog log = new SVCOrderModifyLog();

		try { log.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.modifier = jsonObj.getLong("modifier"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.md_time = ApiGlobal.String2Date(jsonObj.getString("md_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.md_column = jsonObj.getString("md_column"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.old_value = jsonObj.getString("old_value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.new_value = jsonObj.getString("new_value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.order_exec_id = jsonObj.getLong("order_exec_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return log;
	}


	public static SVCOrderModifyLog decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOrderModifyLog log = new SVCOrderModifyLog();

		try { log.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.modifier = resultSet.getLong("modifier"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.md_time = ApiGlobal.String2Date(resultSet.getString("md_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.md_column = resultSet.getString("md_column"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.old_value = resultSet.getString("old_value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.new_value = resultSet.getString("new_value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.order_exec_id = resultSet.getLong("order_exec_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { log.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return log;
	}


}







