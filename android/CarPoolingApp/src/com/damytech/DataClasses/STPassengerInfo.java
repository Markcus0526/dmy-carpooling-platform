package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created by RiGSNote on 14-9-16.
 */
public class STPassengerInfo
{
    public long     uid;
    public String   name;
    public String   image;
    public int      sex;
    public int      age;
    public int      pverified;
    public String   pverified_desc;
    public double   goodeval_rate;
    public String   goodeval_rate_desc;
    public int      carpool_count;
    public String   carpool_count_desc;
    public int      state;
    public String   state_desc;
    public int      seat_count;
    public String   seat_count_desc;
    public String   phone;
    public int      evaluated;
    public String   evaluated_desc;
    public String   eval_content;

    // eval history array

    public static STPassengerInfo decodeFromJSON(JSONObject jsonObj)
    {
        STPassengerInfo passInfo = new STPassengerInfo();

        try { passInfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.image = jsonObj.getString("img"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.sex = jsonObj.getInt("gender"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.age = jsonObj.getInt("age"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.state = jsonObj.getInt("state"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.state_desc = jsonObj.getString("state_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.seat_count = jsonObj.getInt("seat_count"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.seat_count_desc = jsonObj.getString("seat_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.phone = jsonObj.getString("phone"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.goodeval_rate = jsonObj.getDouble("evgood_rate"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.goodeval_rate_desc = jsonObj.getString("evgood_rate_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.carpool_count = jsonObj.getInt("carpool_count"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.carpool_count_desc = jsonObj.getString("carpool_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.pverified = jsonObj.getInt("verified"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.pverified_desc = jsonObj.getString("verified_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.evaluated = jsonObj.getInt("evaluated"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.evaluated_desc = jsonObj.getString("evaluated_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { passInfo.eval_content = jsonObj.getString("eval_content"); } catch (Exception ex) { ex.printStackTrace(); }

        return passInfo;
    }
}
