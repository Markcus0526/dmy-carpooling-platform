package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCRoutesSetting {
	public long id = 0;
	public String startcity = "";
	public String endcity = "";
	public String from_ = "";
	public String to_ = "";
	public String city = "";
	public double lat_from = 0;
	public double lng_from = 0;
	public double lat_to = 0;
	public double lng_to = 0;
	public Date ps_date = null;
	public Date start_time = null;
	public int type = 0;
	public String whichdays = "";
	public long userid = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("startcity", startcity); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("endcity", endcity); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("from_", from_); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("to_", to_); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("city", city); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("lat_from", lat_from); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("lng_from", lng_from); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("lat_to", lat_to); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("lng_to", lng_to); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ApiGlobal.Date2String(ps_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_time", ApiGlobal.Date2String(start_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("type", type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("whichdays", whichdays); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCRoutesSetting decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCRoutesSetting setting = new SVCRoutesSetting();

		try { setting.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.startcity = jsonObj.getString("startcity"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.endcity = jsonObj.getString("endcity"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.from_ = jsonObj.getString("from_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.to_ = jsonObj.getString("to_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.city = jsonObj.getString("city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.lat_from = jsonObj.getDouble("lat_from"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.lng_from = jsonObj.getDouble("lng_from"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.lat_to = jsonObj.getDouble("lat_to"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.lng_to = jsonObj.getDouble("lng_to"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.start_time = ApiGlobal.String2Date(jsonObj.getString("start_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.type = jsonObj.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.whichdays = jsonObj.getString("whichdays"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.userid = jsonObj.getLong("user_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return setting;
	}


	public static SVCRoutesSetting decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCRoutesSetting setting = new SVCRoutesSetting();

		try { setting.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.startcity = resultSet.getString("startcity"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.endcity = resultSet.getString("endcity"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.from_ = resultSet.getString("from_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.to_ = resultSet.getString("to_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.city = resultSet.getString("city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.lat_from = resultSet.getDouble("lat_from"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.lng_from = resultSet.getDouble("lng_from"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.lat_to = resultSet.getDouble("lat_to"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.lng_to = resultSet.getDouble("lng_to"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.start_time = ApiGlobal.String2Date(resultSet.getString("start_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.type = resultSet.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.whichdays = resultSet.getString("whichdays"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { setting.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return setting;
	}

}
