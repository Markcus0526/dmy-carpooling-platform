package com.damytech.DataClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by RiGSNote on 14-9-16.
 */
public class STDetPassOrderInfo
{
    public long     uid;
    public String   order_num;
    public STDriverInfo driver_info = new STDriverInfo();
    public double   price;
    public String   price_desc;
    public int      left_seats;
    public String   start_addr;
    public String   end_addr;
    public String   start_city;
    public String   end_city;
    public double start_lat;
    public double start_lng;
    public double end_lat;
    public double end_lng;
    public String   leftdays;
    public String   days;
    public ArrayList<STMidPoint> mid_points = new ArrayList<STMidPoint>(0);
    public String password;
    public String   start_time;
    public String   create_time;
    public String   accept_time;
    public String startsrv_time;
    public int      state;
    public String   state_desc;
    public int evaluated;
    public String eval_content;
    public String evaluated_desc;

	// order statistics
    public int      total_count;
    public String   total_count_desc;
    public int      total_income;
    public String   total_income_desc;
    public int      evgood_count;
    public String   evgood_count_desc;
    public int      evnormal_count;
    public String   evnormal_count_desc;
    public int      evbad_count;
    public String   evbad_count_desc;

    public static STDetPassOrderInfo decodeFromJSON(JSONObject jsonObj) {
        STDetPassOrderInfo dataInfo = new STDetPassOrderInfo();

        try { dataInfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.order_num = jsonObj.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }

        try {
                JSONObject drvlist = jsonObj.getJSONObject("driver_info");

                dataInfo.driver_info = STDriverInfo.decodeFromJSON(drvlist);

        } catch (Exception ex) { ex.printStackTrace(); }

        try { dataInfo.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); dataInfo.price = 0; }
        try { dataInfo.price_desc = jsonObj.getString("price_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.left_seats = jsonObj.getInt("left_seats"); } catch (Exception ex) { ex.printStackTrace(); dataInfo.left_seats = 0; }
        try { dataInfo.start_city = jsonObj.getString("start_city"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.end_city = jsonObj.getString("end_city"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.start_addr = jsonObj.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.start_lat = jsonObj.getDouble("start_lat"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.start_lng = jsonObj.getDouble("start_lng"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.end_addr = jsonObj.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.end_lat = jsonObj.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.end_lng = jsonObj.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.leftdays = jsonObj.getString("leftdays"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.days = jsonObj.getString("days"); } catch (Exception ex) { ex.printStackTrace(); }

        try {
            JSONArray midlist = jsonObj.getJSONArray("mid_points");
            for (int i = 0; i < midlist.length(); i++)
            {
                // parse mid point info
                JSONObject obj = midlist.getJSONObject(i);
                STMidPoint midpointInfo = STMidPoint.decodeFromJSON(obj);
                // add one mid point info
                dataInfo.mid_points.add(midpointInfo);
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.password = jsonObj.getString("password"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.start_time = jsonObj.getString("start_time"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.create_time = jsonObj.getString("create_time"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.accept_time = jsonObj.getString("accept_time"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.startsrv_time = jsonObj.getString("startsrv_time"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.state = jsonObj.getInt("state"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.state_desc = jsonObj.getString("state_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.evaluated = jsonObj.getInt("evaluated"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.eval_content = jsonObj.getString("eval_content"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.evaluated_desc = jsonObj.getString("evaluated_desc"); } catch (Exception ex) { ex.printStackTrace(); }

		JSONObject objStati = null;
        try {
            objStati = jsonObj.getJSONObject("statistics");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (objStati != null)
        {
            try { dataInfo.total_count = objStati.getInt("total_count"); } catch (Exception ex) { ex.printStackTrace(); }
            try { dataInfo.total_count_desc = objStati.getString("total_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
            try { dataInfo.total_income = objStati.getInt("total_income"); } catch (Exception ex) { ex.printStackTrace(); }
            try { dataInfo.total_income_desc = objStati.getString("total_income_desc"); } catch (Exception ex) { ex.printStackTrace(); }
            try { dataInfo.evgood_count = objStati.getInt("evgood_count"); } catch (Exception ex) { ex.printStackTrace(); }
            try { dataInfo.evgood_count_desc = objStati.getString("evgood_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
            try { dataInfo.evnormal_count = objStati.getInt("evnormal_count"); } catch (Exception ex) { ex.printStackTrace(); }
            try { dataInfo.evnormal_count_desc = objStati.getString("evnormal_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
            try { dataInfo.evbad_count = objStati.getInt("evbad_count"); } catch (Exception ex) { ex.printStackTrace(); }
            try { dataInfo.evbad_count_desc = objStati.getString("evbad_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        }

        return dataInfo;
    }
}
