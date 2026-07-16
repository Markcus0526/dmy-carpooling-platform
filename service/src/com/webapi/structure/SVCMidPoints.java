package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCMidPoints {
	public long id = 0;
	public int order_type = 0;
	public long orderid = 0;
	public int point_index = 0;
	public double lat = 0;
	public double lng = 0;
	public String addr = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_type", order_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("orderid", orderid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("point_index", point_index); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("lat", lat); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("lng", lng); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("addr", addr); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCMidPoints decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCMidPoints mid_points = new SVCMidPoints();

		try { mid_points.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.order_type = jsonObj.getInt("order_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.orderid = jsonObj.getLong("orderid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.point_index = jsonObj.getInt("point_index"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.lat = jsonObj.getDouble("lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.lng = jsonObj.getDouble("lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.addr = jsonObj.getString("addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return mid_points;
	}


	public static SVCMidPoints decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCMidPoints mid_points = new SVCMidPoints();

		try { mid_points.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.order_type = resultSet.getInt("order_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.orderid = resultSet.getLong("orderid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.point_index = resultSet.getInt("point_index"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.lat = resultSet.getDouble("lat"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.lng = resultSet.getDouble("lng"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.addr = resultSet.getString("addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { mid_points.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return mid_points;
	}


}
