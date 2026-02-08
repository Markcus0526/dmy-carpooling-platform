package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCUserLogin {
	public long id = 0;
	public long userid = 0;
	public int source = 0;
	public String devtoken = "";
	public int platform = 0;
	public Date login_time = null;
	public String pushnotif_token = "";
	public int loggedout = 0;
	public int auto_login = 0;
	public int remem_pwd = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("source", source); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("devtoken", devtoken); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("platform", platform); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("login_time", ApiGlobal.Date2String(login_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("pushnotif_token", pushnotif_token); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("loggedout", loggedout); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("auto_login", auto_login); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remem_pwd", remem_pwd); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCUserLogin decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCUserLogin userlogin = new SVCUserLogin();

		try { userlogin.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.source = jsonObj.getInt("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.devtoken = jsonObj.getString("devtoken"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.platform = jsonObj.getInt("platform"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.login_time = ApiGlobal.String2Date(jsonObj.getString("login_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.pushnotif_token = jsonObj.getString("pushnotif_token"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.loggedout = jsonObj.getInt("loggedout"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.auto_login = jsonObj.getInt("auto_login"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.remem_pwd = jsonObj.getInt("remem_pwd"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return userlogin;
	}


	public static SVCUserLogin decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCUserLogin userlogin = new SVCUserLogin();

		try { userlogin.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.source = resultSet.getInt("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.devtoken = resultSet.getString("devtoken"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.platform = resultSet.getInt("platform"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.login_time = ApiGlobal.String2Date(resultSet.getString("login_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.pushnotif_token = resultSet.getString("pushnotif_token"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.loggedout = resultSet.getInt("loggedout"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.auto_login = resultSet.getInt("auto_login"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.remem_pwd = resultSet.getInt("remem_pwd"); } catch (Exception ex) { ex.printStackTrace(); }
		try { userlogin.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return userlogin;
	}

}
