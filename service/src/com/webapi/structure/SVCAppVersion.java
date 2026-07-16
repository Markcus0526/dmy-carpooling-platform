package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCAppVersion {
	public long id = 0;
	public String app_code = "";
	public String url = "";
	public String version = "";
	public int version_code = 1;
	public Date upload_time = null;
	public long upload_user = 0;
	public String remark = "";
	public String qrcode_path = "";
	public long size = 0;
	public String icon_path = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("app_code", app_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("url", url); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("version", version); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("version_code", version_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("upload_time", ApiGlobal.Date2String(upload_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("upload_user", upload_user); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("qrcode_path", qrcode_path); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("size", size); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("icon_path", icon_path); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCAppVersion decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCAppVersion app_version = new SVCAppVersion();

		try { app_version.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.app_code = jsonObj.getString("app_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.url = jsonObj.getString("url"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.version = jsonObj.getString("version"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.version_code = jsonObj.getInt("version_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.upload_time = ApiGlobal.String2Date(jsonObj.getString("upload_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.upload_user = jsonObj.getLong("upload_user"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.qrcode_path = jsonObj.getString("qrcode_path"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.size = jsonObj.getLong("size"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.icon_path = jsonObj.getString("icon_path"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return app_version;
	}


	public static SVCAppVersion decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCAppVersion app_version = new SVCAppVersion();

		try { app_version.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.app_code = resultSet.getString("app_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.url = resultSet.getString("url"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.version = resultSet.getString("version"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.version_code = resultSet.getInt("version_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.upload_time = ApiGlobal.String2Date(resultSet.getString("upload_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.upload_user = resultSet.getLong("upload_user"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.qrcode_path = resultSet.getString("qrcode_path"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.size = resultSet.getLong("size"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.icon_path = resultSet.getString("icon_path"); } catch (Exception ex) { ex.printStackTrace(); }
		try { app_version.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return app_version;
	}


}
