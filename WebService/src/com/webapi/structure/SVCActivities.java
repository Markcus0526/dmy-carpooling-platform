package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCActivities {
	public long id = 0;
	public String active_code = "";
	public int limit_count = 0;
	public long syscoupon_id = 0;
	public Date at_date = null;
	public int isenabled = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("active_code", active_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_count", limit_count); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("syscoupon_id", syscoupon_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("at_date", ApiGlobal.Date2String(at_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("isenabled", isenabled); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCActivities decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCActivities act_info = new SVCActivities();

		try { act_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.active_code = jsonObj.getString("active_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.limit_count = jsonObj.getInt("limit_count"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.syscoupon_id = jsonObj.getLong("syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.at_date = ApiGlobal.String2Date(jsonObj.getString("at_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.isenabled = jsonObj.getInt("isenabled"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return act_info;
	}


	public static SVCActivities decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCActivities act_info = new SVCActivities();

		try { act_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.active_code = resultSet.getString("active_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.limit_count = resultSet.getInt("limit_count"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.syscoupon_id = resultSet.getLong("syscoupon_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.at_date = ApiGlobal.String2Date(resultSet.getString("at_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.isenabled = resultSet.getInt("isenabled"); } catch (Exception ex) { ex.printStackTrace(); }
		try { act_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return act_info;
	}




}




