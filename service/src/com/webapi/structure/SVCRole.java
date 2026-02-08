package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCRole {
	public long id = 0;
	public String name = "";
	public String remark = "";
	public int type = 0;
	public int deleted = 0;



	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.accumulate("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.accumulate("name", name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.accumulate("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.accumulate("type", type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.accumulate("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCRole decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCRole role = new SVCRole();

		try { role.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { role.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { role.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { role.type = jsonObj.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { role.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return role;
	}


	public static SVCRole decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCRole role = new SVCRole();

		try { role.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { role.name = resultSet.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { role.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { role.type = resultSet.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { role.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return role;
	}


}


