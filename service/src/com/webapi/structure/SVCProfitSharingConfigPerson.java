package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCProfitSharingConfigPerson {
	public long id = 0;
	public double ratio_as_passenger = 0;
	public double integer_as_passenger = 0;
	public int active_as_passenger = 0;
	public double ratio_as_driver = 0;
	public double integer_as_driver = 0;
	public int active_as_driver = 0;
	public int limit_way = 3;
	public int limit_month_as_passenger = 0;
	public int limit_month_as_driver = 0;
	public int limit_count_as_passenger = 0;
	public int limit_count_as_driver = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ratio_as_passenger", ratio_as_passenger); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("integer_as_passenger", integer_as_passenger); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("active_as_passenger", active_as_passenger); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ratio_as_driver", ratio_as_driver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("integer_as_driver", integer_as_driver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("active_as_driver", active_as_driver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_way", limit_way); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_month_as_passenger", limit_month_as_passenger); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_month_as_driver", limit_month_as_driver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_count_as_passenger", limit_count_as_passenger); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("limit_count_as_driver", limit_count_as_driver); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCProfitSharingConfigPerson decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCProfitSharingConfigPerson config_info = new SVCProfitSharingConfigPerson();

		try { config_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.ratio_as_passenger = jsonObj.getDouble("ratio_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.integer_as_passenger = jsonObj.getDouble("integer_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.active_as_passenger = jsonObj.getInt("active_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.ratio_as_driver = jsonObj.getDouble("ratio_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.integer_as_driver = jsonObj.getDouble("integer_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.active_as_driver = jsonObj.getInt("active_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_way = jsonObj.getInt("limit_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_month_as_passenger = jsonObj.getInt("limit_month_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_month_as_driver = jsonObj.getInt("limit_month_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_count_as_passenger = jsonObj.getInt("limit_count_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_count_as_driver = jsonObj.getInt("limit_count_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return config_info;
	}


	public static SVCProfitSharingConfigPerson decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCProfitSharingConfigPerson config_info = new SVCProfitSharingConfigPerson();

		try { config_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.ratio_as_passenger = resultSet.getDouble("ratio_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.integer_as_passenger = resultSet.getDouble("integer_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.active_as_passenger = resultSet.getInt("active_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.ratio_as_driver = resultSet.getDouble("ratio_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.integer_as_driver = resultSet.getDouble("integer_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.active_as_driver = resultSet.getInt("active_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_way = resultSet.getInt("limit_way"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_month_as_passenger = resultSet.getInt("limit_month_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_month_as_driver = resultSet.getInt("limit_month_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_count_as_passenger = resultSet.getInt("limit_count_as_passenger"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.limit_count_as_driver = resultSet.getInt("limit_count_as_driver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { config_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return config_info;
	}

}
