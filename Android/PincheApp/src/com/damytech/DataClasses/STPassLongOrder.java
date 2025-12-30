package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: DavidYin
 * Date: 9/19/14
 * Time: 10:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class STPassLongOrder {
    public long uid = 0;
    public String order_num = "";
    public long driver_id = 0;
    public String driver_img = "";
    public String driver_name = "";
    public int driver_gender = 0;
    public int driver_age = 0;
    public double price = 0;
    public String price_desc = "";
    public String start_city = "";
    public String end_city = "";
    public String start_addr = "";
    public String end_addr = "";
    public String start_time = "2014-01-01 00:00:00";
    public String start_time_desc = "";
    public int left_seats = 0;
    public String left_seats_desc = "";
    public int grab_seats = 1;
    public String create_time = "2014-01-01 00:00:00";

    public static STPassLongOrder decodeFromJSON(JSONObject jsonObj)
    {
        STPassLongOrder orderInfo = new STPassLongOrder();

        try { orderInfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.order_num = jsonObj.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.driver_id = jsonObj.getLong("driver_id"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.driver_img = jsonObj.getString("driver_img"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.driver_name = jsonObj.getString("driver_name"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.driver_gender = jsonObj.getInt("driver_gender"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.driver_age = jsonObj.getInt("driver_age"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.price_desc = jsonObj.getString("price_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.start_city = jsonObj.getString("start_city"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.end_city = jsonObj.getString("end_city"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.start_addr = jsonObj.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.end_addr = jsonObj.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.start_time = jsonObj.getString("start_time"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.start_time_desc = jsonObj.getString("start_time_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.left_seats = jsonObj.getInt("leftseats"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.left_seats_desc = jsonObj.getString("leftseats_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { orderInfo.create_time = jsonObj.getString("create_time"); } catch (Exception ex) { ex.printStackTrace(); }


        return orderInfo;
    }
}
