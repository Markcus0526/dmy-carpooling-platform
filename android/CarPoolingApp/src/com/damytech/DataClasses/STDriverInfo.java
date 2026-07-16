package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created by RiGSNote on 14-9-16.
 */
public class STDriverInfo
{
    public long     uid;
    public String   image;
    public String   name;
    public int      gender;
    public int      age;
    public String   phone;
    public String   carimg;
    public int   drv_career;
    public String   drv_career_desc;
    public int      evgood_rate;
    public String   evgood_rate_desc;
    public int      carpool_count;
    public String   carpool_count_desc;
    public String carno;
    public String brand;
    public String style;
    public int type;
    public String color;

    // eval history array

    public static STDriverInfo decodeFromJSON(JSONObject jsonObj)
    {
        STDriverInfo driverInfo = new STDriverInfo();

        try { driverInfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.image = jsonObj.getString("img"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.gender = jsonObj.getInt("gender"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.age = jsonObj.getInt("age"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.phone = jsonObj.getString("phone"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.carimg = jsonObj.getString("carimg"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.drv_career = jsonObj.getInt("drv_career"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.drv_career_desc = jsonObj.getString("drv_career_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.evgood_rate = jsonObj.getInt("evgood_rate"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.evgood_rate_desc = jsonObj.getString("evgood_rate_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.carpool_count = jsonObj.getInt("carpool_count"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.carpool_count_desc = jsonObj.getString("carpool_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.carno = jsonObj.getString("carno"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.brand = jsonObj.getString("brand"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.style = jsonObj.getString("style"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.type = jsonObj.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
        try { driverInfo.color = jsonObj.getString("color"); } catch (Exception ex) { ex.printStackTrace(); }

        return driverInfo;
    }
}
