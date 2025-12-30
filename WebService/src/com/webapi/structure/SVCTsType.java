package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCTsType {
	public long id = 0;
	public String tx_code = "";
	public String comment = "";
	public String comment_mobile = "";
	public int deleted = 0;

	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("tx_code", tx_code); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("comment", comment); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("comment_mobile", comment_mobile); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}



	public static SVCTsType decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCTsType ts_type = new SVCTsType();

		try { ts_type.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_type.tx_code = jsonObj.getString("tx_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_type.comment = jsonObj.getString("comment"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_type.comment_mobile = jsonObj.getString("comment_mobile"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_type.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return ts_type;
	}


	public static SVCTsType decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCTsType ts_type = new SVCTsType();

		try { ts_type.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_type.tx_code = resultSet.getString("tx_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_type.comment = resultSet.getString("comment"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_type.comment_mobile = resultSet.getString("comment_mobile"); } catch (Exception ex) { ex.printStackTrace(); }
		try { ts_type.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return ts_type;
	}

}
