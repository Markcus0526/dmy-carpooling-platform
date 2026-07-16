package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCOrderLongDistanceUsersCS {
	public long id = 0;
	public long orderdriverlongdistance_id = 0;
	public long userid = 0;
	public long order_exec_cs_id = 0;
	public int seat_num = 0;
	public Date ps_date = null;
	public int deleted = 0;

	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("orderdriverlongdistance_id", orderdriverlongdistance_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_exec_cs_id", order_exec_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("seat_num", seat_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ps_date); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCOrderLongDistanceUsersCS decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;


		SVCOrderLongDistanceUsersCS users_cs = new SVCOrderLongDistanceUsersCS();

		try { users_cs.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.orderdriverlongdistance_id = jsonObj.getLong("orderdriverlongdistance_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.order_exec_cs_id = jsonObj.getLong("order_exec_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.seat_num = jsonObj.getInt("seat_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return users_cs;
	}


	public static SVCOrderLongDistanceUsersCS decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;


		SVCOrderLongDistanceUsersCS users_cs = new SVCOrderLongDistanceUsersCS();

		try { users_cs.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.orderdriverlongdistance_id = resultSet.getLong("orderdriverlongdistance_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.order_exec_cs_id = resultSet.getLong("order_exec_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.seat_num = resultSet.getInt("seat_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { users_cs.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return users_cs;
	}
}
