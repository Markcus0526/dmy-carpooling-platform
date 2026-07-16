package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCOrderExecCS {
	public long id = 0;
	public int order_type = 0;
	public long passenger = 0;
	public long driver = 0;
	public String from_ = "";
	public String to_ = "";
	public double average_price_platform = 0;
	public double price = 0;
	public Date pre_time = null;
	public String remark = null;
	public Date cr_date = null;
	public Date ti_accept_order = null;
	public Date begin_exec_time = null;
	public Date driverarrival_time = null;
	public Date beginservice_time = null;
	public Date stopservice_time = null;
	public Date pay_time = null;
	public Date pass_cancel_time = null;
	public Date driver_cancel_time = null;
	public String city_from = "";
	public String city_to = "";
	public int has_evaluation_passenger = 0;
	public int has_evaluation_driver = 0;
	public String password = "";
	public double begin_lat = 0;
	public double begin_lng = 0;
	public double end_lat = 0;
	public double end_lng = 0;
	public double freeze_points = 0;
	public double total_distance = 0;
	public String order_city = "";
	public int status = 0;
	public int deleted = 0;
	public long insu_id_driver= 0;//执行订单表中车主的保单id
	public long insu_id_pass= 0;//执行订单表中车主的保单id


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_type", order_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("passenger", passenger); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driver", driver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("from_", from_); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("to_", to_); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("average_price_platform", average_price_platform); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("price", price); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pre_time", ApiGlobal.Date2String(pre_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("cr_date", ApiGlobal.Date2String(cr_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ti_accept_order", ApiGlobal.Date2String(ti_accept_order, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("begin_exec_time", ApiGlobal.Date2String(begin_exec_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driverarrival_time", ApiGlobal.Date2String(driverarrival_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("beginservice_time", ApiGlobal.Date2String(beginservice_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("stopservice_time", ApiGlobal.Date2String(stopservice_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pay_time", ApiGlobal.Date2String(pay_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pass_cancel_time", ApiGlobal.Date2String(pass_cancel_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driver_cancel_time", ApiGlobal.Date2String(driver_cancel_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("city_from", city_from); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("city_to", city_to); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("has_evaluation_passenger", has_evaluation_passenger); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("has_evaluation_driver", has_evaluation_driver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("password", password); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("begin_lat", begin_lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("begin_lng", begin_lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_lat", end_lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("end_lng", end_lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("freeze_points", freeze_points); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("total_distance", total_distance); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_city", order_city); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("insu_id_driver", insu_id_driver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("insu_id_pass", insu_id_pass); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCOrderExecCS decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOrderExecCS order_exec_info = new SVCOrderExecCS();

		try { order_exec_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.order_type = jsonObj.getInt("order_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.passenger = jsonObj.getLong("passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.driver = jsonObj.getLong("driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.from_ = jsonObj.getString("from_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.to_ = jsonObj.getString("to_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.average_price_platform = jsonObj.getDouble("average_price_platform"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.pre_time = ApiGlobal.String2Date(jsonObj.getString("pre_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.cr_date = ApiGlobal.String2Date(jsonObj.getString("cr_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.ti_accept_order = ApiGlobal.String2Date(jsonObj.getString("ti_accept_order")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.begin_exec_time = ApiGlobal.String2Date(jsonObj.getString("begin_exec_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.driverarrival_time = ApiGlobal.String2Date(jsonObj.getString("driverarrival_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.beginservice_time = ApiGlobal.String2Date(jsonObj.getString("beginservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.stopservice_time = ApiGlobal.String2Date(jsonObj.getString("stopservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.pay_time = ApiGlobal.String2Date(jsonObj.getString("pay_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.pass_cancel_time = ApiGlobal.String2Date(jsonObj.getString("pass_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.driver_cancel_time = ApiGlobal.String2Date(jsonObj.getString("driver_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.city_from = jsonObj.getString("city_from"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.city_to = jsonObj.getString("city_to"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.has_evaluation_passenger = jsonObj.getInt("has_evaluation_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.has_evaluation_driver = jsonObj.getInt("has_evaluation_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.password = jsonObj.getString("password"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.begin_lat = jsonObj.getDouble("begin_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.begin_lng = jsonObj.getDouble("begin_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.end_lat = jsonObj.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.end_lng = jsonObj.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.freeze_points = jsonObj.getDouble("freeze_points"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.total_distance = jsonObj.getDouble("total_distance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.order_city = jsonObj.getString("order_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.status = jsonObj.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.insu_id_driver = jsonObj.getLong("insu_id_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.insu_id_pass = jsonObj.getLong("insu_id_pass"); } catch (Exception ex) { ex.printStackTrace(); }


		return order_exec_info;
	}



	public static SVCOrderExecCS decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOrderExecCS order_exec_info = new SVCOrderExecCS();

		try { order_exec_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.order_type = resultSet.getInt("order_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.passenger = resultSet.getLong("passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.driver = resultSet.getLong("driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.from_ = resultSet.getString("from_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.to_ = resultSet.getString("to_"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.average_price_platform = resultSet.getDouble("average_price_platform"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.price = resultSet.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.pre_time = ApiGlobal.String2Date(resultSet.getString("pre_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.cr_date = ApiGlobal.String2Date(resultSet.getString("cr_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.ti_accept_order = ApiGlobal.String2Date(resultSet.getString("ti_accept_order")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.begin_exec_time = ApiGlobal.String2Date(resultSet.getString("begin_exec_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.driverarrival_time = ApiGlobal.String2Date(resultSet.getString("driverarrival_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.beginservice_time = ApiGlobal.String2Date(resultSet.getString("beginservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.stopservice_time = ApiGlobal.String2Date(resultSet.getString("stopservice_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.pay_time = ApiGlobal.String2Date(resultSet.getString("pay_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.pass_cancel_time = ApiGlobal.String2Date(resultSet.getString("pass_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.driver_cancel_time = ApiGlobal.String2Date(resultSet.getString("driver_cancel_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.city_from = resultSet.getString("city_from"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.city_to = resultSet.getString("city_to"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.has_evaluation_passenger = resultSet.getInt("has_evaluation_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.has_evaluation_driver = resultSet.getInt("has_evaluation_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.password = resultSet.getString("password"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.begin_lat = resultSet.getDouble("begin_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.begin_lng = resultSet.getDouble("begin_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.end_lat = resultSet.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.end_lng = resultSet.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.freeze_points = resultSet.getDouble("freeze_points"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.total_distance = resultSet.getDouble("total_distance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.order_city = resultSet.getString("order_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.status = resultSet.getInt("status"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.insu_id_driver = resultSet.getLong("insu_id_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { order_exec_info.insu_id_pass = resultSet.getLong("insu_id_pass"); } catch (Exception ex) { ex.printStackTrace(); }


		return order_exec_info;
	}




}
