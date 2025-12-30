package com.webapi.structure;

import net.sf.json.JSONObject;

public class STPushNotificationData {
	public String title = "";
	public String description = "";
	public STPushNotificationCustomData custom_data = new STPushNotificationCustomData();

	public JSONObject encodeToJSON()
	{
		JSONObject result = new JSONObject();

		result.put("title", title);
		result.put("description", description);
		result.put("custom_content", custom_data == null ? "" : custom_data.encodeToJSON().toString());

		return result;
	}
}
