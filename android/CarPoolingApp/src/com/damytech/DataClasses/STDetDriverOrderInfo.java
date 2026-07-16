package com.damytech.DataClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by RiGSNote on 14-9-16.
 */
public class STDetDriverOrderInfo
{
    public long     uid;
    public String   order_num;
    public ArrayList<STPassengerInfo> pass_list = new ArrayList<STPassengerInfo>(0);
    public int      left_seat;
    public double   price;
    public double   sysinfo_fee;
    public String   sysinfo_fee_desc;
    public String   start_addr;
    public String   end_addr;
    public String   left_days;
    public String   valid_days;
    public ArrayList<STMidPoint> mid_points = new ArrayList<STMidPoint>(0);
    public String   start_time;
    public String   create_time;
    public String   accept_time;
    public int      state;
    public String   state_desc;

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
	public String   remark = "";


    public static STDetDriverOrderInfo decodeFromJSON(JSONObject jsonObj) {
        STDetDriverOrderInfo dataInfo = new STDetDriverOrderInfo();

        try { dataInfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.order_num = jsonObj.getString("order_num"); } catch (Exception ex) { ex.printStackTrace(); }

        try {
            JSONArray passlist = jsonObj.getJSONArray("pass_list");
            for (int i = 0; i < passlist.length(); i++)
            {
                // parse passenger info
                JSONObject obj = passlist.getJSONObject(i);
                STPassengerInfo passInfo = STPassengerInfo.decodeFromJSON(obj);
                // add one passenger info
                dataInfo.pass_list.add(passInfo);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        try { dataInfo.left_seat = jsonObj.getInt("left_seat"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.sysinfo_fee = jsonObj.getDouble("sysinfo_fee"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.sysinfo_fee_desc = jsonObj.getString("sysinfo_fee_descc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.start_addr = jsonObj.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.end_addr = jsonObj.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.valid_days = jsonObj.getString("valid_days"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.left_days = jsonObj.getString("left_days"); } catch (Exception ex) { ex.printStackTrace(); }

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
        try { dataInfo.start_time = jsonObj.getString("start_time"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.create_time = jsonObj.getString("create_time"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.accept_time = jsonObj.getString("accept_time"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.state = jsonObj.getInt("state"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.state_desc = jsonObj.getString("state_desc"); } catch (Exception ex) { ex.printStackTrace(); }

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

	    try { dataInfo.remark = jsonObj.getString("remark"); } catch (Exception ex) { ex.printStackTrace(); }

        return dataInfo;
    }
}
