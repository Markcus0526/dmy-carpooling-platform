package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCGroup {
	public long id = 0;
	public String groupid = "";
	public String group_name = "";
	public Date create_date = null;
	public String linkname = "";
	public String linkphone = "";
	public String group_property = "";
	public String contract_no = "";
	public String fix_phone = "";
	public String email = "";
	public String fax = "";
	public String group_address = "";
	public Date sign_time = null;
	public String invitecode_self = "";
	public long balance_ts = 0;
	public double ratio_as_passenger_self = 0;
	public double integer_as_passenger_self = 0;
	public int active_as_passenger_self = 0;
	public double ratio_as_driver_self = 0;
	public double integer_as_driver_self = 0;
	public int active_as_driver_self = 0;
	public int limit_way = 3;
	public int limit_month_as_passenger_self = 0;
	public int limit_month_as_driver_self = 0;
	public int limit_count_as_passenger_self = 0;
	public int limit_count_as_driver_self = 0;
	public String remark = "";
	public int deleted = 0;

	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("groupid", groupid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("group_name", group_name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("create_date", create_date); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("linkname", linkname); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("linkphone", linkphone); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("group_property", group_property); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("contract_no", contract_no); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("fix_phone", fix_phone); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("email", email); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("fax", fax); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("group_address", group_address); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sign_time", sign_time); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("invitecode_self", invitecode_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("balance_ts", balance_ts); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ratio_as_passenger_self", ratio_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("integer_as_passenger_self", integer_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("active_as_passenger_self", active_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ratio_as_driver_self", ratio_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("integer_as_driver_self", integer_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("active_as_driver_self", active_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_way", limit_way); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_month_as_passenger_self", limit_month_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_month_as_driver_self", limit_month_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_count_as_passenger_self", limit_count_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_count_as_driver_self", limit_count_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("remark", remark); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCGroup decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCGroup group_info = new SVCGroup();

		try { group_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.groupid = jsonObj.getString("groupid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.group_name = jsonObj.getString("group_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.create_date = ApiGlobal.String2Date(jsonObj.getString("create_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.linkname = jsonObj.getString("linkname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.linkphone = jsonObj.getString("linkphone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.group_property = jsonObj.getString("group_property"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.contract_no = jsonObj.getString("contract_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.fix_phone = jsonObj.getString("fix_phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.email = jsonObj.getString("email"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.fax = jsonObj.getString("fax"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.group_address = jsonObj.getString("group_address"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.sign_time = ApiGlobal.String2Date(jsonObj.getString("sign_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.invitecode_self = jsonObj.getString("invitecode_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.balance_ts = jsonObj.getLong("balance_ts"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.ratio_as_passenger_self = jsonObj.getDouble("ratio_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.integer_as_passenger_self = jsonObj.getDouble("integer_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.active_as_passenger_self = jsonObj.getInt("active_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.ratio_as_driver_self = jsonObj.getDouble("ratio_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.integer_as_driver_self = jsonObj.getDouble("integer_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.active_as_driver_self = jsonObj.getInt("active_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_way = jsonObj.getInt("limit_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_month_as_passenger_self = jsonObj.getInt("limit_month_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_month_as_driver_self = jsonObj.getInt("limit_month_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_count_as_passenger_self = jsonObj.getInt("limit_count_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_count_as_driver_self = jsonObj.getInt("limit_count_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return group_info;
	}


	public static SVCGroup decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCGroup group_info = new SVCGroup();

		try { group_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.groupid = resultSet.getString("groupid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.group_name = resultSet.getString("group_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.create_date = ApiGlobal.String2Date(resultSet.getString("create_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.linkname = resultSet.getString("linkname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.linkphone = resultSet.getString("linkphone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.group_property = resultSet.getString("group_property"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.contract_no = resultSet.getString("contract_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.fix_phone = resultSet.getString("fix_phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.email = resultSet.getString("email"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.fax = resultSet.getString("fax"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.group_address = resultSet.getString("group_address"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.sign_time = ApiGlobal.String2Date(resultSet.getString("sign_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.invitecode_self = resultSet.getString("invitecode_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.balance_ts = resultSet.getLong("balance_ts"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.ratio_as_passenger_self = resultSet.getDouble("ratio_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.integer_as_passenger_self = resultSet.getDouble("integer_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.active_as_passenger_self = resultSet.getInt("active_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.ratio_as_driver_self = resultSet.getDouble("ratio_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.integer_as_driver_self = resultSet.getDouble("integer_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.active_as_driver_self = resultSet.getInt("active_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_way = resultSet.getInt("limit_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_month_as_passenger_self = resultSet.getInt("limit_month_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_month_as_driver_self = resultSet.getInt("limit_month_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_count_as_passenger_self = resultSet.getInt("limit_count_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.limit_count_as_driver_self = resultSet.getInt("limit_count_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.remark = resultSet.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }
		try { group_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return group_info;
	}
}
