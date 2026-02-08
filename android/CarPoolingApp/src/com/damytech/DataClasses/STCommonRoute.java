package com.damytech.DataClasses;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

public class STCommonRoute implements Parcelable
{
	public long uid = 0;
    public String startcity = "";
    public String endcity = "";
	public String startaddr = "";
	public String endaddr = "";
	public String create_time = "";
	public int daytype = 1;
    public double startlat = 0.0f;
    public double startlon = 0.0f;
    public double endlat = 0.0f;
    public double endlon = 0.0f;

    public STCommonRoute () {}

    public STCommonRoute (Parcel in)
    {
        readFromParcel(in);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(uid);
        dest.writeString(startcity);
        dest.writeString(endcity);
        dest.writeString(startaddr);
        dest.writeString(endaddr);
        dest.writeString(create_time);
        dest.writeInt(daytype);
        dest.writeDouble(startlat);
        dest.writeDouble(startlon);
        dest.writeDouble(endlat);
        dest.writeDouble(endlon);

        return;
    }

    private void readFromParcel(Parcel in)
    {
        uid = in.readLong();
        startcity = in.readString();
        endcity = in.readString();
        startaddr = in.readString();
        endaddr = in.readString();
        create_time = in.readString();
        daytype = in.readInt();
        startlat = in.readDouble();
        startlon = in.readDouble();
        endlat = in.readDouble();
        endlon = in.readDouble();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final Parcelable.Creator<STCommonRoute> CREATOR = new Parcelable.Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new STCommonRoute(source);
        }
        @Override
        public Object[] newArray(int size) {
            return new STCommonRoute[size];
        }
    };

	public static STCommonRoute decodeFromJSON(JSONObject jsonObj)
	{
		STCommonRoute retData = new STCommonRoute();

        try { retData.uid = jsonObj.getLong("uid"); } catch (Exception ex) {}
        try { retData.startcity = jsonObj.getString("startcity"); } catch (Exception ex) {}
        try { retData.endcity = jsonObj.getString("endcity"); } catch (Exception ex) {}
        try { retData.startaddr = jsonObj.getString("startaddr"); } catch (Exception ex) {}
        try { retData.endaddr = jsonObj.getString("endaddr"); } catch (Exception ex) {}
        try { retData.create_time = jsonObj.getString("create_time"); } catch (Exception ex) {}
        try { retData.daytype = jsonObj.getInt("daytype"); } catch (Exception ex) {}
        try { retData.startlat = jsonObj.getDouble("startlat"); } catch (Exception ex) {}
        try { retData.startlon = jsonObj.getDouble("startlng"); } catch (Exception ex) {}
        try { retData.endlat = jsonObj.getDouble("endlat"); } catch (Exception ex) {}
        try { retData.endlon = jsonObj.getDouble("endlng"); } catch (Exception ex) {}

		return retData;
	}
}
