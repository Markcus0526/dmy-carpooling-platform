package com.damytech.DataClasses;

import com.damytech.Utils.mutil.Utils;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-13
 * Time: 上午10:44
 * To change this template use File | Settings | File Templates.
 */
public class STDrvOrderListItem {
	public long uid = 0;
	public String order_num = "";
	public int type = 1;
	public String type_desc = "";
	public long pass_id = 0;
	public String pass_img = "";
	public String pass_name = "";
	public int pass_gender = 0;
	public int pass_age = 0;
	public int pass_count = 0;
	public double price = 0;
	public double sysinfo_fee = 0;
	public String start_addr = "";
	public String end_addr = "";
    public String start_city = "";
    public String end_city = "";
	public int evaluated = 0;
	public String eval_content = "";
	public String evaluated_desc = "";
	public String start_time = "";
	public int state = 1;
	public String state_desc = "";
	public String create_time = "";
    public double insuranceFee;
    public String pass_phone;
    public double totalDistance = 0;


	public static STDrvOrderListItem decodeFromJSON(JSONObject jsonObj)
	{
		STDrvOrderListItem orderItem = new STDrvOrderListItem();
        try { orderItem.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.order_num = jsonObj.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.type = jsonObj.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.type_desc = jsonObj.getString("type_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.pass_id = jsonObj.getLong("pass_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.pass_img = jsonObj.getString("pass_img"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.pass_name = jsonObj.getString("pass_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.pass_gender = jsonObj.getInt("pass_gender"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.pass_age = jsonObj.getInt("pass_age"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.pass_count = jsonObj.getInt("pass_count"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.sysinfo_fee = jsonObj.getDouble("sysinfo_fee"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.start_addr = jsonObj.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.end_addr = jsonObj.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderItem.start_city = jsonObj.getString("start_city"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderItem.end_city = jsonObj.getString("end_city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.evaluated = jsonObj.getInt("evaluated"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.eval_content = jsonObj.getString("eval_content"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.evaluated_desc = jsonObj.getString("evaluated_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.start_time = jsonObj.getString("start_time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.state = jsonObj.getInt("state"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.state_desc = jsonObj.getString("state_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderItem.create_time = jsonObj.getString("create_time"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderItem.insuranceFee = jsonObj.getDouble("insu_fee"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderItem.pass_phone = jsonObj.getString("pass_phone"); } catch (Exception ex) { ex.printStackTrace(); }
        try {
            orderItem.totalDistance = jsonObj.getDouble("total_distance"); } catch (Exception ex) { ex.printStackTrace(); }

		return orderItem;
	}

}
