package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KimHM
 * Date: 14-9-14
 * Time: 下午11:13
 * To change this template use File | Settings | File Templates.
 */
public class STDrvOnoffOrder
{
	public long uid = 0;
	public String order_num = "";
	public long pass_id = 0;
	public String pass_img = "";
	public String pass_name = "";
	public int pass_gender = 0;
	public int pass_age = 0;
	public double price = 0;
	public double sysinfo_fee = 0;
	public String sysinfo_fee_desc = "";
	public String start_addr = "";
	public String end_addr = "";
	public int midpoints = 0;
	public String midpoints_desc = "";
	public String start_time = "";
	public String start_time_desc = "";
	public String days = "";
	public String create_time = "";


	public static STDrvOnoffOrder decodeFromJSON(JSONObject jsonObj)
	{
		STDrvOnoffOrder orderInfo = new STDrvOnoffOrder();

		try { orderInfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.order_num = jsonObj.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.pass_id = jsonObj.getLong("pass_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.pass_img = jsonObj.getString("pass_img"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.pass_name = jsonObj.getString("pass_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.pass_gender = jsonObj.getInt("pass_gender"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.pass_age = jsonObj.getInt("pass_age"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.sysinfo_fee = jsonObj.getDouble("sysinfo_fee"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.sysinfo_fee_desc = jsonObj.getString("sysinfo_fee_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.start_addr = jsonObj.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.end_addr = jsonObj.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.midpoints = jsonObj.getInt("midpoints"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.midpoints_desc = jsonObj.getString("midpoints_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.start_time = jsonObj.getString("start_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.start_time_desc = jsonObj.getString("start_time_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.days = jsonObj.getString("days"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderInfo.create_time = jsonObj.getString("create_time"); } catch (Exception ex) { ex.printStackTrace(); }

		return orderInfo;
	}
}
