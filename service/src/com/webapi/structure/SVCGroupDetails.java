package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCGroupDetails {
	public long id = 0;
	public long groupid = 0;
	public long userid = 0;
	public int group_type = 0;
	public String application = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("groupid", groupid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("group_type", group_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCGroupDetails decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCGroupDetails details = new SVCGroupDetails();

		try { details.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.groupid = jsonObj.getLong("groupid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.group_type = jsonObj.getInt("group_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.application = jsonObj.getString("application"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}


	public static SVCGroupDetails decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCGroupDetails details = new SVCGroupDetails();

		try { details.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.groupid = resultSet.getLong("groupid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.group_type = resultSet.getInt("group_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.application = resultSet.getString("application"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}

}
