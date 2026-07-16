package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCFeedback {
	public long id = 0;
	public long userid = 0;
	public String title = "";
	public String content = "";
	public String application = "";
	public Date ps_date = null;
	public String sys_msg = "";
	public Date date_sys = null;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("title", title); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("content", content); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("application", application); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ApiGlobal.Date2String(ps_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sys_msg", sys_msg); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("date_sys", ApiGlobal.Date2String(date_sys, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCFeedback decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCFeedback feedback = new SVCFeedback();

		try { feedback.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.title = jsonObj.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.content = jsonObj.getString("content"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.application = jsonObj.getString("application"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.sys_msg = jsonObj.getString("sys_msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.date_sys = ApiGlobal.String2Date(jsonObj.getString("date_sys")); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return feedback;
	}


	public static SVCFeedback decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCFeedback feedback = new SVCFeedback();

		try { feedback.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.title = resultSet.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.content = resultSet.getString("content"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.application = resultSet.getString("application"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.sys_msg = resultSet.getString("sys_msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.date_sys = ApiGlobal.String2Date(resultSet.getString("date_sys")); } catch (Exception ex) { ex.printStackTrace(); }
		try { feedback.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return feedback;
	}


}
