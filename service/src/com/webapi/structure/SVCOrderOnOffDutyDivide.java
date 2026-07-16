package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCOrderOnOffDutyDivide {
	public long id = 0;
	public String which_days = "";
	public long orderdetails_id = 0;
	public long driver_id = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("which_days", which_days); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("orderdetails_id", orderdetails_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("driver_id", driver_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCOrderOnOffDutyDivide decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOrderOnOffDutyDivide divide = new SVCOrderOnOffDutyDivide();

		try { divide.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { divide.which_days = jsonObj.getString("which_days"); } catch (Exception ex) { ex.printStackTrace(); }
		try { divide.orderdetails_id = jsonObj.getLong("orderdetails_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { divide.driver_id = jsonObj.getLong("driver_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { divide.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return divide;
	}


	public static SVCOrderOnOffDutyDivide decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOrderOnOffDutyDivide divide = new SVCOrderOnOffDutyDivide();

		try { divide.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { divide.which_days = resultSet.getString("which_days"); } catch (Exception ex) { ex.printStackTrace(); }
		try { divide.orderdetails_id = resultSet.getLong("orderdetails_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { divide.driver_id = resultSet.getLong("driver_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { divide.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return divide;
	}



}
