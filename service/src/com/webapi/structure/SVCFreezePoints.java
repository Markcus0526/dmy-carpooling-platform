package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCFreezePoints {
	public long id = 0;
	public long userid = 0;
	public String source = "";
	public long adminid = 0;
	public double balance = 0;
	public int state = 0;
	public long freeze_ts_id = 0;
	public long release_ts_id = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("source", source); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("adminid", adminid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("balance", balance); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("state", state); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("freeze_ts_id", freeze_ts_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("release_ts_id", release_ts_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCFreezePoints decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCFreezePoints points = new SVCFreezePoints();

		try { points.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.source = jsonObj.getString("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.adminid = jsonObj.getLong("adminid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.balance = jsonObj.getDouble("balance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.state = jsonObj.getInt("state"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.freeze_ts_id = jsonObj.getLong("freeze_ts_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.release_ts_id = jsonObj.getLong("release_ts_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return points;
	}



	public static SVCFreezePoints decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCFreezePoints points = new SVCFreezePoints();

		try { points.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.source = resultSet.getString("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.adminid = resultSet.getLong("adminid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.balance = resultSet.getDouble("balance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.state = resultSet.getInt("state"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.freeze_ts_id = resultSet.getLong("freeze_ts_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.release_ts_id = resultSet.getLong("release_ts_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { points.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return points;
	}

}


