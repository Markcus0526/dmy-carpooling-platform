package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCUserOnline {
	public long id = 0;
	public double lat = 0;
	public double lng = 0;
	public Date login_time = null;
	public Date last_heartbeat_time = null;
	public long userid = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("lat", lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("lng", lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("login_time", ApiGlobal.Date2String(login_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("last_heartbeat_time", ApiGlobal.Date2String(last_heartbeat_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCUserOnline decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCUserOnline user_online = new SVCUserOnline();

		try { user_online.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.lat = jsonObj.getDouble("lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.lng = jsonObj.getDouble("lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.login_time = ApiGlobal.String2Date(jsonObj.getString("login_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.last_heartbeat_time = ApiGlobal.String2Date(jsonObj.getString("last_heartbeat_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return user_online;
	}


	public static SVCUserOnline decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCUserOnline user_online = new SVCUserOnline();

		try { user_online.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.lat = resultSet.getDouble("lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.lng = resultSet.getDouble("lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.login_time = ApiGlobal.String2Date(resultSet.getString("login_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.last_heartbeat_time = ApiGlobal.String2Date(resultSet.getString("last_heartbeat_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_online.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return user_online;
	}


}
