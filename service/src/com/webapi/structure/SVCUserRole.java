package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCUserRole {
	public long id = 0;
	public long user_id = 0;
	public long role_id = 0;
	public int deleted = 0;



	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("user_id", user_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("role_id", role_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCUserRole decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCUserRole user_role = new SVCUserRole();

		try { user_role.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_role.user_id = jsonObj.getLong("user_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_role.role_id = jsonObj.getLong("role_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_role.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return user_role;
	}



	public static SVCUserRole decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCUserRole user_role = new SVCUserRole();

		try { user_role.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_role.user_id = resultSet.getLong("user_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_role.role_id = resultSet.getLong("role_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_role.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return user_role;
	}



}
