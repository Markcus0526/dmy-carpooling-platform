package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCNotifyPerson {
	public long id = 0;
	public String title = "";
	public String msg = "";
	public int msg_type = 0;
	public Date ps_date = null;
	public long couponid = 0;
	public long receiver = 0;
	public int has_read = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("title", title); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("msg", msg); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("msg_type", msg_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ApiGlobal.Date2String(ps_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("couponid", couponid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("receiver", receiver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("has_read", has_read); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCNotifyPerson decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCNotifyPerson notify_person = new SVCNotifyPerson();

		try { notify_person.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.title = jsonObj.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.msg = jsonObj.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.msg_type = jsonObj.getInt("msg_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.couponid = jsonObj.getLong("couponid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.receiver = jsonObj.getLong("receiver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.has_read = jsonObj.getInt("has_read"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return notify_person;
	}


	public static SVCNotifyPerson decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCNotifyPerson notify_person = new SVCNotifyPerson();

		try { notify_person.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.title = resultSet.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.msg = resultSet.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.msg_type = resultSet.getInt("msg_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.couponid = resultSet.getLong("couponid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.receiver = resultSet.getLong("receiver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.has_read = resultSet.getInt("has_read"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notify_person.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return notify_person;
	}


}
