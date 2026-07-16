package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCOrderLongDistanceDetails {
	public long id = 0;
	public String order_num = "";
	public Date pre_time = null;
	public double price = 0;
	public String start_city = "";
	public String end_city = "";
	public String start_addr = "";
	public String end_addr = "";
	public double start_lat = 0;
	public double start_lng = 0;
	public double end_lat = 0;
	public double end_lng = 0;
	public int seat_num = 0;
	public long publisher = 0;
	public Date ps_time = null;
	public Date ti_accept_order = null;
	public Date begin_exec_time = null;
	public Date driverarrival_time = null;
	public Date beginservice_time = null;
	public Date endservice_time = null;
	public Date pay_time = null;
	public Date pass_cancel_time = null;
	public Date driver_cancel_time = null;
	public int occupied_num = 0;
	public String remark = "";
	public int status = 0;
	public int source = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_num", order_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pre_time", ApiGlobal.Date2String(pre_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("price", price); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_city", start_city); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_city", end_city); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_addr", start_addr); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_addr", end_addr); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_lat", start_lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("start_lng", start_lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_lat", end_lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_lng", end_lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("seat_num", seat_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("publisher", publisher); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_time", ApiGlobal.Date2String(ps_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ti_accept_order", ApiGlobal.Date2String(ti_accept_order, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("begin_exec_time", ApiGlobal.Date2String(begin_exec_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driverarrival_time", ApiGlobal.Date2String(driverarrival_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("beginservice_time", ApiGlobal.Date2String(beginservice_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("endservice_time", ApiGlobal.Date2String(endservice_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pay_time", ApiGlobal.Date2String(pay_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pass_cancel_time", ApiGlobal.Date2String(pass_cancel_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driver_cancel_time", ApiGlobal.Date2String(driver_cancel_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("occupied_num", occupied_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("source", source); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }


		return jsonObj;
	}



	public static SVCOrderLongDistanceDetails decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOrderLongDistanceDetails detail_info = new SVCOrderLongDistanceDetails();

		try { detail_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.order_num = jsonObj.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.pre_time = ApiGlobal.String2Date(jsonObj.getString("pre_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.start_city = jsonObj.getString("start_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.end_city = jsonObj.getString("end_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.start_addr = jsonObj.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.end_addr = jsonObj.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.start_lat = jsonObj.getDouble("start_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.start_lng = jsonObj.getDouble("start_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.end_lat = jsonObj.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.end_lng = jsonObj.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.seat_num = jsonObj.getInt("seat_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.publisher = jsonObj.getLong("publisher"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.ps_time = ApiGlobal.String2Date(jsonObj.getString("ps_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.ti_accept_order = ApiGlobal.String2Date(jsonObj.getString("ti_accept_order")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.begin_exec_time = ApiGlobal.String2Date(jsonObj.getString("begin_exec_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.driverarrival_time = ApiGlobal.String2Date(jsonObj.getString("driverarrival_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.beginservice_time = ApiGlobal.String2Date(jsonObj.getString("beginservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.endservice_time = ApiGlobal.String2Date(jsonObj.getString("endservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.pay_time = ApiGlobal.String2Date(jsonObj.getString("pay_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.pass_cancel_time = ApiGlobal.String2Date(jsonObj.getString("pass_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.driver_cancel_time = ApiGlobal.String2Date(jsonObj.getString("driver_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.occupied_num = jsonObj.getInt("occupied_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.status = jsonObj.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.source = jsonObj.getInt("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return detail_info;
	}



	public static SVCOrderLongDistanceDetails decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOrderLongDistanceDetails detail_info = new SVCOrderLongDistanceDetails();

		try { detail_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.order_num = resultSet.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.pre_time = ApiGlobal.String2Date(resultSet.getString("pre_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.price = resultSet.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.start_city = resultSet.getString("start_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.end_city = resultSet.getString("end_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.start_addr = resultSet.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.end_addr = resultSet.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.start_lat = resultSet.getDouble("start_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.start_lng = resultSet.getDouble("start_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.end_lat = resultSet.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.end_lng = resultSet.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.seat_num = resultSet.getInt("seat_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.publisher = resultSet.getLong("publisher"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.ps_time = ApiGlobal.String2Date(resultSet.getString("ps_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.ti_accept_order = ApiGlobal.String2Date(resultSet.getString("ti_accept_order")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.begin_exec_time = ApiGlobal.String2Date(resultSet.getString("begin_exec_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.driverarrival_time = ApiGlobal.String2Date(resultSet.getString("driverarrival_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.beginservice_time = ApiGlobal.String2Date(resultSet.getString("beginservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.endservice_time = ApiGlobal.String2Date(resultSet.getString("endservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.pay_time = ApiGlobal.String2Date(resultSet.getString("pay_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.pass_cancel_time = ApiGlobal.String2Date(resultSet.getString("pass_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.driver_cancel_time = ApiGlobal.String2Date(resultSet.getString("driver_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.occupied_num = resultSet.getInt("occupied_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.status = resultSet.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.source = resultSet.getInt("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { detail_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return detail_info;
	}

}
