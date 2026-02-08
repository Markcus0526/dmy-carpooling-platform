package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCAppDownload {
	public long id = 0;
	public long app_version_id = 0;
	public long userid = 0;
	public String ip = "";
	public Date ps_date = null;
	public int is_completed = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("app_version_id", app_version_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ip", ip); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ApiGlobal.Date2String(ps_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("is_completed", is_completed); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCAppDownload decodeToJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCAppDownload download = new SVCAppDownload();

		try { download.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.app_version_id = jsonObj.getLong("app_version_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.ip = jsonObj.getString("ip"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.is_completed = jsonObj.getInt("is_completed"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return download;
	}


	public static SVCAppDownload decodeToResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCAppDownload download = new SVCAppDownload();

		try { download.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.app_version_id = resultSet.getLong("app_version_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.ip = resultSet.getString("ip"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.is_completed = resultSet.getInt("is_completed"); } catch (Exception ex) { ex.printStackTrace(); }
		try { download.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return download;
	}

}
