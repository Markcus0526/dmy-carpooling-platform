package com.damytech.DataClasses;

import com.damytech.PincheApp.PassPubCityOrderActivity;
import org.json.JSONObject;

/**
 * Created by KimHM on 2014-10-27.
 */
public class STPlaceSearchItem
{
	public String name = "";
	public double lat = 0;
	public double lng = 0;
	public String address = "";
	public String telephone = "";
	public String uid = "";
	public String tag = "";
	public String detail_url = "";


	public static STPlaceSearchItem decodeFromJSON(JSONObject jsonObj)
	{
		STPlaceSearchItem item = new STPlaceSearchItem();

		try { item.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }

		try {
			JSONObject jsonLoc = jsonObj.getJSONObject("location");

			item.lat = jsonLoc.getDouble("lat");
			item.lng = jsonLoc.getDouble("lng");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try { item.address = jsonObj.getString("address"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.telephone = jsonObj.getString("telephone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.uid = jsonObj.getString("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.tag = jsonObj.getString("tag"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.detail_url = jsonObj.getString("detail_url"); } catch (Exception ex) { ex.printStackTrace(); }

		return item;
	}

}
