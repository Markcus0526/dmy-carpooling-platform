package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created by KimHM on 2014-10-27.
 */
public class STSuggestionItem
{
	public String name = "";
	public String city = "";
	public String district = "";
	public String business = "";
	public String cityid = "";

	public static STSuggestionItem decodeFromJSON(JSONObject jsonObj)
	{
		STSuggestionItem item = new STSuggestionItem();

		try { item.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.city = jsonObj.getString("city"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.district = jsonObj.getString("district"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.business = jsonObj.getString("business"); } catch (Exception ex) { ex.printStackTrace(); }
		try { item.cityid = jsonObj.getString("cityid"); } catch (Exception ex) { ex.printStackTrace(); }

		return item;
	}
}
