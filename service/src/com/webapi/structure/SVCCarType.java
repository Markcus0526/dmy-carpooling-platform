package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCCarType {
	public long id = 0;
	public String brand = "";
	public String car_style = "";
	public int type = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("brand", brand); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("car_style", car_style); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("type", type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCCarType decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCCarType cartype = new SVCCarType();

		try { cartype.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { cartype.brand = jsonObj.getString("brand"); } catch (Exception ex) { ex.printStackTrace(); }
		try { cartype.car_style = jsonObj.getString("car_style"); } catch (Exception ex) { ex.printStackTrace(); }
		try { cartype.type = jsonObj.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { cartype.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return cartype;
	}


	public static SVCCarType decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCCarType cartype = new SVCCarType();

		try { cartype.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { cartype.brand = resultSet.getString("brand"); } catch (Exception ex) { ex.printStackTrace(); }
		try { cartype.car_style = resultSet.getString("car_style"); } catch (Exception ex) { ex.printStackTrace(); }
		try { cartype.type = resultSet.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { cartype.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return cartype;
	}

}
