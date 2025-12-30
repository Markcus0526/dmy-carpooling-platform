package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCSmsUsers {
	public long id = 0;
	public long sms_plan_id = 0;
	public long userid = 0;
	public String phone = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sms_plan_id", sms_plan_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("phone", phone); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCSmsUsers decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;


		SVCSmsUsers users = new SVCSmsUsers();

		try { users.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users.sms_plan_id = jsonObj.getLong("sms_plan_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users.phone = jsonObj.getString("phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return users;
	}


	public static SVCSmsUsers decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;


		SVCSmsUsers users = new SVCSmsUsers();

		try { users.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users.sms_plan_id = resultSet.getLong("sms_plan_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users.phone = resultSet.getString("phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { users.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return users;
	}

}




























