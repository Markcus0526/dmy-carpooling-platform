package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCNotifyOrder {
	public long id = 0;
	public int notification_type = 0;
	public Date ps_date = null;
	public long order_cs_id = 0;
	public long receiver = 0;
	public String msg = "";
	public String title = "";
	public int has_read = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("notification_type", notification_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ApiGlobal.Date2String(ps_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("receiver", receiver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("msg", msg); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("title", title); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("has_read", has_read); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCNotifyOrder decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCNotifyOrder order = new SVCNotifyOrder();

		try { order.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.notification_type = jsonObj.getInt("notification_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.order_cs_id = jsonObj.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.receiver = jsonObj.getLong("receiver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.msg = jsonObj.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.title = jsonObj.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.has_read = jsonObj.getInt("has_read"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return order;
	}


	public static SVCNotifyOrder decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCNotifyOrder order = new SVCNotifyOrder();

		try { order.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.notification_type = resultSet.getInt("notification_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.order_cs_id = resultSet.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.receiver = resultSet.getLong("receiver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.msg = resultSet.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.title = resultSet.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.has_read = resultSet.getInt("has_read"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return order;
	}


}
