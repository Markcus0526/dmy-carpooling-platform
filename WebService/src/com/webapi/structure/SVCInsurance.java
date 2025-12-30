package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;


import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;

public class SVCInsurance {
	public long id = 0;//主键
	public String appl_no;//投保单号，被保险人可以通过这个倒保险公司网站查询
	public Date effect_time = null;//保单生效日期
	public Date insexpr_date;//保单终止时间
	public double total_amount;//合计保额
	public String isd_name;//被保险人姓名
	public long isd_id = 0;//被保险人id
	public int insu_status = 0;//保单状态，0有效，1失效
	


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("effect_time", effect_time); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("insexpr_date", insexpr_date); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("appl_no", appl_no); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("total_amount", total_amount); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("isd_name", isd_name); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("isd_id", isd_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("insu_status", insu_status); } catch (Exception ex) { ex.printStackTrace(); }
	
		return jsonObj;
	}



	public static SVCInsurance decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCInsurance insurance = new SVCInsurance();

		try { insurance.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.appl_no = jsonObj.getString("appl_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.effect_time = ApiGlobal.String2Date(jsonObj.getString("effect_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.insexpr_date = ApiGlobal.String2Date(jsonObj.getString("insexpr_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.total_amount =jsonObj.getDouble("total_amount"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.isd_name =jsonObj.getString("isd_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.isd_id = jsonObj.getLong("isd_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.insu_status = jsonObj.getInt("insu_status"); } catch (Exception ex) { ex.printStackTrace(); }

		return insurance;
	}



	public static SVCInsurance decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCInsurance insurance = new SVCInsurance();

		try { insurance.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.appl_no = resultSet.getString("appl_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.effect_time = ApiGlobal.String2Date(resultSet.getString("effect_time")); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.insexpr_date = ApiGlobal.String2Date(resultSet.getString("insexpr_date")); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.total_amount = resultSet.getDouble("total_amount"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.isd_name = resultSet.getString("isd_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.isd_id = resultSet.getLong("isd_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { insurance.insu_status = resultSet.getInt("insu_status"); } catch (Exception ex) { ex.printStackTrace(); }

		return insurance;
	}


}
