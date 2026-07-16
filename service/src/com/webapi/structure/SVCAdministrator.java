package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCAdministrator {
	public long id = 0;
	public String user_code = "";
	public String password = "";
	public String username = "";
	public int unit = 0;
	public String phoneNum = "";
	public String sex = "";
	public String phoneNum2 = "";
	public String qq = "";
	public String email = "";
	public int level = 0;
	public String note = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("user_code", user_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("password", password); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("username", username); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("unit", unit); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("phoneNum", phoneNum); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sex", sex); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("phoneNum2", phoneNum2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("qq", qq); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("email", email); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("level", level); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("note", note); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCAdministrator decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCAdministrator admin = new SVCAdministrator();

		try { admin.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.user_code = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.password = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.username = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.unit = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.phoneNum = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.sex = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.phoneNum2 = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.qq = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.email = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.level = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.note = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.deleted = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }

		return admin;
	}


	public static SVCAdministrator decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCAdministrator admin = new SVCAdministrator();

		try { admin.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.user_code = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.password = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.username = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.unit = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.phoneNum = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.sex = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.phoneNum2 = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.qq = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.email = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.level = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.note = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { admin.deleted = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }

		return admin;
	}



}





























