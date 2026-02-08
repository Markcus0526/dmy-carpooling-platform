package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCColor {
	public long id = 0;
	public String code = "";
	public String color_desc = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("code", code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("color_desc", color_desc); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCColor decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCColor color = new SVCColor();

		try { color.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { color.code = jsonObj.getString("code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { color.color_desc = jsonObj.getString("color_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { color.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return color;
	}


	public static SVCColor decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCColor color = new SVCColor();

		try { color.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { color.code = resultSet.getString("code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { color.color_desc = resultSet.getString("color_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { color.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return color;
	}


}
