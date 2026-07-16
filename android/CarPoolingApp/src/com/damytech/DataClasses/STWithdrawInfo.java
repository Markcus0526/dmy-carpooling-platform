package com.damytech.DataClasses;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

public class STWithdrawInfo
{
	public long uid = 0;
	public String req_date = "";
    public double balance = 0.0f;
	public int state = 1;

	public static STWithdrawInfo decodeFromJSON(JSONObject jsonObj)
	{
		STWithdrawInfo retData = new STWithdrawInfo();

        try { retData.uid = jsonObj.getLong("uid"); } catch (Exception ex) {}
        try { retData.req_date = jsonObj.getString("req_date"); } catch (Exception ex) {}
        try { retData.balance = jsonObj.getDouble("balance"); } catch (Exception ex) {}
        try { retData.state = jsonObj.getInt("state"); } catch (Exception ex) {}

		return retData;
	}
}
