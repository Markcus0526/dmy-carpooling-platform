package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCProfitSharingTree {
	public long id = 0;
	public long order_cs_id = 0;
	public String branch_no = "";
	public long node1 = 0;
	public long node2 = 0;
	public double ps_value = 0;
	public int ps_type = 0;
	public double value = 0;
	public String node1_source = "1";
	public String node2_source = "1";
	public int deleted = 0;

	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("order_cs_id", order_cs_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("branch_no", branch_no); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("node1", node1); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("node2", node2); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_value", ps_value); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("ps_type", ps_type); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("value", value); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("node1_source", node1_source); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("node2_source", node2_source); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCProfitSharingTree decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCProfitSharingTree share_info = new SVCProfitSharingTree();

		try { share_info.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.order_cs_id = jsonObj.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.branch_no = jsonObj.getString("branch_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.node1 = jsonObj.getLong("node1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.node2 = jsonObj.getLong("node2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.ps_value = jsonObj.getDouble("ps_value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.ps_type = jsonObj.getInt("ps_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.value = jsonObj.getDouble("value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.node1_source = jsonObj.getString("node1_source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.node2_source = jsonObj.getString("node2_source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return share_info;
	}


	public static SVCProfitSharingTree decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCProfitSharingTree share_info = new SVCProfitSharingTree();

		try { share_info.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.order_cs_id = resultSet.getLong("order_cs_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.branch_no = resultSet.getString("branch_no"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.node1 = resultSet.getLong("node1"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.node2 = resultSet.getLong("node2"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.ps_value = resultSet.getDouble("ps_value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.ps_type = resultSet.getInt("ps_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.value = resultSet.getDouble("value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.node1_source = resultSet.getString("node1_source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.node2_source = resultSet.getString("node2_source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { share_info.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return share_info;
	}


}
