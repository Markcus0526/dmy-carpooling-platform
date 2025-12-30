package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCAppSpreadUnit {
	public long id = 0;
	public String unit_id = "";
	public String name = "";
	public Date create_date = null;
	public String linkname = "";
	public String linkphone = "";
	public String group_property = "";
	public String contract_no = "";
	public String fix_phone = "";
	public String email = "";
	public String fax = "";
	public String group_address = "";
	public String invite_code = "";
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
	public String goods = "";
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonResult = new JSONObject();

		try { jsonResult.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("unit_id", unit_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("name", name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("create_date", create_date); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("linkname", linkname); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("linkphone", linkphone); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("group_property", group_property); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("contract_no", contract_no); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("fix_phone", fix_phone); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("email", email); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("fax", fax); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("group_address", group_address); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("invite_code", invite_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("balance_ts", balance_ts); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("ratio_as_passenger_self", ratio_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("integer_as_passenger_self", integer_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("active_as_passenger_self", active_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("ratio_as_driver_self", ratio_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("integer_as_driver_self", integer_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("active_as_driver_self", active_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("limit_way", limit_way); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("limit_month_as_passenger_self", limit_month_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("limit_month_as_driver_self", limit_month_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("limit_count_as_passenger_self", limit_count_as_passenger_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("limit_count_as_driver_self", limit_count_as_driver_self); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("goods", goods); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonResult.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonResult;
	}


	public static SVCAppSpreadUnit decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCAppSpreadUnit unitinfo = new SVCAppSpreadUnit();

		try { unitinfo.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.unit_id = jsonObj.getString("unit_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.create_date = ApiGlobal.String2Date(jsonObj.getString("create_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.linkname = jsonObj.getString("linkname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.linkphone = jsonObj.getString("linkphone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.group_property = jsonObj.getString("group_property"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.contract_no = jsonObj.getString("contract_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.fix_phone = jsonObj.getString("fix_phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.email = jsonObj.getString("email"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.fax = jsonObj.getString("fax"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.group_address = jsonObj.getString("group_address"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.invite_code = jsonObj.getString("invite_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.balance_ts = jsonObj.getLong("balance_ts"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.ratio_as_passenger_self = jsonObj.getDouble("ratio_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.integer_as_passenger_self = jsonObj.getDouble("integer_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.active_as_passenger_self = jsonObj.getInt("active_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.ratio_as_driver_self = jsonObj.getDouble("ratio_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.integer_as_driver_self = jsonObj.getDouble("integer_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.active_as_driver_self = jsonObj.getInt("active_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_way = jsonObj.getInt("limit_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_month_as_passenger_self = jsonObj.getInt("limit_month_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_month_as_driver_self = jsonObj.getInt("limit_month_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_count_as_passenger_self = jsonObj.getInt("limit_count_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_count_as_driver_self = jsonObj.getInt("limit_count_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.goods = jsonObj.getString("goods"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return unitinfo;
	}


	public static SVCAppSpreadUnit decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCAppSpreadUnit unitinfo = new SVCAppSpreadUnit();

		try { unitinfo.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.unit_id = resultSet.getString("unit_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.name = resultSet.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.create_date = ApiGlobal.String2Date(resultSet.getString("create_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.linkname = resultSet.getString("linkname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.linkphone = resultSet.getString("linkphone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.group_property = resultSet.getString("group_property"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.contract_no = resultSet.getString("contract_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.fix_phone = resultSet.getString("fix_phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.email = resultSet.getString("email"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.fax = resultSet.getString("fax"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.group_address = resultSet.getString("group_address"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.invite_code = resultSet.getString("invite_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.balance_ts = resultSet.getLong("balance_ts"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.ratio_as_passenger_self = resultSet.getDouble("ratio_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.integer_as_passenger_self = resultSet.getDouble("integer_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.active_as_passenger_self = resultSet.getInt("active_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.ratio_as_driver_self = resultSet.getDouble("ratio_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.integer_as_driver_self = resultSet.getDouble("integer_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.active_as_driver_self = resultSet.getInt("active_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_way = resultSet.getInt("limit_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_month_as_passenger_self = resultSet.getInt("limit_month_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_month_as_driver_self = resultSet.getInt("limit_month_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_count_as_passenger_self = resultSet.getInt("limit_count_as_passenger_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.limit_count_as_driver_self = resultSet.getInt("limit_count_as_driver_self"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.goods = resultSet.getString("goods"); } catch (Exception ex) { ex.printStackTrace(); }
		try { unitinfo.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }


		return unitinfo;
	}
}
