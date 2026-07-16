package com.webapi.structure;

import com.webapi.common.ConstMgr;

import net.sf.json.JSONObject;

// Template container class to return data.
public class SVCResult
{
	// Return code
	public int retcode = ConstMgr.ErrCode_None;

	// Return description
	public String retmsg = ConstMgr.ErrMsg_None;

	// Actual return data
	public Object retdata = new JSONObject();


	// Method to encode to JSON format
	public JSONObject encodeToJSON()
	{
		JSONObject ret = new JSONObject();

		try
		{
			ret.put("retcode", retcode);

			if (retmsg == null)
				retmsg = "";
			ret.put("retmsg", retmsg);

			if (retdata != null)
				ret.put("retdata", retdata);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			ret = null;
		}

		return ret;
	}

}
