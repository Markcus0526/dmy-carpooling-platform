package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCApp {
	public long id = 0;
	public String app_code = "";
	public String app_name = "";
	public String pack_name = "";
	public String bundle_id = "";
	public String url_scheme = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("app_code", app_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("app_name", app_name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pack_name", pack_name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("bundle_id", bundle_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("url_scheme", url_scheme); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCApp decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCApp appinfo = new SVCApp();

		try { appinfo.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.app_code = jsonObj.getString("app_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.app_name = jsonObj.getString("app_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.pack_name = jsonObj.getString("pack_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.bundle_id = jsonObj.getString("bundle_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.url_scheme = jsonObj.getString("url_scheme"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return appinfo;
	}


	public static SVCApp decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCApp appinfo = new SVCApp();

		try { appinfo.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.app_code = resultSet.getString("app_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.app_name = resultSet.getString("app_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.pack_name = resultSet.getString("pack_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.bundle_id = resultSet.getString("bundle_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.url_scheme = resultSet.getString("url_scheme"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return appinfo;
	}
}
