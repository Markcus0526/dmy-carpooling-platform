package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCOrderOnOffDutyDetails {
	public long id = 0;
	public String order_num = "";
	public int alldays_beaccepted = 0;
	public Date from_date = null;
	public double price = 0;
	public double start_lat = 0;
	public double start_lng = 0;
	public String start_addr = "";
	public double end_lat = 0;
	public double end_lng = 0;
	public String end_addr = "";
	public String order_city = "";
	public String leftdays = "";
	public Date pre_time = null;
	public long publisher = 0;
	public Date publish_date = null;
	public Date ti_accept_order = null;
	public Date endservice_time = null;
	public int reqcarstyle = 0;
	public int status = 0;
	public String remark = "";
	public int source = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_num", order_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("alldays_beaccepted", alldays_beaccepted); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("from_date", ApiGlobal.Date2String(from_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("price", price); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_lat", start_lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_lng", start_lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_addr", start_addr); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_lat", end_lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_lng", end_lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_addr", end_addr); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_city", order_city); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("leftdays", leftdays); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pre_time", ApiGlobal.Date2String(pre_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("publisher", publisher); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("publish_date", ApiGlobal.Date2String(publish_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ti_accept_order", ApiGlobal.Date2String(ti_accept_order, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("endservice_time", ApiGlobal.Date2String(endservice_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("reqcarstyle", reqcarstyle); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("source", source); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCOrderOnOffDutyDetails decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOrderOnOffDutyDetails details = new SVCOrderOnOffDutyDetails();

		try { details.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_num = jsonObj.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.alldays_beaccepted = jsonObj.getInt("alldays_beaccepted"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.from_date = ApiGlobal.String2Date(jsonObj.getString("from_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_lat = jsonObj.getDouble("start_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_lng = jsonObj.getDouble("start_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_addr = jsonObj.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_lat = jsonObj.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_lng = jsonObj.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_addr = jsonObj.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_city = jsonObj.getString("order_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.leftdays = jsonObj.getString("leftdays"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.pre_time = ApiGlobal.String2Date(jsonObj.getString("pre_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.publisher = jsonObj.getLong("publisher"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.publish_date = ApiGlobal.String2Date(jsonObj.getString("publish_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.ti_accept_order = ApiGlobal.String2Date(jsonObj.getString("ti_accept_order")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.endservice_time = ApiGlobal.String2Date(jsonObj.getString("endservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.reqcarstyle = jsonObj.getInt("reqcarstyle"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.status = jsonObj.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.source = jsonObj.getInt("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}



	public static SVCOrderOnOffDutyDetails decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOrderOnOffDutyDetails details = new SVCOrderOnOffDutyDetails();

		try { details.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_num = resultSet.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.alldays_beaccepted = resultSet.getInt("alldays_beaccepted"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.from_date = ApiGlobal.String2Date(resultSet.getString("from_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.price = resultSet.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_lat = resultSet.getDouble("start_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_lng = resultSet.getDouble("start_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_addr = resultSet.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_lat = resultSet.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_lng = resultSet.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_addr = resultSet.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_city = resultSet.getString("order_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.leftdays = resultSet.getString("leftdays"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.pre_time = ApiGlobal.String2Date(resultSet.getString("pre_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.publisher = resultSet.getLong("publisher"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.publish_date = ApiGlobal.String2Date(resultSet.getString("publish_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.ti_accept_order = ApiGlobal.String2Date(resultSet.getString("ti_accept_order")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.endservice_time = ApiGlobal.String2Date(resultSet.getString("endservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.reqcarstyle = resultSet.getInt("reqcarstyle"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.status = resultSet.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.source = resultSet.getInt("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}


}
