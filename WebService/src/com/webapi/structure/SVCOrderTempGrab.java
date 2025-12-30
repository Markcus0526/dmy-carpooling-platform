package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCOrderTempGrab {
	public long id = 0;
	public Date grab_time = null;
	public double distance = 0;
	public int status = 0;
	public long driverid = 0;
	public long order_id = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("grab_time", ApiGlobal.Date2String(grab_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("distance", distance); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driverid", driverid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_id", order_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCOrderTempGrab decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOrderTempGrab temp_grab = new SVCOrderTempGrab();

		try { temp_grab.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.grab_time = ApiGlobal.String2Date(jsonObj.getString("grab_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.distance = jsonObj.getLong("distance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.status = jsonObj.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.driverid = jsonObj.getLong("driverid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.order_id = jsonObj.getLong("order_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return temp_grab;
	}


	public static SVCOrderTempGrab decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOrderTempGrab temp_grab = new SVCOrderTempGrab();

		try { temp_grab.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.grab_time = ApiGlobal.String2Date(resultSet.getString("grab_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.distance = resultSet.getLong("distance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.status = resultSet.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.driverid = resultSet.getLong("driverid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.order_id = resultSet.getLong("order_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_grab.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return temp_grab;
	}



}
