package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCRoleItem {
	public long id = 0;
	public String name = "";
	public String mid = "";
	public String rid = "";
	public int level = 0;
	public long parent = 0;
	public int chk = 0;
	public long associd = 0;
	public long cid = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("name", name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("mid", mid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("rid", rid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("level", level); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("parent", parent); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("chk", chk); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("associd", associd); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("cid", cid); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCRoleItem decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCRoleItem item = new SVCRoleItem();

		try { item.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.mid = jsonObj.getString("mid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.rid = jsonObj.getString("rid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.level = jsonObj.getInt("level"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.parent = jsonObj.getLong("parent"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.chk = jsonObj.getInt("chk"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.associd = jsonObj.getLong("associd"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.cid = jsonObj.getLong("cid"); } catch (Exception ex) { ex.printStackTrace(); }

		return item;
	}


	public static SVCRoleItem decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCRoleItem item = new SVCRoleItem();

		try { item.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.name = resultSet.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.mid = resultSet.getString("mid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.rid = resultSet.getString("rid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.level = resultSet.getInt("level"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.parent = resultSet.getLong("parent"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.chk = resultSet.getInt("chk"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.associd = resultSet.getLong("associd"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.cid = resultSet.getLong("cid"); } catch (Exception ex) { ex.printStackTrace(); }

		return item;
	}


}
