package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import com.webapi.common.ApiGlobal;

import net.sf.json.JSONObject;

public class SVCOrderTempNotify {
	public long id = 0;
	public long userid = 0;
	public int turn = 0;
	public long order_cs_id = 0;
	public Date push_time = null;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("turn", turn); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("push_time", ApiGlobal.Date2String(push_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCOrderTempNotify decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOrderTempNotify temp_notify = new SVCOrderTempNotify();

		try { temp_notify.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.turn = jsonObj.getInt("turn"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.order_cs_id = jsonObj.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.push_time = ApiGlobal.String2Date(jsonObj.getString("push_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return temp_notify;
	}


	public static SVCOrderTempNotify decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOrderTempNotify temp_notify = new SVCOrderTempNotify();

		try { temp_notify.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.turn = resultSet.getInt("turn"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.order_cs_id = resultSet.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.push_time = ApiGlobal.String2Date(resultSet.getString("push_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { temp_notify.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return temp_notify;
	}

}
