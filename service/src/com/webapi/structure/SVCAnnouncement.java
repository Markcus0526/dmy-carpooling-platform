package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCAnnouncement {
	public long id = 0;
	public String title = "";
	public String content = "";
	public Date ps_date = null;
	public long publisher = 0;
	public String ps_city = "";
	public int range = 0;
	public Date validate = null;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("title", title); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("content", content); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ApiGlobal.Date2String(ps_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("publisher", publisher); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_city", ps_city); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("range", range); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("validate", ApiGlobal.Date2String(validate, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCAnnouncement decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCAnnouncement anc_info = new SVCAnnouncement();

		try { anc_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.title = jsonObj.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.content = jsonObj.getString("content"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.publisher = jsonObj.getLong("publisher"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.ps_city = jsonObj.getString("ps_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.range = jsonObj.getInt("range"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.validate = ApiGlobal.String2Date(jsonObj.getString("validate")); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }


		return anc_info;
	}


	public static SVCAnnouncement decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCAnnouncement anc_info = new SVCAnnouncement();

		try { anc_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.title = resultSet.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.content = resultSet.getString("content"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.publisher = resultSet.getLong("publisher"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.ps_city = resultSet.getString("ps_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.range = resultSet.getInt("range"); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.validate = ApiGlobal.String2Date(resultSet.getString("validate")); } catch (Exception ex) { ex.printStackTrace(); }
		try { anc_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }


		return anc_info;
	}

}
