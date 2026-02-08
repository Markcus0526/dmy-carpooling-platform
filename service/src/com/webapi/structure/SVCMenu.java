package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCMenu {
	public long id = 0;
	public String name = "";
	public String url = "";
	public int menucode = 0;
	public int submenu = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("name", name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("url", url); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("menucode", menucode); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("submenu", submenu); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCMenu deocdeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCMenu menu = new SVCMenu();

		try { menu.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.url = jsonObj.getString("url"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.menucode = jsonObj.getInt("menucode"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.submenu = jsonObj.getInt("submenu"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return menu;
	}


	public static SVCMenu deocdeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCMenu menu = new SVCMenu();

		try { menu.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.name = resultSet.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.url = resultSet.getString("url"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.menucode = resultSet.getInt("menucode"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.submenu = resultSet.getInt("submenu"); } catch (Exception ex) { ex.printStackTrace(); }
		try { menu.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return menu;
	}







}











