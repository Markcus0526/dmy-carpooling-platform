package com.nmdy.common;

import java.util.Map;

public class STPushNotificationData<CustomerData> {
	public static final int PNOTIF_TYPE_USER_VERIFY_FAIL = 101;
	public static final int PNOTIF_TYPE_DRIVER_VERIFY_FAIL = 102;

	String title = "";
	String description = "";
	STPushNotificationCustomData custom_content = new STPushNotificationCustomData();
	Aps aps = new Aps();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		aps.alert = description;
	}

	public Aps getAps() {
		return aps;
	}

	public void setAps(Aps aps) {
		this.aps = aps;
	}

	/*
	 * public JSONObject encodeToJSON() { JSONObject result = new JSONObject();
	 * 
	 * result.put("title", title); result.put("description", description);
	 * if(null != customData) result.put("custom_content", customData);
	 * 
	 * JSONObject aps = new JSONObject(); aps.put("alert",
	 * StringUtils.isBlank(description)?"提示":description); aps.put("sound", "");
	 * aps.put("badge", 0); result.put("aps", aps);
	 * 
	 * return result; }
	 */

	static public class STPushNotificationCustomData {
		public int typecode;
		Map data;

		public int getTypecode() {
			return typecode;
		}

		public void setTypecode(int typecode) {
			this.typecode = typecode;
		}

		public Map getData() {
			return data;
		}

		public void setData(Map data) {
			this.data = data;
		}
	}

	static public class Aps {
		String alert = "通知";
		String sound = "";
		int badge = 0;

		public String getAlert() {
			return alert;
		}

		public void setAlert(String alert) {
			this.alert = alert;
		}

		public String getSound() {
			return sound;
		}

		public void setSound(String sound) {
			this.sound = sound;
		}

		public int getBadge() {
			return badge;
		}

		public void setBadge(int badge) {
			this.badge = badge;
		}
	}

	public STPushNotificationCustomData getCustom_content() {
		return custom_content;
	}

	public void setCustom_content(STPushNotificationCustomData custom_content) {
		this.custom_content = custom_content;
	}
}
