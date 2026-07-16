package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCGroupAssociation {
	public long id = 0;
	public String ga_code = "";
	public String ga_name = "";
	public String linkname = "";
	public String linkphone = "";
	public String group_property = "";
	public String contract_no = "";
	public String fix_phone = "";
	public String email = "";
	public String fax = "";
	public String group_address = "";
	public Date sign_time = null;
	public String desc = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ga_code", ga_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ga_name", ga_name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("linkname", linkname); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("linkphone", linkphone); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("group_property", group_property); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("contract_no", contract_no); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("fix_phone", fix_phone); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("email", email); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("fax", fax); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("group_address", group_address); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sign_time", ApiGlobal.Date2String(sign_time, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("desc", desc); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCGroupAssociation decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCGroupAssociation assoc = new SVCGroupAssociation();

		try { assoc.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.ga_code = jsonObj.getString("ga_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.ga_name = jsonObj.getString("ga_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.linkname = jsonObj.getString("linkname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.linkphone = jsonObj.getString("linkphone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.group_property = jsonObj.getString("group_property"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.contract_no = jsonObj.getString("contract_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.fix_phone = jsonObj.getString("fix_phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.email = jsonObj.getString("email"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.fax = jsonObj.getString("fax"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.group_address = jsonObj.getString("group_address"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.sign_time = ApiGlobal.String2Date(jsonObj.getString("sign_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.desc = jsonObj.getString("desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return assoc;
	}


	public static SVCGroupAssociation decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCGroupAssociation assoc = new SVCGroupAssociation();

		try { assoc.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.ga_code = resultSet.getString("ga_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.ga_name = resultSet.getString("ga_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.linkname = resultSet.getString("linkname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.linkphone = resultSet.getString("linkphone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.group_property = resultSet.getString("group_property"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.contract_no = resultSet.getString("contract_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.fix_phone = resultSet.getString("fix_phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.email = resultSet.getString("email"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.fax = resultSet.getString("fax"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.group_address = resultSet.getString("group_address"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.sign_time = ApiGlobal.String2Date(resultSet.getString("sign_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.desc = resultSet.getString("desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { assoc.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return assoc;
	}


}


















