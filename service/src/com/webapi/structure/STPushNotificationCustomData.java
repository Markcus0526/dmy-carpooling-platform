package com.webapi.structure;

import com.webapi.common.ConstMgr;

import net.sf.json.JSONObject;

/**
 * Created by KimHM on 2014-10-15.
 */
public class STPushNotificationCustomData {
	public static final int PNOTIF_TYPE_ONOFFORDER_ACCEPTED				= 1;
	public static final int PNOTIF_TYPE_ONOFFORDER_MUSTBEPUBLISHED		 = 2;
	public static final int PNOTIF_TYPE_LONGORDER_ACCEPTABLE			   = 3;
	public static final int PNOTIF_TYPE_ONOFFORDER_ACCEPTABLE			  = 4;
	public static final int PNOTIF_TYPE_ORDER_NEEDTOSTART				  = 5;
	public static final int PNOTIF_TYPE_OTHER								= 6;
	public static final int PNOTIF_TYPE_DRIVER_GRABORDER					= 7;//added by zyl,车主抢单

	public static final int USER_ROLE_DRIVER								= 1;
	public static final int USER_ROLE_PASSENGER								= 2;

	public int typecode = PNOTIF_TYPE_OTHER;
	public int userrole = USER_ROLE_PASSENGER;
	public long orderid = 0;
	public int ordertype = ConstMgr.ORDER_ONCE;


	public JSONObject encodeToJSON()
	{
		JSONObject result = new JSONObject();

		result.put("typecode", typecode);
		result.put("orderid", orderid);
		result.put("ordertype", ordertype);
		result.put("userrole", userrole);

		return result;
	}}
