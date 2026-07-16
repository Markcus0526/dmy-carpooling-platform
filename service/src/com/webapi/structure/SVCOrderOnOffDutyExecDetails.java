package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCOrderOnOffDutyExecDetails {
	public long id = 0;
	public long onoffduty_divide_id = 0;
	public long order_cs_id = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("onoffduty_divide_id", onoffduty_divide_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCOrderOnOffDutyExecDetails decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCOrderOnOffDutyExecDetails details = new SVCOrderOnOffDutyExecDetails();

		try { details.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.onoffduty_divide_id = jsonObj.getLong("onoffduty_divide_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_cs_id = jsonObj.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}



	public static SVCOrderOnOffDutyExecDetails decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCOrderOnOffDutyExecDetails details = new SVCOrderOnOffDutyExecDetails();

		try { details.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.onoffduty_divide_id = resultSet.getLong("onoffduty_divide_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.order_cs_id = resultSet.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}



}
