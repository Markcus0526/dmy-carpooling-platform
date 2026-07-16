package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCDriverOnlineStat {
	public long id = 0;
	public long userid = 0;
	public int count = 0;
	public int hour = 0;
	public Date ps_date = null;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("count", count); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("hour", hour); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ps_date); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCDriverOnlineStat decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCDriverOnlineStat stat = new SVCDriverOnlineStat();

		try { stat.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.count = jsonObj.getInt("count"); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.hour = jsonObj.getInt("hour"); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return stat;
	}


	public static SVCDriverOnlineStat decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCDriverOnlineStat stat = new SVCDriverOnlineStat();

		try { stat.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.count = resultSet.getInt("count"); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.hour = resultSet.getInt("hour"); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { stat.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return stat;
	}

}
