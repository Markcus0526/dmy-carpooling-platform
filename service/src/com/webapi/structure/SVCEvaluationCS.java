package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCEvaluationCS {
	public long id = 0;
	public long from_userid = 0;
	public long to_userid = 0;
	public int level = 0;
	public String msg = "";
	public Date ps_date = null;
	public int usertype = 0;
	public long order_cs_id = 0;
	public int blocked = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("from_userid", from_userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("to_userid", to_userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("level", level); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("msg", msg); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_date", ApiGlobal.Date2String(ps_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("usertype", usertype); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("blocked", blocked); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCEvaluationCS decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCEvaluationCS evaluation = new SVCEvaluationCS();

		try { evaluation.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.from_userid = jsonObj.getLong("from_userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.to_userid = jsonObj.getLong("to_userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.level = jsonObj.getInt("level"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.msg = jsonObj.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.ps_date = ApiGlobal.String2Date(jsonObj.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.usertype = jsonObj.getInt("usertype"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.order_cs_id = jsonObj.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.blocked = jsonObj.getInt("blocked"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return evaluation;
	}



	public static SVCEvaluationCS decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCEvaluationCS evaluation = new SVCEvaluationCS();

		try { evaluation.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.from_userid = resultSet.getLong("from_userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.to_userid = resultSet.getLong("to_userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.level = resultSet.getInt("level"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.msg = resultSet.getString("msg"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.ps_date = ApiGlobal.String2Date(resultSet.getString("ps_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.usertype = resultSet.getInt("usertype"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.order_cs_id = resultSet.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.blocked = resultSet.getInt("blocked"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evaluation.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return evaluation;
	}



}
