package com.webapi.environment.action;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.environment.service.SVCEnvService;
import com.webapi.structure.SVCResult;

import net.sf.json.JSONObject;

public class SVCEnvAction
{
	// Input parameters
	private String source			= "";
	private long userid				= -1;
	private String city				= "";
	private double start_lat		= 0;
	private double start_lng		= 0;
	private double end_lat			= 0;
	private double end_lng			= 0;
	private String start_time		= "";
	private String midpoints		= "";
	private String devtoken			= "";
	private String inputUrl			= "";//分享功能参数
	private int shareFlag			= 0;//分享功能参数


	// Output parameters
	private JSONObject result = new JSONObject();


	SVCEnvService env_service = new SVCEnvService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		city = ApiGlobal.fixEncoding(city);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}


	public String defaultShareContents()
	{
		convertParamsToUTF8();

		SVCResult stResult = env_service.defaultShareContents(source, userid,inputUrl, shareFlag, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String sysPriceInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = env_service.sysPriceInfo(source, userid, city, start_lat, start_lng, end_lat, end_lng, start_time, midpoints, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String brandsAndColors()
	{
		convertParamsToUTF8();

		SVCResult stResult = env_service.brandsAndColors(source);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String infoFeeMethod()
	{
		convertParamsToUTF8();

		SVCResult stResult = env_service.infoFeeMethod(source, userid, city, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String shareLinkAddr()
	{
		convertParamsToUTF8();

		SVCResult stResult = env_service.shareLinkAddr(source);

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


	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}


	public JSONObject getResult() {
		return result;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public void setStart_lat(double start_lat) {
		this.start_lat = start_lat;
	}


	public void setStart_lng(double start_lng) {
		this.start_lng = start_lng;
	}


	public void setEnd_lat(double end_lat) {
		this.end_lat = end_lat;
	}


	public void setEnd_lng(double end_lng) {
		this.end_lng = end_lng;
	}


	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}


	public void setMidpoints(String midpoints) {
		this.midpoints = midpoints;
	}

	//------------分享功能参数-----------------
	public String getInputUrl() {
		return inputUrl;
	}


	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}


	public int getShareFlag() {
		return shareFlag;
	}


	public void setShareFlag(int shareFlag) {
		this.shareFlag = shareFlag;
	}
}
