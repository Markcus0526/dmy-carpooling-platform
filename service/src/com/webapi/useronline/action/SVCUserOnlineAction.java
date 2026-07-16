package com.webapi.useronline.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.structure.SVCResult;
import com.webapi.useronline.service.SVCUserOnlineService;

public class SVCUserOnlineAction
{
	private String source = "";
	private long userid = -1;
	private long driverid = -1;
	private double lat = -1;
	private double lng = -1;
	private String devtoken = "";


	private JSONObject result = new JSONObject();


	private SVCUserOnlineService online_service = new SVCUserOnlineService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}


	public String reportDriverPos()
	{
		convertParamsToUTF8();

		SVCResult stResult = online_service.reportDriverPos(source, userid, lat, lng, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String driverPos()
	{
		convertParamsToUTF8();

		SVCResult stResult = online_service.driverPos(source, userid, driverid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public void setSource(String source) {
		this.source = source;
	}


	public void setUserid(long userid) {
		this.userid = userid;
	}


	public void setDriverid(long driverid) {
		this.driverid = driverid;
	}


	public void setLat(double lat) {
		this.lat = lat;
	}


	public void setLng(double lng) {
		this.lng = lng;
	}


	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}


	public JSONObject getResult() {
		return result;
	}

}
