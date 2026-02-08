package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created by RiGSNote on 14-9-16.
 */
public class STMidPoint
{
    public long        uid;
    public double      lat;
    public double      lng;
    public String       address;

    public static STMidPoint decodeFromJSON(JSONObject jsonObj)
    {
        STMidPoint dataInfo = new STMidPoint();

        try { dataInfo.uid = jsonObj.getLong("index"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.lat = jsonObj.getDouble("lat"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.lng = jsonObj.getDouble("lng"); } catch (Exception ex) { ex.printStackTrace(); }
        try { dataInfo.address = jsonObj.getString("addr"); } catch (Exception ex) { ex.printStackTrace(); }

        return dataInfo;
    }
}
