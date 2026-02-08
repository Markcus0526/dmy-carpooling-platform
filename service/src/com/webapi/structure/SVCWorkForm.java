package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCWorkForm {
	public long id = 0;
	public String workform_num = "";
	public long adm_id = 0;
	public int form_type = 0;
	public int bussi_type = 0;
	public String phone_incoming = "";
	public long order_cs_id = 0;
	public String order_cs_no = "";
	public long order_dj_id = 0;
	public String order_dj_no = "";
	public String customer_name = "";
	public int sex = 0;
	public String city = "";
	public String reason = "";
	public int form_agree = 0;
	public String process_result = "";
	public int status = 0;
	public Date wf_date = null;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("workform_num", workform_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("adm_id", adm_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("form_type", form_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("bussi_type", bussi_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("phone_incoming", phone_incoming); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_no", order_cs_no); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_dj_id", order_dj_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_dj_no", order_dj_no); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("customer_name", customer_name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("sex", sex); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("city", city); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("reason", reason); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("form_agree", form_agree); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("process_result", process_result); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("status", status); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("wf_date", ApiGlobal.Date2String(wf_date, true)); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCWorkForm decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCWorkForm work_form = new SVCWorkForm();

		try { work_form.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.workform_num = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.adm_id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.form_type = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.bussi_type = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.phone_incoming = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.order_cs_id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.order_cs_no = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.order_dj_id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.order_dj_no = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.customer_name = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.sex = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.city = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.reason = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.form_agree = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.process_result = jsonObj.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.status = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.wf_date = ApiGlobal.String2Date(jsonObj.getString("id")); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.deleted = jsonObj.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }

		return work_form;
	}


	public static SVCWorkForm decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCWorkForm work_form = new SVCWorkForm();

		try { work_form.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.workform_num = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.adm_id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.form_type = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.bussi_type = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.phone_incoming = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.order_cs_id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.order_cs_no = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.order_dj_id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.order_dj_no = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.customer_name = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.sex = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.city = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.reason = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.form_agree = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.process_result = resultSet.getString("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.status = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.wf_date = ApiGlobal.String2Date(resultSet.getString("id")); } catch (Exception ex) { ex.printStackTrace(); }
		try { work_form.deleted = resultSet.getInt("id"); } catch (Exception ex) { ex.printStackTrace(); }

		return work_form;
	}

}







