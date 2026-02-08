package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCOrderTempDetails {
	public long id = 0;
	public String order_num = "";
	public int seats_num = 0;
	public int is_broadcast_stop = 0;
	public int turn_num = 0;
	public Date ps_date = null;
	public Date accept_time = null;
	public Date begin_exec_time = null;
	public Date driverarrival_time = null;
	public Date beginservice_time = null;
	public Date endservice_time = null;
	public Date pay_time = null;
	public Date pass_cancel_time = null;
	public Date driver_cancel_time = null;
	public double start_lat = 0;
	public double start_lng = 0;
	public String start_addr = "";
	public double end_lat = 0;
	public double end_lng = 0;
	public String end_addr = "";
	public String order_city = "";
	public Date pre_time = null;
	public double price = 0;
	public int wait_time = 0;
	public long order_cs_id = 0;
	public int is_from_onoffdutyorder = 0;
	public long orderonoffduty_id = 0;
	public long publisher = 0;
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
		try { jsonObj.put("seats_num", seats_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("is_broadcast_stop", is_broadcast_stop); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("turn_num", turn_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ApiGlobal.Date2String(ps_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("accept_time", ApiGlobal.Date2String(accept_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("begin_exec_time", ApiGlobal.Date2String(begin_exec_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driverarrival_time", ApiGlobal.Date2String(driverarrival_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("beginservice_time", ApiGlobal.Date2String(beginservice_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("endservice_time", ApiGlobal.Date2String(endservice_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pay_time", ApiGlobal.Date2String(pay_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pass_cancel_time", ApiGlobal.Date2String(pass_cancel_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driver_cancel_time", ApiGlobal.Date2String(driver_cancel_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_lat", start_lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_lng", start_lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_addr", start_addr); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_lat", end_lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_lng", end_lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_addr", end_addr); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_city", order_city); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pre_time", ApiGlobal.Date2String(pre_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("price", price); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("wait_time", wait_time); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("is_from_onoffdutyorder", is_from_onoffdutyorder); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("orderonoffduty_id", orderonoffduty_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("publisher", publisher); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("reqcarstyle", reqcarstyle); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("source", source); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCOrderTempDetails decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOrderTempDetails details = new SVCOrderTempDetails();

		try { details.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_num = jsonObj.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.seats_num = jsonObj.getInt("seats_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.is_broadcast_stop = jsonObj.getInt("is_broadcast_stop"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.turn_num = jsonObj.getInt("turn_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.accept_time = ApiGlobal.String2Date(jsonObj.getString("accept_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.begin_exec_time = ApiGlobal.String2Date(jsonObj.getString("begin_exec_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.driverarrival_time = ApiGlobal.String2Date(jsonObj.getString("driverarrival_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.beginservice_time = ApiGlobal.String2Date(jsonObj.getString("beginservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.endservice_time = ApiGlobal.String2Date(jsonObj.getString("endservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.pay_time = ApiGlobal.String2Date(jsonObj.getString("pay_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.pass_cancel_time = ApiGlobal.String2Date(jsonObj.getString("pass_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.driver_cancel_time = ApiGlobal.String2Date(jsonObj.getString("driver_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_lat = jsonObj.getDouble("start_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_lng = jsonObj.getDouble("start_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_addr = jsonObj.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_lat = jsonObj.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_lng = jsonObj.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_addr = jsonObj.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_city = jsonObj.getString("order_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.pre_time = ApiGlobal.String2Date(jsonObj.getString("pre_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.wait_time = jsonObj.getInt("wait_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_cs_id = jsonObj.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.is_from_onoffdutyorder = jsonObj.getInt("is_from_onoffdutyorder"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.orderonoffduty_id = jsonObj.getLong("orderonoffduty_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.publisher = jsonObj.getLong("publisher"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.reqcarstyle = jsonObj.getInt("reqcarstyle"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.status = jsonObj.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.source = jsonObj.getInt("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}



	public static SVCOrderTempDetails decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOrderTempDetails details = new SVCOrderTempDetails();

		try { details.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_num = resultSet.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.seats_num = resultSet.getInt("seats_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.is_broadcast_stop = resultSet.getInt("is_broadcast_stop"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.turn_num = resultSet.getInt("turn_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.accept_time = ApiGlobal.String2Date(resultSet.getString("accept_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.begin_exec_time = ApiGlobal.String2Date(resultSet.getString("begin_exec_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.driverarrival_time = ApiGlobal.String2Date(resultSet.getString("driverarrival_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.beginservice_time = ApiGlobal.String2Date(resultSet.getString("beginservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.endservice_time = ApiGlobal.String2Date(resultSet.getString("endservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.pay_time = ApiGlobal.String2Date(resultSet.getString("pay_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.pass_cancel_time = ApiGlobal.String2Date(resultSet.getString("pass_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.driver_cancel_time = ApiGlobal.String2Date(resultSet.getString("driver_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_lat = resultSet.getDouble("start_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_lng = resultSet.getDouble("start_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.start_addr = resultSet.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_lat = resultSet.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_lng = resultSet.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.end_addr = resultSet.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_city = resultSet.getString("order_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.pre_time = ApiGlobal.String2Date(resultSet.getString("pre_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.price = resultSet.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.wait_time = resultSet.getInt("wait_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_cs_id = resultSet.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.is_from_onoffdutyorder = resultSet.getInt("is_from_onoffdutyorder"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.orderonoffduty_id = resultSet.getLong("orderonoffduty_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.publisher = resultSet.getLong("publisher"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.reqcarstyle = resultSet.getInt("reqcarstyle"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.status = resultSet.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.source = resultSet.getInt("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}


}
