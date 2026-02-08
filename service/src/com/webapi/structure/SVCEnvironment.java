package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCEnvironment {
	public long id = 0;
	public String code = "";
	public String value = "";
	public String remark = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("code", code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("value", value); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCEnvironment decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCEnvironment env = new SVCEnvironment();

		try { env.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { env.code = jsonObj.getString("code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { env.value = jsonObj.getString("value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { env.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { env.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return env;
	}


	public static SVCEnvironment decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCEnvironment env = new SVCEnvironment();

		try { env.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { env.code = resultSet.getString("code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { env.value = resultSet.getString("value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { env.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { env.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return env;
	}


}
