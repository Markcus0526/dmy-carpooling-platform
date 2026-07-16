package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCGroupAssociationDetails {
	public long id = 0;
	public long associd = 0;
	public long groupid = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("associd", associd); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("groupid", groupid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCGroupAssociationDetails decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCGroupAssociationDetails details = new SVCGroupAssociationDetails();

		try { details.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.associd = jsonObj.getLong("associd"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.groupid = jsonObj.getLong("groupid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}


	public static SVCGroupAssociationDetails decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCGroupAssociationDetails details = new SVCGroupAssociationDetails();

		try { details.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.associd = resultSet.getLong("associd"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.groupid = resultSet.getLong("groupid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { details.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return details;
	}


}
