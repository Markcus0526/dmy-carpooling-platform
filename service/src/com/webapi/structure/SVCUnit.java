package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCUnit {
	public long id = 0;
	public int unit_type = 0;
	public String name = "";
	public long entityid = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("unit_type", unit_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("name", name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("entityid", entityid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCUnit decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCUnit unit = new SVCUnit();

		try { unit.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unit.unit_type = jsonObj.getInt("unit_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unit.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unit.entityid = jsonObj.getLong("entityid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unit.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return unit;
	}


	public static SVCUnit decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCUnit unit = new SVCUnit();

		try { unit.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unit.unit_type = resultSet.getInt("unit_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unit.name = resultSet.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unit.entityid = resultSet.getLong("entityid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unit.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return unit;
	}



}
