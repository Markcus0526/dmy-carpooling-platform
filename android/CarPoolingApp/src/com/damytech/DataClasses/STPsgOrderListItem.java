package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-19
 * Time: 下午3:08
 * To change this template use File | Settings | File Templates.
 */
public class STPsgOrderListItem {
	public long uid = 0;
	public String order_num = "";
	public int type = ConstData.ORDER_TYPE_ONCE;
	public String type_desc = "";
	public long driver_id = 0;
	public String driver_img = "";
	public String driver_name = "";
	public int driver_gender = ConstData.GENDER_MALE;
	public int driver_age = 0;
	public double price = 0;
    public String start_city = "";
    public String end_city = "";
	public String start_addr = "";
	public String end_addr = "";
	public String start_time = "";
	public int evaluated = 0;
	public String eval_content = "";
	public String evaluated_desc = "";
	public int state = ConstData.ORDER_STATE_DRV_ACCEPTED;
	public String state_desc = "";
	public String create_time = "";


	public static STPsgOrderListItem decodeFromJSON(JSONObject jsonObj)
	{
		STPsgOrderListItem orderListItem = new STPsgOrderListItem();

		try { orderListItem.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.order_num = jsonObj.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.type = jsonObj.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.type_desc = jsonObj.getString("type_desc");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.driver_id = jsonObj.getLong("driver_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.driver_img = jsonObj.getString("driver_img");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.driver_name = jsonObj.getString("driver_name");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.driver_gender = jsonObj.getInt("driver_gender"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.driver_age = jsonObj.getInt("driver_age"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderListItem.start_city = jsonObj.getString("start_city");; } catch (Exception ex) { ex.printStackTrace(); }
        try { orderListItem.end_city = jsonObj.getString("end_city");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.start_addr = jsonObj.getString("start_addr");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.end_addr = jsonObj.getString("end_addr");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.start_time = jsonObj.getString("start_time");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.evaluated = jsonObj.getInt("evaluated"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.eval_content = jsonObj.getString("eval_content");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.evaluated_desc = jsonObj.getString("evaluated_desc");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.state = jsonObj.getInt("state"); } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.state_desc = jsonObj.getString("state_desc");; } catch (Exception ex) { ex.printStackTrace(); }
		try { orderListItem.create_time = jsonObj.getString("create_time");; } catch (Exception ex) { ex.printStackTrace(); }

		return orderListItem;
	}

}
